package com.larpclient.update;

import com.google.gson.JsonObject;
import com.larp.updater.ArtifactVerifier;
import com.larp.updater.Host;
import com.larp.updater.Identity;
import com.larp.updater.UpdateManifest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public final class UpdateService implements AutoCloseable {
   private final Identity identity;
   private final Host host;
   private final byte[] trust;
   private final UpdateStore store;
   private final UpdateTransport transport;
   private final Executor callbacks;
   private final Clock clock;
   private final BiConsumer<String, Throwable> log;
   private final ExecutorService worker = Executors.newSingleThreadExecutor(threads("larp-update-worker"));
   private final AtomicLong generation = new AtomicLong();
   static final String QUEUED_MESSAGE = "Update ready. It installs when you close Minecraft, and the next launch uses it.";
   private volatile UpdateService.Snapshot snapshot = new UpdateService.Snapshot(
      UpdateService.State.STARTING, null, 0L, "Preparing updates", false, false, 0L, ""
   );
   private volatile UpdateTransport.Cancellation token = new UpdateTransport.Cancellation();
   private volatile boolean closed;
   private volatile boolean leaseReady;
   private byte[] offeredWire;
   private long lastCheckNanos;
   private int failures;
   private boolean initialized;

   UpdateService(Identity var1, Host var2, byte[] var3, UpdateStore var4, UpdateTransport var5, Executor var6, Clock var7, BiConsumer<String, Throwable> var8) {
      this.identity = var1;
      this.host = var2;
      this.trust = (byte[])var3.clone();
      this.store = var4;
      this.transport = var5;
      this.callbacks = var6;
      this.clock = var7;
      this.log = var8;
   }

   public UpdateService.Snapshot snapshot() {
      return this.snapshot;
   }

   public Identity identity() {
      return this.identity;
   }

   public synchronized void initialize(boolean var1) {
      if (!this.initialized && !this.closed) {
         this.initialized = true;
         long var2 = this.generation.incrementAndGet();
         this.worker
            .execute(
               () -> {
                  try {
                     this.store.runtimeLease();
                     this.leaseReady = true;
                     UpdateService.Snapshot var4 = this.store
                        .locked(
                           () -> {
                              this.store.floor();
                              if (this.store.read("trust-state.json") == null) {
                                 JsonObject var1xx = UpdateStore.object();
                                 var1xx.addProperty("authorityVersion", 0);
                                 var1xx.addProperty("sequence", 0);
                                 var1xx.addProperty("releaseId", "");
                                 var1xx.addProperty("sha256", "");
                                 this.store.write("trust-state.json", var1xx);
                              }

                              boolean var10 = this.installable();

                              UpdateStore.Receipt var2x;
                              try {
                                 var2x = this.store.receipt();
                              } catch (IOException var8) {
                                 this.log.accept("Installation receipt is invalid; suppressing success notice", var8);
                                 var2x = null;
                              }

                              String var3x = var2x == null ? "" : var2x.releaseId() + ":" + var2x.buildId();
                              byte[] var4x = this.store.read("manifest.json");
                              if (var4x != null) {
                                 UpdateManifest.Verified var5x;
                                 try {
                                    var5x = this.store.verify(var4x, this.trust, this.host, this.clock.instant());
                                 } catch (IOException var9) {
                                    if (var9.getMessage() != null && var9.getMessage().contains("validity period")) {
                                       this.store.cancelStaged();
                                       this.log
                                          .accept(
                                             "Staged metadata expired or is outside its validity period; discarded approval and retained rollback protection",
                                             var9
                                          );
                                       return new UpdateService.Snapshot(
                                          UpdateService.State.CURRENT,
                                          null,
                                          0L,
                                          "The staged update authorization expired. A fresh check and approval are required.",
                                          false,
                                          var10,
                                          0L,
                                          var3x
                                       );
                                    }

                                    throw var9;
                                 }

                                 UpdateManifest.Release var6 = var5x.release();
                                 if (!newer(this.identity, var6)) {
                                    this.store.cancelStaged();
                                    this.log.accept("Discarded staged release that is already installed or older than the loaded build", null);
                                    return new UpdateService.Snapshot(
                                       UpdateService.State.CURRENT,
                                       null,
                                       0L,
                                       "The staged release is already installed or older than this build.",
                                       false,
                                       var10,
                                       0L,
                                       var3x
                                    );
                                 }

                                 ArtifactVerifier.verify(this.store.file("artifact.bin"), var6, this.host);
                                 boolean var7 = this.store.pending(var6, this.clock.instant());
                                 if (!var7 && this.store.read("pending.json") != null) {
                                    this.store.delete("pending.json");
                                    this.log.accept("Queued update expired; fresh approval is required", null);
                                 }

                                 this.offeredWire = var4x;
                                 return new UpdateService.Snapshot(
                                    var7 ? UpdateService.State.QUEUED : UpdateService.State.READY,
                                    var6,
                                    var6.size(),
                                    var7
                                       ? "Update ready. It installs when you close Minecraft, and the next launch uses it."
                                       : "The downloaded update is verified. Press Update to install it when you close Minecraft.",
                                    true,
                                    var10,
                                    0L,
                                    var3x
                                 );
                              } else if (this.store.read("pending.json") != null) {
                                 throw new IOException("Queued approval is missing its staged manifest");
                              } else {
                                 return new UpdateService.Snapshot(UpdateService.State.CURRENT, null, 0L, "Check for a newer version", false, var10, 0L, var3x);
                              }
                           }
                        );
                     this.deliver(var2, () -> {
                        this.snapshot = var4;
                        if (var1 && var4.state() == UpdateService.State.CURRENT) {
                           this.check();
                        }
                     });
                  } catch (Exception var5) {
                     this.log.accept("Update initialization failed; installation disabled", var5);
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           UpdateService.State.ERROR,
                           null,
                           0L,
                           "Update storage or staged verification failed. Clear the staged update to recover, or use the official downloads page. The client log identifies the failing stage.",
                           false,
                           false,
                           0L,
                           ""
                        )
                     );
                  }
               }
            );
      }
   }

   public synchronized void check() {
      if (!this.closed && !this.snapshot.busy() && this.snapshot.state() != UpdateService.State.READY && this.snapshot.state() != UpdateService.State.QUEUED) {
         long var1 = System.nanoTime();
         long var3 = TimeUnit.SECONDS.toNanos(Math.min(300L, 15L << Math.min(this.failures, 4)));
         if (this.lastCheckNanos == 0L || var1 - this.lastCheckNanos >= var3) {
            this.lastCheckNanos = var1;
            long var5 = this.generation.incrementAndGet();
            UpdateTransport.Cancellation var7 = new UpdateTransport.Cancellation();
            this.token = var7;
            UpdateService.Snapshot var8 = this.snapshot;
            this.snapshot = new UpdateService.Snapshot(
               UpdateService.State.CHECKING,
               var8.release(),
               0L,
               "Checking for updates",
               var8.cached(),
               var8.installable(),
               var8.checkedAt(),
               var8.installedNotice()
            );
            this.worker
               .execute(
                  () -> {
                     try {
                        byte[] var5x = this.transport.metadata(this.identity, var7);
                        var7.check();
                        UpdateService.Snapshot var10 = this.store.locked(() -> {
                           var7.check();
                           UpdateManifest.Verified var4 = this.store.verify(var5x, this.trust, this.host, this.clock.instant());
                           this.store.retain(var4);
                           this.store.write("cached-manifest.json", var5x);
                           this.store.retainCheckedAt(var4.release(), this.clock.instant());
                           this.offeredWire = var5x;
                           UpdateManifest.Release var5xx = var4.release();
                           return this.result(var5xx, false, this.installable(), var8.installedNotice(), this.clock.instant().getEpochSecond());
                        });
                        this.deliver(var5, () -> {
                           this.failures = 0;
                           this.snapshot = var10;
                        });
                        this.log.accept("Signed public update check completed", null);
                     } catch (Exception var9) {
                        this.log.accept("Update check failed; attempting verified offline cache", var9);

                        UpdateService.Snapshot var6;
                        try {
                           var6 = this.store
                              .locked(
                                 () -> {
                                    byte[] var2x = this.store.read("cached-manifest.json");
                                    if (var2x == null) {
                                       throw new IOException("No verified cache");
                                    }

                                    UpdateManifest.Verified var3xx = this.store.verify(var2x, this.trust, this.host, this.clock.instant());
                                    this.offeredWire = var2x;
                                    long var4 = this.store.cacheCheckedAt(var3xx.release(), this.clock.instant());
                                    UpdateService.Snapshot var6x = this.result(var3xx.release(), true, this.installable(), var8.installedNotice(), var4);
                                    return new UpdateService.Snapshot(
                                       var6x.state(),
                                       var6x.release(),
                                       0L,
                                       "Offline. Showing a previously verified release; installation still revalidates expiry.",
                                       true,
                                       var6x.installable(),
                                       var6x.checkedAt(),
                                       var6x.installedNotice()
                                    );
                                 }
                              );
                        } catch (Exception var8x) {
                           var6 = new UpdateService.Snapshot(
                              UpdateService.State.ERROR,
                              null,
                              0L,
                              "Updates could not be checked. Try again later or use the official downloads page.",
                              false,
                              var8.installable(),
                              0L,
                              var8.installedNotice()
                           );
                        }

                        UpdateService.Snapshot var7x = var6;
                        this.deliver(var5, () -> {
                           this.failures++;
                           this.snapshot = var7x;
                        });
                     }
                  }
               );
         }
      }
   }

   private UpdateService.Snapshot result(UpdateManifest.Release var1, boolean var2, boolean var3, String var4, long var5) throws IOException {
      boolean var7 = newer(this.identity, var1);
      return new UpdateService.Snapshot(
         var7 ? UpdateService.State.AVAILABLE : UpdateService.State.CURRENT,
         var1,
         0L,
         var7 ? "A signed update is available" : "You are using the current version",
         var2,
         var3,
         var5,
         var4
      );
   }

   static boolean newer(Identity var0, UpdateManifest.Release var1) throws IOException {
      if (var0.buildId().equals(var1.buildId())) {
         return false;
      }

      try {
         return Version.parse(var1.version()).compareTo(Version.parse(var0.version())) > 0;
      } catch (VersionParsingException var3) {
         throw new IOException("Cannot compare installed and proposed version", var3);
      }
   }

   public synchronized void download() {
      UpdateService.Snapshot var1 = this.snapshot;
      if (!this.closed && this.leaseReady && var1.state() == UpdateService.State.AVAILABLE && var1.installable() && var1.release() != null) {
         long var2 = this.generation.incrementAndGet();
         UpdateTransport.Cancellation var4 = new UpdateTransport.Cancellation();
         this.token = var4;
         byte[] var5 = (byte[])this.offeredWire.clone();
         UpdateManifest.Release var6 = var1.release();
         this.snapshot = new UpdateService.Snapshot(
            UpdateService.State.DOWNLOADING, var6, 0L, "Downloading approved release", var1.cached(), true, var1.checkedAt(), var1.installedNotice()
         );
         this.worker
            .execute(
               () -> {
                  try {
                     this.store
                        .locked(
                           () -> {
                              var4.check();
                              UpdateManifest.Verified var6xx = this.store.verify(var5, this.trust, this.host, this.clock.instant());
                              if (var6xx.release().equals(var6) && this.installable()) {
                                 this.store.delete("download.part");
                                 Path var7 = this.store.file("download.part");
                                 this.transport
                                    .download(
                                       var6,
                                       var7,
                                       var4,
                                       var4xxx -> this.deliver(
                                          var2,
                                          () -> {
                                             UpdateService.Snapshot var4xxxx = this.snapshot;
                                             this.snapshot = new UpdateService.Snapshot(
                                                UpdateService.State.DOWNLOADING,
                                                var6,
                                                var4xxx,
                                                var4xxxx.message(),
                                                var4xxxx.cached(),
                                                var4xxxx.installable(),
                                                var4xxxx.checkedAt(),
                                                var4xxxx.installedNotice()
                                             );
                                          }
                                       )
                                    );
                                 var4.check();
                                 ArtifactVerifier.verify(var7, var6, this.host);
                                 var4.check();
                                 this.store.verify(var5, this.trust, this.host, this.clock.instant());
                                 if (!this.installable()) {
                                    throw new IOException("This client can no longer install updates in place");
                                 }

                                 this.store.delete("pending.json");
                                 Files.move(var7, this.store.file("artifact.bin"), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
                                 this.store.write("manifest.json", var5);
                                 this.store.syncDirectory();
                                 this.store.queue(var6, this.clock.instant());
                                 return null;
                              } else {
                                 throw new IOException("Update approval is no longer valid");
                              }
                           }
                        );
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           UpdateService.State.QUEUED,
                           var6,
                           var6.size(),
                           "Update ready. It installs when you close Minecraft, and the next launch uses it.",
                           var1.cached(),
                           true,
                           var1.checkedAt(),
                           var1.installedNotice()
                        )
                     );
                     this.log
                        .accept("Update download passed signature, digest, archive and compatibility verification; it installs after Minecraft closes", null);
                  } catch (Exception var10) {
                     try {
                        this.store.locked(() -> {
                           this.store.delete("download.part");
                           return null;
                        });
                     } catch (IOException var9) {
                        var10.addSuppressed(var9);
                     }

                     this.log.accept("Update download failed; the installed mod is unchanged", var10);
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           UpdateService.State.AVAILABLE,
                           var6,
                           0L,
                           "Download failed. Try again, or use the official download page.",
                           var1.cached(),
                           var1.installable(),
                           var1.checkedAt(),
                           var1.installedNotice()
                        )
                     );
                  }
               }
            );
      }
   }

   public synchronized void queue() {
      UpdateService.Snapshot var1 = this.snapshot;
      if (!this.closed && this.leaseReady && var1.state() == UpdateService.State.READY && var1.installable()) {
         long var2 = this.generation.incrementAndGet();
         byte[] var4 = (byte[])this.offeredWire.clone();
         UpdateManifest.Release var5 = var1.release();
         this.snapshot = new UpdateService.Snapshot(
            UpdateService.State.CHECKING,
            var5,
            var5.size(),
            "Revalidating installation approval",
            var1.cached(),
            true,
            var1.checkedAt(),
            var1.installedNotice()
         );
         this.worker
            .execute(
               () -> {
                  try {
                     this.store.locked(() -> {
                        UpdateManifest.Verified var3x = this.store.verify(var4, this.trust, this.host, this.clock.instant());
                        if (var3x.release().equals(var5) && newer(this.identity, var5)) {
                           ArtifactVerifier.verify(this.store.file("artifact.bin"), var5, this.host);
                           this.store.queue(var5, this.clock.instant());
                           return null;
                        } else {
                           throw new IOException("Release approval mismatch or version downgrade");
                        }
                     });
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           UpdateService.State.QUEUED,
                           var5,
                           var5.size(),
                           "Update ready. It installs when you close Minecraft, and the next launch uses it.",
                           var1.cached(),
                           true,
                           var1.checkedAt(),
                           var1.installedNotice()
                        )
                     );
                     this.log.accept("Exact verified release approved; it installs after Minecraft closes", null);
                  } catch (Exception var7) {
                     this.log.accept("Installation approval failed", var7);
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           UpdateService.State.READY,
                           var5,
                           var5.size(),
                           "Approval failed. Cancel this download and check again.",
                           var1.cached(),
                           true,
                           var1.checkedAt(),
                           var1.installedNotice()
                        )
                     );
                  }
               }
            );
      }
   }

   public synchronized void cancel() {
      if (!this.closed && this.snapshot.state() != UpdateService.State.STARTING && this.snapshot.state() != UpdateService.State.CANCELLING) {
         UpdateService.Snapshot var1 = this.snapshot;
         long var2 = this.generation.incrementAndGet();
         this.token.cancel();
         this.snapshot = new UpdateService.Snapshot(
            UpdateService.State.CANCELLING,
            var1.release(),
            var1.downloaded(),
            "Cancelling update",
            var1.cached(),
            var1.installable(),
            var1.checkedAt(),
            var1.installedNotice()
         );
         this.worker
            .execute(
               () -> {
                  try {
                     this.store.locked(() -> {
                        this.store.cancelStaged();
                        return null;
                     });
                     boolean var4 = this.installable();
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           var1.release() != null && this.safeNewer(var1.release()) ? UpdateService.State.AVAILABLE : UpdateService.State.CURRENT,
                           var1.release(),
                           0L,
                           "Update cancelled. The installed mod is unchanged.",
                           var1.cached(),
                           var4,
                           var1.checkedAt(),
                           var1.installedNotice()
                        )
                     );
                  } catch (Exception var5) {
                     this.log.accept("Update cancellation failed; queued approval could not be removed", var5);
                     this.deliver(
                        var2,
                        () -> this.snapshot = new UpdateService.Snapshot(
                           UpdateService.State.ERROR,
                           var1.release(),
                           0L,
                           "Could not remove queued approval. Inspect the update folder before the next configured launch.",
                           var1.cached(),
                           false,
                           var1.checkedAt(),
                           var1.installedNotice()
                        )
                     );
                  }
               }
            );
      }
   }

   private boolean installable() {
      try {
         return this.leaseReady && !this.host.platform().equals("other") && this.store.installable();
      } catch (IOException var2) {
         return false;
      }
   }

   public void launchInstaller(byte[] var1, Path var2) throws IOException {
      this.store.launchInstaller(var1, var2, this.host.loaderVersion());
   }

   private boolean safeNewer(UpdateManifest.Release var1) {
      try {
         return newer(this.identity, var1);
      } catch (IOException var3) {
         return false;
      }
   }

   private void deliver(long var1, Runnable var3) {
      this.callbacks.execute(() -> {
         if (!this.closed && this.generation.get() == var1) {
            var3.run();
         }
      });
   }

   static ThreadFactory threads(String var0) {
      AtomicLong var1 = new AtomicLong();
      return var2 -> {
         Thread var3 = new Thread(var2, var0 + "-" + var1.incrementAndGet());
         var3.setDaemon(true);
         return var3;
      };
   }

   @Override
   public synchronized void close() {
      this.closed = true;
      this.generation.incrementAndGet();
      this.token.cancel();
      this.worker.shutdownNow();
      this.transport.close();
   }

   public record Snapshot(
      UpdateService.State state,
      UpdateManifest.Release release,
      long downloaded,
      String message,
      boolean cached,
      boolean installable,
      long checkedAt,
      String installedNotice
   ) {
      public boolean busy() {
         return this.state == UpdateService.State.STARTING
            || this.state == UpdateService.State.CHECKING
            || this.state == UpdateService.State.DOWNLOADING
            || this.state == UpdateService.State.CANCELLING;
      }
   }

   public enum State {
      STARTING,
      CHECKING,
      CURRENT,
      AVAILABLE,
      DOWNLOADING,
      READY,
      QUEUED,
      CANCELLING,
      ERROR;
   }
}

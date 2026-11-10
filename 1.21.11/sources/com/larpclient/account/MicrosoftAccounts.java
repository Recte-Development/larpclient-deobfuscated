package com.larpclient.account;

import com.larpclient.LarpClient;
import com.larpclient.gf;
import com.larpclient.id;
import com.larpclient.q8;
import com.larpclient.w9;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import net.fabricmc.loader.api.FabricLoader;
import net.lenni0451.commons.httpclient.HttpClient;
import net.minecraft.class_156;
import net.minecraft.class_310;
import net.minecraft.class_320;
import net.minecraft.class_437;
import net.raphimc.minecraftauth.MinecraftAuth;
import net.raphimc.minecraftauth.java.JavaAuthManager;
import net.raphimc.minecraftauth.java.model.MinecraftProfile;
import net.raphimc.minecraftauth.java.model.MinecraftToken;
import net.raphimc.minecraftauth.msa.model.MsaDeviceCode;
import net.raphimc.minecraftauth.msa.service.impl.DeviceCodeMsaAuthService;

public final class MicrosoftAccounts {
   public static final MicrosoftAccounts INSTANCE = new MicrosoftAccounts();
   private AccountVault vault;
   private final ThreadPoolExecutor worker = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4), var0 -> {
      Thread var1 = new Thread(var0, "Larp Microsoft accounts");
      var1.setDaemon(true);
      return var1;
   });
   private final AtomicLong generation = new AtomicLong();
   private class_320 launcherUser;
   private Path proxyMarker;
   private Future<?> pending;
   private class_437 owner;
   private volatile boolean stopped;
   private boolean busy;
   private boolean error;
   private boolean ready;
   private boolean legacy;
   private boolean stored;
   private List<AccountVault.Entry> snapshot = List.of();
   private List<id> proxySnapshot = List.of();
   private UUID activeProxyId;
   private String status = "";
   private String code = "";
   private URI verification;
   private int revision;

   private MicrosoftAccounts() {
   }

   public void start(class_310 var1) {
      if (this.launcherUser == null) {
         this.launcherUser = var1.method_1548();
      }

      if (this.vault == null) {
         Path var2 = FabricLoader.getInstance().getConfigDir().resolve("larp-client/accounts");
         this.vault = new AccountVault(var2);
         this.proxyMarker = var2.resolve("proxy-enabled");
         if (Files.exists(this.proxyMarker, LinkOption.NOFOLLOW_LINKS)) {
            gf.W("The saved proxy is still loading. Try again in a moment.");

            try {
               this.worker.execute(this::restoreProxy);
            } catch (RejectedExecutionException var4) {
               gf.W("The saved proxy could not be loaded. Open the account list to retry or turn the proxy off.");
            }
         }
      }
   }

   public boolean unlocked() {
      return this.ready;
   }

   public boolean exists() {
      return this.stored;
   }

   public boolean busy() {
      return this.busy;
   }

   public boolean error() {
      return this.error;
   }

   public String status() {
      return this.status;
   }

   public String code() {
      return this.code;
   }

   public int revision() {
      return this.revision;
   }

   public List<AccountVault.Entry> entries() {
      return this.snapshot;
   }

   public List<id> proxies() {
      return this.proxySnapshot;
   }

   public UUID activeProxyId() {
      return this.activeProxyId;
   }

   public boolean canReturn(class_310 var1) {
      return this.launcherUser != null && var1.method_1548() != this.launcherUser && !this.launcherUser.method_1674().equals("0");
   }

   public class_320 launcher() {
      return this.launcherUser;
   }

   public boolean legacy() {
      return this.legacy;
   }

   public void open(class_437 var1) {
      this.start(class_310.method_1551());
      this.run(var1, "Loading saved accounts...", () -> {
         this.vault.open();
         this.syncProxy();
         return this.vault.legacyExists();
      }, var1x -> {
         this.legacy = var1x;
         this.message(var1x && this.snapshot.isEmpty() ? "Previous list kept. Add your accounts again." : "", false);
      });
   }

   public void add(class_437 var1) {
      if (this.unlocked()) {
         long var2 = this.generation.get() + 1L;
         this.run(var1, "Requesting Microsoft sign-in...", () -> {
            if (this.generation.get() != var2) {
               throw new InterruptedException();
            }

            HttpClient var4 = MinecraftAuth.createHttpClient("LarpClient/" + LarpClient.z + " MinecraftAuth/5.0.2");
            JavaAuthManager var5 = JavaAuthManager.create(var4).login(DeviceCodeMsaAuthService::new, (Consumer<MsaDeviceCode>)var4x -> {
               URI var5x = verificationLink(var4x.getDirectVerificationUri());
               if (!var4x.getUserCode().matches("[A-Za-z0-9-]{4,16}")) {
                  throw new IllegalArgumentException("Unexpected Microsoft verification code");
               }

               class_310.method_1551().execute(() -> {
                  if (this.current(var2, var1)) {
                     this.code = var4x.getUserCode();
                     this.verification = var5x;
                     this.message("Approve this code on Microsoft's website.", false);
                     this.openBrowser();
                  }
               });
            });
            interrupted();
            MinecraftProfile var6 = (MinecraftProfile)var5.getMinecraftProfile().getUpToDate();
            var5.getMinecraftToken().getUpToDate();
            interrupted();
            if (this.generation.get() != var2) {
               throw new InterruptedException();
            }

            this.vault.put(new AccountVault.Entry(var6.getId(), var6.getName(), JavaAuthManager.toJson(var5)));
            return var6.getId();
         }, var1x -> this.message("Microsoft account saved. Select it and choose Switch.", false));
      }
   }

   public void addToken(class_437 var1, String var2, Runnable var3) {
      if (this.unlocked() && !this.busy) {
         String var4;
         try {
            var4 = TokenLogin.normalize(var2);
         } catch (IllegalArgumentException var7) {
            this.message(var7.getMessage(), true);
            return;
         }

         class_310 var5 = class_310.method_1551();
         class_320 var6 = var5.method_1548();
         this.run(var1, "Checking the access token...", () -> {
            TokenLogin.Profile var3x = TokenLogin.profile(var4, var5.method_1487());
            interrupted();
            AccountVault.Entry var4x = this.vault.entry(var3x.id());
            if (var4x != null && !TokenLogin.saved(var4x.authentication())) {
               throw new AccountVault.Failure(var3x.name() + " is already saved with Microsoft sign-in. Choose Switch on that row.");
            }

            this.vault.put(new AccountVault.Entry(var3x.id(), var3x.name(), TokenLogin.state(var4, TokenLogin.expiry(var4))));
            interrupted();
            return ClientAccountState.prepare(var5, new class_320(var3x.name(), var3x.id(), var4, Optional.empty(), Optional.empty()));
         }, var4x -> {
            var3.run();
            this.apply(var1, var6, var4x);
         });
      }
   }

   public void switchTo(class_437 var1, UUID var2) {
      if (this.ready && !this.busy) {
         AccountVault.Entry var3 = this.snapshot.stream().filter(var1x -> var1x.id().equals(var2)).findFirst().orElse(null);
         if (var3 != null) {
            class_320 var4 = class_310.method_1551().method_1548();
            if (TokenLogin.saved(var3.authentication())) {
               this.run(var1, "Checking the saved token...", () -> {
                  class_310 var3x = class_310.method_1551();
                  String var4x = TokenLogin.token(var3.authentication());
                  TokenLogin.Profile var5 = TokenLogin.profile(var4x, var3x.method_1487());
                  if (!var2.equals(var5.id())) {
                     throw new AccountVault.Failure("This token now belongs to another account. Remove it and add it again.");
                  }

                  if (!var5.name().equals(var3.name())) {
                     this.vault.put(new AccountVault.Entry(var2, var5.name(), var3.authentication()));
                  }

                  interrupted();
                  return ClientAccountState.prepare(var3x, new class_320(var5.name(), var2, var4x, Optional.empty(), Optional.empty()));
               }, var3x -> this.apply(var1, var4, var3x));
            } else {
               this.run(
                  var1,
                  "Refreshing Microsoft account...",
                  () -> {
                     HttpClient var3x = MinecraftAuth.createHttpClient("LarpClient/" + LarpClient.z + " MinecraftAuth/5.0.2");
                     JavaAuthManager var4x = JavaAuthManager.fromJson(var3x, var3.authentication());

                     try {
                        var4x.getMinecraftToken().refresh();
                        MinecraftProfile var5 = (MinecraftProfile)var4x.getMinecraftProfile().refresh();
                        if (!var2.equals(var5.getId())) {
                           throw new IOException("The saved account identity changed. Add it again.");
                        }

                        this.vault.put(new AccountVault.Entry(var2, var5.getName(), JavaAuthManager.toJson(var4x)));
                        interrupted();
                        return ClientAccountState.prepare(
                           class_310.method_1551(),
                           new class_320(
                              var5.getName(),
                              var5.getId(),
                              ((MinecraftToken)var4x.getMinecraftToken().getUpToDate()).getToken(),
                              Optional.empty(),
                              Optional.empty()
                           )
                        );
                     } catch (Exception var8) {
                        try {
                           MinecraftProfile var6 = (MinecraftProfile)var4x.getMinecraftProfile().getCached();
                           if (var6 != null && var2.equals(var6.getId())) {
                              this.vault.put(new AccountVault.Entry(var2, var6.getName(), JavaAuthManager.toJson(var4x)));
                           }
                        } catch (Exception var7) {
                           q8.L("Accounts", "Could not retain refreshed account state; sign in again if refresh fails");
                        }

                        throw var8;
                     }
                  },
                  var3x -> this.apply(var1, var4, var3x)
               );
            }
         }
      }
   }

   public void returnToLauncher(class_437 var1) {
      class_310 var2 = class_310.method_1551();
      if (this.canReturn(var2)) {
         class_320 var3 = var2.method_1548();
         this.run(var1, "Preparing the launcher account...", () -> ClientAccountState.prepare(var2, this.launcherUser), var3x -> this.apply(var1, var3, var3x));
      }
   }

   private void apply(class_437 var1, class_320 var2, ClientAccountState var3) {
      class_310 var4 = class_310.method_1551();

      try (var3) {
         if (var4.field_1755 == var1 && var4.method_1548() == var2 && ClientAccountState.outsideWorld(var4)) {
            var3.install(var4);
            this.message("Account switched.", false);
            return;
         }

         this.message("Minecraft changed while signing in. Your current account was retained.", true);
      }
   }

   public void remove(class_437 var1, UUID var2) {
      this.run(var1, "Removing saved account...", () -> {
         this.vault.remove(var2);
         return null;
      }, var1x -> this.message("Saved account removed. Its current Minecraft login, if active, stays signed in.", false));
   }

   public void archive(class_437 var1) {
      boolean var2 = gf.Q() != null || gf.D() != null;
      this.run(var1, "Starting a new saved list...", () -> {
         this.vault.archive();
         this.vault.open();
         this.syncProxy();
         return null;
      }, var2x -> this.message("Previous list kept for recovery. Add your accounts again." + (var2 ? " The proxy is off." : ""), false));
   }

   public void saveProxy(class_437 var1, id var2, Runnable var3) {
      if (this.unlocked() && !this.busy) {
         this.run(var1, "Testing the proxy...", () -> {
            MicrosoftAccounts.Tested var2x = this.test(var2);
            this.vault.putProxy(var2, true);
            this.marker(true);
            return var2x;
         }, var2x -> {
            var3.run();
            this.activate(var2x);
         });
      }
   }

   public void useProxy(class_437 var1, UUID var2) {
      if (this.unlocked() && !this.busy) {
         id var3 = this.proxySnapshot.stream().filter(var1x -> var1x.id().equals(var2)).findFirst().orElse(null);
         if (var3 != null) {
            this.run(var1, "Testing the proxy...", () -> {
               MicrosoftAccounts.Tested var3x = this.test(var3);
               this.vault.selectProxy(var2);
               this.marker(true);
               return var3x;
            }, this::activate);
         }
      }
   }

   public void proxyOff(class_437 var1) {
      if (!this.busy) {
         if (!this.ready) {
            gf.A();

            try {
               this.marker(false);
            } catch (IOException var3) {
               q8.L("Proxy", "Could not clear the proxy marker ({})", var3.getClass().getSimpleName());
            }

            this.message("Proxy off for this session. The saved choice returns once the account list opens.", false);
         } else {
            this.run(var1, "Turning the proxy off...", () -> {
               this.vault.selectProxy(null);
               this.marker(false);
               return null;
            }, var1x -> {
               gf.A();
               this.message("Proxy off. Server traffic connects directly.", false);
            });
         }
      }
   }

   public void removeProxy(class_437 var1, UUID var2) {
      if (this.unlocked() && !this.busy) {
         boolean var3 = var2.equals(this.activeProxyId);
         this.run(var1, "Removing the saved proxy...", () -> {
            this.vault.removeProxy(var2);
            if (var3) {
               this.marker(false);
            }

            return null;
         }, var2x -> {
            if (var3) {
               gf.A();
            }

            this.message(var3 ? "Proxy removed and turned off. Server traffic connects directly." : "Saved proxy removed.", false);
         });
      }
   }

   private MicrosoftAccounts.Tested test(id var1) throws Exception {
      id.u var2;
      try {
         var2 = var1.resolve();
      } catch (UnknownHostException var5) {
         throw new w9.h("Proxy " + var1.label() + ": the proxy host could not be found");
      }

      long var3 = gf.N(var2);
      interrupted();
      return new MicrosoftAccounts.Tested(var2, var3);
   }

   private void activate(MicrosoftAccounts.Tested var1) {
      gf.W(var1.proxy());
      this.message("Proxy on: " + var1.proxy().settings().label() + " (" + var1.millis() + " ms). Server traffic now uses it.", false);
   }

   private void syncProxy() throws IOException {
      id var1 = this.vault.activeProxy();
      if (var1 == null) {
         gf.A();
         this.marker(false);
      } else {
         id.u var2 = gf.Q();
         if (var2 != null && var2.settings().equals(var1)) {
            this.marker(true);
         } else {
            try {
               gf.W(var1.resolve());
               this.marker(true);
            } catch (UnknownHostException var4) {
               gf.W("Proxy " + var1.label() + " could not be found. Choose another proxy or turn it off in the account list.");
            }
         }
      }
   }

   private void restoreProxy() {
      try {
         this.vault.open();
         this.syncProxy();
      } catch (Exception var2) {
         q8.L("Proxy", "Saved proxy could not be loaded ({}); server connections stay paused", var2.getClass().getSimpleName());
         gf.W("The saved proxy could not be loaded. Open the account list to retry or turn the proxy off.");
      }
   }

   private void marker(boolean var1) throws IOException {
      if (this.proxyMarker != null) {
         if (!var1) {
            Files.deleteIfExists(this.proxyMarker);
         } else {
            if (Files.isSymbolicLink(this.proxyMarker)) {
               throw new AccountVault.Failure("Unsafe proxy marker file.");
            }

            if (!Files.exists(this.proxyMarker, LinkOption.NOFOLLOW_LINKS)) {
               Files.createFile(this.proxyMarker);
            }
         }
      }
   }

   public void openBrowser() {
      if (this.verification != null && this.busy) {
         try {
            class_156.method_668().method_673(this.verification);
         } catch (RuntimeException var2) {
            this.message("Open microsoft.com/link in your browser and enter the code shown here.", true);
         }
      }
   }

   public void copyCode() {
      if (this.busy && !this.code.isBlank()) {
         class_310.method_1551().field_1774.method_1455(this.code);
      }
   }

   public void clearMessage() {
      if (!this.busy && !this.status.isEmpty()) {
         this.message("", false);
      }
   }

   static URI verificationLink(String var0) {
      URI var1 = URI.create(var0);
      String var2 = var1.getHost();
      String var3 = var1.getPath();
      boolean var4 = ("microsoft.com".equals(var2) || "www.microsoft.com".equals(var2)) && "/link".equals(var3);
      boolean var5 = "login.live.com".equals(var2) && "/oauth20_remoteconnect.srf".equals(var3);
      if ("https".equals(var1.getScheme()) && var1.getUserInfo() == null && var1.getPort() == -1 && var1.getFragment() == null && (var4 || var5)) {
         return var1;
      } else {
         throw new IllegalArgumentException("Unexpected Microsoft verification address");
      }
   }

   private boolean current(long var1, class_437 var3) {
      return validRequest(!this.stopped, this.generation.get(), var1, this.owner, var3, class_310.method_1551().field_1755);
   }

   static boolean validRequest(boolean var0, long var1, long var3, Object var5, Object var6, Object var7) {
      return var0 && var1 == var3 && var5 == var6 && var7 == var6;
   }

   private static void interrupted() throws InterruptedException {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException();
      }
   }

   private <T> void run(class_437 var1, String var2, Callable<T> var3, Consumer<T> var4) {
      class_310 var5 = class_310.method_1551();
      if (!this.stopped && !this.busy && var5.field_1755 == var1 && ClientAccountState.outsideWorld(var5)) {
         long var6 = this.generation.incrementAndGet();
         this.owner = var1;
         this.busy = true;
         this.code = "";
         this.verification = null;
         this.message(var2, false);
         q8.G("Accounts", "Starting {}; explicit user request, local encrypted storage", var2);

         try {
            this.pending = this.worker.submit(() -> {
               Object var8 = null;

               try {
                  var8 = var3.call();
                  if (this.stopped || this.generation.get() != var6) {
                     discard(var8);
                     return;
                  }

                  Object var9x = var8;
                  var5.execute(() -> {
                     if (!this.current(var6, var1)) {
                        discard(var9x);
                     } else {
                        this.busy = false;
                        this.code = "";
                        this.verification = null;
                        this.refreshSnapshot();

                        try {
                           var4.accept(var9x);
                        } catch (RuntimeException var8x) {
                           discard(var9x);
                           this.failed(var2, var8x);
                        }

                        this.revision++;
                     }
                  });
               } catch (Exception var10) {
                  discard(var8);
                  var5.execute(() -> {
                     if (this.current(var6, var1)) {
                        this.busy = false;
                        this.code = "";
                        this.verification = null;
                        this.failed(var2, var10);
                     }
                  });
               }
            });
         } catch (RejectedExecutionException var9) {
            this.busy = false;
            this.message("A cancelled request is finishing. Retry in a moment.", true);
         }
      }
   }

   private void failed(String var1, Exception var2) {
      this.refreshSnapshot();
      q8.L("Accounts", "stage={} failed ({}); current Minecraft account retained", var1, var2.getClass().getSimpleName());
      String var3 = var2 instanceof TimeoutException
         ? "Microsoft sign-in expired. Choose Add to try again."
         : (
            var2 instanceof FileAlreadyExistsException
               ? "Another instance created the saved list. Choose Retry."
               : (
                  !(var2 instanceof AccountVault.Failure) && !(var2 instanceof w9.h)
                     ? (
                        var1.contains("token")
                           ? "Token sign-in could not finish. Check your connection and that the token still works, then retry."
                           : (
                              var1.contains("proxy")
                                 ? "The proxy could not be used. Check its address and login, then retry."
                                 : "Microsoft sign-in could not finish. Check your connection, Xbox account and Minecraft ownership, then retry."
                           )
                     )
                     : var2.getMessage()
               )
         );
      this.message(var3 == null ? "Account operation failed. Retry or sign in again." : var3, true);
   }

   private static void discard(Object var0) {
      if (var0 instanceof ClientAccountState var1) {
         var1.close();
      }
   }

   private void message(String var1, boolean var2) {
      this.status = var1;
      this.error = var2;
      this.revision++;
   }

   private void refreshSnapshot() {
      this.ready = this.vault != null && this.vault.unlocked();
      this.stored = this.vault != null && this.vault.exists();
      this.snapshot = this.ready ? this.vault.entries() : List.of();
      this.proxySnapshot = this.ready ? this.vault.proxies() : List.of();
      id var1 = this.ready ? this.vault.activeProxy() : null;
      this.activeProxyId = var1 == null ? null : var1.id();
   }

   public void cancel(class_437 var1) {
      if (this.owner == var1 && this.busy) {
         this.generation.incrementAndGet();
         if (this.pending != null) {
            this.pending.cancel(true);
         }

         this.worker.purge();
         this.busy = false;
         this.code = "";
         this.verification = null;
         this.message("Cancelled. Your current Minecraft account was retained.", false);
         q8.G("Accounts", "Pending account operation cancelled; late results will be discarded");
      }
   }

   public void stop() {
      this.stopped = true;
      this.generation.incrementAndGet();
      if (this.pending != null) {
         this.pending.cancel(true);
      }

      this.worker.shutdownNow();
      Thread var1 = new Thread(() -> {
         try {
            this.worker.awaitTermination(35L, TimeUnit.SECONDS);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         }

         if (this.vault != null) {
            this.vault.close();
         }
      }, "Larp account cleanup");
      var1.setDaemon(true);
      var1.start();
   }

   private record Tested(id.u proxy, long millis) {
   }
}

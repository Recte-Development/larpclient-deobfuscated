package com.larpclient.update;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class UpdateStore {
   static final String TARGET = "mods/LarpClient.jar";
   private final Path game;
   private final Path directory;
   private final Identity installed;
   private final Path origin;
   static final List<UpdateStore.RuntimeLease> LEASES = new ArrayList<>();

   UpdateStore(Path var1, Path var2, Identity var3) throws IOException {
      this.game = var1.toAbsolutePath().normalize();
      safeAncestors(this.game);
      if (!Files.isDirectory(this.game, LinkOption.NOFOLLOW_LINKS)) {
         throw new IOException("Game directory is unavailable");
      }

      this.directory = this.game.resolve("config/larp-client/updates");
      this.origin = var2 == null ? null : var2.toAbsolutePath().normalize();
      this.installed = var3;
      this.ensureDirectory();
   }

   Path file(String var1) throws IOException {
      if (!var1.matches("[A-Za-z0-9._-]+")) {
         throw new IOException("Unsafe update filename");
      } else {
         safeAncestors(this.directory);
         Path var2 = this.directory.resolve(var1);
         if (Files.exists(var2, LinkOption.NOFOLLOW_LINKS) && !attributes(var2).isRegularFile()) {
            throw new IOException("Update state is not a regular file");
         } else {
            return var2;
         }
      }
   }

   static void safeAncestors(Path var0) throws IOException {
      Path var1 = var0.toAbsolutePath().normalize();
      Path var2 = var1.getRoot();

      for (Path var4 : var1) {
         var2 = var2.resolve(var4);
         if (Files.isSymbolicLink(var2)) {
            throw new IOException("Symbolic links are not permitted in update paths");
         }

         if (Files.exists(var2, LinkOption.NOFOLLOW_LINKS) && !var2.equals(var1) && !Files.isDirectory(var2, LinkOption.NOFOLLOW_LINKS)) {
            throw new IOException("Invalid update directory ancestor");
         }
      }
   }

   private void ensureDirectory() throws IOException {
      safeAncestors(this.directory);
      Files.createDirectories(this.directory);
      safeAncestors(this.directory);
   }

   private static BasicFileAttributes attributes(Path var0) throws IOException {
      BasicFileAttributes var1 = Files.readAttributes(var0, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
      if (var1.isSymbolicLink()) {
         throw new IOException("Symbolic update state");
      } else {
         return var1;
      }
   }

   <T> T locked(UpdateStore.Work<T> var1) throws IOException {
      Path var2 = this.file("update.lock");

      try (FileChannel var3 = FileChannel.open(var2, StandardOpenOption.CREATE, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS)) {
         FileLock var4;
         try {
            var4 = var3.tryLock();
         } catch (OverlappingFileLockException var10) {
            throw new IOException("Another update operation is active");
         }

         if (var4 == null) {
            throw new IOException("Another update operation is active");
         }

         try (var4) {
            safeAncestors(this.directory);
            return (T)var1.run();
         }
      }
   }

   byte[] read(String var1) throws IOException {
      Path var2 = this.file(var1);
      if (!Files.exists(var2, LinkOption.NOFOLLOW_LINKS)) {
         return null;
      }

      BasicFileAttributes var3 = attributes(var2);
      if (var3.isRegularFile() && var3.size() <= 131072L) {
         try (
            FileChannel var4 = FileChannel.open(var2, StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS);
            InputStream var5 = Channels.newInputStream(var4);
         ) {
            byte[] var6 = var5.readNBytes(131073);
            if (var6.length > 131072 || var6.length != var3.size()) {
               throw new IOException("Update state changed while reading");
            } else {
               return var6;
            }
         }
      } else {
         throw new IOException("Update state size limit");
      }
   }

   JsonObject json(String var1) throws IOException {
      byte[] var2 = this.read(var1);
      return var2 == null ? null : StrictJson.object(var2);
   }

   void write(String var1, JsonObject var2) throws IOException {
      this.write(var1, var2.toString().getBytes(StandardCharsets.UTF_8));
   }

   void write(String var1, byte[] var2) throws IOException {
      if (var2.length > 131072) {
         throw new IOException("Update state size limit");
      }

      Path var3 = this.file(var1);
      Path var4 = Files.createTempFile(this.directory, "state-", ".tmp");

      try {
         try (FileChannel var5 = FileChannel.open(var4, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS)) {
            ByteBuffer var6 = ByteBuffer.wrap(var2);

            while (var6.hasRemaining()) {
               var5.write(var6);
            }

            var5.force(true);
         }

         this.file(var1);
         Files.move(var4, var3, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         this.syncDirectory();
      } finally {
         Files.deleteIfExists(var4);
      }
   }

   void syncDirectory() {
      try (FileChannel var1 = FileChannel.open(this.directory, StandardOpenOption.READ)) {
         var1.force(true);
      } catch (IOException | UnsupportedOperationException var6) {
      }
   }

   void delete(String var1) throws IOException {
      Files.deleteIfExists(this.file(var1));
   }

   void cancelStaged() throws IOException {
      this.delete("pending.json");
      this.syncDirectory();
      this.delete("artifact.bin");
      this.delete("manifest.json");
      this.delete("download.part");
   }

   UpdateStore.Floor floor() throws IOException {
      JsonObject var1 = this.json("trust-state.json");
      if (var1 == null) {
         if (this.read("registration.json") == null
            && this.read("cached-manifest.json") == null
            && this.read("manifest.json") == null
            && this.read("pending.json") == null
            && this.read("receipt.json") == null) {
            return new UpdateStore.Floor(0L, 0L, "", "");
         } else {
            throw new IOException("Previously initialized update trust state is missing");
         }
      } else {
         StrictJson.fields(var1, "schema", "authorityVersion", "sequence", "releaseId", "sha256");
         schema(var1);
         long var2 = StrictJson.number(var1, "authorityVersion", 0L, Long.MAX_VALUE);
         long var4 = StrictJson.number(var1, "sequence", 0L, Long.MAX_VALUE);
         String var6 = StrictJson.string(var1, "releaseId", 128);
         String var7 = StrictJson.string(var1, "sha256", 64);
         if (var4 == 0L ? var6.isEmpty() && var7.isEmpty() : var6.matches("[A-Za-z0-9][A-Za-z0-9._-]{0,127}") && var7.matches("[0-9a-f]{64}")) {
            if (var4 > 0L && var2 == 0L) {
               throw new IOException("Release sequence has no retained signing authority");
            } else {
               return new UpdateStore.Floor(var2, var4, var6, var7);
            }
         } else {
            throw new IOException("Invalid update trust binding");
         }
      }
   }

   UpdateManifest.Verified verify(byte[] var1, byte[] var2, Host var3, Instant var4) throws IOException {
      UpdateStore.Floor var5 = this.floor();
      UpdateManifest.Verified var6 = UpdateManifest.verify(var1, var2, this.installed, var3, var4, var5.authority(), var5.sequence());
      UpdateManifest.Release var7 = var6.release();
      if (var7.sequence() != var5.sequence() || var7.releaseId().equals(var5.releaseId()) && var7.sha256().equals(var5.sha256())) {
         return var6;
      } else {
         throw new IOException("Conflicting release at retained sequence");
      }
   }

   void retain(UpdateManifest.Verified var1) throws IOException {
      UpdateManifest.Release var2 = var1.release();
      JsonObject var3 = object();
      var3.addProperty("authorityVersion", var1.authorityVersion());
      var3.addProperty("sequence", var2.sequence());
      var3.addProperty("releaseId", var2.releaseId());
      var3.addProperty("sha256", var2.sha256());
      this.write("trust-state.json", var3);
   }

   boolean registered() throws IOException {
      JsonObject var1 = this.json("registration.json");
      if (var1 != null && this.origin != null) {
         StrictJson.fields(var1, "schema", "gameDir", "target", "identity", "host");
         schema(var1);
         if (StrictJson.string(var1, "gameDir", 4096).equals(this.game.toString()) && StrictJson.string(var1, "target", 128).equals("mods/LarpClient.jar")) {
            JsonObject var2 = StrictJson.child(var1, "identity");
            StrictJson.fields(var2, "product", "edition", "minecraft", "loader", "channel", "flavor", "version", "buildId");

            Identity var3;
            try {
               var3 = new Identity(
                  StrictJson.string(var2, "product", 128),
                  StrictJson.string(var2, "edition", 128),
                  StrictJson.string(var2, "minecraft", 128),
                  StrictJson.string(var2, "loader", 128),
                  StrictJson.string(var2, "channel", 128),
                  StrictJson.string(var2, "flavor", 128),
                  StrictJson.string(var2, "version", 128),
                  StrictJson.string(var2, "buildId", 128)
               );
            } catch (IllegalArgumentException var7) {
               throw new IOException("Invalid helper build identity", var7);
            }

            JsonObject var4 = StrictJson.child(var1, "host");
            StrictJson.fields(var4, "platform", "javaVersion", "loaderVersion");

            try {
               new Host(
                  StrictJson.string(var4, "platform", 32), (int)StrictJson.number(var4, "javaVersion", 21L, 99L), StrictJson.string(var4, "loaderVersion", 128)
               );
            } catch (IllegalArgumentException var6) {
               throw new IOException("Invalid helper host", var6);
            }

            Path var5 = this.game.resolve("mods/LarpClient.jar");
            safeAncestors(var5);
            return this.installed.sameStream(var3)
               && this.installed.buildId().equals(var3.buildId())
               && this.installed.version().equals(var3.version())
               && this.origin.equals(var5)
               && Files.isRegularFile(var5, LinkOption.NOFOLLOW_LINKS);
         } else {
            throw new IOException("Update helper registration belongs to another instance");
         }
      } else {
         return false;
      }
   }

   boolean installable() throws IOException {
      if (this.origin == null) {
         return false;
      }

      try {
         if (this.registered()) {
            return true;
         }
      } catch (IOException var2) {
      }

      return this.game.resolve("mods").equals(this.origin.getParent())
         && this.origin.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".jar")
         && Files.isRegularFile(this.origin, LinkOption.NOFOLLOW_LINKS)
         && Files.isWritable(this.origin);
   }

   List<String> installerCommand(Path var1, Path var2, String var3) throws IOException {
      if (this.origin == null) {
         throw new IOException("Loaded client JAR is unavailable");
      }

      ProcessHandle var4 = ProcessHandle.current();
      long var5 = var4.info().startInstant().orElseThrow(() -> new IOException("Cannot establish current process start time")).toEpochMilli();
      return List.of(
         var1.toString(),
         "-jar",
         var2.toString(),
         "apply-after-exit",
         "--game-dir",
         this.game.toString(),
         "--jar",
         this.origin.toString(),
         "--loader-version",
         var3,
         "--pid",
         Long.toString(var4.pid()),
         "--started-at",
         Long.toString(var5)
      );
   }

   void launchInstaller(byte[] var1, Path var2, String var3) throws IOException {
      Path var4 = this.file("LarpUpdater.jar");
      Path var5 = Files.createTempFile(this.directory, "installer-", ".tmp");

      try {
         Files.write(var5, var1);
         Files.move(var5, var4, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } finally {
         Files.deleteIfExists(var5);
      }

      new ProcessBuilder(this.installerCommand(var2, var4, var3))
         .directory(this.directory.toFile())
         .redirectErrorStream(true)
         .redirectOutput(this.file("installer.log").toFile())
         .start();
   }

   void retainCheckedAt(UpdateManifest.Release var1, Instant var2) throws IOException {
      JsonObject var3 = object();
      var3.addProperty("checkedAt", var2.getEpochSecond());
      var3.addProperty("releaseId", var1.releaseId());
      var3.addProperty("sha256", var1.sha256());
      this.write("cached-status.json", var3);
   }

   long cacheCheckedAt(UpdateManifest.Release var1, Instant var2) throws IOException {
      JsonObject var3 = this.json("cached-status.json");
      if (var3 == null) {
         return 0L;
      } else {
         StrictJson.fields(var3, "schema", "checkedAt", "releaseId", "sha256");
         schema(var3);
         long var4 = StrictJson.number(var3, "checkedAt", 1L, Long.MAX_VALUE);
         if (var4 <= var2.getEpochSecond() + 300L
            && StrictJson.identifier(var3, "releaseId").equals(var1.releaseId())
            && StrictJson.string(var3, "sha256", 64).equals(var1.sha256())) {
            return var4;
         } else {
            throw new IOException("Cached check timestamp binding mismatch");
         }
      }
   }

   UpdateStore.Receipt receipt() throws IOException {
      JsonObject var1 = this.json("receipt.json");
      if (var1 == null) {
         return null;
      } else {
         StrictJson.fields(var1, "schema", "releaseId", "buildId", "version", "sha256", "installedAt", "previousBuildId");
         schema(var1);
         String var2 = StrictJson.identifier(var1, "releaseId");
         String var3 = StrictJson.identifier(var1, "buildId");
         String var4 = StrictJson.string(var1, "version", 128);
         String var5 = StrictJson.string(var1, "sha256", 64);
         StrictJson.identifier(var1, "previousBuildId");
         StrictJson.number(var1, "installedAt", 1L, Long.MAX_VALUE);
         if (!var5.matches("[0-9a-f]{64}")) {
            throw new IOException("Invalid installation receipt");
         } else {
            return var3.equals(this.installed.buildId()) && var4.equals(this.installed.version()) ? new UpdateStore.Receipt(var2, var3, var4, var5) : null;
         }
      }
   }

   void queue(UpdateManifest.Release var1, Instant var2) throws IOException {
      if (!this.installable()) {
         throw new IOException("This client cannot install updates in place; use the official download page");
      }

      JsonObject var3 = object();
      var3.addProperty("releaseId", var1.releaseId());
      var3.addProperty("buildId", var1.buildId());
      var3.addProperty("sha256", var1.sha256());
      var3.addProperty("artifactId", var1.artifactId());
      var3.addProperty("target", "mods/LarpClient.jar");
      var3.addProperty("approvedAt", var2.getEpochSecond());
      this.write("pending.json", var3);
   }

   boolean pending(UpdateManifest.Release var1, Instant var2) throws IOException {
      JsonObject var3 = this.json("pending.json");
      if (var3 == null) {
         return false;
      } else {
         StrictJson.fields(var3, "schema", "releaseId", "buildId", "sha256", "artifactId", "target", "approvedAt");
         schema(var3);
         long var4 = StrictJson.number(var3, "approvedAt", 1L, Long.MAX_VALUE);
         if (StrictJson.identifier(var3, "releaseId").equals(var1.releaseId())
            && StrictJson.identifier(var3, "buildId").equals(var1.buildId())
            && StrictJson.string(var3, "sha256", 64).equals(var1.sha256())
            && StrictJson.identifier(var3, "artifactId").equals(var1.artifactId())
            && StrictJson.string(var3, "target", 128).equals("mods/LarpClient.jar")) {
            return this.installable() && var4 <= var2.getEpochSecond() && var2.getEpochSecond() - var4 < 86400L && var2.getEpochSecond() < var1.expiresAt();
         } else {
            throw new IOException("Queued approval does not match verified update");
         }
      }
   }

   synchronized void runtimeLease() throws IOException {
      ProcessHandle var1 = ProcessHandle.current();
      long var2 = var1.pid();
      long var4 = var1.info().startInstant().orElseThrow(() -> new IOException("Cannot establish current process start time")).toEpochMilli();
      String var6 = "runtime-" + var2 + "-" + var4;
      FileChannel var7 = FileChannel.open(this.file(var6 + ".lock"), StandardOpenOption.CREATE, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS);

      FileLock var8;
      try {
         var8 = var7.tryLock();
      } catch (OverlappingFileLockException var13) {
         var7.close();
         throw new IOException("A runtime lease is already owned by this JVM", var13);
      }

      if (var8 == null) {
         var7.close();
         throw new IOException("Cannot hold runtime lease");
      }

      try {
         this.locked(() -> {
            JsonObject var6x = object();
            var6x.addProperty("pid", var2);
            var6x.addProperty("startedAt", var4);
            var6x.addProperty("buildId", this.installed.buildId());
            this.write(var6 + ".json", var6x);
            return null;
         });
         synchronized (LEASES) {
            LEASES.add(new UpdateStore.RuntimeLease(var7, var8));
         }
      } catch (IOException var12) {
         var8.release();
         var7.close();
         throw var12;
      }
   }

   static JsonObject object() {
      JsonObject var0 = new JsonObject();
      var0.addProperty("schema", 1);
      return var0;
   }

   static void schema(JsonObject var0) throws IOException {
      StrictJson.number(var0, "schema", 1L, 1L);
   }

   record Floor(long authority, long sequence, String releaseId, String sha256) {
   }

   record Receipt(String releaseId, String buildId, String version, String sha256) {
   }

   private record RuntimeLease(FileChannel channel, FileLock lock) {
   }

   interface Work<T> {
      T run() throws IOException;
   }
}

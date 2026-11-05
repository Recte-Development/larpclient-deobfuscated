package com.larpclient.account;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.larpclient.h4;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AccountVault implements AutoCloseable {
   private static final byte[] MAGIC = "LARPAC02".getBytes(StandardCharsets.US_ASCII);
   private static final int NONCE = 12;
   private static final int MAX_WRAPPED = 4096;
   public static final int MAX_ACCOUNTS = 32;
   public static final int MAX_PROXIES = 32;
   public static final int MAX_BYTES = 2097152;
   private final Path file;
   private final AccountVault.Protection protection;
   private byte[] key;
   private byte[] wrappedKey;
   private byte[] savedDigest;
   private final LinkedHashMap<UUID, AccountVault.Entry> entries = new LinkedHashMap<>();
   private final LinkedHashMap<UUID, h4> proxies = new LinkedHashMap<>();
   private UUID activeProxy;

   public AccountVault(Path var1) {
      this(var1, new SystemAccountProtection());
   }

   public AccountVault(Path var1, AccountVault.Protection var2) {
      this.file = var1.toAbsolutePath().normalize().resolve("microsoft-accounts.v2.vault");
      this.protection = Objects.requireNonNull(var2);
   }

   public boolean exists() {
      return Files.exists(this.file, LinkOption.NOFOLLOW_LINKS);
   }

   public boolean legacyExists() {
      return Files.exists(this.file.resolveSibling("microsoft-accounts.vault"), LinkOption.NOFOLLOW_LINKS);
   }

   public synchronized boolean unlocked() {
      return this.key != null;
   }

   public synchronized List<AccountVault.Entry> entries() {
      return List.copyOf(this.entries.values());
   }

   public synchronized AccountVault.Entry entry(UUID var1) {
      return this.entries.get(var1);
   }

   public synchronized List<h4> proxies() {
      return List.copyOf(this.proxies.values());
   }

   public synchronized h4 activeProxy() {
      return this.activeProxy == null ? null : this.proxies.get(this.activeProxy);
   }

   public synchronized void open() throws IOException {
      this.close();
      byte[] var1 = null;
      byte[] var2 = null;

      try {
         this.directory();
         if (!this.exists()) {
            var1 = random(32);
            this.wrappedKey = this.protection.protect(var1);
            if (this.wrappedKey.length < 2 || this.wrappedKey.length > 4096) {
               throw new AccountVault.Failure("System account encryption returned an invalid key.");
            }

            this.write(var1, this.wrappedKey, new LinkedHashMap<>(), new LinkedHashMap<>(), null);
         } else {
            label186: {
               byte[] var3 = this.read();
               if (var3.length >= MAGIC.length + 4 + 12 + 18 && Arrays.equals(MAGIC, Arrays.copyOf(var3, MAGIC.length))) {
                  int var4 = ByteBuffer.wrap(var3, MAGIC.length, 4).getInt();
                  int var5 = MAGIC.length + 4 + var4 + 12;
                  if (var4 >= 2 && var4 <= 4096 && var5 <= var3.length - 16) {
                     this.wrappedKey = Arrays.copyOfRange(var3, MAGIC.length + 4, MAGIC.length + 4 + var4);
                     var1 = this.protection.unprotect(this.wrappedKey);
                     if (var1.length != 32) {
                        throw new AccountVault.Failure("System account key is unavailable. Original file retained.");
                     }

                     Cipher var6 = Cipher.getInstance("AES/GCM/NoPadding");
                     var6.init(2, new SecretKeySpec(var1, "AES"), new GCMParameterSpec(128, var3, var5 - 12, 12));
                     var6.updateAAD(var3, 0, var5);
                     var2 = var6.doFinal(var3, var5, var3.length - var5);
                     JsonObject var7 = JsonParser.parseString(new String(var2, StandardCharsets.UTF_8)).getAsJsonObject();
                     if (var7.get("schema").getAsInt() != 1) {
                        throw new AccountVault.Failure("Unsupported account vault. Original file retained.");
                     }

                     JsonArray var8 = var7.getAsJsonArray("accounts");
                     if (var8.size() > 32) {
                        throw new AccountVault.Failure("Too many saved accounts.");
                     }

                     for (JsonElement var10 : var8) {
                        JsonObject var11 = var10.getAsJsonObject();
                        AccountVault.Entry var12 = new AccountVault.Entry(
                           UUID.fromString(var11.get("id").getAsString()), var11.get("name").getAsString(), var11.getAsJsonObject("authentication")
                        );
                        if (this.entries.putIfAbsent(var12.id(), var12) != null) {
                           throw new AccountVault.Failure("Duplicate saved account. Original file retained.");
                        }
                     }

                     if (var7.has("proxies")) {
                        JsonArray var22 = var7.getAsJsonArray("proxies");
                        if (var22.size() > 32) {
                           throw new AccountVault.Failure("Too many saved proxies.");
                        }

                        for (JsonElement var25 : var22) {
                           h4 var26 = h4.fromJson(var25.getAsJsonObject());
                           if (this.proxies.putIfAbsent(var26.id(), var26) != null) {
                              throw new AccountVault.Failure("Duplicate saved proxy. Original file retained.");
                           }
                        }
                     }

                     if (var7.has("activeProxy")) {
                        UUID var23 = UUID.fromString(var7.get("activeProxy").getAsString());
                        this.activeProxy = this.proxies.containsKey(var23) ? var23 : null;
                     }

                     this.savedDigest = digest(var3);
                     break label186;
                  }

                  throw new AccountVault.Failure("Damaged account vault. Original file retained.");
               }

               throw new AccountVault.Failure("Unrecognized account vault. Original file retained.");
            }
         }

         this.key = var1;
         var1 = null;
      } catch (AEADBadTagException var18) {
         this.close();
         throw new AccountVault.Failure("Saved accounts could not be verified. Original file retained.");
      } catch (GeneralSecurityException | RuntimeException var19) {
         this.close();
         throw new AccountVault.Failure("Could not open the account vault. Original file retained.");
      } catch (IOException var20) {
         this.close();
         throw var20;
      } finally {
         if (var1 != null) {
            Arrays.fill(var1, (byte)0);
         }

         if (var2 != null) {
            Arrays.fill(var2, (byte)0);
         }
      }
   }

   public synchronized void put(AccountVault.Entry var1) throws IOException {
      this.requireUnlocked();
      LinkedHashMap var2 = new LinkedHashMap<>(this.entries);
      var2.put(var1.id(), var1);
      if (var2.size() > 32) {
         throw new AccountVault.Failure("The account list is full (32 accounts).");
      }

      this.write(this.key, this.wrappedKey, var2, this.proxies, this.activeProxy);
      this.entries.clear();
      this.entries.putAll(var2);
   }

   public synchronized void remove(UUID var1) throws IOException {
      this.requireUnlocked();
      LinkedHashMap var2 = new LinkedHashMap<>(this.entries);
      var2.remove(var1);
      this.write(this.key, this.wrappedKey, var2, this.proxies, this.activeProxy);
      this.entries.clear();
      this.entries.putAll(var2);
   }

   public synchronized void putProxy(h4 var1, boolean var2) throws IOException {
      this.requireUnlocked();
      LinkedHashMap var3 = new LinkedHashMap<>(this.proxies);
      var3.put(var1.id(), var1);
      if (var3.size() > 32) {
         throw new AccountVault.Failure("The proxy list is full (32 proxies).");
      }

      UUID var4 = var2 ? var1.id() : this.activeProxy;
      this.write(this.key, this.wrappedKey, this.entries, var3, var4);
      this.proxies.clear();
      this.proxies.putAll(var3);
      this.activeProxy = var4;
   }

   public synchronized void removeProxy(UUID var1) throws IOException {
      this.requireUnlocked();
      LinkedHashMap var2 = new LinkedHashMap<>(this.proxies);
      var2.remove(var1);
      UUID var3 = var1.equals(this.activeProxy) ? null : this.activeProxy;
      this.write(this.key, this.wrappedKey, this.entries, var2, var3);
      this.proxies.clear();
      this.proxies.putAll(var2);
      this.activeProxy = var3;
   }

   public synchronized void selectProxy(UUID var1) throws IOException {
      this.requireUnlocked();
      if (var1 != null && !this.proxies.containsKey(var1)) {
         throw new AccountVault.Failure("That proxy is no longer saved. Reopen the list.");
      }

      this.write(this.key, this.wrappedKey, this.entries, this.proxies, var1);
      this.activeProxy = var1;
   }

   public synchronized void archive() throws IOException {
      this.directory();
      if (!Files.isRegularFile(this.file, LinkOption.NOFOLLOW_LINKS)) {
         throw new AccountVault.Failure("No regular account vault is available to archive.");
      }

      Path var1 = this.file.resolveSibling(this.file.getFileName() + ".lock");

      try (
         FileChannel var2 = FileChannel.open(var1, StandardOpenOption.CREATE, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS);
         FileLock var3 = var2.tryLock();
      ) {
         if (var3 == null) {
            throw new AccountVault.Failure("Another Minecraft instance is saving accounts. Retry later.");
         }

         privatePermissions(var1, false);
         Path var4 = this.file.resolveSibling("microsoft-accounts.recovery-" + UUID.randomUUID() + ".vault");
         Files.move(this.file, var4, StandardCopyOption.ATOMIC_MOVE);
         this.close();
      } catch (OverlappingFileLockException var10) {
         throw new AccountVault.Failure("Another account save is active. Retry later.");
      }
   }

   private void requireUnlocked() throws IOException {
      if (this.key == null) {
         throw new AccountVault.Failure("Saved accounts are unavailable. Choose Retry.");
      }
   }

   private void directory() throws IOException {
      for (Path var1 = this.file.getParent(); var1 != null; var1 = var1.getParent()) {
         if (Files.isSymbolicLink(var1)) {
            throw new AccountVault.Failure("Account storage cannot use symbolic links.");
         }
      }

      Files.createDirectories(this.file.getParent());
      privatePermissions(this.file.getParent(), true);
   }

   private byte[] read() throws IOException {
      if (Files.isRegularFile(this.file, LinkOption.NOFOLLOW_LINKS) && Files.size(this.file) <= 2097152L) {
         try (InputStream var1 = Files.newInputStream(this.file, StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS)) {
            byte[] var2 = var1.readNBytes(2097153);
            if (var2.length > 2097152) {
               throw new AccountVault.Failure("Account vault is too large.");
            } else {
               return var2;
            }
         }
      } else {
         throw new AccountVault.Failure("Account vault is not a bounded regular file. Original file retained.");
      }
   }

   private void write(byte[] var1, byte[] var2, Map<UUID, AccountVault.Entry> var3, Map<UUID, h4> var4, UUID var5) throws IOException {
      this.directory();
      JsonObject var6 = new JsonObject();
      var6.addProperty("schema", 1);
      JsonArray var7 = new JsonArray();

      for (AccountVault.Entry var9 : var3.values()) {
         JsonObject var10 = new JsonObject();
         var10.addProperty("id", var9.id().toString());
         var10.addProperty("name", var9.name());
         var10.add("authentication", var9.authentication());
         var7.add(var10);
      }

      var6.add("accounts", var7);
      if (!var4.isEmpty()) {
         JsonArray var48 = new JsonArray();

         for (h4 var52 : var4.values()) {
            var48.add(var52.json());
         }

         var6.add("proxies", var48);
      }

      if (var5 != null) {
         var6.addProperty("activeProxy", var5.toString());
      }

      byte[] var49 = var6.toString().getBytes(StandardCharsets.UTF_8);

      byte[] var51;
      try {
         byte[] var53 = ByteBuffer.allocate(MAGIC.length + 4 + var2.length + 12).put(MAGIC).putInt(var2.length).put(var2).put(random(12)).array();
         Cipher var11 = Cipher.getInstance("AES/GCM/NoPadding");
         var11.init(1, new SecretKeySpec(var1, "AES"), new GCMParameterSpec(128, var53, var53.length - 12, 12));
         var11.updateAAD(var53);
         byte[] var12 = var11.doFinal(var49);
         var51 = ByteBuffer.allocate(var53.length + var12.length).put(var53).put(var12).array();
         if (var51.length > 2097152) {
            throw new AccountVault.Failure("Account vault is too large.");
         }
      } catch (GeneralSecurityException var41) {
         throw new AccountVault.Failure("Account encryption failed.");
      } finally {
         Arrays.fill(var49, (byte)0);
      }

      Path var54 = this.file.resolveSibling(this.file.getFileName() + ".lock");
      if (Files.isSymbolicLink(var54)) {
         throw new AccountVault.Failure("Unsafe account lock file.");
      }

      try (
         FileChannel var55 = FileChannel.open(var54, StandardOpenOption.CREATE, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS);
         FileLock var56 = var55.tryLock();
      ) {
         if (var56 == null) {
            throw new AccountVault.Failure("Another Minecraft instance is saving accounts. Retry later.");
         }

         privatePermissions(var54, false);
         if (this.savedDigest == null ? this.exists() : !this.exists() || !MessageDigest.isEqual(this.savedDigest, digest(this.read()))) {
            throw new AccountVault.Failure("Saved accounts changed in another instance. Close this list and retry.");
         }

         Path var13 = Files.createTempFile(this.file.getParent(), "accounts-", ".tmp");

         try {
            privatePermissions(var13, false);

            try (FileChannel var14 = FileChannel.open(var13, StandardOpenOption.WRITE)) {
               ByteBuffer var15 = ByteBuffer.wrap(var51);

               while (var15.hasRemaining()) {
                  var14.write(var15);
               }

               var14.force(true);
            }

            if (this.savedDigest == null) {
               Files.createLink(this.file, var13);
            } else {
               Files.move(var13, this.file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }

            this.savedDigest = digest(var51);
         } finally {
            Files.deleteIfExists(var13);
         }
      } catch (OverlappingFileLockException var47) {
         throw new AccountVault.Failure("Another account save is active. Retry later.");
      }
   }

   private static byte[] random(int var0) {
      byte[] var1 = new byte[var0];
      new SecureRandom().nextBytes(var1);
      return var1;
   }

   private static byte[] digest(byte[] var0) {
      try {
         return MessageDigest.getInstance("SHA-256").digest(var0);
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   private static void privatePermissions(Path var0, boolean var1) throws IOException {
      try {
         Files.setPosixFilePermissions(var0, PosixFilePermissions.fromString(var1 ? "rwx------" : "rw-------"));
      } catch (UnsupportedOperationException var3) {
      }
   }

   @Override
   public synchronized void close() {
      if (this.key != null) {
         Arrays.fill(this.key, (byte)0);
      }

      this.key = this.wrappedKey = this.savedDigest = null;
      this.entries.clear();
      this.proxies.clear();
      this.activeProxy = null;
   }

   public record Entry(UUID id, String name, JsonObject authentication) {
      public Entry(UUID id, String name, JsonObject authentication) {
         Objects.requireNonNull(id);
         if (name != null && name.matches("[A-Za-z0-9_]{1,16}")) {
            authentication = Objects.requireNonNull(authentication).deepCopy();
            if (authentication.toString().length() > 65536) {
               throw new IllegalArgumentException("Account state too large");
            }

            this.id = id;
            this.name = name;
            this.authentication = authentication;
         } else {
            throw new IllegalArgumentException("Invalid Minecraft name");
         }
      }

      public JsonObject authentication() {
         return this.authentication.deepCopy();
      }

      public boolean token() {
         return TokenLogin.saved(this.authentication);
      }

      public Instant tokenExpiry() {
         return TokenLogin.expiresAt(this.authentication);
      }

      @Override
      public String toString() {
         return "Saved Minecraft account";
      }
   }

   public static final class Failure extends IOException {
      public Failure(String var1) {
         super(var1);
      }
   }

   public interface Protection {
      byte[] protect(byte[] var1) throws IOException;

      byte[] unprotect(byte[] var1) throws IOException;
   }
}

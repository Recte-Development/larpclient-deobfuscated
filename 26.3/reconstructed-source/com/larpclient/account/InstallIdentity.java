package com.larpclient.account;

import com.google.gson.JsonObject;
import com.larpclient.util.misc.LarpLog;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;

public final class InstallIdentity {
   private final KeyPair keyPair;
   private static InstallIdentity instance;

   InstallIdentity(KeyPair var1) {
      this.keyPair = var1;
   }

   public static synchronized InstallIdentity getInstance() throws IOException {
      if (instance == null) {
         instance = load(HardwareIdentity.configDir);
      }

      return instance;
   }

   static InstallIdentity load(Path var0) throws IOException {
      CredentialProtector var1 = new CredentialProtector(var0);
      Path var2 = var0.resolve("identity.json");
      Path var3 = var0.resolve("identity.lock");

      try {
         JsonObject var4;
         try {
            var4 = CredentialFileStore.read(var2);
         } catch (NoSuchFileException var8) {
            return CredentialFileStore.withLock(var3, () -> loadOrCreate(var2, var1));
         }

         InstallIdentity var5 = parse(var4, var1);
         if (!var4.has("protectedPrivateKey") && CredentialProtector.isSupported()) {
            try {
               CredentialFileStore.withLock(var3, () -> {
                  JsonObject var2x = CredentialFileStore.read(var2);
                  if (!var2x.has("protectedPrivateKey")) {
                     save(var2, parse(var2x, var1), var1, true);
                  }

                  return null;
               });
            } catch (IOException | RuntimeException var7) {
               LarpLog.errorOnce("Auth", "Could not migrate the device key to OS credential storage", var7);
            }
         }

         return var5;
      } catch (RuntimeException var9) {
         throw new IOException("Unable to read install identity; restore identity.json or reset the license", var9);
      }
   }

   private static InstallIdentity loadOrCreate(Path var0, CredentialProtector var1) throws IOException {
      try {
         return parse(CredentialFileStore.read(var0), var1);
      } catch (NoSuchFileException var4) {
         try {
            InstallIdentity var2 = new InstallIdentity(KeyPairGenerator.getInstance("Ed25519").generateKeyPair());
            save(var0, var2, var1, false);
            return var2;
         } catch (GeneralSecurityException var3) {
            throw new IOException("Unable to create install identity", var3);
         }
      }
   }

   private static void save(Path var0, InstallIdentity var1, CredentialProtector var2, boolean var3) throws IOException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("publicKey", var1.getPublicKey());
      byte[] var5 = var1.keyPair.getPrivate().getEncoded();

      try {
         String var6 = var2.protect(var5, "device-key");
         if (var6 != null) {
            var4.addProperty("protectedPrivateKey", var6);
         } else {
            if (var3) {
               return;
            }

            var4.addProperty("privateKey", base64UrlEncode(var5));
         }

         CredentialFileStore.writeAtomic(var0, var4);
      } finally {
         Arrays.fill(var5, (byte)0);
      }
   }

   private static InstallIdentity parse(JsonObject var0, CredentialProtector var1) throws IOException {
      byte[] var2 = var0.has("protectedPrivateKey")
         ? var1.unprotect(var0.get("protectedPrivateKey").getAsString(), "device-key")
         : base64UrlDecode(var0.get("privateKey").getAsString());

      try {
         KeyFactory var3 = KeyFactory.getInstance("Ed25519");
         PrivateKey var4 = var3.generatePrivate(new PKCS8EncodedKeySpec(var2));
         PublicKey var5 = var3.generatePublic(new X509EncodedKeySpec(base64UrlDecode(var0.get("publicKey").getAsString())));
         InstallIdentity var6 = new InstallIdentity(new KeyPair(var5, var4));
         Signature var7 = Signature.getInstance("Ed25519");
         var7.initVerify(var5);
         var7.update("install-key-check".getBytes(StandardCharsets.UTF_8));
         if (!var7.verify(base64UrlDecode(var6.sign("install-key-check")))) {
            throw new IOException("Install identity keys do not match");
         } else {
            return var6;
         }
      } catch (GeneralSecurityException var12) {
         throw new IOException("Unable to read install identity; restore identity.json or reset the license", var12);
      } finally {
         Arrays.fill(var2, (byte)0);
      }
   }

   static void restrictPermissions(Path var0, boolean var1) throws IOException {
      try {
         Files.setPosixFilePermissions(var0, PosixFilePermissions.fromString(var1 ? "rwx------" : "rw-------"));
      } catch (UnsupportedOperationException var3) {
      }
   }

   public String getPublicKey() {
      return base64UrlEncode(this.keyPair.getPublic().getEncoded());
   }

   public String getFingerprint() {
      return sha256HexBytes(this.keyPair.getPublic().getEncoded());
   }

   public String sign(String var1) throws IOException {
      try {
         Signature var2 = Signature.getInstance("Ed25519");
         var2.initSign(this.keyPair.getPrivate());
         var2.update(var1.getBytes(StandardCharsets.UTF_8));
         return base64UrlEncode(var2.sign());
      } catch (GeneralSecurityException var3) {
         throw new IOException("Install signature failed", var3);
      }
   }

   public static String sha256Hex(String var0) {
      return sha256HexBytes(var0.getBytes(StandardCharsets.UTF_8));
   }

   static String sha256HexBytes(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String randomToken() {
      byte[] var0 = new byte[32];
      new SecureRandom().nextBytes(var0);
      return HexFormat.of().formatHex(var0);
   }

   static String base64UrlEncode(byte[] var0) {
      return Base64.getUrlEncoder().withoutPadding().encodeToString(var0);
   }

   static byte[] base64UrlDecode(String var0) {
      return Base64.getUrlDecoder().decode(var0);
   }
}

package com.larpclient;

import com.google.gson.JsonObject;
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

public final class n {
   private final KeyPair A;
   private static n y;

   n(KeyPair var1) {
      this.A = var1;
   }

   public static synchronized n B() throws IOException {
      if (y == null) {
         y = E(u2.d);
      }

      return y;
   }

   static n E(Path var0) throws IOException {
      h_ var1 = new h_(var0);
      Path var2 = var0.resolve("identity.json");
      Path var3 = var0.resolve("identity.lock");

      try {
         JsonObject var4;
         try {
            var4 = hm.a(var2);
         } catch (NoSuchFileException var8) {
            return hm.Q(var3, () -> J(var2, var1));
         }

         n var5 = T(var4, var1);
         if (!var4.has("protectedPrivateKey") && h_.a()) {
            try {
               hm.Q(var3, () -> {
                  JsonObject var2x = hm.a(var2);
                  if (!var2x.has("protectedPrivateKey")) {
                     U(var2, T(var2x, var1), var1, true);
                  }

                  return null;
               });
            } catch (IOException | RuntimeException var7) {
               h.w("Auth", "Could not migrate the device key to OS credential storage", var7);
            }
         }

         return var5;
      } catch (RuntimeException var9) {
         throw new IOException("Unable to read install identity; restore identity.json or reset the license", var9);
      }
   }

   private static n J(Path var0, h_ var1) throws IOException {
      try {
         return T(hm.a(var0), var1);
      } catch (NoSuchFileException var4) {
         try {
            n var2 = new n(KeyPairGenerator.getInstance("Ed25519").generateKeyPair());
            U(var0, var2, var1, false);
            return var2;
         } catch (GeneralSecurityException var3) {
            throw new IOException("Unable to create install identity", var3);
         }
      }
   }

   private static void U(Path var0, n var1, h_ var2, boolean var3) throws IOException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("publicKey", var1.D());
      byte[] var5 = var1.A.getPrivate().getEncoded();

      try {
         String var6 = var2.w(var5, "device-key");
         if (var6 != null) {
            var4.addProperty("protectedPrivateKey", var6);
         } else {
            if (var3) {
               return;
            }

            var4.addProperty("privateKey", f(var5));
         }

         hm.c(var0, var4);
      } finally {
         Arrays.fill(var5, (byte)0);
      }
   }

   private static n T(JsonObject var0, h_ var1) throws IOException {
      byte[] var2 = var0.has("protectedPrivateKey")
         ? var1.l(var0.get("protectedPrivateKey").getAsString(), "device-key")
         : e(var0.get("privateKey").getAsString());

      try {
         KeyFactory var3 = KeyFactory.getInstance("Ed25519");
         PrivateKey var4 = var3.generatePrivate(new PKCS8EncodedKeySpec(var2));
         PublicKey var5 = var3.generatePublic(new X509EncodedKeySpec(e(var0.get("publicKey").getAsString())));
         n var6 = new n(new KeyPair(var5, var4));
         Signature var7 = Signature.getInstance("Ed25519");
         var7.initVerify(var5);
         var7.update("install-key-check".getBytes(StandardCharsets.UTF_8));
         if (!var7.verify(e(var6.Y("install-key-check")))) {
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

   static void q(Path var0, boolean var1) throws IOException {
      try {
         Files.setPosixFilePermissions(var0, PosixFilePermissions.fromString(var1 ? "rwx------" : "rw-------"));
      } catch (UnsupportedOperationException var3) {
      }
   }

   public String D() {
      return f(this.A.getPublic().getEncoded());
   }

   public String e() {
      return O(this.A.getPublic().getEncoded());
   }

   public String Y(String var1) throws IOException {
      try {
         Signature var2 = Signature.getInstance("Ed25519");
         var2.initSign(this.A.getPrivate());
         var2.update(var1.getBytes(StandardCharsets.UTF_8));
         return f(var2.sign());
      } catch (GeneralSecurityException var3) {
         throw new IOException("Install signature failed", var3);
      }
   }

   public static String J(String var0) {
      return O(var0.getBytes(StandardCharsets.UTF_8));
   }

   static String O(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String s() {
      byte[] var0 = new byte[32];
      new SecureRandom().nextBytes(var0);
      return HexFormat.of().formatHex(var0);
   }

   static String f(byte[] var0) {
      return Base64.getUrlEncoder().withoutPadding().encodeToString(var0);
   }

   static byte[] e(String var0) {
      return Base64.getUrlDecoder().decode(var0);
   }
}

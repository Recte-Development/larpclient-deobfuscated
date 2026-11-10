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

public final class e1 {
   private final KeyPair U;
   private static e1 X;

   e1(KeyPair var1) {
      this.U = var1;
   }

   public static synchronized e1 h() throws IOException {
      if (X == null) {
         X = g(wj.E);
      }

      return X;
   }

   static e1 g(Path var0) throws IOException {
      jw var1 = new jw(var0);
      Path var2 = var0.resolve("identity.json");
      Path var3 = var0.resolve("identity.lock");

      try {
         JsonObject var4;
         try {
            var4 = jh.Q(var2);
         } catch (NoSuchFileException var8) {
            return jh.l(var3, () -> n(var2, var1));
         }

         e1 var5 = u(var4, var1);
         if (!var4.has("protectedPrivateKey") && jw.z()) {
            try {
               jh.l(var3, () -> {
                  JsonObject var2x = jh.Q(var2);
                  if (!var2x.has("protectedPrivateKey")) {
                     k(var2, u(var2x, var1), var1, true);
                  }

                  return null;
               });
            } catch (IOException | RuntimeException var7) {
               q8.O("Auth", "Could not migrate the device key to OS credential storage", var7);
            }
         }

         return var5;
      } catch (RuntimeException var9) {
         throw new IOException("Unable to read install identity; restore identity.json or reset the license", var9);
      }
   }

   private static e1 n(Path var0, jw var1) throws IOException {
      try {
         return u(jh.Q(var0), var1);
      } catch (NoSuchFileException var4) {
         try {
            e1 var2 = new e1(KeyPairGenerator.getInstance("Ed25519").generateKeyPair());
            k(var0, var2, var1, false);
            return var2;
         } catch (GeneralSecurityException var3) {
            throw new IOException("Unable to create install identity", var3);
         }
      }
   }

   private static void k(Path var0, e1 var1, jw var2, boolean var3) throws IOException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("publicKey", var1.S());
      byte[] var5 = var1.U.getPrivate().getEncoded();

      try {
         String var6 = var2.e(var5, "device-key");
         if (var6 != null) {
            var4.addProperty("protectedPrivateKey", var6);
         } else {
            if (var3) {
               return;
            }

            var4.addProperty("privateKey", a(var5));
         }

         jh.c(var0, var4);
      } finally {
         Arrays.fill(var5, (byte)0);
      }
   }

   private static e1 u(JsonObject var0, jw var1) throws IOException {
      byte[] var2 = var0.has("protectedPrivateKey")
         ? var1.s(var0.get("protectedPrivateKey").getAsString(), "device-key")
         : j(var0.get("privateKey").getAsString());

      try {
         KeyFactory var3 = KeyFactory.getInstance("Ed25519");
         PrivateKey var4 = var3.generatePrivate(new PKCS8EncodedKeySpec(var2));
         PublicKey var5 = var3.generatePublic(new X509EncodedKeySpec(j(var0.get("publicKey").getAsString())));
         e1 var6 = new e1(new KeyPair(var5, var4));
         Signature var7 = Signature.getInstance("Ed25519");
         var7.initVerify(var5);
         var7.update("install-key-check".getBytes(StandardCharsets.UTF_8));
         if (!var7.verify(j(var6.V("install-key-check")))) {
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

   static void H(Path var0, boolean var1) throws IOException {
      try {
         Files.setPosixFilePermissions(var0, PosixFilePermissions.fromString(var1 ? "rwx------" : "rw-------"));
      } catch (UnsupportedOperationException var3) {
      }
   }

   public String S() {
      return a(this.U.getPublic().getEncoded());
   }

   public String n() {
      return x(this.U.getPublic().getEncoded());
   }

   public String V(String var1) throws IOException {
      try {
         Signature var2 = Signature.getInstance("Ed25519");
         var2.initSign(this.U.getPrivate());
         var2.update(var1.getBytes(StandardCharsets.UTF_8));
         return a(var2.sign());
      } catch (GeneralSecurityException var3) {
         throw new IOException("Install signature failed", var3);
      }
   }

   public static String v(String var0) {
      return x(var0.getBytes(StandardCharsets.UTF_8));
   }

   static String x(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String j() {
      byte[] var0 = new byte[32];
      new SecureRandom().nextBytes(var0);
      return HexFormat.of().formatHex(var0);
   }

   static String a(byte[] var0) {
      return Base64.getUrlEncoder().withoutPadding().encodeToString(var0);
   }

   static byte[] j(String var0) {
      return Base64.getUrlDecoder().decode(var0);
   }
}

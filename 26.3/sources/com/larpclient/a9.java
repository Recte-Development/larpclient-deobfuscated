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

public final class a9 {
   private final KeyPair i;
   private static a9 u;

   a9(KeyPair var1) {
      this.i = var1;
   }

   public static synchronized a9 b() throws IOException {
      if (u == null) {
         u = O(ie.p);
      }

      return u;
   }

   static a9 O(Path var0) throws IOException {
      jy var1 = new jy(var0);
      Path var2 = var0.resolve("identity.json");
      Path var3 = var0.resolve("identity.lock");

      try {
         JsonObject var4;
         try {
            var4 = f.J(var2);
         } catch (NoSuchFileException var8) {
            return f.B(var3, () -> r(var2, var1));
         }

         a9 var5 = Y(var4, var1);
         if (!var4.has("protectedPrivateKey") && jy.h()) {
            try {
               f.B(var3, () -> {
                  JsonObject var2x = f.J(var2);
                  if (!var2x.has("protectedPrivateKey")) {
                     N(var2, Y(var2x, var1), var1, true);
                  }

                  return null;
               });
            } catch (IOException | RuntimeException var7) {
               _q.g("Auth", "Could not migrate the device key to OS credential storage", var7);
            }
         }

         return var5;
      } catch (RuntimeException var9) {
         throw new IOException("Unable to read install identity; restore identity.json or reset the license", var9);
      }
   }

   private static a9 r(Path var0, jy var1) throws IOException {
      try {
         return Y(f.J(var0), var1);
      } catch (NoSuchFileException var4) {
         try {
            a9 var2 = new a9(KeyPairGenerator.getInstance("Ed25519").generateKeyPair());
            N(var0, var2, var1, false);
            return var2;
         } catch (GeneralSecurityException var3) {
            throw new IOException("Unable to create install identity", var3);
         }
      }
   }

   private static void N(Path var0, a9 var1, jy var2, boolean var3) throws IOException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("publicKey", var1.V());
      byte[] var5 = var1.i.getPrivate().getEncoded();

      try {
         String var6 = var2.R(var5, "device-key");
         if (var6 != null) {
            var4.addProperty("protectedPrivateKey", var6);
         } else {
            if (var3) {
               return;
            }

            var4.addProperty("privateKey", a(var5));
         }

         f.D(var0, var4);
      } finally {
         Arrays.fill(var5, (byte)0);
      }
   }

   private static a9 Y(JsonObject var0, jy var1) throws IOException {
      byte[] var2 = var0.has("protectedPrivateKey")
         ? var1.f(var0.get("protectedPrivateKey").getAsString(), "device-key")
         : k(var0.get("privateKey").getAsString());

      try {
         KeyFactory var3 = KeyFactory.getInstance("Ed25519");
         PrivateKey var4 = var3.generatePrivate(new PKCS8EncodedKeySpec(var2));
         PublicKey var5 = var3.generatePublic(new X509EncodedKeySpec(k(var0.get("publicKey").getAsString())));
         a9 var6 = new a9(new KeyPair(var5, var4));
         Signature var7 = Signature.getInstance("Ed25519");
         var7.initVerify(var5);
         var7.update("install-key-check".getBytes(StandardCharsets.UTF_8));
         if (!var7.verify(k(var6.G("install-key-check")))) {
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

   static void z(Path var0, boolean var1) throws IOException {
      try {
         Files.setPosixFilePermissions(var0, PosixFilePermissions.fromString(var1 ? "rwx------" : "rw-------"));
      } catch (UnsupportedOperationException var3) {
      }
   }

   public String V() {
      return a(this.i.getPublic().getEncoded());
   }

   public String g() {
      return t(this.i.getPublic().getEncoded());
   }

   public String G(String var1) throws IOException {
      try {
         Signature var2 = Signature.getInstance("Ed25519");
         var2.initSign(this.i.getPrivate());
         var2.update(var1.getBytes(StandardCharsets.UTF_8));
         return a(var2.sign());
      } catch (GeneralSecurityException var3) {
         throw new IOException("Install signature failed", var3);
      }
   }

   public static String A(String var0) {
      return t(var0.getBytes(StandardCharsets.UTF_8));
   }

   static String t(byte[] var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static String w() {
      byte[] var0 = new byte[32];
      new SecureRandom().nextBytes(var0);
      return HexFormat.of().formatHex(var0);
   }

   static String a(byte[] var0) {
      return Base64.getUrlEncoder().withoutPadding().encodeToString(var0);
   }

   static byte[] k(String var0) {
      return Base64.getUrlDecoder().decode(var0);
   }
}

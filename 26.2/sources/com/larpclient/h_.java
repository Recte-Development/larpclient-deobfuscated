package com.larpclient;

import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Crypt32Util;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

final class h_ {
   private static final String A = "dpapi-v1:";
   private static final String Q = "keyring-v1:";
   private static final int F = 12;
   private static final int O = 1;
   private final ud V;

   h_(Path var1) {
      this.V = new ud(var1);
   }

   static boolean a() {
      return Platform.isWindows() || ud.D();
   }

   String w(byte[] var1, String var2) throws IOException {
      if (!Platform.isWindows()) {
         byte[] var3 = this.V.H(true);
         return var3 == null ? null : "keyring-v1:" + n.f(p(var3, var1, var2));
      }

      try {
         return "dpapi-v1:" + n.f(Crypt32Util.cryptProtectData(var1, b(var2), 1, "Larp Client", null));
      } catch (RuntimeException | LinkageError var4) {
         throw new IOException("Windows could not protect the saved Larp credential", var4);
      }
   }

   byte[] l(String var1, String var2) throws IOException {
      if (var1.startsWith("keyring-v1:") && !Platform.isWindows()) {
         byte[] var3 = this.V.H(false);
         if (var3 == null) {
            throw new IOException("Saved credential requires its original keyring");
         } else {
            byte[] var4 = n.e(var1.substring("keyring-v1:".length()));
            if (var4.length < 28) {
               throw new IOException("Invalid saved credential");
            } else {
               return t(2, var3, Arrays.copyOf(var4, 12), Arrays.copyOfRange(var4, 12, var4.length), var2);
            }
         }
      } else if (var1.startsWith("dpapi-v1:") && Platform.isWindows()) {
         try {
            return Crypt32Util.cryptUnprotectData(n.e(var1.substring("dpapi-v1:".length())), b(var2), 1, null);
         } catch (RuntimeException | LinkageError var5) {
            throw new IOException("Windows could not unlock the saved Larp credential", var5);
         }
      } else {
         throw new IOException("Saved credential requires its original OS account");
      }
   }

   private static byte[] p(byte[] var0, byte[] var1, String var2) throws IOException {
      byte[] var3 = new byte[12];
      new SecureRandom().nextBytes(var3);
      byte[] var4 = t(1, var0, var3, var1, var2);
      byte[] var5 = Arrays.copyOf(var3, var3.length + var4.length);
      System.arraycopy(var4, 0, var5, var3.length, var4.length);
      return var5;
   }

   private static byte[] t(int var0, byte[] var1, byte[] var2, byte[] var3, String var4) throws IOException {
      try {
         Cipher var5 = Cipher.getInstance("AES/GCM/NoPadding");
         var5.init(var0, new SecretKeySpec(var1, "AES"), new GCMParameterSpec(128, var2));
         var5.updateAAD(b(var4));
         return var5.doFinal(var3);
      } catch (GeneralSecurityException var6) {
         throw new IOException("Unable to unlock or protect the saved Larp credential", var6);
      }
   }

   private static byte[] b(String var0) {
      return ("larp-credential-v1:" + var0).getBytes(StandardCharsets.US_ASCII);
   }
}

package com.larpclient;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Arrays;

final class c6 {
   private static final String v = "com.larpclient.credentials";
   private static final int t = 32;
   private final Path i;
   private final String z;
   private byte[] P;

   c6(Path var1) {
      this.i = var1;
      this.z = e1.v(var1.toAbsolutePath().normalize().toString());
   }

   static boolean J() {
      if (Platform.isMac()) {
         return true;
      }

      if (!Platform.isLinux()) {
         return false;
      }

      String var0 = System.getenv("DBUS_SESSION_BUS_ADDRESS");
      String var1 = System.getenv("XDG_RUNTIME_DIR");
      return var0 != null && !var0.isBlank() || var1 != null && Files.exists(Path.of(var1, "bus"));
   }

   synchronized byte[] y(boolean var1) throws IOException {
      if (this.P != null) {
         return this.P;
      }

      if (!J()) {
         return null;
      }

      try {
         this.P = this.z();
         if (this.P == null && var1) {
            this.P = jh.l(this.i.resolve("keyring.lock"), () -> {
               byte[] var1x = this.z();
               if (var1x != null) {
                  return var1x;
               }

               byte[] var2 = new byte[32];
               new SecureRandom().nextBytes(var2);

               try {
                  this.O(var2);
                  return var2;
               } catch (IOException | RuntimeException | LinkageError var4x) {
                  Arrays.fill(var2, (byte)0);
                  throw var4x;
               }
            });
         }

         return this.P;
      } catch (c6.n var3) {
         return null;
      } catch (RuntimeException | LinkageError var4) {
         throw new IOException("Unable to access the OS credential keyring", var4);
      }
   }

   private byte[] z() throws IOException {
      byte[] var1 = Platform.isMac() ? this.a() : this.N();
      if (var1 != null && var1.length != 32) {
         Arrays.fill(var1, (byte)0);
         throw new IOException("Invalid Larp key in the OS credential keyring");
      } else {
         return var1;
      }
   }

   private void O(byte[] var1) throws IOException {
      if (Platform.isMac()) {
         byte[] var2 = "com.larpclient.credentials".getBytes(StandardCharsets.US_ASCII);
         byte[] var3 = this.z.getBytes(StandardCharsets.US_ASCII);
         int var4 = c6.a.v
            .getFunction("SecKeychainAddGenericPassword")
            .invokeInt(new Object[]{null, var2.length, var2, var3.length, var3, var1.length, var1, null});
         if (var4 != 0) {
            throw new IOException("Could not store the Larp key in macOS Keychain (" + var4 + ")");
         }
      } else {
         PointerByReference var9 = new PointerByReference();
         NativeLibrary var10 = R();
         Pointer var11 = p(var10);

         try {
            int var5 = var10.getFunction("secret_password_store_sync")
               .invokeInt(new Object[]{var11, null, "Larp Client credentials", e1.a(var1), null, var9, "scope", this.z, null});
            A(var9);
            if (var5 == 0) {
               throw new IOException("Could not store the Larp key in Secret Service");
            }
         } finally {
            var10.getFunction("secret_schema_unref").invokeVoid(new Object[]{var11});
         }
      }
   }

   private byte[] a() throws IOException {
      byte[] var1 = "com.larpclient.credentials".getBytes(StandardCharsets.US_ASCII);
      byte[] var2 = this.z.getBytes(StandardCharsets.US_ASCII);
      IntByReference var3 = new IntByReference();
      PointerByReference var4 = new PointerByReference();
      NativeLibrary var5 = c6.a.v;
      int var6 = var5.getFunction("SecKeychainFindGenericPassword").invokeInt(new Object[]{null, var1.length, var1, var2.length, var2, var3, var4, null});
      if (var6 == -25300) {
         return null;
      }

      if (var6 != 0) {
         throw new IOException("Could not unlock the Larp key from macOS Keychain (" + var6 + ")");
      }

      Pointer var7 = var4.getValue();
      if (var7 == null) {
         throw new IOException("Missing Larp key data in macOS Keychain");
      }

      try {
         if (var3.getValue() != 32) {
            throw new IOException("Invalid Larp key length in macOS Keychain");
         } else {
            return var7.getByteArray(0L, 32);
         }
      } finally {
         var5.getFunction("SecKeychainItemFreeContent").invokeInt(new Object[]{null, var7});
      }
   }

   private byte[] N() throws IOException {
      PointerByReference var1 = new PointerByReference();
      NativeLibrary var2 = R();
      Pointer var3 = p(var2);

      try {
         Pointer var4 = var2.getFunction("secret_password_lookup_sync").invokePointer(new Object[]{var3, null, var1, "scope", this.z, null});

         try {
            A(var1);
            return var4 == null ? null : e1.j(var4.getString(0L, "UTF-8"));
         } finally {
            if (var4 != null) {
               var2.getFunction("secret_password_free").invokeVoid(new Object[]{var4});
            }
         }
      } finally {
         var2.getFunction("secret_schema_unref").invokeVoid(new Object[]{var3});
      }
   }

   private static NativeLibrary R() throws c6.n {
      try {
         return c6.h.e;
      } catch (UnsatisfiedLinkError | NoClassDefFoundError var1) {
         throw new c6.n();
      }
   }

   private static Pointer p(NativeLibrary var0) throws IOException {
      Pointer var1 = var0.getFunction("secret_schema_new").invokePointer(new Object[]{"com.larpclient.credentials", 0, "scope", 0, null});
      if (var1 == null) {
         throw new IOException("Could not create the Larp Secret Service schema");
      } else {
         return var1;
      }
   }

   private static void A(PointerByReference var0) throws IOException {
      Pointer var1 = var0.getValue();
      if (var1 != null) {
         NativeLibrary var2 = c6.u.H;

         try {
            Pointer var3 = c6.o.s.getFunction("g_dbus_error_get_remote_error").invokePointer(new Object[]{var1});
            if (var3 != null) {
               try {
                  String var4 = var3.getString(0L);
                  if ("org.freedesktop.DBus.Error.ServiceUnknown".equals(var4) || "org.freedesktop.DBus.Error.NameHasNoOwner".equals(var4)) {
                     throw new c6.n();
                  }
               } finally {
                  var2.getFunction("g_free").invokeVoid(new Object[]{var3});
               }
            }

            throw new IOException("Unable to unlock or store the Larp key in Secret Service");
         } finally {
            var2.getFunction("g_error_free").invokeVoid(new Object[]{var1});
         }
      }
   }

   private static final class a {
      static final NativeLibrary v = NativeLibrary.getInstance("/System/Library/Frameworks/Security.framework/Security");
   }

   private static final class h {
      static final NativeLibrary e = NativeLibrary.getInstance("secret-1");
   }

   private static final class n extends IOException {
   }

   private static final class o {
      static final NativeLibrary s = NativeLibrary.getInstance("gio-2.0");
   }

   private static final class u {
      static final NativeLibrary H = NativeLibrary.getInstance("glib-2.0");
   }
}

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

final class js {
   private static final String f = "com.larpclient.credentials";
   private static final int V = 32;
   private final Path P;
   private final String a;
   private byte[] O;

   js(Path var1) {
      this.P = var1;
      this.a = a9.A(var1.toAbsolutePath().normalize().toString());
   }

   static boolean v() {
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

   synchronized byte[] U(boolean var1) throws IOException {
      if (this.O != null) {
         return this.O;
      }

      if (!v()) {
         return null;
      }

      try {
         this.O = this.a();
         if (this.O == null && var1) {
            this.O = com.larpclient.f.B(this.P.resolve("keyring.lock"), () -> {
               byte[] var1x = this.a();
               if (var1x != null) {
                  return var1x;
               }

               byte[] var2 = new byte[32];
               new SecureRandom().nextBytes(var2);

               try {
                  this.U(var2);
                  return var2;
               } catch (IOException | RuntimeException | LinkageError var4x) {
                  Arrays.fill(var2, (byte)0);
                  throw var4x;
               }
            });
         }

         return this.O;
      } catch (js.h var3) {
         return null;
      } catch (RuntimeException | LinkageError var4) {
         throw new IOException("Unable to access the OS credential keyring", var4);
      }
   }

   private byte[] a() throws IOException {
      byte[] var1 = Platform.isMac() ? this.B() : this.I();
      if (var1 != null && var1.length != 32) {
         Arrays.fill(var1, (byte)0);
         throw new IOException("Invalid Larp key in the OS credential keyring");
      } else {
         return var1;
      }
   }

   private void U(byte[] var1) throws IOException {
      if (Platform.isMac()) {
         byte[] var2 = "com.larpclient.credentials".getBytes(StandardCharsets.US_ASCII);
         byte[] var3 = this.a.getBytes(StandardCharsets.US_ASCII);
         int var4 = js.y.z
            .getFunction("SecKeychainAddGenericPassword")
            .invokeInt(new Object[]{null, var2.length, var2, var3.length, var3, var1.length, var1, null});
         if (var4 != 0) {
            throw new IOException("Could not store the Larp key in macOS Keychain (" + var4 + ")");
         }
      } else {
         PointerByReference var9 = new PointerByReference();
         NativeLibrary var10 = u();
         Pointer var11 = q(var10);

         try {
            int var5 = var10.getFunction("secret_password_store_sync")
               .invokeInt(new Object[]{var11, null, "Larp Client credentials", a9.a(var1), null, var9, "scope", this.a, null});
            x(var9);
            if (var5 == 0) {
               throw new IOException("Could not store the Larp key in Secret Service");
            }
         } finally {
            var10.getFunction("secret_schema_unref").invokeVoid(new Object[]{var11});
         }
      }
   }

   private byte[] B() throws IOException {
      byte[] var1 = "com.larpclient.credentials".getBytes(StandardCharsets.US_ASCII);
      byte[] var2 = this.a.getBytes(StandardCharsets.US_ASCII);
      IntByReference var3 = new IntByReference();
      PointerByReference var4 = new PointerByReference();
      NativeLibrary var5 = js.y.z;
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

   private byte[] I() throws IOException {
      PointerByReference var1 = new PointerByReference();
      NativeLibrary var2 = u();
      Pointer var3 = q(var2);

      try {
         Pointer var4 = var2.getFunction("secret_password_lookup_sync").invokePointer(new Object[]{var3, null, var1, "scope", this.a, null});

         try {
            x(var1);
            return var4 == null ? null : a9.k(var4.getString(0L, "UTF-8"));
         } finally {
            if (var4 != null) {
               var2.getFunction("secret_password_free").invokeVoid(new Object[]{var4});
            }
         }
      } finally {
         var2.getFunction("secret_schema_unref").invokeVoid(new Object[]{var3});
      }
   }

   private static NativeLibrary u() throws js.h {
      try {
         return js.a.d;
      } catch (UnsatisfiedLinkError | NoClassDefFoundError var1) {
         throw new js.h();
      }
   }

   private static Pointer q(NativeLibrary var0) throws IOException {
      Pointer var1 = var0.getFunction("secret_schema_new").invokePointer(new Object[]{"com.larpclient.credentials", 0, "scope", 0, null});
      if (var1 == null) {
         throw new IOException("Could not create the Larp Secret Service schema");
      } else {
         return var1;
      }
   }

   private static void x(PointerByReference var0) throws IOException {
      Pointer var1 = var0.getValue();
      if (var1 != null) {
         NativeLibrary var2 = js.u.g;

         try {
            Pointer var3 = js.k.D.getFunction("g_dbus_error_get_remote_error").invokePointer(new Object[]{var1});
            if (var3 != null) {
               try {
                  String var4 = var3.getString(0L);
                  if ("org.freedesktop.DBus.Error.ServiceUnknown".equals(var4) || "org.freedesktop.DBus.Error.NameHasNoOwner".equals(var4)) {
                     throw new js.h();
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
      static final NativeLibrary d = NativeLibrary.getInstance("secret-1");
   }

   private static final class h extends IOException {
   }

   private static final class k {
      static final NativeLibrary D = NativeLibrary.getInstance("gio-2.0");
   }

   private static final class u {
      static final NativeLibrary g = NativeLibrary.getInstance("glib-2.0");
   }

   private static final class y {
      static final NativeLibrary z = NativeLibrary.getInstance("/System/Library/Frameworks/Security.framework/Security");
   }
}

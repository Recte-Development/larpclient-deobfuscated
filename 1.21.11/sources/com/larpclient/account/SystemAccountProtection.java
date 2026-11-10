package com.larpclient.account;

import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Crypt32Util;
import com.sun.jna.ptr.PointerByReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class SystemAccountProtection implements AccountVault.Protection {
   private static final byte[] CONTEXT = "LarpClient Microsoft accounts v2".getBytes(StandardCharsets.UTF_8);
   private static final String SERVICE = "com.larpclient.microsoft-accounts";

   @Override
   public byte[] protect(byte[] var1) throws AccountVault.Failure {
      try {
         if (Platform.isWindows()) {
            return envelope(1, Crypt32Util.cryptProtectData(var1, CONTEXT, 1, "Larp Minecraft accounts", null));
         }

         String var2 = UUID.randomUUID().toString();
         if (Platform.isMac()) {
            mac(var2, var1);
         } else {
            if (!Platform.isLinux()) {
               throw new AccountVault.Failure("Automatic account encryption is unavailable on this system.");
            }

            linux(var2, var1);
         }

         return envelope(Platform.isMac() ? 2 : 3, var2.getBytes(StandardCharsets.US_ASCII));
      } catch (AccountVault.Failure var3) {
         throw var3;
      } catch (RuntimeException | LinkageError var4) {
         throw unavailable();
      }
   }

   @Override
   public byte[] unprotect(byte[] var1) throws AccountVault.Failure {
      try {
         if (var1.length < 2) {
            throw unavailable();
         } else {
            byte[] var2 = Arrays.copyOfRange(var1, 1, var1.length);
            if (var1[0] == 1 && Platform.isWindows()) {
               return Crypt32Util.cryptUnprotectData(var2, CONTEXT, 1, null);
            } else {
               String var3 = new String(var2, StandardCharsets.US_ASCII);
               if (!UUID.fromString(var3).toString().equals(var3)) {
                  throw unavailable();
               } else if (var1[0] == 2 && Platform.isMac()) {
                  return mac(var3, null);
               } else if (var1[0] == 3 && Platform.isLinux()) {
                  return linux(var3, null);
               } else {
                  throw new AccountVault.Failure("These saved accounts belong to another system. Add them again on this computer.");
               }
            }
         }
      } catch (AccountVault.Failure var4) {
         throw var4;
      } catch (RuntimeException | LinkageError var5) {
         throw unavailable();
      }
   }

   private static byte[] envelope(int var0, byte[] var1) {
      byte[] var2 = new byte[var1.length + 1];
      var2[0] = (byte)var0;
      System.arraycopy(var1, 0, var2, 1, var1.length);
      return var2;
   }

   private static AccountVault.Failure unavailable() {
      return new AccountVault.Failure("System account storage is unavailable or locked. Unlock your system keychain, then retry.");
   }

   private static Pointer symbol(NativeLibrary var0, String var1) {
      return var0.getGlobalVariableAddress(var1).getPointer(0L);
   }

   private static void release(NativeLibrary var0, Pointer var1) {
      if (var1 != null) {
         var0.getFunction("CFRelease").invokeVoid(new Object[]{var1});
      }
   }

   private static byte[] mac(String var0, byte[] var1) throws AccountVault.Failure {
      NativeLibrary var2 = NativeLibrary.getInstance("/System/Library/Frameworks/CoreFoundation.framework/CoreFoundation");
      NativeLibrary var3 = NativeLibrary.getInstance("/System/Library/Frameworks/Security.framework/Security");
      Pointer var4 = var2.getFunction("CFDictionaryCreateMutable")
         .invokePointer(
            new Object[]{
               null,
               new NativeLong(0L),
               var2.getGlobalVariableAddress("kCFTypeDictionaryKeyCallBacks"),
               var2.getGlobalVariableAddress("kCFTypeDictionaryValueCallBacks")
            }
         );
      ArrayList var5 = new ArrayList();
      Pointer var6 = null;

      try {
         BiConsumer var7 = (var3x, var4x) -> var2.getFunction("CFDictionarySetValue").invokeVoid(new Object[]{var4, symbol(var3, var3x), var4x});
         Function var8 = var2x -> {
            Pointer var3x = var2.getFunction("CFStringCreateWithCString").invokePointer(new Object[]{null, var2x, 134217984});
            var5.add(var3x);
            return var3x;
         };
         var7.accept("kSecClass", symbol(var3, "kSecClassGenericPassword"));
         var7.accept("kSecAttrService", (Pointer)var8.apply("com.larpclient.microsoft-accounts"));
         var7.accept("kSecAttrAccount", (Pointer)var8.apply(var0));
         var7.accept("kSecUseAuthenticationUI", symbol(var3, "kSecUseAuthenticationUIFail"));
         if (var1 != null) {
            Pointer var21 = var2.getFunction("CFDataCreate").invokePointer(new Object[]{null, var1, new NativeLong(var1.length)});
            var5.add(var21);
            var7.accept("kSecValueData", var21);
            var7.accept("kSecAttrLabel", (Pointer)var8.apply("Larp Minecraft accounts"));
            int var22 = var3.getFunction("SecItemAdd").invokeInt(new Object[]{var4, null});
            if (var22 != 0) {
               throw unavailable();
            } else {
               return null;
            }
         } else {
            var7.accept("kSecReturnData", symbol(var2, "kCFBooleanTrue"));
            var7.accept("kSecMatchLimit", symbol(var3, "kSecMatchLimitOne"));
            PointerByReference var9 = new PointerByReference();
            int var10 = var3.getFunction("SecItemCopyMatching").invokeInt(new Object[]{var4, var9});
            var6 = var9.getValue();
            if (var10 == 0 && var6 != null) {
               long var11 = ((NativeLong)var2.getFunction("CFDataGetLength").invoke(NativeLong.class, new Object[]{var6})).longValue();
               if (var11 != 32L) {
                  throw unavailable();
               } else {
                  return var2.getFunction("CFDataGetBytePtr").invokePointer(new Object[]{var6}).getByteArray(0L, 32);
               }
            } else {
               throw unavailable();
            }
         }
      } finally {
         release(var2, var6);
         release(var2, var4);

         for (Pointer var18 : var5) {
            release(var2, var18);
         }
      }
   }

   private static byte[] linux(String var0, byte[] var1) throws AccountVault.Failure {
      NativeLibrary var2 = NativeLibrary.getInstance("secret-1");
      NativeLibrary var3 = NativeLibrary.getInstance("gio-2.0");
      NativeLibrary var4 = NativeLibrary.getInstance("gobject-2.0");
      NativeLibrary var5 = NativeLibrary.getInstance("glib-2.0");
      Pointer var6 = var2.getFunction("secret_schema_new").invokePointer(new Object[]{"com.larpclient.microsoft-accounts", 0, "vault", 0, null});
      Pointer var7 = var3.getFunction("g_cancellable_new").invokePointer(new Object[0]);
      PointerByReference var8 = new PointerByReference();
      Object var9 = new Object();
      Pointer[] var10 = new Pointer[]{var7};
      CompletableFuture.delayedExecutor(30L, TimeUnit.SECONDS).execute(() -> {
         synchronized (var9) {
            if (var10[0] != null) {
               var3.getFunction("g_cancellable_cancel").invokeVoid(new Object[]{var10[0]});
            }
         }
      });
      Pointer var11 = null;

      try {
         if (var1 != null) {
            int var32 = var2.getFunction("secret_password_store_sync")
               .invokeInt(new Object[]{var6, "default", "Larp Minecraft accounts", Base64.getEncoder().encodeToString(var1), var7, var8, "vault", var0, null});
            if (var32 != 0 && var8.getValue() == null) {
               return null;
            } else {
               throw unavailable();
            }
         } else {
            var11 = var2.getFunction("secret_password_lookup_sync").invokePointer(new Object[]{var6, var7, var8, "vault", var0, null});
            if (var11 != null && var8.getValue() == null) {
               byte[] var12 = new byte[44];

               byte[] var13;
               try {
                  for (int var14 = 0; var14 < 44; var14++) {
                     var12[var14] = var11.getByte(var14);
                     if (var12[var14] == 0) {
                        throw unavailable();
                     }
                  }

                  if (var11.getByte(44L) != 0) {
                     throw unavailable();
                  }

                  var13 = Base64.getDecoder().decode(var12);
               } finally {
                  Arrays.fill(var12, (byte)0);
               }

               if (var13.length != 32) {
                  Arrays.fill(var13, (byte)0);
                  throw unavailable();
               } else {
                  return var13;
               }
            } else {
               throw unavailable();
            }
         }
      } finally {
         if (var11 != null) {
            var2.getFunction("secret_password_free").invokeVoid(new Object[]{var11});
         }

         if (var8.getValue() != null) {
            var5.getFunction("g_error_free").invokeVoid(new Object[]{var8.getValue()});
         }

         var2.getFunction("secret_schema_unref").invokeVoid(new Object[]{var6});
         synchronized (var9) {
            var10[0] = null;
            var4.getFunction("g_object_unref").invokeVoid(new Object[]{var7});
         }
      }
   }
}

package com.larpclient;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import java.io.IOException;
import java.time.Instant;
import java.util.Locale;

public final class o implements fc {
   private static final String j = "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager";
   private static final String K = "2050C4EE-11A0-57DE-AED7-C97C70338245";
   private static final String M = "00000036-0000-0000-C000-000000000046";
   private static final long h = 4000000000L;
   private volatile boolean X;
   private boolean f;

   @Override
   public synchronized p6 read() throws IOException, InterruptedException {
      if (this.X) {
         return null;
      }

      if (!Platform.isWindows()) {
         throw new IOException("Windows media sessions require Windows");
      }

      if (Thread.currentThread().isVirtual()) {
         throw new IOException("Windows media sessions require the lyrics platform worker");
      }

      byte var1 = 0;

      try {
         E(o.x.z("RoInitialize").invokeInt(new Object[]{1}), "initialize WinRT");
         var1 = 1;
         long var2 = System.nanoTime() + 4000000000L;

         try (
            o.q var4 = this.v();
            o.q var5 = this.E(var4.z(6, "request media sessions"), var2);
            o.q var6 = var5.z(7, "enumerate media sessions");
         ) {
            if (!this.f) {
               _q.z("WorldLyrics", "Direct Windows media-session reader ready through JNA");
               this.f = (boolean)1;
            }

            try (o.q var7 = this.i(var6)) {
               if (var7 == null) {
                  return null;
               }

               try (
                  o.q var8 = this.E(var7.z(7, "request Spotify metadata"), var2);
                  o.q var9 = var7.z(8, "read Spotify timeline");
                  o.q var10 = var7.z(9, "read Spotify playback");
               ) {
                  this.N();
                  p6 var11 = j(
                     var7.G(6, 1024),
                     var8.G(6, 512),
                     var8.G(9, 512),
                     var8.G(10, 512),
                     var9.U(7),
                     var9.U(10),
                     var9.U(11),
                     var10.m(7) == 4,
                     Instant.now(),
                     System.nanoTime()
                  );
                  return this.X ? null : var11;
               }
            }
         }
      } catch (LinkageError | RuntimeException var44) {
         throw new IOException("Windows Spotify media access failed; re-enable WorldLyrics to retry", var44);
      } finally {
         if (var1 != 0) {
            o.x.z("RoUninitialize").invokeVoid(new Object[0]);
         }
      }
   }

   private void N() throws IOException, InterruptedException {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException("Spotify reader interrupted");
      }

      if (this.X) {
         throw new IOException("Windows Spotify reader stopped");
      }
   }

   private o.q v() throws IOException {
      PointerByReference var1 = new PointerByReference();
      GUID var2 = new GUID("2050C4EE-11A0-57DE-AED7-C97C70338245");
      var2.write();
      Memory var3 = new Memory(("Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager".length() + 1L) * 2L);

      o.q var5;
      try {
         var3.setWideString(0L, "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager");
         PointerByReference var4 = new PointerByReference();
         E(
            o.x.z("WindowsCreateString").invokeInt(new Object[]{var3, "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager".length(), var4}),
            "create media class name"
         );

         try {
            E(o.x.z("RoGetActivationFactory").invokeInt(new Object[]{var4.getValue(), var2.getPointer(), var1}), "activate media sessions");
            var5 = new o.q(var1.getValue());
         } finally {
            o.x.z("WindowsDeleteString").invokeInt(new Object[]{var4.getValue()});
         }
      } catch (Throwable var12) {
         try {
            var3.close();
         } catch (Throwable var10) {
            var12.addSuppressed(var10);
         }

         throw var12;
      }

      var3.close();
      return var5;
   }

   private o.q i(o.q var1) throws IOException, InterruptedException {
      o.q var2 = null;

      try {
         int var3 = var1.m(7);
         if (var3 >= 0 && var3 <= 256) {
            for (int var4 = 0; var4 < var3; var4++) {
               this.N();
               o.q var5 = var1.z(6, "read media session", var4);

               try {
                  if (var5.G(6, 1024).toLowerCase(Locale.ROOT).contains("spotify")) {
                     int var6;
                     try (o.q var7 = var5.z(9, "read media playback")) {
                        var6 = var7.m(7) == 4 ? 1 : 0;
                     }

                     if (var2 == null || var6 != 0) {
                        if (var2 != null) {
                           var2.close();
                        }

                        var2 = var5;
                        var5 = null;
                        if (var6 != 0) {
                           break;
                        }
                     }
                  }
               } finally {
                  if (var5 != null) {
                     var5.close();
                  }
               }
            }

            o.q var24 = var2;
            var2 = null;
            return var24;
         } else {
            throw new IOException("Too many Windows media sessions");
         }
      } finally {
         if (var2 != null) {
            var2.close();
         }
      }
   }

   o.q E(o.q var1, long var2) throws IOException, InterruptedException {
      try (
         var1;
         o.q var5 = var1.E("00000036-0000-0000-C000-000000000046");
      ) {
         byte var6 = 0;

         try {
            while (true) {
               this.N();
               int var7 = var5.m(7);
               var6 = (byte)(var7 != 0 ? 1 : 0);
               if (var7 == 1) {
                  return var1.z(8, "complete media request");
               }

               if (var7 == 2) {
                  throw new IOException("Windows Spotify media request cancelled");
               }

               if (var7 == 3) {
                  int var8 = var5.m(8);
                  throw new IOException("Windows Spotify media request failed (" + G(var8) + ")");
               }

               if (var7 != 0) {
                  throw new IOException("Invalid Windows media request state");
               }

               if (System.nanoTime() - var2 >= 0L) {
                  throw new IOException("Windows Spotify media request timed out; re-enable WorldLyrics to retry");
               }

               Thread.sleep(10L);
            }
         } finally {
            if (var6 == 0) {
               var5.l(9);
            }

            if (var6 != 0 || var5.m(7) != 0) {
               var5.l(10);
            }
         }
      }
   }

   static p6 j(String var0, String var1, String var2, String var3, long var4, long var6, long var8, boolean var10, Instant var11, long var12) {
      if (!var1.isBlank() && !var2.isBlank()) {
         double var14 = Math.max(0.0, var4 / 1.0E7);
         double var16 = var6 / 1.0E7;
         if (var10) {
            double var18 = var11.getEpochSecond() + var11.getNano() / 1.0E9;
            var16 += Math.max(0.0, var18 - (var8 / 1.0E7 - 1.16444736E10));
         }

         var16 = Math.max(0.0, var14 > 0.0 ? Math.min(var14, var16) : var16);
         return new p6(var0, var1, var2, var3, var14, var16, var10, var12);
      } else {
         return null;
      }
   }

   @Override
   public void close() {
      this.X = (boolean)1;
   }

   private static String G(int var0) {
      return String.format(Locale.ROOT, "0x%08X", var0);
   }

   static void E(int var0, String var1) throws IOException {
      if (var0 < 0) {
         throw new IOException("Windows Spotify: " + var1 + " failed (" + G(var0) + "); re-enable WorldLyrics to retry");
      }
   }

   static final class q implements AutoCloseable {
      private Pointer q;

      q(Pointer var1) throws IOException {
         if (var1 == null) {
            throw new IOException("Windows media API returned a missing interface");
         }

         this.q = var1;
      }

      int l(int var1, Object... var2) {
         if (this.q == null) {
            throw new IllegalStateException("Released Windows media interface");
         }

         Object[] var3 = new Object[var2.length + 1];
         var3[0] = this.q;
         System.arraycopy(var2, 0, var3, 1, var2.length);
         Pointer var4 = this.q.getPointer(0L).getPointer((long)var1 * Native.POINTER_SIZE);
         return Function.getFunction(var4, 63).invokeInt(var3);
      }

      o.q z(int var1, String var2, Object... var3) throws IOException {
         PointerByReference var4 = new PointerByReference();
         Object[] var5 = new Object[var3.length + 1];
         System.arraycopy(var3, 0, var5, 0, var3.length);
         var5[var3.length] = var4;
         o.E(this.l(var1, var5), var2);
         return new o.q(var4.getValue());
      }

      o.q E(String var1) throws IOException {
         GUID var2 = new GUID(var1);
         var2.write();
         return this.z(0, "query media request status", var2.getPointer());
      }

      int m(int var1) throws IOException {
         IntByReference var2 = new IntByReference();
         o.E(this.l(var1, var2), "read media value");
         return var2.getValue();
      }

      long U(int var1) throws IOException {
         LongByReference var2 = new LongByReference();
         o.E(this.l(var1, var2), "read media time");
         return var2.getValue();
      }

      String G(int var1, int var2) throws IOException {
         PointerByReference var3 = new PointerByReference();
         o.E(this.l(var1, var3), "read media text");
         Pointer var4 = var3.getValue();

         try {
            IntByReference var5 = new IntByReference();
            Pointer var6 = o.x.z("WindowsGetStringRawBuffer").invokePointer(new Object[]{var4, var5});
            int var7 = var5.getValue();
            if (var7 < 0 || var7 > var2) {
               throw new IOException("Oversized Windows media metadata");
            } else if (var7 == 0) {
               return "";
            } else if (var6 == null) {
               throw new IOException("Missing Windows media text buffer");
            } else {
               return new String(var6.getCharArray(0L, var7));
            }
         } finally {
            o.x.z("WindowsDeleteString").invokeInt(new Object[]{var4});
         }
      }

      @Override
      public void close() {
         if (this.q != null) {
            this.l(2);
            this.q = null;
         }
      }
   }

   private static final class x {
      private static final NativeLibrary G = NativeLibrary.getInstance("combase");

      static Function z(String var0) {
         return G.getFunction(var0, 63);
      }
   }
}

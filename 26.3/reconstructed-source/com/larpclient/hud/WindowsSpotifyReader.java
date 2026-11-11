package com.larpclient.hud;

import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.MediaSession;
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

public final class WindowsSpotifyReader implements SpotifyLyricsSource {
   private static final String mANAGER_CLASS = "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager";
   private static final String mANAGER_IID = "2050C4EE-11A0-57DE-AED7-C97C70338245";
   private static final String aSYNC_INFO_IID = "00000036-0000-0000-C000-000000000046";
   private static final long tIMEOUT_NANOS = 4000000000L;
   private volatile boolean closed;
   private boolean readerReadyLogged;

   @Override
   public synchronized MediaSession read() throws IOException, InterruptedException {
      if (this.closed) {
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
         awaitRequest(WindowsSpotifyReader.Combase.getFunction("RoInitialize").invokeInt(new Object[]{1}), "initialize WinRT");
         var1 = 1;
         long var2 = System.nanoTime() + 4000000000L;

         try (
            WindowsSpotifyReader.ComInterface var4 = this.activateSessionManager();
            WindowsSpotifyReader.ComInterface var5 = this.awaitRequest(var4.callForInterface(6, "request media sessions"), var2);
            WindowsSpotifyReader.ComInterface var6 = var5.callForInterface(7, "enumerate media sessions");
         ) {
            if (!this.readerReadyLogged) {
               LarpLog.info("WorldLyrics", "Direct Windows media-session reader ready through JNA");
               this.readerReadyLogged = (boolean)1;
            }

            try (WindowsSpotifyReader.ComInterface var7 = this.findSpotifySession(var6)) {
               if (var7 == null) {
                  return null;
               }

               try (
                  WindowsSpotifyReader.ComInterface var8 = this.awaitRequest(var7.callForInterface(7, "request Spotify metadata"), var2);
                  WindowsSpotifyReader.ComInterface var9 = var7.callForInterface(8, "read Spotify timeline");
                  WindowsSpotifyReader.ComInterface var10 = var7.callForInterface(9, "read Spotify playback");
               ) {
                  this.checkNotStopped();
                  MediaSession var11 = buildTrackState(
                     var7.readString(6, 1024),
                     var8.readString(6, 512),
                     var8.readString(9, 512),
                     var8.readString(10, 512),
                     var9.readLong(7),
                     var9.readLong(10),
                     var9.readLong(11),
                     var10.readInt(7) == 4,
                     Instant.now(),
                     System.nanoTime()
                  );
                  return this.closed ? null : var11;
               }
            }
         }
      } catch (LinkageError | RuntimeException var44) {
         throw new IOException("Windows Spotify media access failed; re-enable WorldLyrics to retry", var44);
      } finally {
         if (var1 != 0) {
            WindowsSpotifyReader.Combase.getFunction("RoUninitialize").invokeVoid(new Object[0]);
         }
      }
   }

   private void checkNotStopped() throws IOException, InterruptedException {
      if (Thread.currentThread().isInterrupted()) {
         throw new InterruptedException("Spotify reader interrupted");
      }

      if (this.closed) {
         throw new IOException("Windows Spotify reader stopped");
      }
   }

   private WindowsSpotifyReader.ComInterface activateSessionManager() throws IOException {
      PointerByReference var1 = new PointerByReference();
      GUID var2 = new GUID("2050C4EE-11A0-57DE-AED7-C97C70338245");
      var2.write();
      Memory var3 = new Memory(("Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager".length() + 1L) * 2L);

      WindowsSpotifyReader.ComInterface var5;
      try {
         var3.setWideString(0L, "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager");
         PointerByReference var4 = new PointerByReference();
         awaitRequest(
            WindowsSpotifyReader.Combase.getFunction("WindowsCreateString")
               .invokeInt(new Object[]{var3, "Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager".length(), var4}),
            "create media class name"
         );

         try {
            awaitRequest(
               WindowsSpotifyReader.Combase.getFunction("RoGetActivationFactory").invokeInt(new Object[]{var4.getValue(), var2.getPointer(), var1}),
               "activate media sessions"
            );
            var5 = new WindowsSpotifyReader.ComInterface(var1.getValue());
         } finally {
            WindowsSpotifyReader.Combase.getFunction("WindowsDeleteString").invokeInt(new Object[]{var4.getValue()});
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

   private WindowsSpotifyReader.ComInterface findSpotifySession(WindowsSpotifyReader.ComInterface var1) throws IOException, InterruptedException {
      WindowsSpotifyReader.ComInterface var2 = null;

      try {
         int var3 = var1.readInt(7);
         if (var3 >= 0 && var3 <= 256) {
            for (int var4 = 0; var4 < var3; var4++) {
               this.checkNotStopped();
               WindowsSpotifyReader.ComInterface var5 = var1.callForInterface(6, "read media session", var4);

               try {
                  if (var5.readString(6, 1024).toLowerCase(Locale.ROOT).contains("spotify")) {
                     int var6;
                     try (WindowsSpotifyReader.ComInterface var7 = var5.callForInterface(9, "read media playback")) {
                        var6 = var7.readInt(7) == 4 ? 1 : 0;
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

            WindowsSpotifyReader.ComInterface var24 = var2;
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

   WindowsSpotifyReader.ComInterface awaitRequest(WindowsSpotifyReader.ComInterface var1, long var2) throws IOException, InterruptedException {
      try (
         var1;
         WindowsSpotifyReader.ComInterface var5 = var1.queryInterface("00000036-0000-0000-C000-000000000046");
      ) {
         byte var6 = 0;

         try {
            while (true) {
               this.checkNotStopped();
               int var7 = var5.readInt(7);
               var6 = (byte)(var7 != 0 ? 1 : 0);
               if (var7 == 1) {
                  return var1.callForInterface(8, "complete media request");
               }

               if (var7 == 2) {
                  throw new IOException("Windows Spotify media request cancelled");
               }

               if (var7 == 3) {
                  int var8 = var5.readInt(8);
                  throw new IOException("Windows Spotify media request failed (" + formatHResult(var8) + ")");
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
               var5.invokeVtable(9);
            }

            if (var6 != 0 || var5.readInt(7) != 0) {
               var5.invokeVtable(10);
            }
         }
      }
   }

   static MediaSession buildTrackState(
      String var0, String var1, String var2, String var3, long var4, long var6, long var8, boolean var10, Instant var11, long var12
   ) {
      if (!var1.isBlank() && !var2.isBlank()) {
         double var14 = Math.max(0.0, var4 / 1.0E7);
         double var16 = var6 / 1.0E7;
         if (var10) {
            double var18 = var11.getEpochSecond() + var11.getNano() / 1.0E9;
            var16 += Math.max(0.0, var18 - (var8 / 1.0E7 - 1.16444736E10));
         }

         var16 = Math.max(0.0, var14 > 0.0 ? Math.min(var14, var16) : var16);
         return new MediaSession(var0, var1, var2, var3, var14, var16, var10, var12);
      } else {
         return null;
      }
   }

   @Override
   public void close() {
      this.closed = (boolean)1;
   }

   private static String formatHResult(int var0) {
      return String.format(Locale.ROOT, "0x%08X", var0);
   }

   static void awaitRequest(int var0, String var1) throws IOException {
      if (var0 < 0) {
         throw new IOException("Windows Spotify: " + var1 + " failed (" + formatHResult(var0) + "); re-enable WorldLyrics to retry");
      }
   }

   static final class ComInterface implements AutoCloseable {
      private Pointer pointer;

      ComInterface(Pointer var1) throws IOException {
         if (var1 == null) {
            throw new IOException("Windows media API returned a missing interface");
         }

         this.pointer = var1;
      }

      int invokeVtable(int var1, Object... var2) {
         if (this.pointer == null) {
            throw new IllegalStateException("Released Windows media interface");
         }

         Object[] var3 = new Object[var2.length + 1];
         var3[0] = this.pointer;
         System.arraycopy(var2, 0, var3, 1, var2.length);
         Pointer var4 = this.pointer.getPointer(0L).getPointer((long)var1 * Native.POINTER_SIZE);
         return Function.getFunction(var4, 63).invokeInt(var3);
      }

      WindowsSpotifyReader.ComInterface callForInterface(int var1, String var2, Object... var3) throws IOException {
         PointerByReference var4 = new PointerByReference();
         Object[] var5 = new Object[var3.length + 1];
         System.arraycopy(var3, 0, var5, 0, var3.length);
         var5[var3.length] = var4;
         WindowsSpotifyReader.awaitRequest(this.invokeVtable(var1, var5), var2);
         return new WindowsSpotifyReader.ComInterface(var4.getValue());
      }

      WindowsSpotifyReader.ComInterface queryInterface(String var1) throws IOException {
         GUID var2 = new GUID(var1);
         var2.write();
         return this.callForInterface(0, "query media request status", var2.getPointer());
      }

      int readInt(int var1) throws IOException {
         IntByReference var2 = new IntByReference();
         WindowsSpotifyReader.awaitRequest(this.invokeVtable(var1, var2), "read media value");
         return var2.getValue();
      }

      long readLong(int var1) throws IOException {
         LongByReference var2 = new LongByReference();
         WindowsSpotifyReader.awaitRequest(this.invokeVtable(var1, var2), "read media time");
         return var2.getValue();
      }

      String readString(int var1, int var2) throws IOException {
         PointerByReference var3 = new PointerByReference();
         WindowsSpotifyReader.awaitRequest(this.invokeVtable(var1, var3), "read media text");
         Pointer var4 = var3.getValue();

         try {
            IntByReference var5 = new IntByReference();
            Pointer var6 = WindowsSpotifyReader.Combase.getFunction("WindowsGetStringRawBuffer").invokePointer(new Object[]{var4, var5});
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
            WindowsSpotifyReader.Combase.getFunction("WindowsDeleteString").invokeInt(new Object[]{var4});
         }
      }

      @Override
      public void close() {
         if (this.pointer != null) {
            this.invokeVtable(2);
            this.pointer = null;
         }
      }
   }

   private static final class Combase {
      private static final NativeLibrary library = NativeLibrary.getInstance("combase");

      static Function getFunction(String var0) {
         return library.getFunction(var0, 63);
      }
   }
}

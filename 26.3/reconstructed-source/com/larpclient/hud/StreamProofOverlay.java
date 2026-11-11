package com.larpclient.hud;

import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.WindowHandles;
import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HBITMAP;
import com.sun.jna.platform.win32.WinDef.HDC;
import com.sun.jna.platform.win32.WinDef.HINSTANCE;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinGDI.BITMAPINFO;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.platform.win32.WinUser.BLENDFUNCTION;
import com.sun.jna.platform.win32.WinUser.SIZE;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import org.lwjgl.system.MemoryUtil;

final class StreamProofOverlay {
   static final int affinityExcludeFromCapture = 17;
   static final int minWindowsBuild = 19041;
   private static final String windowClassName = "LarpStreamProofOverlay";
   private static final int wsPopup = Integer.MIN_VALUE;
   private static final int wsExLayered = 524288;
   private static final int wsExTransparent = 32;
   private static final int wsExToolWindow = 128;
   private static final int wsExNoActivate = 134217728;
   private static final int swpNoZOrder = 4;
   private static final int swpNoActivate = 16;
   private static final int swpNoOwnerZOrder = 512;
   private static final int swHide = 0;
   private static final int swShowNoActivate = 4;
   private static final int wmNcHitTest = 132;
   private static final int wmMouseActivate = 33;
   private static final int htTransparent = -1;
   private static final int maNoActivateAndEat = 3;
   private static final int ulwAlpha = 2;
   private final HWND gameWindow;
   private final HWND overlayWindow;
   private final HINSTANCE moduleInstance;
   private final WindowProc windowProc;
   private final boolean displayCaptureExcluded;
   private final Thread presentThread;
   private final AtomicReference<StreamProofOverlay.Inner> pendingFrame = new AtomicReference<>();
   private final AtomicReference<ByteBuffer> recycledBuffer = new AtomicReference<>();
   private volatile boolean running = (boolean)1;
   private volatile long presentedSequence = -1L;
   private volatile int targetWidth;
   private volatile int targetHeight;
   private volatile String workerError;
   private int lastX = Integer.MIN_VALUE;
   private int lastY;
   private int lastWidth;
   private int lastHeight;
   private boolean shown;

   private StreamProofOverlay(HWND var1, HWND var2, HINSTANCE var3, WindowProc var4, boolean var5) {
      this.gameWindow = var1;
      this.overlayWindow = var2;
      this.moduleInstance = var3;
      this.windowProc = var4;
      this.displayCaptureExcluded = var5;
      this.presentThread = new Thread(this::presentLoop, "Larp Stream Proof overlay");
      this.presentThread.setDaemon(true);
      this.presentThread.start();
   }

   static StreamProofOverlay create(long var0) {
      if (!Platform.isWindows()) {
         throw new IllegalStateException("Stream Proof needs Windows");
      }

      long var2 = WindowHandles.getWin32Hwnd(var0);
      if (var2 == 0L) {
         throw new IllegalStateException("glfwGetWin32Window returned no HWND for the game window");
      }

      HWND var4 = new HWND(new Pointer(var2));
      LarpLog.info("StreamProof", "Game window HWND=0x{}", Long.toHexString(var2));
      HMODULE var5 = Kernel32.INSTANCE.GetModuleHandle(null);
      WindowProc var6 = (var0x, var1, var2x, var3) -> {
         if (var1 == 132) {
            return new LRESULT(-1L);
         } else {
            return var1 == 33 ? new LRESULT(3L) : User32.INSTANCE.DefWindowProc(var0x, var1, var2x, var3);
         }
      };
      WNDCLASSEX var7 = new WNDCLASSEX();
      var7.hInstance = var5;
      var7.lpfnWndProc = var6;
      var7.lpszClassName = "LarpStreamProofOverlay";
      if (User32.INSTANCE.RegisterClassEx(var7) == null) {
         int var8 = Kernel32.INSTANCE.GetLastError();
         if (var8 != 1410) {
            throw new IllegalStateException("RegisterClassEx failed, error " + var8);
         }

         User32.INSTANCE.UnregisterClass("LarpStreamProofOverlay", var5);
         if (User32.INSTANCE.RegisterClassEx(var7) == null) {
            throw new IllegalStateException("RegisterClassEx retry failed, error " + Kernel32.INSTANCE.GetLastError());
         }
      }

      HWND var14 = User32.INSTANCE.CreateWindowEx(134742176, "LarpStreamProofOverlay", "", Integer.MIN_VALUE, 0, 0, 1, 1, var4, null, var5, null);
      if (var14 == null) {
         int var15 = Kernel32.INSTANCE.GetLastError();
         User32.INSTANCE.UnregisterClass("LarpStreamProofOverlay", var5);
         throw new IllegalStateException("CreateWindowEx failed, error " + var15);
      }

      byte var9 = 0;
      int var10 = getWindowsBuild();
      if (var10 >= 19041) {
         int var11 = user32Function("SetWindowDisplayAffinity").invokeInt(new Object[]{var14, 17});
         IntByReference var12 = new IntByReference();
         int var13 = user32Function("GetWindowDisplayAffinity").invokeInt(new Object[]{var14, var12});
         var9 = (byte)(var11 != 0 && var13 != 0 && var12.getValue() == 17 ? 1 : 0);
         if (var9 == 0) {
            LarpLog.warn(
               "StreamProof",
               "Display capture exclusion was refused (set={}, affinity={}, error={}); display capture will show the overlay",
               var11,
               var12.getValue(),
               Kernel32.INSTANCE.GetLastError()
            );
         }
      } else {
         LarpLog.warn(
            "StreamProof",
            "Windows build {} predates display capture exclusion (needs {}); game and window capture stay clean, display capture will show the overlay",
            var10,
            19041
         );
      }

      LarpLog.info("StreamProof", "Overlay window created; display capture exclusion={}", Boolean.valueOf((boolean)var9));
      return new StreamProofOverlay(var4, var14, var5, var6, (boolean)var9);
   }

   boolean isDisplayCaptureExcluded() {
      return this.displayCaptureExcluded;
   }

   String getWorkerError() {
      return this.workerError;
   }

   long getPresentedSequence() {
      return this.presentedSequence;
   }

   void updateVisibility(boolean var1, long var2, int var4, int var5) {
      if (var1 && !this.isMinimized()) {
         POINT var6 = new POINT(0, 0);
         user32Function("ClientToScreen").invokeInt(new Object[]{this.gameWindow, var6});
         var6.read();
         if (var6.x != this.lastX || var6.y != this.lastY || var4 != this.lastWidth || var5 != this.lastHeight) {
            this.lastX = var6.x;
            this.lastY = var6.y;
            this.lastWidth = var4;
            this.lastHeight = var5;
            User32.INSTANCE.SetWindowPos(this.overlayWindow, null, this.lastX, this.lastY, this.lastWidth, this.lastHeight, 532);
            this.targetWidth = this.lastWidth;
            this.targetHeight = this.lastHeight;
         }

         if (!this.shown && this.presentedSequence >= var2) {
            User32.INSTANCE.ShowWindow(this.overlayWindow, 4);
            this.shown = (boolean)1;
            LarpLog.info(
               "StreamProof", "Overlay shown at {},{} size {}x{}; hidden content now draws here", this.lastX, this.lastY, this.lastWidth, this.lastHeight
            );
         }
      } else if (this.shown) {
         User32.INSTANCE.ShowWindow(this.overlayWindow, 0);
         this.shown = (boolean)0;
      }
   }

   void submitFrame(long var1, int var3, int var4, ByteBuffer var5) {
      int var6 = var3 * var4 * 4;
      ByteBuffer var7 = this.recycledBuffer.getAndSet(null);
      if (var7 == null || var7.capacity() != var6) {
         var7 = ByteBuffer.allocateDirect(var6).order(ByteOrder.nativeOrder());
      }

      MemoryUtil.memCopy(MemoryUtil.memAddress(var5), MemoryUtil.memAddress(var7), var6);
      StreamProofOverlay.Inner var8 = this.pendingFrame.getAndSet(new StreamProofOverlay.Inner(var1, var3, var4, var7));
      if (var8 != null) {
         this.recycledBuffer.compareAndSet(null, var8.pixels());
      }

      LockSupport.unpark(this.presentThread);
   }

   private void presentLoop() {
      HDC var1 = null;
      HBITMAP var2 = null;
      HANDLE var3 = null;
      byte var4 = 0;
      Pointer var5 = null;
      int var6 = 0;
      int var7 = 0;
      BLENDFUNCTION var8 = new BLENDFUNCTION();
      var8.BlendOp = 0;
      var8.SourceConstantAlpha = -1;
      var8.AlphaFormat = 1;
      POINT var9 = new POINT(0, 0);

      try {
         var1 = GDI32.INSTANCE.CreateCompatibleDC(null);
         if (var1 == null) {
            throw new IllegalStateException("CreateCompatibleDC failed");
         }

         while (this.running) {
            StreamProofOverlay.Inner var10 = this.pendingFrame.getAndSet(null);
            if (var10 == null) {
               LockSupport.parkNanos(50000000L);
            } else {
               try {
                  if (var10.width() == this.targetWidth && var10.height() == this.targetHeight) {
                     if (var2 == null || var6 != var10.width() || var7 != var10.height()) {
                        if (var2 != null) {
                           GDI32.INSTANCE.SelectObject(var1, var3);
                           GDI32.INSTANCE.DeleteObject(var2);
                           var2 = null;
                        }

                        BITMAPINFO var11 = new BITMAPINFO();
                        var11.bmiHeader.biWidth = var10.width();
                        var11.bmiHeader.biHeight = -var10.height();
                        var11.bmiHeader.biPlanes = 1;
                        var11.bmiHeader.biBitCount = 32;
                        var11.bmiHeader.biCompression = 0;
                        PointerByReference var12 = new PointerByReference();
                        var2 = GDI32.INSTANCE.CreateDIBSection(var1, var11, 0, var12, null, 0);
                        if (var2 == null) {
                           throw new IllegalStateException("CreateDIBSection failed for " + var10.width() + "x" + var10.height());
                        }

                        var5 = var12.getValue();
                        var3 = GDI32.INSTANCE.SelectObject(var1, var2);
                        var6 = var10.width();
                        var7 = var10.height();
                     }

                     long var25 = (long)var10.width() * var10.height() * 4L;
                     MemoryUtil.memCopy(MemoryUtil.memAddress(var10.pixels()), Pointer.nativeValue(var5), var25);
                     SIZE var13 = new SIZE(var10.width(), var10.height());
                     if (User32.INSTANCE.UpdateLayeredWindow(this.overlayWindow, null, null, var13, var1, var9, 0, var8, 2)) {
                        this.presentedSequence = var10.sequence();
                     } else if (var4 == 0) {
                        var4 = 1;
                        LarpLog.warn(
                           "StreamProof", "UpdateLayeredWindow failed, error {}; the overlay keeps its previous frame", Kernel32.INSTANCE.GetLastError()
                        );
                     }
                  }
               } finally {
                  this.recycledBuffer.compareAndSet(null, var10.pixels());
               }
            }
         }
      } catch (Throwable var23) {
         this.workerError = var23.getClass().getSimpleName() + ": " + var23.getMessage();
         LarpLog.error("StreamProof", "Overlay presentation stopped; Stream Proof will fall back to normal drawing", var23);
      } finally {
         if (var2 != null) {
            if (var3 != null) {
               GDI32.INSTANCE.SelectObject(var1, var3);
            }

            GDI32.INSTANCE.DeleteObject(var2);
         }

         if (var1 != null) {
            GDI32.INSTANCE.DeleteDC(var1);
         }
      }
   }

   void close() {
      this.running = (boolean)0;
      LockSupport.unpark(this.presentThread);

      try {
         this.presentThread.join(500L);
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
      }

      if (this.presentThread.isAlive()) {
         LarpLog.warn("StreamProof", "Overlay worker did not stop within 500 ms; destroying the window anyway");
      }

      User32.INSTANCE.ShowWindow(this.overlayWindow, 0);
      User32.INSTANCE.DestroyWindow(this.overlayWindow);
      User32.INSTANCE.UnregisterClass("LarpStreamProofOverlay", this.moduleInstance);
      LarpLog.info("StreamProof", "Overlay window destroyed");
      Reference.reachabilityFence(this.windowProc);
   }

   private boolean isMinimized() {
      return user32Function("IsIconic").invokeInt(new Object[]{this.gameWindow}) != 0;
   }

   private static Function user32Function(String var0) {
      return Function.getFunction("user32", var0, 63);
   }

   static int getWindowsBuild() {
      try {
         Memory var0 = new Memory(284L);
         var0.clear();
         var0.setInt(0L, 284);
         int var1 = Function.getFunction("ntdll", "RtlGetVersion", 63).invokeInt(new Object[]{var0});
         return var1 == 0 ? var0.getInt(12L) : 0;
      } catch (RuntimeException | UnsatisfiedLinkError var2) {
         LarpLog.warn("StreamProof", "Windows build lookup failed: {}", var2.toString());
         return 0;
      }
   }

   private record Inner(long sequence, int width, int height, ByteBuffer pixels) {
   }
}

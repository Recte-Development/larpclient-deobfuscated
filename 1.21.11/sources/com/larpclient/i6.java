package com.larpclient;

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
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.system.MemoryUtil;

final class i6 {
   static final int N = 17;
   static final int Z = 19041;
   private static final String c = "LarpStreamProofOverlay";
   private static final int o = Integer.MIN_VALUE;
   private static final int I = 524288;
   private static final int s = 32;
   private static final int J = 128;
   private static final int n = 134217728;
   private static final int g = 4;
   private static final int a = 16;
   private static final int R = 512;
   private static final int M = 0;
   private static final int e = 4;
   private static final int U = 132;
   private static final int y = 33;
   private static final int t = -1;
   private static final int i = 3;
   private static final int T = 2;
   private final HWND F;
   private final HWND r;
   private final HINSTANCE S;
   private final WindowProc C;
   private final boolean D;
   private final Thread q;
   private final AtomicReference<i6.r> H = new AtomicReference<>();
   private final AtomicReference<ByteBuffer> A = new AtomicReference<>();
   private volatile boolean m = (boolean)1;
   private volatile long f = -1L;
   private volatile int x;
   private volatile int W;
   private volatile String E;
   private int w = Integer.MIN_VALUE;
   private int B;
   private int j;
   private int h;
   private boolean l;

   private i6(HWND var1, HWND var2, HINSTANCE var3, WindowProc var4, boolean var5) {
      this.F = var1;
      this.r = var2;
      this.S = var3;
      this.C = var4;
      this.D = var5;
      this.q = new Thread(this::U, "Larp Stream Proof overlay");
      this.q.setDaemon(true);
      this.q.start();
   }

   static i6 K(long var0) {
      if (!Platform.isWindows()) {
         throw new IllegalStateException("Stream Proof needs Windows");
      }

      long var2 = GLFWNativeWin32.glfwGetWin32Window(var0);
      if (var2 == 0L) {
         throw new IllegalStateException("glfwGetWin32Window returned no HWND for the game window");
      }

      HWND var4 = new HWND(new Pointer(var2));
      q8.G("StreamProof", "Game window HWND=0x{}", Long.toHexString(var2));
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
      int var10 = k();
      if (var10 >= 19041) {
         int var11 = V("SetWindowDisplayAffinity").invokeInt(new Object[]{var14, 17});
         IntByReference var12 = new IntByReference();
         int var13 = V("GetWindowDisplayAffinity").invokeInt(new Object[]{var14, var12});
         var9 = (byte)(var11 != 0 && var13 != 0 && var12.getValue() == 17 ? 1 : 0);
         if (var9 == 0) {
            q8.L(
               "StreamProof",
               "Display capture exclusion was refused (set={}, affinity={}, error={}); display capture will show the overlay",
               var11,
               var12.getValue(),
               Kernel32.INSTANCE.GetLastError()
            );
         }
      } else {
         q8.L(
            "StreamProof",
            "Windows build {} predates display capture exclusion (needs {}); game and window capture stay clean, display capture will show the overlay",
            var10,
            19041
         );
      }

      q8.G("StreamProof", "Overlay window created; display capture exclusion={}", Boolean.valueOf((boolean)var9));
      return new i6(var4, var14, var5, var6, (boolean)var9);
   }

   boolean z() {
      return this.D;
   }

   String y() {
      return this.E;
   }

   long N() {
      return this.f;
   }

   void V(boolean var1, long var2, int var4, int var5) {
      if (var1 && !this.j()) {
         POINT var6 = new POINT(0, 0);
         V("ClientToScreen").invokeInt(new Object[]{this.F, var6});
         var6.read();
         if (var6.x != this.w || var6.y != this.B || var4 != this.j || var5 != this.h) {
            this.w = var6.x;
            this.B = var6.y;
            this.j = var4;
            this.h = var5;
            User32.INSTANCE.SetWindowPos(this.r, null, this.w, this.B, this.j, this.h, 532);
            this.x = this.j;
            this.W = this.h;
         }

         if (!this.l && this.f >= var2) {
            User32.INSTANCE.ShowWindow(this.r, 4);
            this.l = (boolean)1;
            q8.G("StreamProof", "Overlay shown at {},{} size {}x{}; hidden content now draws here", this.w, this.B, this.j, this.h);
         }
      } else if (this.l) {
         User32.INSTANCE.ShowWindow(this.r, 0);
         this.l = (boolean)0;
      }
   }

   void L(long var1, int var3, int var4, ByteBuffer var5) {
      int var6 = var3 * var4 * 4;
      ByteBuffer var7 = this.A.getAndSet(null);
      if (var7 == null || var7.capacity() != var6) {
         var7 = ByteBuffer.allocateDirect(var6).order(ByteOrder.nativeOrder());
      }

      MemoryUtil.memCopy(MemoryUtil.memAddress(var5), MemoryUtil.memAddress(var7), var6);
      i6.r var8 = this.H.getAndSet(new i6.r(var1, var3, var4, var7));
      if (var8 != null) {
         this.A.compareAndSet(null, var8.pixels());
      }

      LockSupport.unpark(this.q);
   }

   private void U() {
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

         while (this.m) {
            i6.r var10 = this.H.getAndSet(null);
            if (var10 == null) {
               LockSupport.parkNanos(50000000L);
            } else {
               try {
                  if (var10.width() == this.x && var10.height() == this.W) {
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
                     if (User32.INSTANCE.UpdateLayeredWindow(this.r, null, null, var13, var1, var9, 0, var8, 2)) {
                        this.f = var10.sequence();
                     } else if (var4 == 0) {
                        var4 = 1;
                        q8.L("StreamProof", "UpdateLayeredWindow failed, error {}; the overlay keeps its previous frame", Kernel32.INSTANCE.GetLastError());
                     }
                  }
               } finally {
                  this.A.compareAndSet(null, var10.pixels());
               }
            }
         }
      } catch (Throwable var23) {
         this.E = var23.getClass().getSimpleName() + ": " + var23.getMessage();
         q8.u("StreamProof", "Overlay presentation stopped; Stream Proof will fall back to normal drawing", var23);
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

   void X() {
      this.m = (boolean)0;
      LockSupport.unpark(this.q);

      try {
         this.q.join(500L);
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
      }

      if (this.q.isAlive()) {
         q8.L("StreamProof", "Overlay worker did not stop within 500 ms; destroying the window anyway");
      }

      User32.INSTANCE.ShowWindow(this.r, 0);
      User32.INSTANCE.DestroyWindow(this.r);
      User32.INSTANCE.UnregisterClass("LarpStreamProofOverlay", this.S);
      q8.G("StreamProof", "Overlay window destroyed");
      Reference.reachabilityFence(this.C);
   }

   private boolean j() {
      return V("IsIconic").invokeInt(new Object[]{this.F}) != 0;
   }

   private static Function V(String var0) {
      return Function.getFunction("user32", var0, 63);
   }

   static int k() {
      try {
         Memory var0 = new Memory(284L);
         var0.clear();
         var0.setInt(0L, 284);
         int var1 = Function.getFunction("ntdll", "RtlGetVersion", 63).invokeInt(new Object[]{var0});
         return var1 == 0 ? var0.getInt(12L) : 0;
      } catch (RuntimeException | UnsatisfiedLinkError var2) {
         q8.L("StreamProof", "Windows build lookup failed: {}", var2.toString());
         return 0;
      }
   }

   private record r(long sequence, int width, int height, ByteBuffer pixels) {
   }
}

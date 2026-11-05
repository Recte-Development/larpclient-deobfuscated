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
import org.lwjgl.system.MemoryUtil;

final class ua {
   static final int s = 17;
   static final int L = 19041;
   private static final String S = "LarpStreamProofOverlay";
   private static final int I = Integer.MIN_VALUE;
   private static final int D = 524288;
   private static final int Q = 32;
   private static final int R = 128;
   private static final int z = 134217728;
   private static final int A = 4;
   private static final int j = 16;
   private static final int T = 512;
   private static final int G = 0;
   private static final int M = 4;
   private static final int F = 132;
   private static final int x = 33;
   private static final int a = -1;
   private static final int d = 3;
   private static final int O = 2;
   private final HWND y;
   private final HWND f;
   private final HINSTANCE k;
   private final WindowProc v;
   private final boolean B;
   private final Thread N;
   private final AtomicReference<ua.y> q = new AtomicReference<>();
   private final AtomicReference<ByteBuffer> E = new AtomicReference<>();
   private volatile boolean p = (boolean)1;
   private volatile long l = -1L;
   private volatile int Y;
   private volatile int C;
   private volatile String K;
   private int V = Integer.MIN_VALUE;
   private int H;
   private int P;
   private int i;
   private boolean b;

   private ua(HWND var1, HWND var2, HINSTANCE var3, WindowProc var4, boolean var5) {
      this.y = var1;
      this.f = var2;
      this.k = var3;
      this.v = var4;
      this.B = var5;
      this.N = new Thread(this::k, "Larp Stream Proof overlay");
      this.N.setDaemon(true);
      this.N.start();
   }

   static ua F(long var0) {
      if (!Platform.isWindows()) {
         throw new IllegalStateException("Stream Proof needs Windows");
      }

      long var2 = jl.m(var0);
      if (var2 == 0L) {
         throw new IllegalStateException("glfwGetWin32Window returned no HWND for the game window");
      }

      HWND var4 = new HWND(new Pointer(var2));
      r4.V("StreamProof", "Game window HWND=0x{}", Long.toHexString(var2));
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
      int var10 = y();
      if (var10 >= 19041) {
         int var11 = m("SetWindowDisplayAffinity").invokeInt(new Object[]{var14, 17});
         IntByReference var12 = new IntByReference();
         int var13 = m("GetWindowDisplayAffinity").invokeInt(new Object[]{var14, var12});
         var9 = (byte)(var11 != 0 && var13 != 0 && var12.getValue() == 17 ? 1 : 0);
         if (var9 == 0) {
            r4.m(
               "StreamProof",
               "Display capture exclusion was refused (set={}, affinity={}, error={}); display capture will show the overlay",
               var11,
               var12.getValue(),
               Kernel32.INSTANCE.GetLastError()
            );
         }
      } else {
         r4.m(
            "StreamProof",
            "Windows build {} predates display capture exclusion (needs {}); game and window capture stay clean, display capture will show the overlay",
            var10,
            19041
         );
      }

      r4.V("StreamProof", "Overlay window created; display capture exclusion={}", Boolean.valueOf((boolean)var9));
      return new ua(var4, var14, var5, var6, (boolean)var9);
   }

   boolean d() {
      return this.B;
   }

   String D() {
      return this.K;
   }

   long v() {
      return this.l;
   }

   void w(boolean var1, long var2, int var4, int var5) {
      if (var1 && !this.u()) {
         POINT var6 = new POINT(0, 0);
         m("ClientToScreen").invokeInt(new Object[]{this.y, var6});
         var6.read();
         if (var6.x != this.V || var6.y != this.H || var4 != this.P || var5 != this.i) {
            this.V = var6.x;
            this.H = var6.y;
            this.P = var4;
            this.i = var5;
            User32.INSTANCE.SetWindowPos(this.f, null, this.V, this.H, this.P, this.i, 532);
            this.Y = this.P;
            this.C = this.i;
         }

         if (!this.b && this.l >= var2) {
            User32.INSTANCE.ShowWindow(this.f, 4);
            this.b = (boolean)1;
            r4.V("StreamProof", "Overlay shown at {},{} size {}x{}; hidden content now draws here", this.V, this.H, this.P, this.i);
         }
      } else if (this.b) {
         User32.INSTANCE.ShowWindow(this.f, 0);
         this.b = (boolean)0;
      }
   }

   void Y(long var1, int var3, int var4, ByteBuffer var5) {
      int var6 = var3 * var4 * 4;
      ByteBuffer var7 = this.E.getAndSet(null);
      if (var7 == null || var7.capacity() != var6) {
         var7 = ByteBuffer.allocateDirect(var6).order(ByteOrder.nativeOrder());
      }

      MemoryUtil.memCopy(MemoryUtil.memAddress(var5), MemoryUtil.memAddress(var7), var6);
      ua.y var8 = this.q.getAndSet(new ua.y(var1, var3, var4, var7));
      if (var8 != null) {
         this.E.compareAndSet(null, var8.pixels());
      }

      LockSupport.unpark(this.N);
   }

   private void k() {
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

         while (this.p) {
            ua.y var10 = this.q.getAndSet(null);
            if (var10 == null) {
               LockSupport.parkNanos(50000000L);
            } else {
               try {
                  if (var10.width() == this.Y && var10.height() == this.C) {
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
                     if (User32.INSTANCE.UpdateLayeredWindow(this.f, null, null, var13, var1, var9, 0, var8, 2)) {
                        this.l = var10.sequence();
                     } else if (var4 == 0) {
                        var4 = 1;
                        r4.m("StreamProof", "UpdateLayeredWindow failed, error {}; the overlay keeps its previous frame", Kernel32.INSTANCE.GetLastError());
                     }
                  }
               } finally {
                  this.E.compareAndSet(null, var10.pixels());
               }
            }
         }
      } catch (Throwable var23) {
         this.K = var23.getClass().getSimpleName() + ": " + var23.getMessage();
         r4.r("StreamProof", "Overlay presentation stopped; Stream Proof will fall back to normal drawing", var23);
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

   void y() {
      this.p = (boolean)0;
      LockSupport.unpark(this.N);

      try {
         this.N.join(500L);
      } catch (InterruptedException var2) {
         Thread.currentThread().interrupt();
      }

      if (this.N.isAlive()) {
         r4.m("StreamProof", "Overlay worker did not stop within 500 ms; destroying the window anyway");
      }

      User32.INSTANCE.ShowWindow(this.f, 0);
      User32.INSTANCE.DestroyWindow(this.f);
      User32.INSTANCE.UnregisterClass("LarpStreamProofOverlay", this.k);
      r4.V("StreamProof", "Overlay window destroyed");
      Reference.reachabilityFence(this.v);
   }

   private boolean u() {
      return m("IsIconic").invokeInt(new Object[]{this.y}) != 0;
   }

   private static Function m(String var0) {
      return Function.getFunction("user32", var0, 63);
   }

   static int y() {
      try {
         Memory var0 = new Memory(284L);
         var0.clear();
         var0.setInt(0L, 284);
         int var1 = Function.getFunction("ntdll", "RtlGetVersion", 63).invokeInt(new Object[]{var0});
         return var1 == 0 ? var0.getInt(12L) : 0;
      } catch (RuntimeException | UnsatisfiedLinkError var2) {
         r4.m("StreamProof", "Windows build lookup failed: {}", var2.toString());
         return 0;
      }
   }

   private record y(long sequence, int width, int height, ByteBuffer pixels) {
   }
}

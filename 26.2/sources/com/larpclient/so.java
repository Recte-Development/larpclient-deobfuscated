package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.Minecraft;

public final class so {
   private static final mw f = new mw();
   private static GpuTexture n;
   private static GpuTexture T;
   private static GpuTextureView z;
   private static GpuTextureView G;
   private static GpuTexture V;
   private static GpuTexture H;
   private static GpuTexture i;
   private static GpuTextureView Y;
   private static boolean I;
   private static boolean S;
   private static boolean l;
   private static boolean U;
   private static boolean j;
   private static boolean X;
   private static int O = -1;
   private static final long M = 3000000000L;
   private static long g;
   private static long o;

   public static void W() {
      if (O != wa.s) {
         O = wa.s;
         X = (boolean)0;
         U = (boolean)0;
         l = (boolean)0;

         try {
            b();
         } catch (RuntimeException var1) {
            X = (boolean)1;
            U = (boolean)1;
            l = (boolean)1;
            h.m("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      f.h();
      j = (boolean)0;
      S = (boolean)0;
      I = (boolean)0;
   }

   public static void k() {
      if (!X) {
         try {
            GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
            if (var0 == null) {
               return;
            }

            if (!I(i, var0)) {
               D();
               i = m("Larp depth before translucent terrain", var0);
               Y = RenderSystem.getDevice().createTextureView(i);
            }

            p(var0, i);
            j = (boolean)1;
            zj.K("Scene depth copies", 1L);
         } catch (RuntimeException var3) {
            X = (boolean)1;

            try {
               D();
            } catch (RuntimeException var2) {
               var3.addSuppressed(var2);
            }

            h.m("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var3);
         }
      }
   }

   public static GpuTextureView N() {
      return j ? Y : null;
   }

   public static void K() {
      f.L();
   }

   public static void v() {
      f.g();
   }

   public static GpuTextureView K() {
      I = (boolean)1;
      if (l) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTexture();
         if (var0 == null) {
            return null;
         }

         if (!I(n, var0)) {
            L();
            n = m("Larp shared scene color", var0);
            z = RenderSystem.getDevice().createTextureView(n);
            f.L();
         }

         if (var0 != V) {
            V = var0;
            f.L();
         }

         if (!f.A()) {
            p(var0, n);
            f.u();
            zj.K("Scene color copies", 1L);
         }

         return z;
      } catch (RuntimeException var3) {
         l = (boolean)1;

         try {
            L();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         h.m("Scene resources", "Color capture failed; geometric materials retained until resource reload", var3);
         return null;
      }
   }

   public static GpuTextureView v() {
      S = (boolean)1;
      if (U) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
         if (var0 == null) {
            return null;
         }

         if (!I(T, var0)) {
            f();
            T = m("Larp shared scene depth", var0);
            G = RenderSystem.getDevice().createTextureView(T);
            f.g();
         }

         if (var0 != H) {
            H = var0;
            f.g();
         }

         if (!f.d()) {
            p(var0, T);
            f.M();
            zj.K("Scene depth copies", 1L);
         }

         return G;
      } catch (RuntimeException var3) {
         U = (boolean)1;

         try {
            f();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         h.m("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var3);
         return null;
      }
   }

   private static boolean I(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture m(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      h.U("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), A(var2));
      return var2;
   }

   private static void p(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      }

      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
   }

   private static long A(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * var0.getHeight(0) * var0.getFormat().blockSize();
   }

   public static long C() {
      return A(n) + A(T) + A(i);
   }

   public static void V() {
      long var0 = n == null && T == null ? 0L : System.nanoTime();
      if (I) {
         g = var0;
      }

      if (S) {
         o = var0;
      }

      try {
         if (!I && var0 - g > 3000000000L) {
            L();
         }
      } finally {
         try {
            if (!S && var0 - o > 3000000000L) {
               f();
            }
         } finally {
            if (!j) {
               D();
            }
         }
      }

      zj.s("Scene attachment bytes (estimate)", C());
   }

   private static void L() {
      GpuTextureView var0 = z;
      GpuTexture var1 = n;
      z = null;
      n = null;
      V = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      f.L();
   }

   private static void f() {
      GpuTextureView var0 = G;
      GpuTexture var1 = T;
      G = null;
      T = null;
      H = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      f.g();
   }

   private static void D() {
      GpuTextureView var0 = Y;
      GpuTexture var1 = i;
      Y = null;
      i = null;
      j = (boolean)0;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }
   }

   public static void b() {
      try {
         L();
      } finally {
         try {
            f();
         } finally {
            D();
            f.E();
         }
      }
   }

   private so() {
   }
}

package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;

public final class x1 {
   private static final m_ W = new m_();
   private static GpuTexture y;
   private static GpuTexture N;
   private static GpuTextureView Q;
   private static GpuTextureView q;
   private static GpuTexture O;
   private static GpuTexture L;
   private static GpuTexture x;
   private static GpuTextureView G;
   private static boolean H;
   private static boolean i;
   private static boolean g;
   private static boolean M;
   private static boolean A;
   private static boolean t;
   private static int U = -1;

   public static void X() {
      if (U != jk.v) {
         U = jk.v;
         t = (boolean)0;
         M = (boolean)0;
         g = (boolean)0;

         try {
            k();
         } catch (RuntimeException var1) {
            t = (boolean)1;
            M = (boolean)1;
            g = (boolean)1;
            r4.r("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      W.w();
      A = (boolean)0;
      i = (boolean)0;
      H = (boolean)0;
   }

   public static void t() {
      if (!t) {
         try {
            GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
            if (var0 == null) {
               return;
            }

            if (!T(x, var0)) {
               i();
               x = p("Larp depth before translucent terrain", var0);
               G = RenderSystem.getDevice().createTextureView(x);
            }

            s(var0, x);
            A = (boolean)1;
            h_.A("Scene depth copies", 1L);
         } catch (RuntimeException var3) {
            t = (boolean)1;

            try {
               i();
            } catch (RuntimeException var2) {
               var3.addSuppressed(var2);
            }

            r4.r("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var3);
         }
      }
   }

   public static GpuTextureView G() {
      return A ? G : null;
   }

   public static void N() {
      W.F();
   }

   public static void p() {
      W.m();
   }

   public static GpuTextureView x() {
      H = (boolean)1;
      if (g) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTexture();
         if (var0 == null) {
            return null;
         }

         if (!T(y, var0)) {
            Z();
            y = p("Larp shared scene color", var0);
            Q = RenderSystem.getDevice().createTextureView(y);
            W.F();
         }

         if (var0 != O) {
            O = var0;
            W.F();
         }

         if (!W.Y()) {
            s(var0, y);
            W.E();
            h_.A("Scene color copies", 1L);
         }

         return Q;
      } catch (RuntimeException var3) {
         g = (boolean)1;

         try {
            Z();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         r4.r("Scene resources", "Color capture failed; geometric materials retained until resource reload", var3);
         return null;
      }
   }

   public static GpuTextureView z() {
      i = (boolean)1;
      if (M) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
         if (var0 == null) {
            return null;
         }

         if (!T(N, var0)) {
            g();
            N = p("Larp shared scene depth", var0);
            q = RenderSystem.getDevice().createTextureView(N);
            W.m();
         }

         if (var0 != L) {
            L = var0;
            W.m();
         }

         if (!W.N()) {
            s(var0, N);
            W.l();
            h_.A("Scene depth copies", 1L);
         }

         return q;
      } catch (RuntimeException var3) {
         M = (boolean)1;

         try {
            g();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         r4.r("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var3);
         return null;
      }
   }

   private static boolean T(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture p(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      r4.g("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), w(var2));
      return var2;
   }

   private static void s(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      }

      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
   }

   private static long w(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * var0.getHeight(0) * var0.getFormat().blockSize();
   }

   public static long A() {
      return w(y) + w(N) + w(x);
   }

   public static void V() {
      try {
         if (!H) {
            Z();
         }
      } finally {
         try {
            if (!i) {
               g();
            }
         } finally {
            if (!A) {
               i();
            }
         }
      }

      h_.S("Scene attachment bytes (estimate)", A());
   }

   private static void Z() {
      GpuTextureView var0 = Q;
      GpuTexture var1 = y;
      Q = null;
      y = null;
      O = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      W.F();
   }

   private static void g() {
      GpuTextureView var0 = q;
      GpuTexture var1 = N;
      q = null;
      N = null;
      L = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      W.m();
   }

   private static void i() {
      GpuTextureView var0 = G;
      GpuTexture var1 = x;
      G = null;
      x = null;
      A = (boolean)0;

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

   public static void k() {
      try {
         Z();
      } finally {
         try {
            g();
         } finally {
            i();
            W.i();
         }
      }
   }

   private x1() {
   }
}

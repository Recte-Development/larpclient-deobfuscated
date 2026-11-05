package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;

public final class ex {
   private static final kw N = new kw();
   private static GpuTexture W;
   private static GpuTexture x;
   private static GpuTextureView j;
   private static GpuTextureView w;
   private static GpuTexture h;
   private static GpuTexture G;
   private static GpuTexture k;
   private static GpuTextureView i;
   private static boolean y;
   private static boolean f;
   private static boolean d;
   private static boolean s;
   private static boolean Q;
   private static boolean o;
   private static int n = -1;

   public static void L() {
      if (n != m5.k) {
         n = m5.k;
         o = (boolean)0;
         s = (boolean)0;
         d = (boolean)0;

         try {
            x();
         } catch (RuntimeException var1) {
            o = (boolean)1;
            s = (boolean)1;
            d = (boolean)1;
            mt.P("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      N.y();
      Q = (boolean)0;
      f = (boolean)0;
      y = (boolean)0;
   }

   public static void s() {
      if (!o) {
         try {
            GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
            if (var0 == null) {
               return;
            }

            if (!J(k, var0)) {
               k();
               k = S("Larp depth before translucent terrain", var0);
               i = RenderSystem.getDevice().createTextureView(k);
            }

            U(var0, k);
            Q = (boolean)1;
            kx.U("Scene depth copies", 1L);
         } catch (RuntimeException var3) {
            o = (boolean)1;

            try {
               k();
            } catch (RuntimeException var2) {
               var3.addSuppressed(var2);
            }

            mt.P("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var3);
         }
      }
   }

   public static GpuTextureView t() {
      return Q ? i : null;
   }

   public static void w() {
      N.F();
   }

   public static void n() {
      N.S();
   }

   public static GpuTextureView n() {
      y = (boolean)1;
      if (d) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTexture();
         if (var0 == null) {
            return null;
         }

         if (!J(W, var0)) {
            D();
            W = S("Larp shared scene color", var0);
            j = RenderSystem.getDevice().createTextureView(W);
            N.F();
         }

         if (var0 != h) {
            h = var0;
            N.F();
         }

         if (!N.F()) {
            U(var0, W);
            N.j();
            kx.U("Scene color copies", 1L);
         }

         return j;
      } catch (RuntimeException var3) {
         d = (boolean)1;

         try {
            D();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         mt.P("Scene resources", "Color capture failed; geometric materials retained until resource reload", var3);
         return null;
      }
   }

   public static GpuTextureView X() {
      f = (boolean)1;
      if (s) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
         if (var0 == null) {
            return null;
         }

         if (!J(x, var0)) {
            q();
            x = S("Larp shared scene depth", var0);
            w = RenderSystem.getDevice().createTextureView(x);
            N.S();
         }

         if (var0 != G) {
            G = var0;
            N.S();
         }

         if (!N.A()) {
            U(var0, x);
            N.N();
            kx.U("Scene depth copies", 1L);
         }

         return w;
      } catch (RuntimeException var3) {
         s = (boolean)1;

         try {
            q();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         mt.P("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var3);
         return null;
      }
   }

   private static boolean J(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture S(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      mt.U("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), e(var2));
      return var2;
   }

   private static void U(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      }

      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
   }

   private static long e(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * var0.getHeight(0) * var0.getFormat().blockSize();
   }

   public static long v() {
      return e(W) + e(x) + e(k);
   }

   public static void h() {
      try {
         if (!y) {
            D();
         }
      } finally {
         try {
            if (!f) {
               q();
            }
         } finally {
            if (!Q) {
               k();
            }
         }
      }

      kx.s("Scene attachment bytes (estimate)", v());
   }

   private static void D() {
      GpuTextureView var0 = j;
      GpuTexture var1 = W;
      j = null;
      W = null;
      h = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      N.F();
   }

   private static void q() {
      GpuTextureView var0 = w;
      GpuTexture var1 = x;
      w = null;
      x = null;
      G = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      N.S();
   }

   private static void k() {
      GpuTextureView var0 = i;
      GpuTexture var1 = k;
      i = null;
      k = null;
      Q = (boolean)0;

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

   public static void x() {
      try {
         D();
      } finally {
         try {
            q();
         } finally {
            k();
            N.V();
         }
      }
   }

   private ex() {
   }
}

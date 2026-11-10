package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;

public final class _s {
   private static final ff r = new ff();
   private static GpuTexture G;
   private static GpuTexture E;
   private static GpuTextureView k;
   private static GpuTextureView M;
   private static GpuTexture U;
   private static GpuTexture l;
   private static GpuTexture P;
   private static GpuTextureView w;
   private static boolean Z;
   private static boolean b;
   private static boolean h;
   private static boolean a;
   private static boolean R;
   private static boolean A;
   private static int J = -1;
   private static final long j = 3000000000L;
   private static long u;
   private static long I;

   public static void v() {
      if (J != nl.q) {
         J = nl.q;
         A = (boolean)0;
         a = (boolean)0;
         h = (boolean)0;

         try {
            m();
         } catch (RuntimeException var1) {
            A = (boolean)1;
            a = (boolean)1;
            h = (boolean)1;
            _q.o("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      r.p();
      R = (boolean)0;
      b = (boolean)0;
      Z = (boolean)0;
   }

   public static void b() {
      if (!A) {
         try {
            GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
            if (var0 == null) {
               return;
            }

            if (!R(P, var0)) {
               H();
               P = I("Larp depth before translucent terrain", var0);
               w = RenderSystem.getDevice().createTextureView(P);
            }

            S(var0, P);
            R = (boolean)1;
            he.Z("Scene depth copies", 1L);
         } catch (RuntimeException var3) {
            A = (boolean)1;

            try {
               H();
            } catch (RuntimeException var2) {
               var3.addSuppressed(var2);
            }

            _q.o("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var3);
         }
      }
   }

   public static GpuTextureView v() {
      return R ? w : null;
   }

   public static void g() {
      r.u();
   }

   public static void i() {
      r.H();
   }

   public static GpuTextureView N() {
      Z = (boolean)1;
      if (h) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTexture();
         if (var0 == null) {
            return null;
         }

         if (!R(G, var0)) {
            E();
            G = I("Larp shared scene color", var0);
            k = RenderSystem.getDevice().createTextureView(G);
            r.u();
         }

         if (var0 != U) {
            U = var0;
            r.u();
         }

         if (!r.K()) {
            S(var0, G);
            r.J();
            he.Z("Scene color copies", 1L);
         }

         return k;
      } catch (RuntimeException var3) {
         h = (boolean)1;

         try {
            E();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         _q.o("Scene resources", "Color capture failed; geometric materials retained until resource reload", var3);
         return null;
      }
   }

   public static GpuTextureView a() {
      b = (boolean)1;
      if (a) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
         if (var0 == null) {
            return null;
         }

         if (!R(E, var0)) {
            d();
            E = I("Larp shared scene depth", var0);
            M = RenderSystem.getDevice().createTextureView(E);
            r.H();
         }

         if (var0 != l) {
            l = var0;
            r.H();
         }

         if (!r.a()) {
            S(var0, E);
            r.W();
            he.Z("Scene depth copies", 1L);
         }

         return M;
      } catch (RuntimeException var3) {
         a = (boolean)1;

         try {
            d();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         _q.o("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var3);
         return null;
      }
   }

   private static boolean R(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture I(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      _q.c("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), e(var2));
      return var2;
   }

   private static void S(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      }

      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
   }

   private static long e(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * var0.getHeight(0) * var0.getFormat().blockSize();
   }

   public static long x() {
      return e(G) + e(E) + e(P);
   }

   public static void X() {
      long var0 = G == null && E == null ? 0L : System.nanoTime();
      if (Z) {
         u = var0;
      }

      if (b) {
         I = var0;
      }

      try {
         if (!Z && var0 - u > 3000000000L) {
            E();
         }
      } finally {
         try {
            if (!b && var0 - I > 3000000000L) {
               d();
            }
         } finally {
            if (!R) {
               H();
            }
         }
      }

      he.z("Scene attachment bytes (estimate)", x());
   }

   private static void E() {
      GpuTextureView var0 = k;
      GpuTexture var1 = G;
      k = null;
      G = null;
      U = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      r.u();
   }

   private static void d() {
      GpuTextureView var0 = M;
      GpuTexture var1 = E;
      M = null;
      E = null;
      l = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      r.H();
   }

   private static void H() {
      GpuTextureView var0 = w;
      GpuTexture var1 = P;
      w = null;
      P = null;
      R = (boolean)0;

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

   public static void m() {
      try {
         E();
      } finally {
         try {
            d();
         } finally {
            H();
            r.r();
         }
      }
   }

   private _s() {
   }
}

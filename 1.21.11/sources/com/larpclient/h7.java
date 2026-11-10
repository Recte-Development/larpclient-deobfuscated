package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.class_310;

public final class h7 {
   private static final gp d = new gp();
   private static GpuTexture E;
   private static GpuTexture F;
   private static GpuTextureView S;
   private static GpuTextureView D;
   private static GpuTexture R;
   private static GpuTexture q;
   private static GpuTexture W;
   private static GpuTextureView C;
   private static boolean X;
   private static boolean s;
   private static boolean i;
   private static boolean O;
   private static boolean x;
   private static boolean c;
   private static int a = -1;
   private static final long T = 3000000000L;
   private static long u;
   private static long V;

   public static void x() {
      if (a != t_.g) {
         a = t_.g;
         c = (boolean)0;
         O = (boolean)0;
         i = (boolean)0;

         try {
            Q();
         } catch (RuntimeException var1) {
            c = (boolean)1;
            O = (boolean)1;
            i = (boolean)1;
            q8.u("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      d.p();
      x = (boolean)0;
      s = (boolean)0;
      X = (boolean)0;
   }

   public static void S() {
      if (!c) {
         try {
            GpuTexture var0 = class_310.method_1551().method_1522().method_30278();
            if (var0 == null) {
               return;
            }

            if (!B(W, var0)) {
               V();
               W = y("Larp depth before translucent terrain", var0);
               C = RenderSystem.getDevice().createTextureView(W);
            }

            p(var0, W);
            x = (boolean)1;
            c5.V("Scene depth copies", 1L);
         } catch (RuntimeException var3) {
            c = (boolean)1;

            try {
               V();
            } catch (RuntimeException var2) {
               var3.addSuppressed(var2);
            }

            q8.u("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var3);
         }
      }
   }

   public static GpuTextureView K() {
      return x ? C : null;
   }

   public static void G() {
      d.N();
   }

   public static void c() {
      d.Q();
   }

   public static GpuTextureView I() {
      X = (boolean)1;
      if (i) {
         return null;
      }

      try {
         GpuTexture var0 = class_310.method_1551().method_1522().method_30277();
         if (var0 == null) {
            return null;
         }

         if (!B(E, var0)) {
            n();
            E = y("Larp shared scene color", var0);
            S = RenderSystem.getDevice().createTextureView(E);
            d.N();
         }

         if (var0 != R) {
            R = var0;
            d.N();
         }

         if (!d.p()) {
            p(var0, E);
            d.H();
            c5.V("Scene color copies", 1L);
         }

         return S;
      } catch (RuntimeException var3) {
         i = (boolean)1;

         try {
            n();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         q8.u("Scene resources", "Color capture failed; geometric materials retained until resource reload", var3);
         return null;
      }
   }

   public static GpuTextureView J() {
      s = (boolean)1;
      if (O) {
         return null;
      }

      try {
         GpuTexture var0 = class_310.method_1551().method_1522().method_30278();
         if (var0 == null) {
            return null;
         }

         if (!B(F, var0)) {
            X();
            F = y("Larp shared scene depth", var0);
            D = RenderSystem.getDevice().createTextureView(F);
            d.Q();
         }

         if (var0 != q) {
            q = var0;
            d.Q();
         }

         if (!d.Q()) {
            p(var0, F);
            d.L();
            c5.V("Scene depth copies", 1L);
         }

         return D;
      } catch (RuntimeException var3) {
         O = (boolean)1;

         try {
            X();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         q8.u("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var3);
         return null;
      }
   }

   private static boolean B(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture y(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      q8.v("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), n(var2));
      return var2;
   }

   private static void p(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      }

      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
   }

   private static long n(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * var0.getHeight(0) * var0.getFormat().pixelSize();
   }

   public static long d() {
      return n(E) + n(F) + n(W);
   }

   public static void d() {
      long var0 = E == null && F == null ? 0L : System.nanoTime();
      if (X) {
         u = var0;
      }

      if (s) {
         V = var0;
      }

      try {
         if (!X && var0 - u > 3000000000L) {
            n();
         }
      } finally {
         try {
            if (!s && var0 - V > 3000000000L) {
               X();
            }
         } finally {
            if (!x) {
               V();
            }
         }
      }

      c5.L("Scene attachment bytes (estimate)", d());
   }

   private static void n() {
      GpuTextureView var0 = S;
      GpuTexture var1 = E;
      S = null;
      E = null;
      R = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      d.N();
   }

   private static void X() {
      GpuTextureView var0 = D;
      GpuTexture var1 = F;
      D = null;
      F = null;
      q = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      d.Q();
   }

   private static void V() {
      GpuTextureView var0 = C;
      GpuTexture var1 = W;
      C = null;
      W = null;
      x = (boolean)0;

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

   public static void Q() {
      try {
         n();
      } finally {
         try {
            X();
         } finally {
            V();
            d.D();
         }
      }
   }

   private h7() {
   }
}

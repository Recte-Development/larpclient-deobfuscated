package com.larpclient;

import com.larpclient.module.kx;
import java.util.Locale;
import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class _b {
   public static final double p = 180.0;
   public static final float S = 0.55F;
   public static final float N = 0.8F;
   public static final float Q = 1.0F;
   public static final float Y = 30.0F;
   public static final float V = 0.5F;
   public static final float m = 0.15F;
   private static final double s = 0.3;
   private static final float j = 6.0F;
   private static final int t = 16;
   private final SplittableRandom U;
   private final com.larpclient.module.f0 o;
   private final kx L = new kx();
   private float c;
   private float b;
   private float Z;
   private boolean J;
   private final StringBuilder D = new StringBuilder();
   private int W;
   private float n;
   private float r;

   public _b() {
      this(new SplittableRandom());
   }

   public _b(SplittableRandom var1) {
      this.U = var1;
      this.o = new com.larpclient.module.f0(com.larpclient.module.f0.Y, var1.split());
   }

   public float[] i(float var1, float var2, float var3, float var4, double var5, boolean var7) {
      return this.e(var1, var2, var3, var4, var5, var7, com.larpclient.module.w.d());
   }

   public float[] e(float var1, float var2, float var3, float var4, double var5, boolean var7, float var8) {
      float var9 = Mth.clamp(var4, -90.0F, 90.0F);
      float[] var10 = var7 ? this.B(var1, var2, var3, var9, (float)Math.clamp(var5, 1.0, 180.0), var8) : F(var1, var2, var3, var9, var5);
      this.p(var1, var2, var10);
      return var10;
   }

   public static float[] F(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.clamp(var3, -90.0F, 90.0F);
      return var4 >= 180.0 ? new float[]{var0 + Mth.wrapDegrees(var2 - var0), var6} : nx.M(var0, var1, var2, var6, var4);
   }

   private float[] B(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (var9 < 1.0E-4F) {
         this.c = 0.0F;
         this.J = (boolean)0;
         return new float[]{var1, var2};
      }

      if (this.c == 0.0F && !this.J && var9 > 30.0F && this.U.nextDouble() < 0.3) {
         float var10 = (float)(0.02 + this.U.nextDouble() * 0.04);
         this.b = var7 * var10;
         this.Z = var8 * var10;
         this.J = (boolean)1;
      }

      if (this.J) {
         float var18 = var7 + this.b;
         float var11 = Mth.clamp(var4 + this.Z, -90.0F, 90.0F) - var2;
         float var12 = (float)Math.hypot(var18, var11);
         if (var12 <= 1.5F) {
            this.J = (boolean)0;
         } else {
            var7 = var18;
            var8 = var11;
            var9 = var12;
         }
      }

      int var19 = var9 < 6.0F ? 1 : 0;
      this.o.U();
      float var20 = this.o.c(var9, (boolean)var19);
      float var21 = this.o.E(com.larpclient.module.f0.p.g, (boolean)var19) + this.o.h(com.larpclient.module.f0.p.g) * 0.05F;
      float var13 = this.o.E(com.larpclient.module.f0.p.K, (boolean)var19) + this.o.h(com.larpclient.module.f0.p.K) * 0.05F;
      float var14;
      float var15;
      if (var9 <= 1.0F) {
         var14 = var7;
         var15 = var8;
      } else {
         float var16 = Math.min(var5, this.c + var5 * 0.55F);
         var16 = Math.min(var16, Math.max(1.0F, var9 * 0.8F)) * var20;
         var14 = var7 / var9 * var16 * var21;
         var15 = var8 / var9 * var16 * var13;
         if (var19 == 0) {
            var15 += this.o.a(var14);
         }

         var14 = T(var14, var7);
         var15 = T(var15, var8);
         float var17 = (float)Math.hypot(var14, var15);
         if (var17 > var5) {
            var14 *= var5 / var17;
            var15 *= var5 / var17;
         }
      }

      com.larpclient.module.c var25 = this.L
         .j(new com.larpclient.module.c(var1, var2), new com.larpclient.module.c(var1 + var14, Mth.clamp(var2 + var15, -90.0F, 90.0F)), var6);
      this.c = (float)Math.hypot(var25.V - var1, var25.T - var2);
      return new float[]{var25.V, var25.T};
   }

   private static float T(float var0, float var1) {
      return var1 >= 0.0F ? Mth.clamp(var0, -0.5F, var1) : Mth.clamp(var0, var1, 0.5F);
   }

   public static boolean Z(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Math.max(0.15F, var4);
      return Math.abs(Mth.wrapDegrees(var2 - var0)) <= var5 && Math.abs(var3 - var1) <= var5;
   }

   public static float M(boolean var0, float var1, float var2) {
      return var0 ? Math.max(var1, var2 * 0.6F) : var1;
   }

   public void u() {
      this.o.G();
      this.L.L();
      this.c = this.b = this.Z = 0.0F;
      this.J = (boolean)0;
   }

   public void T() {
      this.D.setLength(0);
      this.W = 0;
      this.n = this.r = 0.0F;
   }

   public int r() {
      return this.W;
   }

   public String m() {
      return String.format(Locale.ROOT, "%d ticks, %.1f degrees, fastest %.1f/tick: %s", this.W, this.n, this.r, this.D.length() == 0 ? "no turn" : this.D);
   }

   private void p(float var1, float var2, float[] var3) {
      float var4 = (float)Math.hypot(Mth.wrapDegrees(var3[0] - var1), var3[1] - var2);
      if (!(var4 <= 0.0F)) {
         this.W++;
         this.n += var4;
         this.r = Math.max(this.r, var4);
         if (this.W <= 16) {
            if (this.D.length() > 0) {
               this.D.append(" > ");
            }

            this.D.append(String.format(Locale.ROOT, "%.1f/%.1f", Mth.wrapDegrees(var3[0]), var3[1]));
         } else if (this.W == 17) {
            this.D.append(" > ...");
         }
      }
   }
}

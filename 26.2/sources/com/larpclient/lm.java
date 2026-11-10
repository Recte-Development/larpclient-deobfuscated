package com.larpclient;

import com.larpclient.module.tl;
import java.util.Locale;
import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class lm {
   public static final double b = 180.0;
   public static final float u = 0.55F;
   public static final float Q = 0.8F;
   public static final float W = 1.0F;
   public static final float D = 30.0F;
   public static final float h = 0.5F;
   public static final float M = 0.15F;
   private static final double F = 0.3;
   private static final float l = 6.0F;
   private static final int x = 16;
   private final SplittableRandom w;
   private final com.larpclient.module.ti G;
   private final tl a = new tl();
   private float O;
   private float e;
   private float X;
   private boolean g;
   private final StringBuilder s = new StringBuilder();
   private int v;
   private float q;
   private float N;

   public lm() {
      this(new SplittableRandom());
   }

   public lm(SplittableRandom var1) {
      this.w = var1;
      this.G = new com.larpclient.module.ti(com.larpclient.module.ti.J, var1.split());
   }

   public float[] f(float var1, float var2, float var3, float var4, double var5, boolean var7) {
      return this.T(var1, var2, var3, var4, var5, var7, com.larpclient.module.tv.g());
   }

   public float[] T(float var1, float var2, float var3, float var4, double var5, boolean var7, float var8) {
      float var9 = Mth.clamp(var4, -90.0F, 90.0F);
      float[] var10 = var7 ? this.J(var1, var2, var3, var9, (float)Math.clamp(var5, 1.0, 180.0), var8) : T(var1, var2, var3, var9, var5);
      this.M(var1, var2, var10);
      return var10;
   }

   public static float[] T(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.clamp(var3, -90.0F, 90.0F);
      return var4 >= 180.0 ? new float[]{var0 + Mth.wrapDegrees(var2 - var0), var6} : u6.A(var0, var1, var2, var6, var4);
   }

   private float[] J(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (var9 < 1.0E-4F) {
         this.O = 0.0F;
         this.g = (boolean)0;
         return new float[]{var1, var2};
      }

      if (this.O == 0.0F && !this.g && var9 > 30.0F && this.w.nextDouble() < 0.3) {
         float var10 = (float)(0.02 + this.w.nextDouble() * 0.04);
         this.e = var7 * var10;
         this.X = var8 * var10;
         this.g = (boolean)1;
      }

      if (this.g) {
         float var18 = var7 + this.e;
         float var11 = Mth.clamp(var4 + this.X, -90.0F, 90.0F) - var2;
         float var12 = (float)Math.hypot(var18, var11);
         if (var12 <= 1.5F) {
            this.g = (boolean)0;
         } else {
            var7 = var18;
            var8 = var11;
            var9 = var12;
         }
      }

      int var19 = var9 < 6.0F ? 1 : 0;
      this.G.x();
      float var20 = this.G.v(var9, (boolean)var19);
      float var21 = this.G.O(com.larpclient.module.ti.y.X, (boolean)var19) + this.G.b(com.larpclient.module.ti.y.X) * 0.05F;
      float var13 = this.G.O(com.larpclient.module.ti.y.G, (boolean)var19) + this.G.b(com.larpclient.module.ti.y.G) * 0.05F;
      float var14;
      float var15;
      if (var9 <= 1.0F) {
         var14 = var7;
         var15 = var8;
      } else {
         float var16 = Math.min(var5, this.O + var5 * 0.55F);
         var16 = Math.min(var16, Math.max(1.0F, var9 * 0.8F)) * var20;
         var14 = var7 / var9 * var16 * var21;
         var15 = var8 / var9 * var16 * var13;
         if (var19 == 0) {
            var15 += this.G.d(var14);
         }

         var14 = R(var14, var7);
         var15 = R(var15, var8);
         float var17 = (float)Math.hypot(var14, var15);
         if (var17 > var5) {
            var14 *= var5 / var17;
            var15 *= var5 / var17;
         }
      }

      com.larpclient.module.u var25 = this.a
         .x(new com.larpclient.module.u(var1, var2), new com.larpclient.module.u(var1 + var14, Mth.clamp(var2 + var15, -90.0F, 90.0F)), var6);
      this.O = (float)Math.hypot(var25.R - var1, var25.x - var2);
      return new float[]{var25.R, var25.x};
   }

   private static float R(float var0, float var1) {
      return var1 >= 0.0F ? Mth.clamp(var0, -0.5F, var1) : Mth.clamp(var0, var1, 0.5F);
   }

   public static boolean S(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Math.max(0.15F, var4);
      return Math.abs(Mth.wrapDegrees(var2 - var0)) <= var5 && Math.abs(var3 - var1) <= var5;
   }

   public static float A(boolean var0, float var1, float var2) {
      return var0 ? Math.max(var1, var2 * 0.6F) : var1;
   }

   public void U() {
      this.G.l();
      this.a.g();
      this.O = this.e = this.X = 0.0F;
      this.g = (boolean)0;
   }

   public void q() {
      this.s.setLength(0);
      this.v = 0;
      this.q = this.N = 0.0F;
   }

   public int D() {
      return this.v;
   }

   public String u() {
      return String.format(Locale.ROOT, "%d ticks, %.1f degrees, fastest %.1f/tick: %s", this.v, this.q, this.N, this.s.length() == 0 ? "no turn" : this.s);
   }

   private void M(float var1, float var2, float[] var3) {
      float var4 = (float)Math.hypot(Mth.wrapDegrees(var3[0] - var1), var3[1] - var2);
      if (!(var4 <= 0.0F)) {
         this.v++;
         this.q += var4;
         this.N = Math.max(this.N, var4);
         if (this.v <= 16) {
            if (this.s.length() > 0) {
               this.s.append(" > ");
            }

            this.s.append(String.format(Locale.ROOT, "%.1f/%.1f", Mth.wrapDegrees(var3[0]), var3[1]));
         } else if (this.v == 17) {
            this.s.append(" > ...");
         }
      }
   }
}

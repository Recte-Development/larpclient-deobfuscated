package com.larpclient;

import com.larpclient.module.a_;
import java.util.Locale;
import java.util.SplittableRandom;
import net.minecraft.class_3532;

public final class gn {
   public static final double I = 180.0;
   public static final float n = 0.55F;
   public static final float H = 0.8F;
   public static final float R = 1.0F;
   public static final float f = 30.0F;
   public static final float O = 0.5F;
   public static final float S = 0.15F;
   private static final double Z = 0.3;
   private static final float e = 6.0F;
   private static final int J = 16;
   private final SplittableRandom k;
   private final com.larpclient.module.lq K;
   private final com.larpclient.module.ld T = new com.larpclient.module.ld();
   private float t;
   private float G;
   private float N;
   private boolean v;
   private final StringBuilder F = new StringBuilder();
   private int Q;
   private float j;
   private float u;

   public gn() {
      this(new SplittableRandom());
   }

   public gn(SplittableRandom var1) {
      this.k = var1;
      this.K = new com.larpclient.module.lq(com.larpclient.module.lq.u, var1.split());
   }

   public float[] j(float var1, float var2, float var3, float var4, double var5, boolean var7) {
      return this.J(var1, var2, var3, var4, var5, var7, a_.i());
   }

   public float[] J(float var1, float var2, float var3, float var4, double var5, boolean var7, float var8) {
      float var9 = class_3532.method_15363(var4, -90.0F, 90.0F);
      float[] var10 = var7 ? this.r(var1, var2, var3, var9, (float)Math.clamp(var5, 1.0, 180.0), var8) : z(var1, var2, var3, var9, var5);
      this.T(var1, var2, var10);
      return var10;
   }

   public static float[] z(float var0, float var1, float var2, float var3, double var4) {
      float var6 = class_3532.method_15363(var3, -90.0F, 90.0F);
      return var4 >= 180.0 ? new float[]{var0 + class_3532.method_15393(var2 - var0), var6} : hl.m(var0, var1, var2, var6, var4);
   }

   private float[] r(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = class_3532.method_15393(var3 - var1);
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (var9 < 1.0E-4F) {
         this.t = 0.0F;
         this.v = (boolean)0;
         return new float[]{var1, var2};
      }

      if (this.t == 0.0F && !this.v && var9 > 30.0F && this.k.nextDouble() < 0.3) {
         float var10 = (float)(0.02 + this.k.nextDouble() * 0.04);
         this.G = var7 * var10;
         this.N = var8 * var10;
         this.v = (boolean)1;
      }

      if (this.v) {
         float var18 = var7 + this.G;
         float var11 = class_3532.method_15363(var4 + this.N, -90.0F, 90.0F) - var2;
         float var12 = (float)Math.hypot(var18, var11);
         if (var12 <= 1.5F) {
            this.v = (boolean)0;
         } else {
            var7 = var18;
            var8 = var11;
            var9 = var12;
         }
      }

      int var19 = var9 < 6.0F ? 1 : 0;
      this.K.c();
      float var20 = this.K.v(var9, (boolean)var19);
      float var21 = this.K.M(com.larpclient.module.lq.g.h, (boolean)var19) + this.K.Y(com.larpclient.module.lq.g.h) * 0.05F;
      float var13 = this.K.M(com.larpclient.module.lq.g.u, (boolean)var19) + this.K.Y(com.larpclient.module.lq.g.u) * 0.05F;
      float var14;
      float var15;
      if (var9 <= 1.0F) {
         var14 = var7;
         var15 = var8;
      } else {
         float var16 = Math.min(var5, this.t + var5 * 0.55F);
         var16 = Math.min(var16, Math.max(1.0F, var9 * 0.8F)) * var20;
         var14 = var7 / var9 * var16 * var21;
         var15 = var8 / var9 * var16 * var13;
         if (var19 == 0) {
            var15 += this.K.f(var14);
         }

         var14 = e(var14, var7);
         var15 = e(var15, var8);
         float var17 = (float)Math.hypot(var14, var15);
         if (var17 > var5) {
            var14 *= var5 / var17;
            var15 *= var5 / var17;
         }
      }

      com.larpclient.module.l4 var25 = this.T
         .C(new com.larpclient.module.l4(var1, var2), new com.larpclient.module.l4(var1 + var14, class_3532.method_15363(var2 + var15, -90.0F, 90.0F)), var6);
      this.t = (float)Math.hypot(var25.j - var1, var25.l - var2);
      return new float[]{var25.j, var25.l};
   }

   private static float e(float var0, float var1) {
      return var1 >= 0.0F ? class_3532.method_15363(var0, -0.5F, var1) : class_3532.method_15363(var0, var1, 0.5F);
   }

   public static boolean j(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Math.max(0.15F, var4);
      return Math.abs(class_3532.method_15393(var2 - var0)) <= var5 && Math.abs(var3 - var1) <= var5;
   }

   public static float l(boolean var0, float var1, float var2) {
      return var0 ? Math.max(var1, var2 * 0.6F) : var1;
   }

   public void s() {
      this.K.A();
      this.T.Y();
      this.t = this.G = this.N = 0.0F;
      this.v = (boolean)0;
   }

   public void z() {
      this.F.setLength(0);
      this.Q = 0;
      this.j = this.u = 0.0F;
   }

   public int n() {
      return this.Q;
   }

   public String E() {
      return String.format(Locale.ROOT, "%d ticks, %.1f degrees, fastest %.1f/tick: %s", this.Q, this.j, this.u, this.F.length() == 0 ? "no turn" : this.F);
   }

   private void T(float var1, float var2, float[] var3) {
      float var4 = (float)Math.hypot(class_3532.method_15393(var3[0] - var1), var3[1] - var2);
      if (!(var4 <= 0.0F)) {
         this.Q++;
         this.j += var4;
         this.u = Math.max(this.u, var4);
         if (this.Q <= 16) {
            if (this.F.length() > 0) {
               this.F.append(" > ");
            }

            this.F.append(String.format(Locale.ROOT, "%.1f/%.1f", class_3532.method_15393(var3[0]), var3[1]));
         } else if (this.Q == 17) {
            this.F.append(" > ...");
         }
      }
   }
}

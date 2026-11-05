package com.larpclient;

import java.util.Locale;
import java.util.SplittableRandom;
import net.minecraft.util.Mth;

public final class l0 {
   public static final double r = 180.0;
   public static final float P = 0.55F;
   public static final float H = 0.8F;
   public static final float Y = 1.0F;
   public static final float N = 30.0F;
   public static final float B = 0.5F;
   public static final float Q = 0.15F;
   private static final double X = 0.3;
   private static final float f = 6.0F;
   private static final int S = 16;
   private final SplittableRandom i;
   private final com.larpclient.module.lk F;
   private final com.larpclient.module.lj v = new com.larpclient.module.lj();
   private float J;
   private float W;
   private float t;
   private boolean n;
   private final StringBuilder I = new StringBuilder();
   private int E;
   private float l;
   private float p;

   public l0() {
      this(new SplittableRandom());
   }

   public l0(SplittableRandom var1) {
      this.i = var1;
      this.F = new com.larpclient.module.lk(com.larpclient.module.lk.x, var1.split());
   }

   public float[] G(float var1, float var2, float var3, float var4, double var5, boolean var7) {
      return this.B(var1, var2, var3, var4, var5, var7, com.larpclient.module.le.e());
   }

   public float[] B(float var1, float var2, float var3, float var4, double var5, boolean var7, float var8) {
      float var9 = Mth.clamp(var4, -90.0F, 90.0F);
      float[] var10 = var7 ? this.A(var1, var2, var3, var9, (float)Math.clamp(var5, 1.0, 180.0), var8) : q(var1, var2, var3, var9, var5);
      this.Q(var1, var2, var10);
      return var10;
   }

   public static float[] q(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.clamp(var3, -90.0F, 90.0F);
      return var4 >= 180.0 ? new float[]{var0 + Mth.wrapDegrees(var2 - var0), var6} : ru.Y(var0, var1, var2, var6, var4);
   }

   private float[] A(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = var4 - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (var9 < 1.0E-4F) {
         this.J = 0.0F;
         this.n = (boolean)0;
         return new float[]{var1, var2};
      }

      if (this.J == 0.0F && !this.n && var9 > 30.0F && this.i.nextDouble() < 0.3) {
         float var10 = (float)(0.02 + this.i.nextDouble() * 0.04);
         this.W = var7 * var10;
         this.t = var8 * var10;
         this.n = (boolean)1;
      }

      if (this.n) {
         float var18 = var7 + this.W;
         float var11 = Mth.clamp(var4 + this.t, -90.0F, 90.0F) - var2;
         float var12 = (float)Math.hypot(var18, var11);
         if (var12 <= 1.5F) {
            this.n = (boolean)0;
         } else {
            var7 = var18;
            var8 = var11;
            var9 = var12;
         }
      }

      int var19 = var9 < 6.0F ? 1 : 0;
      this.F.Q();
      float var20 = this.F.I(var9, (boolean)var19);
      float var21 = this.F.S(com.larpclient.module.lk.o.W, (boolean)var19) + this.F.w(com.larpclient.module.lk.o.W) * 0.05F;
      float var13 = this.F.S(com.larpclient.module.lk.o.Z, (boolean)var19) + this.F.w(com.larpclient.module.lk.o.Z) * 0.05F;
      float var14;
      float var15;
      if (var9 <= 1.0F) {
         var14 = var7;
         var15 = var8;
      } else {
         float var16 = Math.min(var5, this.J + var5 * 0.55F);
         var16 = Math.min(var16, Math.max(1.0F, var9 * 0.8F)) * var20;
         var14 = var7 / var9 * var16 * var21;
         var15 = var8 / var9 * var16 * var13;
         if (var19 == 0) {
            var15 += this.F.D(var14);
         }

         var14 = H(var14, var7);
         var15 = H(var15, var8);
         float var17 = (float)Math.hypot(var14, var15);
         if (var17 > var5) {
            var14 *= var5 / var17;
            var15 *= var5 / var17;
         }
      }

      com.larpclient.module.lx var25 = this.v
         .M(new com.larpclient.module.lx(var1, var2), new com.larpclient.module.lx(var1 + var14, Mth.clamp(var2 + var15, -90.0F, 90.0F)), var6);
      this.J = (float)Math.hypot(var25.z - var1, var25.I - var2);
      return new float[]{var25.z, var25.I};
   }

   private static float H(float var0, float var1) {
      return var1 >= 0.0F ? Mth.clamp(var0, -0.5F, var1) : Mth.clamp(var0, var1, 0.5F);
   }

   public static boolean g(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Math.max(0.15F, var4);
      return Math.abs(Mth.wrapDegrees(var2 - var0)) <= var5 && Math.abs(var3 - var1) <= var5;
   }

   public static float t(boolean var0, float var1, float var2) {
      return var0 ? Math.max(var1, var2 * 0.6F) : var1;
   }

   public void x() {
      this.F.c();
      this.v.y();
      this.J = this.W = this.t = 0.0F;
      this.n = (boolean)0;
   }

   public void O() {
      this.I.setLength(0);
      this.E = 0;
      this.l = this.p = 0.0F;
   }

   public int M() {
      return this.E;
   }

   public String F() {
      return String.format(Locale.ROOT, "%d ticks, %.1f degrees, fastest %.1f/tick: %s", this.E, this.l, this.p, this.I.length() == 0 ? "no turn" : this.I);
   }

   private void Q(float var1, float var2, float[] var3) {
      float var4 = (float)Math.hypot(Mth.wrapDegrees(var3[0] - var1), var3[1] - var2);
      if (!(var4 <= 0.0F)) {
         this.E++;
         this.l += var4;
         this.p = Math.max(this.p, var4);
         if (this.E <= 16) {
            if (this.I.length() > 0) {
               this.I.append(" > ");
            }

            this.I.append(String.format(Locale.ROOT, "%.1f/%.1f", Mth.wrapDegrees(var3[0]), var3[1]));
         } else if (this.E == 17) {
            this.I.append(" > ...");
         }
      }
   }
}

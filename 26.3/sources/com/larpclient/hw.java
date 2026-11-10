package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.kx;
import java.util.SplittableRandom;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class hw {
   static final double O = 0.546;
   static final double T = 0.4;
   static final double a = 1.5;
   private static final float g = 6.0F;
   private static final int w = Integer.MIN_VALUE;
   static final double Q = 0.85;
   static final float f = 6.0F;
   static final float j = 1.2F;
   static final float I = 1.4F;
   static final float X = 0.35F;
   static final float W = 0.3F;
   private final hw.b J;
   private final com.larpclient.module.f0 M = new com.larpclient.module.f0(com.larpclient.module.f0.Y);
   private final kx P = new kx();
   private final SplittableRandom z = new SplittableRandom();
   private float F;
   private float R;
   private float A;
   private boolean D;
   private boolean b;
   private float U;
   private float V = 1.0F;
   private float E;
   private float L;
   private float l;
   private float s;
   private float i = Float.NaN;
   private float q = Float.NaN;
   private float S;
   private float k;
   private float C;
   private float r;
   private int n;
   private int t;
   private boolean B;
   private boolean v;
   private float h;
   private float H;
   static final float Y = 4.0F;
   private int N = -1;
   private int x = Integer.MIN_VALUE;

   public hw() {
      this(hw.b.STEADY);
   }

   public hw(hw.b var1) {
      this.J = var1;
      this.W();
   }

   public void W() {
      this.F = this.R = 0.0F;
      this.D = this.b = (boolean)0;
      this.N = -1;
      this.x = Integer.MIN_VALUE;
      this.A = this.u();
      this.M.G();
      this.P.L();
      this.U = 0.0F;
      this.V = 1.0F;
      this.E = this.L = this.l = this.s = 0.0F;
      this.i = this.q = Float.NaN;
      this.S = this.k = this.C = this.r = 0.0F;
      this.n = this.t = 0;
      this.B = (boolean)0;
      this.h = this.H = 0.0F;
   }

   public float[] M(float var1, float var2, float var3, float var4, boolean var5) {
      return this.n(var1, var2, var3, var4, var5, false);
   }

   public float[] n(float var1, float var2, float var3, float var4, boolean var5, boolean var6) {
      this.v = var6;
      if (this.J.glide()) {
         return this.E(var1, var2, var3, var4, var5);
      }

      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = Mth.clamp(var4, -90.0F, 90.0F) - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (!var5 || !(var9 <= this.A) && (!this.D || !(var9 <= 3.5F))) {
         this.D = (boolean)0;
         int var10 = var9 < 6.0F ? 1 : 0;
         this.M.U();
         float var11 = this.M.c(var9, (boolean)var10);
         this.F = J(
            var7, this.F, 12.0F * this.M.E(com.larpclient.module.f0.p.g, (boolean)var10) * var11, 3.0F, 0.32F, this.M.h(com.larpclient.module.f0.p.g) * 0.08F
         );
         this.R = J(
            var8, this.R, 9.0F * this.M.E(com.larpclient.module.f0.p.K, (boolean)var10) * var11, 2.25F, 0.32F, this.M.h(com.larpclient.module.f0.p.K) * 0.06F
         );
         this.R = X(this.R + this.M.a(this.F), var8);
         com.larpclient.module.c var12 = this.P.X(new com.larpclient.module.c(var1, var2), new com.larpclient.module.c(var1 + this.F, var2 + this.R));
         return new float[]{var12.V, var12.T};
      }

      if (!this.D) {
         this.A = this.u();
      }

      this.D = (boolean)1;
      this.F = this.R = 0.0F;
      return new float[]{var1, var2};
   }

   private float[] E(float var1, float var2, float var3, float var4, boolean var5) {
      var4 = Mth.clamp(var4, -90.0F, 90.0F);
      if (Float.isNaN(this.i) || Math.hypot(Mth.wrapDegrees(var3 - this.i), var4 - this.q) > 3.0) {
         this.d(var1, var2, var3, var4);
      }

      this.i = var3;
      this.q = var4;
      this.M.U();
      float var6;
      float var7;
      if (var5) {
         if (!this.B) {
            this.B = (boolean)1;
            float var8 = this.v ? 0.0F : 4.0F;
            this.h = Mth.clamp(Mth.wrapDegrees(var1 - var3), -var8, var8);
            this.H = Mth.clamp(var2 - var4, -var8, var8);
         }

         var3 += this.h;
         var4 = Mth.clamp(var4 + this.H, -90.0F, 90.0F);
         if (--this.n <= 0) {
            double var26 = this.z.nextDouble() * Math.PI * 2.0;
            double var10 = Math.sqrt(this.z.nextDouble()) * 1.4F * (this.v ? 0.3F : 1.0F);
            this.C = (float)(Math.cos(var26) * var10);
            this.r = (float)(Math.sin(var26) * var10 * 0.7);
            this.n = 12 + this.z.nextInt(20);
         }

         this.S = this.S + (this.C - this.S) * 0.12F;
         this.k = this.k + (this.r - this.k) * 0.12F;
         float var27 = Mth.wrapDegrees(var3 + this.S - var1);
         float var9 = Mth.clamp(var4 + this.k, -90.0F, 90.0F) - var2;
         float var31 = (float)Math.hypot(var27, var9);
         this.U *= 0.45F;
         float var11 = Math.max(this.U, Math.min(0.35F, var31 * 0.15F));
         float var12 = Math.min(var11, var31);
         var6 = var31 < 1.0E-4F ? 0.0F : var27 / var31 * var12;
         var7 = var31 < 1.0E-4F ? 0.0F : var9 / var31 * var12;
      } else {
         this.B = (boolean)0;
         if (this.t > 0) {
            this.t--;
            return new float[]{var1, var2};
         }

         float var28 = Mth.wrapDegrees(var3 + this.L - var1);
         float var30 = Mth.clamp(var4 + this.l, -90.0F, 90.0F) - var2;
         float var32 = (float)Math.hypot(var28, var30);
         if (var32 < 1.5F && (this.L != 0.0F || this.l != 0.0F)) {
            this.L = this.l = 0.0F;
            var28 = Mth.wrapDegrees(var3 - var1);
            var30 = var4 - var2;
            var32 = (float)Math.hypot(var28, var30);
            if (this.J == hw.b.QUICK && var32 > 1.5F && !this.v) {
               this.U *= 0.35F;
               this.t = this.z.nextInt(2);
               this.s = var32;
            }
         }

         if (var32 < 1.0E-4F) {
            return new float[]{var1, var2};
         }

         float var33 = this.J.speed() * this.V * (0.9F + 0.1F * this.M.E(com.larpclient.module.f0.p.g, var32 < 6.0F));
         float var34 = (float)Math.sqrt(2.0F * this.J.braking() * var32) - this.J.braking() * 0.5F;
         float var13 = Math.max(0.3F, Math.min(var33, Math.min(var34, var32 * this.J.share())));
         this.U = Math.min(var32, this.U + Mth.clamp(var13 - this.U, -this.J.braking() * 1.5F, this.J.acceleration()));
         float var14 = this.s > 0.0F ? Mth.clamp(1.0F - var32 / this.s, 0.0F, 1.0F) : 1.0F;
         float var15 = this.E * Mth.sin((float) Math.PI * var14);
         float var16 = var28 / var32;
         float var17 = var30 / var32;
         float var18 = var16 - var17 * var15;
         float var19 = var17 + var16 * var15;
         float var20 = (float)Math.hypot(var18, var19);
         var6 = var18 / var20 * this.U + this.M.h(com.larpclient.module.f0.p.g) * 0.04F * this.U;
         var7 = var19 / var20 * this.U + this.M.h(com.larpclient.module.f0.p.K) * 0.03F * this.U + this.M.a(var18 / var20 * this.U) * 0.5F;
         var6 = X(var6, var28);
         var7 = X(var7, var30);
      }

      com.larpclient.module.c var29 = this.P.X(new com.larpclient.module.c(var1, var2), new com.larpclient.module.c(var1 + var6, var2 + var7));
      return new float[]{var29.V, var29.T};
   }

   private void d(float var1, float var2, float var3, float var4) {
      float var5 = Mth.wrapDegrees(var3 - var1);
      float var6 = var4 - var2;
      this.s = (float)Math.hypot(var5, var6);
      int var7 = var6 > 0.0F && var6 > Math.abs(var5) * 0.5F ? 1 : 0;
      this.V = (0.88F + (float)this.z.nextDouble() * 0.24F) * (var7 != 0 ? 0.92F : 1.0F);
      this.E = (float)(this.z.nextDouble() * 2.0 - 1.0) * 0.16F;
      if (this.J == hw.b.QUICK && this.s > 2.0F && !this.v) {
         double var14 = this.z.nextDouble() < 0.85 ? -(0.02 + this.z.nextDouble() * 0.1) * (var7 != 0 ? 1.3 : 1.0) : this.z.nextDouble() * 0.03;
         var14 = Mth.clamp(var14 * this.s, -6.0, 1.2F);
         double var10 = (this.z.nextDouble() * 2.0 - 1.0) * Math.abs(var14) / 2.25;
         float var12 = var5 / this.s;
         float var13 = var6 / this.s;
         this.L = (float)(var12 * var14 - var13 * var10);
         this.l = (float)(var13 * var14 + var12 * var10);
      } else {
         float var8 = Math.min(1.2F, this.s * 0.03F);
         this.L = (float)(this.z.nextDouble() * 2.0 - 1.0) * var8;
         this.l = (float)(this.z.nextDouble() * 2.0 - 1.0) * var8 * 0.7F;
      }

      this.S = this.k = 0.0F;
      this.n = 0;
      this.B = (boolean)0;
      this.t = this.s > 8.0F ? this.z.nextInt(3) : 0;
   }

   private static float J(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = Mth.clamp(var0 * var4 * (1.0F + var5), -var2, var2);
      return X(var1 + Mth.clamp(var6 - var1, -var3, var3), var0);
   }

   private static float X(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float u() {
      return 0.8F + (float)this.z.nextDouble() * 1.2F;
   }

   public boolean g(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.b) {
            if (var1 <= V(var3) + 0.04) {
               this.b = (boolean)0;
            }
         } else if (var1 > 0.4) {
            if (this.N < 0) {
               this.N = this.z.nextInt(1, 5);
            } else if (--this.N <= 0) {
               this.b = (boolean)1;
               this.N = -1;
            }
         } else {
            this.N = -1;
         }

         return this.b;
      } else {
         this.b = (boolean)0;
         this.N = -1;
         return false;
      }
   }

   public boolean Y() {
      return this.b;
   }

   public static double V(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public Input q(float var1, Vec3 var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0;
      double var5 = Mth.wrapDegrees(var3 - var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.x != Integer.MIN_VALUE && Math.abs(Mth.wrapDegrees(var5 - this.x * 45.0)) < 28.5) {
         var7 = this.x;
      }

      this.x = var7;
      float[] var8 = AuraModule.a(var1 + var7 * 45.0, var1);
      return new Input(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static Vec2 w(Input var0) {
      float var1 = var0.forward() == var0.backward() ? 0.0F : (var0.forward() ? 1.0F : -1.0F);
      float var2 = var0.left() == var0.right() ? 0.0F : (var0.left() ? 1.0F : -1.0F);
      return new Vec2(var2, var1).normalized();
   }

   public static double r(Vec3 var0, Vec3 var1, Direction var2) {
      return (var1.x - var0.x) * var2.getStepX() + (var1.z - var0.z) * var2.getStepZ();
   }

   public static double e(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      return (var1.x - var0.x) * var3.getStepX() + (var1.z - var0.z) * var3.getStepZ();
   }

   public static Vec3 N(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      double var4 = e(var0, var1, var2);
      return new Vec3(var2.getStepX() * 1.5 + var3.getStepX() * var4, 0.0, var2.getStepZ() * 1.5 + var3.getStepZ() * var4);
   }

   public record b(float speed, float acceleration, float braking, float share, boolean glide) {
      public static final hw.b STEADY = new hw.b(12.0F, 3.0F, 3.0F, 0.32F, false);
      public static final hw.b QUICK = new hw.b(18.0F, 4.5F, 4.0F, 0.45F, true);
   }
}

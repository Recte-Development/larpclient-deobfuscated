package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.tl;
import java.util.SplittableRandom;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class hc {
   static final double f = 0.546;
   static final double O = 0.4;
   static final double W = 1.5;
   private static final float l = 6.0F;
   private static final int q = Integer.MIN_VALUE;
   static final double r = 0.85;
   static final float i = 6.0F;
   static final float a = 1.2F;
   static final float m = 1.4F;
   static final float x = 0.35F;
   static final float J = 0.3F;
   private final hc.h X;
   private final com.larpclient.module.ti Z = new com.larpclient.module.ti(com.larpclient.module.ti.J);
   private final tl c = new tl();
   private final SplittableRandom w = new SplittableRandom();
   private float U;
   private float N;
   private float v;
   private boolean h;
   private boolean Q;
   private float E;
   private float k = 1.0F;
   private float A;
   private float s;
   private float B;
   private float d;
   private float o = Float.NaN;
   private float I = Float.NaN;
   private float p;
   private float g;
   private float u;
   private float R;
   private int C;
   private int y;
   private boolean V;
   private boolean j;
   private float T;
   private float b;
   static final float M = 4.0F;
   private int G = -1;
   private int t = Integer.MIN_VALUE;

   public hc() {
      this(hc.h.STEADY);
   }

   public hc(hc.h var1) {
      this.X = var1;
      this.x();
   }

   public void x() {
      this.U = this.N = 0.0F;
      this.h = this.Q = (boolean)0;
      this.G = -1;
      this.t = Integer.MIN_VALUE;
      this.v = this.r();
      this.Z.l();
      this.c.g();
      this.E = 0.0F;
      this.k = 1.0F;
      this.A = this.s = this.B = this.d = 0.0F;
      this.o = this.I = Float.NaN;
      this.p = this.g = this.u = this.R = 0.0F;
      this.C = this.y = 0;
      this.V = (boolean)0;
      this.T = this.b = 0.0F;
   }

   public float[] Q(float var1, float var2, float var3, float var4, boolean var5) {
      return this.c(var1, var2, var3, var4, var5, false);
   }

   public float[] c(float var1, float var2, float var3, float var4, boolean var5, boolean var6) {
      this.j = var6;
      if (this.X.glide()) {
         return this.g(var1, var2, var3, var4, var5);
      }

      float var7 = Mth.wrapDegrees(var3 - var1);
      float var8 = Mth.clamp(var4, -90.0F, 90.0F) - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (!var5 || !(var9 <= this.v) && (!this.h || !(var9 <= 3.5F))) {
         this.h = (boolean)0;
         int var10 = var9 < 6.0F ? 1 : 0;
         this.Z.x();
         float var11 = this.Z.v(var9, (boolean)var10);
         this.U = m(
            var7, this.U, 12.0F * this.Z.O(com.larpclient.module.ti.y.X, (boolean)var10) * var11, 3.0F, 0.32F, this.Z.b(com.larpclient.module.ti.y.X) * 0.08F
         );
         this.N = m(
            var8, this.N, 9.0F * this.Z.O(com.larpclient.module.ti.y.G, (boolean)var10) * var11, 2.25F, 0.32F, this.Z.b(com.larpclient.module.ti.y.G) * 0.06F
         );
         this.N = a(this.N + this.Z.d(this.U), var8);
         com.larpclient.module.u var12 = this.c.M(new com.larpclient.module.u(var1, var2), new com.larpclient.module.u(var1 + this.U, var2 + this.N));
         return new float[]{var12.R, var12.x};
      }

      if (!this.h) {
         this.v = this.r();
      }

      this.h = (boolean)1;
      this.U = this.N = 0.0F;
      return new float[]{var1, var2};
   }

   private float[] g(float var1, float var2, float var3, float var4, boolean var5) {
      var4 = Mth.clamp(var4, -90.0F, 90.0F);
      if (Float.isNaN(this.o) || Math.hypot(Mth.wrapDegrees(var3 - this.o), var4 - this.I) > 3.0) {
         this.X(var1, var2, var3, var4);
      }

      this.o = var3;
      this.I = var4;
      this.Z.x();
      float var6;
      float var7;
      if (var5) {
         if (!this.V) {
            this.V = (boolean)1;
            float var8 = this.j ? 0.0F : 4.0F;
            this.T = Mth.clamp(Mth.wrapDegrees(var1 - var3), -var8, var8);
            this.b = Mth.clamp(var2 - var4, -var8, var8);
         }

         var3 += this.T;
         var4 = Mth.clamp(var4 + this.b, -90.0F, 90.0F);
         if (--this.C <= 0) {
            double var26 = this.w.nextDouble() * Math.PI * 2.0;
            double var10 = Math.sqrt(this.w.nextDouble()) * 1.4F * (this.j ? 0.3F : 1.0F);
            this.u = (float)(Math.cos(var26) * var10);
            this.R = (float)(Math.sin(var26) * var10 * 0.7);
            this.C = 12 + this.w.nextInt(20);
         }

         this.p = this.p + (this.u - this.p) * 0.12F;
         this.g = this.g + (this.R - this.g) * 0.12F;
         float var27 = Mth.wrapDegrees(var3 + this.p - var1);
         float var9 = Mth.clamp(var4 + this.g, -90.0F, 90.0F) - var2;
         float var31 = (float)Math.hypot(var27, var9);
         this.E *= 0.45F;
         float var11 = Math.max(this.E, Math.min(0.35F, var31 * 0.15F));
         float var12 = Math.min(var11, var31);
         var6 = var31 < 1.0E-4F ? 0.0F : var27 / var31 * var12;
         var7 = var31 < 1.0E-4F ? 0.0F : var9 / var31 * var12;
      } else {
         this.V = (boolean)0;
         if (this.y > 0) {
            this.y--;
            return new float[]{var1, var2};
         }

         float var28 = Mth.wrapDegrees(var3 + this.s - var1);
         float var30 = Mth.clamp(var4 + this.B, -90.0F, 90.0F) - var2;
         float var32 = (float)Math.hypot(var28, var30);
         if (var32 < 1.5F && (this.s != 0.0F || this.B != 0.0F)) {
            this.s = this.B = 0.0F;
            var28 = Mth.wrapDegrees(var3 - var1);
            var30 = var4 - var2;
            var32 = (float)Math.hypot(var28, var30);
            if (this.X == hc.h.QUICK && var32 > 1.5F && !this.j) {
               this.E *= 0.35F;
               this.y = this.w.nextInt(2);
               this.d = var32;
            }
         }

         if (var32 < 1.0E-4F) {
            return new float[]{var1, var2};
         }

         float var33 = this.X.speed() * this.k * (0.9F + 0.1F * this.Z.O(com.larpclient.module.ti.y.X, var32 < 6.0F));
         float var34 = (float)Math.sqrt(2.0F * this.X.braking() * var32) - this.X.braking() * 0.5F;
         float var13 = Math.max(0.3F, Math.min(var33, Math.min(var34, var32 * this.X.share())));
         this.E = Math.min(var32, this.E + Mth.clamp(var13 - this.E, -this.X.braking() * 1.5F, this.X.acceleration()));
         float var14 = this.d > 0.0F ? Mth.clamp(1.0F - var32 / this.d, 0.0F, 1.0F) : 1.0F;
         float var15 = this.A * Mth.sin((float) Math.PI * var14);
         float var16 = var28 / var32;
         float var17 = var30 / var32;
         float var18 = var16 - var17 * var15;
         float var19 = var17 + var16 * var15;
         float var20 = (float)Math.hypot(var18, var19);
         var6 = var18 / var20 * this.E + this.Z.b(com.larpclient.module.ti.y.X) * 0.04F * this.E;
         var7 = var19 / var20 * this.E + this.Z.b(com.larpclient.module.ti.y.G) * 0.03F * this.E + this.Z.d(var18 / var20 * this.E) * 0.5F;
         var6 = a(var6, var28);
         var7 = a(var7, var30);
      }

      com.larpclient.module.u var29 = this.c.M(new com.larpclient.module.u(var1, var2), new com.larpclient.module.u(var1 + var6, var2 + var7));
      return new float[]{var29.R, var29.x};
   }

   private void X(float var1, float var2, float var3, float var4) {
      float var5 = Mth.wrapDegrees(var3 - var1);
      float var6 = var4 - var2;
      this.d = (float)Math.hypot(var5, var6);
      int var7 = var6 > 0.0F && var6 > Math.abs(var5) * 0.5F ? 1 : 0;
      this.k = (0.88F + (float)this.w.nextDouble() * 0.24F) * (var7 != 0 ? 0.92F : 1.0F);
      this.A = (float)(this.w.nextDouble() * 2.0 - 1.0) * 0.16F;
      if (this.X == hc.h.QUICK && this.d > 2.0F && !this.j) {
         double var14 = this.w.nextDouble() < 0.85 ? -(0.02 + this.w.nextDouble() * 0.1) * (var7 != 0 ? 1.3 : 1.0) : this.w.nextDouble() * 0.03;
         var14 = Mth.clamp(var14 * this.d, -6.0, 1.2F);
         double var10 = (this.w.nextDouble() * 2.0 - 1.0) * Math.abs(var14) / 2.25;
         float var12 = var5 / this.d;
         float var13 = var6 / this.d;
         this.s = (float)(var12 * var14 - var13 * var10);
         this.B = (float)(var13 * var14 + var12 * var10);
      } else {
         float var8 = Math.min(1.2F, this.d * 0.03F);
         this.s = (float)(this.w.nextDouble() * 2.0 - 1.0) * var8;
         this.B = (float)(this.w.nextDouble() * 2.0 - 1.0) * var8 * 0.7F;
      }

      this.p = this.g = 0.0F;
      this.C = 0;
      this.V = (boolean)0;
      this.y = this.d > 8.0F ? this.w.nextInt(3) : 0;
   }

   private static float m(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = Mth.clamp(var0 * var4 * (1.0F + var5), -var2, var2);
      return a(var1 + Mth.clamp(var6 - var1, -var3, var3), var0);
   }

   private static float a(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float r() {
      return 0.8F + (float)this.w.nextDouble() * 1.2F;
   }

   public boolean u(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.Q) {
            if (var1 <= T(var3) + 0.04) {
               this.Q = (boolean)0;
            }
         } else if (var1 > 0.4) {
            if (this.G < 0) {
               this.G = this.w.nextInt(1, 5);
            } else if (--this.G <= 0) {
               this.Q = (boolean)1;
               this.G = -1;
            }
         } else {
            this.G = -1;
         }

         return this.Q;
      } else {
         this.Q = (boolean)0;
         this.G = -1;
         return false;
      }
   }

   public boolean D() {
      return this.Q;
   }

   public static double T(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public Input e(float var1, Vec3 var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0;
      double var5 = Mth.wrapDegrees(var3 - var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.t != Integer.MIN_VALUE && Math.abs(Mth.wrapDegrees(var5 - this.t * 45.0)) < 28.5) {
         var7 = this.t;
      }

      this.t = var7;
      float[] var8 = AuraModule.i(var1 + var7 * 45.0, var1);
      return new Input(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static Vec2 D(Input var0) {
      float var1 = var0.forward() == var0.backward() ? 0.0F : (var0.forward() ? 1.0F : -1.0F);
      float var2 = var0.left() == var0.right() ? 0.0F : (var0.left() ? 1.0F : -1.0F);
      return new Vec2(var2, var1).normalized();
   }

   public static double m(Vec3 var0, Vec3 var1, Direction var2) {
      return (var1.x - var0.x) * var2.getStepX() + (var1.z - var0.z) * var2.getStepZ();
   }

   public static double f(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      return (var1.x - var0.x) * var3.getStepX() + (var1.z - var0.z) * var3.getStepZ();
   }

   public static Vec3 H(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      double var4 = f(var0, var1, var2);
      return new Vec3(var2.getStepX() * 1.5 + var3.getStepX() * var4, 0.0, var2.getStepZ() * 1.5 + var3.getStepZ() * var4);
   }

   public record h(float speed, float acceleration, float braking, float share, boolean glide) {
      public static final hc.h STEADY = new hc.h(12.0F, 3.0F, 3.0F, 0.32F, false);
      public static final hc.h QUICK = new hc.h(18.0F, 4.5F, 4.0F, 0.45F, true);
   }
}

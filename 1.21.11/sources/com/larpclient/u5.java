package com.larpclient;

import com.larpclient.module.AuraModule;
import java.util.SplittableRandom;
import net.minecraft.class_10185;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class u5 {
   static final double Q = 0.546;
   static final double q = 0.4;
   static final double d = 1.5;
   private static final float i = 6.0F;
   private static final int n = Integer.MIN_VALUE;
   static final double g = 0.85;
   static final float A = 6.0F;
   static final float K = 1.2F;
   static final float x = 1.4F;
   static final float E = 0.35F;
   static final float D = 0.3F;
   private final u5.p O;
   private final com.larpclient.module.lq h = new com.larpclient.module.lq(com.larpclient.module.lq.u);
   private final com.larpclient.module.ld t = new com.larpclient.module.ld();
   private final SplittableRandom B = new SplittableRandom();
   private float G;
   private float m;
   private float p;
   private boolean H;
   private boolean u;
   private float X;
   private float s = 1.0F;
   private float N;
   private float U;
   private float r;
   private float l;
   private float v = Float.NaN;
   private float C = Float.NaN;
   private float j;
   private float Y;
   private float P;
   private float k;
   private int y;
   private int V;
   private boolean c;
   private boolean L;
   private float F;
   private float S;
   static final float J = 4.0F;
   private int W = -1;
   private int Z = Integer.MIN_VALUE;

   public u5() {
      this(u5.p.STEADY);
   }

   public u5(u5.p var1) {
      this.O = var1;
      this.k();
   }

   public void k() {
      this.G = this.m = 0.0F;
      this.H = this.u = (boolean)0;
      this.W = -1;
      this.Z = Integer.MIN_VALUE;
      this.p = this.o();
      this.h.A();
      this.t.Y();
      this.X = 0.0F;
      this.s = 1.0F;
      this.N = this.U = this.r = this.l = 0.0F;
      this.v = this.C = Float.NaN;
      this.j = this.Y = this.P = this.k = 0.0F;
      this.y = this.V = 0;
      this.c = (boolean)0;
      this.F = this.S = 0.0F;
   }

   public float[] V(float var1, float var2, float var3, float var4, boolean var5) {
      return this.I(var1, var2, var3, var4, var5, false);
   }

   public float[] I(float var1, float var2, float var3, float var4, boolean var5, boolean var6) {
      this.L = var6;
      if (this.O.glide()) {
         return this.r(var1, var2, var3, var4, var5);
      }

      float var7 = class_3532.method_15393(var3 - var1);
      float var8 = class_3532.method_15363(var4, -90.0F, 90.0F) - var2;
      float var9 = (float)Math.hypot(var7, var8);
      if (!var5 || !(var9 <= this.p) && (!this.H || !(var9 <= 3.5F))) {
         this.H = (boolean)0;
         int var10 = var9 < 6.0F ? 1 : 0;
         this.h.c();
         float var11 = this.h.v(var9, (boolean)var10);
         this.G = A(
            var7, this.G, 12.0F * this.h.M(com.larpclient.module.lq.g.h, (boolean)var10) * var11, 3.0F, 0.32F, this.h.Y(com.larpclient.module.lq.g.h) * 0.08F
         );
         this.m = A(
            var8, this.m, 9.0F * this.h.M(com.larpclient.module.lq.g.u, (boolean)var10) * var11, 2.25F, 0.32F, this.h.Y(com.larpclient.module.lq.g.u) * 0.06F
         );
         this.m = U(this.m + this.h.f(this.G), var8);
         com.larpclient.module.l4 var12 = this.t.E(new com.larpclient.module.l4(var1, var2), new com.larpclient.module.l4(var1 + this.G, var2 + this.m));
         return new float[]{var12.j, var12.l};
      }

      if (!this.H) {
         this.p = this.o();
      }

      this.H = (boolean)1;
      this.G = this.m = 0.0F;
      return new float[]{var1, var2};
   }

   private float[] r(float var1, float var2, float var3, float var4, boolean var5) {
      var4 = class_3532.method_15363(var4, -90.0F, 90.0F);
      if (Float.isNaN(this.v) || Math.hypot(class_3532.method_15393(var3 - this.v), var4 - this.C) > 3.0) {
         this.G(var1, var2, var3, var4);
      }

      this.v = var3;
      this.C = var4;
      this.h.c();
      float var6;
      float var7;
      if (var5) {
         if (!this.c) {
            this.c = (boolean)1;
            float var8 = this.L ? 0.0F : 4.0F;
            this.F = class_3532.method_15363(class_3532.method_15393(var1 - var3), -var8, var8);
            this.S = class_3532.method_15363(var2 - var4, -var8, var8);
         }

         var3 += this.F;
         var4 = class_3532.method_15363(var4 + this.S, -90.0F, 90.0F);
         if (--this.y <= 0) {
            double var26 = this.B.nextDouble() * Math.PI * 2.0;
            double var10 = Math.sqrt(this.B.nextDouble()) * 1.4F * (this.L ? 0.3F : 1.0F);
            this.P = (float)(Math.cos(var26) * var10);
            this.k = (float)(Math.sin(var26) * var10 * 0.7);
            this.y = 12 + this.B.nextInt(20);
         }

         this.j = this.j + (this.P - this.j) * 0.12F;
         this.Y = this.Y + (this.k - this.Y) * 0.12F;
         float var27 = class_3532.method_15393(var3 + this.j - var1);
         float var9 = class_3532.method_15363(var4 + this.Y, -90.0F, 90.0F) - var2;
         float var31 = (float)Math.hypot(var27, var9);
         this.X *= 0.45F;
         float var11 = Math.max(this.X, Math.min(0.35F, var31 * 0.15F));
         float var12 = Math.min(var11, var31);
         var6 = var31 < 1.0E-4F ? 0.0F : var27 / var31 * var12;
         var7 = var31 < 1.0E-4F ? 0.0F : var9 / var31 * var12;
      } else {
         this.c = (boolean)0;
         if (this.V > 0) {
            this.V--;
            return new float[]{var1, var2};
         }

         float var28 = class_3532.method_15393(var3 + this.U - var1);
         float var30 = class_3532.method_15363(var4 + this.r, -90.0F, 90.0F) - var2;
         float var32 = (float)Math.hypot(var28, var30);
         if (var32 < 1.5F && (this.U != 0.0F || this.r != 0.0F)) {
            this.U = this.r = 0.0F;
            var28 = class_3532.method_15393(var3 - var1);
            var30 = var4 - var2;
            var32 = (float)Math.hypot(var28, var30);
            if (this.O == u5.p.QUICK && var32 > 1.5F && !this.L) {
               this.X *= 0.35F;
               this.V = this.B.nextInt(2);
               this.l = var32;
            }
         }

         if (var32 < 1.0E-4F) {
            return new float[]{var1, var2};
         }

         float var33 = this.O.speed() * this.s * (0.9F + 0.1F * this.h.M(com.larpclient.module.lq.g.h, var32 < 6.0F));
         float var34 = (float)Math.sqrt(2.0F * this.O.braking() * var32) - this.O.braking() * 0.5F;
         float var13 = Math.max(0.3F, Math.min(var33, Math.min(var34, var32 * this.O.share())));
         this.X = Math.min(var32, this.X + class_3532.method_15363(var13 - this.X, -this.O.braking() * 1.5F, this.O.acceleration()));
         float var14 = this.l > 0.0F ? class_3532.method_15363(1.0F - var32 / this.l, 0.0F, 1.0F) : 1.0F;
         float var15 = this.N * class_3532.method_15374((float) Math.PI * var14);
         float var16 = var28 / var32;
         float var17 = var30 / var32;
         float var18 = var16 - var17 * var15;
         float var19 = var17 + var16 * var15;
         float var20 = (float)Math.hypot(var18, var19);
         var6 = var18 / var20 * this.X + this.h.Y(com.larpclient.module.lq.g.h) * 0.04F * this.X;
         var7 = var19 / var20 * this.X + this.h.Y(com.larpclient.module.lq.g.u) * 0.03F * this.X + this.h.f(var18 / var20 * this.X) * 0.5F;
         var6 = U(var6, var28);
         var7 = U(var7, var30);
      }

      com.larpclient.module.l4 var29 = this.t.E(new com.larpclient.module.l4(var1, var2), new com.larpclient.module.l4(var1 + var6, var2 + var7));
      return new float[]{var29.j, var29.l};
   }

   private void G(float var1, float var2, float var3, float var4) {
      float var5 = class_3532.method_15393(var3 - var1);
      float var6 = var4 - var2;
      this.l = (float)Math.hypot(var5, var6);
      int var7 = var6 > 0.0F && var6 > Math.abs(var5) * 0.5F ? 1 : 0;
      this.s = (0.88F + (float)this.B.nextDouble() * 0.24F) * (var7 != 0 ? 0.92F : 1.0F);
      this.N = (float)(this.B.nextDouble() * 2.0 - 1.0) * 0.16F;
      if (this.O == u5.p.QUICK && this.l > 2.0F && !this.L) {
         double var14 = this.B.nextDouble() < 0.85 ? -(0.02 + this.B.nextDouble() * 0.1) * (var7 != 0 ? 1.3 : 1.0) : this.B.nextDouble() * 0.03;
         var14 = class_3532.method_15350(var14 * this.l, -6.0, 1.2F);
         double var10 = (this.B.nextDouble() * 2.0 - 1.0) * Math.abs(var14) / 2.25;
         float var12 = var5 / this.l;
         float var13 = var6 / this.l;
         this.U = (float)(var12 * var14 - var13 * var10);
         this.r = (float)(var13 * var14 + var12 * var10);
      } else {
         float var8 = Math.min(1.2F, this.l * 0.03F);
         this.U = (float)(this.B.nextDouble() * 2.0 - 1.0) * var8;
         this.r = (float)(this.B.nextDouble() * 2.0 - 1.0) * var8 * 0.7F;
      }

      this.j = this.Y = 0.0F;
      this.y = 0;
      this.c = (boolean)0;
      this.V = this.l > 8.0F ? this.B.nextInt(3) : 0;
   }

   private static float A(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = class_3532.method_15363(var0 * var4 * (1.0F + var5), -var2, var2);
      return U(var1 + class_3532.method_15363(var6 - var1, -var3, var3), var0);
   }

   private static float U(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float o() {
      return 0.8F + (float)this.B.nextDouble() * 1.2F;
   }

   public boolean V(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.u) {
            if (var1 <= W(var3) + 0.04) {
               this.u = (boolean)0;
            }
         } else if (var1 > 0.4) {
            if (this.W < 0) {
               this.W = this.B.nextInt(1, 5);
            } else if (--this.W <= 0) {
               this.u = (boolean)1;
               this.W = -1;
            }
         } else {
            this.W = -1;
         }

         return this.u;
      } else {
         this.u = (boolean)0;
         this.W = -1;
         return false;
      }
   }

   public boolean O() {
      return this.u;
   }

   public static double W(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public class_10185 V(float var1, class_243 var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.field_1350, var2.field_1352)) - 90.0;
      double var5 = class_3532.method_15338(var3 - var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.Z != Integer.MIN_VALUE && Math.abs(class_3532.method_15338(var5 - this.Z * 45.0)) < 28.5) {
         var7 = this.Z;
      }

      this.Z = var7;
      float[] var8 = AuraModule.V(var1 + var7 * 45.0, var1);
      return new class_10185(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static class_241 R(class_10185 var0) {
      float var1 = var0.comp_3159() == var0.comp_3160() ? 0.0F : (var0.comp_3159() ? 1.0F : -1.0F);
      float var2 = var0.comp_3161() == var0.comp_3162() ? 0.0F : (var0.comp_3161() ? 1.0F : -1.0F);
      return new class_241(var2, var1).method_35581();
   }

   public static double h(class_243 var0, class_243 var1, class_2350 var2) {
      return (var1.field_1352 - var0.field_1352) * var2.method_10148() + (var1.field_1350 - var0.field_1350) * var2.method_10165();
   }

   public static double D(class_243 var0, class_243 var1, class_2350 var2) {
      class_2350 var3 = var2.method_10170();
      return (var1.field_1352 - var0.field_1352) * var3.method_10148() + (var1.field_1350 - var0.field_1350) * var3.method_10165();
   }

   public static class_243 z(class_243 var0, class_243 var1, class_2350 var2) {
      class_2350 var3 = var2.method_10170();
      double var4 = D(var0, var1, var2);
      return new class_243(var2.method_10148() * 1.5 + var3.method_10148() * var4, 0.0, var2.method_10165() * 1.5 + var3.method_10165() * var4);
   }

   public record p(float speed, float acceleration, float braking, float share, boolean glide) {
      public static final u5.p STEADY = new u5.p(12.0F, 3.0F, 3.0F, 0.32F, false);
      public static final u5.p QUICK = new u5.p(18.0F, 4.5F, 4.0F, 0.45F, true);
   }
}

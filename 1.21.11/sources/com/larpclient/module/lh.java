package com.larpclient.module;

import java.util.SplittableRandom;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public final class lh implements z {
   private final SplittableRandom B = new SplittableRandom();
   private final lq e = new lq(lq.u);
   private final ld g = new ld();
   private boolean P;
   private float N;
   private float x;
   private float I;
   private float Y;
   private float L;
   private float y;
   private float i;
   private float Q;
   private int q;

   @Override
   public l4 rotate(l4 var1, l4 var2, class_1309 var3, boolean var4, boolean var5) {
      class_746 var6 = class_310.method_1551().field_1724;
      if (var1 != null && var2 != null && var3 != null && var6 != null) {
         int var7 = !var4 && var5 ? 0 : 1;
         this.e.c();
         double var8 = var3.method_23317() - var6.method_23317();
         double var10 = var3.method_23321() - var6.method_23321();
         double var12 = var8 * var8 + var10 * var10;
         float var14 = a_.H(var1.j, this.U(var1, var2, var12, (boolean)var7));
         float var15 = class_3532.method_15363(var2.l, -90.0F, 90.0F);
         float var16 = var14 - var1.j;
         float var17 = var15 - var1.l;
         float var18 = (float)Math.hypot(var16, var17);
         if (var7 != 0) {
            this.Q = 0.0F;
            this.q = 0;
         } else if (var18 > 25.0F && this.q <= 0 && this.B.nextDouble() < 0.18) {
            this.Q = (float)(this.B.nextDouble() * 2.5 - 1.25);
            this.q = this.B.nextInt(4, 10);
         } else if (this.q > 0) {
            this.q--;
            this.Q *= 0.65F;
         } else {
            this.Q = 0.0F;
         }

         float var19 = var7 != 0 ? 0.0F : this.P(var18, var12, var4, var5);
         this.F(var19);
         this.k(var3, var14 + this.Q, var15, var19);
         float var20 = this.e.v(var18, (boolean)var7);
         float var21 = this.e.Y(lq.g.h) * 0.07F;
         float var22 = this.e.Y(lq.g.u) * 0.055F;
         float var23 = this.e.M(lq.g.h, (boolean)var7);
         float var24 = this.e.M(lq.g.u, (boolean)var7);
         float var25 = this.J(this.y - var1.j, 20.0F, 54.0F, 42.0F, var20, var21, var23, (boolean)var7, var5);
         float var26 = this.J(this.i - var1.l, 14.0F, 42.0F, 28.0F, var20, var22, var24, (boolean)var7, var5);
         if (var7 == 0) {
            var26 += this.e.f(var25);
         }

         return new l4(var1.j + var25, class_3532.method_15363(var1.l + var26, -90.0F, 90.0F));
      } else {
         return var1 != null ? var1 : var2;
      }
   }

   public l4 c(l4 var1, l4 var2) {
      return this.g.E(var1, var2);
   }

   private float U(l4 var1, l4 var2, double var3, boolean var5) {
      if (!var5 && !(var3 >= 0.16F)) {
         return this.P ? this.N : var1.j;
      }

      this.N = var2.j;
      this.P = (boolean)1;
      return this.N;
   }

   private float P(float var1, double var2, boolean var4, boolean var5) {
      if (!(var2 < 0.16F) && (var4 || var5)) {
         float var6 = 1.0F - class_3532.method_15363(var1 / 14.0F, 0.0F, 1.0F);
         return var6 * var6 * (var4 ? 0.35F : 0.65F);
      } else {
         return 0.0F;
      }
   }

   private void F(float var1) {
      if (var1 <= 1.0E-5F) {
         this.x *= 0.5F;
         this.I *= 0.5F;
         this.Y = 0.0F;
         this.L = 0.0F;
      } else {
         float var2 = this.M() * (0.22F + var1 * 0.22F);
         float var3 = this.M() * (0.15F + var1 * 0.15F);
         this.Y = class_3532.method_15363(this.Y * 0.68F + var2, -0.65F, 0.65F);
         this.L = class_3532.method_15363(this.L * 0.68F + var3, -0.45F, 0.45F);
         this.x = class_3532.method_15363(this.x + this.Y, -4.1F, 4.1F);
         this.I = class_3532.method_15363(this.I + this.L, -2.85F, 2.85F);
      }
   }

   private void k(class_1309 var1, float var2, float var3, float var4) {
      if (var4 <= 1.0E-5F) {
         this.y = var2;
         this.i = var3;
      } else {
         float var5 = var2 + this.x * var4;
         float var6 = class_3532.method_15363(var3 + this.I * var4, -90.0F, 90.0F);
         if (c(var1, var5, var6)) {
            this.y = var5;
            this.i = var6;
         } else {
            this.x *= 0.5F;
            this.I *= 0.5F;
            var5 = var2 + this.x * var4;
            var6 = class_3532.method_15363(var3 + this.I * var4, -90.0F, 90.0F);
            if (c(var1, var5, var6)) {
               this.y = var5;
               this.i = var6;
            } else {
               this.x = this.I = this.Y = this.L = 0.0F;
               this.y = var2;
               this.i = var3;
            }
         }
      }
   }

   private float J(float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
      float var10 = Math.abs(var1);
      if (var10 <= 1.0E-5F) {
         return 0.0F;
      }

      float var11 = class_3532.method_15363(var10 / var4, 0.0F, 1.0F);
      float var12 = var11 * var11 * (3.0F - 2.0F * var11);
      float var13 = Math.min(var10, var2 + (var3 - var2) * var12);
      float var14;
      if (!var8 && var9) {
         float var15 = 0.78F + (float)this.B.nextDouble() * 0.22000003F;
         var14 = class_3532.method_15363(var15 * var5 * var7 + var6, 0.65F, 1.08F);
      } else {
         var14 = class_3532.method_15363(var7 + var6 * 0.18F, 0.92F, 1.15F);
      }

      float var16;
      if (!var8 && var9) {
         var16 = Math.min(var10, Math.min(var3, var13 * var14));
      } else {
         var16 = Math.min(var10, Math.min(var3, Math.max(var13 * var14, var10 * 0.93F)));
      }

      return Math.copySign(var16, var1);
   }

   private float M() {
      return (float)(this.B.nextDouble() * 2.0 - 1.0);
   }

   private static boolean c(class_1309 var0, float var1, float var2) {
      class_746 var3 = class_310.method_1551().field_1724;
      class_238 var4 = var0.method_5829();
      double var5 = Math.min(0.0125, Math.max(0.0, var4.method_17939() * 0.25));
      double var7 = Math.min(0.0125, Math.max(0.0, var4.method_17940() * 0.25));
      double var9 = Math.min(0.0125, Math.max(0.0, var4.method_17941() * 0.25));
      class_238 var11 = new class_238(
         var4.field_1323 + var5, var4.field_1322 + var7, var4.field_1321 + var9, var4.field_1320 - var5, var4.field_1325 - var7, var4.field_1324 - var9
      );
      class_243 var12 = var3.method_33571();
      if (var11.method_1006(var12)) {
         return true;
      }

      class_243 var13 = a_.w(var1, var2);
      double var14 = var12.method_1022(var11.method_1005()) + Math.max(var11.method_17939(), Math.max(var11.method_17940(), var11.method_17941())) + 1.0;
      return var11.method_992(var12, var12.method_1019(var13.method_1021(var14))).isPresent();
   }

   @Override
   public float yawSpeed() {
      return 22.0F;
   }

   @Override
   public float pitchSpeed() {
      return 14.0F;
   }

   @Override
   public void reset() {
      this.P = (boolean)0;
      this.N = 0.0F;
      this.x = this.I = this.Y = this.L = 0.0F;
      this.y = this.i = 0.0F;
      this.Q = 0.0F;
      this.q = 0;
      this.e.A();
      this.g.Y();
   }

   @Override
   public void targetChanged(class_1309 var1) {
      this.reset();
   }
}

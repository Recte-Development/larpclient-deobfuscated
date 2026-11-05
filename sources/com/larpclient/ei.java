package com.larpclient;

import com.larpclient.module.AuraModule;
import java.util.SplittableRandom;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class ei {
   static final double e = 0.546;
   static final double x = 0.4;
   static final double q = 1.5;
   private static final float a = 6.0F;
   private static final int w = Integer.MIN_VALUE;
   private final com.larpclient.module.lk I = new com.larpclient.module.lk(com.larpclient.module.lk.x);
   private final com.larpclient.module.lj S = new com.larpclient.module.lj();
   private final SplittableRandom W = new SplittableRandom();
   private float d;
   private float P;
   private float v;
   private boolean y;
   private boolean O;
   private int t = -1;
   private int h = Integer.MIN_VALUE;

   public ei() {
      this.A();
   }

   public void A() {
      this.d = this.P = 0.0F;
      this.y = this.O = (boolean)0;
      this.t = -1;
      this.h = Integer.MIN_VALUE;
      this.v = this.u();
      this.I.c();
      this.S.y();
   }

   public float[] Y(float var1, float var2, float var3, float var4, boolean var5) {
      float var6 = Mth.wrapDegrees(var3 - var1);
      float var7 = Mth.clamp(var4, -90.0F, 90.0F) - var2;
      float var8 = (float)Math.hypot(var6, var7);
      if (!var5 || !(var8 <= this.v) && (!this.y || !(var8 <= 3.5F))) {
         this.y = (boolean)0;
         int var9 = var8 < 6.0F ? 1 : 0;
         this.I.Q();
         float var10 = this.I.I(var8, (boolean)var9);
         this.d = x(var6, this.d, 12.0F * this.I.S(com.larpclient.module.lk.o.W, (boolean)var9) * var10, 3.0F, this.I.w(com.larpclient.module.lk.o.W) * 0.08F);
         this.P = x(var7, this.P, 9.0F * this.I.S(com.larpclient.module.lk.o.Z, (boolean)var9) * var10, 2.25F, this.I.w(com.larpclient.module.lk.o.Z) * 0.06F);
         this.P = L(this.P + this.I.D(this.d), var7);
         com.larpclient.module.lx var11 = this.S.Q(new com.larpclient.module.lx(var1, var2), new com.larpclient.module.lx(var1 + this.d, var2 + this.P));
         return new float[]{var11.z, var11.I};
      }

      if (!this.y) {
         this.v = this.u();
      }

      this.y = (boolean)1;
      this.d = this.P = 0.0F;
      return new float[]{var1, var2};
   }

   private static float x(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Mth.clamp(var0 * 0.32F * (1.0F + var4), -var2, var2);
      return L(var1 + Mth.clamp(var5 - var1, -var3, var3), var0);
   }

   private static float L(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float u() {
      return 0.8F + (float)this.W.nextDouble() * 1.2F;
   }

   public boolean m(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.O) {
            if (var1 <= V(var3) + 0.04) {
               this.O = (boolean)0;
            }
         } else if (var1 > 0.4) {
            if (this.t < 0) {
               this.t = this.W.nextInt(1, 5);
            } else if (--this.t <= 0) {
               this.O = (boolean)1;
               this.t = -1;
            }
         } else {
            this.t = -1;
         }

         return this.O;
      } else {
         this.O = (boolean)0;
         this.t = -1;
         return false;
      }
   }

   public boolean F() {
      return this.O;
   }

   public static double V(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public Input c(float var1, Vec3 var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0;
      double var5 = Mth.wrapDegrees(var3 - var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.h != Integer.MIN_VALUE && Math.abs(Mth.wrapDegrees(var5 - this.h * 45.0)) < 28.5) {
         var7 = this.h;
      }

      this.h = var7;
      float[] var8 = AuraModule.g(var1 + var7 * 45.0, var1);
      return new Input(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static Vec2 r(Input var0) {
      float var1 = var0.forward() == var0.backward() ? 0.0F : (var0.forward() ? 1.0F : -1.0F);
      float var2 = var0.left() == var0.right() ? 0.0F : (var0.left() ? 1.0F : -1.0F);
      return new Vec2(var2, var1).normalized();
   }

   public static double k(Vec3 var0, Vec3 var1, Direction var2) {
      return (var1.x - var0.x) * var2.getStepX() + (var1.z - var0.z) * var2.getStepZ();
   }

   public static double x(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      return (var1.x - var0.x) * var3.getStepX() + (var1.z - var0.z) * var3.getStepZ();
   }

   public static Vec3 r(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      double var4 = x(var0, var1, var2);
      return new Vec3(var2.getStepX() * 1.5 + var3.getStepX() * var4, 0.0, var2.getStepZ() * 1.5 + var3.getStepZ() * var4);
   }
}

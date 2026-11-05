package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.d5;
import java.util.SplittableRandom;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class ob {
   static final double P = 0.546;
   static final double J = 0.4;
   static final double g = 1.5;
   private static final float j = 6.0F;
   private static final int H = Integer.MIN_VALUE;
   private final com.larpclient.module.d8 R = new com.larpclient.module.d8(com.larpclient.module.d8.D);
   private final com.larpclient.module.a d = new com.larpclient.module.a();
   private final SplittableRandom M = new SplittableRandom();
   private float F;
   private float w;
   private float N;
   private boolean X;
   private boolean n;
   private int o = -1;
   private int Z = Integer.MIN_VALUE;

   public ob() {
      this.x();
   }

   public void x() {
      this.F = this.w = 0.0F;
      this.X = this.n = (boolean)0;
      this.o = -1;
      this.Z = Integer.MIN_VALUE;
      this.N = this.C();
      this.R.F();
      this.d.P();
   }

   public float[] Z(float var1, float var2, float var3, float var4, boolean var5) {
      float var6 = Mth.wrapDegrees(var3 - var1);
      float var7 = Mth.clamp(var4, -90.0F, 90.0F) - var2;
      float var8 = (float)Math.hypot(var6, var7);
      if (!var5 || !(var8 <= this.N) && (!this.X || !(var8 <= 3.5F))) {
         this.X = (boolean)0;
         int var9 = var8 < 6.0F ? 1 : 0;
         this.R.q();
         float var10 = this.R.g(var8, (boolean)var9);
         this.F = B(var6, this.F, 12.0F * this.R.O(com.larpclient.module.d8.a.G, (boolean)var9) * var10, 3.0F, this.R.c(com.larpclient.module.d8.a.G) * 0.08F);
         this.w = B(var7, this.w, 9.0F * this.R.O(com.larpclient.module.d8.a.L, (boolean)var9) * var10, 2.25F, this.R.c(com.larpclient.module.d8.a.L) * 0.06F);
         this.w = B(this.w + this.R.Q(this.F), var7);
         d5 var11 = this.d.T(new d5(var1, var2), new d5(var1 + this.F, var2 + this.w));
         return new float[]{var11.M, var11.G};
      }

      if (!this.X) {
         this.N = this.C();
      }

      this.X = (boolean)1;
      this.F = this.w = 0.0F;
      return new float[]{var1, var2};
   }

   private static float B(float var0, float var1, float var2, float var3, float var4) {
      float var5 = Mth.clamp(var0 * 0.32F * (1.0F + var4), -var2, var2);
      return B(var1 + Mth.clamp(var5 - var1, -var3, var3), var0);
   }

   private static float B(float var0, float var1) {
      return Math.signum(var0) != Math.signum(var1) ? 0.0F : Math.copySign(Math.min(Math.abs(var0), Math.abs(var1)), var1);
   }

   private float C() {
      return 0.8F + (float)this.M.nextDouble() * 1.2F;
   }

   public boolean d(double var1, double var3, boolean var5) {
      if (var5 && !(var1 <= 0.0)) {
         if (this.n) {
            if (var1 <= Y(var3) + 0.04) {
               this.n = (boolean)0;
            }
         } else if (var1 > 0.4) {
            if (this.o < 0) {
               this.o = this.M.nextInt(1, 5);
            } else if (--this.o <= 0) {
               this.n = (boolean)1;
               this.o = -1;
            }
         } else {
            this.o = -1;
         }

         return this.n;
      } else {
         this.n = (boolean)0;
         this.o = -1;
         return false;
      }
   }

   public boolean c() {
      return this.n;
   }

   public static double Y(double var0) {
      return Math.max(0.0, var0) * 0.546 / 0.45399999999999996;
   }

   public Input M(float var1, Vec3 var2) {
      double var3 = Math.toDegrees(Math.atan2(var2.z, var2.x)) - 90.0;
      double var5 = Mth.wrapDegrees(var3 - var1);
      int var7 = (int)Math.round(var5 / 45.0);
      if (var7 == -4) {
         var7 = 4;
      }

      if (this.Z != Integer.MIN_VALUE && Math.abs(Mth.wrapDegrees(var5 - this.Z * 45.0)) < 28.5) {
         var7 = this.Z;
      }

      this.Z = var7;
      float[] var8 = AuraModule.l(var1 + var7 * 45.0, var1);
      return new Input(var8[0] > 0.0F, var8[0] < 0.0F, var8[1] > 0.0F, var8[1] < 0.0F, false, false, false);
   }

   public static Vec2 M(Input var0) {
      float var1 = var0.forward() == var0.backward() ? 0.0F : (var0.forward() ? 1.0F : -1.0F);
      float var2 = var0.left() == var0.right() ? 0.0F : (var0.left() ? 1.0F : -1.0F);
      return new Vec2(var2, var1).normalized();
   }

   public static double a(Vec3 var0, Vec3 var1, Direction var2) {
      return (var1.x - var0.x) * var2.getStepX() + (var1.z - var0.z) * var2.getStepZ();
   }

   public static double P(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      return (var1.x - var0.x) * var3.getStepX() + (var1.z - var0.z) * var3.getStepZ();
   }

   public static Vec3 U(Vec3 var0, Vec3 var1, Direction var2) {
      Direction var3 = var2.getClockWise();
      double var4 = P(var0, var1, var2);
      return new Vec3(var2.getStepX() * 1.5 + var3.getStepX() * var4, 0.0, var2.getStepZ() * 1.5 + var3.getStepZ() * var4);
   }
}

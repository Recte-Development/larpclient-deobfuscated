package com.larpclient;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class hs {
   private Input Z = Input.EMPTY;
   private int m;
   private double g = 1.0;

   public static Vec3 E(float var0, Input var1, double var2, double var4, boolean var6) {
      double var7 = j(var1.forward(), var1.backward());
      double var9 = j(var1.left(), var1.right());
      double var11 = j(var1.jump(), var1.shift());
      double var13 = Math.toRadians(var0);
      double var15 = var2 * (var6 ? 0.5 : 1.0);
      return new Vec3(
         (-Math.sin(var13) * var7 + Math.cos(var13) * var9) * var15, var11 * var15 * var4, (Math.cos(var13) * var7 + Math.sin(var13) * var9) * var15
      );
   }

   public void b(Input var1, boolean var2, boolean var3) {
      this.M();
      this.Z = var3 ? var1 : Input.EMPTY;
      if (var2) {
         this.Z = b(this.Z, new Input(false, false, false, false, false, true, false));
      }

      this.m = 40;
   }

   public void O(Input var1, boolean var2, boolean var3, boolean var4, Vec2 var5, Vec3 var6, float var7, boolean var8) {
      if (!var8) {
         if (this.m <= 0) {
            return;
         }

         this.m--;
      }

      if (var2 && var3) {
         this.Z = b(this.Z, var1);
      }

      if (var4) {
         this.Z = b(this.Z, new Input(false, false, false, false, false, true, false));
      }

      if (var2) {
         this.Z = b(this.Z, new Input(var5.y > 0.02, var5.y < -0.02, var5.x > 0.02, var5.x < -0.02, false, false, false));
         if (!var8 || !this.Z.forward() && !this.Z.backward() && !this.Z.left() && !this.Z.right()) {
            if (!(var6.horizontalDistanceSqr() < 1.0E-8)) {
               double var9 = Math.toRadians(var7);
               double var11 = -var6.x * Math.sin(var9) + var6.z * Math.cos(var9);
               double var13 = -var6.z * Math.sin(var9) - var6.x * Math.cos(var9);
               int var15 = Math.abs(var11) >= Math.abs(var13) ? 1 : 0;
               this.Z = b(
                  this.Z,
                  new Input(
                     var15 != 0 && var11 > 0.01, var15 != 0 && var11 < -0.01, var15 == 0 && var13 > 0.01, var15 == 0 && var13 < -0.01, false, false, false
                  )
               );
            }
         }
      }
   }

   public Input K(Input var1, boolean var2) {
      Input var3 = var2 ? b(this.Z, new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), false, var1.shift(), var1.sprint())) : this.Z;
      if (var2) {
         this.Z = new Input(
            this.Z.forward() && !var1.backward(),
            this.Z.backward() && !var1.forward(),
            this.Z.left() && !var1.right(),
            this.Z.right() && !var1.left(),
            this.Z.jump(),
            this.Z.shift(),
            this.Z.sprint()
         );
      }

      return var3;
   }

   public static Vec2 y(Input var0) {
      return new Vec2(j(var0.left(), var0.right()), j(var0.forward(), var0.backward())).normalized();
   }

   public boolean v(double var1, double var3, boolean var5) {
      if (Double.isFinite(var1) && var1 != 0.0) {
         if (var5) {
            this.g = Math.clamp(this.g * (var1 > 0.0 ? 1.2 : 0.8333333333333334), 0.1 / var3, 50.0 / var3);
         }

         return true;
      } else {
         return false;
      }
   }

   public double u(double var1, boolean var3) {
      return var3 ? Math.clamp(var1 * this.g, 0.1, 50.0) : var1;
   }

   public void Q() {
      this.g = 1.0;
   }

   public void t() {
      this.Z = Input.EMPTY;
      this.m = 0;
   }

   public void M() {
      this.t();
      this.Q();
   }

   public static hs.n B(Vec3 var0, int var1, int var2, int var3, int var4) {
      int var5 = Mth.floor(var0.x) >> 4;
      int var6 = Mth.floor(var0.z) >> 4;
      int var7 = Math.max(Math.abs(var5 - var1), Math.abs(var6 - var2));
      int var8 = Math.clamp(var3, 2, 32);
      int var9 = Math.max(2, var4);
      int var10 = Math.max(var9, Math.clamp(Math.max(var8, var7 / 2 + 3), 2, 32));
      int var11 = var7 > var8 ? var1 + (var5 - var1) / 2 : var5;
      int var12 = var7 > var8 ? var2 + (var6 - var2) / 2 : var6;
      int var13 = var10 - var9;
      return new hs.n(Math.clamp(var11, var1 - var13, var1 + var13), Math.clamp(var12, var2 - var13, var2 + var13), var10);
   }

   private static float j(boolean var0, boolean var1) {
      return (var0 ? 1 : 0) - (var1 ? 1 : 0);
   }

   public static Input b(Input var0, Input var1) {
      return new Input(
         var0.forward() || var1.forward(),
         var0.backward() || var1.backward(),
         var0.left() || var1.left(),
         var0.right() || var1.right(),
         var0.jump() || var1.jump(),
         var0.shift() || var1.shift(),
         var0.sprint() || var1.sprint()
      );
   }

   public record n(int x, int z, int radius) {
   }
}

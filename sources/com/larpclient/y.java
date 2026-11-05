package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class y {
   private AABB X;
   private AABB Z;
   private double v;
   private double r;
   private double p;
   private double y;
   private double W;

   public void h() {
      this.X = this.Z = null;
      this.p = this.y = 0.0;
   }

   public y.f A(double var1) {
      if (this.Z == null) {
         return new y.f(null, 0.0);
      }

      double var3 = p(this.r <= 0.0 ? 1.0 : (var1 - this.v) / this.r);
      double var5 = p((var1 - this.W) / 0.1);
      return new y.f(
         new AABB(
            k(this.X.minX, this.Z.minX, var3),
            k(this.X.minY, this.Z.minY, var3),
            k(this.X.minZ, this.Z.minZ, var3),
            k(this.X.maxX, this.Z.maxX, var3),
            k(this.X.maxY, this.Z.maxY, var3),
            k(this.X.maxZ, this.Z.maxZ, var3)
         ),
         k(this.p, this.y, var5)
      );
   }

   public y.f E(AABB var1, double var2, double var4) {
      y.f var6 = this.A(var2);
      if (var1 != null && !var1.equals(this.Z)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.getCenter().distanceTo(var6.bounds.getCenter()) > 8.0) ? 0 : 1;
         this.X = var7 != 0 ? var1 : var6.bounds;
         this.Z = var1;
         this.v = var2;
         this.r = Math.max(0.0, var4);
         if (var7 != 0) {
            this.p = 0.0;
            this.y = 1.0;
            this.W = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.y) {
         this.p = var6.alpha;
         this.y = var9;
         this.W = var2;
      }

      return this.A(var2);
   }

   private static double p(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double k(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record f(AABB bounds, double alpha) {
   }
}

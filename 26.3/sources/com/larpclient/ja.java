package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class ja {
   private AABB C;
   private AABB E;
   private double I;
   private double n;
   private double x;
   private double J;
   private double o;

   public void F() {
      this.C = this.E = null;
      this.x = this.J = 0.0;
   }

   public ja.i E(double var1) {
      if (this.E == null) {
         return new ja.i(null, 0.0);
      }

      double var3 = u(this.n <= 0.0 ? 1.0 : (var1 - this.I) / this.n);
      double var5 = u((var1 - this.o) / 0.1);
      return new ja.i(
         new AABB(
            F(this.C.minX, this.E.minX, var3),
            F(this.C.minY, this.E.minY, var3),
            F(this.C.minZ, this.E.minZ, var3),
            F(this.C.maxX, this.E.maxX, var3),
            F(this.C.maxY, this.E.maxY, var3),
            F(this.C.maxZ, this.E.maxZ, var3)
         ),
         F(this.x, this.J, var5)
      );
   }

   public ja.i Q(AABB var1, double var2, double var4) {
      ja.i var6 = this.E(var2);
      if (var1 != null && !var1.equals(this.E)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.getCenter().distanceTo(var6.bounds.getCenter()) > 8.0) ? 0 : 1;
         this.C = var7 != 0 ? var1 : var6.bounds;
         this.E = var1;
         this.I = var2;
         this.n = Math.max(0.0, var4);
         if (var7 != 0) {
            this.x = 0.0;
            this.J = 1.0;
            this.o = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.J) {
         this.x = var6.alpha;
         this.J = var9;
         this.o = var2;
      }

      return this.E(var2);
   }

   private static double u(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double F(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record i(AABB bounds, double alpha) {
   }
}

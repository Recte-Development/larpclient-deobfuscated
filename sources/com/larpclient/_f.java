package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class _f {
   private AABB x;
   private AABB h;
   private double C;
   private double u;
   private double O;
   private double r;
   private double R;

   public void t() {
      this.x = this.h = null;
      this.O = this.r = 0.0;
   }

   public _f.e v(double var1) {
      if (this.h == null) {
         return new _f.e(null, 0.0);
      }

      double var3 = A(this.u <= 0.0 ? 1.0 : (var1 - this.C) / this.u);
      double var5 = A((var1 - this.R) / 0.1);
      return new _f.e(
         new AABB(
            k(this.x.minX, this.h.minX, var3),
            k(this.x.minY, this.h.minY, var3),
            k(this.x.minZ, this.h.minZ, var3),
            k(this.x.maxX, this.h.maxX, var3),
            k(this.x.maxY, this.h.maxY, var3),
            k(this.x.maxZ, this.h.maxZ, var3)
         ),
         k(this.O, this.r, var5)
      );
   }

   public _f.e A(AABB var1, double var2, double var4) {
      _f.e var6 = this.v(var2);
      if (var1 != null && !var1.equals(this.h)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.getCenter().distanceTo(var6.bounds.getCenter()) > 8.0) ? 0 : 1;
         this.x = var7 != 0 ? var1 : var6.bounds;
         this.h = var1;
         this.C = var2;
         this.u = Math.max(0.0, var4);
         if (var7 != 0) {
            this.O = 0.0;
            this.r = 1.0;
            this.R = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.r) {
         this.O = var6.alpha;
         this.r = var9;
         this.R = var2;
      }

      return this.v(var2);
   }

   private static double A(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double k(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record e(AABB bounds, double alpha) {
   }
}

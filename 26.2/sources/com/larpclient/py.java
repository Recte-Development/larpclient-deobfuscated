package com.larpclient;

import net.minecraft.world.phys.AABB;

public final class py {
   private AABB n;
   private AABB V;
   private double q;
   private double r;
   private double K;
   private double c;
   private double Q;

   public void v() {
      this.n = this.V = null;
      this.K = this.c = 0.0;
   }

   public py.o Y(double var1) {
      if (this.V == null) {
         return new py.o(null, 0.0);
      }

      double var3 = G(this.r <= 0.0 ? 1.0 : (var1 - this.q) / this.r);
      double var5 = G((var1 - this.Q) / 0.1);
      return new py.o(
         new AABB(
            h(this.n.minX, this.V.minX, var3),
            h(this.n.minY, this.V.minY, var3),
            h(this.n.minZ, this.V.minZ, var3),
            h(this.n.maxX, this.V.maxX, var3),
            h(this.n.maxY, this.V.maxY, var3),
            h(this.n.maxZ, this.V.maxZ, var3)
         ),
         h(this.K, this.c, var5)
      );
   }

   public py.o q(AABB var1, double var2, double var4) {
      py.o var6 = this.Y(var2);
      if (var1 != null && !var1.equals(this.V)) {
         int var7 = var6.bounds != null && !(var6.alpha < 0.001) && !(var1.getCenter().distanceTo(var6.bounds.getCenter()) > 8.0) ? 0 : 1;
         this.n = var7 != 0 ? var1 : var6.bounds;
         this.V = var1;
         this.q = var2;
         this.r = Math.max(0.0, var4);
         if (var7 != 0) {
            this.K = 0.0;
            this.c = 1.0;
            this.Q = var2;
         }
      }

      double var9 = var1 == null ? 0.0 : 1.0;
      if (var9 != this.c) {
         this.K = var6.alpha;
         this.c = var9;
         this.Q = var2;
      }

      return this.Y(var2);
   }

   private static double G(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   private static double h(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public record o(AABB bounds, double alpha) {
   }
}

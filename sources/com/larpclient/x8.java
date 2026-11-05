package com.larpclient;

import java.util.Random;

public final class x8 {
   public static final int R = 160;
   private final x8.p[] L = new x8.p[160];
   private long W;
   private double j;

   public x8() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.L.length; var2++) {
         this.L[var2] = new x8.p(var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0);
      }
   }

   public void Q(long var1, boolean var3) {
      if (var3 && this.W != 0L && var1 > this.W && var1 - this.W <= 500000000L) {
         this.j = this.j + Math.min(0.1, (var1 - this.W) / 1.0E9);
      }

      this.W = var1;
   }

   public double J() {
      return this.j;
   }

   public x8.p u(int var1) {
      return this.L[var1];
   }

   public static double Q(double var0) {
      return var0 - Math.floor(var0);
   }

   public record p(double x, double y, double depth, double phase) {
   }
}

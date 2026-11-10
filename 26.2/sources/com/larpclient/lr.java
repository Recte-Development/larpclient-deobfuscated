package com.larpclient;

import java.util.Random;

public final class lr {
   public static final int S = 160;
   private final lr.j[] G = new lr.j[160];
   private long D;
   private double r;

   public lr() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.G.length; var2++) {
         this.G[var2] = new lr.j(var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0);
      }
   }

   public void H(long var1, boolean var3) {
      if (var3 && this.D != 0L && var1 > this.D && var1 - this.D <= 500000000L) {
         this.r = this.r + Math.min(0.1, (var1 - this.D) / 1.0E9);
      }

      this.D = var1;
   }

   public double x() {
      return this.r;
   }

   public lr.j L(int var1) {
      return this.G[var1];
   }

   public static double G(double var0) {
      return var0 - Math.floor(var0);
   }

   public record j(double x, double y, double depth, double phase) {
   }
}

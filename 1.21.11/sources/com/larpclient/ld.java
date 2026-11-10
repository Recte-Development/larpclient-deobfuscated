package com.larpclient;

import java.util.Random;

public final class ld {
   public static final int b = 160;
   private final ld.a[] W = new ld.a[160];
   private long A;
   private double x;

   public ld() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.W.length; var2++) {
         this.W[var2] = new ld.a(var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0);
      }
   }

   public void X(long var1, boolean var3) {
      if (var3 && this.A != 0L && var1 > this.A && var1 - this.A <= 500000000L) {
         this.x = this.x + Math.min(0.1, (var1 - this.A) / 1.0E9);
      }

      this.A = var1;
   }

   public double F() {
      return this.x;
   }

   public ld.a b(int var1) {
      return this.W[var1];
   }

   public static double T(double var0) {
      return var0 - Math.floor(var0);
   }

   public record a(double x, double y, double depth, double phase) {
   }
}

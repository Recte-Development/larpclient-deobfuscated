package com.larpclient;

import java.util.Random;

public final class e7 {
   public static final int J = 160;
   private final e7.s[] u = new e7.s[160];
   private long v;
   private double c;

   public e7() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.u.length; var2++) {
         this.u[var2] = new e7.s(var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0);
      }
   }

   public void Q(long var1, boolean var3) {
      if (var3 && this.v != 0L && var1 > this.v && var1 - this.v <= 500000000L) {
         this.c = this.c + Math.min(0.1, (var1 - this.v) / 1.0E9);
      }

      this.v = var1;
   }

   public double i() {
      return this.c;
   }

   public e7.s I(int var1) {
      return this.u[var1];
   }

   public static double l(double var0) {
      return var0 - Math.floor(var0);
   }

   public record s(double x, double y, double depth, double phase) {
   }
}

package com.larpclient;

import java.util.Random;

public final class s2 {
   public static final int H = 160;
   private final s2.i[] f = new s2.i[160];
   private long C;
   private double z;

   public s2() {
      Random var1 = new Random(14044L);

      for (int var2 = 0; var2 < this.f.length; var2++) {
         this.f[var2] = new s2.i(var1.nextDouble(), var1.nextDouble(), 0.25 + var1.nextDouble() * 0.75, var1.nextDouble() * Math.PI * 2.0);
      }
   }

   public void k(long var1, boolean var3) {
      if (var3 && this.C != 0L && var1 > this.C && var1 - this.C <= 500000000L) {
         this.z = this.z + Math.min(0.1, (var1 - this.C) / 1.0E9);
      }

      this.C = var1;
   }

   public double A() {
      return this.z;
   }

   public s2.i i(int var1) {
      return this.f[var1];
   }

   public static double S(double var0) {
      return var0 - Math.floor(var0);
   }

   public record i(double x, double y, double depth, double phase) {
   }
}

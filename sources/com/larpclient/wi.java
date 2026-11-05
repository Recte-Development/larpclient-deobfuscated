package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class wi {
   public static final int f = 4;
   public static final double b = 0.06;
   public static final double K = 0.06;
   public static final double A = 0.18;
   private final List<wi.r> Z = new ArrayList<>(4);
   private double L = Double.NaN;

   public void P() {
      this.Z.clear();
      this.L = Double.NaN;
   }

   public List<wi.k> T(BlockPos var1, double var2) {
      return this.i(var1, var2, 0.06, 0.18);
   }

   public List<wi.k> i(BlockPos var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.L || var2 - this.L > 1.0) {
         this.P();
      }

      this.L = var2;
      this.Z.removeIf(var4x -> var2 - var4x.v >= var8);
      if (var1 != null) {
         wi.r var10 = this.Z.stream().filter(var1x -> var1x.p.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.Z.size() == 4) {
               this.Z.removeFirst();
            }

            this.Z.add(new wi.r(var1, var2));
         } else {
            if (var2 - var10.v > var4) {
               var10.i = M(var10, var2, var4, var6);
               var10.x = var2;
            }

            var10.v = var2;
            this.Z.remove(var10);
            this.Z.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.Z.size());

      for (wi.r var12 : this.Z) {
         double var13 = M(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new wi.k(var12.p, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double M(wi.r var0, double var1, double var3, double var5) {
      return (var0.i + (1.0 - var0.i) * a((var1 - var0.x) / 0.06)) * (1.0 - a((var1 - var0.v - var3) / var5));
   }

   private static double a(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public record k(BlockPos pos, double alpha) {
   }

   private static final class r {
      final BlockPos p;
      double x;
      double i;
      double v;

      r(BlockPos var1, double var2) {
         this.p = var1.immutable();
         this.x = this.v = var2;
      }
   }
}

package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class d7 {
   public static final int x = 4;
   public static final double T = 0.06;
   public static final double R = 0.06;
   public static final double q = 0.18;
   private final List<d7.c> K = new ArrayList<>(4);
   private double W = Double.NaN;

   public void n() {
      this.K.clear();
      this.W = Double.NaN;
   }

   public List<d7.r> n(BlockPos var1, double var2) {
      return this.P(var1, var2, 0.06, 0.18);
   }

   public List<d7.r> P(BlockPos var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.W || var2 - this.W > 1.0) {
         this.n();
      }

      this.W = var2;
      this.K.removeIf(var4x -> var2 - var4x.j >= var8);
      if (var1 != null) {
         d7.c var10 = this.K.stream().filter(var1x -> var1x.M.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.K.size() == 4) {
               this.K.removeFirst();
            }

            this.K.add(new d7.c(var1, var2));
         } else {
            if (var2 - var10.j > var4) {
               var10.E = I(var10, var2, var4, var6);
               var10.J = var2;
            }

            var10.j = var2;
            this.K.remove(var10);
            this.K.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.K.size());

      for (d7.c var12 : this.K) {
         double var13 = I(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new d7.r(var12.M, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double I(d7.c var0, double var1, double var3, double var5) {
      return (var0.E + (1.0 - var0.E) * C((var1 - var0.J) / 0.06)) * (1.0 - C((var1 - var0.j - var3) / var5));
   }

   private static double C(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static final class c {
      final BlockPos M;
      double J;
      double E;
      double j;

      c(BlockPos var1, double var2) {
         this.M = var1.immutable();
         this.J = this.j = var2;
      }
   }

   public record r(BlockPos pos, double alpha) {
   }
}

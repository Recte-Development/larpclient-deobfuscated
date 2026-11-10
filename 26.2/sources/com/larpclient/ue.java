package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class ue {
   public static final int A = 4;
   public static final double a = 0.06;
   public static final double o = 0.06;
   public static final double S = 0.18;
   private final List<ue.n> M = new ArrayList<>(4);
   private double G = Double.NaN;

   public void Y() {
      this.M.clear();
      this.G = Double.NaN;
   }

   public List<ue.l> T(BlockPos var1, double var2) {
      return this.N(var1, var2, 0.06, 0.18);
   }

   public List<ue.l> N(BlockPos var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.G || var2 - this.G > 1.0) {
         this.Y();
      }

      this.G = var2;
      this.M.removeIf(var4x -> var2 - var4x.N >= var8);
      if (var1 != null) {
         ue.n var10 = this.M.stream().filter(var1x -> var1x.o.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.M.size() == 4) {
               this.M.removeFirst();
            }

            this.M.add(new ue.n(var1, var2));
         } else {
            if (var2 - var10.N > var4) {
               var10.d = n(var10, var2, var4, var6);
               var10.m = var2;
            }

            var10.N = var2;
            this.M.remove(var10);
            this.M.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.M.size());

      for (ue.n var12 : this.M) {
         double var13 = n(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new ue.l(var12.o, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double n(ue.n var0, double var1, double var3, double var5) {
      return (var0.d + (1.0 - var0.d) * l((var1 - var0.m) / 0.06)) * (1.0 - l((var1 - var0.N - var3) / var5));
   }

   private static double l(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public record l(BlockPos pos, double alpha) {
   }

   private static final class n {
      final BlockPos o;
      double m;
      double d;
      double N;

      n(BlockPos var1, double var2) {
         this.o = var1.immutable();
         this.m = this.N = var2;
      }
   }
}

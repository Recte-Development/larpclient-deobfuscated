package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2338;

public final class e2 {
   public static final int O = 4;
   public static final double n = 0.06;
   public static final double h = 0.06;
   public static final double L = 0.18;
   private final List<e2.c> y = new ArrayList<>(4);
   private double C = Double.NaN;

   public void j() {
      this.y.clear();
      this.C = Double.NaN;
   }

   public List<e2.w> d(class_2338 var1, double var2) {
      return this.B(var1, var2, 0.06, 0.18);
   }

   public List<e2.w> B(class_2338 var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.C || var2 - this.C > 1.0) {
         this.j();
      }

      this.C = var2;
      this.y.removeIf(var4x -> var2 - var4x.W >= var8);
      if (var1 != null) {
         e2.c var10 = this.y.stream().filter(var1x -> var1x.k.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.y.size() == 4) {
               this.y.removeFirst();
            }

            this.y.add(new e2.c(var1, var2));
         } else {
            if (var2 - var10.W > var4) {
               var10.G = T(var10, var2, var4, var6);
               var10.C = var2;
            }

            var10.W = var2;
            this.y.remove(var10);
            this.y.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.y.size());

      for (e2.c var12 : this.y) {
         double var13 = T(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new e2.w(var12.k, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double T(e2.c var0, double var1, double var3, double var5) {
      return (var0.G + (1.0 - var0.G) * h((var1 - var0.C) / 0.06)) * (1.0 - h((var1 - var0.W - var3) / var5));
   }

   private static double h(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static final class c {
      final class_2338 k;
      double C;
      double G;
      double W;

      c(class_2338 var1, double var2) {
         this.k = var1.method_10062();
         this.C = this.W = var2;
      }
   }

   public record w(class_2338 pos, double alpha) {
   }
}

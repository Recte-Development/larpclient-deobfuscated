package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class jb {
   public static final int b = 4;
   public static final double w = 0.06;
   public static final double B = 0.06;
   public static final double Z = 0.18;
   private final List<jb.z> S = new ArrayList<>(4);
   private double Y = Double.NaN;

   public void E() {
      this.S.clear();
      this.Y = Double.NaN;
   }

   public List<jb.r> H(BlockPos var1, double var2) {
      return this.B(var1, var2, 0.06, 0.18);
   }

   public List<jb.r> B(BlockPos var1, double var2, double var4, double var6) {
      var4 = Math.clamp(var4, 0.0, 3.0);
      var6 = Math.clamp(var6, 0.01, 2.0);
      double var8 = var4 + var6;
      if (var2 < this.Y || var2 - this.Y > 1.0) {
         this.E();
      }

      this.Y = var2;
      this.S.removeIf(var4x -> var2 - var4x.B >= var8);
      if (var1 != null) {
         jb.z var10 = this.S.stream().filter(var1x -> var1x.U.equals(var1)).findFirst().orElse(null);
         if (var10 == null) {
            if (this.S.size() == 4) {
               this.S.removeFirst();
            }

            this.S.add(new jb.z(var1, var2));
         } else {
            if (var2 - var10.B > var4) {
               var10.h = l(var10, var2, var4, var6);
               var10.E = var2;
            }

            var10.B = var2;
            this.S.remove(var10);
            this.S.add(var10);
         }
      }

      ArrayList var17 = new ArrayList(this.S.size());

      for (jb.z var12 : this.S) {
         double var13 = l(var12, var2, var4, var6);
         if (var13 > 0.001) {
            var17.add(new jb.r(var12.U, var13));
         }
      }

      return List.copyOf(var17);
   }

   private static double l(jb.z var0, double var1, double var3, double var5) {
      return (var0.h + (1.0 - var0.h) * H((var1 - var0.E) / 0.06)) * (1.0 - H((var1 - var0.B - var3) / var5));
   }

   private static double H(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   public record r(BlockPos pos, double alpha) {
   }

   private static final class z {
      final BlockPos U;
      double E;
      double h;
      double B;

      z(BlockPos var1, double var2) {
         this.U = var1.immutable();
         this.E = this.B = var2;
      }
   }
}

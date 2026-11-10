package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2346;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3486;
import net.minecraft.class_3610;
import net.minecraft.class_437;
import net.minecraft.class_2338.class_2339;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_2350.class_2353;

public final class cm implements ig.j {
   private static final byte I = 1;
   private static final byte n = 2;
   private static final byte o = 4;
   private static final byte s = 8;
   private static final byte t = 16;
   private static final byte u = 32;
   static final double G = 4.6;
   private final class_1937 S;
   private final Map<Long, Byte> m = new HashMap<>();
   private final Map<Long, Double> d = new HashMap<>();
   private final Set<Long> b = new HashSet<>();
   private final List<double[]> N = new ArrayList<>();
   static final double A = 30.0;
   private final class_2339 j = new class_2339();
   private final ToIntFunction<class_2680> X;
   private final boolean F;
   private int[] L;
   private Set<class_2338> v;

   static class_437 j() {
      return class_310.method_1551().field_1755;
   }

   public cm(ToIntFunction<class_2680> var1, boolean var2) {
      this.S = class_310.method_1551().field_1687;
      this.X = var1;
      this.F = var2;
   }

   public cm j(double var1, double var3, double var5, double var7) {
      this.N.add(new double[]{var1, var3, var5, var7});
      return this;
   }

   public cm M(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.L = new int[]{var1, var2, var3, var4, var5, var6};
      return this;
   }

   public cm S(Set<class_2338> var1) {
      this.v = var1;
      return this;
   }

   public cm D(class_2338 var1) {
      this.b.add(g8.M(var1.method_10263(), var1.method_10264(), var1.method_10260()));
      return this;
   }

   private byte d(int var1, int var2, int var3) {
      long var4 = g8.M(var1, var2, var3);
      Byte var6 = this.m.get(var4);
      if (var6 != null) {
         return var6;
      }

      byte var7 = this.S(var1, var2, var3);
      this.m.put(var4, var7);
      return var7;
   }

   private byte S(int var1, int var2, int var3) {
      this.j.method_10103(var1, var2, var3);
      if (this.S != null && !this.S.method_31606(this.j) && this.S.method_22340(this.j)) {
         byte var4 = 1;
         class_2680 var5 = this.S.method_8320(this.j);
         class_3610 var6 = var5.method_26227();
         if (var6.method_15767(class_3486.field_15518)) {
            var4 = (byte)(var4 | 8);
         }

         if (var6.method_15767(class_3486.field_15517)) {
            var4 = (byte)(var4 | 16);
         }

         boolean var7 = var5.method_26220(this.S, this.j).method_1110();
         if (!var7) {
            var4 = (byte)(var4 | 32);
         }

         if (var7 && !var6.method_15767(class_3486.field_15518) && !D(var5)) {
            var4 = (byte)(var4 | 2);
         }

         if (!var7
            && var6.method_15769()
            && (var5.method_26206(this.S, this.j, class_2350.field_11036) || this.W(var5) >= 0.75 && this.W(var5) <= 1.0)
            && !var5.method_27852(class_2246.field_10092)
            && !var5.method_27852(class_2246.field_17350)
            && !var5.method_27852(class_2246.field_23860)
            && !var5.method_27852(class_2246.field_27879)
            && !this.b.contains(g8.M(var1, var2, var3))) {
            var4 = (byte)(var4 | 4);
         }

         return var4;
      } else {
         return 0;
      }
   }

   private double W(class_2680 var1) {
      return var1.method_26220(this.S, this.j).method_1105(class_2351.field_11052);
   }

   private static boolean D(class_2680 var0) {
      return var0.method_27852(class_2246.field_10036)
         || var0.method_27852(class_2246.field_22089)
         || var0.method_27852(class_2246.field_10343)
         || var0.method_27852(class_2246.field_16999)
         || var0.method_27852(class_2246.field_27879)
         || var0.method_27852(class_2246.field_10606)
         || var0.method_27852(class_2246.field_10316)
         || var0.method_27852(class_2246.field_10027);
   }

   @Override
   public boolean loaded(int var1, int var2, int var3) {
      return (this.d(var1, var2, var3) & 1) != 0;
   }

   @Override
   public boolean open(int var1, int var2, int var3) {
      return (this.d(var1, var2, var3) & 2) != 0;
   }

   @Override
   public boolean floor(int var1, int var2, int var3) {
      return (this.d(var1, var2, var3) & 4) != 0;
   }

   @Override
   public boolean lava(int var1, int var2, int var3) {
      return (this.d(var1, var2, var3) & 8) != 0;
   }

   @Override
   public boolean water(int var1, int var2, int var3) {
      return (this.d(var1, var2, var3) & 16) != 0;
   }

   @Override
   public boolean solid(int var1, int var2, int var3) {
      return (this.d(var1, var2, var3) & 32) != 0;
   }

   @Override
   public boolean swim(int var1, int var2, int var3) {
      return this.water(var1, var2, var3) && this.open(var1, var2, var3);
   }

   @Override
   public double penalty(int var1, int var2, int var3) {
      double var4 = 0.0;
      if (this.water(var1, var2, var3) || this.water(var1, var2 + 1, var3)) {
         var4 += 3.0;
      }

      for (class_2350 var7 : class_2353.field_11062) {
         if (this.lava(var1 + var7.method_10148(), var2, var3 + var7.method_10165())
            || this.lava(var1 + var7.method_10148(), var2 + 1, var3 + var7.method_10165())) {
            var4 += 8.0;
         }
      }

      if (this.lava(var1, var2 - 1, var3) || this.lava(var1, var2 + 2, var3)) {
         var4 += 8.0;
      }

      for (double[] var15 : this.N) {
         double var8 = var1 + 0.5 - var15[0];
         double var10 = var2 - var15[1];
         double var12 = var3 + 0.5 - var15[2];
         if (var8 * var8 + var12 * var12 <= var15[3] * var15[3] && Math.abs(var10) <= 4.0) {
            var4 += 30.0;
         }
      }

      return var4;
   }

   @Override
   public double digCost(int var1, int var2, int var3) {
      long var4 = g8.M(var1, var2, var3);
      Double var6 = this.d.get(var4);
      if (var6 != null) {
         return var6;
      }

      double var7 = this.L(var1, var2, var3);
      this.d.put(var4, var7);
      return var7;
   }

   private double L(int var1, int var2, int var3) {
      if (this.b.contains(g8.M(var1, var2, var3))) {
         return -1.0;
      }

      if (this.L == null || var1 >= this.L[0] && var2 >= this.L[1] && var3 >= this.L[2] && var1 <= this.L[3] && var2 <= this.L[4] && var3 <= this.L[5]) {
         class_2338 var4 = new class_2338(var1, var2, var3);
         if (this.v != null && !this.v.contains(var4)) {
            return -1.0;
         }

         if (!bv.P(var4)) {
            return -1.0;
         }

         class_2680 var5 = this.S.method_8320(var4);
         if (!var5.method_31709() && !var5.method_27852(class_2246.field_23152) && !var5.method_27852(class_2246.field_9987)) {
            for (class_2350 var9 : class_2350.values()) {
               if (var9 != class_2350.field_11033
                  && (
                     this.lava(var1 + var9.method_10148(), var2 + var9.method_10164(), var3 + var9.method_10165())
                        || this.water(var1 + var9.method_10148(), var2 + var9.method_10164(), var3 + var9.method_10165())
                  )) {
                  return -1.0;
               }
            }

            if (this.F && this.Y(var1, var2, var3)) {
               return -1.0;
            }

            float var15 = var5.method_26214(this.S, var4);
            if (var15 < 0.0F) {
               return -1.0;
            }

            double var16 = 1.0;
            if (var15 > 0.0F) {
               int var17 = this.X.applyAsInt(var5);
               class_1799 var10 = var17 >= 0 ? class_310.method_1551().field_1724.method_31548().method_5438(var17) : class_1799.field_8037;
               float var11 = Math.max(1.0F, var10.method_7924(var5));
               int var12 = var5.method_29291() && !var10.method_7951(var5) ? 0 : 1;
               double var13 = Math.ceil(var15 * (var12 != 0 ? 30 : 100) / var11);
               var16 += var13 / 4.6;
            }

            if (this.S.method_8320(var4.method_10084()).method_26204() instanceof class_2346) {
               var16 += 4.0;
            }

            return var16;
         } else {
            return -1.0;
         }
      } else {
         return -1.0;
      }
   }

   private boolean Y(int var1, int var2, int var3) {
      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            for (int var6 = -1; var6 <= 1; var6++) {
               if (this.lava(var1 + var4, var2 + var5, var3 + var6) || this.water(var1 + var4, var2 + var5, var3 + var6)) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}

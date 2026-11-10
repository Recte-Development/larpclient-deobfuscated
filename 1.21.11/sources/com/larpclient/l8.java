package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class l8 {
   static final int m = 12000;
   private static final int[][] a = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
   private static final int[][] P;
   private static final int[][] L;
   private final l8.h y;
   private final Map<Long, Integer> e = new HashMap<>();
   private final Map<Long, l8.g> S = new HashMap<>();
   private final double X;
   private final double K;
   private final double F;
   private final double o;
   private final int p;
   private final boolean N;
   private final boolean E;
   private int k;
   private l8.c i;
   private static final double[][] h;
   static final double H = 0.25;
   static final int r = 24;

   private l8(l8.h var1, double var2, double var4, double var6, double var8, int var10, boolean var11, boolean var12) {
      this.y = var1;
      this.X = var2;
      this.K = var4;
      this.F = var6;
      this.o = var8;
      this.p = var10;
      this.N = var11;
      this.E = var12;
   }

   public static l8.c b(l8.h var0, double var1, double var3, double var5, double var7, int var9, boolean var10, boolean var11) {
      if (var0 != null && Double.isFinite(var1) && Double.isFinite(var3)) {
         l8 var12 = new l8(var0, var1, var3, var5, var7, Math.clamp(var9, 0, 512), var10, var11);
         return var12.l();
      } else {
         return null;
      }
   }

   private l8.c l() {
      for (byte var1 = 0; var1 <= this.p && this.k < 12000 && (this.i == null || !(var1 - 12 >= this.i.score())); var1 += var1 < 6 ? 1 : 3) {
         int var2 = var1 == 0 ? 1 : Math.max(6, (int)Math.ceil((Math.PI * 2) * var1 / 3.0));
         int var3 = Integer.MIN_VALUE;
         int var4 = Integer.MIN_VALUE;

         for (int var5 = 0; var5 < var2 && this.k < 12000; var5++) {
            double var6 = (Math.PI * 2) * var5 / var2;
            int var8 = (int)Math.floor(this.X + var1 * Math.cos(var6));
            int var9 = (int)Math.floor(this.K + var1 * Math.sin(var6));
            if (var8 != var3 || var9 != var4) {
               var3 = var8;
               var4 = var9;
               this.z(var8, var9);
            }
         }
      }

      return this.i;
   }

   private boolean Z(l8.g var1) {
      return var1 == l8.g.m || this.N && var1 == l8.g.M || this.N && this.E && var1 == l8.g.u;
   }

   private void z(int var1, int var2) {
      double var3 = var1 + 0.5 - this.X;
      double var5 = var2 + 0.5 - this.K;
      double var7 = Math.sqrt(var3 * var3 + var5 * var5);
      if (!(var7 > this.p + 0.75)) {
         int var9 = this.j(var1, var2);
         if (var9 != Integer.MIN_VALUE) {
            l8.g var10 = this.Y(var1, var9, var2);
            if (this.Z(var10)) {
               double var11 = var7 + (var10 == l8.g.M ? 400 : 0) + (var10 == l8.g.u ? 240 : 0);
               if (this.i == null || !(var11 - 10.0 >= this.i.score())) {
                  byte var13 = 0;
                  if (!this.H(var1, var9, var2, var10)) {
                     if (!this.E || var10 != l8.g.m || !this.x(var1, var9, var2)) {
                        return;
                     }

                     var13 = 1;
                  }

                  double var14 = this.t(var1, var9, var2);
                  if (!Double.isNaN(var14)) {
                     int var16 = this.X(var1, var9, var2);
                     if (var16 != 0) {
                        int var17 = 0;
                        double var18 = 0.0;

                        for (int[] var23 : L) {
                           int var24 = this.j(var1 + var23[0], var2 + var23[1]);
                           if (var24 != Integer.MIN_VALUE) {
                              l8.g var25 = this.Y(var1 + var23[0], var24, var2 + var23[1]);
                              if (var25 == l8.g.e || var25 == l8.g.C) {
                                 var18++;
                              } else if (var24 == var9 && var25 == var10) {
                                 var17++;
                              }
                           }
                        }

                        double var30 = this.F - this.X;
                        double var31 = this.o - this.K;
                        double var32 = Math.hypot(var30, var31);
                        double var26 = var32 > 1.0 && var7 > 0.5 ? 0.35 * Math.min(var7, 10.0) * (var3 * var30 + var5 * var31) / (var32 * var7) : 0.0;
                        double var28 = var11
                           + 5.0 * (var14 + var18)
                           - 6.0 * var17 / L.length
                           - var26
                           + (var13 != 0 ? 25 : 0)
                           + this.H(var1, var9, var2)
                           + 3.0 * (h.length - var16);
                        if (this.i == null || var28 < this.i.score()) {
                           this.i = new l8.c(var1, var9, var2, var28, var10 == l8.g.M);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   int X(int var1, int var2, int var3) {
      int var4 = 0;

      for (double[] var8 : h) {
         byte var9 = 1;

         for (int var10 = 3; var10 <= 24 && var9 != 0; var10++) {
            int var11 = this.j((int)Math.floor(var1 + 0.5 + var8[0] * var10), (int)Math.floor(var3 + 0.5 + var8[1] * var10));
            if (var11 != Integer.MIN_VALUE && var11 > var2 + 1 + (var10 - 2) * 0.25) {
               var9 = 0;
            }
         }

         if (var9 != 0) {
            var4++;
         }
      }

      return var4;
   }

   private boolean H(int var1, int var2, int var3, l8.g var4) {
      if (this.j(var1, var3) != var2) {
         return false;
      }

      for (int[] var8 : a) {
         if (this.j(var1 + var8[0], var3 + var8[1]) != var2 || this.Y(var1 + var8[0], var2, var3 + var8[1]) != var4) {
            return false;
         }
      }

      return true;
   }

   private boolean x(int var1, int var2, int var3) {
      for (int[] var7 : a) {
         int var8 = this.j(var1 + var7[0], var3 + var7[1]);
         if (var8 == Integer.MIN_VALUE || Math.abs(var8 - var2) > 1 || this.Y(var1 + var7[0], var8, var3 + var7[1]) != l8.g.m) {
            return false;
         }
      }

      return true;
   }

   private double t(int var1, int var2, int var3) {
      double var4 = 0.0;
      int var6 = 0;

      for (int[] var10 : P) {
         int var11 = this.j(var1 + var10[0], var3 + var10[1]);
         if (var11 == Integer.MIN_VALUE) {
            var4 += 0.5;
         } else {
            int var12 = var11 - var2;
            if (var12 > 3) {
               if (var10[0] * var10[0] + var10[1] * var10[1] <= 9) {
                  return Double.NaN;
               }

               var6++;
               var4 += 6.0;
            } else {
               var4 += var12 == 3 ? 3.0 : (var12 == 2 ? 1.2 : (var12 == 1 ? 0.4 : (var12 < -4 ? 0.25 : 0.0)));
            }
         }
      }

      return var6 * 3 > P.length ? Double.NaN : 10.0 * var4 / P.length;
   }

   private double H(int var1, int var2, int var3) {
      double var4 = this.F - (var1 + 0.5);
      double var6 = this.o - (var3 + 0.5);
      double var8 = Math.hypot(var4, var6);
      double var10 = 0.0;
      int var12 = 0;

      for (byte var13 = -14; var13 <= 14; var13 += 2) {
         for (byte var14 = -14; var14 <= 14; var14 += 2) {
            int var15 = var13 * var13 + var14 * var14;
            if (var15 >= 36 && var15 <= 196) {
               var12++;
               int var16 = this.j(var1 + var13, var3 + var14);
               if (var16 == Integer.MIN_VALUE) {
                  var10 += 0.5;
               } else {
                  int var17 = var16 - var2 - 6;
                  if (var17 > 0) {
                     double var18 = var8 >= 4.0 ? 0.7 + 0.3 * (var13 * var4 + var14 * var6) / (Math.sqrt(var15) * var8) : 0.7;
                     var10 += var18 * Math.min(var17, 30);
                  }
               }
            }
         }
      }

      return var12 == 0 ? 0.0 : 2.0 * var10 / var12;
   }

   private int j(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.e.get(var3);
      if (var5 != null) {
         return var5;
      }

      this.k++;
      int var6 = this.y.surface(var1, var2);
      this.e.put(var3, var6);
      return var6;
   }

   private l8.g Y(int var1, int var2, int var3) {
      long var4 = (long)var1 << 32 ^ var3 & 4294967295L;
      l8.g var6 = this.S.get(var4);
      if (var6 != null && this.e.getOrDefault(var4, -2147483647) == var2) {
         return var6;
      }

      this.k++;
      l8.g var7 = this.y.ground(var1, var2, var3);
      if (this.e.getOrDefault(var4, -2147483647) == var2) {
         this.S.put(var4, var7);
      }

      return var7 == null ? l8.g.q : var7;
   }

   static {
      ArrayList var0 = new ArrayList();

      for (int var1 = 4; var1 <= 30; var1++) {
         for (int var2 = -5; var2 <= 5; var2++) {
            for (int var3 = -5; var3 <= 5; var3++) {
               if (var2 * var2 + var3 * var3 == var1) {
                  var0.add(new int[]{var2, var3});
               }
            }
         }
      }

      P = var0.toArray(int[][]::new);
      ArrayList var5 = new ArrayList();

      for (int var6 = -2; var6 <= 2; var6++) {
         for (int var7 = -2; var7 <= 2; var7++) {
            if (Math.max(Math.abs(var6), Math.abs(var7)) == 2) {
               var5.add(new int[]{var6, var7});
            }
         }
      }

      L = var5.toArray(int[][]::new);
      h = new double[8][];

      for (int var4 = 0; var4 < 8; var4++) {
         h[var4] = new double[]{Math.cos((Math.PI / 4) * var4), Math.sin((Math.PI / 4) * var4)};
      }
   }

   public record c(int x, int y, int z, double score, boolean water) {
   }

   public enum g {
      m,
      M,
      u,
      e,
      C,
      q;
   }

   public interface h {
      int surface(int var1, int var2);

      l8.g ground(int var1, int var2, int var3);
   }
}

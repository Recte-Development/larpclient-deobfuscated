package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class mj {
   static final int P = 12000;
   private static final int[][] M = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
   private static final int[][] s;
   private static final int[][] R;
   private final mj.q F;
   private final Map<Long, Integer> o = new HashMap<>();
   private final Map<Long, mj.b> f = new HashMap<>();
   private final double T;
   private final double I;
   private final double A;
   private final double V;
   private final int C;
   private final boolean x;
   private final boolean n;
   private int d;
   private mj.h p;
   private static final double[][] H;
   static final double t = 0.25;
   static final int W = 24;

   private mj(mj.q var1, double var2, double var4, double var6, double var8, int var10, boolean var11, boolean var12) {
      this.F = var1;
      this.T = var2;
      this.I = var4;
      this.A = var6;
      this.V = var8;
      this.C = var10;
      this.x = var11;
      this.n = var12;
   }

   public static mj.h b(mj.q var0, double var1, double var3, double var5, double var7, int var9, boolean var10, boolean var11) {
      if (var0 != null && Double.isFinite(var1) && Double.isFinite(var3)) {
         mj var12 = new mj(var0, var1, var3, var5, var7, Math.clamp(var9, 0, 512), var10, var11);
         return var12.R();
      } else {
         return null;
      }
   }

   private mj.h R() {
      for (byte var1 = 0; var1 <= this.C && this.d < 12000 && (this.p == null || !(var1 - 12 >= this.p.score())); var1 += var1 < 6 ? 1 : 3) {
         int var2 = var1 == 0 ? 1 : Math.max(6, (int)Math.ceil((Math.PI * 2) * var1 / 3.0));
         int var3 = Integer.MIN_VALUE;
         int var4 = Integer.MIN_VALUE;

         for (int var5 = 0; var5 < var2 && this.d < 12000; var5++) {
            double var6 = (Math.PI * 2) * var5 / var2;
            int var8 = (int)Math.floor(this.T + var1 * Math.cos(var6));
            int var9 = (int)Math.floor(this.I + var1 * Math.sin(var6));
            if (var8 != var3 || var9 != var4) {
               var3 = var8;
               var4 = var9;
               this.e(var8, var9);
            }
         }
      }

      return this.p;
   }

   private boolean B(mj.b var1) {
      return var1 == mj.b.g || this.x && var1 == mj.b.K || this.x && this.n && var1 == mj.b.M;
   }

   private void e(int var1, int var2) {
      double var3 = var1 + 0.5 - this.T;
      double var5 = var2 + 0.5 - this.I;
      double var7 = Math.sqrt(var3 * var3 + var5 * var5);
      if (!(var7 > this.C + 0.75)) {
         int var9 = this.Y(var1, var2);
         if (var9 != Integer.MIN_VALUE) {
            mj.b var10 = this.t(var1, var9, var2);
            if (this.B(var10)) {
               double var11 = var7 + (var10 == mj.b.K ? 400 : 0) + (var10 == mj.b.M ? 240 : 0);
               if (this.p == null || !(var11 - 10.0 >= this.p.score())) {
                  byte var13 = 0;
                  if (!this.a(var1, var9, var2, var10)) {
                     if (!this.n || var10 != mj.b.g || !this.L(var1, var9, var2)) {
                        return;
                     }

                     var13 = 1;
                  }

                  double var14 = this.a(var1, var9, var2);
                  if (!Double.isNaN(var14)) {
                     int var16 = this.c(var1, var9, var2);
                     if (var16 != 0) {
                        int var17 = 0;
                        double var18 = 0.0;

                        for (int[] var23 : R) {
                           int var24 = this.Y(var1 + var23[0], var2 + var23[1]);
                           if (var24 != Integer.MIN_VALUE) {
                              mj.b var25 = this.t(var1 + var23[0], var24, var2 + var23[1]);
                              if (var25 == mj.b.Z || var25 == mj.b.B) {
                                 var18++;
                              } else if (var24 == var9 && var25 == var10) {
                                 var17++;
                              }
                           }
                        }

                        double var30 = this.A - this.T;
                        double var31 = this.V - this.I;
                        double var32 = Math.hypot(var30, var31);
                        double var26 = var32 > 1.0 && var7 > 0.5 ? 0.35 * Math.min(var7, 10.0) * (var3 * var30 + var5 * var31) / (var32 * var7) : 0.0;
                        double var28 = var11
                           + 5.0 * (var14 + var18)
                           - 6.0 * var17 / R.length
                           - var26
                           + (var13 != 0 ? 25 : 0)
                           + this.x(var1, var9, var2)
                           + 3.0 * (H.length - var16);
                        if (this.p == null || var28 < this.p.score()) {
                           this.p = new mj.h(var1, var9, var2, var28, var10 == mj.b.K);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   int c(int var1, int var2, int var3) {
      int var4 = 0;

      for (double[] var8 : H) {
         byte var9 = 1;

         for (int var10 = 3; var10 <= 24 && var9 != 0; var10++) {
            int var11 = this.Y((int)Math.floor(var1 + 0.5 + var8[0] * var10), (int)Math.floor(var3 + 0.5 + var8[1] * var10));
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

   private boolean a(int var1, int var2, int var3, mj.b var4) {
      if (this.Y(var1, var3) != var2) {
         return false;
      }

      for (int[] var8 : M) {
         if (this.Y(var1 + var8[0], var3 + var8[1]) != var2 || this.t(var1 + var8[0], var2, var3 + var8[1]) != var4) {
            return false;
         }
      }

      return true;
   }

   private boolean L(int var1, int var2, int var3) {
      for (int[] var7 : M) {
         int var8 = this.Y(var1 + var7[0], var3 + var7[1]);
         if (var8 == Integer.MIN_VALUE || Math.abs(var8 - var2) > 1 || this.t(var1 + var7[0], var8, var3 + var7[1]) != mj.b.g) {
            return false;
         }
      }

      return true;
   }

   private double a(int var1, int var2, int var3) {
      double var4 = 0.0;
      int var6 = 0;

      for (int[] var10 : s) {
         int var11 = this.Y(var1 + var10[0], var3 + var10[1]);
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

      return var6 * 3 > s.length ? Double.NaN : 10.0 * var4 / s.length;
   }

   private double x(int var1, int var2, int var3) {
      double var4 = this.A - (var1 + 0.5);
      double var6 = this.V - (var3 + 0.5);
      double var8 = Math.hypot(var4, var6);
      double var10 = 0.0;
      int var12 = 0;

      for (byte var13 = -14; var13 <= 14; var13 += 2) {
         for (byte var14 = -14; var14 <= 14; var14 += 2) {
            int var15 = var13 * var13 + var14 * var14;
            if (var15 >= 36 && var15 <= 196) {
               var12++;
               int var16 = this.Y(var1 + var13, var3 + var14);
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

   private int Y(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.o.get(var3);
      if (var5 != null) {
         return var5;
      }

      this.d++;
      int var6 = this.F.surface(var1, var2);
      this.o.put(var3, var6);
      return var6;
   }

   private mj.b t(int var1, int var2, int var3) {
      long var4 = (long)var1 << 32 ^ var3 & 4294967295L;
      mj.b var6 = this.f.get(var4);
      if (var6 != null && this.o.getOrDefault(var4, -2147483647) == var2) {
         return var6;
      }

      this.d++;
      mj.b var7 = this.F.ground(var1, var2, var3);
      if (this.o.getOrDefault(var4, -2147483647) == var2) {
         this.f.put(var4, var7);
      }

      return var7 == null ? mj.b.H : var7;
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

      s = var0.toArray(int[][]::new);
      ArrayList var5 = new ArrayList();

      for (int var6 = -2; var6 <= 2; var6++) {
         for (int var7 = -2; var7 <= 2; var7++) {
            if (Math.max(Math.abs(var6), Math.abs(var7)) == 2) {
               var5.add(new int[]{var6, var7});
            }
         }
      }

      R = var5.toArray(int[][]::new);
      H = new double[8][];

      for (int var4 = 0; var4 < 8; var4++) {
         H[var4] = new double[]{Math.cos((Math.PI / 4) * var4), Math.sin((Math.PI / 4) * var4)};
      }
   }

   public enum b {
      g,
      K,
      M,
      Z,
      B,
      H;
   }

   public record h(int x, int y, int z, double score, boolean water) {
   }

   public interface q {
      int surface(int var1, int var2);

      mj.b ground(int var1, int var2, int var3);
   }
}

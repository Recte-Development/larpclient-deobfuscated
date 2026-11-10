package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class _e {
   static final int L = 12000;
   private static final int[][] S = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
   private static final int[][] m;
   private static final int[][] x;
   private final _e.d f;
   private final Map<Long, Integer> V = new HashMap<>();
   private final Map<Long, _e.t> O = new HashMap<>();
   private final double X;
   private final double Y;
   private final double K;
   private final double n;
   private final int g;
   private final boolean B;
   private final boolean A;
   private int u;
   private _e.k P;
   private static final double[][] j;
   static final double I = 0.25;
   static final int h = 24;

   private _e(_e.d var1, double var2, double var4, double var6, double var8, int var10, boolean var11, boolean var12) {
      this.f = var1;
      this.X = var2;
      this.Y = var4;
      this.K = var6;
      this.n = var8;
      this.g = var10;
      this.B = var11;
      this.A = var12;
   }

   public static _e.k d(_e.d var0, double var1, double var3, double var5, double var7, int var9, boolean var10, boolean var11) {
      if (var0 != null && Double.isFinite(var1) && Double.isFinite(var3)) {
         _e var12 = new _e(var0, var1, var3, var5, var7, Math.clamp(var9, 0, 512), var10, var11);
         return var12.h();
      } else {
         return null;
      }
   }

   private _e.k h() {
      for (byte var1 = 0; var1 <= this.g && this.u < 12000 && (this.P == null || !(var1 - 12 >= this.P.score())); var1 += var1 < 6 ? 1 : 3) {
         int var2 = var1 == 0 ? 1 : Math.max(6, (int)Math.ceil((Math.PI * 2) * var1 / 3.0));
         int var3 = Integer.MIN_VALUE;
         int var4 = Integer.MIN_VALUE;

         for (int var5 = 0; var5 < var2 && this.u < 12000; var5++) {
            double var6 = (Math.PI * 2) * var5 / var2;
            int var8 = (int)Math.floor(this.X + var1 * Math.cos(var6));
            int var9 = (int)Math.floor(this.Y + var1 * Math.sin(var6));
            if (var8 != var3 || var9 != var4) {
               var3 = var8;
               var4 = var9;
               this.L(var8, var9);
            }
         }
      }

      return this.P;
   }

   private boolean l(_e.t var1) {
      return var1 == _e.t.K || this.B && var1 == _e.t.X || this.B && this.A && var1 == _e.t.h;
   }

   private void L(int var1, int var2) {
      double var3 = var1 + 0.5 - this.X;
      double var5 = var2 + 0.5 - this.Y;
      double var7 = Math.sqrt(var3 * var3 + var5 * var5);
      if (!(var7 > this.g + 0.75)) {
         int var9 = this.S(var1, var2);
         if (var9 != Integer.MIN_VALUE) {
            _e.t var10 = this.I(var1, var9, var2);
            if (this.l(var10)) {
               double var11 = var7 + (var10 == _e.t.X ? 400 : 0) + (var10 == _e.t.h ? 240 : 0);
               if (this.P == null || !(var11 - 10.0 >= this.P.score())) {
                  byte var13 = 0;
                  if (!this.K(var1, var9, var2, var10)) {
                     if (!this.A || var10 != _e.t.K || !this.M(var1, var9, var2)) {
                        return;
                     }

                     var13 = 1;
                  }

                  double var14 = this.C(var1, var9, var2);
                  if (!Double.isNaN(var14)) {
                     int var16 = this.w(var1, var9, var2);
                     if (var16 != 0) {
                        int var17 = 0;
                        double var18 = 0.0;

                        for (int[] var23 : x) {
                           int var24 = this.S(var1 + var23[0], var2 + var23[1]);
                           if (var24 != Integer.MIN_VALUE) {
                              _e.t var25 = this.I(var1 + var23[0], var24, var2 + var23[1]);
                              if (var25 == _e.t.t || var25 == _e.t.H) {
                                 var18++;
                              } else if (var24 == var9 && var25 == var10) {
                                 var17++;
                              }
                           }
                        }

                        double var30 = this.K - this.X;
                        double var31 = this.n - this.Y;
                        double var32 = Math.hypot(var30, var31);
                        double var26 = var32 > 1.0 && var7 > 0.5 ? 0.35 * Math.min(var7, 10.0) * (var3 * var30 + var5 * var31) / (var32 * var7) : 0.0;
                        double var28 = var11
                           + 5.0 * (var14 + var18)
                           - 6.0 * var17 / x.length
                           - var26
                           + (var13 != 0 ? 25 : 0)
                           + this.j(var1, var9, var2)
                           + 3.0 * (j.length - var16);
                        if (this.P == null || var28 < this.P.score()) {
                           this.P = new _e.k(var1, var9, var2, var28, var10 == _e.t.X);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   int w(int var1, int var2, int var3) {
      int var4 = 0;

      for (double[] var8 : j) {
         byte var9 = 1;

         for (int var10 = 3; var10 <= 24 && var9 != 0; var10++) {
            int var11 = this.S((int)Math.floor(var1 + 0.5 + var8[0] * var10), (int)Math.floor(var3 + 0.5 + var8[1] * var10));
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

   private boolean K(int var1, int var2, int var3, _e.t var4) {
      if (this.S(var1, var3) != var2) {
         return false;
      }

      for (int[] var8 : S) {
         if (this.S(var1 + var8[0], var3 + var8[1]) != var2 || this.I(var1 + var8[0], var2, var3 + var8[1]) != var4) {
            return false;
         }
      }

      return true;
   }

   private boolean M(int var1, int var2, int var3) {
      for (int[] var7 : S) {
         int var8 = this.S(var1 + var7[0], var3 + var7[1]);
         if (var8 == Integer.MIN_VALUE || Math.abs(var8 - var2) > 1 || this.I(var1 + var7[0], var8, var3 + var7[1]) != _e.t.K) {
            return false;
         }
      }

      return true;
   }

   private double C(int var1, int var2, int var3) {
      double var4 = 0.0;
      int var6 = 0;

      for (int[] var10 : m) {
         int var11 = this.S(var1 + var10[0], var3 + var10[1]);
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

      return var6 * 3 > m.length ? Double.NaN : 10.0 * var4 / m.length;
   }

   private double j(int var1, int var2, int var3) {
      double var4 = this.K - (var1 + 0.5);
      double var6 = this.n - (var3 + 0.5);
      double var8 = Math.hypot(var4, var6);
      double var10 = 0.0;
      int var12 = 0;

      for (byte var13 = -14; var13 <= 14; var13 += 2) {
         for (byte var14 = -14; var14 <= 14; var14 += 2) {
            int var15 = var13 * var13 + var14 * var14;
            if (var15 >= 36 && var15 <= 196) {
               var12++;
               int var16 = this.S(var1 + var13, var3 + var14);
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

   private int S(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.V.get(var3);
      if (var5 != null) {
         return var5;
      }

      this.u++;
      int var6 = this.f.surface(var1, var2);
      this.V.put(var3, var6);
      return var6;
   }

   private _e.t I(int var1, int var2, int var3) {
      long var4 = (long)var1 << 32 ^ var3 & 4294967295L;
      _e.t var6 = this.O.get(var4);
      if (var6 != null && this.V.getOrDefault(var4, -2147483647) == var2) {
         return var6;
      }

      this.u++;
      _e.t var7 = this.f.ground(var1, var2, var3);
      if (this.V.getOrDefault(var4, -2147483647) == var2) {
         this.O.put(var4, var7);
      }

      return var7 == null ? _e.t.P : var7;
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

      m = var0.toArray(int[][]::new);
      ArrayList var5 = new ArrayList();

      for (int var6 = -2; var6 <= 2; var6++) {
         for (int var7 = -2; var7 <= 2; var7++) {
            if (Math.max(Math.abs(var6), Math.abs(var7)) == 2) {
               var5.add(new int[]{var6, var7});
            }
         }
      }

      x = var5.toArray(int[][]::new);
      j = new double[8][];

      for (int var4 = 0; var4 < 8; var4++) {
         j[var4] = new double[]{Math.cos((Math.PI / 4) * var4), Math.sin((Math.PI / 4) * var4)};
      }
   }

   public interface d {
      int surface(int var1, int var2);

      _e.t ground(int var1, int var2, int var3);
   }

   public record k(int x, int y, int z, double score, boolean water) {
   }

   public enum t {
      K,
      X,
      h,
      t,
      H,
      P;
   }
}

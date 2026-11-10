package com.larpclient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3486;
import net.minecraft.class_3965;
import net.minecraft.class_638;

public final class ex {
   public static final int i = 4096;
   private static final int Z = 16384;
   private static final int Q = 48;
   private static final int T = 8192;
   private static final double U = 0.1;
   private static final double S = 0.064;
   private static final double N = 0.18;
   private static final double[] e = new double[4096];
   private static final double[] V = new double[4096];
   private static final double[] R = new double[4096];
   private static final double[] q = new double[4096];
   private static final double[] r = new double[4096];
   private static final double[] C = new double[4096];
   private static final double[] G = new double[4096];
   private static final double[] y = new double[4096];
   private static final double[] o = new double[4096];
   private static final double[] D = new double[4096];
   private static final double[] h = new double[4096];
   private static final double[] Y = new double[4096];
   private static final double[] s = new double[4096];
   private static final double[] I = new double[4096];
   private static final double[] c = new double[4096];
   private static final int[] g = new int[16384];
   private static final int[] u = new int[4096];
   private static final int[] W = new int[4096];
   private static final int[] F = new int[4096];
   private static final int[] d = new int[4096];
   private static final Set<Long> v = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<class_238>> z = new HashMap<>();
   private static final Map<Long, Long> X = new HashMap<>();
   private static class_638 A;
   private static int t;
   private static long P;
   private static boolean O;

   private static boolean n() {
      RealisticWaterModule var0 = RealisticWaterModule.C();
      return var0 != null && var0.isRuntimeActive() && var0.M.getValue() && !qs.k();
   }

   public static int s() {
      return t;
   }

   public static boolean P(class_2338 var0) {
      return n() && v.contains(var0.method_10063());
   }

   public static double c() {
      return 0.064;
   }

   public static double A(int var0, float var1) {
      return q[var0] + (e[var0] - q[var0]) * var1;
   }

   public static double F(int var0, float var1) {
      return r[var0] + (V[var0] - r[var0]) * var1;
   }

   public static double n(int var0, float var1) {
      return C[var0] + (R[var0] - C[var0]) * var1;
   }

   public static void V(class_2338 var0, class_2680 var1, class_2680 var2) {
      if (n()) {
         class_310 var3 = class_310.method_1551();
         if (var3 != null
            && var3.field_1687 != null
            && var3.field_1724 != null
            && !(var3.field_1724.method_5649(var0.method_10263() + 0.5, var0.method_10264() + 0.5, var0.method_10260() + 0.5) > 49.0)) {
            x(var3.field_1687);
            boolean var4 = var1.method_26227().method_15767(class_3486.field_15517);
            boolean var5 = var2.method_26227().method_15767(class_3486.field_15517);
            if (!var1.method_26215() && !var4 && var2.method_26215()) {
               if (X.size() < 128) {
                  X.put(var0.method_10063(), P + 20L);
               }

               for (class_2350 var9 : class_2350.values()) {
                  if (var3.field_1687.method_8316(var0.method_10093(var9)).method_15767(class_3486.field_15517)) {
                     Y(var0);
                     break;
                  }
               }
            }

            if (var5 && (!var4 || var2.method_26227().method_15771() && !var1.method_26227().method_15771())) {
               byte var11 = 0;

               for (class_2350 var10 : class_2350.values()) {
                  if (v.contains(var0.method_10093(var10).method_10063())) {
                     var11 = 1;
                     break;
                  }
               }

               int var13 = !var3.field_1724.method_6047().method_31574(class_1802.field_8705)
                     && !var3.field_1724.method_6079().method_31574(class_1802.field_8705)
                     && !var3.field_1724.method_6047().method_31574(class_1802.field_8550)
                     && !var3.field_1724.method_6079().method_31574(class_1802.field_8550)
                  ? 0
                  : 1;
               int var15 = !(
                     var3.field_1765 instanceof class_3965 var17
                        && (var0.equals(var17.method_17777()) || var0.equals(var17.method_17777().method_10093(var17.method_17780())))
                  )
                  ? 0
                  : 1;
               int var18 = var2.method_26227().method_15771() && var3.field_1690.field_1904.method_1434() && var13 != 0 && var15 != 0 ? 1 : 0;
               if (var18 != 0) {
                  u(var0, true);
               } else if (X.containsKey(var0.method_10063())) {
                  Y(var0);
               } else if (var11 != 0) {
                  I(var0);
               }
            }

            if (!var5) {
               v.remove(var0.method_10063());
            }
         }
      }
   }

   private static void I(class_2338 var0) {
      if (v.size() < 4096 && v.add(var0.method_10063())) {
         class_310 var1 = class_310.method_1551();
         if (var1 != null && var1.field_1687 != null) {
            var1.field_1769.method_18145(var0.method_10263() >> 4, var0.method_10264() >> 4, var0.method_10260() >> 4);
         }
      }
   }

   private static void Y(class_2338 var0) {
      u(var0, false);
   }

   private static void u(class_2338 var0, boolean var1) {
      if (var1 || !v.contains(var0.method_10063())) {
         if (t + 1000 > 4096) {
            if (!O) {
               q8.G("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               O = (boolean)1;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = t++;
                     double var6 = ((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     e[var5] = q[var5] = var0.method_10263() + (var4 + 0.5) * 0.1 + var6;
                     V[var5] = r[var5] = var0.method_10264() + (var2 + 0.5) * 0.1 - var6;
                     R[var5] = C[var5] = var0.method_10260() + (var3 + 0.5) * 0.1;
                     G[var5] = y[var5] = o[var5] = 0.0;
                  }
               }
            }

            I(var0);
            q8.v("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, t);
         }
      }
   }

   public static void o(class_310 var0) {
      if (!n()) {
         if (t > 0 || !v.isEmpty()) {
            v();
         }
      } else if (var0.field_1687 != null && var0.field_1724 != null && !var0.method_1493()) {
         x(var0.field_1687);
         P++;
         X.entrySet().removeIf(var0x -> var0x.getValue() < P);
         if (t != 0) {
            System.arraycopy(e, 0, q, 0, t);
            System.arraycopy(V, 0, r, 0, t);
            System.arraycopy(R, 0, C, 0, t);
            z.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               A(var0, 0.025);
            }
         }
      }
   }

   private static void x(class_638 var0) {
      if (A != var0) {
         v();
         A = var0;
      }
   }

   private static void A(class_310 var0, double var1) {
      for (int var3 = 0; var3 < t; var3++) {
         D[var3] = e[var3];
         h[var3] = V[var3];
         Y[var3] = R[var3];
         if (!A.method_22340(class_2338.method_49637(e[var3], V[var3], R[var3]))) {
            G[var3] = y[var3] = o[var3] = 0.0;
         } else {
            y[var3] = Math.max(-6.0, y[var3] - 9.8 * var1);
            double var4 = e[var3];
            double var6 = V[var3];
            double var8 = R[var3];
            e[var3] = e[var3] + G[var3] * var1;
            V[var3] = V[var3] + y[var3] * var1;
            R[var3] = R[var3] + o[var3] * var1;
            r(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         n();
         Arrays.fill(s, 0, t, 0.0);
         Arrays.fill(I, 0, t, 0.0);
         Arrays.fill(c, 0, t, 0.0);

         label108:
         for (int var29 = 0; var29 < t; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = W[var29] + var36;
                     int var10 = F[var29] + var7;
                     int var11 = d[var29] + var34;

                     for (int var12 = g[B(var9, var10, var11)]; var12 >= 0; var12 = u[var12]) {
                        if (var12 != var29 && W[var12] == var9 && F[var12] == var10 && d[var12] == var11) {
                           double var13 = e[var29] - e[var12];
                           double var15 = V[var29] - V[var12];
                           double var17 = R[var29] - R[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label108;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              s[var29] = s[var29] + var13 * var25;
                              I[var29] = I[var29] + var15 * var25;
                              c[var29] = c[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < t; var30++) {
            double var32 = e[var30];
            double var35 = V[var30];
            double var37 = R[var30];
            e[var30] = e[var30] + Math.clamp(s[var30], -0.015, 0.015);
            V[var30] = V[var30] + Math.clamp(I[var30], -0.015, 0.015);
            R[var30] = R[var30] + Math.clamp(c[var30], -0.015, 0.015);
            r(var30, var32, var35, var37);
         }
      }

      class_238 var28 = var0.field_1724.method_5829().method_1014(0.064);
      class_243 var31 = var0.field_1724.method_18798();

      for (int var33 = 0; var33 < t; var33++) {
         G[var33] = (e[var33] - D[var33]) / var1;
         y[var33] = (V[var33] - h[var33]) / var1;
         o[var33] = (R[var33] - Y[var33]) / var1;
         if (var28.method_1008(e[var33], V[var33], R[var33])) {
            G[var33] = G[var33] + var31.field_1352 * 3.0;
            o[var33] = o[var33] + var31.field_1350 * 3.0;
            y[var33] = y[var33] + Math.max(0.0, var31.field_1351) * 2.0;
         }

         G[var33] = Math.clamp(G[var33] * 0.99, -6.0, 6.0);
         y[var33] = Math.clamp(y[var33] * 0.99, -6.0, 6.0);
         o[var33] = Math.clamp(o[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(e[var33] + V[var33] + R[var33]) || V[var33] < A.method_31607() - 8) {
            f(var33--);
         }
      }
   }

   private static void n() {
      Arrays.fill(g, -1);
      int var0 = 0;

      while (var0 < t) {
         W[var0] = (int)Math.floor(e[var0] / 0.18);
         F[var0] = (int)Math.floor(V[var0] / 0.18);
         d[var0] = (int)Math.floor(R[var0] / 0.18);
         int var1 = B(W[var0], F[var0], d[var0]);
         u[var0] = g[var1];
         g[var1] = var0++;
      }
   }

   private static int B(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void r(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(e[var0]);
      int var8 = (int)Math.floor(V[var0]);
      int var9 = (int)Math.floor(R[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               class_2338 var13 = new class_2338(var7 + var12, var8 + var11, var9 + var10);
               List var14 = z.get(var13.method_10063());
               if (var14 == null) {
                  if (z.size() >= 8192 || !A.method_22340(var13)) {
                     e[var0] = var1;
                     V[var0] = var3;
                     R[var0] = var5;
                     G[var0] = y[var0] = o[var0] = 0.0;
                     return;
                  }

                  var14 = A.method_8320(var13).method_26220(A, var13).method_1090();
                  z.put(var13.method_10063(), var14);
               }

               for (class_238 var16 : var14) {
                  double var17 = 0.0352;
                  double var19 = var16.field_1323 + var13.method_10263() - var17;
                  double var21 = var16.field_1320 + var13.method_10263() + var17;
                  double var23 = var16.field_1322 + var13.method_10264() - var17;
                  double var25 = var16.field_1325 + var13.method_10264() + var17;
                  double var27 = var16.field_1321 + var13.method_10260() - var17;
                  double var29 = var16.field_1324 + var13.method_10260() + var17;
                  if (!(e[var0] <= var19) && !(e[var0] >= var21) && !(V[var0] <= var23) && !(V[var0] >= var25) && !(R[var0] <= var27) && !(R[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = e[var0] - var19;
                     double var34 = var21 - e[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = V[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - V[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = R[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - R[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           e[var0] = var19 - 1.0E-4;
                           G[var0] = Math.min(0.0, G[var0]) * 0.15;
                           break;
                        case 1:
                           e[var0] = var21 + 1.0E-4;
                           G[var0] = Math.max(0.0, G[var0]) * 0.15;
                           break;
                        case 2:
                           V[var0] = var23 - 1.0E-4;
                           y[var0] = Math.min(0.0, y[var0]) * 0.08;
                           break;
                        case 3:
                           V[var0] = var25 + 1.0E-4;
                           y[var0] = Math.max(0.0, y[var0]) * 0.08;
                           G[var0] = G[var0] * 0.97;
                           o[var0] = o[var0] * 0.97;
                           break;
                        case 4:
                           R[var0] = var27 - 1.0E-4;
                           o[var0] = Math.min(0.0, o[var0]) * 0.15;
                           break;
                        case 5:
                           R[var0] = var29 + 1.0E-4;
                           o[var0] = Math.max(0.0, o[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void f(int var0) {
      int var1 = --t;
      e[var0] = e[var1];
      V[var0] = V[var1];
      R[var0] = R[var1];
      q[var0] = q[var1];
      r[var0] = r[var1];
      C[var0] = C[var1];
      G[var0] = G[var1];
      y[var0] = y[var1];
      o[var0] = o[var1];
   }

   public static void v() {
      int var0 = !v.isEmpty() ? 1 : 0;
      t = 0;
      P = 0L;
      O = (boolean)0;
      v.clear();
      z.clear();
      X.clear();
      A = null;
      if (var0 != 0) {
         qs.O();
      }

      eu.n();
   }

   private ex() {
   }
}

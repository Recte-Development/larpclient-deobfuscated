package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public final class s_ {
   public static final String o = "no standing spot within reach";
   public static final String w = "keep walking";
   public static final double c = 1.62;
   public static final int F = 6;
   public static final int E = 8;
   public static final int R = 3000;
   public static final int G = 3;
   static final int q = 3;
   static final int B = 600;
   public static final int g = 8;
   static final double C = 1.2;
   private static final int Z = 256;
   private static final double[] K = new double[]{-0.35, 0.0, 0.35};
   static final int Q = 24;
   static final double u = 0.05;
   public static final int V = 3;

   private s_() {
   }

   public static int u(s_.k var0, s_.k var1) {
      int var2 = Math.abs(var1.x() - var0.x());
      int var3 = Math.abs(var1.y() - var0.y());
      int var4 = Math.abs(var1.z() - var0.z());
      if (var2 + var3 + var4 == 1) {
         return 0;
      }

      int var5 = Math.max(var2, Math.max(var3, var4));
      return var5 <= 1 ? 1 : var5 + 1;
   }

   public static int v(List<s_.k> var0, Predicate<s_.k> var1) {
      int var2 = Integer.MAX_VALUE;

      for (s_.k var4 : var0) {
         if (var4.y() < var2 && var1.test(var4)) {
            var2 = var4.y();
         }
      }

      return var2;
   }

   static boolean R(zf.q var0, s_.k var1) {
      return var0.loaded(var1.x(), var1.y() + 1, var1.z()) && !var0.open(var1.x(), var1.y() + 1, var1.z());
   }

   public static double D(int var0, int var1, int var2, s_.k var3) {
      double var4 = var3.x() - var0;
      double var6 = var3.y() + 0.5 - (var1 + 1.62);
      double var8 = var3.z() - var2;
      return var4 * var4 + var6 * var6 + var8 * var8;
   }

   public static List<s_.k> c(List<s_.k> var0, int var1, int var2, int var3, int var4, double var5) {
      ArrayList var7 = new ArrayList();
      double var8 = var5 * var5;

      for (s_.k var11 : var0) {
         if (var11.y() == var1 && D(var2, var3, var4, var11) <= var8) {
            var7.add(var11);
         }
      }

      var7.sort(Comparator.<s_.k>comparingDouble(var3x -> -D(var2, var3, var4, var3x)).thenComparingInt(s_.k::x).thenComparingInt(s_.k::z));
      return var7;
   }

   public static boolean g(s_.k var0, int var1, int var2, int var3) {
      return var0.x() == var1 && var0.z() == var3 && (var0.y() == var2 || var0.y() == var2 + 1);
   }

   public static s_.y E(
      zf.q var0,
      List<s_.k> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      u1.y var8,
      Predicate<s_.k> var9,
      s_.l var10,
      s_.l var11,
      int var12
   ) {
      return u(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, true, Set.of(), null);
   }

   public static s_.y C(
      zf.q var0,
      List<s_.k> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      u1.y var8,
      Predicate<s_.k> var9,
      s_.l var10,
      s_.l var11,
      int var12,
      boolean var13,
      Set<Long> var14
   ) {
      return u(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, null);
   }

   public static s_.y u(
      zf.q var0,
      List<s_.k> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      u1.y var8,
      Predicate<s_.k> var9,
      s_.l var10,
      s_.l var11,
      int var12,
      boolean var13,
      Set<Long> var14,
      s_.k var15
   ) {
      return l(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, var15, true);
   }

   public static s_.y l(
      zf.q var0,
      List<s_.k> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      u1.y var8,
      Predicate<s_.k> var9,
      s_.l var10,
      s_.l var11,
      int var12,
      boolean var13,
      Set<Long> var14,
      s_.k var15,
      boolean var16
   ) {
      int var17 = v(var1, var9);
      if (var17 == Integer.MAX_VALUE) {
         return s_.y.none(var1.isEmpty() ? null : "nothing to place the blocks against", var1);
      }

      ArrayList var18 = new ArrayList();
      int var19 = 0;

      for (s_.k var21 : var1) {
         if (var21.y() == var17) {
            var19++;
            if (var9.test(var21) && R(var0, var21)) {
               var18.add(var21);
            }
         }
      }

      if (!var18.isEmpty() && var18.size() < var19) {
         s_.y var33 = l(var0, var18, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, var15, var16);
         if (var33.place() != null || var16 && (var33.walk() || "keep walking".equals(var33.reason()))) {
            return var33;
         }
      }

      Boolean var34 = null;
      int var35 = 0;
      List var22 = c(var1, var17, var3, var4, var5, var6);
      Comparator var23 = Comparator.comparing(var1x -> !R(var0, var1x));
      if (var15 != null) {
         var23 = var23.thenComparingInt(var1x -> u(var15, var1x));
      }

      var22.sort(var23);
      HashSet var24 = new HashSet();

      for (s_.k var26 : var1) {
         var24.add(zf.O(var26.x(), var26.y(), var26.z()));
      }

      for (s_.k var38 : var22) {
         if (!g(var38, var3, var4, var5) && var9.test(var38) && (!var2.contains(var38.id()) || !var8.containsColumn(var3, var5))) {
            if (var35++ < var12) {
               if (!var10.r(var38, var3, var4, var5)) {
                  continue;
               }

               if (var38.y() >= var4 && var38.y() <= var4 + 2) {
                  if (var34 == null) {
                     var34 = G(var0, var3, var4, var5, var8);
                  }

                  if (var34 && !G(l(var0, var38), var3, var4, var5, var8)) {
                     var2.add(var38.id());
                     continue;
                  }
               }

               s_.k var43 = R(var0, var1, var38, var3, var4, var5, var9, var10, var8, var34);
               if (var43 != null) {
                  return s_.y.placing(var43);
               }

               if (var16 && var15 != null && u(var15, var38) > 3) {
                  ArrayList var44 = new ArrayList();

                  for (s_.k var50 : var1) {
                     if (var50.y() == var17 && u(var15, var50) <= 3 && var9.test(var50)) {
                        var44.add(var50);
                     }
                  }

                  if (!var44.isEmpty()) {
                     if (!var13) {
                        return s_.y.none("keep walking", List.of());
                     }

                     var44.sort(Comparator.<s_.k>comparingInt(var1x -> u(var15, var1x)).thenComparingDouble(var3x -> D(var3, var4, var5, var3x)));
                     List var47 = var44.subList(0, Math.min(6, var44.size()));
                     HashSet var51 = new HashSet();

                     for (s_.k var55 : var47) {
                        var51.addAll(N(var0, var55, var6, var8, var2.contains(var55.id()), var11, var24));
                     }

                     var51.remove(zf.O(var3, var4, var5));
                     var51.removeAll(var14);
                     if (!var51.isEmpty()) {
                        return s_.y.walking(var51, (s_.k)var47.getFirst(), new ArrayList<>(var47));
                     }
                  }
               }

               return s_.y.placing(var38);
            }
            break;
         }
      }

      if (!var13) {
         return s_.y.none("keep walking", List.of());
      }

      ArrayList var37 = new ArrayList();

      for (s_.k var27 : var1) {
         if (var27.y() == var17 && var9.test(var27)) {
            var37.add(var27);
         }
      }

      Comparator var40 = Comparator.comparing(var1x -> !R(var0, var1x));
      if (var15 != null) {
         var40 = var40.thenComparingInt(var1x -> u(var15, var1x));
      }

      var37.sort(var40.thenComparingDouble(var3x -> D(var3, var4, var5, var3x)));
      List var41 = var37.subList(0, Math.min(6, var37.size()));
      HashSet var28 = new HashSet();

      for (s_.k var30 : var41) {
         var28.addAll(N(var0, var30, var6, var8, var2.contains(var30.id()), var11, var24));
      }

      var28.remove(zf.O(var3, var4, var5));
      var28.removeAll(var14);
      List var45 = var41;

      for (byte var48 = 6; var28.isEmpty() && var16 && var48 < Math.min(var37.size(), 48); var48 += 6) {
         var41 = var37.subList(var48, Math.min(var48 + 6, var37.size()));

         for (s_.k var32 : var41) {
            var28.addAll(N(var0, var32, var6, var8, var2.contains(var32.id()), var11, var24));
         }

         var28.remove(zf.O(var3, var4, var5));
         var28.removeAll(var14);
      }

      if (var28.isEmpty()) {
         var41 = var45;
         byte var49 = 1;

         for (s_.k var54 : var41) {
            if (!var2.contains(var54.id())) {
               var49 = 0;
            }
         }

         return s_.y.none(var49 != 0 ? "it would wall you in, and no spot outside reaches it" : "no standing spot within reach", new ArrayList<>(var41));
      } else {
         return s_.y.walking(var28, (s_.k)var41.getFirst(), new ArrayList<>(var41));
      }
   }

   static s_.k R(zf.q var0, List<s_.k> var1, s_.k var2, int var3, int var4, int var5, Predicate<s_.k> var6, s_.l var7, u1.y var8, Boolean var9) {
      int[][] var10 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int[] var14 : var10) {
         int var15 = var2.x() + var14[0];
         int var16 = var2.z() + var14[1];
         s_.k var17 = null;

         for (s_.k var19 : var1) {
            if (var19.x() == var15 && var19.y() == var2.y() && var19.z() == var16) {
               var17 = var19;
               break;
            }
         }

         if (var17 != null && !g(var17, var3, var4, var5) && var6.test(var17)) {
            int var25 = 1;
            if (var0.loaded(var15, var2.y() + 1, var16) && !var0.open(var15, var2.y() + 1, var16)) {
               var25++;
            }

            for (int[] var22 : var10) {
               int var23 = var15 + var22[0];
               int var24 = var16 + var22[1];
               if ((var23 != var2.x() || var24 != var2.z()) && var0.loaded(var23, var2.y(), var24) && !var0.open(var23, var2.y(), var24)) {
                  var25++;
               }
            }

            if (var25 >= 2
               && var7.r(var17, var3, var4, var5)
               && (
                  var17.y() < var4
                     || var17.y() > var4 + 2
                     || (var9 == null ? !G(var0, var3, var4, var5, var8) : !var9)
                     || G(l(var0, var17), var3, var4, var5, var8)
               )) {
               return var17;
            }
         }
      }

      return null;
   }

   public static Set<Long> N(zf.q var0, s_.k var1, double var2, u1.y var4, boolean var5, s_.l var6, Set<Long> var7) {
      HashSet var8 = new HashSet();
      int var9 = (int)Math.ceil(var2);
      double var10 = (var2 - 0.3) * (var2 - 0.3);

      for (int var12 = -var9 - 1; var12 <= 1; var12++) {
         for (int var13 = -var9; var13 <= var9; var13++) {
            for (int var14 = -var9; var14 <= var9; var14++) {
               int var15 = var1.x() + var13;
               int var16 = var1.y() + var12;
               int var17 = var1.z() + var14;
               if (!g(var1, var15, var16, var17)
                  && !(D(var15, var16, var17, var1) > var10)
                  && (!var5 || !var4.containsColumn(var15, var17))
                  && var0.loaded(var15, var16, var17)
                  && var0.floor(var15, var16 - 1, var17)
                  && var0.open(var15, var16, var17)
                  && var0.open(var15, var16 + 1, var17)
                  && !var7.contains(zf.O(var15, var16, var17))
                  && !var7.contains(zf.O(var15, var16 + 1, var17))
                  && var6.r(var1, var15, var16, var17)) {
                  var8.add(zf.O(var15, var16, var17));
               }
            }
         }
      }

      return var8.isEmpty() && !var7.isEmpty() && var1.y() < var4.maxY() ? N(var0, var1, var2, var4, var5, var6, Set.of()) : var8;
   }

   public static s_.a I(zf.q var0, List<s_.k> var1, int var2, int var3, int var4, double var5, int var7, s_.u var8, s_.l var9, Set<Long> var10, int var11) {
      return R(var0, var1, var1, var2, var3, var4, var5, var7, var8, var9, var10, var11);
   }

   public static s_.a R(
      zf.q var0, List<s_.k> var1, List<s_.k> var2, int var3, int var4, int var5, double var6, int var8, s_.u var9, s_.l var10, Set<Long> var11, int var12
   ) {
      s_.a var13 = T(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, false, var4 + 1);
      return var13 != null ? var13 : T(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, true, var4 + 1);
   }

   private static s_.a T(
      zf.q var0,
      List<s_.k> var1,
      List<s_.k> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      int var8,
      s_.u var9,
      s_.l var10,
      Set<Long> var11,
      int var12,
      boolean var13,
      int var14
   ) {
      s_.a var15 = null;
      double var16 = -1.0;
      int var18 = Integer.MAX_VALUE;
      int var19 = 0;
      HashSet var20 = new HashSet();
      int var21 = (int)Math.floor(var6);

      for (s_.k var23 : var1) {
         for (int var24 = -var21; var24 <= var21; var24++) {
            for (int var25 = -var21; var25 <= var21; var25++) {
               int var26 = var23.x() + var24;
               int var27 = var23.z() + var25;
               long var28 = zf.O(var26, 0, var27);
               if (!(var24 * var24 + var25 * var25 > var6 * var6) && !var11.contains(var28) && var20.add(var28)) {
                  int var30 = Integer.MIN_VALUE;

                  for (int var31 = Math.min(var23.y(), var14); var31 >= var23.y() - var8 - 3; var31--) {
                     if (var0.loaded(var26, var31, var27)
                        && var0.floor(var26, var31 - 1, var27)
                        && var0.open(var26, var31, var27)
                        && var0.open(var26, var31 + 1, var27)) {
                        var30 = var31;
                        break;
                     }
                  }

                  if (var30 != Integer.MIN_VALUE && !var9.i(var26, var30, var27) && !var9.i(var26, var30 + 1, var27)) {
                     int var42 = Math.max(1, var23.y() - var30 - 2);
                     int var32 = Math.min(var8, var23.y() - var30 + 1);

                     for (int var33 = var42; var33 <= var32 && J(var0, var26, var30, var27, var33, var9, var13 ? var23.y() : Integer.MAX_VALUE); var33++) {
                        if (var19++ >= var12) {
                           return var15;
                        }

                        if (var10.r(var23, var26, var30 + var33, var27)) {
                           ArrayList var34 = new ArrayList();

                           for (s_.k var36 : var1) {
                              if (var36 == var23 || var10.r(var36, var26, var30 + var33, var27)) {
                                 var34.add(var36);
                              }
                           }

                           var19 += var1.size() - 1;
                           int var43 = 0;
                           double var44 = var6 * var6;

                           for (s_.k var39 : var2) {
                              if (D(var26, var30 + var33, var27, var39) <= var44) {
                                 var43++;
                              }
                           }

                           double var45 = (double)(var34.size() * 2 + var43) / (var33 + 3);
                           int var40 = Math.abs(var26 - var3) + Math.abs(var27 - var5) + Math.abs(var30 - var4);
                           int var41 = Math.abs(var45 - var16) < 1.0E-9 ? 1 : 0;
                           if (var45 > var16 && var41 == 0 || var41 != 0 && (var33 < var15.height() || var33 == var15.height() && var40 < var18)) {
                              var15 = new s_.a(var26, var30, var27, var33, var34);
                              var16 = var45;
                              var18 = var40;
                           }
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      return var15;
   }

   public static int D(zf.q var0, List<s_.k> var1, s_.a var2, int var3, int var4, s_.u var5, s_.l var6) {
      int var7 = Math.min(var3, var2.height() + Math.min(2, var4));

      for (int var8 = var2.height() + 1; var8 <= var7; var8++) {
         for (int var9 = var2.topY(); var9 <= var2.ground() + var8 + 1; var9++) {
            if (!var0.loaded(var2.x(), var9, var2.z()) || !var0.open(var2.x(), var9, var2.z()) || var5.i(var2.x(), var9, var2.z())) {
               return 0;
            }
         }

         for (s_.k var10 : var1) {
            if (var6.r(var10, var2.x(), var2.ground() + var8, var2.z())) {
               return var8;
            }
         }
      }

      return 0;
   }

   public static int[] K(zf.q var0, List<s_.k> var1, int var2, int var3, int var4, int var5, double var6, s_.u var8, s_.l var9) {
      return L(var0, var1, var2, var3, var4, var5, var6, var8, var9, Set.of());
   }

   public static int[] L(zf.q var0, List<s_.k> var1, int var2, int var3, int var4, int var5, double var6, s_.u var8, s_.l var9, Set<Long> var10) {
      int[][] var11 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      HashSet var12 = new HashSet(var10);
      var12.add(zf.O(var2, 0, var4));
      ArrayList var13 = new ArrayList();
      var13.add(new int[]{var2, var4, 0, 0});
      int var14 = 0;

      for (int var15 = 1; var15 <= var5; var15++) {
         ArrayList var16 = new ArrayList();

         for (int[] var18 : var13) {
            for (int[] var22 : var11) {
               int var23 = var18[0] + var22[0];
               int var24 = var18[1] + var22[1];
               if (var12.add(zf.O(var23, 0, var24)) && I(var0, var23, var3, var24, var8)) {
                  var16.add(new int[]{var23, var24, var15 == 1 ? var22[0] : var18[2], var15 == 1 ? var22[1] : var18[3]});
               }
            }
         }

         for (int[] var26 : var16) {
            for (s_.k var28 : var1) {
               if (!(D(var26[0], var3, var26[1], var28) > var6 * var6)) {
                  if (var14++ >= 600) {
                     return null;
                  }

                  if (var9.r(var28, var26[0], var3, var26[1])) {
                     return new int[]{var26[2], var26[3]};
                  }
               }
            }
         }

         var13 = var16;
      }

      return null;
   }

   static boolean I(zf.q var0, int var1, int var2, int var3, s_.u var4) {
      for (int var5 = var2; var5 <= var2 + 1; var5++) {
         if (!var0.loaded(var1, var5, var3) || !var0.open(var1, var5, var3) || var4.i(var1, var5, var3)) {
            return false;
         }
      }

      return !var0.loaded(var1, var2 - 1, var3) ? false : var0.floor(var1, var2 - 1, var3) || var0.open(var1, var2 - 1, var3) && !var4.i(var1, var2 - 1, var3);
   }

   public static s_.a O(zf.q var0, Set<Long> var1, List<s_.k> var2, int var3, s_.u var4, Set<Long> var5) {
      int[][] var6 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      s_.a var7 = null;
      int var8 = Integer.MAX_VALUE;

      for (long var10 : var1) {
         int var12 = zf.d(var10);
         HashSet var13 = new HashSet<>(Set.of(zf.O(zf.W(var10), 0, zf.F(var10))));
         ArrayList var14 = new ArrayList();
         var14.add(new int[]{zf.W(var10), zf.F(var10)});

         for (int var15 = 0; var15 <= 8 && !var14.isEmpty(); var15++) {
            ArrayList var16 = new ArrayList();

            for (int[] var18 : var14) {
               for (int[] var22 : var6) {
                  int var23 = var18[0] + var22[0];
                  int var24 = var18[1] + var22[1];
                  if (var13.add(zf.O(var23, 0, var24))) {
                     if (var0.loaded(var23, var12 - 1, var24)
                        && var0.floor(var23, var12 - 1, var24)
                        && var0.loaded(var23, var12, var24)
                        && var0.open(var23, var12, var24)
                        && var0.open(var23, var12 + 1, var24)) {
                        var16.add(new int[]{var23, var24});
                     } else if (!var5.contains(zf.O(var23, 0, var24))) {
                        int var25 = j(var0, var23, var12, var24, var3, var4);
                        if (var25 != Integer.MIN_VALUE) {
                           int var26 = (var12 - var25) * 4 + var15;
                           if (var26 < var8) {
                              var8 = var26;
                              var7 = new s_.a(var23, var25, var24, var12 - var25, var2);
                           }
                        }
                     }
                  }
               }
            }

            var14 = var16;
         }
      }

      return var7;
   }

   public static s_.a B(zf.q var0, s_.j var1, int var2, s_.u var3, List<s_.k> var4) {
      int var5 = j(var0, var1.outX(), var1.outY(), var1.outZ(), var2, var3);
      return var5 == Integer.MIN_VALUE ? null : new s_.a(var1.outX(), var5, var1.outZ(), var1.outY() - var5, var4);
   }

   private static int j(zf.q var0, int var1, int var2, int var3, int var4, s_.u var5) {
      for (int var6 = var2 + 1; var6 >= var2 - var4; var6--) {
         if (!var0.loaded(var1, var6, var3) || !var0.open(var1, var6, var3) || var5.i(var1, var6, var3)) {
            return Integer.MIN_VALUE;
         }

         if (var6 < var2 && var0.floor(var1, var6 - 1, var3)) {
            return var6;
         }
      }

      return Integer.MIN_VALUE;
   }

   public static List<s_.j> F(s_.u var0, s_.u var1, List<s_.k> var2, u1.y var3, double var4) {
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList(var2);

      while (!var7.isEmpty()) {
         s_.k var8 = (s_.k)var7.removeFirst();
         Set var9 = n(var0, var8, var4);
         Set var10 = var9.isEmpty() ? null : u(var0, var9, var3);
         if (var10 != null) {
            ArrayList var11 = new ArrayList<>(List.of(var8));
            Iterator var12 = var7.iterator();

            while (var12.hasNext()) {
               s_.k var13 = (s_.k)var12.next();
               if (n(var0, var13, var4).stream().anyMatch(var10::contains)) {
                  var11.add(var13);
                  var12.remove();
               }
            }

            s_.j var14 = q(var0, var1, var10, var11);
            if (var14 != null) {
               var6.add(var14);
            }
         }
      }

      return var6;
   }

   static Set<Long> n(s_.u var0, s_.k var1, double var2) {
      HashSet var4 = new HashSet();
      if (var0.i(var1.x(), var1.y() + 1, var1.z())) {
         return var4;
      }

      int var5 = (int)Math.ceil(var2);

      for (int var6 = var1.y() - var5 - 1; var6 < var1.y(); var6++) {
         for (int var7 = var1.x() - var5; var7 <= var1.x() + var5; var7++) {
            for (int var8 = var1.z() - var5; var8 <= var1.z() + var5; var8++) {
               if (!var0.i(var7, var6 - 1, var8) && var0.i(var7, var6, var8) && var0.i(var7, var6 + 1, var8) && Z(var0, var1, var7, var6, var8, var2)) {
                  var4.add(zf.O(var7, var6, var8));
               }
            }
         }
      }

      return var4;
   }

   private static boolean Z(s_.u var0, s_.k var1, int var2, int var3, int var4, double var5) {
      double var7 = var2 + 0.5;
      double var9 = var3 + 1.62;
      double var11 = var4 + 0.5;
      double var13 = var1.y() + 1 - 0.001;

      for (double var18 : K) {
         for (double var23 : K) {
            double var25 = var1.x() + 0.5 + var18 - var7;
            double var27 = var13 - var9;
            double var29 = var1.z() + 0.5 + var23 - var11;
            double var31 = Math.sqrt(var25 * var25 + var27 * var27 + var29 * var29);
            if (!(var31 > var5) && !(var27 < Math.max(Math.abs(var25), Math.abs(var29)) * 1.2)) {
               byte var33 = 1;

               for (double var34 = 0.0; var34 < 1.0 && var33 != 0; var34 += 0.04 / var31) {
                  int var36 = (int)Math.floor(var7 + var25 * var34);
                  int var37 = (int)Math.floor(var9 + var27 * var34);
                  int var38 = (int)Math.floor(var11 + var29 * var34);
                  if ((var36 != var1.x() || var37 != var1.y() || var38 != var1.z()) && !var0.i(var36, var37, var38)) {
                     var33 = 0;
                  }
               }

               if (var33 != 0) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private static Set<Long> u(s_.u var0, Set<Long> var1, u1.y var2) {
      int[][] var3 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      HashSet var4 = new HashSet(var1);
      ArrayList var5 = new ArrayList(var1);

      while (!var5.isEmpty()) {
         long var6 = (Long)var5.removeLast();
         int var8 = zf.W(var6);
         int var9 = zf.d(var6);
         int var10 = zf.F(var6);
         if (!var2.containsColumn(var8, var10) || var4.size() > 256) {
            return null;
         }

         for (int[] var14 : var3) {
            int var15 = var8 + var14[0];
            int var16 = var10 + var14[1];
            int var17;
            if (var0.i(var15, var9, var16) && var0.i(var15, var9 + 1, var16)) {
               if (!var0.i(var15, var9 - 1, var16)) {
                  var17 = var9;
               } else {
                  if (var0.i(var15, var9 - 2, var16)) {
                     return null;
                  }

                  var17 = var9 - 1;
               }
            } else {
               if (var0.i(var15, var9, var16) || !var0.i(var15, var9 + 1, var16) || !var0.i(var15, var9 + 2, var16) || !var0.i(var8, var9 + 2, var10)) {
                  continue;
               }

               var17 = var9 + 1;
            }

            if (var4.add(zf.O(var15, var17, var16))) {
               var5.add(zf.O(var15, var17, var16));
            }
         }
      }

      return var4;
   }

   private static s_.j q(s_.u var0, s_.u var1, Set<Long> var2, List<s_.k> var3) {
      int[][] var4 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      s_.j var5 = null;
      int var6 = Integer.MAX_VALUE;

      for (long var8 : var2) {
         int var10 = zf.W(var8);
         int var11 = zf.d(var8);
         int var12 = zf.F(var8);

         for (int[] var16 : var4) {
            int var17 = var10 + var16[0];
            int var18 = var12 + var16[1];
            int var19 = var17 + var16[0];
            int var20 = var18 + var16[1];
            if (!var0.i(var17, var11 - 1, var18)) {
               boolean var21 = var0.i(var17, var11, var18);
               boolean var22 = var0.i(var17, var11 + 1, var18);
               if ((!var21 || !var22)
                  && (var21 || var1.i(var17, var11, var18))
                  && (var22 || var1.i(var17, var11 + 1, var18))
                  && var0.i(var19, var11, var20)
                  && var0.i(var19, var11 + 1, var20)) {
                  int var23 = 0;

                  while (var23 < 4 && var0.i(var19, var11 + 2 + var23, var20)) {
                     var23++;
                  }

                  int var24 = ((var21 ? 0 : 1) + (var22 ? 0 : 1)) * 100 + (4 - var23) * 10;
                  if (var24 < var6) {
                     var6 = var24;
                     ArrayList var25 = new ArrayList();
                     if (!var21) {
                        var25.add(new int[]{var17, var11, var18});
                     }

                     if (!var22) {
                        var25.add(new int[]{var17, var11 + 1, var18});
                     }

                     var5 = new s_.j(var25, var19, var11, var20, var3, var2);
                  }
               }
            }
         }
      }

      return var5;
   }

   public static s_.z i(zf.q var0, List<s_.k> var1, s_.u var2, s_.u var3, Predicate<s_.k> var4, Set<Long> var5, int var6, int var7, int var8) {
      return L(var0, var1, var2, var3, var4, var5, var6, var7, var8, var0x -> true);
   }

   public static s_.z L(
      zf.q var0, List<s_.k> var1, s_.u var2, s_.u var3, Predicate<s_.k> var4, Set<Long> var5, int var6, int var7, int var8, Predicate<s_.z> var9
   ) {
      int[][] var10 = new int[][]{{0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
      ArrayList var11 = new ArrayList();
      HashMap var12 = new HashMap();

      for (s_.k var14 : var1) {
         if (!var5.contains(zf.O(var14.x(), var14.y(), var14.z())) && var4.test(var14)) {
            for (int var15 = 0; var15 < var10.length; var15++) {
               int var16 = var14.x() + var10[var15][0];
               int var17 = var14.y() + var10[var15][1];
               int var18 = var14.z() + var10[var15][2];
               int var19 = c(var0, var16, var17, var18, var2, var3);
               if (var19 != 0) {
                  s_.z var20 = new s_.z(var14, var16, var17 - var19 + 1, var18, var19);
                  var11.add(var20);
                  var12.put(var20, var19 * 1000 + (var15 == 0 ? 0 : 500) + Math.abs(var16 - var6) + Math.abs(var17 - var7) + Math.abs(var18 - var8));
               }
            }
         }
      }

      var11.sort(Comparator.comparingInt(var12::get));
      int var21 = 0;

      for (s_.z var23 : var11) {
         if (var21++ >= 24) {
            return null;
         }

         if (var9.test(var23)) {
            return var23;
         }
      }

      return null;
   }

   public static boolean y(s_.z var0, int var1, int var2, int var3) {
      s_.k var4 = var0.seed();
      if (var1 == var0.x() && var3 == var0.z()) {
         return false;
      }

      int var5 = var0.x() == var4.x() && var0.z() == var4.z() ? 1 : 0;
      return var5 == 0 && var0.height() == 1 || var2 + 1.62 > var4.y() + 0.05;
   }

   public static s_.a O(
      zf.q var0,
      List<s_.k> var1,
      int var2,
      int var3,
      int var4,
      double var5,
      int var7,
      s_.u var8,
      s_.u var9,
      Predicate<s_.k> var10,
      s_.x var11,
      Set<Long> var12,
      Set<Long> var13,
      int var14
   ) {
      ArrayList var15 = new ArrayList();
      HashMap var16 = new HashMap();

      for (s_.k var18 : var1) {
         if (!var12.contains(zf.O(var18.x(), var18.y(), var18.z())) && var10.test(var18)) {
            int var19 = c(var0, var18.x(), var18.y() - 1, var18.z(), var8, var9);
            if (var19 != 0) {
               var15.add(var18);
               var16.put(var18, new s_.z(var18, var18.x(), var18.y() - var19, var18.z(), var19));
            }
         }
      }

      return R(
         var0,
         var15,
         var1,
         var2,
         var3,
         var4,
         var5,
         var7,
         var8,
         (var2x, var3x, var4x, var5x) -> var16.containsKey(var2x)
            && y((s_.z)var16.get(var2x), var3x, var4x, var5x)
            && var11.y((s_.z)var16.get(var2x), var3x, var4x, var5x),
         var13,
         var14
      );
   }

   static int c(zf.q var0, int var1, int var2, int var3, s_.u var4, s_.u var5) {
      int var6 = 1;

      while (var6 <= 3) {
         int var7 = var2 - var6 + 1;
         if (var0.loaded(var1, var7, var3) && var0.open(var1, var7, var3) && !var4.i(var1, var7, var3)) {
            if (!var0.floor(var1, var7 - 1, var3) && !var5.i(var1, var7, var3)) {
               var6++;
               continue;
            }

            return var6;
         }

         return 0;
      }

      return 0;
   }

   static boolean r(zf.q var0, int var1, int var2, int var3, int var4, s_.u var5) {
      return J(var0, var1, var2, var3, var4, var5, Integer.MAX_VALUE);
   }

   static boolean J(zf.q var0, int var1, int var2, int var3, int var4, s_.u var5, int var6) {
      for (int var7 = var2; var7 <= var2 + var4 + 1; var7++) {
         if (!var0.loaded(var1, var7, var3) || !var0.open(var1, var7, var3) || (var7 < var2 + var4 || var7 <= var6) && var5.i(var1, var7, var3)) {
            return false;
         }
      }

      return true;
   }

   public static boolean G(zf.q var0, int var1, int var2, int var3, final u1.y var4) {
      if (!var4.containsColumn(var1, var3)) {
         return true;
      }

      zf.f var5 = new zf.f() {
         @Override
         public boolean reached(int var1, int var2x, int var3x) {
            return !var4.containsColumn(var1, var3x);
         }

         @Override
         public double estimate(int var1, int var2x, int var3x) {
            return Math.max(0, Math.min(Math.min(var1 - var4.minX() + 1, var4.maxX() + 1 - var1), Math.min(var3x - var4.minZ() + 1, var4.maxZ() + 1 - var3x)));
         }
      };
      zf.s var6 = new zf.s(var4.minX() - 3, Math.min(var2, var4.minY()) - 4, var4.minZ() - 3, var4.maxX() + 3, Math.max(var2, var4.maxY()) + 4, var4.maxZ() + 3);
      return zf.B(var0, var1, var2, var3, var5, var6, zf.z.walking(3000)).found();
   }

   public static zf.q l(final zf.q var0, s_.k var1) {
      final long var2 = zf.O(var1.x(), var1.y(), var1.z());
      return new zf.q() {
         @Override
         public boolean loaded(int var1, int var2x, int var3) {
            return var0.loaded(var1, var2x, var3);
         }

         @Override
         public boolean open(int var1, int var2x, int var3) {
            return zf.O(var1, var2x, var3) != var2 && var0.open(var1, var2x, var3);
         }

         @Override
         public boolean floor(int var1, int var2x, int var3) {
            return zf.O(var1, var2x, var3) == var2 || var0.floor(var1, var2x, var3);
         }

         @Override
         public double digCost(int var1, int var2x, int var3) {
            return zf.O(var1, var2x, var3) == var2 ? -1.0 : var0.digCost(var1, var2x, var3);
         }

         @Override
         public double penalty(int var1, int var2x, int var3) {
            return var0.penalty(var1, var2x, var3);
         }

         @Override
         public boolean swim(int var1, int var2x, int var3) {
            return zf.O(var1, var2x, var3) != var2 && var0.swim(var1, var2x, var3);
         }
      };
   }

   public record a(int x, int ground, int z, int height, List<s_.k> unlocks) {
      public int topY() {
         return this.ground + this.height;
      }
   }

   public record j(List<int[]> cells, int outX, int outY, int outZ, List<s_.k> aims, Set<Long> space) {
   }

   public record k(int x, int y, int z, int id) {
   }

   public interface l {
      boolean r(s_.k var1, int var2, int var3, int var4);
   }

   public static final class m {
      private static final long q = Long.MIN_VALUE;
      private final Set<Long> H = new HashSet<>();
      private long T = Long.MIN_VALUE;

      public void v(int var1, int var2, int var3) {
         this.T = zf.O(var1, var2, var3);
      }

      public void x() {
         this.H.clear();
         this.T = Long.MIN_VALUE;
      }

      public boolean p() {
         if (this.T == Long.MIN_VALUE) {
            return false;
         }

         this.H.add(this.T);
         this.T = Long.MIN_VALUE;
         return true;
      }

      public void D(int var1, int var2, int var3) {
         this.H.add(zf.O(var1, var2, var3));
      }

      public Set<Long> e() {
         return this.H;
      }

      public void v() {
         this.H.clear();
         this.T = Long.MIN_VALUE;
      }
   }

   public interface u {
      boolean i(int var1, int var2, int var3);
   }

   public interface x {
      boolean y(s_.z var1, int var2, int var3, int var4);
   }

   public record y(s_.k place, Set<Long> stands, s_.k toward, List<s_.k> walkingFor, String reason) {
      static s_.y placing(s_.k var0) {
         return new s_.y(var0, Set.of(), null, List.of(), null);
      }

      static s_.y walking(Set<Long> var0, s_.k var1, List<s_.k> var2) {
         return new s_.y(null, var0, var1, var2, null);
      }

      static s_.y none(String var0, List<s_.k> var1) {
         return new s_.y(null, Set.of(), null, var1, var0);
      }

      public boolean walk() {
         return !this.stands.isEmpty();
      }
   }

   public record z(s_.k seed, int x, int ground, int z, int height) {
      public int top() {
         return this.ground + this.height - 1;
      }
   }
}

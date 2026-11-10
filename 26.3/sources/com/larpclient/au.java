package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class au {
   static final double h = 1.62;
   public static final double v = 4.3;
   static final int X = 32;

   public static int z(au.l var0, int var1, int var2, int var3, Map<Long, Integer> var4) {
      if (!var0.maybeHasAnchor()) {
         return 0;
      }

      int var5 = 0;

      for (int var6 = 0; var6 < 16; var6++) {
         for (int var7 = 0; var7 < 16; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               BlockState var9 = var0.state(var8, var6, var7);
               if (var9.is(Blocks.RESPAWN_ANCHOR)) {
                  var4.put(hn.f(var1 + var8, var2 + var6, var3 + var7), q(var9));
                  var5++;
               }
            }
         }
      }

      return var5;
   }

   public static int q(BlockState var0) {
      return var0.hasProperty(RespawnAnchorBlock.CHARGE) ? (Integer)var0.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   public static au.q b(au.n var0, int var1, int var2, int var3, au.z var4) {
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      int var7 = 0;
      int var8 = 0;
      int var9 = (int)Math.ceil(4.3);

      for (int var10 = (int)Math.floor(var2 + 0.5 - 4.3 - 1.62); var10 <= (int)Math.ceil(var2 + 0.5 + 4.3 - 1.62); var10++) {
         for (int var11 = var1 - var9; var11 <= var1 + var9; var11++) {
            for (int var12 = var3 - var9; var12 <= var3 + var9; var12++) {
               if (var11 != var1 || var12 != var3 || var10 < var2 - 1 || var10 > var2 + 1) {
                  double var13 = var11 + 0.5;
                  double var15 = var10 + 1.62;
                  double var17 = var12 + 0.5;
                  if (!(P(var13, var15, var17, var1, var2, var3) > 4.3)
                     && var0.loaded(var11, var10, var12)
                     && var0.floor(var11, var10 - 1, var12)
                     && var0.open(var11, var10, var12)
                     && var0.open(var11, var10 + 1, var12)
                     && (var11 != var1 || var12 != var3 || var10 - 1 != var2)) {
                     if (B(var0, var11, var10, var12) || B(var0, var11, var10 + 1, var12)) {
                        var7++;
                     } else if (var0.water(var11, var10, var12) || var0.water(var11, var10 + 1, var12)) {
                        var8++;
                     } else if (var4 != null && var4.F(var13, var15, var17, var1, var2, var3)) {
                        var5.add(new au.m(var11, var10, var12, true, 0, p(var0, var11, var10, var12, var1, var2, var3)));
                     }
                  }
               }
            }
         }
      }

      int[][] var22 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int var23 = 0; var23 >= -1; var23--) {
         for (int[] var27 : var22) {
            int var16 = var1 + var27[0];
            int var28 = var2 + var23;
            int var18 = var3 + var27[1];
            if (var0.loaded(var16, var28, var18) && var0.floor(var16, var28 - 1, var18)) {
               if (!B(var0, var16, var28, var18) && !B(var0, var16, var28 + 1, var18)) {
                  int var19 = 0;
                  byte var20 = 1;

                  for (int var21 = 0; var21 <= 1 && var20 != 0; var21++) {
                     if (!var0.open(var16, var28 + var21, var18)) {
                        if (!var0.water(var16, var28 + var21, var18) && !(var0.digCost(var16, var28 + var21, var18) < 0.0)) {
                           var19++;
                        } else {
                           var20 = 0;
                        }
                     }
                  }

                  if (var20 != 0 && var19 != 0) {
                     var6.add(new au.m(var16, var28, var18, false, var19, 2 + var19 + p(var0, var16, var28, var18, var1, var2, var3)));
                  }
               } else {
                  var7++;
               }
            }
         }
      }

      Comparator var24 = Comparator.comparingDouble(au.m::cost).thenComparingInt(au.m::y).thenComparingInt(au.m::x).thenComparingInt(au.m::z);
      var5.sort(var24);
      var6.sort(var24);
      return new au.q(List.copyOf(var5), List.copyOf(var6), var7, var8);
   }

   static double p(au.n var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      double var7 = var1 + 0.5;
      double var9 = var2 + 1.62;
      double var11 = var3 + 0.5;
      double var13 = P(var7, var9, var11, var4, var5, var6);
      double var15 = Math.toDegrees(Math.atan2(var9 - (var5 + 0.5), Math.hypot(var4 + 0.5 - var7, var6 + 0.5 - var11)));
      double var17 = var13;
      if (var13 < 1.5) {
         var17 += (1.5 - var13) * 2.0;
      }

      if (var13 > 3.5) {
         var17 += (var13 - 3.5) * 2.0;
      }

      if (var15 < -20.0) {
         var17 += (-20.0 - var15) / 10.0;
      }

      if (var15 > 60.0) {
         var17 += (var15 - 60.0) / 10.0;
      }

      if (P(var0, var1, var2, var3)) {
         var17 += 2.0;
      }

      return var17;
   }

   private static boolean P(au.n var0, int var1, int var2, int var3) {
      int[][] var4 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int[] var8 : var4) {
         int var9 = var1 + var8[0];
         int var10 = var3 + var8[1];
         if (var0.open(var9, var2, var10) && var0.open(var9, var2 + 1, var10)) {
            int var11 = 0;

            while (
               var11 < 4
                  && var0.loaded(var9, var2 - 1 - var11, var10)
                  && !var0.solid(var9, var2 - 1 - var11, var10)
                  && !var0.water(var9, var2 - 1 - var11, var10)
            ) {
               var11++;
            }

            if (var11 > 3) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean B(au.n var0, int var1, int var2, int var3) {
      return var0.lava(var1 + 1, var2, var3)
         || var0.lava(var1 - 1, var2, var3)
         || var0.lava(var1, var2 + 1, var3)
         || var0.lava(var1, var2 - 1, var3)
         || var0.lava(var1, var2, var3 + 1)
         || var0.lava(var1, var2, var3 - 1);
   }

   public static String s(au.n var0, int var1, int var2, int var3, int var4, au.q var5, boolean var6, boolean var7) {
      if (var7) {
         return "protected";
      } else if (B(var0, var1, var2, var3)) {
         return "lava";
      } else if (var4 - var2 > 32) {
         return "deep";
      } else if (var5 == null) {
         return "unreachable";
      } else if (!var5.exposed().isEmpty()) {
         return null;
      } else if (!var5.dig().isEmpty()) {
         return var6 ? null : "hidden";
      } else if (var5.wet() > 0) {
         return "underwater";
      } else {
         return var5.nextToLava() > 0 ? "lava nearby" : "unreachable";
      }
   }

   public static int[] T(au.n var0, int var1, int var2, int var3) {
      int[] var4 = null;
      double var5 = Double.MAX_VALUE;

      for (int var7 = var2 - 1; var7 <= var2 + 1; var7++) {
         for (int var8 = var1 - 2; var8 <= var1 + 2; var8++) {
            for (int var9 = var3 - 2; var9 <= var3 + 2; var9++) {
               if ((var8 != var1 || var9 != var3)
                  && var0.loaded(var8, var7, var9)
                  && var0.open(var8, var7, var9)
                  && !var0.water(var8, var7, var9)
                  && !var0.lava(var8, var7, var9)
                  && var0.floor(var8, var7 - 1, var9)
                  && !var0.solid(var8, var7 + 1, var9)
                  && !B(var0, var8, var7, var9)) {
                  double var10 = P(var1 + 0.5, var2 + 1.62, var3 + 0.5, var8, var7, var9);
                  if (!(var10 > 4.0)) {
                     double var12 = Math.abs(var10 - 2.0) + Math.abs(var7 - var2) * 0.5;
                     if (var12 < var5) {
                        var5 = var12;
                        var4 = new int[]{var8, var7, var9};
                     }
                  }
               }
            }
         }
      }

      return var4;
   }

   static double P(double var0, double var2, double var4, int var6, int var7, int var8) {
      double var9 = var6 + 0.5 - var0;
      double var11 = var7 + 0.5 - var2;
      double var13 = var8 + 0.5 - var4;
      return Math.sqrt(var9 * var9 + var11 * var11 + var13 * var13);
   }

   public static au.z s(au.n var0) {
      return (var1, var3, var5, var7, var8, var9) -> {
         double[][] var10 = new double[][]{{0.5, 0.99, 0.5}, {0.5, 0.01, 0.5}, {0.01, 0.5, 0.5}, {0.99, 0.5, 0.5}, {0.5, 0.5, 0.01}, {0.5, 0.5, 0.99}};

         for (double[] var14 : var10) {
            if (d(var0, var1, var3, var5, var7 + var14[0], var8 + var14[1], var9 + var14[2], var7, var8, var9)) {
               return true;
            }
         }

         return false;
      };
   }

   private static boolean d(au.n var0, double var1, double var3, double var5, double var7, double var9, double var11, int var13, int var14, int var15) {
      double var16 = var7 - var1;
      double var18 = var9 - var3;
      double var20 = var11 - var5;
      int var22 = Math.max(1, (int)Math.ceil(Math.sqrt(var16 * var16 + var18 * var18 + var20 * var20) / 0.05));

      for (int var23 = 1; var23 <= var22; var23++) {
         double var24 = (double)var23 / var22;
         int var26 = (int)Math.floor(var1 + var16 * var24);
         int var27 = (int)Math.floor(var3 + var18 * var24);
         int var28 = (int)Math.floor(var5 + var20 * var24);
         if (var26 == var13 && var27 == var14 && var28 == var15) {
            return true;
         }

         if (var0.solid(var26, var27, var28)) {
            return false;
         }
      }

      return false;
   }

   private au() {
   }

   public interface l {
      boolean maybeHasAnchor();

      BlockState state(int var1, int var2, int var3);
   }

   public record m(int x, int y, int z, boolean exposed, int digs, double cost) {
   }

   public interface n extends hn.s {
      boolean lava(int var1, int var2, int var3);

      boolean water(int var1, int var2, int var3);

      boolean solid(int var1, int var2, int var3);
   }

   public record q(List<au.m> exposed, List<au.m> dig, int nextToLava, int wet) {
      public List<au.m> usable(boolean var1) {
         return this.exposed.isEmpty() && var1 ? this.dig : this.exposed;
      }
   }

   public interface z {
      boolean F(double var1, double var3, double var5, int var7, int var8, int var9);
   }
}

package com.larpclient.util.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class SafeSpotFinder {
   static final int maxQueries = 12000;
   private static final int[][] neighborOffsets = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
   private static final int[][] roughnessOffsets;
   private static final int[][] outerRingOffsets;
   private final SafeSpotFinder.TerrainView terrain;
   private final Map<Long, Integer> surfaceCache = new HashMap<>();
   private final Map<Long, SafeSpotFinder.GroundType> groundCache = new HashMap<>();
   private final double originX;
   private final double originZ;
   private final double targetX;
   private final double targetZ;
   private final int maxRadius;
   private final boolean allowWater;
   private final boolean allowRisky;
   private int queryCount;
   private SafeSpotFinder.SafeSpot best;
   private static final double[][] rayDirections;
   static final double slopePerBlock = 0.25;
   static final int maxRayDistance = 24;

   private SafeSpotFinder(SafeSpotFinder.TerrainView var1, double var2, double var4, double var6, double var8, int var10, boolean var11, boolean var12) {
      this.terrain = var1;
      this.originX = var2;
      this.originZ = var4;
      this.targetX = var6;
      this.targetZ = var8;
      this.maxRadius = var10;
      this.allowWater = var11;
      this.allowRisky = var12;
   }

   public static SafeSpotFinder.SafeSpot find(
      SafeSpotFinder.TerrainView var0, double var1, double var3, double var5, double var7, int var9, boolean var10, boolean var11
   ) {
      if (var0 != null && Double.isFinite(var1) && Double.isFinite(var3)) {
         SafeSpotFinder var12 = new SafeSpotFinder(var0, var1, var3, var5, var7, Math.clamp(var9, 0, 512), var10, var11);
         return var12.search();
      } else {
         return null;
      }
   }

   private SafeSpotFinder.SafeSpot search() {
      for (byte var1 = 0;
         var1 <= this.maxRadius && this.queryCount < 12000 && (this.best == null || !(var1 - 12 >= this.best.score()));
         var1 += var1 < 6 ? 1 : 3
      ) {
         int var2 = var1 == 0 ? 1 : Math.max(6, (int)Math.ceil((Math.PI * 2) * var1 / 3.0));
         int var3 = Integer.MIN_VALUE;
         int var4 = Integer.MIN_VALUE;

         for (int var5 = 0; var5 < var2 && this.queryCount < 12000; var5++) {
            double var6 = (Math.PI * 2) * var5 / var2;
            int var8 = (int)Math.floor(this.originX + var1 * Math.cos(var6));
            int var9 = (int)Math.floor(this.originZ + var1 * Math.sin(var6));
            if (var8 != var3 || var9 != var4) {
               var3 = var8;
               var4 = var9;
               this.evaluate(var8, var9);
            }
         }
      }

      return this.best;
   }

   private boolean isAcceptable(SafeSpotFinder.GroundType var1) {
      return var1 == SafeSpotFinder.GroundType.SOLID
         || this.allowWater && var1 == SafeSpotFinder.GroundType.WATER
         || this.allowWater && this.allowRisky && var1 == SafeSpotFinder.GroundType.LEAVES;
   }

   private void evaluate(int var1, int var2) {
      double var3 = var1 + 0.5 - this.originX;
      double var5 = var2 + 0.5 - this.originZ;
      double var7 = Math.sqrt(var3 * var3 + var5 * var5);
      if (!(var7 > this.maxRadius + 0.75)) {
         int var9 = this.surfaceAt(var1, var2);
         if (var9 != Integer.MIN_VALUE) {
            SafeSpotFinder.GroundType var10 = this.groundAt(var1, var9, var2);
            if (this.isAcceptable(var10)) {
               double var11 = var7 + (var10 == SafeSpotFinder.GroundType.WATER ? 400 : 0) + (var10 == SafeSpotFinder.GroundType.LEAVES ? 240 : 0);
               if (this.best == null || !(var11 - 10.0 >= this.best.score())) {
                  byte var13 = 0;
                  if (!this.isFlatNeighborhood(var1, var9, var2, var10)) {
                     if (!this.allowRisky || var10 != SafeSpotFinder.GroundType.SOLID || !this.isNearlyFlat(var1, var9, var2)) {
                        return;
                     }

                     var13 = 1;
                  }

                  double var14 = this.roughness(var1, var9, var2);
                  if (!Double.isNaN(var14)) {
                     int var16 = this.countOpenDirections(var1, var9, var2);
                     if (var16 != 0) {
                        int var17 = 0;
                        double var18 = 0.0;

                        for (int[] var23 : outerRingOffsets) {
                           int var24 = this.surfaceAt(var1 + var23[0], var2 + var23[1]);
                           if (var24 != Integer.MIN_VALUE) {
                              SafeSpotFinder.GroundType var25 = this.groundAt(var1 + var23[0], var24, var2 + var23[1]);
                              if (var25 == SafeSpotFinder.GroundType.LAVA || var25 == SafeSpotFinder.GroundType.HAZARD) {
                                 var18++;
                              } else if (var24 == var9 && var25 == var10) {
                                 var17++;
                              }
                           }
                        }

                        double var30 = this.targetX - this.originX;
                        double var31 = this.targetZ - this.originZ;
                        double var32 = Math.hypot(var30, var31);
                        double var26 = var32 > 1.0 && var7 > 0.5 ? 0.35 * Math.min(var7, 10.0) * (var3 * var30 + var5 * var31) / (var32 * var7) : 0.0;
                        double var28 = var11
                           + 5.0 * (var14 + var18)
                           - 6.0 * var17 / outerRingOffsets.length
                           - var26
                           + (var13 != 0 ? 25 : 0)
                           + this.opennessPenalty(var1, var9, var2)
                           + 3.0 * (rayDirections.length - var16);
                        if (this.best == null || var28 < this.best.score()) {
                           this.best = new SafeSpotFinder.SafeSpot(var1, var9, var2, var28, var10 == SafeSpotFinder.GroundType.WATER);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   int countOpenDirections(int var1, int var2, int var3) {
      int var4 = 0;

      for (double[] var8 : rayDirections) {
         byte var9 = 1;

         for (int var10 = 3; var10 <= 24 && var9 != 0; var10++) {
            int var11 = this.surfaceAt((int)Math.floor(var1 + 0.5 + var8[0] * var10), (int)Math.floor(var3 + 0.5 + var8[1] * var10));
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

   private boolean isFlatNeighborhood(int var1, int var2, int var3, SafeSpotFinder.GroundType var4) {
      if (this.surfaceAt(var1, var3) != var2) {
         return false;
      }

      for (int[] var8 : neighborOffsets) {
         if (this.surfaceAt(var1 + var8[0], var3 + var8[1]) != var2 || this.groundAt(var1 + var8[0], var2, var3 + var8[1]) != var4) {
            return false;
         }
      }

      return true;
   }

   private boolean isNearlyFlat(int var1, int var2, int var3) {
      for (int[] var7 : neighborOffsets) {
         int var8 = this.surfaceAt(var1 + var7[0], var3 + var7[1]);
         if (var8 == Integer.MIN_VALUE || Math.abs(var8 - var2) > 1 || this.groundAt(var1 + var7[0], var8, var3 + var7[1]) != SafeSpotFinder.GroundType.SOLID) {
            return false;
         }
      }

      return true;
   }

   private double roughness(int var1, int var2, int var3) {
      double var4 = 0.0;
      int var6 = 0;

      for (int[] var10 : roughnessOffsets) {
         int var11 = this.surfaceAt(var1 + var10[0], var3 + var10[1]);
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

      return var6 * 3 > roughnessOffsets.length ? Double.NaN : 10.0 * var4 / roughnessOffsets.length;
   }

   private double opennessPenalty(int var1, int var2, int var3) {
      double var4 = this.targetX - (var1 + 0.5);
      double var6 = this.targetZ - (var3 + 0.5);
      double var8 = Math.hypot(var4, var6);
      double var10 = 0.0;
      int var12 = 0;

      for (byte var13 = -14; var13 <= 14; var13 += 2) {
         for (byte var14 = -14; var14 <= 14; var14 += 2) {
            int var15 = var13 * var13 + var14 * var14;
            if (var15 >= 36 && var15 <= 196) {
               var12++;
               int var16 = this.surfaceAt(var1 + var13, var3 + var14);
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

   private int surfaceAt(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.surfaceCache.get(var3);
      if (var5 != null) {
         return var5;
      }

      this.queryCount++;
      int var6 = this.terrain.surface(var1, var2);
      this.surfaceCache.put(var3, var6);
      return var6;
   }

   private SafeSpotFinder.GroundType groundAt(int var1, int var2, int var3) {
      long var4 = (long)var1 << 32 ^ var3 & 4294967295L;
      SafeSpotFinder.GroundType var6 = this.groundCache.get(var4);
      if (var6 != null && this.surfaceCache.getOrDefault(var4, -2147483647) == var2) {
         return var6;
      }

      this.queryCount++;
      SafeSpotFinder.GroundType var7 = this.terrain.ground(var1, var2, var3);
      if (this.surfaceCache.getOrDefault(var4, -2147483647) == var2) {
         this.groundCache.put(var4, var7);
      }

      return var7 == null ? SafeSpotFinder.GroundType.INVALID : var7;
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

      roughnessOffsets = var0.toArray(int[][]::new);
      ArrayList var5 = new ArrayList();

      for (int var6 = -2; var6 <= 2; var6++) {
         for (int var7 = -2; var7 <= 2; var7++) {
            if (Math.max(Math.abs(var6), Math.abs(var7)) == 2) {
               var5.add(new int[]{var6, var7});
            }
         }
      }

      outerRingOffsets = var5.toArray(int[][]::new);
      rayDirections = new double[8][];

      for (int var4 = 0; var4 < 8; var4++) {
         rayDirections[var4] = new double[]{Math.cos((Math.PI / 4) * var4), Math.sin((Math.PI / 4) * var4)};
      }
   }

   public enum GroundType {
      SOLID,
      WATER,
      LEAVES,
      LAVA,
      HAZARD,
      INVALID;
   }

   public record SafeSpot(int x, int y, int z, double score, boolean water) {
      private final int blockX;
      private final int blockY;
      private final int blockZ;

      public SafeSpot(int x, int y, int z, double score, boolean water) {
         this.blockX = x;
         this.blockY = y;
         this.blockZ = z;
         this.score = score;
         this.water = water;
      }
   }

   public interface TerrainView {
      int surface(int var1, int var2);

      SafeSpotFinder.GroundType ground(int var1, int var2, int var3);
   }
}

package com.larpclient.util.world;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public final class BuildPlanner {
   public static final String noSpotReason = "no standing spot within reach";
   public static final String keepWalkingReason = "keep walking";
   public static final double eyeHeight = 1.62;
   public static final int maxWalkTargets = 6;
   public static final int maxFallDepth = 8;
   public static final int searchNodeLimit = 3000;
   public static final int maxSupportDistance = 3;
   static final int nearTargetRadius = 3;
   static final int stepSearchBudget = 600;
   public static final int maxStepDepth = 8;
   static final double minRaySlope = 1.2;
   private static final int maxRegionSize = 256;
   private static final double[] sampleOffsets = new double[]{-0.35, 0.0, 0.35};
   static final int maxColumnCandidates = 24;
   static final double rayStep = 0.05;
   public static final int maxColumnHeight = 3;

   private BuildPlanner() {
   }

   public static int adjacencyCost(BuildPlanner.BlockTarget var0, BuildPlanner.BlockTarget var1) {
      int var2 = Math.abs(var1.blockX() - var0.blockX());
      int var3 = Math.abs(var1.blockY() - var0.blockY());
      int var4 = Math.abs(var1.blockZ() - var0.blockZ());
      if (var2 + var3 + var4 == 1) {
         return 0;
      }

      int var5 = Math.max(var2, Math.max(var3, var4));
      return var5 <= 1 ? 1 : var5 + 1;
   }

   public static int lowestLayerY(List<BuildPlanner.BlockTarget> var0, Predicate<BuildPlanner.BlockTarget> var1) {
      int var2 = Integer.MAX_VALUE;

      for (BuildPlanner.BlockTarget var4 : var0) {
         if (var4.blockY() < var2 && var1.test(var4)) {
            var2 = var4.blockY();
         }
      }

      return var2;
   }

   static boolean isCovered(AStarPathfinder.WorldView var0, BuildPlanner.BlockTarget var1) {
      return var0.loaded(var1.blockX(), var1.blockY() + 1, var1.blockZ()) && !var0.open(var1.blockX(), var1.blockY() + 1, var1.blockZ());
   }

   public static double reachDistanceSq(int var0, int var1, int var2, BuildPlanner.BlockTarget var3) {
      double var4 = var3.blockX() - var0;
      double var6 = var3.blockY() + 0.5 - (var1 + 1.62);
      double var8 = var3.blockZ() - var2;
      return var4 * var4 + var6 * var6 + var8 * var8;
   }

   public static List<BuildPlanner.BlockTarget> targetsInReach(List<BuildPlanner.BlockTarget> var0, int var1, int var2, int var3, int var4, double var5) {
      ArrayList var7 = new ArrayList();
      double var8 = var5 * var5;

      for (BuildPlanner.BlockTarget var11 : var0) {
         if (var11.blockY() == var1 && reachDistanceSq(var2, var3, var4, var11) <= var8) {
            var7.add(var11);
         }
      }

      var7.sort(
         Comparator.<BuildPlanner.BlockTarget>comparingDouble(var3x -> -reachDistanceSq(var2, var3, var4, var3x))
            .thenComparingInt(BuildPlanner.BlockTarget::blockX)
            .thenComparingInt(BuildPlanner.BlockTarget::blockZ)
      );
      return var7;
   }

   public static boolean occupiesPlayerSpace(BuildPlanner.BlockTarget var0, int var1, int var2, int var3) {
      return var0.blockX() == var1 && var0.blockZ() == var3 && (var0.blockY() == var2 || var0.blockY() == var2 + 1);
   }

   public static BuildPlanner.PlacementPlan planPlacementDefault(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      LayerScanPlan.Region var8,
      Predicate<BuildPlanner.BlockTarget> var9,
      BuildPlanner.PlaceCheck var10,
      BuildPlanner.PlaceCheck var11,
      int var12
   ) {
      return planPlacementToward(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, true, Set.of(), null);
   }

   public static BuildPlanner.PlacementPlan planPlacementAvoiding(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      LayerScanPlan.Region var8,
      Predicate<BuildPlanner.BlockTarget> var9,
      BuildPlanner.PlaceCheck var10,
      BuildPlanner.PlaceCheck var11,
      int var12,
      boolean var13,
      Set<Long> var14
   ) {
      return planPlacementToward(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, null);
   }

   public static BuildPlanner.PlacementPlan planPlacementToward(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      LayerScanPlan.Region var8,
      Predicate<BuildPlanner.BlockTarget> var9,
      BuildPlanner.PlaceCheck var10,
      BuildPlanner.PlaceCheck var11,
      int var12,
      boolean var13,
      Set<Long> var14,
      BuildPlanner.BlockTarget var15
   ) {
      return planPlacement(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, var15, true);
   }

   public static BuildPlanner.PlacementPlan planPlacement(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      Set<Integer> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      LayerScanPlan.Region var8,
      Predicate<BuildPlanner.BlockTarget> var9,
      BuildPlanner.PlaceCheck var10,
      BuildPlanner.PlaceCheck var11,
      int var12,
      boolean var13,
      Set<Long> var14,
      BuildPlanner.BlockTarget var15,
      boolean var16
   ) {
      int var17 = lowestLayerY(var1, var9);
      if (var17 == Integer.MAX_VALUE) {
         return BuildPlanner.PlacementPlan.none(var1.isEmpty() ? null : "nothing to place the blocks against", var1);
      }

      ArrayList var18 = new ArrayList();
      int var19 = 0;

      for (BuildPlanner.BlockTarget var21 : var1) {
         if (var21.blockY() == var17) {
            var19++;
            if (var9.test(var21) && isCovered(var0, var21)) {
               var18.add(var21);
            }
         }
      }

      if (!var18.isEmpty() && var18.size() < var19) {
         BuildPlanner.PlacementPlan var33 = planPlacement(
            var0, var18, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, var13, var14, var15, var16
         );
         if (var33.place() != null || var16 && (var33.walk() || "keep walking".equals(var33.reason()))) {
            return var33;
         }
      }

      Boolean var34 = null;
      int var35 = 0;
      List var22 = targetsInReach(var1, var17, var3, var4, var5, var6);
      Comparator var23 = Comparator.comparing(var1x -> !isCovered(var0, var1x));
      if (var15 != null) {
         var23 = var23.thenComparingInt(var1x -> adjacencyCost(var15, var1x));
      }

      var22.sort(var23);
      HashSet var24 = new HashSet();

      for (BuildPlanner.BlockTarget var26 : var1) {
         var24.add(AStarPathfinder.packPos(var26.blockX(), var26.blockY(), var26.blockZ()));
      }

      for (BuildPlanner.BlockTarget var38 : var22) {
         if (!occupiesPlayerSpace(var38, var3, var4, var5) && var9.test(var38) && (!var2.contains(var38.stateId()) || !var8.containsColumn(var3, var5))) {
            if (var35++ < var12) {
               if (!var10.canPlaceFrom(var38, var3, var4, var5)) {
                  continue;
               }

               if (var38.blockY() >= var4 && var38.blockY() <= var4 + 2) {
                  if (var34 == null) {
                     var34 = findColumn(var0, var3, var4, var5, var8);
                  }

                  if (var34 && !findColumn(withBlockPlaced(var0, var38), var3, var4, var5, var8)) {
                     var2.add(var38.stateId());
                     continue;
                  }
               }

               BuildPlanner.BlockTarget var43 = findNeighborPlacement(var0, var1, var38, var3, var4, var5, var9, var10, var8, var34);
               if (var43 != null) {
                  return BuildPlanner.PlacementPlan.placing(var43);
               }

               if (var16 && var15 != null && adjacencyCost(var15, var38) > 3) {
                  ArrayList var44 = new ArrayList();

                  for (BuildPlanner.BlockTarget var50 : var1) {
                     if (var50.blockY() == var17 && adjacencyCost(var15, var50) <= 3 && var9.test(var50)) {
                        var44.add(var50);
                     }
                  }

                  if (!var44.isEmpty()) {
                     if (!var13) {
                        return BuildPlanner.PlacementPlan.none("keep walking", List.of());
                     }

                     var44.sort(
                        Comparator.<BuildPlanner.BlockTarget>comparingInt(var1x -> adjacencyCost(var15, var1x))
                           .thenComparingDouble(var3x -> reachDistanceSq(var3, var4, var5, var3x))
                     );
                     List var47 = var44.subList(0, Math.min(6, var44.size()));
                     HashSet var51 = new HashSet();

                     for (BuildPlanner.BlockTarget var55 : var47) {
                        var51.addAll(findStandingSpots(var0, var55, var6, var8, var2.contains(var55.stateId()), var11, var24));
                     }

                     var51.remove(AStarPathfinder.packPos(var3, var4, var5));
                     var51.removeAll(var14);
                     if (!var51.isEmpty()) {
                        return BuildPlanner.PlacementPlan.walking(var51, (BuildPlanner.BlockTarget)var47.getFirst(), new ArrayList<>(var47));
                     }
                  }
               }

               return BuildPlanner.PlacementPlan.placing(var38);
            }
            break;
         }
      }

      if (!var13) {
         return BuildPlanner.PlacementPlan.none("keep walking", List.of());
      }

      ArrayList var37 = new ArrayList();

      for (BuildPlanner.BlockTarget var27 : var1) {
         if (var27.blockY() == var17 && var9.test(var27)) {
            var37.add(var27);
         }
      }

      Comparator var40 = Comparator.comparing(var1x -> !isCovered(var0, var1x));
      if (var15 != null) {
         var40 = var40.thenComparingInt(var1x -> adjacencyCost(var15, var1x));
      }

      var37.sort(var40.thenComparingDouble(var3x -> reachDistanceSq(var3, var4, var5, var3x)));
      List var41 = var37.subList(0, Math.min(6, var37.size()));
      HashSet var28 = new HashSet();

      for (BuildPlanner.BlockTarget var30 : var41) {
         var28.addAll(findStandingSpots(var0, var30, var6, var8, var2.contains(var30.stateId()), var11, var24));
      }

      var28.remove(AStarPathfinder.packPos(var3, var4, var5));
      var28.removeAll(var14);
      List var45 = var41;

      for (byte var48 = 6; var28.isEmpty() && var16 && var48 < Math.min(var37.size(), 48); var48 += 6) {
         var41 = var37.subList(var48, Math.min(var48 + 6, var37.size()));

         for (BuildPlanner.BlockTarget var32 : var41) {
            var28.addAll(findStandingSpots(var0, var32, var6, var8, var2.contains(var32.stateId()), var11, var24));
         }

         var28.remove(AStarPathfinder.packPos(var3, var4, var5));
         var28.removeAll(var14);
      }

      if (var28.isEmpty()) {
         var41 = var45;
         byte var49 = 1;

         for (BuildPlanner.BlockTarget var54 : var41) {
            if (!var2.contains(var54.stateId())) {
               var49 = 0;
            }
         }

         return BuildPlanner.PlacementPlan.none(
            var49 != 0 ? "it would wall you in, and no spot outside reaches it" : "no standing spot within reach", new ArrayList<>(var41)
         );
      } else {
         return BuildPlanner.PlacementPlan.walking(var28, (BuildPlanner.BlockTarget)var41.getFirst(), new ArrayList<>(var41));
      }
   }

   static BuildPlanner.BlockTarget findNeighborPlacement(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      BuildPlanner.BlockTarget var2,
      int var3,
      int var4,
      int var5,
      Predicate<BuildPlanner.BlockTarget> var6,
      BuildPlanner.PlaceCheck var7,
      LayerScanPlan.Region var8,
      Boolean var9
   ) {
      int[][] var10 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

      for (int[] var14 : var10) {
         int var15 = var2.blockX() + var14[0];
         int var16 = var2.blockZ() + var14[1];
         BuildPlanner.BlockTarget var17 = null;

         for (BuildPlanner.BlockTarget var19 : var1) {
            if (var19.blockX() == var15 && var19.blockY() == var2.blockY() && var19.blockZ() == var16) {
               var17 = var19;
               break;
            }
         }

         if (var17 != null && !occupiesPlayerSpace(var17, var3, var4, var5) && var6.test(var17)) {
            int var25 = 1;
            if (var0.loaded(var15, var2.blockY() + 1, var16) && !var0.open(var15, var2.blockY() + 1, var16)) {
               var25++;
            }

            for (int[] var22 : var10) {
               int var23 = var15 + var22[0];
               int var24 = var16 + var22[1];
               if ((var23 != var2.blockX() || var24 != var2.blockZ()) && var0.loaded(var23, var2.blockY(), var24) && !var0.open(var23, var2.blockY(), var24)) {
                  var25++;
               }
            }

            if (var25 >= 2
               && var7.canPlaceFrom(var17, var3, var4, var5)
               && (
                  var17.blockY() < var4
                     || var17.blockY() > var4 + 2
                     || (var9 == null ? !findColumn(var0, var3, var4, var5, var8) : !var9)
                     || findColumn(withBlockPlaced(var0, var17), var3, var4, var5, var8)
               )) {
               return var17;
            }
         }
      }

      return null;
   }

   public static Set<Long> findStandingSpots(
      AStarPathfinder.WorldView var0,
      BuildPlanner.BlockTarget var1,
      double var2,
      LayerScanPlan.Region var4,
      boolean var5,
      BuildPlanner.PlaceCheck var6,
      Set<Long> var7
   ) {
      HashSet var8 = new HashSet();
      int var9 = (int)Math.ceil(var2);
      double var10 = (var2 - 0.3) * (var2 - 0.3);

      for (int var12 = -var9 - 1; var12 <= 1; var12++) {
         for (int var13 = -var9; var13 <= var9; var13++) {
            for (int var14 = -var9; var14 <= var9; var14++) {
               int var15 = var1.blockX() + var13;
               int var16 = var1.blockY() + var12;
               int var17 = var1.blockZ() + var14;
               if (!occupiesPlayerSpace(var1, var15, var16, var17)
                  && !(reachDistanceSq(var15, var16, var17, var1) > var10)
                  && (!var5 || !var4.containsColumn(var15, var17))
                  && var0.loaded(var15, var16, var17)
                  && var0.floor(var15, var16 - 1, var17)
                  && var0.open(var15, var16, var17)
                  && var0.open(var15, var16 + 1, var17)
                  && !var7.contains(AStarPathfinder.packPos(var15, var16, var17))
                  && !var7.contains(AStarPathfinder.packPos(var15, var16 + 1, var17))
                  && var6.canPlaceFrom(var1, var15, var16, var17)) {
                  var8.add(AStarPathfinder.packPos(var15, var16, var17));
               }
            }
         }
      }

      return var8.isEmpty() && !var7.isEmpty() && var1.blockY() < var4.maxY() ? findStandingSpots(var0, var1, var2, var4, var5, var6, Set.of()) : var8;
   }

   public static BuildPlanner.Pillar findPillar(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      int var2,
      int var3,
      int var4,
      double var5,
      int var7,
      BuildPlanner.CellFilter var8,
      BuildPlanner.PlaceCheck var9,
      Set<Long> var10,
      int var11
   ) {
      return findPillarRelaxed(var0, var1, var1, var2, var3, var4, var5, var7, var8, var9, var10, var11);
   }

   public static BuildPlanner.Pillar findPillarRelaxed(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      List<BuildPlanner.BlockTarget> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      int var8,
      BuildPlanner.CellFilter var9,
      BuildPlanner.PlaceCheck var10,
      Set<Long> var11,
      int var12
   ) {
      BuildPlanner.Pillar var13 = findPillar(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, false, var4 + 1);
      return var13 != null ? var13 : findPillar(var0, var1, var2, var3, var4, var5, var6, var8, var9, var10, var11, var12, true, var4 + 1);
   }

   private static BuildPlanner.Pillar findPillar(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      List<BuildPlanner.BlockTarget> var2,
      int var3,
      int var4,
      int var5,
      double var6,
      int var8,
      BuildPlanner.CellFilter var9,
      BuildPlanner.PlaceCheck var10,
      Set<Long> var11,
      int var12,
      boolean var13,
      int var14
   ) {
      BuildPlanner.Pillar var15 = null;
      double var16 = -1.0;
      int var18 = Integer.MAX_VALUE;
      int var19 = 0;
      HashSet var20 = new HashSet();
      int var21 = (int)Math.floor(var6);

      for (BuildPlanner.BlockTarget var23 : var1) {
         for (int var24 = -var21; var24 <= var21; var24++) {
            for (int var25 = -var21; var25 <= var21; var25++) {
               int var26 = var23.blockX() + var24;
               int var27 = var23.blockZ() + var25;
               long var28 = AStarPathfinder.packPos(var26, 0, var27);
               if (!(var24 * var24 + var25 * var25 > var6 * var6) && !var11.contains(var28) && var20.add(var28)) {
                  int var30 = Integer.MIN_VALUE;

                  for (int var31 = Math.min(var23.blockY(), var14); var31 >= var23.blockY() - var8 - 3; var31--) {
                     if (var0.loaded(var26, var31, var27)
                        && var0.floor(var26, var31 - 1, var27)
                        && var0.open(var26, var31, var27)
                        && var0.open(var26, var31 + 1, var27)) {
                        var30 = var31;
                        break;
                     }
                  }

                  if (var30 != Integer.MIN_VALUE && !var9.isBlocked(var26, var30, var27) && !var9.isBlocked(var26, var30 + 1, var27)) {
                     int var42 = Math.max(1, var23.blockY() - var30 - 2);
                     int var32 = Math.min(var8, var23.blockY() - var30 + 1);

                     for (int var33 = var42;
                        var33 <= var32 && isShaftOpen(var0, var26, var30, var27, var33, var9, var13 ? var23.blockY() : Integer.MAX_VALUE);
                        var33++
                     ) {
                        if (var19++ >= var12) {
                           return var15;
                        }

                        if (var10.canPlaceFrom(var23, var26, var30 + var33, var27)) {
                           ArrayList var34 = new ArrayList();

                           for (BuildPlanner.BlockTarget var36 : var1) {
                              if (var36 == var23 || var10.canPlaceFrom(var36, var26, var30 + var33, var27)) {
                                 var34.add(var36);
                              }
                           }

                           var19 += var1.size() - 1;
                           int var43 = 0;
                           double var44 = var6 * var6;

                           for (BuildPlanner.BlockTarget var39 : var2) {
                              if (reachDistanceSq(var26, var30 + var33, var27, var39) <= var44) {
                                 var43++;
                              }
                           }

                           double var45 = (double)(var34.size() * 2 + var43) / (var33 + 3);
                           int var40 = Math.abs(var26 - var3) + Math.abs(var27 - var5) + Math.abs(var30 - var4);
                           int var41 = Math.abs(var45 - var16) < 1.0E-9 ? 1 : 0;
                           if (var45 > var16 && var41 == 0 || var41 != 0 && (var33 < var15.height() || var33 == var15.height() && var40 < var18)) {
                              var15 = new BuildPlanner.Pillar(var26, var30, var27, var33, var34);
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

   public static int extendPillarHeight(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      BuildPlanner.Pillar var2,
      int var3,
      int var4,
      BuildPlanner.CellFilter var5,
      BuildPlanner.PlaceCheck var6
   ) {
      int var7 = Math.min(var3, var2.height() + Math.min(2, var4));

      for (int var8 = var2.height() + 1; var8 <= var7; var8++) {
         for (int var9 = var2.topY(); var9 <= var2.ground() + var8 + 1; var9++) {
            if (!var0.loaded(var2.pillarX(), var9, var2.pillarZ())
               || !var0.open(var2.pillarX(), var9, var2.pillarZ())
               || var5.isBlocked(var2.pillarX(), var9, var2.pillarZ())) {
               return 0;
            }
         }

         for (BuildPlanner.BlockTarget var10 : var1) {
            if (var6.canPlaceFrom(var10, var2.pillarX(), var2.ground() + var8, var2.pillarZ())) {
               return var8;
            }
         }
      }

      return 0;
   }

   public static int[] findStepDirection(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      int var2,
      int var3,
      int var4,
      int var5,
      double var6,
      BuildPlanner.CellFilter var8,
      BuildPlanner.PlaceCheck var9
   ) {
      return withBlockPlaced(var0, var1, var2, var3, var4, var5, var6, var8, var9, Set.of());
   }

   public static int[] withBlockPlaced(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      int var2,
      int var3,
      int var4,
      int var5,
      double var6,
      BuildPlanner.CellFilter var8,
      BuildPlanner.PlaceCheck var9,
      Set<Long> var10
   ) {
      int[][] var11 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      HashSet var12 = new HashSet(var10);
      var12.add(AStarPathfinder.packPos(var2, 0, var4));
      ArrayList var13 = new ArrayList();
      var13.add(new int[]{var2, var4, 0, 0});
      int var14 = 0;

      for (int var15 = 1; var15 <= var5; var15++) {
         ArrayList var16 = new ArrayList();

         for (int[] var18 : var13) {
            for (int[] var22 : var11) {
               int var23 = var18[0] + var22[0];
               int var24 = var18[1] + var22[1];
               if (var12.add(AStarPathfinder.packPos(var23, 0, var24)) && canStandAt(var0, var23, var3, var24, var8)) {
                  var16.add(new int[]{var23, var24, var15 == 1 ? var22[0] : var18[2], var15 == 1 ? var22[1] : var18[3]});
               }
            }
         }

         for (int[] var26 : var16) {
            for (BuildPlanner.BlockTarget var28 : var1) {
               if (!(reachDistanceSq(var26[0], var3, var26[1], var28) > var6 * var6)) {
                  if (var14++ >= 600) {
                     return null;
                  }

                  if (var9.canPlaceFrom(var28, var26[0], var3, var26[1])) {
                     return new int[]{var26[2], var26[3]};
                  }
               }
            }
         }

         var13 = var16;
      }

      return null;
   }

   static boolean canStandAt(AStarPathfinder.WorldView var0, int var1, int var2, int var3, BuildPlanner.CellFilter var4) {
      for (int var5 = var2; var5 <= var2 + 1; var5++) {
         if (!var0.loaded(var1, var5, var3) || !var0.open(var1, var5, var3) || var4.isBlocked(var1, var5, var3)) {
            return false;
         }
      }

      return !var0.loaded(var1, var2 - 1, var3)
         ? false
         : var0.floor(var1, var2 - 1, var3) || var0.open(var1, var2 - 1, var3) && !var4.isBlocked(var1, var2 - 1, var3);
   }

   public static BuildPlanner.Pillar findPillarFromCells(
      AStarPathfinder.WorldView var0, Set<Long> var1, List<BuildPlanner.BlockTarget> var2, int var3, BuildPlanner.CellFilter var4, Set<Long> var5
   ) {
      int[][] var6 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      BuildPlanner.Pillar var7 = null;
      int var8 = Integer.MAX_VALUE;

      for (long var10 : var1) {
         int var12 = AStarPathfinder.unpackY(var10);
         HashSet var13 = new HashSet<>(Set.of(AStarPathfinder.packPos(AStarPathfinder.unpackX(var10), 0, AStarPathfinder.unpackZ(var10))));
         ArrayList var14 = new ArrayList();
         var14.add(new int[]{AStarPathfinder.unpackX(var10), AStarPathfinder.unpackZ(var10)});

         for (int var15 = 0; var15 <= 8 && !var14.isEmpty(); var15++) {
            ArrayList var16 = new ArrayList();

            for (int[] var18 : var14) {
               for (int[] var22 : var6) {
                  int var23 = var18[0] + var22[0];
                  int var24 = var18[1] + var22[1];
                  if (var13.add(AStarPathfinder.packPos(var23, 0, var24))) {
                     if (var0.loaded(var23, var12 - 1, var24)
                        && var0.floor(var23, var12 - 1, var24)
                        && var0.loaded(var23, var12, var24)
                        && var0.open(var23, var12, var24)
                        && var0.open(var23, var12 + 1, var24)) {
                        var16.add(new int[]{var23, var24});
                     } else if (!var5.contains(AStarPathfinder.packPos(var23, 0, var24))) {
                        int var25 = findLandingY(var0, var23, var12, var24, var3, var4);
                        if (var25 != Integer.MIN_VALUE) {
                           int var26 = (var12 - var25) * 4 + var15;
                           if (var26 < var8) {
                              var8 = var26;
                              var7 = new BuildPlanner.Pillar(var23, var25, var24, var12 - var25, var2);
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

   public static BuildPlanner.Pillar pillarAfterBreakout(
      AStarPathfinder.WorldView var0, BuildPlanner.Breakout var1, int var2, BuildPlanner.CellFilter var3, List<BuildPlanner.BlockTarget> var4
   ) {
      int var5 = findLandingY(var0, var1.outX(), var1.outY(), var1.outZ(), var2, var3);
      return var5 == Integer.MIN_VALUE ? null : new BuildPlanner.Pillar(var1.outX(), var5, var1.outZ(), var1.outY() - var5, var4);
   }

   private static int findLandingY(AStarPathfinder.WorldView var0, int var1, int var2, int var3, int var4, BuildPlanner.CellFilter var5) {
      for (int var6 = var2 + 1; var6 >= var2 - var4; var6--) {
         if (!var0.loaded(var1, var6, var3) || !var0.open(var1, var6, var3) || var5.isBlocked(var1, var6, var3)) {
            return Integer.MIN_VALUE;
         }

         if (var6 < var2 && var0.floor(var1, var6 - 1, var3)) {
            return var6;
         }
      }

      return Integer.MIN_VALUE;
   }

   public static List<BuildPlanner.Breakout> planPlacement(
      BuildPlanner.CellFilter var0, BuildPlanner.CellFilter var1, List<BuildPlanner.BlockTarget> var2, LayerScanPlan.Region var3, double var4
   ) {
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList(var2);

      while (!var7.isEmpty()) {
         BuildPlanner.BlockTarget var8 = (BuildPlanner.BlockTarget)var7.removeFirst();
         Set var9 = findLineOfSightCells(var0, var8, var4);
         Set var10 = var9.isEmpty() ? null : floodEnclosedRegion(var0, var9, var3);
         if (var10 != null) {
            ArrayList var11 = new ArrayList<>(List.of(var8));
            Iterator var12 = var7.iterator();

            while (var12.hasNext()) {
               BuildPlanner.BlockTarget var13 = (BuildPlanner.BlockTarget)var12.next();
               if (findLineOfSightCells(var0, var13, var4).stream().anyMatch(var10::contains)) {
                  var11.add(var13);
                  var12.remove();
               }
            }

            BuildPlanner.Breakout var14 = findBreakout(var0, var1, var10, var11);
            if (var14 != null) {
               var6.add(var14);
            }
         }
      }

      return var6;
   }

   static Set<Long> findLineOfSightCells(BuildPlanner.CellFilter var0, BuildPlanner.BlockTarget var1, double var2) {
      HashSet var4 = new HashSet();
      if (var0.isBlocked(var1.blockX(), var1.blockY() + 1, var1.blockZ())) {
         return var4;
      }

      int var5 = (int)Math.ceil(var2);

      for (int var6 = var1.blockY() - var5 - 1; var6 < var1.blockY(); var6++) {
         for (int var7 = var1.blockX() - var5; var7 <= var1.blockX() + var5; var7++) {
            for (int var8 = var1.blockZ() - var5; var8 <= var1.blockZ() + var5; var8++) {
               if (!var0.isBlocked(var7, var6 - 1, var8)
                  && var0.isBlocked(var7, var6, var8)
                  && var0.isBlocked(var7, var6 + 1, var8)
                  && reachDistanceSq(var0, var1, var7, var6, var8, var2)) {
                  var4.add(AStarPathfinder.packPos(var7, var6, var8));
               }
            }
         }
      }

      return var4;
   }

   private static boolean reachDistanceSq(BuildPlanner.CellFilter var0, BuildPlanner.BlockTarget var1, int var2, int var3, int var4, double var5) {
      double var7 = var2 + 0.5;
      double var9 = var3 + 1.62;
      double var11 = var4 + 0.5;
      double var13 = var1.blockY() + 1 - 0.001;

      for (double var18 : sampleOffsets) {
         for (double var23 : sampleOffsets) {
            double var25 = var1.blockX() + 0.5 + var18 - var7;
            double var27 = var13 - var9;
            double var29 = var1.blockZ() + 0.5 + var23 - var11;
            double var31 = Math.sqrt(var25 * var25 + var27 * var27 + var29 * var29);
            if (!(var31 > var5) && !(var27 < Math.max(Math.abs(var25), Math.abs(var29)) * 1.2)) {
               byte var33 = 1;

               for (double var34 = 0.0; var34 < 1.0 && var33 != 0; var34 += 0.04 / var31) {
                  int var36 = (int)Math.floor(var7 + var25 * var34);
                  int var37 = (int)Math.floor(var9 + var27 * var34);
                  int var38 = (int)Math.floor(var11 + var29 * var34);
                  if ((var36 != var1.blockX() || var37 != var1.blockY() || var38 != var1.blockZ()) && !var0.isBlocked(var36, var37, var38)) {
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

   private static Set<Long> floodEnclosedRegion(BuildPlanner.CellFilter var0, Set<Long> var1, LayerScanPlan.Region var2) {
      int[][] var3 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      HashSet var4 = new HashSet(var1);
      ArrayList var5 = new ArrayList(var1);

      while (!var5.isEmpty()) {
         long var6 = (Long)var5.removeLast();
         int var8 = AStarPathfinder.unpackX(var6);
         int var9 = AStarPathfinder.unpackY(var6);
         int var10 = AStarPathfinder.unpackZ(var6);
         if (!var2.containsColumn(var8, var10) || var4.size() > 256) {
            return null;
         }

         for (int[] var14 : var3) {
            int var15 = var8 + var14[0];
            int var16 = var10 + var14[1];
            int var17;
            if (var0.isBlocked(var15, var9, var16) && var0.isBlocked(var15, var9 + 1, var16)) {
               if (!var0.isBlocked(var15, var9 - 1, var16)) {
                  var17 = var9;
               } else {
                  if (var0.isBlocked(var15, var9 - 2, var16)) {
                     return null;
                  }

                  var17 = var9 - 1;
               }
            } else {
               if (var0.isBlocked(var15, var9, var16)
                  || !var0.isBlocked(var15, var9 + 1, var16)
                  || !var0.isBlocked(var15, var9 + 2, var16)
                  || !var0.isBlocked(var8, var9 + 2, var10)) {
                  continue;
               }

               var17 = var9 + 1;
            }

            if (var4.add(AStarPathfinder.packPos(var15, var17, var16))) {
               var5.add(AStarPathfinder.packPos(var15, var17, var16));
            }
         }
      }

      return var4;
   }

   private static BuildPlanner.Breakout findBreakout(
      BuildPlanner.CellFilter var0, BuildPlanner.CellFilter var1, Set<Long> var2, List<BuildPlanner.BlockTarget> var3
   ) {
      int[][] var4 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
      BuildPlanner.Breakout var5 = null;
      int var6 = Integer.MAX_VALUE;

      for (long var8 : var2) {
         int var10 = AStarPathfinder.unpackX(var8);
         int var11 = AStarPathfinder.unpackY(var8);
         int var12 = AStarPathfinder.unpackZ(var8);

         for (int[] var16 : var4) {
            int var17 = var10 + var16[0];
            int var18 = var12 + var16[1];
            int var19 = var17 + var16[0];
            int var20 = var18 + var16[1];
            if (!var0.isBlocked(var17, var11 - 1, var18)) {
               boolean var21 = var0.isBlocked(var17, var11, var18);
               boolean var22 = var0.isBlocked(var17, var11 + 1, var18);
               if ((!var21 || !var22)
                  && (var21 || var1.isBlocked(var17, var11, var18))
                  && (var22 || var1.isBlocked(var17, var11 + 1, var18))
                  && var0.isBlocked(var19, var11, var20)
                  && var0.isBlocked(var19, var11 + 1, var20)) {
                  int var23 = 0;

                  while (var23 < 4 && var0.isBlocked(var19, var11 + 2 + var23, var20)) {
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

                     var5 = new BuildPlanner.Breakout(var25, var19, var11, var20, var3, var2);
                  }
               }
            }
         }
      }

      return var5;
   }

   public static BuildPlanner.Column findLandingY(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      BuildPlanner.CellFilter var2,
      BuildPlanner.CellFilter var3,
      Predicate<BuildPlanner.BlockTarget> var4,
      Set<Long> var5,
      int var6,
      int var7,
      int var8
   ) {
      return findColumn(var0, var1, var2, var3, var4, var5, var6, var7, var8, var0x -> true);
   }

   public static BuildPlanner.Column findColumn(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      BuildPlanner.CellFilter var2,
      BuildPlanner.CellFilter var3,
      Predicate<BuildPlanner.BlockTarget> var4,
      Set<Long> var5,
      int var6,
      int var7,
      int var8,
      Predicate<BuildPlanner.Column> var9
   ) {
      int[][] var10 = new int[][]{{0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
      ArrayList var11 = new ArrayList();
      HashMap var12 = new HashMap();

      for (BuildPlanner.BlockTarget var14 : var1) {
         if (!var5.contains(AStarPathfinder.packPos(var14.blockX(), var14.blockY(), var14.blockZ())) && var4.test(var14)) {
            for (int var15 = 0; var15 < var10.length; var15++) {
               int var16 = var14.blockX() + var10[var15][0];
               int var17 = var14.blockY() + var10[var15][1];
               int var18 = var14.blockZ() + var10[var15][2];
               int var19 = measureColumnHeight(var0, var16, var17, var18, var2, var3);
               if (var19 != 0) {
                  BuildPlanner.Column var20 = new BuildPlanner.Column(var14, var16, var17 - var19 + 1, var18, var19);
                  var11.add(var20);
                  var12.put(var20, var19 * 1000 + (var15 == 0 ? 0 : 500) + Math.abs(var16 - var6) + Math.abs(var17 - var7) + Math.abs(var18 - var8));
               }
            }
         }
      }

      var11.sort(Comparator.comparingInt(var12::get));
      int var21 = 0;

      for (BuildPlanner.Column var23 : var11) {
         if (var21++ >= 24) {
            return null;
         }

         if (var9.test(var23)) {
            return var23;
         }
      }

      return null;
   }

   public static boolean canPlaceFromColumn(BuildPlanner.Column var0, int var1, int var2, int var3) {
      BuildPlanner.BlockTarget var4 = var0.seed();
      if (var1 == var0.columnX() && var3 == var0.columnZ()) {
         return false;
      }

      int var5 = var0.columnX() == var4.blockX() && var0.columnZ() == var4.blockZ() ? 1 : 0;
      return var5 == 0 && var0.height() == 1 || var2 + 1.62 > var4.blockY() + 0.05;
   }

   public static BuildPlanner.Pillar findPillarForColumns(
      AStarPathfinder.WorldView var0,
      List<BuildPlanner.BlockTarget> var1,
      int var2,
      int var3,
      int var4,
      double var5,
      int var7,
      BuildPlanner.CellFilter var8,
      BuildPlanner.CellFilter var9,
      Predicate<BuildPlanner.BlockTarget> var10,
      BuildPlanner.ColumnFilter var11,
      Set<Long> var12,
      Set<Long> var13,
      int var14
   ) {
      ArrayList var15 = new ArrayList();
      HashMap var16 = new HashMap();

      for (BuildPlanner.BlockTarget var18 : var1) {
         if (!var12.contains(AStarPathfinder.packPos(var18.blockX(), var18.blockY(), var18.blockZ())) && var10.test(var18)) {
            int var19 = measureColumnHeight(var0, var18.blockX(), var18.blockY() - 1, var18.blockZ(), var8, var9);
            if (var19 != 0) {
               var15.add(var18);
               var16.put(var18, new BuildPlanner.Column(var18, var18.blockX(), var18.blockY() - var19, var18.blockZ(), var19));
            }
         }
      }

      return findPillarRelaxed(
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
            && canPlaceFromColumn((BuildPlanner.Column)var16.get(var2x), var3x, var4x, var5x)
            && var11.accepts((BuildPlanner.Column)var16.get(var2x), var3x, var4x, var5x),
         var13,
         var14
      );
   }

   static int measureColumnHeight(AStarPathfinder.WorldView var0, int var1, int var2, int var3, BuildPlanner.CellFilter var4, BuildPlanner.CellFilter var5) {
      int var6 = 1;

      while (var6 <= 3) {
         int var7 = var2 - var6 + 1;
         if (var0.loaded(var1, var7, var3) && var0.open(var1, var7, var3) && !var4.isBlocked(var1, var7, var3)) {
            if (!var0.floor(var1, var7 - 1, var3) && !var5.isBlocked(var1, var7, var3)) {
               var6++;
               continue;
            }

            return var6;
         }

         return 0;
      }

      return 0;
   }

   static boolean isShaftClear(AStarPathfinder.WorldView var0, int var1, int var2, int var3, int var4, BuildPlanner.CellFilter var5) {
      return isShaftOpen(var0, var1, var2, var3, var4, var5, Integer.MAX_VALUE);
   }

   static boolean isShaftOpen(AStarPathfinder.WorldView var0, int var1, int var2, int var3, int var4, BuildPlanner.CellFilter var5, int var6) {
      for (int var7 = var2; var7 <= var2 + var4 + 1; var7++) {
         if (!var0.loaded(var1, var7, var3) || !var0.open(var1, var7, var3) || (var7 < var2 + var4 || var7 <= var6) && var5.isBlocked(var1, var7, var3)) {
            return false;
         }
      }

      return true;
   }

   public static boolean findColumn(AStarPathfinder.WorldView var0, int var1, int var2, int var3, final LayerScanPlan.Region var4) {
      if (!var4.containsColumn(var1, var3)) {
         return true;
      }

      AStarPathfinder.Goal var5 = new AStarPathfinder.Goal() {
         @Override
         public boolean reached(int var1, int var2x, int var3x) {
            return !var4.containsColumn(var1, var3x);
         }

         @Override
         public double estimate(int var1, int var2x, int var3x) {
            return Math.max(0, Math.min(Math.min(var1 - var4.minX() + 1, var4.maxX() + 1 - var1), Math.min(var3x - var4.minZ() + 1, var4.maxZ() + 1 - var3x)));
         }
      };
      AStarPathfinder.Bounds var6 = new AStarPathfinder.Bounds(
         var4.minX() - 3, Math.min(var2, var4.minY()) - 4, var4.minZ() - 3, var4.maxX() + 3, Math.max(var2, var4.maxY()) + 4, var4.maxZ() + 3
      );
      return AStarPathfinder.findPath(var0, var1, var2, var3, var5, var6, AStarPathfinder.Options.walking(3000)).found();
   }

   public static AStarPathfinder.WorldView withBlockPlaced(final AStarPathfinder.WorldView var0, BuildPlanner.BlockTarget var1) {
      final long var2 = AStarPathfinder.packPos(var1.blockX(), var1.blockY(), var1.blockZ());
      return new AStarPathfinder.WorldView() {
         @Override
         public boolean loaded(int var1, int var2x, int var3) {
            return var0.loaded(var1, var2x, var3);
         }

         @Override
         public boolean open(int var1, int var2x, int var3) {
            return AStarPathfinder.packPos(var1, var2x, var3) != var2 && var0.open(var1, var2x, var3);
         }

         @Override
         public boolean floor(int var1, int var2x, int var3) {
            return AStarPathfinder.packPos(var1, var2x, var3) == var2 || var0.floor(var1, var2x, var3);
         }

         @Override
         public double digCost(int var1, int var2x, int var3) {
            return AStarPathfinder.packPos(var1, var2x, var3) == var2 ? -1.0 : var0.digCost(var1, var2x, var3);
         }

         @Override
         public double penalty(int var1, int var2x, int var3) {
            return var0.penalty(var1, var2x, var3);
         }

         @Override
         public boolean swim(int var1, int var2x, int var3) {
            return AStarPathfinder.packPos(var1, var2x, var3) != var2 && var0.swim(var1, var2x, var3);
         }
      };
   }

   public record BlockTarget(int x, int y, int z, int id) {
      private final int blockX;
      private final int blockY;
      private final int blockZ;
      private final int stateId;

      public BlockTarget(int x, int y, int z, int id) {
         this.blockX = x;
         this.blockY = y;
         this.blockZ = z;
         this.stateId = id;
      }
   }

   public record Breakout(List<int[]> cells, int outX, int outY, int outZ, List<BuildPlanner.BlockTarget> aims, Set<Long> space) {
   }

   public interface CellFilter {
      boolean isBlocked(int var1, int var2, int var3);
   }

   public record Column(BuildPlanner.BlockTarget seed, int x, int ground, int z, int height) {
      private final int columnX;
      private final int columnZ;

      public Column(BuildPlanner.BlockTarget seed, int x, int ground, int z, int height) {
         this.seed = seed;
         this.columnX = x;
         this.ground = ground;
         this.columnZ = z;
         this.height = height;
      }

      public int top() {
         return this.ground + this.height - 1;
      }
   }

   public interface ColumnFilter {
      boolean accepts(BuildPlanner.Column var1, int var2, int var3, int var4);
   }

   public record Pillar(int x, int ground, int z, int height, List<BuildPlanner.BlockTarget> unlocks) {
      private final int pillarX;
      private final int pillarZ;

      public Pillar(int x, int ground, int z, int height, List<BuildPlanner.BlockTarget> unlocks) {
         this.pillarX = x;
         this.ground = ground;
         this.pillarZ = z;
         this.height = height;
         this.unlocks = unlocks;
      }

      public int topY() {
         return this.ground + this.height;
      }
   }

   public interface PlaceCheck {
      boolean canPlaceFrom(BuildPlanner.BlockTarget var1, int var2, int var3, int var4);
   }

   public record PlacementPlan(
      BuildPlanner.BlockTarget place, Set<Long> stands, BuildPlanner.BlockTarget toward, List<BuildPlanner.BlockTarget> walkingFor, String reason
   ) {
      static BuildPlanner.PlacementPlan placing(BuildPlanner.BlockTarget var0) {
         return new BuildPlanner.PlacementPlan(var0, Set.of(), null, List.of(), null);
      }

      static BuildPlanner.PlacementPlan walking(Set<Long> var0, BuildPlanner.BlockTarget var1, List<BuildPlanner.BlockTarget> var2) {
         return new BuildPlanner.PlacementPlan(null, var0, var1, var2, null);
      }

      static BuildPlanner.PlacementPlan none(String var0, List<BuildPlanner.BlockTarget> var1) {
         return new BuildPlanner.PlacementPlan(null, Set.of(), null, var1, var0);
      }

      public boolean walk() {
         return !this.stands.isEmpty();
      }
   }

   public static final class SkippedStands {
      private static final long nO_POS = Long.MIN_VALUE;
      private final Set<Long> skipped = new HashSet<>();
      private long pending = Long.MIN_VALUE;

      public void setPending(int var1, int var2, int var3) {
         this.pending = AStarPathfinder.packPos(var1, var2, var3);
      }

      public void clear() {
         this.skipped.clear();
         this.pending = Long.MIN_VALUE;
      }

      public boolean commitPending() {
         if (this.pending == Long.MIN_VALUE) {
            return false;
         }

         this.skipped.add(this.pending);
         this.pending = Long.MIN_VALUE;
         return true;
      }

      public void add(int var1, int var2, int var3) {
         this.skipped.add(AStarPathfinder.packPos(var1, var2, var3));
      }

      public Set<Long> getSkipped() {
         return this.skipped;
      }

      public void reset() {
         this.skipped.clear();
         this.pending = Long.MIN_VALUE;
      }
   }
}

package com.larpclient.util.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public final class AStarPathfinder {
   static final double jUMP_UP_COST_BASE = 2.0;
   static final double dIAGONAL_COST = Math.sqrt(2.0);
   static final double vERTICAL_STEP_COST = 1.5;
   static final double vERTICAL_HEURISTIC_WEIGHT = 0.5;
   private static final int[][] cARDINAL_OFFSETS = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] dIAGONAL_OFFSETS = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static AStarPathfinder.PathResult findPath(
      AStarPathfinder.WorldView var0, int var1, int var2, int var3, AStarPathfinder.Goal var4, AStarPathfinder.Bounds var5, AStarPathfinder.Options var6
   ) {
      if (var4.reached(var1, var2, var3)) {
         return new AStarPathfinder.PathResult(List.of(), true, 0, 0.0);
      }

      HashMap var7 = new HashMap();
      PriorityQueue var8 = new PriorityQueue();
      AStarPathfinder.Node var9 = new AStarPathfinder.Node(packPos(var1, var2, var3), var1, var2, var3);
      var9.heuristic = var4.estimate(var1, var2, var3);
      var9.totalEstimate = var9.heuristic;
      var7.put(var9.key, var9);
      var8.add(var9);
      AStarPathfinder.Node var10 = var9;
      int var11 = 0;
      AStarPathfinder.NeighborExpander var12 = new AStarPathfinder.NeighborExpander(var0, var4, var5, var6, var7, var8);

      while (!var8.isEmpty()) {
         AStarPathfinder.Node var13 = (AStarPathfinder.Node)var8.poll();
         if (!var13.closed) {
            var13.closed = (boolean)1;
            if (var4.reached(var13.blockX, var13.blockY, var13.blockZ)) {
               return new AStarPathfinder.PathResult(buildSteps(var13), true, var11, var13.costSoFar);
            }

            if (var13.heuristic < var10.heuristic || var13.heuristic == var10.heuristic && var13.costSoFar < var10.costSoFar) {
               var10 = var13;
            }

            if (++var11 > var6.maxNodes()) {
               break;
            }

            var12.expand(var13);
         }
      }

      return var6.partial() && var10 != var9
         ? new AStarPathfinder.PathResult(buildSteps(var10), false, var11, var10.costSoFar)
         : new AStarPathfinder.PathResult(null, false, var11, 0.0);
   }

   private static List<AStarPathfinder.Step> buildSteps(AStarPathfinder.Node var0) {
      ArrayList var1 = new ArrayList();

      for (AStarPathfinder.Node var2 = var0; var2.parent != null; var2 = var2.parent) {
         var1.add(new AStarPathfinder.Step(var2.blockX, var2.blockY, var2.blockZ, var2.move, var2.digs));
      }

      Collections.reverse(var1);
      return var1;
   }

   static double heuristic(int var0, int var1, int var2) {
      int var3 = Math.abs(var0);
      int var4 = Math.abs(var2);
      return Math.max(var3, var4) + (dIAGONAL_COST - 1.0) * Math.min(var3, var4) + Math.abs(var1) * 0.5;
   }

   public static long packPos(int var0, int var1, int var2) {
      return (long)(var0 & 67108863) << 38 | (long)(var2 & 67108863) << 12 | var1 & 4095;
   }

   public static int unpackX(long var0) {
      return (int)(var0 >> 38);
   }

   public static int unpackY(long var0) {
      return (int)(var0 << 52 >> 52);
   }

   public static int unpackZ(long var0) {
      return (int)(var0 << 26 >> 38);
   }

   private AStarPathfinder() {
   }

   public record Bounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public boolean contains(int var1, int var2, int var3) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }

      public static AStarPathfinder.Bounds around(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         return new AStarPathfinder.Bounds(
            Math.min(var0, var3) - var6,
            Math.min(var1, var4) - var7,
            Math.min(var2, var5) - var6,
            Math.max(var0, var3) + var6,
            Math.max(var1, var4) + var8,
            Math.max(var2, var5) + var6
         );
      }
   }

   public record DigTarget(int x, int y, int z) {
      private final int blockX;
      private final int blockY;
      private final int blockZ;

      public DigTarget(int x, int y, int z) {
         this.blockX = x;
         this.blockY = y;
         this.blockZ = z;
      }
   }

   public interface Goal {
      boolean reached(int var1, int var2, int var3);

      double estimate(int var1, int var2, int var3);

      static AStarPathfinder.Goal at(final int var0, final int var1, final int var2) {
         return new AStarPathfinder.Goal() {
            @Override
            public boolean reached(int var1x, int var2x, int var3) {
               return var1x == var0 && var2x == var1 && var3 == var2;
            }

            @Override
            public double estimate(int var1x, int var2x, int var3) {
               return AStarPathfinder.heuristic(var1x - var0, var2x - var1, var3 - var2);
            }
         };
      }

      static AStarPathfinder.Goal near(final double var0, final double var2, final double var4, final double var6, final double var8) {
         return new AStarPathfinder.Goal() {
            @Override
            public boolean reached(int var1, int var2x, int var3) {
               return Math.hypot(var1 + 0.5 - var0, var3 + 0.5 - var4) <= var6 && Math.abs(var2x - var2) <= var8;
            }

            @Override
            public double estimate(int var1, int var2x, int var3) {
               return Math.max(0.0, Math.hypot(var1 + 0.5 - var0, var3 + 0.5 - var4) - var6) + Math.max(0.0, Math.abs(var2x - var2) - var8);
            }
         };
      }

      static AStarPathfinder.Goal awayFrom(final List<double[]> var0, final double var1) {
         return new AStarPathfinder.Goal() {
            private double nearestDistance(int var1x, int var2) {
               double var3 = Double.MAX_VALUE;

               for (double[] var6 : var0) {
                  var3 = Math.min(var3, Math.hypot(var1x + 0.5 - var6[0], var2 + 0.5 - var6[1]));
               }

               return var3;
            }

            @Override
            public boolean reached(int var1x, int var2, int var3) {
               return this.nearestDistance(var1x, var3) >= var1;
            }

            @Override
            public double estimate(int var1x, int var2, int var3) {
               return Math.max(0.0, var1 - this.nearestDistance(var1x, var3));
            }
         };
      }

      static AStarPathfinder.Goal anyOf(final Set<Long> var0, final int var1, final int var2, final int var3, final double var4) {
         return new AStarPathfinder.Goal() {
            @Override
            public boolean reached(int var1x, int var2x, int var3x) {
               return var0.contains(AStarPathfinder.packPos(var1x, var2x, var3x));
            }

            @Override
            public double estimate(int var1x, int var2x, int var3x) {
               return Math.max(0.0, AStarPathfinder.heuristic(var1x - var1, var2x - var2, var3x - var3) - var4);
            }
         };
      }
   }

   public enum MoveType {
      WALK,
      DIAGONAL,
      ASCEND,
      DESCEND,
      DIG_DOWN,
      SWIM_UP;
   }

   private static final class NeighborExpander {
      final AStarPathfinder.WorldView world;
      final AStarPathfinder.Goal goal;
      final AStarPathfinder.Bounds bounds;
      final AStarPathfinder.Options options;
      final Map<Long, AStarPathfinder.Node> nodes;
      final PriorityQueue<AStarPathfinder.Node> openQueue;

      NeighborExpander(
         AStarPathfinder.WorldView var1,
         AStarPathfinder.Goal var2,
         AStarPathfinder.Bounds var3,
         AStarPathfinder.Options var4,
         Map<Long, AStarPathfinder.Node> var5,
         PriorityQueue<AStarPathfinder.Node> var6
      ) {
         this.world = var1;
         this.goal = var2;
         this.bounds = var3;
         this.options = var4;
         this.nodes = var5;
         this.openQueue = var6;
      }

      void expand(AStarPathfinder.Node var1) {
         int var2 = var1.blockX;
         int var3 = var1.blockY;
         int var4 = var1.blockZ;

         for (int[] var8 : AStarPathfinder.cARDINAL_OFFSETS) {
            int var9 = var2 + var8[0];
            int var10 = var4 + var8[1];
            this.addWalk(var1, var9, var3, var10);
            this.addAscend(var1, var9, var10);
            this.addDescend(var1, var9, var10);
         }

         for (int[] var14 : AStarPathfinder.dIAGONAL_OFFSETS) {
            this.addDiagonal(var1, var2 + var14[0], var4 + var14[1], var14);
         }

         this.relax(var1);
         this.clearanceCost(var1);
      }

      private boolean isStandable(int var1, int var2, int var3) {
         return this.world.floor(var1, var2 - 1, var3) || this.world.swim(var1, var2, var3);
      }

      private void clearanceCost(AStarPathfinder.Node var1) {
         int var2 = var1.blockX;
         int var3 = var1.blockY;
         int var4 = var1.blockZ;
         if (this.world.swim(var2, var3, var4)
            && this.isUsable(var2, var3 + 1, var4)
            && this.world.swim(var2, var3 + 1, var4)
            && this.world.open(var2, var3 + 2, var4)) {
            this.relax(var1, var2, var3 + 1, var4, AStarPathfinder.MoveType.SWIM_UP, 1.5, List.of());
         }
      }

      private void addWalk(AStarPathfinder.Node var1, int var2, int var3, int var4) {
         if (this.isUsable(var2, var3, var4) && this.isStandable(var2, var3, var4)) {
            ArrayList var5 = new ArrayList(2);
            double var6 = 1.0;
            double var8 = this.clearanceCost(var2, var3 + 1, var4, var5);
            if (!(var8 < 0.0)) {
               double var10 = this.clearanceCost(var2, var3, var4, var5);
               if (!(var10 < 0.0)) {
                  this.relax(var1, var2, var3, var4, AStarPathfinder.MoveType.WALK, var6 + var8 + var10, var5);
               }
            }
         }
      }

      private void addAscend(AStarPathfinder.Node var1, int var2, int var3) {
         int var4 = var1.blockY;
         if (this.isUsable(var2, var4 + 1, var3) && this.isUsable(var1.blockX, var4 + 2, var1.blockZ) && this.world.floor(var2, var4, var3)) {
            ArrayList var5 = new ArrayList(3);
            double var6 = this.clearanceCost(var1.blockX, var4 + 2, var1.blockZ, var5);
            if (!(var6 < 0.0)) {
               double var8 = this.clearanceCost(var2, var4 + 2, var3, var5);
               if (!(var8 < 0.0)) {
                  double var10 = this.clearanceCost(var2, var4 + 1, var3, var5);
                  if (!(var10 < 0.0)) {
                     this.relax(var1, var2, var4 + 1, var3, AStarPathfinder.MoveType.ASCEND, 2.0 + var6 + var8 + var10, var5);
                  }
               }
            }
         }
      }

      private void addDescend(AStarPathfinder.Node var1, int var2, int var3) {
         int var4 = var1.blockY;
         if (this.isUsable(var2, var4, var3)
            && this.world.open(var2, var4, var3)
            && this.world.open(var2, var4 + 1, var3)
            && this.world.open(var2, var4 - 1, var3)) {
            for (int var5 = 1; var5 <= this.options.maxDrop(); var5++) {
               int var6 = var4 - var5;
               if (!this.isUsable(var2, var6, var3) || !this.world.open(var2, var6, var3)) {
                  return;
               }

               if (this.isStandable(var2, var6, var3)) {
                  this.relax(var1, var2, var6, var3, AStarPathfinder.MoveType.DESCEND, 1.0 + 0.5 * var5, List.of());
                  return;
               }
            }
         }
      }

      private void addDiagonal(AStarPathfinder.Node var1, int var2, int var3, int[] var4) {
         int var5 = var1.blockY;
         if (this.isUsable(var2, var5, var3)
            && this.isStandable(var2, var5, var3)
            && this.world.open(var2, var5, var3)
            && this.world.open(var2, var5 + 1, var3)) {
            if (this.world.open(var1.blockX + var4[0], var5, var1.blockZ)
               && this.world.open(var1.blockX + var4[0], var5 + 1, var1.blockZ)
               && this.world.open(var1.blockX, var5, var1.blockZ + var4[1])
               && this.world.open(var1.blockX, var5 + 1, var1.blockZ + var4[1])) {
               this.relax(var1, var2, var5, var3, AStarPathfinder.MoveType.DIAGONAL, AStarPathfinder.dIAGONAL_COST, List.of());
            }
         }
      }

      private void relax(AStarPathfinder.Node var1) {
         if (this.options.dig()) {
            int var2 = var1.blockX;
            int var3 = var1.blockY - 1;
            int var4 = var1.blockZ;
            if (this.isUsable(var2, var3, var4) && !this.world.open(var2, var3, var4) && this.world.floor(var2, var3 - 1, var4)) {
               double var5 = this.world.digCost(var2, var3, var4);
               if (!(var5 < 0.0)) {
                  this.relax(
                     var1,
                     var2,
                     var3,
                     var4,
                     AStarPathfinder.MoveType.DIG_DOWN,
                     1.5 + var5 * this.options.digWeight(),
                     List.of(new AStarPathfinder.DigTarget(var2, var3, var4))
                  );
               }
            }
         }
      }

      private double clearanceCost(int var1, int var2, int var3, List<AStarPathfinder.DigTarget> var4) {
         if (!this.isUsable(var1, var2, var3)) {
            return -1.0;
         }

         if (this.world.open(var1, var2, var3)) {
            return 0.0;
         }

         if (!this.options.dig()) {
            return -1.0;
         }

         double var5 = this.world.digCost(var1, var2, var3);
         if (var5 < 0.0) {
            return -1.0;
         }

         var4.add(new AStarPathfinder.DigTarget(var1, var2, var3));
         return var5 * this.options.digWeight();
      }

      private boolean isUsable(int var1, int var2, int var3) {
         return this.bounds.contains(var1, var2, var3) && this.world.loaded(var1, var2, var3);
      }

      private void relax(
         AStarPathfinder.Node var1, int var2, int var3, int var4, AStarPathfinder.MoveType var5, double var6, List<AStarPathfinder.DigTarget> var8
      ) {
         long var9 = AStarPathfinder.packPos(var2, var3, var4);
         double var11 = var1.costSoFar + var6 + this.world.penalty(var2, var3, var4);
         AStarPathfinder.Node var13 = this.nodes.get(var9);
         if (var13 == null) {
            var13 = new AStarPathfinder.Node(var9, var2, var3, var4);
            var13.heuristic = this.goal.estimate(var2, var3, var4);
            this.nodes.put(var9, var13);
         } else if (var13.closed || var11 >= var13.costSoFar - 1.0E-9) {
            return;
         }

         var13.costSoFar = var11;
         var13.totalEstimate = var11 + var13.heuristic;
         var13.parent = var1;
         var13.move = var5;
         var13.digs = var8.isEmpty() ? List.of() : List.copyOf(var8);
         this.openQueue.add(var13);
      }
   }

   private static final class Node implements Comparable<AStarPathfinder.Node> {
      final long key;
      final int blockX;
      final int blockY;
      final int blockZ;
      double costSoFar;
      double totalEstimate;
      double heuristic;
      AStarPathfinder.Node parent;
      AStarPathfinder.MoveType move;
      List<AStarPathfinder.DigTarget> digs;
      boolean closed;

      Node(long var1, int var3, int var4, int var5) {
         this.key = var1;
         this.blockX = var3;
         this.blockY = var4;
         this.blockZ = var5;
      }

      public int compareTo(AStarPathfinder.Node var1) {
         int var2 = Double.compare(this.totalEstimate, var1.totalEstimate);
         return var2 != 0 ? var2 : Double.compare(this.heuristic, var1.heuristic);
      }
   }

   public record Options(int maxNodes, boolean dig, int maxDrop, boolean partial, double digWeight) {
      public static AStarPathfinder.Options walking(int var0) {
         return new AStarPathfinder.Options(var0, false, 3, false, 1.0);
      }

      public static AStarPathfinder.Options digging(int var0) {
         return new AStarPathfinder.Options(var0, true, 3, false, 1.0);
      }

      public AStarPathfinder.Options withPartial(boolean var1) {
         return new AStarPathfinder.Options(this.maxNodes, this.dig, this.maxDrop, var1, this.digWeight);
      }
   }

   public record PathResult(List<AStarPathfinder.Step> steps, boolean complete, int explored, double cost) {
      public boolean found() {
         return this.steps != null;
      }

      public int digs() {
         if (this.steps == null) {
            return 0;
         }

         int var1 = 0;

         for (AStarPathfinder.Step var3 : this.steps) {
            var1 += var3.digs().size();
         }

         return var1;
      }
   }

   public record Step(int x, int y, int z, AStarPathfinder.MoveType move, List<AStarPathfinder.DigTarget> digs) {
      private final int blockX;
      private final int blockY;
      private final int blockZ;

      public Step(int x, int y, int z, AStarPathfinder.MoveType move, List<AStarPathfinder.DigTarget> digs) {
         this.blockX = x;
         this.blockY = y;
         this.blockZ = z;
         this.move = move;
         this.digs = digs;
      }

      public boolean digging() {
         return !this.digs.isEmpty();
      }
   }

   public interface WorldView {
      boolean loaded(int var1, int var2, int var3);

      boolean open(int var1, int var2, int var3);

      boolean floor(int var1, int var2, int var3);

      double digCost(int var1, int var2, int var3);

      default double penalty(int var1, int var2, int var3) {
         return 0.0;
      }

      default boolean swim(int var1, int var2, int var3) {
         return false;
      }
   }
}

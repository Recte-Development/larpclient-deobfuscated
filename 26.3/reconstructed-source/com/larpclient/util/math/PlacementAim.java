package com.larpclient.util.math;

import com.larpclient.core.RotationManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class PlacementAim {
   private static final int mAX_LAYERS = 4;
   private static final int mAX_EXTRA_SUPPORTS = 8;
   public static final double mIN_HORIZONTAL_DISTANCE = 0.35;
   private static final List<AABB> fULL_BLOCK_SHAPE = List.of(new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
   private static final float[] yAW_OFFSETS = new float[]{0.0F, 10.0F, -10.0F, 20.0F, -20.0F, 30.0F, -30.0F, 40.0F, -40.0F};
   private static final int mAX_PITCH_SAMPLES = 6;

   public static int feetBlockY(double var0) {
      return Mth.floor(var0 + 1.0E-6) - 1;
   }

   public static List<Integer> layerYs(int var0, int var1) {
      ArrayList var2 = new ArrayList();
      int var3 = Math.max(var1, var0 - 4 + 1);
      var2.add(var0);

      for (int var4 = var0 - 1; var4 >= var3; var4--) {
         var2.add(var4);
      }

      return List.copyOf(var2);
   }

   public static List<List<BlockPos>> collectLayers(
      Vec3 var0, AABB var1, Vec3 var2, List<Integer> var3, boolean var4, Predicate<BlockPos> var5, Predicate<BlockPos> var6
   ) {
      ArrayList var7 = new ArrayList();

      for (int var9 : var3) {
         List var10 = candidateBlocks(var0, var1, var2, var9);
         var7.add(var10);
         if (var4) {
            List var11 = findSupportBlocks(var10, var5, var6);
            if (!var11.isEmpty()) {
               var7.add(var11);
            }
         }
      }

      return List.copyOf(var7);
   }

   public static List<BlockPos> findSupportBlocks(List<BlockPos> var0, Predicate<BlockPos> var1, Predicate<BlockPos> var2) {
      LinkedHashSet var3 = new LinkedHashSet();

      for (BlockPos var5 : var0) {
         if (var1.test(var5) && !hasAdjacentMatch(var5, var2)) {
            for (Direction var7 : Plane.HORIZONTAL) {
               BlockPos var8 = var5.relative(var7);
               if (!var0.contains(var8) && var1.test(var8) && hasAdjacentMatch(var8, var2)) {
                  var3.add(var8);
               }
            }
         }
      }

      return var3.stream().limit(8L).toList();
   }

   private static boolean hasAdjacentMatch(BlockPos var0, Predicate<BlockPos> var1) {
      for (Direction var5 : Direction.values()) {
         if (var1.test(var0.relative(var5))) {
            return true;
         }
      }

      return false;
   }

   public static List<BlockPos> candidateBlocks(Vec3 var0, AABB var1, Vec3 var2, int var3) {
      Vec3 var4 = new Vec3(var2.x, 0.0, var2.z);
      if (var4.lengthSqr() > 0.36) {
         var4 = var4.normalize().scale(0.6);
      }

      double var5 = Math.clamp(var1.getXsize() * 0.5 - 0.001, 0.0, 0.49);
      double var7 = Math.clamp(var1.getZsize() * 0.5 - 0.001, 0.0, 0.49);
      LinkedHashSet var9 = new LinkedHashSet();
      BlockPos var10 = new BlockPos(Mth.floor(var0.x), var3, Mth.floor(var0.z));
      var9.add(var10);

      for (double var14 : new double[]{0.0, 0.5, 1.0}) {
         Vec3 var16 = var0.add(var4.scale(var14));

         for (double var20 : new double[]{-var5, 0.0, var5}) {
            for (double var25 : new double[]{-var7, 0.0, var7}) {
               var9.add(new BlockPos(Mth.floor(var16.x + var20), var3, Mth.floor(var16.z + var25)));
            }
         }
      }

      ArrayList var27 = new ArrayList(var9);
      var27.remove(var10);
      var27.sort(Comparator.comparingDouble(var1x -> Vec3.atCenterOf(var1x).multiply(1.0, 0.0, 1.0).distanceToSqr(var0.multiply(1.0, 0.0, 1.0))));
      var27.addFirst(var10);
      return List.copyOf(var27.subList(0, Math.min(9, var27.size())));
   }

   public static int selectIndex(List<Boolean> var0, int var1) {
      int var2 = var0.indexOf(true);
      if (var2 < 0 && !var0.isEmpty()) {
         return var1 >= 0 && var1 < var0.size() ? var1 : 0;
      } else {
         return var2;
      }
   }

   public static List<Vec3> samplePoints(BlockPos var0) {
      Vec3 var1 = Vec3.atCenterOf(var0);
      ArrayList var2 = new ArrayList(27);

      for (double var6 : new double[]{0.3, 0.0, -0.3}) {
         for (double var11 : new double[]{0.0, -0.3, 0.3}) {
            for (double var16 : new double[]{0.0, -0.3, 0.3}) {
               var2.add(var1.add(var11, var6, var16));
            }
         }
      }

      return var2;
   }

   public static Vec3 findBestPoint(List<Vec3> var0, Vec3 var1, float var2, float var3, Predicate<Vec3> var4) {
      double[] var5 = new double[var0.size()];
      ArrayList var6 = new ArrayList(var0.size());

      for (int var7 = 0; var7 < var0.size(); var7++) {
         Vec3 var8 = ((Vec3)var0.get(var7)).subtract(var1);
         double var9 = Math.hypot(var8.x, var8.z);
         float var11 = (float)Math.toDegrees(Math.atan2(var8.z, var8.x)) - 90.0F;
         float var12 = (float)(-Math.toDegrees(Math.atan2(var8.y, var9)));
         var5[var7] = Math.abs(Mth.wrapDegrees(var11 - var2)) + Math.abs(var12 - var3) + (var9 < 0.35 ? 1000 : 0);
         var6.add(var7);
      }

      var6.sort(Comparator.comparingDouble(var1x -> var5[var1x]));

      for (int var14 : var6) {
         if (var4.test(((Vec3)var0.get(var14)).subtract(var1))) {
            return (Vec3)var0.get(var14);
         }
      }

      return null;
   }

   public static float yawToward(Vec3 var0, float var1) {
      return var0.x * var0.x + var0.z * var0.z < 1.0E-8 ? var1 : (float)Math.toDegrees(Math.atan2(-var0.z, -var0.x)) - 90.0F;
   }

   public static float[] computeRotation(BlockPos var0, Direction var1, List<Vec3> var2, Vec3 var3, float var4, float var5, float var6, Predicate<Vec3> var7) {
      float[] var8 = searchYaw(var0, var1, var3, var4, var5, var7);
      if (var8 != null) {
         return var8;
      }

      Vec3 var9 = findBestPoint(var2, var3, var4, var6, var7);
      return var9 == null ? null : RotationManager.calculateAngles(var3, var9);
   }

   public static float[] searchYaw(BlockPos var0, Direction var1, Vec3 var2, float var3, float var4, Predicate<Vec3> var5) {
      ArrayList var6 = new ArrayList(yAW_OFFSETS.length);

      for (float var10 : yAW_OFFSETS) {
         var6.add(var3 + var10);
      }

      var6.sort(Comparator.comparingDouble(var1x -> Math.abs(Mth.wrapDegrees(var1x - var4))));

      for (float var12 : var6) {
         float var13 = findPitch(var0, var1, var2, var12, var5);
         if (!Float.isNaN(var13)) {
            return new float[]{var12, var13};
         }
      }

      return null;
   }

   private static float findPitch(BlockPos var0, Direction var1, Vec3 var2, float var3, Predicate<Vec3> var4) {
      ArrayList var5 = new ArrayList();

      for (int var6 = 0; var6 <= 180; var6++) {
         float var7 = var6 * 0.5F;
         BlockHitResult var8 = AABB.clip(fULL_BLOCK_SHAPE, var2, var2.add(Vec3.directionFromRotation(var7, var3).scale(8.0)), var0);
         if (var8 != null && (var1 == null || var8.getDirection() == var1)) {
            var5.add(var7);
         }
      }

      if (var5.isEmpty()) {
         return Float.NaN;
      }

      ArrayList var12 = new ArrayList(6);
      double var13 = Math.max(1.0, var5.size() / 6.0);

      for (double var9 = var13 / 2.0; var9 < var5.size() && var12.size() < 6; var9 += var13) {
         var12.add((Float)var5.get((int)var9));
      }

      float var14 = (Float)var5.get(var5.size() / 2);
      var12.sort(Comparator.comparingDouble(var1x -> Math.abs(var1x - var14)));

      for (float var11 : var12) {
         if (var4.test(Vec3.directionFromRotation(var11, var3))) {
            return var11;
         }
      }

      return Float.NaN;
   }

   public static float[] aimAtBlock(BlockPos var0, Vec3 var1, float var2, float var3, float var4, Predicate<Vec3> var5, Predicate<Vec3> var6) {
      List var7 = samplePoints(var0);
      float[] var8 = computeRotation(var0, null, var7, var1, var2, var3, var4, var5);
      return var8 != null ? var8 : computeRotation(var0, null, var7, var1, var2, var3, var4, var6);
   }

   public static float[] aimAtFace(
      BlockPos var0, Direction var1, List<Vec3> var2, Vec3 var3, float var4, float var5, float var6, Predicate<Vec3> var7, Predicate<Vec3> var8, boolean var9
   ) {
      float[] var10 = searchYaw(var0, var1, var3, var4, var5, var7);
      if (var10 == null) {
         var10 = searchYaw(var0, var1, var3, var4, var5, var8);
      }

      if (var10 == null && var9) {
         var10 = computeRotation(var0, var1, var2, var3, var4, var5, var6, var7);
         return var10 != null ? var10 : computeRotation(var0, var1, var2, var3, var4, var5, var6, var8);
      } else {
         return var10;
      }
   }

   public static boolean computeRotation(BlockPos var0, Vec3 var1, Vec3 var2, double var3) {
      return AABB.clip(fULL_BLOCK_SHAPE, var1, var1.add(var2.normalize().scale(var3)), var0) != null;
   }

   public static Vec3 predictPosition(Vec3 var0, Vec3 var1, double var2) {
      Vec3 var4 = new Vec3(var1.x, 0.0, var1.z);
      if (var4.lengthSqr() > 1.0E-8) {
         var4 = var4.normalize().scale(var2);
      }

      return new Vec3(var0.x, 0.0, var0.z).add(var4);
   }

   public static double findSupportBlocks(double var0, boolean var2, boolean var3, double var4) {
      return var2 ? (var3 ? var4 : 0.0) : var0;
   }

   public static boolean isGrounded(int var0) {
      return var0 <= 1;
   }

   public static boolean hasGroundBelow(AABB var0, Vec3 var1, double var2, Predicate<AABB> var4) {
      return var4.test(
         new AABB(
            var0.minX + 1.0E-7 + var1.x,
            var0.minY - var2 - 1.0E-7,
            var0.minZ + 1.0E-7 + var1.z,
            var0.maxX - 1.0E-7 + var1.x,
            var0.minY,
            var0.maxZ - 1.0E-7 + var1.z
         )
      );
   }

   public static BlockPos yawToward(List<BlockPos> var0, Predicate<BlockPos> var1, Predicate<BlockPos> var2, Predicate<BlockPos> var3) {
      for (BlockPos var5 : var0) {
         if (var1.test(var5)) {
            for (Direction var9 : Direction.values()) {
               if (var9 != Direction.UP) {
                  BlockPos var10 = var5.relative(var9);
                  if (var2.test(var10) && var3.test(var10)) {
                     return var10;
                  }
               }
            }
         }
      }

      return null;
   }

   private PlacementAim() {
   }
}

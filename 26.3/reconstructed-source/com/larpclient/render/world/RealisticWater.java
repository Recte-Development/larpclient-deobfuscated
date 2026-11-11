package com.larpclient.render.world;

import com.larpclient.module.render.RealisticWaterModule;
import com.larpclient.render.overlay.DropletSurfaceRenderer;
import com.larpclient.util.misc.LarpLog;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class RealisticWater {
   public static final int mAX_DROPLETS = 4096;
   private static final int gRID_SIZE = 16384;
   private static final int mAX_NEIGHBORS = 48;
   private static final int mAX_CACHED_SHAPES = 8192;
   private static final double dROPLET_SPACING = 0.1;
   private static final double pLAYER_MARGIN = 0.064;
   private static final double cELL_SIZE = 0.18;
   private static final double[] posX = new double[4096];
   private static final double[] posY = new double[4096];
   private static final double[] posZ = new double[4096];
   private static final double[] prevX = new double[4096];
   private static final double[] prevY = new double[4096];
   private static final double[] prevZ = new double[4096];
   private static final double[] velX = new double[4096];
   private static final double[] velY = new double[4096];
   private static final double[] velZ = new double[4096];
   private static final double[] stepStartX = new double[4096];
   private static final double[] stepStartY = new double[4096];
   private static final double[] stepStartZ = new double[4096];
   private static final double[] forceX = new double[4096];
   private static final double[] forceY = new double[4096];
   private static final double[] forceZ = new double[4096];
   private static final int[] gridHeads = new int[16384];
   private static final int[] gridNext = new int[4096];
   private static final int[] cellX = new int[4096];
   private static final int[] cellY = new int[4096];
   private static final int[] cellZ = new int[4096];
   private static final Set<Long> volumetricBlocks = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<AABB>> collisionCache = new HashMap<>();
   private static final Map<Long, Long> pendingPours = new HashMap<>();
   private static ClientLevel level;
   private static int dropletCount;
   private static long tickCount;
   private static boolean capacityWarned;

   private static boolean isEnabled() {
      RealisticWaterModule var0 = RealisticWaterModule.getInstance();
      return var0 != null && var0.isRuntimeActive() && var0.volumetric.getValue() && !TerrainMaterials.isHookDisabled();
   }

   public static int getDropletCount() {
      return dropletCount;
   }

   public static boolean isVolumetric(BlockPos var0) {
      return isEnabled() && volumetricBlocks.contains(var0.asLong());
   }

   public static double getDropletRadius() {
      return 0.064;
   }

   public static double lerpX(int var0, float var1) {
      return prevX[var0] + (posX[var0] - prevX[var0]) * var1;
   }

   public static double lerpY(int var0, float var1) {
      return prevY[var0] + (posY[var0] - prevY[var0]) * var1;
   }

   public static double lerpZ(int var0, float var1) {
      return prevZ[var0] + (posZ[var0] - prevZ[var0]) * var1;
   }

   public static void onBlockChange(BlockPos var0, BlockState var1, BlockState var2) {
      if (isEnabled()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null
            && var3.level != null
            && var3.player != null
            && !(var3.player.distanceToSqr(var0.getX() + 0.5, var0.getY() + 0.5, var0.getZ() + 0.5) > 49.0)) {
            syncLevel(var3.level);
            boolean var4 = var1.getFluidState().is(FluidTags.WATER);
            boolean var5 = var2.getFluidState().is(FluidTags.WATER);
            if (!var1.isAir() && !var4 && var2.isAir()) {
               if (pendingPours.size() < 128) {
                  pendingPours.put(var0.asLong(), tickCount + 20L);
               }

               for (Direction var9 : Direction.values()) {
                  if (var3.level.getFluidState(var0.relative(var9)).is(FluidTags.WATER)) {
                     pour(var0);
                     break;
                  }
               }
            }

            if (var5 && (!var4 || var2.getFluidState().isSource() && !var1.getFluidState().isSource())) {
               byte var11 = 0;

               for (Direction var10 : Direction.values()) {
                  if (volumetricBlocks.contains(var0.relative(var10).asLong())) {
                     var11 = 1;
                     break;
                  }
               }

               int var13 = !var3.player.getMainHandItem().is(Items.WATER_BUCKET)
                     && !var3.player.getOffhandItem().is(Items.WATER_BUCKET)
                     && !var3.player.getMainHandItem().is(Items.BUCKET)
                     && !var3.player.getOffhandItem().is(Items.BUCKET)
                  ? 0
                  : 1;
               int var15 = !(
                     var3.hitResult instanceof BlockHitResult var17
                        && (var0.equals(var17.getBlockPos()) || var0.equals(var17.getBlockPos().relative(var17.getDirection())))
                  )
                  ? 0
                  : 1;
               int var18 = var2.getFluidState().isSource() && var3.options.keyUse.isDown() && var13 != 0 && var15 != 0 ? 1 : 0;
               if (var18 != 0) {
                  spawnDroplets(var0, true);
               } else if (pendingPours.containsKey(var0.asLong())) {
                  pour(var0);
               } else if (var11 != 0) {
                  markVolumetric(var0);
               }
            }

            if (!var5) {
               volumetricBlocks.remove(var0.asLong());
            }
         }
      }
   }

   private static void markVolumetric(BlockPos var0) {
      if (volumetricBlocks.size() < 4096 && volumetricBlocks.add(var0.asLong())) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.level != null) {
            var1.levelExtractor.setSectionDirtyWithNeighbors(var0.getX() >> 4, var0.getY() >> 4, var0.getZ() >> 4);
         }
      }
   }

   private static void pour(BlockPos var0) {
      spawnDroplets(var0, false);
   }

   private static void spawnDroplets(BlockPos var0, boolean var1) {
      if (var1 || !volumetricBlocks.contains(var0.asLong())) {
         if (dropletCount + 1000 > 4096) {
            if (!capacityWarned) {
               LarpLog.info("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               capacityWarned = (boolean)1;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = dropletCount++;
                     double var6 = ((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     posX[var5] = prevX[var5] = var0.getX() + (var4 + 0.5) * 0.1 + var6;
                     posY[var5] = prevY[var5] = var0.getY() + (var2 + 0.5) * 0.1 - var6;
                     posZ[var5] = prevZ[var5] = var0.getZ() + (var3 + 0.5) * 0.1;
                     velX[var5] = velY[var5] = velZ[var5] = 0.0;
                  }
               }
            }

            markVolumetric(var0);
            LarpLog.debug("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, dropletCount);
         }
      }
   }

   public static void tick(Minecraft var0) {
      if (!isEnabled()) {
         if (dropletCount > 0 || !volumetricBlocks.isEmpty()) {
            reset();
         }
      } else if (var0.level != null && var0.player != null && !var0.isPaused()) {
         syncLevel(var0.level);
         tickCount++;
         pendingPours.entrySet().removeIf(var0x -> var0x.getValue() < tickCount);
         if (dropletCount != 0) {
            System.arraycopy(posX, 0, prevX, 0, dropletCount);
            System.arraycopy(posY, 0, prevY, 0, dropletCount);
            System.arraycopy(posZ, 0, prevZ, 0, dropletCount);
            collisionCache.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               step(var0, 0.025);
            }
         }
      }
   }

   private static void syncLevel(ClientLevel var0) {
      if (level != var0) {
         reset();
         level = var0;
      }
   }

   private static void step(Minecraft var0, double var1) {
      for (int var3 = 0; var3 < dropletCount; var3++) {
         stepStartX[var3] = posX[var3];
         stepStartY[var3] = posY[var3];
         stepStartZ[var3] = posZ[var3];
         if (!level.hasChunkAt(BlockPos.containing(posX[var3], posY[var3], posZ[var3]))) {
            velX[var3] = velY[var3] = velZ[var3] = 0.0;
         } else {
            velY[var3] = Math.max(-6.0, velY[var3] - 9.8 * var1);
            double var4 = posX[var3];
            double var6 = posY[var3];
            double var8 = posZ[var3];
            posX[var3] = posX[var3] + velX[var3] * var1;
            posY[var3] = posY[var3] + velY[var3] * var1;
            posZ[var3] = posZ[var3] + velZ[var3] * var1;
            collideWithBlocks(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         buildGrid();
         Arrays.fill(forceX, 0, dropletCount, 0.0);
         Arrays.fill(forceY, 0, dropletCount, 0.0);
         Arrays.fill(forceZ, 0, dropletCount, 0.0);

         label108:
         for (int var29 = 0; var29 < dropletCount; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = cellX[var29] + var36;
                     int var10 = cellY[var29] + var7;
                     int var11 = cellZ[var29] + var34;

                     for (int var12 = gridHeads[spawnDroplets(var9, var10, var11)]; var12 >= 0; var12 = gridNext[var12]) {
                        if (var12 != var29 && cellX[var12] == var9 && cellY[var12] == var10 && cellZ[var12] == var11) {
                           double var13 = posX[var29] - posX[var12];
                           double var15 = posY[var29] - posY[var12];
                           double var17 = posZ[var29] - posZ[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label108;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              forceX[var29] = forceX[var29] + var13 * var25;
                              forceY[var29] = forceY[var29] + var15 * var25;
                              forceZ[var29] = forceZ[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < dropletCount; var30++) {
            double var32 = posX[var30];
            double var35 = posY[var30];
            double var37 = posZ[var30];
            posX[var30] = posX[var30] + Math.clamp(forceX[var30], -0.015, 0.015);
            posY[var30] = posY[var30] + Math.clamp(forceY[var30], -0.015, 0.015);
            posZ[var30] = posZ[var30] + Math.clamp(forceZ[var30], -0.015, 0.015);
            collideWithBlocks(var30, var32, var35, var37);
         }
      }

      AABB var28 = var0.player.getBoundingBox().inflate(0.064);
      Vec3 var31 = var0.player.getDeltaMovement();

      for (int var33 = 0; var33 < dropletCount; var33++) {
         velX[var33] = (posX[var33] - stepStartX[var33]) / var1;
         velY[var33] = (posY[var33] - stepStartY[var33]) / var1;
         velZ[var33] = (posZ[var33] - stepStartZ[var33]) / var1;
         if (var28.contains(posX[var33], posY[var33], posZ[var33])) {
            velX[var33] = velX[var33] + var31.x * 3.0;
            velZ[var33] = velZ[var33] + var31.z * 3.0;
            velY[var33] = velY[var33] + Math.max(0.0, var31.y) * 2.0;
         }

         velX[var33] = Math.clamp(velX[var33] * 0.99, -6.0, 6.0);
         velY[var33] = Math.clamp(velY[var33] * 0.99, -6.0, 6.0);
         velZ[var33] = Math.clamp(velZ[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(posX[var33] + posY[var33] + posZ[var33]) || posY[var33] < level.getMinY() - 8) {
            removeDroplet(var33--);
         }
      }
   }

   private static void buildGrid() {
      Arrays.fill(gridHeads, -1);
      int var0 = 0;

      while (var0 < dropletCount) {
         cellX[var0] = (int)Math.floor(posX[var0] / 0.18);
         cellY[var0] = (int)Math.floor(posY[var0] / 0.18);
         cellZ[var0] = (int)Math.floor(posZ[var0] / 0.18);
         int var1 = spawnDroplets(cellX[var0], cellY[var0], cellZ[var0]);
         gridNext[var0] = gridHeads[var1];
         gridHeads[var1] = var0++;
      }
   }

   private static int spawnDroplets(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void collideWithBlocks(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(posX[var0]);
      int var8 = (int)Math.floor(posY[var0]);
      int var9 = (int)Math.floor(posZ[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               BlockPos var13 = new BlockPos(var7 + var12, var8 + var11, var9 + var10);
               List var14 = collisionCache.get(var13.asLong());
               if (var14 == null) {
                  if (collisionCache.size() >= 8192 || !level.hasChunkAt(var13)) {
                     posX[var0] = var1;
                     posY[var0] = var3;
                     posZ[var0] = var5;
                     velX[var0] = velY[var0] = velZ[var0] = 0.0;
                     return;
                  }

                  var14 = level.getBlockState(var13).getCollisionShape(level, var13).toAabbs();
                  collisionCache.put(var13.asLong(), var14);
               }

               for (AABB var16 : var14) {
                  double var17 = 0.0352;
                  double var19 = var16.minX + var13.getX() - var17;
                  double var21 = var16.maxX + var13.getX() + var17;
                  double var23 = var16.minY + var13.getY() - var17;
                  double var25 = var16.maxY + var13.getY() + var17;
                  double var27 = var16.minZ + var13.getZ() - var17;
                  double var29 = var16.maxZ + var13.getZ() + var17;
                  if (!(posX[var0] <= var19)
                     && !(posX[var0] >= var21)
                     && !(posY[var0] <= var23)
                     && !(posY[var0] >= var25)
                     && !(posZ[var0] <= var27)
                     && !(posZ[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = posX[var0] - var19;
                     double var34 = var21 - posX[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = posY[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - posY[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = posZ[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - posZ[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           posX[var0] = var19 - 1.0E-4;
                           velX[var0] = Math.min(0.0, velX[var0]) * 0.15;
                           break;
                        case 1:
                           posX[var0] = var21 + 1.0E-4;
                           velX[var0] = Math.max(0.0, velX[var0]) * 0.15;
                           break;
                        case 2:
                           posY[var0] = var23 - 1.0E-4;
                           velY[var0] = Math.min(0.0, velY[var0]) * 0.08;
                           break;
                        case 3:
                           posY[var0] = var25 + 1.0E-4;
                           velY[var0] = Math.max(0.0, velY[var0]) * 0.08;
                           velX[var0] = velX[var0] * 0.97;
                           velZ[var0] = velZ[var0] * 0.97;
                           break;
                        case 4:
                           posZ[var0] = var27 - 1.0E-4;
                           velZ[var0] = Math.min(0.0, velZ[var0]) * 0.15;
                           break;
                        case 5:
                           posZ[var0] = var29 + 1.0E-4;
                           velZ[var0] = Math.max(0.0, velZ[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void removeDroplet(int var0) {
      int var1 = --dropletCount;
      posX[var0] = posX[var1];
      posY[var0] = posY[var1];
      posZ[var0] = posZ[var1];
      prevX[var0] = prevX[var1];
      prevY[var0] = prevY[var1];
      prevZ[var0] = prevZ[var1];
      velX[var0] = velX[var1];
      velY[var0] = velY[var1];
      velZ[var0] = velZ[var1];
   }

   public static void reset() {
      int var0 = !volumetricBlocks.isEmpty() ? 1 : 0;
      dropletCount = 0;
      tickCount = 0L;
      capacityWarned = (boolean)0;
      volumetricBlocks.clear();
      collisionCache.clear();
      pendingPours.clear();
      level = null;
      if (var0 != 0) {
         TerrainMaterials.refreshChunks();
      }

      DropletSurfaceRenderer.releaseResources();
   }

   private RealisticWater() {
   }
}

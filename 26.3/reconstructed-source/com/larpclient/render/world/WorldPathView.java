package com.larpclient.render.world;

import com.larpclient.util.world.AStarPathfinder;
import com.larpclient.util.world.AnchorPlacement;
import com.larpclient.util.world.BlockBreaker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Direction.Plane;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public final class WorldPathView implements AnchorPlacement.AnchorWorld {
   private static final byte fLAG_LOADED = 1;
   private static final byte fLAG_OPEN = 2;
   private static final byte fLAG_FLOOR = 4;
   private static final byte fLAG_LAVA = 8;
   private static final byte fLAG_WATER = 16;
   private static final byte fLAG_SOLID = 32;
   static final double dIG_TICKS_PER_COST = 4.6;
   private final Level level;
   private final Map<Long, Byte> flagCache = new HashMap<>();
   private final Map<Long, Double> digCostCache = new HashMap<>();
   private final Set<Long> blockedPositions = new HashSet<>();
   private final List<double[]> avoidZones = new ArrayList<>();
   static final double aVOID_PENALTY = 30.0;
   private final MutableBlockPos scratchPos = new MutableBlockPos();
   private final ToIntFunction<BlockState> toolSlotSelector;
   private final boolean avoidLiquidNeighbors;
   private int[] digBounds;
   private Set<BlockPos> allowedDigPositions;

   static Screen currentScreen() {
      return Minecraft.getInstance().gui.screen();
   }

   public WorldPathView(ToIntFunction<BlockState> var1, boolean var2) {
      this.level = Minecraft.getInstance().level;
      this.toolSlotSelector = var1;
      this.avoidLiquidNeighbors = var2;
   }

   public WorldPathView addAvoidZone(double var1, double var3, double var5, double var7) {
      this.avoidZones.add(new double[]{var1, var3, var5, var7});
      return this;
   }

   public WorldPathView setDigBounds(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.digBounds = new int[]{var1, var2, var3, var4, var5, var6};
      return this;
   }

   public WorldPathView setAllowedDigPositions(Set<BlockPos> var1) {
      this.allowedDigPositions = var1;
      return this;
   }

   public WorldPathView setDigBounds(BlockPos var1) {
      this.blockedPositions.add(AStarPathfinder.packPos(var1.getX(), var1.getY(), var1.getZ()));
      return this;
   }

   private byte getFlags(int var1, int var2, int var3) {
      long var4 = AStarPathfinder.packPos(var1, var2, var3);
      Byte var6 = this.flagCache.get(var4);
      if (var6 != null) {
         return var6;
      }

      byte var7 = this.computeFlags(var1, var2, var3);
      this.flagCache.put(var4, var7);
      return var7;
   }

   private byte computeFlags(int var1, int var2, int var3) {
      this.scratchPos.set(var1, var2, var3);
      if (this.level != null && !this.level.isOutsideBuildHeight(this.scratchPos) && this.level.hasChunkAt(this.scratchPos)) {
         byte var4 = 1;
         BlockState var5 = this.level.getBlockState(this.scratchPos);
         FluidState var6 = var5.getFluidState();
         if (var6.is(FluidTags.LAVA)) {
            var4 = (byte)(var4 | 8);
         }

         if (var6.is(FluidTags.WATER)) {
            var4 = (byte)(var4 | 16);
         }

         boolean var7 = var5.getCollisionShape(this.level, this.scratchPos).isEmpty();
         if (!var7) {
            var4 = (byte)(var4 | 32);
         }

         if (var7 && !var6.is(FluidTags.LAVA) && !isHazard(var5)) {
            var4 = (byte)(var4 | 2);
         }

         if (!var7
            && var6.isEmpty()
            && (var5.isFaceSturdy(this.level, this.scratchPos, Direction.UP) || this.collisionTop(var5) >= 0.75 && this.collisionTop(var5) <= 1.0)
            && !var5.is(Blocks.MAGMA_BLOCK)
            && !var5.is(Blocks.CAMPFIRE)
            && !var5.is(Blocks.SOUL_CAMPFIRE)
            && !var5.is(Blocks.POWDER_SNOW)
            && !this.blockedPositions.contains(AStarPathfinder.packPos(var1, var2, var3))) {
            var4 = (byte)(var4 | 4);
         }

         return var4;
      } else {
         return 0;
      }
   }

   private double collisionTop(BlockState var1) {
      return var1.getCollisionShape(this.level, this.scratchPos).max(Axis.Y);
   }

   private static boolean isHazard(BlockState var0) {
      return var0.is(Blocks.FIRE)
         || var0.is(Blocks.SOUL_FIRE)
         || var0.is(Blocks.COBWEB)
         || var0.is(Blocks.SWEET_BERRY_BUSH)
         || var0.is(Blocks.POWDER_SNOW)
         || var0.is(Blocks.WITHER_ROSE)
         || var0.is(Blocks.NETHER_PORTAL)
         || var0.is(Blocks.END_PORTAL);
   }

   @Override
   public boolean loaded(int var1, int var2, int var3) {
      return (this.getFlags(var1, var2, var3) & 1) != 0;
   }

   @Override
   public boolean open(int var1, int var2, int var3) {
      return (this.getFlags(var1, var2, var3) & 2) != 0;
   }

   @Override
   public boolean floor(int var1, int var2, int var3) {
      return (this.getFlags(var1, var2, var3) & 4) != 0;
   }

   @Override
   public boolean lava(int var1, int var2, int var3) {
      return (this.getFlags(var1, var2, var3) & 8) != 0;
   }

   @Override
   public boolean water(int var1, int var2, int var3) {
      return (this.getFlags(var1, var2, var3) & 16) != 0;
   }

   @Override
   public boolean solid(int var1, int var2, int var3) {
      return (this.getFlags(var1, var2, var3) & 32) != 0;
   }

   @Override
   public boolean swim(int var1, int var2, int var3) {
      return this.water(var1, var2, var3) && this.open(var1, var2, var3);
   }

   @Override
   public double penalty(int var1, int var2, int var3) {
      double var4 = 0.0;
      if (this.water(var1, var2, var3) || this.water(var1, var2 + 1, var3)) {
         var4 += 3.0;
      }

      for (Direction var7 : Plane.HORIZONTAL) {
         if (this.lava(var1 + var7.getStepX(), var2, var3 + var7.getStepZ()) || this.lava(var1 + var7.getStepX(), var2 + 1, var3 + var7.getStepZ())) {
            var4 += 8.0;
         }
      }

      if (this.lava(var1, var2 - 1, var3) || this.lava(var1, var2 + 2, var3)) {
         var4 += 8.0;
      }

      for (double[] var15 : this.avoidZones) {
         double var8 = var1 + 0.5 - var15[0];
         double var10 = var2 - var15[1];
         double var12 = var3 + 0.5 - var15[2];
         if (var8 * var8 + var12 * var12 <= var15[3] * var15[3] && Math.abs(var10) <= 4.0) {
            var4 += 30.0;
         }
      }

      return var4;
   }

   @Override
   public double digCost(int var1, int var2, int var3) {
      long var4 = AStarPathfinder.packPos(var1, var2, var3);
      Double var6 = this.digCostCache.get(var4);
      if (var6 != null) {
         return var6;
      }

      double var7 = this.computeDigCost(var1, var2, var3);
      this.digCostCache.put(var4, var7);
      return var7;
   }

   private double computeDigCost(int var1, int var2, int var3) {
      if (this.blockedPositions.contains(AStarPathfinder.packPos(var1, var2, var3))) {
         return -1.0;
      }

      if (this.digBounds == null
         || var1 >= this.digBounds[0]
            && var2 >= this.digBounds[1]
            && var3 >= this.digBounds[2]
            && var1 <= this.digBounds[3]
            && var2 <= this.digBounds[4]
            && var3 <= this.digBounds[5]) {
         BlockPos var4 = new BlockPos(var1, var2, var3);
         if (this.allowedDigPositions != null && !this.allowedDigPositions.contains(var4)) {
            return -1.0;
         }

         if (!BlockBreaker.canBreak(var4)) {
            return -1.0;
         }

         BlockState var5 = this.level.getBlockState(var4);
         if (!var5.hasBlockEntity() && !var5.is(Blocks.RESPAWN_ANCHOR) && !var5.is(Blocks.BEDROCK)) {
            for (Direction var9 : Direction.values()) {
               if (var9 != Direction.DOWN
                  && (
                     this.lava(var1 + var9.getStepX(), var2 + var9.getStepY(), var3 + var9.getStepZ())
                        || this.water(var1 + var9.getStepX(), var2 + var9.getStepY(), var3 + var9.getStepZ())
                  )) {
                  return -1.0;
               }
            }

            if (this.avoidLiquidNeighbors && this.touchesLiquid(var1, var2, var3)) {
               return -1.0;
            }

            float var15 = var5.getDestroySpeed(this.level, var4);
            if (var15 < 0.0F) {
               return -1.0;
            }

            double var16 = 1.0;
            if (var15 > 0.0F) {
               int var17 = this.toolSlotSelector.applyAsInt(var5);
               ItemStack var10 = var17 >= 0 ? Minecraft.getInstance().player.getInventory().getItem(var17) : ItemStack.EMPTY;
               float var11 = Math.max(1.0F, var10.getDestroySpeed(var5));
               int var12 = var5.requiresCorrectToolForDrops() && !var10.isCorrectToolForDrops(var5) ? 0 : 1;
               double var13 = Math.ceil(var15 * (var12 != 0 ? 30 : 100) / var11);
               var16 += var13 / 4.6;
            }

            if (this.level.getBlockState(var4.above()).getBlock() instanceof FallingBlock) {
               var16 += 4.0;
            }

            return var16;
         } else {
            return -1.0;
         }
      } else {
         return -1.0;
      }
   }

   private boolean touchesLiquid(int var1, int var2, int var3) {
      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            for (int var6 = -1; var6 <= 1; var6++) {
               if (this.lava(var1 + var4, var2 + var5, var3 + var6) || this.water(var1 + var4, var2 + var5, var3 + var6)) {
                  return true;
               }
            }
         }
      }

      return false;
   }
}

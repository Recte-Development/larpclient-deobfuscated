package com.larpclient;

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

public final class c6 implements tz.f {
   private static final byte f = 1;
   private static final byte n = 2;
   private static final byte K = 4;
   private static final byte I = 8;
   private static final byte A = 16;
   private static final byte U = 32;
   static final double E = 4.6;
   private final Level c;
   private final Map<Long, Byte> T = new HashMap<>();
   private final Map<Long, Double> p = new HashMap<>();
   private final Set<Long> b = new HashSet<>();
   private final List<double[]> x = new ArrayList<>();
   static final double F = 30.0;
   private final MutableBlockPos L = new MutableBlockPos();
   private final ToIntFunction<BlockState> y;
   private final boolean B;
   private int[] a;
   private Set<BlockPos> w;

   static Screen D() {
      return Minecraft.getInstance().gui.screen();
   }

   public c6(ToIntFunction<BlockState> var1, boolean var2) {
      this.c = Minecraft.getInstance().level;
      this.y = var1;
      this.B = var2;
   }

   public c6 Y(double var1, double var3, double var5, double var7) {
      this.x.add(new double[]{var1, var3, var5, var7});
      return this;
   }

   public c6 S(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.a = new int[]{var1, var2, var3, var4, var5, var6};
      return this;
   }

   public c6 Q(Set<BlockPos> var1) {
      this.w = var1;
      return this;
   }

   public c6 I(BlockPos var1) {
      this.b.add(zf.O(var1.getX(), var1.getY(), var1.getZ()));
      return this;
   }

   private byte l(int var1, int var2, int var3) {
      long var4 = zf.O(var1, var2, var3);
      Byte var6 = this.T.get(var4);
      if (var6 != null) {
         return var6;
      }

      byte var7 = this.Z(var1, var2, var3);
      this.T.put(var4, var7);
      return var7;
   }

   private byte Z(int var1, int var2, int var3) {
      this.L.set(var1, var2, var3);
      if (this.c != null && !this.c.isOutsideBuildHeight(this.L) && this.c.hasChunkAt(this.L)) {
         byte var4 = 1;
         BlockState var5 = this.c.getBlockState(this.L);
         FluidState var6 = var5.getFluidState();
         if (var6.is(FluidTags.LAVA)) {
            var4 = (byte)(var4 | 8);
         }

         if (var6.is(FluidTags.WATER)) {
            var4 = (byte)(var4 | 16);
         }

         boolean var7 = var5.getCollisionShape(this.c, this.L).isEmpty();
         if (!var7) {
            var4 = (byte)(var4 | 32);
         }

         if (var7 && !var6.is(FluidTags.LAVA) && !T(var5)) {
            var4 = (byte)(var4 | 2);
         }

         if (!var7
            && var6.isEmpty()
            && (var5.isFaceSturdy(this.c, this.L, Direction.UP) || this.L(var5) >= 0.75 && this.L(var5) <= 1.0)
            && !var5.is(Blocks.MAGMA_BLOCK)
            && !var5.is(Blocks.CAMPFIRE)
            && !var5.is(Blocks.SOUL_CAMPFIRE)
            && !var5.is(Blocks.POWDER_SNOW)
            && !this.b.contains(zf.O(var1, var2, var3))) {
            var4 = (byte)(var4 | 4);
         }

         return var4;
      } else {
         return 0;
      }
   }

   private double L(BlockState var1) {
      return var1.getCollisionShape(this.c, this.L).max(Axis.Y);
   }

   private static boolean T(BlockState var0) {
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
      return (this.l(var1, var2, var3) & 1) != 0;
   }

   @Override
   public boolean open(int var1, int var2, int var3) {
      return (this.l(var1, var2, var3) & 2) != 0;
   }

   @Override
   public boolean floor(int var1, int var2, int var3) {
      return (this.l(var1, var2, var3) & 4) != 0;
   }

   @Override
   public boolean lava(int var1, int var2, int var3) {
      return (this.l(var1, var2, var3) & 8) != 0;
   }

   @Override
   public boolean water(int var1, int var2, int var3) {
      return (this.l(var1, var2, var3) & 16) != 0;
   }

   @Override
   public boolean solid(int var1, int var2, int var3) {
      return (this.l(var1, var2, var3) & 32) != 0;
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

      for (double[] var15 : this.x) {
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
      long var4 = zf.O(var1, var2, var3);
      Double var6 = this.p.get(var4);
      if (var6 != null) {
         return var6;
      }

      double var7 = this.h(var1, var2, var3);
      this.p.put(var4, var7);
      return var7;
   }

   private double h(int var1, int var2, int var3) {
      if (this.b.contains(zf.O(var1, var2, var3))) {
         return -1.0;
      }

      if (this.a == null || var1 >= this.a[0] && var2 >= this.a[1] && var3 >= this.a[2] && var1 <= this.a[3] && var2 <= this.a[4] && var3 <= this.a[5]) {
         BlockPos var4 = new BlockPos(var1, var2, var3);
         if (this.w != null && !this.w.contains(var4)) {
            return -1.0;
         }

         if (!l0.B(var4)) {
            return -1.0;
         }

         BlockState var5 = this.c.getBlockState(var4);
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

            if (this.B && this.S(var1, var2, var3)) {
               return -1.0;
            }

            float var15 = var5.getDestroySpeed(this.c, var4);
            if (var15 < 0.0F) {
               return -1.0;
            }

            double var16 = 1.0;
            if (var15 > 0.0F) {
               int var17 = this.y.applyAsInt(var5);
               ItemStack var10 = var17 >= 0 ? Minecraft.getInstance().player.getInventory().getItem(var17) : ItemStack.EMPTY;
               float var11 = Math.max(1.0F, var10.getDestroySpeed(var5));
               int var12 = var5.requiresCorrectToolForDrops() && !var10.isCorrectToolForDrops(var5) ? 0 : 1;
               double var13 = Math.ceil(var15 * (var12 != 0 ? 30 : 100) / var11);
               var16 += var13 / 4.6;
            }

            if (this.c.getBlockState(var4.above()).getBlock() instanceof FallingBlock) {
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

   private boolean S(int var1, int var2, int var3) {
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

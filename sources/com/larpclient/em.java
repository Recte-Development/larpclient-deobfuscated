package com.larpclient;

import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class em {
   private static final int J = 1;
   private static final int W = 2;
   private static final int G = 3;
   private static final int I = 4;
   private static final Set<String> y = Set.of(
      "short_grass",
      "tall_grass",
      "short_dry_grass",
      "tall_dry_grass",
      "fern",
      "large_fern",
      "dead_bush",
      "sweet_berry_bush",
      "azalea",
      "flowering_azalea",
      "pink_petals",
      "leaf_litter",
      "wildflowers",
      "bush",
      "firefly_bush",
      "cactus_flower",
      "torchflower_crop",
      "pitcher_crop",
      "pitcher_plant"
   );
   private static final ThreadLocal<em.n> s = new ThreadLocal<>();
   private static final ThreadLocal<Integer> C = new ThreadLocal<>();
   private static final ThreadLocal<em.w> S = new ThreadLocal<>();
   private static volatile boolean N;
   public static final int p = 252;
   public static final int h = 251;
   public static final int o = 249;
   private static volatile boolean r;
   private static volatile boolean M;
   private static volatile boolean t;
   private static volatile boolean b;
   private static volatile boolean T;

   public static void C() {
      SnowGroundModule var0 = SnowGroundModule.d();
      N = (boolean)(var0 != null && var0.isRuntimeActive() ? 1 : 0);
      WavingFoliageModule var1 = WavingFoliageModule.O();
      r = (boolean)(var1 != null && var1.isRuntimeActive() ? 1 : 0);
      M = (boolean)(r && var1.x.P() ? 1 : 0);
      t = (boolean)(M && var1.H.P() ? 1 : 0);
      b = (boolean)(r && var1.F.P() ? 1 : 0);
      RealisticWaterModule var2 = RealisticWaterModule.G();
      T = (boolean)(var2 != null && var2.isRuntimeActive() ? 1 : 0);
   }

   public static boolean t(BlockState var0, float var1, BlockGetter var2, BlockPos var3) {
      return x(var0, 0.0F, var1, 0.0F, var2, var3);
   }

   public static boolean x(BlockState var0, float var1, float var2, float var3, BlockGetter var4, BlockPos var5) {
      if (!N && !r) {
         return false;
      }

      s.remove();
      S.remove();
      if (N && !k.K() && var0.getFluidState().isEmpty()) {
         VoxelShape var6 = var0.getCollisionShape(var4, var5);
         if (!var6.isEmpty()) {
            float var7 = (float)var6.max(Axis.Y);
            boolean var8 = var4.getBlockState(var5.above()).isFaceSturdy(var4, var5.above(), Direction.DOWN);
            int var9 = 0;
            if (!var8) {
               for (int var10 = 0; var10 <= 1; var10++) {
                  for (int var11 = -1; var11 <= 1; var11++) {
                     for (int var12 = -1; var12 <= 1; var12++) {
                        if (var11 != 0 || var10 != 0 || var12 != 0) {
                           BlockPos var13 = var5.offset(var11, var10, var12);
                           BlockState var14 = var4.getBlockState(var13);
                           if (!var14.isAir()) {
                              for (AABB var16 : var14.getCollisionShape(var4, var13).toAabbs()) {
                                 if (!(var10 + var16.minY > var7 + 0.135) && !(var10 + var16.maxY <= var7 + 0.001)) {
                                    for (int var17 = 0; var17 < 4; var17++) {
                                       double var18 = (var17 & 1) - var11;
                                       double var20 = (var17 >> 1) - var12;
                                       if (var18 >= var16.minX - 0.001
                                          && var18 <= var16.maxX + 0.001
                                          && var20 >= var16.minZ - 0.001
                                          && var20 <= var16.maxZ + 0.001) {
                                          var9 |= 1 << var17;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }

            S.set(new em.w(var1, var3, var8 ? Float.POSITIVE_INFINITY : var2 + var7, var9));
         }
      }

      if (r && !k.K()) {
         int var22 = B(var0);
         if ((var22 != 1 && var22 != 2 || M) && (var22 != 3 || b) && (var22 != 4 || t) && var22 != 0) {
            float var23 = !t || var22 != 4 && var22 != 1 ? -1.0F : B(var0, var22, var4, var5);
            if (var22 == 4 && var23 < 0.0F) {
               return true;
            }

            int var24 = var0.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)
                  && var0.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER
               ? 1
               : 0;
            s.set(new em.n(var22, var2, var23, (boolean)var24));
            return true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   public static void s() {
      s.remove();
      S.remove();
   }

   public static int h(int var0, float var1) {
      return T(var0, Float.NaN, var1, Float.NaN);
   }

   public static int T(int var0, float var1, float var2, float var3) {
      if (!N && !r) {
         return var0;
      }

      em.n var4 = s.get();
      if (var4 == null) {
         em.w var10 = S.get();
         if (var10 == null) {
            return var0;
         }

         short var11 = 251;
         if (var2 >= var10.top - 0.001F) {
            var11 = 252;
            float var13 = var1 - var10.x;
            float var8 = var3 - var10.z;

            for (int var9 = 0; var9 < 4; var9++) {
               if ((var10.pinned & 1 << var9) != 0 && Math.abs(var13 - (var9 & 1)) < 0.001F && Math.abs(var8 - (var9 >> 1)) < 0.001F) {
                  var11 = 249;
                  break;
               }
            }
         }

         return var0 & 16777215 | var11 << 24;
      } else {
         float var5 = Math.clamp(var2 - var4.blockY, 0.0F, 1.0F);
         int var6;
         if (var4.height >= 0.0F) {
            float var7 = var4.height + var5;
            var6 = var4.kind == 4 ? 100 + Math.clamp(Math.round(var7 * 2.0F), 0, 49) : 2 + Math.clamp(Math.round(var7 * 4.0F), 0, 97);
         } else if (var4.kind == 1) {
            var6 = 254;
         } else if (var4.kind == 2) {
            var6 = 253;
         } else {
            float var12 = var4.upper ? 1.0F + var5 * 0.8F : var5;
            if (var12 < 0.001F) {
               return var0;
            }

            var6 = 150 + Math.clamp(Math.round(var12 * 50.0F), 0, 90);
         }

         return var0 & 16777215 | var6 << 24;
      }
   }

   public static boolean U(BlockGetter var0, BlockPos var1, FluidState var2) {
      if (!T) {
         return false;
      }

      C.remove();
      if (!k.K() && var2.is(FluidTags.WATER)) {
         short var3;
         if (hr.p(var1)) {
            var3 = 150;
         } else if (!var2.isSource() && var2.hasProperty(FlowingFluid.FALLING) && (Boolean)var2.getValue(FlowingFluid.FALLING)) {
            var3 = 160;
         } else {
            Vec3 var4 = var2.getFlow(var0, var1);
            var3 = (short)(var4.x == 0.0 && var4.z == 0.0 ? 224 : 192 + ((int)Math.round(Math.atan2(var4.z, var4.x) * 32.0 / (Math.PI * 2)) & 31));
         }

         C.set(Integer.valueOf(var3));
         return true;
      } else {
         return true;
      }
   }

   public static int o(int var0) {
      if (!T) {
         return var0;
      }

      Integer var1 = C.get();
      return var1 == null ? var0 : var0 & 16777215 | var1 << 24;
   }

   public static void k() {
      C.remove();
   }

   private static int B(BlockState var0) {
      String var1 = BuiltInRegistries.BLOCK.getKey(var0.getBlock()).getPath();
      if (var1.endsWith("_leaves")) {
         return 1;
      } else if (var1.equals("vine")) {
         return 2;
      } else if ((var1.endsWith("_log") || var1.endsWith("_stem")) && !var1.startsWith("stripped_")) {
         return 4;
      } else {
         return !y.contains(var1)
               && !var1.endsWith("_sapling")
               && !var0.is(BlockTags.FLOWERS)
               && !var0.is(BlockTags.CROPS)
               && !(var0.getBlock() instanceof CropBlock)
            ? 0
            : 3;
      }
   }

   private static boolean K(BlockState var0) {
      return B(var0) == 1 && (!var0.hasProperty(BlockStateProperties.PERSISTENT) || !(Boolean)var0.getValue(BlockStateProperties.PERSISTENT));
   }

   private static float B(BlockState var0, int var1, BlockGetter var2, BlockPos var3) {
      if (var1 == 1 && !K(var0)) {
         return -1.0F;
      }

      MutableBlockPos var4 = var3.mutable();
      int var5 = 0;
      if (var1 == 1) {
         byte var6 = 0;

         while (var5 < 24) {
            var4.move(0, -1, 0);
            BlockState var7 = var2.getBlockState(var4);
            if (B(var7) == 4) {
               var5++;
               var6 = 1;
               break;
            }

            if (!var7.isAir() && B(var7) != 1) {
               return -1.0F;
            }

            var5++;
         }

         if (var6 == 0) {
            return -1.0F;
         }
      } else {
         MutableBlockPos var12 = var3.mutable();

         for (int var14 = 0; var14 < 24 && B(var2.getBlockState(var12.above())) == 4; var14++) {
            var12.move(0, 1, 0);
         }

         byte var15 = 0;

         for (Direction var11 : Direction.values()) {
            if (var11 != Direction.DOWN && K(var2.getBlockState(var12.relative(var11)))) {
               var15 = 1;
               break;
            }
         }

         if (var15 == 0) {
            return -1.0F;
         }
      }

      int var13;
      for (var13 = 0; var13 < 24 && B(var2.getBlockState(var4.below())) == 4; var13++) {
         var4.move(0, -1, 0);
      }

      return var5 + var13;
   }

   private em() {
   }

   private record n(int kind, float blockY, float height, boolean upper) {
   }

   private record w(float x, float z, float top, int pinned) {
   }
}

package com.larpclient;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class cp {
   public static boolean x(BlockState var0) {
      return var0.is(Blocks.KELP)
         || var0.is(Blocks.KELP_PLANT)
         || var0.is(Blocks.BAMBOO)
         || var0.is(Blocks.SWEET_BERRY_BUSH)
         || var0.is(Blocks.VINE)
         || var0.is(Blocks.POINTED_DRIPSTONE);
   }

   public static cp.z H(BlockState var0, BlockPos var1, Function<BlockPos, BlockState> var2) {
      if (!var0.is(Blocks.KELP) && !var0.is(Blocks.KELP_PLANT)) {
         if (var0.is(Blocks.BAMBOO)) {
            if (((BlockState)var2.apply(var1.below())).is(Blocks.BAMBOO)) {
               return null;
            } else {
               return I(var1, Direction.UP, var2, Blocks.BAMBOO) >= 12 ? cp.z.D : null;
            }
         } else {
            if (var0.is(Blocks.SWEET_BERRY_BUSH)) {
               return var0.getValue(BlockStateProperties.AGE_3) == 3 ? cp.z.L : null;
            }

            if (var0.is(Blocks.VINE)) {
               if (((BlockState)var2.apply(var1.above())).is(Blocks.VINE)) {
                  return null;
               } else {
                  return I(var1, Direction.DOWN, var2, Blocks.VINE) >= 7 ? cp.z.k : null;
               }
            } else if (var0.is(Blocks.POINTED_DRIPSTONE) && var0.getValue(BlockStateProperties.VERTICAL_DIRECTION) == Direction.DOWN) {
               BlockState var4 = (BlockState)var2.apply(var1.above());
               if (var4.is(Blocks.POINTED_DRIPSTONE) && var4.getValue(BlockStateProperties.VERTICAL_DIRECTION) == Direction.DOWN) {
                  return null;
               } else {
                  return I(
                           var1,
                           Direction.DOWN,
                           var1x -> {
                              BlockState var2x = (BlockState)var2.apply(var1x);
                              return var2x.is(Blocks.POINTED_DRIPSTONE) && var2x.getValue(BlockStateProperties.VERTICAL_DIRECTION) != Direction.DOWN
                                 ? Blocks.AIR.defaultBlockState()
                                 : var2x;
                           },
                           Blocks.POINTED_DRIPSTONE
                        )
                        >= 5
                     ? cp.z.o
                     : null;
               }
            } else {
               return null;
            }
         }
      } else {
         if (N((BlockState)var2.apply(var1.above()))) {
            return null;
         }

         int var3 = I(var1, Direction.DOWN, var2, Blocks.KELP, Blocks.KELP_PLANT);
         return var3 < 14 && (!var0.is(Blocks.KELP) || var0.getValue(BlockStateProperties.AGE_25) != 25 || var3 < 8) ? null : cp.z.P;
      }
   }

   private static boolean N(BlockState var0) {
      return var0.is(Blocks.KELP) || var0.is(Blocks.KELP_PLANT);
   }

   public static int I(BlockPos var0, Direction var1, Function<BlockPos, BlockState> var2, Block... var3) {
      int var4 = 1;
      MutableBlockPos var5 = var0.mutable();

      while (var4 < 40) {
         var5.move(var1);
         BlockState var6 = (BlockState)var2.apply(var5);
         byte var7 = 0;

         for (Block var11 : var3) {
            if (var6.is(var11)) {
               var7 = 1;
               break;
            }
         }

         if (var7 == 0) {
            break;
         }

         var4++;
      }

      return var4;
   }

   public static boolean i(BlockState var0, int var1, BlockPos var2, Function<BlockPos, BlockState> var3) {
      if (var1 == 5 && (var0.isAir() || var0.is(Blocks.AMETHYST_CLUSTER))) {
         for (int var4 = -1; var4 <= 1; var4++) {
            for (int var5 = -1; var5 <= 1; var5++) {
               for (int var6 = -1; var6 <= 1; var6++) {
                  if (var4 != 0 || var5 != 0 || var6 != 0) {
                     BlockState var7 = (BlockState)var3.apply(var2.offset(var4, var5, var6));
                     if (var7.is(Blocks.AMETHYST_BLOCK) || var7.is(Blocks.BUDDING_AMETHYST)) {
                        return true;
                     }
                  }
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private cp() {
   }

   public enum z {
      P,
      D,
      L,
      k,
      o;
   }
}

package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e.qd2632e42fbe92036;
import com.larpclient.module.Module;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.DaylightDetectorBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RedstoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class qf {
   static final List<Property<?>> v = List.of(
      BlockStateProperties.FACING,
      BlockStateProperties.HORIZONTAL_FACING,
      BlockStateProperties.FACING_HOPPER,
      BlockStateProperties.AXIS,
      BlockStateProperties.HORIZONTAL_AXIS,
      BlockStateProperties.HALF,
      BlockStateProperties.SLAB_TYPE,
      BlockStateProperties.ATTACH_FACE,
      BlockStateProperties.VERTICAL_DIRECTION,
      BlockStateProperties.ORIENTATION,
      BlockStateProperties.DOOR_HINGE
   );
   static final List<IntegerProperty> t = List.of(
      BlockStateProperties.CANDLES,
      BlockStateProperties.PICKLES,
      BlockStateProperties.EGGS,
      BlockStateProperties.LAYERS,
      BlockStateProperties.FLOWER_AMOUNT,
      BlockStateProperties.SEGMENT_AMOUNT
   );
   static final float Y = 3.0F;
   static final float x = 9.0F;
   static final float r = 10.0F;
   static final Direction[] U = new Direction[]{Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP};
   static final double L = 0.03;

   private qf() {
   }

   public static BlockState f(String var0) {
      Identifier var1 = Identifier.tryParse(o4.D(var0));
      if (var1 == null) {
         return null;
      }

      Optional var2 = BuiltInRegistries.BLOCK.getOptional(var1);
      if (var2.isEmpty()) {
         return null;
      }

      BlockState var3 = ((Block)var2.get()).defaultBlockState();
      StateDefinition var4 = ((Block)var2.get()).getStateDefinition();

      for (Entry var6 : o4.y(var0).entrySet()) {
         Property var7 = var4.getProperty((String)var6.getKey());
         if (var7 != null) {
            var3 = o(var3, var7, (String)var6.getValue());
         }
      }

      return var3;
   }

   private static <T extends Comparable<T>> BlockState o(BlockState var0, Property<T> var1, String var2) {
      return var1.getValue(var2).map(var2x -> (BlockState)var0.setValue(var1, var2x)).orElse(var0);
   }

   public static BlockState r(BlockState var0, o4.h var1) {
      BlockState var2 = switch (var1.mirror()) {
         case e -> var0.mirror(Mirror.FRONT_BACK);
         case n -> var0.mirror(Mirror.LEFT_RIGHT);
         case W -> var0;
      };
      return var2.rotate(Q(var1.rotation()));
   }

   public static Rotation Q(int var0) {
      return switch (Math.floorMod(var0, 360)) {
         case 90 -> Rotation.CLOCKWISE_90;
         case 180 -> Rotation.CLOCKWISE_180;
         case 270 -> Rotation.COUNTERCLOCKWISE_90;
         default -> Rotation.NONE;
      };
   }

   public static BlockItem h(BlockState var0) {
      return !(var0.getBlock().asItem() instanceof BlockItem var1 && (var1.getBlock() == var0.getBlock() || var1 instanceof StandingAndWallBlockItem))
         ? null
         : var1;
   }

   public static boolean s(BlockState var0) {
      return var0.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) && var0.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER
         || var0.hasProperty(BlockStateProperties.BED_PART) && var0.getValue(BlockStateProperties.BED_PART) == BedPart.HEAD;
   }

   public static BlockPos p(BlockState var0, BlockPos var1) {
      if (var0.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
         return var0.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? var1.above() : var1.below();
      } else if (var0.hasProperty(BlockStateProperties.BED_PART) && var0.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
         Direction var2 = (Direction)var0.getValue(BlockStateProperties.HORIZONTAL_FACING);
         return var0.getValue(BlockStateProperties.BED_PART) == BedPart.FOOT ? var1.relative(var2) : var1.relative(var2.getOpposite());
      } else {
         return null;
      }
   }

   public static boolean n(BlockState var0) {
      return var0.getBlock() instanceof FallingBlock;
   }

   public static String N(BlockState var0) {
      if (var0.getBlock() instanceof LiquidBlock) {
         return "fluids are not placed";
      } else {
         return h(var0) == null ? "no item places it directly" : null;
      }
   }

   public static boolean W(BlockState var0) {
      if (var0.hasProperty(BlockStateProperties.SLAB_TYPE)) {
         return var0.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE;
      }

      for (IntegerProperty var2 : t) {
         if (var0.hasProperty(var2)) {
            return (Integer)var0.getValue(var2) > 1;
         }
      }

      return false;
   }

   public static int I(BlockState var0, BlockState var1) {
      int var2 = var1 != null && var1.getBlock() == var0.getBlock() ? 1 : 0;
      if (var0.hasProperty(BlockStateProperties.SLAB_TYPE) && var0.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) {
         return var2 != 0 ? (var1.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE ? 0 : 1) : 2;
      }

      for (IntegerProperty var4 : t) {
         if (var0.hasProperty(var4)) {
            return Math.max(0, (Integer)var0.getValue(var4) - (var2 != 0 ? (Integer)var1.getValue(var4) : 0));
         }
      }

      return var2 != 0 ? 0 : 1;
   }

   public static List<Property<?>> Y(BlockState var0) {
      Block var1 = var0.getBlock();
      if (var1 instanceof RepeaterBlock) {
         return List.of(BlockStateProperties.DELAY);
      } else if (var1 instanceof ComparatorBlock) {
         return List.of(BlockStateProperties.MODE_COMPARATOR);
      } else if (var1 instanceof NoteBlock) {
         return List.of(BlockStateProperties.NOTE);
      } else if (var1 instanceof DaylightDetectorBlock) {
         return List.of(BlockStateProperties.INVERTED);
      } else if (var1 instanceof LeverBlock) {
         return List.of(BlockStateProperties.POWERED);
      } else {
         return !(var1 instanceof DoorBlock) && !(var1 instanceof TrapDoorBlock) && !(var1 instanceof FenceGateBlock)
               || var1 == Blocks.IRON_DOOR
               || var1 == Blocks.IRON_TRAPDOOR
               || var0.hasProperty(BlockStateProperties.POWERED) && var0.getValue(BlockStateProperties.POWERED)
            ? List.of()
            : List.of(BlockStateProperties.OPEN);
      }
   }

   public static int n(BlockState var0, BlockState var1) {
      if (var1 != null && var1.getBlock() == var0.getBlock() && O(var0, var1) && !e(var0, var1)) {
         for (IntegerProperty var3 : t) {
            if (var0.hasProperty(var3) && !((Integer)var0.getValue(var3)).equals(var1.getValue(var3))) {
               return -1;
            }
         }

         if (var0.hasProperty(BlockStateProperties.SLAB_TYPE) && var0.getValue(BlockStateProperties.SLAB_TYPE) != var1.getValue(BlockStateProperties.SLAB_TYPE)
            )
          {
            return -1;
         }

         int var5 = 0;

         for (Property var4 : Y(var0)) {
            var5 += g(var4, var1, var0);
         }

         return var5;
      } else {
         return -1;
      }
   }

   private static <T extends Comparable<T>> int g(Property<T> var0, BlockState var1, BlockState var2) {
      List var3 = List.copyOf(var0.getPossibleValues());
      int var4 = var3.indexOf(var1.getValue(var0));
      int var5 = var3.indexOf(var2.getValue(var0));
      return var4 >= 0 && var5 >= 0 ? Math.floorMod(var5 - var4, var3.size()) : 0;
   }

   private static boolean O(BlockState var0, BlockState var1) {
      for (Property var3 : v) {
         if (var0.hasProperty(var3)
            && var1.hasProperty(var3)
            && (var3 != BlockStateProperties.SLAB_TYPE || var0.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE)
            && !var0.getValue(var3).equals(var1.getValue(var3))) {
            return false;
         }
      }

      return true;
   }

   private static boolean e(BlockState var0, BlockState var1) {
      if (var0.hasProperty(BlockStateProperties.SLAB_TYPE) && var0.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) {
         return var1.getValue(BlockStateProperties.SLAB_TYPE) != SlabType.DOUBLE;
      }

      for (IntegerProperty var3 : t) {
         if (var0.hasProperty(var3)) {
            return (Integer)var1.getValue(var3) < (Integer)var0.getValue(var3);
         }
      }

      return false;
   }

   public static boolean s(BlockState var0, BlockState var1) {
      if (var1 != null && var1.getBlock() == var0.getBlock()) {
         for (Property var3 : v) {
            if (var0.hasProperty(var3) && var1.hasProperty(var3) && !var0.getValue(var3).equals(var1.getValue(var3))) {
               return false;
            }
         }

         for (IntegerProperty var6 : t) {
            if (var0.hasProperty(var6) && !((Integer)var0.getValue(var6)).equals(var1.getValue(var6))) {
               return false;
            }
         }

         for (Property var7 : Y(var0)) {
            if (!var0.getValue(var7).equals(var1.getValue(var7))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static qf.s j(BlockState var0, BlockState var1) {
      if (var1 == null) {
         return qf.s.x;
      }

      if (var1.getBlock() == var0.getBlock()) {
         if (s(var0, var1)) {
            return qf.s.j;
         } else {
            return O(var0, var1) && e(var0, var1) ? qf.s.N : qf.s.X;
         }
      } else {
         return !var1.isAir() && !var1.canBeReplaced() ? qf.s.I : qf.s.N;
      }
   }

   public static boolean D(BlockState var0, BlockState var1, BlockState var2) {
      if (var2 != null && var2.getBlock() == var0.getBlock() && O(var0, var2)) {
         int var3 = var1 != null && var1.getBlock() == var0.getBlock() ? 1 : 0;
         if (var0.hasProperty(BlockStateProperties.SLAB_TYPE) && var0.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE) {
            return (var2.getValue(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE ? 1 : 0) == var3;
         }

         for (IntegerProperty var5 : t) {
            if (var0.hasProperty(var5)) {
               return (Integer)var2.getValue(var5) == (var3 != 0 ? (Integer)var1.getValue(var5) : 0) + 1
                  && (Integer)var2.getValue(var5) <= (Integer)var0.getValue(var5);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean v(BlockState var0) {
      for (Property var2 : v) {
         if (var0.hasProperty(var2)) {
            return true;
         }
      }

      return false;
   }

   public static BlockState Q(Player var0, ItemStack var1, BlockHitResult var2, float var3, float var4) {
      if (var1.getItem() instanceof BlockItem var5) {
         qf.r var8 = new qf.r(var0, var1, var2, var3, var4);
         if (!var8.canPlace()) {
            return null;
         }

         if (var5.updatePlacementContext(var8) != var8) {
            return null;
         }

         BlockState var7 = var5 instanceof StandingAndWallBlockItem
            ? ((qd2632e42fbe92036)var5).larp$placementState(var8)
            : var5.getBlock().getStateForPlacement(var8);
         return var7 != null && var7.canSurvive(var0.level(), var8.getClickedPos()) ? var7 : null;
      } else {
         return null;
      }
   }

   public static boolean T(Player var0, ItemStack var1, BlockHitResult var2, float var3, float var4, BlockState var5, BlockState var6, BlockPos var7) {
      if (!new qf.r(var0, var1, var2, var3, var4).getClickedPos().equals(var7)) {
         return false;
      }

      boolean var8 = v(var5);
      float var9 = q(var5) ? 9.0F : 3.0F;
      float[][] var10 = var8 ? new float[][]{{0.0F, 0.0F}, {var9, 0.0F}, {-var9, 0.0F}, {0.0F, R(var5)}, {0.0F, -R(var5)}} : new float[][]{{0.0F, 0.0F}};

      for (float[] var14 : var10) {
         BlockState var15 = Q(var0, var1, var2, var3 + var14[0], Mth.clamp(var4 + var14[1], -90.0F, 90.0F));
         if (!D(var5, var6, var15)) {
            return false;
         }
      }

      return true;
   }

   private static float R(BlockState var0) {
      if (var0.hasProperty(BlockStateProperties.FACING) && ((Direction)var0.getValue(BlockStateProperties.FACING)).getAxis().isVertical()) {
         return !q(var0) && !(var0.getBlock() instanceof ObserverBlock) ? 3.0F : 10.0F;
      } else {
         return 3.0F;
      }
   }

   public static boolean q(BlockState var0) {
      return var0.getBlock() instanceof DispenserBlock || var0.getBlock() instanceof PistonBaseBlock;
   }

   public static boolean Y(BlockState var0) {
      if (!var0.hasProperty(BlockStateProperties.FACING)) {
         return false;
      }

      Direction var1 = (Direction)var0.getValue(BlockStateProperties.FACING);
      return var0.getBlock() instanceof ObserverBlock ? var1 == Direction.UP : q(var0) && var1 == Direction.DOWN;
   }

   public static String j(Module var0, Player var1, ItemStack var2, BlockPos var3, BlockState var4, Vec3 var5, double var6) {
      BlockState var8 = var1.level().getBlockState(var3);
      byte var9 = 0;
      byte var10 = 0;
      byte var11 = 0;
      byte var12 = 0;

      for (Direction var16 : U) {
         BlockPos var17 = var3.relative(var16);
         if (x(var1, var17)) {
            var9 = 1;
            Direction var18 = var16.getOpposite();
            Vec3 var19 = new Vec3(var18.getStepX(), var18.getStepY(), var18.getStepZ());

            for (Vec3 var21 : vi.O(var17, var18)) {
               if (!(var5.subtract(var21).dot(var19) <= 0.02)) {
                  var10 = 1;
                  if (!(var5.distanceToSqr(var21) > var6 * var6)) {
                     var11 = 1;
                     BlockHitResult var22 = vi.y(var0, var5, var17, var21.subtract(var5), var6);
                     if (var22 != null && var22.getDirection() == var18) {
                        var12 = 1;
                        float[] var23 = b6.R(var5, var22.getLocation());
                        if (T(var1, var2, var22, var23[0], var23[1], var4, var8, var3)) {
                           return null;
                        }
                     }
                  }
               }
            }
         }
      }

      return var9 == 0 ? "no support" : (var10 == 0 ? "face turned away" : (var11 == 0 ? "out of reach" : (var12 == 0 ? "view blocked" : "wrong direction")));
   }

   public static BlockHitResult E(Module var0, Player var1, ItemStack var2, BlockPos var3, BlockState var4, Vec3 var5, double var6, boolean var8) {
      return L(var0, var1, var2, var3, var4, var5, var6, var8, false);
   }

   public static BlockHitResult L(Module var0, Player var1, ItemStack var2, BlockPos var3, BlockState var4, Vec3 var5, double var6, boolean var8, boolean var9) {
      BlockState var10 = var1.level().getBlockState(var3);
      int var11 = var10.getBlock() == var4.getBlock() ? 1 : 0;

      for (int var12 = 0; var12 < 2; var12++) {
         for (int var13 = 0; var13 < U.length * (var11 != 0 ? 2 : 1); var13++) {
            int var14 = var13 >= U.length ? 1 : 0;
            Direction var15 = U[var13 % U.length];
            BlockPos var16 = var14 != 0 ? var3 : var3.relative(var15);
            if ((var14 != 0 ? var12 != 1 : g(var1.level().getBlockState(var16)) == (var12 == 1 ? 1 : 0))
               && (var14 != 0 ? !var10.getShape(var1.level(), var3).isEmpty() && !var10.hasBlockEntity() : x(var1, var16))) {
               Direction var17 = var14 != 0 ? var15 : var15.getOpposite();
               Vec3 var18 = new Vec3(var17.getStepX(), var17.getStepY(), var17.getStepZ());
               List var19 = vi.O(var16, var17);

               for (int var20 = 0; var20 < var19.size(); var20++) {
                  Vec3 var21 = (Vec3)var19.get(var20);
                  if (!(var5.subtract(var21).dot(var18) <= 0.02) && !(var5.distanceToSqr(var21) > var6 * var6)) {
                     BlockHitResult var22;
                     if (var8) {
                        var22 = vi.y(var0, var5, var16, var21.subtract(var5), var6);
                        if (var22 == null || var22.getDirection() != var17) {
                           continue;
                        }
                     } else {
                        var22 = new BlockHitResult(var21, var17, var16, false);
                     }

                     float[] var23 = b6.R(var5, var22.getLocation());
                     if (T(var1, var2, var22, var23[0], var23[1], var4, var10, var3)) {
                        return var22;
                     }

                     if ((!var8 || var9) && !v(var4)) {
                        break;
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   public static boolean H(BlockState var0) {
      Block var1 = var0.getBlock();
      return var1 instanceof DoorBlock
         || var1 instanceof TrapDoorBlock
         || var1 instanceof FenceGateBlock
         || var1 instanceof LeverBlock
         || var1 instanceof ButtonBlock
         || var1 instanceof DiodeBlock
         || var1 instanceof NoteBlock
         || var1 instanceof DaylightDetectorBlock
         || var1 instanceof BedBlock
         || var1 instanceof CakeBlock
         || var1 instanceof RedstoneWireBlock;
   }

   static boolean x(Player var0, BlockPos var1) {
      BlockState var2 = var0.level().getBlockState(var1);
      if (!g(var2)) {
         return !vi.k(var1) ? false : !var2.getCollisionShape(var0.level(), var1).isEmpty();
      } else {
         return var0.level().hasChunkAt(var1) && !var2.canBeReplaced() && !var2.getCollisionShape(var0.level(), var1).isEmpty();
      }
   }

   public static boolean g(BlockState var0) {
      return var0.hasBlockEntity() || H(var0) || var0.getBlock() instanceof RespawnAnchorBlock || var0.getBlock() instanceof CraftingTableBlock;
   }

   public static boolean J(Direction var0, double var1, double var3, double var5, double var7) {
      return (boolean)(switch (var0) {
         case UP -> var5 > var1 + 0.03 ? 1 : 0;
         case DOWN -> var5 < var1 - 0.03 ? 1 : 0;
         case EAST -> var3 > var1 + 0.03 ? 1 : 0;
         case WEST -> var3 < var1 - 0.03 ? 1 : 0;
         case SOUTH -> var7 > var1 + 0.03 ? 1 : 0;
         case NORTH -> var7 < var1 - 0.03 ? 1 : 0;
         default -> throw new MatchException(null, null);
      });
   }

   public static boolean F(Player var0, BlockHitResult var1) {
      Direction var2 = var1.getDirection();
      Vec3 var3 = var1.getLocation();

      double var4 = switch (var2.getAxis()) {
         case X -> var3.x;
         case Y -> var3.y;
         case Z -> var3.z;
         default -> throw new MatchException(null, null);
      };
      q71cffb54eb30bf94 var6 = (q71cffb54eb30bf94)var0;
      double var7 = var0.getEyeHeight();
      return J(var2, var4, var0.getX(), var0.getY() + var7, var0.getZ()) && J(var2, var4, var6.larpXLast(), var6.larpYLast() + var7, var6.larpZLast());
   }

   public static boolean i(BlockPos var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;

      for (Direction var5 : Direction.values()) {
         if (x(var1, var0.relative(var5))) {
            return true;
         }
      }

      return false;
   }

   public static BlockHitResult k(Module var0, BlockPos var1, Vec3 var2, double var3, BlockState var5) {
      for (Direction var9 : U) {
         Direction var10 = var9.getOpposite();
         Vec3 var11 = new Vec3(var10.getStepX(), var10.getStepY(), var10.getStepZ());

         for (Vec3 var13 : vi.O(var1, var10)) {
            if (!(var2.subtract(var13).dot(var11) <= 0.02) && !(var2.distanceToSqr(var13) > var3 * var3)) {
               BlockHitResult var14 = vi.y(var0, var2, var1, var13.subtract(var2), var3);
               if (var14 != null && var14.getDirection() == var10 && R(var5, b6.R(var2, var14.getLocation())[0])) {
                  return var14;
               }
            }
         }
      }

      return null;
   }

   public static boolean R(BlockState var0, float var1) {
      return var0 != null && var0.getBlock() instanceof FenceGateBlock && var0.getValue(BlockStateProperties.OPEN)
         ? Direction.fromYRot(var1) != ((Direction)var0.getValue(BlockStateProperties.HORIZONTAL_FACING)).getOpposite()
         : true;
   }

   static Direction[] f(float var0, float var1) {
      float var2 = var1 * (float) (Math.PI / 180.0);
      float var3 = -var0 * (float) (Math.PI / 180.0);
      float var4 = Mth.sin(var2);
      float var5 = Mth.cos(var2);
      float var6 = Mth.sin(var3);
      float var7 = Mth.cos(var3);
      int var8 = var6 > 0.0F ? 1 : 0;
      int var9 = var4 < 0.0F ? 1 : 0;
      int var10 = var7 > 0.0F ? 1 : 0;
      float var11 = var8 != 0 ? var6 : -var6;
      float var12 = var9 != 0 ? -var4 : var4;
      float var13 = var10 != 0 ? var7 : -var7;
      float var14 = var11 * var5;
      float var15 = var13 * var5;
      Direction var16 = var8 != 0 ? Direction.EAST : Direction.WEST;
      Direction var17 = var9 != 0 ? Direction.UP : Direction.DOWN;
      Direction var18 = var10 != 0 ? Direction.SOUTH : Direction.NORTH;
      if (var11 > var13) {
         if (var12 > var14) {
            return Q(var17, var16, var18);
         } else {
            return var15 > var12 ? Q(var16, var18, var17) : Q(var16, var17, var18);
         }
      } else if (var12 > var15) {
         return Q(var17, var18, var16);
      } else {
         return var14 > var12 ? Q(var18, var16, var17) : Q(var18, var17, var16);
      }
   }

   private static Direction[] Q(Direction var0, Direction var1, Direction var2) {
      return new Direction[]{var0, var1, var2, var2.getOpposite(), var1.getOpposite(), var0.getOpposite()};
   }

   static final class r extends BlockPlaceContext {
      private final float C;
      private final float h;

      r(Player var1, ItemStack var2, BlockHitResult var3, float var4, float var5) {
         super(var1, InteractionHand.MAIN_HAND, var2, var3);
         this.C = var4;
         this.h = var5;
      }

      public Direction getHorizontalDirection() {
         return Direction.fromYRot(this.C);
      }

      public float getRotation() {
         return this.C;
      }

      public Direction getNearestLookingDirection() {
         return qf.f(this.C, this.h)[0];
      }

      public Direction getNearestLookingVerticalDirection() {
         return this.h < 0.0F ? Direction.UP : Direction.DOWN;
      }

      public Direction[] getNearestLookingDirections() {
         Direction[] var1 = qf.f(this.C, this.h);
         if (this.replaceClicked) {
            return var1;
         }

         Direction var2 = this.getClickedFace();
         int var3 = 0;

         while (var3 < var1.length && var1[var3] != var2.getOpposite()) {
            var3++;
         }

         if (var3 > 0 && var3 < var1.length) {
            System.arraycopy(var1, 0, var1, 1, var3);
            var1[0] = var2.getOpposite();
         }

         return var1;
      }
   }

   public enum s {
      N,
      I,
      X,
      j,
      x;
   }
}

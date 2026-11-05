package com.larpclient;

import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class yx {
   private static s w;
   private static BlockPos A;
   private static Object P;
   private static Object c;
   private static int E = -1;
   private static int i = -1;
   private static int b;
   private static ItemStack p = ItemStack.EMPTY;
   private static boolean j;

   public static boolean w(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.level != null
         && var1.player != null
         && var1.level.hasChunkAt(var0)
         && !var1.level.isOutsideBuildHeight(var0)
         && var1.level.getWorldBorder().isWithinBounds(var0)) {
         BlockState var2 = var1.level.getBlockState(var0);
         return !var2.isAir() && var2.getFluidState().isEmpty() && var2.getDestroySpeed(var1.level, var0) >= 0.0F;
      } else {
         return false;
      }
   }

   public static BlockHitResult E(BlockPos var0, double var1) {
      return C(null, var0, var1);
   }

   static BlockHitResult C(s var0, BlockPos var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (!d8.r(var0)) {
         return null;
      }

      Vec3 var5 = var4.player.getEyePosition();
      double var6 = Math.min(var2, var4.player.blockInteractionRange());
      VoxelShape var8 = var4.level.getBlockState(var1).getShape(var4.level, var1);
      ArrayList var9 = new ArrayList();
      if (!var8.isEmpty()) {
         var9.add(var8.bounds().getCenter().add(var1.getX(), var1.getY(), var1.getZ()));
      }

      for (Direction var13 : Direction.values()) {
         var9.addAll(m.S(var1, var13));
      }

      for (Vec3 var15 : var9) {
         Vec3 var16 = var15.subtract(var5);
         if (!(var16.lengthSqr() > var6 * var6)) {
            BlockHitResult var17 = var4.level.clip(new ClipContext(var5, var5.add(var16.normalize().scale(var6)), Block.OUTLINE, Fluid.NONE, var4.player));
            if (var17.getType() == Type.BLOCK && var17.getBlockPos().equals(var1)) {
               return var17;
            }
         }
      }

      return null;
   }

   static boolean R(BlockPos var0, Vec3 var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      Vec3 var5 = var4.player.getEyePosition();
      Vec3 var6 = var1.subtract(var5);
      double var7 = Math.min(var2, var4.player.blockInteractionRange());
      if (!(var6.lengthSqr() > var7 * var7) && !(var6.lengthSqr() < 1.0E-8)) {
         BlockHitResult var9 = var4.level.clip(new ClipContext(var5, var5.add(var6.normalize().scale(var7)), Block.OUTLINE, Fluid.NONE, var4.player));
         return var9.getType() == Type.BLOCK && var9.getBlockPos().equals(var0);
      } else {
         return false;
      }
   }

   static BlockHitResult B(BlockPos var0, double var1) {
      Minecraft var3 = Minecraft.getInstance();
      Vec3 var4 = var3.player.getEyePosition();
      BlockHitResult var5 = var3.level
         .clip(
            new ClipContext(
               var4, var4.add(var3.player.getLookAngle().scale(Math.min(var1, var3.player.blockInteractionRange()))), Block.OUTLINE, Fluid.NONE, var3.player
            )
         );
      return var5.getType() == Type.BLOCK && var5.getBlockPos().equals(var0) ? var5 : null;
   }

   static boolean Y(s var0, BlockPos var1, double var2, boolean var4, float var5, boolean var6, BooleanSupplier var7) {
      if (var0.G() && w(var1)) {
         Minecraft var8 = Minecraft.getInstance();
         BlockPos var9 = var1.immutable();
         ClientLevel var10 = var8.level;
         LocalPlayer var11 = var8.player;
         int var12 = var8.player.tickCount;
         BlockState var13 = var8.level.getBlockState(var9);
         BooleanSupplier var14 = () -> var0.G()
            && var7.getAsBoolean()
            && m.X(var10, var8.level, var11, var8.player, var12, var8.player == null ? -10 : var8.player.tickCount)
            && w(var9)
            && var8.level.getBlockState(var9).equals(var13)
            && var8.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var9)) <= var2 * var2;
         BlockHitResult var15 = var4 ? C(var0, var9, var2) : null;
         if (var4 && var15 == null) {
            H(var0);
            return false;
         }

         if (var4 && d8.m(var0)) {
            var0.B(var15.getLocation(), var5);
         }

         d8.A(var0, () -> {
            if (var14.getAsBoolean()) {
               BlockHitResult var9x = var4 ? B(var9, var2) : null;
               if (!var4 || var9x != null) {
                  if (w != null && w != var0) {
                     H(w);
                  }

                  if (w != null && var8.player.getInventory().getSelectedSlot() != i) {
                     H(var0);
                  } else {
                     if (w == null) {
                        w = var0;
                        P = var8.level;
                        c = var8.player;
                        E = var8.player.getInventory().getSelectedSlot();
                     }

                     if (!var9.equals(A)) {
                        if (A != null) {
                           r4.g(var0.getName(), "Next block {}, {}, {}", var9.getX(), var9.getY(), var9.getZ());
                        }

                        A = var9;
                        i = var6 ? w(var13) : var8.player.getInventory().getSelectedSlot();
                        p = var8.player.getInventory().getItem(i);
                     }

                     if (var6) {
                        var8.player.getInventory().setSelectedSlot(i);
                     }

                     b = var8.player.tickCount;
                     Direction var10x = var9x == null ? Direction.UP : var9x.getDirection();
                     if (var8.gameMode.continueDestroyBlock(var9, var10x)) {
                        var8.level.addBreakingBlockEffects(var9, var10x, true);
                        hp.o(var8.player, InteractionHand.MAIN_HAND);
                     }

                     var0.I("Mining " + var9.getX() + ", " + var9.getY() + ", " + var9.getZ());
                  }
               }
            }
         }, () -> var14.getAsBoolean() && (!var4 || B(var9, var2) != null));
         return true;
      } else {
         H(var0);
         return false;
      }
   }

   private static int w(BlockState var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = var1.getSelectedSlot();
      float var3 = var1.getItem(var2).getDestroySpeed(var0) + (var1.getItem(var2).isCorrectToolForDrops(var0) ? 1000 : 0);

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getItem(var4);
         float var6 = var5.getDestroySpeed(var0);
         if (var5.isCorrectToolForDrops(var0)) {
            var6 += 1000.0F;
         }

         if (var6 > var3) {
            var3 = var6;
            var2 = var4;
         }
      }

      return var2;
   }

   public static boolean C() {
      Minecraft var0 = Minecraft.getInstance();
      return !j
         && w != null
         && w.G()
         && var0.level == P
         && var0.player == c
         && var0.player.tickCount - b <= 1
         && w(A)
         && var0.player.getInventory().getSelectedSlot() == i;
   }

   public static void H(Module var0) {
      if (w == var0) {
         Minecraft var1 = Minecraft.getInstance();

         try {
            j = (boolean)1;
            if (var1.level == P && var1.player == c && var1.gameMode != null) {
               if (!var1.options.keyAttack.isDown()) {
                  var1.gameMode.stopDestroyBlock();
               }

               if (!var1.options.keyAttack.isDown()
                  && i != E
                  && var1.player.getInventory().getSelectedSlot() == i
                  && var1.player.getInventory().getItem(i) == p) {
                  var1.player.getInventory().setSelectedSlot(E);
               }
            }
         } finally {
            j = (boolean)0;
            w = null;
            A = null;
            P = null;
            c = null;
            E = -1;
            i = -1;
            p = ItemStack.EMPTY;
         }
      }
   }

   private yx() {
   }
}

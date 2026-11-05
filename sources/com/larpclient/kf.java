package com.larpclient;

import com.larpclient.module.dh;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class kf extends kl {
   private final dh F = this.Y("Legit", "Repeat normal crosshair placement at normal use speed", false);
   private final com.larpclient.module.d9 T = this.H("Range", "Distance along your view to search for a supported position", 4.5, 1.0, 6.0, 0.5);
   private final com.larpclient.module.d9 A = this.H("Delay", "Ticks between placement attempts", 4.0, 0.0, 20.0, 1.0);
   private int R;

   public kf() {
      super("Air Place", "Place held blocks along your crosshair using nearby support faces");
   }

   @Override
   public void onTick() {
      if (this.N()) {
         int var10000 = mc.options.keyUse.isDown();
         boolean var11 = false;
         byte var10 = (byte)var10000;
         var10000 = Integer.signum(var10);
         if ((var10000 | -var10000) >>> 31 != 0) {
            int var13 = this.R;
            int var12 = mc.player.tickCount;
            if (Integer.compare(var12, var13) >>> 31 == 0 && mc.player.getMainHandItem().getItem() instanceof BlockItem) {
               if (this.F.P()) {
                  if (mc.hitResult instanceof BlockHitResult var18 && var18.getType() == Type.BLOCK) {
                     BlockPos var22 = var18.getBlockPos().relative(var18.getDirection());
                     _d.a(
                        this, var22, mc.player.getMainHandItem().getItem(), this.T.P(), false, false, () -> this.N() && mc.options.keyUse.isDown(), var1x -> {
                           this.R = mc.player.tickCount + Math.max(4, this.A.P().intValue());
                           this.w(var1x ? "Placed" : "Placement rejected");
                        }
                     );
                  }

                  return;
               }

               BlockPos var1 = null;
               HitResult var3 = mc.hitResult;
               int var29 = var3 instanceof BlockHitResult;
               boolean var15 = false;
               byte var14 = (byte)var29;
               var29 = Integer.signum(var14);
               if (((var29 | -var29) >>> 31 ^ 1) == 0) {
                  BlockHitResult var2 = (BlockHitResult)var3;
                  if (var2.getType() == Type.BLOCK) {
                     BlockPos var23 = var2.getBlockPos().relative(var2.getDirection());
                     int var31 = _d.Z(var23);
                     boolean var17 = false;
                     byte var16 = (byte)var31;
                     var31 = Integer.signum(var16);
                     if (((var31 | -var31) >>> 31 ^ 1) == 0) {
                        var1 = var23;
                     }
                  }
               }

               if (var1 == null) {
                  Vec3 var19 = mc.player.getEyePosition();
                  Vec3 var24 = mc.player.getLookAngle();
                  BlockPos var4 = null;
                  double var5 = Math.min(this.T.P(), mc.player.blockInteractionRange());

                  for (double var7 = 0.5; var7 <= var5; var7 += 0.25) {
                     BlockPos var9 = BlockPos.containing(var19.add(var24.scale(var7)));
                     if (!var9.equals(var4)) {
                        var4 = var9;
                        if (this.W(var9) != null) {
                           var1 = var9;
                           break;
                        }
                     }
                  }
               }

               if (var1 == null) {
                  this.w("No supported position along view");
                  return;
               }

               BlockPos var20 = var1.immutable();
               Item var25 = mc.player.getMainHandItem().getItem();
               ClientLevel var26 = mc.level;
               LocalPlayer var27 = mc.player;
               int var6 = mc.player.tickCount;
               _m.F(
                  this,
                  () -> {
                     BlockHitResult var2x = this.W(var20);
                     if (var2x != null) {
                        boolean var3x = mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, var2x).consumesAction();
                        if (var3x) {
                           _8.G(mc.player, InteractionHand.MAIN_HAND);
                        }

                        this.R = mc.player.tickCount + Math.max(1, this.A.P().intValue());
                        this.w(var3x ? "Placed" : "Server or placement context rejected action");
                     }
                  },
                  () -> this.N()
                     && mc.options.keyUse.isDown()
                     && mc.player.getMainHandItem().is(var25)
                     && _d.o(var26, mc.level, var27, mc.player, var6, mc.player.tickCount)
                     && var20.equals(this.v(var20))
                     && this.W(var20) != null
               );
               return;
            }

            return;
         }
      }

      _d.N(this);
   }

   private BlockPos v(BlockPos var1) {
      if (mc.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK && var2.getBlockPos().relative(var2.getDirection()).equals(var1)) {
         return var1;
      } else {
         Vec3 var5 = mc.player.getEyePosition();

         for (double var6 = 0.5; var6 <= Math.min(this.T.P(), mc.player.blockInteractionRange()); var6 += 0.25) {
            if (BlockPos.containing(var5.add(mc.player.getLookAngle().scale(var6))).equals(var1)) {
               return var1;
            }
         }

         return null;
      }
   }

   private BlockHitResult W(BlockPos var1) {
      if (_d.Z(var1) && mc.player.getMainHandItem().getItem() instanceof BlockItem var2) {
         for (Direction var6 : Direction.values()) {
            BlockPos var7 = var1.relative(var6);
            if (mc.level.hasChunkAt(var7)) {
               BlockState var8 = mc.level.getBlockState(var7);
               if (!var8.canBeReplaced() && !var8.getCollisionShape(mc.level, var7).isEmpty()) {
                  Direction var9 = var6.getOpposite();
                  Vec3 var10 = Vec3.atCenterOf(var7).add(var9.getStepX() * 0.5, var9.getStepY() * 0.5, var9.getStepZ() * 0.5);
                  double var11 = Math.min(this.T.P(), mc.player.blockInteractionRange());
                  if (!(mc.player.getEyePosition().distanceToSqr(var10) > var11 * var11)) {
                     BlockHitResult var13 = new BlockHitResult(var10, var9, var7, false);
                     BlockPlaceContext var14 = new BlockPlaceContext(mc.player, InteractionHand.MAIN_HAND, mc.player.getMainHandItem(), var13);
                     if (var14.canPlace() && var14.getClickedPos().equals(var1)) {
                        BlockState var15 = var2.getBlock().getStateForPlacement(var14);
                        if (var15 != null
                           && var15.canSurvive(mc.level, var1)
                           && mc.level.isUnobstructed(var15, var1, CollisionContext.placementContext(mc.player))) {
                           return var13;
                        }
                     }
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.R = 0;
   }

   @Override
   public void onDisable() {
      super.onDisable();
      _d.N(this);
      this.R = 0;
   }
}

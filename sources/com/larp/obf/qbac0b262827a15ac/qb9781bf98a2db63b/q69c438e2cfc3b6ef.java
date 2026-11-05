package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import com.larpclient.eu;
import com.larpclient.h2;
import com.larpclient.wk;
import com.larpclient.wt;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class q69c438e2cfc3b6ef {
   @Unique
   private int C;

   @Inject(method = "handleCreativeModeItemAdd", at = @At("HEAD"), cancellable = true)
   private void C(ItemStack var1, int var2, CallbackInfo var3) {
      if (h2.I(var1)) {
         var3.cancel();
      }
   }

   @Inject(method = "handleCreativeModeItemDrop", at = @At("HEAD"), cancellable = true)
   private void P(ItemStack var1, CallbackInfo var2) {
      if (h2.I(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void k(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (var1 == Minecraft.getInstance().player) {
         dr var4 = dr.z();
         if (var4.t(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40) || wt.v()) {
            var3.setReturnValue(InteractionResult.PASS);
         }
      }
   }

   @WrapMethod(method = "handleContainerInput")
   private void Z(int var1, int var2, int var3, ContainerInput var4, Player var5, Operation<Void> var6) {
      if (var1 >= 0 && var5 == Minecraft.getInstance().player) {
         if (dr.z().v(var1, var2, var3, var4)) {
            dr.z().K(() -> var6.call(new Object[]{var1, var2, var3, var4, var5}));
         }
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"))
   private void h(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      this.C = var1.getItemInHand(var2).getCount();
   }

   @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void g(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      dr var4 = dr.z();
      if (!var4.C(var1) && (!var4.O(var1) || !var4.V(var1))) {
         if (var4.H() && var4.Y(var1)) {
            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void S(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      dr var4 = dr.z();
      boolean var5 = var4.O(var1);
      if (!var4.C(var1) && (!var5 || !var4.V(var1))) {
         if (var4.H() && var4.h(var1, var2)) {
            if (var5 && !var4.O(var1)) {
               Minecraft.getInstance().levelExtractor.setSectionDirtyWithNeighbors(var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4);
            }

            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void i(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      dr var5 = dr.z();
      if (var5.H() && var1 != null && var3 != null) {
         if (wt.v() || var5.t(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40)) {
            var4.setReturnValue(InteractionResult.PASS);
            return;
         }

         BlockPos var6 = var3.getBlockPos().immutable();
         int var7 = var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40;
         if (var5.O(var6)) {
            if (var5.V(var6)) {
               var4.setReturnValue(InteractionResult.SUCCESS);
               return;
            }

            if (!var5.C(var6, var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown())) {
               Minecraft.getInstance().execute(() -> var5.S(var6));
            } else if (var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()) {
               var5.V(var6.relative(var3.getDirection()));
            }

            var4.setReturnValue(InteractionResult.SUCCESS);
            return;
         }

         if (!var5.H(var1.getItemInHand(var2)) && var5.x(var1.getItemInHand(var2).getItem()) != null) {
            BlockPlaceContext var9 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var9.canPlace()) {
               var5.R(var9.getClickedPos().immutable());
            }

            return;
         }

         if (var5.H(var1.getItemInHand(var2))) {
            BlockPlaceContext var8 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var8.canPlace()) {
               var5.F(
                  var8.getClickedPos().immutable(), var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()
               );
            }

            return;
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("RETURN"))
   private void a(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      dr var5 = dr.z();
      if (((InteractionResult)var4.getReturnValue()).consumesAction()) {
         var5.P();
      }

      boolean var6 = var5.H(var1.getItemInHand(var2));
      if (var6
         && ((InteractionResult)var4.getReturnValue()).consumesAction()
         && this.C > 1
         && var1.getItemInHand(var2).getCount() == this.C - 1
         && var5.H(var1.getItemInHand(var2))) {
         var5.H(var2);
      }
   }

   @Inject(method = "destroyBlock", at = @At("RETURN"))
   private void L(BlockPos var1, CallbackInfoReturnable<Boolean> var2) {
      dr var3 = dr.z();
      if (var3.H() && (Boolean)var2.getReturnValue()) {
         var3.D(var1);
      }
   }

   @WrapOperation(
      method = {"startDestroyBlock", "continueDestroyBlock"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState e(ClientLevel var1, BlockPos var2, Operation<BlockState> var3) {
      return dr.z().a(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @WrapOperation(
      method = "destroyBlock",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState h(Level var1, BlockPos var2, Operation<BlockState> var3) {
      return dr.z().a(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void M(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (Minecraft.getInstance().gui.screen() instanceof eu || Minecraft.getInstance().gui.screen() instanceof wk) {
         var6.cancel();
      } else if (var1 < 0 && var5 != null) {
         var5.containerMenu.clicked(var2, var3, var4, var5);
         var6.cancel();
      }
   }
}

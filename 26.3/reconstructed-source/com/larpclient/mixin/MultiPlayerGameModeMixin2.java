package com.larpclient.mixin;

import com.larpclient.gui.screen.ConfirmSellScreen;
import com.larpclient.gui.screen.FakeSpawnerScreen;
import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.util.inventory.ContainerTransaction;
import com.larpclient.util.world.FakeSpawnerController;
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
public abstract class MultiPlayerGameModeMixin2 {
   @Unique
   private int l;

   @Inject(method = "handleCreativeModeItemAdd", at = @At("HEAD"), cancellable = true)
   private void T(ItemStack var1, int var2, CallbackInfo var3) {
      if (ItemStackMarkers.isPreview(var1)) {
         var3.cancel();
      }
   }

   @Inject(method = "handleCreativeModeItemDrop", at = @At("HEAD"), cancellable = true)
   private void Z(ItemStack var1, CallbackInfo var2) {
      if (ItemStackMarkers.isPreview(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void j(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (var1 == Minecraft.getInstance().player) {
         FakeSpawnerController var4 = FakeSpawnerController.getInstance();
         if (var4.isReservedSlot(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40) || ContainerTransaction.isActive()) {
            var3.setReturnValue(InteractionResult.PASS);
         }
      }
   }

   @WrapMethod(method = "handleContainerInput")
   private void l(int var1, int var2, int var3, ContainerInput var4, Player var5, Operation<Void> var6) {
      if (var1 >= 0 && var5 == Minecraft.getInstance().player) {
         if (FakeSpawnerController.getInstance().snapshotInventory(var1, var2, var3, var4)) {
            FakeSpawnerController.getInstance().isReservedHeldStack(() -> var6.call(new Object[]{var1, var2, var3, var4, var5}));
         }
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"))
   private void F(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      this.l = var1.getItemInHand(var2).getCount();
   }

   @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void c(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      FakeSpawnerController var4 = FakeSpawnerController.getInstance();
      if (!var4.isFakeSpawnerBlock(var1) && (!var4.isFakeSpawnerAt(var1) || !var4.hasPendingPlacement(var1))) {
         if (var4.isActive() && var4.buildSaveKey(var1)) {
            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void f(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      FakeSpawnerController var4 = FakeSpawnerController.getInstance();
      boolean var5 = var4.isFakeSpawnerAt(var1);
      if (!var4.isFakeSpawnerBlock(var1) && (!var5 || !var4.hasPendingPlacement(var1))) {
         if (var4.isActive() && var4.loadPiles(var1, var2)) {
            if (var5 && !var4.isFakeSpawnerAt(var1)) {
               Minecraft.getInstance().levelExtractor.setSectionDirtyWithNeighbors(var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4);
            }

            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void e(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      FakeSpawnerController var5 = FakeSpawnerController.getInstance();
      if (var5.isActive() && var1 != null && var3 != null) {
         if (ContainerTransaction.isActive() || var5.isReservedSlot(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40)) {
            var4.setReturnValue(InteractionResult.PASS);
            return;
         }

         BlockPos var6 = var3.getBlockPos().immutable();
         int var7 = var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40;
         if (var5.isFakeSpawnerAt(var6)) {
            if (var5.hasPendingPlacement(var6)) {
               var4.setReturnValue(InteractionResult.SUCCESS);
               return;
            }

            if (!var5.reconcileInventory(var6, var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown())) {
               Minecraft.getInstance().execute(() -> var5.openLootMenu(var6));
            } else if (var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()) {
               var5.restorePiles(var6.relative(var3.getDirection()));
            }

            var4.setReturnValue(InteractionResult.SUCCESS);
            return;
         }

         if (!var5.isFakeSpawnerStack(var1.getItemInHand(var2)) && var5.writePilesTo(var1.getItemInHand(var2).getItem()) != null) {
            BlockPlaceContext var9 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var9.canPlace()) {
               var5.markFinalBreak(var9.getClickedPos().immutable());
            }

            return;
         }

         if (var5.isFakeSpawnerStack(var1.getItemInHand(var2))) {
            BlockPlaceContext var8 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var8.canPlace()) {
               var5.isReservedSlot(
                  var8.getClickedPos().immutable(), var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()
               );
            }

            return;
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("RETURN"))
   private void D(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      FakeSpawnerController var5 = FakeSpawnerController.getInstance();
      if (((InteractionResult)var4.getReturnValue()).consumesAction()) {
         var5.trackInventoryStacks();
      }

      boolean var6 = var5.isFakeSpawnerStack(var1.getItemInHand(var2));
      if (var6
         && ((InteractionResult)var4.getReturnValue()).consumesAction()
         && this.l > 1
         && var1.getItemInHand(var2).getCount() == this.l - 1
         && var5.isFakeSpawnerStack(var1.getItemInHand(var2))) {
         var5.loadPiles(var2);
      }
   }

   @Inject(method = "destroyBlock", at = @At("RETURN"))
   private void t(BlockPos var1, CallbackInfoReturnable<Boolean> var2) {
      FakeSpawnerController var3 = FakeSpawnerController.getInstance();
      if (var3.isActive() && (Boolean)var2.getReturnValue()) {
         var3.isActive(var1);
      }
   }

   @WrapOperation(
      method = {"startDestroyBlock", "continueDestroyBlock"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState S(ClientLevel var1, BlockPos var2, Operation<BlockState> var3) {
      return FakeSpawnerController.getInstance().getStackName(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @WrapOperation(
      method = "destroyBlock",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState H(Level var1, BlockPos var2, Operation<BlockState> var3) {
      return FakeSpawnerController.getInstance().getStackName(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void t(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (Minecraft.getInstance().gui.screen() instanceof FakeSpawnerScreen || Minecraft.getInstance().gui.screen() instanceof ConfirmSellScreen) {
         var6.cancel();
      } else if (var1 < 0 && var5 != null) {
         var5.containerMenu.clicked(var2, var3, var4, var5);
         var6.cancel();
      }
   }
}

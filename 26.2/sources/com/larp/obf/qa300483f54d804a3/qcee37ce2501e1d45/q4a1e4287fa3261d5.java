package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.gz;
import com.larpclient.m3;
import com.larpclient.mn;
import com.larpclient.t3;
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
public abstract class q4a1e4287fa3261d5 {
   @Unique
   private int n;

   @Inject(method = "handleCreativeModeItemAdd", at = @At("HEAD"), cancellable = true)
   private void x(ItemStack var1, int var2, CallbackInfo var3) {
      if (gz.w(var1)) {
         var3.cancel();
      }
   }

   @Inject(method = "handleCreativeModeItemDrop", at = @At("HEAD"), cancellable = true)
   private void z(ItemStack var1, CallbackInfo var2) {
      if (gz.w(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void H(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (var1 == Minecraft.getInstance().player) {
         b8 var4 = b8.k();
         if (var4.t(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40) || m3.u()) {
            var3.setReturnValue(InteractionResult.PASS);
         }
      }
   }

   @WrapMethod(method = "handleContainerInput")
   private void q(int var1, int var2, int var3, ContainerInput var4, Player var5, Operation<Void> var6) {
      if (var1 >= 0 && var5 == Minecraft.getInstance().player) {
         if (b8.k().a(var1, var2, var3, var4)) {
            b8.k().z(() -> var6.call(new Object[]{var1, var2, var3, var4, var5}));
         }
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"))
   private void m(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      this.n = var1.getItemInHand(var2).getCount();
   }

   @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void F(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      b8 var4 = b8.k();
      if (!var4.C(var1) && (!var4.a(var1) || !var4.k(var1))) {
         if (var4.J() && var4.q(var1)) {
            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void y(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      b8 var4 = b8.k();
      boolean var5 = var4.a(var1);
      if (!var4.C(var1) && (!var5 || !var4.k(var1))) {
         if (var4.J() && var4.l(var1, var2)) {
            if (var5 && !var4.a(var1)) {
               Minecraft.getInstance().levelExtractor.setSectionDirtyWithNeighbors(var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4);
            }

            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void B(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      b8 var5 = b8.k();
      if (var5.J() && var1 != null && var3 != null) {
         if (m3.u() || var5.t(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40)) {
            var4.setReturnValue(InteractionResult.PASS);
            return;
         }

         BlockPos var6 = var3.getBlockPos().immutable();
         int var7 = var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40;
         if (var5.a(var6)) {
            if (var5.k(var6)) {
               var4.setReturnValue(InteractionResult.SUCCESS);
               return;
            }

            if (!var5.H(var6, var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown())) {
               Minecraft.getInstance().execute(() -> var5.V(var6));
            } else if (var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()) {
               var5.j(var6.relative(var3.getDirection()));
            }

            var4.setReturnValue(InteractionResult.SUCCESS);
            return;
         }

         if (!var5.r(var1.getItemInHand(var2)) && var5.K(var1.getItemInHand(var2).getItem()) != null) {
            BlockPlaceContext var9 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var9.canPlace()) {
               var5.S(var9.getClickedPos().immutable());
            }

            return;
         }

         if (var5.r(var1.getItemInHand(var2))) {
            BlockPlaceContext var8 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var8.canPlace()) {
               var5.f(
                  var8.getClickedPos().immutable(), var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()
               );
            }

            return;
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("RETURN"))
   private void Z(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      b8 var5 = b8.k();
      if (((InteractionResult)var4.getReturnValue()).consumesAction()) {
         var5.E();
      }

      boolean var6 = var5.r(var1.getItemInHand(var2));
      if (var6
         && ((InteractionResult)var4.getReturnValue()).consumesAction()
         && this.n > 1
         && var1.getItemInHand(var2).getCount() == this.n - 1
         && var5.r(var1.getItemInHand(var2))) {
         var5.D(var2);
      }
   }

   @Inject(method = "destroyBlock", at = @At("RETURN"))
   private void J(BlockPos var1, CallbackInfoReturnable<Boolean> var2) {
      b8 var3 = b8.k();
      if (var3.J() && (Boolean)var2.getReturnValue()) {
         var3.Z(var1);
      }
   }

   @WrapOperation(
      method = {"startDestroyBlock", "continueDestroyBlock"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState D(ClientLevel var1, BlockPos var2, Operation<BlockState> var3) {
      return b8.k().I(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @WrapOperation(
      method = "destroyBlock",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState W(Level var1, BlockPos var2, Operation<BlockState> var3) {
      return b8.k().I(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void E(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (Minecraft.getInstance().gui.screen() instanceof mn || Minecraft.getInstance().gui.screen() instanceof t3) {
         var6.cancel();
      } else if (var1 < 0 && var5 != null) {
         var5.containerMenu.clicked(var2, var3, var4, var5);
         var6.cancel();
      }
   }
}

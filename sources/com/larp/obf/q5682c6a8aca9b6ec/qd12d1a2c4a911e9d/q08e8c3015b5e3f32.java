package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.ja;
import com.larpclient.lj;
import com.larpclient.oy;
import com.larpclient.rm;
import com.larpclient.x4;
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
public abstract class q08e8c3015b5e3f32 {
   @Unique
   private int a;

   @Inject(method = "handleCreativeModeItemAdd", at = @At("HEAD"), cancellable = true)
   private void R(ItemStack var1, int var2, CallbackInfo var3) {
      if (ja.Q(var1)) {
         var3.cancel();
      }
   }

   @Inject(method = "handleCreativeModeItemDrop", at = @At("HEAD"), cancellable = true)
   private void r(ItemStack var1, CallbackInfo var2) {
      if (ja.Q(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void Z(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (var1 == Minecraft.getInstance().player) {
         lj var4 = lj.j();
         if (var4.U(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40) || rm.L()) {
            var3.setReturnValue(InteractionResult.PASS);
         }
      }
   }

   @WrapMethod(method = "handleContainerInput")
   private void A(int var1, int var2, int var3, ContainerInput var4, Player var5, Operation<Void> var6) {
      if (var1 >= 0 && var5 == Minecraft.getInstance().player) {
         if (lj.j().H(var1, var2, var3, var4)) {
            lj.j().U(() -> var6.call(new Object[]{var1, var2, var3, var4, var5}));
         }
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"))
   private void Y(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      this.a = var1.getItemInHand(var2).getCount();
   }

   @Inject(method = "startDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void Z(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      lj var4 = lj.j();
      if (!var4.c(var1) && (!var4.G(var1) || !var4.K(var1))) {
         if (var4.B() && var4.f(var1)) {
            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "continueDestroyBlock", at = @At("HEAD"), cancellable = true)
   private void U(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      lj var4 = lj.j();
      boolean var5 = var4.G(var1);
      if (!var4.c(var1) && (!var5 || !var4.K(var1))) {
         if (var4.B() && var4.z(var1, var2)) {
            if (var5 && !var4.G(var1)) {
               Minecraft.getInstance().levelExtractor.setSectionDirtyWithNeighbors(var1.getX() >> 4, var1.getY() >> 4, var1.getZ() >> 4);
            }

            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void R(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      lj var5 = lj.j();
      if (var5.B() && var1 != null && var3 != null) {
         if (rm.L() || var5.U(var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40)) {
            var4.setReturnValue(InteractionResult.PASS);
            return;
         }

         BlockPos var6 = var3.getBlockPos().immutable();
         int var7 = var2 == InteractionHand.MAIN_HAND ? var1.getInventory().getSelectedSlot() : 40;
         if (var5.G(var6)) {
            if (var5.K(var6)) {
               var4.setReturnValue(InteractionResult.SUCCESS);
               return;
            }

            if (!var5.W(var6, var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown())) {
               Minecraft.getInstance().execute(() -> var5.V(var6));
            } else if (var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()) {
               var5.g(var6.relative(var3.getDirection()));
            }

            var4.setReturnValue(InteractionResult.SUCCESS);
            return;
         }

         if (!var5.u(var1.getItemInHand(var2)) && var5.u(var1.getItemInHand(var2).getItem()) != null) {
            BlockPlaceContext var9 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var9.canPlace()) {
               var5.J(var9.getClickedPos().immutable());
            }

            return;
         }

         if (var5.u(var1.getItemInHand(var2))) {
            BlockPlaceContext var8 = new BlockPlaceContext(var1, var2, var1.getItemInHand(var2), var3);
            if (var8.canPlace()) {
               var5.D(
                  var8.getClickedPos().immutable(), var1.getItemInHand(var2), var7, var1.isShiftKeyDown() || Minecraft.getInstance().options.keyShift.isDown()
               );
            }

            return;
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("RETURN"))
   private void B(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      lj var5 = lj.j();
      if (((InteractionResult)var4.getReturnValue()).consumesAction()) {
         var5.U();
      }

      boolean var6 = var5.u(var1.getItemInHand(var2));
      if (var6
         && ((InteractionResult)var4.getReturnValue()).consumesAction()
         && this.a > 1
         && var1.getItemInHand(var2).getCount() == this.a - 1
         && var5.u(var1.getItemInHand(var2))) {
         var5.r(var2);
      }
   }

   @Inject(method = "destroyBlock", at = @At("RETURN"))
   private void N(BlockPos var1, CallbackInfoReturnable<Boolean> var2) {
      lj var3 = lj.j();
      if (var3.B() && (Boolean)var2.getReturnValue()) {
         var3.B(var1);
      }
   }

   @WrapOperation(
      method = {"startDestroyBlock", "continueDestroyBlock"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState K(ClientLevel var1, BlockPos var2, Operation<BlockState> var3) {
      return lj.j().I(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @WrapOperation(
      method = "destroyBlock",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/Level;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState G(Level var1, BlockPos var2, Operation<BlockState> var3) {
      return lj.j().I(var1, var2, (BlockState)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void v(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (Minecraft.getInstance().gui.screen() instanceof oy || Minecraft.getInstance().gui.screen() instanceof x4) {
         var6.cancel();
      } else if (var1 < 0 && var5 != null) {
         var5.containerMenu.clicked(var2, var3, var4, var5);
         var6.cancel();
      }
   }
}

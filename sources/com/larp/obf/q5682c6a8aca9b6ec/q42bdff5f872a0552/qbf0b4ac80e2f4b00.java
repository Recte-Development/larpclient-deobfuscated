package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.eg;
import com.larpclient.f2;
import com.larpclient.hk;
import com.larpclient.js;
import com.larpclient.mm;
import com.larpclient.oi;
import com.larpclient.ro;
import com.larpclient.ue;
import com.larpclient.ug;
import com.larpclient.um;
import com.larpclient.xa;
import com.larpclient.xs;
import com.larpclient.yg;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class qbf0b4ac80e2f4b00 {
   @WrapOperation(method = "handleContainerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getStateId()I"))
   private int j(AbstractContainerMenu var1, Operation<Integer> var2) {
      return mm.W().w(var1, ro.H().P(var1, ug.h().clickRevision(var1)));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void A(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (!mm.W().E()) {
         if (yg.a(var1, var2, var3, var4)) {
            var6.cancel();
         } else {
            yg.n();
            if (xs.j(Minecraft.getInstance(), var2, var3, var4)) {
               var6.cancel();
            } else if (ro.H().p()) {
               var6.cancel();
            } else if (mm.W().h()) {
               var6.cancel();
            } else if (!(ug.x() instanceof um) && ug.x().blocksUserInteraction()) {
               var6.cancel();
            } else if (!ug.x().isSwapActive()) {
               Minecraft var7 = Minecraft.getInstance();
               if (var7 != null && var7.player != null && var7.player.containerMenu != null) {
                  if (var7.player.containerMenu.containerId == var1) {
                     if (hk.G().a(var7, var1, var2, var3, var4)
                        || oi.A().h(var7, var1, var2, var3, var4)
                        || xa.r().x(var7, var1, var2, var3, var4)
                        || js.A().X(var7, var1, var2, var3, var4)) {
                        var6.cancel();
                     }
                  }
               }
            }
         }
      }
   }

   @Inject(method = "handleContainerInput", at = @At("TAIL"))
   private void Y(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      yg.n();
   }

   @ModifyVariable(method = "interact", at = @At("HEAD"), argsOnly = true)
   private EntityHitResult h(EntityHitResult var1) {
      return var1.getEntity() instanceof ArmorStand var2 ? hk.T.X(var2, var1) : var1;
   }

   @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
   private void l(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      yg.T();
      if (var4 == InteractionHand.MAIN_HAND && !yg.q()) {
         var5.setReturnValue(InteractionResult.FAIL);
      } else if (var2 instanceof ItemFrame var6
         && var6.getItem().isEmpty()
         && var4 == InteractionHand.MAIN_HAND
         && !xs.f(Minecraft.getInstance(), var4)
         && xs.f(Minecraft.getInstance(), InteractionHand.OFF_HAND)) {
         var5.setReturnValue(InteractionResult.PASS);
      } else if (var2 instanceof ItemFrame var7 && xs.v(Minecraft.getInstance(), var7, var4)) {
         var5.setReturnValue(InteractionResult.SUCCESS);
      } else if (var2 instanceof ItemFrame && ug.x().l(var1.getMainHandItem())) {
         var5.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(method = "interact", at = @At("RETURN"))
   private void Z(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      if (var2 instanceof ItemFrame var6 && var4 == InteractionHand.MAIN_HAND && ((InteractionResult)var5.getReturnValue()).consumesAction()) {
         ug.x().signalFramePlacement(var6);
      }
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void x(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof ItemFrame var4 && xs.W(Minecraft.getInstance(), var4)) {
         var3.cancel();
      } else {
         if (var2 instanceof ItemFrame && ug.x().l(var1.getMainHandItem())) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void y(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      if (xs.f(Minecraft.getInstance(), var2)) {
         var4.setReturnValue(InteractionResult.FAIL);
      } else if (!ug.x().j()) {
         mm.W().l(var3.getBlockPos());
         if (var1.level().getBlockState(var3.getBlockPos()).is(Blocks.LEVER)) {
            mm.W().Q(var3.getBlockPos());
         }

         if (xa.r().v(var1.getItemInHand(var2)) && var1.level().getBlockState(var3.getBlockPos()).getBlock() instanceof AbstractCauldronBlock) {
            var4.setReturnValue(InteractionResult.FAIL);
         } else if (ro.H().i(Minecraft.getInstance(), var2, var3)) {
            var4.setReturnValue(InteractionResult.SUCCESS);
         } else if ((!ug.x().blocksUserInteraction() || ug.x().allowsRecoveryOpen(var3.getBlockPos()))
            && um.T().e(var3.getBlockPos())
            && ue.V().W(var3.getBlockPos())) {
            if (var2 == InteractionHand.MAIN_HAND) {
               Minecraft var5 = Minecraft.getInstance();
               f2 var6 = f2.f();
               if (var6.P(var5, var3.getBlockPos()) || var6.H(var5, var3.getBlockPos())) {
                  var4.setReturnValue(InteractionResult.SUCCESS);
               }
            }
         } else {
            var4.setReturnValue(InteractionResult.FAIL);
         }
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void E(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (xs.f(Minecraft.getInstance(), var2)) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND && !yg.q()) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (ug.x().blocksUserInteraction() || xa.r().v(var1.getItemInHand(var2))) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND) {
         eg var4 = eg.y();
         if (!var4.k() && var4.w()) {
            Minecraft var5 = Minecraft.getInstance();
            int var6 = var4.g();
            if (var6 >= 0 && var6 <= 8) {
               int var7 = var1.getInventory().getSelectedSlot();
               if (var7 != var6) {
                  var4.l();

                  try {
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var6));
                     InteractionResult var8 = var5.gameMode.useItem(var1, var2);
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var7));
                     var3.setReturnValue(var8);
                  } finally {
                     var4.k();
                  }
               }
            }
         }
      }
   }
}

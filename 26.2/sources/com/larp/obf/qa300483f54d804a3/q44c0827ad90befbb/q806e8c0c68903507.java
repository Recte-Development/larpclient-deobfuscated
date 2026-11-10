package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.bs;
import com.larpclient.cc;
import com.larpclient.cy;
import com.larpclient.g5;
import com.larpclient.lh;
import com.larpclient.mg;
import com.larpclient.pq;
import com.larpclient.pw;
import com.larpclient.sz;
import com.larpclient.t5;
import com.larpclient.w3;
import com.larpclient.wc;
import com.larpclient.we;
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
public class q806e8c0c68903507 {
   @WrapOperation(method = "handleContainerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getStateId()I"))
   private int S(AbstractContainerMenu var1, Operation<Integer> var2) {
      return lh.I().A(var1, pw.S().n(var1, we.l().clickRevision(var1)));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void U(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (!lh.I().y()) {
         if (mg.f(var1, var2, var3, var4)) {
            var6.cancel();
         } else {
            mg.U();
            if (sz.b(Minecraft.getInstance(), var2, var3, var4)) {
               var6.cancel();
            } else if (pw.S().C()) {
               var6.cancel();
            } else if (lh.I().A()) {
               var6.cancel();
            } else if (!(we.Y() instanceof wc) && we.Y().blocksUserInteraction()) {
               var6.cancel();
            } else if (!we.Y().isSwapActive()) {
               Minecraft var7 = Minecraft.getInstance();
               if (var7 != null && var7.player != null && var7.player.containerMenu != null) {
                  if (var7.player.containerMenu.containerId == var1) {
                     if (cy.Q().C(var7, var1, var2, var3, var4)
                        || g5.a().B(var7, var1, var2, var3, var4)
                        || t5.O().J(var7, var1, var2, var3, var4)
                        || pq.Q().O(var7, var1, var2, var3, var4)) {
                        var6.cancel();
                     }
                  }
               }
            }
         }
      }
   }

   @Inject(method = "handleContainerInput", at = @At("TAIL"))
   private void N(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      mg.U();
   }

   @ModifyVariable(method = "interact", at = @At("HEAD"), argsOnly = true)
   private EntityHitResult H(EntityHitResult var1) {
      return var1.getEntity() instanceof ArmorStand var2 ? cy.d.d(var2, var1) : var1;
   }

   @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
   private void S(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      mg.V();
      if (var4 == InteractionHand.MAIN_HAND && !mg.g()) {
         var5.setReturnValue(InteractionResult.FAIL);
      } else if (var2 instanceof ItemFrame var6
         && var6.getItem().isEmpty()
         && var4 == InteractionHand.MAIN_HAND
         && !sz.Z(Minecraft.getInstance(), var4)
         && sz.Z(Minecraft.getInstance(), InteractionHand.OFF_HAND)) {
         var5.setReturnValue(InteractionResult.PASS);
      } else if (var2 instanceof ItemFrame var7 && sz.T(Minecraft.getInstance(), var7, var4)) {
         var5.setReturnValue(InteractionResult.SUCCESS);
      } else if (var2 instanceof ItemFrame && we.Y().q(var1.getMainHandItem())) {
         var5.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(method = "interact", at = @At("RETURN"))
   private void Y(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      if (var2 instanceof ItemFrame var6 && var4 == InteractionHand.MAIN_HAND && ((InteractionResult)var5.getReturnValue()).consumesAction()) {
         we.Y().signalFramePlacement(var6);
      }
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void M(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof ItemFrame var4 && sz.N(Minecraft.getInstance(), var4)) {
         var3.cancel();
      } else {
         if (var2 instanceof ItemFrame && we.Y().q(var1.getMainHandItem())) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void Q(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      if (sz.Z(Minecraft.getInstance(), var2)) {
         var4.setReturnValue(InteractionResult.FAIL);
      } else if (!we.Y().p()) {
         lh.I().N(var3.getBlockPos());
         if (var1.level().getBlockState(var3.getBlockPos()).is(Blocks.LEVER)) {
            lh.I().H(var3.getBlockPos());
         }

         if (t5.O().y(var1.getItemInHand(var2)) && var1.level().getBlockState(var3.getBlockPos()).getBlock() instanceof AbstractCauldronBlock) {
            var4.setReturnValue(InteractionResult.FAIL);
         } else if (pw.S().z(Minecraft.getInstance(), var2, var3)) {
            var4.setReturnValue(InteractionResult.SUCCESS);
         } else if ((!we.Y().blocksUserInteraction() || we.Y().allowsRecoveryOpen(var3.getBlockPos()))
            && wc.s().Y(var3.getBlockPos())
            && w3.M().m(var3.getBlockPos())) {
            if (var2 == InteractionHand.MAIN_HAND) {
               Minecraft var5 = Minecraft.getInstance();
               bs var6 = bs.R();
               if (var6.W(var5, var3.getBlockPos()) || var6.x(var5, var3.getBlockPos())) {
                  var4.setReturnValue(InteractionResult.SUCCESS);
               }
            }
         } else {
            var4.setReturnValue(InteractionResult.FAIL);
         }
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void p(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (sz.Z(Minecraft.getInstance(), var2)) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND && !mg.g()) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (we.Y().blocksUserInteraction() || t5.O().y(var1.getItemInHand(var2))) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND) {
         cc var4 = cc.z();
         if (!var4.L() && var4.z()) {
            Minecraft var5 = Minecraft.getInstance();
            int var6 = var4.W();
            if (var6 >= 0 && var6 <= 8) {
               int var7 = var1.getInventory().getSelectedSlot();
               if (var7 != var6) {
                  var4.E();

                  try {
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var6));
                     InteractionResult var8 = var5.gameMode.useItem(var1, var2);
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var7));
                     var3.setReturnValue(var8);
                  } finally {
                     var4.c();
                  }
               }
            }
         }
      }
   }
}

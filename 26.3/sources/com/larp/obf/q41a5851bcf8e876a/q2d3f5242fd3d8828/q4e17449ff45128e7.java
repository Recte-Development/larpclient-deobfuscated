package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.hh;
import com.larpclient.ig;
import com.larpclient.p2;
import com.larpclient.pp;
import com.larpclient.q5;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.vb;
import com.larpclient.vh;
import com.larpclient.vy;
import com.larpclient.vz;
import com.larpclient.wj;
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
public class q4e17449ff45128e7 {
   @WrapOperation(method = "handleContainerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getStateId()I"))
   private int G(AbstractContainerMenu var1, Operation<Integer> var2) {
      return q5.a().V(var1, wj.m().z(var1, vy.x().clickRevision(var1)));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void K(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (!q5.a().l()) {
         if (qm.j(var1, var2, var3, var4)) {
            var6.cancel();
         } else {
            qm.I();
            if (vb.D(Minecraft.getInstance(), var2, var3, var4)) {
               var6.cancel();
            } else if (wj.m().Q()) {
               var6.cancel();
            } else if (q5.a().C()) {
               var6.cancel();
            } else if (!(vy.a() instanceof vh) && vy.a().blocksUserInteraction()) {
               var6.cancel();
            } else if (!vy.a().isSwapActive()) {
               Minecraft var7 = Minecraft.getInstance();
               if (var7 != null && var7.player != null && var7.player.containerMenu != null) {
                  if (var7.player.containerMenu.containerId == var1) {
                     if (sl.X().N(var7, var1, var2, var3, var4)
                        || qo.Z().Q(var7, var1, var2, var3, var4)
                        || p2.p().F(var7, var1, var2, var3, var4)
                        || pp.A().p(var7, var1, var2, var3, var4)) {
                        var6.cancel();
                     }
                  }
               }
            }
         }
      }
   }

   @Inject(method = "handleContainerInput", at = @At("TAIL"))
   private void F(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      qm.I();
   }

   @ModifyVariable(method = "interact", at = @At("HEAD"), argsOnly = true)
   private EntityHitResult P(EntityHitResult var1) {
      return var1.getEntity() instanceof ArmorStand var2 ? sl.s.N(var2, var1) : var1;
   }

   @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
   private void j(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      qm.p();
      if (var4 == InteractionHand.MAIN_HAND && !qm.o()) {
         var5.setReturnValue(InteractionResult.FAIL);
      } else if (var2 instanceof ItemFrame var6
         && var6.getItem().isEmpty()
         && var4 == InteractionHand.MAIN_HAND
         && !vb.H(Minecraft.getInstance(), var4)
         && vb.H(Minecraft.getInstance(), InteractionHand.OFF_HAND)) {
         var5.setReturnValue(InteractionResult.PASS);
      } else if (var2 instanceof ItemFrame var7 && vb.Y(Minecraft.getInstance(), var7, var4)) {
         var5.setReturnValue(InteractionResult.SUCCESS);
      } else if (var2 instanceof ItemFrame && vy.a().j(var1.getMainHandItem())) {
         var5.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(method = "interact", at = @At("RETURN"))
   private void c(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      if (var2 instanceof ItemFrame var6 && var4 == InteractionHand.MAIN_HAND && ((InteractionResult)var5.getReturnValue()).consumesAction()) {
         vy.a().signalFramePlacement(var6);
      }
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void D(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof ItemFrame var4 && vb.j(Minecraft.getInstance(), var4)) {
         var3.cancel();
      } else {
         if (var2 instanceof ItemFrame && vy.a().j(var1.getMainHandItem())) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void k(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      if (vb.H(Minecraft.getInstance(), var2)) {
         var4.setReturnValue(InteractionResult.FAIL);
      } else if (!vy.a().l()) {
         q5.a().c(var3.getBlockPos());
         if (var1.level().getBlockState(var3.getBlockPos()).is(Blocks.LEVER)) {
            q5.a().F(var3.getBlockPos());
         }

         if (p2.p().I(var1.getItemInHand(var2)) && var1.level().getBlockState(var3.getBlockPos()).getBlock() instanceof AbstractCauldronBlock) {
            var4.setReturnValue(InteractionResult.FAIL);
         } else if (wj.m().g(Minecraft.getInstance(), var2, var3)) {
            var4.setReturnValue(InteractionResult.SUCCESS);
         } else if ((!vy.a().blocksUserInteraction() || vy.a().allowsRecoveryOpen(var3.getBlockPos()))
            && vh.R().M(var3.getBlockPos())
            && vz.E().V(var3.getBlockPos())) {
            if (var2 == InteractionHand.MAIN_HAND) {
               Minecraft var5 = Minecraft.getInstance();
               ig var6 = ig.r();
               if (var6.O(var5, var3.getBlockPos()) || var6.M(var5, var3.getBlockPos())) {
                  var4.setReturnValue(InteractionResult.SUCCESS);
               }
            }
         } else {
            var4.setReturnValue(InteractionResult.FAIL);
         }
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void L(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (vb.H(Minecraft.getInstance(), var2)) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND && !qm.o()) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (vy.a().blocksUserInteraction() || p2.p().I(var1.getItemInHand(var2))) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND) {
         hh var4 = hh.R();
         if (!var4.y() && var4.P()) {
            Minecraft var5 = Minecraft.getInstance();
            int var6 = var4.t();
            if (var6 >= 0 && var6 <= 8) {
               int var7 = var1.getInventory().getSelectedSlot();
               if (var7 != var6) {
                  var4.a();

                  try {
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var6));
                     InteractionResult var8 = var5.gameMode.useItem(var1, var2);
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var7));
                     var3.setReturnValue(var8);
                  } finally {
                     var4.o();
                  }
               }
            }
         }
      }
   }
}

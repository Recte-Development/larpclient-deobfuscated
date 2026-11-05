package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._a;
import com.larpclient._g;
import com.larpclient._u;
import com.larpclient.c6;
import com.larpclient.kq;
import com.larpclient.mi;
import com.larpclient.pj;
import com.larpclient.w5;
import com.larpclient.z;
import com.larpclient.z8;
import com.larpclient.ze;
import com.larpclient.zu;
import com.larpclient.zv;
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
public class qf8b93dd3218cbe55 {
   @WrapOperation(method = "handleContainerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getStateId()I"))
   private int C(AbstractContainerMenu var1, Operation<Integer> var2) {
      return kq.I().j(var1, c6.K().k(var1, ze.n().clickRevision(var1)));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void L(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (!kq.I().A()) {
         if (w5.f(var1, var2, var3, var4)) {
            var6.cancel();
         } else {
            w5.P();
            if (_a.E(Minecraft.getInstance(), var2, var3, var4)) {
               var6.cancel();
            } else if (c6.K().j()) {
               var6.cancel();
            } else if (kq.I().j()) {
               var6.cancel();
            } else if (!(ze.c() instanceof z8) && ze.c().blocksUserInteraction()) {
               var6.cancel();
            } else if (!ze.c().isSwapActive()) {
               Minecraft var7 = Minecraft.getInstance();
               if (var7 != null && var7.player != null && var7.player.containerMenu != null) {
                  if (var7.player.containerMenu.containerId == var1) {
                     if (_g.E().V(var7, var1, var2, var3, var4)
                        || mi.L().G(var7, var1, var2, var3, var4)
                        || zv.x().w(var7, var1, var2, var3, var4)
                        || _u.O().x(var7, var1, var2, var3, var4)) {
                        var6.cancel();
                     }
                  }
               }
            }
         }
      }
   }

   @Inject(method = "handleContainerInput", at = @At("TAIL"))
   private void a(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      w5.P();
   }

   @ModifyVariable(method = "interact", at = @At("HEAD"), argsOnly = true)
   private EntityHitResult L(EntityHitResult var1) {
      return var1.getEntity() instanceof ArmorStand var2 ? _g.Y.d(var2, var1) : var1;
   }

   @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
   private void b(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      w5.Z();
      if (var4 == InteractionHand.MAIN_HAND && !w5.O()) {
         var5.setReturnValue(InteractionResult.FAIL);
      } else if (var2 instanceof ItemFrame var6
         && var6.getItem().isEmpty()
         && var4 == InteractionHand.MAIN_HAND
         && !_a.M(Minecraft.getInstance(), var4)
         && _a.M(Minecraft.getInstance(), InteractionHand.OFF_HAND)) {
         var5.setReturnValue(InteractionResult.PASS);
      } else if (var2 instanceof ItemFrame var7 && _a.D(Minecraft.getInstance(), var7, var4)) {
         var5.setReturnValue(InteractionResult.SUCCESS);
      } else if (var2 instanceof ItemFrame && ze.c().d(var1.getMainHandItem())) {
         var5.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(method = "interact", at = @At("RETURN"))
   private void J(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      if (var2 instanceof ItemFrame var6 && var4 == InteractionHand.MAIN_HAND && ((InteractionResult)var5.getReturnValue()).consumesAction()) {
         ze.c().signalFramePlacement(var6);
      }
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void s(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof ItemFrame var4 && _a.Y(Minecraft.getInstance(), var4)) {
         var3.cancel();
      } else {
         if (var2 instanceof ItemFrame && ze.c().d(var1.getMainHandItem())) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void O(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      if (_a.M(Minecraft.getInstance(), var2)) {
         var4.setReturnValue(InteractionResult.FAIL);
      } else if (!ze.c().I()) {
         kq.I().N(var3.getBlockPos());
         if (var1.level().getBlockState(var3.getBlockPos()).is(Blocks.LEVER)) {
            kq.I().J(var3.getBlockPos());
         }

         if (zv.x().k(var1.getItemInHand(var2)) && var1.level().getBlockState(var3.getBlockPos()).getBlock() instanceof AbstractCauldronBlock) {
            var4.setReturnValue(InteractionResult.FAIL);
         } else if (c6.K().A(Minecraft.getInstance(), var2, var3)) {
            var4.setReturnValue(InteractionResult.SUCCESS);
         } else if ((!ze.c().blocksUserInteraction() || ze.c().allowsRecoveryOpen(var3.getBlockPos()))
            && z8.l().v(var3.getBlockPos())
            && zu.v().T(var3.getBlockPos())) {
            if (var2 == InteractionHand.MAIN_HAND) {
               Minecraft var5 = Minecraft.getInstance();
               z var6 = z.v();
               if (var6.Y(var5, var3.getBlockPos()) || var6.D(var5, var3.getBlockPos())) {
                  var4.setReturnValue(InteractionResult.SUCCESS);
               }
            }
         } else {
            var4.setReturnValue(InteractionResult.FAIL);
         }
      }
   }

   @Inject(method = "useItem", at = @At("HEAD"), cancellable = true)
   private void O(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      if (_a.M(Minecraft.getInstance(), var2)) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND && !w5.O()) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (ze.c().blocksUserInteraction() || zv.x().k(var1.getItemInHand(var2))) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND) {
         pj var4 = pj.U();
         if (!var4.V() && var4.W()) {
            Minecraft var5 = Minecraft.getInstance();
            int var6 = var4.B();
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
                     var4.N();
                  }
               }
            }
         }
      }
   }
}

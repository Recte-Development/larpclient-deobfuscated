package com.larpclient.mixin;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.DispenserMinigameSpoofer;
import com.larpclient.module.exploit.HigherLowerSwapper;
import com.larpclient.module.exploit.OddEvenCardForce;
import com.larpclient.module.misc.ArrowGame;
import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.module.player.PhantomItemManager;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.inventory.MapSwapInventory;
import com.larpclient.util.misc.FiftyFiftyDispenser;
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
public class MultiPlayerGameModeMixin3 {
   @WrapOperation(method = "handleContainerInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getStateId()I"))
   private int G(AbstractContainerMenu var1, Operation<Integer> var2) {
      return BlackjackRigger.getInstance()
         .onTrigger(var1, FiftyFiftyDispenser.getInstance().overrideInputFlags(var1, CardForce.findInventorySlot().clickRevision(var1)));
   }

   @Inject(method = "handleContainerInput", at = @At("HEAD"), cancellable = true)
   private void K(int var1, int var2, int var3, ContainerInput var4, Player var5, CallbackInfo var6) {
      if (!BlackjackRigger.getInstance().isInternalAction()) {
         if (MapSwapInventory.interceptSlotClick(var1, var2, var3, var4)) {
            var6.cancel();
         } else {
            MapSwapInventory.refresh();
            if (PhantomItemManager.handleSlotClick(Minecraft.getInstance(), var2, var3, var4)) {
               var6.cancel();
            } else if (FiftyFiftyDispenser.getInstance().shouldBlockInput()) {
               var6.cancel();
            } else if (BlackjackRigger.getInstance().isHidingCards()) {
               var6.cancel();
            } else if (!(CardForce.getActive() instanceof HigherLowerSwapper) && CardForce.getActive().blocksUserInteraction()) {
               var6.cancel();
            } else if (!CardForce.getActive().isSwapActive()) {
               Minecraft var7 = Minecraft.getInstance();
               if (var7 != null && var7.player != null && var7.player.containerMenu != null) {
                  if (var7.player.containerMenu.containerId == var1) {
                     if (DispenserMinigameSpoofer.getActive().handleClick(var7, var1, var2, var3, var4)
                        || VisualWinnerConfig.getInstance().setLinked(var7, var1, var2, var3, var4)
                        || DispenserSpoofer.getInstance().redirectClick(var7, var1, var2, var3, var4)
                        || DispenserRemap.getInstance().handleSlotClick(var7, var1, var2, var3, var4)) {
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
      MapSwapInventory.refresh();
   }

   @ModifyVariable(method = "interact", at = @At("HEAD"), argsOnly = true)
   private EntityHitResult P(EntityHitResult var1) {
      return var1.getEntity() instanceof ArmorStand var2 ? DispenserMinigameSpoofer.aRMOR_GAME.handleClick(var2, var1) : var1;
   }

   @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
   private void j(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      MapSwapInventory.update();
      if (var4 == InteractionHand.MAIN_HAND && !MapSwapInventory.hasSelectedItem()) {
         var5.setReturnValue(InteractionResult.FAIL);
      } else if (var2 instanceof ItemFrame var6
         && var6.getItem().isEmpty()
         && var4 == InteractionHand.MAIN_HAND
         && !PhantomItemManager.isPhantomHeld(Minecraft.getInstance(), var4)
         && PhantomItemManager.isPhantomHeld(Minecraft.getInstance(), InteractionHand.OFF_HAND)) {
         var5.setReturnValue(InteractionResult.PASS);
      } else if (var2 instanceof ItemFrame var7 && PhantomItemManager.interactItemFrame(Minecraft.getInstance(), var7, var4)) {
         var5.setReturnValue(InteractionResult.SUCCESS);
      } else if (var2 instanceof ItemFrame && CardForce.getActive().dumpCards(var1.getMainHandItem())) {
         var5.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(method = "interact", at = @At("RETURN"))
   private void c(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      if (var2 instanceof ItemFrame var6 && var4 == InteractionHand.MAIN_HAND && ((InteractionResult)var5.getReturnValue()).consumesAction()) {
         CardForce.getActive().signalFramePlacement(var6);
      }
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void D(Player var1, Entity var2, CallbackInfo var3) {
      if (var2 instanceof ItemFrame var4 && PhantomItemManager.attackItemFrame(Minecraft.getInstance(), var4)) {
         var3.cancel();
      } else {
         if (var2 instanceof ItemFrame && CardForce.getActive().dumpCards(var1.getMainHandItem())) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void k(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      if (PhantomItemManager.isPhantomHeld(Minecraft.getInstance(), var2)) {
         var4.setReturnValue(InteractionResult.FAIL);
      } else if (!CardForce.getActive().continueFromPacket()) {
         BlackjackRigger.getInstance().onDispenserUse(var3.getBlockPos());
         if (var1.level().getBlockState(var3.getBlockPos()).is(Blocks.LEVER)) {
            BlackjackRigger.getInstance().onLeverUse(var3.getBlockPos());
         }

         if (DispenserSpoofer.getInstance().isTotemBucketSpoof(var1.getItemInHand(var2))
            && var1.level().getBlockState(var3.getBlockPos()).getBlock() instanceof AbstractCauldronBlock) {
            var4.setReturnValue(InteractionResult.FAIL);
         } else if (FiftyFiftyDispenser.getInstance().onUseBlock(Minecraft.getInstance(), var2, var3)) {
            var4.setReturnValue(InteractionResult.SUCCESS);
         } else if ((!CardForce.getActive().blocksUserInteraction() || CardForce.getActive().allowsRecoveryOpen(var3.getBlockPos()))
            && HigherLowerSwapper.getInstance().canOpenDispenser(var3.getBlockPos())
            && OddEvenCardForce.getInstance().canUseDispenser(var3.getBlockPos())) {
            if (var2 == InteractionHand.MAIN_HAND) {
               Minecraft var5 = Minecraft.getInstance();
               ArrowGame var6 = ArrowGame.getInstance();
               if (var6.onLeverUse(var5, var3.getBlockPos()) || var6.onNoteBlockUse(var5, var3.getBlockPos())) {
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
      if (PhantomItemManager.isPhantomHeld(Minecraft.getInstance(), var2)) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND && !MapSwapInventory.hasSelectedItem()) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (CardForce.getActive().blocksUserInteraction() || DispenserSpoofer.getInstance().isTotemBucketSpoof(var1.getItemInHand(var2))) {
         var3.setReturnValue(InteractionResult.FAIL);
      } else if (var2 == InteractionHand.MAIN_HAND) {
         FishingRodSwapper var4 = FishingRodSwapper.getInstance();
         if (!var4.isLocked() && var4.isAutoSwapReady()) {
            Minecraft var5 = Minecraft.getInstance();
            int var6 = var4.findRodSlot();
            if (var6 >= 0 && var6 <= 8) {
               int var7 = var1.getInventory().getSelectedSlot();
               if (var7 != var6) {
                  var4.lock();

                  try {
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var6));
                     InteractionResult var8 = var5.gameMode.useItem(var1, var2);
                     var5.getConnection().send(new ServerboundSetCarriedItemPacket(var7));
                     var3.setReturnValue(var8);
                  } finally {
                     var4.unlock();
                  }
               }
            }
         }
      }
   }
}

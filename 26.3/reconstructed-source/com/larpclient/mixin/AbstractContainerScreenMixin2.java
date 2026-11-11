package com.larpclient.mixin;

import com.larpclient.hud.VisualInventory;
import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.misc.AuctionUtil;
import com.larpclient.util.misc.PayNotice;
import com.larpclient.util.world.FakeSpawnerController;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin2 {
   @Shadow
   protected AbstractContainerMenu menu;
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected Set<Slot> quickCraftSlots;

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private Slot R(Slot var1) {
      return VisualInventory.wrapSlot(this.menu, var1);
   }

   @Inject(method = "getHoveredSlot", at = @At("RETURN"), cancellable = true)
   private void h(double var1, double var3, CallbackInfoReturnable<Slot> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(VisualInventory.findFreeSlot(this.menu, (Slot)var5.getReturnValue(), this.quickCraftSlots));
      }
   }

   @WrapOperation(
      method = {"extractSlot", "extractCarriedItem"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack G(ItemStack var1, int var2, Operation<ItemStack> var3) {
      return FakeSpawnerController.getInstance().trackDerivedStack(var1, (ItemStack)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "extractSlots", at = @At("TAIL"))
   private void c(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      if (this.hoveredSlot != null) {
         this.hoveredSlot = VisualInventory.wrapSlot(this.menu, this.hoveredSlot);
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void H(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (VisualInventory.isTransferBusy()) {
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP && FakeSpawnerController.getInstance().getSavePath(this.menu, var1, var3)) {
         var5.cancel();
      } else if (VisualInventory.isVirtualGap(var1)) {
         PlayerMessages.sendMessage(
            Component.literal("This slot is reserved or awaiting real items. Free a physical slot or finish the operation first."), true
         );
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP_ALL && FakeSpawnerController.getInstance().restorePiles(this.menu.getCarried())) {
         PlayerMessages.sendMessage(Component.literal("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && VisualInventory.isActive()) {
         VisualInventory.swapSlots(this.menu, var1, var3);
         var5.cancel();
      } else {
         if (var4 == ContainerInput.QUICK_MOVE) {
            FakeSpawnerController.getInstance().snapshotInventory(var1);
         }

         if (var4 == ContainerInput.SWAP && var1 != null && var1.getItem().is(Items.BONE) && var3 >= 0 && var3 < 9) {
            FakeSpawnerController.getInstance().snapshotInventory(VisualInventory.getPhysicalSlot(var3));
         }

         FakeSpawnerController.getInstance().tickBreakAttempts(this.menu, var1);
         VisualInventory.onSlotPickup(var1);
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void S(CallbackInfo var1) {
      FakeSpawnerController.getInstance().restorePiles(((Screen)this).getTitle(), this.menu);
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void R(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var1 != null && var4 == ContainerInput.PICKUP && AuctionUtil.isSellPrompt(var1.getItem())) {
         long var6 = AuctionUtil.getContainerWorth();
         if (var6 > 0L) {
            PayNotice.show(var6);
         }
      }
   }
}

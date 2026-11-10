package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.k4;
import com.larpclient.ki;
import com.larpclient.px;
import com.larpclient.za;
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
public abstract class q54e7dfeb381c1300 {
   @Shadow
   protected AbstractContainerMenu menu;
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected Set<Slot> quickCraftSlots;

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private Slot I(Slot var1) {
      return ki.x(this.menu, var1);
   }

   @Inject(method = "getHoveredSlot", at = @At("RETURN"), cancellable = true)
   private void w(double var1, double var3, CallbackInfoReturnable<Slot> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(ki.e(this.menu, (Slot)var5.getReturnValue(), this.quickCraftSlots));
      }
   }

   @WrapOperation(
      method = {"extractSlot", "extractCarriedItem"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack N(ItemStack var1, int var2, Operation<ItemStack> var3) {
      return b8.k().c(var1, (ItemStack)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "extractSlots", at = @At("TAIL"))
   private void u(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      if (this.hoveredSlot != null) {
         this.hoveredSlot = ki.x(this.menu, this.hoveredSlot);
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void p(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (ki.y()) {
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP && b8.k().f(this.menu, var1, var3)) {
         var5.cancel();
      } else if (ki.z(var1)) {
         k4.f(Component.literal("This slot is reserved or awaiting real items. Free a physical slot or finish the operation first."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP_ALL && b8.k().o(this.menu.getCarried())) {
         k4.f(Component.literal("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && ki.k()) {
         ki.z(this.menu, var1, var3);
         var5.cancel();
      } else {
         if (var4 == ContainerInput.QUICK_MOVE) {
            b8.k().E(var1);
         }

         if (var4 == ContainerInput.SWAP && var1 != null && var1.getItem().is(Items.BONE) && var3 >= 0 && var3 < 9) {
            b8.k().Y(ki.t(var3));
         }

         b8.k().r(this.menu, var1);
         ki.K(var1);
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void G(CallbackInfo var1) {
      b8.k().V(((Screen)this).getTitle(), this.menu);
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void H(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var1 != null && var4 == ContainerInput.PICKUP && px.C(var1.getItem())) {
         long var6 = px.I();
         if (var6 > 0L) {
            za.j(var6);
         }
      }
   }
}

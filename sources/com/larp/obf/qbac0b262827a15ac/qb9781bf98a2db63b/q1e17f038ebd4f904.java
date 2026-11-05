package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.d1;
import com.larpclient.dr;
import com.larpclient.k9;
import com.larpclient.u0;
import com.larpclient.ws;
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
public abstract class q1e17f038ebd4f904 {
   @Shadow
   protected AbstractContainerMenu menu;
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected Set<Slot> quickCraftSlots;

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private Slot k(Slot var1) {
      return u0.W(this.menu, var1);
   }

   @Inject(method = "getHoveredSlot", at = @At("RETURN"), cancellable = true)
   private void H(double var1, double var3, CallbackInfoReturnable<Slot> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(u0.g(this.menu, (Slot)var5.getReturnValue(), this.quickCraftSlots));
      }
   }

   @WrapOperation(
      method = {"extractSlot", "extractCarriedItem"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack I(ItemStack var1, int var2, Operation<ItemStack> var3) {
      return dr.z().m(var1, (ItemStack)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "extractSlots", at = @At("TAIL"))
   private void p(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      if (this.hoveredSlot != null) {
         this.hoveredSlot = u0.W(this.menu, this.hoveredSlot);
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void A(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (u0.f()) {
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP && dr.z().p(this.menu, var1, var3)) {
         var5.cancel();
      } else if (u0.w(var1)) {
         ws.c(Component.literal("This slot is reserved or awaiting real items. Free a physical slot or finish the operation first."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP_ALL && dr.z().y(this.menu.getCarried())) {
         ws.c(Component.literal("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && u0.w()) {
         u0.i(this.menu, var1, var3);
         var5.cancel();
      } else {
         if (var4 == ContainerInput.QUICK_MOVE) {
            dr.z().f(var1);
         }

         if (var4 == ContainerInput.SWAP && var1 != null && var1.getItem().is(Items.BONE) && var3 >= 0 && var3 < 9) {
            dr.z().Q(u0.f(var3));
         }

         dr.z().X(this.menu, var1);
         u0.I(var1);
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void c(CallbackInfo var1) {
      dr.z().B(((Screen)this).getTitle(), this.menu);
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void e(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var1 != null && var4 == ContainerInput.PICKUP && d1.I(var1.getItem())) {
         long var6 = d1.Q();
         if (var6 > 0L) {
            k9.S(var6);
         }
      }
   }
}

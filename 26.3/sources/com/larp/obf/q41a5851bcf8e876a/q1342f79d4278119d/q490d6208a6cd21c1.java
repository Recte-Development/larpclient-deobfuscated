package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient._8;
import com.larpclient.h4;
import com.larpclient.iw;
import com.larpclient.jn;
import com.larpclient.nb;
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
public abstract class q490d6208a6cd21c1 {
   @Shadow
   protected AbstractContainerMenu menu;
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected Set<Slot> quickCraftSlots;

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private Slot R(Slot var1) {
      return _8.W(this.menu, var1);
   }

   @Inject(method = "getHoveredSlot", at = @At("RETURN"), cancellable = true)
   private void h(double var1, double var3, CallbackInfoReturnable<Slot> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(_8.y(this.menu, (Slot)var5.getReturnValue(), this.quickCraftSlots));
      }
   }

   @WrapOperation(
      method = {"extractSlot", "extractCarriedItem"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack G(ItemStack var1, int var2, Operation<ItemStack> var3) {
      return jn.g().q(var1, (ItemStack)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "extractSlots", at = @At("TAIL"))
   private void c(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      if (this.hoveredSlot != null) {
         this.hoveredSlot = _8.W(this.menu, this.hoveredSlot);
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void H(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (_8.e()) {
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP && jn.g().n(this.menu, var1, var3)) {
         var5.cancel();
      } else if (_8.z(var1)) {
         iw.V(Component.literal("This slot is reserved or awaiting real items. Free a physical slot or finish the operation first."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP_ALL && jn.g().o(this.menu.getCarried())) {
         iw.V(Component.literal("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && _8.S()) {
         _8.k(this.menu, var1, var3);
         var5.cancel();
      } else {
         if (var4 == ContainerInput.QUICK_MOVE) {
            jn.g().f(var1);
         }

         if (var4 == ContainerInput.SWAP && var1 != null && var1.getItem().is(Items.BONE) && var3 >= 0 && var3 < 9) {
            jn.g().f(_8.t(var3));
         }

         jn.g().y(this.menu, var1);
         _8.L(var1);
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void S(CallbackInfo var1) {
      jn.g().o(((Screen)this).getTitle(), this.menu);
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void R(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var1 != null && var4 == ContainerInput.PICKUP && nb.n(var1.getItem())) {
         long var6 = nb.E();
         if (var6 > 0L) {
            h4.C(var6);
         }
      }
   }
}

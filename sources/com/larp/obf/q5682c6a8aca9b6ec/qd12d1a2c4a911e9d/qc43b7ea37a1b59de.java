package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.hr;
import com.larpclient.jb;
import com.larpclient.lj;
import com.larpclient.mx;
import com.larpclient.ul;
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
public abstract class qc43b7ea37a1b59de {
   @Shadow
   protected AbstractContainerMenu menu;
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   @Final
   protected Set<Slot> quickCraftSlots;

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private Slot A(Slot var1) {
      return mx.v(this.menu, var1);
   }

   @Inject(method = "getHoveredSlot", at = @At("RETURN"), cancellable = true)
   private void L(double var1, double var3, CallbackInfoReturnable<Slot> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(mx.B(this.menu, (Slot)var5.getReturnValue(), this.quickCraftSlots));
      }
   }

   @WrapOperation(
      method = {"extractSlot", "extractCarriedItem"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;copyWithCount(I)Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack Y(ItemStack var1, int var2, Operation<ItemStack> var3) {
      return lj.j().B(var1, (ItemStack)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "extractSlots", at = @At("TAIL"))
   private void d(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      if (this.hoveredSlot != null) {
         this.hoveredSlot = mx.v(this.menu, this.hoveredSlot);
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void t(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (mx.m()) {
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP && lj.j().m(this.menu, var1, var3)) {
         var5.cancel();
      } else if (mx.r(var1)) {
         jb.v(Component.literal("This slot is reserved or awaiting real items. Free a physical slot or finish the operation first."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.PICKUP_ALL && lj.j().g(this.menu.getCarried())) {
         jb.v(Component.literal("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && mx.U()) {
         mx.b(this.menu, var1, var3);
         var5.cancel();
      } else {
         if (var4 == ContainerInput.QUICK_MOVE) {
            lj.j().e(var1);
         }

         if (var4 == ContainerInput.SWAP && var1 != null && var1.getItem().is(Items.BONE) && var3 >= 0 && var3 < 9) {
            lj.j().x(mx.V(var3));
         }

         lj.j().c(this.menu, var1);
         mx.E(var1);
      }
   }

   @Inject(method = "removed", at = @At("HEAD"))
   private void s(CallbackInfo var1) {
      lj.j().J(((Screen)this).getTitle(), this.menu);
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void I(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var1 != null && var4 == ContainerInput.PICKUP && hr.C(var1.getItem())) {
         long var6 = hr.h();
         if (var6 > 0L) {
            ul.p(var6);
         }
      }
   }
}

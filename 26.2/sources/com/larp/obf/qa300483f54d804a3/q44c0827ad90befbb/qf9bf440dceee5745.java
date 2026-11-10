package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cc;
import com.larpclient.mg;
import com.larpclient.we;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class qf9bf440dceee5745 {
   @Unique
   private boolean b;

   @ModifyVariable(method = "setSelectedSlot", at = @At("HEAD"), argsOnly = true)
   private int p(int var1) {
      return ((Inventory)this).player == Minecraft.getInstance().player && !cc.z().v() ? mg.t(var1) : var1;
   }

   @Inject(method = "removeFromSelected", at = @At("HEAD"))
   private void u(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      if (((Inventory)this).player == Minecraft.getInstance().player) {
         mg.V();
      }
   }

   @Inject(method = "removeFromSelected", at = @At("RETURN"))
   private void f(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      Inventory var3 = (Inventory)this;
      if (var3.player == Minecraft.getInstance().player && !((ItemStack)var2.getReturnValue()).isEmpty()) {
         we.Y().B(var3.getSelectedSlot());
      }
   }

   @Inject(method = "setSelectedSlot", at = @At("HEAD"), cancellable = true)
   private void J(int var1, CallbackInfo var2) {
      if (!this.b) {
         cc var3 = cc.z();
         if (var3.v()) {
            var3.l(var1);
            if (var1 == var3.W()) {
               int var4 = (var1 + 8) % 9;
               var2.cancel();
               this.b = true;

               try {
                  ((Inventory)this).setSelectedSlot(var4);
               } finally {
                  this.b = false;
               }
            }
         }
      }
   }
}

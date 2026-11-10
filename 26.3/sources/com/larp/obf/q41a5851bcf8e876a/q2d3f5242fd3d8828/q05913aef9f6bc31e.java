package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.hh;
import com.larpclient.qm;
import com.larpclient.vy;
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
public class q05913aef9f6bc31e {
   @Unique
   private boolean u;

   @ModifyVariable(method = "setSelectedSlot", at = @At("HEAD"), argsOnly = true)
   private int O(int var1) {
      return ((Inventory)this).player == Minecraft.getInstance().player && !hh.R().U() ? qm.c(var1) : var1;
   }

   @Inject(method = "removeFromSelected", at = @At("HEAD"))
   private void t(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      if (((Inventory)this).player == Minecraft.getInstance().player) {
         qm.p();
      }
   }

   @Inject(method = "removeFromSelected", at = @At("RETURN"))
   private void f(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      Inventory var3 = (Inventory)this;
      if (var3.player == Minecraft.getInstance().player && !((ItemStack)var2.getReturnValue()).isEmpty()) {
         vy.a().v(var3.getSelectedSlot());
      }
   }

   @Inject(method = "setSelectedSlot", at = @At("HEAD"), cancellable = true)
   private void V(int var1, CallbackInfo var2) {
      if (!this.u) {
         hh var3 = hh.R();
         if (var3.U()) {
            var3.A(var1);
            if (var1 == var3.t()) {
               int var4 = (var1 + 8) % 9;
               var2.cancel();
               this.u = true;

               try {
                  ((Inventory)this).setSelectedSlot(var4);
               } finally {
                  this.u = false;
               }
            }
         }
      }
   }
}

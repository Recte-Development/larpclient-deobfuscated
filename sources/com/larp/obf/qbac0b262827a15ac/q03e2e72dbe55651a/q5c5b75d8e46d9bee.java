package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.pj;
import com.larpclient.w5;
import com.larpclient.ze;
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
public class q5c5b75d8e46d9bee {
   @Unique
   private boolean E;

   @ModifyVariable(method = "setSelectedSlot", at = @At("HEAD"), argsOnly = true)
   private int y(int var1) {
      return ((Inventory)this).player == Minecraft.getInstance().player && !pj.U().c() ? w5.h(var1) : var1;
   }

   @Inject(method = "removeFromSelected", at = @At("HEAD"))
   private void Z(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      if (((Inventory)this).player == Minecraft.getInstance().player) {
         w5.Z();
      }
   }

   @Inject(method = "removeFromSelected", at = @At("RETURN"))
   private void g(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      Inventory var3 = (Inventory)this;
      if (var3.player == Minecraft.getInstance().player && !((ItemStack)var2.getReturnValue()).isEmpty()) {
         ze.c().o(var3.getSelectedSlot());
      }
   }

   @Inject(method = "setSelectedSlot", at = @At("HEAD"), cancellable = true)
   private void V(int var1, CallbackInfo var2) {
      if (!this.E) {
         pj var3 = pj.U();
         if (var3.c()) {
            var3.A(var1);
            if (var1 == var3.B()) {
               int var4 = (var1 + 8) % 9;
               var2.cancel();
               this.E = true;

               try {
                  ((Inventory)this).setSelectedSlot(var4);
               } finally {
                  this.E = false;
               }
            }
         }
      }
   }
}

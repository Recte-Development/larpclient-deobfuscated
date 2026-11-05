package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.eg;
import com.larpclient.ug;
import com.larpclient.yg;
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
public class q8ea74c9ef521e97b {
   @Unique
   private boolean I;

   @ModifyVariable(method = "setSelectedSlot", at = @At("HEAD"), argsOnly = true)
   private int k(int var1) {
      return ((Inventory)this).player == Minecraft.getInstance().player && !eg.y().G() ? yg.K(var1) : var1;
   }

   @Inject(method = "removeFromSelected", at = @At("HEAD"))
   private void s(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      if (((Inventory)this).player == Minecraft.getInstance().player) {
         yg.T();
      }
   }

   @Inject(method = "removeFromSelected", at = @At("RETURN"))
   private void R(boolean var1, CallbackInfoReturnable<ItemStack> var2) {
      Inventory var3 = (Inventory)this;
      if (var3.player == Minecraft.getInstance().player && !((ItemStack)var2.getReturnValue()).isEmpty()) {
         ug.x().m(var3.getSelectedSlot());
      }
   }

   @Inject(method = "setSelectedSlot", at = @At("HEAD"), cancellable = true)
   private void P(int var1, CallbackInfo var2) {
      if (!this.I) {
         eg var3 = eg.y();
         if (var3.G()) {
            var3.O(var1);
            if (var1 == var3.g()) {
               int var4 = (var1 + 8) % 9;
               var2.cancel();
               this.I = true;

               try {
                  ((Inventory)this).setSelectedSlot(var4);
               } finally {
                  this.I = false;
               }
            }
         }
      }
   }
}

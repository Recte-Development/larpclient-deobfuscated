package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import com.larpclient.u0;
import com.larpclient.wt;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class q14e4a9f270f236cc {
   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
   private boolean n(KeyMapping var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(new Object[]{var1});
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && u0.w()) {
         dr var5 = dr.z();
         boolean var6 = var5.t(var4.player.getInventory().getSelectedSlot());
         if (var1 == var4.options.keyDrop && var6) {
            return false;
         } else {
            return var1 != var4.options.keySwapOffhand || !var6 && !var5.t(40) && !wt.v() ? var3 : false;
         }
      } else {
         return var3;
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void b(Inventory var1, int var2, Operation<Void> var3) {
      if (u0.w()) {
         u0.K(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }
}

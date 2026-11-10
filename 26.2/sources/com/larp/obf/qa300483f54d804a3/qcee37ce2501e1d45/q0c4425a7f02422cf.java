package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.ki;
import com.larpclient.m3;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class q0c4425a7f02422cf {
   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
   private boolean H(KeyMapping var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(new Object[]{var1});
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && ki.k()) {
         b8 var5 = b8.k();
         boolean var6 = var5.t(var4.player.getInventory().getSelectedSlot());
         if (var1 == var4.options.keyDrop && var6) {
            return false;
         } else {
            return var1 != var4.options.keySwapOffhand || !var6 && !var5.t(40) && !m3.u() ? var3 : false;
         }
      } else {
         return var3;
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void A(Inventory var1, int var2, Operation<Void> var3) {
      if (ki.k()) {
         ki.x(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }
}

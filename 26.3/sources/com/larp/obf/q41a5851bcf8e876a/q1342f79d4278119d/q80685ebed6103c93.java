package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient._8;
import com.larpclient.jn;
import com.larpclient.q7;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class q80685ebed6103c93 {
   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
   private boolean o(KeyMapping var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(new Object[]{var1});
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && _8.S()) {
         jn var5 = jn.g();
         boolean var6 = var5.G(var4.player.getInventory().getSelectedSlot());
         if (var1 == var4.options.keyDrop && var6) {
            return false;
         } else {
            return var1 != var4.options.keySwapOffhand || !var6 && !var5.G(40) && !q7.M() ? var3 : false;
         }
      } else {
         return var3;
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void S(Inventory var1, int var2, Operation<Void> var3) {
      if (_8.S()) {
         _8.s(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }
}

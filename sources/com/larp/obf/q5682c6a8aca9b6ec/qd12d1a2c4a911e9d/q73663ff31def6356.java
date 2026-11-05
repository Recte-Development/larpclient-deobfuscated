package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.lj;
import com.larpclient.mx;
import com.larpclient.rm;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public abstract class q73663ff31def6356 {
   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z"))
   private boolean p(KeyMapping var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(new Object[]{var1});
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && mx.U()) {
         lj var5 = lj.j();
         boolean var6 = var5.U(var4.player.getInventory().getSelectedSlot());
         if (var1 == var4.options.keyDrop && var6) {
            return false;
         } else {
            return var1 != var4.options.keySwapOffhand || !var6 && !var5.U(40) && !rm.L() ? var3 : false;
         }
      } else {
         return var3;
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
   private void T(Inventory var1, int var2, Operation<Void> var3) {
      if (mx.U()) {
         mx.P(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }
}

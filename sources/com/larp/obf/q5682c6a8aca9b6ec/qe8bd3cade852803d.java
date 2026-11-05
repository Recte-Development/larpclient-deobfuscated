package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.ei;
import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class qe8bd3cade852803d extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void n(CallbackInfo var1) {
      Input var2 = OrderDropperModule.Q();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = ei.r(var2);
      }
   }
}

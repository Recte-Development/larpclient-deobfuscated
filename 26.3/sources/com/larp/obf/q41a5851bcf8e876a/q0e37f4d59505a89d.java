package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.hw;
import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q0e37f4d59505a89d extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void q(CallbackInfo var1) {
      Input var2 = OrderDropperModule.e();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = hw.w(var2);
      }
   }
}

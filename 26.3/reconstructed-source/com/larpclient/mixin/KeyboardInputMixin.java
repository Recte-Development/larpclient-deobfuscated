package com.larpclient.mixin;

import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.util.math.RotationSmoother2;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void q(CallbackInfo var1) {
      Input var2 = OrderDropperModule.getAfkInput();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = RotationSmoother2.inputToVector(var2);
      }
   }
}

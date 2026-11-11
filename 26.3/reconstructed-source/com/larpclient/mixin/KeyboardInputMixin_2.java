package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import com.larpclient.module.movement.FreecamInput;
import com.larpclient.module.render.FreecamModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin_2 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void y(CallbackInfo var1) {
      if (ViewController.isFreecam()) {
         this.keyPresses = FreecamModule.setGuidedSpeed(this.keyPresses);
         this.moveVector = FreecamInput.reset(this.keyPresses);
      }
   }
}

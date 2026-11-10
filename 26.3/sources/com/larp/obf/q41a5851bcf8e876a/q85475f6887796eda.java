package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.b7;
import com.larpclient.ij;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q85475f6887796eda extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void y(CallbackInfo var1) {
      if (ij.y()) {
         this.keyPresses = FreecamModule.D(this.keyPresses);
         this.moveVector = b7.A(this.keyPresses);
      }
   }
}

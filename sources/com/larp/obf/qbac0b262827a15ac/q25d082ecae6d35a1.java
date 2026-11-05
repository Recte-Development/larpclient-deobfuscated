package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.hs;
import com.larpclient.tq;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q25d082ecae6d35a1 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void l(CallbackInfo var1) {
      if (tq.f()) {
         this.keyPresses = FreecamModule.P(this.keyPresses);
         this.moveVector = hs.y(this.keyPresses);
      }
   }
}

package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.rr;
import com.larpclient.yw;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q74ee97054d781690 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void P(CallbackInfo var1) {
      if (rr.o()) {
         this.keyPresses = FreecamModule.Z(this.keyPresses);
         this.moveVector = yw.g(this.keyPresses);
      }
   }
}

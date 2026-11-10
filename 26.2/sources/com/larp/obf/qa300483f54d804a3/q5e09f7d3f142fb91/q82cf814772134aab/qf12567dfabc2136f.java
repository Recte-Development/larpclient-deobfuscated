package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q82cf814772134aab;

import com.larpclient.af;
import com.larpclient.hc;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class qf12567dfabc2136f extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void X(CallbackInfo var1) {
      Input var2 = af.B();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = hc.D(var2);
      }
   }
}

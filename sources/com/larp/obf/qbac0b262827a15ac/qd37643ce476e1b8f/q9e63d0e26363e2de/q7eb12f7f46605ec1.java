package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q9e63d0e26363e2de;

import com.larpclient.ob;
import com.larpclient.wq;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q7eb12f7f46605ec1 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void V(CallbackInfo var1) {
      Input var2 = wq.z();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = ob.M(var2);
      }
   }
}

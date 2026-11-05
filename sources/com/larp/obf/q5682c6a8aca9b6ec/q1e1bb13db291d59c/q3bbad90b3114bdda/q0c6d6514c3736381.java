package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q3bbad90b3114bdda;

import com.larpclient.ei;
import com.larpclient.k;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q0c6d6514c3736381 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void W(CallbackInfo var1) {
      Input var2 = k.E();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = ei.r(var2);
      }
   }
}

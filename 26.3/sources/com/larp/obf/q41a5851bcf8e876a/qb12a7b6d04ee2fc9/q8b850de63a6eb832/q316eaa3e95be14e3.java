package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q8b850de63a6eb832;

import com.larpclient._n;
import com.larpclient.hw;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q316eaa3e95be14e3 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void K(CallbackInfo var1) {
      Input var2 = _n.q();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = hw.w(var2);
      }
   }
}

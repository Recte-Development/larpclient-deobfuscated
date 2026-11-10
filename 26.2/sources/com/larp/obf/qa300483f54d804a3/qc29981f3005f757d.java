package com.larp.obf.qa300483f54d804a3;

import com.larpclient.hc;
import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class qc29981f3005f757d extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void V(CallbackInfo var1) {
      Input var2 = OrderDropperModule.z();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = hc.D(var2);
      }
   }
}

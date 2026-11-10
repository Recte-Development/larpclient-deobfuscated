package com.larp.obf.qa300483f54d804a3;

import com.larpclient.cr;
import com.larpclient.p_;
import com.larpclient.module.FreecamModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q48e5c29a6fadd919 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void f(CallbackInfo var1) {
      if (p_.V()) {
         this.keyPresses = FreecamModule.T(this.keyPresses);
         this.moveVector = cr.v(this.keyPresses);
      }
   }
}

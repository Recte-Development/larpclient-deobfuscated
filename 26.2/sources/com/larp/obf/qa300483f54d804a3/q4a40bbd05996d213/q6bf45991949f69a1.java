package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import com.larpclient.a4;
import com.larpclient.ac;
import com.larpclient.ae;
import com.larpclient.ak;
import com.larpclient.hc;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q6bf45991949f69a1 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void X(CallbackInfo var1) {
      Input var2 = ac.n();
      if (var2 == null) {
         var2 = a4.a();
      }

      if (var2 == null) {
         var2 = ae.r();
      }

      if (var2 == null) {
         var2 = ak.Q();
      }

      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = hc.D(var2);
      }
   }
}

package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.ob;
import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class qa251466578a08b1e extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void H(CallbackInfo var1) {
      Input var2 = OrderDropperModule.X();
      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = ob.M(var2);
      }
   }
}

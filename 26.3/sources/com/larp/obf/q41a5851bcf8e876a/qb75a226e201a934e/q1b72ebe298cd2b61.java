package com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e;

import com.larpclient._6;
import com.larpclient._7;
import com.larpclient._d;
import com.larpclient._z;
import com.larpclient.hw;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class q1b72ebe298cd2b61 extends ClientInput {
   @Inject(method = "tick", at = @At("TAIL"))
   private void X(CallbackInfo var1) {
      Input var2 = _7.z();
      if (var2 == null) {
         var2 = _z.x();
      }

      if (var2 == null) {
         var2 = _6.i();
      }

      if (var2 == null) {
         var2 = _d.a();
      }

      if (var2 != null) {
         this.keyPresses = var2;
         this.moveVector = hw.w(var2);
      }
   }
}

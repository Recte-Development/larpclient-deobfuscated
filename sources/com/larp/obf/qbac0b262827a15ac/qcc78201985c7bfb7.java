package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.w7;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class qcc78201985c7bfb7 {
   @Inject(method = "onAttack", at = @At("RETURN"))
   private void H(CallbackInfo var1) {
      w7.I((Player)this);
   }
}

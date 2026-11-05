package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.j5;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class q708b9d77f9b03c95 {
   @Inject(method = "onAttack", at = @At("RETURN"))
   private void e(CallbackInfo var1) {
      j5.o((Player)this);
   }
}

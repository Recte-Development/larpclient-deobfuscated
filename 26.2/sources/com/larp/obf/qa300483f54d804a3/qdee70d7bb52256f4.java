package com.larp.obf.qa300483f54d804a3;

import com.larpclient.si;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class qdee70d7bb52256f4 {
   @Inject(method = "onAttack", at = @At("RETURN"))
   private void x(CallbackInfo var1) {
      si.o((Player)this);
   }
}

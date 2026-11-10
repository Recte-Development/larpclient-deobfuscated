package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qg;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class qcc596652d6bcd1bc {
   @Inject(method = "onAttack", at = @At("RETURN"))
   private void f(CallbackInfo var1) {
      qg.f((Player)this);
   }
}

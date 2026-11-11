package com.larpclient.mixin;

import com.larpclient.util.entity.CombatUtil;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin_2 {
   @Inject(method = "onAttack", at = @At("RETURN"))
   private void f(CallbackInfo var1) {
      CombatUtil.markPlayerTicked((Player)this);
   }
}

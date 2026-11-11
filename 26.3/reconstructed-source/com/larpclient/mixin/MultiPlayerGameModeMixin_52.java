package com.larpclient.mixin;

import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class MultiPlayerGameModeMixin_52 {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void d(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (FiftyFiftyDispenser.getInstance().shouldBlockInput() || BlackjackRigger.getInstance().isHidingCards()) {
         var3.cancel();
      }
   }
}

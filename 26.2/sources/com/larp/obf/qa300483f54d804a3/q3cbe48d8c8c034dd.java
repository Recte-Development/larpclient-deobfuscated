package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.AutoTotemModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q3cbe48d8c8c034dd {
   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getTutorial()Lnet/minecraft/client/tutorial/Tutorial;"))
   private void p(CallbackInfo var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      if (AutoTotemModule.T(var2)) {
         var2.input.keyPresses = Input.EMPTY;
         var2.input.moveVector = Vec2.ZERO;
      }
   }
}

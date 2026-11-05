package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.xs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qdf5f118906ff0a85 {
   @Inject(method = "closeContainer", at = @At("HEAD"))
   private void B(CallbackInfo var1) {
      xs.Z(Minecraft.getInstance());
   }
}

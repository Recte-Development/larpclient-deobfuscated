package com.larp.obf.qa300483f54d804a3;

import com.larpclient.gy;
import com.larpclient.ly;
import com.larpclient.my;
import com.larpclient.wa;
import net.minecraft.client.renderer.ShaderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShaderManager.class)
public class q5ddb7e53eb532a11 {
   @Inject(
      method = "apply(Lnet/minecraft/client/renderer/ShaderManager$Configs;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
      at = @At("TAIL")
   )
   private void Y(CallbackInfo var1) {
      wa.x();
      my.K();
      ly.a();
      gy.B();
   }
}

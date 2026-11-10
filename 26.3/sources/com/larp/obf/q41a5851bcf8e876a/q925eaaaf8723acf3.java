package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.PerformanceModule;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelExtractor.class)
public abstract class q925eaaaf8723acf3 {
   @ModifyArg(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
   private double N(double var1) {
      return PerformanceModule.u(var1);
   }
}

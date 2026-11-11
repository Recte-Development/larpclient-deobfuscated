package com.larpclient.mixin;

import com.larpclient.module.misc.PerformanceModule;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelExtractor.class)
public abstract class LevelExtractorMixin {
   @ModifyArg(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
   private double N(double var1) {
      return PerformanceModule.adjustCloudStatus(var1);
   }
}

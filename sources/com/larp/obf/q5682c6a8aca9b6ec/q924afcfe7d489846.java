package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.PerformanceModule;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelExtractor.class)
public abstract class q924afcfe7d489846 {
   @ModifyArg(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
   private double y(double var1) {
      return PerformanceModule.j(var1);
   }
}

package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.PerformanceModule;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelExtractor.class)
public abstract class q54988627b3ea9dee {
   @ModifyArg(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
   private double W(double var1) {
      return PerformanceModule.e(var1);
   }
}

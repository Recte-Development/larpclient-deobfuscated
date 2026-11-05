package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.PerformanceModule;
import net.minecraft.client.renderer.extract.LevelExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelExtractor.class)
public abstract class q7457870a49d7930f {
   @ModifyArg(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setViewScale(D)V"))
   private double J(double var1) {
      return PerformanceModule.u(var1);
   }
}

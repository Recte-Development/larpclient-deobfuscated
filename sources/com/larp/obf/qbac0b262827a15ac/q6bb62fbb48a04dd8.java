package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface q6bb62fbb48a04dd8 {
   @Invoker("addLayer")
   boolean larp$addLayer(RenderLayer var1);
}

package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface q98ce83f18e93a418 {
   @Invoker("addLayer")
   boolean larp$addLayer(RenderLayer var1);
}

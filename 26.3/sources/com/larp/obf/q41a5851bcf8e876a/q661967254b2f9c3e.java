package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface q661967254b2f9c3e {
   @Invoker("addLayer")
   boolean larp$addLayer(RenderLayer var1);
}

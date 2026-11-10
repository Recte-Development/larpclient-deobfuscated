package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntityRenderer.class)
public interface q5bad0598f25603b6 {
   @Invoker("addLayer")
   boolean larp$addLayer(RenderLayer var1);
}

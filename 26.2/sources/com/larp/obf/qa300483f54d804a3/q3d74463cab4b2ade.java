package com.larp.obf.qa300483f54d804a3;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface q3d74463cab4b2ade {
   @Invoker("bobHurt")
   void larpBobHurt(CameraRenderState var1, PoseStack var2);

   @Invoker("bobView")
   void larpBobView(CameraRenderState var1, PoseStack var2);

   @Accessor("spinningEffectTime")
   float larpSpinningEffectTime();

   @Accessor("spinningEffectSpeed")
   float larpSpinningEffectSpeed();
}

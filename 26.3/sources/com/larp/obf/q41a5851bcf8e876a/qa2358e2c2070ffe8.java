package com.larp.obf.q41a5851bcf8e876a;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface qa2358e2c2070ffe8 {
   @Invoker("bobHurt")
   void larpBobHurt(CameraRenderState var1, PoseStack var2);

   @Invoker("bobView")
   void larpBobView(CameraRenderState var1, PoseStack var2);
}

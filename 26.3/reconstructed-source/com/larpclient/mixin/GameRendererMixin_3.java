package com.larpclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererMixin_3 {
   @Invoker("bobHurt")
   void larpBobHurt(CameraRenderState var1, PoseStack var2);

   @Invoker("bobView")
   void larpBobView(CameraRenderState var1, PoseStack var2);
}

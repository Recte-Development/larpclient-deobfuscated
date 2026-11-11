package com.larpclient.mixin;

import com.larpclient.module.misc.EmotesModule;
import com.larpclient.render.entity.PoseOverride;
import com.larpclient.social.emote.EmoteState;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin_3 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void w(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         EmotesModule.applyToRenderState(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((EmoteState)var2).larp$setEmoteWeight(0.0F);
         LarpLog.errorOnce("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(
      method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
      at = @At("RETURN")
   )
   private void Y(AvatarRenderState var1, PoseStack var2, float var3, float var4, CallbackInfo var5) {
      EmoteState var6 = (EmoteState)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         PoseOverride.applyRootTransform(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }
}

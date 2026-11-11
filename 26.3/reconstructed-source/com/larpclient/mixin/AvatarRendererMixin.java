package com.larpclient.mixin;

import com.larpclient.event.AvatarRenderHooks;
import com.larpclient.render.entity.KorbloxLegLayer;
import com.larpclient.social.cosmetic.SkinMarker;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
   @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At("TAIL"))
   private void h(Context var1, boolean var2, CallbackInfo var3) {
      AvatarRenderer var4 = (AvatarRenderer)this;
      ((SkinMarker)var4.getModel()).larp$markSkin();
      ((LivingEntityRendererMixin_2)this).larp$addLayer(new KorbloxLegLayer(var4));
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void o(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         AvatarRenderHooks.extractRenderState(var1, var2);
      } catch (RuntimeException var6) {
         AvatarRenderHooks.resetRenderState(var2);
         LarpLog.errorOnce("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }
}

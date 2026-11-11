package com.larpclient.mixin;

import com.larpclient.event.AvatarRenderHooks;
import com.larpclient.social.cosmetic.SkinMarker;
import com.larpclient.social.cosmetic.SkinOverrideAccess;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin implements SkinMarker {
   private static final String e = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V";
   @Unique
   private boolean W;
   @Unique
   private boolean s;

   @Override
   public void larp$markSkin() {
      this.W = true;
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void P(AvatarRenderState var1, CallbackInfo var2) {
      if (this.s) {
         ((PlayerModel)this).head.visible = true;
         this.s = false;
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void L(AvatarRenderState var1, CallbackInfo var2) {
      SkinOverrideAccess var3 = (SkinOverrideAccess)var1;
      if (AvatarRenderHooks.hideModelParts((PlayerModel)this, this.W, var3.larp$headless(), var3.larp$korblox())) {
         this.s = true;
      }
   }
}

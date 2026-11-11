package com.larpclient.mixin;

import com.larpclient.render.entity.PoseOverride;
import com.larpclient.social.emote.EmoteState;
import com.larpclient.util.entity.WalkRigAccess;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
   private void p(HumanoidRenderState var1, CallbackInfo var2) {
      if (var1 instanceof EmoteState var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            PoseOverride.applyToModel((HumanoidModel<?>)this, var3.larp$emoteSample(), var4);
            if (this instanceof PlayerModel && var1 instanceof AvatarRenderState var5) {
               float[] var6 = PoseOverride.getLimbSwings(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((WalkRigAccess)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}

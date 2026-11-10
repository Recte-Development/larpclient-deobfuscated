package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._x;
import com.larpclient.f0;
import com.larpclient.jc;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class q3d2304540bd5bf26 {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
   private void p(HumanoidRenderState var1, CallbackInfo var2) {
      if (var1 instanceof f0 var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            _x.U((HumanoidModel<?>)this, var3.larp$emoteSample(), var4);
            if (this instanceof PlayerModel && var1 instanceof AvatarRenderState var5) {
               float[] var6 = _x.N(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((jc)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}

package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.xe;
import com.larpclient.y1;
import com.larpclient.yo;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class q483ec88edbf808bb {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
   private void d(HumanoidRenderState var1, CallbackInfo var2) {
      if (var1 instanceof yo var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            y1.p((HumanoidModel<?>)this, var3.larp$emoteSample(), var4);
            if (this instanceof PlayerModel && var1 instanceof AvatarRenderState var5) {
               float[] var6 = y1.t(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((xe)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}

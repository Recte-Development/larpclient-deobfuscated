package com.larp.obf.qa300483f54d804a3;

import com.larpclient.gn;
import com.larpclient.kp;
import com.larpclient.lf;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class qf206ba19551ecbce {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
   private void B(HumanoidRenderState var1, CallbackInfo var2) {
      if (var1 instanceof gn var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            kp.y((HumanoidModel<?>)this, var3.larp$emoteSample(), var4);
            if (this instanceof PlayerModel && var1 instanceof AvatarRenderState var5) {
               float[] var6 = kp.M(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((lf)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}

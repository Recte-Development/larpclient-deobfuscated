package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._b;
import com.larpclient.cu;
import com.larpclient.o6;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class q1f2a51c7935ef3a4 {
   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
   private void n(HumanoidRenderState var1, CallbackInfo var2) {
      if (var1 instanceof cu var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            o6.I((HumanoidModel<?>)this, var3.larp$emoteSample(), var4);
            if (this instanceof PlayerModel && var1 instanceof AvatarRenderState var5) {
               float[] var6 = o6.k(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((_b)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}

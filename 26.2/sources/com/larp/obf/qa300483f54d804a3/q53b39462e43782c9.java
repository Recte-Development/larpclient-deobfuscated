package com.larp.obf.qa300483f54d804a3;

import com.larpclient.gi;
import com.larpclient.hl;
import com.larpclient.p7;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class q53b39462e43782c9 implements hl {
   private static final String t = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V";
   @Unique
   private boolean L;
   @Unique
   private boolean y;

   @Override
   public void larp$markSkin() {
      this.L = true;
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void M(AvatarRenderState var1, CallbackInfo var2) {
      if (this.y) {
         ((PlayerModel)this).head.visible = true;
         this.y = false;
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void v(AvatarRenderState var1, CallbackInfo var2) {
      p7 var3 = (p7)var1;
      if (gi.M((PlayerModel)this, this.L, var3.larp$headless(), var3.larp$korblox())) {
         this.y = true;
      }
   }
}

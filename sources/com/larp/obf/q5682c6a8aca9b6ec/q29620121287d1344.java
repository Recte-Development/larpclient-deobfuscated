package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.hb;
import com.larpclient.p1;
import com.larpclient.pq;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class q29620121287d1344 implements hb {
   private static final String Y = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V";
   @Unique
   private boolean r;
   @Unique
   private boolean L;

   @Override
   public void larp$markSkin() {
      this.r = true;
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void W(AvatarRenderState var1, CallbackInfo var2) {
      if (this.L) {
         ((PlayerModel)this).head.visible = true;
         this.L = false;
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void V(AvatarRenderState var1, CallbackInfo var2) {
      pq var3 = (pq)var1;
      if (p1.h((PlayerModel)this, this.r, var3.larp$headless(), var3.larp$korblox())) {
         this.L = true;
      }
   }
}

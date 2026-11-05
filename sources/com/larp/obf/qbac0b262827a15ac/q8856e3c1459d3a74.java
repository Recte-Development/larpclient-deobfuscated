package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.kv;
import com.larpclient.pq;
import com.larpclient.q;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerModel.class)
public abstract class q8856e3c1459d3a74 implements pq {
   private static final String q = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V";
   @Unique
   private boolean G;
   @Unique
   private boolean c;

   @Override
   public void larp$markSkin() {
      this.G = true;
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("HEAD"))
   private void p(AvatarRenderState var1, CallbackInfo var2) {
      if (this.c) {
         ((PlayerModel)this).head.visible = true;
         this.c = false;
      }
   }

   @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)V", at = @At("TAIL"))
   private void G(AvatarRenderState var1, CallbackInfo var2) {
      q var3 = (q)var1;
      if (kv.Y((PlayerModel)this, this.G, var3.larp$headless(), var3.larp$korblox())) {
         this.c = true;
      }
   }
}

package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._q;
import com.larpclient.a1;
import com.larpclient.d4;
import com.larpclient.pu;
import com.larpclient.qr;
import com.larpclient.vl;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class q0c5291a3c9683003 {
   @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At("TAIL"))
   private void h(Context var1, boolean var2, CallbackInfo var3) {
      AvatarRenderer var4 = (AvatarRenderer)this;
      ((vl)var4.getModel()).larp$markSkin();
      ((q661967254b2f9c3e)this).larp$addLayer(new pu(var4));
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void o(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         d4.e(var1, var2);
      } catch (RuntimeException var6) {
         d4.y(var2);
         _q.g("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class q129c0cecb46017db implements qr {
      @Unique
      private int K;
      @Unique
      private a1 d;

      @Override
      public int larp$headless() {
         return this.K;
      }

      @Override
      public a1 larp$korblox() {
         return this.d;
      }

      @Override
      public void larp$roblox(int var1, a1 var2) {
         this.K = var1;
         this.d = var2;
      }
   }
}

package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.eu;
import com.larpclient.fd;
import com.larpclient.hb;
import com.larpclient.p1;
import com.larpclient.pq;
import com.larpclient.r4;
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
public abstract class q60638c82d2ffbf3e {
   @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At("TAIL"))
   private void O(Context var1, boolean var2, CallbackInfo var3) {
      AvatarRenderer var4 = (AvatarRenderer)this;
      ((hb)var4.getModel()).larp$markSkin();
      ((q98ce83f18e93a418)this).larp$addLayer(new eu(var4));
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void T(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         p1.m(var1, var2);
      } catch (RuntimeException var6) {
         p1.w(var2);
         r4.T("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class qb8af52cbd8876703 implements pq {
      @Unique
      private int p;
      @Unique
      private fd Z;

      @Override
      public int larp$headless() {
         return this.p;
      }

      @Override
      public fd larp$korblox() {
         return this.Z;
      }

      @Override
      public void larp$roblox(int var1, fd var2) {
         this.p = var1;
         this.Z = var2;
      }
   }
}

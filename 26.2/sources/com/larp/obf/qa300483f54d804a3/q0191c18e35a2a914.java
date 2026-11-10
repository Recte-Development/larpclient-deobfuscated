package com.larp.obf.qa300483f54d804a3;

import com.larpclient.gi;
import com.larpclient.h;
import com.larpclient.hl;
import com.larpclient.l8;
import com.larpclient.p7;
import com.larpclient.ty;
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
public abstract class q0191c18e35a2a914 {
   @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At("TAIL"))
   private void I(Context var1, boolean var2, CallbackInfo var3) {
      AvatarRenderer var4 = (AvatarRenderer)this;
      ((hl)var4.getModel()).larp$markSkin();
      ((q5bad0598f25603b6)this).larp$addLayer(new ty(var4));
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void v(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         gi.r(var1, var2);
      } catch (RuntimeException var6) {
         gi.y(var2);
         h.w("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class qce0eba2040686cbe implements p7 {
      @Unique
      private int b;
      @Unique
      private l8 u;

      @Override
      public int larp$headless() {
         return this.b;
      }

      @Override
      public l8 larp$korblox() {
         return this.u;
      }

      @Override
      public void larp$roblox(int var1, l8 var2) {
         this.b = var1;
         this.u = var2;
      }
   }
}

package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.kv;
import com.larpclient.ms;
import com.larpclient.mt;
import com.larpclient.pq;
import com.larpclient.q;
import com.larpclient.u3;
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
public abstract class qdc3e82e7c42dd62a {
   @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Z)V", at = @At("TAIL"))
   private void c(Context var1, boolean var2, CallbackInfo var3) {
      AvatarRenderer var4 = (AvatarRenderer)this;
      ((pq)var4.getModel()).larp$markSkin();
      ((q6bb62fbb48a04dd8)this).larp$addLayer(new ms(var4));
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void V(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         kv.P(var1, var2);
      } catch (RuntimeException var6) {
         kv.k(var2);
         mt.B("Roblox", "Headless/Korblox extraction failed; native head and legs kept", var6);
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class q8dc3c1380286094b implements q {
      @Unique
      private int J;
      @Unique
      private u3 v;

      @Override
      public int larp$headless() {
         return this.J;
      }

      @Override
      public u3 larp$korblox() {
         return this.v;
      }

      @Override
      public void larp$roblox(int var1, u3 var2) {
         this.J = var1;
         this.v = var2;
      }
   }
}

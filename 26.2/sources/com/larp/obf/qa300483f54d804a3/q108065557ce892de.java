package com.larp.obf.qa300483f54d804a3;

import com.larpclient.cx;
import com.larpclient.gn;
import com.larpclient.h;
import com.larpclient.kp;
import com.larpclient.module.EmotesModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class q108065557ce892de {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void y(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         EmotesModule.u(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((gn)var2).larp$setEmoteWeight(0.0F);
         h.w("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(
      method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
      at = @At("RETURN")
   )
   private void T(AvatarRenderState var1, PoseStack var2, float var3, float var4, CallbackInfo var5) {
      gn var6 = (gn)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         kp.o(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class q7173e4e5a86d5598 implements gn {
      @Unique
      private final float[] d = new float[cx.B];
      @Unique
      private float j;
      @Unique
      private float k;
      @Unique
      private String Q;

      @Override
      public float[] larp$emoteSample() {
         return this.d;
      }

      @Override
      public float larp$emoteWeight() {
         return this.j;
      }

      @Override
      public void larp$setEmoteWeight(float var1) {
         this.j = var1;
      }

      @Override
      public String larp$emoteProp() {
         return this.Q;
      }

      @Override
      public float larp$emoteCycle() {
         return this.k;
      }

      @Override
      public void larp$setEmoteProp(String var1, float var2) {
         this.Q = var1;
         this.k = var2;
      }
   }
}

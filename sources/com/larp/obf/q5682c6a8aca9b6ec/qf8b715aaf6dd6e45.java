package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.jc;
import com.larpclient.r4;
import com.larpclient.y1;
import com.larpclient.yo;
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
public abstract class qf8b715aaf6dd6e45 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void L(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         EmotesModule.w(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((yo)var2).larp$setEmoteWeight(0.0F);
         r4.T("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(
      method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
      at = @At("RETURN")
   )
   private void O(AvatarRenderState var1, PoseStack var2, float var3, float var4, CallbackInfo var5) {
      yo var6 = (yo)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         y1.A(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class q5abb60f3ec446683 implements yo {
      @Unique
      private final float[] u = new float[jc.t];
      @Unique
      private float i;
      @Unique
      private float t;
      @Unique
      private String N;

      @Override
      public float[] larp$emoteSample() {
         return this.u;
      }

      @Override
      public float larp$emoteWeight() {
         return this.i;
      }

      @Override
      public void larp$setEmoteWeight(float var1) {
         this.i = var1;
      }

      @Override
      public String larp$emoteProp() {
         return this.N;
      }

      @Override
      public float larp$emoteCycle() {
         return this.t;
      }

      @Override
      public void larp$setEmoteProp(String var1, float var2) {
         this.N = var1;
         this.t = var2;
      }
   }
}

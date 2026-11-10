package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._q;
import com.larpclient._x;
import com.larpclient.bo;
import com.larpclient.f0;
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
public abstract class qaef0630073b0e0da {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void w(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         EmotesModule.R(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((f0)var2).larp$setEmoteWeight(0.0F);
         _q.g("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(
      method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
      at = @At("RETURN")
   )
   private void Y(AvatarRenderState var1, PoseStack var2, float var3, float var4, CallbackInfo var5) {
      f0 var6 = (f0)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         _x.I(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class q91b1b4fc62dcd63b implements f0 {
      @Unique
      private final float[] Z = new float[bo.s];
      @Unique
      private float k;
      @Unique
      private float L;
      @Unique
      private String g;

      @Override
      public float[] larp$emoteSample() {
         return this.Z;
      }

      @Override
      public float larp$emoteWeight() {
         return this.k;
      }

      @Override
      public void larp$setEmoteWeight(float var1) {
         this.k = var1;
      }

      @Override
      public String larp$emoteProp() {
         return this.g;
      }

      @Override
      public float larp$emoteCycle() {
         return this.L;
      }

      @Override
      public void larp$setEmoteProp(String var1, float var2) {
         this.g = var1;
         this.L = var2;
      }
   }
}

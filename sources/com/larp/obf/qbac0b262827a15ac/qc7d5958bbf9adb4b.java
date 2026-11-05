package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.cu;
import com.larpclient.mt;
import com.larpclient.o6;
import com.larpclient.uy;
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
public abstract class qc7d5958bbf9adb4b {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
      at = @At("TAIL")
   )
   private void d(Avatar var1, AvatarRenderState var2, float var3, CallbackInfo var4) {
      try {
         EmotesModule.z(var1, var2, var3);
      } catch (RuntimeException var6) {
         ((cu)var2).larp$setEmoteWeight(0.0F);
         mt.B("Emotes", "Emote sampling failed; native pose kept", var6);
      }
   }

   @Inject(
      method = "setupRotations(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;FF)V",
      at = @At("RETURN")
   )
   private void I(AvatarRenderState var1, PoseStack var2, float var3, float var4, CallbackInfo var5) {
      cu var6 = (cu)var1;
      if (var6.larp$emoteWeight() > 0.0F) {
         o6.M(var2, var6.larp$emoteSample(), var6.larp$emoteWeight());
      }
   }

   @Mixin(AvatarRenderState.class)
   public abstract static class qdc1bcf2c121d2d91 implements cu {
      @Unique
      private final float[] f = new float[uy.Y];
      @Unique
      private float h;
      @Unique
      private float e;
      @Unique
      private String T;

      @Override
      public float[] larp$emoteSample() {
         return this.f;
      }

      @Override
      public float larp$emoteWeight() {
         return this.h;
      }

      @Override
      public void larp$setEmoteWeight(float var1) {
         this.h = var1;
      }

      @Override
      public String larp$emoteProp() {
         return this.T;
      }

      @Override
      public float larp$emoteCycle() {
         return this.e;
      }

      @Override
      public void larp$setEmoteProp(String var1, float var2) {
         this.T = var1;
         this.e = var2;
      }
   }
}

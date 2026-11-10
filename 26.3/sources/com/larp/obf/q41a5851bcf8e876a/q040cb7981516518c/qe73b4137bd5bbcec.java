package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.larpclient.ij;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public abstract class qe73b4137bd5bbcec {
   @ModifyExpressionValue(
      method = "submitHandsWithItems",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;xRot:F"),
      require = 1,
      allow = 1
   )
   private float V(float var1) {
      float var2 = ij.M();
      return Float.isNaN(var2) ? var1 : var2;
   }
}

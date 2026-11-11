package com.larpclient.mixin;

import com.larpclient.module.render.CrystalChamsModule;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalModel.class)
public class EndCrystalModelMixin {
   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;")
   )
   private float a(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.getInstance();
      return var1 * (var2 == null ? 1.0F : var2.getSpinScale());
   }

   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;")
   )
   private float Z(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.getInstance();
      return var1 * (var2 == null ? 1.0F : var2.getSpinScale());
   }
}

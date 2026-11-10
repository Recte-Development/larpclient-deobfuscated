package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q4ad210cb713c6dfc;

import com.larpclient.CrystalChamsModule;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalModel.class)
public class q40f5d6337abfaaf5 {
   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;")
   )
   private float M(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.Y();
      return var1 * (var2 == null ? 1.0F : var2.w());
   }

   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;")
   )
   private float I(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.Y();
      return var1 * (var2 == null ? 1.0F : var2.w());
   }
}

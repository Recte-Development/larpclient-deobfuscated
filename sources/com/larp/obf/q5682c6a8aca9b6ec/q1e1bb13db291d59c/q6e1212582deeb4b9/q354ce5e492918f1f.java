package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9;

import com.larpclient.CrystalChamsModule;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalModel.class)
public class q354ce5e492918f1f {
   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;")
   )
   private float L(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.l();
      return var1 * (var2 == null ? 1.0F : var2.H());
   }

   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;")
   )
   private float z(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.l();
      return var1 * (var2 == null ? 1.0F : var2.H());
   }
}

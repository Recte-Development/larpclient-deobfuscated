package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q3f4e251a0dcf56a9;

import com.larpclient.CrystalChamsModule;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalModel.class)
public class qe3a28d94228343f6 {
   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;")
   )
   private float a(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.W();
      return var1 * (var2 == null ? 1.0F : var2.u());
   }

   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;")
   )
   private float Z(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.W();
      return var1 * (var2 == null ? 1.0F : var2.u());
   }
}

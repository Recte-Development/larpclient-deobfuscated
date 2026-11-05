package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q60d951b25740ad52;

import com.larpclient.CrystalChamsModule;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EndCrystalModel.class)
public class qd2783e8e79cc4ad6 {
   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;")
   )
   private float g(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.O();
      return var1 * (var2 == null ? 1.0F : var2.B());
   }

   @ModifyArg(
      method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;")
   )
   private float C(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.O();
      return var1 * (var2 == null ? 1.0F : var2.B());
   }
}

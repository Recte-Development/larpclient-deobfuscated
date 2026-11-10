package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689;

import com.larpclient.CrystalChamsModule;
import net.minecraft.class_9946;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_9946.class)
public class qbcefe41b42f9d5ba {
   @ModifyArg(
      method = "method_62083(Lnet/minecraft/class_10014;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_7833;rotationDegrees(F)Lorg/joml/Quaternionf;")
   )
   private float C(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.X();
      return var1 * (var2 == null ? 1.0F : var2.i());
   }

   @ModifyArg(
      method = "method_62083(Lnet/minecraft/class_10014;)V",
      at = @At(value = "INVOKE", target = "Lorg/joml/Quaternionf;rotateY(F)Lorg/joml/Quaternionf;")
   )
   private float m(float var1) {
      CrystalChamsModule var2 = CrystalChamsModule.X();
      return var1 * (var2 == null ? 1.0F : var2.i());
   }
}

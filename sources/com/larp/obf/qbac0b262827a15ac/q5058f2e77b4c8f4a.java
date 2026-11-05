package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.StretchedResModule;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public class q5058f2e77b4c8f4a {
   @ModifyArg(
      method = "setupPerspective",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Projection;setupPerspective(FFFFF)V"),
      index = 3
   )
   private float Q(float var1) {
      return var1 / StretchedResModule.F();
   }

   @ModifyArg(
      method = "createProjectionMatrixForCulling",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFFZ)Lorg/joml/Matrix4f;"),
      index = 1
   )
   private float f(float var1) {
      return var1 / StretchedResModule.F();
   }
}

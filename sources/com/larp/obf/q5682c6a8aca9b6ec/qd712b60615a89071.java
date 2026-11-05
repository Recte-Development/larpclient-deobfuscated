package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.StretchedResModule;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public class qd712b60615a89071 {
   @ModifyArg(
      method = "setupPerspective",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Projection;setupPerspective(FFFFF)V"),
      index = 3
   )
   private float s(float var1) {
      return var1 / StretchedResModule.n();
   }

   @ModifyArg(
      method = "createProjectionMatrixForCulling",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFFZ)Lorg/joml/Matrix4f;"),
      index = 1
   )
   private float j(float var1) {
      return var1 / StretchedResModule.n();
   }
}

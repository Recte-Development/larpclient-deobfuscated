package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.StretchedResModule;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public class q71410f557d0c692b {
   @ModifyArg(
      method = "setupPerspective",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Projection;setupPerspective(FFFFF)V"),
      index = 3
   )
   private float z(float var1) {
      return var1 / StretchedResModule.T();
   }

   @ModifyArg(
      method = "createProjectionMatrixForCulling",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFFZ)Lorg/joml/Matrix4f;"),
      index = 1
   )
   private float W(float var1) {
      return var1 / StretchedResModule.T();
   }
}

package com.larpclient.mixin;

import com.larpclient.module.render.StretchedResModule;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public class CameraMixin_2 {
   @ModifyArg(
      method = "setupPerspective",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/Projection;setupPerspective(FFFFF)V"),
      index = 3
   )
   private float t(float var1) {
      return var1 / StretchedResModule.getStretch();
   }

   @ModifyArg(
      method = "createProjectionMatrixForCulling",
      at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFFZ)Lorg/joml/Matrix4f;"),
      index = 1
   )
   private float X(float var1) {
      return var1 / StretchedResModule.getStretch();
   }
}

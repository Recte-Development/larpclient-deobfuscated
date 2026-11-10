package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.StretchedResModule;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_757.class)
public class q97e312d5e296a211 {
   @ModifyArg(method = "method_22973", at = @At(value = "INVOKE", target = "Lorg/joml/Matrix4f;perspective(FFFF)Lorg/joml/Matrix4f;"), index = 1)
   private float S(float var1) {
      return var1 / StretchedResModule.u();
   }
}

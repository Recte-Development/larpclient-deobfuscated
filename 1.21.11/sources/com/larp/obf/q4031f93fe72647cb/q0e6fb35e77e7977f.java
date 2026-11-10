package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_315;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(class_315.class)
public abstract class q0e6fb35e77e7977f {
   @ModifyArg(
      method = "<init>",
      slice = @Slice(
         from = @At(value = "CONSTANT", args = "stringValue=options.guiScale"),
         to = @At(value = "FIELD", target = "Lnet/minecraft/class_315;field_1868:Lnet/minecraft/class_7172;")
      ),
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_7172;<init>(Ljava/lang/String;Lnet/minecraft/class_7172$class_7277;Lnet/minecraft/class_7172$class_7303;Lnet/minecraft/class_7172$class_7178;Ljava/lang/Object;Ljava/util/function/Consumer;)V"
      ),
      index = 4,
      require = 1,
      allow = 1
   )
   private Object K(Object var1) {
      return 2;
   }
}

package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.t0;
import net.minecraft.class_10442;
import net.minecraft.class_1799;
import net.minecraft.class_9331;
import net.minecraft.class_9334;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_10442.class)
public abstract class qe0ad5ebcf176db70 {
   @Redirect(
      method = "method_65596",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1799;method_58694(Lnet/minecraft/class_9331;)Ljava/lang/Object;")
   )
   private Object r(class_1799 var1, class_9331<?> var2) {
      return var2 != class_9334.field_54199 ? var1.method_58694(var2) : t0.Y(var1).method_58694(class_9334.field_54199);
   }

   @ModifyVariable(method = "method_65598", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private class_1799 U(class_1799 var1) {
      return t0.Y(var1);
   }
}

package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_2818.class)
public abstract class q358b9509cf72f095 {
   @Shadow
   @Final
   private class_1937 field_12858;

   @Redirect(
      method = {"method_12208", "method_12204", "method_12007"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2818;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;")
   )
   private class_2680 t(class_2818 var1, class_2338 var2) {
      if (this.field_12858 instanceof class_638 && cz.T().z(var2)) {
         int var3 = var1.method_31602(var2.method_10264());
         return var3 >= 0 && var3 < var1.method_12006().length
            ? var1.method_38259(var3).method_12254(var2.method_10263() & 15, var2.method_10264() & 15, var2.method_10260() & 15)
            : class_2246.field_10124.method_9564();
      } else {
         return var1.method_8320(var2);
      }
   }
}

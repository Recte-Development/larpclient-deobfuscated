package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_2394;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_1309.class)
public class q8623cd6c9eb99922 {
   @Redirect(
      method = "method_6050",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8406(Lnet/minecraft/class_2394;DDDDDD)V"),
      require = 1
   )
   private void M(class_1937 var1, class_2394 var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (this != class_310.method_1551().field_1724) {
         var1.method_8406(var2, var3, var5, var7, var9, var11, var13);
      }
   }
}

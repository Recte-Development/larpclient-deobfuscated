package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.ux;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_332.class)
public abstract class q641c092c6aec953f {
   @Shadow
   @Final
   private class_310 field_44656;

   @ModifyVariable(method = "method_51432(Lnet/minecraft/class_327;Lnet/minecraft/class_1799;IILjava/lang/String;)V", at = @At("HEAD"), argsOnly = true)
   private String A(String var1, class_327 var2, class_1799 var3, int var4, int var5, String var6) {
      var1 = ux.o().M(var3, var1);
      int var7 = var3.method_7947();
      if (var7 > 1) {
         var1 = String.valueOf(var7);
      }

      return var1;
   }
}

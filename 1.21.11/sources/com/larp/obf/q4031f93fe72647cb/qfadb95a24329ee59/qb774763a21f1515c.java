package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.q;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_332.class)
public class qb774763a21f1515c {
   @ModifyVariable(
      method = "method_51425(Lnet/minecraft/class_1309;Lnet/minecraft/class_1937;Lnet/minecraft/class_1799;III)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private class_1799 N(class_1799 var1) {
      return q.U().g(var1);
   }
}

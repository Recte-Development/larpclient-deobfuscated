package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.f4;
import com.larpclient.il;
import com.larpclient.q7;
import com.larpclient.t3;
import com.larpclient.ux;
import com.larpclient.yb;
import net.minecraft.class_10442;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_916;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_916.class)
public class q7d34209f1f454971 {
   @ModifyArg(
      method = "method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_10039;method_65581(Lnet/minecraft/class_1297;Lnet/minecraft/class_1799;Lnet/minecraft/class_10442;)V"
      ),
      index = 1
   )
   private class_1799 r(class_1297 var1, class_1799 var2, class_10442 var3) {
      class_1542 var4 = (class_1542)var1;
      var2 = il.o(var4, var2);
      var2 = f4.L().h(var4, var2);
      var2 = t3.c().C(var4, var2);
      class_1799 var5 = q7.L().getVisualOverride(var2);
      if (var5 != null) {
         var2 = var5;
      }

      var2 = ux.o().V(var4, var2);
      var2 = yb.b().E(var4, var2);
      return bk.W().f(var4.method_5628(), var2);
   }
}

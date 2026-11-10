package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.f4;
import com.larpclient.ju;
import com.larpclient.q7;
import com.larpclient.t3;
import com.larpclient.tw;
import com.larpclient.ux;
import com.larpclient.yb;
import net.minecraft.class_10442;
import net.minecraft.class_10444;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_811;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_10442.class)
public abstract class q683445609ac21fa5 {
   @ModifyVariable(
      method = "method_65597(Lnet/minecraft/class_10444;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_1309;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private class_1799 p(class_1799 var1, class_10444 var2, class_1799 var3, class_811 var4, class_1309 var5) {
      if (var5 == class_310.method_1551().field_1724 && !var1.method_7960() && ju.b().q(var1).method_7960()) {
         return class_1799.field_8037;
      }

      if (var5 == class_310.method_1551().field_1724 && !var1.method_7960() && tw.s().k(var1).method_7960()) {
         return class_1799.field_8037;
      }

      class_1799 var6 = t3.c().p(var5, var1);
      if (var5 == class_310.method_1551().field_1724) {
         var6 = f4.L().U(var6);
      }

      var6 = ux.o().z(var5, var6);
      var6 = yb.b().C(var5, var6);
      class_1799 var7 = q7.L().getVisualOverride(var6);
      if (var7 != null) {
         var6 = var7;
      }

      return var6;
   }

   @ModifyVariable(
      method = "method_65595(Lnet/minecraft/class_10444;Lnet/minecraft/class_1799;Lnet/minecraft/class_811;Lnet/minecraft/class_1297;)V",
      at = @At("HEAD"),
      argsOnly = true
   )
   private class_1799 Q(class_1799 var1, class_10444 var2, class_1799 var3, class_811 var4, class_1297 var5) {
      if (var5 instanceof class_1542 var6) {
         var1 = f4.L().h(var6, var1);
         var1 = t3.c().C(var6, var1);
         var1 = ux.o().V(var6, var1);
         var1 = yb.b().E(var6, var1);
      }

      class_1799 var10 = q7.L().getVisualOverride(var1);
      if (var10 != null) {
         var1 = var10;
      }

      return var1;
   }
}

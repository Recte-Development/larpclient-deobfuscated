package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import com.larpclient.iy;
import com.larpclient.wu;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1661;
import net.minecraft.class_304;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_310.class)
public abstract class q94116ec4d30eb9a0 {
   @WrapOperation(method = "method_1508", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_304;method_1436()Z"))
   private boolean U(class_304 var1, Operation<Boolean> var2) {
      boolean var3 = (Boolean)var2.call(new Object[]{var1});
      class_310 var4 = class_310.method_1551();
      if (var3 && iy.n()) {
         cz var5 = cz.T();
         boolean var6 = var5.W(var4.field_1724.method_31548().method_67532());
         if (var1 == var4.field_1690.field_1869 && var6) {
            return false;
         } else {
            return var1 != var4.field_1690.field_1831 || !var6 && !var5.W(40) && !wu.e() ? var3 : false;
         }
      } else {
         return var3;
      }
   }

   @WrapOperation(method = "method_1508", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_61496(I)V"))
   private void C(class_1661 var1, int var2, Operation<Void> var3) {
      if (iy.n()) {
         iy.S(var2);
      } else {
         var3.call(new Object[]{var1, var2});
      }
   }
}

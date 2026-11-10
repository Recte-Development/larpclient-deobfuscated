package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1297.class)
public class qbadd2a05e36ac0a0 {
   @Inject(method = "method_5872", at = @At("HEAD"), cancellable = true)
   private void s(double var1, double var3, CallbackInfo var5) {
      class_1297 var6 = (class_1297)this;
      class_310 var7 = class_310.method_1551();
      if (wp.o() != wp.z.D && var6 == var7.field_1724) {
         wp.E(var1, var3);
         var5.cancel();
      }
   }
}

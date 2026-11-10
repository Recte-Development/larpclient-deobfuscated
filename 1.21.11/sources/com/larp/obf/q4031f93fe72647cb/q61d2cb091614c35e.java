package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bn;
import com.larpclient.module.MaceModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_636.class)
public abstract class q61d2cb091614c35e {
   @Inject(method = "method_2918", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_636;method_2911()V"), require = 1, allow = 1)
   private void y(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.c();
      if (var1 == class_310.method_1551().field_1724 && var1 instanceof class_746 var5 && var4 != null && var4.isRuntimeActive() && !bn.w(var5)) {
         var4.w(var5, var2);
      }
   }

   @Inject(method = "method_2918", at = @At("TAIL"), require = 1, allow = 1)
   private void L(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      MaceModule var4 = MaceModule.c();
      if (var1 == class_310.method_1551().field_1724 && var1 instanceof class_746 var5 && var4 != null && var4.isRuntimeActive()) {
         var4.D(var5, var2);
      }
   }
}

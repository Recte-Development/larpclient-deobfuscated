package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_11400;
import net.minecraft.class_1297;
import net.minecraft.class_4184;
import net.minecraft.class_5636;
import net.minecraft.class_7286;
import net.minecraft.class_758;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_758.class)
public class qeaf64832fde64af0 {
   @Inject(method = "method_71652", at = @At("RETURN"), cancellable = true)
   private void w(class_4184 var1, CallbackInfoReturnable<class_5636> var2) {
      if (var2.getReturnValue() == class_5636.field_27886 && RemovalsModule.o("water")
         || var2.getReturnValue() == class_5636.field_27885 && RemovalsModule.o("lava")) {
         var2.setReturnValue(class_5636.field_60563);
      }
   }

   @Redirect(
      method = {"method_62185", "method_3211"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11400;method_42593(Lnet/minecraft/class_5636;Lnet/minecraft/class_1297;)Z"),
      require = 2,
      allow = 2
   )
   private boolean T(class_11400 var1, class_5636 var2, class_1297 var3) {
      return (!(var1 instanceof class_7286) || !RemovalsModule.o()) && var1.method_42593(var2, var3);
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FreecamModule;
import com.larpclient.module.WorldMenuModule;
import com.larpclient.module.ZoomModule;
import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(class_312.class)
public abstract class qc0b15f4f82a0a5b5 {
   @ModifyArgs(method = "method_1606(D)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_5872(DD)V"))
   private void R(Args var1) {
      double var2 = ZoomModule.n();
      var1.set(0, (Double)var1.get(0) * var2);
      var1.set(1, (Double)var1.get(1) * var2);
   }

   @Inject(method = "method_1598(JDD)V", at = @At("HEAD"), cancellable = true)
   private void y(long var1, double var3, double var5, CallbackInfo var7) {
      class_310 var8 = class_310.method_1551();
      if (var1 == var8.method_22683().method_4490() && var5 != 0.0) {
         double var9 = (var8.field_1690.method_42439().method_41753() ? Math.signum(var5) : var5) * (Double)var8.field_1690.method_41806().method_41753();
         if (var8.field_1755 == null && var8.method_18506() == null && (WorldMenuModule.U(var9) || ZoomModule.D(var9) || FreecamModule.f(var9))) {
            var7.cancel();
         }
      }
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.ez;
import com.larpclient.gu;
import com.larpclient.hh;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_350;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_350.class)
public abstract class qa24434185cb935c3 {
   @Inject(method = "method_57715", at = @At("HEAD"), cancellable = true)
   private void j(class_332 var1, CallbackInfo var2) {
      if (class_310.method_1551().field_1687 == null && hh.D()) {
         class_350 var3 = (class_350)this;
         gu.Z(var1, var3.method_46426(), var3.method_46427(), var3.method_25368(), var3.method_25364(), ez.C);
         var2.cancel();
      }
   }

   @Inject(method = "method_57713", at = @At("HEAD"), cancellable = true)
   private void e(class_332 var1, CallbackInfo var2) {
      if (class_310.method_1551().field_1687 == null && hh.D()) {
         class_350 var3 = (class_350)this;
         int var4 = ez.D;
         gu.Z(var1, var3.method_46426(), var3.method_46427() - 1, var3.method_25368(), 1, var4);
         gu.Z(var1, var3.method_46426(), var3.method_55443(), var3.method_25368(), 1, var4);
         var2.cancel();
      }
   }
}

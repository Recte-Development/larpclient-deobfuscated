package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.IgnoreEntityModule;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_636.class)
public class q87bc12c36458d22d {
   @Inject(method = "method_2896", at = @At("HEAD"), cancellable = true)
   private void I(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      class_638 var5 = class_310.method_1551().field_1687;
      if (var5 != null && AntiTrapModule.J(var5.method_8320(var3.method_17777())) && !var1.method_5715()) {
         var4.setReturnValue(class_1269.field_5814);
      }
   }

   @Inject(method = "method_2905", at = @At("HEAD"), cancellable = true)
   private void J(class_1657 var1, class_1297 var2, class_1268 var3, CallbackInfoReturnable<class_1269> var4) {
      if (AntiTrapModule.C(var2) || IgnoreEntityModule.U(var2)) {
         var4.setReturnValue(class_1269.field_5814);
      }
   }

   @Inject(method = "method_2917", at = @At("HEAD"), cancellable = true)
   private void t(class_1657 var1, class_1297 var2, class_3966 var3, class_1268 var4, CallbackInfoReturnable<class_1269> var5) {
      if (AntiTrapModule.C(var2) || IgnoreEntityModule.U(var2)) {
         var5.setReturnValue(class_1269.field_5814);
      }
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FakePayModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_266;
import net.minecraft.class_268;
import net.minecraft.class_269;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_5250;
import net.minecraft.class_9011;
import net.minecraft.class_9022;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_329.class)
public class qae8750668847826c {
   @ModifyExpressionValue(
      method = "method_55439",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_268;method_1142(Lnet/minecraft/class_270;Lnet/minecraft/class_2561;)Lnet/minecraft/class_5250;"
      )
   )
   private class_5250 g(class_5250 var1, class_269 var2, class_9022 var3, class_9011 var4) {
      class_268 var5 = var2.method_1164(var4.comp_2127());
      return FakePayModule.a(var1, null, var4.comp_2127(), var5 == null ? "" : var5.method_1197());
   }

   @ModifyExpressionValue(
      method = "method_55439",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_9011;method_55386(Lnet/minecraft/class_9022;)Lnet/minecraft/class_5250;")
   )
   private class_5250 H(class_5250 var1, class_269 var2, class_9022 var3, class_9011 var4) {
      class_268 var5 = var2.method_1164(var4.comp_2127());
      return FakePayModule.a(var1, class_268.method_1142(var5, var4.method_55387()), var4.comp_2127(), var5 == null ? "" : var5.method_1197());
   }

   @Inject(method = "method_1757", at = @At("RETURN"))
   private void h(class_332 var1, class_266 var2, CallbackInfo var3) {
      FakePayModule.k(var2);
   }
}

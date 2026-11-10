package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.PlayerModelModule;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11890;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_9334;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1007.class)
public class q0d8bae504e2066d1 {
   @Inject(method = "method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V", at = @At("TAIL"))
   private void O(class_11890 var1, class_10055 var2, float var3, CallbackInfo var4) {
      PlayerModelModule var5 = PlayerModelModule.h();
      if (var5 != null && var1 == class_310.method_1551().field_1724) {
         var5.S(null);
         if (var5.X()) {
            var2.field_53333 = true;
            var2.field_53461 = true;
            var2.field_55309 = var2.field_53419 = var2.field_53420 = class_1799.field_8037;
            if (!var2.field_53418.method_57826(class_9334.field_54197)) {
               var2.field_53418 = class_1799.field_8037;
            }

            var2.field_53526 = var2.field_53527 = null;
            var5.S(var2);
         }
      }
   }
}

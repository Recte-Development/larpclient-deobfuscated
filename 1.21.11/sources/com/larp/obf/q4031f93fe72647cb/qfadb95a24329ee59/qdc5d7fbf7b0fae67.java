package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.f4;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_11890;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1007.class)
public class qdc5d7fbf7b0fae67 {
   @Inject(method = "method_62604(Lnet/minecraft/class_11890;Lnet/minecraft/class_10055;F)V", at = @At("TAIL"))
   private void z(class_11890 var1, class_10055 var2, float var3, CallbackInfo var4) {
      class_310 var5 = class_310.method_1551();
      if (var1 == var5.field_1724) {
         class_1799[] var6 = f4.Y.p(var5);
         if (var6 != null) {
            var2.field_55309 = var6[1];
            var2.field_53418 = var6[2];
            var2.field_53419 = var6[3];
            var2.field_53420 = var6[4];
         }
      }
   }
}

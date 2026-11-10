package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.AirStuckModule;
import com.larpclient.module.ElytraMotionModule;
import net.minecraft.class_1297;
import net.minecraft.class_1313;
import net.minecraft.class_243;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1297.class)
public class q470079dd10657b13 {
   @Inject(method = "method_5784", at = @At("HEAD"), cancellable = true)
   private void d(class_1313 var1, class_243 var2, CallbackInfo var3) {
      if (this == class_310.method_1551().field_1724) {
         AirStuckModule var4 = AirStuckModule.U();
         ElytraMotionModule var5 = ElytraMotionModule.N();
         if (var4 != null && var4.E() || var5 != null && var5.k()) {
            ((class_1297)this).method_18799(class_243.field_1353);
            var3.cancel();
         }
      }
   }
}

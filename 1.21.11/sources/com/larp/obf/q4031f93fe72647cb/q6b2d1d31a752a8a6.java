package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.HitFXModule;
import net.minecraft.class_2394;
import net.minecraft.class_2396;
import net.minecraft.class_2398;
import net.minecraft.class_702;
import net.minecraft.class_703;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_702.class)
public class q6b2d1d31a752a8a6 {
   @Inject(method = "method_3056", at = @At("HEAD"), cancellable = true)
   private void N(class_2394 var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<class_703> var14) {
      class_2396 var15 = var1.method_10295();
      HitFXModule var16 = HitFXModule.H();
      if (var16 != null) {
         if ((
               var15 == class_2398.field_11251
                  || var15 == class_2398.field_11237
                  || var15 == class_2398.field_46911
                  || var15 == class_2398.field_17430
                  || var15 == class_2398.field_17431
                  || var15 == class_2398.field_11205
                  || var15 == class_2398.field_11208
                  || var15 == class_2398.field_11209
                  || var15 == class_2398.field_11227
            )
            && var16.K()) {
            var14.setReturnValue(null);
         }
      }
   }
}

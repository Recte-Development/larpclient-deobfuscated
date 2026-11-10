package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.ju;
import com.larpclient.q7;
import com.larpclient.tw;
import net.minecraft.class_1703;
import net.minecraft.class_310;
import net.minecraft.class_3917;
import net.minecraft.class_3944;
import net.minecraft.class_4463;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_634.class)
public abstract class q45979340de48dba3 {
   @Inject(method = "method_17587", at = @At("TAIL"))
   private void b(class_3944 var1, CallbackInfo var2) {
      class_310 var3 = class_310.method_1551();
      if (var3.field_1724 != null) {
         tw.s().c(var3.field_1724.field_7512);
      }
   }

   @Inject(method = "method_21707", at = @At("TAIL"))
   private void Y(class_4463 var1, CallbackInfo var2) {
      q7.B().onBlockInteractionAcknowledged(var1.comp_633());
   }

   @Inject(
      method = "method_17587",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_11980;)V",
         shift = Shift.AFTER
      ),
      cancellable = true
   )
   private void X(class_3944 var1, CallbackInfo var2) {
      if (var1 != null) {
         class_3917 var3 = var1.method_17593();
         if (ju.b().w(var3, var1.method_17592())) {
            var2.cancel();
         } else if (tw.s().c(var3, var1.method_17592())) {
            var2.cancel();
         } else {
            q7 var4 = q7.L();
            if (var4.shouldSuppressOpen(var3)) {
               class_310 var5 = class_310.method_1551();
               if (var5 != null && var5.field_1724 != null) {
                  class_1703 var6 = var3.method_17434(var1.method_17592(), var5.field_1724.method_31548());
                  if (var4.isSwapActive()) {
                     var6.method_34254(var5.field_1724.field_7512.method_34255().method_7972());
                  }

                  var5.field_1724.field_7512 = var6;
                  var4.onSwapContainerOpened(var3, var1.method_17592());
                  var2.cancel();
               }
            }
         }
      }
   }
}

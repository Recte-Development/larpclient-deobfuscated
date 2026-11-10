package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.ej;
import com.larpclient.q1;
import com.larpclient.wm;
import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_572;
import net.minecraft.class_591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_572.class)
public abstract class qc402d46f82bdf47d {
   @Inject(method = "method_17087(Lnet/minecraft/class_10034;)V", at = @At("TAIL"))
   private void f(class_10034 var1, CallbackInfo var2) {
      if (var1 instanceof q1 var3) {
         float var4 = var3.larp$emoteWeight();
         if (!(var4 <= 0.0F)) {
            ej.F((class_572<?>)this, var3.larp$emoteSample(), var4);
            if (this instanceof class_591 && var1 instanceof class_10055 var5) {
               float[] var6 = ej.y(var3.larp$emoteSample(), var4);
               if (var6 != null) {
                  ((wm)this).larpApplyJoints(var6, var5);
               }
            }
         }
      }
   }
}

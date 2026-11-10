package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.hu;
import com.larpclient.v;
import net.minecraft.class_1703;
import net.minecraft.class_1716;
import net.minecraft.class_310;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_465.class)
public abstract class qe7fc61813b426f7e {
   @Shadow
   @Final
   protected class_1703 field_2797;

   @Inject(method = "method_25426", at = @At("TAIL"))
   private void m(CallbackInfo var1) {
      if (this.field_2797 instanceof class_1716 var2) {
         v.D().T();
         hu.f().M(class_310.method_1551(), var2);
      }
   }
}

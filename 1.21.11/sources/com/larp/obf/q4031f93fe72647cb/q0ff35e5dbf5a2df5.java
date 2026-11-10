package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.he;
import com.larpclient.module.CapesModule;
import com.larpclient.module.SkinChangerModule;
import java.util.UUID;
import net.minecraft.class_742;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_742.class)
public class q0ff35e5dbf5a2df5 {
   @Inject(method = "method_52814", at = @At("RETURN"), cancellable = true)
   private void d(CallbackInfoReturnable<class_8685> var1) {
      UUID var2 = ((class_742)this).method_5667();
      class_8685 var3 = he.a(var2, (class_8685)var1.getReturnValue());
      SkinChangerModule var4 = SkinChangerModule.m();
      if (var4 != null) {
         var3 = var4.q(var2, var3);
      }

      CapesModule var5 = CapesModule.i();
      var1.setReturnValue(var5 == null ? var3 : var5.C(var2, var3));
   }
}

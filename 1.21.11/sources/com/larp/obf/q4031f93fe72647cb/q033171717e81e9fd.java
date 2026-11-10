package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.JesusModule;
import net.minecraft.class_1941;
import net.minecraft.class_2338;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3727;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_3727.class)
public class q033171717e81e9fd {
   @Inject(method = "method_62878", at = @At("RETURN"), cancellable = true)
   private void n(class_2680 var1, class_1941 var2, class_2338 var3, CallbackInfoReturnable<class_265> var4) {
      var4.setReturnValue(JesusModule.a(var1, var2, var3, (class_3727)this, (class_265)var4.getReturnValue()));
   }
}

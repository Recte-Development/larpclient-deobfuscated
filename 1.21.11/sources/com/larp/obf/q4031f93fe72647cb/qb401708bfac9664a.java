package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.HolesESPModule;
import com.larpclient.module.PrimeChunkFinderModule;
import com.larpclient.module.zu;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_638.class)
public abstract class qb401708bfac9664a {
   @Inject(method = "method_41928", at = @At("HEAD"))
   private void f(class_2338 var1, class_2680 var2, int var3, CallbackInfo var4) {
      zu.a((class_638)this, var1, var2);
   }

   @Inject(method = "method_41928", at = @At("HEAD"))
   private void X(class_2338 var1, class_2680 var2, int var3, CallbackInfo var4) {
      PrimeChunkFinderModule.G((class_638)this, var1, var2);
   }

   @Inject(method = "method_41928", at = @At("TAIL"))
   private void m(class_2338 var1, class_2680 var2, int var3, CallbackInfo var4) {
      HolesESPModule.e((class_638)this, var1);
   }
}

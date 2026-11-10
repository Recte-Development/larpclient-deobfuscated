package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.ex;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_638.class)
public abstract class q7d6f706ef169690d {
   @Inject(method = "method_8413", at = @At("HEAD"))
   private void f(class_2338 var1, class_2680 var2, class_2680 var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         ex.V(var1, var2, var3);
      }
   }
}

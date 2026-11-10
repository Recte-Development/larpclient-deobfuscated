package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.jf;
import com.larpclient.sg;
import com.larpclient.t_;
import com.larpclient.w1;
import net.minecraft.class_10151;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_10151.class)
public class qae7fa711c9d6bb4a {
   @Inject(method = "method_62945(Lnet/minecraft/class_10151$class_10153;Lnet/minecraft/class_3300;Lnet/minecraft/class_3695;)V", at = @At("TAIL"))
   private void c(CallbackInfo var1) {
      t_.P();
      sg.E();
      jf.o();
      w1.J();
   }
}

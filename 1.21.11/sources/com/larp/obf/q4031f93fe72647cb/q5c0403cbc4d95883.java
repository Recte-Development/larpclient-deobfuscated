package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.AutoReconnectModule;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_437;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_412.class)
public abstract class q5c0403cbc4d95883 {
   @Inject(method = "method_36877", at = @At("HEAD"))
   private static void G(class_437 var0, class_310 var1, class_639 var2, class_642 var3, boolean var4, class_9112 var5, CallbackInfo var6) {
      AutoReconnectModule.V(var2, var3);
   }
}

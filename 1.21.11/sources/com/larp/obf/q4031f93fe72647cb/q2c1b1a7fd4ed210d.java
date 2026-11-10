package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gv;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.class_10055;
import net.minecraft.class_11659;
import net.minecraft.class_4587;
import net.minecraft.class_972;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_972.class)
public class q2c1b1a7fd4ed210d {
   @Inject(method = "method_4177(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10055;FF)V", at = @At("HEAD"), cancellable = true)
   private void q(class_4587 var1, class_11659 var2, int var3, class_10055 var4, float var5, float var6, CallbackInfo var7) {
      if (!gv.a() && SelfVisualsModule.Q(var4) && SelfVisualsModule.o().W()) {
         var7.cancel();
      }
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gv;
import com.larpclient.module.HideArmourModule;
import com.larpclient.module.SelfVisualsModule;
import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_11659;
import net.minecraft.class_4587;
import net.minecraft.class_970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_970.class)
public class qc65b1acc61b1f45c {
   @Inject(method = "method_17157(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_10034;FF)V", at = @At("HEAD"), cancellable = true)
   private void C(class_4587 var1, class_11659 var2, int var3, class_10034 var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof class_10055 var8 && HideArmourModule.z(var8)) {
         var7.cancel();
      } else {
         if (!gv.a() && SelfVisualsModule.Q(var4) && SelfVisualsModule.o().I.getValue()) {
            var7.cancel();
         }
      }
   }
}

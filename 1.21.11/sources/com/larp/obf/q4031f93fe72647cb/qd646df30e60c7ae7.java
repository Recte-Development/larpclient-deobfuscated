package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.CrystalOptimizerModule;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_636.class)
public abstract class qd646df30e60c7ae7 {
   @Inject(method = "method_2918", at = @At("TAIL"))
   private void e(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      CrystalOptimizerModule.K(var1, var2);
   }
}

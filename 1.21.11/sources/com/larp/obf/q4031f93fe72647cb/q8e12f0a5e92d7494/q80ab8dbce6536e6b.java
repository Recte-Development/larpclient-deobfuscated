package com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494;

import com.larpclient.sz;
import net.minecraft.class_5903;
import net.minecraft.class_5904;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_634.class)
public abstract class q80ab8dbce6536e6b {
   @Inject(method = "method_34083", at = @At("HEAD"))
   private void y(class_5904 var1, CallbackInfo var2) {
      if (var1.comp_2281() != null) {
         sz.g(var1.comp_2281().getString());
      }
   }

   @Inject(method = "method_34082", at = @At("HEAD"))
   private void l(class_5903 var1, CallbackInfo var2) {
      if (var1.comp_2280() != null) {
         sz.g(var1.comp_2280().getString());
      }
   }
}

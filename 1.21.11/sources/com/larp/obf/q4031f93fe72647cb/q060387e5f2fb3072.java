package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.KillFXModule;
import net.minecraft.class_10017;
import net.minecraft.class_11659;
import net.minecraft.class_12075;
import net.minecraft.class_1297;
import net.minecraft.class_4587;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_898.class)
public final class q060387e5f2fb3072 {
   @Inject(method = "method_72977", at = @At("RETURN"))
   private void j(class_1297 var1, float var2, CallbackInfoReturnable<class_10017> var3) {
      KillFXModule var4 = KillFXModule.y();
      if (var4 != null && var4.u()) {
         var4.H(var1, (class_10017)var3.getReturnValue());
      }
   }

   @Inject(
      method = "method_72976(Lnet/minecraft/class_10017;Lnet/minecraft/class_12075;DDDLnet/minecraft/class_4587;Lnet/minecraft/class_11659;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void L(class_10017 var1, class_12075 var2, double var3, double var5, double var7, class_4587 var9, class_11659 var10, CallbackInfo var11) {
      KillFXModule var12 = KillFXModule.y();
      if (var12 != null && var12.u() && var12.B(var1)) {
         var11.cancel();
      }
   }
}

package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_1297;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import net.minecraft.class_6880;
import net.minecraft.class_7923;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_638.class)
public abstract class q276f5e65dc7e8eeb {
   @Inject(method = "method_30092", at = @At("RETURN"))
   private void L(class_2338 var1, class_2680 var2, int var3, int var4, CallbackInfoReturnable<Boolean> var5) {
      cz var6 = cz.T();
      if ((Boolean)var5.getReturnValue() && var6.J()) {
         var6.Z(var1, var2);
      }
   }

   @Inject(method = "method_41926", at = @At("HEAD"))
   private void z(class_2338 var1, class_2680 var2, class_243 var3, CallbackInfo var4) {
      cz var5 = cz.T();
      if (var5.J()) {
         var5.v(var1, var2);
      }
   }

   @Redirect(
      method = {"method_30092", "method_41926"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_638;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;")
   )
   private class_2680 P(class_638 var1, class_2338 var2) {
      if (!cz.T().J()) {
         return var1.method_8320(var2);
      }

      if (var1.method_31606(var2)) {
         return class_2246.field_10124.method_9564();
      }

      class_2818 var3 = var1.method_8500(var2);
      return var3.method_38259(var3.method_31602(var2.method_10264()))
         .method_12254(var2.method_10263() & 15, var2.method_10264() & 15, var2.method_10260() & 15);
   }

   @Inject(method = "method_41928", at = @At("HEAD"))
   private void Y(class_2338 var1, class_2680 var2, int var3, CallbackInfo var4) {
      cz var5 = cz.T();
      if (var5.J()) {
         var5.v(var1, var2);
      }
   }

   @ModifyVariable(method = "method_31595", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private class_2680 e(class_2680 var1, class_2338 var2, class_2680 var3) {
      return cz.T().G(var2, var1);
   }

   @Redirect(
      method = "method_74254",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_638;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;")
   )
   private class_2680 R(class_638 var1, class_2338 var2) {
      return cz.T().G(var2, var1.method_8320(var2));
   }

   @Inject(method = "method_8465(Lnet/minecraft/class_1297;DDDLnet/minecraft/class_6880;Lnet/minecraft/class_3419;FFJ)V", at = @At("HEAD"), cancellable = true)
   private void s(
      class_1297 var1,
      double var2,
      double var4,
      double var6,
      class_6880<class_3414> var8,
      class_3419 var9,
      float var10,
      float var11,
      long var12,
      CallbackInfo var14
   ) {
      class_310 var15 = class_310.method_1551();
      if (var15.method_18854() && var9 == class_3419.field_15245) {
         cz var16 = cz.T();
         cz.l var17 = var16.y(class_2338.method_49637(var2, var4, var6), (class_3414)var8.comp_349(), var10, var11);
         if (var17 != null) {
            var14.cancel();
            ((class_638)this)
               .method_8465(var1, var2, var4, var6, class_7923.field_41172.method_47983(var17.event()), var9, var17.volume(), var17.pitch(), var12);
         }
      }
   }
}

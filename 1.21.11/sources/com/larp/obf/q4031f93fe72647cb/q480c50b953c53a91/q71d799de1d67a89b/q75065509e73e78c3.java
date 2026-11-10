package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q71d799de1d67a89b;

import com.larpclient.bv;
import com.larpclient.sq;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_310;
import net.minecraft.class_4071;
import net.minecraft.class_437;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_310.class)
public abstract class q75065509e73e78c3 {
   @WrapOperation(method = "method_1523", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25421()Z"))
   private boolean w(class_437 var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !sq.A();
   }

   @WrapOperation(method = "method_1523", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4071;method_18640()Z"))
   private boolean Z(class_4071 var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !sq.A();
   }

   @WrapOperation(method = "method_1590", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_636;method_2925()V"))
   private void I(class_636 var1, Operation<Void> var2) {
      if (!bv.C()) {
         bv.p();
         var2.call(new Object[]{var1});
      }
   }
}

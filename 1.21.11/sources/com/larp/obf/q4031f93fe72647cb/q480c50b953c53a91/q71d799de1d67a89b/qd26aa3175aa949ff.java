package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q71d799de1d67a89b;

import com.larpclient.sq;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_310;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_757.class)
public abstract class qd26aa3175aa949ff {
   @WrapOperation(method = "method_3192", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_310;method_20539(Z)V"))
   private void F(class_310 var1, boolean var2, Operation<Void> var3) {
      if (!sq.A()) {
         var3.call(new Object[]{var1, var2});
      }
   }
}

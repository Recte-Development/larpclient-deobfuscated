package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.j5;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.class_1920;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_3610;
import net.minecraft.class_4588;
import net.minecraft.class_775;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_775.class)
public abstract class q47dbe9b388aac6a9 {
   @WrapMethod(method = "method_3347")
   private void U(class_1920 var1, class_2338 var2, class_4588 var3, class_2680 var4, class_3610 var5, Operation<Void> var6) {
      if (!j5.w(var1, var2, var5)) {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      } else {
         try {
            var6.call(new Object[]{var1, var2, var3, var4, var5});
         } finally {
            j5.d();
         }
      }
   }

   @ModifyArg(
      method = "method_23072",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4588;method_22915(FFFF)Lnet/minecraft/class_4588;"),
      index = 3
   )
   private float E(float var1) {
      return (j5.G(-16777216) >>> 24) / 255.0F;
   }
}

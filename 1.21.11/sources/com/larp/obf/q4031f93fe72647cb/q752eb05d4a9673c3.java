package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import com.larpclient.module.FreecamModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_4604;
import net.minecraft.class_761;
import net.minecraft.class_898;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_761.class)
public abstract class q752eb05d4a9673c3 {
   @Unique
   private boolean g;

   @WrapOperation(
      method = "method_72917",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_898;method_3950(Lnet/minecraft/class_1297;Lnet/minecraft/class_4604;DDD)Z")
   )
   private boolean j(class_898 var1, class_1297 var2, class_4604 var3, double var4, double var6, double var8, Operation<Boolean> var10) {
      return (!wp.d() || FreecamModule.z() || var2 != class_310.method_1551().field_1724)
         && (Boolean)var10.call(new Object[]{var1, var2, var3, var4, var6, var8});
   }

   @Redirect(method = "method_72917", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_761;method_40050(Lnet/minecraft/class_2338;)Z"))
   private boolean Z(class_761 var1, class_2338 var2) {
      return wp.d() || var1.method_40050(var2);
   }

   @WrapOperation(
      method = "method_22710",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_761;method_74752(Lnet/minecraft/class_4184;Lnet/minecraft/class_4604;Z)V")
   )
   private void E(class_761 var1, class_4184 var2, class_4604 var3, boolean var4, Operation<Void> var5) {
      boolean var6 = wp.d();
      if (var6 != this.g) {
         var1.method_3292();
      }

      this.g = var6;
      if (!var6) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         class_310 var7 = class_310.method_1551();
         boolean var8 = var7.field_1730;

         try {
            var7.field_1730 = false;
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            var7.field_1730 = var8;
         }
      }
   }
}

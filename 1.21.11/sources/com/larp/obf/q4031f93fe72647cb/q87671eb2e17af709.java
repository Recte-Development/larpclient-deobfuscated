package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_310;
import net.minecraft.class_408;
import net.minecraft.class_5481;
import net.minecraft.class_303.class_7590;
import net.minecraft.class_338.class_12233;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net/minecraft/class_338$1")
public class q87671eb2e17af709 {
   @WrapOperation(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_338$class_12233;method_75807(IFLnet/minecraft/class_5481;)Z"))
   private boolean Y(class_12233 var1, int var2, float var3, class_5481 var4, Operation<Boolean> var5, @Local(argsOnly = true) class_7590 var6) {
      GUIAnimationModule var7 = GUIAnimationModule.Z();
      class_310 var8 = class_310.method_1551();
      if (var7 != null && var7.r() && !(var8.field_1755 instanceof class_408)) {
         double var9 = Math.clamp((var8.field_1705.method_1738() - var6.comp_895() + var8.method_61966().method_60637(false)) / 9.0, 0.0, 1.0);
         float var11 = (float)(1.0 - Math.pow(1.0 - var9, 3.0));
         float var12 = -8.0F * (1.0F - var11);
         var1.method_75811(var1x -> var1x.translate(var12, 0.0F));

         try {
            return (Boolean)var5.call(new Object[]{var1, var2, var3 * var11, var4});
         } finally {
            var1.method_75811(var1x -> var1x.translate(-var12, 0.0F));
         }
      } else {
         return (Boolean)var5.call(new Object[]{var1, var2, var3, var4});
      }
   }
}

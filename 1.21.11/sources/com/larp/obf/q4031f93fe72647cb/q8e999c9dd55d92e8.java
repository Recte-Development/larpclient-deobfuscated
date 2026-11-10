package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.XRayModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_2350;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net/caffeinemc/mods/sodium/client/render/model/AbstractBlockRenderContext", remap = false)
public abstract class q8e999c9dd55d92e8 {
   @WrapOperation(
      method = "shouldDrawSide",
      require = 0,
      remap = false,
      at = @At(
         value = "INVOKE",
         ordinal = 0,
         target = "Lnet/minecraft/class_2680;method_26173(Lnet/minecraft/class_2350;)Lnet/minecraft/class_265;",
         remap = true
      )
   )
   private class_265 d(class_2680 var1, class_2350 var2, Operation<class_265> var3) {
      return XRayModule.m() && !XRayModule.r(var1.method_26204()) ? class_259.method_1073() : (class_265)var3.call(new Object[]{var1, var2});
   }
}

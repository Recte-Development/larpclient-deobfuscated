package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wi;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.minecraft.class_11228;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_757;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_11228.class)
public abstract class q9006ad41390bfd09 {
   @WrapOperation(method = "method_71291", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_310;method_1522()Lnet/minecraft/class_276;"))
   private class_276 G(class_310 var1, Operation<class_276> var2) {
      class_276 var3 = wi.V(this);
      return var3 != null ? var3 : (class_276)var2.call(new Object[]{var1});
   }

   @Inject(method = "method_70890", at = @At("HEAD"))
   private void B(GpuBufferSlice var1, CallbackInfo var2) {
      wi.Y(this, var1);
   }

   @WrapOperation(method = "method_71291", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_757;method_57796()V"))
   private void z(class_757 var1, Operation<Void> var2) {
      if (!wi.x(this)) {
         var2.call(new Object[]{var1});
      }
   }
}

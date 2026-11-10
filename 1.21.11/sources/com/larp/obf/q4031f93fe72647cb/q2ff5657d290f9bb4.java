package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.b9;
import com.larpclient.bi;
import com.larpclient.c5;
import com.larpclient.cf;
import com.larpclient.f8;
import com.larpclient.qo;
import com.larpclient.sm;
import com.larpclient.t_;
import com.larpclient.wi;
import com.larpclient.module.SkyShaderModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_757.class)
public class q2ff5657d290f9bb4 {
   @Inject(method = "method_3192", at = @At("HEAD"))
   private void r(class_9779 var1, boolean var2, CallbackInfo var3) {
      wi.d();
      qo.z(var1.method_60637(false));
      t_.H();
      c5.O();
      wi.O();
      t_.E();
      f8.m();
      SkyShaderModule var4 = SkyShaderModule.y();
      if (var4 != null) {
         var4.g();
      }
   }

   @ModifyArg(
      method = "method_3188",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11286;method_71123(Lorg/joml/Matrix4f;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;")
   )
   private Matrix4f g(Matrix4f var1) {
      sm.x(var1);
      return var1;
   }

   @Inject(
      method = "method_3192",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11228;method_70890(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V")
   )
   private void C(CallbackInfo var1) {
      bi.e();
      cf.A();
      t_.r();
   }

   @ModifyExpressionValue(
      method = "method_3188",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_757;method_3196(Lnet/minecraft/class_4184;FZ)F", ordinal = 0)
   )
   private float s(float var1) {
      b9.X(var1);
      return var1;
   }

   @ModifyExpressionValue(
      method = "method_3188",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_757;method_3196(Lnet/minecraft/class_4184;FZ)F", ordinal = 1)
   )
   private float o(float var1) {
      b9.x(var1);
      return var1;
   }

   @Inject(
      method = "method_3188",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;clearDepthTexture(Lcom/mojang/blaze3d/textures/GpuTexture;D)V")
   )
   private void x(CallbackInfo var1) {
      wi.h();
   }

   @Inject(
      method = "method_3192",
      at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/class_11228;method_70890(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V")
   )
   private void m(CallbackInfo var1) {
      wi.E();
   }
}

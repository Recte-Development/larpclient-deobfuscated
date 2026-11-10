package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.y1;
import com.larpclient.module.WorldColorModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_765.class)
public abstract class qa53f21885de498d4 {
   @Shadow
   private boolean field_4135;
   @Unique
   private int Q = -1;
   @Unique
   private float x;
   @Unique
   private Object s;

   @Inject(method = "method_3313", at = @At("HEAD"))
   private void u(float var1, CallbackInfo var2) {
      WorldColorModule var3 = WorldColorModule.n();
      boolean var4 = var3 != null && var3.O();
      int var5 = var4 ? var3.k.getValue() : -1;
      float var6 = var4 ? var3.P.getValue().floatValue() : 0.0F;
      class_638 var7 = class_310.method_1551().field_1687;
      if (var7 != this.s || var5 != this.Q || var6 != this.x) {
         this.field_4135 = true;
         this.s = var7;
         this.Q = var5;
         this.x = var6;
      }

      if (this.field_4135) {
         y1.t(this.Q, this.x);
      }
   }

   @WrapOperation(
      method = "method_3313",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V")
   )
   private void h(RenderPass var1, RenderPipeline var2, Operation<Void> var3) {
      RenderPipeline var4 = y1.e(var2, this.Q, this.x);
      var3.call(new Object[]{var1, var4});
      y1.v(var1, var4);
   }
}

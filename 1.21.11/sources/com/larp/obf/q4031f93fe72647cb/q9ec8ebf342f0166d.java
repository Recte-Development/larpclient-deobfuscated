package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.lb;
import com.larpclient.ws;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import net.minecraft.class_1041;
import net.minecraft.class_11228;
import net.minecraft.class_11244;
import net.minecraft.class_310;
import net.minecraft.class_4588;
import net.minecraft.class_8030;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_11228.class)
public abstract class q9ec8ebf342f0166d {
   @WrapOperation(
      method = "method_71287",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11244;comp_4055()Lcom/mojang/blaze3d/pipeline/RenderPipeline;")
   )
   private RenderPipeline w(class_11244 var1, Operation<RenderPipeline> var2) {
      RenderPipeline var3 = (RenderPipeline)var2.call(new Object[]{var1});
      if (lb.Y() == null) {
         return var3;
      }

      RenderPipeline var4 = ws.i(var3);
      return var4 != null ? var4 : var3;
   }

   @WrapOperation(method = "method_71287", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11244;method_70917(Lnet/minecraft/class_4588;)V"))
   private void Q(class_11244 var1, class_4588 var2, Operation<Void> var3) {
      lb.m var4 = lb.Y();
      if (var4 == null) {
         var3.call(new Object[]{var1, var2});
      } else {
         RenderPipeline var5 = var1.comp_4055();
         lb.a var6 = !ws.L(var5) ? lb.a.I : (ws.i(var5) != null ? lb.a.u : lb.a.A);
         lb.w var7 = lb.S(var2, var4, var5.getVertexFormatMode() == class_5596.field_27382, var1.comp_4069(), var6);

         try {
            var3.call(new Object[]{var1, var7});
         } finally {
            var7.E();
         }
      }
   }

   @WrapOperation(method = "method_71287", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11244;comp_4069()Lnet/minecraft/class_8030;"))
   private class_8030 h(class_11244 var1, Operation<class_8030> var2) {
      lb.m var3 = lb.Y();
      class_8030 var4 = (class_8030)var2.call(new Object[]{var1});
      if (var3 != null && var4 != null) {
         class_1041 var5 = class_310.method_1551().method_22683();
         return var3.C(var4, var5.method_4486(), var5.method_4502());
      } else {
         return var4;
      }
   }
}

package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;
import net.minecraft.class_11219;
import net.minecraft.class_12137;
import net.minecraft.class_12246;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_276;
import net.minecraft.class_12247.class_12337;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

public record jv(RenderPipeline pipeline, class_12246 outputTarget, GpuBufferSlice dynamicTransforms, class_11219 scissorState, List<jv.z> textures) {
   public static jv prepare(class_1921 var0) {
      class_12247 var1 = var0.field_64013;
      Matrix4fStack var2 = RenderSystem.getModelViewStack();
      Consumer var3 = var1.field_63996.method_75918();
      if (var3 != null) {
         var2.pushMatrix();
         var3.accept(var2);
      }

      try {
         GpuBufferSlice var4 = RenderSystem.getDynamicUniforms()
            .method_71106(RenderSystem.getModelViewMatrix(), new Vector4f(1.0F), new Vector3f(), var1.field_63988.method_76030());
         List var5 = var1.method_75926()
            .entrySet()
            .stream()
            .map(var0x -> new jv.z((String)var0x.getKey(), ((class_12337)var0x.getValue()).comp_5226(), ((class_12337)var0x.getValue()).comp_5227()))
            .toList();
         class_11219 var6 = RenderSystem.getScissorStateForRenderTypeDraws();
         class_11219 var7 = new class_11219();
         if (var6.method_72091()) {
            var7.method_70814(var6.method_72092(), var6.method_72093(), var6.method_72094(), var6.method_72095());
         }

         return new jv(var0.method_73243(), var1.field_63989, var4, var7, var5);
      } finally {
         if (var3 != null) {
            var2.popMatrix();
         }
      }
   }

   public void drawFromBuffer(tq.l var1) {
      class_276 var2 = this.outputTarget.method_75921();
      GpuTextureView var3 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var2.method_71639();
      GpuTextureView var4 = !var2.field_1478
         ? null
         : (RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var2.method_71640());
      RenderPass var5 = RenderSystem.getDevice()
         .createCommandEncoder()
         .createRenderPass(() -> "Larp immediate material " + this.pipeline.getLocation(), var3, OptionalInt.empty(), var4, OptionalDouble.empty());

      try {
         var5.setPipeline(this.pipeline);
         if (this.scissorState.method_72091()) {
            var5.enableScissor(
               this.scissorState.method_72092(), this.scissorState.method_72093(), this.scissorState.method_72094(), this.scissorState.method_72095()
            );
         }

         RenderSystem.bindDefaultUniforms(var5);
         var5.setUniform("DynamicTransforms", this.dynamicTransforms);

         for (jv.z var7 : this.textures) {
            yf.R(var5, var7.name(), var7.textureView(), var7.sampler());
         }

         var5.setVertexBuffer(0, var1.vertexBuffer());
         var5.setIndexBuffer(var1.indexBuffer(), var1.indexType());
         var5.drawIndexed(var1.baseVertex(), var1.firstIndex(), var1.indexCount(), 1);
      } catch (Throwable var9) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (var5 != null) {
         var5.close();
      }
   }

   public record z(String name, GpuTextureView textureView, class_12137 sampler) {
   }
}

package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.class_12137;
import net.minecraft.class_12289;
import net.minecraft.class_276;
import net.minecraft.class_2960;
import org.jspecify.annotations.Nullable;

public final class yf {
   public static void R(RenderPass var0, String var1, GpuTextureView var2, class_12137 var3) {
      var0.bindTexture(var1, var2, var3);
   }

   public static boolean V(RenderPipeline var0, @Nullable class_12289 var1) {
      return (var1 == null ? RenderSystem.getDevice().precompilePipeline(var0) : RenderSystem.getDevice().precompilePipeline(var0, var1)).isValid();
   }

   public static void l(RenderPass var0, RenderPipeline var1) {
      var0.setPipeline(var1);
   }

   public static class_2960 E(RenderPipeline var0) {
      return var0.getVertexShader();
   }

   public static class_2960 z(RenderPipeline var0) {
      return var0.getFragmentShader();
   }

   public static void o(jv var0, tq.l var1) {
      var0.drawFromBuffer(var1);
   }

   public static jv w(jv var0, RenderPipeline var1) {
      return new jv(var1, var0.outputTarget(), var0.dynamicTransforms(), var0.scissorState(), var0.textures());
   }

   public static class_276 G(jv var0) {
      return var0.outputTarget().method_75921();
   }

   public static String Q() {
      return "OpenGL";
   }

   public static boolean O(class_276 var0) {
      return var0.field_1478;
   }

   private yf() {
   }
}

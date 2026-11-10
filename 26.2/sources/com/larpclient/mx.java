package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.shaders.ShaderSource;
import com.mojang.blaze3d.shaders.ShaderType;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public final class mx {
   public static void x(RenderPass var0, String var1, GpuTextureView var2, GpuSampler var3) {
      var0.bindTexture(var1, var2, var3);
   }

   public static boolean p(RenderPipeline var0, @Nullable ShaderSource var1) {
      return (var1 == null ? RenderSystem.getDevice().precompilePipeline(var0) : RenderSystem.getDevice().precompilePipeline(var0, var1)).isValid();
   }

   public static void i(RenderPass var0, RenderPipeline var1) {
      var0.setPipeline(var1);
   }

   public static Identifier R(RenderPipeline var0) {
      return var0.getVertexShader();
   }

   public static Identifier K(RenderPipeline var0) {
      return var0.getFragmentShader();
   }

   public static void o(PreparedRenderType var0, ExecuteInfo var1) {
      var0.drawFromBuffer(var1);
   }

   public static TextureTarget n(String var0, int var1, int var2, boolean var3, GpuFormat var4) {
      return new TextureTarget(var0, var1, var2, var3, var4);
   }

   public static ShaderManager Q() {
      return Minecraft.getInstance().getShaderManager();
   }

   public static ShaderSource V(BiFunction<Identifier, ShaderType, String> var0) {
      return var0::apply;
   }

   public static List<ColorTargetState> G(RenderPipeline var0) {
      return Arrays.asList(var0.getColorTargetStates());
   }

   public static List<VertexFormat> i(RenderPipeline var0) {
      return Arrays.asList(var0.getVertexFormatBindings());
   }

   public static PreparedRenderType z(PreparedRenderType var0, RenderPipeline var1) {
      return new PreparedRenderType(var1, var0.outputTarget(), var0.dynamicTransforms(), var0.scissorState(), var0.textures());
   }

   public static RenderTarget k(PreparedRenderType var0) {
      return var0.outputTarget().getRenderTarget();
   }

   public static String Z() {
      return Minecraft.getInstance().getWindow().backend().getName();
   }

   public static boolean a(RenderTarget var0) {
      return var0.useDepth;
   }

   private mx() {
   }
}

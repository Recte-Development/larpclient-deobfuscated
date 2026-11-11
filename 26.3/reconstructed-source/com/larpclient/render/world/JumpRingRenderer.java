package com.larpclient.render.world;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.OverlayTextures;
import com.larpclient.render.overlay.PostProcessing;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class JumpRingRenderer {
   private static final RenderType[][] renderTypes = new RenderType[7][2];
   private static GpuBuffer parametersBuffer;
   private static int shaderGeneration = -1;
   private static int color;
   private static float time;

   public static void setParameters(int var0) {
      color = var0;
      time = (float)(System.nanoTime() / 1.0E9 % 3600.0);
   }

   public static RenderType getRenderType(int var0, boolean var1) {
      if (shaderGeneration != PostProcessing.reloadCount) {
         closeBuffer();
         shaderGeneration = PostProcessing.reloadCount;

         for (RenderType[] var5 : renderTypes) {
            Arrays.fill(var5, null);
         }
      }

      int var6 = var1 ? 1 : 0;
      if (renderTypes[var0][var6] == null) {
         Identifier var7 = Identifier.fromNamespaceAndPath("larp-client", "jump/jumpring");
         Builder var8 = RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "jump_" + var0 + "_" + var6))
            .withVertexShader(var7)
            .withFragmentShader(var7)
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withShaderDefine("CLARITY_JUMP")
            .withShaderDefine("JUMP_STYLE", var0)
            .withBindGroupLayout(BindGroupLayout.builder().withUniform("JumpParameters", UniformType.UNIFORM_BUFFER).build())
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA))
            .withCull(false);
         if (var1) {
            var8.withDepthStencilState(Optional.empty());
         } else {
            var8.withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false));
         }

         RenderPipeline var9 = var8.build();
         ClientPipelines.validatePipeline(var9);
         renderTypes[var0][var6] = RenderType.create("larp_jump_" + var0 + "_" + var6, RenderSetup.builder(var9).sortOnUpload().createRenderSetup());
      }

      return renderTypes[var0][var6];
   }

   public static boolean drawJumpRing(PreparedRenderType var0, ExecuteInfo var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("CLARITY_JUMP")) {
         return false;
      }

      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      if (parametersBuffer == null) {
         parametersBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp jump parameters", 136, 16L);
      }

      MemoryStack var3 = MemoryStack.stackPush();

      try {
         ByteBuffer var4 = var3.malloc(16);
         var4.putFloat((color >> 16 & 0xFF) / 255.0F).putFloat((color >> 8 & 0xFF) / 255.0F).putFloat((color & 0xFF) / 255.0F).putFloat(time).flip();
         var2.writeToBuffer(parametersBuffer.slice(), var4);
      } catch (Throwable var11) {
         if (var3 != null) {
            try {
               var3.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }
         }

         throw var11;
      }

      if (var3 != null) {
         var3.close();
      }

      RenderTarget var13 = RenderPipelineCompat.getCompiledPipeline(var0);
      GpuTextureView var14 = OverlayTextures.colorTexture != null ? OverlayTextures.colorTexture : var13.getColorTextureView();
      GpuTextureView var5 = OverlayTextures.depthTexture != null ? OverlayTextures.depthTexture : var13.getDepthTextureView();
      RenderPass var6 = var2.createRenderPass(() -> "Larp jump circles", var14, Optional.empty(), var5, OptionalDouble.empty());

      try {
         RenderPipelineCompat.setPipeline(var6, var0.pipeline());
         ScissorState var7 = var0.scissorState();
         if (var7.enabled()) {
            var6.enableScissor(var7.x(), var7.y(), var7.width(), var7.height());
         }

         RenderSystem.bindDefaultUniforms(var6);
         var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
         var6.setUniform("JumpParameters", parametersBuffer.slice());
         var6.setVertexBuffer(0, var1.vertexBuffer().slice());
         var6.setIndexBuffer(var1.indexBuffer(), var1.indexType());
         var6.drawIndexed(var1.indexCount(), 1, var1.firstIndex(), var1.baseVertex(), 0);
      } catch (Throwable var12) {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }
         }

         throw var12;
      }

      if (var6 != null) {
         var6.close();
      }

      return true;
   }

   public static void closeBuffer() {
      if (parametersBuffer != null) {
         parametersBuffer.close();
         parametersBuffer = null;
      }
   }

   private JumpRingRenderer() {
   }
}

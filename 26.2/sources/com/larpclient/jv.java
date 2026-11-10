package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
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

public final class jv {
   private static final RenderType[][] Z = new RenderType[7][2];
   private static GpuBuffer Q;
   private static int E = -1;
   private static int r;
   private static float I;

   public static void G(int var0) {
      r = var0;
      I = (float)(System.nanoTime() / 1.0E9 % 3600.0);
   }

   public static RenderType A(int var0, boolean var1) {
      if (E != wa.s) {
         x();
         E = wa.s;

         for (RenderType[] var5 : Z) {
            Arrays.fill(var5, null);
         }
      }

      int var6 = var1 ? 1 : 0;
      if (Z[var0][var6] == null) {
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
         ai.U(var9);
         Z[var0][var6] = RenderType.create("larp_jump_" + var0 + "_" + var6, RenderSetup.builder(var9).sortOnUpload().createRenderSetup());
      }

      return Z[var0][var6];
   }

   public static boolean B(PreparedRenderType var0, ExecuteInfo var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("CLARITY_JUMP")) {
         return false;
      }

      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      if (Q == null) {
         Q = RenderSystem.getDevice().createBuffer(() -> "Larp jump parameters", 136, 16L);
      }

      MemoryStack var3 = MemoryStack.stackPush();

      try {
         ByteBuffer var4 = var3.malloc(16);
         var4.putFloat((r >> 16 & 0xFF) / 255.0F).putFloat((r >> 8 & 0xFF) / 255.0F).putFloat((r & 0xFF) / 255.0F).putFloat(I).flip();
         var2.writeToBuffer(Q.slice(), var4);
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

      RenderTarget var13 = mx.k(var0);
      GpuTextureView var14 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var13.getColorTextureView();
      GpuTextureView var5 = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var13.getDepthTextureView();
      RenderPass var6 = var2.createRenderPass(() -> "Larp jump circles", var14, Optional.empty(), var5, OptionalDouble.empty());

      try {
         mx.i(var6, var0.pipeline());
         ScissorState var7 = var0.scissorState();
         if (var7.enabled()) {
            var6.enableScissor(var7.x(), var7.y(), var7.width(), var7.height());
         }

         RenderSystem.bindDefaultUniforms(var6);
         var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
         var6.setUniform("JumpParameters", Q.slice());
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

   public static void x() {
      if (Q != null) {
         Q.close();
         Q = null;
      }
   }

   private jv() {
   }
}

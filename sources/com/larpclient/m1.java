package com.larpclient;

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

public final class m1 {
   private static final RenderType[][] L = new RenderType[7][2];
   private static GpuBuffer B;
   private static int F = -1;
   private static int I;
   private static float o;

   public static void W(int var0) {
      I = var0;
      o = (float)(System.nanoTime() / 1.0E9 % 3600.0);
   }

   public static RenderType F(int var0, boolean var1) {
      if (F != jk.v) {
         n();
         F = jk.v;

         for (RenderType[] var5 : L) {
            Arrays.fill(var5, null);
         }
      }

      int var6 = var1 ? 1 : 0;
      if (L[var0][var6] == null) {
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
         e4.U(var9);
         L[var0][var6] = RenderType.create("larp_jump_" + var0 + "_" + var6, RenderSetup.builder(var9).sortOnUpload().createRenderSetup());
      }

      return L[var0][var6];
   }

   public static boolean u(PreparedRenderType var0, ExecuteInfo var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("CLARITY_JUMP")) {
         return false;
      }

      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      if (B == null) {
         B = RenderSystem.getDevice().createBuffer(() -> "Larp jump parameters", 136, 16L);
      }

      MemoryStack var3 = MemoryStack.stackPush();

      try {
         ByteBuffer var4 = var3.malloc(16);
         var4.putFloat((I >> 16 & 0xFF) / 255.0F).putFloat((I >> 8 & 0xFF) / 255.0F).putFloat((I & 0xFF) / 255.0F).putFloat(o).flip();
         var2.writeToBuffer(B.slice(), var4);
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

      RenderTarget var13 = of.y(var0);
      GpuTextureView var14 = _m.M != null ? _m.M : var13.getColorTextureView();
      GpuTextureView var5 = _m.U != null ? _m.U : var13.getDepthTextureView();
      RenderPass var6 = var2.createRenderPass(() -> "Larp jump circles", var14, Optional.empty(), var5, OptionalDouble.empty());

      try {
         of.z(var6, var0.pipeline());
         ScissorState var7 = var0.scissorState();
         if (var7.enabled()) {
            var6.enableScissor(var7.x(), var7.y(), var7.width(), var7.height());
         }

         RenderSystem.bindDefaultUniforms(var6);
         var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
         var6.setUniform("JumpParameters", B.slice());
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

   public static void n() {
      if (B != null) {
         B.close();
         B = null;
      }
   }

   private m1() {
   }
}

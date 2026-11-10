package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_11219;
import net.minecraft.class_12247;
import net.minecraft.class_1921;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import org.lwjgl.system.MemoryStack;

public final class jg {
   private static final class_1921[][] G = new class_1921[7][2];
   private static GpuBuffer b;
   private static int L = -1;
   private static int W;
   private static float e;

   public static void E(int var0) {
      W = var0;
      e = (float)(System.nanoTime() / 1.0E9 % 3600.0);
   }

   public static class_1921 h(int var0, boolean var1) {
      if (L != t_.g) {
         d();
         L = t_.g;

         for (class_1921[] var5 : G) {
            Arrays.fill(var5, null);
         }
      }

      int var6 = var1 ? 1 : 0;
      if (G[var0][var6] == null) {
         class_2960 var7 = class_2960.method_60655("larp-client", "jump/jumpring");
         Builder var8 = RenderPipeline.builder(new Snippet[]{class_10799.field_56860})
            .withLocation(class_2960.method_60655("larp-client", "jump_" + var0 + "_" + var6))
            .withVertexShader(var7)
            .withFragmentShader(var7)
            .withVertexFormat(class_290.field_1580, class_5596.field_27382)
            .withShaderDefine("CLARITY_JUMP")
            .withShaderDefine("JUMP_STYLE", var0)
            .withUniform("JumpParameters", class_10789.field_60031)
            .withBlend(BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA)
            .withCull(false);
         if (var1) {
            var8.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(false);
         } else {
            var8.withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST).withDepthWrite(false);
         }

         RenderPipeline var9 = var8.build();
         f2.a(var9);
         G[var0][var6] = class_1921.method_75940("larp_jump_" + var0 + "_" + var6, class_12247.method_75927(var9).method_75937().method_75938());
      }

      return G[var0][var6];
   }

   public static boolean g(jv var0, tq.l var1) {
      if (!var0.pipeline().getShaderDefines().comp_3104().contains("CLARITY_JUMP")) {
         return false;
      }

      CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
      if (b == null) {
         b = RenderSystem.getDevice().createBuffer(() -> "Larp jump parameters", 136, 16L);
      }

      MemoryStack var3 = MemoryStack.stackPush();

      try {
         ByteBuffer var4 = var3.malloc(16);
         var4.putFloat((W >> 16 & 0xFF) / 255.0F).putFloat((W >> 8 & 0xFF) / 255.0F).putFloat((W & 0xFF) / 255.0F).putFloat(e).flip();
         var2.writeToBuffer(b.slice(), var4);
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

      class_276 var13 = yf.G(var0);
      GpuTextureView var14 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var13.method_71639();
      GpuTextureView var5 = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var13.method_71640();
      RenderPass var6 = var2.createRenderPass(() -> "Larp jump circles", var14, OptionalInt.empty(), var5, OptionalDouble.empty());

      try {
         yf.l(var6, var0.pipeline());
         class_11219 var7 = var0.scissorState();
         if (var7.method_72091()) {
            var6.enableScissor(var7.method_72092(), var7.method_72093(), var7.method_72094(), var7.method_72095());
         }

         RenderSystem.bindDefaultUniforms(var6);
         var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
         var6.setUniform("JumpParameters", b.slice());
         var6.setVertexBuffer(0, var1.vertexBuffer());
         var6.setIndexBuffer(var1.indexBuffer(), var1.indexType());
         var6.drawIndexed(var1.baseVertex(), var1.firstIndex(), var1.indexCount(), 1);
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

   public static void d() {
      if (b != null) {
         b.close();
         b = null;
      }
   }

   private jg() {
   }
}

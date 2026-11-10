package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.PreparedRenderType.Texture;
import org.lwjgl.system.MemoryStack;

public final class he {
   private static GpuBuffer u;
   private static int I = -1;
   public static boolean C = (boolean)1;
   private static boolean T;
   private static boolean S;
   private static boolean z;

   public static void J(Builder var0) {
      var0.withShaderDefine("SOFT_PARTICLE")
         .withBindGroupLayout(BindGroupLayout.builder().withSampler("SceneDepth").withUniform("ParticleDepth", UniformType.UNIFORM_BUFFER).build());
   }

   public static boolean K() {
      return T;
   }

   public static void W(boolean var0) {
      T = (boolean)0;

      try {
         if (I != wa.s) {
            t();
            z = (boolean)0;
            S = (boolean)0;
            I = wa.s;
         }

         if (!C) {
            t();
            return;
         }

         if (!var0) {
            t();
            return;
         }

         if (S) {
            return;
         }

         if (!z) {
            kj.h();
            z = (boolean)1;
         }

         if (so.v() == null) {
            return;
         }

         if (u == null) {
            u = RenderSystem.getDevice().createBuffer(() -> "Larp particle depth parameters", 136, 16L);
         }

         CommandEncoder var1 = RenderSystem.getDevice().createCommandEncoder();
         boolean var2 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
         MemoryStack var3 = MemoryStack.stackPush();

         try {
            ByteBuffer var4 = var3.malloc(16);
            var4.putFloat(var2 ? 1.0F : 2.0F).putFloat(var2 ? 0.0F : -1.0F).putFloat(0.16F).putFloat(0.0F).flip();
            var1.writeToBuffer(u.slice(), var4);
         } catch (Throwable var8) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (var3 != null) {
            var3.close();
         }

         T = (boolean)1;
         zj.s("Soft particle depth consumers", 1L);
      } catch (RuntimeException var9) {
         S = (boolean)1;

         try {
            t();
         } catch (RuntimeException var6) {
            var9.addSuppressed(var6);
         }

         h.m("Particles", "Soft depth unavailable; ordinary sprites retained until resource reload", var9);
      }
   }

   static boolean F(PreparedRenderType var0, ExecuteInfo var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("SOFT_PARTICLE")) {
         return false;
      }

      if (!T) {
         mx.o(kj.K(var0), var1);
         return true;
      }

      GpuTextureView var2 = so.v();
      if (var2 == null) {
         mx.o(kj.K(var0), var1);
         return true;
      }

      RenderTarget var3 = mx.k(var0);
      GpuTextureView var4 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var3.getColorTextureView();
      GpuTextureView var5 = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var3.getDepthTextureView();
      if (var2 == var5) {
         mx.o(kj.K(var0), var1);
         return true;
      }

      try {
         RenderPass var6 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp soft particles", var4, Optional.empty(), var5, OptionalDouble.empty());

         try {
            mx.i(var6, var0.pipeline());
            ScissorState var7 = var0.scissorState();
            if (var7.enabled()) {
               var6.enableScissor(var7.x(), var7.y(), var7.width(), var7.height());
            }

            RenderSystem.bindDefaultUniforms(var6);
            var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
            var6.setUniform("ParticleDepth", u.slice());
            mx.x(var6, "SceneDepth", var2, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));

            for (Texture var9 : var0.textures()) {
               mx.x(var6, var9.name(), var9.textureView(), var9.sampler());
            }

            var6.setVertexBuffer(0, var1.vertexBuffer().slice());
            var6.setIndexBuffer(var1.indexBuffer(), var1.indexType());
            var6.drawIndexed(var1.indexCount(), 1, var1.firstIndex(), var1.baseVertex(), 0);
         } catch (Throwable var12) {
            if (var6 != null) {
               try {
                  var6.close();
               } catch (Throwable var11) {
                  var12.addSuppressed(var11);
               }
            }

            throw var12;
         }

         if (var6 != null) {
            var6.close();
         }
      } catch (RuntimeException var13) {
         S = (boolean)1;

         try {
            t();
         } catch (RuntimeException var10) {
            var13.addSuppressed(var10);
         }

         h.m("Particles", "Soft particle draw failed; ordinary sprites retained until resource reload", var13);
      }

      return true;
   }

   public static void t() {
      T = (boolean)0;
      if (u != null) {
         AutoCloseable[] var0 = new AutoCloseable[]{u};
         u = null;
         RuntimeException var1 = null;

         for (AutoCloseable var5 : var0) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Exception var9) {
                  RuntimeException var7 = var9 instanceof RuntimeException var8 ? var8 : new IllegalStateException(var9);
                  if (var1 == null) {
                     var1 = var7;
                  } else {
                     var1.addSuppressed(var7);
                  }
               }
            }
         }

         if (var1 != null) {
            throw var1;
         }
      }
   }

   private he() {
   }
}

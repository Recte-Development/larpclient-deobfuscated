package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.PreparedRenderType.Texture;
import org.lwjgl.system.MemoryStack;

public final class d3 {
   private static GpuBuffer E;
   private static int M = -1;
   public static boolean y = (boolean)1;
   private static boolean O;
   private static boolean V;
   private static boolean e;

   public static void R(Builder var0) {
      var0.withShaderDefine("SOFT_PARTICLE")
         .withBindGroupLayout(BindGroupLayout.builder().withUniform("SceneDepth", p7.X).withUniform("ParticleDepth", UniformType.UNIFORM_BUFFER).build());
   }

   public static boolean t() {
      return O;
   }

   public static void G(boolean var0) {
      O = (boolean)0;

      try {
         if (M != nl.q) {
            X();
            e = (boolean)0;
            V = (boolean)0;
            M = nl.q;
         }

         if (!y) {
            X();
            return;
         }

         if (!var0) {
            X();
            return;
         }

         if (V) {
            return;
         }

         if (!e) {
            fz.y();
            e = (boolean)1;
         }

         if (_s.a() == null) {
            return;
         }

         if (E == null) {
            E = RenderSystem.getDevice().createBuffer(() -> "Larp particle depth parameters", 136, 16L);
         }

         CommandEncoder var1 = RenderSystem.getDevice().createCommandEncoder();
         boolean var2 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
         MemoryStack var3 = MemoryStack.stackPush();

         try {
            ByteBuffer var4 = var3.malloc(16);
            var4.putFloat(var2 ? 1.0F : 2.0F).putFloat(var2 ? 0.0F : -1.0F).putFloat(0.16F).putFloat(0.0F).flip();
            var1.writeToBuffer(E.slice(), var4);
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

         O = (boolean)1;
         he.z("Soft particle depth consumers", 1L);
      } catch (RuntimeException var9) {
         V = (boolean)1;

         try {
            X();
         } catch (RuntimeException var6) {
            var9.addSuppressed(var6);
         }

         _q.o("Particles", "Soft depth unavailable; ordinary sprites retained until resource reload", var9);
      }
   }

   static boolean m(PreparedRenderType var0, ExecuteInfo var1) {
      if (!var0.pipeline().getShaderDefines().flags().contains("SOFT_PARTICLE")) {
         return false;
      }

      if (!O) {
         p7.v(fz.H(var0), var1);
         return true;
      }

      GpuTextureView var2 = _s.a();
      if (var2 == null) {
         p7.v(fz.H(var0), var1);
         return true;
      }

      RenderTarget var3 = p7.g(var0);
      GpuTextureView var4 = jf.b != null ? jf.b : var3.getColorTextureView();
      GpuTextureView var5 = jf.q != null ? jf.q : var3.getDepthTextureView();
      if (var2 == var5) {
         p7.v(fz.H(var0), var1);
         return true;
      }

      try {
         RenderPass var6 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp soft particles", var4, Optional.empty(), var5, OptionalDouble.empty());

         try {
            p7.K(var6, var0.pipeline());
            ScissorState var7 = var0.scissorState();
            if (var7.enabled()) {
               var6.enableScissor(var7.x(), var7.y(), var7.width(), var7.height());
            }

            RenderSystem.bindDefaultUniforms(var6);
            var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
            var6.setUniform("ParticleDepth", E.slice());
            p7.t(var6, "SceneDepth", var2, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));

            for (Texture var9 : var0.textures()) {
               p7.t(var6, var9.name(), var9.textureView(), var9.sampler());
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
         V = (boolean)1;

         try {
            X();
         } catch (RuntimeException var10) {
            var13.addSuppressed(var10);
         }

         _q.o("Particles", "Soft particle draw failed; ordinary sprites retained until resource reload", var13);
      }

      return true;
   }

   public static void X() {
      O = (boolean)0;
      if (E != null) {
         AutoCloseable[] var0 = new AutoCloseable[]{E};
         E = null;
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

   private d3() {
   }
}

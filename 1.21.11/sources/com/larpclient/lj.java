package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_11219;
import net.minecraft.class_276;
import org.lwjgl.system.MemoryStack;

public final class lj {
   private static GpuBuffer s;
   private static int L = -1;
   public static boolean B = (boolean)1;
   private static boolean h;
   private static boolean X;
   private static boolean a;

   public static void o(Builder var0) {
      var0.withShaderDefine("SOFT_PARTICLE").withSampler("SceneDepth").withUniform("ParticleDepth", class_10789.field_60031);
   }

   public static boolean x() {
      return h;
   }

   public static void s(boolean var0) {
      h = (boolean)0;

      try {
         if (L != t_.g) {
            m();
            a = (boolean)0;
            X = (boolean)0;
            L = t_.g;
         }

         if (!B) {
            m();
            return;
         }

         if (!var0) {
            m();
            return;
         }

         if (X) {
            return;
         }

         if (!a) {
            f1.P();
            a = (boolean)1;
         }

         if (h7.J() == null) {
            return;
         }

         if (s == null) {
            s = RenderSystem.getDevice().createBuffer(() -> "Larp particle depth parameters", 136, 16L);
         }

         CommandEncoder var1 = RenderSystem.getDevice().createCommandEncoder();
         byte var2 = 0;
         MemoryStack var3 = MemoryStack.stackPush();

         try {
            ByteBuffer var4 = var3.malloc(16);
            var4.putFloat(var2 != 0 ? 1.0F : 2.0F).putFloat(var2 != 0 ? 0.0F : -1.0F).putFloat(0.16F).putFloat(0.0F).flip();
            var1.writeToBuffer(s.slice(), var4);
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

         h = (boolean)1;
         c5.L("Soft particle depth consumers", 1L);
      } catch (RuntimeException var9) {
         X = (boolean)1;

         try {
            m();
         } catch (RuntimeException var6) {
            var9.addSuppressed(var6);
         }

         q8.u("Particles", "Soft depth unavailable; ordinary sprites retained until resource reload", var9);
      }
   }

   static boolean s(jv var0, tq.l var1) {
      if (!var0.pipeline().getShaderDefines().comp_3104().contains("SOFT_PARTICLE")) {
         return false;
      }

      if (!h) {
         yf.o(f1.X(var0), var1);
         return true;
      }

      GpuTextureView var2 = h7.J();
      if (var2 == null) {
         yf.o(f1.X(var0), var1);
         return true;
      }

      class_276 var3 = yf.G(var0);
      GpuTextureView var4 = RenderSystem.outputColorTextureOverride != null ? RenderSystem.outputColorTextureOverride : var3.method_71639();
      GpuTextureView var5 = RenderSystem.outputDepthTextureOverride != null ? RenderSystem.outputDepthTextureOverride : var3.method_71640();
      if (var2 == var5) {
         yf.o(f1.X(var0), var1);
         return true;
      }

      try {
         RenderPass var6 = RenderSystem.getDevice()
            .createCommandEncoder()
            .createRenderPass(() -> "Larp soft particles", var4, OptionalInt.empty(), var5, OptionalDouble.empty());

         try {
            yf.l(var6, var0.pipeline());
            class_11219 var7 = var0.scissorState();
            if (var7.method_72091()) {
               var6.enableScissor(var7.method_72092(), var7.method_72093(), var7.method_72094(), var7.method_72095());
            }

            RenderSystem.bindDefaultUniforms(var6);
            var6.setUniform("DynamicTransforms", var0.dynamicTransforms());
            var6.setUniform("ParticleDepth", s.slice());
            yf.R(var6, "SceneDepth", var2, RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST));

            for (jv.z var9 : var0.textures()) {
               yf.R(var6, var9.name(), var9.textureView(), var9.sampler());
            }

            var6.setVertexBuffer(0, var1.vertexBuffer());
            var6.setIndexBuffer(var1.indexBuffer(), var1.indexType());
            var6.drawIndexed(var1.baseVertex(), var1.firstIndex(), var1.indexCount(), 1);
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
         X = (boolean)1;

         try {
            m();
         } catch (RuntimeException var10) {
            var13.addSuppressed(var10);
         }

         q8.u("Particles", "Soft particle draw failed; ordinary sprites retained until resource reload", var13);
      }

      return true;
   }

   public static void m() {
      h = (boolean)0;
      if (s != null) {
         AutoCloseable[] var0 = new AutoCloseable[]{s};
         s = null;
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

   private lj() {
   }
}

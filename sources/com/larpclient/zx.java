package com.larpclient;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.resources.Identifier;

public final class zx implements AutoCloseable {
   public static final int Q = 160;
   private GpuBuffer h;
   private GpuBuffer K;
   private GpuBuffer b;

   public static RenderPipeline f(String var0, String... var1) {
      Builder var2 = BindGroupLayout.builder().withUniform("SkyPalette", UniformType.UNIFORM_BUFFER);

      for (String var6 : var1) {
         var2.withUniform(var6, hj.G);
      }

      com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder var7 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "sky/" + var0))
         .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", var0.startsWith("larp/") ? "larp/fullscreen" : "sky/dome"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", var0.startsWith("larp/") ? var0 : "sky/" + var0))
         .withVertexBinding(0, DefaultVertexFormat.POSITION)
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(Optional.empty())
         .withCull(false)
         .withColorTargetState(var0.startsWith("larp/") ? new ColorTargetState(BlendFunction.TRANSLUCENT) : ColorTargetState.DEFAULT);
      if (!var0.startsWith("larp/")) {
         var7.withBindGroupLayout(BindGroupLayouts.DYNAMIC_TRANSFORMS).withBindGroupLayout(BindGroupLayouts.PROJECTION);
      }

      return var7.withBindGroupLayout(var2.build()).build();
   }

   private void K(boolean var1) {
      if (!var1 && this.h == null) {
         mt.K("Sky", "Sky renderer initialized: camera-centered cube, no depth writes");
         ByteBufferBuilder var2 = ByteBufferBuilder.exactlySized(36 * DefaultVertexFormat.POSITION.getVertexSize());

         try {
            BufferBuilder var3 = new BufferBuilder(var2, PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION);
            float[][] var4 = new float[][]{
               {-1.0F, -1.0F, -1.0F},
               {1.0F, -1.0F, -1.0F},
               {1.0F, 1.0F, -1.0F},
               {-1.0F, 1.0F, -1.0F},
               {-1.0F, -1.0F, 1.0F},
               {1.0F, -1.0F, 1.0F},
               {1.0F, 1.0F, 1.0F},
               {-1.0F, 1.0F, 1.0F}
            };
            int[][] var5 = new int[][]{{0, 1, 2, 3}, {5, 4, 7, 6}, {4, 0, 3, 7}, {1, 5, 6, 2}, {3, 2, 6, 7}, {4, 5, 1, 0}};
            int[] var6 = new int[]{0, 1, 2, 0, 2, 3};

            for (int[] var10 : var5) {
               for (int var14 : var6) {
                  float[] var15 = var4[var10[var14]];
                  var3.addVertex(var15[0], var15[1], var15[2]);
               }
            }

            MeshData var20 = var3.buildOrThrow();

            try {
               this.h = RenderSystem.getDevice().createBuffer(() -> "Larp sky cube", 32, var20.vertexBuffer());
            } catch (Throwable var18) {
               if (var20 != null) {
                  try {
                     var20.close();
                  } catch (Throwable var17) {
                     var18.addSuppressed(var17);
                  }
               }

               throw var18;
            }

            if (var20 != null) {
               var20.close();
            }
         } catch (Throwable var19) {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (Throwable var16) {
                  var19.addSuppressed(var16);
               }
            }

            throw var19;
         }

         if (var2 != null) {
            var2.close();
         }
      }

      if (this.b == null) {
         this.b = RenderSystem.getDevice().createBuffer(() -> "Larp sky uniforms", 136, 160L);
      }
   }

   public void r(e_ var1, oq var2, ByteBuffer var3) {
      boolean var4 = hj.q(var2.i()).getPath().startsWith("larp/");
      this.K(var4);
      if (var4 && this.K == null) {
         this.T();
      }

      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      var5.writeToBuffer(this.b.slice(), var3);
      GpuBufferSlice var6 = var4 ? null : RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
      RenderPass var7 = var5.createRenderPass(
         () -> "Larp world sky", var1.F.getColorTextureView(), Optional.empty(), var1.F.getDepthTextureView(), OptionalDouble.empty()
      );

      try {
         hj.P(var7, var2.i());
         if (!var4) {
            RenderSystem.bindDefaultUniforms(var7);
            var7.setUniform("DynamicTransforms", var6);
         }

         var7.setUniform("SkyPalette", this.b.slice());
         var2.bindTextures(var7);
         var7.setVertexBuffer(0, (var4 ? this.K : this.h).slice());
         var7.draw(var4 ? 3 : 36, 1, 0, 0);
      } catch (Throwable var11) {
         if (var7 != null) {
            try {
               var7.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }
         }

         throw var11;
      }

      if (var7 != null) {
         var7.close();
      }
   }

   private void T() {
      ByteBufferBuilder var1 = ByteBufferBuilder.exactlySized(3 * DefaultVertexFormat.POSITION.getVertexSize());

      try {
         BufferBuilder var2 = new BufferBuilder(var1, PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION);
         var2.addVertex(-1.0F, -1.0F, 0.0F);
         var2.addVertex(3.0F, -1.0F, 0.0F);
         var2.addVertex(-1.0F, 3.0F, 0.0F);
         MeshData var3 = var2.buildOrThrow();

         try {
            this.K = RenderSystem.getDevice().createBuffer(() -> "Larp full-screen sky", 32, var3.vertexBuffer());
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
      } catch (Throwable var9) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }
         }

         throw var9;
      }

      if (var1 != null) {
         var1.close();
      }

      mt.K("SkyShader", "Full-screen sky triangle initialized; one fragment per pixel, camera rays independent of native sky transforms");
   }

   @Override
   public void close() {
      if (this.h != null) {
         this.h.close();
         this.h = null;
      }

      if (this.K != null) {
         this.K.close();
         this.K = null;
      }

      if (this.b != null) {
         this.b.close();
         this.b = null;
      }
   }
}

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

public final class bm implements AutoCloseable {
   public static final int D = 160;
   private GpuBuffer v;
   private GpuBuffer H;
   private GpuBuffer L;

   public static RenderPipeline m(String var0, String... var1) {
      Builder var2 = BindGroupLayout.builder().withUniform("SkyPalette", UniformType.UNIFORM_BUFFER);

      for (String var6 : var1) {
         var2.withUniform(var6, p7.X);
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

   private void C(boolean var1) {
      if (!var1 && this.v == null) {
         _q.z("Sky", "Sky renderer initialized: camera-centered cube, no depth writes");
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
               this.v = RenderSystem.getDevice().createBuffer(() -> "Larp sky cube", 32, var20.vertexBuffer());
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

      if (this.L == null) {
         this.L = RenderSystem.getDevice().createBuffer(() -> "Larp sky uniforms", 136, 160L);
      }
   }

   public void a(pa var1, wm var2, ByteBuffer var3) {
      boolean var4 = p7.a(var2.S()).getPath().startsWith("larp/");
      this.C(var4);
      if (var4 && this.H == null) {
         this.H();
      }

      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      var5.writeToBuffer(this.L.slice(), var3);
      GpuBufferSlice var6 = var4 ? null : RenderSystem.getDynamicUniforms().writeTransform(RenderSystem.getModelViewMatrixCopy());
      RenderPass var7 = var5.createRenderPass(
         () -> "Larp world sky", var1.V.getColorTextureView(), Optional.empty(), var1.V.getDepthTextureView(), OptionalDouble.empty()
      );

      try {
         p7.K(var7, var2.S());
         if (!var4) {
            RenderSystem.bindDefaultUniforms(var7);
            var7.setUniform("DynamicTransforms", var6);
         }

         var7.setUniform("SkyPalette", this.L.slice());
         var2.bindTextures(var7);
         var7.setVertexBuffer(0, (var4 ? this.H : this.v).slice());
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

   private void H() {
      ByteBufferBuilder var1 = ByteBufferBuilder.exactlySized(3 * DefaultVertexFormat.POSITION.getVertexSize());

      try {
         BufferBuilder var2 = new BufferBuilder(var1, PrimitiveTopology.TRIANGLES, DefaultVertexFormat.POSITION);
         var2.addVertex(-1.0F, -1.0F, 0.0F);
         var2.addVertex(3.0F, -1.0F, 0.0F);
         var2.addVertex(-1.0F, 3.0F, 0.0F);
         MeshData var3 = var2.buildOrThrow();

         try {
            this.H = RenderSystem.getDevice().createBuffer(() -> "Larp full-screen sky", 32, var3.vertexBuffer());
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

      _q.z("SkyShader", "Full-screen sky triangle initialized; one fragment per pixel, camera rays independent of native sky transforms");
   }

   @Override
   public void close() {
      if (this.v != null) {
         this.v.close();
         this.v = null;
      }

      if (this.H != null) {
         this.H.close();
         this.H = null;
      }

      if (this.L != null) {
         this.L.close();
         this.L = null;
      }
   }
}

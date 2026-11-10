package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_287;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_9799;
import net.minecraft.class_9801;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class fy implements AutoCloseable {
   public static final int T = 160;
   private GpuBuffer o;
   private GpuBuffer j;
   private GpuBuffer Z;

   public static RenderPipeline o(String var0, String... var1) {
      Builder var2 = RenderPipeline.builder(new Snippet[0]).withUniform("SkyPalette", class_10789.field_60031);

      for (String var6 : var1) {
         var2.withSampler(var6);
      }

      Builder var7 = var2.withLocation(class_2960.method_60655("larp-client", "sky/" + var0))
         .withVertexShader(class_2960.method_60655("larp-client", var0.startsWith("larp/") ? "larp/fullscreen" : "sky/dome"))
         .withFragmentShader(class_2960.method_60655("larp-client", var0.startsWith("larp/") ? var0 : "sky/" + var0))
         .withVertexFormat(class_290.field_1592, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false)
         .withoutBlend();
      if (var0.startsWith("larp/")) {
         var7.withBlend(BlendFunction.TRANSLUCENT);
      }

      if (!var0.startsWith("larp/")) {
         var7.withUniform("DynamicTransforms", class_10789.field_60031).withUniform("Projection", class_10789.field_60031);
      }

      return var7.build();
   }

   private void g(boolean var1) {
      if (!var1 && this.o == null) {
         q8.G("Sky", "Sky renderer initialized: camera-centered cube, no depth writes");
         class_9799 var2 = class_9799.method_72201(36 * class_290.field_1592.getVertexSize());

         try {
            class_287 var3 = new class_287(var2, class_5596.field_27379, class_290.field_1592);
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
                  var3.method_22912(var15[0], var15[1], var15[2]);
               }
            }

            class_9801 var20 = var3.method_60800();

            try {
               this.o = RenderSystem.getDevice().createBuffer(() -> "Larp sky cube", 32, var20.method_60818());
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

      if (this.Z == null) {
         this.Z = RenderSystem.getDevice().createBuffer(() -> "Larp sky uniforms", 136, 160L);
      }
   }

   public void D(gx var1, o var2, ByteBuffer var3) {
      boolean var4 = yf.z(var2.h()).method_12832().startsWith("larp/");
      this.g(var4);
      if (var4 && this.j == null) {
         this.R();
      }

      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      var5.writeToBuffer(this.Z.slice(), var3);
      GpuBufferSlice var6 = var4
         ? null
         : RenderSystem.getDynamicUniforms().method_71106(RenderSystem.getModelViewMatrix(), new Vector4f(1.0F), new Vector3f(), new Matrix4f());
      RenderPass var7 = var5.createRenderPass(() -> "Larp world sky", var1.X.method_71639(), OptionalInt.empty(), var1.X.method_71640(), OptionalDouble.empty());

      try {
         yf.l(var7, var2.h());
         if (!var4) {
            RenderSystem.bindDefaultUniforms(var7);
            var7.setUniform("DynamicTransforms", var6);
         }

         var7.setUniform("SkyPalette", this.Z.slice());
         var2.bindTextures(var7);
         var7.setVertexBuffer(0, var4 ? this.j : this.o);
         var7.draw(0, var4 ? 3 : 36);
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

   private void R() {
      class_9799 var1 = class_9799.method_72201(3 * class_290.field_1592.getVertexSize());

      try {
         class_287 var2 = new class_287(var1, class_5596.field_27379, class_290.field_1592);
         var2.method_22912(-1.0F, -1.0F, 0.0F);
         var2.method_22912(3.0F, -1.0F, 0.0F);
         var2.method_22912(-1.0F, 3.0F, 0.0F);
         class_9801 var3 = var2.method_60800();

         try {
            this.j = RenderSystem.getDevice().createBuffer(() -> "Larp full-screen sky", 32, var3.method_60818());
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

      q8.G("SkyShader", "Full-screen sky triangle initialized; one fragment per pixel, camera rays independent of native sky transforms");
   }

   @Override
   public void close() {
      if (this.o != null) {
         this.o.close();
         this.o = null;
      }

      if (this.j != null) {
         this.j.close();
         this.j = null;
      }

      if (this.Z != null) {
         this.Z.close();
         this.Z = null;
      }
   }
}

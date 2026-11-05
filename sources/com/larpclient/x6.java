package com.larpclient;

import com.larpclient.module.MotionBlurModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class x6 {
   static final float Q = 0.033333335F;
   static final float w = 0.1F;
   static final float r = 0.03F;
   static final float g = 60.0F;
   private static final RenderPipeline X = I("motion_blur", "motion_blur", true, true, ColorTargetState.DEFAULT);
   private static final RenderPipeline A = I(
      "motion_accumulate", "motion_accumulate", true, true, new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15)
   );
   private static final RenderPipeline J = I("motion_present", "motion_present", false, false, ColorTargetState.DEFAULT);
   private static final e4 x = new e4();
   private static final e4 k = new e4(GpuFormat.RGBA16_FLOAT);
   private static GpuBuffer i;
   private static int E = -1;
   private static int K;
   private static int p;
   private static int s;
   private static boolean L;
   private static boolean h;
   private static boolean Z;
   private static long F;
   private static float l;
   private static float q;
   private static float Y;
   private static float C;

   public static void t() {
      Minecraft var0 = Minecraft.getInstance();
      MotionBlurModule var1 = MotionBlurModule.U();
      if (var0.level != null && var1 != null && var1.isRuntimeActive()) {
         if (E != m5.k) {
            o();
            L = (boolean)0;
            E = m5.k;
         }

         if (!L) {
            Camera var2 = var0.gameRenderer.mainCamera();
            long var3 = System.nanoTime();
            float var5 = Z ? (float)(var3 - F) / 1.0E9F : 0.0F;
            float var6 = var2.yRot();
            float var7 = var2.xRot();
            int var8 = System.identityHashCode(var0.level) * 31 + System.identityHashCode(var2.entity());
            float var9 = Mth.wrapDegrees(var6 - l);
            float var10 = var7 - q;
            if (Z && var8 == s && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               Y = x(Y, var9 / var5, var5);
               C = x(C, var10 / var5, var5);
            } else {
               C = 0.0F;
               Y = 0.0F;
               h = (boolean)0;
            }

            Z = (boolean)1;
            s = var8;
            F = var3;
            l = var6;
            q = var7;
            float var11 = var1.K.P().floatValue();
            boolean var12 = var1.n.P().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               x.close();
               h = (boolean)(h & (k.b != null ? 1 : 0));
            } else {
               k.close();
               h = (boolean)0;
               if (Math.max(Math.abs(Y), Math.abs(C)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = kx.G();
            String var16 = "resources";

            try {
               if (i == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{X, A, J}) {
                     ea.z(var20);
                  }

                  i = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  mt.K("MotionBlur", "Motion blur shaders loaded");
               }

               RenderTarget var49 = var0.gameRenderer.mainRenderTarget();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians(var2.getFov()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  e(var6, var7, var6 - Y * var13, var7 - C * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * var49.width / Math.max(1, var49.height))
                     .putFloat(var51)
                     .putFloat(var1.V.P().floatValue() / 100.0F)
                     .putFloat(Y(var1.v.P()));
                  var21.putFloat(1.0F / var49.width).putFloat(1.0F / var49.height).putFloat(h ? K(var5, var11) : 0.0F).putFloat(h ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(i.slice(), var21);
               } catch (Throwable var46) {
                  if (var52 != null) {
                     try {
                        var52.close();
                     } catch (Throwable var42) {
                        var46.addSuppressed(var42);
                     }
                  }

                  throw var46;
               }

               if (var52 != null) {
                  var52.close();
               }

               GpuSampler var53 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var54 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               if (var12) {
                  var16 = "accumulate";
                  if (var49.width != K || var49.height != p) {
                     h = (boolean)0;
                  }

                  k.e(var49.width, var49.height, 1, e4.o.x);
                  K = var49.width;
                  p = var49.height;
                  RenderPass var22 = var50.createRenderPass(() -> "Larp MotionBlur accumulate", k.v.getColorTextureView(), Optional.empty());

                  try {
                     hj.P(var22, A);
                     var22.setUniform("MotionBlur", i.slice());
                     hj.u(var22, "Source", var49.getColorTextureView(), var54);
                     hj.u(var22, "History", k.b.getColorTextureView(), var54);
                     hj.u(var22, "Depth", var49.getDepthTextureView(), var54);
                     var22.draw(3, 1, 0, 0);
                  } catch (Throwable var45) {
                     if (var22 != null) {
                        try {
                           var22.close();
                        } catch (Throwable var41) {
                           var45.addSuppressed(var41);
                        }
                     }

                     throw var45;
                  }

                  if (var22 != null) {
                     var22.close();
                  }

                  TextureTarget var55 = k.v;
                  k.v = k.b;
                  k.b = var55;
                  h = (boolean)1;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49.getColorTextureView(), Optional.empty());

                  try {
                     hj.P(var23, J);
                     hj.u(var23, "History", var55.getColorTextureView(), var54);
                     hj.u(var23, "Depth", var49.getDepthTextureView(), var54);
                     var23.draw(3, 1, 0, 0);
                  } catch (Throwable var44) {
                     if (var23 != null) {
                        try {
                           var23.close();
                        } catch (Throwable var40) {
                           var44.addSuppressed(var40);
                        }
                     }

                     throw var44;
                  }

                  if (var23 != null) {
                     var23.close();
                  }
               } else {
                  var16 = "streak";
                  x.e(var49.width, var49.height, 1, e4.o.c);
                  m5.l(ea.m, var49, x.b);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49.getColorTextureView(), Optional.empty());

                  try {
                     hj.P(var56, X);
                     var56.setUniform("MotionBlur", i.slice());
                     hj.u(var56, "Source", x.b.getColorTextureView(), var53);
                     hj.u(var56, "Depth", var49.getDepthTextureView(), var54);
                     var56.draw(3, 1, 0, 0);
                  } catch (Throwable var43) {
                     if (var56 != null) {
                        try {
                           var56.close();
                        } catch (Throwable var39) {
                           var43.addSuppressed(var39);
                        }
                     }

                     throw var43;
                  }

                  if (var56 != null) {
                     var56.close();
                  }
               }
            } catch (RuntimeException var47) {
               L = (boolean)1;

               try {
                  o();
               } catch (RuntimeException var38) {
                  var47.addSuppressed(var38);
               }

               mt.P("MotionBlur", "MotionBlurRenderer stage=" + var16 + " mode=" + var1.n.P() + " framebuffer=scene; disabled until resource reload", var47);
            } finally {
               kx.u("MotionBlur", var14);
            }
         }
      } else {
         b();
      }
   }

   static float x(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 / 0.03F));
   }

   static float K(float var0, float var1) {
      return (float)Math.exp(-var0 / (var1 * 0.1F));
   }

   static int Y(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f e(float var0, float var1, float var2, float var3) {
      return k(var2, var3).mul(k(var0, var1).transpose());
   }

   private static Matrix4f k(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians(var1)).rotateY((float)Math.toRadians(var0) + (float) Math.PI);
   }

   private static void b() {
      h = (boolean)0;
      Z = (boolean)0;
      C = 0.0F;
      Y = 0.0F;
   }

   public static void o() {
      b();

      try {
         x.close();
         k.close();
      } finally {
         if (i != null) {
            i.close();
            i = null;
         }
      }
   }

   private static RenderPipeline I(String var0, String var1, boolean var2, boolean var3, ColorTargetState var4) {
      Builder var5 = BindGroupLayout.builder();
      if (var2) {
         var5.withUniform("Source", hj.G);
      }

      if (var1.equals("motion_blur")) {
         var5.withUniform("Depth", hj.G);
      } else {
         var5.withUniform("History", hj.G).withUniform("Depth", hj.G);
      }

      if (var3) {
         var5.withUniform("MotionBlur", UniformType.UNIFORM_BUFFER);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(T(var0))
         .withVertexShader(T("post/fullscreen"))
         .withFragmentShader(T("post/" + var1))
         .withBindGroupLayout(var5.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var4)
         .withCull(false)
         .build();
   }

   private static Identifier T(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private x6() {
   }
}

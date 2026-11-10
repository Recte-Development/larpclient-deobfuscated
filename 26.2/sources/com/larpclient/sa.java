package com.larpclient;

import com.larpclient.module.MotionBlurModule;
import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.pipeline.BindGroupLayout.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class sa {
   static final float z = 0.033333335F;
   static final float a = 0.1F;
   static final float o = 0.03F;
   static final float R = 60.0F;
   private static final RenderPipeline I = d("motion_blur", "motion_blur", true, true, ColorTargetState.DEFAULT);
   private static final RenderPipeline H = d(
      "motion_accumulate", "motion_accumulate", true, true, new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15)
   );
   private static final RenderPipeline M = d("motion_present", "motion_present", false, false, ColorTargetState.DEFAULT);
   private static final bt O = new bt();
   private static final bt K = new bt(GpuFormat.RGBA16_FLOAT);
   private static GpuBuffer Q;
   private static int V = -1;
   private static int r;
   private static int s;
   private static int D;
   private static boolean g;
   private static boolean w;
   private static boolean C;
   private static long t;
   private static float X;
   private static float y;
   private static float F;
   private static float d;

   public static void l() {
      Minecraft var0 = Minecraft.getInstance();
      MotionBlurModule var1 = MotionBlurModule.E();
      if (var0.level != null && var1 != null && var1.isRuntimeActive()) {
         if (V != wa.s) {
            u();
            g = (boolean)0;
            V = wa.s;
         }

         if (!g) {
            Camera var2 = var0.gameRenderer.mainCamera();
            long var3 = System.nanoTime();
            float var5 = C ? (float)(var3 - t) / 1.0E9F : 0.0F;
            float var6 = var2.yRot();
            float var7 = var2.xRot();
            int var8 = System.identityHashCode(var0.level) * 31 + System.identityHashCode(var2.entity());
            float var9 = Mth.wrapDegrees(var6 - X);
            float var10 = var7 - y;
            if (C && var8 == D && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               F = o(F, var9 / var5, var5);
               d = o(d, var10 / var5, var5);
            } else {
               d = 0.0F;
               F = 0.0F;
               w = (boolean)0;
            }

            C = (boolean)1;
            D = var8;
            t = var3;
            X = var6;
            y = var7;
            float var11 = var1.N.getValue().floatValue();
            boolean var12 = var1.g.getValue().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               O.close();
               w = (boolean)(w & (K.M != null ? 1 : 0));
            } else {
               K.close();
               w = (boolean)0;
               if (Math.max(Math.abs(F), Math.abs(d)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = zj.m();
            String var16 = "resources";

            try {
               if (Q == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{I, H, M}) {
                     ai.U(var20);
                  }

                  Q = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  h.A("MotionBlur", "Motion blur shaders loaded");
               }

               RenderTarget var49 = var0.gameRenderer.mainRenderTarget();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians(var2.getFov()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  V(var6, var7, var6 - F * var13, var7 - d * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * var49.width / Math.max(1, var49.height))
                     .putFloat(var51)
                     .putFloat(var1.q.getValue().floatValue() / 100.0F)
                     .putFloat(t(var1.i.getValue()));
                  var21.putFloat(1.0F / var49.width).putFloat(1.0F / var49.height).putFloat(w ? S(var5, var11) : 0.0F).putFloat(w ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(Q.slice(), var21);
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
                  if (var49.width != r || var49.height != s) {
                     w = (boolean)0;
                  }

                  K.V(var49.width, var49.height, 1, bt.p.M);
                  r = var49.width;
                  s = var49.height;
                  RenderPass var22 = var50.createRenderPass(() -> "Larp MotionBlur accumulate", K.D.getColorTextureView(), Optional.empty());

                  try {
                     mx.i(var22, H);
                     var22.setUniform("MotionBlur", Q.slice());
                     mx.x(var22, "Source", var49.getColorTextureView(), var54);
                     mx.x(var22, "History", K.M.getColorTextureView(), var54);
                     mx.x(var22, "Depth", var49.getDepthTextureView(), var54);
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

                  TextureTarget var55 = K.D;
                  K.D = K.M;
                  K.M = var55;
                  w = (boolean)1;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49.getColorTextureView(), Optional.empty());

                  try {
                     mx.i(var23, M);
                     mx.x(var23, "History", var55.getColorTextureView(), var54);
                     mx.x(var23, "Depth", var49.getDepthTextureView(), var54);
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
                  O.V(var49.width, var49.height, 1, bt.p.S);
                  wa.Y(ai.C, var49, O.M);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49.getColorTextureView(), Optional.empty());

                  try {
                     mx.i(var56, I);
                     var56.setUniform("MotionBlur", Q.slice());
                     mx.x(var56, "Source", O.M.getColorTextureView(), var53);
                     mx.x(var56, "Depth", var49.getDepthTextureView(), var54);
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
               g = (boolean)1;

               try {
                  u();
               } catch (RuntimeException var38) {
                  var47.addSuppressed(var38);
               }

               h.m(
                  "MotionBlur",
                  "MotionBlurRenderer stage=" + var16 + " mode=" + var1.g.getValue() + " framebuffer=scene; disabled until resource reload",
                  var47
               );
            } finally {
               zj.l("MotionBlur", var14);
            }
         }
      } else {
         F();
      }
   }

   static float o(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 / 0.03F));
   }

   static float S(float var0, float var1) {
      return (float)Math.exp(-var0 / (var1 * 0.1F));
   }

   static int t(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f V(float var0, float var1, float var2, float var3) {
      return T(var2, var3).mul(T(var0, var1).transpose());
   }

   private static Matrix4f T(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians(var1)).rotateY((float)Math.toRadians(var0) + (float) Math.PI);
   }

   private static void F() {
      w = (boolean)0;
      C = (boolean)0;
      d = 0.0F;
      F = 0.0F;
   }

   public static void u() {
      F();

      try {
         O.close();
         K.close();
      } finally {
         if (Q != null) {
            Q.close();
            Q = null;
         }
      }
   }

   private static RenderPipeline d(String var0, String var1, boolean var2, boolean var3, ColorTargetState var4) {
      Builder var5 = BindGroupLayout.builder();
      if (var2) {
         var5.withSampler("Source");
      }

      if (var1.equals("motion_blur")) {
         var5.withSampler("Depth");
      } else {
         var5.withSampler("History").withSampler("Depth");
      }

      if (var3) {
         var5.withUniform("MotionBlur", UniformType.UNIFORM_BUFFER);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(O(var0))
         .withVertexShader(O("post/fullscreen"))
         .withFragmentShader(O("post/" + var1))
         .withBindGroupLayout(var5.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var4)
         .withCull(false)
         .build();
   }

   private static Identifier O(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private sa() {
   }
}

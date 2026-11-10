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

public final class u {
   static final float a = 0.033333335F;
   static final float p = 0.1F;
   static final float v = 0.03F;
   static final float r = 60.0F;
   private static final RenderPipeline M = T("motion_blur", "motion_blur", true, true, ColorTargetState.DEFAULT);
   private static final RenderPipeline q = T(
      "motion_accumulate", "motion_accumulate", true, true, new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15)
   );
   private static final RenderPipeline O = T("motion_present", "motion_present", false, false, ColorTargetState.DEFAULT);
   private static final bk l = new bk();
   private static final bk j = new bk(GpuFormat.RGBA16_FLOAT);
   private static GpuBuffer y;
   private static int K = -1;
   private static int U;
   private static int Q;
   private static int Z;
   private static boolean F;
   private static boolean Y;
   private static boolean d;
   private static long J;
   private static float X;
   private static float u;
   private static float W;
   private static float b;

   public static void C() {
      Minecraft var0 = Minecraft.getInstance();
      MotionBlurModule var1 = MotionBlurModule.l();
      if (var0.level != null && var1 != null && var1.isRuntimeActive()) {
         if (K != nl.q) {
            e();
            F = (boolean)0;
            K = nl.q;
         }

         if (!F) {
            Camera var2 = var0.gameRenderer.mainCamera();
            long var3 = System.nanoTime();
            float var5 = d ? (float)(var3 - J) / 1.0E9F : 0.0F;
            float var6 = var2.yRot();
            float var7 = var2.xRot();
            int var8 = System.identityHashCode(var0.level) * 31 + System.identityHashCode(var2.entity());
            float var9 = Mth.wrapDegrees(var6 - X);
            float var10 = var7 - u;
            if (d && var8 == Z && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               W = s(W, var9 / var5, var5);
               b = s(b, var10 / var5, var5);
            } else {
               b = 0.0F;
               W = 0.0F;
               Y = (boolean)0;
            }

            d = (boolean)1;
            Z = var8;
            J = var3;
            X = var6;
            u = var7;
            float var11 = var1.T.getValue().floatValue();
            boolean var12 = var1.W.getValue().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               l.close();
               Y = (boolean)(Y & (j.B != null ? 1 : 0));
            } else {
               j.close();
               Y = (boolean)0;
               if (Math.max(Math.abs(W), Math.abs(b)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = he.s();
            String var16 = "resources";

            try {
               if (y == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{M, q, O}) {
                     se.j(var20);
                  }

                  y = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  _q.z("MotionBlur", "Motion blur shaders loaded");
               }

               RenderTarget var49 = var0.gameRenderer.mainRenderTarget();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians(var2.getFov()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  W(var6, var7, var6 - W * var13, var7 - b * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * var49.width / Math.max(1, var49.height))
                     .putFloat(var51)
                     .putFloat(var1.V.getValue().floatValue() / 100.0F)
                     .putFloat(W(var1.u.getValue()));
                  var21.putFloat(1.0F / var49.width).putFloat(1.0F / var49.height).putFloat(Y ? V(var5, var11) : 0.0F).putFloat(Y ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(y.slice(), var21);
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
                  if (var49.width != U || var49.height != Q) {
                     Y = (boolean)0;
                  }

                  j.l(var49.width, var49.height, 1, bk.p.U);
                  U = var49.width;
                  Q = var49.height;
                  RenderPass var22 = var50.createRenderPass(() -> "Larp MotionBlur accumulate", j.w.getColorTextureView(), Optional.empty());

                  try {
                     p7.K(var22, q);
                     var22.setUniform("MotionBlur", y.slice());
                     p7.t(var22, "Source", var49.getColorTextureView(), var54);
                     p7.t(var22, "History", j.B.getColorTextureView(), var54);
                     p7.t(var22, "Depth", var49.getDepthTextureView(), var54);
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

                  TextureTarget var55 = j.w;
                  j.w = j.B;
                  j.B = var55;
                  Y = (boolean)1;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49.getColorTextureView(), Optional.empty());

                  try {
                     p7.K(var23, O);
                     p7.t(var23, "History", var55.getColorTextureView(), var54);
                     p7.t(var23, "Depth", var49.getDepthTextureView(), var54);
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
                  l.l(var49.width, var49.height, 1, bk.p.m);
                  nl.G(se.j, var49, l.B);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49.getColorTextureView(), Optional.empty());

                  try {
                     p7.K(var56, M);
                     var56.setUniform("MotionBlur", y.slice());
                     p7.t(var56, "Source", l.B.getColorTextureView(), var53);
                     p7.t(var56, "Depth", var49.getDepthTextureView(), var54);
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
               F = (boolean)1;

               try {
                  e();
               } catch (RuntimeException var38) {
                  var47.addSuppressed(var38);
               }

               _q.o(
                  "MotionBlur",
                  "MotionBlurRenderer stage=" + var16 + " mode=" + var1.W.getValue() + " framebuffer=scene; disabled until resource reload",
                  var47
               );
            } finally {
               he.T("MotionBlur", var14);
            }
         }
      } else {
         P();
      }
   }

   static float s(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 / 0.03F));
   }

   static float V(float var0, float var1) {
      return (float)Math.exp(-var0 / (var1 * 0.1F));
   }

   static int W(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f W(float var0, float var1, float var2, float var3) {
      return w(var2, var3).mul(w(var0, var1).transpose());
   }

   private static Matrix4f w(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians(var1)).rotateY((float)Math.toRadians(var0) + (float) Math.PI);
   }

   private static void P() {
      Y = (boolean)0;
      d = (boolean)0;
      b = 0.0F;
      W = 0.0F;
   }

   public static void e() {
      P();

      try {
         l.close();
         j.close();
      } finally {
         if (y != null) {
            y.close();
            y = null;
         }
      }
   }

   private static RenderPipeline T(String var0, String var1, boolean var2, boolean var3, ColorTargetState var4) {
      Builder var5 = BindGroupLayout.builder();
      if (var2) {
         var5.withUniform("Source", p7.X);
      }

      if (var1.equals("motion_blur")) {
         var5.withUniform("Depth", p7.X);
      } else {
         var5.withUniform("History", p7.X).withUniform("Depth", p7.X);
      }

      if (var3) {
         var5.withUniform("MotionBlur", UniformType.UNIFORM_BUFFER);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(Z(var0))
         .withVertexShader(Z("post/fullscreen"))
         .withFragmentShader(Z("post/" + var1))
         .withBindGroupLayout(var5.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var4)
         .withCull(false)
         .build();
   }

   private static Identifier Z(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private u() {
   }
}

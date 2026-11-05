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

public final class mc {
   static final float P = 0.033333335F;
   static final float v = 0.1F;
   static final float I = 0.03F;
   static final float E = 60.0F;
   private static final RenderPipeline g = U("motion_blur", "motion_blur", true, true, ColorTargetState.DEFAULT);
   private static final RenderPipeline R = U(
      "motion_accumulate", "motion_accumulate", true, true, new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15)
   );
   private static final RenderPipeline t = U("motion_present", "motion_present", false, false, ColorTargetState.DEFAULT);
   private static final ld U = new ld();
   private static final ld Z = new ld(GpuFormat.RGBA16_FLOAT);
   private static GpuBuffer u;
   private static int N = -1;
   private static int s;
   private static int a;
   private static int A;
   private static boolean w;
   private static boolean y;
   private static boolean m;
   private static long q;
   private static float S;
   private static float V;
   private static float H;
   private static float J;

   public static void S() {
      Minecraft var0 = Minecraft.getInstance();
      MotionBlurModule var1 = MotionBlurModule.S();
      if (var0.level != null && var1 != null && var1.isRuntimeActive()) {
         if (N != jk.v) {
            J();
            w = (boolean)0;
            N = jk.v;
         }

         if (!w) {
            Camera var2 = var0.gameRenderer.mainCamera();
            long var3 = System.nanoTime();
            float var5 = m ? (float)(var3 - q) / 1.0E9F : 0.0F;
            float var6 = var2.yRot();
            float var7 = var2.xRot();
            int var8 = System.identityHashCode(var0.level) * 31 + System.identityHashCode(var2.entity());
            float var9 = Mth.wrapDegrees(var6 - S);
            float var10 = var7 - V;
            if (m && var8 == A && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               H = g(H, var9 / var5, var5);
               J = g(J, var10 / var5, var5);
            } else {
               J = 0.0F;
               H = 0.0F;
               y = (boolean)0;
            }

            m = (boolean)1;
            A = var8;
            q = var3;
            S = var6;
            V = var7;
            float var11 = var1.J.getValue().floatValue();
            boolean var12 = var1.M.getValue().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               U.close();
               y = (boolean)(y & (Z.U != null ? 1 : 0));
            } else {
               Z.close();
               y = (boolean)0;
               if (Math.max(Math.abs(H), Math.abs(J)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = h_.U();
            String var16 = "resources";

            try {
               if (u == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{g, R, t}) {
                     e4.U(var20);
                  }

                  u = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  r4.V("MotionBlur", "Motion blur shaders loaded");
               }

               RenderTarget var49 = var0.gameRenderer.mainRenderTarget();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians(var2.getFov()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  V(var6, var7, var6 - H * var13, var7 - J * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * var49.width / Math.max(1, var49.height))
                     .putFloat(var51)
                     .putFloat(var1.U.getValue().floatValue() / 100.0F)
                     .putFloat(T(var1.s.getValue()));
                  var21.putFloat(1.0F / var49.width).putFloat(1.0F / var49.height).putFloat(y ? u(var5, var11) : 0.0F).putFloat(y ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(u.slice(), var21);
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
                  if (var49.width != s || var49.height != a) {
                     y = (boolean)0;
                  }

                  Z.m(var49.width, var49.height, 1, ld.y.H);
                  s = var49.width;
                  a = var49.height;
                  RenderPass var22 = var50.createRenderPass(() -> "Larp MotionBlur accumulate", Z.H.getColorTextureView(), Optional.empty());

                  try {
                     of.z(var22, R);
                     var22.setUniform("MotionBlur", u.slice());
                     of.h(var22, "Source", var49.getColorTextureView(), var54);
                     of.h(var22, "History", Z.U.getColorTextureView(), var54);
                     of.h(var22, "Depth", var49.getDepthTextureView(), var54);
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

                  TextureTarget var55 = Z.H;
                  Z.H = Z.U;
                  Z.U = var55;
                  y = (boolean)1;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49.getColorTextureView(), Optional.empty());

                  try {
                     of.z(var23, t);
                     of.h(var23, "History", var55.getColorTextureView(), var54);
                     of.h(var23, "Depth", var49.getDepthTextureView(), var54);
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
                  U.m(var49.width, var49.height, 1, ld.y.s);
                  jk.y(e4.g, var49, U.U);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49.getColorTextureView(), Optional.empty());

                  try {
                     of.z(var56, g);
                     var56.setUniform("MotionBlur", u.slice());
                     of.h(var56, "Source", U.U.getColorTextureView(), var53);
                     of.h(var56, "Depth", var49.getDepthTextureView(), var54);
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
               w = (boolean)1;

               try {
                  J();
               } catch (RuntimeException var38) {
                  var47.addSuppressed(var38);
               }

               r4.r(
                  "MotionBlur",
                  "MotionBlurRenderer stage=" + var16 + " mode=" + var1.M.getValue() + " framebuffer=scene; disabled until resource reload",
                  var47
               );
            } finally {
               h_.n("MotionBlur", var14);
            }
         }
      } else {
         o();
      }
   }

   static float g(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 / 0.03F));
   }

   static float u(float var0, float var1) {
      return (float)Math.exp(-var0 / (var1 * 0.1F));
   }

   static int T(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f V(float var0, float var1, float var2, float var3) {
      return u(var2, var3).mul(u(var0, var1).transpose());
   }

   private static Matrix4f u(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians(var1)).rotateY((float)Math.toRadians(var0) + (float) Math.PI);
   }

   private static void o() {
      y = (boolean)0;
      m = (boolean)0;
      J = 0.0F;
      H = 0.0F;
   }

   public static void J() {
      o();

      try {
         U.close();
         Z.close();
      } finally {
         if (u != null) {
            u.close();
            u = null;
         }
      }
   }

   private static RenderPipeline U(String var0, String var1, boolean var2, boolean var3, ColorTargetState var4) {
      Builder var5 = BindGroupLayout.builder();
      if (var2) {
         var5.withUniform("Source", of.t);
      }

      if (var1.equals("motion_blur")) {
         var5.withUniform("Depth", of.t);
      } else {
         var5.withUniform("History", of.t).withUniform("Depth", of.t);
      }

      if (var3) {
         var5.withUniform("MotionBlur", UniformType.UNIFORM_BUFFER);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(q(var0))
         .withVertexShader(q("post/fullscreen"))
         .withFragmentShader(q("post/" + var1))
         .withBindGroupLayout(var5.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var4)
         .withCull(false)
         .build();
   }

   private static Identifier q(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private mc() {
   }
}

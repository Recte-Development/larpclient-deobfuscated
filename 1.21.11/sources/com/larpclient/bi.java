package com.larpclient;

import com.larpclient.module.MotionBlurModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_6367;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class bi {
   static final float T = 0.033333335F;
   static final float p = 0.1F;
   static final float k = 0.03F;
   static final float I = 60.0F;
   private static final RenderPipeline Q = M("motion_blur", "motion_blur", true, true, false);
   private static final RenderPipeline e = M("motion_accumulate", "motion_accumulate", true, true, true);
   private static final RenderPipeline h = M("motion_present", "motion_present", false, false, false);
   private static final w7 O = new w7();
   private static final w7 F = new w7(w7.c.d);
   private static GpuBuffer c;
   private static int n = -1;
   private static int Y;
   private static int D;
   private static int A;
   private static boolean W;
   private static boolean P;
   private static boolean B;
   private static long m;
   private static float w;
   private static float b;
   private static float E;
   private static float z;

   public static void e() {
      class_310 var0 = class_310.method_1551();
      MotionBlurModule var1 = MotionBlurModule.L();
      if (var0.field_1687 != null && var1 != null && var1.isRuntimeActive()) {
         if (n != t_.g) {
            M();
            W = (boolean)0;
            n = t_.g;
         }

         if (!W) {
            class_4184 var2 = var0.field_1773.method_19418();
            long var3 = System.nanoTime();
            float var5 = B ? (float)(var3 - m) / 1.0E9F : 0.0F;
            float var6 = var2.method_19330();
            float var7 = var2.method_19329();
            int var8 = System.identityHashCode(var0.field_1687) * 31 + System.identityHashCode(var2.method_19331());
            float var9 = class_3532.method_15393(var6 - w);
            float var10 = var7 - b;
            if (B && var8 == A && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               E = y(E, var9 / var5, var5);
               z = y(z, var10 / var5, var5);
            } else {
               z = 0.0F;
               E = 0.0F;
               P = (boolean)0;
            }

            B = (boolean)1;
            A = var8;
            m = var3;
            w = var6;
            b = var7;
            float var11 = var1.h.getValue().floatValue();
            boolean var12 = var1.s.getValue().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               O.close();
               P = (boolean)(P & (F.F != null ? 1 : 0));
            } else {
               F.close();
               P = (boolean)0;
               if (Math.max(Math.abs(E), Math.abs(z)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = c5.X();
            String var16 = "resources";

            try {
               if (c == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{Q, e, h}) {
                     f2.a(var20);
                  }

                  c = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  q8.G("MotionBlur", "Motion blur shaders loaded");
               }

               class_276 var49 = var0.method_1522();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians(b9.U()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  l(var6, var7, var6 - E * var13, var7 - z * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * var49.field_1482 / Math.max(1, var49.field_1481))
                     .putFloat(var51)
                     .putFloat(var1.N.getValue().floatValue() / 100.0F)
                     .putFloat(n(var1.p.getValue()));
                  var21.putFloat(1.0F / var49.field_1482).putFloat(1.0F / var49.field_1481).putFloat(P ? r(var5, var11) : 0.0F).putFloat(P ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(c.slice(), var21);
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

               class_12137 var53 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
               class_12137 var54 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
               if (var12) {
                  var16 = "accumulate";
                  if (var49.field_1482 != Y || var49.field_1481 != D) {
                     P = (boolean)0;
                  }

                  F.f(var49.field_1482, var49.field_1481, 1, w7.p.z);
                  Y = var49.field_1482;
                  D = var49.field_1481;
                  RenderPass var22 = var50.createRenderPass(() -> "Larp MotionBlur accumulate", F.d.method_71639(), OptionalInt.empty());

                  try {
                     yf.l(var22, e);
                     var22.setUniform("MotionBlur", c.slice());
                     yf.R(var22, "Source", var49.method_71639(), var54);
                     yf.R(var22, "History", F.F.method_71639(), var54);
                     yf.R(var22, "Depth", var49.method_71640(), var54);
                     var22.draw(0, 3);
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

                  class_6367 var55 = F.d;
                  F.d = F.F;
                  F.F = var55;
                  P = (boolean)1;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49.method_71639(), OptionalInt.empty());

                  try {
                     yf.l(var23, h);
                     yf.R(var23, "History", var55.method_71639(), var54);
                     yf.R(var23, "Depth", var49.method_71640(), var54);
                     var23.draw(0, 3);
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
                  O.f(var49.field_1482, var49.field_1481, 1, w7.p.x);
                  t_.L(f2.f, var49, O.F);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49.method_71639(), OptionalInt.empty());

                  try {
                     yf.l(var56, Q);
                     var56.setUniform("MotionBlur", c.slice());
                     yf.R(var56, "Source", O.F.method_71639(), var53);
                     yf.R(var56, "Depth", var49.method_71640(), var54);
                     var56.draw(0, 3);
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
               W = (boolean)1;

               try {
                  M();
               } catch (RuntimeException var38) {
                  var47.addSuppressed(var38);
               }

               q8.u(
                  "MotionBlur",
                  "MotionBlurRenderer stage=" + var16 + " mode=" + var1.s.getValue() + " framebuffer=scene; disabled until resource reload",
                  var47
               );
            } finally {
               c5.w("MotionBlur", var14);
            }
         }
      } else {
         v();
      }
   }

   static float y(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 / 0.03F));
   }

   static float r(float var0, float var1) {
      return (float)Math.exp(-var0 / (var1 * 0.1F));
   }

   static int n(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f l(float var0, float var1, float var2, float var3) {
      return w(var2, var3).mul(w(var0, var1).transpose());
   }

   private static Matrix4f w(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians(var1)).rotateY((float)Math.toRadians(var0) + (float) Math.PI);
   }

   private static void v() {
      P = (boolean)0;
      B = (boolean)0;
      z = 0.0F;
      E = 0.0F;
   }

   public static void M() {
      v();

      try {
         O.close();
         F.close();
      } finally {
         if (c != null) {
            c.close();
            c = null;
         }
      }
   }

   private static RenderPipeline M(String var0, String var1, boolean var2, boolean var3, boolean var4) {
      Builder var5 = RenderPipeline.builder(new Snippet[0])
         .withLocation(Q(var0))
         .withVertexShader(Q("post/fullscreen"))
         .withFragmentShader(Q("post/" + var1))
         .withVertexFormat(class_290.field_60033, class_5596.field_27379)
         .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
         .withDepthWrite(false)
         .withCull(false);
      if (var2) {
         var5.withSampler("Source");
      }

      if (!var1.equals("motion_blur")) {
         var5.withSampler("History");
      }

      var5.withSampler("Depth");
      if (var3) {
         var5.withUniform("MotionBlur", class_10789.field_60031);
      }

      return var5.build();
   }

   private static class_2960 Q(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private bi() {
   }
}

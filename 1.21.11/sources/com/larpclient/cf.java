package com.larpclient;

import com.larpclient.module.WorldColorModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
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
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.lwjgl.system.MemoryStack;

public final class cf {
   private static final RenderPipeline i = RenderPipeline.builder(new Snippet[0])
      .withLocation(x("world_color"))
      .withVertexShader(x("post/fullscreen"))
      .withFragmentShader(x("post/world_color"))
      .withSampler("Source")
      .withUniform("Grade", class_10789.field_60031)
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final w7 f = new w7();
   private static GpuBuffer M;
   private static double r = -100.0;
   private static int L;
   private static int t = -1;
   private static boolean I;

   public static void I(int var0) {
      r = qo.x();
      L = var0;
   }

   public static void A() {
      class_310 var0 = class_310.method_1551();
      WorldColorModule var1 = WorldColorModule.n();
      if (var0.field_1687 != null) {
         double var2 = Math.max(0.0, 1.0 - (qo.x() - r) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (t != t_.g) {
               P();
               I = (boolean)0;
               t = t_.g;
            }

            if (!I) {
               long var5 = c5.X();

               try {
                  if (M == null) {
                     f2.a(i);
                     M = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     q8.G("Shader", "WorldColor shader loaded");
                  }

                  class_276 var7 = var0.method_1522();
                  f.f(var7.field_1482, var7.field_1481, 1, w7.p.x);
                  t_.L(f2.f, var7, f.F);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.t.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.y.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.S.getValue().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.w.getValue().floatValue() : 1.0F);
                     K(var10, var4 != 0 ? var1.g.getValue() : -1, var4 != 0 ? var1.j.getValue().floatValue() : 0.0F);
                     K(var10, var4 != 0 ? var1.T.getValue() : -1, 0.0F);
                     K(var10, var4 != 0 ? var1.Y.getValue() : -1, 0.0F);
                     K(var10, L, (float)var2);
                     var10.putFloat(1.0F / var7.field_1482).putFloat(1.0F / var7.field_1481).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(M.slice(), var10);
                  } catch (Throwable var22) {
                     if (var9 != null) {
                        try {
                           var9.close();
                        } catch (Throwable var20) {
                           var22.addSuppressed(var20);
                        }
                     }

                     throw var22;
                  }

                  if (var9 != null) {
                     var9.close();
                  }

                  RenderPass var26 = var8.createRenderPass(() -> "Larp WorldColor grade + impact", var7.method_71639(), OptionalInt.empty());

                  try {
                     yf.l(var26, i);
                     var26.setUniform("Grade", M.slice());
                     yf.R(var26, "Source", f.F.method_71639(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR));
                     var26.draw(0, 3);
                  } catch (Throwable var21) {
                     if (var26 != null) {
                        try {
                           var26.close();
                        } catch (Throwable var19) {
                           var21.addSuppressed(var19);
                        }
                     }

                     throw var21;
                  }

                  if (var26 != null) {
                     var26.close();
                  }
               } catch (RuntimeException var23) {
                  I = (boolean)1;
                  P();
                  q8.u("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var23);
               } finally {
                  c5.w("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void K(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void P() {
      f.close();
      if (M != null) {
         M.close();
         M = null;
      }
   }

   public static void z() {
      r = -100.0;
      P();
   }

   private static class_2960 x(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }
}

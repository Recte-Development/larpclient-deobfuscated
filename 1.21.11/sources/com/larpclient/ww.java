package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.OptionalInt;
import net.minecraft.class_10017;
import net.minecraft.class_10789;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import org.lwjgl.system.MemoryStack;

public final class ww {
   private static final w7 h = new w7();
   private static final RenderPipeline I = RenderPipeline.builder(new Snippet[0])
      .withLocation(q("entity_halo"))
      .withVertexShader(q("post/fullscreen"))
      .withFragmentShader(q("post/entity_halo"))
      .withSampler("Source")
      .withSampler("Mask")
      .withUniform("Halo", class_10789.field_60031)
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withBlend(BlendFunction.TRANSLUCENT)
      .withCull(false)
      .build();
   private static cw m;
   private static GpuBuffer W;
   private static int y = -1;
   private static boolean C;

   public static void I(class_4587 var0, class_10017 var1) {
      SelfVisualsModule var2 = SelfVisualsModule.o();
      int var3 = var2 != null && var2.Q() && var2.T() && var2.a.getValue() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (y != t_.g) {
            E();
            C = (boolean)0;
            y = t_.g;
         }

         if (!C) {
            GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;

            try {
               if (m == null) {
                  m = new cw();
               }

               if (W == null) {
                  f2.a(I);
                  W = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  q8.G("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               class_310 var6 = class_310.method_1551();
               class_276 var7 = var6.method_1522();
               h.f(var7.field_1482, var7.field_1481, 2, var2.F.getValue() ? w7.p.G : w7.p.u);
               if (var3 != 0) {
                  long var8 = c5.X();
                  B();
                  gv.S(var7.field_1482, var7.field_1481);

                  try {
                     gv.x(var1, var0, m, gv.y.s, -1, 1.0, true);
                  } finally {
                     int[] var10 = gv.E();
                  }

                  m.P();
                  N(var4, var5);
                  c5.w("SelfVisuals outline mask", var8);
                  var8 = c5.X();
                  Object var23;
                  if (var23 == null || ((Object[])var23)[2] > 0) {
                     Q(var2.i.getValue(), var2.x.getValue().floatValue(), var2.Y.getValue().floatValue(), var2.F.getValue() ? 0.2F : 0.0F, (int[])var23);
                  }

                  c5.w("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var20) {
               C = (boolean)1;
               q8.u("SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var20);
               E();
            } finally {
               N(var4, var5);
               if (m != null) {
                  m.W();
               }
            }
         }
      }
   }

   private static void N(GpuTextureView var0, GpuTextureView var1) {
      RenderSystem.outputColorTextureOverride = var0;
      RenderSystem.outputDepthTextureOverride = var1;
   }

   private static void B() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(h.V.method_30277(), 0);
      h.V.method_29329(class_310.method_1551().method_1522());
      RenderSystem.outputColorTextureOverride = h.V.method_71639();
      RenderSystem.outputDepthTextureOverride = h.V.method_71640();
   }

   static int u(float var0, boolean var1) {
      return (int)Math.ceil(var0) * 2 + (var1 ? 12 : 0) + 2;
   }

   private static void Q(int var0, float var1, float var2, float var3, int[] var4) {
      if (var3 > 0.0F) {
         t_.L(f2.y, h.V, h.F);
         t_.L(f2.D, h.F, h.d);
      }

      class_276 var5 = class_310.method_1551().method_1522();
      CommandEncoder var6 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var7 = MemoryStack.stackPush();

      try {
         ByteBuffer var8 = var7.malloc(32);
         var8.putFloat((var0 >> 16 & 0xFF) / 255.0F).putFloat((var0 >> 8 & 0xFF) / 255.0F).putFloat((var0 & 0xFF) / 255.0F).putFloat(var2);
         var8.putFloat(var1).putFloat(var3).putFloat(1.0F / var5.field_1482).putFloat(1.0F / var5.field_1481);
         var8.flip();
         var6.writeToBuffer(W.slice(), var8);
      } catch (Throwable var15) {
         if (var7 != null) {
            try {
               var7.close();
            } catch (Throwable var14) {
               var15.addSuppressed(var14);
            }
         }

         throw var15;
      }

      if (var7 != null) {
         var7.close();
      }

      RenderPass var17 = var6.createRenderPass(() -> "Larp entity outline and halo", wi.p(var5).method_71639(), OptionalInt.empty());

      try {
         yf.l(var17, I);
         if (var4 != null) {
            int var18 = u(var1, var3 > 0.0F);
            int var9 = Math.clamp(var4[0] - var18, 0, var5.field_1482);
            int var10 = Math.clamp(var4[1] - var18, 0, var5.field_1481);
            int var11 = Math.clamp(var4[0] + var4[2] + var18, var9, var5.field_1482);
            int var12 = Math.clamp(var4[1] + var4[3] + var18, var10, var5.field_1481);
            var17.enableScissor(var9, var5.field_1481 - var12, var11 - var9, var12 - var10);
         }

         var17.setUniform("Halo", W.slice());
         class_12137 var19 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
         yf.R(var17, "Source", (var3 > 0.0F ? h.d : h.V).method_71639(), var19);
         yf.R(var17, "Mask", h.V.method_71639(), var19);
         var17.draw(0, 3);
      } catch (Throwable var16) {
         if (var17 != null) {
            try {
               var17.close();
            } catch (Throwable var13) {
               var16.addSuppressed(var13);
            }
         }

         throw var16;
      }

      if (var17 != null) {
         var17.close();
      }
   }

   public static void E() {
      h.close();
      if (W != null) {
         W.close();
         W = null;
      }

      if (m != null) {
         m.close();
         m = null;
      }
   }

   private static class_2960 q(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }
}

package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
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
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import net.minecraft.class_10789;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.lwjgl.system.MemoryStack;

public final class gr {
   private static final w7 D = new w7(w7.c.d);
   private static final Set<String> A = new HashSet<>();
   private static final RenderPipeline M = RenderPipeline.builder(new Snippet[0])
      .withLocation(o("localized_refraction"))
      .withVertexShader(o("post/fullscreen"))
      .withFragmentShader(o("post/refraction"))
      .withSampler("Source")
      .withSampler("Mask")
      .withSampler("SceneDepth")
      .withUniform("Refraction", class_10789.field_60031)
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static GpuBuffer y;
   private static int S = -1;
   private static boolean Y;
   private static boolean J;
   private static int C;

   public static void v() {
      if (S != t_.g) {
         S = t_.g;
         A.clear();
         J = (boolean)0;

         try {
            H();
         } catch (RuntimeException var1) {
            J = (boolean)1;
            q8.u("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      Y = (boolean)0;
      C = 0;
   }

   public static boolean b(String var0, Runnable var1, double var2) {
      if (!J && C < 2 && b0.E() && !(var2 <= 0.0) && !A.contains(var0)) {
         GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
         long var6 = c5.X();

         try {
            GpuTextureView var8 = h7.I();
            GpuTextureView var9 = h7.J();
            if (var8 != null && var9 != null) {
               if (y == null) {
                  f2.a(M);
                  y = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
                  q8.G("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
               }

               class_276 var10 = class_310.method_1551().method_1522();
               D.f(var10.field_1482, var10.field_1481, 1, w7.p.r);
               Y = (boolean)1;
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(D.V.method_30277(), 0);
               RenderSystem.outputColorTextureOverride = D.V.method_71639();
               RenderSystem.outputDepthTextureOverride = var9;
               var1.run();
               RenderSystem.outputColorTextureOverride = var4;
               RenderSystem.outputDepthTextureOverride = var5;
               MemoryStack var12 = MemoryStack.stackPush();

               try {
                  ByteBuffer var13 = var12.malloc(16);
                  var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.field_1482)
                     .putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.field_1481)
                     .putFloat(0.0F)
                     .putFloat(0.0F)
                     .flip();
                  var11.writeToBuffer(y.slice(), var13);
               } catch (Throwable var26) {
                  if (var12 != null) {
                     try {
                        var12.close();
                     } catch (Throwable var24) {
                        var26.addSuppressed(var24);
                     }
                  }

                  throw var26;
               }

               if (var12 != null) {
                  var12.close();
               }

               class_276 var29 = wi.p(var10);
               RenderPass var30 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var29.method_71639(), OptionalInt.empty());

               try {
                  yf.l(var30, M);
                  var30.setUniform("Refraction", y.slice());
                  class_12137 var14 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
                  yf.R(var30, "Source", var8, var14);
                  yf.R(var30, "Mask", D.V.method_71639(), var14);
                  yf.R(var30, "SceneDepth", var9, RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST));
                  var30.draw(0, 3);
               } catch (Throwable var25) {
                  if (var30 != null) {
                     try {
                        var30.close();
                     } catch (Throwable var23) {
                        var25.addSuppressed(var23);
                     }
                  }

                  throw var25;
               }

               if (var30 != null) {
                  var30.close();
               }

               C++;
               h7.G();
               c5.V("Localized refraction composites", 1L);
               return true;
            } else {
               return false;
            }
         } catch (RuntimeException var27) {
            A.add(var0);
            q8.u("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            RenderSystem.outputColorTextureOverride = var4;
            RenderSystem.outputDepthTextureOverride = var5;
            c5.w(var0 + " refraction CPU", var6);
         }
      } else {
         return false;
      }
   }

   public static void z() {
      if (!Y) {
         H();
      }

      c5.L("Refraction mask bytes (estimate)", D.G());
      c5.L("Refraction quarantined owners", A.size());
   }

   public static void H() {
      try {
         D.close();
      } finally {
         GpuBuffer var2 = y;
         y = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static class_2960 o(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private gr() {
   }
}

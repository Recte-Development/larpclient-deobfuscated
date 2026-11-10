package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_11658;
import net.minecraft.class_12075;
import net.minecraft.class_12137;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_6367;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public final class eu {
   private static final int X = 512;
   private static final int G = 8240;
   private static final RenderPipeline l = RenderPipeline.builder(new Snippet[0])
      .withLocation(l("pipeline/temple/droplet_surface"))
      .withVertexShader(l("temple/droplet_surface"))
      .withFragmentShader(l("temple/droplet_surface"))
      .withUniform("LarpMaterial", class_10789.field_60031)
      .withUniform("DropletBatch", class_10789.field_60031)
      .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
      .withDepthWrite(true)
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withCull(false)
      .build();
   private static final RenderPipeline h = RenderPipeline.builder(new Snippet[0])
      .withLocation(l("pipeline/temple/droplet_composite"))
      .withVertexShader(l("post/fullscreen"))
      .withFragmentShader(l("temple/droplet_composite"))
      .withUniform("LarpMaterial", class_10789.field_60031)
      .withSampler("SceneColor")
      .withSampler("SceneDepth")
      .withSampler("DropletMask")
      .withSampler("DropletDepth")
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static class_6367 u;
   private static GpuBuffer a;

   public static void t() {
      if (ex.s() != 0 && qs.S()) {
         class_11658 var0 = qs.y();
         if (var0 != null) {
            class_276 var1 = class_310.method_1551().method_1522();
            if (u == null || u.field_1482 != var1.field_1482 || u.field_1481 != var1.field_1481) {
               n();
               u = new class_6367("Larp volumetric water surface", var1.field_1482, var1.field_1481, true);
            }

            if (a == null) {
               f2.a(l);
               f2.a(h);
               a = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               q8.G("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            qs.F();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(u.method_30277(), 0, u.method_30278(), 1.0);
            var3.copyTextureToTexture(var1.method_30278(), u.method_30278(), 0, 0, 0, 0, 0, var1.field_1482, var1.field_1481);
            class_12075 var4 = var0.field_63082;
            Matrix4f var5 = new Matrix4f(b9.T()).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = qo.C();

            for (short var9 = 0; var9 < ex.s(); var9 += 512) {
               int var10 = Math.min(512, ex.s() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(ex.A(var14, var8) - var4.field_63078.field_1352))
                        .putFloat((float)(ex.F(var14, var8) - var4.field_63078.field_1351))
                        .putFloat((float)(ex.n(var14, var8) - var4.field_63078.field_1350))
                        .putFloat((float)ex.c());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat(var1.field_1482).putFloat(var1.field_1481).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(a.slice(), var12);
               } catch (Throwable var20) {
                  if (var11 != null) {
                     try {
                        var11.close();
                     } catch (Throwable var17) {
                        var20.addSuppressed(var17);
                     }
                  }

                  throw var20;
               }

               if (var11 != null) {
                  var11.close();
               }

               RenderPass var23 = var3.createRenderPass(
                  () -> "Larp volumetric surface", u.method_71639(), OptionalInt.empty(), u.method_71640(), OptionalDouble.empty()
               );

               try {
                  yf.l(var23, l);
                  var23.setUniform("LarpMaterial", qs.T().slice());
                  var23.setUniform("DropletBatch", a.slice());
                  var23.draw(0, 6 * var10);
               } catch (Throwable var19) {
                  if (var23 != null) {
                     try {
                        var23.close();
                     } catch (Throwable var16) {
                        var19.addSuppressed(var16);
                     }
                  }

                  throw var19;
               }

               if (var23 != null) {
                  var23.close();
               }
            }

            RenderPass var21 = var3.createRenderPass(() -> "Larp volumetric water composite", var1.method_71639(), OptionalInt.empty());

            try {
               yf.l(var21, h);
               var21.setUniform("LarpMaterial", qs.T().slice());
               class_12137 var22 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
               class_12137 var24 = RenderSystem.getSamplerCache().method_75294(FilterMode.NEAREST);
               yf.R(var21, "SceneColor", qs.Q(), var22);
               yf.R(var21, "SceneDepth", qs.h(), var24);
               yf.R(var21, "DropletMask", u.method_71639(), var24);
               yf.R(var21, "DropletDepth", u.method_71640(), var24);
               var21.draw(0, 3);
            } catch (Throwable var18) {
               if (var21 != null) {
                  try {
                     var21.close();
                  } catch (Throwable var15) {
                     var18.addSuppressed(var15);
                  }
               }

               throw var18;
            }

            if (var21 != null) {
               var21.close();
            }
         }
      }
   }

   public static void n() {
      if (u != null) {
         u.method_1238();
         u = null;
      }

      if (a != null) {
         a.close();
         a = null;
      }
   }

   private static class_2960 l(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   private eu() {
   }
}

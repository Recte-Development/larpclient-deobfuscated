package com.larpclient;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class l3 {
   private static final bt R = new bt(GpuFormat.RGBA16_FLOAT);
   private static final Set<String> p = new HashSet<>();
   private static final RenderPipeline F = RenderPipeline.builder(new Snippet[0])
      .withLocation(s("localized_refraction"))
      .withVertexShader(s("post/fullscreen"))
      .withFragmentShader(s("post/refraction"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withSampler("Source")
            .withSampler("Mask")
            .withSampler("SceneDepth")
            .withUniform("Refraction", UniformType.UNIFORM_BUFFER)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static GpuBuffer a;
   private static int Z = -1;
   private static boolean T;
   private static boolean I;
   private static int c;

   public static void E() {
      if (Z != wa.s) {
         Z = wa.s;
         p.clear();
         I = (boolean)0;

         try {
            l();
         } catch (RuntimeException var1) {
            I = (boolean)1;
            h.m("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      T = (boolean)0;
      c = 0;
   }

   public static boolean N(String var0, Runnable var1, double var2) {
      if (!I && c < 2 && y.y() && !(var2 <= 0.0) && !p.contains(var0)) {
         GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
         GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;
         long var6 = zj.m();

         try {
            GpuTextureView var8 = so.K();
            GpuTextureView var9 = so.v();
            if (var8 != null && var9 != null) {
               if (a == null) {
                  ai.U(F);
                  a = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
                  h.A("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
               }

               RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               R.V(var10.width, var10.height, 1, bt.p.V);
               T = (boolean)1;
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(R.I.getColorTexture(), new Vector4f());
               RenderSystem.outputColorTextureOverride = R.I.getColorTextureView();
               RenderSystem.outputDepthTextureOverride = var9;
               var1.run();
               RenderSystem.outputColorTextureOverride = var4;
               RenderSystem.outputDepthTextureOverride = var5;
               MemoryStack var12 = MemoryStack.stackPush();

               try {
                  ByteBuffer var13 = var12.malloc(16);
                  var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.width)
                     .putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.height)
                     .putFloat(0.0F)
                     .putFloat(0.0F)
                     .flip();
                  var11.writeToBuffer(a.slice(), var13);
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

               RenderTarget var29 = p2.Q(var10);
               RenderPass var30 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var29.getColorTextureView(), Optional.empty());

               try {
                  mx.i(var30, F);
                  var30.setUniform("Refraction", a.slice());
                  GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
                  mx.x(var30, "Source", var8, var14);
                  mx.x(var30, "Mask", R.I.getColorTextureView(), var14);
                  mx.x(var30, "SceneDepth", var9, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
                  var30.draw(3, 1, 0, 0);
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

               c++;
               so.K();
               zj.K("Localized refraction composites", 1L);
               return true;
            } else {
               return false;
            }
         } catch (RuntimeException var27) {
            p.add(var0);
            h.m("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            RenderSystem.outputColorTextureOverride = var4;
            RenderSystem.outputDepthTextureOverride = var5;
            zj.l(var0 + " refraction CPU", var6);
         }
      } else {
         return false;
      }
   }

   public static void e() {
      if (!T) {
         l();
      }

      zj.s("Refraction mask bytes (estimate)", R.N());
      zj.s("Refraction quarantined owners", p.size());
   }

   public static void l() {
      try {
         R.close();
      } finally {
         GpuBuffer var2 = a;
         a = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static Identifier s(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private l3() {
   }
}

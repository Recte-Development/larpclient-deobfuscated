package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class m6 {
   private static final ld I = new ld(GpuFormat.RGBA16_FLOAT);
   private static final Set<String> r = new HashSet<>();
   private static final RenderPipeline g = RenderPipeline.builder(new Snippet[0])
      .withLocation(J("localized_refraction"))
      .withVertexShader(J("post/fullscreen"))
      .withFragmentShader(J("post/refraction"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("Source", of.t)
            .withUniform("Mask", of.t)
            .withUniform("SceneDepth", of.t)
            .withUniform("Refraction", UniformType.UNIFORM_BUFFER)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static GpuBuffer B;
   private static int x = -1;
   private static boolean N;
   private static boolean p;
   private static int H;

   public static void w() {
      if (x != jk.v) {
         x = jk.v;
         r.clear();
         p = (boolean)0;

         try {
            D();
         } catch (RuntimeException var1) {
            p = (boolean)1;
            r4.r("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      N = (boolean)0;
      H = 0;
   }

   public static boolean a(String var0, Runnable var1, double var2) {
      if (!p && H < 2 && lo.i() && !(var2 <= 0.0) && !r.contains(var0)) {
         GpuTextureView var4 = _m.M;
         GpuTextureView var5 = _m.U;
         long var6 = h_.U();

         try {
            GpuTextureView var8 = x1.x();
            GpuTextureView var9 = x1.z();
            if (var8 != null && var9 != null) {
               if (B == null) {
                  e4.U(g);
                  B = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
                  r4.V("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
               }

               RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               I.m(var10.width, var10.height, 1, ld.y.R);
               N = (boolean)1;
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(I.B.getColorTexture(), new Vector4f());
               _m.M = I.B.getColorTextureView();
               _m.U = var9;
               var1.run();
               _m.M = var4;
               _m.U = var5;
               MemoryStack var12 = MemoryStack.stackPush();

               try {
                  ByteBuffer var13 = var12.malloc(16);
                  var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.width)
                     .putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.height)
                     .putFloat(0.0F)
                     .putFloat(0.0F)
                     .flip();
                  var11.writeToBuffer(B.slice(), var13);
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

               RenderTarget var29 = y8.m(var10);
               RenderPass var30 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var29.getColorTextureView(), Optional.empty());

               try {
                  of.z(var30, g);
                  var30.setUniform("Refraction", B.slice());
                  GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
                  of.h(var30, "Source", var8, var14);
                  of.h(var30, "Mask", I.B.getColorTextureView(), var14);
                  of.h(var30, "SceneDepth", var9, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

               H++;
               x1.N();
               h_.A("Localized refraction composites", 1L);
               return true;
            } else {
               return false;
            }
         } catch (RuntimeException var27) {
            r.add(var0);
            r4.r("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            _m.M = var4;
            _m.U = var5;
            h_.n(var0 + " refraction CPU", var6);
         }
      } else {
         return false;
      }
   }

   public static void J() {
      if (!N) {
         D();
      }

      h_.S("Refraction mask bytes (estimate)", I.I());
      h_.S("Refraction quarantined owners", r.size());
   }

   public static void D() {
      try {
         I.close();
      } finally {
         GpuBuffer var2 = B;
         B = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static Identifier J(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private m6() {
   }
}

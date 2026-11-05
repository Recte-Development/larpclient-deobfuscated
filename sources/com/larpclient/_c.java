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

public final class _c {
   private static final e4 J = new e4(GpuFormat.RGBA16_FLOAT);
   private static final Set<String> T = new HashSet<>();
   private static final RenderPipeline c = RenderPipeline.builder(new Snippet[0])
      .withLocation(G("localized_refraction"))
      .withVertexShader(G("post/fullscreen"))
      .withFragmentShader(G("post/refraction"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("Source", hj.G)
            .withUniform("Mask", hj.G)
            .withUniform("SceneDepth", hj.G)
            .withUniform("Refraction", UniformType.UNIFORM_BUFFER)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static GpuBuffer A;
   private static int y = -1;
   private static boolean P;
   private static boolean d;
   private static int f;

   public static void U() {
      if (y != m5.k) {
         y = m5.k;
         T.clear();
         d = (boolean)0;

         try {
            o();
         } catch (RuntimeException var1) {
            d = (boolean)1;
            mt.P("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      P = (boolean)0;
      f = 0;
   }

   public static boolean i(String var0, Runnable var1, double var2) {
      if (!d && f < 2 && _k.C() && !(var2 <= 0.0) && !T.contains(var0)) {
         GpuTextureView var4 = xn.j;
         GpuTextureView var5 = xn.o;
         long var6 = kx.G();

         try {
            GpuTextureView var8 = ex.n();
            GpuTextureView var9 = ex.X();
            if (var8 != null && var9 != null) {
               if (A == null) {
                  ea.z(c);
                  A = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
                  mt.K("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
               }

               RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               J.e(var10.width, var10.height, 1, e4.o.C);
               P = (boolean)1;
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(J.M.getColorTexture(), new Vector4f());
               xn.j = J.M.getColorTextureView();
               xn.o = var9;
               var1.run();
               xn.j = var4;
               xn.o = var5;
               MemoryStack var12 = MemoryStack.stackPush();

               try {
                  ByteBuffer var13 = var12.malloc(16);
                  var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.width)
                     .putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.height)
                     .putFloat(0.0F)
                     .putFloat(0.0F)
                     .flip();
                  var11.writeToBuffer(A.slice(), var13);
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

               RenderTarget var29 = zo.t(var10);
               RenderPass var30 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var29.getColorTextureView(), Optional.empty());

               try {
                  hj.P(var30, c);
                  var30.setUniform("Refraction", A.slice());
                  GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
                  hj.u(var30, "Source", var8, var14);
                  hj.u(var30, "Mask", J.M.getColorTextureView(), var14);
                  hj.u(var30, "SceneDepth", var9, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

               f++;
               ex.w();
               kx.U("Localized refraction composites", 1L);
               return true;
            } else {
               return false;
            }
         } catch (RuntimeException var27) {
            T.add(var0);
            mt.P("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            xn.j = var4;
            xn.o = var5;
            kx.u(var0 + " refraction CPU", var6);
         }
      } else {
         return false;
      }
   }

   public static void M() {
      if (!P) {
         o();
      }

      kx.s("Refraction mask bytes (estimate)", J.E());
      kx.s("Refraction quarantined owners", T.size());
   }

   public static void o() {
      try {
         J.close();
      } finally {
         GpuBuffer var2 = A;
         A = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static Identifier G(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private _c() {
   }
}

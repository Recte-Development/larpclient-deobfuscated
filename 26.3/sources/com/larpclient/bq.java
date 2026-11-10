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

public final class bq {
   private static final bk k = new bk(GpuFormat.RGBA16_FLOAT);
   private static final Set<String> J = new HashSet<>();
   private static final RenderPipeline y = RenderPipeline.builder(new Snippet[0])
      .withLocation(J("localized_refraction"))
      .withVertexShader(J("post/fullscreen"))
      .withFragmentShader(J("post/refraction"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("Source", p7.X)
            .withUniform("Mask", p7.X)
            .withUniform("SceneDepth", p7.X)
            .withUniform("Refraction", UniformType.UNIFORM_BUFFER)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static GpuBuffer d;
   private static int X = -1;
   private static boolean t;
   private static boolean T;
   private static int u;

   public static void K() {
      if (X != nl.q) {
         X = nl.q;
         J.clear();
         T = (boolean)0;

         try {
            r();
         } catch (RuntimeException var1) {
            T = (boolean)1;
            _q.o("Refraction", "Generation cleanup failed; base materials retained until resource reload", var1);
         }
      }

      t = (boolean)0;
      u = 0;
   }

   public static boolean W(String var0, Runnable var1, double var2) {
      if (!T && u < 2 && a4.K() && !(var2 <= 0.0) && !J.contains(var0)) {
         GpuTextureView var4 = jf.b;
         GpuTextureView var5 = jf.q;
         long var6 = he.s();

         try {
            GpuTextureView var8 = _s.N();
            GpuTextureView var9 = _s.a();
            if (var8 != null && var9 != null) {
               if (d == null) {
                  se.j(y);
                  d = RenderSystem.getDevice().createBuffer(() -> "Larp refraction parameters", 136, 16L);
                  _q.z("Refraction", "Localized depth-aware pipeline ready; shared scene samples and mask");
               }

               RenderTarget var10 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
               k.l(var10.width, var10.height, 1, bk.p.y);
               t = (boolean)1;
               CommandEncoder var11 = RenderSystem.getDevice().createCommandEncoder();
               var11.clearColorTexture(k.Y.getColorTexture(), new Vector4f());
               jf.b = k.Y.getColorTextureView();
               jf.q = var9;
               var1.run();
               jf.b = var4;
               jf.q = var5;
               MemoryStack var12 = MemoryStack.stackPush();

               try {
                  ByteBuffer var13 = var12.malloc(16);
                  var13.putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.width)
                     .putFloat((float)Math.clamp(var2, 0.0, 24.0) / var10.height)
                     .putFloat(0.0F)
                     .putFloat(0.0F)
                     .flip();
                  var11.writeToBuffer(d.slice(), var13);
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

               RenderTarget var29 = qc.Y(var10);
               RenderPass var30 = var11.createRenderPass(() -> "Larp localized refraction " + var0, var29.getColorTextureView(), Optional.empty());

               try {
                  p7.K(var30, y);
                  var30.setUniform("Refraction", d.slice());
                  GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
                  p7.t(var30, "Source", var8, var14);
                  p7.t(var30, "Mask", k.Y.getColorTextureView(), var14);
                  p7.t(var30, "SceneDepth", var9, RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST));
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

               u++;
               _s.g();
               he.Z("Localized refraction composites", 1L);
               return true;
            } else {
               return false;
            }
         } catch (RuntimeException var27) {
            J.add(var0);
            _q.o("Refraction", "Owner=" + var0 + "; mask/composite failed, base material retained until resource reload", var27);
            return false;
         } finally {
            jf.b = var4;
            jf.q = var5;
            he.T(var0 + " refraction CPU", var6);
         }
      } else {
         return false;
      }
   }

   public static void P() {
      if (!t) {
         r();
      }

      he.z("Refraction mask bytes (estimate)", k.Z());
      he.z("Refraction quarantined owners", J.size());
   }

   public static void r() {
      try {
         k.close();
      } finally {
         GpuBuffer var2 = d;
         d = null;
         if (var2 != null) {
            var2.close();
         }
      }
   }

   private static Identifier J(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private bq() {
   }
}

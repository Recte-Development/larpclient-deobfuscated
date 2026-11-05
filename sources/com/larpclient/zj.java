package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class zj {
   private static final e4 j = new e4();
   private static final RenderPipeline D = RenderPipeline.builder(new Snippet[0])
      .withLocation(j("entity_halo"))
      .withVertexShader(j("post/fullscreen"))
      .withFragmentShader(j("post/entity_halo"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("Source", hj.G).withUniform("Mask", hj.G).withUniform("Halo", UniformType.UNIFORM_BUFFER).build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
      .withCull(false)
      .build();
   private static xh N;
   private static GpuBuffer p;
   private static int G = -1;
   private static boolean e;

   public static void G(PoseStack var0, EntityRenderState var1) {
      SelfVisualsModule var2 = SelfVisualsModule.V();
      int var3 = var2 != null && var2.M() && var2.T() && var2.y.P() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (G != m5.k) {
            g();
            e = (boolean)0;
            G = m5.k;
         }

         if (!e) {
            GpuTextureView var4 = xn.j;
            GpuTextureView var5 = xn.o;

            try {
               if (N == null) {
                  N = new xh();
               }

               if (p == null) {
                  ea.z(D);
                  p = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  mt.K("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               Minecraft var6 = Minecraft.getInstance();
               RenderTarget var7 = var6.gameRenderer.mainRenderTarget();
               j.e(var7.width, var7.height, 2, var2.b.P() ? e4.o.G : e4.o.f);
               if (var3 != 0) {
                  long var8 = kx.G();
                  X();
                  tj.u(var1, var0, N, tj.b.V, -1, 1.0, true);
                  N.b();
                  B(var4, var5);
                  kx.u("SelfVisuals outline mask", var8);
                  var8 = kx.G();
                  h(var2.Y.P(), var2.p.P().floatValue(), var2.B.P().floatValue(), var2.b.P() ? 0.2F : 0.0F);
                  kx.u("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var13) {
               e = (boolean)1;
               mt.P("SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var13);
               g();
            } finally {
               B(var4, var5);
               if (N != null) {
                  N.s();
               }
            }
         }
      }
   }

   private static void B(GpuTextureView var0, GpuTextureView var1) {
      xn.j = var0;
      xn.o = var1;
   }

   private static void X() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(j.M.getColorTexture(), new Vector4f());
      j.M.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      xn.j = j.M.getColorTextureView();
      xn.o = j.M.getDepthTextureView();
   }

   private static void h(int var0, float var1, float var2, float var3) {
      if (var3 > 0.0F) {
         m5.l(ea.X, j.M, j.b);
         m5.l(ea.y, j.b, j.v);
      }

      RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var6 = MemoryStack.stackPush();

      try {
         ByteBuffer var7 = var6.malloc(32);
         var7.putFloat((var0 >> 16 & 0xFF) / 255.0F).putFloat((var0 >> 8 & 0xFF) / 255.0F).putFloat((var0 & 0xFF) / 255.0F).putFloat(var2);
         var7.putFloat(var1).putFloat(var3).putFloat(1.0F / var4.width).putFloat(1.0F / var4.height);
         var7.flip();
         var5.writeToBuffer(p.slice(), var7);
      } catch (Throwable var12) {
         if (var6 != null) {
            try {
               var6.close();
            } catch (Throwable var10) {
               var12.addSuppressed(var10);
            }
         }

         throw var12;
      }

      if (var6 != null) {
         var6.close();
      }

      RenderPass var13 = var5.createRenderPass(() -> "Larp entity outline and halo", zo.t(var4).getColorTextureView(), Optional.empty());

      try {
         hj.P(var13, D);
         var13.setUniform("Halo", p.slice());
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         hj.u(var13, "Source", (var3 > 0.0F ? j.v : j.M).getColorTextureView(), var14);
         hj.u(var13, "Mask", j.M.getColorTextureView(), var14);
         var13.draw(3, 1, 0, 0);
      } catch (Throwable var11) {
         if (var13 != null) {
            try {
               var13.close();
            } catch (Throwable var9) {
               var11.addSuppressed(var9);
            }
         }

         throw var11;
      }

      if (var13 != null) {
         var13.close();
      }
   }

   public static void g() {
      j.close();
      if (p != null) {
         p.close();
         p = null;
      }

      if (N != null) {
         N.close();
         N = null;
      }
   }

   private static Identifier j(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}

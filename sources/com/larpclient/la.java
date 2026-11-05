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

public final class la {
   private static final ld S = new ld();
   private static final RenderPipeline l = RenderPipeline.builder(new Snippet[0])
      .withLocation(X("entity_halo"))
      .withVertexShader(X("post/fullscreen"))
      .withFragmentShader(X("post/entity_halo"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("Source", of.t).withUniform("Mask", of.t).withUniform("Halo", UniformType.UNIFORM_BUFFER).build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
      .withCull(false)
      .build();
   private static y2 H;
   private static GpuBuffer n;
   private static int V = -1;
   private static boolean q;

   public static void x(PoseStack var0, EntityRenderState var1) {
      SelfVisualsModule var2 = SelfVisualsModule.f();
      int var3 = var2 != null && var2.b() && var2.Z() && var2.h.getValue() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (V != jk.v) {
            J();
            q = (boolean)0;
            V = jk.v;
         }

         if (!q) {
            GpuTextureView var4 = _m.M;
            GpuTextureView var5 = _m.U;

            try {
               if (H == null) {
                  H = new y2();
               }

               if (n == null) {
                  e4.U(l);
                  n = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  r4.V("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               Minecraft var6 = Minecraft.getInstance();
               RenderTarget var7 = var6.gameRenderer.mainRenderTarget();
               S.m(var7.width, var7.height, 2, var2.B.getValue() ? ld.y.c : ld.y.O);
               if (var3 != 0) {
                  long var8 = h_.U();
                  l();
                  uv.k(var1, var0, H, uv.w.c, -1, 1.0, true);
                  H.v();
                  F(var4, var5);
                  h_.n("SelfVisuals outline mask", var8);
                  var8 = h_.U();
                  J(var2.Y.getValue(), var2.m.getValue().floatValue(), var2.e.getValue().floatValue(), var2.B.getValue() ? 0.2F : 0.0F);
                  h_.n("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var13) {
               q = (boolean)1;
               r4.r("SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var13);
               J();
            } finally {
               F(var4, var5);
               if (H != null) {
                  H.R();
               }
            }
         }
      }
   }

   private static void F(GpuTextureView var0, GpuTextureView var1) {
      _m.M = var0;
      _m.U = var1;
   }

   private static void l() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(S.B.getColorTexture(), new Vector4f());
      S.B.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      _m.M = S.B.getColorTextureView();
      _m.U = S.B.getDepthTextureView();
   }

   private static void J(int var0, float var1, float var2, float var3) {
      if (var3 > 0.0F) {
         jk.y(e4.P, S.B, S.U);
         jk.y(e4.M, S.U, S.H);
      }

      RenderTarget var4 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      CommandEncoder var5 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var6 = MemoryStack.stackPush();

      try {
         ByteBuffer var7 = var6.malloc(32);
         var7.putFloat((var0 >> 16 & 0xFF) / 255.0F).putFloat((var0 >> 8 & 0xFF) / 255.0F).putFloat((var0 & 0xFF) / 255.0F).putFloat(var2);
         var7.putFloat(var1).putFloat(var3).putFloat(1.0F / var4.width).putFloat(1.0F / var4.height);
         var7.flip();
         var5.writeToBuffer(n.slice(), var7);
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

      RenderPass var13 = var5.createRenderPass(() -> "Larp entity outline and halo", y8.m(var4).getColorTextureView(), Optional.empty());

      try {
         of.z(var13, l);
         var13.setUniform("Halo", n.slice());
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         of.h(var13, "Source", (var3 > 0.0F ? S.H : S.B).getColorTextureView(), var14);
         of.h(var13, "Mask", S.B.getColorTextureView(), var14);
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

   public static void J() {
      S.close();
      if (n != null) {
         n.close();
         n = null;
      }

      if (H != null) {
         H.close();
         H = null;
      }
   }

   private static Identifier X(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}

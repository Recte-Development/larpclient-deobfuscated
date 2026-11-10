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

public final class h9 {
   private static final bk n = new bk();
   private static final RenderPipeline I = RenderPipeline.builder(new Snippet[0])
      .withLocation(Y("entity_halo"))
      .withVertexShader(Y("post/fullscreen"))
      .withFragmentShader(Y("post/entity_halo"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("Source", p7.X).withUniform("Mask", p7.X).withUniform("Halo", UniformType.UNIFORM_BUFFER).build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
      .withCull(false)
      .build();
   private static wh G;
   private static GpuBuffer f;
   private static int g = -1;
   private static boolean w;

   public static void W(PoseStack var0, EntityRenderState var1) {
      SelfVisualsModule var2 = SelfVisualsModule.v();
      int var3 = var2 != null && var2.g() && var2.k() && var2.a.getValue() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (g != nl.q) {
            L();
            w = (boolean)0;
            g = nl.q;
         }

         if (!w) {
            GpuTextureView var4 = jf.b;
            GpuTextureView var5 = jf.q;

            try {
               if (G == null) {
                  G = new wh();
               }

               if (f == null) {
                  se.j(I);
                  f = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  _q.z("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               Minecraft var6 = Minecraft.getInstance();
               RenderTarget var7 = var6.gameRenderer.mainRenderTarget();
               n.l(var7.width, var7.height, 2, var2.c.getValue() ? bk.p.v : bk.p.R);
               if (var3 != 0) {
                  long var8 = he.s();
                  J();
                  w7.q(var7.width, var7.height);

                  try {
                     w7.Q(var1, var0, G, w7.b.j, -1, 1.0, true);
                  } finally {
                     int[] var10 = w7.L();
                  }

                  G.S();
                  y(var4, var5);
                  he.T("SelfVisuals outline mask", var8);
                  var8 = he.s();
                  Object var23;
                  if (var23 == null || ((Object[])var23)[2] > 0) {
                     f(var2.e.getValue(), var2.x.getValue().floatValue(), var2.E.getValue().floatValue(), var2.c.getValue() ? 0.2F : 0.0F, (int[])var23);
                  }

                  he.T("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var20) {
               w = (boolean)1;
               _q.o("SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var20);
               L();
            } finally {
               y(var4, var5);
               if (G != null) {
                  G.A();
               }
            }
         }
      }
   }

   private static void y(GpuTextureView var0, GpuTextureView var1) {
      jf.b = var0;
      jf.q = var1;
   }

   private static void J() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(n.Y.getColorTexture(), new Vector4f());
      n.Y.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      jf.b = n.Y.getColorTextureView();
      jf.q = n.Y.getDepthTextureView();
   }

   static int p(float var0, boolean var1) {
      return (int)Math.ceil(var0) * 2 + (var1 ? 12 : 0) + 2;
   }

   private static void f(int var0, float var1, float var2, float var3, int[] var4) {
      if (var3 > 0.0F) {
         nl.G(se.i, n.Y, n.B);
         nl.G(se.p, n.B, n.w);
      }

      RenderTarget var5 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      CommandEncoder var6 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var7 = MemoryStack.stackPush();

      try {
         ByteBuffer var8 = var7.malloc(32);
         var8.putFloat((var0 >> 16 & 0xFF) / 255.0F).putFloat((var0 >> 8 & 0xFF) / 255.0F).putFloat((var0 & 0xFF) / 255.0F).putFloat(var2);
         var8.putFloat(var1).putFloat(var3).putFloat(1.0F / var5.width).putFloat(1.0F / var5.height);
         var8.flip();
         var6.writeToBuffer(f.slice(), var8);
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

      RenderPass var17 = var6.createRenderPass(() -> "Larp entity outline and halo", qc.Y(var5).getColorTextureView(), Optional.empty());

      try {
         p7.K(var17, I);
         if (var4 != null) {
            int var18 = p(var1, var3 > 0.0F);
            int var9 = Math.clamp(var4[0] - var18, 0, var5.width);
            int var10 = Math.clamp(var4[1] - var18, 0, var5.height);
            int var11 = Math.clamp(var4[0] + var4[2] + var18, var9, var5.width);
            int var12 = Math.clamp(var4[1] + var4[3] + var18, var10, var5.height);
            var17.enableScissor(var9, var5.height - var12, var11 - var9, var12 - var10);
         }

         var17.setUniform("Halo", f.slice());
         GpuSampler var19 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         p7.t(var17, "Source", (var3 > 0.0F ? n.w : n.Y).getColorTextureView(), var19);
         p7.t(var17, "Mask", n.Y.getColorTextureView(), var19);
         var17.draw(3, 1, 0, 0);
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

   public static void L() {
      n.close();
      if (f != null) {
         f.close();
         f = null;
      }

      if (G != null) {
         G.close();
         G = null;
      }
   }

   private static Identifier Y(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}

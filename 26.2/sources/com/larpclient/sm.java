package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.BindGroupLayout;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
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
import com.mojang.blaze3d.vertex.PoseStack;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class sm {
   private static final bt e = new bt();
   private static final RenderPipeline T = RenderPipeline.builder(new Snippet[0])
      .withLocation(d("entity_halo"))
      .withVertexShader(d("post/fullscreen"))
      .withFragmentShader(d("post/entity_halo"))
      .withBindGroupLayout(BindGroupLayout.builder().withSampler("Source").withSampler("Mask").withUniform("Halo", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
      .withCull(false)
      .build();
   private static hh p;
   private static GpuBuffer y;
   private static int I = -1;
   private static boolean g;

   public static void M(PoseStack var0, EntityRenderState var1) {
      SelfVisualsModule var2 = SelfVisualsModule.k();
      int var3 = var2 != null && var2.S() && var2.Z() && var2.V.getValue() && var1 != null ? 1 : 0;
      if (var3 != 0) {
         if (I != wa.s) {
            q();
            g = (boolean)0;
            I = wa.s;
         }

         if (!g) {
            GpuTextureView var4 = RenderSystem.outputColorTextureOverride;
            GpuTextureView var5 = RenderSystem.outputDepthTextureOverride;

            try {
               if (p == null) {
                  p = new hh();
               }

               if (y == null) {
                  ai.U(T);
                  y = RenderSystem.getDevice().createBuffer(() -> "Larp entity halo parameters", 136, 32L);
                  h.A("SelfVisuals", "Outline pass initialized; full-resolution silhouette edges");
               }

               Minecraft var6 = Minecraft.getInstance();
               RenderTarget var7 = var6.gameRenderer.mainRenderTarget();
               e.V(var7.width, var7.height, 2, var2.e.getValue() ? bt.p.F : bt.p.a);
               if (var3 != 0) {
                  long var8 = zj.m();
                  Z();
                  j4.a(var7.width, var7.height);

                  try {
                     j4.I(var1, var0, p, j4.j.j, -1, 1.0, true);
                  } finally {
                     int[] var10 = j4.m();
                  }

                  p.K();
                  D(var4, var5);
                  zj.l("SelfVisuals outline mask", var8);
                  var8 = zj.m();
                  Object var23;
                  if (var23 == null || ((Object[])var23)[2] > 0) {
                     A(var2.W.getValue(), var2.u.getValue().floatValue(), var2.z.getValue().floatValue(), var2.e.getValue() ? 0.2F : 0.0F, (int[])var23);
                  }

                  zj.l("SelfVisuals outline composite", var8);
               }
            } catch (RuntimeException var20) {
               g = (boolean)1;
               h.m("SelfVisuals", "EntityMaskRenderer stage=mask/composite framebuffer=entity mask shader=post/entity_halo; disabled until reload", var20);
               q();
            } finally {
               D(var4, var5);
               if (p != null) {
                  p.g();
               }
            }
         }
      }
   }

   private static void D(GpuTextureView var0, GpuTextureView var1) {
      RenderSystem.outputColorTextureOverride = var0;
      RenderSystem.outputDepthTextureOverride = var1;
   }

   private static void Z() {
      CommandEncoder var0 = RenderSystem.getDevice().createCommandEncoder();
      var0.clearColorTexture(e.I.getColorTexture(), new Vector4f());
      e.I.copyDepthFrom(Minecraft.getInstance().gameRenderer.mainRenderTarget());
      RenderSystem.outputColorTextureOverride = e.I.getColorTextureView();
      RenderSystem.outputDepthTextureOverride = e.I.getDepthTextureView();
   }

   static int u(float var0, boolean var1) {
      return (int)Math.ceil(var0) * 2 + (var1 ? 12 : 0) + 2;
   }

   private static void A(int var0, float var1, float var2, float var3, int[] var4) {
      if (var3 > 0.0F) {
         wa.Y(ai.j, e.I, e.M);
         wa.Y(ai.F, e.M, e.D);
      }

      RenderTarget var5 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
      CommandEncoder var6 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var7 = MemoryStack.stackPush();

      try {
         ByteBuffer var8 = var7.malloc(32);
         var8.putFloat((var0 >> 16 & 0xFF) / 255.0F).putFloat((var0 >> 8 & 0xFF) / 255.0F).putFloat((var0 & 0xFF) / 255.0F).putFloat(var2);
         var8.putFloat(var1).putFloat(var3).putFloat(1.0F / var5.width).putFloat(1.0F / var5.height);
         var8.flip();
         var6.writeToBuffer(y.slice(), var8);
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

      RenderPass var17 = var6.createRenderPass(() -> "Larp entity outline and halo", p2.Q(var5).getColorTextureView(), Optional.empty());

      try {
         mx.i(var17, T);
         if (var4 != null) {
            int var18 = u(var1, var3 > 0.0F);
            int var9 = Math.clamp(var4[0] - var18, 0, var5.width);
            int var10 = Math.clamp(var4[1] - var18, 0, var5.height);
            int var11 = Math.clamp(var4[0] + var4[2] + var18, var9, var5.width);
            int var12 = Math.clamp(var4[1] + var4[3] + var18, var10, var5.height);
            var17.enableScissor(var9, var5.height - var12, var11 - var9, var12 - var10);
         }

         var17.setUniform("Halo", y.slice());
         GpuSampler var19 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         mx.x(var17, "Source", (var3 > 0.0F ? e.D : e.I).getColorTextureView(), var19);
         mx.x(var17, "Mask", e.I.getColorTextureView(), var19);
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

   public static void q() {
      e.close();
      if (y != null) {
         y.close();
         y = null;
      }

      if (p != null) {
         p.close();
         p = null;
      }
   }

   private static Identifier d(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}

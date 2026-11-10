package com.larpclient;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.device.GpuDevice;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.OptionalDouble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class v_ {
   private static final int C = 512;
   private static final int I = 8240;
   private static final RenderPipeline Y = RenderPipeline.builder(new Snippet[0])
      .withLocation(h("pipeline/temple/droplet_surface"))
      .withVertexShader(h("temple/droplet_surface"))
      .withFragmentShader(h("temple/droplet_surface"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).withUniform("DropletBatch", UniformType.UNIFORM_BUFFER).build()
      )
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final RenderPipeline d = RenderPipeline.builder(new Snippet[0])
      .withLocation(h("pipeline/temple/droplet_composite"))
      .withVertexShader(h("post/fullscreen"))
      .withFragmentShader(h("temple/droplet_composite"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
            .withUniform("SceneColor", p7.X)
            .withUniform("SceneDepth", p7.X)
            .withUniform("DropletMask", p7.X)
            .withUniform("DropletDepth", p7.X)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static TextureTarget t;
   private static GpuBuffer F;

   public static void Z() {
      if (v0.a() != 0 && at.Y()) {
         LevelRenderState var0 = at.J();
         if (var0 != null) {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (t == null || t.width != var1.width || t.height != var1.height) {
               U();
               t = p7.m("Larp volumetric water surface", var1.width, var1.height, true, GpuFormat.RGBA8_UNORM);
            }

            if (F == null) {
               se.j(Y);
               se.j(d);
               F = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               _q.z("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            at.M();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(t.getColorTexture(), new Vector4f(), t.getDepthTexture(), 0.0);
            var3.copyTextureToTexture(var1.getDepthTexture(), t.getDepthTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
            CameraRenderState var4 = var0.cameraRenderState;
            Matrix4f var5 = new Matrix4f(var4.viewRotationMatrix).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = hz.C();

            for (short var9 = 0; var9 < v0.a(); var9 += 512) {
               int var10 = Math.min(512, v0.a() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(v0.K(var14, var8) - var4.pos.x))
                        .putFloat((float)(v0.H(var14, var8) - var4.pos.y))
                        .putFloat((float)(v0.c(var14, var8) - var4.pos.z))
                        .putFloat((float)v0.e());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat(var1.width).putFloat(var1.height).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(F.slice(), var12);
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
                  () -> "Larp volumetric surface", t.getColorTextureView(), Optional.empty(), t.getDepthTextureView(), OptionalDouble.empty()
               );

               try {
                  p7.K(var23, Y);
                  var23.setUniform("LarpMaterial", at.T().slice());
                  var23.setUniform("DropletBatch", F.slice());
                  var23.draw(6, var10, 0, 0);
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

            RenderPass var21 = var3.createRenderPass(() -> "Larp volumetric water composite", var1.getColorTextureView(), Optional.empty());

            try {
               p7.K(var21, d);
               var21.setUniform("LarpMaterial", at.T().slice());
               GpuSampler var22 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var24 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               p7.t(var21, "SceneColor", at.H(), var22);
               p7.t(var21, "SceneDepth", at.A(), var24);
               p7.t(var21, "DropletMask", t.getColorTextureView(), var24);
               p7.t(var21, "DropletDepth", t.getDepthTextureView(), var24);
               var21.draw(3, 1, 0, 0);
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

   public static void U() {
      if (t != null) {
         t.destroyBuffers();
         t = null;
      }

      if (F != null) {
         F.close();
         F = null;
      }
   }

   private static Identifier h(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private v_() {
   }
}

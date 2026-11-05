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

public final class y {
   private static final int b = 512;
   private static final int O = 8240;
   private static final RenderPipeline W = RenderPipeline.builder(new Snippet[0])
      .withLocation(q("pipeline/temple/droplet_surface"))
      .withVertexShader(q("temple/droplet_surface"))
      .withFragmentShader(q("temple/droplet_surface"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).withUniform("DropletBatch", UniformType.UNIFORM_BUFFER).build()
      )
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final RenderPipeline P = RenderPipeline.builder(new Snippet[0])
      .withLocation(q("pipeline/temple/droplet_composite"))
      .withVertexShader(q("post/fullscreen"))
      .withFragmentShader(q("temple/droplet_composite"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
            .withUniform("SceneColor", of.t)
            .withUniform("SceneDepth", of.t)
            .withUniform("DropletMask", of.t)
            .withUniform("DropletDepth", of.t)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static TextureTarget K;
   private static GpuBuffer E;

   public static void m() {
      if (df.u() != 0 && y3.n()) {
         LevelRenderState var0 = y3.b();
         if (var0 != null) {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (K == null || K.width != var1.width || K.height != var1.height) {
               Y();
               K = of.T("Larp volumetric water surface", var1.width, var1.height, true, GpuFormat.RGBA8_UNORM);
            }

            if (E == null) {
               e4.U(W);
               e4.U(P);
               E = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               r4.V("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            y3.I();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(K.getColorTexture(), new Vector4f(), K.getDepthTexture(), 0.0);
            var3.copyTextureToTexture(var1.getDepthTexture(), K.getDepthTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
            CameraRenderState var4 = var0.cameraRenderState;
            Matrix4f var5 = new Matrix4f(var4.viewRotationMatrix).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = ea.Z();

            for (short var9 = 0; var9 < df.u(); var9 += 512) {
               int var10 = Math.min(512, df.u() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(df.L(var14, var8) - var4.pos.x))
                        .putFloat((float)(df.o(var14, var8) - var4.pos.y))
                        .putFloat((float)(df.I(var14, var8) - var4.pos.z))
                        .putFloat((float)df.V());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat(var1.width).putFloat(var1.height).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(E.slice(), var12);
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
                  () -> "Larp volumetric surface", K.getColorTextureView(), Optional.empty(), K.getDepthTextureView(), OptionalDouble.empty()
               );

               try {
                  of.z(var23, W);
                  var23.setUniform("LarpMaterial", y3.x().slice());
                  var23.setUniform("DropletBatch", E.slice());
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
               of.z(var21, P);
               var21.setUniform("LarpMaterial", y3.x().slice());
               GpuSampler var22 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var24 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               of.h(var21, "SceneColor", y3.Z(), var22);
               of.h(var21, "SceneDepth", y3.u(), var24);
               of.h(var21, "DropletMask", K.getColorTextureView(), var24);
               of.h(var21, "DropletDepth", K.getDepthTextureView(), var24);
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

   public static void Y() {
      if (K != null) {
         K.destroyBuffers();
         K = null;
      }

      if (E != null) {
         E.close();
         E = null;
      }
   }

   private static Identifier q(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private y() {
   }
}

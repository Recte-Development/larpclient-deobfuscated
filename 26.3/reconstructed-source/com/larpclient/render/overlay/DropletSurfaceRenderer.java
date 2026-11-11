package com.larpclient.render.overlay;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.world.RealisticWater;
import com.larpclient.render.world.TerrainMaterials;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
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

public final class DropletSurfaceRenderer {
   private static final int maxDropletsPerDraw = 512;
   private static final int batchBufferSize = 8240;
   private static final RenderPipeline surfacePipeline = RenderPipeline.builder(new Snippet[0])
      .withLocation(larpId("pipeline/temple/droplet_surface"))
      .withVertexShader(larpId("temple/droplet_surface"))
      .withFragmentShader(larpId("temple/droplet_surface"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER).withUniform("DropletBatch", UniformType.UNIFORM_BUFFER).build()
      )
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final RenderPipeline compositePipeline = RenderPipeline.builder(new Snippet[0])
      .withLocation(larpId("pipeline/temple/droplet_composite"))
      .withVertexShader(larpId("post/fullscreen"))
      .withFragmentShader(larpId("temple/droplet_composite"))
      .withBindGroupLayout(
         BindGroupLayout.builder()
            .withUniform("LarpMaterial", UniformType.UNIFORM_BUFFER)
            .withUniform("SceneColor", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("SceneDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("DropletMask", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .withUniform("DropletDepth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE)
            .build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static TextureTarget maskTarget;
   private static GpuBuffer batchBuffer;

   public static void render() {
      if (RealisticWater.getDropletCount() != 0 && TerrainMaterials.isMaterialPassActive()) {
         LevelRenderState var0 = TerrainMaterials.getLevelState();
         if (var0 != null) {
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            if (maskTarget == null || maskTarget.width != var1.width || maskTarget.height != var1.height) {
               releaseResources();
               maskTarget = RenderPipelineCompat.createTextureTarget("Larp volumetric water surface", var1.width, var1.height, true, GpuFormat.RGBA8_UNORM);
            }

            if (batchBuffer == null) {
               ClientPipelines.validatePipeline(surfacePipeline);
               ClientPipelines.validatePipeline(compositePipeline);
               batchBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp volumetric droplet batch", 136, 8240L);
               LarpLog.info("TerrainMaterials", "Volumetric surface ready; {} droplets maximum, {} per draw", 4096, 512);
            }

            TerrainMaterials.captureScene();
            GpuDevice var2 = RenderSystem.getDevice();
            CommandEncoder var3 = var2.createCommandEncoder();
            var3.clearColorAndDepthTextures(maskTarget.getColorTexture(), new Vector4f(), maskTarget.getDepthTexture(), 0.0);
            var3.copyTextureToTexture(var1.getDepthTexture(), maskTarget.getDepthTexture(), 0, 0, 0, 0, 0, var1.width, var1.height);
            CameraRenderState var4 = var0.cameraRenderState;
            Matrix4f var5 = new Matrix4f(var4.viewRotationMatrix).invert();
            Vector3f var6 = var5.transformDirection(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var7 = var5.transformDirection(new Vector3f(0.0F, 1.0F, 0.0F));
            float var8 = TickTimer.getPartialTick();

            for (short var9 = 0; var9 < RealisticWater.getDropletCount(); var9 += 512) {
               int var10 = Math.min(512, RealisticWater.getDropletCount() - var9);
               MemoryStack var11 = MemoryStack.stackPush();

               try {
                  ByteBuffer var12 = var11.calloc(8240);

                  for (int var13 = 0; var13 < var10; var13++) {
                     int var14 = var9 + var13;
                     var12.putFloat((float)(RealisticWater.lerpX(var14, var8) - var4.pos.x))
                        .putFloat((float)(RealisticWater.lerpY(var14, var8) - var4.pos.y))
                        .putFloat((float)(RealisticWater.lerpZ(var14, var8) - var4.pos.z))
                        .putFloat((float)RealisticWater.getDropletRadius());
                  }

                  var12.position(8192);
                  var12.putFloat(var6.x).putFloat(var6.y).putFloat(var6.z).putFloat(0.0F);
                  var12.putFloat(var7.x).putFloat(var7.y).putFloat(var7.z).putFloat(0.0F);
                  var12.putFloat(var1.width).putFloat(var1.height).putFloat(0.0F).putFloat(0.0F);
                  var12.flip();
                  var3.writeToBuffer(batchBuffer.slice(), var12);
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
                  () -> "Larp volumetric surface", maskTarget.getColorTextureView(), Optional.empty(), maskTarget.getDepthTextureView(), OptionalDouble.empty()
               );

               try {
                  RenderPipelineCompat.setPipeline(var23, surfacePipeline);
                  var23.setUniform("LarpMaterial", TerrainMaterials.releaseResources().slice());
                  var23.setUniform("DropletBatch", batchBuffer.slice());
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
               RenderPipelineCompat.setPipeline(var21, compositePipeline);
               var21.setUniform("LarpMaterial", TerrainMaterials.releaseResources().slice());
               GpuSampler var22 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var24 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               RenderPipelineCompat.setSamplerUniform(var21, "SceneColor", TerrainMaterials.getSceneColorView(), var22);
               RenderPipelineCompat.setSamplerUniform(var21, "SceneDepth", TerrainMaterials.bindMaterial(), var24);
               RenderPipelineCompat.setSamplerUniform(var21, "DropletMask", maskTarget.getColorTextureView(), var24);
               RenderPipelineCompat.setSamplerUniform(var21, "DropletDepth", maskTarget.getDepthTextureView(), var24);
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

   public static void releaseResources() {
      if (maskTarget != null) {
         maskTarget.destroyBuffers();
         maskTarget = null;
      }

      if (batchBuffer != null) {
         batchBuffer.close();
         batchBuffer = null;
      }
   }

   private static Identifier larpId(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private DropletSurfaceRenderer() {
   }
}

package com.larpclient.render.world;

import com.larpclient.module.render.WorldColorModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.GlowFramebuffers;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.system.MemoryStack;

public final class WorldColorRenderer {
   private static final RenderPipeline pipeline = RenderPipeline.builder(new Snippet[0])
      .withLocation(id("world_color"))
      .withVertexShader(id("post/fullscreen"))
      .withFragmentShader(id("post/world_color"))
      .withBindGroupLayout(
         BindGroupLayout.builder().withUniform("Source", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE).withUniform("Grade", UniformType.UNIFORM_BUFFER).build()
      )
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final GlowFramebuffers sceneCopy = new GlowFramebuffers();
   private static GpuBuffer paramsBuffer;
   private static double impactTime = -100.0;
   private static int impactColor;
   private static int generation = -1;
   private static boolean failed;

   public static void triggerImpact(int var0) {
      impactTime = TickTimer.getTime();
      impactColor = var0;
   }

   public static void render() {
      Minecraft var0 = Minecraft.getInstance();
      WorldColorModule var1 = WorldColorModule.getInstance();
      if (var0.level != null) {
         double var2 = Math.max(0.0, 1.0 - (TickTimer.getTime() - impactTime) / 0.24);
         var2 *= var2;
         int var4 = var1 != null && var1.isRuntimeActive() ? 1 : 0;
         if (var4 != 0 || var2 != 0.0) {
            if (generation != PostProcessing.reloadCount) {
               release();
               failed = (boolean)0;
               generation = PostProcessing.reloadCount;
            }

            if (!failed) {
               long var5 = PerfProfiler.startTimer();

               try {
                  if (paramsBuffer == null) {
                     ClientPipelines.validatePipeline(pipeline);
                     paramsBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp WorldColor parameters", 136, 96L);
                     LarpLog.info("Shader", "WorldColor shader loaded");
                  }

                  RenderTarget var7 = var0.gameRenderer.mainRenderTarget();
                  sceneCopy.resize(var7.width, var7.height, 1, GlowFramebuffers.Mode.PING_ONLY);
                  PostProcessing.blit(ClientPipelines.cOPY, var7, sceneCopy.blurPing);
                  CommandEncoder var8 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var9 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var10 = var9.malloc(96);
                     var10.putFloat(var4 != 0 ? var1.saturation.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.contrast.getValue().floatValue() : 1.0F)
                        .putFloat(var4 != 0 ? var1.exposure.getValue().floatValue() : 0.0F)
                        .putFloat(var4 != 0 ? var1.gamma.getValue().floatValue() : 1.0F);
                     putColor(var10, var4 != 0 ? var1.tint.getValue() : -1, var4 != 0 ? var1.vignette.getValue().floatValue() : 0.0F);
                     putColor(var10, var4 != 0 ? var1.shadowTint.getValue() : -1, 0.0F);
                     putColor(var10, var4 != 0 ? var1.highlightTint.getValue() : -1, 0.0F);
                     putColor(var10, impactColor, (float)var2);
                     var10.putFloat(1.0F / var7.width).putFloat(1.0F / var7.height).putFloat(0.0F).putFloat(0.0F);
                     var10.flip();
                     var8.writeToBuffer(paramsBuffer.slice(), var10);
                  } catch (Throwable var22) {
                     if (var9 != null) {
                        try {
                           var9.close();
                        } catch (Throwable var20) {
                           var22.addSuppressed(var20);
                        }
                     }

                     throw var22;
                  }

                  if (var9 != null) {
                     var9.close();
                  }

                  RenderPass var26 = var8.createRenderPass(() -> "Larp WorldColor grade + impact", var7.getColorTextureView(), Optional.empty());

                  try {
                     RenderPipelineCompat.setPipeline(var26, pipeline);
                     var26.setUniform("Grade", paramsBuffer.slice());
                     RenderPipelineCompat.setSamplerUniform(
                        var26, "Source", sceneCopy.blurPing.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)
                     );
                     var26.draw(3, 1, 0, 0);
                  } catch (Throwable var21) {
                     if (var26 != null) {
                        try {
                           var26.close();
                        } catch (Throwable var19) {
                           var21.addSuppressed(var19);
                        }
                     }

                     throw var21;
                  }

                  if (var26 != null) {
                     var26.close();
                  }
               } catch (RuntimeException var23) {
                  failed = (boolean)1;
                  release();
                  LarpLog.error("WorldColor", "WorldPostRenderer stage=grade framebuffer=scene shader=post/world_color; disabled until reload", var23);
               } finally {
                  PerfProfiler.endTimer("WorldColor", var5);
               }
            }
         }
      }
   }

   private static void putColor(ByteBuffer var0, int var1, float var2) {
      var0.putFloat((var1 >> 16 & 0xFF) / 255.0F).putFloat((var1 >> 8 & 0xFF) / 255.0F).putFloat((var1 & 0xFF) / 255.0F).putFloat(var2);
   }

   public static void release() {
      sceneCopy.close();
      if (paramsBuffer != null) {
         paramsBuffer.close();
         paramsBuffer = null;
      }
   }

   public static void reset() {
      impactTime = -100.0;
      release();
   }

   private static Identifier id(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }
}

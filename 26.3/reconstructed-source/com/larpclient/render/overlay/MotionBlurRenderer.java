package com.larpclient.render.overlay;

import com.larpclient.module.render.MotionBlurModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import com.mojang.renderpearl.api.textures.GpuSampler;
import java.nio.ByteBuffer;
import java.util.Optional;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

public final class MotionBlurRenderer {
   static final float rEFERENCE_FRAME_TIME = 0.033333335F;
   static final float hISTORY_DECAY = 0.1F;
   static final float sMOOTHING_TIME = 0.03F;
   static final float mAX_DELTA_DEGREES = 60.0F;
   private static final RenderPipeline streakPipeline = createPipeline("motion_blur", "motion_blur", true, true, ColorTargetState.DEFAULT);
   private static final RenderPipeline accumulatePipeline = createPipeline(
      "motion_accumulate", "motion_accumulate", true, true, new ColorTargetState(Optional.empty(), GpuFormat.RGBA16_FLOAT, 15)
   );
   private static final RenderPipeline presentPipeline = createPipeline("motion_present", "motion_present", false, false, ColorTargetState.DEFAULT);
   private static final GlowFramebuffers streakTarget = new GlowFramebuffers();
   private static final GlowFramebuffers accumulateTarget = new GlowFramebuffers(GpuFormat.RGBA16_FLOAT);
   private static GpuBuffer paramsBuffer;
   private static int resourceGeneration = -1;
   private static int lastWidth;
   private static int lastHeight;
   private static int lastSceneId;
   private static boolean disabled;
   private static boolean hasHistory;
   private static boolean hasLastCamera;
   private static long lastFrameNanos;
   private static float lastYaw;
   private static float lastPitch;
   private static float yawVelocity;
   private static float pitchVelocity;

   public static void render() {
      Minecraft var0 = Minecraft.getInstance();
      MotionBlurModule var1 = MotionBlurModule.getInstance();
      if (var0.level != null && var1 != null && var1.isRuntimeActive()) {
         if (resourceGeneration != PostProcessing.reloadCount) {
            releaseResources();
            disabled = (boolean)0;
            resourceGeneration = PostProcessing.reloadCount;
         }

         if (!disabled) {
            Camera var2 = var0.gameRenderer.mainCamera();
            long var3 = System.nanoTime();
            float var5 = hasLastCamera ? (float)(var3 - lastFrameNanos) / 1.0E9F : 0.0F;
            float var6 = var2.yRot();
            float var7 = var2.xRot();
            int var8 = System.identityHashCode(var0.level) * 31 + System.identityHashCode(var2.entity());
            float var9 = Mth.wrapDegrees(var6 - lastYaw);
            float var10 = var7 - lastPitch;
            if (hasLastCamera && var8 == lastSceneId && !(var5 <= 0.0F) && !(var5 > 0.25F) && !(Math.abs(var9) > 60.0F) && !(Math.abs(var10) > 60.0F)) {
               yawVelocity = smooth(yawVelocity, var9 / var5, var5);
               pitchVelocity = smooth(pitchVelocity, var10 / var5, var5);
            } else {
               pitchVelocity = 0.0F;
               yawVelocity = 0.0F;
               hasHistory = (boolean)0;
            }

            hasLastCamera = (boolean)1;
            lastSceneId = var8;
            lastFrameNanos = var3;
            lastYaw = var6;
            lastPitch = var7;
            float var11 = var1.strengthSetting.getValue().floatValue();
            boolean var12 = var1.modeSetting.getValue().equals("Classic");
            float var13 = var11 * 0.033333335F;
            if (var12) {
               streakTarget.close();
               hasHistory = (boolean)(hasHistory & (accumulateTarget.blurPing != null ? 1 : 0));
            } else {
               accumulateTarget.close();
               hasHistory = (boolean)0;
               if (Math.max(Math.abs(yawVelocity), Math.abs(pitchVelocity)) * var13 < 0.02F) {
                  return;
               }
            }

            long var14 = PerfProfiler.startTimer();
            String var16 = "resources";

            try {
               if (paramsBuffer == null) {
                  for (RenderPipeline var20 : new RenderPipeline[]{streakPipeline, accumulatePipeline, presentPipeline}) {
                     ClientPipelines.validatePipeline(var20);
                  }

                  paramsBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp MotionBlur parameters", 136, 96L);
                  LarpLog.info("MotionBlur", "Motion blur shaders loaded");
               }

               RenderTarget var49 = var0.gameRenderer.mainRenderTarget();
               CommandEncoder var50 = RenderSystem.getDevice().createCommandEncoder();
               float var51 = (float)Math.tan(Math.toRadians(var2.getFov()) / 2.0);
               MemoryStack var52 = MemoryStack.stackPush();

               try {
                  ByteBuffer var21 = var52.malloc(96);
                  rotationDelta(var6, var7, var6 - yawVelocity * var13, var7 - pitchVelocity * var13).get(var21);
                  var21.position(64);
                  var21.putFloat(var51 * var49.width / Math.max(1, var49.height))
                     .putFloat(var51)
                     .putFloat(var1.maxLengthSetting.getValue().floatValue() / 100.0F)
                     .putFloat(rotationDelta(var1.qualitySetting.getValue()));
                  var21.putFloat(1.0F / var49.width)
                     .putFloat(1.0F / var49.height)
                     .putFloat(hasHistory ? historyBlend(var5, var11) : 0.0F)
                     .putFloat(hasHistory ? 0.0F : 1.0F);
                  var21.flip();
                  var50.writeToBuffer(paramsBuffer.slice(), var21);
               } catch (Throwable var46) {
                  if (var52 != null) {
                     try {
                        var52.close();
                     } catch (Throwable var42) {
                        var46.addSuppressed(var42);
                     }
                  }

                  throw var46;
               }

               if (var52 != null) {
                  var52.close();
               }

               GpuSampler var53 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
               GpuSampler var54 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
               if (var12) {
                  var16 = "accumulate";
                  if (var49.width != lastWidth || var49.height != lastHeight) {
                     hasHistory = (boolean)0;
                  }

                  accumulateTarget.resize(var49.width, var49.height, 1, GlowFramebuffers.Mode.BLUR_ONLY);
                  lastWidth = var49.width;
                  lastHeight = var49.height;
                  RenderPass var22 = var50.createRenderPass(
                     () -> "Larp MotionBlur accumulate", accumulateTarget.blurPong.getColorTextureView(), Optional.empty()
                  );

                  try {
                     RenderPipelineCompat.setPipeline(var22, accumulatePipeline);
                     var22.setUniform("MotionBlur", paramsBuffer.slice());
                     RenderPipelineCompat.setSamplerUniform(var22, "Source", var49.getColorTextureView(), var54);
                     RenderPipelineCompat.setSamplerUniform(var22, "History", accumulateTarget.blurPing.getColorTextureView(), var54);
                     RenderPipelineCompat.setSamplerUniform(var22, "Depth", var49.getDepthTextureView(), var54);
                     var22.draw(3, 1, 0, 0);
                  } catch (Throwable var45) {
                     if (var22 != null) {
                        try {
                           var22.close();
                        } catch (Throwable var41) {
                           var45.addSuppressed(var41);
                        }
                     }

                     throw var45;
                  }

                  if (var22 != null) {
                     var22.close();
                  }

                  TextureTarget var55 = accumulateTarget.blurPong;
                  accumulateTarget.blurPong = accumulateTarget.blurPing;
                  accumulateTarget.blurPing = var55;
                  hasHistory = (boolean)1;
                  var16 = "present";
                  RenderPass var23 = var50.createRenderPass(() -> "Larp MotionBlur present", var49.getColorTextureView(), Optional.empty());

                  try {
                     RenderPipelineCompat.setPipeline(var23, presentPipeline);
                     RenderPipelineCompat.setSamplerUniform(var23, "History", var55.getColorTextureView(), var54);
                     RenderPipelineCompat.setSamplerUniform(var23, "Depth", var49.getDepthTextureView(), var54);
                     var23.draw(3, 1, 0, 0);
                  } catch (Throwable var44) {
                     if (var23 != null) {
                        try {
                           var23.close();
                        } catch (Throwable var40) {
                           var44.addSuppressed(var40);
                        }
                     }

                     throw var44;
                  }

                  if (var23 != null) {
                     var23.close();
                  }
               } else {
                  var16 = "streak";
                  streakTarget.resize(var49.width, var49.height, 1, GlowFramebuffers.Mode.PING_ONLY);
                  PostProcessing.blit(ClientPipelines.cOPY, var49, streakTarget.blurPing);
                  RenderPass var56 = var50.createRenderPass(() -> "Larp MotionBlur streaks", var49.getColorTextureView(), Optional.empty());

                  try {
                     RenderPipelineCompat.setPipeline(var56, streakPipeline);
                     var56.setUniform("MotionBlur", paramsBuffer.slice());
                     RenderPipelineCompat.setSamplerUniform(var56, "Source", streakTarget.blurPing.getColorTextureView(), var53);
                     RenderPipelineCompat.setSamplerUniform(var56, "Depth", var49.getDepthTextureView(), var54);
                     var56.draw(3, 1, 0, 0);
                  } catch (Throwable var43) {
                     if (var56 != null) {
                        try {
                           var56.close();
                        } catch (Throwable var39) {
                           var43.addSuppressed(var39);
                        }
                     }

                     throw var43;
                  }

                  if (var56 != null) {
                     var56.close();
                  }
               }
            } catch (RuntimeException var47) {
               disabled = (boolean)1;

               try {
                  releaseResources();
               } catch (RuntimeException var38) {
                  var47.addSuppressed(var38);
               }

               LarpLog.error(
                  "MotionBlur",
                  "MotionBlurRenderer stage=" + var16 + " mode=" + var1.modeSetting.getValue() + " framebuffer=scene; disabled until resource reload",
                  var47
               );
            } finally {
               PerfProfiler.endTimer("MotionBlur", var14);
            }
         }
      } else {
         resetState();
      }
   }

   static float smooth(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * (1.0F - (float)Math.exp(-var2 / 0.03F));
   }

   static float historyBlend(float var0, float var1) {
      return (float)Math.exp(-var0 / (var1 * 0.1F));
   }

   static int rotationDelta(String var0) {
      return switch (var0) {
         case "Low" -> 8;
         case "High" -> 20;
         default -> 12;
      };
   }

   static Matrix4f rotationDelta(float var0, float var1, float var2, float var3) {
      return cameraRotation(var2, var3).mul(cameraRotation(var0, var1).transpose());
   }

   private static Matrix4f cameraRotation(float var0, float var1) {
      return new Matrix4f().rotateX((float)Math.toRadians(var1)).rotateY((float)Math.toRadians(var0) + (float) Math.PI);
   }

   private static void resetState() {
      hasHistory = (boolean)0;
      hasLastCamera = (boolean)0;
      pitchVelocity = 0.0F;
      yawVelocity = 0.0F;
   }

   public static void releaseResources() {
      resetState();

      try {
         streakTarget.close();
         accumulateTarget.close();
      } finally {
         if (paramsBuffer != null) {
            paramsBuffer.close();
            paramsBuffer = null;
         }
      }
   }

   private static RenderPipeline createPipeline(String var0, String var1, boolean var2, boolean var3, ColorTargetState var4) {
      Builder var5 = BindGroupLayout.builder();
      if (var2) {
         var5.withUniform("Source", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
      }

      if (var1.equals("motion_blur")) {
         var5.withUniform("Depth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
      } else {
         var5.withUniform("History", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE).withUniform("Depth", RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
      }

      if (var3) {
         var5.withUniform("MotionBlur", UniformType.UNIFORM_BUFFER);
      }

      return RenderPipeline.builder(new Snippet[0])
         .withLocation(shaderId(var0))
         .withVertexShader(shaderId("post/fullscreen"))
         .withFragmentShader(shaderId("post/" + var1))
         .withBindGroupLayout(var5.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var4)
         .withCull(false)
         .build();
   }

   private static Identifier shaderId(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private MotionBlurRenderer() {
   }
}

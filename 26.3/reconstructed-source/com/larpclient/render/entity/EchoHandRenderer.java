package com.larpclient.render.entity;

import com.larpclient.core.ViewController;
import com.larpclient.module.misc.EchoHandModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.overlay.OverlayTextures;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
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
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class EchoHandRenderer {
   private static final RenderPipeline echoHistoryPipeline = createPipeline("echo_hand", false);
   private static final RenderPipeline echoCompositePipeline = createPipeline("echo_hand", true);
   private static final RenderPipeline flameHistoryPipeline = createPipeline("hand_flame", false);
   private static final RenderPipeline flameCompositePipeline = createPipeline("hand_flame", true);
   private static TextureTarget beforeTarget;
   private static TextureTarget afterTarget;
   private static TextureTarget historyTarget;
   private static TextureTarget nextTarget;
   private static GpuBuffer materialBuffer;
   private static int resourceGeneration = -1;
   private static boolean disabled;
   private static boolean captured;
   private static boolean paused;
   private static long lastFrameNanos;
   private static Object capturedLevel;
   private static Object capturedPlayer;
   private static String capturedStyle;
   private static double lastX;
   private static double lastY;
   private static double lastZ;
   private static float deltaSeconds = 0.016666668F;
   private static final EchoHandRenderer.HandSway sway = new EchoHandRenderer.HandSway();

   private static RenderPipeline createPipeline(String var0, boolean var1) {
      Builder var2 = BindGroupLayout.builder();

      for (int var3 = 0; var3 < 5; var3++) {
         var2.withUniform("Sampler" + var3, RenderPipelineCompat.sAMPLER_UNIFORM_TYPE);
      }

      var2.withUniform("EchoParameters", UniformType.UNIFORM_BUFFER);
      return RenderPipeline.builder(new Snippet[0])
         .withLocation(identifier("core/" + var0 + (var1 ? "_composite" : "_history")))
         .withVertexShader(identifier("core/echo_hand"))
         .withFragmentShader(identifier("core/" + var0))
         .withBindGroupLayout(var2.build())
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withColorTargetState(var1 ? new ColorTargetState(distanceSquared(var0)) : ColorTargetState.DEFAULT)
         .withCull(false)
         .build();
   }

   private static BlendFunction distanceSquared(String var0) {
      return var0.equals("hand_flame") ? BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA : BlendFunction.LIGHTNING;
   }

   public static boolean isActive() {
      Minecraft var0 = Minecraft.getInstance();
      EchoHandModule var1 = EchoHandModule.getInstance();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator()
         && (var1.isFlame() || var1.echoTrail.getValue())
         && (!var1.isFlame() || !var1.onlyItems.getValue() || !var0.player.getMainHandItem().isEmpty() || !var0.player.getOffhandItem().isEmpty());
   }

   public static void releaseIfInvalid() {
      if (!isActive()) {
         release();
      } else if (capturedLevel != null && (capturedLevel != Minecraft.getInstance().level || capturedPlayer != Minecraft.getInstance().player)) {
         release();
      }
   }

   public static boolean beginCapture() {
      if (resourceGeneration != PostProcessing.reloadCount) {
         release();
         resourceGeneration = PostProcessing.reloadCount;
         disabled = (boolean)0;
      }

      if (!isActive()) {
         release();
         return false;
      }

      if (disabled) {
         return false;
      }

      try {
         Minecraft var0 = Minecraft.getInstance();
         EchoHandModule var1 = EchoHandModule.getInstance();
         RenderTarget var2 = var0.gameRenderer.mainRenderTarget();
         if (OverlayTextures.colorTexture == null && OverlayTextures.depthTexture == null) {
            int var3 = beforeTarget != null
                  && beforeTarget.width == var2.width
                  && beforeTarget.height == var2.height
                  && capturedLevel == var0.level
                  && capturedPlayer == var0.player
                  && var1.mode.getValue().equals(capturedStyle)
                  && !(distanceSquared(var0.player.getX(), var0.player.getY(), var0.player.getZ(), lastX, lastY, lastZ) > 256.0)
               ? 0
               : 1;
            if (var3 != 0) {
               release();
               allocateTargets(var2.width, var2.height);
               capturedLevel = var0.level;
               capturedPlayer = var0.player;
               capturedStyle = var1.mode.getValue();
            }

            lastX = var0.player.getX();
            lastY = var0.player.getY();
            lastZ = var0.player.getZ();
            if (var0.isPaused() != paused) {
               paused = (boolean)(!paused ? 1 : 0);
               LarpLog.debug("EchoHand", "Game {}; history keeps settling", paused ? "paused" : "resumed");
            }

            float var4 = var0.player.getYRot();
            float var5 = var0.player.getXRot();
            float var6 = ViewController.getYaw(var4);
            if (!Float.isNaN(var6)) {
               var4 = var6;
               var5 = ViewController.getPitch();
            }

            sway.update(var4, var5, var1.isFlame() ? 1.0F : var1.lag.getValue().floatValue());
            PostProcessing.blit(ClientPipelines.cOPY, var2, beforeTarget);
            beforeTarget.copyDepthFrom(var2);
            captured = (boolean)1;
            return true;
         } else {
            release();
            return false;
         }
      } catch (RuntimeException var7) {
         disableOnError("capture", var7);
         return false;
      }
   }

   public static void finishFrame() {
      if (captured) {
         captured = (boolean)0;

         try {
            EchoHandModule var0 = EchoHandModule.getInstance();
            RenderTarget var1 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            PostProcessing.blit(ClientPipelines.cOPY, var1, afterTarget);
            afterTarget.copyDepthFrom(var1);
            long var2 = System.nanoTime();
            deltaSeconds = lastFrameNanos == 0L ? 0.016666668F : (float)Math.clamp((var2 - lastFrameNanos) / 1.0E9, 0.0, 0.1);
            lastFrameNanos = var2;
            drawPass(var0.isFlame() ? flameHistoryPipeline : echoHistoryPipeline, nextTarget, var0, 0.0F);
            TextureTarget var4 = historyTarget;
            historyTarget = nextTarget;
            nextTarget = var4;
            drawPass(var0.isFlame() ? flameCompositePipeline : echoCompositePipeline, var1, var0, 1.0F);
         } catch (RuntimeException var5) {
            disableOnError("history/composite", var5);
         }
      }
   }

   public static void reset() {
      captured = (boolean)0;
      release();
   }

   private static void allocateTargets(int var0, int var1) {
      try {
         ClientPipelines.validatePipeline(echoHistoryPipeline);
         ClientPipelines.validatePipeline(echoCompositePipeline);
         ClientPipelines.validatePipeline(flameHistoryPipeline);
         ClientPipelines.validatePipeline(flameCompositePipeline);
         beforeTarget = RenderPipelineCompat.createTextureTarget("Larp EchoHand before", var0, var1, true, GpuFormat.RGBA8_UNORM);
         afterTarget = RenderPipelineCompat.createTextureTarget("Larp EchoHand after", var0, var1, true, GpuFormat.RGBA8_UNORM);
         historyTarget = RenderPipelineCompat.createTextureTarget("Larp EchoHand history", var0, var1, false, GpuFormat.RGBA8_UNORM);
         nextTarget = RenderPipelineCompat.createTextureTarget("Larp EchoHand next", var0, var1, false, GpuFormat.RGBA8_UNORM);
         materialBuffer = RenderSystem.getDevice().createBuffer(() -> "Larp EchoHand material", 136, 96L);
         CommandEncoder var2 = RenderSystem.getDevice().createCommandEncoder();
         var2.clearColorTexture(historyTarget.getColorTexture(), new Vector4f());
         var2.clearColorTexture(nextTarget.getColorTexture(), new Vector4f());
         LarpLog.info("EchoHand", "History initialized {}x{}; four targets, both hands/items", var0, var1);
      } catch (RuntimeException var3) {
         release();
         throw var3;
      }
   }

   private static void drawPass(RenderPipeline var0, RenderTarget var1, EchoHandModule var2, float var3) {
      CommandEncoder var4 = RenderSystem.getDevice().createCommandEncoder();
      MemoryStack var5 = MemoryStack.stackPush();

      try {
         ByteBuffer var6 = var5.malloc(96);
         var6.putFloat(beforeTarget.width)
            .putFloat(beforeTarget.height)
            .putFloat((var2.isFlame() ? var2.flameStrength : var2.strength).getValue().floatValue())
            .putFloat(var2.radius.getValue().floatValue());
         var6.putFloat(var2.trail.getValue().floatValue()).putFloat(var2.lag.getValue().floatValue()).putFloat(var3).putFloat((float)TickTimer.getTime());
         var6.putFloat(sway.offsetX).putFloat(sway.offsetY).putFloat(var2.outsideOnly.getValue() ? 1.0F : 0.0F).putFloat(deltaSeconds);
         int var7 = var2.getColor();
         var6.putFloat((var7 >> 16 & 0xFF) / 255.0F).putFloat((var7 >> 8 & 0xFF) / 255.0F).putFloat((var7 & 0xFF) / 255.0F).putFloat((var7 >>> 24) / 255.0F);
         var6.putFloat(var2.riseSpeed.getValue().floatValue())
            .putFloat(var2.wobble.getValue().floatValue())
            .putFloat(var2.length.getValue().floatValue())
            .putFloat(var2.flameBrightness.getValue().floatValue());
         var6.putFloat(var2.width.getValue().floatValue())
            .putFloat(var2.fire.getValue().floatValue())
            .putFloat(var2.turbulence.getValue().floatValue())
            .putFloat(0.0F)
            .flip();
         var4.writeToBuffer(materialBuffer.slice(), var6);
      } catch (Throwable var11) {
         if (var5 != null) {
            try {
               var5.close();
            } catch (Throwable var9) {
               var11.addSuppressed(var9);
            }
         }

         throw var11;
      }

      if (var5 != null) {
         var5.close();
      }

      RenderPass var12 = var4.createRenderPass(() -> "Larp EchoHand history/composite", var1.getColorTextureView(), Optional.empty());

      try {
         RenderPipelineCompat.setPipeline(var12, var0);
         var12.setUniform("EchoParameters", materialBuffer.slice());
         GpuSampler var13 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR);
         RenderPipelineCompat.setSamplerUniform(var12, "Sampler0", afterTarget.getColorTextureView(), var13);
         RenderPipelineCompat.setSamplerUniform(var12, "Sampler1", beforeTarget.getColorTextureView(), var13);
         RenderPipelineCompat.setSamplerUniform(var12, "Sampler2", historyTarget.getColorTextureView(), var13);
         GpuSampler var14 = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
         RenderPipelineCompat.setSamplerUniform(var12, "Sampler3", afterTarget.getDepthTextureView(), var14);
         RenderPipelineCompat.setSamplerUniform(var12, "Sampler4", beforeTarget.getDepthTextureView(), var14);
         var12.draw(3, 1, 0, 0);
      } catch (Throwable var10) {
         if (var12 != null) {
            try {
               var12.close();
            } catch (Throwable var8) {
               var10.addSuppressed(var8);
            }
         }

         throw var10;
      }

      if (var12 != null) {
         var12.close();
      }
   }

   private static void disableOnError(String var0, RuntimeException var1) {
      disabled = (boolean)1;
      release();
      LarpLog.error("EchoHand", "stage=" + var0 + "; history disabled until resource reload", var1);
   }

   public static void release() {
      TextureTarget[] var0 = new TextureTarget[]{beforeTarget, afterTarget, historyTarget, nextTarget};
      nextTarget = null;
      historyTarget = null;
      afterTarget = null;
      beforeTarget = null;
      GpuBuffer var1 = materialBuffer;
      materialBuffer = null;
      captured = (boolean)0;
      capturedPlayer = null;
      capturedLevel = null;
      capturedStyle = null;
      sway.reset();
      lastFrameNanos = 0L;
      RuntimeException var2 = null;

      for (TextureTarget var6 : var0) {
         if (var6 != null) {
            try {
               var6.destroyBuffers();
            } catch (RuntimeException var9) {
               if (var2 == null) {
                  var2 = var9;
               } else {
                  var2.addSuppressed(var9);
               }
            }
         }
      }

      if (var1 != null) {
         try {
            var1.close();
         } catch (RuntimeException var8) {
            if (var2 == null) {
               var2 = var8;
            } else {
               var2.addSuppressed(var8);
            }
         }
      }

      if (var2 != null) {
         LarpLog.error("EchoHand", "stage=cleanup; attachments released", var2);
      }
   }

   public static double distanceSquared(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = var0 - var6;
      double var14 = var2 - var8;
      double var16 = var4 - var10;
      return var12 * var12 + var14 * var14 + var16 * var16;
   }

   private static Identifier identifier(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static final class HandSway {
      public float offsetX;
      public float offsetY;
      private float lastYaw;
      private float lastPitch;
      private float velocityX;
      private float velocityY;
      private boolean initialized;

      public void update(float var1, float var2, float var3) {
         if (!this.initialized) {
            this.lastYaw = var1;
            this.lastPitch = var2;
            this.initialized = (boolean)1;
         } else {
            float var4 = Mth.wrapDegrees(var1 - this.lastYaw);
            float var5 = var2 - this.lastPitch;
            this.lastYaw = var1;
            this.lastPitch = var2;
            float var6 = Mth.clamp(var4 * 0.002F * var3, -0.065F, 0.065F);
            float var7 = Mth.clamp(-var5 * 0.002F * var3, -0.065F, 0.065F);
            this.velocityX = (this.velocityX + (var6 - this.offsetX) * 0.18F) * 0.74F;
            this.velocityY = (this.velocityY + (var7 - this.offsetY) * 0.18F) * 0.74F;
            this.offsetX = Mth.clamp((this.offsetX + this.velocityX) * 0.92F, -0.075F, 0.075F);
            this.offsetY = Mth.clamp((this.offsetY + this.velocityY) * 0.92F, -0.075F, 0.075F);
         }
      }

      public void reset() {
         this.initialized = (boolean)0;
         this.offsetX = this.offsetY = this.lastYaw = this.lastPitch = this.velocityX = this.velocityY = 0.0F;
      }
   }
}

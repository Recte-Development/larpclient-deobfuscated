package com.larpclient.render.entity;

import com.larpclient.module.render.FlameHandsModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.random.RandomGenerator;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class FlameHandsRenderer {
   public static final int maxParticles = 768;
   private static final Identifier flameTexture = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png");
   private static final Identifier glowTexture = Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame_glow.png");
   private static final FlameHandsRenderer.Emitter[] handEmitters = new FlameHandsRenderer.Emitter[]{
      new FlameHandsRenderer.Emitter(new Random()), new FlameHandsRenderer.Emitter(new Random())
   };
   private static Object lastLevel;
   private static Object lastPlayer;
   private static int resourceGeneration = -1;
   private static boolean suspended;
   private static Vec3 lastPlayerPos;

   public static void reset() {
      for (FlameHandsRenderer.Emitter var3 : handEmitters) {
         var3.clear();
      }

      lastPlayer = null;
      lastLevel = null;
      lastPlayerPos = null;
      suspended = (boolean)0;
   }

   public static void syncState() {
      Minecraft var0 = Minecraft.getInstance();
      if (lastLevel != var0.level || lastPlayer != var0.player || resourceGeneration != PostProcessing.reloadCount) {
         reset();
         lastLevel = var0.level;
         lastPlayer = var0.player;
         resourceGeneration = PostProcessing.reloadCount;
      }

      if (var0.player != null) {
         Vec3 var1 = var0.player.position();
         if (lastPlayerPos != null && lastPlayerPos.distanceToSqr(var1) > 64.0) {
            for (FlameHandsRenderer.Emitter var5 : handEmitters) {
               var5.clear();
            }

            LarpLog.debug("FlameHands", "Cleared histories after player relocation");
         }

         lastPlayerPos = var1;
      }

      if (var0.player == null || var0.level == null || !var0.options.getCameraType().isFirstPerson()) {
         for (FlameHandsRenderer.Emitter var9 : handEmitters) {
            var9.clear();
         }
      }
   }

   public static void renderHand(HumanoidArm var0, PoseStack var1, SubmitNodeCollector var2, boolean var3) {
      FlameHandsModule var4 = FlameHandsModule.getInstance();
      Minecraft var5 = Minecraft.getInstance();
      if (var4 != null && var4.isRuntimeActive() && var5.player != null && var5.level != null && var5.options.getCameraType().isFirstPerson()) {
         syncState();
         FlameHandsRenderer.Emitter var6 = handEmitters[var0 == HumanoidArm.RIGHT ? 0 : 1];
         if (!var4.shouldEmit(var0 == var5.player.getMainArm(), var3)) {
            var6.clear();
         } else if (!suspended) {
            try {
               FlameHandsRenderer.RenderTypes.validatePipeline();
               Camera var7 = var5.gameRenderer.mainCamera();
               Vec3 var13 = var5.player.getDeltaMovement();
               Vector3f var14 = new Vector3f((float)var13.x, (float)var13.y, (float)var13.z).rotate(new Quaternionf(var7.rotation()).conjugate());
               FlameHandsRenderer.Settings var15 = var4.createRendererSettings();
               var6.update(TickTimer.getTime(), var7.yRot(), var7.xRot(), var14.x, var14.y, var14.z, var3, var15);
               double[] var16 = var6.buildVertexData(var1.last().pose(), var15);
               if (var16.length == 0) {
                  return;
               }

               var2.order(2)
                  .submitCustomGeometry(
                     new PoseStack(),
                     FlameHandsRenderer.RenderTypes.select(var4.renderMode.isMode("Glow")),
                     (var1x, var2x) -> submitParticles(var16, var1x, var2x)
                  );
               PerfProfiler.setCounter("Flame hand particles", var16.length / 6);
            } catch (RuntimeException var12) {
               for (FlameHandsRenderer.Emitter var11 : handEmitters) {
                  var11.clear();
               }

               suspended = (boolean)1;
               LarpLog.error("FlameHands", "Hand particle submission failed; suspended until disable or resource reload", var12);
            }
         }
      }
   }

   private static void submitParticles(double[] var0, Pose var1, VertexConsumer var2) {
      for (byte var3 = 0; var3 < var0.length; var3 += 6) {
         float var4 = (float)var0[var3];
         float var5 = (float)var0[var3 + 1];
         float var6 = (float)var0[var3 + 2];
         float var7 = (float)var0[var3 + 3] * 0.5F;
         float var8 = (float)var0[var3 + 4];
         float var9 = (float)Math.cos(var8) * var7;
         float var10 = (float)Math.sin(var8) * var7;
         int var11 = (int)((long)var0[var3 + 5]);
         addVertex(var2, var1, var4 - var9 - var10, var5 + var9 - var10, var6, var11, 0.0F, 1.0F);
         addVertex(var2, var1, var4 + var9 - var10, var5 + var9 + var10, var6, var11, 1.0F, 1.0F);
         addVertex(var2, var1, var4 + var9 + var10, var5 - var9 + var10, var6, var11, 1.0F, 0.0F);
         addVertex(var2, var1, var4 - var9 + var10, var5 - var9 - var10, var6, var11, 0.0F, 0.0F);
      }
   }

   private static void addVertex(VertexConsumer var0, Pose var1, float var2, float var3, float var4, int var5, float var6, float var7) {
      var0.addVertex(var1, var2, var3, var4).setColor(var5).setUv(var6, var7);
   }

   public static float wrapDegrees(float var0) {
      float var1 = var0 % 360.0F;
      return var1 >= 180.0F ? var1 - 360.0F : (var1 < -180.0F ? var1 + 360.0F : var1);
   }

   public static int particleColor(double var0, FlameHandsRenderer.Settings var2) {
      return switch (var2.colorMode) {
         case "Gradient" -> lerpColor(var2.primary, var2.secondary, (float)(Math.sin(var0 * 3.0) * 0.5 + 0.5));
         case "Rainbow" -> Color.HSBtoRGB((float)(var0 % 4.0 / 4.0), 1.0F, 1.0F);
         default -> var2.primary;
      };
   }

   public static int lerpColor(int var0, int var1, float var2) {
      float var3 = Math.clamp(var2, 0.0F, 1.0F);
      int var4 = 0;

      for (byte var5 = 0; var5 <= 24; var5 += 8) {
         var4 |= Math.round((var0 >>> var5 & 0xFF) * (1.0F - var3) + (var1 >>> var5 & 0xFF) * var3) << var5;
      }

      return var4;
   }

   private FlameHandsRenderer() {
   }

   public static final class Emitter {
      private final RandomGenerator random;
      private final ArrayList<FlameHandsRenderer.Particle> particles = new ArrayList<>();
      private double lastTime = Double.NaN;
      private double spawnAccumulator;
      private float lastYaw;
      private float lastPitch;

      public Emitter(RandomGenerator var1) {
         this.random = var1;
      }

      public int particleCount() {
         return this.particles.size();
      }

      public void clear() {
         this.particles.clear();
         this.lastTime = Double.NaN;
         this.spawnAccumulator = 0.0;
      }

      public void update(double var1, float var3, float var4, float var5, float var6, float var7, boolean var8, FlameHandsRenderer.Settings var9) {
         if (!Double.isFinite(var1)) {
            this.clear();
         } else if (Double.isFinite(this.lastTime) && !(var1 < this.lastTime) && !(var1 - this.lastTime > 0.25)) {
            float var10 = (float)Math.clamp(var1 - this.lastTime, 0.0, 0.05);
            if (!(var10 <= 0.0F)) {
               this.lastTime = var1;
               float var11 = FlameHandsRenderer.wrapDegrees(var3 - this.lastYaw);
               float var12 = var4 - this.lastPitch;
               this.lastYaw = var3;
               this.lastPitch = var4;
               float var13 = var9.inertia;
               float var14 = -var11 * var13 * 0.008F - var5 * var13 * 0.15F;
               float var15 = var12 * var13 * 0.008F - var6 * var13 * 0.15F + var9.buoyancy * 0.012F;
               float var16 = -var7 * var13 * 0.15F;
               float var17 = var10 * 60.0F;
               float var18 = (float)Math.pow(0.86, var17);
               float var19 = var9.turbulence * 0.008F;

               for (int var20 = 0; var20 < this.particles.size(); var20++) {
                  FlameHandsRenderer.Particle var21 = this.particles.get(var20);
                  if (var1 - var21.spawnTime >= var21.lifetime) {
                     this.particles.remove(var20--);
                  } else {
                     var21.x = var21.x + var21.velocityX * var17;
                     var21.y = var21.y + var21.velocityY * var17;
                     var21.z2 = var21.z2 + var21.velocityZ * var17;
                     var21.velocityX = (var21.velocityX + (var14 + this.randomRange(-var19, var19)) * var17) * var18;
                     var21.velocityY = (var21.velocityY + (var15 + this.randomRange(-var19, var19) * 0.5F) * var17) * var18;
                     var21.velocityZ = (var21.velocityZ + (var16 + this.randomRange(-var19, var19)) * var17) * var18;
                     var21.rotation = var21.rotation + var21.spin * var10;
                  }
               }

               this.spawnAccumulator += var10;
               double var26 = 1.0 / var9.density;

               while (this.spawnAccumulator + 1.0E-8 >= var26) {
                  this.spawnAccumulator = Math.max(0.0, this.spawnAccumulator - var26);
                  float var22 = this.random.nextFloat();
                  float var23 = var8 ? 0.45F : 0.15F;
                  float var24 = var8 ? 0.06F : 0.04F;
                  FlameHandsRenderer.Particle var25 = new FlameHandsRenderer.Particle();
                  var25.x = this.randomRange(-var24, var24);
                  var25.y = var22 * var23 + this.randomRange(-0.015F, 0.015F);
                  var25.z2 = -var22 * var23 * 0.35F + this.randomRange(-0.015F, 0.015F);
                  var25.velocityX = this.randomRange(-0.001F, 0.001F);
                  var25.velocityY = this.randomRange(0.003F, 0.008F);
                  var25.velocityZ = this.randomRange(-0.001F, 0.001F);
                  var25.size = var9.size * this.randomRange(0.7F, 1.3F) * (1.0F - var22 * 0.4F);
                  var25.lifetime = Math.max(0.1F, var9.lifetime * this.randomRange(0.8F, 1.2F));
                  var25.spawnTime = var1;
                  var25.color = FlameHandsRenderer.particleColor(var1, var9);
                  var25.rotation = this.randomRange(0.0F, 360.0F);
                  var25.spin = this.randomRange(-90.0F, 90.0F);
                  if (this.particles.size() >= 768) {
                     this.particles.removeFirst();
                  }

                  this.particles.add(var25);
               }
            }
         } else {
            this.clear();
            this.lastTime = var1;
            this.lastYaw = var3;
            this.lastPitch = var4;
         }
      }

      private float randomRange(float var1, float var2) {
         return var1 + (var2 - var1) * this.random.nextFloat();
      }

      public double[] buildVertexData(Matrix4f var1, FlameHandsRenderer.Settings var2) {
         double[] var3 = new double[this.particles.size() * 6];
         int var4 = 0;

         for (FlameHandsRenderer.Particle var6 : this.particles) {
            Vector3f var7 = new Vector3f(var6.x, var6.y, var6.z2);
            var1.transformPosition(var7);
            float var8 = (float)Math.clamp((this.lastTime - var6.spawnTime) / var6.lifetime, 0.0, 1.0);
            int var9 = var2.colorMode.equals("Gradient") ? FlameHandsRenderer.lerpColor(var6.color, var2.secondary, var8) : var6.color;
            int var10 = Math.round((var9 >>> 24) * (1.0F - var8));
            var3[var4++] = var7.x;
            var3[var4++] = var7.y;
            var3[var4++] = var7.z;
            var3[var4++] = var6.size;
            var3[var4++] = (float)Math.toRadians(var6.rotation);
            var3[var4++] = var9 & 16777215 | var10 << 24;
         }

         return var3;
      }
   }

   private static final class Particle {
      float x;
      float y;
      float z2;
      float velocityX;
      float velocityY;
      float velocityZ;
      float size;
      float lifetime;
      float rotation;
      float spin;
      double spawnTime;
      int color;
   }

   private static final class RenderTypes {
      private static final RenderPipeline pipeline = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "flame_hands"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      private static int validatedGeneration = -1;
      private static final RenderType bloomType = createRenderType("bloom", FlameHandsRenderer.flameTexture);
      private static final RenderType glowType = createRenderType("glow", FlameHandsRenderer.glowTexture);

      private static RenderType createRenderType(String var0, Identifier var1) {
         return RenderType.create("larp_flame_hand_" + var0, RenderSetup.builder(pipeline).withTexture("Sampler0", var1).createRenderSetup());
      }

      private static RenderType select(boolean var0) {
         return var0 ? glowType : bloomType;
      }

      private static void validatePipeline() {
         if (validatedGeneration != PostProcessing.reloadCount) {
            ClientPipelines.validatePipeline(pipeline);
            validatedGeneration = PostProcessing.reloadCount;
            LarpLog.debug("FlameHands", "Reference additive sprite material validated for resource generation {}", validatedGeneration);
         }
      }
   }

   public record Settings(
      float size, float density, float lifetime, float buoyancy, float turbulence, float inertia, int primary, int secondary, String colorMode
   ) {
   }
}

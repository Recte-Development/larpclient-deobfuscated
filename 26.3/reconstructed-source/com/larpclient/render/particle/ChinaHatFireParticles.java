package com.larpclient.render.particle;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.render.ColorUtil;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.SplittableRandom;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import org.joml.Matrix4f;

public final class ChinaHatFireParticles {
   public static final int mAX_PARTICLES = 288;
   static final double fLAME_RATE = 120.0;
   static final double sPARK_RATE = 90.0;
   public static final float bASE_SCALE = 0.24F;
   static final double fLAME_MIN_LIFE = 0.6;
   static final double fLAME_LIFE_VARIANCE = 0.25;
   static final double sPARK_MIN_LIFE = 0.36;
   static final double sPARK_LIFE_VARIANCE = 0.16;
   static final double fLAME_SPEED = 0.85;
   static final double sPARK_SPEED = 0.95;
   static final double sPEED_JITTER = 0.3;
   static final double fLAME_BUOYANCY = 1.9;
   static final double sPARK_BUOYANCY = 2.5;
   static final double fLAME_SIZE = 0.19;
   static final double sPARK_SIZE = 0.11;
   static final double fLAME_SPAWN_RADIUS = 0.09;
   static final double sPARK_SPAWN_RADIUS = 0.05;
   static final double lATERAL_SPEED = 0.09;
   public static final double fLAME_STRETCH = 1.8;
   public static final double sPARK_STRETCH = 1.4;
   static final double vELOCITY_INHERIT = 0.5;
   private final double[] posX = new double[288];
   private final double[] posY = new double[288];
   private final double[] posZ = new double[288];
   private final double[] velX = new double[288];
   private final double[] velY = new double[288];
   private final double[] velZ = new double[288];
   private final float[] age = new float[288];
   private final float[] lifetime = new float[288];
   private final float[] size = new float[288];
   private final float[] rotation = new float[288];
   private final float[] spin = new float[288];
   private final float[] variant = new float[288];
   private final boolean[] isSpark = new boolean[288];
   private final SplittableRandom random;
   private int count;
   private double lastTime = Double.NaN;
   private double flameAccumulator;
   private double sparkAccumulator;
   private double lastX;
   private double lastY;
   private double lastZ;
   private double smoothVelX;
   private double smoothVelY;
   private double smoothVelZ;
   public final Matrix4f pipeline = new Matrix4f();
   public long textureGeneration = -1L;

   public ChinaHatFireParticles(long var1) {
      this.random = new SplittableRandom(var1);
   }

   public int getCount() {
      return this.count;
   }

   public void reset() {
      this.count = 0;
      this.lastTime = Double.NaN;
      this.flameAccumulator = this.sparkAccumulator = this.smoothVelX = this.smoothVelY = this.smoothVelZ = 0.0;
   }

   public static double getCount(double var0, double var2) {
      double var4 = 0.7224999999999999;
      double var6 = 1.15;
      return (var6 * var4 + 0.95 * var4 * var4) * var2 * var0 + 0.1368 * var0;
   }

   public void update(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
      double var19 = var1 - this.lastTime;
      double var21 = (var3 - this.lastX) * (var3 - this.lastX) + (var5 - this.lastY) * (var5 - this.lastY) + (var7 - this.lastZ) * (var7 - this.lastZ);
      if (var19 >= 0.0 && !(var19 > 0.25) && !(var21 > 4.0)) {
         if (var19 != 0.0) {
            float var23 = (float)var19;
            double var24 = 1.0 - Math.exp(-var19 * 12.0);
            this.smoothVelX = this.smoothVelX + ((var3 - this.lastX) / var19 - this.smoothVelX) * var24;
            this.smoothVelY = this.smoothVelY + ((var5 - this.lastY) / var19 - this.smoothVelY) * var24;
            this.smoothVelZ = this.smoothVelZ + ((var7 - this.lastZ) / var19 - this.smoothVelZ) * var24;
            double var26 = Math.exp(-2.5 * var19);
            int var28 = 0;

            while (var28 < this.count) {
               this.age[var28] = this.age[var28] + var23;
               if (this.age[var28] >= this.lifetime[var28]) {
                  this.removeParticle(var28);
               } else {
                  double var29 = (this.isSpark[var28] ? 2.5 : 1.9) * var17 * var15 * var19;
                  double var31 = this.velX[var28] * var9 + this.velY[var28] * var11 + this.velZ[var28] * var13 + var29;
                  this.velX[var28] = (this.velX[var28] - var9 * (var31 - var29)) * var26 + var9 * var31;
                  this.velY[var28] = (this.velY[var28] - var11 * (var31 - var29)) * var26 + var11 * var31;
                  this.velZ[var28] = (this.velZ[var28] - var13 * (var31 - var29)) * var26 + var13 * var31;
                  this.posX[var28] = this.posX[var28] + this.velX[var28] * var19;
                  this.posY[var28] = this.posY[var28] + this.velY[var28] * var19;
                  this.posZ[var28] = this.posZ[var28] + this.velZ[var28] * var19;
                  this.rotation[var28] = this.rotation[var28] + this.spin[var28] * var23;
                  var28++;
               }
            }

            double var62 = Math.abs(var11) < 0.9 ? 0.0 : 1.0;
            double var30 = Math.abs(var11) < 0.9 ? 1.0 : 0.0;
            double var32 = var30 * var13 - 0.0 * var11;
            double var34 = 0.0 * var9 - var62 * var13;
            double var36 = var62 * var11 - var30 * var9;
            double var38 = Math.sqrt(var32 * var32 + var34 * var34 + var36 * var36);
            var32 /= var38;
            var34 /= var38;
            var36 /= var38;
            double var40 = var11 * var36 - var13 * var34;
            double var42 = var13 * var32 - var9 * var36;
            double var44 = var9 * var34 - var11 * var32;
            this.flameAccumulator += 120.0 * var19;
            this.sparkAccumulator += 90.0 * var19;

            while (this.flameAccumulator >= 1.0 || this.sparkAccumulator >= 1.0) {
               int var46 = this.sparkAccumulator >= this.flameAccumulator ? 1 : 0;
               if (var46 != 0) {
                  this.sparkAccumulator--;
               } else {
                  this.flameAccumulator--;
               }

               if (this.count != 288) {
                  double var47 = this.random.nextDouble();
                  double var49 = (var46 != 0 ? 0.05 : 0.09) * var15 * Math.sqrt(this.random.nextDouble());
                  double var51 = this.random.nextDouble() * Math.PI * 2.0;
                  double var53 = Math.cos(var51);
                  double var55 = Math.sin(var51);
                  double var57 = ((var46 != 0 ? 0.95 : 0.85) + this.random.nextDouble() * 0.3) * var17 * var15;
                  double var59 = 0.09 * var15 * this.random.nextDouble();
                  int var61 = this.count++;
                  this.isSpark[var61] = (boolean)var46;
                  this.posX[var61] = this.lastX + (var3 - this.lastX) * var47 + (var32 * var53 + var40 * var55) * var49;
                  this.posY[var61] = this.lastY + (var5 - this.lastY) * var47 + (var34 * var53 + var42 * var55) * var49;
                  this.posZ[var61] = this.lastZ + (var7 - this.lastZ) * var47 + (var36 * var53 + var44 * var55) * var49;
                  this.velX[var61] = var9 * var57 + (var32 * var53 + var40 * var55) * var59 + this.smoothVelX * 0.5;
                  this.velY[var61] = var11 * var57 + (var34 * var53 + var42 * var55) * var59 + this.smoothVelY * 0.5;
                  this.velZ[var61] = var13 * var57 + (var36 * var53 + var44 * var55) * var59 + this.smoothVelZ * 0.5;
                  this.lifetime[var61] = (float)(var46 != 0 ? 0.36 + this.random.nextDouble() * 0.16 : 0.6 + this.random.nextDouble() * 0.25);
                  this.age[var61] = (float)((1.0 - var47) * var19);
                  this.size[var61] = (float)((var46 != 0 ? 0.11 : 0.19) * var15 * (0.8 + this.random.nextDouble() * 0.4));
                  this.rotation[var61] = (float)((this.random.nextDouble() - 0.5) * 0.5);
                  this.spin[var61] = (float)((this.random.nextDouble() - 0.5) * 1.2);
                  this.variant[var61] = (float)this.random.nextDouble();
               }
            }

            this.lastTime = var1;
            this.lastX = var3;
            this.lastY = var5;
            this.lastZ = var7;
         }
      } else {
         this.reset();
         this.lastTime = var1;
         this.lastX = var3;
         this.lastY = var5;
         this.lastZ = var7;
      }
   }

   private void removeParticle(int var1) {
      int var2 = --this.count;
      this.posX[var1] = this.posX[var2];
      this.posY[var1] = this.posY[var2];
      this.posZ[var1] = this.posZ[var2];
      this.velX[var1] = this.velX[var2];
      this.velY[var1] = this.velY[var2];
      this.velZ[var1] = this.velZ[var2];
      this.age[var1] = this.age[var2];
      this.lifetime[var1] = this.lifetime[var2];
      this.size[var1] = this.size[var2];
      this.rotation[var1] = this.rotation[var2];
      this.spin[var1] = this.spin[var2];
      this.variant[var1] = this.variant[var2];
      this.isSpark[var1] = this.isSpark[var2];
   }

   public void update(ChinaHatFireParticles.ParticleSink var1, int var2, int var3, double var4) {
      int var6 = ColorUtil.lerpColor(var2, -13631488, 0.3);
      int var7 = ColorUtil.lerpColor(var3, -1, 0.35);

      for (int var8 = 0; var8 < 2; var8++) {
         for (int var9 = 0; var9 < this.count; var9++) {
            if (this.isSpark[var9] == (var8 == 1 ? 1 : 0)) {
               double var10 = Math.clamp(this.age[var9] / this.lifetime[var9], 0.0F, 1.0F);
               double var12;
               double var14;
               int var16;
               if (this.isSpark[var9]) {
                  var12 = 1.0 - 0.55 * var10;
                  var14 = reset(var10, 0.18) * (1.0 - var10) * 0.9;
                  var16 = ColorUtil.lerpColor(var7, var3, var10);
               } else {
                  var12 = var10 < 0.15 ? 0.55 + 0.45 * var10 / 0.15 : 1.0 - 0.6 * (var10 - 0.15) / 0.85;
                  var14 = reset(var10, 0.22) * (1.0 - var10) * 0.62;
                  var16 = var10 < 0.4 ? ColorUtil.lerpColor(var3, var2, var10 / 0.4) : ColorUtil.lerpColor(var2, var6, (var10 - 0.4) / 0.6);
               }

               int var17 = (int)Math.round(Math.clamp(var14 * var4, 0.0, 1.0) * 255.0);
               if (var17 != 0) {
                  var1.drawParticle(
                     this.posX[var9],
                     this.posY[var9],
                     this.posZ[var9],
                     this.size[var9] * var12,
                     this.isSpark[var9] ? 1.4 : 1.8,
                     this.rotation[var9],
                     var17 << 24 | var16 & 16777215
                  );
               }
            }
         }
      }
   }

   static double reset(double var0, double var2) {
      double var4 = Math.clamp(var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static RenderType getRenderType() {
      if (ChinaHatFireParticles.FireRenderResources.validatedGeneration != PostProcessing.reloadCount) {
         ClientPipelines.validatePipeline(ChinaHatFireParticles.FireRenderResources.pIPELINE);
         ChinaHatFireParticles.FireRenderResources.validatedGeneration = PostProcessing.reloadCount;
      }

      return ChinaHatFireParticles.FireRenderResources.rENDER_TYPE;
   }

   private static final class FireRenderResources {
      static final RenderPipeline pIPELINE = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "china_hat_fire"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
         .withCull(false)
         .build();
      static final RenderType rENDER_TYPE = RenderType.create(
         "larp_china_hat_fire",
         RenderSetup.builder(pIPELINE)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/flame.png"))
            .createRenderSetup()
      );
      private static long validatedGeneration = -1L;
   }

   @FunctionalInterface
   public interface ParticleSink {
      void drawParticle(double var1, double var3, double var5, double var7, double var9, double var11, int var13);
   }
}

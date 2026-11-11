package com.larpclient.render.world;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.entity.ChinaHatRenderer;
import com.larpclient.render.entity.FlameRingRenderer;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.model.ChinaHatHorns;
import com.larpclient.render.model.CrystalMesh;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.model.RibbonMesh;
import com.larpclient.render.particle.ChinaHatFireParticles;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.particle.SoftParticleRenderer;
import com.larpclient.social.cosmetic.DominusHatMesh;
import com.larpclient.social.cosmetic.HatMesh;
import com.larpclient.social.pet.CryptoCoinPets;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.misc.PreviewSink;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.HeadCoinLayout;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public final class EffectsRenderer implements PreviewSink {
   public static final RenderType effectsRenderType = RenderType.create(
      "larp_effects", RenderSetup.builder(ClientPipelines.wORLD).sortOnUpload().createRenderSetup()
   );
   private static final RenderType[][] spriteRenderTypes = new RenderType[HitEffectConfig.Texture.values().length][3];
   private static final RenderType[][] softSpriteRenderTypes = new RenderType[HitEffectConfig.Texture.values().length][3];
   private static final RenderType cashRenderType;
   private static final double[] cashScratch = new double[60];
   private final double[] burstScratch = new double[18];
   private static final RenderType[] geometryRenderTypes = new RenderType[3];
   private static final RenderType solidGeometryRenderType = RenderType.create(
      "larp_geometry_solid",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "geometry_solid"))
               .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
               .withColorTargetState(ColorTargetState.DEFAULT)
               .withCull(false)
               .build()
         )
         .createRenderSetup()
   );
   private boolean softParticles;
   private HitEffectConfig.BlendMode blendMode = HitEffectConfig.BlendMode.NORMAL;
   private boolean solid;
   private final EffectBatch bufferSource;
   private final PoseStack poseStack;
   private final Vector3f cameraRight;
   private final Vector3f cameraUp;
   private final Matrix4f poseMatrix;
   private final Vec3 cameraPos;
   private static final double[] cosTable;
   private static final double[] sinTable;

   public void drawDisc(Vec3 var1, double var2, float var4, float var5, int var6, int var7, boolean var8, float var9) {
      VertexConsumer var10 = this.bufferSource.getConsumer(JumpRingRenderer.getRenderType(var7, var8));
      int var11 = Float.floatToRawIntBits(var4);
      int var12 = Float.floatToRawIntBits(var5);
      int var13 = Math.clamp(Math.round(var9 * 255.0F), 0, 255) << 24 | var6 & 16777215;

      for (int var14 = 0; var14 < 4; var14++) {
         float var15 = var14 != 0 && var14 != 3 ? 1.25F : -1.25F;
         float var16 = var14 < 2 ? -1.25F : 1.25F;
         var10.addVertex(
               this.poseMatrix,
               (float)(var1.x + var15 * var2 - this.cameraPos.x),
               (float)(var1.y + 0.02 - this.cameraPos.y),
               (float)(var1.z + var16 * var2 - this.cameraPos.z)
            )
            .setColor(var13)
            .setUv(var15, var16)
            .setUv1(var11 & 65535, var11 >>> 16)
            .setUv2(var12 & 65535, var12 >>> 16)
            .setNormal(0.0F, 1.0F, 0.0F);
      }
   }

   public void setSoftParticles(boolean var1) {
      this.softParticles = (boolean)(var1 && SoftParticleRenderer.isActive() ? 1 : 0);
   }

   public void flush() {
      this.bufferSource.flush();
   }

   @Override
   public void blend(HitEffectConfig.BlendMode var1) {
      this.blendMode = var1;
   }

   @Override
   public void solid(boolean var1) {
      this.solid = var1;
   }

   public static RenderType geometryRenderType(HitEffectConfig.BlendMode var0) {
      return geometryRenderTypes[var0.ordinal()];
   }

   public static RenderType drawChinaHat() {
      return solidGeometryRenderType;
   }

   public static void registerPipelines() {
      for (RenderType var3 : spriteRenderTypes[HitEffectConfig.Texture.EFFECTS.ordinal()]) {
         ClientPipelines.validatePipeline(var3.pipeline());
      }

      for (RenderType var7 : geometryRenderTypes) {
         ClientPipelines.validatePipeline(var7.pipeline());
      }

      ClientPipelines.validatePipeline(solidGeometryRenderType.pipeline());
      ClientPipelines.validatePipeline(cashRenderType.pipeline());
   }

   static PreparedRenderType drawChinaHat(PreparedRenderType var0) {
      for (int var1 = 0; var1 < 3; var1++) {
         if (var0.pipeline() == softSpriteRenderTypes[0][var1].pipeline()) {
            return RenderPipelineCompat.createTextureTarget(var0, spriteRenderTypes[0][var1].pipeline());
         }
      }

      throw new IllegalArgumentException("Unknown soft particle pipeline");
   }

   public static void registerSoftPipelines() {
      for (RenderType var3 : softSpriteRenderTypes[HitEffectConfig.Texture.EFFECTS.ordinal()]) {
         ClientPipelines.validatePipeline(var3.pipeline());
      }
   }

   public EffectsRenderer(PoseStack var1, EffectBatch var2) {
      this(var1, var2, Minecraft.getInstance().gameRenderer.mainCamera().position(), Minecraft.getInstance().gameRenderer.mainCamera().rotation());
   }

   public EffectsRenderer(PoseStack var1, EffectBatch var2, CameraRenderState var3) {
      this(var1, var2, var3.pos, var3.orientation);
   }

   private EffectsRenderer(PoseStack var1, EffectBatch var2, Vec3 var3, Quaternionfc var4) {
      this.bufferSource = var2;
      this.poseStack = var1;
      this.cameraRight = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var4);
      this.cameraUp = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var4);
      this.poseMatrix = var1.last().pose();
      this.cameraPos = var3;
   }

   public void registerSoftPipelines(EntityRenderState var1, double var2, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      Matrix4f var13 = ModelVisualRenderer.resetBounds(var1, this.poseStack);
      if (var13 != null) {
         HatMesh.render(this, var13, var2, var4, var6, var8, var9, var10, var11);
      }
   }

   public Matrix4f drawEntityHat(EntityRenderState var1, HeadCoinLayout.Accessory var2, double var3, double var5, double var7, double var9) {
      Matrix4f var11 = ModelVisualRenderer.resetBounds(var1, this.poseStack);
      if (var11 == null) {
         return null;
      }

      List var12 = HeadCoinLayout.slotsFor(var2);
      int var13 = !var12.isEmpty() && CryptoCoinPets.tryLoad("ChinaHat") ? 1 : 0;
      if (var13 != 0) {
         Matrix4f var14 = new Matrix4f();

         for (HeadCoinLayout.CoinSlot var16 : var12) {
            CryptoCoinPets.renderWithMatrix(this, CryptoCoinPets.cOINS.get(var16.coin()), HeadCoinLayout.buildSlots(var11, var16, var3, var14));
         }
      }

      HeadCoinLayout.render(this, var11, var2, var3, var5, (boolean)var13, var7, var9);
      return var11;
   }

   public void drawEntityEffect(Matrix4f var1, double var2, double var4, double var6) {
      FlameRingRenderer.renderGlowRing(this, var1, var2, var4, var6);
   }

   public Matrix4f drawChinaHat(EntityRenderState var1, double var2) {
      ChinaHatHorns.HornMesh var4 = ChinaHatHorns.loadMesh();
      if (var4 == null) {
         return null;
      }

      Matrix4f var5 = ModelVisualRenderer.resetBounds(var1, this.poseStack);
      if (var5 == null) {
         return null;
      }

      ChinaHatHorns.addTriangles(this.bufferSource.getConsumer(ChinaHatHorns.getRenderType()), var5, var4, var2);
      return var5;
   }

   public void drawHatGlow(EntityRenderState var1, double var2, int var4) {
      ChinaHatHorns.HornMesh var5 = DominusHatMesh.loadMesh();
      if (var5 != null) {
         Matrix4f var6 = ModelVisualRenderer.resetBounds(var1, this.poseStack);
         if (var6 != null) {
            DominusHatMesh.renderMesh(this.bufferSource.getConsumer(DominusHatMesh.getRenderType()), var6, var5, var2, var4);
         }
      }
   }

   public void drawChinaHatAt(Matrix4f var1, double var2) {
      ChinaHatHorns.HornMesh var4 = ChinaHatHorns.loadMesh();
      if (var4 != null) {
         ChinaHatHorns.addGlowTriangles(this.bufferSource.getConsumer(ChinaHatHorns.getRenderType()), var1, var4, var2);
      }
   }

   public void drawDisc(ChinaHatFireParticles var1, Matrix4f var2, double var3, double var5, double var7, int var9, int var10, double var11) {
      ChinaHatHorns.HornMesh var13 = ChinaHatHorns.loadMesh();
      if (var13 != null) {
         Vector3f var14 = new Matrix4f(this.poseMatrix).invert().mul(var2).transformPosition(0.0F, -0.5F, var13.centerZ() * (float)var3, new Vector3f());
         var1.update(var11, this.cameraPos.x + var14.x, this.cameraPos.y + var14.y + 0.24F * var3, this.cameraPos.z + var14.z, 0.0, 1.0, 0.0, var3 * var5, var7);
         this.emitParticles(var1, var9, var10, 1.0);
         PerfProfiler.setCounter("China Hat fire particles", var1.getCount());
      }
   }

   public void emitParticles(ChinaHatFireParticles var1, int var2, int var3, double var4) {
      RenderType var6 = ChinaHatFireParticles.getRenderType();
      var1.update(
         (var2x, var4x, var6x, var8, var10, var12, var14) -> this.emitParticleQuad(var6, var2x, var4x, var6x, var8, var10, var12, var14), var2, var3, var4
      );
   }

   private void emitParticleQuad(RenderType var1, double var2, double var4, double var6, double var8, double var10, double var12, int var14) {
      VertexConsumer var15 = this.bufferSource.getConsumer(var1);
      double var16 = Math.cos(var12);
      double var18 = Math.sin(var12);

      for (int var20 = 0; var20 < 4; var20++) {
         double var21 = var20 != 0 && var20 != 3 ? 1.0 : -1.0;
         double var23 = var20 < 2 ? -1.0 : 1.0;
         double var25 = (var21 * var16 - var23 * var10 * var18) * var8;
         double var27 = (var21 * var18 + var23 * var10 * var16) * var8;
         var15.addVertex(
               this.poseMatrix,
               (float)(var2 - this.cameraPos.x + this.cameraRight.x * var25 + this.cameraUp.x * var27),
               (float)(var4 - this.cameraPos.y + this.cameraRight.y * var25 + this.cameraUp.y * var27),
               (float)(var6 - this.cameraPos.z + this.cameraRight.z * var25 + this.cameraUp.z * var27)
            )
            .setUv((float)((var21 + 1.0) * 0.5), (float)((var23 + 1.0) * 0.5))
            .setColor(var14);
      }
   }

   public void writeVertices(RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      drawRibbon(this.bufferSource, var1, var2, var3, var4);
   }

   public void drawEntityModel(EntityRenderState var1, ModelVisualRenderer.Material var2, int var3, double var4) {
      ModelVisualRenderer.renderEntity(var1, this.poseStack, this.bufferSource, var2, var3, var4, false);
   }

   @Override
   public Vector3f viewer(Matrix4f var1, Vector3f var2) {
      return new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 0.0F));
   }

   @Override
   public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
      this.bufferSource
         .getConsumer(this.solid ? solidGeometryRenderType : geometryRenderTypes[this.blendMode.ordinal()])
         .addVertex(var1, var2, var3, var4)
         .setColor(var5);
   }

   public void drawTransformedVertices(RenderType var1, Vec3 var2, Quaternionfc var3, float var4, float[] var5, int[] var6) {
      Matrix4f var7 = new Matrix4f(this.poseMatrix)
         .translate((float)(var2.x - this.cameraPos.x), (float)(var2.y - this.cameraPos.y), (float)(var2.z - this.cameraPos.z))
         .rotate(var3)
         .scale(var4);
      drawRibbon(this.bufferSource, var1, var7, var5, var6);
   }

   public static void drawRibbon(EffectBatch var0, RenderType var1, Matrix4f var2, float[] var3, int[] var4) {
      VertexConsumer var5 = var0.getConsumer(var1);

      for (int var6 = 0; var6 < var4.length; var6++) {
         int var7 = var6 * 5;
         var5.addVertex(var2, var3[var7], var3[var7 + 1], var3[var7 + 2]).setUv(var3[var7 + 3], var3[var7 + 4]).setColor(var4[var6]);
      }
   }

   @Override
   public void vertex(double var1, double var3, double var5, int var7) {
      this.bufferSource
         .getConsumer(geometryRenderTypes[this.blendMode.ordinal()])
         .addVertex(this.poseMatrix, (float)(var1 - this.cameraPos.x), (float)(var3 - this.cameraPos.y), (float)(var5 - this.cameraPos.z))
         .setColor(this.blendMode == HitEffectConfig.BlendMode.SCREEN ? drawChinaHat(var7) : var7);
   }

   private void drawHatGlow(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.poseMatrix, (float)(var2 - this.cameraPos.x), (float)(var4 - this.cameraPos.y), (float)(var6 - this.cameraPos.z)).setColor(var8);
   }

   @Override
   public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      this.drawRibbon(var1.x, var1.y, var1.z, var2.x, var2.y, var2.z, var3, var3, var5, var6);
   }

   public void drawRibbon(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      double var19 = var7 - var1;
      double var21 = var9 - var3;
      double var23 = var11 - var5;
      if (!(var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10)) {
         double var25 = this.cameraPos.x - var1;
         double var27 = this.cameraPos.y - var3;
         double var29 = this.cameraPos.z - var5;
         double var31 = var21 * var29 - var23 * var27;
         double var33 = var23 * var25 - var19 * var29;
         double var35 = var19 * var27 - var21 * var25;
         double var37 = Math.sqrt(var31 * var31 + var33 * var33 + var35 * var35);
         if (var37 < 1.0E-8) {
            var31 = this.cameraRight.x;
            var33 = this.cameraRight.y;
            var35 = this.cameraRight.z;
            var37 = 1.0;
         }

         var31 /= var37 * 2.0;
         var33 /= var37 * 2.0;
         var35 /= var37 * 2.0;
         this.vertex(var1 + var31 * var13, var3 + var33 * var13, var5 + var35 * var13, var17);
         this.vertex(var7 + var31 * var15, var9 + var33 * var15, var11 + var35 * var15, var18);
         this.vertex(var7 - var31 * var15, var9 - var33 * var15, var11 - var35 * var15, var18);
         this.vertex(var1 - var31 * var13, var3 - var33 * var13, var5 - var35 * var13, var17);
      }
   }

   public void drawGlowRibbon(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17, int var18) {
      this.drawRibbon(var1, var3, var5, var7, var9, var11, var13 * 2.5, var15 * 2.5, ColorUtil.multiplyAlpha(var17, 0.07), ColorUtil.multiplyAlpha(var18, 0.07));
      this.drawRibbon(var1, var3, var5, var7, var9, var11, var13 * 1.25, var15 * 1.25, ColorUtil.multiplyAlpha(var17, 0.3), ColorUtil.multiplyAlpha(var18, 0.3));
      this.drawRibbon(
         var1,
         var3,
         var5,
         var7,
         var9,
         var11,
         var13 * 0.23,
         var15 * 0.23,
         ColorUtil.lerpColor(var17, var17 & 0xFF000000 | 16777215, 0.48),
         ColorUtil.lerpColor(var18, var18 & 0xFF000000 | 16777215, 0.48)
      );
   }

   public void drawTrail(CatmullRomSpline.Ribbon var1, boolean var2) {
      for (int var3 = 0; var3 < (var2 ? 3 : 1); var3++) {
         double var4 = !var2 ? 1.0 : (var3 == 0 ? 2.5 : (var3 == 1 ? 1.25 : 0.23));

         for (int var6 = 1; var6 < var1.size; var6++) {
            Vec3 var7 = var1.points[var6 - 1];
            Vec3 var8 = var1.points[var6];
            if (!(var7.distanceToSqr(this.cameraPos) > var1.maxDistanceSq) && !(var7.distanceToSqr(var8) < 1.0E-10)) {
               Vec3 var9 = var1.offsets[var6 - 1];
               Vec3 var10 = var1.offsets[var6];
               double var11 = var9.x * var4;
               double var13 = var9.y * var4;
               double var15 = var9.z * var4;
               double var17 = var10.x * var4;
               double var19 = var10.y * var4;
               double var21 = var10.z * var4;
               int var23 = trailLayerColor(var1.colors[var6 - 1], var3, var2);
               int var24 = trailLayerColor(var1.colors[var6], var3, var2);
               this.vertex(var7.x + var11, var7.y + var13, var7.z + var15, var23);
               this.vertex(var8.x + var17, var8.y + var19, var8.z + var21, var24);
               this.vertex(var8.x - var17, var8.y - var19, var8.z - var21, var24);
               this.vertex(var7.x - var11, var7.y - var13, var7.z - var15, var23);
            }
         }
      }
   }

   public void drawFadedTrail(CatmullRomSpline.Ribbon var1, double var2) {
      var2 = Math.clamp(var2, 0.0, 0.95);

      for (int var4 = 1; var4 < var1.size; var4++) {
         Vec3 var5 = var1.points[var4 - 1];
         Vec3 var6 = var1.points[var4];
         if (!(var5.distanceToSqr(this.cameraPos) > var1.maxDistanceSq) && !(var5.distanceToSqr(var6) < 1.0E-10)) {
            Vec3 var7 = var1.offsets[var4 - 1];
            Vec3 var8 = var1.offsets[var4];
            int var9 = var1.colors[var4 - 1];
            int var10 = var1.colors[var4];
            if (var9 >>> 24 != 0 || var10 >>> 24 != 0) {
               int var11 = var9 & 16777215;
               int var12 = var10 & 16777215;
               if (var2 > 0.0) {
                  this.vertex(var5.x + var7.x * var2, var5.y + var7.y * var2, var5.z + var7.z * var2, var9);
                  this.vertex(var6.x + var8.x * var2, var6.y + var8.y * var2, var6.z + var8.z * var2, var10);
                  this.vertex(var6.x - var8.x * var2, var6.y - var8.y * var2, var6.z - var8.z * var2, var10);
                  this.vertex(var5.x - var7.x * var2, var5.y - var7.y * var2, var5.z - var7.z * var2, var9);
               }

               for (byte var13 = -1; var13 <= 1; var13 += 2) {
                  double var14 = var2 * var13;
                  this.vertex(var5.x + var7.x * var14, var5.y + var7.y * var14, var5.z + var7.z * var14, var9);
                  this.vertex(var6.x + var8.x * var14, var6.y + var8.y * var14, var6.z + var8.z * var14, var10);
                  this.vertex(var6.x + var8.x * var13, var6.y + var8.y * var13, var6.z + var8.z * var13, var12);
                  this.vertex(var5.x + var7.x * var13, var5.y + var7.y * var13, var5.z + var7.z * var13, var11);
               }
            }
         }
      }
   }

   private static int trailLayerColor(int var0, int var1, boolean var2) {
      if (!var2) {
         return var0;
      } else {
         return var1 < 2 ? ColorUtil.multiplyAlpha(var0, var1 == 0 ? 0.07 : 0.3) : ColorUtil.lerpColor(var0, var0 & 0xFF000000 | 16777215, 0.48);
      }
   }

   public void drawBurst(EntityRenderState var1, Vec3 var2, double var3, double var5, double var7, int var9, int var10, boolean var11) {
      Matrix4f var12 = ModelVisualRenderer.resetBounds(var1, this.poseStack);
      if (var12 != null) {
         ChinaHatRenderer.render(this, var12, var3, var5, var7, var9, var10, var11);
      } else {
         this.drawCone(var2, var3, var5, var9, var10, var11);
      }
   }

   public void drawCone(Vec3 var1, double var2, double var4, int var6, int var7, boolean var8) {
      for (int var9 = 0; var9 < 64; var9++) {
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
         this.vertex(var1.x + cosTable[var9 + 1] * var2, var1.y, var1.z + sinTable[var9 + 1] * var2, var7);
         this.vertex(var1.x + cosTable[var9] * var2, var1.y, var1.z + sinTable[var9] * var2, var7);
         this.vertex(var1.x, var1.y + var4, var1.z, var6);
      }

      if (var8) {
         this.drawDisc(var1, var2, 0.009, ColorUtil.lerpColor(var7, var7 & 0xFF000000 | 16777215, 0.4));
      }
   }

   public void drawDisc(Vec3 var1, double var2, double var4, int var6) {
      this.drawRingFlat(var1.x, var1.y, var1.z, var2, var4, var6);
   }

   public void drawRingFlat(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.drawRing(var1, var3, var5, var7, var9, var11, 0.0, 1.0, 0.0);
   }

   public void drawRing(double var1, double var3, double var5, double var7, double var9, int var11, double var12, double var14, double var16) {
      double var18 = Math.sqrt(var12 * var12 + var14 * var14 + var16 * var16);
      if (var18 < 1.0E-8) {
         var12 = 0.0;
         var14 = 1.0;
         var16 = 0.0;
      } else {
         var12 /= var18;
         var14 /= var18;
         var16 /= var18;
      }

      double var20 = Math.abs(var14) > 0.95 ? 1.0 : -var16;
      double var22 = 0.0;
      double var24 = Math.abs(var14) > 0.95 ? 0.0 : var12;
      double var26 = var20 * var12 + var22 * var14 + var24 * var16;
      var20 -= var12 * var26;
      var22 -= var14 * var26;
      var24 -= var16 * var26;
      double var28 = Math.sqrt(var20 * var20 + var22 * var22 + var24 * var24);
      var20 /= var28;
      var22 /= var28;
      var24 /= var28;
      double var30 = var22 * var16 - var24 * var14;
      double var32 = var24 * var12 - var20 * var16;
      double var34 = var20 * var14 - var22 * var12;
      VertexConsumer var36 = this.bufferSource.getConsumer(geometryRenderTypes[this.blendMode.ordinal()]);
      int var37 = this.blendMode == HitEffectConfig.BlendMode.SCREEN ? 1 : 0;

      for (int var38 = 0; var38 < 4; var38++) {
         double var39 = Math.max(0.0, var7 + (var38 / 2.0 - 1.0) * var9);
         double var41 = Math.max(0.0, var7 + ((var38 + 1) / 2.0 - 1.0) * var9);
         double var43 = var38 == 0 ? 0.0 : (var38 == 2 ? 1.0 : 0.22);
         double var45 = var38 == 3 ? 0.0 : (var38 == 1 ? 1.0 : 0.22);
         int var47 = ColorUtil.multiplyAlpha(var11, var43);
         int var48 = ColorUtil.multiplyAlpha(var11, var45);
         if (var37 != 0) {
            var47 = drawChinaHat(var47);
            var48 = drawChinaHat(var48);
         }

         for (int var49 = 0; var49 < 64; var49++) {
            double var50 = var20 * cosTable[var49] + var30 * sinTable[var49];
            double var52 = var22 * cosTable[var49] + var32 * sinTable[var49];
            double var54 = var24 * cosTable[var49] + var34 * sinTable[var49];
            double var56 = var20 * cosTable[var49 + 1] + var30 * sinTable[var49 + 1];
            double var58 = var22 * cosTable[var49 + 1] + var32 * sinTable[var49 + 1];
            double var60 = var24 * cosTable[var49 + 1] + var34 * sinTable[var49 + 1];
            this.drawHatGlow(var36, var1 + var50 * var39, var3 + var52 * var39, var5 + var54 * var39, var47);
            this.drawHatGlow(var36, var1 + var56 * var39, var3 + var58 * var39, var5 + var60 * var39, var47);
            this.drawHatGlow(var36, var1 + var56 * var41, var3 + var58 * var41, var5 + var60 * var41, var48);
            this.drawHatGlow(var36, var1 + var50 * var41, var3 + var52 * var41, var5 + var54 * var41, var48);
         }
      }
   }

   public void drawCone(double var1, double var3, double var5, double var7, double var9, double var11, double var13, int var15) {
      CrystalMesh.faceCount(this.burstScratch, var7, var9, var11, var13);

      for (int var16 = 0; var16 < CrystalMesh.faceCount(); var16++) {
         int var17 = CrystalMesh.shadeFace(this.burstScratch, var16, var15);

         for (int var18 = 0; var18 < 4; var18++) {
            int var19 = CrystalMesh.faceVertexIndex(var16, var18) * 3;
            this.vertex(var1 + this.burstScratch[var19], var3 + this.burstScratch[var19 + 1], var5 + this.burstScratch[var19 + 2], var17);
         }
      }
   }

   public void drawBurst(double var1, double var3, double var5, double var7, double var9, int var11, int var12, boolean var13) {
      int var14 = var13 ? var12 * 2 : var12;

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = var9 + var15 * Math.PI * 2.0 / var14;
         double var18 = var9 + (var15 + 1) * Math.PI * 2.0 / var14;
         double var20 = var7 * (var13 && var15 % 2 == 1 ? 0.4 : 1.0);
         double var22 = var7 * (var13 && (var15 + 1) % 2 == 1 ? 0.4 : 1.0);
         this.vertex(var1, var3, var5, var11);
         this.vertex(
            var1 + this.cameraRight.x * Math.cos(var16) * var20 + this.cameraUp.x * Math.sin(var16) * var20,
            var3 + this.cameraRight.y * Math.cos(var16) * var20 + this.cameraUp.y * Math.sin(var16) * var20,
            var5 + this.cameraRight.z * Math.cos(var16) * var20 + this.cameraUp.z * Math.sin(var16) * var20,
            var11
         );
         this.vertex(
            var1 + this.cameraRight.x * Math.cos(var18) * var22 + this.cameraUp.x * Math.sin(var18) * var22,
            var3 + this.cameraRight.y * Math.cos(var18) * var22 + this.cameraUp.y * Math.sin(var18) * var22,
            var5 + this.cameraRight.z * Math.cos(var18) * var22 + this.cameraUp.z * Math.sin(var18) * var22,
            var11
         );
         this.vertex(var1, var3, var5, var11);
      }
   }

   private static int drawChinaHat(int var0) {
      double var1 = (var0 >>> 24) / 255.0;
      return var0 & 0xFF000000 | (int)((var0 >> 16 & 0xFF) * var1) << 16 | (int)((var0 >> 8 & 0xFF) * var1) << 8 | (int)((var0 & 0xFF) * var1);
   }

   public void drawChinaHatAt(RenderType var1, double var2, double var4, double var6, double var8, double var10, int var12) {
      VertexConsumer var13 = this.bufferSource.getConsumer(var1);
      double var14 = Math.cos(var10);
      double var16 = Math.sin(var10);

      for (int var18 = 0; var18 < 4; var18++) {
         double var19 = var18 != 0 && var18 != 3 ? 1.0 : -1.0;
         double var21 = var18 < 2 ? -1.0 : 1.0;
         double var23 = (var19 * var14 - var21 * var16) * var8;
         double var25 = (var19 * var16 + var21 * var14) * var8;
         var13.addVertex(
               this.poseMatrix,
               (float)(var2 - this.cameraPos.x + this.cameraRight.x * var23 + this.cameraUp.x * var25),
               (float)(var4 - this.cameraPos.y + this.cameraRight.y * var23 + this.cameraUp.y * var25),
               (float)(var6 - this.cameraPos.z + this.cameraRight.z * var23 + this.cameraUp.z * var25)
            )
            .setUv((float)((var19 + 1.0) * 0.5), (float)((var21 + 1.0) * 0.5))
            .setColor(var12);
      }
   }

   public void drawSprite(double var1, double var3, double var5, double var7, double var9, int var11) {
      this.drawSpriteWithType(var1, var3, var5, var7, var9, var11, ParticleSystem.Style.STAR, 0.0, 0.0, 0.0);
   }

   public void drawSpriteWithType(
      double var1, double var3, double var5, double var7, double var9, int var11, ParticleSystem.Style var12, double var13, double var15, double var17
   ) {
      this.emitParticleQuad(var1, var3, var5, var7, var9, var11, var12, var13, var15, var17, HitEffectConfig.Texture.EFFECTS);
   }

   public void drawOrientedSprite(
      double var1,
      double var3,
      double var5,
      double var7,
      double var9,
      double var11,
      double var13,
      double var15,
      double var17,
      int var19,
      ParticleSystem.Style var20
   ) {
      int var21 = drawTransformedVertices(var20);
      VertexConsumer var22 = this.bufferSource.getConsumer(spriteRenderTypes[HitEffectConfig.Texture.EFFECTS.ordinal()][this.blendMode.ordinal()]);

      for (int var23 = 0; var23 < 4; var23++) {
         double var24 = var23 != 0 && var23 != 3 ? 1.0 : -1.0;
         double var26 = var23 < 2 ? -1.0 : 1.0;
         var22.addVertex(
               this.poseMatrix,
               (float)(var1 - this.cameraPos.x + var7 * var24 + var13 * var26),
               (float)(var3 - this.cameraPos.y + var9 * var24 + var15 * var26),
               (float)(var5 - this.cameraPos.z + var11 * var24 + var17 * var26)
            )
            .setUv((float)((var21 + (var24 + 1.0) * 0.5) / 9.0), (float)((var26 + 1.0) * 0.5))
            .setColor(var19);
      }
   }

   private static int drawTransformedVertices(ParticleSystem.Style var0) {
      return switch (var0) {
         case GLOW -> 0;
         case CIRCLE -> 1;
         case SPARK -> 2;
         case STAR, FOUR_POINT_STAR -> 3;
         case TRIANGLE -> 4;
         case CRYSTAL, SHARD -> 5;
         case PETAL -> 6;
         case SOFT_GLOW -> 7;
         default -> 8;
      };
   }

   public void emitParticleQuad(
      double var1,
      double var3,
      double var5,
      double var7,
      double var9,
      int var11,
      ParticleSystem.Style var12,
      double var13,
      double var15,
      double var17,
      HitEffectConfig.Texture var19
   ) {
      int var20 = drawTransformedVertices(var12);
      double var21 = 1.0;
      if (var12 == ParticleSystem.Style.STREAK || var12 == ParticleSystem.Style.SPARK) {
         var9 = Math.atan2(
            var13 * this.cameraUp.x + var15 * this.cameraUp.y + var17 * this.cameraUp.z,
            var13 * this.cameraRight.x + var15 * this.cameraRight.y + var17 * this.cameraRight.z
         );
         var21 = var12 == ParticleSystem.Style.STREAK ? 3.0 : 1.6;
      }

      VertexConsumer var23 = this.bufferSource
         .getConsumer((this.softParticles ? softSpriteRenderTypes : spriteRenderTypes)[var19.ordinal()][this.blendMode.ordinal()]);
      double var24 = Math.cos(var9);
      double var26 = Math.sin(var9);

      for (int var28 = 0; var28 < 4; var28++) {
         double var29 = var28 != 0 && var28 != 3 ? 1.0 : -1.0;
         double var31 = var28 < 2 ? -1.0 : 1.0;
         double var33 = (var29 * var21 * var24 - var31 * var26) * var7;
         double var35 = (var29 * var21 * var26 + var31 * var24) * var7;
         var23.addVertex(
               this.poseMatrix,
               (float)(var1 - this.cameraPos.x + this.cameraRight.x * var33 + this.cameraUp.x * var35),
               (float)(var3 - this.cameraPos.y + this.cameraRight.y * var33 + this.cameraUp.y * var35),
               (float)(var5 - this.cameraPos.z + this.cameraRight.z * var33 + this.cameraUp.z * var35)
            )
            .setUv((float)((var20 + (var29 + 1.0) * 0.5) / 9.0), (float)((var31 + 1.0) * 0.5))
            .setColor(var11);
      }
   }

   public void setSoftParticles(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, int var17) {
      RibbonMesh.fillWavingVertices(cashScratch, var7, var9, var11, var13, var15);
      VertexConsumer var18 = this.bufferSource.getConsumer(cashRenderType);

      for (int var19 = 0; var19 < 18; var19++) {
         int var20 = RibbonMesh.shadeFace(cashScratch, var19, var17);

         for (int var21 = 0; var21 < 4; var21++) {
            int var22 = RibbonMesh.vertexIndex(var19, var21) * 3;
            var18.addVertex(
                  this.poseMatrix,
                  (float)(var1 - this.cameraPos.x + cashScratch[var22]),
                  (float)(var3 - this.cameraPos.y + cashScratch[var22 + 1]),
                  (float)(var5 - this.cameraPos.z + cashScratch[var22 + 2])
               )
               .setUv((float)RibbonMesh.textureU(var19, var21), (float)RibbonMesh.textureV(var19, var21))
               .setColor(var20);
         }
      }
   }

   public void drawCone(Vec3 var1, double var2, double var4, double var6, double var8, int var10, int var11, double var12) {
      int var14 = Math.clamp((int)Math.ceil(Math.abs(var8 - var6) * 12.0), 4, 96);

      for (int var15 = 0; var15 < var14; var15++) {
         double var16 = (double)var15 / var14;
         double var18 = (double)(var15 + 1) / var14;
         double var20 = var6 + (var8 - var6) * var16;
         double var22 = var6 + (var8 - var6) * var18;
         int var24 = ColorUtil.lerpColor(var10, var11, var16);
         int var25 = ColorUtil.lerpColor(var10, var11, var18);
         double var26 = var1.y + Math.sin(var20 * 2.0) * var12;
         double var28 = var1.y + Math.sin(var22 * 2.0) * var12;
         this.vertex(var1.x + Math.cos(var20) * var2, var26, var1.z + Math.sin(var20) * var2, var24);
         this.vertex(var1.x + Math.cos(var22) * var2, var28, var1.z + Math.sin(var22) * var2, var25);
         this.vertex(var1.x + Math.cos(var22) * (var2 + var4), var28, var1.z + Math.sin(var22) * (var2 + var4), ColorUtil.multiplyAlpha(var25, 0.15));
         this.vertex(var1.x + Math.cos(var20) * (var2 + var4), var26, var1.z + Math.sin(var20) * (var2 + var4), ColorUtil.multiplyAlpha(var24, 0.15));
      }
   }

   public void drawFlatRing(Vec3 var1, double var2, double var4, int var6) {
      for (int var7 = 0; var7 < 40; var7++) {
         double var8 = var7 * Math.PI / 20.0;
         double var10 = (var7 + 1) * Math.PI / 20.0;
         this.vertex(var1.x + Math.cos(var8) * var2, var1.y, var1.z + Math.sin(var8) * var2, var6);
         this.vertex(var1.x + Math.cos(var10) * var2, var1.y, var1.z + Math.sin(var10) * var2, var6);
         this.vertex(var1.x + Math.cos(var10) * (var2 + var4), var1.y, var1.z + Math.sin(var10) * (var2 + var4), var6);
         this.vertex(var1.x + Math.cos(var8) * (var2 + var4), var1.y, var1.z + Math.sin(var8) * (var2 + var4), var6);
      }
   }

   static {
      RenderPipeline var0 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", "cash"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
         .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
         .withCull(true)
         .build();
      cashRenderType = RenderType.create(
         "larp_cash",
         RenderSetup.builder(var0)
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/cash.png"))
            .sortOnUpload()
            .createRenderSetup()
      );

      for (HitEffectConfig.BlendMode var4 : HitEffectConfig.BlendMode.values()) {
         BlendFunction var5 = switch (var4) {
            case NORMAL -> BlendFunction.TRANSLUCENT;
            case ADDITIVE -> BlendFunction.LIGHTNING;
            case SCREEN -> BlendFunction.TRANSLUCENT_PREMULTIPLIED_ALPHA;
         };
         Builder var6 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
            .withLocation(Identifier.fromNamespaceAndPath("larp-client", "sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
            .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
            .withColorTargetState(new ColorTargetState(var5))
            .withCull(false);
         if (var4 == HitEffectConfig.BlendMode.SCREEN) {
            var6.withShaderDefine("PREMULTIPLIED");
         }

         RenderPipeline var7 = var6.build();
         SoftParticleRenderer.applySoftParticleDefine(var6);
         RenderPipeline var8 = var6.withLocation(Identifier.fromNamespaceAndPath("larp-client", "soft_sprites_" + var4.name().toLowerCase(Locale.ROOT)))
            .build();

         for (HitEffectConfig.Texture var12 : HitEffectConfig.Texture.values()) {
            spriteRenderTypes[var12.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_sprites_" + var12 + "_" + var4,
               RenderSetup.builder(var7)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var12.FILENAME))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         for (HitEffectConfig.Texture var17 : HitEffectConfig.Texture.values()) {
            softSpriteRenderTypes[var17.ordinal()][var4.ordinal()] = RenderType.create(
               "larp_soft_sprites_" + var17 + "_" + var4,
               RenderSetup.builder(var8)
                  .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "textures/particles/" + var17.FILENAME))
                  .sortOnUpload()
                  .createRenderSetup()
            );
         }

         geometryRenderTypes[var4.ordinal()] = RenderType.create(
            "larp_geometry_" + var4,
            RenderSetup.builder(
                  RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
                     .withLocation(Identifier.fromNamespaceAndPath("larp-client", "geometry_" + var4.name().toLowerCase(Locale.ROOT)))
                     .withColorTargetState(new ColorTargetState(var5))
                     .build()
               )
               .sortOnUpload()
               .createRenderSetup()
         );
      }

      cosTable = new double[65];
      sinTable = new double[65];

      for (int var13 = 0; var13 <= 64; var13++) {
         cosTable[var13] = Math.cos(var13 * Math.PI / 32.0);
         sinTable[var13] = Math.sin(var13 * Math.PI / 32.0);
      }
   }
}

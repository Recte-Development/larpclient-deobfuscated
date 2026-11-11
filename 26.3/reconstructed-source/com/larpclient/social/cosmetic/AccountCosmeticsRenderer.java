package com.larpclient.social.cosmetic;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.gui.misc.PlayerPreviewAnimator;
import com.larpclient.module.misc.SkinChangerModule;
import com.larpclient.module.render.CapesModule;
import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.HeadlessKorbloxModule;
import com.larpclient.module.render.WingsModule;
import com.larpclient.render.entity.ChinaHatRenderer;
import com.larpclient.render.entity.KorbloxLegRenderer;
import com.larpclient.render.entity.LegTrail;
import com.larpclient.render.model.AvatarPoseModel;
import com.larpclient.render.model.BitcoinMeshLoader;
import com.larpclient.render.model.ChinaHatHorns;
import com.larpclient.render.model.WingGeometry;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.ChinaHatFireParticles;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.social.pet.CryptoCoinPets;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PreviewSink;
import com.larpclient.util.render.HeadCoinLayout;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.pip.GuiSkinRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class AccountCosmeticsRenderer {
   private static final int fULL_BRIGHT = 15728880;
   static final float lUMINOUS_WING_SPAN = 1.85F;
   static final float lUMINOUS_WING_RISE = 0.15F;
   static final float dRAGON_WING_SPAN = 1.4F;
   static final float dRAGON_WING_RISE = 0.15F;
   private final WingGeometry.WingPreviewBuffer wingAnimator = new WingGeometry.WingPreviewBuffer();
   private DragonWings.WingModel dragonModel;
   private String lastLoggedState = "";
   private static int failedReloadId = -1;
   private static final ChinaHatFireParticles hornFire = new ChinaHatFireParticles(0L);

   static List<ChinaHatModule.HatVariant> activeHats() {
      ChinaHatModule var0 = ChinaHatModule.getInstance();
      return var0 == null ? List.of() : var0.getHatVariants();
   }

   static WingsModule.WingStyle activeWings() {
      WingsModule var0 = WingsModule.getInstance();
      return var0 == null ? null : var0.getWingStyle();
   }

   static boolean hidesHead() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.getInstance();
      return var0 != null && var0.isSelfHeadless() || activeHats().stream().anyMatch(ChinaHatModule.HatVariant::dominus);
   }

   static LegTrail activeKorblox() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.getInstance();
      return var0 == null ? null : var0.getSelfLegTrail();
   }

   static double maxHatRise(List<ChinaHatModule.HatVariant> var0, WingsModule.WingStyle var1) {
      double var2 = 0.0;

      for (ChinaHatModule.HatVariant var5 : var0) {
         var2 = Math.max(var2, var5.rise() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 0.15F : 0.15F) * var1.size());
      }

      return var2;
   }

   static double maxHatRadius(List<ChinaHatModule.HatVariant> var0, WingsModule.WingStyle var1) {
      double var2 = 0.0;

      for (ChinaHatModule.HatVariant var5 : var0) {
         var2 = Math.max(var2, var5.radius() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 1.4F : 1.85F) * var1.size());
      }

      return var2;
   }

   static PlayerSkin applySkinCosmetics(UUID var0, PlayerSkin var1) {
      PlayerSkin var2 = CosmeticOverrides.applySkin(var0, var1);
      SkinChangerModule var3 = SkinChangerModule.getInstance();
      if (var3 != null) {
         var2 = var3.overrideSkin(var2);
      }

      CapesModule var4 = CapesModule.getInstance();
      return var4 == null ? var2 : var4.applyCape(var2);
   }

   static void applyCapePhysics(AvatarRenderState var0, PlayerPreviewAnimator.Pose var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.capeLean = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.capeLean2 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.capeFlap = (float)Math.sin(var3 * 6.0F) * 32.0F * var4;
   }

   static Identifier capeTexture(PlayerSkin var0) {
      return var0.cape() != null && Minecraft.getInstance().options.isModelPartEnabled(PlayerModelPart.CAPE) ? var0.cape().texturePath() : null;
   }

   AccountCosmeticsRenderer.CosmeticsSnapshot buildCosmetics(
      Identifier var1, List<ChinaHatModule.HatVariant> var2, WingsModule.WingStyle var3, PlayerPreviewAnimator.Pose var4, PlayerCapeModel var5
   ) {
      return this.buildCosmeticsFull(var1, var2, var3, false, null, var4, var5);
   }

   AccountCosmeticsRenderer.CosmeticsSnapshot buildCosmeticsFull(
      Identifier var1,
      List<ChinaHatModule.HatVariant> var2,
      WingsModule.WingStyle var3,
      boolean var4,
      LegTrail var5,
      PlayerPreviewAnimator.Pose var6,
      PlayerCapeModel var7
   ) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.wingAnimator.beginWith(var8, false, var6.walkSpeed(), false, false, Vec3.ZERO);
      DragonWings.WingAngles var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.dragonModel == null) {
            this.dragonModel = DragonWings.WingModel.createLayer();
         }

         this.wingAnimator.end(DragonWings.flapSpeed(this.wingAnimator.ribbon(), 0.0F, 0.0F, 0.0F));
         var9 = DragonWings.computePose(this.wingAnimator.reset(), this.wingAnimator.ribbon(), 0.0F, 0.0F, 0.0F);
      }

      String var10 = var2.isEmpty() ? "none" : String.join("+", var2.stream().map(ChinaHatModule.HatVariant::label).toList());
      String var11 = (var1 == null ? "-" : var1)
         + "|"
         + var10
         + "|"
         + (var3 == null ? "-" : (var3.dragon() ? "dragon" : "luminous"))
         + "|"
         + var4
         + "|"
         + (var5 == null ? "-" : var5.legs());
      if (!var11.equals(this.lastLoggedState)) {
         this.lastLoggedState = var11;
         LarpLog.info(
            "Menu",
            "Account model cosmetics: cape={}, hats={}, wings={}, headless={}, korblox={}",
            var1 == null ? "none" : var1,
            var10,
            var3 == null ? "none" : (var3.dragon() ? "Ender Dragon" : "Luminous"),
            var4,
            var5 == null ? "none" : (var5.right() && var5.left() ? "both legs" : (var5.right() ? "right leg" : "left leg"))
         );
      }

      if (var1 == null && var2.isEmpty() && var3 == null && !var4 && var5 == null) {
         return null;
      }

      int var12 = var3 == null ? 0 : WingsModule.getVisibilitySettings(var3.first(), var3.second(), var8 / 20.0);
      int var13 = var3 == null ? 0 : WingsModule.blendColorOffset(var3.first(), var3.second(), var8 / 20.0);
      return new AccountCosmeticsRenderer.CosmeticsSnapshot(
         var7, (Identifier)var1, var2, var3, var12, var13, this.dragonModel, var9, var8, this.wingAnimator.ribbon(), var4, var5
      );
   }

   public static void submitGuiCosmetics(GuiSkinRenderState var0, Matrix4fc var1, PoseStack var2, SubmitNodeCollector var3) {
      if (var0.playerModel() instanceof AvatarPoseModel var4 && var4.applyRootTransform() != null) {
         int var13 = PostProcessing.reloadCount;
         if (failedReloadId != var13) {
            AccountCosmeticsRenderer.CosmeticsSnapshot var6 = var4.applyRootTransform();

            try {
               if (var6.cape() != null) {
                  var3.submitModel(var6.capeModel(), var4.getRenderState(), var2, RenderTypes.entitySolid(var6.cape()), 15728880, OverlayTexture.NO_OVERLAY, 0);
               }

               LegTrail var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     KorbloxLegRenderer.renderLeg(var2, var3, var4.getRightLeg(), var7, 15728880, OverlayTexture.NO_OVERLAY, false);
                  }

                  if (var7.left()) {
                     KorbloxLegRenderer.renderLeg(var2, var3, var4.getLeftLeg(), var7, 15728880, OverlayTexture.NO_OVERLAY, true);
                  }
               }

               WingsModule.WingStyle var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  PoseStack var9 = new PoseStack();
                  var9.last().set(var2.last());
                  var4.getBody().translateAndRotate(var9);
                  DragonWings.WingModel var10 = var6.dragon();
                  DragonWings.WingAngles var11 = var6.angles();
                  var3.order(1).submitCustomGeometry(var9, DragonWings.renderType(var8.dragonColor()), (var3x, var4x) -> {
                     PoseStack var5 = new PoseStack();
                     var5.last().set(var3x);
                     var10.render(var11);
                     var10.getRoot(var5, var4x, var8.size(), false, 15728880, var8.dragonColor());
                  });
               }

               AccountCosmeticsRenderer.CosmeticsBatch var14 = new AccountCosmeticsRenderer.CosmeticsBatch();
               collectHats(var14, var4, new Matrix4f(var1).invert().mul(var2.last().pose()), var6);
               PoseStack var15 = new PoseStack();
               var15.last().pose().set(var1);
               var14.submit(var3, var15, 2);
            } catch (RuntimeException var12) {
               failedReloadId = var13;
               LarpLog.error("Menu", "Account model cosmetics submission failed; body only until resource reload", var12);
            }
         }
      }
   }

   static void collectHats(AccountCosmeticsRenderer.CosmeticsBatch var0, AvatarPoseModel var1, Matrix4f var2, AccountCosmeticsRenderer.CosmeticsSnapshot var3) {
      if (!var3.hats().isEmpty()) {
         PoseStack var4 = new PoseStack();
         var4.last().pose().set(var2);
         var1.getHead().translateAndRotate(var4);
         var0.blend(HitEffectConfig.BlendMode.NORMAL);

         for (ChinaHatModule.HatVariant var6 : var3.hats()) {
            if (var6.dominus()) {
               var0.setDominusHat(var4.last().pose(), var6.radius(), var6.top());
            } else if (var6.horns() != null) {
               ChinaHatHorns.HornMesh var11 = ChinaHatHorns.loadMesh();
               if (var11 != null) {
                  float var12 = (float)var6.radius();
                  Vector3f var13 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
                  Vector3f var14 = var4.last()
                     .pose()
                     .transformPosition(0.0F, -0.5F, var11.centerZ() * var12, new Vector3f())
                     .add(new Vector3f(var13).mul(0.24F * var12));
                  hornFire.update(var3.age() / 20.0, var14.x, var14.y, var14.z, var13.x, var13.y, var13.z, var12 * var6.horns().size(), var6.horns().heat());
                  var0.setHorns(var4.last().pose(), var6.radius(), var6.horns(), hornFire);
               }
            } else if (var6.accessory() == null) {
               if (var6.kippah()) {
                  HatMesh.render(var0, var4.last().pose(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline(), 1.0);
               } else {
                  ChinaHatRenderer.render(var0, var4.last().pose(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline());
               }
            } else {
               List var7 = HeadCoinLayout.slotsFor(var6.accessory());
               int var8 = !var7.isEmpty() && CryptoCoinPets.tryLoad("Menu") ? 1 : 0;
               HeadCoinLayout.render(
                  var0, var4.last().pose(), var6.accessory(), var6.radius(), (var6.top() >>> 24) / 255.0, (boolean)var8, 1.0, var3.age() / 20.0
               );
               if (var8 != 0) {
                  for (HeadCoinLayout.CoinSlot var10 : var7) {
                     var0.addCoin(var10.coin(), HeadCoinLayout.buildSlots(var4.last().pose(), var10, var6.radius(), new Matrix4f()));
                  }
               }
            }
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         collectWings(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void collectWings(
      AccountCosmeticsRenderer.CosmeticsBatch var0,
      AvatarPoseModel var1,
      Matrix4f var2,
      AccountCosmeticsRenderer.CosmeticsSnapshot var3,
      WingsModule.WingStyle var4
   ) {
      PoseStack var5 = new PoseStack();
      var5.last().pose().set(var2);
      var1.getBody().translateAndRotate(var5);
      WingGeometry.WingPose var6 = WingGeometry.poseFromBlend(0.0F, 0.0F, 0.0F, 0.0F);
      WingGeometry.AnimatedWing var7 = WingGeometry.animatePoints(var3.age(), var3.walking(), 0.0F, 0.0F);
      WingGeometry.WingInstance[] var8 = new WingGeometry.WingInstance[2];
      byte var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.last().pose()).mul(WingGeometry.chestWingMatrix(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = WingGeometry.buildWing(Vec3.ZERO, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (WingGeometry.RenderMode var12 : WingGeometry.RenderMode.values()) {
         for (WingGeometry.WingInstance var16 : var8) {
            WingGeometry.renderWing(var0, var16, var12);
         }
      }
   }

   static final class CosmeticsBatch implements PreviewSink {
      private final List<AccountCosmeticsRenderer.CosmeticsBatch.Batch> batches = new ArrayList<>();
      private final List<AccountCosmeticsRenderer.CosmeticsBatch.CoinInstance> coins = new ArrayList<>();
      private AccountCosmeticsRenderer.CosmeticsBatch.HornsHat horns;
      private AccountCosmeticsRenderer.CosmeticsBatch.DominusHat dominusHat;
      private final Vector3f scratchPos = new Vector3f();
      private HitEffectConfig.BlendMode blend = HitEffectConfig.BlendMode.NORMAL;
      private HitEffectConfig.BlendMode batchBlend;
      private boolean solid;
      private boolean batchSolid;
      private float[] points = new float[768];
      private int[] colors = new int[256];
      private int vertexCount;

      @Override
      public void blend(HitEffectConfig.BlendMode var1) {
         this.blend = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.solid = var1;
      }

      @Override
      public Vector3f viewer(Matrix4f var1, Vector3f var2) {
         new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 1000.0F));
         if (var2.z > 0.0F) {
            new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, -1000.0F));
         }

         return var2;
      }

      void addCoin(int var1, Matrix4f var2) {
         this.coins.add(new AccountCosmeticsRenderer.CosmeticsBatch.CoinInstance(var1, var2));
      }

      void setHorns(Matrix4f var1, double var2, ChinaHatModule.HornStyle var4, ChinaHatFireParticles var5) {
         this.horns = new AccountCosmeticsRenderer.CosmeticsBatch.HornsHat(new Matrix4f(var1), var2, var4, var5);
      }

      void setDominusHat(Matrix4f var1, double var2, int var4) {
         this.dominusHat = new AccountCosmeticsRenderer.CosmeticsBatch.DominusHat(new Matrix4f(var1), var2, var4);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.scratchPos);
         this.vertex(this.scratchPos.x, this.scratchPos.y, this.scratchPos.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.batchBlend != this.blend || this.batchSolid != this.solid) {
            this.flushBatch();
            this.batchBlend = this.blend;
            this.batchSolid = this.solid;
         }

         if (this.vertexCount == this.colors.length) {
            this.colors = Arrays.copyOf(this.colors, this.vertexCount * 2);
            this.points = Arrays.copyOf(this.points, this.vertexCount * 6);
         }

         this.points[this.vertexCount * 3] = (float)var1;
         this.points[this.vertexCount * 3 + 1] = (float)var3;
         this.points[this.vertexCount * 3 + 2] = (float)var5;
         this.colors[this.vertexCount++] = var7;
      }

      @Override
      public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
         double var7 = var2.x - var1.x;
         double var9 = var2.y - var1.y;
         double var11 = var2.z - var1.z;
         if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
            double var13 = Math.hypot(var7, var9);
            double var15 = var13 < 1.0E-8 ? 0.5 : var9 / var13 / 2.0;
            double var17 = var13 < 1.0E-8 ? 0.0 : -var7 / var13 / 2.0;
            this.vertex(var1.x + var15 * var3, var1.y + var17 * var3, var1.z, var5);
            this.vertex(var2.x + var15 * var3, var2.y + var17 * var3, var2.z, var6);
            this.vertex(var2.x - var15 * var3, var2.y - var17 * var3, var2.z, var6);
            this.vertex(var1.x - var15 * var3, var1.y - var17 * var3, var1.z, var5);
         }
      }

      List<Vector3f> collectPoints() {
         this.flushBatch();
         ArrayList var1 = new ArrayList();

         for (AccountCosmeticsRenderer.CosmeticsBatch.Batch var3 : this.batches) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void flushBatch() {
         if (this.batchBlend != null && this.vertexCount > 0) {
            this.batches
               .add(
                  new AccountCosmeticsRenderer.CosmeticsBatch.Batch(
                     this.batchBlend, this.batchSolid, Arrays.copyOf(this.points, this.vertexCount * 3), Arrays.copyOf(this.colors, this.vertexCount)
                  )
               );
         }

         this.vertexCount = 0;
      }

      void submit(SubmitNodeCollector var1, PoseStack var2, int var3) {
         this.flushBatch();
         AccountCosmeticsRenderer.CosmeticsBatch.HornsHat var4 = this.horns;
         this.horns = null;
         ChinaHatHorns.HornMesh var5 = ChinaHatHorns.loadMesh();
         int var6 = var4 != null && var5 != null && ChinaHatHorns.updateTexture(var4.look().horn(), var4.look().fire(), var4.look().core()) ? 1 : 0;
         if (var6 != 0) {
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  ChinaHatHorns.getRenderType(),
                  (var2x, var3x) -> ChinaHatHorns.addTriangles(var3x, new Matrix4f(var2x.pose()).mul(var4.head()), var5, var4.scale())
               );
         }

         AccountCosmeticsRenderer.CosmeticsBatch.DominusHat var7 = this.dominusHat;
         this.dominusHat = null;
         ChinaHatHorns.HornMesh var8 = DominusHatMesh.loadMesh();
         if (var7 != null && var8 != null) {
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  DominusHatMesh.getRenderType(),
                  (var2x, var3x) -> DominusHatMesh.renderMesh(var3x, new Matrix4f(var2x.pose()).mul(var7.head()), var8, var7.scale(), var7.tint())
               );
         }

         for (boolean var12 : new boolean[]{true, false}) {
            for (AccountCosmeticsRenderer.CosmeticsBatch.Batch var14 : this.batches) {
               if (var14.solid == var12) {
                  var1.order(var3++)
                     .submitCustomGeometry(
                        var2,
                        var14.solid ? EffectsRenderer.drawChinaHat() : EffectsRenderer.geometryRenderType(var14.blend),
                        (var1x, var2x) -> {
                           for (int var3x = 0; var3x < var14.colors.length; var3x++) {
                              var2x.addVertex(var1x, var14.points[var3x * 3], var14.points[var3x * 3 + 1], var14.points[var3x * 3 + 2])
                                 .setColor(var14.colors[var3x]);
                           }
                        }
                     );
               }
            }

            if (var12) {
               BitcoinMeshLoader.Anonymous var18 = CryptoCoinPets.getMesh();
               if (var18 != null) {
                  for (AccountCosmeticsRenderer.CosmeticsBatch.CoinInstance var15 : this.coins) {
                     CryptoCoinPets.Coin var16 = CryptoCoinPets.cOINS.get(var15.coin());
                     var1.order(var3++)
                        .submitCustomGeometry(
                           var2,
                           var16.type(),
                           (var2x, var3x) -> {
                              Matrix4f var4x = new Matrix4f(var2x.pose()).mul(var15.transform());
                              float[] var5x = var18.vertices();
                              int[] var6x = var18.colors();

                              for (int var7x = 0; var7x < var6x.length; var7x++) {
                                 var3x.addVertex(var4x, var5x[var7x * 5], var5x[var7x * 5 + 1], var5x[var7x * 5 + 2])
                                    .setUv(var5x[var7x * 5 + 3], var5x[var7x * 5 + 4])
                                    .setColor(var6x[var7x]);
                              }
                           }
                        );
                  }
               }
            }
         }

         this.batches.clear();
         this.coins.clear();
         if (var6 != 0) {
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  ChinaHatFireParticles.getRenderType(),
                  (var1x, var2x) -> var4.fire()
                     .update(
                        (var2xx, var4x, var6x, var8x, var10, var12x, var14x) -> {
                           float var15x = (float)Math.cos(var12x);
                           float var16x = (float)Math.sin(var12x);
                           float var17 = (float)var8x;
                           float var18x = (float)(var8x * var10);
                           float var19 = (float)var2xx;
                           float var20 = (float)var4x;
                           float var21 = (float)var6x;

                           for (int var22 = 0; var22 < 4; var22++) {
                              float var23 = var22 != 0 && var22 != 3 ? 1.0F : -1.0F;
                              float var24 = var22 < 2 ? -1.0F : 1.0F;
                              var2x.addVertex(
                                    var1x,
                                    var19 + var23 * var17 * var15x - var24 * var18x * var16x,
                                    var20 + var23 * var17 * var16x + var24 * var18x * var15x,
                                    var21
                                 )
                                 .setUv((var23 + 1.0F) * 0.5F, (var24 + 1.0F) * 0.5F)
                                 .setColor(var14x);
                           }
                        },
                        var4.look().fire(),
                        var4.look().core(),
                        1.0
                     )
               );
         }
      }

      private record Batch(HitEffectConfig.BlendMode blend, boolean solid, float[] points, int[] colors) {
      }

      private record CoinInstance(int coin, Matrix4f transform) {
      }

      private record DominusHat(Matrix4f head, double scale, int tint) {
      }

      private record HornsHat(Matrix4f head, double scale, ChinaHatModule.HornStyle look, ChinaHatFireParticles fire) {
      }
   }

   record CosmeticsSnapshot(
      PlayerCapeModel capeModel,
      Identifier cape,
      List<ChinaHatModule.HatVariant> hats,
      WingsModule.WingStyle wings,
      int primary,
      int secondary,
      DragonWings.WingModel dragon,
      DragonWings.WingAngles angles,
      float age,
      float walking,
      boolean headless,
      LegTrail korblox
   ) {
   }
}

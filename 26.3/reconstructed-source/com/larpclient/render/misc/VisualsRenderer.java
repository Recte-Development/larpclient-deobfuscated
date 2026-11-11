package com.larpclient.render.misc;

import com.larpclient.LarpClient;
import com.larpclient.config.HitEffectConfig;
import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.clickgui.ClickGuiCloseEffect;
import com.larpclient.gui.misc.MenuBackground2;
import com.larpclient.gui.theme.AuroraBackdrop;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.HudLayout;
import com.larpclient.module.combat.TargetAuraModule;
import com.larpclient.module.misc.CryptoPetsModule;
import com.larpclient.module.misc.PetsModule;
import com.larpclient.module.misc.WorldLyricsModule;
import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.module.render.AmbientParticlesModule;
import com.larpclient.module.render.AuraRenderer;
import com.larpclient.module.render.BlockOverlayModule;
import com.larpclient.module.render.ChamsModule;
import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.CrystalChamsModule;
import com.larpclient.module.render.DamageMarkersModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.module.render.LineGlyphsModule;
import com.larpclient.module.render.PearlParticlesModule;
import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.module.render.SnowGroundModule;
import com.larpclient.module.render.WingsModule;
import com.larpclient.render.entity.BodyTrailEngine;
import com.larpclient.render.entity.EchoHandRenderer;
import com.larpclient.render.entity.EntityHaloRenderer;
import com.larpclient.render.entity.FlameHandsRenderer;
import com.larpclient.render.entity.GhostEcho;
import com.larpclient.render.entity.HandChamsRenderer;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.overlay.ExactTextRenderer;
import com.larpclient.render.overlay.LiquidGlass;
import com.larpclient.render.overlay.LocalizedRefraction;
import com.larpclient.render.overlay.MotionBlurRenderer;
import com.larpclient.render.overlay.OverlayTextures;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.SceneCapture;
import com.larpclient.render.particle.CometTrailRenderer;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.particle.SoftParticleRenderer;
import com.larpclient.render.sky.DistanceFogRenderer;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.render.world.TrailRenderer;
import com.larpclient.render.world.WorldColorRenderer;
import com.larpclient.social.emote.EmoteProps;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.misc.SafetyLevel;
import com.larpclient.util.world.BlockRaycast;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents.ClientStopping;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents.AfterBlockOutlineExtraction;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.AfterOpaqueTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeBlockOutline;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeTranslucentTerrain;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.EndMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.entity.LivingEntity;

public final class VisualsRenderer {
   public static final ParticleSystem particles = new ParticleSystem();
   public static final TrailRenderer trails = new TrailRenderer();
   public static final BodyTrailEngine bodyTrails = new BodyTrailEngine();
   public static final GhostEcho ghostEcho = new GhostEcho();
   private static List<PlacementHighlight> placementHighlights = List.of();
   private static EffectBatch batch;
   private static ClientLevel lastLevel;
   private static boolean renderFailed;
   private static int lastInitEpoch = -1;
   private static LivingEntity lastTarget;
   private static double lastTargetTime;
   private static LevelRenderState levelState;
   private static float partialTick;
   private static EntityRenderState selfRenderState;
   private static ModelVisualRenderer.Material selfMaterial;
   private static double selfStrength;
   private static double refractionStrength;
   private static int selfColor;
   private static boolean selfBloomEnabled;
   private static boolean drewBeforeTranslucent;
   private static Boolean lastLoggedBeforeTranslucent;
   private static final Set<String> isolatedStages = new HashSet<>();
   private static int isolationEpoch = -1;

   static void runIsolated(String var0, Runnable var1) {
      if (isolationEpoch != PostProcessing.reloadCount) {
         isolatedStages.clear();
         isolationEpoch = PostProcessing.reloadCount;
      }

      if (!isolatedStages.contains(var0)) {
         try {
            var1.run();
         } catch (RuntimeException var4) {
            isolatedStages.add(var0);
            RenderEffects.clear(var0);
            if (var0.equals("Chams")) {
               ChamsModule var3 = ChamsModule.getInstance();
               if (var3 != null) {
                  var3.reset();
               }
            }

            if (var0.equals("CrystalChams")) {
               CrystalChamsModule var5 = CrystalChamsModule.getInstance();
               if (var5 != null) {
                  var5.clearFrame();
               }
            }

            if (var0.equals("Pets")) {
               PetsModule var6 = PetsModule.getInstance();
               if (var6 != null) {
                  var6.reset();
               }
            }

            if (var0.equals("CryptoPets")) {
               CryptoPetsModule var7 = CryptoPetsModule.getInstance();
               if (var7 != null) {
                  var7.reset();
               }
            }

            if (var0.equals("EmoteProps")) {
               EmoteProps.clear();
            }

            if (var0.equals("PlayerModel")) {
               PlayerModelModule var8 = PlayerModelModule.getInstance();
               if (var8 != null) {
                  var8.onResourceReload();
               }
            }

            if (var0.equals("SnowGround")) {
               SnowGroundModule var9 = SnowGroundModule.getQualityLevel();
               if (var9 != null) {
                  var9.suppressUntilReload();
               }
            }

            if (var0.equals("KillFX")) {
               KillFXModule var10 = KillFXModule.getInstance();
               if (var10 != null) {
                  var10.reset();
               }
            }

            if (batch != null) {
               batch.flushWith();
            }

            LarpLog.error("Render", "Cosmetic stage=" + var0 + "; isolated until resource reload", var4);
         }
      }
   }

   static boolean isStageIsolated(String var0) {
      return !isStageActive(var0);
   }

   public static boolean isStageActive(String var0) {
      return isolationEpoch != PostProcessing.reloadCount || !isolatedStages.contains(var0);
   }

   private static void runGuarded(String var0, EffectsRenderer var1, Runnable var2) {
      runIsolated(var0, () -> {
         try {
            var2.run();
         } finally {
            var1.setSoftParticles(false);
            var1.blend(HitEffectConfig.BlendMode.NORMAL);
         }
      });
   }

   private static void extractLevel(LevelExtractionContext var0) {
      levelState = var0.levelState();
      drewBeforeTranslucent = (boolean)0;

      for (PlacementHighlight var2 : placementHighlights) {
         runIsolated(var2.getLabel(), () -> var2.extract(var0));
      }

      partialTick = var0.deltaTracker().getGameTimeDeltaPartialTick(false);
      CameraRenderState var17 = levelState.cameraRenderState;
      LiquidGlass.updateFrame(var17);
      RenderEffects.prepare(partialTick, var17);
      PlayerModelModule var18 = PlayerModelModule.getInstance();
      if (var18 != null) {
         runIsolated("PlayerModel", () -> var18.updatePose(partialTick, var17.pos));
      }

      JumpFXModule var3 = JumpFXModule.getInstance();
      if (var3 != null) {
         runIsolated("JumpFX", var3::updateWaveParams);
      }

      KillFXModule var4 = KillFXModule.getInstance();
      if (var4 != null) {
         runIsolated("KillFX", () -> var4.prepareFrame(var17));
      }

      CrystalChamsModule var5 = CrystalChamsModule.getInstance();
      if (var5 != null) {
         runIsolated("CrystalChams", () -> var5.updateFrame(partialTick));
      }

      PetsModule var6 = PetsModule.getInstance();
      if (var6 != null) {
         runIsolated("Pets", () -> var6.updateFrame(partialTick, var17));
      }

      CryptoPetsModule var7 = CryptoPetsModule.getInstance();
      if (var7 != null) {
         runIsolated("CryptoPets", () -> var7.updateFrame(partialTick, var17));
      }

      runIsolated("EmoteProps", () -> EmoteProps.prepare(partialTick));
      SnowGroundModule var8 = SnowGroundModule.getQualityLevel();
      if (var8 != null) {
         runIsolated("SnowGround", () -> var8.prepareRender(partialTick, var17));
      }

      runIsolated("Trails", () -> trails.buildRibbons(var17.pos, partialTick));
      runIsolated("Body Trails", () -> bodyTrails.buildGeometry(var17.pos, partialTick));
      runIsolated("GhostEcho", () -> ghostEcho.update(var17.pos));
      runIsolated("Comets", () -> CometTrailRenderer.update(partialTick));
      runIsolated("HitZone", AuraRenderer::updateTargetOverlay);
      TargetAuraModule var9 = TargetAuraModule.getInstance();
      if (var9 != null) {
         runIsolated("TargetAura", () -> var9.updateEffects(partialTick));
      }

      runIsolated("Particles", () -> particles.prepare(var17.pos, partialTick, var17.cullFrustum));
      AmbientParticlesModule var10 = AmbientParticlesModule.getInstance();
      if (var10 != null && var10.isOperational()) {
         runIsolated("Ambient Particles", () -> var10.getEffect().prepareRender(var17.pos, partialTick, var17.cullFrustum));
      }

      ChamsModule var11 = ChamsModule.getInstance();
      if (var11 != null) {
         runIsolated("Chams", () -> var11.extractEntities(partialTick));
      }

      ChinaHatModule var12 = ChinaHatModule.getInstance();
      if (var12 != null) {
         runIsolated("ChinaHat", () -> var12.collectRenderEntries(partialTick));
      }

      WingsModule var13 = WingsModule.getInstance();
      if (var13 != null) {
         runIsolated("Wings", () -> var13.updateWings(partialTick));
      }

      DamageMarkersModule var14 = DamageMarkersModule.getInstance();
      if (var14 != null) {
         runIsolated("DamageMarkers", () -> var14.submitLabels(var17));
      }

      WorldLyricsModule var15 = WorldLyricsModule.getInstance();
      if (var15 != null) {
         runIsolated("WorldLyrics", () -> var15.updateLabels(var17));
      }

      selfRenderState = null;
      SelfVisualsModule var16 = SelfVisualsModule.getPrimaryColor();
      selfMaterial = null;
      selfBloomEnabled = (boolean)0;
      refractionStrength = 0.0;
      if (var16 != null && var16.isActiveForSelf() && var16.shouldRenderOutline()) {
         runIsolated(
            "SelfVisuals",
            () -> {
               selfRenderState = ModelVisualRenderer.extractEntity(Minecraft.getInstance().player, partialTick);
               selfMaterial = !var16.rendersModel() ? null : (var16.getMaterial() == null ? ModelVisualRenderer.Material.TINT : var16.getMaterial());
               selfStrength = var16.getPrimaryColor();
               selfColor = Theme.accent;
               refractionStrength = var16.isLiquidGlass() && var16.enhancedRefraction.getValue() ? var16.refractionStrength.getValue() : 0.0;
               selfBloomEnabled = (boolean)(!var16.rendersModel()
                     || !var16.modelBloom.getValue()
                     || var16.isLiquidGlass() && SafetyLevel.current == PostProcessing.Quality.OFF
                  ? 0
                  : 1);
            }
         );
      }
   }

   public static boolean isRenderingEnabled() {
      return !renderFailed;
   }

   public static boolean isSelfRenderState(EntityRenderState var0) {
      return selfRenderState != null && selfMaterial != null && levelState != null && levelState.entityRenderStates.contains(var0);
   }

   public static void register() {
      RenderEffects.setModules(LarpClient.getInstance().getModuleManager().getModules());
      placementHighlights = LarpClient.getInstance()
         .getModuleManager()
         .getModules()
         .stream()
         .filter(var0 -> var0 instanceof PlacementHighlight.PlacementHighlightHolder)
         .map(var0 -> ((PlacementHighlight.PlacementHighlightHolder)var0).placementHighlight())
         .toList();
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
         JumpFXModule var1 = JumpFXModule.getInstance();
         if (var1 != null && !var0.isPaused()) {
            var1.onTickUpdate();
         }

         if (!var0.isPaused()) {
            RenderEffects.tick(var0);
         }
      });
      ClientTickEvents.START_CLIENT_TICK.register((StartTick)var0 -> {
         FlameHandsRenderer.syncState();
         if (var0.level != lastLevel) {
            RenderEffects.clearAll();
            runIsolated("Scene resource cleanup", SceneCapture::releaseAll);
            runIsolated("Refraction cleanup", LocalizedRefraction::release);
            EchoHandRenderer.release();
            HandChamsRenderer.reset();
            FlameHandsRenderer.reset();
            CrystalChamsModule var1 = CrystalChamsModule.getInstance();
            if (var1 != null) {
               var1.clearFrame();
            }

            PetsModule var2 = PetsModule.getInstance();
            if (var2 != null) {
               var2.reset();
            }

            CryptoPetsModule var3 = CryptoPetsModule.getInstance();
            if (var3 != null) {
               var3.reset();
            }

            DamageMarkersModule var4 = DamageMarkersModule.getInstance();
            if (var4 != null) {
               var4.reset();
            }

            WorldLyricsModule var5 = WorldLyricsModule.getInstance();
            if (var5 != null) {
               var5.reset();
            }

            KillFXModule var6 = KillFXModule.getInstance();
            if (var6 != null) {
               var6.reset();
            }

            placementHighlights.forEach(PlacementHighlight::clear);
            lastLevel = var0.level;
            levelState = null;
            selfRenderState = null;
            particles.clear();
            trails.clear();
            bodyTrails.clear();
            ghostEcho.clear();
            CometTrailRenderer.reset();
            AmbientParticlesModule var7 = AmbientParticlesModule.getInstance();
            if (var7 != null) {
               var7.resetEffect();
            }

            JumpFXModule var8 = JumpFXModule.getInstance();
            if (var8 != null) {
               var8.reset();
            }

            BlockOverlayModule var9 = BlockOverlayModule.getInstance();
            if (var9 != null) {
               var9.reset();
            }

            WorldColorRenderer.reset();
            MotionBlurRenderer.releaseResources();
            DistanceFogRenderer.releaseResources();
            EntityHaloRenderer.close();
            SoftParticleRenderer.releaseBuffer();
            ModelVisualRenderer.clearCaches();
            ChamsModule var10 = ChamsModule.getInstance();
            if (var10 != null) {
               var10.reset();
            }

            PlayerModelModule var11 = PlayerModelModule.getInstance();
            if (var11 != null) {
               var11.reset();
            }

            lastTarget = null;
            HudLayout.addBounds();
            HudLayout.reset();
            LarpLog.debug("Render", "World changed; transient effects cleared");
         }

         if (!var0.isPaused()) {
            TickTimer.tick();
            ghostEcho.tick();
            particles.tickWithCollision(1.0, (var1x, var2x) -> BlockRaycast.raycastBlock(var0.level, var1x, var2x));
            trails.tick();
            bodyTrails.tick();
            KillFXModule var12 = KillFXModule.getInstance();
            if (var12 != null) {
               runIsolated("KillFX", () -> var12.onTick());
            }
         }
      });
      LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register((AfterBlockOutlineExtraction)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.getInstance();
         if (var2 != null) {
            var2.extract(var0, var1);
         }
      });
      LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register((BeforeBlockOutline)(var0, var1) -> {
         BlockOverlayModule var2 = BlockOverlayModule.getInstance();
         return var2 == null || StreamProofManager.submitFrame() || !var2.shouldReplaceOutline(var1.pos(), var0.levelState());
      });
      LevelExtractionEvents.END_EXTRACTION.register(VisualsRenderer::extractLevel);
      LevelRenderEvents.AFTER_OPAQUE_TERRAIN.register((AfterOpaqueTerrain)var0 -> StreamProofManager.isOverlayScreen());
      LevelRenderEvents.BEFORE_TRANSLUCENT_TERRAIN
         .register((BeforeTranslucentTerrain)var0 -> StreamProofManager.isOverlayActive(() -> renderBeforeTranslucent(var0)));
      LevelRenderEvents.END_MAIN.register((EndMain)var0 -> StreamProofManager.clearLayers(var0, () -> register(var0)));
      DistanceFogRenderer.register();
      ClientLifecycleEvents.CLIENT_STOPPING.register((ClientStopping)var0 -> {
         StreamProofManager.shutdown();
         RenderEffects.clearAll();
         runIsolated("Scene resource cleanup", SceneCapture::releaseAll);
         runIsolated("Refraction cleanup", LocalizedRefraction::release);
         EchoHandRenderer.release();
         FlameHandsRenderer.reset();
         DamageMarkersModule var1 = DamageMarkersModule.getInstance();
         if (var1 != null) {
            var1.reset();
         }

         WorldLyricsModule var2 = WorldLyricsModule.getInstance();
         if (var2 != null) {
            var2.stop();
         }

         KillFXModule var3 = KillFXModule.getInstance();
         if (var3 != null) {
            var3.reset();
         }

         placementHighlights.forEach(PlacementHighlight::clear);
         PostProcessing.releaseResources();
         SoftParticleRenderer.releaseBuffer();
         DistanceFogRenderer.releaseResources();
         EntityHaloRenderer.close();
         ChamsModule var4 = ChamsModule.getInstance();
         if (var4 != null) {
            var4.reset();
         }

         ghostEcho.clear();
         CometTrailRenderer.reset();
         AmbientParticlesModule var5 = AmbientParticlesModule.getInstance();
         if (var5 != null) {
            var5.resetEffect();
         }

         JumpFXModule var6 = JumpFXModule.getInstance();
         if (var6 != null) {
            var6.reset();
         }

         BlockOverlayModule var7 = BlockOverlayModule.getInstance();
         if (var7 != null) {
            var7.reset();
         }

         LineGlyphsModule var8 = LineGlyphsModule.getInstance();
         if (var8 != null) {
            var8.reset();
         }

         PearlParticlesModule var9 = PearlParticlesModule.getInstance();
         if (var9 != null) {
            var9.reset();
         }

         SkyShaderModule var10 = SkyShaderModule.getInstance();
         if (var10 != null) {
            var10.reset();
         }

         SnowGroundModule var11 = SnowGroundModule.getQualityLevel();
         if (var11 != null) {
            var11.reset();
         }

         MenuBackground2.releaseResources();
         AuroraBackdrop.release();
         ExactTextRenderer.release();
         ClickGuiCloseEffect.releaseSnapshot();
         if (batch != null) {
            batch.close();
         }
      });
      LarpLog.info("Render", "Shared effects initialized; depth-tested ribbons, particle pool, bloom and blur");
   }

   public static void onEntityHit(LivingEntity var0) {
      setLastTarget(var0);
      HitFXModule var1 = HitFXModule.getInstance();
      if (var1 != null && var1.isOperational()) {
         var1.spawnAtEntity(var0);
      }
   }

   public static void setLastTarget(LivingEntity var0) {
      lastTarget = var0;
      lastTargetTime = TickTimer.getTickTime();
   }

   public static LivingEntity getRecentTarget() {
      return lastTarget != null
            && lastTarget.isAlive()
            && lastTarget.level() == Minecraft.getInstance().level
            && TickTimer.getTickTime() - lastTargetTime < 3.0
         ? lastTarget
         : null;
   }

   private static void initShaders() {
      SceneCapture.beginFrame();
      LocalizedRefraction.beginFrame();
      if (lastInitEpoch != PostProcessing.reloadCount) {
         lastInitEpoch = PostProcessing.reloadCount;
         ClientPipelines.validatePipeline(ClientPipelines.wORLD);
         EffectsRenderer.registerPipelines();
      }
   }

   private static void renderBeforeTranslucent(LevelRenderContext var0) {
      if (lastInitEpoch != PostProcessing.reloadCount) {
         renderFailed = (boolean)0;
      }

      if (!renderFailed && var0.poseStack() != null && var0.levelState() == levelState) {
         if (Minecraft.getInstance().level != null) {
            if (batch == null) {
               batch = new EffectBatch();
            }

            GpuTextureView var1 = OverlayTextures.colorTexture;
            GpuTextureView var2 = OverlayTextures.depthTexture;
            String var3 = "shader initialization before translucent terrain";

            try {
               initShaders();
               drewBeforeTranslucent = (boolean)1;
               EntityRenderState var4 = selfRenderState;
               var3 = "SelfVisuals outline mask / player material before translucent terrain";
               isRenderingEnabled(var0, var4);
               var3 = "Comets before translucent terrain";
               EffectsRenderer var5 = new EffectsRenderer(var0.poseStack(), batch, var0.levelState().cameraRenderState);
               runGuarded("Comets", var5, () -> {
                  CometTrailRenderer.render(var5, false);
                  batch.flush();
               });
               SceneCapture.invalidateColor();
               SceneCapture.invalidateDepth();
               if (var4 != null && selfBloomEnabled || CometTrailRenderer.isGlowActive()) {
                  SceneCapture.capturePreTranslucentDepth();
               }
            } catch (RuntimeException var9) {
               renderFailed = (boolean)1;
               LarpLog.error("Render", "VisualRuntime stage=" + var3 + "; output attachments restored; retry on resource reload", var9);
            } finally {
               OverlayTextures.colorTexture = var1;
               OverlayTextures.depthTexture = var2;
            }
         }
      }
   }

   private static void isRenderingEnabled(LevelRenderContext var0, EntityRenderState var1) {
      if (var1 != null && !Boolean.valueOf(drewBeforeTranslucent).equals(lastLoggedBeforeTranslucent)) {
         lastLoggedBeforeTranslucent = drewBeforeTranslucent;
         if (drewBeforeTranslucent) {
            LarpLog.info("SelfVisuals", "Drawing before translucent terrain; glass, water and ice blend over the player");
         } else {
            LarpLog.warn("SelfVisuals", "Hook before translucent terrain did not run; drawing at END_MAIN, where glass hides the player");
         }
      }

      runIsolated("SelfVisuals outline", () -> EntityHaloRenderer.render(var0.poseStack(), var1));
      if (var1 != null && selfMaterial == ModelVisualRenderer.Material.LIQUID_GLASS && refractionStrength > 0.0 && LiquidGlass.isMaterialAvailable()) {
         runIsolated("Liquid Glass refraction", () -> LocalizedRefraction.composite("Liquid Glass", () -> {
            try {
               ModelVisualRenderer.renderEntityLayers(var1, var0.poseStack(), batch, ModelVisualRenderer.Material.GLASS_MASK, -1, selfStrength, true, false);
               batch.flush();
            } finally {
               batch.flushWith();
            }
         }, refractionStrength));
      }

      if (var1 != null && selfMaterial != null && (selfMaterial != ModelVisualRenderer.Material.LIQUID_GLASS || LiquidGlass.isMaterialAvailable())) {
         runIsolated("SelfVisuals", () -> {
            long var2 = PerfProfiler.startTimer();
            ModelVisualRenderer.renderEntity(var1, var0.poseStack(), batch, selfMaterial, selfColor, selfStrength, true);
            batch.flush();
            SceneCapture.invalidateColor();
            SceneCapture.invalidateDepth();
            PerfProfiler.endTimer("SelfVisuals body material", var2);
         });
      }
   }

   private static void register(LevelRenderContext var0) {
      if (lastInitEpoch != PostProcessing.reloadCount) {
         renderFailed = (boolean)0;
      }

      if (!renderFailed && var0.poseStack() != null && var0.levelState() == levelState) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null) {
            if (batch == null) {
               batch = new EffectBatch();
            }

            GpuTextureView var2 = OverlayTextures.colorTexture;
            GpuTextureView var3 = OverlayTextures.depthTexture;
            String var4 = "shader initialization";

            try {
               if (drewBeforeTranslucent) {
                  SceneCapture.invalidateColor();
                  SceneCapture.invalidateDepth();
               } else {
                  initShaders();
               }

               float var5 = partialTick;
               EffectsRenderer var6 = new EffectsRenderer(var0.poseStack(), batch, var0.levelState().cameraRenderState);
               var4 = "Wings Ender Dragon opaque pass";
               WingsModule var7 = WingsModule.getInstance();
               if (var7 != null && var7.isOperational() && var7.isDragonStyle()) {
                  runIsolated("Wings", () -> {
                     if (var7.renderWings(var0.poseStack(), batch)) {
                        batch.flush();
                        SceneCapture.invalidateColor();
                        SceneCapture.invalidateDepth();
                     }
                  });
               }

               AmbientParticlesModule var8 = AmbientParticlesModule.getInstance();
               int var9 = var8 != null && var8.isOperational() && !isolatedStages.contains("Ambient Particles") ? 1 : 0;
               SoftParticleRenderer.prepareFrame(
                  !isolatedStages.contains("Particles") && particles.hasDepthSortedVisible()
                     || var9 != 0 && var8.getEffect().hasParticles()
                     || RenderEffects.hasSoft()
               );
               var4 = "Particles/Trails/Body Trails/TargetAura/GhostEcho/Comets/HitZone/BlockOverlay base geometry";
               renderGeometry(var0, var6, var5, false);
               PlayerModelModule var10 = PlayerModelModule.getInstance();
               if (var10 != null) {
                  runIsolated("PlayerModel", () -> var10.renderModel(var0.poseStack(), batch));
               }

               OrderDropperModule var11 = OrderDropperModule.getInstance();
               if (var11 != null) {
                  runIsolated("Order Dropper", () -> var11.fillShulker(var0.poseStack(), batch, var0.levelState().cameraRenderState.pos));
               }

               batch.flush();
               SceneCapture.invalidateColor();
               SceneCapture.invalidateDepth();
               var4 = "Chams nearest-surface gradient silhouette";
               ChamsModule var12 = ChamsModule.getInstance();
               if (var12 != null) {
                  runIsolated("Chams", () -> StreamProofManager.runOnOverlayThread(() -> var12.reset(var0.poseStack(), batch, var5)));
               }

               EntityRenderState var13 = selfRenderState;
               var4 = "SelfVisuals outline mask / player material";
               if (!drewBeforeTranslucent) {
                  isRenderingEnabled(var0, var13);
               }

               TargetAuraModule var14 = TargetAuraModule.getInstance();
               DamageMarkersModule var15 = DamageMarkersModule.getInstance();
               if (var15 != null) {
                  runIsolated("DamageMarkers", () -> var15.renderLabels(var0.poseStack(), var0.levelState().cameraRenderState));
               }

               WorldLyricsModule var16 = WorldLyricsModule.getInstance();
               if (var16 != null) {
                  runIsolated("WorldLyrics", () -> var16.renderLabels(var0.poseStack(), var0.levelState().cameraRenderState, false));
               }

               JumpFXModule var17 = JumpFXModule.getInstance();
               BlockOverlayModule var18 = BlockOverlayModule.getInstance();
               KillFXModule var19 = KillFXModule.getInstance();
               var4 = "ChinaHat after player materials";
               ChinaHatModule var20 = ChinaHatModule.getInstance();
               if (var20 != null) {
                  runIsolated("ChinaHat", () -> {
                     var20.renderHats(var6, var5);
                     batch.flush();
                  });
               }

               var4 = "Wings after player materials";
               if (var7 != null && var7.isOperational() && !var7.isDragonStyle()) {
                  runGuarded("Wings", var6, () -> var7.renderWings(var6, batch));
               }

               RenderTarget var21 = !RenderEffects.hasGlowing()
                     && !hasBloomSources(TickTimer.getTime())
                     && (isolatedStages.contains("Particles") || !particles.getVisibleCount())
                     && (var9 == 0 || !var8.getEffect().randomPointInCylinder())
                     && (isolatedStages.contains("Trails") || !trails.isGlowVisible())
                     && (isolatedStages.contains("Body Trails") || !bodyTrails.hasVisibleRibbons())
                     && (isolatedStages.contains("BlockOverlay") || var18 == null || !var18.hasGlow(var0.levelState()))
                     && !placementHighlights.stream().anyMatch(var1x -> !isolatedStages.contains(var1x.getLabel()) && var1x.reset(var0.levelState()))
                     && (isolatedStages.contains("Comets") || !CometTrailRenderer.isGlowActive())
                     && (isolatedStages.contains("JumpFX") || var17 == null || !var17.hasGlow())
                     && (isolatedStages.contains("KillFX") || var19 == null || !var19.shouldHideVictim())
                     && (isolatedStages.contains("HitZone") || !AuraRenderer.hasOutline())
                     && (isolatedStages.contains("GhostEcho") || !ghostEcho.hasBloom())
                     && (isolatedStages.contains("TargetAura") || var14 == null || !var14.isBloomActive())
                     && (isolatedStages.contains("SelfVisuals") || var13 == null || !selfBloomEnabled || !isStageActive("SelfVisuals bloom"))
                     && (isolatedStages.contains("Wings") || var7 == null || !var7.hasMembraneOutline())
                     && (isolatedStages.contains("ChinaHat") || var20 == null || !var20.hasActiveGlow())
                     && (isolatedStages.contains("WorldLyrics") || var16 == null || !var16.hasGlow())
                  ? null
                  : PostProcessing.beginBloom();
               var4 = "explicit bloom mask / composite";
               if (var21 != null) {
                  OverlayTextures.colorTexture = var21.getColorTextureView();
                  OverlayTextures.depthTexture = var21.getDepthTextureView();
                  renderGeometry(var0, var6, var5, true);
                  if (var16 != null && var16.hasGlow()) {
                     runIsolated("WorldLyrics", () -> var16.renderLabels(var0.poseStack(), var0.levelState().cameraRenderState, true));
                  }

                  if (var7 != null && var7.hasMembraneOutline()) {
                     runGuarded("Wings", var6, () -> var7.renderOutlines(var6, batch));
                  }

                  if (var20 != null && !isolatedStages.contains("ChinaHat") && var20.hasActiveGlow()) {
                     runIsolated("ChinaHat", () -> var20.migrateLegacyMode(var6, batch));
                  }

                  GpuTextureView var22 = drewBeforeTranslucent ? SceneCapture.beginFrame() : null;
                  if (var22 != null) {
                     OverlayTextures.depthTexture = var22;
                  }

                  if (drewBeforeTranslucent) {
                     runGuarded("Comets", var6, () -> {
                        CometTrailRenderer.render(var6, true);
                        batch.flush();
                     });
                  }

                  if (var13 != null && selfBloomEnabled) {
                     runIsolated(
                        "SelfVisuals bloom",
                        () -> {
                           ModelVisualRenderer.renderEntityLayers(
                              var13,
                              var0.poseStack(),
                              batch,
                              selfMaterial == ModelVisualRenderer.Material.LIQUID_GLASS ? ModelVisualRenderer.Material.GLASS_GLOW : selfMaterial,
                              selfColor,
                              selfMaterial == ModelVisualRenderer.Material.LIQUID_GLASS ? selfStrength * 0.25 : 0.25,
                              true,
                              false
                           );
                           batch.flush();
                        }
                     );
                  }

                  OverlayTextures.colorTexture = var2;
                  OverlayTextures.depthTexture = var3;
                  PostProcessing.applyBloom();
               }
            } catch (RuntimeException var28) {
               renderFailed = (boolean)1;
               LarpLog.error("Render", "VisualRuntime stage=" + var4 + "; output attachments restored; retry on resource reload", var28);
            } finally {
               OverlayTextures.colorTexture = var2;
               OverlayTextures.depthTexture = var3;
               DamageMarkersModule var24 = DamageMarkersModule.getInstance();
               if (var24 != null) {
                  var24.endBatch();
               }

               WorldLyricsModule var25 = WorldLyricsModule.getInstance();
               if (var25 != null) {
                  var25.endBatch();
               }

               if (batch != null) {
                  batch.endFrame();
               }

               runIsolated("Refraction cleanup", LocalizedRefraction::endFrame);
               runIsolated("Scene resource cleanup", SceneCapture::endFrame);
            }
         }
      }
   }

   public static void renderHud(GuiGraphicsExtractor var0) {
      if (isRenderingEnabled()) {
         PetsModule var1 = PetsModule.getInstance();
         if (var1 != null) {
            runIsolated("Pets", () -> var1.drawSpeechBubble(var0));
         }
      }
   }

   private static boolean hasBloomSources(double var0) {
      LineGlyphsModule var2 = LineGlyphsModule.getInstance();
      PearlParticlesModule var3 = PearlParticlesModule.getInstance();
      CrystalChamsModule var4 = CrystalChamsModule.getInstance();
      PetsModule var5 = PetsModule.getInstance();
      CryptoPetsModule var6 = CryptoPetsModule.getInstance();
      return !isolatedStages.contains("Pets") && var5 != null && var5.hasGlow()
         || !isolatedStages.contains("CryptoPets") && var6 != null && var6.hasGlow()
         || !isolatedStages.contains("CrystalChams") && var4 != null && var4.isCustomized()
         || !isolatedStages.contains("LineGlyphs") && var2 != null && var2.hasVisibleGlyphs(var0)
         || !isolatedStages.contains("PearlParticles") && var3 != null && var3.hasVisibleParticles(var0);
   }

   private static void renderGeometry(LevelRenderContext var0, EffectsRenderer var1, float var2, boolean var3) {
      EffectBatch var4 = batch;
      RenderEffects.render(var1, var3);

      for (PlacementHighlight var6 : placementHighlights) {
         runGuarded(var6.getLabel(), var1, () -> var6.render(var1, var3, var0.levelState()));
      }

      SnowGroundModule var16 = SnowGroundModule.getQualityLevel();
      if (!var3 && var16 != null) {
         runGuarded("SnowGround", var1, () -> var16.isSnowfallActive(var1));
      }

      CrystalChamsModule var17 = CrystalChamsModule.getInstance();
      if (var17 != null) {
         runGuarded("CrystalChams", var1, () -> var17.render(var1, var3));
      }

      PetsModule var7 = PetsModule.getInstance();
      if (var7 != null) {
         runGuarded("Pets", var1, () -> var7.render(var1, var3));
      }

      CryptoPetsModule var8 = CryptoPetsModule.getInstance();
      if (var8 != null) {
         runGuarded("CryptoPets", var1, () -> var8.render(var1, var3));
      }

      runGuarded("EmoteProps", var1, () -> EmoteProps.render(var1, var3));
      runGuarded("Particles", var1, () -> particles.render(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      runGuarded("Trails", var1, () -> trails.render(var1, var2, var3));
      runGuarded("Body Trails", var1, () -> bodyTrails.buildRibbon(var1, var3));
      if (!drewBeforeTranslucent) {
         runGuarded("Comets", var1, () -> CometTrailRenderer.render(var1, var3));
      }

      AmbientParticlesModule var9 = AmbientParticlesModule.getInstance();
      if (var9 != null && var9.isOperational()) {
         runGuarded("Ambient Particles", var1, () -> var9.getEffect().render(var1, var0.levelState().cameraRenderState.pos, var2, var3));
      }

      JumpFXModule var10 = JumpFXModule.getInstance();
      if (var10 != null) {
         runGuarded("JumpFX", var1, () -> var10.renderWorld(var1, var3));
      }

      KillFXModule var11 = KillFXModule.getInstance();
      if (var11 != null) {
         runGuarded("KillFX", var1, () -> var11.render(var1, var0.poseStack(), var4, var3));
      }

      BlockOverlayModule var12 = BlockOverlayModule.getInstance();
      if (var12 != null) {
         runGuarded("BlockOverlay", var1, () -> var12.render(var1, var3, var0.levelState()));
      }

      runGuarded("HitZone", var1, () -> AuraRenderer.registerRenderHooks(var1, var3));
      TargetAuraModule var13 = TargetAuraModule.getInstance();
      if (var13 != null) {
         runGuarded("TargetAura", var1, () -> var13.render(var1, var2, var3));
      }

      runGuarded("GhostEcho", var1, () -> ghostEcho.render(var0.poseStack(), var4, var3));
      LineGlyphsModule var14 = LineGlyphsModule.getInstance();
      if (var14 != null) {
         runIsolated("LineGlyphs", () -> var14.render(var0.poseStack(), var4, var0.levelState().cameraRenderState, TickTimer.getTime(), var3));
      }

      PearlParticlesModule var15 = PearlParticlesModule.getInstance();
      if (var15 != null) {
         runIsolated("PearlParticles", () -> var15.renderParticles(var0.poseStack(), var4, var0.levelState().cameraRenderState, TickTimer.getTime(), var3));
      }

      runIsolated("Shared geometry", var4::flush);
   }
}

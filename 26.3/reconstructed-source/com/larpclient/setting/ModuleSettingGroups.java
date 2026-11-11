package com.larpclient.setting;

import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.AutoAnchorModule;
import com.larpclient.module.combat.AutoCartModule;
import com.larpclient.module.combat.AutoClickerModule;
import com.larpclient.module.combat.AutoCrystalModule;
import com.larpclient.module.combat.AutoPearlCatchModule;
import com.larpclient.module.combat.AutoTrap;
import com.larpclient.module.combat.TargetAuraModule;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.module.misc.AutoGGModule;
import com.larpclient.module.misc.AutoSellModule;
import com.larpclient.module.misc.CasinoGameModule;
import com.larpclient.module.misc.CryptoPetsModule;
import com.larpclient.module.misc.EchoHandModule;
import com.larpclient.module.misc.HitSoundsModule;
import com.larpclient.module.misc.HomeMacroModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.PerformanceModule;
import com.larpclient.module.misc.PetsModule;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.player.AutoArmorModule;
import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.module.player.RodSwapModule;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.module.render.AmbientParticlesModule;
import com.larpclient.module.render.AnimationsModule;
import com.larpclient.module.render.AstralWeaponTrailsModule;
import com.larpclient.module.render.BlockOverlayModule;
import com.larpclient.module.render.CelestialWeatherModule;
import com.larpclient.module.render.ChamsModule;
import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.ChooseASkyEffectAndAdjustItsAppearanceModule;
import com.larpclient.module.render.CrystalChamsModule;
import com.larpclient.module.render.CustomCrosshairModule;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.DamageMarkersModule;
import com.larpclient.module.render.ESPModule;
import com.larpclient.module.render.FlameHandsModule;
import com.larpclient.module.render.FogModule;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.module.render.HandChamsModule;
import com.larpclient.module.render.HeadlessKorbloxModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.HolesESPModule;
import com.larpclient.module.render.ItemESPModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.module.render.LightDebugModule;
import com.larpclient.module.render.MotionBlurModule;
import com.larpclient.module.render.NametagsModule;
import com.larpclient.module.render.PearlParticlesModule;
import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.module.render.PlayerTrailModule;
import com.larpclient.module.render.RealisticWaterModule;
import com.larpclient.module.render.RemovalsModule;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.module.render.SnowGroundModule;
import com.larpclient.module.render.StorageESPModule;
import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.module.render.TracersModule;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.module.render.WavingFoliageModule;
import com.larpclient.module.render.WaypointsModule;
import com.larpclient.module.render.WetGroundModule;
import com.larpclient.module.render.WingsModule;
import com.larpclient.module.render.WorldColorModule;
import com.larpclient.module.render.XRayModule;
import com.larpclient.module.render.ZoomModule;
import com.larpclient.module.world.BaseFinder;
import com.larpclient.module.world.ChunkFindingModule;
import com.larpclient.module.world.MotionScannerModule;
import com.larpclient.render.world.PlacementHighlight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public final class ModuleSettingGroups {
   public static List<ModuleSettingGroups.SettingGroup> groupsFor(Module var0) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var6) {
         ArrayList var7 = new ArrayList();
         var7.add(new ModuleSettingGroups.SettingGroup("Sky", List.of(var6.getStyleSetting())));
         if (var6.getActiveModule() instanceof CustomSkyModule var80) {
            var7.addAll(customSkyGroups(var80));
         } else if (var6.getActiveModule() instanceof SkyShaderModule var85) {
            var7.addAll(skyShaderGroups(var85));
         }

         var7.add(ModuleSettingGroups.SettingGroup.advanced("Existing keybinds", var6.getKeybindSettings()));
         return List.copyOf(var7);
      } else {
         ArrayList var1 = new ArrayList();
         Set var2 = Collections.newSetFromMap(new IdentityHashMap());
         if (var0.keybindMode() != null) {
            var2.add(var0.keybindMode());
         }

         if (var0 instanceof AutoClickerModule var3) {
            var1.add(new ModuleSettingGroups.SettingGroup("Click controls", var3.getActivationSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Timing", var3.isLeftClickActive()));
            var1.add(new ModuleSettingGroups.SettingGroup("Targets and mining", var3.getTargetSettings()));
         }

         if (var0 instanceof CasinoGameModule var8) {
            var2.addAll(var8.getBindSettings());
         }

         if (var0 instanceof RodSwapModule var9) {
            var1.add(new ModuleSettingGroups.SettingGroup("Rod controls", var9.getModeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Range and timing", var9.hasManualSlotSelection()));
         }

         if (var0 instanceof ZoomModule var10) {
            var1.add(new ModuleSettingGroups.SettingGroup("Zoom controls", var10.getKeySettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("View", var10.getZoomSettings()));
         }

         if (var0 instanceof RemovalsModule var11) {
            var1.add(new ModuleSettingGroups.SettingGroup("Effects", var11.getVisualSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Sounds", var11.getSoundSettings(), true));
         }

         if (var0 instanceof AutoCrystalModule var12) {
            var1.add(new ModuleSettingGroups.SettingGroup("Actions", var12.addNumberSetting()));
            var1.add(new ModuleSettingGroups.SettingGroup("Obsidian bases", var12.getObsidianSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Targets", var12.getTargetSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Aim", var12.updateAim()));
            var1.add(new ModuleSettingGroups.SettingGroup("Damage and pauses", var12.scoreCrystal()));
         }

         if (var0 instanceof AutoAnchorModule var13) {
            var1.add(new ModuleSettingGroups.SettingGroup("Actions", var13.canPlaceShield()));
            var1.add(new ModuleSettingGroups.SettingGroup("Targets", var13.getTargetSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Aim", var13.getRotationSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Damage and pauses", var13.getDamageSettings()));
         }

         if (var0 instanceof AutoCartModule var14) {
            var1.add(new ModuleSettingGroups.SettingGroup("Aim", var14.getRotationSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Shot", var14.getChargeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Targets", var14.getTargetSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Damage", var14.getSafetySettings()));
         }

         if (var0 instanceof AutoPearlCatchModule var15) {
            var1.add(new ModuleSettingGroups.SettingGroup("Catch", var15.aimToward()));
            var1.add(new ModuleSettingGroups.SettingGroup("Aim", var15.getTurnSettings()));
         }

         if (var0 instanceof PerformanceModule var16) {
            var1.add(new ModuleSettingGroups.SettingGroup("Render quality", var16.createQualitySettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Chunks and frame rate", var16.getStreamingSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Aggressive", var16.getAggressiveSettings()));
         }

         if (var0 instanceof AutoArmorModule var17) {
            var1.add(new ModuleSettingGroups.SettingGroup("Armor materials", var17.getMaterialSettings()));
         }

         if (var0 instanceof AuraModule var18) {
            var1.add(new ModuleSettingGroups.SettingGroup("Rotation", var18.getAimSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Attack", var18.getCombatSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Targets", var18.getMoveYaw(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Selection", var18.getTargetSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Utils", var18.getMiscSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("No Attack When", var18.getBaseReach(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Indicators", var18.getVisualSettings()));
         }

         if (var0 instanceof HitSoundsModule var19) {
            var1.add(new ModuleSettingGroups.SettingGroup("Custom sound", var19.getSoundSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Behavior", var19.getFilterSettings()));
         }

         if (var0 instanceof HitFXModule var20) {
            var1.add(new ModuleSettingGroups.SettingGroup("Impact style", var20.getParticleSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Hit rings", var20.getRingToggleSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var20.getColorSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Advanced", var20.getAdvancedSettings()));
         }

         if (var0 instanceof KillFXModule var21) {
            var1.add(new ModuleSettingGroups.SettingGroup("Finisher", var21.getStyleSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Feedback", var21.getEffectSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Distance", var21.getDistanceSettings()));
         }

         if (var0 instanceof CustomSkyModule var22) {
            var1.add(new ModuleSettingGroups.SettingGroup("Sky", List.of(var22.getSkyStyleSetting())));
            var1.addAll(customSkyGroups(var22));
         }

         if (var0 instanceof SkyShaderModule var23) {
            var1.add(new ModuleSettingGroups.SettingGroup("Sky", List.of(var23.getModeSetting())));
            var1.addAll(skyShaderGroups(var23));
         }

         if (var0 instanceof WorldColorModule var24) {
            var1.add(new ModuleSettingGroups.SettingGroup("World ambience", List.of(var24.ambientStrength, var24.ambientColor)));
            var1.add(new ModuleSettingGroups.SettingGroup("Color grading", List.of(var24.saturation, var24.exposure, var24.tint)));
            var1.add(
               ModuleSettingGroups.SettingGroup.advanced(
                  "Fine grading", List.of(var24.contrast, var24.gamma, var24.shadowTint, var24.highlightTint, var24.vignette)
               )
            );
         }

         if (var0 instanceof MotionBlurModule var25) {
            var1.add(new ModuleSettingGroups.SettingGroup("Motion blur", List.of(var25.modeSetting, var25.strengthSetting)));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Streaks", List.of(var25.qualitySetting, var25.maxLengthSetting)));
         }

         if (var0 instanceof AmbientParticlesModule var26) {
            var1.add(
               new ModuleSettingGroups.SettingGroup(
                  "World particles", List.of(var26.style, var26.radius, var26.height, var26.size, var26.density, var26.lifetime)
               )
            );
            var1.add(
               new ModuleSettingGroups.SettingGroup("Floating motion", List.of(var26.speed, var26.verticalTravel, var26.sidewaysTravel, var26.motionPeriod))
            );
            var1.add(new ModuleSettingGroups.SettingGroup("Flying comets", List.of(var26.cometCount, var26.trailLength, var26.trailWidth)));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", List.of(var26.themeColors, var26.color, var26.tailColor, var26.opacity, var26.bloom)));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Particle limit", List.of(var26.maxParticles)));
         }

         if (var0 instanceof WingsModule var27) {
            var1.add(new ModuleSettingGroups.SettingGroup("Wearers", var27.getVisibilitySettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var27.getStyleSettings()));
         }

         if (var0 instanceof HeadlessKorbloxModule var28) {
            var1.add(new ModuleSettingGroups.SettingGroup("Wearers", List.of(var28.self, var28.otherPlayers), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Headless head", List.of(var28.headless, var28.hideHelmets)));
            var1.add(new ModuleSettingGroups.SettingGroup("Korblox leg", List.of(var28.korblox, var28.leg, var28.length, var28.thickness)));
            var1.add(new ModuleSettingGroups.SettingGroup("Spike colors", List.of(var28.hipColor, var28.tipColor, var28.tipGlow)));
         }

         if (var0 instanceof ChinaHatModule var29) {
            var1.add(new ModuleSettingGroups.SettingGroup("Hats", var29.getHatToggles(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Wearers", var29.getVisibilitySettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var29.getColorSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Shape", var29.getSizeSettings()));
         }

         if (var0 instanceof PlayerTrailModule var30) {
            var1.add(new ModuleSettingGroups.SettingGroup("Trail", var30.getTargetSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var30.getAppearanceSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Motion", var30.getTimingSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("History and limits", var30.getLimitSettings()));
         }

         if (var0 instanceof AstralWeaponTrailsModule var31) {
            var1.add(new ModuleSettingGroups.SettingGroup("Effect", var31.getStyleSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var31.getColorSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Advanced", var31.getBehaviorSettings()));
         }

         if (var0 instanceof CelestialWeatherModule var32) {
            var1.add(new ModuleSettingGroups.SettingGroup("Effect", var32.getMainSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var32.getColorSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Advanced", var32.getAreaSettings()));
         }

         if (var0 instanceof FogModule var33) {
            var1.add(new ModuleSettingGroups.SettingGroup("Distance", var33.getDistanceSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Atmosphere", var33.getAppearanceSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Sky", var33.getSkySettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Where", var33.getFilterSettings()));
         }

         if (var0 instanceof SelfVisualsModule var34) {
            var1.add(new ModuleSettingGroups.SettingGroup("Body effect", var34.getStyleSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var34.getSecondaryColor()));
            var1.add(new ModuleSettingGroups.SettingGroup("Motion", var34.getMotionSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Refraction", var34.getRefractionSettings()));
         }

         if (var0 instanceof JumpFXModule var35) {
            var1.add(new ModuleSettingGroups.SettingGroup("Jump effect", var35.getPrimarySettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Shape and timing", var35.isOutlineMode()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var35.getColorSettings()));
         }

         if (var0 instanceof TargetAuraModule var36) {
            var1.add(new ModuleSettingGroups.SettingGroup("Target effect", var36.getStyleSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var36.addDisc()));
         }

         if (var0 instanceof CustomCrosshairModule var37) {
            var1.add(new ModuleSettingGroups.SettingGroup("Shape", var37.getShapeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var37.getAppearanceSettings()));
         }

         if (var0 instanceof EchoHandModule var38) {
            var1.add(new ModuleSettingGroups.SettingGroup("Hand effect", var38.getModeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var38.getAppearanceSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("History and motion", var38.addNumberSetting()));
         }

         if (var0 instanceof FlameHandsModule var39) {
            var1.add(new ModuleSettingGroups.SettingGroup("Hand flames", var39.getEmissionSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var39.getColorSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Motion", var39.getMotionSettings()));
         }

         if (var0 instanceof PearlParticlesModule var40) {
            var1.add(new ModuleSettingGroups.SettingGroup("Pearl trail", var40.getEmissionSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var40.getColorSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Drift and fade", var40.getPhysicsSettings()));
         }

         if (var0 instanceof DamageMarkersModule var41) {
            var1.add(new ModuleSettingGroups.SettingGroup("Damage sources", var41.getFilterSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Labels", var41.getAppearanceSettings()));
         }

         if (var0 instanceof PlayerModelModule var42) {
            var1.add(new ModuleSettingGroups.SettingGroup("Character", var42.getCharacterSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Diagnostics", var42.onResourceReload()));
         }

         if (var0 instanceof GUIAnimationModule var43) {
            var1.add(new ModuleSettingGroups.SettingGroup("Interface", var43.getInventorySettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Camera", var43.addToggle(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("World and timing", var43.getChunkSettings()));
         }

         if (var0 instanceof ChamsModule var44) {
            var1.add(new ModuleSettingGroups.SettingGroup("Entities", var44.getTargetSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var44.shouldRenderMobs()));
            var1.add(new ModuleSettingGroups.SettingGroup("Outline and fill", var44.outlineSettings.outlineSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Glow", var44.outlineSettings.glowSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Gradient motion", var44.outlineSettings.animationSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Occlusion", var44.getGreen()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Distance and limits", var44.getRangeSettings()));
         }

         if (var0 instanceof HandChamsModule var45) {
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var45.outlineSettings.colorSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Outline and fill", var45.outlineSettings.outlineSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Glow", var45.outlineSettings.glowSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Gradient motion", var45.outlineSettings.animationSettings()));
         }

         if (var0 instanceof ESPModule var46) {
            var1.add(new ModuleSettingGroups.SettingGroup("Entities", var46.getTargetSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Highlight", var46.getModeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Colors", var46.getColorSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Stroke", var46.getOutlineFillSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Health", var46.getHealthBarSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Limits and alerts", var46.getLimitSettings()));
         }

         if (var0 instanceof ItemESPModule var47) {
            var1.add(new ModuleSettingGroups.SettingGroup("Drops", var47.getMainSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var47.getColorSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Item limit", var47.getLimitSettings()));
         }

         if (var0 instanceof TracersModule var48) {
            var1.add(new ModuleSettingGroups.SettingGroup("Entities", var48.getTargetSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Lines", var48.getRangeSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Entity limit", var48.getLimitSettings()));
         }

         if (var0 instanceof NametagsModule var49) {
            var1.add(new ModuleSettingGroups.SettingGroup("Entities", var49.getTargetSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Label content", var49.getInfoSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Placement", var49.getStyleSettings()));
         }

         if (var0 instanceof XRayModule var50) {
            var1.add(new ModuleSettingGroups.SettingGroup("Ore view", var50.getModeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Show", var50.getCategorySettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Markers", var50.getAppearanceSettings()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Marker limit", var50.getLimitSettings()));
         }

         if (var0 instanceof CrystalChamsModule var51) {
            var1.add(new ModuleSettingGroups.SettingGroup("Crystal", var51.getAppearanceSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var51.getColorSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Motion", var51.getSpinScale()));
         }

         if (var0 instanceof PetsModule var52) {
            var1.add(new ModuleSettingGroups.SettingGroup("Companion", var52.getAppearanceSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Follow", var52.getBehaviorSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Finish", var52.hasGlow()));
            var1.add(new ModuleSettingGroups.SettingGroup("Glow", var52.updateFrame()));
         }

         if (var0 instanceof CryptoPetsModule var53) {
            var1.add(new ModuleSettingGroups.SettingGroup("Coins", var53.getCoinSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Ring", var53.getAppearanceSettings()));
         }

         if (var0 instanceof RealisticWaterModule var54) {
            var1.add(new ModuleSettingGroups.SettingGroup("Surface", List.of(var54.intensity, var54.waves, var54.worldReflections, var54.foam, var54.caustics)));
            var1.add(new ModuleSettingGroups.SettingGroup("Water view", List.of(var54.underwater, var54.volumetric)));
         }

         if (var0 instanceof WavingFoliageModule var55) {
            var1.add(new ModuleSettingGroups.SettingGroup("Foliage", List.of(var55.leaves, var55.trees, var55.plants), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Wind", List.of(var55.strength, var55.speed, var55.gusts)));
         }

         if (var0 instanceof WetGroundModule var56) {
            var1.add(new ModuleSettingGroups.SettingGroup("Puddles", List.of(var56.style, var56.puddles, var56.wetness, var56.shine, var56.worldReflections)));
            var1.add(new ModuleSettingGroups.SettingGroup("Rain", List.of(var56.raindrops, var56.wetterInRain), true));
         }

         if (var0 instanceof SnowGroundModule var57) {
            var1.add(new ModuleSettingGroups.SettingGroup("Ground", List.of(var57.coverage, var57.depth, var57.sparkle, var57.quality)));
            var1.add(new ModuleSettingGroups.SettingGroup("Snowfall", List.of(var57.snowfall, var57.density, var57.wind, var57.radius)));
            var1.add(new ModuleSettingGroups.SettingGroup("Footsteps", List.of(var57.footsteps, var57.trailTime)));
         }

         if (var0 instanceof BlockOverlayModule var58) {
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var58.getAppearanceSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Animation", var58.getAnimationSettings()));
         }

         if (var0 instanceof ChunkFindingModule var59) {
            var1.add(new ModuleSettingGroups.SettingGroup("Detection", var59.detectionSettings()));
            if (var59 instanceof BaseFinder var4) {
               var1.add(new ModuleSettingGroups.SettingGroup("Diagnostics", var4.isDiagnosticActive()));
            }

            var1.add(new ModuleSettingGroups.SettingGroup("Scanning", var59.scanSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Markers", var59.markerSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Alerts", var59.reset()));
         }

         if (var0 instanceof AutoSellModule var60) {
            var1.add(new ModuleSettingGroups.SettingGroup("Items", var60.getSelectionSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Sell menu", var60.getMenuSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Timing", var60.getDelaySettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Feedback", var60.getItemKey()));
         }

         if (var0 instanceof MotionScannerModule var61) {
            var1.add(new ModuleSettingGroups.SettingGroup("Detection", var61.getDetectionSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Alerts", var61.getNotificationSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var61.getRenderSettings()));
         }

         if (var0 instanceof HomeMacroModule var62) {
            var1.add(new ModuleSettingGroups.SettingGroup("Home replacement", var62.getSettings()));
         }

         if (var0 instanceof ModulePanel var63) {
            for (ModulePanel.SettingGroup var5 : var63.settingGroups()) {
               var1.add(
                  var5.advanced()
                     ? ModuleSettingGroups.SettingGroup.advanced(var5.title(), var5.settings())
                     : new ModuleSettingGroups.SettingGroup(var5.title(), var5.settings())
               );
            }
         }

         if (var0 instanceof AutoGGModule var64) {
            var1.add(new ModuleSettingGroups.SettingGroup("Messages", var64.getMessageSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Timing", var64.getTimingSettings()));
         }

         if (var0 instanceof WaypointsModule var65) {
            var1.add(new ModuleSettingGroups.SettingGroup("Display", var65.getVisualSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Shortcut", var65.isCurrentWorldFull()));
         }

         if (var0 instanceof OrderDropperModule var66) {
            var1.add(new ModuleSettingGroups.SettingGroup("Orders", var66.validateShulkers()));
            var1.add(new ModuleSettingGroups.SettingGroup("Shulkers", var66.clickSlot()));
            var1.add(new ModuleSettingGroups.SettingGroup("Timing and safety", var66.isAfkActive()));
         }

         if (var0 instanceof LightDebugModule var67) {
            var1.add(new ModuleSettingGroups.SettingGroup("Scanning", var67.getRangeSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var67.renderGizmos()));
         }

         if (var0 instanceof HolesESPModule var68) {
            var1.add(new ModuleSettingGroups.SettingGroup("Passage colors", var68.getColorSettings()));
            var1.add(new ModuleSettingGroups.SettingGroup("Rendering", var68.getLimitSettings()));
         }

         if (var0 instanceof StorageESPModule var69) {
            var1.add(new ModuleSettingGroups.SettingGroup("Storage types", var69.getTypeSettings(), true));
            var1.add(new ModuleSettingGroups.SettingGroup("Highlights", var69.getIcon()));
            var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var69.getBlockName()));
            var1.add(ModuleSettingGroups.SettingGroup.advanced("Distance and limits", var69.getLimitSettings()));
         }

         if (var0 instanceof HUDModule var79) {
            return List.of(
               new ModuleSettingGroups.SettingGroup("Widgets", var79.getWidgetToggles(), true),
               new ModuleSettingGroups.SettingGroup("Panels", var79.getEffectSettings())
            );
         } else {
            if (var0 instanceof AnimationsModule var70) {
               var1.add(new ModuleSettingGroups.SettingGroup("Swing", var70.getSwingSettings()));
               var1.add(ModuleSettingGroups.SettingGroup.advanced("Fine tuning", var70.getSpinSettings()));
            }

            if (var0 instanceof ViewModelModule var71) {
               var1.add(new ModuleSettingGroups.SettingGroup("Swing", var71.resetHand()));
               var1.add(new ModuleSettingGroups.SettingGroup("Main hand", var71.getMainHandSettings()));
               var1.add(new ModuleSettingGroups.SettingGroup("Offhand", var71.getInstance()));
            }

            if (var0 instanceof SwordModelsModule var72) {
               var1.add(new ModuleSettingGroups.SettingGroup("Appearance", List.of(var72.model, var72.finish, var72.enchantmentGlint)));
               var1.add(new ModuleSettingGroups.SettingGroup("Inspect", List.of(var72.inspectKey, var72.inspectSpeed, var72.idleMotion)));
               var1.add(ModuleSettingGroups.SettingGroup.advanced("Hand placement", List.of(var72.modelScale, var72.handX, var72.handY, var72.handZ)));
            }

            if (var0 instanceof FakeSpawnerModule var73) {
               var1.add(new ModuleSettingGroups.SettingGroup("Backing blocks", var73.getBlockSettings()));
               var1.add(new ModuleSettingGroups.SettingGroup("Appearance", var73.getDisplaySettings()));
               var1.add(new ModuleSettingGroups.SettingGroup("Loot worth", var73.getPriceSettings()));
            }

            if (var0 instanceof AutoTrap var74) {
               var1.add(new ModuleSettingGroups.SettingGroup("Trap", var74.getPrimarySettings()));
               var1.add(ModuleSettingGroups.SettingGroup.advanced("Advanced", var74.getTrapPositions()));
            }

            if (var0 instanceof ScaffoldModule var75) {
               var1.add(new ModuleSettingGroups.SettingGroup("Bridge", var75.getSettings()));
            }

            if (var0 instanceof PlacementHighlight.PlacementHighlightHolder var76) {
               var1.add(
                  var0 instanceof AutoTrap
                     ? ModuleSettingGroups.SettingGroup.advanced("Highlight", var76.placementHighlight().getSettings())
                     : new ModuleSettingGroups.SettingGroup("Highlight", var76.placementHighlight().getSettings())
               );
            }

            for (ModuleSettingGroups.SettingGroup var82 : var1) {
               var2.addAll(var82.settings());
            }

            List var78 = var0.getSettings().stream().filter(var1x -> !var2.contains(var1x)).toList();
            if (!var78.isEmpty()) {
               String var83 = var1.isEmpty() ? "Settings" : (var0 instanceof CustomSkyModule ? "General" : "Defaults");
               if (var0 instanceof BaseFinder || var0 instanceof StorageESPModule) {
                  var83 = "Appearance";
               }

               var1.add(new ModuleSettingGroups.SettingGroup(var83, var78));
            }

            if (var0 instanceof CasinoGameModule var84) {
               var1.add(ModuleSettingGroups.SettingGroup.advanced("Shortcuts", var84.getBindSettings()));
            }

            return List.copyOf(var1);
         }
      }
   }

   private static List<ModuleSettingGroups.SettingGroup> customSkyGroups(CustomSkyModule var0) {
      return List.of(
         new ModuleSettingGroups.SettingGroup("Appearance", var0.getColorSettings()),
         new ModuleSettingGroups.SettingGroup("Motion", var0.getAnimationSettings()),
         new ModuleSettingGroups.SettingGroup("Atmosphere", var0.getEnvironmentSettings()),
         ModuleSettingGroups.SettingGroup.advanced("Detail and placement", var0.getDetailSettings())
      );
   }

   private static List<ModuleSettingGroups.SettingGroup> skyShaderGroups(SkyShaderModule var0) {
      return List.of(
         new ModuleSettingGroups.SettingGroup("Appearance", var0.getAppearanceSettings()),
         new ModuleSettingGroups.SettingGroup("Motion and pattern", var0.getAnimationSettings())
      );
   }

   public record SettingGroup(String title, List<Setting<?>> settings, boolean chips, boolean collapsed) {
      public SettingGroup(String var1, List<Setting<?>> var2) {
         this(var1, var2, false, false);
      }

      public SettingGroup(String var1, List<Setting<?>> var2, boolean var3) {
         this(var1, var2, var3, false);
      }

      public static ModuleSettingGroups.SettingGroup advanced(String var0, List<Setting<?>> var1) {
         return new ModuleSettingGroups.SettingGroup(var0, var1, false, true);
      }
   }
}

package com.larpclient.module.misc;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.exploit.NoBlockRotationModule;
import com.larpclient.module.movement.WalkStyleModule;
import com.larpclient.module.render.AnimationsModule;
import com.larpclient.module.render.AstralWeaponTrailsModule;
import com.larpclient.module.render.CapesModule;
import com.larpclient.module.render.CelestialWeatherModule;
import com.larpclient.module.render.Feature3DUIModule;
import com.larpclient.module.render.FlameHandsModule;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.module.render.HeadlessKorbloxModule;
import com.larpclient.module.render.HideArmourModule;
import com.larpclient.module.render.ItemPhysicModule;
import com.larpclient.module.render.MotionBlurModule;
import com.larpclient.module.render.NoWeatherModule;
import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.module.render.RealisticWaterModule;
import com.larpclient.module.render.RemovalsModule;
import com.larpclient.module.render.SnowGroundModule;
import com.larpclient.module.render.StretchedResModule;
import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.module.render.WavingFoliageModule;
import com.larpclient.module.render.WetGroundModule;
import com.larpclient.module.render.XRayModule;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;

public final class StreamProofModule extends Module {
   public static final String hIDE_EVERYTHING = "Hide Everything";
   public static final String kEEP_VISUALS = "Keep Visuals";
   private static StreamProofModule instance;
   public final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Hide Everything also hides ESP, tracers, nametags, trails and other effects drawn over the world. Keep Visuals hides menus, HUD and notifications but lets viewers see world effects",
         "Hide Everything",
         "Hide Everything",
         "Keep Visuals"
      )
   );
   public static final String pAUSE = "Pause";
   public static final String sHOW = "Show";
   public final ModeSetting sceneVisuals = this.addSetting(
      new ModeSetting(
         "Scene Visuals",
         "Fullbright, Time Changer, water and terrain looks, item animations, skins and capes cannot be split from the stream. Pause turns them off while hidden so viewers see vanilla; Show keeps them on and viewers see them too",
         "Pause",
         "Pause",
         "Show"
      )
   );
   public static final Set<Class<? extends Module>> viewModelModules = Set.of(
      ViewModelModule.class, AnimationsModule.class, SwordModelsModule.class, AstralWeaponTrailsModule.class, FlameHandsModule.class
   );
   public static final Set<Class<? extends Module>> sceneModules = Set.of(
      XRayModule.class,
      NoBlockRotationModule.class,
      NoWeatherModule.class,
      RemovalsModule.class,
      GUIAnimationModule.class,
      Feature3DUIModule.class,
      ItemPhysicModule.class,
      EchoHandModule.class,
      PlayerModelModule.class,
      SkinChangerModule.class,
      CapesModule.class,
      MotionBlurModule.class,
      StretchedResModule.class,
      HeadlessKorbloxModule.class,
      HideArmourModule.class,
      CelestialWeatherModule.class,
      EmotesModule.class,
      RealisticWaterModule.class,
      WavingFoliageModule.class,
      WetGroundModule.class,
      SnowGroundModule.class,
      WalkStyleModule.class
   );

   public StreamProofModule() {
      super("Stream Proof", "Hides the client from OBS and Discord capture while you still see and use it. Windows 10 2004 or newer", Category.PLAYER, -1);
      instance = this;
      this.mode.onChange(var0 -> LarpLog.info("StreamProof", "Mode selected: {}", var0));
      this.sceneVisuals.visibleWhen(this::isHidingEverything);
      this.sceneVisuals.onChange(var0 -> LarpLog.info("StreamProof", "Scene visuals: {}", var0));
   }

   public static StreamProofModule getInstance() {
      return instance;
   }

   public boolean isHidingEverything() {
      return this.mode.isMode("Hide Everything");
   }

   public boolean shouldPauseSceneVisuals() {
      return this.isHidingEverything() && this.sceneVisuals.isMode("Pause");
   }

   @Override
   public String getStatus() {
      return StreamProofManager.statusText(this);
   }
}

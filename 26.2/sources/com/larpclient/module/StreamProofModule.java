package com.larpclient.module;

import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.p2;
import java.util.Set;

public final class StreamProofModule extends Module {
   public static final String J = "Hide Everything";
   public static final String h = "Keep Visuals";
   private static StreamProofModule x;
   public final l P = this.addSetting(
      new l(
         "Mode",
         "Hide Everything also hides ESP, tracers, nametags, trails and other effects drawn over the world. Keep Visuals hides menus, HUD and notifications but lets viewers see world effects",
         "Hide Everything",
         "Hide Everything",
         "Keep Visuals"
      )
   );
   public static final String Y = "Pause";
   public static final String s = "Show";
   public final l Z = this.addSetting(
      new l(
         "Scene Visuals",
         "Fullbright, Time Changer, water and terrain looks, item animations, skins and capes cannot be split from the stream. Pause turns them off while hidden so viewers see vanilla; Show keeps them on and viewers see them too",
         "Pause",
         "Pause",
         "Show"
      )
   );
   public static final Set<Class<? extends Module>> u = Set.of(
      ViewModelModule.class, AnimationsModule.class, SwordModelsModule.class, AstralWeaponTrailsModule.class, FlameHandsModule.class
   );
   public static final Set<Class<? extends Module>> F = Set.of(
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
      x = this;
      this.P.o(var0 -> com.larpclient.h.A("StreamProof", "Mode selected: {}", var0));
      this.Z.P(this::z);
      this.Z.o(var0 -> com.larpclient.h.A("StreamProof", "Scene visuals: {}", var0));
   }

   public static StreamProofModule K() {
      return x;
   }

   public boolean z() {
      return this.P.O("Hide Everything");
   }

   public boolean b() {
      return this.z() && this.Z.O("Pause");
   }

   @Override
   public String getStatus() {
      return p2.u(this);
   }
}

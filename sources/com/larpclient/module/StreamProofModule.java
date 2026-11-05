package com.larpclient.module;

import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.mt;
import com.larpclient.zo;
import java.util.Set;

public final class StreamProofModule extends Module {
   public static final String h = "Hide Everything";
   public static final String f = "Keep Visuals";
   private static StreamProofModule e;
   public final d6 u = this.addSetting(
      new d6(
         "Mode",
         "Hide Everything also hides ESP, tracers, nametags, trails and other effects drawn over the world. Keep Visuals hides menus, HUD and notifications but lets viewers see world effects",
         "Hide Everything",
         "Hide Everything",
         "Keep Visuals"
      )
   );
   public static final String c = "Pause";
   public static final String M = "Show";
   public final d6 J = this.addSetting(
      new d6(
         "Scene Visuals",
         "Fullbright, Time Changer, water and terrain looks, item animations, skins and capes cannot be split from the stream. Pause turns them off while hidden so viewers see vanilla; Show keeps them on and viewers see them too",
         "Pause",
         "Pause",
         "Show"
      )
   );
   public static final Set<Class<? extends Module>> w = Set.of(
      ViewModelModule.class, AnimationsModule.class, SwordModelsModule.class, AstralWeaponTrailsModule.class, FlameHandsModule.class
   );
   public static final Set<Class<? extends Module>> d = Set.of(
      XRayModule.class,
      NoBlockRotationModule.class,
      NoWeatherModule.class,
      RemovalsModule.class,
      GUIAnimationModule.class,
      Feature3DUIModule.class,
      HalloweenModule.class,
      ItemPhysicModule.class,
      EchoHandModule.class,
      PlayerModelModule.class,
      SkinChangerModule.class,
      CapesModule.class,
      MotionBlurModule.class,
      StretchedResModule.class,
      HeadlessKorbloxModule.class,
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
      e = this;
      this.u.h(var0 -> mt.K("StreamProof", "Mode selected: {}", var0));
      this.J.H(this::H);
      this.J.h(var0 -> mt.K("StreamProof", "Scene visuals: {}", var0));
   }

   public static StreamProofModule s() {
      return e;
   }

   public boolean H() {
      return this.u.S("Hide Everything");
   }

   public boolean Y() {
      return this.H() && this.J.S("Pause");
   }

   @Override
   public String getStatus() {
      return zo.J(this);
   }
}

package com.larpclient.module;

import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.q8;
import com.larpclient.wi;
import java.util.Set;

public final class StreamProofModule extends Module {
   public static final String P = "Hide Everything";
   public static final String R = "Keep Visuals";
   private static StreamProofModule k;
   public final li N = this.addSetting(
      new li(
         "Mode",
         "Hide Everything also hides ESP, tracers, nametags, trails and other effects drawn over the world. Keep Visuals hides menus, HUD and notifications but lets viewers see world effects",
         "Hide Everything",
         "Hide Everything",
         "Keep Visuals"
      )
   );
   public static final String F = "Pause";
   public static final String p = "Show";
   public final li A = this.addSetting(
      new li(
         "Scene Visuals",
         "Fullbright, Time Changer, water and terrain looks, item animations, skins and capes cannot be split from the stream. Pause turns them off while hidden so viewers see vanilla; Show keeps them on and viewers see them too",
         "Pause",
         "Pause",
         "Show"
      )
   );
   public static final Set<Class<? extends Module>> e = Set.of(
      ViewModelModule.class, AnimationsModule.class, SwordModelsModule.class, AstralWeaponTrailsModule.class, FlameHandsModule.class
   );
   public static final Set<Class<? extends Module>> w = Set.of(
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
      k = this;
      this.N.a(var0 -> q8.G("StreamProof", "Mode selected: {}", var0));
      this.A.O(this::T);
      this.A.a(var0 -> q8.G("StreamProof", "Scene visuals: {}", var0));
   }

   public static StreamProofModule e() {
      return k;
   }

   public boolean T() {
      return this.N.G("Hide Everything");
   }

   public boolean K() {
      return this.T() && this.A.G("Pause");
   }

   @Override
   public String getStatus() {
      return wi.r(this);
   }
}

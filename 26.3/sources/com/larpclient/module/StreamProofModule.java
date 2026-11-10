package com.larpclient.module;

import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient._q;
import com.larpclient.qc;
import java.util.Set;

public final class StreamProofModule extends Module {
   public static final String X = "Hide Everything";
   public static final String C = "Keep Visuals";
   private static StreamProofModule A;
   public final o K = this.addSetting(
      new o(
         "Mode",
         "Hide Everything also hides ESP, tracers, nametags, trails and other effects drawn over the world. Keep Visuals hides menus, HUD and notifications but lets viewers see world effects",
         "Hide Everything",
         "Hide Everything",
         "Keep Visuals"
      )
   );
   public static final String V = "Pause";
   public static final String e = "Show";
   public final o j = this.addSetting(
      new o(
         "Scene Visuals",
         "Fullbright, Time Changer, water and terrain looks, item animations, skins and capes cannot be split from the stream. Pause turns them off while hidden so viewers see vanilla; Show keeps them on and viewers see them too",
         "Pause",
         "Pause",
         "Show"
      )
   );
   public static final Set<Class<? extends Module>> m = Set.of(
      ViewModelModule.class, AnimationsModule.class, SwordModelsModule.class, AstralWeaponTrailsModule.class, FlameHandsModule.class
   );
   public static final Set<Class<? extends Module>> I = Set.of(
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
      A = this;
      this.K.w(var0 -> _q.z("StreamProof", "Mode selected: {}", var0));
      this.j.a(this::b);
      this.j.w(var0 -> _q.z("StreamProof", "Scene visuals: {}", var0));
   }

   public static StreamProofModule r() {
      return A;
   }

   public boolean b() {
      return this.K.w("Hide Everything");
   }

   public boolean B() {
      return this.b() && this.j.w("Pause");
   }

   @Override
   public String getStatus() {
      return qc.G(this);
   }
}

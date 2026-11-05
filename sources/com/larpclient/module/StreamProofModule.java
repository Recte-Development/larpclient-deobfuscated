package com.larpclient.module;

import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.r4;
import com.larpclient.y8;
import java.util.Set;

public final class StreamProofModule extends Module {
   public static final String R = "Hide Everything";
   public static final String E = "Keep Visuals";
   private static StreamProofModule Q;
   public final lp k = this.addSetting(
      new lp(
         "Mode",
         "Hide Everything also hides ESP, tracers, nametags, trails and other effects drawn over the world. Keep Visuals hides menus, HUD and notifications but lets viewers see world effects",
         "Hide Everything",
         "Hide Everything",
         "Keep Visuals"
      )
   );
   public static final String K = "Pause";
   public static final String h = "Show";
   public final lp o = this.addSetting(
      new lp(
         "Scene Visuals",
         "Fullbright, Time Changer, water and terrain looks, item animations, skins and capes cannot be split from the stream. Pause turns them off while hidden so viewers see vanilla; Show keeps them on and viewers see them too",
         "Pause",
         "Pause",
         "Show"
      )
   );
   public static final Set<Class<? extends Module>> B = Set.of(
      ViewModelModule.class, AnimationsModule.class, SwordModelsModule.class, AstralWeaponTrailsModule.class, FlameHandsModule.class
   );
   public static final Set<Class<? extends Module>> y = Set.of(
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
      Q = this;
      this.k.W(var0 -> r4.V("StreamProof", "Mode selected: {}", var0));
      this.o.x(this::F);
      this.o.W(var0 -> r4.V("StreamProof", "Scene visuals: {}", var0));
   }

   public static StreamProofModule I() {
      return Q;
   }

   public boolean F() {
      return this.k.i("Hide Everything");
   }

   public boolean r() {
      return this.F() && this.o.i("Pause");
   }

   @Override
   public String getStatus() {
      return y8.J(this);
   }
}

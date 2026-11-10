package com.larpclient.module;

import com.larpclient.CryptoPetsModule;
import com.larpclient.PetsModule;
import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.tk;
import com.larpclient.tm;
import java.util.Set;
import net.minecraft.class_342;
import net.minecraft.class_408;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_481;

public final class lf {
   public static final int Y = 47;
   private static final Set<Class<? extends Module>> w = Set.of(
      PerformanceModule.class,
      SmoothCameraModule.class,
      StreamProofModule.class,
      WorldLyricsModule.class,
      IslandLyricsModule.class,
      FullbrightModule.class,
      ZoomModule.class,
      NoBlockRotationModule.class,
      RealisticWaterModule.class,
      WavingFoliageModule.class,
      WetGroundModule.class,
      SnowGroundModule.class,
      WalkStyleModule.class,
      PetsModule.class,
      CryptoPetsModule.class,
      EmotesModule.class,
      TimeChangerModule.class,
      AnimationsModule.class,
      ViewModelModule.class,
      GUIAnimationModule.class,
      Feature3DUIModule.class,
      WorldMenuModule.class,
      LineGlyphsModule.class,
      PearlParticlesModule.class,
      SkyShaderModule.class,
      CustomSkyModule.class,
      SwordModelsModule.class,
      PlayerTrailModule.class,
      HitFXModule.class,
      KillFXModule.class,
      ItemPhysicModule.class,
      EchoHandModule.class,
      FlameHandsModule.class,
      HitSoundsModule.class,
      PlayerModelModule.class,
      SkinChangerModule.class,
      MenuBackgroundModule.class,
      CapesModule.class,
      JumpFXModule.class,
      BlockOverlayModule.class,
      SelfVisualsModule.class,
      WorldColorModule.class,
      MotionBlurModule.class,
      AmbientParticlesModule.class,
      AstralWeaponTrailsModule.class,
      CelestialWeatherModule.class,
      FogModule.class,
      CustomCrosshairModule.class,
      ClickPulseModule.class,
      HUDModule.class,
      StretchedResModule.class,
      ChinaHatModule.class,
      WingsModule.class,
      HeadlessKorbloxModule.class,
      HideArmourModule.class,
      LARPModule.class
   );
   private static volatile boolean K;

   private lf() {
   }

   public static boolean l() {
      return K;
   }

   public static void M(boolean var0) {
      K = var0;
   }

   public static boolean D(Module var0) {
      return w.contains(var0.getClass());
   }

   public static boolean Y(Module var0) {
      return K && !D(var0);
   }

   public static boolean D(class_437 var0) {
      if (var0 == null) {
         return true;
      } else if (var0 instanceof class_408) {
         return var0.method_25399() instanceof class_342 var4 && var4.method_1882().matches("/*");
      } else if (var0 instanceof tk var3) {
         return !var3.R();
      } else {
         return var0 instanceof tm var1 ? !var1.s() : var0 instanceof class_433 || var0 instanceof class_465 && !(var0 instanceof class_481);
      }
   }

   public static final class v {
      public static final long R = 400L;
      private int d;
      private long x;

      public boolean u(int var1, int var2, long var3) {
         if (var1 != var2) {
            if (var1 < 340 || var1 > 347) {
               this.d = 0;
            }

            return false;
         } else {
            this.d = this.d > 0 && var3 - this.x <= 400L ? this.d + 1 : 1;
            this.x = var3;
            if (this.d < 3) {
               return false;
            }

            this.d = 0;
            return true;
         }
      }

      public void u() {
         this.d = 0;
      }
   }
}

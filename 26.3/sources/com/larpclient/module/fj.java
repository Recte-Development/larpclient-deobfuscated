package com.larpclient.module;

import com.larpclient.CryptoPetsModule;
import com.larpclient.PetsModule;
import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.f8;
import java.util.Set;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

public final class fj {
   public static final int E = 47;
   private static final Set<Class<? extends Module>> U = Set.of(
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
   private static volatile boolean O;

   private fj() {
   }

   public static boolean r() {
      return O;
   }

   public static void l(boolean var0) {
      O = var0;
   }

   public static boolean G(Module var0) {
      return U.contains(var0.getClass());
   }

   public static boolean r(Module var0) {
      return O && !G(var0);
   }

   public static boolean G(Screen var0) {
      if (var0 == null) {
         return true;
      } else if (var0 instanceof ChatScreen) {
         return var0.getFocused() instanceof EditBox var4 && var4.getValue().matches("/*");
      } else if (var0 instanceof f8 var3) {
         return !var3.g();
      } else {
         return var0 instanceof com.larpclient.fj var1
            ? !var1.d()
            : var0 instanceof PauseScreen || var0 instanceof AbstractContainerScreen && !(var0 instanceof CreativeModeInventoryScreen);
      }
   }

   public static final class b {
      public static final long f = 400L;
      private int q;
      private long b;

      public boolean T(int var1, int var2, long var3) {
         if (var1 != var2) {
            if (var1 < 340 || var1 > 347) {
               this.q = 0;
            }

            return false;
         } else {
            this.q = this.q > 0 && var3 - this.b <= 400L ? this.q + 1 : 1;
            this.b = var3;
            if (this.q < 3) {
               return false;
            }

            this.q = 0;
            return true;
         }
      }

      public void j() {
         this.q = 0;
      }
   }
}

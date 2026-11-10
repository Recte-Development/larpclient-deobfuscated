package com.larpclient.module;

import com.larpclient.CryptoPetsModule;
import com.larpclient.PetsModule;
import com.larpclient.RealisticWaterModule;
import com.larpclient.SnowGroundModule;
import com.larpclient.WalkStyleModule;
import com.larpclient.WavingFoliageModule;
import com.larpclient.WetGroundModule;
import com.larpclient.hf;
import com.larpclient.hi;
import java.util.Set;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

public final class c {
   public static final int j = 47;
   private static final Set<Class<? extends Module>> e = Set.of(
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
   private static volatile boolean x;

   private c() {
   }

   public static boolean p() {
      return x;
   }

   public static void a(boolean var0) {
      x = var0;
   }

   public static boolean P(Module var0) {
      return e.contains(var0.getClass());
   }

   public static boolean W(Module var0) {
      return x && !P(var0);
   }

   public static boolean s(Screen var0) {
      if (var0 == null) {
         return true;
      } else if (var0 instanceof ChatScreen) {
         return var0.getFocused() instanceof EditBox var4 && var4.getValue().matches("/*");
      } else if (var0 instanceof hf var3) {
         return !var3.P();
      } else {
         return var0 instanceof hi var1
            ? !var1.q()
            : var0 instanceof PauseScreen || var0 instanceof AbstractContainerScreen && !(var0 instanceof CreativeModeInventoryScreen);
      }
   }

   public static final class p {
      public static final long J = 400L;
      private int t;
      private long d;

      public boolean a(int var1, int var2, long var3) {
         if (var1 != var2) {
            if (var1 < 340 || var1 > 347) {
               this.t = 0;
            }

            return false;
         } else {
            this.t = this.t > 0 && var3 - this.d <= 400L ? this.t + 1 : 1;
            this.d = var3;
            if (this.t < 3) {
               return false;
            }

            this.t = 0;
            return true;
         }
      }

      public void s() {
         this.t = 0;
      }
   }
}

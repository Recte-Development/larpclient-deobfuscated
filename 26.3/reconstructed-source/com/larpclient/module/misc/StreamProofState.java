package com.larpclient.module.misc;

import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.clickgui.LegacyClickGuiScreen;
import com.larpclient.module.exploit.NoBlockRotationModule;
import com.larpclient.module.movement.WalkStyleModule;
import com.larpclient.module.render.AmbientParticlesModule;
import com.larpclient.module.render.AnimationsModule;
import com.larpclient.module.render.AstralWeaponTrailsModule;
import com.larpclient.module.render.BlockOverlayModule;
import com.larpclient.module.render.CapesModule;
import com.larpclient.module.render.CelestialWeatherModule;
import com.larpclient.module.render.ChinaHatModule;
import com.larpclient.module.render.ClickPulseModule;
import com.larpclient.module.render.CustomCrosshairModule;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.Feature3DUIModule;
import com.larpclient.module.render.FlameHandsModule;
import com.larpclient.module.render.FogModule;
import com.larpclient.module.render.FullbrightModule;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.module.render.HeadlessKorbloxModule;
import com.larpclient.module.render.HideArmourModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.ItemPhysicModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.module.render.LineGlyphsModule;
import com.larpclient.module.render.MenuBackgroundModule;
import com.larpclient.module.render.MotionBlurModule;
import com.larpclient.module.render.PearlParticlesModule;
import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.module.render.PlayerTrailModule;
import com.larpclient.module.render.RealisticWaterModule;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.module.render.SmoothCameraModule;
import com.larpclient.module.render.SnowGroundModule;
import com.larpclient.module.render.StretchedResModule;
import com.larpclient.module.render.SwordModelsModule;
import com.larpclient.module.render.TimeChangerModule;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.module.render.WavingFoliageModule;
import com.larpclient.module.render.WetGroundModule;
import com.larpclient.module.render.WingsModule;
import com.larpclient.module.render.WorldColorModule;
import com.larpclient.module.render.ZoomModule;
import java.util.Set;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

public final class StreamProofState {
   public static final int tRIPLE_TAP_KEY = 47;
   private static final Set<Class<? extends Module>> eXEMPT_MODULES = Set.of(
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
   private static volatile boolean active;

   private StreamProofState() {
   }

   public static boolean isHidden() {
      return active;
   }

   public static void setActive(boolean var0) {
      active = var0;
   }

   public static boolean isExempt(Module var0) {
      return eXEMPT_MODULES.contains(var0.getClass());
   }

   public static boolean isHidden(Module var0) {
      return active && !isExempt(var0);
   }

   public static boolean isExempt(Screen var0) {
      if (var0 == null) {
         return true;
      } else if (var0 instanceof ChatScreen) {
         return var0.getFocused() instanceof EditBox var4 && var4.getValue().matches("/*");
      } else if (var0 instanceof ClickGuiScreen var3) {
         return !var3.finishInspectorClose();
      } else {
         return var0 instanceof LegacyClickGuiScreen var1
            ? !var1.isTextInputFocused()
            : var0 instanceof PauseScreen || var0 instanceof AbstractContainerScreen && !(var0 instanceof CreativeModeInventoryScreen);
      }
   }

   public static final class TripleTapDetector {
      public static final long tAP_WINDOW_MILLIS = 400L;
      private int tapCount;
      private long lastTapMillis;

      public boolean registerKey(int var1, int var2, long var3) {
         if (var1 != var2) {
            if (var1 < 340 || var1 > 347) {
               this.tapCount = 0;
            }

            return false;
         } else {
            this.tapCount = this.tapCount > 0 && var3 - this.lastTapMillis <= 400L ? this.tapCount + 1 : 1;
            this.lastTapMillis = var3;
            if (this.tapCount < 3) {
               return false;
            }

            this.tapCount = 0;
            return true;
         }
      }

      public void reset() {
         this.tapCount = 0;
      }
   }
}

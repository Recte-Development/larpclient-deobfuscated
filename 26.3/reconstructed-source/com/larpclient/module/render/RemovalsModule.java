package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.misc.RenderFilters;
import com.larpclient.util.misc.LarpLog;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RemovalsModule extends Module {
   private static RemovalsModule instance;
   private final Map<String, BooleanSetting> removalSettings = new LinkedHashMap<>();

   public RemovalsModule() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      instance = this;
      this.addRemoval("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.addRemoval("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.addRemoval("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.addRemoval("portal", "Portal", "Hide the portal screen overlay", false);
      this.addRemoval("fire", "Fire", "Hide the first-person fire overlay", false);
      this.addRemoval("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.addRemoval("water", "Water", "Hide the underwater overlay and water fog", false);
      this.addRemoval("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.addRemoval("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.addRemoval("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.addRemoval("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.addRemoval("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.addRemoval("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.addRemoval("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.addRemoval("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.addRemoval("lava", "Lava", "Hide lava fog", false);
      this.addRemoval("beacon", "Beacon", "Mute beacon sounds", false);
      this.addRemoval("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.addRemoval("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.addRemoval("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.addRemoval("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void addRemoval(String var1, final String var2, String var3, boolean var4) {
      this.removalSettings.put(var1, this.addSetting(new BooleanSetting(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<Setting<?>> getVisualSettings() {
      return RenderFilters.visualOptions.stream().map(this.removalSettings::get).toList();
   }

   public List<Setting<?>> getSoundSettings() {
      return RenderFilters.soundOptions.stream().map(this.removalSettings::get).toList();
   }

   public static RemovalsModule getInstance() {
      return instance;
   }

   public static boolean isRemovalEnabled(String var0) {
      if (instance == null || !instance.isEnabled()) {
         return false;
      } else if (instance != null && instance.isRuntimeActive()) {
         BooleanSetting var1 = instance.removalSettings.get(var0);
         return var1 != null && var1.getValue();
      } else {
         return false;
      }
   }

   public static boolean hasScreenEffectFilter() {
      return instance != null && instance.isEnabled() ? RenderFilters.hasScreenEffectFilter(RemovalsModule::isRemovalEnabled) : false;
   }

   public static boolean hasScreenEffectOption() {
      return instance != null && instance.isEnabled() ? RenderFilters.hasScreenEffectOption(RemovalsModule::isRemovalEnabled) : false;
   }

   public static boolean isSoundSuppressed(String var0) {
      return instance != null && instance.isEnabled() ? RenderFilters.isSoundSuppressed(var0, RemovalsModule::isRemovalEnabled) : false;
   }

   @Override
   public void onEnable() {
      LarpLog.info(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.removalSettings.entrySet().stream().filter(var0 -> var0.getValue().getValue()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      LarpLog.info("Removals", "Disabled; native visual and sound decisions restored");
   }
}

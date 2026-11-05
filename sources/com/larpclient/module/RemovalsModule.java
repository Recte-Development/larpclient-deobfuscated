package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.tm;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RemovalsModule extends Module {
   private static RemovalsModule r;
   private final Map<String, dh> s = new LinkedHashMap<>();

   public RemovalsModule() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      r = this;
      this.n("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.n("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.n("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.n("portal", "Portal", "Hide the portal screen overlay", false);
      this.n("fire", "Fire", "Hide the first-person fire overlay", false);
      this.n("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.n("water", "Water", "Hide the underwater overlay and water fog", false);
      this.n("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.n("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.n("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.n("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.n("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.n("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.n("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.n("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.n("lava", "Lava", "Hide lava fog", false);
      this.n("beacon", "Beacon", "Mute beacon sounds", false);
      this.n("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.n("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.n("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.n("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void n(String var1, final String var2, String var3, boolean var4) {
      this.s.put(var1, this.addSetting(new dh(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<df<?>> x() {
      return tm.T.stream().map(this.s::get).toList();
   }

   public List<df<?>> L() {
      return tm.N.stream().map(this.s::get).toList();
   }

   public static RemovalsModule e() {
      return r;
   }

   public static boolean r(String var0) {
      if (r == null || !r.isEnabled()) {
         return false;
      } else if (r != null && r.isRuntimeActive()) {
         dh var1 = r.s.get(var0);
         return var1 != null && var1.P();
      } else {
         return false;
      }
   }

   public static boolean A() {
      return r != null && r.isEnabled() ? tm.z(RemovalsModule::r) : false;
   }

   public static boolean D() {
      return r != null && r.isEnabled() ? tm.h(RemovalsModule::r) : false;
   }

   public static boolean e(String var0) {
      return r != null && r.isEnabled() ? tm.Z(var0, RemovalsModule::r) : false;
   }

   @Override
   public void onEnable() {
      mt.K(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.s.entrySet().stream().filter(var0 -> var0.getValue().P()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      mt.K("Removals", "Disabled; native visual and sound decisions restored");
   }
}

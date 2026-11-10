package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.al;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RemovalsModule extends Module {
   private static RemovalsModule l;
   private final Map<String, m> O = new LinkedHashMap<>();

   public RemovalsModule() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      l = this;
      this.Q("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.Q("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.Q("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.Q("portal", "Portal", "Hide the portal screen overlay", false);
      this.Q("fire", "Fire", "Hide the first-person fire overlay", false);
      this.Q("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.Q("water", "Water", "Hide the underwater overlay and water fog", false);
      this.Q("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.Q("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.Q("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.Q("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.Q("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.Q("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.Q("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.Q("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.Q("lava", "Lava", "Hide lava fog", false);
      this.Q("beacon", "Beacon", "Mute beacon sounds", false);
      this.Q("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.Q("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.Q("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.Q("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void Q(String var1, final String var2, String var3, boolean var4) {
      this.O.put(var1, this.addSetting(new m(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<r<?>> K() {
      return al.b.stream().map(this.O::get).toList();
   }

   public List<r<?>> S() {
      return al.i.stream().map(this.O::get).toList();
   }

   public static RemovalsModule T() {
      return l;
   }

   public static boolean G(String var0) {
      if (l == null || !l.isEnabled()) {
         return false;
      } else if (l != null && l.isRuntimeActive()) {
         m var1 = l.O.get(var0);
         return var1 != null && var1.getValue();
      } else {
         return false;
      }
   }

   public static boolean U() {
      return l != null && l.isEnabled() ? al.o(RemovalsModule::G) : false;
   }

   public static boolean w() {
      return l != null && l.isEnabled() ? al.b(RemovalsModule::G) : false;
   }

   public static boolean d(String var0) {
      return l != null && l.isEnabled() ? al.Z(var0, RemovalsModule::G) : false;
   }

   @Override
   public void onEnable() {
      _q.z(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.O.entrySet().stream().filter(var0 -> var0.getValue().getValue()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      _q.z("Removals", "Disabled; native visual and sound decisions restored");
   }
}

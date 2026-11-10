package com.larpclient.module;

import com.larpclient.m1;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RemovalsModule extends Module {
   private static RemovalsModule O;
   private final Map<String, s> q = new LinkedHashMap<>();

   public RemovalsModule() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      O = this;
      this.x("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.x("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.x("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.x("portal", "Portal", "Hide the portal screen overlay", false);
      this.x("fire", "Fire", "Hide the first-person fire overlay", false);
      this.x("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.x("water", "Water", "Hide the underwater overlay and water fog", false);
      this.x("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.x("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.x("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.x("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.x("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.x("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.x("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.x("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.x("lava", "Lava", "Hide lava fog", false);
      this.x("beacon", "Beacon", "Mute beacon sounds", false);
      this.x("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.x("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.x("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.x("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void x(String var1, final String var2, String var3, boolean var4) {
      this.q.put(var1, this.addSetting(new s(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<q<?>> Y() {
      return m1.d.stream().map(this.q::get).toList();
   }

   public List<q<?>> b() {
      return m1.h.stream().map(this.q::get).toList();
   }

   public static RemovalsModule U() {
      return O;
   }

   public static boolean n(String var0) {
      if (O == null || !O.isEnabled()) {
         return false;
      } else if (O != null && O.isRuntimeActive()) {
         s var1 = O.q.get(var0);
         return var1 != null && var1.getValue();
      } else {
         return false;
      }
   }

   public static boolean U() {
      return O != null && O.isEnabled() ? m1.V(RemovalsModule::n) : false;
   }

   public static boolean A() {
      return O != null && O.isEnabled() ? m1.a(RemovalsModule::n) : false;
   }

   public static boolean e(String var0) {
      return O != null && O.isEnabled() ? m1.N(var0, RemovalsModule::n) : false;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.q.entrySet().stream().filter(var0 -> var0.getValue().getValue()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      com.larpclient.h.A("Removals", "Disabled; native visual and sound decisions restored");
   }
}

package com.larpclient.module;

import com.larpclient.ev;
import com.larpclient.r4;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RemovalsModule extends Module {
   private static RemovalsModule F;
   private final Map<String, ln> c = new LinkedHashMap<>();

   public RemovalsModule() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      F = this;
      this.j("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.j("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.j("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.j("portal", "Portal", "Hide the portal screen overlay", false);
      this.j("fire", "Fire", "Hide the first-person fire overlay", false);
      this.j("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.j("water", "Water", "Hide the underwater overlay and water fog", false);
      this.j("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.j("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.j("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.j("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.j("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.j("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.j("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.j("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.j("lava", "Lava", "Hide lava fog", false);
      this.j("beacon", "Beacon", "Mute beacon sounds", false);
      this.j("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.j("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.j("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.j("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void j(String var1, final String var2, String var3, boolean var4) {
      this.c.put(var1, this.addSetting(new ln(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<l6<?>> e() {
      return ev.i.stream().map(this.c::get).toList();
   }

   public List<l6<?>> w() {
      return ev.E.stream().map(this.c::get).toList();
   }

   public static RemovalsModule o() {
      return F;
   }

   public static boolean s(String var0) {
      if (F == null || !F.isEnabled()) {
         return false;
      } else if (F != null && F.isRuntimeActive()) {
         ln var1 = F.c.get(var0);
         return var1 != null && var1.getValue();
      } else {
         return false;
      }
   }

   public static boolean O() {
      return F != null && F.isEnabled() ? ev.y(RemovalsModule::s) : false;
   }

   public static boolean L() {
      return F != null && F.isEnabled() ? ev.v(RemovalsModule::s) : false;
   }

   public static boolean X(String var0) {
      return F != null && F.isEnabled() ? ev.O(var0, RemovalsModule::s) : false;
   }

   @Override
   public void onEnable() {
      r4.V(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.c.entrySet().stream().filter(var0 -> var0.getValue().getValue()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      r4.V("Removals", "Disabled; native visual and sound decisions restored");
   }
}

package com.larpclient.module;

import com.larpclient.q0;
import com.larpclient.q8;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class RemovalsModule extends Module {
   private static RemovalsModule z;
   private final Map<String, l3> j = new LinkedHashMap<>();

   public RemovalsModule() {
      super("Removals", "Hide screen effects and mute ambient sounds", Category.VISUALS, -1);
      z = this;
      this.h("hurtCam", "Hurt Cam", "Hide hurt camera shake", true);
      this.h("scoreboard", "Scoreboard", "Hide the sidebar scoreboard", false);
      this.h("bossBar", "Boss Bar", "Hide boss health bars", false);
      this.h("portal", "Portal", "Hide the portal screen overlay", false);
      this.h("fire", "Fire", "Hide the first-person fire overlay", false);
      this.h("clip", "Clip", "Hide the in-wall overlay and third-person camera collision", false);
      this.h("water", "Water", "Hide the underwater overlay and water fog", false);
      this.h("nausea", "Nausea", "Hide nausea distortion and status-effect fog", false);
      this.h("blindness", "Blindness", "Hide status-effect fog and nausea distortion", false);
      this.h("darkness", "Darkness", "Hide status-effect fog and nausea distortion", false);
      this.h("pumpkin", "Pumpkin", "Hide the carved pumpkin overlay", false);
      this.h("fov", "FOV", "Hide dynamic field-of-view changes", false);
      this.h("breakParticles", "Break Particles", "Hide block breaking and hit particles", false);
      this.h("weather", "Weather", "Hide precipitation and rain splash particles", false);
      this.h("glowing", "Glowing", "Hide vanilla glowing outlines", false);
      this.h("lava", "Lava", "Hide lava fog", false);
      this.h("beacon", "Beacon", "Mute beacon sounds", false);
      this.h("weatherSound", "Weather Sound", "Mute rain and thunder sounds", false);
      this.h("phantoms", "Phantoms", "Mute phantom sounds and parrot phantom imitations", false);
      this.h("waterSound", "Water Sound", "Mute ambient underwater sounds", false);
      this.h("lavaSound", "Lava Sound", "Mute lava bubbling and popping", false);
   }

   private void h(String var1, final String var2, String var3, boolean var4) {
      this.j.put(var1, this.addSetting(new l3(var1, var3, var4) {
         @Override
         public String getDisplayName() {
            return var2;
         }
      }));
   }

   public List<lb<?>> C() {
      return q0.n.stream().map(this.j::get).toList();
   }

   public List<lb<?>> V() {
      return q0.V.stream().map(this.j::get).toList();
   }

   public static RemovalsModule o() {
      return z;
   }

   public static boolean o(String var0) {
      if (z == null || !z.isEnabled()) {
         return false;
      } else if (z != null && z.isRuntimeActive()) {
         l3 var1 = z.j.get(var0);
         return var1 != null && var1.getValue();
      } else {
         return false;
      }
   }

   public static boolean o() {
      return z != null && z.isEnabled() ? q0.z(RemovalsModule::o) : false;
   }

   public static boolean e() {
      return z != null && z.isEnabled() ? q0.X(RemovalsModule::o) : false;
   }

   public static boolean J(String var0) {
      return z != null && z.isEnabled() ? q0.h(var0, RemovalsModule::o) : false;
   }

   @Override
   public void onEnable() {
      q8.G(
         "Removals",
         "Enabled; selected={}; existing camera and weather preferences retained",
         this.j.entrySet().stream().filter(var0 -> var0.getValue().getValue()).map(Entry::getKey).toList()
      );
   }

   @Override
   public void onDisable() {
      q8.G("Removals", "Disabled; native visual and sound decisions restored");
   }
}

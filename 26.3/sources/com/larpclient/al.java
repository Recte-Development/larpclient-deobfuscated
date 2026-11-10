package com.larpclient;

import java.util.List;
import java.util.function.Predicate;

public final class al {
   public static final List<String> b = List.of(
      "hurtCam",
      "scoreboard",
      "bossBar",
      "portal",
      "fire",
      "clip",
      "water",
      "nausea",
      "blindness",
      "darkness",
      "pumpkin",
      "fov",
      "breakParticles",
      "weather",
      "glowing",
      "lava"
   );
   public static final List<String> i = List.of("beacon", "weatherSound", "phantoms", "waterSound", "lavaSound");

   private al() {
   }

   public static boolean o(Predicate<String> var0) {
      return var0.test("nausea") || var0.test("blindness") || var0.test("darkness");
   }

   public static boolean b(Predicate<String> var0) {
      return o(var0);
   }

   public static boolean Z(String var0, Predicate<String> var1) {
      if (var0 == null) {
         return false;
      }

      int var3 = var0.indexOf(58);
      String var2;
      if (var3 < 0) {
         var2 = var0;
      } else {
         if (!var0.substring(0, var3).equals("minecraft")) {
            return false;
         }

         var2 = var0.substring(var3 + 1);
      }
      return (boolean)(switch (var2) {
         case "block.beacon.activate", "block.beacon.ambient", "block.beacon.power_select", "block.beacon.deactivate" -> var1.test("beacon");
         case "weather.rain", "weather.rain.above", "entity.lightning_bolt.thunder" -> var1.test("weatherSound");
         case "entity.parrot.imitate.phantom", "entity.phantom.ambient", "entity.phantom.bite", "entity.phantom.flap", "entity.phantom.death", "entity.phantom.hurt", "entity.phantom.swoop" -> var1.test(
            "phantoms"
         );
         case "ambient.underwater.enter", "ambient.underwater.exit", "ambient.underwater.loop", "ambient.underwater.loop.additions", "ambient.underwater.loop.additions.rare", "ambient.underwater.loop.additions.ultra_rare" -> var1.test(
            "waterSound"
         );
         case "block.lava.ambient", "block.lava.pop" -> var1.test("lavaSound");
         default -> 0;
      });
   }
}

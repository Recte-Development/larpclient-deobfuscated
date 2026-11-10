package com.larpclient;

import com.larpclient.module.Category;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.class_10799;
import net.minecraft.class_2960;
import net.minecraft.class_332;

public final class lx {
   public static final class_2960 S = f("toggle-left");
   public static final class_2960 h = f("toggle-right");
   public static final class_2960 s = f("chevron-down");
   public static final class_2960 G = f("chevron-up");
   private static final Map<String, class_2960> Q = new HashMap<>();

   public static class_2960 n(String var0) {
      return Q.computeIfAbsent(var0, lx::f);
   }

   public static String Z(Category var0) {
      return switch (var0) {
         case COMBAT -> "swords";
         case MOVEMENT -> "footprints";
         case RENDER -> "eye";
         case VISUALS -> "sparkles";
         case PLAYER -> "user";
      };
   }

   public static String j(String var0) {
      return switch (var0) {
         case "watermark" -> "gauge";
         case "info" -> "map-pin";
         case "speed", "cooldowns" -> "gauge";
         case "staffs" -> "user";
         case "inventory" -> "panels-top-left";
         case "radar" -> "crosshair";
         case "music", "island" -> "bell";
         case "modules" -> "list";
         case "target", "crosshair" -> "crosshair";
         case "keybinds", "keystrokes" -> "keyboard";
         case "effects" -> "flask-conical";
         case "armor" -> "shield";
         case "casino" -> "store";
         case "miner", "builder", "pilot", "hunter" -> "scan";
         default -> "bell";
      };
   }

   private lx() {
   }

   public static class_2960 f(String var0) {
      return class_2960.method_60655("larp-client", "textures/gui/icons/" + var0 + ".png");
   }

   public static void N(class_332 var0, class_2960 var1, int var2, int var3, int var4, int var5) {
      var0.method_25293(class_10799.field_56883, var1, var2, var3, 0.0F, 0.0F, var4, var4, 96, 96, 96, 96, fp.R(var5));
   }
}

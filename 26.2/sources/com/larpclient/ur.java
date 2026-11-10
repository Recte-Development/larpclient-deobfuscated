package com.larpclient;

import com.larpclient.module.Category;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class ur {
   public static final Identifier U = g("toggle-left");
   public static final Identifier o = g("toggle-right");
   public static final Identifier A = g("chevron-down");
   public static final Identifier e = g("chevron-up");
   private static final Map<String, Identifier> M = new HashMap<>();

   public static Identifier V(String var0) {
      return M.computeIfAbsent(var0, ur::g);
   }

   public static String e(Category var0) {
      return switch (var0) {
         case COMBAT -> "swords";
         case MOVEMENT -> "footprints";
         case RENDER -> "eye";
         case VISUALS -> "sparkles";
         case PLAYER -> "user";
      };
   }

   public static String t(String var0) {
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

   private ur() {
   }

   public static Identifier g(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "textures/gui/icons/" + var0 + ".png");
   }

   public static void O(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5) {
      var0.blit(RenderPipelines.GUI_TEXTURED, var1, var2, var3, 0.0F, 0.0F, var4, var4, 96, 96, 96, 96, ua.I(var5));
   }
}

package com.larpclient;

import com.larpclient.module.Category;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class ou {
   public static final Identifier z = G("toggle-left");
   public static final Identifier M = G("toggle-right");
   public static final Identifier k = G("chevron-down");
   public static final Identifier i = G("chevron-up");
   private static final Map<String, Identifier> B = new HashMap<>();

   public static Identifier e(String var0) {
      return B.computeIfAbsent(var0, ou::G);
   }

   public static String d(Category var0) {
      return switch (var0) {
         case COMBAT -> "swords";
         case MOVEMENT -> "footprints";
         case RENDER -> "eye";
         case VISUALS -> "sparkles";
         case PLAYER -> "user";
      };
   }

   public static String Y(String var0) {
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
         default -> "bell";
      };
   }

   private ou() {
   }

   public static Identifier G(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "textures/gui/icons/" + var0 + ".png");
   }

   public static void u(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5) {
      var0.blit(RenderPipelines.GUI_TEXTURED, var1, var2, var3, 0.0F, 0.0F, var4, var4, 96, 96, 96, 96, r.D(var5));
   }
}

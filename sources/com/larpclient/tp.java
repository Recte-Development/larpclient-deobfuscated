package com.larpclient;

import com.larpclient.module.Category;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class tp {
   public static final Identifier f = D("toggle-left");
   public static final Identifier z = D("toggle-right");
   public static final Identifier U = D("chevron-down");
   public static final Identifier b = D("chevron-up");
   private static final Map<String, Identifier> F = new HashMap<>();

   public static Identifier j(String var0) {
      return F.computeIfAbsent(var0, tp::D);
   }

   public static String Y(Category var0) {
      return switch (var0) {
         case COMBAT -> "swords";
         case MOVEMENT -> "footprints";
         case RENDER -> "eye";
         case VISUALS -> "sparkles";
         case PLAYER -> "user";
      };
   }

   public static String M(String var0) {
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

   private tp() {
   }

   public static Identifier D(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "textures/gui/icons/" + var0 + ".png");
   }

   public static void N(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5) {
      var0.blit(RenderPipelines.GUI_TEXTURED, var1, var2, var3, 0.0F, 0.0F, var4, var4, 96, 96, 96, 96, de.h(var5));
   }
}

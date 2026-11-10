package com.larpclient.module;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.client.Minecraft;

public final class NoBlockRotationModule extends Module {
   private static volatile NoBlockRotationModule j;
   public static final long Y = 42L;
   private static volatile boolean E;
   private static volatile boolean a;

   public NoBlockRotationModule() {
      super("No Block Rotation", "Keep randomized block textures facing consistently", Category.RENDER, -1);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> W());
      j = this;
   }

   public static boolean J() {
      return j != null && j.isEnabled() ? E : false;
   }

   public static boolean R() {
      return a;
   }

   @Override
   public void onEnable() {
      E = (boolean)1;
      W();
      this.M();
   }

   @Override
   public void onDisable() {
      E = (boolean)0;
      W();
      this.M();
   }

   private static void W() {
      boolean var0 = J();
      if (var0 != a) {
         a = var0;
         com.larpclient.h.U("Terrain", "No Block Rotation uniform seed {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   private void M() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null && var1.levelExtractor != null) {
         var1.levelExtractor.allChanged();
         com.larpclient.h.U("Terrain", "No Block Rotation: {} uniform seed; rebuilding chunk models", E ? "enabled" : "disabled");
      }
   }
}

package com.larpclient.module;

import com.larpclient._q;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.client.Minecraft;

public final class NoBlockRotationModule extends Module {
   private static volatile NoBlockRotationModule j;
   public static final long x = 42L;
   private static volatile boolean y;
   private static volatile boolean c;

   public NoBlockRotationModule() {
      super("No Block Rotation", "Keep randomized block textures facing consistently", Category.RENDER, -1);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> H());
      j = this;
   }

   public static boolean B() {
      return j != null && j.isEnabled() ? y : false;
   }

   public static boolean v() {
      return c;
   }

   @Override
   public void onEnable() {
      y = (boolean)1;
      H();
      this.q();
   }

   @Override
   public void onDisable() {
      y = (boolean)0;
      H();
      this.q();
   }

   private static void H() {
      boolean var0 = B();
      if (var0 != c) {
         c = var0;
         _q.c("Terrain", "No Block Rotation uniform seed {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   private void q() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null && var1.levelExtractor != null) {
         var1.levelExtractor.allChanged();
         _q.c("Terrain", "No Block Rotation: {} uniform seed; rebuilding chunk models", y ? "enabled" : "disabled");
      }
   }
}

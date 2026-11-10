package com.larpclient.module;

import com.larpclient.q8;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.minecraft.class_310;

public final class NoBlockRotationModule extends Module {
   private static volatile NoBlockRotationModule j;
   public static final long C = 42L;
   private static volatile boolean p;
   private static volatile boolean b;

   public NoBlockRotationModule() {
      super("No Block Rotation", "Keep randomized block textures facing consistently", Category.RENDER, -1);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> M());
      j = this;
   }

   public static boolean J() {
      return j != null && j.isEnabled() ? p : false;
   }

   public static boolean M() {
      return b;
   }

   @Override
   public void onEnable() {
      p = (boolean)1;
      M();
      this.Z();
   }

   @Override
   public void onDisable() {
      p = (boolean)0;
      M();
      this.Z();
   }

   private static void M() {
      boolean var0 = J();
      if (var0 != b) {
         b = var0;
         q8.v("Terrain", "No Block Rotation uniform seed {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   private void Z() {
      class_310 var1 = getMc();
      if (var1 != null && var1.field_1687 != null && var1.field_1769 != null) {
         var1.field_1769.method_3279();
         q8.v("Terrain", "No Block Rotation: {} uniform seed; rebuilding chunk models", p ? "enabled" : "disabled");
      }
   }
}

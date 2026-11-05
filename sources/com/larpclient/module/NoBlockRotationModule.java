package com.larpclient.module;

import com.larpclient.r4;
import net.minecraft.client.Minecraft;

public final class NoBlockRotationModule extends Module {
   private static volatile NoBlockRotationModule K;
   public static final long j = 42L;
   private static volatile boolean F;

   public NoBlockRotationModule() {
      super("No Block Rotation", "Keep randomized block textures facing consistently", Category.RENDER, -1);
      K = this;
   }

   public static boolean F() {
      return K != null && K.isEnabled() ? F : false;
   }

   @Override
   public void onEnable() {
      F = (boolean)1;
      this.O();
   }

   @Override
   public void onDisable() {
      F = (boolean)0;
      this.O();
   }

   private void O() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null && var1.levelExtractor != null) {
         var1.levelExtractor.allChanged();
         r4.g("Terrain", "No Block Rotation: {} uniform seed; rebuilding chunk models", F ? "enabled" : "disabled");
      }
   }
}

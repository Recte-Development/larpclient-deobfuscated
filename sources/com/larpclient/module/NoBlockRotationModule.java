package com.larpclient.module;

import com.larpclient.mt;
import net.minecraft.client.Minecraft;

public final class NoBlockRotationModule extends Module {
   private static volatile NoBlockRotationModule b;
   public static final long R = 42L;
   private static volatile boolean F;

   public NoBlockRotationModule() {
      super("No Block Rotation", "Keep randomized block textures facing consistently", Category.RENDER, -1);
      b = this;
   }

   public static boolean D() {
      return b != null && b.isEnabled() ? F : false;
   }

   @Override
   public void onEnable() {
      F = (boolean)1;
      this.M();
   }

   @Override
   public void onDisable() {
      F = (boolean)0;
      this.M();
   }

   private void M() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null && var1.levelExtractor != null) {
         var1.levelExtractor.allChanged();
         mt.U("Terrain", "No Block Rotation: {} uniform seed; rebuilding chunk models", F ? "enabled" : "disabled");
      }
   }
}

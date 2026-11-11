package com.larpclient.render.entity;

import com.larpclient.config.GradientOutlineSettings;
import com.larpclient.module.render.HandChamsModule;
import net.minecraft.client.Minecraft;

public final class HandChamsRenderer {
   private static final GradientChamsRenderer layer = new GradientChamsRenderer("HandChams");
   private static GradientOutlineSettings.Snapshot activeCapture;
   private static Object level;
   private static Object player;

   public static boolean isActive() {
      Minecraft var0 = Minecraft.getInstance();
      HandChamsModule var1 = HandChamsModule.getInstance();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator();
   }

   public static boolean begin() {
      if (!isActive()) {
         reset();
         return false;
      }

      Minecraft var0 = Minecraft.getInstance();
      if (level != var0.level || player != var0.player) {
         reset();
      }

      level = var0.level;
      player = var0.player;
      activeCapture = HandChamsModule.getInstance().outlineSettings.snapshot(false);
      if (layer.captureHandScene()) {
         return true;
      }

      activeCapture = null;
      return false;
   }

   public static void end() {
      if (activeCapture != null) {
         if (isActive() && level == Minecraft.getInstance().level && player == Minecraft.getInstance().player) {
            GradientOutlineSettings.Snapshot var0 = activeCapture;
            activeCapture = null;
            layer.compositeHand(var0);
         } else {
            reset();
         }
      }
   }

   public static void validate() {
      if (!isActive() || level != null && (level != Minecraft.getInstance().level || player != Minecraft.getInstance().player)) {
         reset();
      }
   }

   public static void reset() {
      activeCapture = null;
      player = null;
      level = null;
      layer.close();
   }

   private HandChamsRenderer() {
   }
}

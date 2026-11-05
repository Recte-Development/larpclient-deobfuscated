package com.larpclient;

import com.larpclient.module.HandChamsModule;
import net.minecraft.client.Minecraft;

public final class dy {
   private static final fy g = new fy("HandChams");
   private static rg.q t;
   private static Object p;
   private static Object G;

   public static boolean E() {
      Minecraft var0 = Minecraft.getInstance();
      HandChamsModule var1 = HandChamsModule.w();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator();
   }

   public static boolean t() {
      if (!E()) {
         z();
         return false;
      }

      Minecraft var0 = Minecraft.getInstance();
      if (p != var0.level || G != var0.player) {
         z();
      }

      p = var0.level;
      G = var0.player;
      t = HandChamsModule.w().S.I(false);
      if (g.j()) {
         return true;
      }

      t = null;
      return false;
   }

   public static void c() {
      if (t != null) {
         if (E() && p == Minecraft.getInstance().level && G == Minecraft.getInstance().player) {
            rg.q var0 = t;
            t = null;
            g.W(var0);
         } else {
            z();
         }
      }
   }

   public static void K() {
      if (!E() || p != null && (p != Minecraft.getInstance().level || G != Minecraft.getInstance().player)) {
         z();
      }
   }

   public static void z() {
      t = null;
      G = null;
      p = null;
      g.close();
   }

   private dy() {
   }
}

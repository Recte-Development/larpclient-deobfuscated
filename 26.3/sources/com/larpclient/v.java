package com.larpclient;

import com.larpclient.module.HandChamsModule;
import net.minecraft.client.Minecraft;

public final class v {
   private static final i4 b = new i4("HandChams");
   private static q6.u i;
   private static Object C;
   private static Object z;

   public static boolean t() {
      Minecraft var0 = Minecraft.getInstance();
      HandChamsModule var1 = HandChamsModule.q();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator();
   }

   public static boolean l() {
      if (!t()) {
         H();
         return false;
      }

      Minecraft var0 = Minecraft.getInstance();
      if (C != var0.level || z != var0.player) {
         H();
      }

      C = var0.level;
      z = var0.player;
      i = HandChamsModule.q().T.g(false);
      if (b.d()) {
         return true;
      }

      i = null;
      return false;
   }

   public static void c() {
      if (i != null) {
         if (t() && C == Minecraft.getInstance().level && z == Minecraft.getInstance().player) {
            q6.u var0 = i;
            i = null;
            b.r(var0);
         } else {
            H();
         }
      }
   }

   public static void n() {
      if (!t() || C != null && (C != Minecraft.getInstance().level || z != Minecraft.getInstance().player)) {
         H();
      }
   }

   public static void H() {
      i = null;
      z = null;
      C = null;
      b.close();
   }

   private v() {
   }
}

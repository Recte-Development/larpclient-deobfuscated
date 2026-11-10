package com.larpclient;

import com.larpclient.module.HandChamsModule;
import net.minecraft.client.Minecraft;

public final class be {
   private static final g1 i = new g1("HandChams");
   private static bl.d L;
   private static Object H;
   private static Object h;

   public static boolean l() {
      Minecraft var0 = Minecraft.getInstance();
      HandChamsModule var1 = HandChamsModule.Y();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator();
   }

   public static boolean Q() {
      if (!l()) {
         o();
         return false;
      }

      Minecraft var0 = Minecraft.getInstance();
      if (H != var0.level || h != var0.player) {
         o();
      }

      H = var0.level;
      h = var0.player;
      L = HandChamsModule.Y().F.H(false);
      if (i.D()) {
         return true;
      }

      L = null;
      return false;
   }

   public static void u() {
      if (L != null) {
         if (l() && H == Minecraft.getInstance().level && h == Minecraft.getInstance().player) {
            bl.d var0 = L;
            L = null;
            i.a(var0);
         } else {
            o();
         }
      }
   }

   public static void O() {
      if (!l() || H != null && (H != Minecraft.getInstance().level || h != Minecraft.getInstance().player)) {
         o();
      }
   }

   public static void o() {
      L = null;
      h = null;
      H = null;
      i.close();
   }

   private be() {
   }
}

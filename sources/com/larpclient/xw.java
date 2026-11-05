package com.larpclient;

import com.larpclient.module.HandChamsModule;
import net.minecraft.client.Minecraft;

public final class xw {
   private static final m6 g = new m6("HandChams");
   private static p3.p E;
   private static Object X;
   private static Object W;

   public static boolean C() {
      Minecraft var0 = Minecraft.getInstance();
      HandChamsModule var1 = HandChamsModule.e();
      return var1 != null
         && var1.isRuntimeActive()
         && var0.level != null
         && var0.player != null
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !var0.player.isSleeping()
         && !var0.player.isSpectator();
   }

   public static boolean z() {
      if (!C()) {
         F();
         return false;
      }

      Minecraft var0 = Minecraft.getInstance();
      if (X != var0.level || W != var0.player) {
         F();
      }

      X = var0.level;
      W = var0.player;
      E = HandChamsModule.e().i.a(false);
      if (g.Y()) {
         return true;
      }

      E = null;
      return false;
   }

   public static void B() {
      if (E != null) {
         if (C() && X == Minecraft.getInstance().level && W == Minecraft.getInstance().player) {
            p3.p var0 = E;
            E = null;
            g.V(var0);
         } else {
            F();
         }
      }
   }

   public static void j() {
      if (!C() || X != null && (X != Minecraft.getInstance().level || W != Minecraft.getInstance().player)) {
         F();
      }
   }

   public static void F() {
      E = null;
      W = null;
      X = null;
      g.close();
   }

   private xw() {
   }
}

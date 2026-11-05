package com.larpclient;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public final class ek {
   private static final m6 r = new m6("ViewModel outline");
   private static InteractionHand y;
   private static InteractionHand k;
   private static boolean q;

   public static void z(InteractionHand var0, InteractionHand var1) {
      y = var0;
      k = var1;
   }

   private static boolean p() {
      Minecraft var0 = Minecraft.getInstance();
      return x3.B() && var0.options.getCameraType().isFirstPerson() && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden;
   }

   public static boolean q() {
      if (!p()) {
         Q();
         return false;
      } else {
         q = (boolean)1;
         return r.Y();
      }
   }

   public static void c() {
      if (!p()) {
         Q();
      } else {
         Window var0 = Minecraft.getInstance().getWindow();
         double var1 = (double)var0.getWidth() / Math.max(1, var0.getGuiScaledWidth());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil(var3 * 2.0F) + 2;
         ArrayList var5 = new ArrayList(2);

         for (InteractionHand var9 : InteractionHand.values()) {
            x3.n var10 = x3.i(var9);
            if (var10 != null) {
               int var11 = var9 == y ? _3.A(f.C, 0.95) : (var9 == k ? -922746881 : 1627389951);
               var5.add(
                  new m6.h(
                     (int)Math.floor(var10.x() * var1) - var4,
                     (int)Math.floor(var10.y() * var1) - var4,
                     (int)Math.ceil(var10.width() * var1) + var4 * 2,
                     (int)Math.ceil(var10.height() * var1) + var4 * 2,
                     new p3.p(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         r.b(var5);
      }
   }

   public static void Q() {
      if (q) {
         q = (boolean)0;
         k = null;
         y = null;
         r.close();
      }
   }

   private ek() {
   }
}

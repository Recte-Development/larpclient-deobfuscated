package com.larpclient;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public final class bp {
   private static final i4 S = new i4("ViewModel outline");
   private static InteractionHand p;
   private static InteractionHand n;
   private static boolean J;

   public static void C(InteractionHand var0, InteractionHand var1) {
      p = var0;
      n = var1;
   }

   private static boolean X() {
      Minecraft var0 = Minecraft.getInstance();
      return jp.N() && var0.options.getCameraType().isFirstPerson() && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden;
   }

   public static boolean M() {
      if (!X()) {
         b();
         return false;
      } else {
         J = (boolean)1;
         return S.d();
      }
   }

   public static void k() {
      if (!X()) {
         b();
      } else {
         Window var0 = Minecraft.getInstance().getWindow();
         double var1 = (double)var0.getWidth() / Math.max(1, var0.getGuiScaledWidth());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil(var3 * 2.0F) + 2;
         ArrayList var5 = new ArrayList(2);

         for (InteractionHand var9 : InteractionHand.values()) {
            jp.c var10 = jp.t(var9);
            if (var10 != null) {
               int var11 = var9 == p ? vv.N(c.y, 0.95) : (var9 == n ? -922746881 : 1627389951);
               var5.add(
                  new i4.q(
                     (int)Math.floor(var10.x() * var1) - var4,
                     (int)Math.floor(var10.y() * var1) - var4,
                     (int)Math.ceil(var10.width() * var1) + var4 * 2,
                     (int)Math.ceil(var10.height() * var1) + var4 * 2,
                     new q6.u(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         S.F(var5);
      }
   }

   public static void b() {
      if (J) {
         J = (boolean)0;
         n = null;
         p = null;
         S.close();
      }
   }

   private bp() {
   }
}

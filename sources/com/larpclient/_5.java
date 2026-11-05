package com.larpclient;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public final class _5 {
   private static final fy F = new fy("ViewModel outline");
   private static InteractionHand p;
   private static InteractionHand E;
   private static boolean O;

   public static void e(InteractionHand var0, InteractionHand var1) {
      p = var0;
      E = var1;
   }

   private static boolean a() {
      Minecraft var0 = Minecraft.getInstance();
      return fg.h() && var0.options.getCameraType().isFirstPerson() && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden;
   }

   public static boolean k() {
      if (!a()) {
         Q();
         return false;
      } else {
         O = (boolean)1;
         return F.j();
      }
   }

   public static void b() {
      if (!a()) {
         Q();
      } else {
         Window var0 = Minecraft.getInstance().getWindow();
         double var1 = (double)var0.getWidth() / Math.max(1, var0.getGuiScaledWidth());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil(var3 * 2.0F) + 2;
         ArrayList var5 = new ArrayList(2);

         for (InteractionHand var9 : InteractionHand.values()) {
            fg.v var10 = fg.T(var9);
            if (var10 != null) {
               int var11 = var9 == p ? oh.i(rv.e, 0.95) : (var9 == E ? -922746881 : 1627389951);
               var5.add(
                  new fy.j(
                     (int)Math.floor(var10.x() * var1) - var4,
                     (int)Math.floor(var10.y() * var1) - var4,
                     (int)Math.ceil(var10.width() * var1) + var4 * 2,
                     (int)Math.ceil(var10.height() * var1) + var4 * 2,
                     new rg.q(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         F.l(var5);
      }
   }

   public static void Q() {
      if (O) {
         O = (boolean)0;
         E = null;
         p = null;
         F.close();
      }
   }

   private _5() {
   }
}

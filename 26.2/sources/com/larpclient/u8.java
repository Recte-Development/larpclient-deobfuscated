package com.larpclient;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public final class u8 {
   private static final g1 R = new g1("ViewModel outline");
   private static InteractionHand q;
   private static InteractionHand i;
   private static boolean C;

   public static void w(InteractionHand var0, InteractionHand var1) {
      q = var0;
      i = var1;
   }

   private static boolean z() {
      Minecraft var0 = Minecraft.getInstance();
      return s8.A() && var0.options.getCameraType().isFirstPerson() && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden;
   }

   public static boolean K() {
      if (!z()) {
         f();
         return false;
      } else {
         C = (boolean)1;
         return R.D();
      }
   }

   public static void H() {
      if (!z()) {
         f();
      } else {
         Window var0 = Minecraft.getInstance().getWindow();
         double var1 = (double)var0.getWidth() / Math.max(1, var0.getGuiScaledWidth());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil(var3 * 2.0F) + 2;
         ArrayList var5 = new ArrayList(2);

         for (InteractionHand var9 : InteractionHand.values()) {
            s8.v var10 = s8.W(var9);
            if (var10 != null) {
               int var11 = var9 == q ? uh.H(ho.J, 0.95) : (var9 == i ? -922746881 : 1627389951);
               var5.add(
                  new g1.b(
                     (int)Math.floor(var10.x() * var1) - var4,
                     (int)Math.floor(var10.y() * var1) - var4,
                     (int)Math.ceil(var10.width() * var1) + var4 * 2,
                     (int)Math.ceil(var10.height() * var1) + var4 * 2,
                     new bl.d(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         R.Z(var5);
      }
   }

   public static void f() {
      if (C) {
         C = (boolean)0;
         i = null;
         q = null;
         R.close();
      }
   }

   private u8() {
   }
}

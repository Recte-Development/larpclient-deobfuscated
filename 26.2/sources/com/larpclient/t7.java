package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public final class t7 {
   private t7() {
   }

   public static void J(GuiGraphicsExtractor var0, long var1, boolean var3) {
      h2 var4 = h2.j;
      int var5 = var0.guiWidth();
      int var6 = var0.guiHeight();
      var0.fill(0, 0, var5, var6, uh.H(-16777216, 1.0 - var4.M(var1, var3)));
      double var7 = var4.d(var1, var3);
      if (!(var7 <= 0.0)) {
         ch.x var9 = ch.t(var5, var6);
         p1.e var10 = var9.glow();
         double var11 = var7 * var4.x(var1, var3);
         var0.nextStratum();
         var0.blit(
            RenderPipelines.GUI_TEXTURED,
            m0.q,
            var10.x(),
            var10.y(),
            0.0F,
            ch.a,
            var10.width(),
            var10.height(),
            256,
            256,
            512,
            ch.Q,
            uh.H(-1379585, var11 * 0.2)
         );
         p1.e var13 = var9.logo();
         var0.nextStratum();
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var13.x() + var13.width() / 2.0F, var13.y() + var13.height() / 2.0F);
            var0.pose().rotate((float)var4.c(var1, var3));
            float var14 = (float)var4.B(var1, var3);
            var0.pose().scale(var14, var14);
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               m0.r,
               -var13.width() / 2,
               -var13.height() / 2,
               0.0F,
               0.0F,
               var13.width(),
               var13.height(),
               64,
               64,
               64,
               64,
               uh.H(-1, var11)
            );
         } finally {
            var0.pose().popMatrix();
         }

         p1.e var18 = var9.title();
         int var15 = var4.g(var1, var3);
         if (var15 > 0) {
            var0.nextStratum();
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               m0.q,
               var18.x(),
               var18.y(),
               0.0F,
               var15 * 96,
               var18.width(),
               var18.height(),
               512,
               96,
               512,
               ch.Q,
               uh.H(-723722, var7)
            );
         }
      }
   }
}

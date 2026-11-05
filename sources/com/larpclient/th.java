package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public final class th {
   private th() {
   }

   public static void P(GuiGraphicsExtractor var0, long var1, boolean var3) {
      v var4 = v.s;
      int var5 = var0.guiWidth();
      int var6 = var0.guiHeight();
      var0.fill(0, 0, var5, var6, _3.A(-16777216, 1.0 - var4.M(var1, var3)));
      double var7 = var4.H(var1, var3);
      if (!(var7 <= 0.0)) {
         c0.t var9 = c0.L(var5, var6);
         p5.w var10 = var9.glow();
         double var11 = var7 * var4.K(var1, var3);
         var0.nextStratum();
         var0.blit(
            RenderPipelines.GUI_TEXTURED,
            ux.M,
            var10.x(),
            var10.y(),
            0.0F,
            c0.F,
            var10.width(),
            var10.height(),
            256,
            256,
            512,
            c0.h,
            _3.A(-1379585, var11 * 0.2)
         );
         p5.w var13 = var9.logo();
         var0.nextStratum();
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var13.x() + var13.width() / 2.0F, var13.y() + var13.height() / 2.0F);
            var0.pose().rotate((float)var4.c(var1, var3));
            float var14 = (float)var4.e(var1, var3);
            var0.pose().scale(var14, var14);
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               ux.x,
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
               _3.A(-1, var11)
            );
         } finally {
            var0.pose().popMatrix();
         }

         p5.w var18 = var9.title();
         int var15 = var4.f(var1, var3);
         if (var15 > 0) {
            var0.nextStratum();
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               ux.M,
               var18.x(),
               var18.y(),
               0.0F,
               var15 * 96,
               var18.width(),
               var18.height(),
               512,
               96,
               512,
               c0.h,
               _3.A(-723722, var7)
            );
         }
      }
   }
}

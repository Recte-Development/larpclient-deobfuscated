package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public final class v8 {
   private v8() {
   }

   public static void F(GuiGraphicsExtractor var0, long var1, boolean var3) {
      sn var4 = sn.p;
      int var5 = var0.guiWidth();
      int var6 = var0.guiHeight();
      var0.fill(0, 0, var5, var6, vv.N(-16777216, 1.0 - var4.p(var1, var3)));
      double var7 = var4.a(var1, var3);
      if (!(var7 <= 0.0)) {
         _1.p var9 = _1.I(var5, var6);
         _c.f var10 = var9.glow();
         double var11 = var7 * var4.E(var1, var3);
         var0.nextStratum();
         var0.blit(
            RenderPipelines.GUI_TEXTURED,
            wg.R,
            var10.x(),
            var10.y(),
            0.0F,
            _1.M,
            var10.width(),
            var10.height(),
            256,
            256,
            512,
            _1.L,
            vv.N(-1379585, var11 * 0.2)
         );
         _c.f var13 = var9.logo();
         var0.nextStratum();
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var13.x() + var13.width() / 2.0F, var13.y() + var13.height() / 2.0F);
            var0.pose().rotate((float)var4.h(var1, var3));
            float var14 = (float)var4.N(var1, var3);
            var0.pose().scale(var14, var14);
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               wg.c,
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
               vv.N(-1, var11)
            );
         } finally {
            var0.pose().popMatrix();
         }

         _c.f var18 = var9.title();
         int var15 = var4.i(var1, var3);
         if (var15 > 0) {
            var0.nextStratum();
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               wg.R,
               var18.x(),
               var18.y(),
               0.0F,
               var15 * 96,
               var18.width(),
               var18.height(),
               512,
               96,
               512,
               _1.L,
               vv.N(-723722, var7)
            );
         }
      }
   }
}

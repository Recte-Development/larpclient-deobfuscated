package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public final class _z {
   private _z() {
   }

   public static void V(GuiGraphicsExtractor var0, long var1, boolean var3) {
      ox var4 = ox.t;
      int var5 = var0.guiWidth();
      int var6 = var0.guiHeight();
      var0.fill(0, 0, var5, var6, oh.i(-16777216, 1.0 - var4.B(var1, var3)));
      double var7 = var4.x(var1, var3);
      if (!(var7 <= 0.0)) {
         xd.s var9 = xd.X(var5, var6);
         o6.b var10 = var9.glow();
         double var11 = var7 * var4.a(var1, var3);
         var0.nextStratum();
         var0.blit(
            RenderPipelines.GUI_TEXTURED,
            eb.C,
            var10.x(),
            var10.y(),
            0.0F,
            xd.u,
            var10.width(),
            var10.height(),
            256,
            256,
            512,
            xd.g,
            oh.i(-1379585, var11 * 0.2)
         );
         o6.b var13 = var9.logo();
         var0.nextStratum();
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var13.x() + var13.width() / 2.0F, var13.y() + var13.height() / 2.0F);
            var0.pose().rotate((float)var4.A(var1, var3));
            float var14 = (float)var4.C(var1, var3);
            var0.pose().scale(var14, var14);
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               eb.j,
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
               oh.i(-1, var11)
            );
         } finally {
            var0.pose().popMatrix();
         }

         o6.b var18 = var9.title();
         int var15 = var4.L(var1, var3);
         if (var15 > 0) {
            var0.nextStratum();
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               eb.C,
               var18.x(),
               var18.y(),
               0.0F,
               var15 * 96,
               var18.width(),
               var18.height(),
               512,
               96,
               512,
               xd.g,
               oh.i(-723722, var7)
            );
         }
      }
   }
}

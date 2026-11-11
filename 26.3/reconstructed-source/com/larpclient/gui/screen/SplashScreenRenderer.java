package com.larpclient.gui.screen;

import com.larpclient.render.misc.StartupTexture;
import com.larpclient.render.overlay.IntroAnimation;
import com.larpclient.util.render.ColorUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;

public final class SplashScreenRenderer {
   private SplashScreenRenderer() {
   }

   public static void renderSplash(GuiGraphicsExtractor var0, long var1, boolean var3) {
      IntroAnimation var4 = IntroAnimation.iNSTANCE;
      int var5 = var0.guiWidth();
      int var6 = var0.guiHeight();
      var0.fill(0, 0, var5, var6, ColorUtil.withAlpha(-16777216, 1.0 - var4.dissolveProgress(var1, var3)));
      double var7 = var4.overlayAlpha(var1, var3);
      if (!(var7 <= 0.0)) {
         SplashLayout.Layout var9 = SplashLayout.computeLayout(var5, var6);
         HomeLayout.Rect var10 = var9.glow();
         double var11 = var7 * var4.start(var1, var3);
         var0.nextStratum();
         var0.blit(
            RenderPipelines.GUI_TEXTURED,
            StartupTexture.tYPOGRAPHY_TEXTURE,
            var10.left(),
            var10.top(),
            0.0F,
            SplashLayout.tITLE_STRIP_WIDTH,
            var10.width(),
            var10.height(),
            256,
            256,
            512,
            SplashLayout.aTLAS_WIDTH,
            ColorUtil.withAlpha(-1379585, var11 * 0.2)
         );
         HomeLayout.Rect var13 = var9.logo();
         var0.nextStratum();
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var13.left() + var13.width() / 2.0F, var13.top() + var13.height() / 2.0F);
            var0.pose().rotate((float)var4.rotation(var1, var3));
            float var14 = (float)var4.scale(var1, var3);
            var0.pose().scale(var14, var14);
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               StartupTexture.bRAND_TEXTURE,
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
               ColorUtil.withAlpha(-1, var11)
            );
         } finally {
            var0.pose().popMatrix();
         }

         HomeLayout.Rect var18 = var9.title();
         int var15 = var4.visibleChars(var1, var3);
         if (var15 > 0) {
            var0.nextStratum();
            var0.blit(
               RenderPipelines.GUI_TEXTURED,
               StartupTexture.tYPOGRAPHY_TEXTURE,
               var18.left(),
               var18.top(),
               0.0F,
               var15 * 96,
               var18.width(),
               var18.height(),
               512,
               96,
               512,
               SplashLayout.aTLAS_WIDTH,
               ColorUtil.withAlpha(-723722, var7)
            );
         }
      }
   }
}

package com.larpclient.gui.widget;

import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class InfoCardWidget extends Widget {
   private final String title;
   private final String subtitle;

   public InfoCardWidget(String var1, String var2) {
      this.title = var1;
      this.subtitle = var2;
      this.enabled = (boolean)0;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (Theme.isThemed()) {
         GlassPainter.drawFocusedField(var1, this.x, this.y, this.width, this.height, Theme.radiusCard, Theme.cardBackground, 0.6);
      } else {
         GlassPainter.drawPanel(var1, this.x, this.y, this.width, this.height, 6, Theme.cardBackground, 0.3);
      }

      FontRenderer.drawText(
         var1,
         FontRenderer.trimToWidth(this.title, this.width - 20, FontRenderer.TextStyle.MODULE),
         this.x + 10,
         this.y + 8,
         Theme.textPrimary,
         FontRenderer.TextStyle.MODULE
      );
      FontRenderer.drawText(
         var1,
         FontRenderer.trimToWidth(this.subtitle, this.width - 20, FontRenderer.TextStyle.HELP),
         this.x + 10,
         this.y + 21,
         Theme.textSecondary,
         FontRenderer.TextStyle.HELP
      );
   }
}

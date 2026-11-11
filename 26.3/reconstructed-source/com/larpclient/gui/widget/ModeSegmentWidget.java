package com.larpclient.gui.widget;

import com.larpclient.config.ThemePalette;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.render.ColorUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ModeSegmentWidget extends Widget {
   private final ModeSetting setting;

   public ModeSegmentWidget(ModeSetting var1) {
      this.setting = var1;
      this.height = 20;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.width / this.setting.getModes().size());

      for (int var5 = 0; var5 < this.setting.getModes().size(); var5++) {
         String var6 = this.setting.getModes().get(var5);
         boolean var7 = this.setting.isMode(var6);
         int var8 = this.x + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? ThemePalette.readableAccent(Theme.accentAt(var8), Theme.cardBackground | 0xFF000000) : Theme.rowBackground | 0xFF000000;
         int var11 = Theme.isThemed() ? 7 : 5;
         if (var7) {
            GlassPainter.fillRoundedRect(var1, var8, this.y, var9, this.height, var11, 5.0F, ColorUtil.withAlpha(var10, 0.22));
         }

         GlassPainter.fillRoundedRect(var1, var8, this.y, var9, this.height, var11, var10);
         GlassPainter.drawBorder(var1, var8, this.y, var9, this.height, var11, var7 ? var10 : Theme.controlBackground);
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(var6, var4 - 9, FontRenderer.TextStyle.HELP),
            var8 + 4,
            this.y + 6,
            ThemePalette.controlInk(var10),
            FontRenderer.TextStyle.HELP
         );
      }

      if (this.focused && this.focusVisible) {
         GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, Theme.isThemed() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.width / this.setting.getModes().size());
         this.setting.setValue(this.setting.getModes().get(Math.clamp((int)(var1 - this.x) / var6, 0, this.setting.getModes().size() - 1)));
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (var1 != 263 && var1 != 262 && var1 != 32 && var1 != 257) {
         return false;
      }

      int var2 = this.setting.getModes().indexOf(this.setting.getValue());
      this.setting.setValue(this.setting.getModes().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.setting.getModes().size())));
      return true;
   }
}

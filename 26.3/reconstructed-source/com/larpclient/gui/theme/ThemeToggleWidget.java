package com.larpclient.gui.theme;

import com.larpclient.gui.widget.Widget;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ThemeToggleWidget extends Widget {
   private final BooleanSupplier isLight;
   private final Consumer<Boolean> onSelect;

   public ThemeToggleWidget(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.isLight = var1;
      this.onSelect = var2;
      this.height = 52;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, (this.width - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.x + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         GlassPainter.fillRoundedRect(var1, var7, this.y, var4, 36, 6, var8);
         GlassPainter.drawBorder(var1, var7, this.y, var4, 36, 6, Theme.controlBackground);
         GlassPainter.fillRoundedRect(var1, var7 + 5, this.y + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            GlassPainter.fillRoundedRect(var1, var7 + 16, this.y + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         FontRenderer.drawText(
            var1,
            var6 != 0 ? "Light" : "Dark",
            var7 + 4,
            this.y + 40,
            var6 == this.isLight.getAsBoolean() ? Theme.textPrimary : Theme.textSecondary,
            FontRenderer.TextStyle.HELP
         );
      }

      if (this.focused && this.focusVisible) {
         GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.onSelect.accept(var1 < this.x + this.width / 2.0);
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

      this.onSelect.accept(var1 == 263 || var1 != 262 && !this.isLight.getAsBoolean());
      return true;
   }
}

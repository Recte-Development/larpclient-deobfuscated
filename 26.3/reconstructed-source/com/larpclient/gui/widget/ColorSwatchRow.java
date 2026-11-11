package com.larpclient.gui.widget;

import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.util.render.ColorUtil;
import java.util.function.IntConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ColorSwatchRow extends Widget {
   private static final int[] pALETTE = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer onSelect;
   private int selectedIndex;

   public ColorSwatchRow(IntConsumer var1) {
      this.onSelect = var1;
      this.height = 18;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.width / pALETTE.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < pALETTE.length; var6++) {
         int var7 = this.x + var6 * var4;
         GlassPainter.fillRoundedRect(var1, var7, this.y + 2, var5, var5, 4, pALETTE[var6]);
         RoundedShapes.drawRimGradient(
            var1, var7, this.y + 2, var5, var5, 4.0F, ColorUtil.multiplyAlpha(Theme.accentGlow, 1.6), ColorUtil.multiplyAlpha(Theme.accentSoft, 2.0)
         );
         if (Theme.accent == pALETTE[var6]) {
            RoundedShapes.drawRim(var1, var7 - 2, this.y, var5 + 4, var5 + 4, 6.0F, ColorUtil.withAlpha(Theme.textPrimary, 0.8));
         }

         if (this.focused && this.focusVisible && this.selectedIndex == var6) {
            GlassPainter.drawFocusRing(var1, var7 - 2, this.y, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.selectedIndex = Math.clamp((int)(var1 - this.x) / Math.max(1, this.width / pALETTE.length), 0, pALETTE.length - 1);
         this.onSelect.accept(pALETTE[this.selectedIndex]);
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

      this.selectedIndex = Math.floorMod(this.selectedIndex + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), pALETTE.length);
      this.onSelect.accept(pALETTE[this.selectedIndex]);
      return true;
   }
}

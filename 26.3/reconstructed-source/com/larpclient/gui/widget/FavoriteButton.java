package com.larpclient.gui.widget;

import com.larpclient.config.ThemePalette;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class FavoriteButton extends Widget {
   private final BooleanSupplier isFavorite;
   private final Runnable onToggle;
   private final Animation favoriteAnim;
   private final Animation hoverAnim = new Animation(0.0);
   private final Animation pressAnim = new Animation(0.0);
   private long pressUntilNanos;
   private MenuButton row;

   public FavoriteButton(BooleanSupplier var1, Runnable var2) {
      this.isFavorite = var1;
      this.onToggle = var2;
      this.favoriteAnim = new Animation(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void attachRow(MenuButton var1) {
      this.row = var1;
      this.bounds(var1.x + var1.width - 62, var1.y + (var1.height - 22) / 2, 22);
      this.height = 22;
      this.visible = var1.visible;
      this.enabled = var1.enabled;
   }

   public String tooltipText() {
      return this.isFavorite.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.visible) {
         double var4 = this.favoriteAnim.animateTo(this.isFavorite.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.hoverAnim.animateTo(!this.contains(var2, var3) && (!this.focused || !this.focusVisible) ? 0.0 : 1.0, 160);
         double var8 = this.pressAnim.animateTo(System.nanoTime() < this.pressUntilNanos ? 1.0 : 0.0, 100);
         ThemePalette var10 = this.row == null ? ThemePalette.forBackground(0.0, 0.0, Theme.accent) : this.row.getPalette();
         float var11 = this.row == null ? 0.0F : this.row.getHoverLift();
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               GlassPainter.fillRoundedRect(
                  var1, this.x, this.y, this.width, this.height, Theme.radiusMedium, ColorUtil.withAlpha(var10.title(), var6 * 0.07 + var8 * 0.07)
               );
            }

            int var12 = this.x + (this.width - 11) / 2;
            int var13 = this.y + (this.height - 11) / 2;
            int var14 = ColorUtil.lerpColor(var10.subtitle(), var10.title(), var6 * 0.5);
            Icons.texture(var1, Icons.get("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && ThemePalette.contrast(Theme.accent, var10.background()) < 3.0) {
               Icons.texture(var1, Icons.get("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               Icons.texture(var1, Icons.get("star-filled"), var12, var13, 11, ColorUtil.multiplyAlpha(Theme.accent | 0xFF000000, var4));
            }

            if (this.focused && this.focusVisible) {
               GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, 4);
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   public void renderTooltip(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = FontRenderer.getTextWidth(this.tooltipText(), var2 - 22);
      int var5 = FontRenderer.drawDefaultText(var4) + 14;
      int var6 = Math.clamp(this.x + this.width / 2 - var5 / 2, 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp(this.y - 23, 4, Math.max(4, var3 - 23));
      GlassPainter.fillRoundedRect(var1, var6, var7, var5, 19, 4, Theme.background);
      FontRenderer.drawText(var1, var4, var6 + 7, var7 + 5, Theme.textPrimary);
   }

   private void toggle() {
      this.pressUntilNanos = System.nanoTime() + 110000000L;
      this.onToggle.run();
      GuiSounds.play(GuiSounds.Sound.CLICK);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.toggle();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.visible && this.enabled && this.focused && (var1 == 257 || var1 == 32)) {
         this.toggle();
         return true;
      } else {
         return false;
      }
   }
}

package com.larpclient.gui.widget;

import com.larpclient.LarpClient;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.PressAnimation;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class IconButton extends Widget {
   public final String label;
   public final String icon;
   private final Runnable onClick;
   private final Animation hoverAnim = new Animation(0.0);
   private final Animation pressAnim = new Animation(0.0);
   private final Animation activeAnim = new Animation(0.0);
   private final Animation tooltipAnim = new Animation(0.0);
   private PressAnimation.Transform transform = new PressAnimation.Transform(1.0, 0.0);
   public boolean animateLift;
   public double lift;
   public Consumer<GuiGraphicsExtractor> customRenderer;
   public boolean active;
   public boolean danger;
   public double alpha = 1.0;
   private long pressedUntil;

   public IconButton(String var1, String var2, Runnable var3) {
      this.label = var1;
      this.icon = var2;
      this.onClick = var3;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.visible) {
         boolean var4 = LarpClient.getInstance().getConfigManager().getPreferences().motion;
         double var5 = var4
            ? this.hoverAnim.animateTo(!this.contains(var2, var3) && (!this.focused || !this.focusVisible) ? 0.0 : 1.0, 140)
            : (!this.contains(var2, var3) && (!this.focused || !this.focusVisible) ? 0.0 : 1.0);
         double var7 = var4 ? this.pressAnim.animateTo(System.nanoTime() < this.pressedUntil ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.activeAnim.animateTo(this.active ? 1.0 : 0.0, 220) : (this.active ? 1.0 : 0.0);
         this.transform = PressAnimation.computeTransform(var5, var7, this.lift, var4, this.animateLift);
         int var11 = this.danger ? -24915 : Theme.textPrimary;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.x + this.width / 2.0F, (float)(this.y + this.height / 2.0 + this.transform.offsetY()));
            var1.pose().scale((float)this.transform.scale(), (float)this.transform.scale());
            var1.pose().translate(-this.x - this.width / 2.0F, -this.y - this.height / 2.0F);
            GlassPainter.fillRoundedRect(
               var1,
               this.x,
               this.y,
               this.width,
               this.height,
               5,
               ColorUtil.multiplyAlpha(
                  ColorUtil.lerpColor(
                     -987684056, ColorUtil.withAlpha(this.danger ? -24915 : Theme.accent, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)
                  ),
                  this.alpha
               )
            );
            if (this.focused && this.focusVisible) {
               GlassPainter.drawInsetBorder(var1, this.x, this.y, this.width, this.height, ColorUtil.multiplyAlpha(Theme.accent, this.alpha));
            }

            if (this.customRenderer != null) {
               this.customRenderer.accept(var1);
            } else if (!this.icon.isEmpty()) {
               Icons.texture(
                  var1,
                  Icons.get(this.icon),
                  this.x + (this.width - 14) / 2,
                  this.y + (this.height - 14) / 2,
                  14,
                  ColorUtil.multiplyAlpha(this.enabled ? var11 : Theme.textMuted, this.alpha)
               );
            } else {
               FontRenderer.drawCenteredText(
                  var1,
                  FontRenderer.drawTextWithFace(this.label, this.width - 12),
                  this.x + this.width / 2,
                  this.y + (this.height - 9) / 2,
                  ColorUtil.multiplyAlpha(this.enabled ? var11 : Theme.textMuted, this.alpha)
               );
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.transform.localX(var1, this.x + this.width / 2.0), this.transform.localY(var3, this.y + this.height / 2.0));
   }

   public void renderTooltip(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
      if (this.visible) {
         boolean var6 = LarpClient.getInstance().getConfigManager().getPreferences().motion;
         int var7 = !this.contains(var2, var3) && (!this.focused || !this.focusVisible) ? 0 : 1;
         double var8 = var6 ? this.tooltipAnim.animateTo(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = FontRenderer.drawTextWithFace(this.label, var4 - 20);
            int var11 = FontRenderer.getDefaultWidth(var10) + 14;
            int var12 = Math.clamp(this.x + this.width / 2 - var11 / 2, 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp(this.y - 23 + (int)Math.round(this.transform.offsetY() + (1.0 - var8) * 4.0), 4, Math.max(4, var5 - 24));
            GlassPainter.fillRoundedRect(var1, var12, var13, var11, 19, 4, ColorUtil.multiplyAlpha(-283567328, var8));
            FontRenderer.drawDefaultText(var1, var10, var12 + 7, var13 + 5, ColorUtil.multiplyAlpha(Theme.textPrimary, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.pressedUntil = System.nanoTime() + 90000000L;
         this.onClick.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.visible && this.enabled && this.focused && (var1 == 257 || var1 == 32)) {
         this.pressedUntil = System.nanoTime() + 90000000L;
         this.onClick.run();
         return true;
      } else {
         return false;
      }
   }
}

package com.larpclient.gui.widget;

import com.larpclient.config.ThemePalette;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.Module;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class ModuleToggleWidget extends Widget {
   private final String label;
   private final Module module;
   private final Animation animation;

   ModuleToggleWidget(String var1, Module var2) {
      this.label = var1;
      this.module = var2;
      this.height = 26;
      this.animation = new Animation(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = this.label.isEmpty() ? (this.module.isEnabled() ? "Enabled" : "Disabled") : this.label;
      double var5 = this.animation.animateTo(this.module.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = ColorUtil.lerpColor(Theme.textMuted, ThemePalette.readableAccent(Theme.accentAt(this.x), Theme.background), var5);
      FontRenderer.TextStyle var8 = FontRenderer.TextStyle.HELP;
      FontRenderer.drawText(var1, FontRenderer.trimToWidth(var4, this.width - 38, var8), this.x + 4, this.y + (this.height - var8.lineHeight()) / 2, var7, var8);
      GlassPainter.drawPulsingGlow(var1, this.x + this.width - 28, this.y + (this.height - 13) / 2, var5);
      if (this.focused && this.focusVisible) {
         GlassPainter.drawFocusRing(var1, this.x + this.width - 28, this.y + (this.height - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
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

   private void toggle() {
      this.module.toggle();
      GuiSounds.play(this.module.isEnabled() ? GuiSounds.Sound.SWITCH_ON : GuiSounds.Sound.SWITCH_OFF);
   }
}

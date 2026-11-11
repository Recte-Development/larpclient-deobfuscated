package com.larpclient.gui.screen;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.config.ConfigManager;
import com.larpclient.gui.misc.MenuBackground2;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.CardDialogLayout;
import com.larpclient.gui.widget.IconButton;
import com.larpclient.gui.widget.Widget;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.sky.SkyboxTheme;
import com.larpclient.util.data.EnumCycler;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.PressAnimation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;

public final class MenuAtmosphereScreen extends AnimatedWindowScreen {
   private final EnumCycler selection;
   private final Animation indicatorAnimation;
   private final Animation[] cardHoverAnimations = new Animation[]{new Animation(0.0), new Animation(0.0), new Animation(0.0)};
   private final double[] cardReveal = new double[3];
   private String statusMessage = "";
   private long lastScrollNanos;
   private long openedNanos;
   private IconButton previousButton;
   private IconButton nextButton;
   private IconButton motionButton;
   private CardDialogLayout layout;
   private final IconButton[] cardButtons = new IconButton[3];

   public MenuAtmosphereScreen(Screen var1) {
      super("Menu atmosphere", var1);
      this.selection = new EnumCycler(LarpClient.getInstance().getConfigManager().getPreferences().background);
      this.indicatorAnimation = new Animation(this.selection.current().ordinal());
   }

   @Override
   public SkyboxTheme atmosphere() {
      return this.selection.current();
   }

   protected void init() {
      this.clearWidgets();
      this.layout = CardDialogLayout.calculate(this.width, this.height);
      this.setWindowSize(this.layout.width(), this.layout.height());
      this.openedNanos = System.nanoTime();

      for (int var1 = 0; var1 < this.cardButtons.length; var1++) {
         int var2 = var1;
         SkyboxTheme var3 = SkyboxTheme.values()[var1];
         HomeLayout.Rect var4 = this.layout.card(var1);
         IconButton var5 = this.cardButtons[var1] = this.addButton(var3.displayName, () -> this.selection.setIndex(var2), var4.left(), var4.top(), var4.width());
         var5.height = var4.height();
         var5.animateLift = (boolean)1;
         var5.customRenderer = var3x -> this.drawCard(var3x, var2, var3);
      }

      this.previousButton = this.addButton("‹", () -> this.selection.getDefault(-1), this.windowX + 16, this.layout.statusY() - 3, 24);
      this.nextButton = this.addButton("›", () -> this.selection.getDefault(1), this.windowX + this.windowWidth - 40, this.layout.statusY() - 3, 24);
      this.previousButton.height = this.nextButton.height = 20;
      this.motionButton = this.addButton("", this::toggleMotion, this.layout.motion());
      this.addButton("Back", this::onClose, this.layout.back());
      this.addButton("Apply", this::applyAtmosphere, this.layout.apply());
      this.motionButton.customRenderer = var1x -> FontRenderer.drawCenteredText(
         var1x,
         FontRenderer.drawTextWithFace("Motion: " + (this.isMotionEnabled() ? "On" : "Reduced"), this.motionButton.width - 12),
         this.motionButton.x + this.motionButton.width / 2,
         this.motionButton.y + 8,
         ColorUtil.multiplyAlpha(Theme.textPrimary, this.motionButton.alpha)
      );
   }

   private IconButton addButton(String var1, Runnable var2, HomeLayout.Rect var3) {
      IconButton var4 = this.addButton(var1, var2, var3.left(), var3.top(), var3.width());
      var4.animateLift = (boolean)1;
      return var4;
   }

   private boolean isMotionEnabled() {
      return LarpClient.getInstance().getConfigManager().getPreferences().motion;
   }

   private void toggleMotion() {
      ConfigManager var1 = LarpClient.getInstance().getConfigManager();
      ClientPreferences var2 = var1.getPreferences();
      var2.motion = (boolean)(!var2.motion ? 1 : 0);
      if (!var1.savePreferences()) {
         var2.motion = (boolean)(!var2.motion ? 1 : 0);
         this.statusMessage = "Could not save motion preference.";
      }
   }

   private void drawCard(GuiGraphicsExtractor var1, int var2, SkyboxTheme var3) {
      IconButton var4 = this.cardButtons[var2];
      double var5 = this.isMotionEnabled() ? this.cardHoverAnimations[var2].animateTo(var4.active ? 1.0 : 0.0, 280) : (var4.active ? 1.0 : 0.0);
      int var7 = var4.height - 28;
      MenuBackground2.drawWallpaper(var1, var3, var4.x + 3, var4.y + 3, var4.width - 6, var7, var4.alpha, false);
      FontRenderer.drawCenteredText(
         var1,
         FontRenderer.drawTextWithFace(var3.displayName, var4.width - 12),
         var4.x + var4.width / 2,
         var4.y + var4.height - 17,
         ColorUtil.multiplyAlpha(Theme.textPrimary, var4.alpha)
      );
      if (var3 == this.selection.getDefault() && var7 >= 24) {
         GlassPainter.fillRoundedRect(var1, var4.x + 7, var4.y + 7, 38, 16, 3, ColorUtil.multiplyAlpha(-535485155, var4.alpha));
         FontRenderer.drawDefaultText(var1, "Saved", var4.x + 12, var4.y + 11, ColorUtil.multiplyAlpha(-7873104, var4.alpha));
      }

      if (var5 > 0.01) {
         int var8 = Math.max(1, (int)((var4.width - 16) * var5));
         GlassPainter.fillRoundedRect(
            var1, var4.x + (var4.width - var8) / 2, var4.y + var4.height - 3, var8, 2, 1, ColorUtil.multiplyAlpha(Theme.accent, var5 * var4.alpha)
         );
      }
   }

   private void applyAtmosphere() {
      ConfigManager var1 = LarpClient.getInstance().getConfigManager();
      ClientPreferences var2 = var1.getPreferences();
      SkyboxTheme var3 = var2.background;
      boolean var4 = var2.cinematicMenu;
      var2.background = this.selection.currentValue();
      var2.cinematicMenu = (boolean)0;
      if (!var1.savePreferences()) {
         var2.background = var3;
         var2.cinematicMenu = var4;
         this.statusMessage = "Could not save background.";
      } else {
         LarpLog.info("Menu", "Applied atmosphere {}", var2.background.displayName);
         this.onClose();
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.beginWindow(var1);
      var3 = this.adjustY(var3);

      try {
         boolean var5 = this.isMotionEnabled();
         double var6 = (System.nanoTime() - this.openedNanos) / 1000000.0;
         double var8 = this.setWindowSize();
         FontRenderer.drawCenteredText(var1, "Menu atmosphere", this.width / 2, this.windowY + 16, ColorUtil.multiplyAlpha(Theme.textPrimary, var8));
         if (this.windowHeight >= 220) {
            FontRenderer.drawCenteredText(
               var1, "Preview a scene, then apply it.", this.width / 2, this.windowY + 35, ColorUtil.multiplyAlpha(Theme.textSecondary, var8)
            );
         }

         for (int var10 = 0; var10 < this.cardButtons.length; var10++) {
            IconButton var11 = this.cardButtons[var10];
            this.cardReveal[var10] = PressAnimation.easeProgress(var6, 60 + var10 * 65, 380.0, var5);
            var11.active = (boolean)(var10 == this.selection.current().ordinal() ? 1 : 0);
            var11.lift = (1.0 - this.cardReveal[var10]) * 7.0;
            var11.enabled = (boolean)(!this.isClosing() && this.cardReveal[var10] > 0.85 ? 1 : 0);
         }

         this.previousButton.enabled = (boolean)(!this.isClosing() && this.selection.current().ordinal() > 0 ? 1 : 0);
         this.nextButton.enabled = (boolean)(!this.isClosing() && this.selection.current().ordinal() < 2 ? 1 : 0);
         String var18 = this.statusMessage.isEmpty() ? "Saved: " + this.selection.getDefault().displayName : this.statusMessage;
         FontRenderer.drawCenteredText(
            var1,
            FontRenderer.drawTextWithFace(var18, this.windowWidth - 104),
            this.width / 2,
            this.layout.statusY(),
            ColorUtil.multiplyAlpha(this.statusMessage.isEmpty() ? Theme.textSecondary : -24915, var8)
         );
         double var19 = var5 ? this.indicatorAnimation.animateTo(this.selection.current().ordinal(), 320) : this.selection.current().ordinal();

         for (int var13 = 0; var13 < 3; var13++) {
            DrawUtil.fillCircle(var1, this.width / 2 + (var13 - 1) * 10, this.layout.statusY() + 16, 1, ColorUtil.multiplyAlpha(Theme.textMuted, var8));
         }

         DrawUtil.fillCircle(
            var1, this.width / 2 + (int)Math.round((var19 - 1.0) * 10.0), this.layout.statusY() + 16, 2, ColorUtil.multiplyAlpha(Theme.accent, var8)
         );
         this.drawControls(var1, var2, var3);
      } finally {
         this.endWindow(var1);
      }
   }

   @Override
   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (Widget var5 : this.widgets) {
         IconButton var6 = (IconButton)var5;
         double var7 = 1.0;

         for (int var9 = 0; var9 < this.cardButtons.length; var9++) {
            if (var6 == this.cardButtons[var9]) {
               var7 = this.cardReveal[var9];
            }
         }

         var6.alpha = this.setWindowSize() * var7;
         if (this.isClosing()) {
            var6.enabled = (boolean)0;
         }

         var6.render(var1, var2, var3);
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.isClosing() && !(this.setWindowSize() <= 0.15)) {
         long var9 = System.nanoTime();
         if (var7 != 0.0 && var9 - this.lastScrollNanos >= 150000000L) {
            this.lastScrollNanos = var9;
            this.selection.getDefault(var7 > 0.0 ? -1 : 1);
         }

         return true;
      } else {
         return true;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.isClosing() || this.setWindowSize() <= 0.15) {
         return true;
      }

      if (KeyCodes.keyOf(var1) != 263 && KeyCodes.keyOf(var1) != 262) {
         return super.keyPressed(var1);
      }

      this.selection.getDefault(KeyCodes.keyOf(var1) == 263 ? -1 : 1);
      return true;
   }
}

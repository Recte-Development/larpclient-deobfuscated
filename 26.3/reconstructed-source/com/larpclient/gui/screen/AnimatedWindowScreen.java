package com.larpclient.gui.screen;

import com.larpclient.LarpClient;
import com.larpclient.gui.widget.IconButton;
import com.larpclient.gui.widget.Widget;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.sky.AtmosphereProvider;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class AnimatedWindowScreen extends WidgetScreen implements AtmosphereProvider {
   protected final Screen parent;
   protected int windowX;
   protected int windowY;
   protected int windowWidth;
   protected int windowHeight;
   private final Animation openAnimation = new Animation(0.0);
   private boolean opened;
   private boolean closing;
   private long closeStartNanos;
   private double progress = 1.0;
   private double offsetY;
   private double closeStartProgress = 1.0;

   protected AnimatedWindowScreen(String var1, Screen var2) {
      super(var1);
      this.parent = var2;
   }

   @Override
   public boolean dimAtmosphere() {
      return true;
   }

   protected IconButton addButton(String var1, Runnable var2, int var3, int var4, int var5) {
      IconButton var6 = new IconButton(var1, "", var2);
      var6.bounds(var3, var4, var5);
      var6.height = 24;
      this.widgets.add(var6);
      return var6;
   }

   protected void setWindowSize(int var1, int var2) {
      this.windowWidth = Math.min(var1, this.width - 24);
      this.windowHeight = Math.min(var2, this.height - 24);
      this.windowX = (this.width - this.windowWidth) / 2;
      this.windowY = (this.height - this.windowHeight) / 2;
   }

   protected void beginWindow(GuiGraphicsExtractor var1) {
      if (!this.opened) {
         this.opened = (boolean)1;
         this.openAnimation.snapTo(0.0);
      }

      double var2 = LarpClient.getInstance().getConfigManager().getPreferences().motion ? this.openAnimation.animateTo(1.0, 260) : 1.0;
      if (this.closing) {
         var2 = this.closeStartProgress * (1.0 - Animation.Easing.EASE_IN_CUBIC.apply((System.nanoTime() - this.closeStartNanos) / 1.6E8));
      }

      this.progress = var2;
      this.offsetY = LarpClient.getInstance().getConfigManager().getPreferences().motion ? (1.0 - var2) * 12.0 : 0.0;
      var1.pose().pushMatrix();
      var1.pose().translate(0.0F, (float)this.offsetY);
      GlassPainter.drawShadow(var1, this.windowX, this.windowY, this.windowWidth, this.windowHeight);
      GlassPainter.fillRoundedRect(var1, this.windowX, this.windowY, this.windowWidth, this.windowHeight, 7, ColorUtil.multiplyAlpha(-451076058, var2));
   }

   protected void drawControls(GuiGraphicsExtractor var1, int var2, int var3) {
      for (Widget var5 : this.widgets) {
         if (var5.visible) {
            if (var5 instanceof IconButton var6) {
               var6.alpha = this.progress;
            }

            if (this.closing) {
               var5.enabled = (boolean)0;
            }

            var5.render(var1, var2, var3);
         }
      }
   }

   public void onClose() {
      if (!this.closing) {
         if (!LarpClient.getInstance().getConfigManager().getPreferences().motion) {
            this.minecraft.gui.setScreen(this.parent);
         } else {
            this.closing = (boolean)1;
            this.closeStartProgress = this.progress;
            this.closeStartNanos = System.nanoTime();

            for (Widget var2 : this.widgets) {
               var2.enabled = (boolean)0;
            }
         }
      }
   }

   public void tick() {
      if (this.closing && System.nanoTime() - this.closeStartNanos >= 160000000L) {
         this.minecraft.gui.setScreen(this.parent);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return !this.opened || this.progress <= 0.15 || this.closing || super.keyPressed(var1);
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      return !this.opened || this.progress <= 0.15 || this.closing || super.charTyped(var1);
   }

   private MouseButtonEvent endWindow(MouseButtonEvent var1) {
      return new MouseButtonEvent(var1.x(), var1.y() - this.offsetY, var1.buttonInfo());
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !this.opened || this.progress <= 0.15 || this.closing || super.mouseClicked(this.endWindow(var1), var2);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      return this.closing || super.mouseDragged(this.endWindow(var1), var2, var4);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      return super.mouseReleased(this.endWindow(var1));
   }

   protected int adjustY(int var1) {
      return (int)Math.round(var1 - this.offsetY);
   }

   protected double adjustYDouble(double var1) {
      return var1 - this.offsetY;
   }

   protected void endWindow(GuiGraphicsExtractor var1) {
      var1.pose().popMatrix();
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.closing;
   }

   protected boolean isClosing() {
      return this.closing;
   }

   protected double setWindowSize() {
      return this.progress;
   }
}

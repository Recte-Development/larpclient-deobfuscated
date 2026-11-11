package com.larpclient.gui.screen;

import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.EmotesModule;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.social.emote.EmoteRegistry;
import com.larpclient.social.emote.EmoteWheelWedge;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.data.Marker;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class EmoteWheelScreen extends Screen implements Marker {
   private static final int iCON_SIZE = 66;
   private static final int hUB_RADIUS = 42;
   private static final int hOVER_OFFSET = 6;
   private static final int fADE_MILLIS = 240;
   private static final double sTAGGER_DELAY = 0.11;
   private static final double sLIDE_DISTANCE = 12.0;
   private static final int lINE_HEIGHT = 12;
   private static final int tEXT_BLOCK_HEIGHT = 11;
   private final EmotesModule module;
   private final Animation openAnimation = new Animation(0.0);
   private final Animation[] hoverAnimations = new Animation[]{new Animation(0.0), new Animation(0.0), new Animation(0.0), new Animation(0.0)};
   private final Animation[] dimAnimations = new Animation[]{new Animation(0.0), new Animation(0.0), new Animation(0.0), new Animation(0.0)};
   private int hoveredSlot = -1;
   private double hoverAngle = Double.NaN;
   private float wheelScale = 1.0F;
   private boolean closed;

   public EmoteWheelScreen(EmotesModule var1) {
      super(Component.literal("Emotes"));
      this.module = var1;
      this.openAnimation.animateTo(1.0, 240);
   }

   public static int slotAt(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void updateHover(double var1, double var3) {
      double var5 = var1 - this.width / 2.0;
      double var7 = var3 - this.height / 2.0;
      this.hoveredSlot = slotAt(var5, var7, 48.0F * this.wheelScale);
      this.hoverAngle = this.hoveredSlot < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void finishSelection() {
      if (!this.closed) {
         this.closed = (boolean)1;
         List var1 = this.module.getSlotLabels();
         if (this.hoveredSlot >= 0 && !"Empty".equals(var1.get(this.hoveredSlot))) {
            this.module.playEmote(this.hoveredSlot);
         } else if (this.hoveredSlot < 0 && this.module.isPlaying()) {
            this.module.stopEmote("stopped from the wheel");
         } else {
            LarpLog.debug("Emotes", "Wheel closed without an emote (slot {})", this.hoveredSlot < 0 ? "hub" : this.hoveredSlot + 1);
         }

         this.onClose();
      }
   }

   public void tick() {
      int var1 = this.module.getWheelKey();
      boolean var2 = BindCodes.isPressed(this.minecraft.getWindow().handle(), var1);
      if (!this.module.isRuntimeActive() || this.minecraft.player == null) {
         this.onClose();
      } else if (!var2) {
         this.finishSelection();
      }
   }

   public boolean keyReleased(KeyEvent var1) {
      if (KeyCodes.keyOf(var1) == this.module.getWheelKey()) {
         this.finishSelection();
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.updateHover(var1, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.updateHover(var1.x(), var1.y());
      if (KeyCodes.mouseButtonOf(var1) == 0) {
         this.finishSelection();
      } else {
         this.onClose();
      }

      return true;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      DrawUtil.fillRect(var1, 0, 0, this.width, this.height, ColorUtil.withAlpha(-16777216, 0.25 * this.openAnimation.getValue()));
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      super.extractRenderState(var1, var2, var3, var4);
      List var5 = this.module.getSlotLabels();
      double var6 = this.openAnimation.animateTo(1.0, 240);
      int var8 = this.width / 2;
      int var9 = this.height / 2;
      float var10 = (float)Math.min(1.0, (Math.min(this.width, this.height) - 24) / 276.0);
      this.wheelScale = (float)((0.9 + 0.1 * var6) * var10);
      this.updateHover(var2, var3);

      try (AlphaScope var11 = AlphaScope.push(var6)) {
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var8, var9);
            var1.pose().scale(this.wheelScale, this.wheelScale);

            for (int var12 = 0; var12 < 4; var12++) {
               this.slotAt(var1, var12, (String)var5.get(var12), var6);
            }

            this.drawHub(var1, var5);
         } finally {
            var1.pose().popMatrix();
         }

         this.drawHint(var1, var8, var9 + Math.round(128.0F * this.wheelScale));
      }
   }

   private void slotAt(GuiGraphicsExtractor var1, int var2, String var3, double var4) {
      boolean var6 = "Empty".equals(var3);
      EmoteRegistry.Emote var7 = EmoteRegistry.findByLabel(var3);
      int var8 = this.module.isPlaying() && var3.equals(this.module.getStopReason()) ? 1 : 0;
      double var9 = this.hoverAnimations[var2].animateTo(var2 == this.hoveredSlot ? 1.0 : 0.0, 160);
      double var11 = this.dimAnimations[var2].animateTo(this.hoveredSlot >= 0 && var2 != this.hoveredSlot ? 1.0 : 0.0, 160);
      double var13 = Math.clamp((var4 - var2 * 0.11) / 0.6699999999999999, 0.0, 1.0);
      double var15 = var9 * 6.0 - (1.0 - var13) * 12.0;
      double var17 = var6 ? var9 * 0.4 : var9;
      int var19 = ColorUtil.withAlpha(Theme.background, var6 ? 0.7 : 0.92 - 0.08 * var11);
      int var20 = ColorUtil.lerpColor(var19, ColorUtil.withAlpha(ColorUtil.lerpColor(Theme.background, Theme.accent, 0.3), 0.96), var17);
      int var21 = ColorUtil.lerpColor(
         ColorUtil.withAlpha(-1381654, 0.09), var6 ? ColorUtil.withAlpha(-1381654, 0.3) : Theme.accent, Math.max(var9, var8 != 0 ? 0.7 : 0.0)
      );
      int var22 = ColorUtil.withAlpha(Theme.accent, var17);

      try (AlphaScope var23 = AlphaScope.push(var13)) {
         EmoteWheelWedge.drawWedge(var1, var2, var15, var20, var21, var22);
         double var24 = (var2 - 1) * Math.PI / 2.0;
         double var26 = 82.0 + var15;
         int var28 = (int)Math.round(Math.cos(var24) * var26);
         int var29 = (int)Math.round(Math.sin(var24) * var26);
         FontRenderer.TextStyle var30 = FontRenderer.TextStyle.BODY;
         if (!var6 && var7 != null) {
            int var42 = ColorUtil.lerpColor(ColorUtil.lerpColor(-1184275, -1, var9), -5855578, var11);
            double var32 = 1.0 + 0.08 * var9;
            var1.pose().pushMatrix();

            try {
               var1.pose().translate(var28, var29);
               var1.pose().scale((float)var32, (float)var32);
               short var34 = 192;
               var1.blit(RenderPipelines.GUI_TEXTURED, var7.icon(), -33, -33, 0.0F, 0.0F, 66, 66, var34, var34, var34, var34, AlphaScope.applyAlpha(var42));
               return;
            } finally {
               var1.pose().popMatrix();
            }
         }

         int var31 = ColorUtil.lerpColor(ColorUtil.lerpColor(Theme.textMuted, Theme.textSecondary, var9), Theme.background, 0.25 * var11);
         DrawUtil.fillRect(var1, var28 - 5, var29 - 9, 11, 1, var31);
         DrawUtil.fillRect(var1, var28, var29 - 14, 1, 11, var31);
         FontRenderer.drawText(var1, "Empty", var28 - FontRenderer.measureWidth("Empty", var30) / 2, var29 + 4, var31, var30);
      }
   }

   private void drawHub(GuiGraphicsExtractor var1, List<String> var2) {
      DrawUtil.fillCircle(var1, 0, 0, 42, ColorUtil.withAlpha(Theme.background, 0.9));
      GlassPainter.drawBorder(var1, -42, -42, 85, 85, 42, ColorUtil.withAlpha(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.hoveredSlot >= 0) {
         var3 = (String)var2.get(this.hoveredSlot);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.module.isPlaying() && var3.equals(this.module.getStopReason()) ? "Replay" : "Play");
         if (var6) {
            var5 = Theme.textMuted;
         }
      } else {
         var3 = this.module.isPlaying() ? "Stop" : "Cancel";
         var4 = this.module.isPlaying() ? this.module.getStopReason() : "Emotes";
         var5 = Theme.textSecondary;
      }

      FontRenderer.TextStyle var13 = FontRenderer.TextStyle.MODULE;
      List var7 = wrapLabel(var3, var13, 72);
      int var8 = -(var7.size() * 12 + 11) / 2;

      for (String var10 : var7) {
         FontRenderer.drawText(var1, var10, -FontRenderer.measureWidth(var10, var13) / 2, var8, var5, var13);
         var8 += 12;
      }

      FontRenderer.TextStyle var14 = FontRenderer.TextStyle.EYEBROW;
      var4 = truncateLabel(var4, var14, 66);
      FontRenderer.drawText(var1, var4, -FontRenderer.measureWidth(var4, var14) / 2, var8 + 2, Theme.textSecondary, var14);
      if (!Double.isNaN(this.hoverAngle)) {
         int var15 = (int)Math.round(Math.cos(this.hoverAngle) * 45.0);
         int var11 = (int)Math.round(Math.sin(this.hoverAngle) * 45.0);
         DrawUtil.fillCircle(var1, var15, var11, 2, Theme.accent);
      }
   }

   private void drawHint(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = BindCodes.displayName(this.module.getWheelKey());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      FontRenderer.TextStyle var6 = FontRenderer.TextStyle.HELP;
      FontRenderer.drawText(var1, var5, var2 - FontRenderer.measureWidth(var5, var6) / 2, var3, Theme.textSecondary, var6);
   }

   private static List<String> wrapLabel(String var0, FontRenderer.TextStyle var1, int var2) {
      if (FontRenderer.measureWidth(var0, var1) <= var2) {
         return List.of(var0);
      }

      for (int var3 = var0.indexOf(32); var3 >= 0; var3 = var0.indexOf(32, var3 + 1)) {
         String var4 = var0.substring(0, var3);
         String var5 = var0.substring(var3 + 1);
         if (FontRenderer.measureWidth(var4, var1) <= var2 && FontRenderer.measureWidth(var5, var1) <= var2) {
            return List.of(var4, var5);
         }
      }

      return List.of(truncateLabel(var0, var1, var2));
   }

   private static String truncateLabel(String var0, FontRenderer.TextStyle var1, int var2) {
      if (FontRenderer.measureWidth(var0, var1) <= var2) {
         return var0;
      }

      String var3 = var0;

      while (var3.length() > 1 && FontRenderer.measureWidth(var3 + "...", var1) > var2) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3.strip() + "...";
   }
}

package com.larpclient.hud;

import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.clickgui.LegacyClickGuiScreen;
import com.larpclient.gui.hud.HudEditorScreen;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.PetsModule;
import com.larpclient.module.misc.StreamProofState;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public class NotificationManager implements HudElement {
   private static final NotificationManager instance = new NotificationManager();
   private final List<NotificationManager.Notification> notifications = new ArrayList<>();
   private static final int collapseThreshold = 5;
   private static final int iconY = 7;
   private static final int maxWidth = 260;
   private static final int maxNotifications = 10;
   public static final int height = 30;
   private static final int cornerRadius = 10;
   private static final int textX = 52;
   private static final int rightPadding = 14;
   private static final int textGap = 5;
   private static final int iconSize = 16;
   private static final int iconX = 12;
   private static final int iconTop = 7;
   private static final int chevronSize = 20;
   private static final int chevronX = 30;
   private static final int chevronY = 5;
   private static final double fontSize = 12.0;
   private static final double textY = SdfText.centerBaselineY(0.0, 30.0, 12.0);
   private static final int enterDurationMs = 330;
   private static final int exitDurationMs = 280;
   private static final int slideDurationMs = 240;
   private static final int enterOffset = 8;
   private static final int slideStartOffset = 14;
   private static final int backgroundColor = -603321844;
   private static final int textColor = -1;
   private static final int disabledColor = -6908266;
   private static final int chevronColor = -5592406;
   private static final double chevronAlpha = 0.58;
   public static final String enabledTitle = "Enabled";
   public static final String disabledTitle = "Disabled";
   private static final long toggleDurationMs = 2000L;

   public static NotificationManager getInstance() {
      return instance;
   }

   public void register() {
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "notificationmanager"), this);
   }

   public void notify(String var1, String var2, NotificationManager.Type var3, long var4) {
      PetsModule var6 = PetsModule.getInstance();
      if (var6 == null || !var6.showNotification(var1, var2, var3, var4)) {
         this.push(var1, var2, var3, var4);
      }
   }

   public void push(String var1, String var2, NotificationManager.Type var3, long var4) {
      this.addNotification(new NotificationManager.Notification(var1, var2, var3, Math.max(0L, var4)));
   }

   public void notifyDefault(String var1, String var2, NotificationManager.Type var3) {
      this.notify(var1, var2, var3, 4000L);
   }

   public void notifyToggle(String var1, boolean var2) {
      this.push(var2 ? "Enabled" : "Disabled", var1, NotificationManager.Type.INFO, 2000L);
   }

   private void addNotification(NotificationManager.Notification var1) {
      this.notifications.add(var1);
      int var2 = 0;

      for (int var3 = this.notifications.size() - 5; var2 < var3; var2++) {
         this.notifications.get(var2).collapse();
      }

      while (this.notifications.size() > 10) {
         this.notifications.removeFirst();
      }
   }

   public void clear() {
      this.notifications.clear();
   }

   private static String getModuleName() {
      return StreamProofState.isHidden() ? "Zoom" : "Kill Aura";
   }

   public void dismiss(String var1) {
      this.notifications.removeIf(var1x -> var1x.title.equals(var1));
      PetsModule var2 = PetsModule.getInstance();
      if (var2 != null) {
         var2.removeNotification(var1);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, DeltaTracker var2) {
      Minecraft var3 = Minecraft.getInstance();
      var1 = StreamProofManager.selectGraphics(var1, false, true);
      HudLayout.beginFrame(var1);
      if (var3.player != null
         && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         && !LegacyClickGuiScreen.isClickGuiScreen(var3.gui.screen())
         && !(var3.gui.screen() instanceof HudEditorScreen var4 && !var4.isLiveContent())) {
         VisualsRenderer.renderHud(var1);
         int var17 = var3.getWindow().getGuiScaledWidth();
         int var18 = var3.getWindow().getGuiScaledHeight();
         HudLayout.HudElement var6 = HudLayout.getElement("notifications");
         var6.width = 260;
         var6.height = 178;
         var6.fitTo(var17, var18);
         Iterator var7 = this.notifications.iterator();
         int var8 = 0;

         while (var7.hasNext()) {
            NotificationManager.Notification var9 = (NotificationManager.Notification)var7.next();
            var9.enterAnimation.animateToWithEasing(1.0, 330, Animation.Easing.EASE_OUT_BACK);
            if (var9.ageMs() > var9.durationMs) {
               var9.exitAnimation.animateToWithEasing(1.0, 280, Animation.Easing.EASE_OUT_SINE);
            }

            if (var9.exitAnimation.getValue() >= 0.95) {
               var7.remove();
            } else {
               double var10 = Math.clamp(1.0 - var9.exitAnimation.getValue(), 0.0, 1.0);
               if (!var6.isHidden() && !(var10 <= 0.01)) {
                  int var12 = measureWidth(var9.title, var9.message);
                  int var13 = alignX(var6.getX(var17), var17, var12);
                  int var14 = var8 * 37;
                  if (!var9.positioned) {
                     var9.positioned = (boolean)1;
                     var9.slideAnimation.snapTo(var14 - 14);
                  }

                  int var15 = var6.getY(var18)
                     + (int)Math.round(
                        var9.slideAnimation.animateToWithEasing(var14, 240, Animation.Easing.EASE_OUT_QUAD) - (1.0 - var9.enterAnimation.getValue()) * 8.0
                     );
                  drawAnimated(var1, var6, var17, var18, var13, var15, var12, var9.title, var9.message, var9.type, var10);
                  var8++;
               }
            }
         }

         if (var8 == 0 && !var6.isHidden() && HudLayout.isChatOpen()) {
            int var19 = measureWidth("Enabled", getModuleName());
            drawAnimated(
               var1,
               var6,
               var17,
               var18,
               alignX(var6.getX(var17), var17, var19),
               var6.getY(var18),
               var19,
               "Enabled",
               getModuleName(),
               NotificationManager.Type.INFO,
               1.0
            );
         }
      }
   }

   private static void drawAnimated(
      GuiGraphicsExtractor var0,
      HudLayout.HudElement var1,
      int var2,
      int var3,
      int var4,
      int var5,
      int var6,
      String var7,
      String var8,
      NotificationManager.Type var9,
      double var10
   ) {
      var1.getY(var0, var1.getX(var2), var1.getY(var3));

      try {
         drawNotification(var0, var4, var5, var6, var7, var8, var9, var10);
      } finally {
         var1.popScale(var0);
      }

      HudLayout.addBounds(
         var1,
         new HudLayout2.Rect(
            var1.getX(var2) + (int)Math.floor((var4 - var1.getX(var2)) * var1.effectiveScale()),
            var1.getY(var3) + (int)Math.floor((var5 - var1.getY(var3)) * var1.effectiveScale()),
            (int)Math.ceil(var6 * var1.effectiveScale()),
            (int)Math.ceil(30.0 * var1.effectiveScale())
         )
      );
   }

   public boolean isAnimating() {
      return this.notifications.stream().anyMatch(var0 -> var0.ageMs() < var0.durationMs + 280L);
   }

   private static int alignX(int var0, int var1, int var2) {
      return var0 + (var0 + 130 > var1 / 2 ? 260 - var2 : 0);
   }

   private static int measureText(String var0, String var1) {
      if (var0.isBlank()) {
         return HudPanels.textWidthAtSize(var1, 12.0);
      }

      int var2 = HudPanels.textWidthAtSize(var0, 12.0);
      return var1.isBlank() ? var2 : var2 + 5 + HudPanels.textWidthAtSize(var1, 12.0);
   }

   private static int measureWidth(String var0, String var1) {
      return Math.min(260, 52 + measureText(var0, var1) + 14);
   }

   private static NotificationManager.TitleKind classifyTitle(String var0) {
      return "Enabled".equals(var0)
         ? NotificationManager.TitleKind.ON
         : ("Disabled".equals(var0) ? NotificationManager.TitleKind.OFF : NotificationManager.TitleKind.NONE);
   }

   private static String iconFor(String var0, NotificationManager.Type var1) {
      if (classifyTitle(var0) != NotificationManager.TitleKind.NONE) {
         return "power";
      }

      return switch (var1) {
         case INFO, WARNING -> "bell";
         case DANGER -> "x";
         case SUCCESS -> "badge-check";
      };
   }

   private static int colorFor(String var0, NotificationManager.Type var1) {
      NotificationManager.TitleKind var2 = classifyTitle(var0);
      if (var2 != NotificationManager.TitleKind.NONE) {
         return var2 == NotificationManager.TitleKind.ON ? Theme.accent : -6908266;
      }

      return switch (var1) {
         case INFO, SUCCESS -> Theme.accent;
         case WARNING -> -996986;
         case DANGER -> -6908266;
      };
   }

   public static void renderPreview(GuiGraphicsExtractor var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = measureWidth("Enabled", getModuleName());
      drawNotification(
         var0, alignX(var1, var3.getWindow().getGuiScaledWidth(), var4), var2, var4, "Enabled", getModuleName(), NotificationManager.Type.INFO, 1.0
      );
   }

   private static void drawNotification(
      GuiGraphicsExtractor var0, int var1, int var2, int var3, String var4, String var5, NotificationManager.Type var6, double var7
   ) {
      DrawUtil.fillRoundedRect(var0, var1, var2, var3, 30.0F, 10.0F, ColorUtil.multiplyAlpha(-603321844, var7));
      HudPanels.drawIconOrFallback(var0, iconFor(var4, var6), var1 + 12, var2 + 7, 16, ColorUtil.multiplyAlpha(colorFor(var4, var6), var7));
      HudPanels.drawIconOrFallback(var0, "chevron-right", var1 + 30, var2 + 5, 20, ColorUtil.multiplyAlpha(-5592406, var7 * 0.58));
      int var9 = var3 - 52 - 14;
      if (var4.isBlank()) {
         HudPanels.drawText(var0, HudPanels.truncate(var5, var9, 12.0), var1 + 52, var2 + textY, 12.0, ColorUtil.multiplyAlpha(-1, var7));
      } else {
         String var10 = HudPanels.truncate(var4, var9, 12.0);
         HudPanels.drawText(var0, var10, var1 + 52, var2 + textY, 12.0, ColorUtil.multiplyAlpha(-1, var7));
         if (!var5.isBlank()) {
            int var11 = HudPanels.textWidthAtSize(var10, 12.0) + 5;
            HudPanels.drawText(
               var0, HudPanels.truncate(var5, var9 - var11, 12.0), var1 + 52 + var11, var2 + textY, 12.0, ColorUtil.multiplyAlpha(Theme.accent, var7)
            );
         }
      }
   }

   private static final class Notification {
      final String title;
      final String message;
      final NotificationManager.Type type;
      final long durationMs;
      long startNanos = System.nanoTime();
      boolean positioned;
      final Animation enterAnimation = new Animation(0.0);
      final Animation exitAnimation = new Animation(0.0);
      final Animation slideAnimation = new Animation(0.0);

      Notification(String var1, String var2, NotificationManager.Type var3, long var4) {
         this.title = var1 == null ? "" : var1;
         this.message = var2 == null ? "" : var2;
         this.type = var3;
         this.durationMs = var4;
      }

      long ageMs() {
         return (System.nanoTime() - this.startNanos) / 1000000L;
      }

      void collapse() {
         this.startNanos = Math.min(this.startNanos, System.nanoTime() - this.durationMs * 1000000L);
      }
   }

   private enum TitleKind {
      NONE,
      ON,
      OFF;
   }

   public enum Type {
      INFO,
      WARNING,
      DANGER,
      SUCCESS;
   }
}

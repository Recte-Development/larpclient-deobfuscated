package com.larpclient.gui.widget;

import com.larpclient.config.ThemePalette;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public final class MenuButton extends Widget {
   public String label;
   public boolean selected;
   public boolean flat;
   public boolean accented;
   public boolean danger;
   public boolean highlightOnSelectOnly;
   public Boolean toggleState;
   public Runnable secondaryAction;
   public GuiSounds.Sound clickSound = GuiSounds.Sound.CLICK;
   public String subtitle = "";
   public String icon = "";
   public String tooltip = "";
   public boolean moduleCard;
   public boolean showKeybind;
   public boolean popupEntry;
   public boolean navItem;
   public boolean serverCard;
   public boolean pill;
   public boolean centeredText;
   public Object tag;
   public double alpha = 1.0;
   public double labelAlpha = 1.0;
   public boolean scissorLabel;
   private final Runnable onClick;
   private final Animation hoverAnim = new Animation(0.0);
   private final Animation selectedAnim = new Animation(0.0);
   private final Animation toggleAnim = new Animation(0.0);
   private final Animation pressAnim = new Animation(0.0);
   private boolean toggleInitialized;
   private ThemePalette palette = ThemePalette.forBackground(0.0, 0.0, Theme.accentAt(this.x));
   private long pressFlashUntil;
   private long rippleStart;
   private float rippleX;
   private float rippleY;
   private float hoverLift;

   public ThemePalette getPalette() {
      return this.palette;
   }

   public float getHoverLift() {
      return this.hoverLift;
   }

   public double hoverProgress(int var1, int var2) {
      return this.hoverAnim.animateTo(!this.contains(var1, var2) && (!this.focused || !this.focusVisible) ? 0.0 : 1.0, 160);
   }

   public MenuButton(String var1, Runnable var2) {
      this.label = var1;
      this.onClick = var2;
   }

   public static int iconSize(int var0) {
      return (var0 < 144 ? 18 : 34) + 14;
   }

   public int labelWidth() {
      return Math.max(1, this.width - (this.showKeybind ? 82 : 54));
   }

   public int toggleY() {
      return this.y + (this.height - 13) / 2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.visible) {
         double var4 = this.hoverProgress(var2, var3);
         double var6 = this.selectedAnim.animateTo(this.selected && !this.highlightOnSelectOnly ? 1.0 : 0.0, 180);
         double var8 = this.pressAnim.animateTo(System.nanoTime() < this.pressFlashUntil ? 1.0 : 0.0, 100);
         if (this.pill) {
            int var35 = this.accented ? (this.danger ? -24915 : ThemePalette.readableAccent(Theme.accentAt(this.x), Theme.background)) : Theme.rowBackground;
            if (!this.enabled) {
               var4 = 0.0;
            }

            int var36 = Theme.radiusMedium;
            if (this.accented && Theme.isThemed()) {
               GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var36, 5.0F, ColorUtil.withAlpha(var35, 0.18 + 0.14 * var4));
            }

            if (!this.accented && Theme.isThemed()) {
               if (var4 > 0.001) {
                  GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var36, ColorUtil.withAlpha(Theme.textPrimary, var4 * 0.045));
               }

               RoundedShapes.drawRimGradient(
                  var1,
                  this.x,
                  this.y,
                  this.width,
                  this.height,
                  var36,
                  ColorUtil.multiplyAlpha(Theme.accentGlow, 1.3 + var4 * 0.7),
                  ColorUtil.multiplyAlpha(Theme.accentSoft, 1.6 + var4 * 0.9)
               );
            } else {
               GlassPainter.fillRoundedRect(
                  var1, this.x, this.y, this.width, this.height, var36, ColorUtil.lerpColor(var35, Theme.cardHoverBackground, var4 * 0.15)
               );
            }

            int var42 = !this.enabled
               ? Theme.textMuted
               : (
                  this.accented
                     ? ThemePalette.controlInk(var35)
                     : (this.danger ? -24915 : ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, this.tag != null ? 0.7 + 0.3 * var4 : var4))
               );
            if (!this.icon.isEmpty()) {
               Icons.texture(var1, Icons.get(this.icon), this.x + (this.width - 10) / 2, this.y + (this.height - 10) / 2, 10, var42);
            } else {
               FontRenderer.TextStyle var44 = this.tag != null ? FontRenderer.TextStyle.BODY : FontRenderer.TextStyle.HELP;
               String var46 = FontRenderer.trimToWidth(this.label, this.width - 8, var44);
               int var48 = Theme.isThemed() ? this.x + (this.width - FontRenderer.measureWidth(var46, var44)) / 2 : this.x + 4;
               FontRenderer.drawText(var1, var46, var48, this.y + (this.height - var44.lineHeight()) / 2, var42, var44);
            }

            if (this.focused && this.focusVisible) {
               GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, var36);
            }
         } else if (this.serverCard) {
            int var34 = Theme.isThemed() ? Theme.radiusCard : 6;
            if (Theme.isThemed()) {
               GlassPainter.drawFocusedField(
                  var1,
                  this.x,
                  this.y,
                  this.width,
                  this.height,
                  var34,
                  ColorUtil.lerpColor(Theme.cardBackground, Theme.cardHoverBackground, var4),
                  0.4 + 0.6 * var4
               );
            } else {
               GlassPainter.fillRoundedRect(
                  var1, this.x, this.y, this.width, this.height, var34, ColorUtil.lerpColor(Theme.background, Theme.cardHoverBackground, var4 * 0.35)
               );
            }

            int var11 = iconSize(this.width);
            int var41 = var11 - 14;
            GlassPainter.fillRoundedRect(var1, this.x + 7, this.y + 7, var41, var41, 6, Theme.headerBackground);
            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               Identifier.fromNamespaceAndPath("minecraft", "textures/misc/unknown_server.png"),
               this.x + 7,
               this.y + 7,
               0.0F,
               0.0F,
               var41,
               var41,
               64,
               64,
               64,
               64,
               AlphaScope.applyAlpha(-1)
            );
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(this.label, this.width - var11 - 7, FontRenderer.TextStyle.MODULE),
               this.x + var11,
               this.y + 6,
               Theme.textPrimary,
               FontRenderer.TextStyle.MODULE
            );
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(this.subtitle, this.width - var11 - 7, FontRenderer.TextStyle.HELP),
               this.x + var11,
               this.y + 19,
               Theme.textSecondary,
               FontRenderer.TextStyle.HELP
            );
            if (this.focused && this.focusVisible) {
               GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, var34);
            }
         } else if (this.popupEntry) {
            this.renderPopupEntry(var1, var4, var8);
         } else if (this.navItem) {
            this.renderNavItem(var1, var4, var6, var8);
         } else {
            if (!this.toggleInitialized && this.toggleState != null) {
               this.toggleAnim.snapTo(this.toggleState ? 1.0 : 0.0);
               this.toggleInitialized = (boolean)1;
            }

            double var10 = this.toggleAnim.animateTo(Boolean.TRUE.equals(this.toggleState) ? 1.0 : 0.0, 180);
            var1.pose().pushMatrix();

            try {
               if (this.moduleCard && Theme.isThemed()) {
                  this.palette = ThemePalette.forBackground(var10, var4, Theme.accentAt(this.x));
                  Theme.Look var39 = Theme.getLook();
                  int var43 = this.palette.accent();
                  boolean var45 = var39.enabled().equals("Glow");
                  int var47 = !var45 && !var39.enabled().equals("Tint") ? 0 : 1;
                  int var16 = Theme.radiusCard;
                  this.hoverLift = var39.hoverLift() ? (float)var4 : 0.0F;
                  var1.pose().translate(0.0F, -this.hoverLift);
                  if (this.hoverLift > 0.01F && !var39.card().equals("Flat")) {
                     RoundedShapes.drawGlow(
                        var1,
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        var16,
                        5.0F,
                        ColorUtil.multiplyAlpha(Theme.shadowColor, 0.25 * this.hoverLift),
                        ColorUtil.multiplyAlpha(Theme.shadowColor, 0.6 * this.hoverLift)
                     );
                  }

                  if (var45 && var10 > 0.01) {
                     GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var16, 9.0F, ColorUtil.withAlpha(var43, 0.14 * var10));
                  }

                  switch (var39.card()) {
                     case "Outline":
                        if (var4 > 0.001) {
                           GlassPainter.fillRoundedRect(
                              var1, this.x, this.y, this.width, this.height, var16, ColorUtil.withAlpha(Theme.textPrimary, 0.035 * var4)
                           );
                        }
                        break;
                     case "Flat":
                        GlassPainter.fillRoundedRect(
                           var1, this.x, this.y, this.width, this.height, var16, ColorUtil.multiplyAlpha(this.palette.background(), 0.75)
                        );
                        break;
                     default:
                        GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var16, this.palette.background());
                  }

                  if (var47 != 0 && var10 > 0.01) {
                     RoundedShapes.fillRoundedGradientHorizontal(
                        var1,
                        this.x,
                        this.y,
                        this.width * 0.7F,
                        this.height,
                        var16,
                        ColorUtil.withAlpha(var43, (var45 ? 0.15 : 0.22) * var10),
                        ColorUtil.withAlpha(var43, 0.0)
                     );
                  }

                  if (!var39.card().equals("Flat")) {
                     double var49 = (var39.card().equals("Outline") ? 0.85 : 0.55) + 0.25 * var4;
                     int var19 = ColorUtil.lerpColor(
                        ColorUtil.withAlpha(Theme.controlBackground, var49), ColorUtil.withAlpha(var43, 0.55), var47 != 0 ? 0.8 * var10 : 0.0
                     );
                     RoundedShapes.drawRimGradient(var1, this.x, this.y, this.width, this.height, var16, var19, var19);
                  }

                  double var50 = (System.nanoTime() - this.rippleStart) / 6.5E8;
                  if (var39.ripple() && var50 < 1.0) {
                     float var51 = 10.0F + (float)Animation.Easing.EASE_OUT_CUBIC.apply(var50) * this.width * 0.9F;
                     RoundedShapes.drawRipple(
                        var1,
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        var16,
                        this.rippleX,
                        this.rippleY,
                        var51,
                        ColorUtil.withAlpha(var43, 0.22 * (1.0 - var50))
                     );
                  }

                  if (var6 > 0.001) {
                     GlassPainter.fillRoundedRect(
                        var1, this.x, this.y, this.width, this.height, Theme.radiusCard, ColorUtil.withAlpha(Theme.textPrimary, 0.045 * var6)
                     );
                     RoundedShapes.drawRimGradient(
                        var1,
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        Theme.radiusCard,
                        ColorUtil.withAlpha(this.palette.accent(), 0.28 * var6),
                        ColorUtil.withAlpha(this.palette.accent(), 0.18 * var6)
                     );
                     GlassPainter.drawAnimatedBorder(
                        var1, this.x, this.y, this.width, this.height, Theme.radiusCard, ColorUtil.withAlpha(this.palette.accent(), var6)
                     );
                  }

                  int var52 = ColorUtil.lerpColor(
                     ColorUtil.lerpColor(this.palette.subtitle(), this.palette.title(), 0.75 + 0.25 * var4), this.palette.title(), var10
                  );
                  if (var39.enabled().equals("Text")) {
                     var52 = ColorUtil.lerpColor(var52, var43, var10);
                  }

                  FontRenderer.drawText(
                     var1,
                     FontRenderer.trimToWidth(this.label, this.labelWidth(), FontRenderer.TextStyle.MODULE),
                     this.x + 12,
                     this.y + this.height / 2 - 12,
                     var52,
                     FontRenderer.TextStyle.MODULE
                  );
                  FontRenderer.drawText(
                     var1,
                     FontRenderer.trimToWidth(this.subtitle, this.labelWidth(), FontRenderer.TextStyle.BODY),
                     this.x + 12,
                     this.y + this.height / 2 + 2,
                     this.palette.subtitle(),
                     FontRenderer.TextStyle.BODY
                  );
                  GlassPainter.drawToggle(var1, this.x + this.width - 32, this.toggleY(), var10, this.palette.accent(), this.palette.light());
               } else if (this.moduleCard) {
                  this.palette = ThemePalette.forBackground(var10, var4, Theme.accentAt(this.x));
                  GlassPainter.drawPanel(
                     var1,
                     this.x,
                     this.y,
                     this.width,
                     this.height,
                     Theme.radiusCard,
                     ColorUtil.lerpColor(this.palette.background(), Theme.borderColor, var6),
                     0.9 + var4 * 0.6
                  );
                  if (var6 > 0.001) {
                     RoundedShapes.drawRimGradient(
                        var1,
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        Theme.radiusCard,
                        ColorUtil.withAlpha(Theme.textPrimary, 0.34 * var6),
                        ColorUtil.withAlpha(Theme.textPrimary, 0.26 * var6)
                     );
                  }

                  FontRenderer.drawText(
                     var1,
                     FontRenderer.trimToWidth(this.label, this.labelWidth(), FontRenderer.TextStyle.MODULE),
                     this.x + 12,
                     this.y + this.height / 2 - 12,
                     this.palette.title(),
                     FontRenderer.TextStyle.MODULE
                  );
                  FontRenderer.drawText(
                     var1,
                     FontRenderer.trimToWidth(this.subtitle, this.labelWidth(), FontRenderer.TextStyle.BODY),
                     this.x + 12,
                     this.y + this.height / 2 + 2,
                     this.palette.subtitle(),
                     FontRenderer.TextStyle.BODY
                  );
                  GlassPainter.drawToggle(var1, this.x + this.width - 32, this.toggleY(), var10, this.palette.accent(), this.palette.light());
               } else if (this.centeredText) {
                  if (var4 > 0.001) {
                     GlassPainter.fillRoundedRect(
                        var1, this.x, this.y, this.width, this.height, Theme.radiusMedium, ColorUtil.withAlpha(Theme.textPrimary, var4 * 0.045)
                     );
                  }

                  RoundedShapes.drawRimGradient(
                     var1,
                     this.x,
                     this.y,
                     this.width,
                     this.height,
                     Theme.radiusMedium,
                     ColorUtil.multiplyAlpha(Theme.accentGlow, 1.3 + var4 * 0.7),
                     ColorUtil.multiplyAlpha(Theme.accentSoft, 1.6 + var4 * 0.9)
                  );
                  String var38 = FontRenderer.getTextWidth(this.label, Math.max(1, this.width - 12));
                  FontRenderer.drawText(
                     var1,
                     var38,
                     this.x + (this.width - FontRenderer.drawDefaultText(var38)) / 2,
                     this.y + (this.height - 9) / 2,
                     this.enabled ? ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, var4) : Theme.textMuted
                  );
               } else {
                  if (this.flat) {
                     double var12 = Math.max(var6, var4 * 0.6);
                     if (var12 > 0.001) {
                        GlassPainter.fillRoundedRect(
                           var1, this.x, this.y, this.width, this.height, Theme.radiusMedium, ColorUtil.withAlpha(Theme.textPrimary, 0.065 * var12)
                        );
                     }
                  } else {
                     GlassPainter.drawPanel(
                        var1,
                        this.x,
                        this.y,
                        this.width,
                        this.height,
                        Theme.radiusLarge,
                        ColorUtil.lerpColor(Theme.cardBackground, Theme.cardHoverBackground, Math.max(var6 * 0.65, var4)),
                        0.9 + var4 * 0.6
                     );
                  }

                  int var37 = this.icon.isEmpty() ? 8 : 27;
                  int var13 = Math.min(13, Math.max(1, this.height - 2));
                  if (!this.icon.isEmpty()) {
                     Icons.texture(
                        var1,
                        Icons.get(this.icon),
                        this.x + (this.label.isEmpty() ? (this.width - var13) / 2 : 8),
                        this.y + (this.height - var13) / 2,
                        var13,
                        this.selected ? Theme.textPrimary : ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, var4)
                     );
                  }

                  int var14 = this.width - var37 - (this.toggleState == null ? 8 : 34);
                  if (var14 > 4 && this.labelAlpha > 0.001) {
                     if (this.scissorLabel) {
                        var1.enableScissor(this.x + var37, this.y, this.x + this.width - 8, this.y + this.height);
                     }

                     try (AlphaScope var15 = AlphaScope.push(this.labelAlpha)) {
                        FontRenderer.drawText(
                           var1,
                           this.scissorLabel ? this.label : FontRenderer.getTextWidth(this.label, var14),
                           this.x + var37,
                           this.y + (this.subtitle.isEmpty() ? (this.height - 9) / 2 : 4),
                           this.enabled
                              ? (
                                 this.danger
                                    ? -24915
                                    : (
                                       this.accented
                                          ? Theme.accentAt(this.x)
                                          : (
                                             this.highlightOnSelectOnly && !this.selected
                                                ? ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, var4)
                                                : Theme.textPrimary
                                          )
                                    )
                              )
                              : Theme.textSecondary
                        );
                     } finally {
                        if (this.scissorLabel) {
                           var1.disableScissor();
                        }
                     }
                  }

                  if (!this.subtitle.isEmpty()) {
                     FontRenderer.drawText(var1, FontRenderer.getTextWidth(this.subtitle, this.width - 42), this.x + 8, this.y + 16, Theme.textSecondary);
                  }

                  if (this.toggleState != null) {
                     GlassPainter.drawPulsingGlow(var1, this.x + this.width - 32, this.y + (this.height - 12) / 2, var10);
                  }
               }

               int var40 = this.moduleCard ? Theme.radiusCard : Theme.radiusMedium;
               if (var8 > 0.001) {
                  GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var40, ColorUtil.withAlpha(Theme.textPrimary, var8 * 0.07));
               }

               if (this.focused && this.focusVisible) {
                  GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, var40);
               }
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private void renderNavItem(GuiGraphicsExtractor var1, double var2, double var4, double var6) {
      double var8 = Math.max(var2, var6) * (1.0 - var4);
      if (var8 > 0.001) {
         DrawUtil.fillRoundedRect(var1, this.x, this.y, this.width, this.height, Theme.radiusControl, ColorUtil.withAlpha(Theme.textPrimary, 0.05 * var8));
      }

      int var10 = ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, 0.45);
      int var11 = ColorUtil.lerpColor(ColorUtil.lerpColor(var10, Theme.textPrimary, var2 * 0.85), Theme.textPrimary, var4);
      int var12 = Math.min(12, this.height - 2);
      int var13 = Theme.readableAccent(this.x, Theme.background);
      if (ThemePalette.controlInk(Theme.background) == -1) {
         var13 = ColorUtil.lerpColor(var13, -1, 0.12);
      }

      if (!this.icon.isEmpty()) {
         Icons.texture(
            var1,
            Icons.get(this.icon),
            this.x + 8,
            this.y + (this.height - var12) / 2,
            var12,
            ColorUtil.lerpColor(ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, var2 * 0.7), var13, var4)
         );
      }

      if (this.labelAlpha > 0.001) {
         byte var14 = 27;
         var1.enableScissor(this.x + var14, this.y, this.x + this.width - 4, this.y + this.height);

         try (AlphaScope var15 = AlphaScope.push(this.labelAlpha)) {
            FontRenderer.TextStyle var16 = FontRenderer.TextStyle.NAVIGATION;
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(this.label, Math.max(1, this.width - var14 - 4), var16),
               this.x + var14,
               this.y + (this.height - var16.lineHeight()) / 2,
               var11,
               var16
            );
         } finally {
            var1.disableScissor();
         }
      }

      if (this.focused && this.focusVisible) {
         GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, Theme.radiusControl);
      }
   }

   private void renderPopupEntry(GuiGraphicsExtractor var1, double var2, double var4) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.x + this.width / 2.0F, this.y + this.height / 2.0F);
         float var6 = (float)(1.0 + 0.018 * var2 - 0.025 * var4);
         var1.pose().scale(var6, var6);
         var1.pose().translate(-this.x - this.width / 2.0F, -this.y - this.height / 2.0F);
         GlassPainter.fillRoundedRect(
            var1, this.x, this.y, this.width, this.height, 2, ColorUtil.multiplyAlpha(ColorUtil.lerpColor(-2144326101, -1337177788, var2), this.alpha)
         );
         int var7 = ColorUtil.multiplyAlpha(this.enabled ? -1711656 : -6711922, this.alpha);
         int var8 = this.icon.isEmpty() ? 7 : 22;
         if (!this.icon.isEmpty()) {
            Icons.texture(var1, Icons.get(this.icon), this.x + 6, this.y + (this.height - 10) / 2, 10, var7);
         }

         FontRenderer.drawText(
            var1,
            FontRenderer.getTextWidth(this.label, this.width - var8 - 6),
            this.x + var8 + (int)Math.round(var2 * 2.0),
            this.y + (this.height - 9) / 2,
            var7
         );
         if (this.focused && this.focusVisible) {
            GlassPainter.drawInsetBorder(var1, this.x, this.y, this.width, this.height, ColorUtil.multiplyAlpha(-1711656, this.alpha));
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.enabled && var5 == 0 && this.contains(var1, var3)) {
         this.pressFlashUntil = System.nanoTime() + 110000000L;
         if (this.moduleCard) {
            this.rippleStart = System.nanoTime();
            this.rippleX = (float)var1;
            this.rippleY = (float)var3;
         }

         GuiSounds.play(this.clickSound);
         if (this.secondaryAction != null && var1 >= this.x + this.width - 38) {
            this.secondaryAction.run();
         } else {
            this.onClick.run();
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.visible && this.focused && this.enabled && (var1 == 257 || var1 == 32)) {
         this.pressFlashUntil = System.nanoTime() + 110000000L;
         GuiSounds.play(this.clickSound);
         if (this.moduleCard && var1 == 32 && this.secondaryAction != null) {
            this.secondaryAction.run();
         } else {
            this.onClick.run();
         }

         return true;
      } else {
         return false;
      }
   }
}

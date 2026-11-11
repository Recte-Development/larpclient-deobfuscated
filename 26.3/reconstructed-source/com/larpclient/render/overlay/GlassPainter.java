package com.larpclient.render.overlay;

import com.larpclient.LarpClient;
import com.larpclient.config.ThemePalette;
import com.larpclient.gui.theme.Theme;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Vector2f;

public final class GlassPainter {
   private static final Identifier fILL_TEXTURE = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/fill.png");
   private static final Identifier rIM_TEXTURE = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/glass/rim.png");
   private static final int[] sLICE_OFFSETS = new int[]{0, 48, 80};
   private static final int[] sLICE_SIZES = new int[]{48, 32, 48};
   static final double dEFAULT_RADIUS = 6.0;

   private GlassPainter() {
   }

   private static void drawNineSlice(GuiGraphicsExtractor var0, Identifier var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      var7 = AlphaScope.applyAlpha(var7);
      if (var4 > 0 && var5 > 0 && var7 >>> 24 != 0) {
         int var8 = Math.min(var6, Math.min(var4, var5) / 2);

         for (int var9 = 0; var9 < 3; var9++) {
            int var10 = var9 == 0 ? var3 : (var9 == 1 ? var3 + var8 : var3 + var5 - var8);
            int var11 = var9 == 1 ? var5 - 2 * var8 : var8;
            if (var11 > 0) {
               for (int var12 = 0; var12 < 3; var12++) {
                  int var13 = var12 == 0 ? var2 : (var12 == 1 ? var2 + var8 : var2 + var4 - var8);
                  int var14 = var12 == 1 ? var4 - 2 * var8 : var8;
                  if (var14 > 0) {
                     var0.blit(
                        RenderPipelines.GUI_TEXTURED,
                        var1,
                        var13,
                        var10,
                        sLICE_OFFSETS[var12],
                        sLICE_OFFSETS[var9],
                        var14,
                        var11,
                        sLICE_SIZES[var12],
                        sLICE_SIZES[var9],
                        128,
                        128,
                        var1.equals(rIM_TEXTURE) ? ColorUtil.multiplyAlpha(var7, var9 == 0 ? 1.0 : (var9 == 1 ? 0.55 : 0.28)) : var7
                     );
                  }
               }
            }
         }
      }
   }

   public static void fillRoundedRect(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      RoundedShapes.fillRounded(var0, var1, var2, var3, var4, var5, var6);
   }

   static void drawGlassFill(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      drawNineSlice(var0, fILL_TEXTURE, var1, var2, var3, var4, var5, var6);
   }

   static void drawGlassRim(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      drawNineSlice(var0, rIM_TEXTURE, var1, var2, var3, var4, var5, var6);
   }

   public static void drawGlassFill(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, double var8) {
      fillRoundedRect(var0, var1, var2, var3, var4, var5, ColorUtil.lerpColor(var6, var7, 0.6));
      RoundedShapes.drawRimGradient(
         var0, var1, var2, var3, var4, var5, ColorUtil.multiplyAlpha(Theme.accentGlow, var8), ColorUtil.multiplyAlpha(Theme.accentSoft, var8)
      );
   }

   public static void drawPanel(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      drawGlassFill(var0, var1, var2, var3, var4, var5, var6, var6, var7);
   }

   public static void drawWindow(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      drawWindowBackground(var0, var1, var2, var3, var4);
      drawWindowBorder(var0, var1, var2, var3, var4);
   }

   public static void drawWindowBackground(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = Minecraft.getInstance().level != null ? 1 : 0;
      int var6 = var5 != 0 && !ScreenWarp.warpGraphics() && PostProcessing.isBlurActive() ? 1 : 0;
      if (var6 != 0) {
         Vector2f var7 = var0.pose().transformPosition(var1, var2, new Vector2f());
         float var8 = var3 * var0.pose().m00();
         float var9 = var3 * var0.pose().m01();
         float var10 = var4 * var0.pose().m10();
         float var11 = var4 * var0.pose().m11();
         PostProcessing.addBlurRegion(
            Math.round(var7.x + Math.min(0.0F, var8) + Math.min(0.0F, var10)),
            Math.round(var7.y + Math.min(0.0F, var9) + Math.min(0.0F, var11)),
            Math.round(Math.abs(var8) + Math.abs(var10)),
            Math.round(Math.abs(var9) + Math.abs(var11))
         );
      }

      drawShadow(var0, var1, var2, var3, var4);
      fillRoundedRect(var0, var1, var2, var3, var4, Theme.radiusPill, var5 != 0 && var6 == 0 ? Theme.backgroundColor() : Theme.panelBackground);
      if (Theme.isThemed()) {
         RoundedShapes.fillRoundedGradient(
            var0,
            var1,
            var2,
            var3,
            var4 * 0.5F,
            Theme.radiusPill,
            ColorUtil.withAlpha(Theme.accent, 0.07 * Theme.accentSaturation()),
            ColorUtil.withAlpha(Theme.accent, 0.0)
         );
      }
   }

   public static void drawPulsingGlow(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      LarpClient var5 = LarpClient.getInstance();
      int var6 = var5 != null && !var5.getConfigManager().getPreferences().motion ? 0 : 1;
      double var7 = var6 != 0 ? 0.5 + 0.5 * Math.sin(System.nanoTime() / 1.0E9 * 1.6) : 0.5;
      fillRoundedRect(var0, var1, var2, var3, var4, Theme.radiusPill, 14.0F, ColorUtil.withAlpha(Theme.accent, (0.12 + 0.05 * var7) * Theme.accentSaturation()));
   }

   public static void drawWindowBorder(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (Theme.isThemed()) {
         RoundedShapes.drawRimGradient(
            var0,
            var1,
            var2,
            var3,
            var4,
            Theme.radiusPill,
            ColorUtil.lerpColor(Theme.accentGlow, ColorUtil.withAlpha(Theme.accent, 0.5), 0.65 * Theme.accentSaturation()),
            Theme.accentSoft
         );
      } else {
         RoundedShapes.drawRimGradient(var0, var1, var2, var3, var4, Theme.radiusPill, Theme.accentGlow, Theme.accentSoft);
      }
   }

   public static void drawFocusedField(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, double var7) {
      if (Theme.isThemed() && var7 > 0.0) {
         RoundedShapes.drawGlow(
            var0,
            var1,
            var2,
            var3,
            var4,
            var5,
            10.0F + 6.0F * (float)var7,
            ColorUtil.multiplyAlpha(Theme.shadowColor, 0.35 * var7),
            ColorUtil.multiplyAlpha(Theme.shadowColor, 0.7 * var7)
         );
      }

      fillRoundedRect(var0, var1, var2, var3, var4, var5, var6);
      RoundedShapes.drawRimGradient(var0, var1, var2, var3, var4, var5, Theme.accentGlow, Theme.accentGlow);
   }

   public static void fillRoundedRect(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, float var6, int var7) {
      if (Theme.isThemed() && PostProcessing.bloomQuality != PostProcessing.Quality.OFF) {
         RoundedShapes.drawGlow(var0, var1, var2, var3, var4, var5, var6, var7, var7);
      }
   }

   public static void drawAnimatedBorder(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      if (Theme.isThemed()) {
         LarpClient var7 = LarpClient.getInstance();
         double var8 = var7 != null && !var7.getConfigManager().getPreferences().motion ? 0.0 : System.nanoTime() / 1.0E9 / 6.0;
         RoundedShapes.drawContourLight(var0, var1, var2, var3, var4, var5, PostProcessing.bloomQuality == PostProcessing.Quality.OFF ? 0.0F : 6.0F, var8, var6);
      }
   }

   public static void drawButton(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      drawWindowBackground(var0, var1, var2, var3, var4, Theme.isThemed() ? Math.min(8, var4 / 2) : Theme.radiusMedium, var5);
   }

   public static void drawWindowBackground(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, double var6) {
      if (!Theme.isThemed()) {
         drawPanel(var0, var1, var2, var3, var4, var5, Theme.rowBackground, 0.5 + 0.6 * var6);
         if (var6 > 0.001) {
            RoundedShapes.drawRimGradient(
               var0, var1, var2, var3, var4, var5, ColorUtil.withAlpha(Theme.textPrimary, 0.45 * var6), ColorUtil.withAlpha(Theme.textPrimary, 0.25 * var6)
            );
         }
      } else {
         int var8 = Theme.readableAccent(var1, Theme.rowBackground);
         if (var6 > 0.001) {
            RoundedShapes.drawGlow(var0, var1, var2, var3, var4, var5, 7.0F, ColorUtil.withAlpha(var8, 0.24 * var6), ColorUtil.withAlpha(var8, 0.24 * var6));
         }

         fillRoundedRect(var0, var1, var2, var3, var4, var5, ColorUtil.lerpColor(Theme.rowBackground, Theme.cardHoverBackground, 0.5 * var6));
         RoundedShapes.drawRimGradient(
            var0,
            var1,
            var2,
            var3,
            var4,
            var5,
            ColorUtil.lerpColor(Theme.accentGlow, ColorUtil.withAlpha(var8, 0.9), var6),
            ColorUtil.lerpColor(Theme.accentSoft, ColorUtil.withAlpha(var8, 0.9), var6)
         );
      }
   }

   public static void drawSelectedItem(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      if (Theme.isThemed()) {
         drawFocusedField(var0, var1, var2, var3, var4, Theme.radiusCard, ColorUtil.lerpColor(Theme.cardBackground, Theme.overlayTint | 0xFF000000, 0.35), 1.0);
      } else {
         fillRoundedRect(var0, var1, var2, var3, var4, Theme.radiusControl, Theme.hoverColor);
         DrawUtil.drawPixelLine(var0, var1, var2 + 4, Math.max(0, var4 - 8), true, Theme.accentGlow);
      }
   }

   public static void drawFocusRing(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (Theme.isThemed()) {
         RoundedShapes.drawRim(
            var0, var1 - 1, var2 - 1, var3 + 2, var4 + 2, var5 + 1, ColorUtil.withAlpha(Theme.readableAccent(var1, Theme.cardBackground), 0.85)
         );
      } else {
         RoundedShapes.drawRimGradient(
            var0,
            var1 - 1,
            var2 - 1,
            var3 + 2,
            var4 + 2,
            var5 + 1,
            ColorUtil.multiplyAlpha(Theme.textPrimary, 0.55),
            ColorUtil.multiplyAlpha(Theme.textPrimary, 0.3)
         );
      }
   }

   public static void drawChip(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5, boolean var7) {
      drawPanel(
         var0,
         var1,
         var2,
         var3,
         var4,
         Theme.radiusMedium,
         ColorUtil.lerpColor(Theme.cardBackground, Theme.cardHoverBackground, var7 ? 1.0 : var5),
         0.6 + var5 * 0.4
      );
   }

   public static void drawToggleState(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      drawPulsingGlow(var0, var1, var2, var3 ? 1.0 : 0.0);
   }

   public static void drawPulsingGlow(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      drawToggle(var0, var1, var2, var3, Theme.accentAt(var1), ThemePalette.controlInk(Theme.background) == -15132391);
   }

   public static void drawToggle(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, boolean var6) {
      double var7 = Math.clamp(var3, 0.0, 1.0);
      int var9 = ThemePalette.readableAccent(var5, Theme.cardBackground | 0xFF000000);
      int var10 = ThemePalette.controlInk(var9);
      if (Theme.isThemed()) {
         var10 = ThemePalette.contrast(-1, var9) >= 2.2 ? -1 : -15132388;
         int var17 = var6 ? -2894888 : ColorUtil.lerpColor(Theme.inputBackground | 0xFF000000, Theme.textPrimary, 0.12);
         int var18 = var6 ? -1 : -3552816;
         switch (Theme.getLook().switches()) {
            case "Square":
               drawSquareToggle(var0, var1, var2, var7, var9, var17, ColorUtil.lerpColor(var18, var10, var7), var6);
               return;
            case "Minimal":
               drawMinimalToggle(var0, var1, var2, var7, var9, var17, var6);
               return;
            default:
               if (var7 > 0.01) {
                  fillRoundedRect(var0, var1, var2, 24.0F, 13.0F, 6.5F, 5.0F, ColorUtil.withAlpha(var9, 0.35 * var7));
               }

               RoundedShapes.fillRounded(var0, var1, var2, 24.0F, 13.0F, 6.5F, ColorUtil.lerpColor(var17, var9, var7));
               if (var7 > 0.01) {
                  RoundedShapes.fillRoundedGradientHorizontal(
                     var0, var1, var2, 24.0F, 13.0F, 6.5F, ColorUtil.withAlpha(Theme.accentEnd, 0.0), ColorUtil.withAlpha(Theme.accentEnd, 0.55 * var7)
                  );
               }

               if (var7 < 0.999) {
                  RoundedShapes.drawRimGradient(
                     var0,
                     var1,
                     var2,
                     24.0F,
                     13.0F,
                     6.5F,
                     ColorUtil.multiplyAlpha(Theme.accentGlow, 1.0 - var7),
                     ColorUtil.multiplyAlpha(Theme.accentSoft, 1.0 - var7)
                  );
               }

               float var19 = (float)(3.15 * (1.0 - Math.abs(var7 * 2.0 - 1.0)));
               float var20 = 9.0F + var19;
               float var15 = var1 + 2 + (float)(11.0 * var7) - var19 * (float)var7;
               RoundedShapes.drawGlow(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 3.0F, 1207959552, 1509949440);
               RoundedShapes.fillRounded(var0, var15, var2 + 2, var20, 9.0F, 4.5F, ColorUtil.lerpColor(var18, var10, var7));
               if (var6) {
                  RoundedShapes.drawRimGradient(var0, var15, var2 + 2, var20, 9.0F, 4.5F, 469762048, 704643072);
               }
         }
      } else {
         int var11 = ColorUtil.lerpColor(var6 ? -9143678 : -12961217, var9, var7);
         drawPanel(var0, var1 + 1, var2 + 1, 22, 12, 6, var11, 0.35);
         int var12 = var1 + 3 + (int)Math.round(var7 * 10.0);
         fillRoundedRect(var0, var12, var2 + 3, 8, 8, 4, 939524096);
         fillRoundedRect(var0, var12, var2 + 3, 8, 8, 4, ColorUtil.lerpColor(-1776408, var10, var7));
      }
   }

   private static void drawSquareToggle(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, int var7, boolean var8) {
      if (var3 > 0.01) {
         fillRoundedRect(var0, var1, var2, 24.0F, 13.0F, 2.5F, 5.0F, ColorUtil.withAlpha(var5, 0.3 * var3));
      }

      RoundedShapes.fillRounded(var0, var1, var2, 24.0F, 13.0F, 2.5F, ColorUtil.lerpColor(var6, var5, var3));
      if (var3 < 0.999) {
         RoundedShapes.drawRimGradient(
            var0, var1, var2, 24.0F, 13.0F, 2.5F, ColorUtil.multiplyAlpha(Theme.accentGlow, 1.0 - var3), ColorUtil.multiplyAlpha(Theme.accentSoft, 1.0 - var3)
         );
      }

      float var9 = var1 + 2 + (float)(11.0 * var3);
      RoundedShapes.drawGlow(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 2.5F, 1073741824, 1342177280);
      RoundedShapes.fillRounded(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, var7);
      if (var8) {
         RoundedShapes.drawRimGradient(var0, var9, var2 + 2, 9.0F, 9.0F, 1.5F, 469762048, 704643072);
      }
   }

   private static void drawMinimalToggle(GuiGraphicsExtractor var0, int var1, int var2, double var3, int var5, int var6, boolean var7) {
      RoundedShapes.fillRounded(var0, var1 + 1, var2 + 4, 22.0F, 5.0F, 2.5F, ColorUtil.lerpColor(var6, ColorUtil.withAlpha(var5, 0.55), var3));
      float var8 = 5.5F;
      float var9 = var1 + 1 + var8 + (float)((22.0F - 2.0F * var8) * var3);
      RoundedShapes.drawGlow(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, 2.5F, 1073741824, 1342177280);
      int var10 = ColorUtil.lerpColor(var7 ? -1 : -1776408, var5 | 0xFF000000, var3);
      RoundedShapes.fillRounded(var0, var9 - var8, var2 + 1, 2.0F * var8, 2.0F * var8, var8, var10);
      float var11 = var8 * 0.42F;
      RoundedShapes.fillRounded(
         var0,
         var9 - var11,
         var2 + 1 + var8 - var11,
         2.0F * var11,
         2.0F * var11,
         var11,
         ColorUtil.lerpColor(ColorUtil.withAlpha(Theme.background, 0.6), -1, var3)
      );
   }

   public static void drawScrollbar(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (var4 > var3) {
         int var6 = Math.min(var3, Math.max(18, var3 * var3 / var4));
         int var7 = (int)((double)(var3 - var6) * var5 / (var4 - var3));
         fillRoundedRect(var0, var1, var2 + var7, 2, var6, 1, Theme.isThemed() ? ColorUtil.withAlpha(Theme.textSecondary, 0.4) : -2139654281);
      }
   }

   public static void drawWindowBorder(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, double var5) {
      drawPanel(var0, var1, var2, var3, var4, Theme.radiusLarge, ColorUtil.multiplyAlpha(Theme.panelBackground, var5), var5 * 0.7);
   }

   public static void drawInsetBorder(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      drawBorder(var0, var1, var2, var3, var4, Theme.radiusControl, var5);
   }

   public static void drawBorder(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6) {
      RoundedShapes.drawRimGradient(var0, var1, var2, var3, var4, var5, var6, ColorUtil.multiplyAlpha(var6, 0.28));
   }

   public static void drawShadow(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      drawShadowWithRadius(var0, var1, var2, var3, var4, Theme.radiusPill);
   }

   public static void drawShadowWithRadius(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5) {
      if (Theme.isThemed()) {
         RoundedShapes.drawGlow(var0, var1, var2, var3, var4, var5, 30.0F, ColorUtil.multiplyAlpha(Theme.shadowColor, 0.25), Theme.shadowColor);
         RoundedShapes.drawGlow(
            var0, var1, var2, var3, var4, var5, 6.0F, ColorUtil.multiplyAlpha(Theme.shadowColor, 0.45), ColorUtil.multiplyAlpha(Theme.shadowColor, 0.7)
         );
      } else {
         for (byte var6 = 8; var6 >= 2; var6 -= 2) {
            fillRoundedRect(var0, var1 - var6, var2 - var6 + 5, var3 + var6 * 2, var4 + var6 * 2, Theme.radiusPill + var6, 150994944);
         }
      }
   }
}

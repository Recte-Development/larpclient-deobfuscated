package com.larpclient.gui.theme;

import com.larpclient.config.ThemePalette;
import com.larpclient.util.render.ColorUtil;

public final class Theme {
   public static final int dEFAULT_ACCENT = -1381654;
   public static final int dEFAULT_ACCENT_END = -6250336;
   public static int accent = -1381654;
   public static int accentEnd = -6250336;
   public static int panelBackground = -434562791;
   public static int cardBackground = -920509918;
   public static int cardHoverBackground = -534173399;
   public static int textPrimary = -1381654;
   public static int textSecondary = -6250336;
   public static int controlBackground = -13882324;
   public static int inputBackground = -1341058799;
   public static int textMuted = -8947849;
   public static int rowBackground = -921036262;
   public static int headerBackground = -98426334;
   public static int background = -15132391;
   public static final int tEXT_BRIGHT = -1381654;
   public static final int cOLOR_INFO = -7873104;
   public static final int cOLOR_WARNING = -996986;
   public static final int cOLOR_ERROR = -24915;
   public static int radiusSmall = 2;
   public static int radiusMedium = 3;
   public static int radiusLarge = 3;
   public static int radiusControl = 5;
   public static int radiusPill = 7;
   public static int radiusCard = 5;
   public static int overlayTint = 0;
   public static int shadowColor = 0;
   public static final int mIN_WIDTH = 100;
   public static final int sIDEBAR_WIDTH = 160;
   public static final int pANEL_WIDTH = 180;
   public static final int sLIDER_WIDTH = 150;
   public static final int cOLUMN_WIDTH = 160;
   public static final int lIST_WIDTH = 160;
   public static final int dIALOG_WIDTH = 200;
   public static final int wINDOW_WIDTH = 280;
   public static final int wINDOW_HEIGHT = 270;
   public static final int pADDING = 4;
   public static final int rOW_HEIGHT = 26;
   public static final int hEADER_HEIGHT = 38;
   public static int borderColor = -533976020;
   public static int windowBackground = -233038818;
   public static int hoverColor = 1075978786;
   public static int disabledColor = -8947844;
   public static int accentGlow = 569043690;
   public static int accentSoft = 166390506;
   public static final int sPACING_SMALL = 8;
   public static final int sPACING_LARGE = 12;
   public static final int mARGIN = 8;
   public static final int tEXT_SIZE = 11;
   public static final int bUTTON_HEIGHT = 22;
   public static final int tOGGLE_HEIGHT = 22;
   public static final int iNPUT_HEIGHT = 20;
   public static final int tAB_HEIGHT = 44;
   public static final int iCON_SIZE = 44;
   private static boolean animatedAccent;
   private static boolean themed;
   private static long themeTimeMillis;
   private static double opacity = 1.0;
   private static Theme.Look look = Theme.Look.DEFAULT;

   public static Theme.ThemeScope applyTheme(boolean var0, double var1) {
      return applyThemeWithAnimation(var0, var1, false);
   }

   public static int backgroundColor() {
      return ColorUtil.multiplyAlpha(background, opacity);
   }

   public static boolean isThemed() {
      return themed;
   }

   public static int readableAccent(int var0, int var1) {
      return ThemePalette.readableAccent(accentAt(var0), var1 | 0xFF000000);
   }

   public static int accentAt(int var0) {
      return animatedAccent ? scaleRounding(0.5 + 0.5 * Math.sin(themeTimeMillis / 800.0 + var0 * 0.008)) : accent;
   }

   public static Theme.Look getLook() {
      return look;
   }

   public static Theme.ThemeScope applyThemeWithAnimation(boolean var0, double var1, boolean var3) {
      return applyThemeWithLook(var0, var1, var3, Theme.Look.DEFAULT);
   }

   public static Theme.ThemeScope applyThemeWithLook(boolean var0, double var1, boolean var3, Theme.Look var4) {
      int[] var5 = accentSaturation();
      boolean var6 = animatedAccent;
      boolean var7 = themed;
      long var8 = themeTimeMillis;
      double var10 = opacity;
      Theme.Look var12 = look;
      look = var4;
      animatedAccent = var3;
      themed = (boolean)1;
      themeTimeMillis = System.currentTimeMillis();
      opacity = var1;
      if (var0) {
         panelBackground = -592136;
         cardBackground = -1;
         cardHoverBackground = -921100;
         textPrimary = -15263974;
         textSecondary = -10592664;
         controlBackground = -2302750;
         inputBackground = -1447443;
         textMuted = -7697772;
         rowBackground = -1;
         headerBackground = -1;
         background = -1;
         borderColor = -1052685;
         windowBackground = -1184272;
         hoverColor = -855307;
         disabledColor = -6710880;
         accentGlow = 385875968;
         accentSoft = 520093696;
         overlayTint = -1250065;
         shadowColor = 706485284;
      } else {
         int var13 = tintTowardAccent(-16119283, 0.09);
         int var14 = tintTowardAccent(-15263973, 0.11);
         int var15 = tintTowardAccent(-14079698, 0.2);
         textPrimary = -1184272;
         textSecondary = -6645085;
         textMuted = -9934735;
         panelBackground = var14;
         controlBackground = var15;
         cardBackground = ColorUtil.lerpColor(var14, ColorUtil.lerpColor(var13, textPrimary, 0.035), 0.62);
         cardHoverBackground = ColorUtil.lerpColor(var14, ColorUtil.lerpColor(var13, textPrimary, 0.06), 0.62);
         inputBackground = var13;
         rowBackground = ColorUtil.lerpColor(var13, textPrimary, 0.04);
         headerBackground = ColorUtil.lerpColor(var14, textPrimary, 0.025);
         background = var14;
         borderColor = ColorUtil.lerpColor(var14, textPrimary, 0.05);
         windowBackground = var13;
         hoverColor = ColorUtil.withAlpha(var14, 0.25);
         disabledColor = -6908257;
         accentGlow = ColorUtil.withAlpha(ColorUtil.lerpColor(var15, textPrimary, 0.12), 0.75);
         accentSoft = ColorUtil.withAlpha(var15, 0.6);
         overlayTint = ColorUtil.withAlpha(var13, 0.45);
         shadowColor = -1946157056;
      }

      overlayTint = ColorUtil.multiplyAlpha(overlayTint, 0.5 + 0.3 * Math.clamp(var1, 0.0, 1.0));
      panelBackground = ColorUtil.multiplyAlpha(panelBackground, var1);
      windowBackground = ColorUtil.multiplyAlpha(windowBackground, var1);
      double var16 = Math.clamp(var4.rounding(), 0.0, 2.0);
      radiusSmall = scaleRounding(4, var16);
      radiusMedium = scaleRounding(6, var16);
      radiusLarge = scaleRounding(8, var16);
      radiusControl = scaleRounding(7, var16);
      radiusPill = scaleRounding(16, var16);
      radiusCard = scaleRounding(10, var16);
      return new Theme.ThemeScope(var5, var6, var7, var8, var10, var12);
   }

   public static double accentSaturation() {
      int var0 = accent >>> 16 & 0xFF;
      int var1 = accent >>> 8 & 0xFF;
      int var2 = accent & 0xFF;
      int var3 = Math.max(var0, Math.max(var1, var2));
      int var4 = Math.min(var0, Math.min(var1, var2));
      return 0.4 + 0.6 * (var3 == 0 ? 0.0 : (double)(var3 - var4) / var3);
   }

   static int tintTowardAccent(int var0, double var1) {
      int var3 = accent >>> 16 & 0xFF;
      int var4 = accent >>> 8 & 0xFF;
      int var5 = accent & 0xFF;
      double var6 = 0.2126 * var3 + 0.7152 * var4 + 0.0722 * var5;
      int[] var8 = new int[]{var0 >>> 16 & 0xFF, var0 >>> 8 & 0xFF, var0 & 0xFF};
      int[] var9 = new int[]{var3, var4, var5};
      int var10 = var0 & 0xFF000000;

      for (int var11 = 0; var11 < 3; var11++) {
         var10 |= Math.clamp(Math.round(var8[var11] + (var9[var11] - var6) * var1), 0, 255) << 16 - 8 * var11;
      }

      return var10;
   }

   private static int[] accentSaturation() {
      return new int[]{
         panelBackground,
         cardBackground,
         cardHoverBackground,
         textPrimary,
         textSecondary,
         controlBackground,
         inputBackground,
         textMuted,
         rowBackground,
         headerBackground,
         background,
         borderColor,
         windowBackground,
         hoverColor,
         disabledColor,
         accentGlow,
         accentSoft,
         overlayTint,
         shadowColor,
         radiusSmall,
         radiusMedium,
         radiusLarge,
         radiusControl,
         radiusPill,
         radiusCard
      };
   }

   private static int scaleRounding(int var0, double var1) {
      return (int)Math.round(var0 * var1);
   }

   public static double getOpacity() {
      return opacity;
   }

   public static int glowColor(int var0, double var1) {
      return ColorUtil.withAlpha(accentAt(var0), Math.clamp(var1, 0.0, 1.0) * 0.2);
   }

   public static int scaleRounding(double var0) {
      return ColorUtil.lerpColor(accent, accentEnd, var0);
   }

   public record Look(String card, String enabled, String switches, String slider, double rounding, boolean accentGlow, boolean ripple, boolean hoverLift) {
      public static final Theme.Look DEFAULT = new Theme.Look("Filled", "Glow", "Pill", "Pill", 1.0, true, true, true);
   }

   public record ThemeScope(int[] previous, boolean animatedAccent, boolean style, long millis, double opacity, Theme.Look look) implements AutoCloseable {
      @Override
      public void close() {
         Theme.panelBackground = this.previous[0];
         Theme.cardBackground = this.previous[1];
         Theme.cardHoverBackground = this.previous[2];
         Theme.textPrimary = this.previous[3];
         Theme.textSecondary = this.previous[4];
         Theme.controlBackground = this.previous[5];
         Theme.inputBackground = this.previous[6];
         Theme.textMuted = this.previous[7];
         Theme.rowBackground = this.previous[8];
         Theme.headerBackground = this.previous[9];
         Theme.background = this.previous[10];
         Theme.borderColor = this.previous[11];
         Theme.windowBackground = this.previous[12];
         Theme.hoverColor = this.previous[13];
         Theme.disabledColor = this.previous[14];
         Theme.accentGlow = this.previous[15];
         Theme.accentSoft = this.previous[16];
         Theme.overlayTint = this.previous[17];
         Theme.shadowColor = this.previous[18];
         Theme.radiusSmall = this.previous[19];
         Theme.radiusMedium = this.previous[20];
         Theme.radiusLarge = this.previous[21];
         Theme.radiusControl = this.previous[22];
         Theme.radiusPill = this.previous[23];
         Theme.radiusCard = this.previous[24];
         animatedAccent = this.animatedAccent;
         Theme.themed = this.style;
         Theme.themeTimeMillis = this.millis;
         opacity = this.opacity;
         look = this.look;
      }
   }
}

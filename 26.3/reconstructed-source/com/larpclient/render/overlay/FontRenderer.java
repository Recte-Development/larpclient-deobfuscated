package com.larpclient.render.overlay;

import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.TextMeasureCache;
import java.util.Arrays;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.FontDescription.Resource;
import net.minecraft.resources.Identifier;

public final class FontRenderer {
   private static int version;
   private static final TextMeasureCache widthCache = new TextMeasureCache();
   private static final Style[] faceStyles = Arrays.stream(FontRenderer.Face.values())
      .map(var0 -> Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", var0.name().toLowerCase(Locale.ROOT)))))
      .toArray(Style[]::new);
   private static final Style worldStyle = Style.EMPTY.withFont(new Resource(Identifier.fromNamespaceAndPath("larp-client", "world_semibold")));
   private static int forceClickUiCount;

   public static int getVersion() {
      return version;
   }

   public static void invalidateCache() {
      widthCache.invalidateIfChanged(null, -1);
      version++;
   }

   public static int measureWidth(String var0, FontRenderer.TextStyle var1) {
      return isClickUiFont() ? measureRawWidth(var0, var1.CLICKFACE) : (int)Math.ceil(getTextWidth(var0, var1.FACE) * var1.SCALE);
   }

   public static String trimToWidth(String var0, int var1, FontRenderer.TextStyle var2) {
      return isClickUiFont() ? truncateToWidth(var0, var1, var2.CLICKFACE) : drawText(var0, Math.max(0, (int)Math.floor(var1 / var2.SCALE)), var2.FACE);
   }

   public static void drawText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, FontRenderer.TextStyle var5) {
      if (isClickUiFont()) {
         drawTextRaw(var0, var1, var2, var3, var4, var5.CLICKFACE);
      } else if (var5.SCALE == 1.0F) {
         drawTextWithFace(var0, var1, var2, var3, var4, var5.FACE);
      } else {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var2, var3);
            var0.pose().scale(var5.SCALE, var5.SCALE);
            drawTextWithFace(var0, var1, 0, 0, var4, var5.FACE);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public static Component worldComponent(String var0) {
      return Component.literal(var0).setStyle(worldStyle);
   }

   public static FontRenderer.ClickUiScope forceClickUiFont() {
      forceClickUiCount++;
      return () -> forceClickUiCount--;
   }

   private static boolean isClickUiFont() {
      if (forceClickUiCount > 0) {
         return true;
      }

      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui != null && var0.gui.screen() instanceof ClickGuiScreen;
   }

   private static FontRenderer.Face isClickUiFont(FontRenderer.Face var0) {
      if (!isClickUiFont()) {
         return var0;
      }

      return switch (var0) {
         case REGULAR -> FontRenderer.Face.CLICK_MEDIUM;
         case MEDIUM -> FontRenderer.Face.CLICK_SEMIBOLD;
         case SEMIBOLD -> FontRenderer.Face.CLICK_SEMIBOLD;
         default -> var0;
      };
   }

   public static int drawDefaultText(String var0) {
      return getTextWidth(var0, FontRenderer.Face.REGULAR);
   }

   public static int getTextWidth(String var0, FontRenderer.Face var1) {
      return measureRawWidth(var0, isClickUiFont(var1));
   }

   public static void drawText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      drawTextWithFace(var0, var1, var2, var3, var4, FontRenderer.Face.REGULAR);
   }

   public static void drawTextWithFace(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, FontRenderer.Face var5) {
      drawTextRaw(var0, var1, var2, var3, var4, isClickUiFont(var5));
   }

   public static String getTextWidth(String var0, int var1) {
      return drawText(var0, var1, FontRenderer.Face.REGULAR);
   }

   public static String drawText(String var0, int var1, FontRenderer.Face var2) {
      return truncateToWidth(var0, var1, isClickUiFont(var2));
   }

   public static Component toComponent(String var0, FontRenderer.Face var1) {
      return Component.literal(var0).setStyle(faceStyles[var1.ordinal()]);
   }

   public static int getDefaultWidth(String var0) {
      return measureRawWidth(var0, FontRenderer.Face.REGULAR);
   }

   public static int measureRawWidth(String var0, FontRenderer.Face var1) {
      Font var2 = Minecraft.getInstance().font;
      widthCache.invalidateIfChanged(var2, PostProcessing.reloadCount);
      return widthCache.getWidth(
         var0, var1.ordinal(), () -> ExactTextRenderer.canRender(var1, var0) ? ExactTextRenderer.getWidthCeil(var0, var1) : var2.width(toComponent(var0, var1))
      );
   }

   public static void drawDefaultText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      drawTextRaw(var0, var1, var2, var3, var4, FontRenderer.Face.REGULAR);
   }

   public static void drawTextRaw(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, FontRenderer.Face var5) {
      var4 = AlphaScope.applyAlpha(var4);
      if (var4 >>> 24 >= 4) {
         if (!ExactTextRenderer.drawText(var0, var1, var2, var3, var4, var5)) {
            var0.text(Minecraft.getInstance().font, toComponent(var1, var5), var2, var3, var4, false);
         }
      }
   }

   public static void drawOutlinedText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5, float var6, FontRenderer.Face var7) {
      var4 = AlphaScope.applyAlpha(var4);
      var5 = AlphaScope.applyAlpha(var5);
      if (var4 >>> 24 >= 4) {
         if (!ExactTextRenderer.drawText(var0, var1, var2, var3, var4, var7, var5, var6)) {
            if (var5 >>> 24 >= 4 && var6 > 0.0F) {
               for (int var8 = 0; var8 < 8; var8++) {
                  double var9 = var8 * Math.PI / 4.0;
                  var0.pose().pushMatrix();

                  try {
                     var0.pose().translate((float)(Math.cos(var9) * var6), (float)(Math.sin(var9) * var6));
                     var0.text(Minecraft.getInstance().font, toComponent(var1, var7), var2, var3, var5, false);
                  } finally {
                     var0.pose().popMatrix();
                  }
               }
            }

            var0.text(Minecraft.getInstance().font, toComponent(var1, var7), var2, var3, var4, false);
         }
      }
   }

   public static void drawSpacedText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, float var5, FontRenderer.Face var6) {
      var4 = AlphaScope.applyAlpha(var4);
      if (var4 >>> 24 >= 2) {
         ExactTextRenderer.drawGlow(var0, var1, var2, var3, var4, var6, var5);
      }
   }

   public static float forceClickUiFont(String var0, FontRenderer.Face var1) {
      return ExactTextRenderer.canRender(var1, var0)
         ? ExactTextRenderer.getWidth(var0, var1)
         : Minecraft.getInstance().font.getSplitter().stringWidth(toComponent(var0, var1));
   }

   public static void drawCenteredText(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      drawDefaultText(var0, var1, var2 - getDefaultWidth(var1) / 2, var3, var4);
   }

   public static String drawTextWithFace(String var0, int var1) {
      return truncateToWidth(var0, var1, FontRenderer.Face.REGULAR);
   }

   public static String truncateToWidth(String var0, int var1, FontRenderer.Face var2) {
      widthCache.invalidateIfChanged(Minecraft.getInstance().font, PostProcessing.reloadCount);
      return widthCache.getString(var0, var2.ordinal(), var1, () -> {
         if (measureRawWidth(var0, var2) <= var1) {
            return var0;
         }

         int var3 = var0.length();

         while (var3 > 0 && measureRawWidth(var0.substring(0, var3) + "…", var2) > var1) {
            var3 = var0.offsetByCodePoints(var3, -1);
         }

         return var3 == 0 ? "" : var0.substring(0, var3) + "…";
      });
   }

   static {
      LarpLog.info(
         "Font",
         "Inter UI/HUD and ClickUI faces registered at native pixel sizes per role; ClickUI text is rasterized at the on-screen pixel size, other text by Minecraft"
      );
   }

   public interface ClickUiScope extends AutoCloseable {
      @Override
      void close();
   }

   public enum Face {
      REGULAR,
      MEDIUM,
      SEMIBOLD,
      CLOCK,
      HUD,
      CLICK_REGULAR,
      CLICK_MEDIUM,
      CLICK_SEMIBOLD,
      CLICK_BRAND,
      CLICK_TITLE,
      CLICK_MODULE,
      CLICK_BODY,
      CLICK_NAVIGATION,
      CLICK_SECTION,
      CLICK_HELP,
      CLICK_EYEBROW;
   }

   public enum TextStyle {
      BRAND(FontRenderer.Face.SEMIBOLD, 1.2F, FontRenderer.Face.CLICK_BRAND),
      TITLE(FontRenderer.Face.SEMIBOLD, 1.15F, FontRenderer.Face.CLICK_TITLE),
      MODULE(FontRenderer.Face.MEDIUM, 1.0F, FontRenderer.Face.CLICK_MODULE),
      BODY(FontRenderer.Face.REGULAR, 1.0F, FontRenderer.Face.CLICK_BODY),
      NAVIGATION(FontRenderer.Face.MEDIUM, 0.9F, FontRenderer.Face.CLICK_NAVIGATION),
      SECTION(FontRenderer.Face.MEDIUM, 0.9F, FontRenderer.Face.CLICK_SECTION),
      HELP(FontRenderer.Face.REGULAR, 0.85F, FontRenderer.Face.CLICK_HELP),
      EYEBROW(FontRenderer.Face.SEMIBOLD, 0.8F, FontRenderer.Face.CLICK_EYEBROW);

      public final FontRenderer.Face FACE;
      public final float SCALE;
      final FontRenderer.Face CLICKFACE;

      TextStyle(FontRenderer.Face var3, float var4, FontRenderer.Face var5) {
         this.FACE = var3;
         this.SCALE = var4;
         this.CLICKFACE = var5;
      }

      public int lineHeight() {
         return (int)Math.ceil(9.0F * this.SCALE);
      }
   }
}

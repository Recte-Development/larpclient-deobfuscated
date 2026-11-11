package com.larpclient.gui.widget;

import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.util.render.DrawUtil;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class SectionLabel extends Widget {
   private final String title;
   private final BooleanSupplier visibleSupplier;
   private final boolean subtitle;
   private final boolean header;

   public SectionLabel(String var1) {
      this(var1, () -> true);
   }

   public SectionLabel(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public SectionLabel(String var1, BooleanSupplier var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private SectionLabel(String var1, BooleanSupplier var2, boolean var3, boolean var4) {
      this.title = var1;
      this.visibleSupplier = var2;
      this.subtitle = var3;
      this.header = var4;
      this.height = var3 ? 16 : (var4 ? 26 : 22);
      this.enabled = (boolean)0;
   }

   public static SectionLabel header(String var0) {
      return new SectionLabel(var0, () -> true, false, true);
   }

   public boolean isVisible() {
      return this.visibleSupplier.getAsBoolean();
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.visible) {
         if (this.header) {
            FontRenderer.TextStyle var9 = FontRenderer.TextStyle.NAVIGATION;
            String var10 = FontRenderer.trimToWidth(this.title.toUpperCase(Locale.ROOT), this.width - 48, var9);
            int var11 = FontRenderer.measureWidth(var10, var9);
            int var12 = this.x + (this.width - var11) / 2;
            int var8 = this.y + 11;
            FontRenderer.drawText(var1, var10, var12, var8 - var9.lineHeight() / 2, Theme.textMuted, var9);
            DrawUtil.drawPixelLine(var1, this.x + 4, var8, var12 - 8 - (this.x + 4), false, Theme.accentGlow);
            DrawUtil.drawPixelLine(var1, var12 + var11 + 8, var8, this.x + this.width - 4 - (var12 + var11 + 8), false, Theme.accentGlow);
         } else if (this.subtitle) {
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(this.title, this.width, FontRenderer.TextStyle.SECTION),
               this.x,
               this.y + 3,
               Theme.textPrimary,
               FontRenderer.TextStyle.SECTION
            );
         } else {
            FontRenderer.TextStyle var4 = FontRenderer.TextStyle.EYEBROW;
            String var5 = FontRenderer.trimToWidth(this.title.toUpperCase(Locale.ROOT), this.width - 8, var4);
            int var6 = this.y + this.height - var4.lineHeight() - 2;
            FontRenderer.drawText(var1, var5, this.x + 4, var6, Theme.textMuted, var4);
            if (Theme.isThemed()) {
               int var7 = this.x + 4 + FontRenderer.measureWidth(var5, var4) + 6;
               DrawUtil.drawPixelLine(var1, var7, var6 + var4.lineHeight() / 2, this.x + this.width - 4 - var7, false, Theme.accentGlow);
            }
         }
      }
   }
}

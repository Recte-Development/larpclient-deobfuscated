package com.larpclient.hud;

import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.social.misc.TimedLyrics;
import com.larpclient.util.misc.MediaSession;
import com.larpclient.util.misc.SpotifyLyricsService;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

public final class IslandLyricsHud {
   public static final int wIDTH = 300;
   public static final int hEIGHT = 20;
   static final int iDLE_WIDTH = 44;
   static final int fONT_SIZE = 10;
   static final int lOGO_SIZE = 13;
   static final long sWAP_NANOS = 380000000L;
   static final long tEXT_NANOS = 520000000L;
   static final double sPIN_SPEED = 1.6;
   private static final int lYRIC_COLOR = -1250068;
   private static final int sUBTITLE_COLOR = -4671294;
   private static final int bACKGROUND_COLOR = 805306368;
   private static final Identifier lOGO = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/logo.png");
   private static final IslandLyricsHud.Line pREVIEW_LINE = new IslandLyricsHud.Line(
      IslandLyricsHud.LineKind.LYRIC, "Island Lyrics preview line", "sample", true, 0.0
   );
   private final IslandLyricsHud.Spring widthSpring = new IslandLyricsHud.Spring(44.0F, 170.0F, 19.0F);
   private final IslandLyricsHud.Spring heightSpring = new IslandLyricsHud.Spring(14.0F, 170.0F, 19.0F);
   private final IslandLyricsHud.Spring scrollSpring = new IslandLyricsHud.Spring(0.0F, 90.0F, 18.0F);
   private final IslandLyricsHud.Spring visibilitySpring = new IslandLyricsHud.Spring(0.0F, 120.0F, 20.0F);
   private IslandLyricsHud.Line current = IslandLyricsHud.Line.IDLE;
   private IslandLyricsHud.Line previous = IslandLyricsHud.Line.IDLE;
   private long changedAt;
   private long lastFrameNanos;
   private double logoAngle;

   static IslandLyricsHud.Line buildLine(SpotifyLyricsService.PlaybackSnapshot var0, long var1) {
      MediaSession var3 = var0 == null ? null : var0.playback();
      if (var3 != null && var1 - var3.observedAt() <= 4000000000L) {
         String var4 = var3.key();
         TimedLyrics var5 = var0.lyrics() == null ? null : var0.lyrics().timed();
         double var6 = var3.positionAt(var1);
         if (var5 != null && var3.playing()) {
            int var8 = var5.findCueIndex(var6);
            String var9 = var5.textAt(var8, var6);
            if (!var9.isBlank()) {
               double var10 = var5.getCues().get(var8).seconds();
               double var12 = var8 + 1 < var5.getCues().size() ? var5.getCues().get(var8 + 1).seconds() : var3.duration();
               double var14 = Math.clamp((var6 - var10) / Math.clamp(var12 - var10, 0.2, 18.0), 0.0, 1.0);
               return new IslandLyricsHud.Line(IslandLyricsHud.LineKind.LYRIC, var9, "l" + var4 + "\u001f" + var8, true, var14);
            }
         }

         String var16 = var3.artist().isBlank() ? var3.title() : var3.title() + "  ·  " + var3.artist();
         return new IslandLyricsHud.Line(IslandLyricsHud.LineKind.SONG, var16, "s" + var4, var3.playing(), 0.0);
      } else {
         return IslandLyricsHud.Line.IDLE;
      }
   }

   public boolean isVisible(SpotifyLyricsService.PlaybackSnapshot var1, long var2) {
      return buildLine(var1, var2).kind != IslandLyricsHud.LineKind.IDLE || this.visibilitySpring.position >= 0.02F;
   }

   public void reset() {
      this.current = this.previous = IslandLyricsHud.Line.IDLE;
      this.widthSpring.snapTo(44.0F);
      this.heightSpring.snapTo(14.0F);
      this.scrollSpring.snapTo(0.0F);
      this.visibilitySpring.snapTo(0.0F);
      this.changedAt = this.lastFrameNanos = 0L;
      this.logoAngle = 0.0;
   }

   public void render(GuiGraphicsExtractor var1, int var2, int var3, int var4, SpotifyLyricsService.PlaybackSnapshot var5, long var6) {
      float var8 = this.lastFrameNanos == 0L ? 0.0F : Math.min(0.05F, (float)(var6 - this.lastFrameNanos) / 1.0E9F);
      this.lastFrameNanos = var6;
      IslandLyricsHud.Line var9 = buildLine(var5, var6);
      if (!var9.key.equals(this.current.key)) {
         this.previous = this.current;
         this.current = var9;
         this.changedAt = var6;
         if (var9.kind == IslandLyricsHud.LineKind.LYRIC) {
            this.scrollSpring.snapTo(0.0F);
         }
      } else {
         this.current = var9;
      }

      if (this.current.playing) {
         this.logoAngle = (this.logoAngle + var8 * 1.6 * Math.PI * 2.0) % (Math.PI * 2);
      }

      float var10 = this.current.kind == IslandLyricsHud.LineKind.IDLE ? 44.0F : targetWidth(this.current, var4);
      float var11 = this.visibilitySpring.step(this.current.kind == IslandLyricsHud.LineKind.IDLE ? 0.0F : 1.0F, var8);
      float var12 = this.widthSpring.step(var10, var8);
      float var13 = this.heightSpring.step(this.current.kind == IslandLyricsHud.LineKind.IDLE ? 14.0F : 20.0F, var8);
      if (!(var11 < 0.02F)) {
         float var14 = isVisible(Math.clamp((float)(var6 - this.changedAt) / 3.8E8F, 0.0F, 1.0F));
         float var15 = 0.0F;
         if (this.current.kind == IslandLyricsHud.LineKind.LYRIC) {
            float var16 = SdfText.measureWidth(this.current.text, 10.0) - (Math.round(var12) - 50);
            var15 = this.scrollSpring.step(var16 <= 0.0F ? 0.0F : (float)Math.clamp(this.current.progress * (var16 + 30.0F) - 15.0, 0.0, var16), var8);
         }

         float var17 = Math.clamp((float)(var6 - this.changedAt) / 5.2E8F, 0.0F, 1.0F);
         this.draw(var1, var2, var3, var4, Math.max(12, Math.round(var12)), Math.max(8, Math.round(var13)), var11, var14, var17, var15, var6);
      }
   }

   public void renderPreview(GuiGraphicsExtractor var1, int var2, int var3, int var4, long var5) {
      IslandLyricsHud.Line var7 = this.current;
      this.current = pREVIEW_LINE;

      try {
         this.draw(var1, var2, var3, var4, Math.round(targetWidth(pREVIEW_LINE, var4)), 20, 1.0F, 1.0F, 1.0F, 0.0F, var5);
      } finally {
         this.current = var7;
      }
   }

   private static float targetWidth(IslandLyricsHud.Line var0, int var1) {
      return Math.clamp(SdfText.measureWidth(var0.text, 10.0) + 62.0F, 110.0F, Math.max(110, var1));
   }

   private void draw(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6, float var7, float var8, float var9, float var10, long var11) {
      int var13 = var2 + (var4 - var5) / 2;
      int var14 = var3 + (20 - var6) / 2;
      int var15 = var6 / 2;

      try (AlphaScope var16 = AlphaScope.push(Math.clamp(var7, 0.0F, 1.0F))) {
         RoundedShapes.drawGlow(var1, var13, var14, var5, var6, var15, 3.0F, 805306368, 805306368);
         RoundedShapes.fillRounded(var1, var13, var14, var5, var6, var15, HudRenderer2.panelColor());
         var1.enableScissor(var13 + 2, var14, var13 + var5 - 2, var14 + var6);

         try {
            if (var8 < 1.0F && this.previous.kind != IslandLyricsHud.LineKind.IDLE) {
               try (AlphaScope var17 = AlphaScope.push((1.0F - var8) * 0.5)) {
                  int var18 = Math.round(var8 * 3.0F);
                  this.drawLine(var1, this.previous, var13 - var18, var14, var5, var6, var11, 0.0F, 1.0F);
                  this.drawLine(var1, this.previous, var13 + var18, var14, var5, var6, var11, 0.0F, 1.0F);
               }
            }

            try (AlphaScope var35 = AlphaScope.push(var8)) {
               if (this.current.kind == IslandLyricsHud.LineKind.IDLE) {
                  double var36 = 0.5 + 0.5 * Math.sin(var11 / 9.0E8);
                  render(var1, var13 + var5 / 2.0F, var14 + var6 / 2.0F, 2.2F, ColorUtil.multiplyAlpha(Theme.accent, 0.55 + 0.35 * var36));
               } else {
                  this.drawLine(var1, this.current, var13, var14, var5, var6, var11, var10, var9);
               }
            }
         } finally {
            var1.disableScissor();
         }
      }
   }

   private void drawLine(GuiGraphicsExtractor var1, IslandLyricsHud.Line var2, int var3, int var4, int var5, int var6, long var7, float var9, float var10) {
      float var11 = var4 + var6 / 2.0F;
      this.drawLogo(var1, var3 + 12, var11);
      drawBars(var1, var3 + var5 - 22, var11, var2.playing, var7);
      int var12 = var3 + 23;
      int var13 = var5 - 50;
      double var14 = SdfText.centerBaselineY(var4, var6, 10.0);
      if (var2.kind == IslandLyricsHud.LineKind.LYRIC) {
         var1.enableScissor(var12 - 1, var4, var12 + var13 + 1, var4 + var6);

         try {
            if (var10 < 1.0F) {
               draw(var1, var2.text, var12 - var9, var14, var10);
            } else {
               HudPanels.drawText(var1, var2.text, var12 - var9, var14, 10.0, -1250068);
            }
         } finally {
            var1.disableScissor();
         }
      } else {
         HudPanels.drawText(var1, SdfText.truncate(var2.text, var13, 10.0), var12, var14, 10.0, -4671294);
      }
   }

   private static void draw(GuiGraphicsExtractor var0, String var1, double var2, double var4, float var6) {
      float var7 = Math.max(1.0F, SdfText.measureWidth(var1, 10.0));
      float var8 = 0.0F;
      int var9 = 0;

      while (var9 < var1.length()) {
         int var10 = var1.codePointAt(var9);
         String var11 = new String(Character.toChars(var10));
         float var12 = SdfText.measureWidth(var11, 10.0);
         float var13 = Math.clamp((var6 - (var8 + var12 / 2.0F) / var7 * 0.5F) / 0.5F, 0.0F, 1.0F);
         if (var13 > 0.0F && !Character.isWhitespace(var10)) {
            try (AlphaScope var14 = AlphaScope.push(var13)) {
               HudPanels.drawText(var0, var11, var2 + var8, var4 + 4.0F * (1.0F - isVisible(var13)), 10.0, -1250068);
            }
         }

         var8 += var12;
         var9 += Character.charCount(var10);
      }
   }

   private void drawLogo(GuiGraphicsExtractor var1, float var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2, var3).rotate((float)this.logoAngle);
         if (!SdfText.drawIcon(var1, "logo", -6.5, -6.5, 13.0, -1)) {
            DrawUtil.blitTexture(var1, lOGO, -6, -6, 13, 13, 64, 64);
         }
      } finally {
         var1.pose().popMatrix();
      }
   }

   private static void drawBars(GuiGraphicsExtractor var0, int var1, float var2, boolean var3, long var4) {
      double var6 = var4 / 1.0E9;

      for (int var8 = 0; var8 < 4; var8++) {
         float var9 = var3 ? (float)(3.0 + 5.0 * Math.abs(Math.sin(var6 * (2.6 + var8 * 1.1) + var8 * 1.7))) : 2.5F;
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1 + var8 * 3.5F, var2 - var9 / 2.0F);
            RoundedShapes.fillRounded(var0, 0.0F, 0.0F, 2.0F, Math.max(2, Math.round(var9)), 1.0F, ColorUtil.multiplyAlpha(Theme.accent, 0.95));
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   private static void render(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2).scale(var3 / 8.0F, var3 / 8.0F);
         RoundedShapes.fillRounded(var0, -8.0F, -8.0F, 16.0F, 16.0F, 8.0F, var4);
      } finally {
         var0.pose().popMatrix();
      }
   }

   static float isVisible(float var0) {
      float var1 = 1.0F - var0;
      return 1.0F - var1 * var1 * var1;
   }

   record Line(IslandLyricsHud.LineKind kind, String text, String key, boolean playing, double progress) {
      static final IslandLyricsHud.Line IDLE = new IslandLyricsHud.Line(IslandLyricsHud.LineKind.IDLE, "", "i", false, 0.0);
   }

   enum LineKind {
      IDLE,
      SONG,
      LYRIC;
   }

   static final class Spring {
      float position;
      float velocity;
      final float stiffness;
      final float damping;

      Spring(float var1, float var2, float var3) {
         this.position = var1;
         this.stiffness = var2;
         this.damping = var3;
      }

      float step(float var1, float var2) {
         int var3 = Math.max(1, (int)Math.ceil(var2 / 0.008F));
         float var4 = var2 / var3;

         for (int var5 = 0; var5 < var3; var5++) {
            float var6 = this.stiffness * (var1 - this.position) - this.damping * this.velocity;
            this.velocity += var6 * var4;
            this.position = this.position + this.velocity * var4;
         }

         return this.position;
      }

      void snapTo(float var1) {
         this.position = var1;
         this.velocity = 0.0F;
      }
   }
}

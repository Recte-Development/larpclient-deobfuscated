package com.larpclient.gui.widget;

import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.misc.TextFadeAnimator;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.render.GUIAnimationModule;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.misc.SdlWindowShim;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class TextField extends Widget {
   public String text = "";
   private final String placeholder;
   private final int maxLength;
   private boolean allSelected;
   private final Animation focusAnim = new Animation(0.0);
   private final TextFadeAnimator charAnimator = new TextFadeAnimator();
   private final Consumer<String> onChange;
   private Runnable onSubmit;
   private Function<String, List<String>> suggestionProvider;
   private List<String> suggestions = List.of();
   private String lastSuggestQuery;
   private int suggestionIndex;
   private boolean suggestionDismissed;
   private boolean masked;
   public String icon = "";
   public int accentColor = -1;

   public TextField masked() {
      this.masked = (boolean)1;
      return this;
   }

   public String getDisplayText() {
      return this.masked ? "*".repeat(this.text.codePointCount(0, this.text.length())) : this.text;
   }

   public TextField setSuggestionProvider(Function<String, List<String>> var1) {
      this.suggestionProvider = var1;
      this.lastSuggestQuery = null;
      return this;
   }

   public String getSuggestion() {
      if (!this.masked && this.suggestionProvider != null && !this.text.isBlank()) {
         if (!this.text.equals(this.lastSuggestQuery)) {
            this.lastSuggestQuery = this.text;
            this.suggestionIndex = 0;
            this.suggestionDismissed = (boolean)0;
            this.suggestions = this.suggestionProvider
               .apply(this.text)
               .stream()
               .filter(var1 -> var1.length() <= this.maxLength)
               .filter(var1 -> !var1.equalsIgnoreCase(this.text))
               .limit(6L)
               .toList();
         }

         return !this.suggestionDismissed && !this.suggestions.isEmpty() ? this.suggestions.get(this.suggestionIndex) : "";
      } else {
         return "";
      }
   }

   public boolean acceptSuggestion() {
      if (!this.focused) {
         return false;
      }

      String var1 = this.getSuggestion();
      if (var1.isEmpty()) {
         return false;
      }

      this.text = var1;
      this.allSelected = (boolean)0;
      this.notifyChange();
      this.suggestionDismissed = (boolean)1;
      this.lastSuggestQuery = this.text;
      return true;
   }

   private void notifyChange() {
      this.lastSuggestQuery = null;
      this.suggestionDismissed = (boolean)0;
      this.onChange.accept(this.text);
   }

   private boolean handleSuggestionKey(int var1) {
      if (var1 == 262) {
         return this.acceptSuggestion();
      }

      if ((var1 == 264 || var1 == 265 || var1 == 256) && !this.getSuggestion().isEmpty()) {
         if (var1 == 256) {
            this.suggestionDismissed = (boolean)1;
         } else {
            this.suggestionIndex = Math.floorMod(this.suggestionIndex + (var1 == 265 ? -1 : 1), this.suggestions.size());
         }

         return true;
      } else {
         return false;
      }
   }

   public void acceptSuggestion() {
      this.allSelected = (boolean)1;
   }

   public TextField onSubmit(Runnable var1) {
      this.onSubmit = var1;
      return this;
   }

   public TextField(String var1, Consumer<String> var2) {
      this(var1, var2, 64);
   }

   public TextField(String var1, Consumer<String> var2, int var3) {
      this.maxLength = var3;
      this.placeholder = var1;
      this.onChange = var2;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.focusAnim.animateTo(this.focused ? 1.0 : 0.0, 180);
      if (this.accentColor < 0) {
         GlassPainter.drawButton(var1, this.x, this.y, this.width, this.height, var4);
      } else {
         GlassPainter.drawWindowBackground(var1, this.x, this.y, this.width, this.height, this.accentColor, var4);
      }

      GUIAnimationModule var6 = GUIAnimationModule.getInstance();
      String var7 = this.getDisplayText();
      this.charAnimator.update(var7, System.nanoTime(), !this.masked && this.focused && var6 != null && var6.isChatAnimationActive());
      int var8 = this.y + (this.height - 9) / 2;
      int var9 = this.icon.isEmpty() ? 6 : 26;
      if (!this.icon.isEmpty()) {
         Icons.texture(
            var1, Icons.get(this.icon), this.x + 9, this.y + (this.height - 12) / 2, 12, ColorUtil.lerpColor(Theme.textMuted, Theme.textPrimary, var4 * 0.6)
         );
      }

      var1.enableScissor(this.x + var9, this.y + 2, this.x + this.width - 6, this.y + this.height - 2);

      try {
         if (this.text.isEmpty()) {
            FontRenderer.drawText(var1, FontRenderer.getTextWidth(this.placeholder, this.width - var9 - 6), this.x + var9, var8, Theme.textMuted);
         } else {
            int var10 = FontRenderer.drawDefaultText(var7);
            int var11 = this.x + var9 - Math.max(0, var10 + (this.focused ? FontRenderer.drawDefaultText("_") : 0) - (this.width - var9 - 6));
            if (!this.charAnimator.update()) {
               FontRenderer.drawText(var1, var7, var11, var8, Theme.textPrimary);
            } else {
               int var12 = 0;

               while (var12 < var7.length()) {
                  int var13 = var7.codePointAt(var12);
                  String var14 = new String(Character.toChars(var13));
                  int var15 = var11 + FontRenderer.drawDefaultText(var7.substring(0, var12));
                  if (var15 + FontRenderer.drawDefaultText(var14) >= this.x + var9 && var15 < this.x + this.width - 6) {
                     var1.pose().pushMatrix();
                     var1.pose().translate(0.0F, (float)this.charAnimator.offset(var12));

                     try {
                        FontRenderer.drawText(var1, var14, var15, var8, this.charAnimator.applyAlpha(Theme.textPrimary, var12));
                     } finally {
                        var1.pose().popMatrix();
                     }
                  }

                  var12 += Character.charCount(var13);
               }
            }

            if (this.focused) {
               String var24 = this.getSuggestion();
               if (!var24.isEmpty()) {
                  if (var24.regionMatches(true, 0, this.text, 0, this.text.length())) {
                     FontRenderer.drawText(var1, var24.substring(this.text.length()), var11 + var10, var8, ColorUtil.multiplyAlpha(Theme.textSecondary, 0.65));
                  } else {
                     String var25 = "Tab: " + var24;
                     int var26 = FontRenderer.measureWidth(var25, FontRenderer.TextStyle.HELP);
                     if (var10 + var26 + 18 <= this.width) {
                        FontRenderer.drawText(var1, var25, this.x + this.width - var26 - 6, var8, Theme.textSecondary, FontRenderer.TextStyle.HELP);
                     }
                  }
               }

               DrawUtil.fillRect(var1, var11 + var10, var8, 1, 9, Theme.textPrimary);
            }
         }
      } finally {
         var1.disableScissor();
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      this.allSelected = (boolean)0;
      this.focused = (boolean)(var5 == 0 && this.contains(var1, var3) ? 1 : 0);
      return this.focused;
   }

   @Override
   public boolean character(int var1) {
      if (this.focused && !Character.isISOControl(var1)) {
         if (this.allSelected) {
            this.text = "";
            this.allSelected = (boolean)0;
         }

         if (this.text.length() + Character.charCount(var1) > this.maxLength) {
            return false;
         }

         this.text = this.text + new String(Character.toChars(var1));
         this.notifyChange();
         GuiSounds.play(GuiSounds.Sound.TYPING);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (!this.focused) {
         return false;
      }

      if (this.handleSuggestionKey(var1)) {
         return true;
      }

      if ((var1 == 257 || var1 == 335) && this.onSubmit != null) {
         this.onSubmit.run();
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      long var3 = var2.getWindow().handle();
      int var5 = SdlWindowShim.getKeyState(var3, 341) != 1 && SdlWindowShim.getKeyState(var3, 345) != 1 ? 0 : 1;
      if (var5 != 0 && var1 == 86) {
         this.setSuggestionProvider(var2.keyboardHandler.getClipboard());
      } else if (var5 != 0 && var1 == 65) {
         this.allSelected = (boolean)1;
      } else if (var1 == 259 && !this.text.isEmpty()) {
         GuiSounds.play(GuiSounds.Sound.TYPING);
         if (this.allSelected) {
            this.text = "";
            this.allSelected = (boolean)0;
            this.notifyChange();
            return true;
         }

         this.text = this.text.substring(0, this.text.offsetByCodePoints(this.text.length(), -1));
         this.notifyChange();
      } else if (var1 == 256) {
         this.focused = (boolean)0;
      }

      return true;
   }

   public void setSuggestionProvider(String var1) {
      if (this.focused && var1 != null) {
         String var2 = var1.replaceAll("[\\p{Cntrl}]", " ");
         if (this.allSelected) {
            this.text = "";
            this.allSelected = (boolean)0;
         }

         int var3 = Math.min(var2.length(), Math.max(0, this.maxLength - this.text.length()));
         if (var3 > 0 && var3 < var2.length() && Character.isHighSurrogate(var2.charAt(var3 - 1)) && Character.isLowSurrogate(var2.charAt(var3))) {
            var3--;
         }

         this.text = this.text + var2.substring(0, var3);
         this.notifyChange();
         GuiSounds.play(GuiSounds.Sound.TYPING);
      }
   }
}

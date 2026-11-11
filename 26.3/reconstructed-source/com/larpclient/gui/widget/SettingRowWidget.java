package com.larpclient.gui.widget;

import com.larpclient.LarpClient;
import com.larpclient.config.ThemePalette;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.TextSetting;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.setting.SettingShortcuts;
import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class SettingRowWidget extends Widget {
   private final Setting<?> setting;
   private final boolean forceActive;
   private boolean chip;
   private boolean dropdownOpen;
   private boolean dragging;
   private boolean listeningForKey;
   private boolean bindingShortcut;
   private boolean editingText;
   private boolean replaceOnType;
   private static final long kEY_CAPTURE_GRACE_NANOS = 300000000L;
   private long listenStartNanos;
   private Module boundModule;
   private boolean moduleResolved;
   private String editBuffer = "";
   private long lastValueBits;
   private long lastUpperBits;
   private String cachedValueText;
   private final Animation hoverAnim = new Animation(0.0);
   private final Animation popupAnim = new Animation(0.0);
   private final Animation toggleAnim;
   private final Animation sliderAnim;
   private final Animation upperSliderAnim;
   private int activeHandle = 1;
   private int valueBoxWidthCache = -1;
   private int colorChannel;
   private int popupX;
   private int popupY;
   private int popupWidth;
   private int popupHeight;
   private int scrollOffset;
   private int viewTop;
   private int viewBottom;
   private final TextField searchField;
   private final TextField textField;
   private boolean editingTextField;
   private List<String> visibleModes = List.of();
   private int highlightedIndex;

   public SettingRowWidget(Setting<?> var1) {
      this(var1, false);
   }

   public SettingRowWidget(Setting<?> var1, boolean var2) {
      this.forceActive = var2;
      this.setting = var1;
      this.searchField = var1 instanceof ModeSetting var3 && var3.isSearchable()
         ? new TextField("Search options...", var1x -> this.getShortcutKey(), 96)
         : null;
      this.textField = var1 instanceof TextSetting var4 ? new TextField(var4.getTitle(), var0 -> {}, 128).onSubmit(this::commitTextEdit) : null;
      if (var1 instanceof ModeSetting var5) {
         this.visibleModes = var5.getVisibleModes();
      }

      this.toggleAnim = new Animation(var1 instanceof BooleanSetting var6 && var6.getValue() ? 1 : 0);
      this.sliderAnim = new Animation(var1 instanceof NumberSetting var7 ? var7.getProgress() : 0.0);
      this.upperSliderAnim = new Animation(this.upperSetting() != null ? this.upperSetting().getProgress() : 0.0);
      this.height = this.previewSize() > 0 ? this.previewSize() + 6 : (this.isRange() ? 38 : 26);
   }

   public boolean isActive() {
      return this.forceActive || this.setting.isVisible();
   }

   public static SettingRowWidget createToggleChip(Setting<?> var0) {
      if (!(var0 instanceof BooleanSetting)) {
         throw new IllegalArgumentException("Toggle chips require boolean settings");
      }

      SettingRowWidget var1 = new SettingRowWidget(var0);
      var1.chip = (boolean)1;
      var1.height = 24;
      return var1;
   }

   public boolean isChip() {
      return this.chip;
   }

   public int preferredWidth() {
      String var1 = this.shortcutLabel();
      return FontRenderer.drawDefaultText(this.setting.getDisplayName()) + 20 + (var1 == null ? 0 : shortcutBadgeWidth(var1) + 4);
   }

   public String tooltip() {
      if (this.getBoundModule() == null) {
         return this.setting.getDescription();
      }

      int var1 = this.getShortcutKey();
      String var2 = this.setting instanceof BooleanSetting ? "toggle" : "cycle";
      String var3 = var1 == -1
         ? "Middle click to bind a key or mouse button that " + var2 + "s this."
         : "Shortcut: " + BindCodes.displayName(var1) + ". Middle click to change, then Escape/Delete to clear.";
      return this.setting.getDescription() + " " + var3;
   }

   private Module getBoundModule() {
      if (!this.moduleResolved) {
         this.moduleResolved = (boolean)1;
         LarpClient var1 = LarpClient.getInstance();
         if (var1 != null && var1.getModuleManager() != null && SettingShortcuts.supportsShortcut(this.setting)) {
            this.boundModule = SettingShortcuts.findOwner(var1.getModuleManager().getModules(), this.setting);
         }
      }

      return this.boundModule;
   }

   private int getShortcutKey() {
      Module var1 = this.getBoundModule();
      return var1 == null ? -1 : LarpClient.getInstance().getConfigManager().getPreferences().settingBinds.getCode(var1, this.setting);
   }

   private void setShortcutKey(int var1) {
      Module var2 = this.getBoundModule();
      if (var2 != null && LarpClient.getInstance().getConfigManager().setShortcut(var2, this.setting, var1)) {
         GuiSounds.play(GuiSounds.Sound.CLICK);
      }
   }

   private String shortcutLabel() {
      if (this.bindingShortcut) {
         return "Press key…";
      }

      int var1 = this.getShortcutKey();
      return var1 == -1 ? null : BindCodes.displayName(var1);
   }

   private static int shortcutBadgeWidth(String var0) {
      return FontRenderer.measureWidth(var0, FontRenderer.TextStyle.HELP) + 8;
   }

   private void drawLabel(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, int var6) {
      String var7 = this.shortcutLabel();
      if (var7 == null) {
         FontRenderer.drawText(var1, FontRenderer.getTextWidth(this.setting.getDisplayName(), var2), var3, var4, var5);
      } else {
         int var8 = Math.min(shortcutBadgeWidth(var7), Math.max(1, var2 / 2));
         String var9 = FontRenderer.getTextWidth(this.setting.getDisplayName(), Math.max(1, var2 - var8 - 4));
         FontRenderer.drawText(var1, var9, var3, var4, var5);
         int var10 = var3 + FontRenderer.drawDefaultText(var9) + 4;
         GlassPainter.fillRoundedRect(var1, var10, var4 - 2, var8, 13, 3, ColorUtil.withAlpha(var5, 0.08));
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(var7, var8 - 8, FontRenderer.TextStyle.HELP),
            var10 + 4,
            var4,
            this.bindingShortcut ? var6 : ColorUtil.withAlpha(var5, 0.7),
            FontRenderer.TextStyle.HELP
         );
      }
   }

   public boolean isPopupOpen() {
      return this.dropdownOpen && this.visible;
   }

   public void closePopup() {
      this.dropdownOpen = (boolean)0;
      this.dragging = (boolean)0;
      this.popupAnim.snapTo(0.0);
      if (this.searchField != null) {
         this.searchField.blur();
      }
   }

   private int tooltip() {
      return this.searchField == null ? 0 : Math.min(24, this.popupHeight);
   }

   private int isChip() {
      return Math.max(0, (this.popupHeight - this.tooltip()) / this.rowHeight());
   }

   private int previewSize() {
      return this.setting instanceof ModeSetting var1 ? var1.previewSize() : 0;
   }

   private int rowHeight() {
      return this.previewSize() > 0 ? 30 : 20;
   }

   private void getShortcutKey() {
      this.visibleModes = ((ModeSetting)this.setting).search(this.searchField.text);
      this.scrollOffset = this.highlightedIndex = 0;
      this.layoutPopup(this.viewTop, this.viewBottom);
   }

   private int fieldWidth() {
      return Math.min(148, this.width / 2 - 4);
   }

   private int fieldX() {
      return this.x + this.width - this.fieldWidth() - 4;
   }

   private boolean isRange() {
      return this.setting instanceof NumberSetting var1 && (var1.hasSuffix() || var1.getUpperBound() != null);
   }

   private int sliderX() {
      return this.isRange() ? this.x + 6 : this.fieldX();
   }

   private int sliderY() {
      return this.isRange() ? this.y + 29 : this.y + 11;
   }

   private int sliderWidth() {
      return Math.max(1, this.isRange() ? this.width - 12 : this.fieldWidth() - this.valueBoxWidth() - 14);
   }

   private NumberSetting upperSetting() {
      return this.setting instanceof NumberSetting var1 ? var1.getUpperBound() : null;
   }

   private int valueBoxWidth() {
      if (!this.isRange()) {
         return 44;
      }

      if (Minecraft.getInstance() == null) {
         return 44;
      }

      if (this.valueBoxWidthCache < 0) {
         this.valueBoxWidthCache = Math.clamp(FontRenderer.drawDefaultText(((NumberSetting)this.setting).getMaxDisplay()) + 10, 44, 96);
      }

      return this.valueBoxWidthCache;
   }

   private int valueBoxX() {
      return this.x + this.width - 4 - this.valueBoxWidth();
   }

   private String displayValue() {
      if (this.setting instanceof NumberSetting var8) {
         NumberSetting var11 = var8.getUpperBound();
         long var3 = Double.doubleToLongBits(var8.getValue());
         long var5 = var11 == null ? 0L : Double.doubleToLongBits(var11.getValue());
         if (this.cachedValueText == null || this.lastValueBits != var3 || this.lastUpperBits != var5) {
            this.cachedValueText = !var8.hasSuffix() && var11 == null && !var8.isAbbreviated()
               ? BigDecimal.valueOf(var8.getValue()).stripTrailingZeros().toPlainString()
               : var8.getDisplayValue();
            this.lastValueBits = var3;
            this.lastUpperBits = var5;
         }

         return this.cachedValueText;
      } else if (this.setting instanceof KeybindSetting var7) {
         return BindCodes.displayName(var7.getValue());
      } else {
         return this.setting instanceof ModeSetting var1 ? var1.getModeLabel(var1.getValue()) : this.setting.getValueAsString();
      }
   }

   public void layoutPopup(int var1, int var2) {
      if (this.searchField == null && this.setting instanceof ModeSetting var3) {
         this.visibleModes = var3.getVisibleModes();
      }

      this.viewTop = var1;
      this.viewBottom = var2;
      this.popupX = this.setting instanceof ModeSetting ? this.fieldX() : this.x;
      this.popupWidth = this.setting instanceof ModeSetting ? this.fieldWidth() : this.width;
      int var5 = this.setting instanceof ModeSetting
         ? (this.searchField == null ? 0 : 24) + Math.max(1, Math.min(8, this.visibleModes.size())) * this.rowHeight()
         : 80;
      this.popupHeight = Math.min(var5, Math.max(0, var2 - var1));
      int var6 = (int)Math.round((1.0 - this.popupAnim.animateTo(this.dropdownOpen ? 1.0 : 0.0, 160)) * 4.0);
      this.popupY = Math.clamp(this.y + this.height + var6, var1, Math.max(var1, var2 - this.popupHeight));
      if (this.setting instanceof ModeSetting) {
         this.scrollOffset = Math.clamp(this.scrollOffset, 0, Math.max(0, this.visibleModes.size() - Math.max(1, this.isChip())));
      }

      if (this.searchField != null) {
         this.searchField.x = this.popupX + 3;
         this.searchField.y = this.popupY + 2;
         this.searchField.width = Math.max(1, this.popupWidth - 6);
         this.searchField.height = Math.max(0, this.tooltip() - 4);
      }
   }

   public boolean containsPopupPoint(double var1, double var3) {
      return this.isPopupOpen() && DrawUtil.isHovered(var1, var3, this.popupX, this.popupY, this.popupWidth, this.popupHeight);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.visible) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.hoverAnim.animateTo(!var4 && !this.focused ? 0.0 : 1.0, 150);
         int var7 = ThemePalette.readableAccent(Theme.accentAt(this.x), Theme.background);
         if (this.chip && this.setting instanceof BooleanSetting var22) {
            double var24 = this.toggleAnim.animateTo(var22.getValue() ? 1.0 : 0.0, 150);
            int var26 = ThemePalette.readableAccent(Theme.accentAt(this.x), Theme.cardBackground | 0xFF000000);
            int var28 = ColorUtil.lerpColor(Theme.rowBackground | 0xFF000000, Theme.cardHoverBackground | 0xFF000000, var5);
            int var29 = ColorUtil.lerpColor(var28, var26, var24);
            int var33 = Theme.isThemed() ? this.height / 2 : 7;
            if (var24 > 0.01) {
               GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var33, 5.0F, ColorUtil.withAlpha(var26, 0.22 * var24));
            }

            GlassPainter.fillRoundedRect(var1, this.x, this.y, this.width, this.height, var33, var29);
            GlassPainter.drawBorder(var1, this.x, this.y, this.width, this.height, var33, ColorUtil.lerpColor(Theme.controlBackground, var26, var24));
            if (this.focused && this.focusVisible) {
               GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, var33);
            }

            this.drawLabel(var1, this.width - 16, this.x + 8, this.y + (this.height - 9) / 2, ThemePalette.controlInk(var29), var7);
         } else {
            if (var5 > 0.001 && Theme.isThemed()) {
               GlassPainter.fillRoundedRect(
                  var1, this.x, this.y, this.width, this.height, Theme.radiusMedium + 1, ColorUtil.withAlpha(Theme.textPrimary, 0.028 * var5)
               );
            }

            int var8 = this.previewSize();
            int var23 = var8 > 0 ? var8 + 8 : 0;
            int var10 = var8 > 0 ? this.y + (this.height - 26) / 2 : this.y;
            if (var8 > 0 && this.setting instanceof ModeSetting var11) {
               var11.drawPreview(var1, var11.getValue(), this.x + 4, this.y + 3, var8);
            }

            this.drawLabel(
               var1,
               this.setting instanceof BooleanSetting
                  ? this.width - 42
                  : (this.isRange() ? this.width - this.valueBoxWidth() - 16 : this.width / 2 - 12 - var23),
               this.x + 4 + var23,
               var10 + 7,
               Theme.textPrimary,
               var7
            );
            int var25 = this.focused && this.focusVisible ? 1 : 0;
            if (this.setting instanceof BooleanSetting var27) {
               if (var25 != 0) {
                  GlassPainter.drawFocusRing(var1, this.x + this.width - 28, this.y + 6, 24, 13, 6);
               }

               GlassPainter.drawPulsingGlow(var1, this.x + this.width - 28, this.y + 6, this.toggleAnim.animateTo(var27.getValue() ? 1.0 : 0.0, 180));
            } else if (this.setting instanceof NumberSetting var13) {
               GlassPainter.drawButton(var1, this.valueBoxX(), this.y + 3, this.valueBoxWidth(), 20, !this.editingText && var25 == 0 ? var5 * 0.3 : 1.0);
               String var31 = FontRenderer.getTextWidth(this.editingText ? this.editBuffer + "_" : this.displayValue(), this.valueBoxWidth() - 10);
               FontRenderer.drawText(
                  var1,
                  var31,
                  this.valueBoxX() + this.valueBoxWidth() - 6 - FontRenderer.drawDefaultText(var31),
                  this.y + 7,
                  this.editingText ? Theme.textPrimary : ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, var5)
               );
               if (this.dragging) {
                  this.sliderAnim.snapTo(var13.getProgress());
               }

               double var15 = this.sliderAnim.animateTo(var13.getProgress(), 160);
               NumberSetting var17 = this.upperSetting();
               if (Theme.isThemed()) {
                  double var37 = -1.0;
                  if (var17 != null) {
                     if (this.dragging) {
                        this.upperSliderAnim.snapTo(var17.getProgress());
                     }

                     var37 = this.upperSliderAnim.animateTo(var17.getProgress(), 160);
                  }

                  this.renderSlider(var1, var15, var37, Math.max(var5, this.dragging ? 1.0 : 0.0), var7);
                  return;
               }

               GlassPainter.fillRoundedRect(var1, this.sliderX(), this.sliderY() + 1, this.sliderWidth(), 2, 1, Theme.controlBackground);
               if (var17 == null) {
                  GlassPainter.fillRoundedRect(var1, this.sliderX(), this.sliderY() + 1, (int)(this.sliderWidth() * var15), 2, 1, var7);
                  drawSliderHandle(var1, this.sliderX() + (int)(this.sliderWidth() * var15), this.sliderY() + 2, var5);
               } else {
                  if (this.dragging) {
                     this.upperSliderAnim.snapTo(var17.getProgress());
                  }

                  double var18 = this.upperSliderAnim.animateTo(var17.getProgress(), 160);
                  int var20 = (int)(this.sliderWidth() * var15);
                  int var21 = (int)(this.sliderWidth() * var18);
                  GlassPainter.fillRoundedRect(var1, this.sliderX() + var20, this.sliderY() + 1, Math.max(0, var21 - var20), 2, 1, var7);
                  drawSliderHandle(var1, this.sliderX() + var20, this.sliderY() + 2, var5);
                  drawSliderHandle(var1, this.sliderX() + var21, this.sliderY() + 2, var5);
               }
            } else {
               int var32 = this.fieldX();
               int var34 = this.fieldWidth();
               GlassPainter.drawButton(var1, var32, var10 + 2, var34, 20, !this.dropdownOpen && !this.listeningForKey && var25 == 0 ? var5 * 0.3 : 1.0);
               if (this.setting instanceof TextSetting) {
                  this.textField.bounds(var32, this.y + 2, var34);
                  this.textField.height = 20;
                  if (!this.editingTextField) {
                     this.textField.text = this.displayValue();
                  }

                  this.textField.render(var1, var2, var3);
               } else if (this.setting instanceof ColorSetting var16) {
                  FontRenderer.drawText(
                     var1, FontRenderer.getTextWidth(String.format("#%08X", var16.getValue()), var34 - 28), var32 + 6, this.y + 6, Theme.textSecondary
                  );
                  GlassPainter.fillRoundedRect(var1, this.x + this.width - 23, this.y + 6, 14, 12, 3, var16.getValue());
                  RoundedShapes.drawRimGradient(
                     var1,
                     this.x + this.width - 23,
                     this.y + 6,
                     14.0F,
                     12.0F,
                     3.0F,
                     ColorUtil.multiplyAlpha(Theme.accentGlow, 2.0),
                     ColorUtil.multiplyAlpha(Theme.accentSoft, 2.5)
                  );
               } else {
                  int var36 = this.setting instanceof ModeSetting var38 ? getShortcutKey(var1, var38, var38.getValue(), var32 + 6, var10 + 4) : 0;
                  int var39 = this.setting instanceof KeybindSetting var40 && var40.getValue() < 0 ? 1 : 0;
                  String var41 = this.setting instanceof KeybindSetting var43 && var43.isToggleable() ? "Press or click…" : "Press key…";
                  FontRenderer.drawText(
                     var1,
                     FontRenderer.getTextWidth(this.listeningForKey ? var41 : this.displayValue(), var34 - 22 - var36),
                     var32 + 6 + var36,
                     var10 + 6,
                     this.listeningForKey ? var7 : (var39 != 0 ? Theme.textSecondary : Theme.textPrimary)
                  );
                  if (this.setting instanceof ModeSetting) {
                     Icons.texture(
                        var1, this.dropdownOpen ? Icons.cHEVRON_UP : Icons.cHEVRON_DOWN, this.x + this.width - 20, var10 + 5, 14, Theme.textSecondary
                     );
                  }
               }
            }
         }
      }
   }

   private void renderSlider(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8) {
      switch (Theme.getLook().slider()) {
         case "Minimal":
         case "Classic":
            this.sliderX(var1, var2, var4, var6, var8, Theme.getLook().slider().equals("Classic"));
            return;
         default:
            float var23 = this.sliderY() + 2;
            float var24 = this.sliderWidth();
            float var11 = this.sliderX();
            float var12 = 6.0F + (float)var6;
            float var13 = var12 / 2.0F;
            float var14 = var23 - var13;
            int var15 = var4 >= 0.0 ? 1 : 0;
            RoundedShapes.fillRounded(var1, var11, var14, var24, var12, var13, ColorUtil.withAlpha(Theme.textPrimary, 0.1));
            RoundedShapes.drawRimGradient(
               var1, var11, var14, var24, var12, var13, ColorUtil.withAlpha(Theme.controlBackground, 0.5), ColorUtil.withAlpha(Theme.controlBackground, 0.5)
            );
            float var16 = var15 != 0 ? (float)(var24 * Math.min(var2, var4)) : 0.0F;
            float var17 = (float)(var24 * (var15 != 0 ? Math.max(var2, var4) : var2));
            float var18 = var11 + Math.max(0.0F, Math.min(var16 - var13, var24 - var12));
            float var19 = var11 + Math.min(var24, Math.max(var17 + var13, var16 + var12));
            float var20 = Math.max(var12, var19 - var18);
            int var21 = ColorUtil.lerpColor(var8, Theme.accentEnd | 0xFF000000, 0.6);
            if (var6 > 0.01) {
               GlassPainter.fillRoundedRect(var1, var18, var14, var20, var12, var13, 5.0F, ColorUtil.withAlpha(var8, 0.35 * var6));
            }

            RoundedShapes.fillRoundedGradientHorizontal(var1, var18, var14, var20, var12, var13, var21, var8);
            RoundedShapes.fillRoundedGradient(var1, var18, var14, var20, var12 / 2.0F, var13, 956301311, 16777215);
            float var22 = var12 - 2.5F;
            if (var15 != 0) {
               drawKnob(var1, var18 + var13, var23, var22, this.dragging && this.activeHandle == 1 ? 1.0 : 0.0, var8);
               drawKnob(var1, var18 + var20 - var13, var23, var22, this.dragging && this.activeHandle == 2 ? 1.0 : 0.0, var8);
            } else {
               drawKnob(var1, var18 + var20 - var13, var23, var22, this.dragging ? 1.0 : 0.0, var8);
            }
      }
   }

   private void sliderX(GuiGraphicsExtractor var1, double var2, double var4, double var6, int var8, boolean var9) {
      float var10 = this.sliderY() + 2;
      float var11 = this.sliderWidth();
      float var12 = this.sliderX();
      float var13 = var9 ? 4.0F : 2.5F + (float)var6;
      float var14 = var10 - var13 / 2.0F;
      int var15 = var4 >= 0.0 ? 1 : 0;
      RoundedShapes.fillRounded(var1, var12, var14, var11, var13, var13 / 2.0F, ColorUtil.withAlpha(Theme.textPrimary, var9 ? 0.14 : 0.16));
      float var16 = var15 != 0 ? (float)(var11 * Math.min(var2, var4)) : 0.0F;
      float var17 = (float)(var11 * (var15 != 0 ? Math.max(var2, var4) : var2));
      float var18 = Math.max(var13, var17 - var16);
      if (var9) {
         if (var6 > 0.01) {
            GlassPainter.fillRoundedRect(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, 5.0F, ColorUtil.withAlpha(var8, 0.4 * var6));
         }

         RoundedShapes.fillRoundedGradientHorizontal(
            var1, var12 + var16, var14, var18, var13, var13 / 2.0F, ColorUtil.lerpColor(var8, Theme.accentEnd | 0xFF000000, 0.6), var8
         );
      } else {
         RoundedShapes.fillRounded(var1, var12 + var16, var14, var18, var13, var13 / 2.0F, var8);
      }

      float var19 = (var9 ? 7 : 6) + 2.0F * (float)var6;
      if (var15 != 0) {
         createToggleChip(var1, var12 + var16, var10, var19, var8, var9);
      }

      createToggleChip(var1, var12 + var17, var10, var19, var8, var9);
   }

   private static void createToggleChip(GuiGraphicsExtractor var0, float var1, float var2, float var3, int var4, boolean var5) {
      float var6 = var1 - var3 / 2.0F;
      float var7 = var2 - var3 / 2.0F;
      RoundedShapes.drawGlow(var0, var6, var7, var3, var3, var3 / 2.0F, 1.5F, 1073741824, 1342177280);
      RoundedShapes.fillRounded(var0, var6, var7, var3, var3, var3 / 2.0F, var5 ? -1 : Theme.background | 0xFF000000);
      RoundedShapes.drawRim(var0, var6, var7, var3, var3, var3 / 2.0F, var4);
   }

   private static void drawKnob(GuiGraphicsExtractor var0, float var1, float var2, float var3, double var4, int var6) {
      float var7 = var1 - var3 / 2.0F;
      float var8 = var2 - var3 / 2.0F;
      RoundedShapes.drawGlow(var0, var7, var8, var3, var3, var3 / 2.0F, 1.5F, 1174405120, 1375731712);
      if (var4 > 0.01) {
         GlassPainter.fillRoundedRect(var0, var7, var8, var3, var3, var3 / 2.0F, 5.0F, ColorUtil.withAlpha(var6, 0.45 * var4));
      }

      RoundedShapes.fillRounded(var0, var7, var8, var3, var3, var3 / 2.0F, -1);
   }

   private static void drawSliderHandle(GuiGraphicsExtractor var0, int var1, int var2, double var3) {
      int var5 = 8 + (int)Math.round(var3);
      RoundedShapes.fillRounded(var0, var1 - var5 / 2 - 1, var2 - var5 / 2 - 1, var5 + 2, var5 + 2, var5 / 2.0F + 1.0F, 855638016);
      RoundedShapes.fillRounded(var0, var1 - var5 / 2, var2 - var5 / 2, var5, var5, var5 / 2.0F, -723722);
   }

   public void renderPopup(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.isPopupOpen()) {
         try (AlphaScope var4 = AlphaScope.push(this.popupAnim.animateTo(1.0, 160))) {
            boolean var5 = Theme.isThemed();
            if (var5) {
               GlassPainter.drawFocusedField(var1, this.popupX, this.popupY, this.popupWidth, this.popupHeight, Theme.radiusLarge, Theme.headerBackground, 1.0);
            } else {
               GlassPainter.drawShadow(var1, this.popupX, this.popupY, this.popupWidth, this.popupHeight);
               GlassPainter.drawPanel(var1, this.popupX, this.popupY, this.popupWidth, this.popupHeight, Theme.radiusMedium, Theme.headerBackground, 1.0);
            }

            var1.enableScissor(this.popupX, this.popupY, this.popupX + this.popupWidth, this.popupY + this.popupHeight);

            try {
               if (this.setting instanceof ModeSetting var6) {
                  if (this.searchField != null) {
                     this.searchField.render(var1, var2, var3);
                  }

                  int var27 = this.popupY + this.tooltip();
                  int var28 = Math.min(this.visibleModes.size() - this.scrollOffset, this.isChip());
                  int var29 = this.rowHeight();
                  if (this.visibleModes.isEmpty() && this.isChip() > 0) {
                     FontRenderer.drawText(var1, "No matching blocks", this.popupX + 8, var27 + 6, Theme.textSecondary);
                  }

                  for (int var30 = 0; var30 < var28; var30++) {
                     String var31 = this.visibleModes.get(var30 + this.scrollOffset);
                     int var33 = var27 + var30 * var29;
                     boolean var34 = DrawUtil.isHovered(var2, var3, this.popupX, var33, this.popupWidth, var29);
                     boolean var35 = var6.isMode(var31);
                     int var16 = !var34 && (this.searchField == null || this.highlightedIndex != var30 + this.scrollOffset) ? 0 : 1;
                     if (var5 && var35) {
                        GlassPainter.fillRoundedRect(
                           var1,
                           this.popupX + 2,
                           var33 + 1,
                           this.popupWidth - 4,
                           var29 - 2,
                           Theme.radiusMedium,
                           Theme.glowColor(this.popupX, var16 != 0 ? 1.3 : 1.0)
                        );
                     } else if (var16 != 0 || var35) {
                        GlassPainter.fillRoundedRect(
                           var1,
                           this.popupX + 2,
                           var33 + 1,
                           this.popupWidth - 4,
                           var29 - 2,
                           Theme.radiusMedium,
                           var5 ? ColorUtil.withAlpha(Theme.textPrimary, 0.08) : Theme.cardHoverBackground
                        );
                     }

                     int var17;
                     if (var6.previewSize() > 0) {
                        var6.drawPreview(var1, var31, this.popupX + 5, var33 + 3, var29 - 6);
                        var17 = var29 - 4;
                     } else {
                        var17 = getShortcutKey(var1, var6, var31, this.popupX + 8, var33 + 2);
                     }

                     FontRenderer.drawText(
                        var1,
                        FontRenderer.getTextWidth(var6.getModeLabel(var31), this.popupWidth - 16 - var17),
                        this.popupX + 8 + var17,
                        var33 + (var29 - 8) / 2,
                        !var35 && !var34 ? ColorUtil.lerpColor(Theme.textSecondary, Theme.textPrimary, 0.5) : Theme.textPrimary
                     );
                  }

                  GlassPainter.drawScrollbar(
                     var1, this.popupX + this.popupWidth - 3, var27, this.isChip() * var29, this.visibleModes.size() * var29, this.scrollOffset * var29
                  );
               } else if (this.setting instanceof ColorSetting var7) {
                  float[] var26 = Color.RGBtoHSB(var7.getValue() >> 16 & 0xFF, var7.getValue() >> 8 & 0xFF, var7.getValue() & 0xFF, null);
                  String[] var9 = new String[]{"H", "S", "V", "A"};
                  int var10 = Math.max(1, this.popupWidth - 28);

                  for (int var11 = 0; var11 < 4; var11++) {
                     int var12 = this.popupY + 6 + var11 * 18;
                     FontRenderer.drawText(var1, var9[var11], this.popupX + 5, var12, Theme.textSecondary);

                     for (byte var13 = 0; var13 < var10; var13 += 3) {
                        float var14 = (float)var13 / var10;

                        int var15 = switch (var11) {
                           case 0 -> ColorUtil.hsbToRgb(var14, 1.0F, 1.0F);
                           case 1 -> ColorUtil.hsbToRgb(var26[0], var14, var26[2]);
                           case 2 -> ColorUtil.hsbToRgb(var26[0], var26[1], var14);
                           default -> ColorUtil.lerpColor(Theme.panelBackground, var7.getValue() | 0xFF000000, var14);
                        };
                        DrawUtil.fillRect(var1, this.popupX + 20 + var13, var12, Math.min(3, var10 - var13), 10, var15);
                     }

                     float var32 = var11 < 3 ? var26[var11] : (var7.getValue() >>> 24) / 255.0F;
                     DrawUtil.drawOutline(var1, this.popupX + 19 + (int)(var32 * var10), var12 - 1, 3, 12, Theme.textPrimary);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            if (!var5) {
               RoundedShapes.drawRimGradient(
                  var1,
                  this.popupX,
                  this.popupY,
                  this.popupWidth,
                  this.popupHeight,
                  Theme.radiusMedium,
                  ColorUtil.multiplyAlpha(Theme.accentGlow, 1.5),
                  ColorUtil.multiplyAlpha(Theme.accentSoft, 1.5)
               );
            }
         }
      }
   }

   private static int getShortcutKey(GuiGraphicsExtractor var0, ModeSetting var1, String var2, int var3, int var4) {
      int var5 = var1.drawModeIcon(var0, var2, var3, var4);
      if (var5 > 0) {
         return var5;
      }

      ItemStack var6 = var1.getModeIcon(var2);
      if (var6.isEmpty()) {
         return 0;
      }

      var0.item(var6, var3, var4);
      return 18;
   }

   public boolean clickPopup(double var1, double var3, int var5) {
      if (!this.containsPopupPoint(var1, var3)) {
         return false;
      }

      if (var5 != 0) {
         return true;
      }

      if (this.setting instanceof ModeSetting var6) {
         if (this.searchField != null && var3 < this.popupY + this.tooltip()) {
            this.searchField.click(var1, var3, var5);
            return true;
         }

         int var8 = (int)(var3 - this.popupY - this.tooltip()) / this.rowHeight();
         if (var8 < 0 || var8 >= this.isChip() || var8 + this.scrollOffset >= this.visibleModes.size()) {
            return true;
         }

         this.valueBoxX(var6, this.visibleModes.get(var8 + this.scrollOffset));
         this.closePopup();
         GuiSounds.play(GuiSounds.Sound.CLICK);
      } else if (this.setting instanceof ColorSetting) {
         this.colorChannel = Math.clamp((int)(var3 - this.popupY - 6.0) / 18, 0, 3);
         this.dragging = (boolean)1;
         this.drag(var1, var3);
      }

      return true;
   }

   private void valueBoxX(ModeSetting var1, String var2) {
      try {
         var1.setValue(var2);
      } catch (IllegalArgumentException var5) {
         FakeSpawnerModule var4 = FakeSpawnerModule.getInstance();
         if (var4 == null || !var4.getBlockSettings().contains(var1)) {
            throw var5;
         }

         PlayerMessages.sendMessage(Component.literal(var5.getMessage()), true);
      }
   }

   public void shortcutBadgeWidth(double var1) {
      if (this.setting instanceof ModeSetting) {
         this.scrollOffset -= (int)var1;
         this.layoutPopup(this.viewTop, this.viewBottom);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (!this.contains(var1, var3)) {
         return false;
      }

      this.focused = (boolean)1;
      if (this.listeningForKey && (var5 == 0 || var5 == 1) && this.setting instanceof KeybindSetting var6 && var6.isToggleable()) {
         if (System.nanoTime() - this.listenStartNanos < 300000000L) {
            return true;
         }

         var6.setKeyCode(BindCodes.toMouseBind(var5));
         this.listeningForKey = (boolean)0;
         GuiSounds.play(GuiSounds.Sound.CLICK);
         return true;
      } else if (var5 == 2 && this.getBoundModule() != null && !this.bindingShortcut) {
         this.editingText = (boolean)0;
         this.editingTextField = (boolean)0;
         this.listeningForKey = (boolean)0;
         this.closePopup();
         if (this.textField != null) {
            this.textField.blur();
         }

         this.bindingShortcut = (boolean)1;
         GuiSounds.play(GuiSounds.Sound.CLICK);
         return true;
      } else if (var5 == 1) {
         this.editingText = (boolean)0;
         this.editingTextField = (boolean)0;
         this.listeningForKey = (boolean)0;
         this.bindingShortcut = (boolean)0;
         if (this.textField != null) {
            this.textField.blur();
         }

         if (this.setting instanceof ModeSetting var12) {
            this.valueBoxX(var12, var12.getDefaultValue());
         } else {
            this.setting.reset();
         }

         if (this.upperSetting() != null) {
            this.upperSetting().reset();
         }

         GuiSounds.play(GuiSounds.Sound.CLICK);
         return true;
      } else {
         if (var5 != 0) {
            return false;
         }

         if (this.bindingShortcut) {
            this.bindingShortcut = (boolean)0;
            return true;
         }

         if (this.setting instanceof BooleanSetting var11) {
            var11.toggle();
            GuiSounds.play(var11.getValue() ? GuiSounds.Sound.SWITCH_ON : GuiSounds.Sound.SWITCH_OFF);
         } else if (this.setting instanceof NumberSetting var13) {
            if (this.upperSetting() != null) {
               if (var3 >= this.y + 22) {
                  double var16 = this.containsPopupPoint(var1);
                  this.activeHandle = Math.abs(var16 - var13.getValue()) > Math.abs(var16 - this.upperSetting().getValue()) ? 2 : 1;
                  this.dragging = (boolean)1;
                  this.drag(var1, var3);
               }
            } else if (this.isRange() ? !(var3 < this.y + 24) || !(var1 >= this.valueBoxX() - 4) : !(var1 >= this.valueBoxX() - 4)) {
               if (this.isRange() ? var3 >= this.y + 22 : var1 >= this.fieldX() - 4) {
                  this.commitNumberEdit();
                  this.dragging = (boolean)1;
                  this.drag(var1, var3);
               }
            } else {
               this.editBuffer = var13.getEditValueAsString();
               this.editingText = (boolean)1;
               this.replaceOnType = (boolean)1;
            }
         } else if (this.setting instanceof ModeSetting var8) {
            this.dropdownOpen = (boolean)(!this.dropdownOpen ? 1 : 0);
            GuiSounds.play(this.dropdownOpen ? GuiSounds.Sound.SETTINGS_OPEN : GuiSounds.Sound.SETTINGS_CLOSE);
            if (this.searchField != null) {
               this.searchField.text = "";
               this.searchField.focused = this.dropdownOpen;
            }

            this.visibleModes = var8.getVisibleModes();
            this.highlightedIndex = Math.max(0, this.visibleModes.indexOf(var8.getValue()));
            this.scrollOffset = Math.max(0, this.highlightedIndex - 2);
            this.layoutPopup(this.viewTop, this.viewBottom);
         } else if (this.setting instanceof ColorSetting) {
            this.dropdownOpen = (boolean)(!this.dropdownOpen ? 1 : 0);
            GuiSounds.play(this.dropdownOpen ? GuiSounds.Sound.SETTINGS_OPEN : GuiSounds.Sound.SETTINGS_CLOSE);
         } else if (this.setting instanceof TextSetting) {
            this.textField.bounds(this.fieldX(), this.y + 2, this.fieldWidth());
            this.textField.height = 20;
            this.textField.text = this.displayValue();
            this.editingTextField = (boolean)1;
            this.textField.click(this.fieldX() + 2, this.y + 4, 0);
            this.textField.acceptSuggestion();
         } else if (this.setting instanceof KeybindSetting) {
            this.listeningForKey = (boolean)1;
            this.listenStartNanos = System.nanoTime();
            GuiSounds.play(GuiSounds.Sound.CLICK);
         }

         return true;
      }
   }

   @Override
   public void drag(double var1, double var3) {
      if (this.dragging) {
         if (this.upperSetting() != null) {
            if (this.createToggleChip(this.containsPopupPoint(var1))) {
               GuiSounds.play(GuiSounds.Sound.SLIDER);
            }
         } else if (this.setting instanceof NumberSetting var5) {
            double var12 = var5.getValue();
            var5.setProgress((var1 - this.sliderX()) / this.sliderWidth());
            if (var12 != var5.getValue()) {
               GuiSounds.play(GuiSounds.Sound.SLIDER);
            }
         }

         if (this.setting instanceof ColorSetting var11) {
            double var14 = Math.clamp((var1 - this.popupX - 20.0) / Math.max(1, this.popupWidth - 28), 0.0, 1.0);
            int var8 = var11.getValue();
            float[] var9 = Color.RGBtoHSB(var8 >> 16 & 0xFF, var8 >> 8 & 0xFF, var8 & 0xFF, null);
            if (this.colorChannel < 3) {
               var9[this.colorChannel] = (float)var14;
            }

            int var10 = this.colorChannel == 3 ? (int)(var14 * 255.0) : var8 >>> 24;
            var11.setValue(ColorUtil.hsbToRgb(var9[0], var9[1], var9[2]) & 16777215 | var10 << 24);
            if (var8 != var11.getValue()) {
               GuiSounds.play(GuiSounds.Sound.SLIDER);
            }
         }
      }
   }

   private double containsPopupPoint(double var1) {
      NumberSetting var3 = (NumberSetting)this.setting;
      return var3.getMin() + Math.clamp((var1 - this.sliderX()) / this.sliderWidth(), 0.0, 1.0) * (var3.getMax() - var3.getMin());
   }

   private boolean createToggleChip(double var1) {
      NumberSetting var3 = (NumberSetting)this.setting;
      NumberSetting var4 = this.upperSetting();
      double var5 = var3.getValue();
      double var7 = var4.getValue();
      if (this.activeHandle == 1) {
         var3.getDisplayValue(Math.min(var1, var4.getValue()));
      } else {
         var4.getDisplayValue(Math.max(var1, var3.getValue()));
      }

      return var5 != var3.getValue() || var7 != var4.getValue();
   }

   private void commitNumberEdit() {
      if (this.editingText) {
         try {
            ((NumberSetting)this.setting).setFromEditString(this.editBuffer);
         } catch (IllegalArgumentException var2) {
         }

         this.editingText = (boolean)0;
      }
   }

   private void commitTextEdit() {
      if (this.editingTextField && this.setting instanceof TextSetting var1) {
         try {
            var1.setText(this.textField.text);
         } catch (IllegalArgumentException var3) {
         }

         this.editingTextField = (boolean)0;
         this.textField.blur();
      }
   }

   @Override
   public void release() {
      this.dragging = (boolean)0;
   }

   @Override
   public void blur() {
      super.blur();
      this.listeningForKey = (boolean)0;
      this.bindingShortcut = (boolean)0;
      this.closePopup();
      this.commitNumberEdit();
      this.commitTextEdit();
   }

   public boolean getShortcutKey() {
      return this.focused
         && (this.editingText || this.editingTextField || this.listeningForKey || this.bindingShortcut || this.dropdownOpen && this.searchField != null);
   }

   @Override
   public boolean captureMouse(int var1) {
      if (this.focused && BindCodes.isExtraMouseButton(var1)) {
         int var2 = BindCodes.toMouseBind(var1);
         if (this.listeningForKey && this.setting instanceof KeybindSetting var3) {
            var3.setKeyCode(var2);
            this.listeningForKey = (boolean)0;
            GuiSounds.play(GuiSounds.Sound.CLICK);
            return true;
         } else if (this.bindingShortcut) {
            this.bindingShortcut = (boolean)0;
            this.setShortcutKey(var2);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean character(int var1) {
      if (this.focused && this.editingTextField) {
         return this.textField.character(var1);
      }

      if (this.focused && this.dropdownOpen && this.searchField != null) {
         return this.searchField.character(var1);
      }

      int var2 = this.setting instanceof NumberSetting var3 && var3.isAbbreviated() && "kKmMbBtT,$".indexOf(var1) >= 0 ? 1 : 0;
      if (this.focused && this.editingText && ("0123456789.-+eE".indexOf(var1) >= 0 || var2 != 0)) {
         if (this.replaceOnType) {
            this.editBuffer = "";
            this.replaceOnType = (boolean)0;
         }

         if (this.editBuffer.length() < 24) {
            this.editBuffer = this.editBuffer + (char)var1;
            GuiSounds.play(GuiSounds.Sound.TYPING);
         }

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

      if (this.editingTextField) {
         if (var1 == 256) {
            this.editingTextField = (boolean)0;
            this.textField.blur();
            return true;
         }

         if (var1 != 257 && var1 != 335) {
            return this.textField.key(var1);
         }

         this.commitTextEdit();
         return true;
      } else {
         if (this.listeningForKey && this.setting instanceof KeybindSetting var8) {
            var8.setKeyCode(var1 != 256 && var1 != 261 ? var1 : -1);
            this.listeningForKey = (boolean)0;
            return true;
         }

         if (this.bindingShortcut) {
            this.bindingShortcut = (boolean)0;
            this.setShortcutKey(var1 != 256 && var1 != 261 ? var1 : -1);
            return true;
         }

         if (this.editingText) {
            if (var1 == 256) {
               this.editingText = (boolean)0;
            } else if (var1 == 257) {
               this.commitNumberEdit();
            } else if (var1 == 259) {
               this.editBuffer = !this.replaceOnType && !this.editBuffer.isEmpty() ? this.editBuffer.substring(0, this.editBuffer.length() - 1) : "";
               this.replaceOnType = (boolean)0;
            }

            return true;
         } else {
            if (this.dropdownOpen && var1 == 256) {
               this.closePopup();
               return true;
            }

            if (this.dropdownOpen && this.searchField != null && this.setting instanceof ModeSetting var7) {
               if (var1 == 264 || var1 == 265) {
                  this.highlightedIndex = Math.clamp(this.highlightedIndex + (var1 == 264 ? 1 : -1), 0, Math.max(0, this.visibleModes.size() - 1));
                  if (this.highlightedIndex < this.scrollOffset) {
                     this.scrollOffset = this.highlightedIndex;
                  } else if (this.highlightedIndex >= this.scrollOffset + Math.max(1, this.isChip())) {
                     this.scrollOffset = this.highlightedIndex - Math.max(1, this.isChip()) + 1;
                  }

                  this.layoutPopup(this.viewTop, this.viewBottom);
                  return true;
               } else {
                  if (var1 != 257 && var1 != 335) {
                     return this.searchField.key(var1);
                  }

                  if (!this.visibleModes.isEmpty()) {
                     this.valueBoxX(var7, this.visibleModes.get(this.highlightedIndex));
                     this.closePopup();
                     GuiSounds.play(GuiSounds.Sound.CLICK);
                  }

                  return true;
               }
            } else if (this.setting instanceof ModeSetting var2 && this.dropdownOpen && (var1 == 264 || var1 == 265)) {
               List var15 = var2.getVisibleModes();
               int var4 = Math.clamp(var15.indexOf(var2.getValue()) + (var1 == 264 ? 1 : -1), 0, var15.size() - 1);
               this.valueBoxX(var2, (String)var15.get(var4));
               this.scrollOffset = Math.max(0, var4 - 2);
               this.layoutPopup(this.viewTop, this.viewBottom);
               return true;
            } else if (this.setting instanceof NumberSetting var5 && this.upperSetting() != null && (var1 == 262 || var1 == 263)) {
               double var14 = this.activeHandle == 1 ? var5.getValue() : this.upperSetting().getValue();
               if (this.createToggleChip(var14 + (var1 == 262 ? 1 : -1) * var5.getStep())) {
                  GuiSounds.play(GuiSounds.Sound.SLIDER);
               }

               return true;
            } else if (this.setting instanceof NumberSetting var6 && (var1 == 262 || var1 == 263)) {
               double var13 = var6.getValue();
               var6.getDisplayValue(var13 + (var1 == 262 ? 1 : -1) * var6.getStep());
               if (var13 != var6.getValue()) {
                  GuiSounds.play(GuiSounds.Sound.SLIDER);
               }

               return true;
            } else {
               return var1 != 257 && var1 != 32 ? false : this.click(this.chip ? this.x + this.width / 2.0 : this.x + 2, this.y + 2, 0);
            }
         }
      }
   }
}

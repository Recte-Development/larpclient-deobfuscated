package com.larpclient.gui.clickgui;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.config.PanelLayoutConfig;
import com.larpclient.config.ThemePalette;
import com.larpclient.gui.misc.GuiScaleInfo;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.misc.UiState;
import com.larpclient.gui.screen.WidgetScreen;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.SettingRowWidget;
import com.larpclient.gui.widget.TextField;
import com.larpclient.gui.widget.Widget;
import com.larpclient.hud.ToastAnimation;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.ModuleCategory;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.render.ChooseASkyEffectAndAdjustItsAppearanceModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.setting.ModuleSettingGroups;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class LegacyClickGuiScreen extends WidgetScreen {
   static final int mARGIN = 8;
   static final int gAP = 6;
   static final int hEADER_HEIGHT = 20;
   static final int rOW_HEIGHT = 14;
   static final int pANEL_TOP = 36;
   static final int fOOTER_HEIGHT = 46;
   static final int bUTTON_HEIGHT = 20;
   static final int tOGGLE_HEIGHT = 18;
   static final int mIN_PANEL_WIDTH = 90;
   static final int mAX_PANEL_WIDTH = 122;
   static final int pANEL_ROW_HEIGHT = 160;
   private static final int pANEL_BACKGROUND = -401337324;
   private static final int pANEL_BORDER = -16119286;
   private final Screen parent;
   private final List<LegacyClickGuiScreen.CategoryPanel> panels = new ArrayList<>();
   private final Animation openAnimation = new Animation(0.0);
   private final Matrix3x2f transform = new Matrix3x2f();
   private GuiScaleInfo workspace;
   private TextField searchField;
   private LegacyClickGuiScreen.TextButton settingsButton;
   private LegacyClickGuiScreen.TextButton resetLayoutButton;
   private LegacyClickGuiScreen.LegacyToggle legacyToggle;
   private String searchQuery = "";
   private String menuKeyName = "";
   private int footerX;
   private int footerWidth;
   private long legacyDisabledAtNanos;
   private LegacyClickGuiScreen.CategoryPanel draggedPanel;
   private int dragOffsetX;
   private int dragOffsetY;
   private boolean closing;
   private boolean opened;
   private boolean preferencesDirty;

   public static Screen create(Screen var0) {
      return legacyPrefs().enabled ? new LegacyClickGuiScreen(var0) : new ClickGuiScreen(var0);
   }

   public static boolean isClickGuiScreen(Screen var0) {
      return var0 instanceof ClickGuiScreen || var0 instanceof LegacyClickGuiScreen;
   }

   private static PanelLayoutConfig legacyPrefs() {
      return LarpClient.getInstance().getConfigManager().getPreferences().legacyClickGui;
   }

   public LegacyClickGuiScreen(Screen var1) {
      super("Larp client");
      this.parent = var1;
   }

   static int panelWidth(int var0, int var1) {
      int var2 = Math.max(1, var1);
      return Math.clamp((var0 - 16 - (var2 - 1) * 6) / var2, 90, 122);
   }

   static int[] loadPanelPositions(int var0, int var1, int var2) {
      int var3 = Math.max(1, (var2 - 16 + 6) / (var1 + 6));
      return new int[]{8 + var0 % var3 * (var1 + 6), 36 + var0 / var3 * 160};
   }

   static int[] clampPanelPosition(int var0, int var1, int var2, int var3, int var4) {
      return new int[]{Math.clamp(var0, 24 - var2, Math.max(24 - var2, var3 - 24)), Math.clamp(var1, 0, Math.max(0, var4 - 20))};
   }

   protected void init() {
      this.workspace = this.workspace();
      this.transform.set(this.workspace.transform());
      this.closing = (boolean)0;
      if (!this.opened) {
         this.opened = (boolean)1;
         this.openAnimation.snapTo(0.0);
         GuiSounds.play(GuiSounds.Sound.OPEN);
      }

      this.openAnimation.animateTo(1.0, this.animationDuration());
      if (this.panels.isEmpty()) {
         this.createPanels();
      }

      this.loadPanelPositions();
      String var1 = this.searchField == null ? null : this.searchField.text;
      this.searchField = new TextField("Search modules...", var1x -> {
         this.searchQuery = var1x;

         for (LegacyClickGuiScreen.CategoryPanel var3 : this.panels) {
            var3.refreshEntries();
            var3.scroll = 0;
         }
      }, 64);
      this.searchField.icon = "search";
      this.searchField.text = var1 == null ? this.searchQuery : var1;
      this.settingsButton = new LegacyClickGuiScreen.TextButton(() -> "Settings", this::openSettings, true);
      this.resetLayoutButton = new LegacyClickGuiScreen.TextButton(() -> "Reset layout", this::resetLayout, true);
      this.legacyToggle = new LegacyClickGuiScreen.LegacyToggle(this::disableLegacy);
      this.layout();
      LarpLog.debug("GUI", "Legacy ClickGUI opened, {} panels, workspace {}x{}", this.panels.size(), this.workspace.width(), this.workspace.height());
   }

   private GuiScaleInfo workspace() {
      return GuiScaleInfo.from(this.minecraft.getWindow(), LarpClient.getInstance().getConfigManager().getPreferences().clickGuiScale);
   }

   private int animationDuration() {
      return LarpClient.getInstance().getConfigManager().getPreferences().clickGuiDuration;
   }

   private void createPanels() {
      this.panels.clear();

      for (ModuleCategory var4 : ModuleCategory.values()) {
         if (var4 != ModuleCategory.fAVORITES) {
            this.panels.add(new LegacyClickGuiScreen.CategoryPanel(var4));
         }
      }

      this.panels.add(new LegacyClickGuiScreen.CategoryPanel(ModuleCategory.fAVORITES));
   }

   private void loadPanelPositions() {
      ArrayList var1 = new ArrayList<>(this.panels);
      var1.sort(Comparator.comparingInt(var0 -> var0.order));
      int var2 = this.workspace().width();
      int var3 = panelWidth(var2, var1.size());

      for (int var4 = 0; var4 < var1.size(); var4++) {
         LegacyClickGuiScreen.CategoryPanel var5 = (LegacyClickGuiScreen.CategoryPanel)var1.get(var4);
         PanelLayoutConfig.PanelPosition var6 = legacyPrefs().getPanel(var5.category.name());
         int[] var7 = var6 == null ? loadPanelPositions(var4, var3, var2) : new int[]{var6.xPos(), var6.yPos()};
         var5.x = var7[0];
         var5.y = var7[1];
         var5.open = (boolean)(var6 != null && !var6.open() ? 0 : 1);
         var5.refreshEntries();
      }
   }

   private void resetLayout() {
      legacyPrefs().clearPanels();
      this.loadPanelPositions();
      this.preferencesDirty = (boolean)1;
      this.savePreferences();
      GuiSounds.play(GuiSounds.Sound.CLICK);
      LarpLog.info("GUI", "Legacy ClickGUI panel layout reset to the default row");
   }

   private void disableLegacy() {
      if (legacyPrefs().enabled) {
         legacyPrefs().enabled = (boolean)0;
         this.legacyDisabledAtNanos = System.nanoTime();
         this.preferencesDirty = (boolean)1;
         this.savePreferences();
         GuiSounds.play(GuiSounds.Sound.SWITCH_OFF);
         LarpLog.info("GUI", "Legacy ClickGUI disabled from the footer switch");
      }
   }

   private void openSettings() {
      this.workspace();
      this.minecraft.gui.setScreen(ClickGuiScreen.openSettings(this, "Interface"));
   }

   private void loadPanelPositions(Module var1) {
      this.workspace();
      UiState.category = ModuleCategory.categorize(var1).name();
      UiState.clickGui = new UiState.ClickGuiState("Modules", var1.getName(), "", 0, 0);
      this.minecraft.gui.setScreen(new ClickGuiScreen(this));
   }

   private void savePanelPosition(LegacyClickGuiScreen.CategoryPanel var1) {
      try {
         legacyPrefs().setPanel(var1.category.name(), new PanelLayoutConfig.PanelPosition(var1.x, var1.y, var1.open));
      } catch (IllegalArgumentException var3) {
         LarpLog.warn("GUI", "Legacy panel {} place not saved: {}", var1.category.name(), var3.getMessage());
      }

      this.preferencesDirty = (boolean)1;
   }

   private void savePreferences() {
      if (this.preferencesDirty) {
         LarpClient.getInstance().getConfigManager().queuePreferencesWrite();
         this.preferencesDirty = (boolean)0;
      }
   }

   public void onClose() {
      if (!this.closing) {
         this.closing = (boolean)1;
         this.workspace();
         GuiSounds.play(GuiSounds.Sound.CLOSE);
         this.minecraft.gui.setScreen(this.parent);
      }
   }

   public void removed() {
      this.workspace();
      this.savePreferences();
      LarpClient.getInstance().getConfigManager().saveActiveProfile();
      LarpLog.debug("GUI", "Legacy ClickGUI closed; preference and profile saves queued");
   }

   public void tick() {
      int var1 = this.legacyDisabledAtNanos != 0L && System.nanoTime() - this.legacyDisabledAtNanos < 100000000L ? 1 : 0;
      if (!this.closing && var1 == 0 && !legacyPrefs().enabled) {
         this.closing = (boolean)1;
         LarpLog.info("GUI", "Legacy ClickGUI turned off; opening the workspace ClickGUI");
         this.minecraft.gui.setScreen(new ClickGuiScreen(this.parent));
      } else {
         for (LegacyClickGuiScreen.CategoryPanel var3 : this.panels) {
            for (LegacyClickGuiScreen.ModuleEntry var5 : var3.entries) {
               var5.tick();
            }
         }
      }
   }

   private void workspace() {
      if (this.focusedWidget != null) {
         this.focusedWidget.blur();
      }

      this.focusedWidget = this.draggedWidget = null;
   }

   private void layout() {
      GuiScaleInfo var1 = this.workspace();
      if (!var1.equals(this.workspace) && this.draggedPanel == null && this.draggedWidget == null) {
         this.workspace = var1;
      }

      int var2 = panelWidth(this.workspace.width(), this.panels.size());
      this.searchField.bounds(this.workspace.width() / 2 - 95, 8, 190);
      this.searchField.height = 20;
      this.settingsButton.bounds(this.searchField.x + this.searchField.width + 6, 8, 58);
      this.settingsButton.height = 20;
      this.resetLayoutButton.bounds(this.settingsButton.x + this.settingsButton.width + 4, 8, 72);
      this.resetLayoutButton.height = 20;
      this.menuKeyName = BindCodes.displayName(LarpClient.getInstance().getGuiKey());
      this.footerWidth = FontRenderer.getTextWidth(this.menuKeyName, FontRenderer.Face.SEMIBOLD)
         + FontRenderer.getTextWidth(" = menu", FontRenderer.Face.SEMIBOLD)
         + 22;
      int var3 = this.legacyToggle.preferredWidth();
      this.footerX = (this.workspace.width() - this.footerWidth - 6 - var3) / 2;
      this.legacyToggle.bounds(this.footerX + this.footerWidth + 6, this.footerY(), var3);
      this.legacyToggle.height = 18;
      this.widgets.clear();
      this.widgets.add(this.searchField);
      this.widgets.add(this.settingsButton);
      this.widgets.add(this.resetLayoutButton);
      this.widgets.add(this.legacyToggle);

      for (LegacyClickGuiScreen.CategoryPanel var5 : this.panels) {
         int[] var6 = clampPanelPosition(var5.x, var5.y, var5.width, this.workspace.width(), this.workspace.height());
         if (var5 != this.draggedPanel) {
            var5.x = var6[0];
            var5.y = var6[1];
         }

         var5.layout(var2, this.workspace.height());
         if (var5.hasEntries()) {
            for (LegacyClickGuiScreen.ModuleEntry var8 : var5.entries) {
               if (var8.expanded) {
                  for (Widget var10 : var8.widgets) {
                     if (var10.visible) {
                        this.widgets.add(var10);
                     }
                  }
               }
            }
         }
      }
   }

   private Vector2f toScreenSpace(double var1, double var3) {
      return new Matrix3x2f(this.transform).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.minecraft.level == null) {
         super.extractBackground(var1, var2, var3, var4);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = PerfProfiler.startTimer();
      this.layout();
      ClientPreferences var7 = LarpClient.getInstance().getConfigManager().getPreferences();
      double var8 = this.openAnimation.animateTo(this.closing ? 0.0 : 1.0, var7.clickGuiDuration);
      this.transform
         .set(this.workspace.transform())
         .mul(ToastAnimation.transform(var7.clickGuiAnimation, var8, this.workspace.width() / 2.0, this.workspace.height() / 2.0));
      if (var7.clickGuiAnimation.equals("Slide")) {
         this.transform.translate(0.0F, (float)((1.0 - var8) * 8.0));
      }

      Vector2f var10 = this.toScreenSpace(var2, var3);
      var2 = Math.round(var10.x);
      var3 = Math.round(var10.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.transform);

      try (AlphaScope var11 = AlphaScope.push(var8)) {
         int var12 = legacyPrefs().color;
         this.searchField.render(var1, var2, var3);
         this.settingsButton.render(var1, var2, var3);
         this.resetLayoutButton.render(var1, var2, var3);
         LegacyClickGuiScreen.CategoryPanel var13 = this.panelAt(var2, var3);
         this.legacyToggle.render(var1, var13 == null ? var2 : -1, var3);

         for (LegacyClickGuiScreen.CategoryPanel var15 : this.panels) {
            if (var15.hasEntries()) {
               var15.render(var1, var15 == var13 ? var2 : -1, var3, var12);
            }
         }

         if (this.focusedWidget instanceof SettingRowWidget var25 && var25.isPopupOpen()) {
            var25.renderPopup(var1, var2, var3);
         }

         String var26 = var13 != null
            ? var13.tooltipAt(var2, var3)
            : (this.legacyToggle.contains(var2, var3) ? "Turn off to switch back to the workspace ClickGUI" : null);
         this.savePanelPosition(var1, var26, var12);
      } finally {
         var1.pose().popMatrix();
      }

      PerfProfiler.endTimer("Legacy ClickGUI", var5);
   }

   private int footerY() {
      return this.workspace.height() - 46 + 20;
   }

   private void savePanelPosition(GuiGraphicsExtractor var1, String var2, int var3) {
      String var4 = var2 != null && !var2.isBlank() ? var2 : "Left click toggles · Right click opens settings · Middle click stars · Drag a title to move it";
      var4 = FontRenderer.getTextWidth(var4, this.workspace.width() - 40);
      int var5 = FontRenderer.drawDefaultText(var4);
      int var6 = this.footerY();
      FontRenderer.drawText(var1, var4, (this.workspace.width() - var5) / 2, var6 - 13, Theme.textSecondary);
      FontRenderer.Face var7 = FontRenderer.Face.SEMIBOLD;
      int var8 = this.footerX;
      int var9 = this.footerWidth;
      DrawUtil.fillRoundedRect(var1, var8 - 1, var6 - 1, var9 + 2, 20.0F, 10.0F, var3 | 0xFF000000);
      DrawUtil.fillRoundedRect(var1, var8, var6, var9, 18.0F, 9.0F, -268106488);
      FontRenderer.drawTextWithFace(var1, this.menuKeyName, var8 + 11, var6 + 5, ThemePalette.readableAccent(var3, -16448248), var7);
      FontRenderer.drawTextWithFace(var1, " = menu", var8 + 11 + FontRenderer.getTextWidth(this.menuKeyName, var7), var6 + 5, -1, var7);
   }

   private LegacyClickGuiScreen.CategoryPanel panelAt(double var1, double var3) {
      for (int var5 = this.panels.size() - 1; var5 >= 0; var5--) {
         if (this.panels.get(var5).hasEntries() && this.panels.get(var5).containsPoint(var1, var3)) {
            return this.panels.get(var5);
         }
      }

      return null;
   }

   private void createPanels(LegacyClickGuiScreen.CategoryPanel var1) {
      this.panels.remove(var1);
      this.panels.add(var1);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.closing) {
         return true;
      }

      Vector2f var3 = this.toScreenSpace(var1.x(), var1.y());
      double var4 = var3.x;
      double var6 = var3.y;
      int var8 = KeyCodes.mouseButtonOf(var1);
      this.layout();
      if (this.focusedWidget != null && this.focusedWidget.captureMouse(var8)) {
         return true;
      }

      if (var8 > 1 && LarpClient.getInstance().getGuiKey() == BindCodes.toMouseBind(var8)) {
         this.onClose();
         return true;
      }

      if (this.focusedWidget instanceof SettingRowWidget var9 && var9.containsPopupPoint(var4, var6)) {
         if (var9.clickPopup(var4, var6, var8)) {
            this.draggedWidget = var9;
         }

         return true;
      } else {
         LegacyClickGuiScreen.CategoryPanel var12 = this.panelAt(var4, var6);
         if (var12 != null) {
            this.createPanels(var12);
            return var12.click(var4, var6, var8);
         }

         for (Widget var11 : List.of(this.searchField, this.settingsButton, this.resetLayoutButton, this.legacyToggle)) {
            if (var11.contains(var4, var6)) {
               this.focus(var11);
               var11.click(var4, var6, var8);
               return true;
            }
         }

         this.workspace();
         return false;
      }
   }

   private void focus(Widget var1) {
      if (this.focusedWidget != null && this.focusedWidget != var1) {
         this.focusedWidget.blur();
      }

      this.focusedWidget = var1;
      var1.focused = (boolean)1;
      var1.focusVisible = (boolean)0;
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.closing) {
         return true;
      } else {
         Vector2f var6 = this.toScreenSpace(var1.x(), var1.y());
         if (this.draggedPanel != null) {
            int[] var7 = clampPanelPosition(
               Math.round(var6.x) - this.dragOffsetX,
               Math.round(var6.y) - this.dragOffsetY,
               this.draggedPanel.width,
               this.workspace.width(),
               this.workspace.height()
            );
            this.draggedPanel.x = var7[0];
            this.draggedPanel.y = var7[1];
            return true;
         } else if (this.draggedWidget != null) {
            this.draggedWidget.drag(var6.x, var6.y);
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.draggedPanel != null) {
         this.savePanelPosition(this.draggedPanel);
         this.savePreferences();
         LarpLog.debug("GUI", "Legacy panel {} moved to {}, {}", this.draggedPanel.category.label, this.draggedPanel.x, this.draggedPanel.y);
         this.draggedPanel = null;
      }

      if (this.draggedWidget != null) {
         this.draggedWidget.release();
      }

      this.draggedWidget = null;
      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.closing) {
         return true;
      } else {
         Vector2f var9 = this.toScreenSpace(var1, var3);
         if (this.focusedWidget instanceof SettingRowWidget var10 && var10.containsPopupPoint(var9.x, var9.y)) {
            var10.shortcutBadgeWidth(var7);
            return true;
         } else {
            LegacyClickGuiScreen.CategoryPanel var12 = this.panelAt(var9.x, var9.y);
            if (var12 == null) {
               return false;
            }

            var12.scroll = var12.scroll - (int)Math.round(var7 * 20.0);
            this.layout();
            return true;
         }
      }
   }

   public boolean isTextInputFocused() {
      return ClickGuiScreen.inspectorBottom(this.focusedWidget);
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.closing) {
         return true;
      } else {
         return ClickGuiScreen.switchPage(KeyCodes.keyOf(var1), this.focusedWidget, null, this::onClose) ? true : super.keyPressed(var1);
      }
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.closing) {
         return true;
      } else if (super.charTyped(var1)) {
         return true;
      } else if ((this.focusedWidget == null || this.focusedWidget == this.searchField) && Character.isLetterOrDigit(var1.codepoint())) {
         this.focus(this.searchField);
         return this.searchField.character(var1.codepoint());
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(Widget var1) {
      for (LegacyClickGuiScreen.CategoryPanel var3 : this.panels) {
         for (LegacyClickGuiScreen.ModuleEntry var5 : var3.entries) {
            if (var5.expanded && var5.widgets.contains(var1)) {
               if (var1.y < var3.bodyTop()) {
                  var3.scroll = var3.scroll - (var3.bodyTop() - var1.y);
               } else if (var1.y + var1.height > var3.bottom) {
                  var3.scroll = var3.scroll + (var1.y + var1.height - var3.bottom);
               }

               this.layout();
               return;
            }
         }
      }
   }

   private final class CategoryPanel {
      final ModuleCategory category;
      final Map<Module, LegacyClickGuiScreen.ModuleEntry> entriesByModule = new IdentityHashMap<>();
      final List<LegacyClickGuiScreen.ModuleEntry> entries = new ArrayList<>();
      final Animation widthAnimation = new Animation(-1.0);
      final Animation hoverOffsetAnimation = new Animation(0.0);
      final Animation hoverFadeAnimation = new Animation(0.0);
      final int order;
      int x;
      int y;
      int width = 90;
      int scroll;
      int bottom;
      int contentHeight;
      boolean open = (boolean)1;

      CategoryPanel(ModuleCategory var2) {
         this.category = var2;
         this.order = LegacyClickGuiScreen.this.panels.size();
      }

      void refreshEntries() {
         this.entries.clear();
         List var1 = LarpClient.getInstance().getModuleManager().getVisibleModules();
         ClientPreferences var2 = LarpClient.getInstance().getConfigManager().getPreferences();

         for (Module var4 : this.category.filterModules(var1, "", var2)) {
            if (LegacyClickGuiScreen.this.searchQuery.isBlank() || ModuleSearch.rankModule(var4, LegacyClickGuiScreen.this.searchQuery) != Integer.MAX_VALUE) {
               this.entries.add(this.entriesByModule.computeIfAbsent(var4, var1x -> LegacyClickGuiScreen.this.new ModuleEntry(var1x)));
            }
         }
      }

      boolean hasEntries() {
         return !this.entries.isEmpty();
      }

      int bodyTop() {
         return this.y + 20;
      }

      boolean containsPoint(double var1, double var3) {
         return DrawUtil.isHovered(var1, var3, this.x, this.y, this.width, 20 + (this.open ? this.bottom - this.bodyTop() : 0));
      }

      void layout(int var1, int var2) {
         boolean var3 = this.entries.stream().anyMatch(var0 -> var0.expanded);
         int var4 = var3 ? Math.max(var1, 160) : var1;
         if (this.widthAnimation.getValue() < 0.0) {
            this.widthAnimation.snapTo(var4);
         }

         this.width = (int)Math.round(this.widthAnimation.animateTo(var4, 200));
         this.contentHeight = 0;

         for (LegacyClickGuiScreen.ModuleEntry var6 : this.entries) {
            for (Widget var8 : var6.widgets) {
               var8.visible = (boolean)(this.open && var6.expanded && var6.isWidgetVisible(var8) ? 1 : 0);
            }

            this.contentHeight = this.contentHeight + 14 + (var6.expanded ? var6.settingsHeight() : 0);
         }

         int var12 = Math.max(14, var2 - 46 - this.bodyTop());
         this.bottom = this.bodyTop() + (this.open ? Math.min(this.contentHeight, var12) : 0);
         this.scroll = Math.clamp(this.scroll, 0, Math.max(0, this.contentHeight - (this.bottom - this.bodyTop())));
         int var13 = this.bodyTop() - this.scroll;

         for (LegacyClickGuiScreen.ModuleEntry var16 : this.entries) {
            var16.y = var13;
            var13 += 14;
            if (var16.expanded) {
               var13 += 2;

               for (Widget var10 : var16.widgets) {
                  if (var10.visible) {
                     var10.bounds(this.x + 4, var13, this.width - 8);
                     if (var10 instanceof SettingRowWidget var11) {
                        var11.layoutPopup(6, LegacyClickGuiScreen.this.workspace.height() - 40);
                     }

                     var13 += var10.height + 2;
                  }
               }

               var13 += 3;
            }
         }
      }

      void render(GuiGraphicsExtractor var1, int var2, int var3, int var4) {
         int var5 = 20 + this.bottom - this.bodyTop();
         DrawUtil.fillRect(var1, this.x - 1, this.y - 1, this.width + 2, var5 + 2, -16119286);
         DrawUtil.fillRect(var1, this.x, this.y, this.width, var5, -401337324);
         String var6 = FontRenderer.drawText(this.category.label, (int)((this.width - 10) / 1.2F), FontRenderer.Face.MEDIUM);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.x + this.width / 2.0F, this.y + 5);
            var1.pose().scale(1.2F, 1.2F);
            FontRenderer.drawTextWithFace(var1, var6, -FontRenderer.getTextWidth(var6, FontRenderer.Face.MEDIUM) / 2, 0, -1, FontRenderer.Face.MEDIUM);
         } finally {
            var1.pose().popMatrix();
         }

         if (this.open) {
            int var7 = var3 >= this.bodyTop() && var3 < this.bottom ? 1 : 0;
            LegacyClickGuiScreen.ModuleEntry var8 = null;
            if (var7 != 0) {
               for (LegacyClickGuiScreen.ModuleEntry var10 : this.entries) {
                  if (DrawUtil.isHovered(var2, var3, this.x, var10.y, this.width, 14)) {
                     var8 = var10;
                     break;
                  }
               }
            }

            int var29 = this.bodyTop() - this.scroll;
            if (var8 != null) {
               if (this.hoverFadeAnimation.getValue() < 0.05) {
                  this.hoverOffsetAnimation.snapTo(var8.y - var29);
               }

               this.hoverOffsetAnimation.animateTo(var8.y - var29, 160);
            }

            double var30 = this.hoverFadeAnimation.animateTo(var8 == null ? 0.0 : 1.0, 160);
            var1.enableScissor(this.x, this.bodyTop(), this.x + this.width, this.bottom);

            try {
               ClientPreferences var12 = LarpClient.getInstance().getConfigManager().getPreferences();

               for (LegacyClickGuiScreen.ModuleEntry var14 : this.entries) {
                  int var15 = var14.y + 14 + (var14.expanded ? var14.settingsHeight() : 0);
                  if (var15 >= this.bodyTop() && var14.y <= this.bottom && var14.module.isEnabled()) {
                     DrawUtil.fillRect(var1, this.x, var14.y, this.width, 14, var4 | 0xFF000000);
                  }
               }

               if (var30 > 0.01) {
                  DrawUtil.fillRect(
                     var1, this.x, var29 + (int)Math.round(this.hoverOffsetAnimation.getValue()), this.width, 14, ColorUtil.multiplyAlpha(419430399, var30)
                  );
               }

               for (LegacyClickGuiScreen.ModuleEntry var34 : this.entries) {
                  int var36 = var34.y + 14 + (var34.expanded ? var34.settingsHeight() : 0);
                  if (var36 >= this.bodyTop() && var34.y <= this.bottom) {
                     boolean var16 = var34.module.isEnabled();
                     int var17 = var16 ? ThemePalette.controlInk(var4 | 0xFF000000) : -1;
                     String var18 = FontRenderer.getTextWidth(var34.module.getDisplayName(), this.width - 14);
                     FontRenderer.drawText(var1, var18, this.x + (this.width - FontRenderer.drawDefaultText(var18)) / 2, var34.y + 3, var17);
                     if (var12.clickGuiStyle(var34.module.getName()) && this.category != ModuleCategory.fAVORITES) {
                        DrawUtil.fillRect(var1, this.x + 3, var34.y + 5, 3, 3, var16 ? var17 : ColorUtil.withAlpha(var4, 0.9));
                     }

                     if (var34.expanded) {
                        int var19 = var34.settingsHeight();
                        DrawUtil.fillRect(var1, this.x, var34.y + 14, this.width, var19, 1342177280);
                        DrawUtil.fillRect(var1, this.x, var34.y + 14, 1, var19, var4 | 0xFF000000);

                        for (Widget var21 : var34.widgets) {
                           if (var21.visible) {
                              var21.render(var1, var7 != 0 ? var2 : -1, var3);
                           }
                        }
                     }
                  }
               }
            } finally {
               var1.disableScissor();
            }

            int var31 = this.bottom - this.bodyTop();
            if (this.contentHeight > var31 && var31 > 0) {
               int var33 = Math.max(12, var31 * var31 / this.contentHeight);
               int var35 = var31 - var33;
               int var37 = this.bodyTop() + (int)Math.round(var35 * ((double)this.scroll / (this.contentHeight - var31)));
               DrawUtil.fillRect(var1, this.x + this.width - 2, var37, 2, var33, ColorUtil.withAlpha(var4, 0.8));
            }
         }
      }

      String tooltipAt(int var1, int var2) {
         if (var2 < this.bodyTop()) {
            return "Drag to move · Right click folds " + this.category.label;
         }

         for (LegacyClickGuiScreen.ModuleEntry var4 : this.entries) {
            if (DrawUtil.isHovered(var1, var2, this.x, var4.y, this.width, 14)) {
               String var8 = var4.module.getDescription();
               return var8.isBlank() ? var4.module.getDisplayName() : var8;
            }

            if (var4.expanded) {
               for (Widget var7 : var4.widgets) {
                  if (var7.visible && var7.contains(var1, var2)) {
                     return var7 instanceof SettingRowWidget var5 ? var5.tooltip() : "";
                  }
               }
            }
         }

         return "";
      }

      boolean click(double var1, double var3, int var5) {
         if (var3 < this.bodyTop()) {
            if (var5 == 0) {
               LegacyClickGuiScreen.this.workspace();
               LegacyClickGuiScreen.this.draggedPanel = this;
               LegacyClickGuiScreen.this.dragOffsetX = (int)Math.round(var1) - this.x;
               LegacyClickGuiScreen.this.dragOffsetY = (int)Math.round(var3) - this.y;
            } else if (var5 == 1) {
               this.open = (boolean)(!this.open ? 1 : 0);
               LegacyClickGuiScreen.this.savePanelPosition(this);
               LegacyClickGuiScreen.this.savePreferences();
               GuiSounds.play(this.open ? GuiSounds.Sound.SETTINGS_OPEN : GuiSounds.Sound.SETTINGS_CLOSE);
            }

            return true;
         } else if (this.open && !(var3 >= this.bottom)) {
            for (LegacyClickGuiScreen.ModuleEntry var7 : this.entries) {
               if (DrawUtil.isHovered(var1, var3, this.x, var7.y, this.width, 14)) {
                  var7.click(var5);
                  return true;
               }

               if (var7.expanded) {
                  for (Widget var9 : var7.widgets) {
                     if (var9.visible && var9.contains(var1, var3)) {
                        LegacyClickGuiScreen.this.focus(var9);
                        if (var9.click(var1, var3, var5)) {
                           LegacyClickGuiScreen.this.draggedWidget = var9;
                        }

                        return true;
                     }
                  }
               }
            }

            LegacyClickGuiScreen.this.workspace();
            return true;
         } else {
            return true;
         }
      }
   }

   private static final class LegacyToggle extends Widget {
      private static final String lABEL = "Legacy";
      private static final int tRACK_WIDTH = 18;
      private static final int tRACK_HEIGHT = 10;
      private final Runnable onToggle;
      private final Animation toggleAnimation = new Animation(1.0);

      LegacyToggle(Runnable var1) {
         this.onToggle = var1;
         this.height = 18;
      }

      int preferredWidth() {
         return 11 + FontRenderer.getTextWidth("Legacy", FontRenderer.Face.SEMIBOLD) + 6 + 18 + 8;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = this.contains(var2, var3);
         double var5 = this.toggleAnimation.animateTo(LegacyClickGuiScreen.legacyPrefs().enabled ? 1.0 : 0.0, 100);
         DrawUtil.fillRoundedRect(var1, this.x - 1, this.y - 1, this.width + 2, this.height + 2, this.height / 2 + 1, var4 ? -12961216 : -14803422);
         DrawUtil.fillRoundedRect(var1, this.x, this.y, this.width, this.height, this.height / 2, -268106488);
         if (this.focused && this.focusVisible) {
            DrawUtil.drawOutline(var1, this.x, this.y, this.width, this.height, Theme.textPrimary);
         }

         FontRenderer.drawTextWithFace(var1, "Legacy", this.x + 11, this.y + 5, var4 ? -1 : Theme.textSecondary, FontRenderer.Face.SEMIBOLD);
         int var7 = this.x + this.width - 8 - 18;
         int var8 = this.y + (this.height - 10) / 2;
         int var9 = LegacyClickGuiScreen.legacyPrefs().color | 0xFF000000;
         DrawUtil.fillRoundedRect(var1, var7, var8, 18.0F, 10.0F, 5.0F, ColorUtil.lerpColor(-13420994, var9, var5));
         int var10 = var7 + 5 + (int)Math.round(8.0 * var5);
         DrawUtil.fillCircle(var1, var10, var8 + 5, 3, -1);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.onToggle.run();
         }

         return false;
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.onToggle.run();
         return true;
      }
   }

   private final class ModuleEntry {
      final Module module;
      final List<Widget> widgets = new ArrayList<>();
      final Map<Widget, BooleanSupplier> visibilityConditions = new IdentityHashMap<>();
      final Set<String> expandedGroups = new HashSet<>();
      boolean expanded;
      int y;
      String skyEffectKey;

      ModuleEntry(Module var2) {
         this.module = var2;
      }

      boolean isWidgetVisible(Widget var1) {
         BooleanSupplier var2 = this.visibilityConditions.get(var1);
         return var2 == null || var2.getAsBoolean();
      }

      int settingsHeight() {
         int var1 = 5;

         for (Widget var3 : this.widgets) {
            if (var3.visible) {
               var1 += var3.height + 2;
            }
         }

         return var1;
      }

      void click(int var1) {
         switch (var1) {
            case 0:
               this.module.toggle();
               GuiSounds.play(this.module.isEnabled() ? GuiSounds.Sound.SWITCH_ON : GuiSounds.Sound.SWITCH_OFF);
               break;
            case 1:
               this.expanded = (boolean)(!this.expanded ? 1 : 0);
               if (this.expanded) {
                  this.rebuildWidgets();
               } else if (this.widgets.contains(LegacyClickGuiScreen.this.focusedWidget)) {
                  LegacyClickGuiScreen.this.workspace();
               }

               GuiSounds.play(this.expanded ? GuiSounds.Sound.SETTINGS_OPEN : GuiSounds.Sound.SETTINGS_CLOSE);
               break;
            case 2:
               ClientPreferences var2 = LarpClient.getInstance().getConfigManager().getPreferences();
               var2.setModuleFavorite(this.module.getName(), !var2.clickGuiStyle(this.module.getName()));
               LarpClient.getInstance().getConfigManager().queuePreferencesWrite();

               for (LegacyClickGuiScreen.CategoryPanel var4 : LegacyClickGuiScreen.this.panels) {
                  var4.refreshEntries();
               }

               GuiSounds.play(GuiSounds.Sound.CLICK);
         }
      }

      void tick() {
         if (this.expanded
            && this.module instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1
            && !Objects.equals(this.skyEffectKey, var1.getStyleSetting().getValue())) {
            this.rebuildWidgets();
         }
      }

      void rebuildWidgets() {
         if (this.widgets.contains(LegacyClickGuiScreen.this.focusedWidget)) {
            LegacyClickGuiScreen.this.workspace();
         }

         this.widgets.clear();
         this.visibilityConditions.clear();
         this.skyEffectKey = this.module instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 ? var1.getStyleSetting().getValue() : null;
         List var8 = ModuleSettingGroups.groupsFor(this.module);
         Iterator var9 = var8.iterator();

         while (true) {
            ModuleSettingGroups.SettingGroup var3;
            BooleanSupplier var4;
            do {
               if (!var9.hasNext()) {
                  if (!(this.module instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     KeybindSetting var10 = new KeybindSetting(
                        this.module instanceof HUDModule ? "Toggle HUD" : "Bind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        this.module.getKeyBind()
                     );
                     var10.onChange(this.module::setKeyBind);
                     this.addWidget(new SettingRowWidget(var10), null);
                     if (this.module.keybindMode() != null) {
                        this.addWidget(new SettingRowWidget(this.module.keybindMode()), null);
                     }
                  }

                  this.addWidget(new LegacyClickGuiScreen.TextButton(() -> "Reset settings", () -> {
                     for (Setting var2 : this.module.getSettings()) {
                        var2.reset();
                     }

                     if (this.module instanceof FakeSpawnerModule var3x) {
                        var3x.resetToDefaults();
                     }

                     this.rebuildWidgets();
                     GuiSounds.play(GuiSounds.Sound.CLICK);
                  }, false), null);
                  this.addWidget(
                     new LegacyClickGuiScreen.TextButton(() -> "Open full editor", () -> LegacyClickGuiScreen.this.loadPanelPositions(this.module), false),
                     null
                  );
                  return;
               }

               var3 = (ModuleSettingGroups.SettingGroup)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(Setting::isVisible);
               if (!var3.collapsed()) {
                  break;
               }

               String var5 = var3.title().toLowerCase(Locale.ROOT);
               LegacyClickGuiScreen.TextButton var6 = new LegacyClickGuiScreen.TextButton(
                  () -> (this.expandedGroups.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                     if (!this.expandedGroups.remove(var3.title())) {
                        this.expandedGroups.add(var3.title());
                     }

                     this.rebuildWidgets();
                  }, false
               );
               this.addWidget(var6, var4);
            } while (!this.expandedGroups.contains(var3.title()));

            if (var8.size() > 1) {
               this.addWidget(new LegacyClickGuiScreen.SectionHeader(var3.title()), var4);
            }

            for (Setting var12 : var3.settings()) {
               if (!(var12 instanceof NumberSetting var7 && var7.getUpperBound())) {
                  SettingRowWidget var13 = new SettingRowWidget(var12);
                  this.addWidget(var13, var13::isActive);
               }
            }
         }
      }

      private void addWidget(Widget var1, BooleanSupplier var2) {
         this.widgets.add(var1);
         if (var2 != null) {
            this.visibilityConditions.put(var1, var2);
         }
      }
   }

   private static final class SectionHeader extends Widget {
      private final String title;

      SectionHeader(String var1) {
         this.title = var1;
         this.height = 12;
      }

      @Override
      public boolean contains(double var1, double var3) {
         return false;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.visible) {
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(this.title.toUpperCase(Locale.ROOT), this.width - 4, FontRenderer.TextStyle.EYEBROW),
               this.x + 2,
               this.y + 2,
               Theme.textMuted,
               FontRenderer.TextStyle.EYEBROW
            );
         }
      }
   }

   private static final class TextButton extends Widget {
      private final Supplier<String> label;
      private final Runnable action;
      private final boolean bordered;

      TextButton(Supplier<String> var1, Runnable var2, boolean var3) {
         this.label = var1;
         this.action = var2;
         this.bordered = var3;
         this.height = 14;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.visible) {
            boolean var4 = this.contains(var2, var3);
            int var5 = this.focused && this.focusVisible ? 1 : 0;
            if (this.bordered) {
               DrawUtil.fillRect(var1, this.x - 1, this.y - 1, this.width + 2, this.height + 2, -16119286);
               DrawUtil.fillRect(var1, this.x, this.y, this.width, this.height, var4 ? -266330076 : -401337324);
            } else if (var4) {
               DrawUtil.fillRect(var1, this.x, this.y, this.width, this.height, 419430399);
            }

            if (var5 != 0) {
               DrawUtil.drawOutline(var1, this.x, this.y, this.width, this.height, Theme.textPrimary);
            }

            String var6 = FontRenderer.getTextWidth(this.label.get(), this.width - 8);
            FontRenderer.drawText(
               var1,
               var6,
               this.x + (this.width - FontRenderer.drawDefaultText(var6)) / 2,
               this.y + (this.height - 8) / 2,
               !var4 && !this.bordered ? Theme.textSecondary : -1
            );
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            GuiSounds.play(GuiSounds.Sound.CLICK);
            this.action.run();
            return false;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (var1 != 257 && var1 != 335 && var1 != 32) {
            return false;
         }

         this.action.run();
         return true;
      }
   }
}

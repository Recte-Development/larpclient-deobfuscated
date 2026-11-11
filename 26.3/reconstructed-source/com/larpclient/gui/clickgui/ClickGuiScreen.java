package com.larpclient.gui.clickgui;

import com.larpclient.LarpClient;
import com.larpclient.account.AccountSkin;
import com.larpclient.config.ClientPreferences;
import com.larpclient.config.ConfigManager;
import com.larpclient.gui.hud.HudEditorScreen;
import com.larpclient.gui.misc.GuiLayout;
import com.larpclient.gui.misc.GuiScaleInfo;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.misc.MenuPopupHost;
import com.larpclient.gui.misc.UiState;
import com.larpclient.gui.screen.LarpTitleScreen;
import com.larpclient.gui.screen.SettingsScreen;
import com.larpclient.gui.screen.WidgetScreen;
import com.larpclient.gui.theme.AuroraBackdrop;
import com.larpclient.gui.theme.MenuBackground;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.ConfigsPanel;
import com.larpclient.gui.widget.FavoriteButton;
import com.larpclient.gui.widget.MenuButton;
import com.larpclient.gui.widget.SectionLabel;
import com.larpclient.gui.widget.SettingRowWidget;
import com.larpclient.gui.widget.TabPanel;
import com.larpclient.gui.widget.TextField;
import com.larpclient.gui.widget.Widget;
import com.larpclient.hud.HudLayout;
import com.larpclient.hud.ToastAnimation;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.ModuleCategory;
import com.larpclient.module.misc.StreamProofState;
import com.larpclient.module.misc.WorldMenuModule;
import com.larpclient.module.render.ChooseASkyEffectAndAdjustItsAppearanceModule;
import com.larpclient.network.ConnectionStatus;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.social.cosmetic.CosmeticPresetJournal;
import com.larpclient.social.presence.DiscordAvatar;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.math.ChargeRamp;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.PressAnimation;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import org.joml.Matrix3x2f;
import org.joml.Vector2f;

public final class ClickGuiScreen extends WidgetScreen {
   private final Screen parent;
   private final List<MenuButton> navigationButtons = new ArrayList<>();
   private final List<MenuButton> moduleButtons = new ArrayList<>();
   private final Map<MenuButton, Module> moduleByButton = new IdentityHashMap<>();
   private final Map<MenuButton, FavoriteButton> favoriteToggles = new IdentityHashMap<>();
   private final Map<MenuButton, SectionLabel> categoryHeaders = new IdentityHashMap<>();
   private int contentHeight;
   private boolean rebuildPending;
   private boolean preferencesDirty;
   private long rebuildAtNanos;
   private final ModuleSettingsPanel moduleInspector = new ModuleSettingsPanel();
   private final SettingsScreen settingsPanel = new SettingsScreen();
   private ConfigsPanel configsPanel;
   private TabPanel activePanel;
   private GuiLayout layout;
   private GuiScaleInfo workspace;
   private ModuleCategory category;
   private int othersStartIndex;
   private Module selectedModule;
   private String page = "Modules";
   private String searchQuery = "";
   private String statusMessage = "";
   private boolean openDiscoverTab;
   private boolean openSoundPlayed;
   private boolean mouseHeld;
   private static ClickGuiScreen closingInstance;
   private Object closeLevel;
   private ClickGuiCloseEffect closeEffect;
   private int moduleScroll;
   private int navigationScroll;
   private double inspectorProgress;
   private double gridProgress = 1.0;
   private boolean initialized;
   private boolean closing;
   private boolean inspectorClosing;
   private final Animation moduleScrollAnim = new Animation(0.0);
   private final Animation gridAnim = new Animation(1.0);
   private TextField searchField;
   private MenuButton inspectorCloseButton;
   private MenuButton israelModeButton;
   private MenuButton larpPresetButton;
   private MenuButton accountButton;
   private final AccountSkin fallbackAvatar = new AccountSkin();
   private static final DiscordAvatar avatarRenderer = new DiscordAvatar();
   private final Animation openAnim = new Animation(0.0);
   private final Animation inspectorAnim = new Animation(0.0);
   private final Animation navIndicatorAnim = new Animation(0.0);
   private final Animation navIndicatorFade = new Animation(0.0);
   private boolean navIndicatorInitialized;
   private ChargeRamp panelAnimator;
   private final Matrix3x2f transform = new Matrix3x2f();

   public ClickGuiScreen() {
      this(null);
   }

   public ClickGuiScreen(Screen var1) {
      super("Larp client");
      this.parent = var1;
      this.category = LarpClient.getInstance().getConfigManager().getPreferences().moduleFavoritesOpen
         ? ModuleCategory.fAVORITES
         : ModuleCategory.fromName(UiState.category);
   }

   public void openConfigsWithDiscover() {
      this.panelAnimator = new ChargeRamp();
   }

   public static ClickGuiScreen openConfigs(Screen var0, String var1, boolean var2) {
      ClickGuiScreen var3 = new ClickGuiScreen(var0);
      var3.initialized = (boolean)1;
      var3.page = "Configs";
      var3.configsPanel = new ConfigsPanel(var3);
      if (var1 != null) {
         var3.configsPanel.myConfigsView.selectedLocal = var1;
      }

      var3.configsPanel.importing = var2;
      return var3;
   }

   public static ClickGuiScreen openConfigsWithDiscover(Screen var0) {
      ClickGuiScreen var1 = openConfigs(var0, null, false);
      var1.openDiscoverTab = (boolean)1;
      return var1;
   }

   public static ClickGuiScreen openSettings(Screen var0, String var1) {
      ClickGuiScreen var2 = new ClickGuiScreen(var0);
      var2.initialized = (boolean)1;
      var2.page = "Settings";
      var2.settingsPanel.setSection(var1);
      return var2;
   }

   public static ClickGuiScreen openHudSettings(Screen var0, HudLayout.HudElement var1) {
      ClickGuiScreen var2 = openSettings(var0, "HUD");
      var2.settingsPanel.setSelectedHudElement(var1);
      return var2;
   }

   Minecraft getMinecraft() {
      return this.minecraft;
   }

   boolean isPointInside(ConfigsPanel var1) {
      return this.activePanel == var1;
   }

   public void onClose() {
      if (!this.closing) {
         if (this.activePanel != null) {
            this.activePanel.suspend();
         }

         this.moduleInspector.clearFocus();
         GuiSounds.play(GuiSounds.Sound.CLOSE);
         this.closing = (boolean)1;
         this.openAnim.animateTo(0.0, this.getActivePanel());
         if (!WorldMenuModule.releaseScreen(this)) {
            this.closeLevel = this.minecraft.level;
            closingInstance = this;
            this.closeEffect = this.openSettings();
            if (this.parent instanceof LarpTitleScreen && !LarpClient.getInstance().getConfigManager().getPreferences().customMenu) {
               LarpLog.info("GUI", "Custom main menu disabled; returning to the vanilla title screen");
               this.minecraft.gui.setScreen(new TitleScreen());
            } else {
               this.minecraft.gui.setScreen(this.parent);
            }
         }
      }
   }

   private int getActivePanel() {
      return LarpClient.getInstance().getConfigManager().getPreferences().clickGuiDuration;
   }

   private ClickGuiCloseEffect openSettings() {
      ClientPreferences var1 = LarpClient.getInstance().getConfigManager().getPreferences();
      String var2 = var1.clickGuiCloseAnimation;
      if (!var2.equals("Reverse") && var1.motion) {
         ArrayList var3 = new ArrayList();
         float var4 = this.transform.m00();
         Vector2f var5 = this.transform.transformPosition(this.layout.left(), this.layout.top(), new Vector2f());
         var3.add(new ClickGuiCloseEffect.WindowBounds(var5.x, var5.y, this.layout.width() * var4, this.layout.height() * var4, Theme.radiusPill * var4));
         if (this.selectedModule != null && this.activePanel == null && this.layout.detachedInspector() && this.inspectorOpacity() > 0.5) {
            GuiLayout var6 = this.layout.moduleInspector(this.inspectorProgress);
            Vector2f var7 = this.transform.transformPosition(var6.inspectorX(), var6.top(), new Vector2f());
            var3.add(
               new ClickGuiCloseEffect.WindowBounds(var7.x, var7.y, (var6.right() - var6.inspectorX()) * var4, var6.height() * var4, Theme.radiusPill * var4)
            );
         }

         return ClickGuiCloseEffect.create(var2, var3, this.getActivePanel());
      } else {
         return null;
      }
   }

   public static boolean isCloseEffectActive() {
      return closingInstance != null;
   }

   public static void renderCloseEffect(GuiGraphicsExtractor var0, float var1) {
      ClickGuiScreen var2 = closingInstance;
      if (var2 != null) {
         Minecraft var3 = Minecraft.getInstance();
         ClickGuiCloseEffect var4 = var2.closeEffect;
         if (var3.gui.screen() == var2.parent
            && var3.level == var2.closeLevel
            && var3.gui.overlay() == null
            && var2.width == var0.guiWidth()
            && var2.height == var0.guiHeight()
            && var2.workspace.equals(currentWorkspace(var3))
            && (var4 == null ? !(var2.openAnim.getValue() <= 0.0) : !var4.isFinished())) {
            try {
               var0.nextStratum();
               if (var4 != null) {
                  ClientPreferences var5 = LarpClient.getInstance().getConfigManager().getPreferences();

                  try (Theme.ThemeScope var6 = Theme.applyThemeWithAnimation(var5.clickGuiLight, var5.clickGuiOpacity, var5.clickGuiGradient)) {
                     switchPage(var0, var0.guiWidth(), var0.guiHeight(), 1.0 - var4.progress(), var5);
                  }

                  var4.render(var0);
               } else {
                  var2.extractRenderState(var0, -10000, -10000, var1);
               }
            } catch (RuntimeException var11) {
               clearCloseEffect();
               LarpLog.error("GUI", "Window exit rendering failed; input remains released", var11);
            }
         } else {
            if (var4 != null) {
               LarpLog.debug("GUI", "Close effect {}", var4.isFinished() ? "finished" : "stopped by a screen, world or window size change");
            }

            clearCloseEffect();
         }
      }
   }

   private static void clearCloseEffect() {
      if (closingInstance != null && closingInstance.closeEffect != null) {
         closingInstance.closeEffect = null;
         ClickGuiCloseEffect.releaseSnapshot();
      }

      closingInstance = null;
   }

   private static GuiScaleInfo currentWorkspace(Minecraft var0) {
      return GuiScaleInfo.paired(var0.getWindow(), LarpClient.getInstance().getConfigManager().getPreferences().clickGuiScale);
   }

   protected void init() {
      this.workspace = currentWorkspace(this.minecraft);
      this.transform.set(this.workspace.transform());
      clearCloseEffect();
      if (!this.initialized) {
         UiState.ClickGuiState var1 = UiState.clickGui;
         this.page = var1.page();
         this.searchQuery = var1.query();
         this.moduleScroll = var1.moduleScroll();
         this.selectedModule = LarpClient.getInstance().getModuleManager().getByName(var1.module());
         this.selectedModule = ChooseASkyEffectAndAdjustItsAppearanceModule.resolveModule(
            this.selectedModule, LarpClient.getInstance().getModuleManager().getModules(), LarpClient.getInstance().getConfigManager().getPreferences()
         );
         if (this.selectedModule == null && !var1.module().isEmpty()) {
            LarpLog.info("GUI", "Restored unavailable module {} with no selection", var1.module());
         }

         if (this.selectedModule != null && this.searchQuery.isBlank() && this.category != ModuleCategory.fAVORITES) {
            this.category = ModuleCategory.categorize(this.selectedModule);
         }

         this.initialized = (boolean)1;
      }

      if (!ModuleCategory.visibleCategories().contains(this.category)) {
         this.category = ModuleCategory.vISUALS;
      }

      if (this.selectedModule != null
         && !(this.selectedModule instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)
         && StreamProofState.isHidden(this.selectedModule)) {
         this.selectedModule = null;
      }

      this.layout = this.page.equals("Modules")
         ? GuiLayout.calculateModules(this.workspace.width(), this.workspace.height(), this.selectedModule == null ? 0.0 : 1.0)
         : GuiLayout.calculate(this.workspace.width(), this.workspace.height());
      this.closing = (boolean)0;
      this.openAnim.snapTo(this.panelAnimator == null ? 0.0 : 1.0);
      this.openAnim.animateTo(1.0, this.getActivePanel());
      if (!this.openSoundPlayed) {
         this.openSoundPlayed = (boolean)1;
         GuiSounds.play(GuiSounds.Sound.OPEN);
      }

      this.inspectorClosing = (boolean)0;
      this.inspectorAnim.snapTo(this.selectedModule == null ? 0.0 : 1.0);
      this.moduleScrollAnim.snapTo(this.moduleScroll);
      this.switchPage();
      this.moduleScrollAnim.snapTo(this.moduleScroll);
      if (this.selectedModule != null) {
         int var2 = this.moduleInspector.frame == null ? UiState.clickGui.settingScroll() : this.moduleInspector.scrollOffset;
         this.moduleInspector.attach(this, this.layout);
         this.moduleInspector.setModule(this.selectedModule);
         this.moduleInspector.scrollOffset = var2;
         this.inspectorAnim.snapTo(1.0);
      }

      this.selectActivePanel();
      LarpLog.debug(
         "GUI",
         "ClickGUI opened, native {}x{} at GUI scale {}, workspace {}x{} / factor {} / {}",
         this.width,
         this.height,
         this.minecraft.getWindow().getGuiScale(),
         this.workspace.width(),
         this.workspace.height(),
         this.workspace.scale(),
         this.page
      );
   }

   private void selectActivePanel() {
      this.activePanel = switch (this.page) {
         case "Configs" -> {
            if (this.configsPanel == null) {
               this.configsPanel = new ConfigsPanel(this);
            }

            yield this.configsPanel;
         }
         case "Settings" -> this.settingsPanel;
         default -> null;
      };
      if (this.activePanel != null) {
         this.activePanel.attach(this, this.layout);
      }

      if (this.openDiscoverTab && this.activePanel == this.configsPanel) {
         this.openDiscoverTab = (boolean)0;
         this.configsPanel.switchTab("Discover");
      }
   }

   private void switchPage(String var1) {
      if (!this.page.equals(var1)) {
         GuiSounds.play(GuiSounds.Sound.SWITCH_CATEGORY);
      }

      if (this.inspectorClosing) {
         this.selectedModule = null;
         this.inspectorClosing = (boolean)0;
         this.inspectorAnim.snapTo(0.0);
      }

      if (this.activePanel != null) {
         this.activePanel.suspend();
      }

      this.moduleInspector.suspend();
      this.page = var1;
      this.switchPage();
      this.selectActivePanel();
      if (this.activePanel == null && this.selectedModule != null) {
         this.moduleInspector.attach(this, this.layout);
         this.moduleInspector.setModule(this.selectedModule);
      }

      this.saveGuiState();
      LarpLog.debug("GUI", "Destination switched to {}", var1);
   }

   void openHudSettingsPage() {
      this.settingsPanel.setSection("HUD");
      this.settingsPanel.setSelectedHudElement(null);
      this.settingsPanel.scrollOffset = 0;
      this.switchPage("Settings");
   }

   void selectCategory() {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         this.saveGuiState();
         this.minecraft.gui.setScreen(new HudEditorScreen(this));
      } else {
         this.statusMessage = "Join a world to edit the HUD.";
      }
   }

   void renderCloseEffect() {
      if (this.selectedModule != null && !this.inspectorClosing) {
         GuiSounds.play(GuiSounds.Sound.SETTINGS_CLOSE);
         this.inspectorClosing = (boolean)1;
         this.moduleInspector.clearFocus();
         this.inspectorAnim.animateTo(0.0, 270);
      }
   }

   private void finishInspectorClose() {
      if (this.inspectorClosing && this.inspectorAnim.getValue() == 0.0) {
         this.selectedModule = null;
         this.inspectorClosing = (boolean)0;
         this.moduleInspector.suspend();
      }
   }

   private void selectCategory(ModuleCategory var1) {
      boolean var2 = var1.switchTo(this.category, () -> {
         if (this.page.equals("Modules")) {
            GuiSounds.play(GuiSounds.Sound.SWITCH_CATEGORY);
         }

         this.gridAnim.restart(220);
         ClientPreferences var2x = LarpClient.getInstance().getConfigManager().getPreferences();
         if (var2x.moduleFavoritesOpen != (var1 == ModuleCategory.fAVORITES ? 1 : 0)) {
            var2x.moduleFavoritesOpen = (boolean)(var1 == ModuleCategory.fAVORITES ? 1 : 0);
            this.preferencesDirty = (boolean)1;
            this.savePreferences();
         }

         this.category = var1;
         this.moduleScroll = 0;
         this.moduleScrollAnim.snapTo(0.0);
         this.selectedModule = null;
         this.inspectorClosing = (boolean)0;
         this.searchQuery = "";
         this.switchPage("Modules");
      });
      if (!var2 && !this.page.equals("Modules")) {
         this.switchPage("Modules");
      }
   }

   private void switchPage() {
      this.clearWidgets();
      this.navigationButtons.clear();
      this.moduleButtons.clear();
      this.moduleByButton.clear();
      this.favoriteToggles.clear();
      List var1 = ModuleCategory.visibleCategories();
      this.othersStartIndex = var1.size();

      for (ModuleCategory var3 : var1) {
         MenuButton var4 = new MenuButton(var3.label, () -> this.selectCategory(var3));
         var4.clickSound = null;
         var4.icon = var3.icon;
         var4.flat = (boolean)1;
         var4.navItem = (boolean)1;
         this.navigationButtons.add(var4);
         this.widgets.add(var4);
      }

      for (String var5 : new String[]{"Editor", "Configs", "Settings"}) {
         MenuButton var6 = new MenuButton(var5, () -> {
            if (var5.equals("Editor")) {
               this.selectCategory();
            } else {
               this.switchPage(var5);
            }
         });
         var6.clickSound = null;
         var6.icon = var5.equals("Editor") ? "panels-top-left" : (var5.equals("Configs") ? "folder" : "settings");
         var6.flat = (boolean)1;
         var6.navItem = (boolean)1;
         this.navigationButtons.add(var6);
         this.widgets.add(var6);
      }

      this.accountButton = new MenuButton("Account", () -> {
         this.settingsPanel.setSection("Account");
         this.switchPage("Settings");
      });
      this.widgets.add(this.accountButton);
      this.israelModeButton = new MenuButton("Israel mode", this::openConfigs);
      this.israelModeButton.flat = (boolean)1;
      this.widgets.add(this.israelModeButton);
      this.larpPresetButton = new MenuButton("Larp Preset", this::toggleLarpPreset);
      this.larpPresetButton.flat = (boolean)1;
      this.widgets.add(this.larpPresetButton);
      this.searchField = new TextField("Search modules...", var1x -> {
         this.searchQuery = var1x;
         this.moduleScroll = 0;
         this.moduleScrollAnim.snapTo(0.0);
         this.rebuildModuleList();
      }, 256);
      this.searchField
         .setSuggestionProvider(
            var0 -> ModuleSearch.search(
               ChooseASkyEffectAndAdjustItsAppearanceModule.mergeSkyModules(
                  LarpClient.getInstance().getModuleManager().getVisibleModules(), LarpClient.getInstance().getConfigManager().getPreferences()
               ),
               var0
            )
         );
      this.searchField.text = this.searchQuery;
      this.searchField.icon = "search";
      this.widgets.add(this.searchField);
      this.inspectorCloseButton = new MenuButton("", this::renderCloseEffect);
      this.inspectorCloseButton.icon = "x";
      this.inspectorCloseButton.flat = (boolean)1;
      this.widgets.add(this.inspectorCloseButton);
      this.inspectorCloseButton.clickSound = null;
      this.rebuildModuleList();
   }

   private void rebuildModuleList() {
      this.rebuildPending = (boolean)0;
      int var1 = -1;
      int var2 = this.focusedWidget != null && this.focusedWidget.focusVisible ? 1 : 0;
      byte var3 = 0;
      Module var4 = null;

      for (int var5 = 0; var5 < this.moduleButtons.size(); var5++) {
         if (this.focusedWidget == this.moduleButtons.get(var5) || this.focusedWidget == this.favoriteToggles.get(this.moduleButtons.get(var5))) {
            var1 = var5;
            var4 = this.moduleByButton.get(this.moduleButtons.get(var5));
            var3 = (byte)(this.focusedWidget == this.favoriteToggles.get(this.moduleButtons.get(var5)) ? 1 : 0);
         }
      }

      if (this.moduleButtons.contains(this.focusedWidget) || this.favoriteToggles.containsValue(this.focusedWidget)) {
         this.focusedWidget.blur();
         this.focusedWidget = null;
      }

      this.widgets.removeAll(this.moduleButtons);
      this.widgets.removeAll(this.favoriteToggles.values());
      this.moduleButtons.clear();
      this.moduleByButton.clear();
      this.favoriteToggles.clear();
      this.categoryHeaders.clear();
      ClientPreferences var13 = LarpClient.getInstance().getConfigManager().getPreferences();
      String var6 = "";

      for (Module var8 : this.category.filterModules(LarpClient.getInstance().getModuleManager().getVisibleModules(), this.searchQuery, var13)) {
         String var9 = this.category.showsGroupHeaders(this.searchQuery) ? ModuleCategory.groupName(var8) : "";
         int var10 = !var9.equals(var6) ? 1 : 0;
         var6 = var9;
         MenuButton var11 = new MenuButton(var8.getDisplayName(), () -> {
            int var2x = this.selectedModule == null ? 1 : 0;
            int var3x = this.selectedModule != var8 ? 1 : 0;
            if (var3x != 0) {
               GuiSounds.play(GuiSounds.Sound.SETTINGS_OPEN);
            }

            this.inspectorClosing = (boolean)0;
            this.selectedModule = var8;
            if (var2x != 0) {
               this.moduleInspector.attach(this, this.layout.moduleInspector(this.inspectorProgress));
            }

            if (var3x != 0) {
               this.moduleInspector.setModule(var8);
            }

            this.inspectorAnim.animateTo(1.0, 270);
            this.updateLayout();
         });
         var11.clickSound = null;
         var11.moduleCard = var11.showKeybind = (boolean)1;
         var11.secondaryAction = () -> {
            var8.toggle();
            GuiSounds.play(var8.isEnabled() ? GuiSounds.Sound.SWITCH_ON : GuiSounds.Sound.SWITCH_OFF);
         };
         var11.toggleState = var8.isEnabled();
         this.moduleButtons.add(var11);
         this.moduleByButton.put(var11, var8);
         if (var10 != 0 && !var6.isEmpty()) {
            this.categoryHeaders.put(var11, SectionLabel.header(var6));
         }

         FavoriteButton var12 = new FavoriteButton(() -> var13.clickGuiStyle(var8.getName()), () -> {
            var13.setModuleFavorite(var8.getName(), !var13.clickGuiStyle(var8.getName()));
            this.preferencesDirty = (boolean)1;
            this.savePreferences();
            this.rebuildPending = (boolean)1;
            this.rebuildAtNanos = System.nanoTime() + 160000000L;
         });
         this.favoriteToggles.put(var11, var12);
         this.widgets.add(var12);
         this.widgets.add(var11);
      }

      this.updateLayout();
      if (var1 >= 0 && var2 != 0 && !this.moduleButtons.isEmpty()) {
         MenuButton var14 = this.moduleButtons.get(Math.min(var1, this.moduleButtons.size() - 1));

         for (MenuButton var16 : this.moduleButtons) {
            if (this.moduleByButton.get(var16) == var4) {
               var14 = var16;
               break;
            }
         }

         this.focusedWidget = var3 != 0 ? this.favoriteToggles.get(var14) : var14;
         this.focusedWidget.focused = this.focusedWidget.focusVisible = (boolean)1;
         this.focusChanged(this.focusedWidget);
      } else if (var1 >= 0 && var2 != 0) {
         this.focusedWidget = this.searchField;
         this.focusedWidget.focused = this.focusedWidget.focusVisible = (boolean)1;
      }
   }

   private boolean savePreferences() {
      LarpClient.getInstance().getConfigManager().queuePreferencesWrite();
      this.preferencesDirty = (boolean)0;
      return true;
   }

   private void openConfigs() {
      LarpClient var1 = LarpClient.getInstance();
      ConfigManager var2 = var1.getConfigManager();
      CosmeticPresetJournal var3 = var2.getPreferences().israelMode;
      boolean var4 = var2.saveProfileAs(!var3.isApplied(), var1.getModuleManager().getModules());
      if (!var4) {
         this.statusMessage = "Israel mode could not be changed. Click to retry; see the client log.";
         LarpLog.warn("GUI", "Israel mode toggle failed; local restore state retained for recovery");
      } else if (this.statusMessage.startsWith("Israel mode")) {
         this.statusMessage = "";
      }

      if (this.selectedModule != null && this.activePanel == null) {
         this.moduleInspector.rebuildPreservingScroll();
      }
   }

   private void toggleLarpPreset() {
      LarpClient var1 = LarpClient.getInstance();
      ConfigManager var2 = var1.getConfigManager();
      if (!var2.loadProfile(!var2.getPreferences().larpPreset.isApplied(), var1.getModuleManager().getModules())) {
         this.statusMessage = "Larp Preset could not be changed. Click to retry; see the client log.";
         LarpLog.warn("GUI", "Larp Preset toggle failed; local restore state retained for recovery");
      } else if (this.statusMessage.startsWith("Larp Preset")) {
         this.statusMessage = "";
      }

      if (this.selectedModule != null && this.activePanel == null) {
         this.moduleInspector.rebuildPreservingScroll();
      }
   }

   private Vector2f toGuiSpace(double var1, double var3) {
      return new Matrix3x2f(this.transform).invert().transformPosition((float)var1, (float)var3, new Vector2f());
   }

   private MouseButtonEvent transformMouseEvent(MouseButtonEvent var1) {
      Vector2f var2 = this.toGuiSpace(var1.x(), var1.y());
      return new MouseButtonEvent(var2.x, var2.y, var1.buttonInfo());
   }

   private TabPanel getActivePanel() {
      return this.activePanel != null ? this.activePanel : (this.selectedModule != null ? this.moduleInspector : null);
   }

   private boolean isInspectorDocked() {
      return this.activePanel == null && this.selectedModule != null && !this.inspectorClosing && !this.layout.columns();
   }

   private int toGuiSpace() {
      return this.layout.moduleListRight(this.inspectorProgress);
   }

   private double inspectorOpacity() {
      return this.layout.detachedInspector() ? Math.clamp(this.inspectorProgress * 2.0 - 1.0, 0.0, 1.0) : this.inspectorProgress;
   }

   private int inspectorOpacity() {
      return this.layout.detachedInspector() ? this.layout.top() : this.layout.bodyTop();
   }

   private int inspectorBottom() {
      return this.layout.detachedInspector() ? this.layout.top() + this.layout.height() : this.layout.bottom();
   }

   private void updateLayout() {
      ClientPreferences var1 = LarpClient.getInstance().getConfigManager().getPreferences();
      if (!this.mouseHeld) {
         GuiScaleInfo var2 = currentWorkspace(this.minecraft);
         if (!var2.equals(this.workspace)) {
            this.workspace = var2;
            LarpLog.debug(
               "GUI",
               "ClickGUI size {}%: workspace {}x{} / factor {}",
               Math.round(var1.clickGuiScale * 100.0),
               this.workspace.width(),
               this.workspace.height(),
               this.workspace.scale()
            );
         }
      }

      double var14 = this.openAnim.animateTo(this.closing ? 0.0 : 1.0, var1.clickGuiDuration);
      this.inspectorProgress = this.inspectorAnim
         .animateTo(this.page.equals("Modules") && this.selectedModule != null && !this.inspectorClosing ? 1.0 : 0.0, 270);
      this.layout = (this.page.equals("Modules")
            ? GuiLayout.calculateModules(this.workspace.width(), this.workspace.height(), this.inspectorProgress)
            : GuiLayout.calculate(this.workspace.width(), this.workspace.height()))
         .offset(0, var1.clickGuiAnimation.equals("Slide") ? (int)Math.round((1.0 - var14) * 8.0) : 0);
      int var4 = this.layout.navigationPitch(this.navigationButtons.size());
      this.navigationScroll = Math.clamp(this.navigationScroll, 0, this.layout.navigationScrollLimit(this.navigationButtons.size()));
      int var5 = this.layout.navigationTop() - this.navigationScroll;

      for (int var6 = 0; var6 < this.navigationButtons.size(); var6++) {
         if (var6 == this.othersStartIndex) {
            var5 += this.layout.navigationGap();
         }

         MenuButton var7 = this.navigationButtons.get(var6);
         var7.bounds(this.layout.left() + 4, var5, this.layout.navigationRight() - this.layout.left() - 4);
         var7.labelAlpha = 1.0;
         var7.height = var4 - 1;
         var5 += var4;
         var7.selected = this.page.equals("Modules") ? var7.label.equals(this.category.label) : var7.label.equals(this.page);
      }

      this.accountButton.bounds(this.layout.left() + 4, this.layout.accountY(), this.layout.navigationRight() - this.layout.left() - 4);
      this.accountButton.height = 30;
      this.israelModeButton.bounds(this.layout.presetLeft(), this.layout.themeSwitchY(), this.layout.presetWidth());
      this.israelModeButton.height = this.layout.themeSwitchHeight();
      this.israelModeButton.toggleState = LarpClient.getInstance().getConfigManager().getPreferences().israelMode.isApplied();
      this.larpPresetButton.bounds(this.layout.larpPresetLeft(), this.layout.themeSwitchY(), this.layout.presetWidth());
      this.larpPresetButton.height = this.layout.themeSwitchHeight();
      this.larpPresetButton.toggleState = LarpClient.getInstance().getConfigManager().getPreferences().larpPreset.isApplied();
      this.searchField.visible = this.page.equals("Modules");
      this.searchField.bounds(this.layout.contentX(), this.layout.top() + 12, this.layout.right() - this.layout.contentX());
      this.searchField.height = 26;
      this.gridProgress = this.gridAnim.getValue();
      int var16 = Math.max(1, this.layout.bottom() - this.layout.bodyTop());
      int var17 = this.layout.moduleGridColumns();
      int var8 = 0;
      int var9 = 0;
      int var10 = var17 * this.layout.moduleCardWidth() + (var17 - 1) * 6;

      for (MenuButton var12 : this.moduleButtons) {
         SectionLabel var13 = this.categoryHeaders.get(var12);
         if (var13 != null) {
            if (var8 > 0) {
               var8 = 0;
               var9 += 38;
            }

            var13.bounds(this.layout.moduleCardX(0, this.gridProgress), var9, var10);
            var9 += var13.height;
         }

         var12.bounds(this.layout.moduleCardX(var8, this.gridProgress), var9, this.layout.moduleCardWidth());
         if (++var8 == var17) {
            var8 = 0;
            var9 += 38;
         }
      }

      this.contentHeight = var9 + (var8 > 0 ? 38 : 0);
      this.moduleScroll = Math.clamp(this.moduleScroll, 0, Math.max(0, this.contentHeight - var16));
      var5 = this.layout.bodyTop() - (int)Math.round(this.moduleScrollAnim.animateTo(this.moduleScroll, 150));

      for (MenuButton var20 : this.moduleButtons) {
         var20.visible = (boolean)(this.page.equals("Modules") && !this.isInspectorDocked() ? 1 : 0);
         var20.y += var5;
         var20.height = 34;
         this.favoriteToggles.get(var20).attachRow(var20);
         SectionLabel var22 = this.categoryHeaders.get(var20);
         if (var22 != null) {
            var22.visible = var20.visible;
            var22.y += var5;
         }
      }

      this.inspectorCloseButton.visible = (boolean)(this.page.equals("Modules") && this.selectedModule != null ? 1 : 0);
      GuiLayout var19 = this.layout.moduleInspector(this.inspectorProgress);
      this.inspectorCloseButton.bounds(var19.right() - 30, var19.bodyTop() + 1, 22);
      this.inspectorCloseButton.height = 22;
      if (this.activePanel != null) {
         this.activePanel.attach(this.layout);
      } else if (this.selectedModule != null) {
         GuiLayout var21 = this.layout.moduleInspector(this.inspectorProgress);
         this.moduleInspector.setInputRange(var21, this.layout.moduleInspectorClipLeft(this.inspectorProgress), var21.right());
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      long var5 = PerfProfiler.startTimer();
      this.finishInspectorClose();
      this.updateLayout();
      double var7 = this.openAnim.getValue();
      ClientPreferences var9 = LarpClient.getInstance().getConfigManager().getPreferences();
      boolean var10 = WorldMenuModule.isMenuScreen(this);
      PostProcessing.setBlurLevel(var9.clickGuiBlur);
      if (!var10 && this.minecraft.level != null && var9.clickGuiBlurArea.equals("Everywhere")) {
         PostProcessing.requestFullscreenBlur();
      }

      if (!var10) {
         switchPage(var1, this.width, this.height, var7, var9);
      }

      this.transform
         .set(this.workspace.transform())
         .mul(
            ToastAnimation.transform(
               var9.clickGuiAnimation, var7, this.layout.left() + this.layout.width() / 2.0, this.layout.top() + this.layout.height() / 2.0
            )
         );
      if (this.panelAnimator != null) {
         PressAnimation.Transform var11 = this.closing
            ? this.panelAnimator.getPose()
            : this.panelAnimator
               .update(System.nanoTime(), this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive(), MenuPopupHost.isPanoramaAnimated());
         this.transform
            .set(this.workspace.transform())
            .mul(var11.matrix(this.workspace.width() / 2.0, this.workspace.height() / 2.0))
            .mul(
               ToastAnimation.transform(
                  var9.clickGuiAnimation, var7, this.layout.left() + this.layout.width() / 2.0, this.layout.top() + this.layout.height() / 2.0
               )
            );
      }

      Vector2f var117 = this.toGuiSpace(var2, var3);
      var2 = Math.round(var117.x);
      var3 = Math.round(var117.y);
      var1.pose().pushMatrix();
      var1.pose().mul(this.transform);

      try (
         Theme.ThemeScope var12 = Theme.applyThemeWithLook(var9.clickGuiLight, var10 ? 1.0 : var9.clickGuiOpacity, var9.clickGuiGradient, var9.clickGuiStyle());
         FontRenderer.ClickUiScope var13 = FontRenderer.forceClickUiFont();
         AlphaScope var14 = AlphaScope.push(ToastAnimation.alpha(var9.clickGuiAnimation, var7));
      ) {
         if (!var10 && Theme.getLook().accentGlow()) {
            GlassPainter.drawPulsingGlow(var1, this.layout.left(), this.layout.top(), this.layout.width(), this.layout.height());
         }

         GlassPainter.drawWindowBackground(var1, this.layout.left(), this.layout.top(), this.layout.width(), this.layout.height());
         this.renderSidebar(var1, this.getActivePanel() != null && this.getActivePanel().isModalOpen() ? -1 : var2, var3);
         GlassPainter.drawWindowBorder(var1, this.layout.left(), this.layout.top(), this.layout.width(), this.layout.height());
         this.israelModeButton.render(var1, this.getActivePanel() != null && this.getActivePanel().isModalOpen() ? -1 : var2, var3);
         this.larpPresetButton.render(var1, this.getActivePanel() != null && this.getActivePanel().isModalOpen() ? -1 : var2, var3);
         if (this.activePanel != null) {
            this.activePanel.extractRenderState(var1, var2, var3, var4);
         } else {
            this.searchField.render(var1, var2, var3);
            var1.enableScissor(this.layout.moduleClipLeft(), this.layout.bodyTop(), this.toGuiSpace(), this.layout.bottom());

            try (AlphaScope var15 = AlphaScope.push(0.35 + 0.65 * this.gridProgress)) {
               for (SectionLabel var17 : this.categoryHeaders.values()) {
                  if (var17.y + var17.height > this.layout.bodyTop() && var17.y < this.layout.bottom()) {
                     var17.render(var1, -1, var3);
                  }
               }

               for (MenuButton var127 : this.moduleButtons) {
                  Module var18 = this.moduleByButton.get(var127);
                  var127.selected = (boolean)(var18 == this.selectedModule ? 1 : 0);
                  var127.toggleState = var18.isEnabled();
                  var127.subtitle = this.searchQuery.isBlank()
                     ? var18.getDescription()
                     : ModuleCategory.categorize(var18).label + " · " + var18.getDescription();
                  if (var127.y + var127.height > this.layout.bodyTop() && var127.y < this.layout.bottom()) {
                     int var19 = var3 >= this.layout.bodyTop() && var3 < this.layout.bottom() && var2 >= this.layout.contentX() && var2 < this.toGuiSpace()
                        ? var2
                        : -1;
                     var127.render(var1, var19, var3);
                     this.favoriteToggles.get(var127).render(var1, var19, var3);
                  }
               }

               if (this.moduleButtons.isEmpty() && !this.isInspectorDocked()) {
                  String var123 = this.category == ModuleCategory.fAVORITES && this.searchQuery.isBlank() ? "Star a module to add it here." : "No matches";
                  FontRenderer.drawText(
                     var1,
                     FontRenderer.getTextWidth(var123, this.toGuiSpace() - this.layout.contentX() - 16),
                     this.layout.moduleListLeft(this.gridProgress) + 8,
                     this.layout.bodyTop() + 12,
                     Theme.textSecondary
                  );
               }
            } finally {
               var1.disableScissor();
            }

            if (!this.isInspectorDocked()) {
               GlassPainter.drawScrollbar(
                  var1,
                  this.layout.moduleScrollbarX(this.inspectorProgress),
                  this.layout.bodyTop(),
                  Math.max(1, this.layout.bottom() - this.layout.bodyTop()),
                  this.contentHeight,
                  (int)Math.round(this.moduleScrollAnim.getValue())
               );
            }

            if (this.selectedModule != null) {
               GuiLayout var118 = this.layout.moduleInspector(this.inspectorProgress);
               int var124 = this.layout.columns() ? var118.inspectorX() : var118.contentX();
               double var128 = this.inspectorOpacity();
               if (this.layout.detachedInspector()) {
                  try (AlphaScope var133 = AlphaScope.push(var128)) {
                     GlassPainter.drawWindow(var1, var124, var118.top(), var118.right() - var124, var118.height());
                  }
               }

               var1.enableScissor(this.layout.moduleInspectorClipLeft(this.inspectorProgress), this.inspectorOpacity(), var118.right(), this.inspectorBottom());

               try (AlphaScope var134 = AlphaScope.push(var128)) {
                  if (!this.layout.detachedInspector()) {
                     GlassPainter.drawSelectedItem(var1, var124, this.layout.bodyTop(), var118.right() - var124, this.layout.bottom() - this.layout.bodyTop());
                  }

                  this.moduleInspector.extractRenderState(var1, var2, var3, var4);
                  this.inspectorCloseButton.render(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }

               var1.enableScissor(this.layout.moduleInspectorClipLeft(this.inspectorProgress), var118.top() + 12, var118.right(), this.inspectorBottom());

               try (AlphaScope var135 = AlphaScope.push(var128)) {
                  this.moduleInspector.renderPopup(var1, var2, var3);
               } finally {
                  var1.disableScissor();
               }
            }

            for (MenuButton var125 : this.moduleButtons) {
               FavoriteButton var129 = this.favoriteToggles.get(var125);
               boolean var131 = this.accepts(var129, var2, var3);
               if ((this.getActivePanel() == null || !this.getActivePanel().isModalOpen())
                  && var129.visible
                  && (var131 || var129.focused && var129.focusVisible)
                  && var129.y >= this.layout.bodyTop()
                  && var129.y + var129.height <= this.layout.bottom()) {
                  var129.renderTooltip(var1, this.workspace.width(), this.workspace.height());
                  break;
               }
            }
         }

         TabPanel var120 = this.getActivePanel();
         String var126 = toggleLarpPreset(var120, var120 == null ? "" : var120.findOpenPopup(var2, var3));
         String var130 = LarpClient.getInstance().getConfigManager().getPersistenceStatus();
         if (!var130.isBlank()) {
            var126 = var130;
         }

         if (!this.statusMessage.isBlank()) {
            var126 = this.statusMessage;
         }

         if (var126.isBlank() && (this.israelModeButton.contains(var2, var3) || this.israelModeButton.focused && this.israelModeButton.focusVisible)) {
            var126 = "Temporary cosmetic preset. Off restores your previous choices.";
         }

         if (var126.isBlank() && (this.larpPresetButton.contains(var2, var3) || this.larpPresetButton.focused && this.larpPresetButton.focusVisible)) {
            var126 = "Purple cosmetic preset. Off restores your previous choices; your theme stays saved.";
         }

         if (var126.isBlank()) {
            var126 = "Enter opens settings · Space toggles · Tab navigates · Right click resets a setting";
         }

         int var132 = this.layout.profileLeft();
         String var136 = "Profile: " + LarpClient.getInstance().getConfigManager().getActiveProfile();
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(var136, this.layout.profileWidth(), FontRenderer.TextStyle.HELP),
            var132,
            this.layout.profileY(),
            Theme.textSecondary,
            FontRenderer.TextStyle.HELP
         );
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(var126, this.layout.helpWidth(), FontRenderer.TextStyle.HELP),
            this.layout.helpLeft(),
            this.layout.helpY(),
            this.statusMessage.isBlank() ? Theme.textSecondary : -24915,
            FontRenderer.TextStyle.HELP
         );
         double var121 = ToastAnimation.overlayDim(var9.clickGuiAnimation, var7);
         if (var121 > 0.01) {
            RoundedShapes.fillRounded(
               var1, this.layout.left(), this.layout.top(), this.layout.width(), this.layout.height(), Theme.radiusPill, ColorUtil.withAlpha(-1, var121)
            );
         }

         if (var10) {
            WorldMenuModule.removePanel(var1, this, this.layout, var2, var3);
         }
      } finally {
         var1.pose().popMatrix();
      }

      PerfProfiler.endTimer("ClickGUI", var5);
   }

   public double[] getBoundsPixels() {
      float var1 = this.workspace.scale();
      return new double[]{
         this.layout.left() * var1,
         this.layout.top() * var1,
         (this.layout.left() + this.layout.width()) * var1,
         (this.layout.top() + this.layout.height()) * var1
      };
   }

   public Screen renderCloseEffect() {
      return this.parent;
   }

   public boolean openHudSettings() {
      return this.closing && this.openAnim.getValue() <= 0.0;
   }

   public boolean isPointInside(double var1, double var3) {
      Vector2f var5 = this.toGuiSpace(var1, var3);
      if (var5.x >= this.layout.left()
         && var5.x < this.layout.left() + this.layout.width()
         && var5.y >= this.layout.top()
         && var5.y < this.layout.top() + this.layout.height()) {
         return true;
      }

      if (this.selectedModule != null && this.layout.detachedInspector() && this.inspectorOpacity() > 0.2) {
         GuiLayout var6 = this.layout.moduleInspector(this.inspectorProgress);
         if (var5.x >= var6.inspectorX() && var5.x < var6.right() && var5.y >= var6.top() && var5.y < var6.top() + var6.height()) {
            return true;
         }
      }

      return WorldMenuModule.isOverControls(this.layout, var5.x, var5.y);
   }

   public boolean finishInspectorClose() {
      TabPanel var1 = this.getActivePanel();
      return inspectorBottom(this.focusedWidget) || var1 != null && inspectorBottom(var1.addButton());
   }

   static boolean inspectorBottom(Widget var0) {
      return var0 instanceof TextField var2 && var2.focused || var0 instanceof SettingRowWidget var1 && var1.getShortcutKey();
   }

   public void cancelTextInput() {
      TabPanel var1 = this.getActivePanel();
      Widget var2 = var1 == null ? null : var1.addButton();
      if (var2 != null) {
         var2.key(256);
      }

      if (this.focusedWidget != null && this.focusedWidget != var2) {
         this.focusedWidget.key(256);
      }

      this.clearFocus();
   }

   public void clearFocus() {
      TabPanel var1 = this.getActivePanel();
      if (var1 != null) {
         var1.clearFocus();
      }

      if (this.focusedWidget != null) {
         this.focusedWidget.blur();
      }

      this.focusedWidget = this.draggedWidget = null;
   }

   private static void switchPage(GuiGraphicsExtractor var0, int var1, int var2, double var3, ClientPreferences var5) {
      int var6 = ColorUtil.withAlpha(-16777216, var5.clickGuiDim);
      switch (var5.clickGuiBackdrop) {
         case "None":
            break;
         case "Gradient":
            int var11 = ColorUtil.multiplyAlpha(var6, var3);
            int var10 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var6, ColorUtil.withAlpha(Theme.accent, 0.5), 0.35), var3);
            DrawUtil.fillGradient(var0, 0, 0, var1, var2, var11, var10);
            break;
         case "Aurora":
         case "Nebula":
         case "Waves":
            AuroraBackdrop.Style var9 = AuroraBackdrop.Style.valueOf(var5.clickGuiBackdrop.toUpperCase(Locale.ROOT));
            if (!AuroraBackdrop.render(var0, var1, var2, var3, Theme.accent, Theme.accentEnd, var5.clickGuiLight, var5.motion, var9, var5.clickGuiDim)) {
               DrawUtil.fillRect(var0, 0, 0, var1, var2, ColorUtil.multiplyAlpha(var6, var3));
            }
            break;
         default:
            DrawUtil.fillRect(var0, 0, 0, var1, var2, ColorUtil.multiplyAlpha(var6, var3));
      }
   }

   static String toggleLarpPreset(TabPanel var0, String var1) {
      return var0 instanceof ConfigsPanel var2 && !var2.statusMessage.isBlank() ? var2.statusMessage : var1;
   }

   private void renderSidebar(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = this.layout.contentX() - this.layout.left() - 10;
      var1.enableScissor(this.layout.left(), this.layout.top(), this.layout.contentX() - 10, this.layout.navigationTop());

      try {
         var1.blit(
            RenderPipelines.GUI_TEXTURED,
            MenuBackground.resolveLogo(),
            this.layout.left() + 7,
            this.layout.top() + 10,
            0.0F,
            0.0F,
            22,
            22,
            64,
            64,
            64,
            64,
            AlphaScope.applyAlpha(-1)
         );

         try (AlphaScope var5 = AlphaScope.push(1.0)) {
            int var6 = this.layout.left() + 35;
            int var7 = var4 - 39;
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth("Larp Client", var7, FontRenderer.TextStyle.BRAND),
               var6,
               this.layout.top() + 10,
               Theme.textPrimary,
               FontRenderer.TextStyle.BRAND
            );
            String var8 = "Version ";
            int var9 = var6 + FontRenderer.measureWidth(var8, FontRenderer.TextStyle.HELP);
            FontRenderer.drawText(var1, var8, var6, this.layout.top() + 24, Theme.textMuted, FontRenderer.TextStyle.HELP);
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(LarpClient.vERSION, Math.max(1, var6 + var7 - var9), FontRenderer.TextStyle.HELP),
               var9,
               this.layout.top() + 24,
               Theme.readableAccent(var9, Theme.background),
               FontRenderer.TextStyle.HELP
            );
            drawSectionLabel(var1, "FEATURES", this.layout.left() + 12, this.layout.navigationTop() - 12, this.layout.navigationRight());
         }
      } finally {
         var1.disableScissor();
      }

      var1.enableScissor(this.layout.left(), this.layout.navigationTop(), this.layout.contentX() - 10, this.layout.navigationBottom());

      try {
         try (AlphaScope var49 = AlphaScope.push(1.0)) {
            drawSectionLabel(var1, "OTHERS", this.layout.left() + 12, this.navigationButtons.get(this.othersStartIndex).y - 12, this.layout.navigationRight());
         }

         this.renderNavIndicator(var1);

         for (MenuButton var52 : this.navigationButtons) {
            var52.render(var1, var3 >= this.layout.navigationTop() && var3 < this.layout.navigationBottom() ? var2 : -1, var3);
         }
      } finally {
         var1.disableScissor();
      }

      if (this.layout.navigationScrollLimit(this.navigationButtons.size()) > 0) {
         GlassPainter.drawScrollbar(
            var1,
            this.layout.contentX() - 12,
            this.layout.navigationTop(),
            this.layout.navigationBottom() - this.layout.navigationTop(),
            this.navigationButtons.size() * this.layout.navigationPitch(this.navigationButtons.size()) + this.layout.navigationGap(),
            this.navigationScroll
         );
      }

      var1.enableScissor(this.layout.left(), this.layout.accountY(), this.layout.contentX() - 10, this.layout.accountY() + this.accountButton.height);

      try {
         double var51 = this.accountButton.hoverProgress(var2, var3);
         if (var51 > 0.001) {
            DrawUtil.fillRoundedRect(
               var1,
               this.accountButton.x,
               this.accountButton.y,
               this.accountButton.width,
               this.accountButton.height,
               Theme.radiusControl,
               ColorUtil.withAlpha(Theme.textPrimary, var51 * 0.045)
            );
         }

         ConnectionStatus var53 = ConnectionStatus.current();
         if (!avatarRenderer.render(var1, this.layout.left() + 8, this.accountButton.y + 5, 20)) {
            this.fallbackAvatar.drawFace(var1, this.layout.left() + 8, this.accountButton.y + 5, 20);
         }

         DrawUtil.fillCircle(var1, this.layout.left() + 25, this.accountButton.y + 24, 4, Theme.background);
         DrawUtil.fillCircle(var1, this.layout.left() + 25, this.accountButton.y + 24, 3, var53.state().color());

         try (AlphaScope var54 = AlphaScope.push(1.0)) {
            int var55 = Math.max(1, var4 - 40);
            String var10 = var53.account() != null ? var53.account().name() : this.minecraft.getUser().getName();
            FontRenderer.drawTextWithFace(
               var1,
               FontRenderer.drawText(var10, var55, FontRenderer.Face.MEDIUM),
               this.layout.left() + 34,
               this.accountButton.y + 6,
               Theme.textPrimary,
               FontRenderer.Face.MEDIUM
            );
            FontRenderer.drawText(
               var1,
               FontRenderer.trimToWidth(var53.state().label(), var55, FontRenderer.TextStyle.HELP),
               this.layout.left() + 34,
               this.accountButton.y + 18,
               Theme.textSecondary,
               FontRenderer.TextStyle.HELP
            );
         }

         if (this.accountButton.focused && this.accountButton.focusVisible) {
            GlassPainter.drawFocusRing(
               var1, this.accountButton.x, this.accountButton.y, this.accountButton.width, this.accountButton.height, Theme.radiusControl
            );
         }
      } finally {
         var1.disableScissor();
      }
   }

   private static void drawSectionLabel(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4) {
      FontRenderer.drawText(var0, var1, var2, var3, ColorUtil.withAlpha(Theme.textSecondary, 0.8), FontRenderer.TextStyle.EYEBROW);
   }

   private void renderNavIndicator(GuiGraphicsExtractor var1) {
      MenuButton var2 = null;

      for (MenuButton var4 : this.navigationButtons) {
         if (var4.selected) {
            var2 = var4;
            break;
         }
      }

      double var9 = this.navIndicatorFade.animateTo(var2 != null ? 1.0 : 0.0, 180);
      if (var2 != null) {
         int var5 = var2.y - this.layout.navigationTop() + this.navigationScroll;
         if (!this.navIndicatorInitialized) {
            this.navIndicatorAnim.snapTo(var5);
            this.navIndicatorInitialized = (boolean)1;
         }

         this.navIndicatorAnim.animateTo(var5, 200);
      }

      if (this.navIndicatorInitialized && !(var9 < 0.01)) {
         MenuButton var10 = this.navigationButtons.getFirst();
         float var6 = this.layout.navigationTop() - this.navigationScroll + (float)this.navIndicatorAnim.getValue();
         int var7 = Theme.readableAccent(var10.x, Theme.background);
         int var8 = Theme.radiusControl;
         GlassPainter.fillRoundedRect(var1, var10.x, var6, var10.width, var10.height, var8, 9.0F, ColorUtil.withAlpha(var7, 0.22 * var9));
         RoundedShapes.fillRoundedGradientHorizontal(
            var1, var10.x, var6, var10.width, var10.height, var8, ColorUtil.withAlpha(var7, 0.26 * var9), ColorUtil.withAlpha(Theme.accentEnd, 0.08 * var9)
         );
         RoundedShapes.drawRimGradient(
            var1, var10.x, var6, var10.width, var10.height, var8, ColorUtil.withAlpha(var7, 0.35 * var9), ColorUtil.withAlpha(var7, 0.35 * var9)
         );
      }
   }

   @Override
   protected boolean accepts(Widget var1, double var2, double var4) {
      if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.navigationButtons.contains(var1)) {
         return var4 >= this.layout.navigationTop() && var4 < this.layout.navigationBottom();
      } else {
         return !this.moduleButtons.contains(var1) && !this.favoriteToggles.containsValue(var1)
            ? true
            : !this.isInspectorDocked()
               && var4 >= this.layout.bodyTop()
               && var4 < this.layout.bottom()
               && var2 >= this.layout.contentX()
               && var2 < this.toGuiSpace();
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.closing) {
         return true;
      }

      this.mouseHeld = (boolean)1;
      var1 = this.transformMouseEvent(var1);
      this.updateLayout();
      TabPanel var3 = this.getActivePanel();
      Widget var4 = var3 == null ? null : var3.addButton();
      if (var4 != null && var4.captureMouse(KeyCodes.mouseButtonOf(var1))) {
         return true;
      }

      if (KeyCodes.mouseButtonOf(var1) > 1 && LarpClient.getInstance().getGuiKey() == BindCodes.toMouseBind(KeyCodes.mouseButtonOf(var1))) {
         this.onClose();
         return true;
      }

      if (var3 != null && var3.isModalOpen()) {
         return var3.mouseClicked(var1, var2);
      }

      if (WorldMenuModule.isAvailable(this, var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1), this.layout)) {
         if (var3 != null) {
            var3.clearFocus();
         }

         return true;
      } else {
         for (MenuButton var6 : this.navigationButtons) {
            if (this.accepts(var6, var1.x(), var1.y())) {
               if (var6.selected && this.page.equals("Modules")) {
                  return var6.click(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1));
               }

               return super.mouseClicked(var1, var2);
            }
         }

         if (this.inspectorOpacity() > 0.2
            && this.inspectorCloseButton.contains(var1.x(), var1.y())
            && var1.x() < this.layout.moduleInspector(this.inspectorProgress).right()) {
            return super.mouseClicked(var1, var2);
         }

         int var8 = this.activePanel != null
            ? this.layout.containsWorkspace(var1.x(), var1.y())
            : (
               this.inspectorOpacity() > 0.2
                     && (!this.inspectorClosing || this.layout.columns())
                     && var1.x() >= this.layout.moduleInspectorClipLeft(this.inspectorProgress)
                     && var1.x() < this.layout.moduleInspector(this.inspectorProgress).right()
                     && var1.y() >= this.inspectorOpacity()
                     && var1.y() < this.inspectorBottom()
                  ? 1
                  : 0
            );
         if (var3 != null && var8 != 0) {
            if (this.focusedWidget != null) {
               this.focusedWidget.blur();
            }

            this.focusedWidget = this.draggedWidget = null;
            return this.inspectorClosing && this.activePanel == null ? true : var3.mouseClicked(var1, var2);
         } else {
            if (var3 != null) {
               var3.clearFocus();
            }

            return super.mouseClicked(var1, var2);
         }
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.closing) {
         return true;
      } else {
         Vector2f var9 = this.toGuiSpace(var1, var3);
         var1 = var9.x;
         var3 = var9.y;
         this.updateLayout();
         TabPanel var10 = this.getActivePanel();
         if ((var10 == null || !var10.isModalOpen())
            && this.layout.containsNavigation(var1, var3)
            && var3 >= this.layout.navigationTop()
            && var3 < this.layout.navigationBottom()) {
            this.navigationScroll -= (int)(var7 * 28.0);
            this.updateLayout();
            return true;
         } else if (var10 != null
            && !this.inspectorClosing
            && (!this.layout.detachedInspector() || this.inspectorOpacity() > 0.2)
            && var10.mouseScrolled(var1, var3, var5, var7)) {
            return true;
         } else if (this.page.equals("Modules")
            && !this.isInspectorDocked()
            && var1 >= this.layout.contentX()
            && var1 < this.toGuiSpace()
            && var3 >= this.layout.bodyTop()
            && var3 < this.layout.bottom()) {
            this.moduleScroll -= (int)(var7 * 27.0);
            this.updateLayout();
            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   protected void focusChanged(Widget var1) {
      if (this.navigationButtons.contains(var1)) {
         if (var1.y < this.layout.navigationTop()) {
            this.navigationScroll = this.navigationScroll - (this.layout.navigationTop() - var1.y);
         } else if (var1.y + var1.height > this.layout.navigationBottom()) {
            this.navigationScroll = this.navigationScroll + (var1.y + var1.height - this.layout.navigationBottom());
         }

         this.updateLayout();
      }

      if (this.moduleButtons.contains(var1) || this.favoriteToggles.containsValue(var1)) {
         for (Entry var3 : this.favoriteToggles.entrySet()) {
            if (var3.getValue() == var1) {
               var1 = (Widget)var3.getKey();
               break;
            }
         }

         int var4 = this.categoryHeaders.containsKey(var1) ? this.categoryHeaders.get(var1).y : var1.y;
         if (var4 < this.layout.bodyTop()) {
            this.moduleScroll = this.moduleScroll - (this.layout.bodyTop() - var4);
         } else if (var1.y + var1.height > this.layout.bottom()) {
            this.moduleScroll = this.moduleScroll + (var1.y + var1.height - this.layout.bottom());
         }

         this.moduleScrollAnim.snapTo(this.moduleScroll);
         this.updateLayout();
      }
   }

   static boolean switchPage(int var0, Widget var1, Widget var2, Runnable var3) {
      if (var0 != 256) {
         return false;
      }

      if (var1 != null) {
         var1.key(var0);
      }

      if (var2 != null && var2 != var1) {
         var2.key(var0);
      }

      var3.run();
      return true;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.closing) {
         return true;
      }

      TabPanel var2 = this.getActivePanel();
      if (switchPage(KeyCodes.keyOf(var1), var2 == null ? null : var2.addButton(), this.focusedWidget, this::onClose)) {
         return true;
      }

      if (this.searchField != null
         && this.page.equals("Modules")
         && this.focusedWidget == this.searchField
         && (var2 == null || !var2.isModalOpen())
         && KeyCodes.keyOf(var1) == 258
         && (var1.modifiers() & 1) == 0
         && this.searchField.acceptSuggestion()) {
         return true;
      }

      if (KeyCodes.keyOf(var1) != 258 || var2 != null && var2.isModalOpen()) {
         return var2 == null || var2.addButton() == null && !var2.isModalOpen() ? super.keyPressed(var1) : var2.keyPressed(var1);
      }

      ArrayList var3 = new ArrayList();
      this.widgets.stream().filter(var0 -> var0.visible && var0.enabled).forEach(var3::add);
      if (var2 != null && !this.inspectorClosing && (!this.layout.detachedInspector() || this.inspectorOpacity() > 0.2)) {
         var3.addAll(var2.getFocusableWidgets());
      }

      Widget var4 = var2 != null && var2.addButton() != null ? var2.addButton() : this.focusedWidget;
      int var5 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var6 = var4 == null ? (var5 > 0 ? -1 : 0) : var3.indexOf(var4);
      if (this.focusedWidget != null) {
         this.focusedWidget.blur();
      }

      this.focusedWidget = null;
      if (var2 != null) {
         var2.clearFocus();
      }

      if (!var3.isEmpty()) {
         Widget var7 = (Widget)var3.get(Math.floorMod(var6 + var5, var3.size()));
         if (this.widgets.contains(var7)) {
            this.focusedWidget = var7;
            var7.focused = var7.focusVisible = (boolean)1;
            this.focusChanged(var7);
         } else {
            var2.focusWidget(var7);
         }
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.closing) {
         return true;
      }

      TabPanel var2 = this.getActivePanel();
      return var2 != null && var2.addButton() != null ? var2.charTyped(var1) : super.charTyped(var1);
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (!this.closing && !this.inspectorClosing) {
         Vector2f var6 = this.toGuiSpace(var1.x(), var1.y());
         Vector2f var7 = this.toGuiSpace(var1.x() - var2, var1.y() - var4);
         var1 = this.transformMouseEvent(var1);
         var2 = var6.x - var7.x;
         var4 = var6.y - var7.y;
         TabPanel var8 = this.getActivePanel();
         return var8 != null && var8.mouseDragged(var1, var2, var4) || super.mouseDragged(var1, var2, var4);
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      this.mouseHeld = (boolean)0;
      var1 = this.transformMouseEvent(var1);
      if (this.getActivePanel() != null) {
         this.getActivePanel().mouseReleased(var1);
      }

      return super.mouseReleased(var1);
   }

   public void tick() {
      this.finishInspectorClose();
      if (this.rebuildPending && System.nanoTime() >= this.rebuildAtNanos) {
         this.rebuildPending = (boolean)0;
         this.rebuildModuleList();
      }

      if (!this.closing && this.getActivePanel() != null) {
         this.getActivePanel().tick();
      }
   }

   void saveGuiState() {
      if (this.category != ModuleCategory.fAVORITES) {
         UiState.category = this.category.name();
      }

      UiState.clickGui = new UiState.ClickGuiState(
         this.page, this.selectedModule == null ? "" : this.selectedModule.getName(), this.searchQuery, this.moduleScroll, this.moduleInspector.scrollOffset
      );
   }

   public void removed() {
      if (this.panelAnimator != null && !this.closing) {
         this.panelAnimator.finish();
      }

      if (this.activePanel != null) {
         this.activePanel.suspend();
      }

      this.moduleInspector.suspend();
      this.saveGuiState();
      ConfigManager var1 = LarpClient.getInstance().getConfigManager();
      if (this.preferencesDirty) {
         this.savePreferences();
      }

      var1.saveActiveProfile();
      LarpLog.debug("GUI", "ClickGUI closed; atomic preference save queued without blocking input");
   }
}

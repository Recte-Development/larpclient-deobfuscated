package com.larpclient.gui.screen;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.config.ConfigManager;
import com.larpclient.core.BuildInfo;
import com.larpclient.gui.misc.UiState;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.theme.ThemeToggleWidget;
import com.larpclient.gui.widget.ColorSwatchRow;
import com.larpclient.gui.widget.InfoCardWidget;
import com.larpclient.gui.widget.MenuButton;
import com.larpclient.gui.widget.ModeSegmentWidget;
import com.larpclient.gui.widget.SectionLabel;
import com.larpclient.gui.widget.SettingRowWidget;
import com.larpclient.gui.widget.TabPanel;
import com.larpclient.gui.widget.Widget;
import com.larpclient.hud.HudLayout;
import com.larpclient.hud.ToastAnimation;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.StreamProofState;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.update.UpdateConsentKeys;
import com.larpclient.update.UpdateRuntime;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

final class SettingsScreen extends TabPanel {
   private String section = "Interface";
   private boolean syncingControls;
   private final Map<Widget, BooleanSupplier> visibilityConditions = new IdentityHashMap<>();
   private MenuButton primaryUpdateButton;
   private MenuButton secondaryUpdateButton;
   private MenuButton skipUpdateButton;
   private final UpdateConsentKeys consentKeys = new UpdateConsentKeys();
   private final List<SettingsScreen.Card> cards = new ArrayList<>();
   private final Animation scrollAnimation = new Animation(0.0);
   private HudLayout.HudElement selectedHudElement;
   private final Map<MenuButton, HudLayout.HudElement> hudButtons = new LinkedHashMap<>();
   private NumberSetting xSlider;
   private NumberSetting ySlider;
   private NumberSetting scaleSlider;
   private BooleanSetting visibleToggle;
   private ModeSetting alignmentSelect;

   void setSelectedHudElement(HudLayout.HudElement var1) {
      this.selectedHudElement = var1;
   }

   SettingsScreen() {
      super("Interface");
   }

   boolean isHudSection() {
      return this.section.equals("HUD");
   }

   void setSection(String var1) {
      this.section = var1;
   }

   @Override
   protected void build() {
      this.clearWidgets();
      this.cards.clear();
      this.hudButtons.clear();
      this.visibilityConditions.clear();
      this.xSlider = this.ySlider = this.scaleSlider = null;
      this.visibleToggle = null;
      this.alignmentSelect = null;
      this.primaryUpdateButton = this.secondaryUpdateButton = this.skipUpdateButton = null;
      this.title = this.section.equals("Interface") ? "GUI Settings" : this.section;

      for (String var4 : new String[]{"Interface", "HUD", "Account", "Updates"}) {
         this.addWidget(var4.equals("Interface") ? "GUI Settings" : var4, this.section.equals(var4), () -> {
            if (!this.section.equals(var4)) {
               this.section = var4;
               this.scrollOffset = 0;
               this.resetReveal();
               LarpLog.info("GUI", "Settings switched to {}", var4);
               this.build();
            }
         });
      }

      switch (this.section) {
         case "Interface":
            this.buildInterface();
            break;
         case "HUD":
            this.buildHud();
            break;
         case "Account":
            this.buildAccount();
            break;
         case "Updates":
            this.buildUpdates();
            break;
         default:
            throw new IllegalStateException("Unknown settings section");
      }
   }

   private void buildAccount() {
      this.addLabeledValue("Minecraft", () -> this.gui.getMinecraft().getUser().getName());
      this.addLabeledValue("Larp account", () -> "".isBlank() ? "Signed in" : "");
      this.addWidget(new SectionLabel("Discord"));
      ConfigManager var1 = LarpClient.getInstance().getConfigManager();
      BooleanSetting var2 = new BooleanSetting(
         "Discord activity", "Show Larp Client, your version and server on Discord. Needs the Discord desktop app", var1.getPreferences().discordPresence
      );
      var2.onChange(var2x -> {
         if (!var1.setFeatureEnabled(var2x)) {
            var2.setValue(var1.getPreferences().discordPresence);
         }
      });
      this.addWidget(new SettingRowWidget(var2));
   }

   private void buildUpdates() {
      this.consentKeys.begin(this::isKeyDown);
      this.addLabeledValue("Version", () -> BuildInfo.larpVersion);
      this.addLabeledValue("New version", UpdateRuntime::offeredVersion);
      this.visibilityConditions.put(this.widgets.getLast(), () -> !UpdateRuntime.offeredVersion().isEmpty());
      this.addLabeledValue("Status", UpdateRuntime::status);
      ClientPreferences var1 = LarpClient.getInstance().getConfigManager().getPreferences();
      BooleanSetting var2 = new BooleanSetting("Automatic checks", "Check for a new version after startup", var1.updateAutoCheck);
      var2.onChange(var2x -> {
         if (!UpdateRuntime.automatic(var2x)) {
            var2.setValue(var1.updateAutoCheck);
         }
      });
      this.addWidget(new SettingRowWidget(var2));
      Object var3 = new Object();
      this.primaryUpdateButton = this.addUpdateButton(var3, UpdateRuntime::primary);
      this.secondaryUpdateButton = this.addUpdateButton(var3, UpdateRuntime::secondary);
      this.skipUpdateButton = this.addUpdateButton(var3, UpdateRuntime::skip);
      this.skipUpdateButton.label = "Skip this release";
      this.visibilityConditions.put(this.skipUpdateButton, UpdateRuntime::skippable);
      this.setSection();
      this.addDynamicText(UpdateRuntime::statusDetail);
      this.visibilityConditions.put(this.widgets.getLast(), () -> !UpdateRuntime.statusDetail().isEmpty());
      this.addWidget(new SectionLabel("What's new", () -> !UpdateRuntime.releaseNotes().isEmpty()));
      this.addDynamicText(UpdateRuntime::releaseNotes);
      this.visibilityConditions.put(this.widgets.getLast(), () -> !UpdateRuntime.releaseNotes().isEmpty());
   }

   private MenuButton addUpdateButton(Object var1, Runnable var2) {
      MenuButton var3 = new MenuButton("", var2);
      var3.pill = (boolean)1;
      var3.tag = var1;
      var3.height = 24;
      this.addWidget(var3);
      return var3;
   }

   private void setSection() {
      if (this.primaryUpdateButton != null) {
         this.primaryUpdateButton.label = UpdateRuntime.primaryLabel();
         this.primaryUpdateButton.enabled = UpdateRuntime.primaryEnabled();
         this.secondaryUpdateButton.label = UpdateRuntime.secondaryLabel();
         this.secondaryUpdateButton.enabled = UpdateRuntime.secondaryEnabled();
      }
   }

   private boolean isKeyDown(int var1) {
      return KeyCodes.isKeyDown(this.gui.getMinecraft().getWindow(), var1);
   }

   @Override
   protected boolean componentShown(Widget var1) {
      BooleanSupplier var2 = this.visibilityConditions.get(var1);
      return (var2 == null || var2.getAsBoolean()) && super.componentShown(var1);
   }

   private void applyAndSave(Runnable var1) {
      var1.run();
      LarpClient.getInstance().getConfigManager().queuePreferencesWrite();
   }

   private void addCard(String var1, String var2, Widget... var3) {
      InfoCardWidget var4 = new InfoCardWidget(var1, var2);
      this.addWidget(var4);

      for (Widget var8 : var3) {
         this.addWidget(var8);
      }

      this.cards.add(new SettingsScreen.Card(var4, List.of(var3)));
   }

   private void buildInterface() {
      ClientPreferences var1 = LarpClient.getInstance().getConfigManager().getPreferences();
      this.addCard(
         "Main color",
         "Choose the main GUI theme.",
         new ThemeToggleWidget(() -> var1.clickGuiLight, var2x -> this.applyAndSave(() -> var1.clickGuiLight = var2x))
      );
      ColorSetting var2 = new ColorSetting("Primary", "Primary theme color", Theme.accent);
      var2.onChange(var0 -> Theme.accent = var0);
      ColorSetting var3 = new ColorSetting("Secondary", "Secondary theme color", Theme.accentEnd);
      var3.onChange(var0 -> Theme.accentEnd = var0);
      MenuButton var4 = new MenuButton("Reset palette", () -> {
         Theme.accent = -1381654;
         Theme.accentEnd = -6250336;
         this.build();
      });
      var4.centeredText = (boolean)1;
      BooleanSetting var5 = new BooleanSetting("Animation gradient", "Animate the GUI accent between primary and secondary", var1.clickGuiGradient);
      var5.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiGradient = var2x));
      this.addCard("Accent color", "Accent colors of the GUI.", new ColorSwatchRow(var1x -> {
         Theme.accent = var1x;
         var2.setValue(var1x);
      }), new SettingRowWidget(var2), new SettingRowWidget(var3), new SettingRowWidget(var5), var4);
      ModeSetting var6 = new ModeSetting("Open", "How the GUI appears", var1.clickGuiAnimation, ToastAnimation.sTYLES.toArray(String[]::new));
      var6.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiAnimation = var2x));
      ModeSetting var7 = new ModeSetting(
         "Close",
         "How the GUI leaves. Reverse plays the opening backwards; the others break the window apart. Reduced motion always uses Reverse",
         var1.clickGuiCloseAnimation,
         ClientPreferences.cLOSE_ANIMATIONS.toArray(String[]::new)
      );
      var7.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiCloseAnimation = var2x));
      NumberSetting var8 = new NumberSetting(
         "Speed", "Opening and closing duration in milliseconds. Breaking effects take about twice as long", var1.clickGuiDuration, 100.0, 1000.0, 10.0
      );
      var8.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiDuration = var2x.intValue()));
      this.addCard("Animation", "GUI open/close animations.", new SettingRowWidget(var6), new SettingRowWidget(var7), new SettingRowWidget(var8));
      NumberSetting var9 = new NumberSetting("Opacity", "Window and navigation opacity", var1.clickGuiOpacity * 100.0, 0.0, 100.0, 1.0);
      var9.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiOpacity = var2x / 100.0));
      this.addCard("Transparent", "Make your GUI transparent.", new SettingRowWidget(var9));
      ModeSetting var10 = new ModeSetting(
         "Style",
         "What the GUI draws behind its window. Dim darkens the world without light",
         var1.clickGuiBackdrop,
         ClientPreferences.bACKDROPS.toArray(String[]::new)
      );
      var10.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiBackdrop = var2x));
      NumberSetting var11 = new NumberSetting(
         "Dim", "How much the backdrop darkens the world, in percent. Not used with None", var1.clickGuiDim * 100.0, 0.0, 90.0, 1.0
      );
      var11.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiDim = var2x / 100.0));
      NumberSetting var12 = new NumberSetting(
         "Blur",
         "Blur behind the glass while in a world: 0 is none, 2 the usual strength, 4 the heaviest. Needs Blur quality on in the Performance module",
         var1.clickGuiBlur,
         0.0,
         4.0,
         1.0
      );
      var12.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiBlur = var2x.intValue()));
      ModeSetting var13 = new ModeSetting(
         "Blur area",
         "Blur only behind the window, or the whole world while the GUI is open",
         var1.clickGuiBlurArea,
         ClientPreferences.bLUR_AREAS.toArray(String[]::new)
      );
      var13.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiBlurArea = var2x));
      this.addCard(
         "Backdrop",
         "Shown behind the GUI.",
         new SettingRowWidget(var10),
         new SettingRowWidget(var11),
         new SettingRowWidget(var12),
         new ModeSegmentWidget(var13)
      );
      ModeSetting var14 = new ModeSetting(
         "Cards",
         "Module cards: Filled, an Outline only, or Flat without a contour",
         var1.clickGuiCardStyle,
         ClientPreferences.cARD_STYLES.toArray(String[]::new)
      );
      var14.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiCardStyle = var2x));
      ModeSetting var15 = new ModeSetting(
         "Enabled cards",
         "How an enabled module stands out: a Glow with an accent wash, only the Tint, an accent Text title, or None",
         var1.clickGuiEnabledStyle,
         ClientPreferences.eNABLED_STYLES.toArray(String[]::new)
      );
      var15.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiEnabledStyle = var2x));
      ModeSetting var16 = new ModeSetting(
         "Switches", "Pill, Square or Minimal switches", var1.clickGuiSwitchStyle, ClientPreferences.sWITCH_STYLES.toArray(String[]::new)
      );
      var16.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiSwitchStyle = var2x));
      ModeSetting var17 = new ModeSetting(
         "Sliders", "Pill, Minimal or Classic sliders", var1.clickGuiSliderStyle, ClientPreferences.sLIDER_STYLES.toArray(String[]::new)
      );
      var17.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiSliderStyle = var2x));
      NumberSetting var18 = new NumberSetting(
         "Rounding", "Corner rounding of the window, cards and controls, in percent. 0 gives square corners", var1.clickGuiRounding * 100.0, 0.0, 200.0, 5.0
      );
      var18.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiRounding = var2x / 100.0));
      BooleanSetting var19 = new BooleanSetting("Accent glow", "A soft accent glow breathing around the window", var1.clickGuiAccentGlow);
      var19.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiAccentGlow = var2x));
      BooleanSetting var20 = new BooleanSetting("Ripple", "A ripple spreads from the pointer when you click a module card", var1.clickGuiRipple);
      var20.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiRipple = var2x));
      BooleanSetting var21 = new BooleanSetting("Hover lift", "Module cards rise a little under the pointer", var1.clickGuiHoverLift);
      var21.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiHoverLift = var2x));
      this.addCard(
         "Style",
         "Look of cards, controls and effects.",
         new SettingRowWidget(var14),
         new SettingRowWidget(var15),
         new SettingRowWidget(var16),
         new SettingRowWidget(var17),
         new SettingRowWidget(var18),
         new SettingRowWidget(var19),
         new SettingRowWidget(var20),
         new SettingRowWidget(var21)
      );
      NumberSetting var22 = new NumberSetting("Intensity", "Glow strength", PostProcessing.bloomStrength, 0.0, 2.0, 0.05);
      var22.onChange(var0 -> PostProcessing.bloomStrength = var0.floatValue());
      NumberSetting var23 = new NumberSetting("Radius", "Halo spread", PostProcessing.bloomSpread, 0.5, 3.0, 0.1);
      var23.onChange(var0 -> PostProcessing.bloomSpread = var0.floatValue());
      this.addCard("Bloom", "Shared world glow.", new SettingRowWidget(var22), new SettingRowWidget(var23));
      LarpClient var24 = LarpClient.getInstance();
      KeybindSetting var25 = new KeybindSetting(
            "ClickUI keybind",
            "Open or close ClickUI with a key or a middle or side mouse button. Escape/Delete clears the binding; right click restores Right Shift.",
            344
         )
         .disableToggle();
      var25.setKeyCode(var24.getGuiKey());
      var25.onChange(var24::setGuiKey);
      NumberSetting var26 = new NumberSetting(
         "Scale", "ClickUI size in percent. Larger sizes are limited by the window size.", var1.clickGuiScale * 100.0, 50.0, 200.0, 5.0
      );
      var26.onChange(var2x -> this.applyAndSave(() -> var1.clickGuiScale = var2x / 100.0));
      MenuButton var27 = new MenuButton("Reset scale", () -> var26.getDisplayValue(100.0));
      var27.centeredText = (boolean)1;
      this.addCard("ClickUI", "Shortcut and size of the client interface.", new SettingRowWidget(var25), new SettingRowWidget(var26), var27);
      BooleanSetting var28 = new BooleanSetting("Custom main menu", "Use the client title screen and menu backgrounds instead of vanilla", var1.customMenu);
      var28.onChange(var2x -> {
         this.applyAndSave(() -> var1.customMenu = var2x);
         LarpLog.info("GUI", "Custom main menu {}", var2x ? "enabled" : "disabled");
      });
      this.addCard("Main menu", "Client title screen outside worlds.", new SettingRowWidget(var28));
      BooleanSetting var29 = new BooleanSetting(
         "Legacy layout",
         "Floating category panels: left click toggles a module, right click drops down its settings. Applies the next time you open the GUI",
         var1.legacyClickGui.enabled
      );
      var29.onChange(var2x -> {
         this.applyAndSave(() -> var1.legacyClickGui.enabled = var2x);
         LarpLog.info("GUI", "Legacy ClickGUI {}", var2x ? "enabled" : "disabled");
      });
      ColorSetting var30 = new ColorSetting("Panel color", "Enabled module rows and the menu key outline in the legacy layout", var1.legacyClickGui.color);
      var30.onChange(var2x -> this.applyAndSave(() -> var1.legacyClickGui.color = var2x));
      MenuButton var31 = new MenuButton("Reset panel layout", () -> this.applyAndSave(var1.legacyClickGui::clearPanels));
      var31.centeredText = (boolean)1;
      this.addCard("Legacy ClickGUI", "Classic panels with drop-down settings.", new SettingRowWidget(var29), new SettingRowWidget(var30), var31);
      if (!StreamProofState.isHidden()) {
         BooleanSetting var32 = new BooleanSetting(
            "Legit mode", "Stop and hide everything the Legit edition does not have. Your modules come back when you switch it off with the key below", false
         );
         var32.onChange(var1x -> {
            if (var1x) {
               var24.setLegitMode(true);
            }
         });
         KeybindSetting var33 = new KeybindSetting(
               "Switch key", "Press this key three times quickly to switch Legit mode on or off. Keyboard keys only; clearing it restores /", var1.legitModeKey
            )
            .disableToggle();
         var33.onChange(var3x -> {
            if (BindCodes.isKeyboardBind(var3x)) {
               this.applyAndSave(() -> var1.legitModeKey = var3x);
            } else {
               var33.setKeyCode(47);
            }
         });
         this.addCard("Legit mode", "Play as the Legit edition without changing clients.", new SettingRowWidget(var32), new SettingRowWidget(var33));
      }
   }

   private static int columnFor(int var0, int var1) {
      if (var1 != 3) {
         return var0 % var1;
      }

      return switch (var0) {
         case 0, 5, 8 -> 0;
         case 1, 4, 6, 9 -> 1;
         default -> 2;
      };
   }

   private static int shortestColumn(int[] var0) {
      int var1 = 0;

      for (int var2 = 1; var2 < var0.length; var2++) {
         if (var0[var2] < var0[var1]) {
            var1 = var2;
         }
      }

      return var1;
   }

   @Override
   protected boolean layoutCustomContent() {
      if (this.section.equals("Interface") && !this.cards.isEmpty()) {
         int var1 = this.contentRight - this.contentLeft - 16;
         int var2 = var1 >= 470 ? 3 : (var1 >= 320 ? 2 : 1);
         int[] var3 = new int[var2];
         int[] var4 = new int[var2];
         if (var2 == 3) {
            var4[0] = (var1 - 16) * 31 / 100;
            var4[1] = (var1 - 16) * 38 / 100;
            var4[2] = var1 - 16 - var4[0] - var4[1];
         } else {
            Arrays.fill(var4, Math.max(1, (var1 - 8 * (var2 - 1)) / var2));
         }

         this.contentHeight = 0;
         int[] var5 = new int[this.cards.size()];

         for (int var6 = 0; var6 < this.cards.size(); var6++) {
            SettingsScreen.Card var7 = this.cards.get(var6);
            int var8 = var2 == 3 ? columnFor(var6, var2) : shortestColumn(var3);
            var5[var6] = var8;
            var7.panel.height = 40 + var7.controls.stream().mapToInt(var0 -> var0.height + 4).sum();
            var3[var8] += var7.panel.height + 8;
            this.contentHeight = Math.max(this.contentHeight, var3[var8] - 8);
         }

         this.scrollOffset = Math.clamp(this.scrollOffset, 0, Math.max(0, this.contentHeight - (this.contentBottom - this.contentTop)));
         int var16 = (int)Math.round(this.scrollAnimation.animateTo(this.scrollOffset, 150));
         Arrays.fill(var3, 0);

         for (int var17 = 0; var17 < this.cards.size(); var17++) {
            SettingsScreen.Card var18 = this.cards.get(var17);
            int var9 = var5[var17];
            int var10 = this.contentLeft + 8;

            for (int var11 = 0; var11 < var9; var11++) {
               var10 += var4[var11] + 8;
            }

            int var19 = this.contentTop + var3[var9] - var16;
            var18.panel.bounds(var10, var19, var4[var9]);
            int var12 = var19 + 36;

            for (Widget var14 : var18.controls) {
               var14.bounds(var10 + 6, var12, Math.max(1, var4[var9] - 12));
               if (var14 instanceof SettingRowWidget var15) {
                  var15.layoutPopup(this.frame.top() + 12, this.contentBottom);
               }

               var12 += var14.height + 4;
            }

            var3[var9] += var18.panel.height + 8;
         }

         return true;
      } else {
         return false;
      }
   }

   private void buildHud() {
      this.title = this.selectedHudElement == null ? "HUD overlays" : "HUD / " + HudLayout.getDisplayName(this.selectedHudElement.id);
      if (this.selectedHudElement == null) {
         this.addButton("Open HUD editor", () -> this.gui.selectCategory());
         this.text("Use Editor for placement and previews. Chat dragging remains available.");

         for (HudLayout.HudElement var8 : HudLayout.addBounds()) {
            MenuButton var11 = this.addButton(HudLayout.getDisplayName(var8.id), () -> {
               this.selectedHudElement = var8;
               this.scrollOffset = 0;
               this.resetReveal();
               this.build();
            });
            var11.subtitle = HudLayout.getStatus(var8);
            var11.moduleCard = (boolean)1;
            var11.height = 34;
            var11.toggleState = HudLayout.isEnabled(var8);
            var11.secondaryAction = () -> {
               HudLayout.setElementEnabled(var8, !HudLayout.isEnabled(var8));
               LarpLog.debug("HUD editor", "{} visibility={}", var8.id, HudLayout.isEnabled(var8));
            };
            this.hudButtons.put(var11, var8);
         }

         this.addWidget(new SectionLabel("Placement guides"));
         BooleanSetting var6 = new BooleanSetting("Snap", "Snap to viewport edges and center while dragging", UiState.hudEditor.snapping());
         var6.onChange(
            var0 -> UiState.hudEditor = new UiState.HudEditorState(UiState.hudEditor.panel(), UiState.hudEditor.collapsed(), var0, UiState.hudEditor.grid())
         );
         this.addWidget(new SettingRowWidget(var6));
         BooleanSetting var9 = new BooleanSetting("8px Grid", "Optional grid; Alt bypasses grid and snapping", UiState.hudEditor.grid());
         var9.onChange(
            var0 -> UiState.hudEditor = new UiState.HudEditorState(UiState.hudEditor.panel(), UiState.hudEditor.collapsed(), UiState.hudEditor.snapping(), var0)
         );
         this.addWidget(new SettingRowWidget(var9));
      } else {
         HudLayout.HudElement var1 = this.selectedHudElement;
         var1.fitTo(this.width, this.height);
         this.addButton("All HUD overlays", () -> {
            this.selectedHudElement = null;
            this.scrollOffset = 0;
            this.resetReveal();
            this.build();
         });
         this.text(HudLayout.getStatus(var1) + ". Visibility restores the required module and content toggle.");
         this.visibleToggle = new BooleanSetting("Visible", "Hide only this overlay; restoring enables its content", HudLayout.isEnabled(var1));
         this.visibleToggle.onChange(var2 -> {
            if (!this.syncingControls) {
               HudLayout.setElementEnabled(var1, var2);
            }
         });
         this.xSlider = new NumberSetting("X", "Layout origin in GUI pixels", var1.getX(this.width), 0.0, this.width, 1.0);
         this.ySlider = new NumberSetting("Y", "Layout origin in GUI pixels", var1.getY(this.height), 0.0, this.height, 1.0);
         this.xSlider.onChange(var2 -> {
            if (!this.syncingControls) {
               var1.setPosition(var2.intValue(), var1.getY(this.height), this.width, this.height);
            }
         });
         this.ySlider.onChange(var2 -> {
            if (!this.syncingControls) {
               var1.setPosition(var1.getX(this.width), var2.intValue(), this.width, this.height);
            }
         });
         this.scaleSlider = new NumberSetting("Scale", "Uniform content scale", var1.scale(), 0.5, 2.5, 0.05);
         this.scaleSlider.onChange(var2 -> {
            if (!this.syncingControls) {
               var1.setScale(var2);
            }
         });
         this.alignmentSelect = new ModeSetting(
            "Alignment",
            "Normalized screen anchor",
            applyAndSave(var1),
            "Free",
            "Top left",
            "Top center",
            "Top right",
            "Center left",
            "Center",
            "Center right",
            "Bottom left",
            "Bottom center",
            "Bottom right"
         );
         this.alignmentSelect.onChange(var2 -> {
            if (!this.syncingControls && !var2.equals("Free")) {
               int var3x = this.alignmentSelect.getModes().indexOf(var2) - 1;
               UiState.hudPositions.put(var1.id, new UiState.HudPosition(var3x % 3 * 0.5, var3x / 3 * 0.5));
            }
         });
         this.addWidget(new SectionLabel("Placement"));

         for (Setting var3 : List.of(this.visibleToggle, this.xSlider, this.ySlider, this.scaleSlider, this.alignmentSelect)) {
            this.addWidget(new SettingRowWidget(var3));
         }

         List var7 = HudLayout.getSettings(var1);
         if (!var7.isEmpty()) {
            this.addWidget(new SectionLabel("Appearance"));
         }

         for (Setting var4 : var7) {
            this.addWidget(new SettingRowWidget(var4, true));
         }

         if (var1.id.equals("crosshair")) {
            this.addButton("Center crosshair", () -> {
               var1.setScale();
               this.build();
            });
         }

         this.addButton("Reset placement", () -> {
            var1.resetPlacement();
            this.build();
         });
      }
   }

   private static String applyAndSave(HudLayout.HudElement var0) {
      UiState.HudPosition var1 = UiState.hudPositions.get(var0.id);
      if (var1 == null) {
         return var0.id.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.xFraction() * 2.0);
      int var3 = (int)Math.round(var1.yFraction() * 2.0);
      return Math.abs(var1.xFraction() - var2 * 0.5) < 0.001 && Math.abs(var1.yFraction() - var3 * 0.5) < 0.001
         ? List.of("Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right")
            .get(var3 * 3 + var2)
         : "Free";
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.section.equals("Updates")) {
         this.consentKeys.reconcile(this::isKeyDown);
      }

      for (Entry var5 : this.hudButtons.entrySet()) {
         ((MenuButton)var5.getKey()).toggleState = HudLayout.isEnabled((HudLayout.HudElement)var5.getValue());
         ((MenuButton)var5.getKey()).subtitle = HudLayout.getStatus((HudLayout.HudElement)var5.getValue());
      }

      if (this.selectedHudElement != null && this.xSlider != null) {
         this.syncingControls = (boolean)1;

         try {
            this.xSlider.getDisplayValue((double)this.selectedHudElement.getX(this.width));
            this.ySlider.getDisplayValue((double)this.selectedHudElement.getY(this.height));
            this.scaleSlider.getDisplayValue(this.selectedHudElement.scale());
            this.visibleToggle.setValue(HudLayout.isEnabled(this.selectedHudElement));
            this.alignmentSelect.setValue(applyAndSave(this.selectedHudElement));
         } finally {
            this.syncingControls = (boolean)0;
         }
      }
   }

   @Override
   protected void layout() {
      this.setSection();
      super.layout();
      this.contentRight = this.section.equals("Interface") ? this.frame.right() : Math.min(this.frame.right(), this.contentLeft + 440);
      this.layoutContent();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return !var2
            || this.primaryUpdateButton == null
            || !this.primaryUpdateButton.contains(var1.x(), var1.y())
               && !this.secondaryUpdateButton.contains(var1.x(), var1.y())
               && !this.skipUpdateButton.contains(var1.x(), var1.y())
         ? super.mouseClicked(var1, var2)
         : true;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      return this.section.equals("Updates") && !this.consentKeys.press(KeyCodes.keyOf(var1)) ? true : super.keyPressed(var1);
   }

   private record Card(InfoCardWidget panel, List<Widget> controls) {
   }
}

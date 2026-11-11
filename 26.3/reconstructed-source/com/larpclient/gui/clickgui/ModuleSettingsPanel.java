package com.larpclient.gui.clickgui;

import com.larpclient.gui.misc.DrawingGrid;
import com.larpclient.gui.misc.GuiLayout;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.MenuButton;
import com.larpclient.gui.widget.ModuleToggleWidget;
import com.larpclient.gui.widget.SectionLabel;
import com.larpclient.gui.widget.SettingRowWidget;
import com.larpclient.gui.widget.TabPanel;
import com.larpclient.gui.widget.TextField;
import com.larpclient.gui.widget.WaypointsPanel;
import com.larpclient.gui.widget.Widget;
import com.larpclient.module.combat.AutoTrap;
import com.larpclient.module.exploit.FakePayModule;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.module.misc.AutoSellModule;
import com.larpclient.module.misc.CasinoGameModule;
import com.larpclient.module.misc.HitSoundsModule;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.LARPModule;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.SkinChangerModule;
import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.module.render.ChooseASkyEffectAndAdjustItsAppearanceModule;
import com.larpclient.module.render.CustomCrosshairModule;
import com.larpclient.module.render.HUDModule;
import com.larpclient.module.render.HitFXModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.module.render.MenuBackgroundModule;
import com.larpclient.module.render.WaypointsModule;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.setting.ModuleSettingGroups;
import com.larpclient.social.cosmetic.LocalAppearance;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.DrawUtil;
import java.util.HashMap;
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

final class ModuleSettingsPanel extends TabPanel {
   private Module module;
   private String larpUsername = "";
   private String fakeBalanceInput = "";
   private String fakeBalanceStatus = "";
   private String statusMessage = "";
   private MenuButton startLarpingButton;
   private final Map<MenuButton, CasinoGameModule.SetupAction> setupActions = new IdentityHashMap<>();
   private final Map<MenuButton, ModulePanel.Action> commandActions = new IdentityHashMap<>();
   private ModulePanel.Status moduleStatus;
   private Object activeActionGroup;
   private WaypointsPanel waypointEditor;
   private int inputMinX;
   private int inputMaxX;
   private final Set<String> expandedGroups = new HashSet<>();
   private final Map<String, MenuButton> groupButtons = new HashMap<>();
   private final Map<Widget, BooleanSupplier> visibilityConditions = new IdentityHashMap<>();
   private String skyEffectValue;
   private ModuleToggleWidget header;

   ModuleSettingsPanel() {
      super("Module");
   }

   void setModule(Module var1) {
      if (this.module instanceof HitSoundsModule var2) {
         var2.stopPreview();
      }

      this.module = var1;
      this.expandedGroups.clear();
      this.scrollOffset = 0;
      if (var1 instanceof FakePayModule var4) {
         this.fakeBalanceInput = var4.getBalanceString();
      }

      this.fakeBalanceStatus = "";
      this.statusMessage = "";
      this.build();
   }

   void setInputRange(GuiLayout var1, int var2, int var3) {
      this.inputMinX = var2;
      this.inputMaxX = var3;
      this.attach(var1);
   }

   @Override
   protected double revealAmount() {
      return 1.0;
   }

   @Override
   protected boolean renderPopupInline() {
      return false;
   }

   @Override
   protected boolean horizontalInput(double var1) {
      return var1 >= this.inputMinX && var1 < this.inputMaxX;
   }

   void rebuildPreservingScroll() {
      int var1 = this.scrollOffset;
      this.build();
      this.layout();
      this.scrollOffset = var1;
      this.layout();
   }

   @Override
   protected void build() {
      this.clearWidgets();
      this.groupButtons.clear();
      this.visibilityConditions.clear();
      this.setupActions.clear();
      this.startLarpingButton = null;
      this.header = null;
      this.commandActions.clear();
      this.moduleStatus = null;
      if (this.module != null) {
         Module var1 = this.module;
         this.skyEffectValue = var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var2 ? var2.getStyleSetting().getValue() : null;
         this.title = var1.getDisplayName();
         this.header = new ModuleToggleWidget("", var1);
         this.widgets.add(this.header);
         if (!var1.getDescription().isBlank()) {
            this.text(var1.getDescription());
         }

         if (var1 instanceof AutoTrap var12) {
            this.addDynamicText(var12::getStatus);
         }

         if (var1 instanceof CustomCrosshairModule var13) {
            this.addWidget(new ModuleSettingsPanel.CrosshairEditorWidget(var13));
            MenuButton var3 = this.addButton("Clear drawing", var13::clearDrawing);
            var3.centeredText = (boolean)1;
            this.visibilityConditions.put(var3, var13::isDrawMode);
         }

         if (var1 instanceof AutoSellModule var14) {
            this.addDynamicText(var14::getSelectionSummary);
            this.addButton("Choose items to sell", () -> var14.openItemPicker(this.gui, false));
            this.addButton("Choose excluded items", () -> var14.openItemPicker(this.gui, true));
            this.addButton("Add held item to selection", () -> {
               try {
                  var14.addHeldItem(false);
                  this.statusMessage = "Held item added.";
               } catch (IllegalArgumentException var3x) {
                  this.statusMessage = var3x.getMessage();
               }
            });
            this.addDynamicText(() -> this.statusMessage);
         }

         HashMap var15 = new HashMap();
         if (var1 instanceof ModulePanel var16) {
            ModulePanel.Status var4 = var16.status();
            if (var4 != null) {
               for (ModulePanel.SettingGroup var6 : var16.settingGroups()) {
                  var15.put(var6.title(), var6);
               }

               this.moduleStatus = var4;
               this.addColoredText(() -> this.moduleStatus.text(), () -> this.moduleStatus.problem() ? -996986 : Theme.textPrimary);
            } else {
               this.addDynamicText(var16::summary);
               this.addDynamicText(var16::detail);
               Widget var30 = var16.preview();
               if (var30 != null) {
                  this.addWidget(var30);
               }

               String var34 = null;

               for (ModulePanel.Command var8 : var16.commands()) {
                  if (!var8.section().equals(var34)) {
                     var34 = var8.section();
                     this.addWidget(new SectionLabel(var34));
                  }

                  this.addButton(var8.label(), () -> this.statusMessage = var8.run().get());
               }

               this.addDynamicText(() -> this.statusMessage);
            }
         }

         if (var1 instanceof WaypointsModule var17) {
            if (this.waypointEditor == null || this.waypointEditor.module != var17) {
               this.waypointEditor = new WaypointsPanel(var17, this);
            }

            this.waypointEditor.build();
         } else {
            this.waypointEditor = null;
         }

         if (var1 instanceof OrderDropperModule var18) {
            this.addDynamicText(var18::describeSelection);
            this.addButton("Use held item", () -> {
               try {
                  var18.setOrderItemFromHand();
                  this.statusMessage = "Order item set from your hand.";
               } catch (IllegalArgumentException var3x) {
                  this.statusMessage = var3x.getMessage();
               }
            });
            this.addDynamicText(
               () -> "Hold the Select Shulker Key and sweep over boxes to select them; start on a selected box to remove. Or use Select shulkers: right-click adds, left-click removes, sneak-click covers an area, Esc finishes."
            );
            this.addButton("Select shulkers", () -> {
               try {
                  var18.beginSelection();
                  this.statusMessage = "";
               } catch (IllegalArgumentException var3x) {
                  this.statusMessage = var3x.getMessage();
               }
            });
            this.addButton("Clear selected shulkers", () -> {
               try {
                  var18.clearShulkers();
                  this.statusMessage = "Shulker selection cleared.";
               } catch (IllegalArgumentException var3x) {
                  this.statusMessage = var3x.getMessage();
               }
            });
            this.addButton("Rescan selected shulkers", () -> {
               try {
                  var18.rescan();
                  this.statusMessage = "Shulkers will be scanned when enabled. Run progress retained.";
               } catch (IllegalArgumentException var3x) {
                  this.statusMessage = var3x.getMessage();
               }
            });
            this.addButton("Reset run progress", () -> {
               try {
                  var18.resetProgress();
                  this.statusMessage = "Run progress reset. Shulker selection retained.";
               } catch (IllegalArgumentException var3x) {
                  this.statusMessage = var3x.getMessage();
               }
            });
            this.addDynamicText(() -> this.statusMessage);
         }

         if (var1 instanceof CasinoGameModule var19) {
            this.addWidget(new SectionLabel("Setup"));
            this.addDynamicText(var19::setupSummary);
            this.addDynamicText(var19::setupHint);

            for (CasinoGameModule.SetupAction var31 : var19.getSetupActions()) {
               MenuButton var35 = this.addButton(var31.currentLabel(), var31.run());
               this.setupActions.put(var35, var31);
            }
         }

         Iterator var20 = ModuleSettingGroups.groupsFor(var1).iterator();

         while (true) {
            ModuleSettingGroups.SettingGroup var28;
            do {
               if (!var20.hasNext()) {
                  if (var1 instanceof HUDModule) {
                     this.addButton("Open HUD editor", () -> this.gui.selectCategory());
                  }

                  if (var1 instanceof SkinChangerModule var21) {
                     this.addButton("Apply skin", var21::loadSkin);
                     this.addButton("Refresh skins", () -> {
                        var21.getInstance();
                        this.build();
                        this.layout();
                     });
                     this.addButton("Open skins folder", var21::openSkinsFolder);
                     this.addDynamicText(() -> "Add a 64x64 or 64x32 PNG to the skins folder, then refresh.");
                  }

                  if (var1 instanceof MenuBackgroundModule var22) {
                     var22.refreshLibraryThrottled();
                     this.addButton("Refresh images", () -> {
                        var22.refreshLibrary();
                        this.build();
                        this.layout();
                     });
                     this.addButton("Open backgrounds folder", var22::openFolder);
                     this.addDynamicText(() -> "Put a PNG or JPG in config/larp-client/backgrounds, refresh, then pick it under Image.\n" + var22.getStatus());
                  }

                  if (var1 instanceof HitSoundsModule var23) {
                     this.addButton("Preview sound", var23::previewSound);
                  }

                  if (var1 instanceof LARPModule var24) {
                     TextField var29 = new TextField("Minecraft username", var1x -> this.larpUsername = var1x, 16);
                     var29.text = this.larpUsername;
                     this.addWidget(var29);
                     this.startLarpingButton = new MenuButton("Start larping", () -> {
                        if (!this.larpUsername.trim().matches("[A-Za-z0-9_]{1,16}")) {
                           LocalAppearance.statusMessage = "Enter a Minecraft username";
                        } else {
                           var24.setEnabled(true);
                           LocalAppearance.importUsername(this.larpUsername.trim());
                        }
                     });
                     this.addWidget(this.startLarpingButton);
                  }

                  if (!(var1 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule)) {
                     this.addWidget(new SectionLabel("Keybind"));
                     KeybindSetting var25 = new KeybindSetting(
                        var1 instanceof HUDModule ? "Toggle HUD" : "Keybind",
                        "Press a key or mouse button. For left or right click, click this field again. Escape/Delete clears the binding",
                        var1.getKeyBind()
                     );
                     var25.onChange(var1::setKeyBind);
                     this.addWidget(new SettingRowWidget(var25));
                     if (var1.keybindMode() != null) {
                        this.addWidget(new SettingRowWidget(var1.keybindMode()));
                     }
                  }

                  MenuButton var26 = this.addButton("Reset module settings", () -> {
                     for (Setting var3x : var1.getSettings()) {
                        var3x.reset();
                     }

                     if (var1 instanceof FakeSpawnerModule var4x) {
                        var4x.resetToDefaults();
                     }

                     if (var1 instanceof FakePayModule var5) {
                        this.fakeBalanceInput = var5.getBalanceString();
                     }

                     this.fakeBalanceStatus = "";
                     this.build();
                  });
                  var26.centeredText = (boolean)1;
                  return;
               }

               var28 = (ModuleSettingGroups.SettingGroup)var20.next();
               if (!var28.collapsed()) {
                  break;
               }

               String var32 = (this.expandedGroups.contains(var28.title()) ? "Hide " : "Show ") + var28.title().toLowerCase(Locale.ROOT);
               MenuButton var36 = this.addButton(var32, () -> {
                  boolean var3x = this.groupButtons.get(var28.title()).focusVisible;
                  if (var1 instanceof ModulePanel) {
                     this.activeActionGroup = null;
                  }

                  if (!this.expandedGroups.remove(var28.title())) {
                     this.expandedGroups.add(var28.title());
                  }

                  this.build();
                  this.layout();
                  if (var3x) {
                     this.focusWidget(this.groupButtons.get(var28.title()));
                  }
               });
               this.groupButtons.put(var28.title(), var36);
               if (!var15.isEmpty()) {
                  var36.centeredText = (boolean)1;
               }

               this.visibilityConditions.put(var36, () -> var28.settings().stream().anyMatch(Setting::isVisible));
            } while (!this.expandedGroups.contains(var28.title()));

            ModulePanel.SettingGroup var33 = (ModulePanel.SettingGroup)var15.get(var28.title());
            ModulePanel.Setting var37 = var33 == null ? null : var33.setup();
            this.addWidget(new SectionLabel(var28.title(), () -> var28.settings().stream().anyMatch(Setting::isVisible), var28.chips()));
            if (var37 != null) {
               this.addDynamicText(var37.value());
               Supplier var38 = var37.value();
               this.visibilityConditions.put(this.widgets.getLast(), () -> !((String)var38.get()).isBlank());
               if (!var37.settingsFirst()) {
                  this.addActionRows(var37);
               }
            }

            for (Setting var40 : var28.settings()) {
               if (var1 instanceof FakePayModule var9 && var40 == var9.blockPayPacket()) {
                  this.addWidget(new SectionLabel("Fake balance"));
                  TextField var10 = new TextField("Amount, e.g. 10T, 1B or 61.4", var1x -> {
                     this.fakeBalanceInput = var1x;
                     this.fakeBalanceStatus = "";
                  }, 40);
                  var10.text = this.fakeBalanceInput;
                  Runnable var11 = () -> {
                     String var2x = var9.setBalanceFromString(this.fakeBalanceInput);
                     this.fakeBalanceStatus = var2x.isEmpty() ? "Balance submitted" : var2x;
                  };
                  var10.onSubmit(var11);
                  this.addWidget(var10);
                  this.addButton("Submit", var11);
                  this.addDynamicText(() -> var9.getBalanceText() + (this.fakeBalanceStatus.isEmpty() ? "" : "\n" + this.fakeBalanceStatus));
               } else if (!(var40 instanceof NumberSetting var41 && var41.getUpperBound())) {
                  if (var28.chips()) {
                     this.addWidget(SettingRowWidget.createToggleChip(var40));
                  } else {
                     this.addWidget(new SettingRowWidget(var40));
                  }

                  if (var1 instanceof HitFXModule var42 && var40 == var42.getPresetSetting()) {
                     this.addDynamicText(var42::describePreset);
                  }

                  if (var1 instanceof KillFXModule var43 && var40 == var43.getStyleSetting()) {
                     this.addDynamicText(var43::getStyleDescription);
                  }
               }
            }

            if (var37 != null && var37.settingsFirst()) {
               this.addActionRows(var37);
            }
         }
      }
   }

   @Override
   protected boolean componentShown(Widget var1) {
      BooleanSupplier var2 = this.visibilityConditions.get(var1);
      return super.componentShown(var1) && (var2 == null || var2.getAsBoolean());
   }

   private void addActionRows(ModulePanel.Setting var1) {
      for (List var3 : var1.rows()) {
         this.addActionRow(var3);
      }

      Widget var4 = var1.picture().get();
      if (var4 != null) {
         this.addWidget(var4);
      }
   }

   private void addActionRow(List<ModulePanel.Action> var1) {
      if (!var1.isEmpty()) {
         Object var2 = new Object();

         for (ModulePanel.Action var4 : var1) {
            MenuButton var5 = new MenuButton(var4.label(), () -> {
               this.activeActionGroup = var2;
               this.statusMessage = var4.run().get();
            });
            var5.pill = (boolean)1;
            var5.tag = var2;
            var5.height = 24;
            var5.enabled = var4.available().getAsBoolean();
            this.commandActions.put(var5, var4);
            this.addWidget(var5);
         }

         this.addDynamicText(() -> this.activeActionGroup == var2 ? this.statusMessage : "");
         this.visibilityConditions.put(this.widgets.getLast(), () -> this.activeActionGroup == var2 && !this.statusMessage.isBlank());
      }
   }

   private void tickPanelModule(ModulePanel var1) {
      var1.panelTick();
      ModulePanel.Status var2 = var1.status();
      if (var2 != null) {
         this.moduleStatus = var2;
      }

      this.commandActions.forEach((var0, var1x) -> var0.enabled = var1x.available().getAsBoolean());
   }

   @Override
   protected int titleX() {
      return this.contentLeft + 12;
   }

   @Override
   protected int titleY() {
      return this.frame.bodyTop() + 6;
   }

   @Override
   protected int bodyTop() {
      return this.frame.bodyTop() + 44;
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.header != null) {
         this.header.render(var1, var2, var3);
      }

      DrawUtil.drawPixelLine(var1, this.contentLeft + 12, this.contentTop - 5, this.contentRight - this.contentLeft - 24, false, Theme.accentGlow);
   }

   @Override
   protected void layout() {
      if (this.frame != null) {
         this.contentLeft = this.frame.columns() ? this.frame.inspectorX() : this.frame.contentX();
         this.contentRight = this.frame.right();
         this.contentTop = this.bodyTop();
         this.contentBottom = this.frame.bottom();
         if (this.header != null) {
            this.header.bounds(this.contentLeft + 8, this.frame.bodyTop() + 21, Math.max(1, this.contentRight - this.contentLeft - 20));
            this.header.height = 16;
         }

         this.layoutContent();
      }
   }

   @Override
   void suspend() {
      if (this.module instanceof HitSoundsModule var1) {
         var1.stopPreview();
      }

      super.suspend();
   }

   @Override
   public void onClose() {
      if (this.module instanceof HitSoundsModule var1) {
         var1.stopPreview();
      }

      this.gui.renderCloseEffect();
   }

   public void tick() {
      if (this.module instanceof ChooseASkyEffectAndAdjustItsAppearanceModule var1 && !Objects.equals(this.skyEffectValue, var1.getStyleSetting().getValue())) {
         int var4 = this.focusedWidget != null && this.focusedWidget.focusVisible ? 1 : 0;
         this.rebuildPreservingScroll();
         if (var4 != 0) {
            this.widgets.stream().filter(var0 -> var0 instanceof SettingRowWidget).findFirst().ifPresent(this::focusWidget);
         }
      }

      this.setupActions.forEach((var0, var1x) -> var0.label = var1x.currentLabel());
      if (this.module instanceof ModulePanel var3) {
         this.tickPanelModule(var3);
      }

      if (this.waypointEditor != null) {
         this.waypointEditor.tick();
      }

      if (this.startLarpingButton != null) {
         this.startLarpingButton.enabled = (boolean)(!LocalAppearance.isLookupInFlight() ? 1 : 0);
         this.startLarpingButton.label = this.startLarpingButton.enabled ? "Start larping" : "Starting...";
      }
   }

   private static final class CrosshairEditorWidget extends Widget {
      private static final int dEFAULT_HEIGHT = 88;
      private static final int pADDING = 6;
      private static final int fOOTER_HEIGHT = 14;
      private static final int gRID_SIZE = 15;
      private static final int pANEL_COLOR = -14999514;
      private static final int cELL_COLOR = -14275533;
      private static final int aXIS_CELL_COLOR = -13880517;
      private static final int cENTER_CELL_COLOR = -12958899;
      private final CustomCrosshairModule module;
      private boolean dragging;
      private boolean painting;
      private int lastCellX;
      private int lastCellY;

      CrosshairEditorWidget(CustomCrosshairModule var1) {
         this.module = var1;
         this.height = 88;
      }

      @Override
      public void bounds(int var1, int var2, int var3) {
         super.bounds(var1, var2, var3);
         this.height = this.module.isDrawMode() ? 12 + 15 * this.cellSize() + 14 : 88;
         if (!this.module.isDrawMode()) {
            this.release();
         }
      }

      private int previewWidth() {
         return Math.clamp(this.width / 3, 56, 96);
      }

      private int editorWidth() {
         return this.width - this.previewWidth() - 4;
      }

      private int cellSize() {
         return Math.clamp((this.editorWidth() - 12) / 15, 3, 10);
      }

      private int gridLeft() {
         return this.x + (this.editorWidth() - 15 * this.cellSize()) / 2;
      }

      private int gridTop() {
         return this.y + 6;
      }

      private int cellColumn(double var1) {
         return (int)Math.floor((var1 - this.gridLeft()) / this.cellSize());
      }

      private int cellRow(double var1) {
         return (int)Math.floor((var1 - this.gridTop()) / this.cellSize());
      }

      private static boolean isInGrid(int var0, int var1) {
         return var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (!this.module.isDrawMode()) {
            int var18 = this.width / 2;
            this.drawPreview(var1, this.x, this.y, var18 - 4, this.height, true);
            this.drawPreview(var1, this.x + var18, this.y, var18 - 4, this.height, false);
         } else {
            int var4 = this.editorWidth();
            int var5 = this.cellSize();
            int var6 = this.gridLeft();
            int var7 = this.gridTop();
            int var8 = var5 >= 5 ? 1 : 0;
            GlassPainter.fillRoundedRect(var1, this.x, this.y, var4, this.height, 5, -14999514);
            DrawingGrid var9 = this.module.getDrawing();
            int var10 = this.module.getColor();
            byte var11 = 7;

            for (int var12 = 0; var12 < 15; var12++) {
               for (int var13 = 0; var13 < 15; var13++) {
                  int var14 = var6 + var13 * var5;
                  int var15 = var7 + var12 * var5;
                  if (var9.isSet(var13, var12)) {
                     DrawUtil.fillRect(var1, var14, var15, var5, var5, var10);
                  } else {
                     DrawUtil.fillRect(
                        var1,
                        var14,
                        var15,
                        var5 - var8,
                        var5 - var8,
                        var13 == var11 && var12 == var11 ? -12958899 : (var13 != var11 && var12 != var11 ? -14275533 : -13880517)
                     );
                  }
               }
            }

            int var19 = this.cellColumn(var2);
            int var20 = this.cellRow(var3);
            if (var2 >= 0 && isInGrid(var19, var20)) {
               DrawUtil.drawOutline(var1, var6 + var19 * var5 - 1, var7 + var20 * var5 - 1, var5 + 1, var5 + 1, -1056964609);
            }

            String var21 = FontRenderer.trimToWidth("Left click draws, right click erases", Math.max(1, var4 - 12), FontRenderer.TextStyle.HELP);
            FontRenderer.drawText(var1, var21, this.x + 6, var7 + 15 * var5 + 4, -6643287, FontRenderer.TextStyle.HELP);
            int var22 = this.x + var4 + 4;
            int var16 = this.previewWidth() - 4;
            int var17 = (this.height - 4) / 2;
            this.drawPreview(var1, var22, this.y, var16, var17, true);
            this.drawPreview(var1, var22, this.y + var17 + 4, var16, this.height - var17 - 4, false);
         }
      }

      private void drawPreview(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
         double var7 = Math.min((var4 - 8) / 2.0, var5 / 2.0 - 15.0);
         double var9 = Math.min(1.0, Math.max(1.0, var7) / CustomCrosshairModule.getCurrentExtent());
         GlassPainter.fillRoundedRect(var1, var2, var3, var4, var5, 5, var6 ? -14999514 : -2762790);
         FontRenderer.drawText(var1, var6 ? "Dark" : "Light", var2 + 7, var3 + 6, var6 ? -4472376 : -12433328);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var2 + var4 / 2.0F, var3 + var5 / 2.0F + 7.0F);
            var1.pose().scale((float)var9, (float)var9);
            CustomCrosshairModule.drawPreview(var1, 0, 0);
         } finally {
            var1.pose().popMatrix();
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (this.module.isDrawMode() && var5 <= 1) {
            int var6 = this.cellColumn(var1);
            int var7 = this.cellRow(var3);
            if (!isInGrid(var6, var7)) {
               return false;
            }

            this.painting = (boolean)(var5 == 0 && !this.module.getDrawing().isSet(var6, var7) ? 1 : 0);
            this.dragging = (boolean)1;
            this.lastCellX = var6;
            this.lastCellY = var7;
            this.module.setCell(var6, var7, this.painting);
            return true;
         } else {
            return false;
         }
      }

      @Override
      public void drag(double var1, double var3) {
         if (this.dragging && this.module.isDrawMode()) {
            int var5 = this.cellColumn(var1);
            int var6 = this.cellRow(var3);
            int var7 = var5 - this.lastCellX;
            int var8 = var6 - this.lastCellY;
            int var9 = Math.max(Math.abs(var7), Math.abs(var8));

            for (int var10 = 1; var10 <= var9; var10++) {
               int var11 = this.lastCellX + (int)Math.round((double)var7 * var10 / var9);
               int var12 = this.lastCellY + (int)Math.round((double)var8 * var10 / var9);
               if (isInGrid(var11, var12)) {
                  this.module.setCell(var11, var12, this.painting);
               }
            }

            this.lastCellX = var5;
            this.lastCellY = var6;
         }
      }

      @Override
      public void release() {
         if (this.dragging) {
            this.dragging = (boolean)0;
            LarpLog.info("Crosshair", "Drawing edited: {} cells filled", this.module.getDrawing().cellCount());
         }
      }
   }
}

package com.larpclient.gui.hud;

import com.larpclient.LarpClient;
import com.larpclient.config.ClientPreferences;
import com.larpclient.config.ConfigManager;
import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.misc.UiState;
import com.larpclient.gui.screen.WidgetScreen;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.MenuButton;
import com.larpclient.gui.widget.SettingRowWidget;
import com.larpclient.gui.widget.TextField;
import com.larpclient.gui.widget.Widget;
import com.larpclient.hud.HudEditor;
import com.larpclient.hud.HudEditorSession;
import com.larpclient.hud.HudLayout;
import com.larpclient.hud.HudLayout2;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.ModuleManager;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public final class HudEditorScreen extends WidgetScreen {
   private static final List<String> alignmentOptions = List.of(
      "Free", "Top left", "Top center", "Top right", "Center left", "Center", "Center right", "Bottom left", "Bottom center", "Bottom right"
   );
   private final Screen parent;
   private HudEditorSession editor;
   private HudEditorSession.EditorLayout layout;
   private HudEditorSession.PanelMode panelMode = HudEditorSession.PanelMode.NONE;
   private final List<HudEditorScreen.ToolbarButton> dockButtons = new ArrayList<>();
   private final List<HudEditorScreen.ToolbarButton> panelButtons = new ArrayList<>();
   private final List<Widget> panelControls = new ArrayList<>();
   private final Set<Widget> lockableControls = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Animation panelAnimation = new Animation(1.0);
   private final Map<Setting<?>, Object> initialSettingValues = new IdentityHashMap<>();
   private TextField searchField;
   private HudEditorScreen.ToolbarButton undoButton;
   private HudEditorScreen.ToolbarButton redoButton;
   private HudEditorScreen.ToolbarButton editButton;
   private HudEditorScreen.ToolbarButton retryButton;
   private NumberSetting xSlider;
   private NumberSetting ySlider;
   private NumberSetting sizeSlider;
   private ModeSetting alignDropdown;
   private BooleanSetting showToggle;
   private BooleanSetting lockToggle;
   private boolean dockVisible = (boolean)1;
   private boolean previewHidden;
   private boolean liveContent;
   private boolean syncingControls;
   private boolean windowWasActive = (boolean)1;
   private boolean saveFailed;
   private boolean draggingPanel;
   private boolean animationsEnabled = (boolean)1;
   private double dragOffsetX;
   private double dragOffsetY;
   private int scrollOffset;
   private int contentHeight;
   private int panelX = -1;
   private int panelY = -1;
   private static boolean saveFailedPending;
   private String searchQuery = "";
   private Object openedLevel;

   public HudEditorScreen(Screen var1) {
      super("HUD Editor");
      this.parent = var1;
   }

   public boolean isLiveContent() {
      return this.liveContent;
   }

   private ConfigManager config() {
      return LarpClient.getInstance().getConfigManager();
   }

   private boolean isLocked(HudLayout.HudElement var1) {
      return this.config().getPreferences().isHudLocked(var1.id);
   }

   protected void init() {
      if (this.editor == null) {
         ModuleManager var1 = LarpClient.getInstance().getModuleManager();
         List var2 = Stream.of(var1.getByName("HUD"), var1.getByName("TargetHUD"), var1.getByName("Staff List"), var1.getByName("CustomCrosshair"))
            .filter(Objects::nonNull)
            .toList();
         this.editor = new HudEditorSession(() -> this.config().saveProfile(this.config().getActiveProfile()), this::isLocked, var2);
         this.openedLevel = this.minecraft.level;
         this.saveFailed = saveFailedPending;
         LarpLog.info("HUD editor", "Opened canvas with floating dock, {}x{}; profile {}", this.width, this.height, this.config().getActiveProfile());
      } else {
         this.editor.settleDrag();
      }

      HudLayout.clearLayout();
      this.rebuild();
   }

   private HudEditorScreen.ToolbarButton addDockButton(String var1, String var2, Runnable var3, BooleanSupplier var4) {
      HudEditorScreen.ToolbarButton var5 = new HudEditorScreen.ToolbarButton(var1, var2, var3, var4);
      this.widgets.add(var5);
      return var5;
   }

   private void addSetting(Setting<?> var1, boolean var2) {
      SettingRowWidget var3 = new SettingRowWidget(var1, true);
      this.panelControls.add(var3);
      this.widgets.add(var3);
      if (var2) {
         this.lockableControls.add(var3);
      }
   }

   private void addButton(String var1, Runnable var2, boolean var3, boolean var4) {
      MenuButton var5 = new MenuButton(var1, var2);
      var5.flat = (boolean)1;
      var5.danger = var4;
      var5.height = 28;
      this.panelControls.add(var5);
      this.widgets.add(var5);
      if (var3) {
         this.lockableControls.add(var5);
      }
   }

   private void addToggle(String var1, String var2, boolean var3, Consumer<Boolean> var4) {
      BooleanSetting var5 = new BooleanSetting(var1, var2, var3);
      var5.onChange(var4);
      this.addSetting(var5, false);
   }

   private void rebuild() {
      this.clearWidgets();
      this.dockButtons.clear();
      this.panelButtons.clear();
      this.panelControls.clear();
      this.lockableControls.clear();
      this.searchField = null;
      this.xSlider = this.ySlider = this.sizeSlider = null;
      this.alignDropdown = null;
      this.showToggle = this.lockToggle = null;
      this.dockButtons
         .add(
            this.addDockButton(
               "Widgets",
               "panels-top-left",
               () -> this.togglePanel(HudEditorSession.PanelMode.WIDGETS),
               () -> this.panelMode == HudEditorSession.PanelMode.WIDGETS
            )
         );
      this.undoButton = this.addDockButton("Undo (Ctrl+Z)", "rotate-ccw", () -> {
         this.finishEdit();
         this.editor.undo();
      }, () -> false);
      this.undoButton.iconOnly = (boolean)1;
      this.dockButtons.add(this.undoButton);
      this.redoButton = this.addDockButton("Redo (Ctrl+Shift+Z)", "rotate-ccw", () -> {
         this.finishEdit();
         this.editor.redo();
      }, () -> false);
      this.redoButton.iconOnly = this.redoButton.flipIcon = (boolean)1;
      this.dockButtons.add(this.redoButton);
      HudEditorScreen.ToolbarButton var1 = this.addDockButton(
         "Editor options", "settings", () -> this.togglePanel(HudEditorSession.PanelMode.OPTIONS), () -> this.panelMode == HudEditorSession.PanelMode.OPTIONS
      );
      var1.iconOnly = (boolean)1;
      this.dockButtons.add(var1);
      this.editButton = this.addDockButton("Edit", "paintbrush", () -> {
         if (this.editor.getSelected() != null) {
            if (this.panelMode != HudEditorSession.PanelMode.APPEARANCE && this.panelMode != HudEditorSession.PanelMode.POSITION) {
               this.openEditPanel(this.editor.getSelected());
            } else {
               this.setPanel(HudEditorSession.PanelMode.NONE);
            }
         }
      }, () -> this.panelMode == HudEditorSession.PanelMode.APPEARANCE || this.panelMode == HudEditorSession.PanelMode.POSITION);
      this.dockButtons.add(this.editButton);
      HudEditorScreen.ToolbarButton var2 = this.addDockButton("Done", "", this::onClose, () -> false);
      var2.primary = (boolean)1;
      this.dockButtons.add(var2);
      this.retryButton = this.addDockButton("Save failed. Retry", "rotate-ccw", () -> {
         this.editor.save();
         this.checkSaveResult();
      }, () -> false);
      if (this.panelMode != HudEditorSession.PanelMode.NONE) {
         HudEditorScreen.ToolbarButton var3 = this.addDockButton("Close panel", "x", () -> this.setPanel(HudEditorSession.PanelMode.NONE), () -> false);
         var3.iconOnly = (boolean)1;
         this.panelButtons.add(var3);
         if (this.panelMode == HudEditorSession.PanelMode.WIDGETS) {
            this.searchField = new TextField("Search widgets", var1x -> {
               this.searchQuery = var1x;
               this.scrollOffset = 0;
               this.updateLayout();
            }, 80);
            this.searchField.text = this.searchQuery;
            this.widgets.add(this.searchField);

            for (HudLayout.HudElement var5 : HudLayout.addBounds()) {
               HudEditorScreen.ElementRow var6 = new HudEditorScreen.ElementRow(var5);
               this.panelControls.add(var6);
               this.widgets.add(var6);
            }
         } else if (this.panelMode == HudEditorSession.PanelMode.OPTIONS) {
            this.addToggle(
               "Snap to guides",
               "Align with screen edges and center",
               UiState.hudEditor.snapping(),
               var1x -> this.editor.undoCount(var1x, UiState.hudEditor.grid())
            );
            this.addToggle(
               "Show grid", "Show an eight-pixel placement grid", UiState.hudEditor.grid(), var1x -> this.editor.undoCount(UiState.hudEditor.snapping(), var1x)
            );
            BooleanSetting var7 = new BooleanSetting("Preview hidden widgets", "Preview without enabling widgets", this.previewHidden);
            var7.visibleWhen(() -> !this.liveContent);
            var7.onChange(var1x -> this.previewHidden = var1x);
            SettingRowWidget var9 = new SettingRowWidget(var7);
            this.panelControls.add(var9);
            this.widgets.add(var9);
            this.addToggle("Use live content", "Show current gameplay content instead of samples", this.liveContent, var1x -> {
               this.editor.settleDrag();
               this.liveContent = var1x;
               HudLayout.clearLayout();
            });
            this.addButton("Reset layout", () -> {
               this.finishEdit();
               this.editor.runEdit();
            }, false, true);
         } else if (this.editor.getSelected() != null) {
            this.panelButtons
               .add(
                  this.addDockButton(
                     "Appearance",
                     "",
                     () -> this.setPanel(HudEditorSession.PanelMode.APPEARANCE),
                     () -> this.panelMode == HudEditorSession.PanelMode.APPEARANCE
                  )
               );
            this.panelButtons
               .add(
                  this.addDockButton(
                     "Position", "", () -> this.setPanel(HudEditorSession.PanelMode.POSITION), () -> this.panelMode == HudEditorSession.PanelMode.POSITION
                  )
               );
            HudLayout.HudElement var8 = this.editor.getSelected();
            if (this.panelMode == HudEditorSession.PanelMode.APPEARANCE) {
               this.showToggle = new BooleanSetting("Show widget", "Keep this widget visible in gameplay", HudLayout.isEnabled(var8));
               this.showToggle.onChange(var2x -> {
                  if (!this.syncingControls) {
                     HudLayout.setElementEnabled(var8, var2x);
                     this.editor.markDirty();
                  }
               });
               this.addSetting(this.showToggle, false);
               this.addSizeSlider(var8);

               for (Setting var11 : HudLayout.getSettings(var8)) {
                  this.addSetting(var11, false);
               }
            } else {
               this.lockToggle = new BooleanSetting("Lock position", "Prevent moving and resizing this widget", this.isLocked(var8));
               this.lockToggle.onChange(var2x -> {
                  if (!this.syncingControls) {
                     this.setLocked(var8, var2x);
                  }
               });
               this.addSetting(this.lockToggle, false);
               this.xSlider = new NumberSetting("X", "Horizontal position in GUI pixels", var8.getX(this.width), 0.0, this.width, 1.0);
               this.ySlider = new NumberSetting("Y", "Vertical position in GUI pixels", var8.getY(this.height), 0.0, this.height, 1.0);
               this.xSlider.onChange(var2x -> {
                  if (!this.syncingControls && !this.isLocked(var8)) {
                     var8.setPosition(var2x.intValue(), var8.getY(this.height), this.width, this.height);
                  }
               });
               this.ySlider.onChange(var2x -> {
                  if (!this.syncingControls && !this.isLocked(var8)) {
                     var8.setPosition(var8.getX(this.width), var2x.intValue(), this.width, this.height);
                  }
               });
               this.addSetting(this.xSlider, true);
               this.addSetting(this.ySlider, true);
               this.addSizeSlider(var8);
               this.alignDropdown = new ModeSetting("Align", "Align within the full game viewport", addToggle(var8), alignmentOptions.toArray(String[]::new));
               this.alignDropdown.onChange(var2x -> {
                  if (!this.syncingControls && !this.isLocked(var8) && !var2x.equals("Free")) {
                     int var3x = alignmentOptions.indexOf(var2x) - 1;
                     UiState.hudPositions.put(var8.id, new UiState.HudPosition(var3x % 3 * 0.5, var3x / 3 * 0.5));
                  }
               });
               this.addSetting(this.alignDropdown, true);
               this.addButton("Reset placement", () -> {
                  this.finishEdit();
                  if (!this.isLocked(var8)) {
                     this.editor.runEdit(var8::resetPlacement);
                  }
               }, true, false);
            }
         }
      }

      this.updateLayout();
   }

   private void addSizeSlider(HudLayout.HudElement var1) {
      this.sizeSlider = new NumberSetting("Size", "Uniform widget scale", var1.scale(), 0.2, 4.0, 0.01);
      this.sizeSlider.onChange(var2 -> {
         if (!this.syncingControls && !this.isLocked(var1)) {
            var1.setScale(var2);
         }
      });
      this.addSetting(this.sizeSlider, true);
   }

   private void togglePanel(HudEditorSession.PanelMode var1) {
      this.setPanel(this.panelMode == var1 ? HudEditorSession.PanelMode.NONE : var1);
   }

   private void setPanel(HudEditorSession.PanelMode var1) {
      this.finishEdit();
      this.editor.settleDrag();
      this.draggingPanel = (boolean)0;
      this.dockVisible = (boolean)1;
      if (this.panelMode == HudEditorSession.PanelMode.NONE || var1 == HudEditorSession.PanelMode.WIDGETS || var1 == HudEditorSession.PanelMode.OPTIONS) {
         this.panelX = this.panelY = -1;
      }

      this.panelMode = var1;
      this.scrollOffset = 0;
      this.panelAnimation.snapTo(this.animationsEnabled ? 0.0 : 1.0);
      this.panelAnimation.animateTo(1.0, 160);
      this.rebuild();
   }

   private void openEditPanel(HudLayout.HudElement var1) {
      this.finishEdit();
      this.editor.select(var1);
      this.config(var1);
      HudLayout2.Rect var2 = HudLayout.findElementBounds(HudLayout.registerOverlay(), var1).orElse(var1.getBounds(this.width, this.height));
      this.panelX = var2.left() + var2.width() + 14;
      this.panelY = var2.top();
      if (this.panelX + 260 > this.width - 12) {
         this.panelX = Math.max(12, var2.left() - 274);
      }

      this.panelMode = HudEditorSession.PanelMode.APPEARANCE;
      this.dockVisible = (boolean)1;
      this.scrollOffset = 0;
      this.panelAnimation.snapTo(this.animationsEnabled ? 0.0 : 1.0);
      this.panelAnimation.animateTo(1.0, 160);
      this.rebuild();
   }

   private void selectWidget(HudLayout.HudElement var1) {
      this.finishEdit();
      this.editor.select(var1);
      this.config(var1);
      this.panelMode = HudEditorSession.PanelMode.NONE;
      this.scrollOffset = 0;
      this.rebuild();
   }

   private void config(HudLayout.HudElement var1) {
      if (this.liveContent && HudLayout.findElementBounds(HudLayout.registerOverlay(), var1).isEmpty()) {
         this.liveContent = (boolean)0;
      }
   }

   private void setLocked(HudLayout.HudElement var1, boolean var2) {
      this.editor.settleDrag();
      this.config().getPreferences().setHudLocked(var1.id, var2);
      this.checkSaveResult();
   }

   private void checkSaveResult() {
      this.saveFailed = saveFailedPending = (boolean)(!this.config().savePreferences() ? 1 : 0);
   }

   private boolean matchesSearch(HudLayout.HudElement var1) {
      return this.searchQuery.isBlank()
         || (HudLayout.getDisplayName(var1.id) + " " + var1.id).toLowerCase(Locale.ROOT).contains(this.searchQuery.toLowerCase(Locale.ROOT));
   }

   private boolean passesSearchFilter(Widget var1) {
      return !(var1 instanceof HudEditorScreen.ElementRow var2 && !this.matchesSearch(var2.element));
   }

   private boolean addSetting(Widget var1) {
      return !(var1 instanceof SettingRowWidget var2 && !var2.isActive());
   }

   private void updateLayout() {
      this.animationsEnabled = this.config().getPreferences().motion;
      this.contentHeight = this.panelControls.stream().filter(this::passesSearchFilter).filter(this::addSetting).mapToInt(var0 -> var0.height + 4).sum();
      this.layout = HudEditorSession.EditorLayout.calculate(
         this.width, this.height, this.panelMode, this.contentHeight, this.panelX, this.panelY, this.dockVisible, this.editor.isDirty() || this.saveFailed
      );

      for (int var1 = 0; var1 < this.dockButtons.size(); var1++) {
         HudEditorScreen.ToolbarButton var2 = this.dockButtons.get(var1);
         var2.visible = (boolean)(this.layout.dock() != null ? 1 : 0);
         if (var2.visible) {
            placeButton(var2, this.layout.tools().get(var1));
         }
      }

      this.undoButton.enabled = this.editor.canUndo();
      this.redoButton.enabled = this.editor.canRedo();
      this.editButton.enabled = (boolean)(this.editor.getSelected() != null ? 1 : 0);
      this.retryButton.visible = (boolean)(this.layout.retry() != null ? 1 : 0);
      if (this.retryButton.visible) {
         placeButton(this.retryButton, this.layout.retry());
      }

      HudLayout2.Rect var9 = this.layout.panel();
      HudLayout2.Rect var10 = this.layout.body();

      for (HudEditorScreen.ToolbarButton var4 : this.panelButtons) {
         var4.visible = (boolean)(var9 != null ? 1 : 0);
      }

      if (var9 != null && !this.panelButtons.isEmpty()) {
         this.panelButtons.getFirst().bounds(var9.left() + var9.width() - 36, var9.top() + 9, 26);
         this.panelButtons.getFirst().height = 26;
         if (this.panelButtons.size() == 3) {
            int var11 = (var9.width() - 24) / 2;

            for (int var13 = 1; var13 < 3; var13++) {
               this.panelButtons.get(var13).bounds(var9.left() + 10 + (var13 - 1) * (var11 + 4), var9.top() + 43, var11);
               this.panelButtons.get(var13).height = 26;
            }
         }
      }

      if (this.searchField != null) {
         this.searchField.visible = (boolean)(var9 != null ? 1 : 0);
         if (var9 != null) {
            this.searchField.bounds(var9.left() + 10, var9.top() + 43, var9.width() - 20);
            this.searchField.height = 25;
         }
      }

      int var12 = var10 == null ? 0 : var10.height();
      this.scrollOffset = Math.clamp(this.scrollOffset, 0, Math.max(0, this.contentHeight - var12));
      int var14 = var10 == null ? 0 : var10.top() - this.scrollOffset;

      for (Widget var6 : this.panelControls) {
         int var7 = this.passesSearchFilter(var6) && this.addSetting(var6) ? 1 : 0;
         var6.visible = (boolean)(var10 != null && var7 != 0 && var14 + var6.height > var10.top() && var14 < var10.top() + var10.height() ? 1 : 0);
         var6.enabled = (boolean)(this.lockableControls.contains(var6) && this.editor.getSelected() != null && this.isLocked(this.editor.getSelected()) ? 0 : 1);
         if (var10 != null && var7 != 0) {
            var6.bounds(var10.left(), var14, var10.width());
            if (var6 instanceof SettingRowWidget var8) {
               var8.layoutPopup(var10.top(), var10.top() + var10.height());
            }

            var14 += var6.height + 4;
         }
      }

      if (this.focusedWidget != null && (!this.focusedWidget.visible || !this.focusedWidget.enabled)) {
         this.focusedWidget.blur();
         this.focusedWidget = this.draggedWidget = null;
         this.commitEdit();
      }
   }

   private static void placeButton(Widget var0, HudLayout2.Rect var1) {
      var0.bounds(var1.left(), var1.top(), var1.width());
      var0.height = var1.height();
   }

   @Override
   protected boolean accepts(Widget var1, double var2, double var4) {
      return this.panelControls.contains(var1)
         ? var1.contains(var2, var4) && this.layout.controlContains(new HudLayout2.Rect(var1.x, var1.y, var1.width, var1.height), var2, var4)
         : var1.contains(var2, var4);
   }

   @Override
   protected void focusChanged(Widget var1) {
      if (this.panelControls.contains(var1) && this.layout.body() != null) {
         HudLayout2.Rect var2 = this.layout.body();
         if (var1.y < var2.top()) {
            this.scrollOffset = this.scrollOffset - (var2.top() - var1.y);
         }

         if (var1.y + var1.height > var2.top() + var2.height()) {
            this.scrollOffset = this.scrollOffset + (var1.y + var1.height - var2.top() - var2.height());
         }

         this.updateLayout();
      }
   }

   private static String addToggle(HudLayout.HudElement var0) {
      UiState.HudPosition var1 = UiState.hudPositions.get(var0.id);
      if (var1 == null) {
         return var0.id.equals("crosshair") ? "Center" : "Free";
      }

      int var2 = (int)Math.round(var1.xFraction() * 2.0);
      int var3 = (int)Math.round(var1.yFraction() * 2.0);
      return var2 >= 0
            && var2 <= 2
            && var3 >= 0
            && var3 <= 2
            && Math.abs(var1.xFraction() - var2 * 0.5) < 0.001
            && Math.abs(var1.yFraction() - var3 * 0.5) < 0.001
         ? alignmentOptions.get(var3 * 3 + var2 + 1)
         : "Free";
   }

   private void syncControls() {
      HudLayout.HudElement var1 = this.editor.getSelected();
      if (var1 != null) {
         this.syncingControls = (boolean)1;

         try {
            if (this.xSlider != null) {
               this.xSlider.getDisplayValue((double)var1.getX(this.width));
            }

            if (this.ySlider != null) {
               this.ySlider.getDisplayValue((double)var1.getY(this.height));
            }

            if (this.sizeSlider != null) {
               this.sizeSlider.getDisplayValue(var1.scale());
            }

            if (this.alignDropdown != null) {
               this.alignDropdown.setValue(addToggle(var1));
            }

            if (this.showToggle != null) {
               this.showToggle.setValue(HudLayout.isEnabled(var1));
            }

            if (this.lockToggle != null) {
               this.lockToggle.setValue(this.isLocked(var1));
            }
         } finally {
            this.syncingControls = (boolean)0;
         }
      }
   }

   private void beginEdit() {
      this.editor.beginEdit();
      if (this.initialSettingValues.isEmpty() && this.editor.getSelected() != null) {
         for (Setting var2 : HudLayout.getSettings(this.editor.getSelected())) {
            this.initialSettingValues.put(var2, var2.getValue());
         }
      }
   }

   private void commitEdit() {
      if (this.initialSettingValues.entrySet().stream().anyMatch(var0 -> !Objects.equals(var0.getValue(), var0.getKey().getValue()))) {
         this.editor.markDirty();
      }

      this.initialSettingValues.clear();
      this.editor.commit();
   }

   private void resetEditorState() {
      this.focusedWidget = this.draggedWidget = null;
      this.widgets.clear();
      this.initialSettingValues.clear();
      HudLayout.clearLayout();
      this.panelMode = HudEditorSession.PanelMode.NONE;
      this.draggingPanel = (boolean)0;
      this.scrollOffset = 0;
   }

   private void finishEdit() {
      if (this.editor.endSession()) {
         this.resetEditorState();
      } else {
         if (this.focusedWidget != null) {
            this.focusedWidget.blur();
         }

         this.focusedWidget = this.draggedWidget = null;
         this.commitEdit();
      }
   }

   private boolean addSizeSlider() {
      return this.focusedWidget instanceof TextField || this.focusedWidget instanceof SettingRowWidget var1 && var1.getShortcutKey();
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.minecraft.player != null && this.minecraft.level != null) {
         if (this.editor.endSession()) {
            this.resetEditorState();
            this.rebuild();
         }

         if (this.editor.commit().isDragging()) {
            return true;
         }

         this.updateLayout();

         for (Widget var4 : this.widgets) {
            if (var4 instanceof SettingRowWidget var5 && var5.isPopupOpen()) {
               if (var5.containsPopupPoint(var1.x(), var1.y())) {
                  this.beginEdit();
                  var5.clickPopup(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1));
                  this.draggedWidget = var5;
                  return true;
               }

               var5.closePopup();
            }
         }

         Widget var6 = this.widgets.stream().filter(var2x -> this.accepts(var2x, var1.x(), var1.y())).findFirst().orElse(null);
         if (var6 != this.focusedWidget) {
            this.finishEdit();
         }

         if (var6 != null) {
            this.beginEdit();
            this.focusedWidget = var6;
            var6.focused = (boolean)1;
            var6.focusVisible = (boolean)0;
            if (var6.click(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1))) {
               if (this.widgets.contains(var6)) {
                  this.draggedWidget = var6;
               } else {
                  this.focusedWidget = this.draggedWidget = null;
               }
            }

            if (!(this.draggedWidget instanceof SettingRowWidget) && !this.addSizeSlider()) {
               this.commitEdit();
            }

            return true;
         } else if (this.layout.panelContains(var1.x(), var1.y())) {
            if (KeyCodes.mouseButtonOf(var1) == 0 && var1.y() < this.layout.panel().top() + 39) {
               this.draggingPanel = (boolean)1;
               this.dragOffsetX = var1.x() - this.layout.panel().left();
               this.dragOffsetY = var1.y() - this.layout.panel().top();
            }

            return true;
         } else {
            if (this.layout.dock() != null && this.layout.dock().contains(var1.x(), var1.y(), 0)) {
               return true;
            }

            if (this.panelMode != HudEditorSession.PanelMode.NONE) {
               this.setPanel(HudEditorSession.PanelMode.NONE);
            }

            if (this.layout.canvasContains(var1.x(), var1.y())) {
               HudLayout.ElementBounds var7 = HudLayout2.findTopmostAt(
                  HudLayout.registerOverlay(), var0 -> true, HudLayout.ElementBounds::bounds, var1.x(), var1.y()
               );
               if (KeyCodes.mouseButtonOf(var1) == 1) {
                  if (var7 == null) {
                     this.setPanel(HudEditorSession.PanelMode.WIDGETS);
                  } else {
                     this.openEditPanel(var7.element());
                  }
               } else if (KeyCodes.mouseButtonOf(var1) == 0) {
                  this.beginEdit();
                  this.editor.commit().mousePressed(var1.x(), var1.y(), 0, this.width, this.height, HudLayout.registerOverlay(), false);
               }
            }

            if (!this.editor.commit().isDragging()) {
               this.commitEdit();
            }

            return true;
         }
      } else {
         return true;
      }
   }

   @Override
   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.editor.endSession()) {
         this.resetEditorState();
         this.rebuild();
         return true;
      } else if (!this.minecraft.isWindowActive()) {
         this.addToggle();
         return true;
      } else if (this.draggingPanel && this.layout.panel() != null) {
         this.panelX = Math.max(0, (int)Math.round(var1.x() - this.dragOffsetX));
         this.panelY = Math.max(0, (int)Math.round(var1.y() - this.dragOffsetY));
         this.updateLayout();
         return true;
      } else {
         return this.editor.commit().isDragging()
            ? this.editor
               .commit()
               .mouseDragged(
                  var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1), this.width, this.height, this.minecraft.hasAltDown(), this.minecraft.hasShiftDown()
               )
            : super.mouseDragged(var1, var2, var4);
      }
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.editor.endSession()) {
         this.resetEditorState();
         this.rebuild();
         return true;
      }

      this.draggingPanel = (boolean)0;
      this.editor.commit().mouseReleased(KeyCodes.mouseButtonOf(var1));
      super.mouseReleased(var1);
      if (!this.addSizeSlider()) {
         this.commitEdit();
      }

      return true;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (this.editor.endSession()) {
         this.resetEditorState();
         this.rebuild();
         return true;
      }

      for (Widget var10 : this.widgets) {
         if (var10 instanceof SettingRowWidget var11 && var11.containsPopupPoint(var1, var3)) {
            var11.shortcutBadgeWidth(var7);
            return true;
         }
      }

      if (this.layout.panelContains(var1, var3)) {
         this.finishEdit();
         this.scrollOffset = this.scrollOffset - (int)Math.round(var7 * 28.0);
         this.updateLayout();
      }

      return true;
   }

   @Override
   public boolean charTyped(CharacterEvent var1) {
      if (this.editor.endSession()) {
         this.resetEditorState();
         this.rebuild();
         return true;
      } else {
         return super.charTyped(var1);
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (this.editor.endSession()) {
         this.resetEditorState();
         this.rebuild();
      }

      int var2 = KeyCodes.keyOf(var1);
      int var3 = (var1.modifiers() & 2) != 0 ? 1 : 0;
      int var4 = (var1.modifiers() & 1) != 0 ? 1 : 0;
      if (this.addSizeSlider()) {
         if (var2 == 258) {
            this.finishEdit();
            return super.keyPressed(var1);
         }

         this.beginEdit();
         boolean var9 = super.keyPressed(var1);
         if (!this.addSizeSlider()) {
            this.commitEdit();
         }

         return var9;
      } else if (var2 == 256) {
         for (Widget var11 : this.panelControls) {
            if (var11 instanceof SettingRowWidget var7 && var7.isPopupOpen()) {
               var7.closePopup();
               return true;
            }
         }

         if (this.panelMode != HudEditorSession.PanelMode.NONE) {
            this.setPanel(HudEditorSession.PanelMode.NONE);
         } else {
            this.onClose();
         }

         return true;
      } else {
         if (var2 == 290) {
            this.finishEdit();
            this.editor.settleDrag();
            this.panelMode = HudEditorSession.PanelMode.NONE;
            this.draggingPanel = (boolean)0;
            this.dockVisible = (boolean)(!this.dockVisible ? 1 : 0);
            this.rebuild();
            return true;
         }

         if (var3 == 0 || var2 != 90 && var2 != 89) {
            if (var2 == 87 && var3 == 0) {
               this.togglePanel(HudEditorSession.PanelMode.WIDGETS);
               return true;
            }

            HudLayout.HudElement var5 = this.editor.getSelected();
            if (var2 == 69 && var3 == 0 && var5 != null) {
               this.openEditPanel(var5);
               return true;
            }

            if (this.focusedWidget == null && var5 != null && !this.isLocked(var5) && var2 >= 262 && var2 <= 265) {
               int var10 = var4 != 0 ? 10 : 1;
               this.editor
                  .moveSelected(var2 == 262 ? var10 : (var2 == 263 ? -var10 : 0), var2 == 264 ? var10 : (var2 == 265 ? -var10 : 0), this.width, this.height);
               return true;
            }

            if (this.focusedWidget == null && (var2 == 261 || var2 == 259) && var5 != null) {
               this.finishEdit();
               this.editor.runEdit(() -> var5.setHidden(true));
               return true;
            }

            this.beginEdit();
            boolean var6 = super.keyPressed(var1);
            if (!this.addSizeSlider()) {
               this.commitEdit();
            }

            return var6;
         } else {
            this.finishEdit();
            if (var2 != 89 && var4 == 0) {
               this.editor.undo();
            } else {
               this.editor.redo();
            }

            return true;
         }
      }
   }

   private void addToggle() {
      this.draggingPanel = (boolean)0;
      this.finishEdit();
      this.editor.settleDrag();
   }

   public void tick() {
      if (this.editor.endSession()) {
         this.resetEditorState();
         this.rebuild();
      }

      if (this.minecraft.level == this.openedLevel && this.minecraft.player != null) {
         boolean var1 = this.minecraft.isWindowActive();
         if (!var1 && this.windowWasActive) {
            this.addToggle();
         }

         this.windowWasActive = var1;
      } else {
         this.onClose();
      }
   }

   public void resize(int var1, int var2) {
      if (this.editor != null) {
         this.addToggle();
      }

      super.resize(var1, var2);
   }

   public void onClose() {
      if (this.editor != null) {
         this.finishEdit();
         this.editor.finishInteraction();
      }

      if (this.saveFailed) {
         this.checkSaveResult();
      }

      if (this.editor != null && this.editor.isDirty() || this.saveFailed) {
         NotificationManager.getInstance()
            .push("HUD save failed", "Edits remain in memory. Reopen the editor to retry.", NotificationManager.Type.WARNING, 6000L);
      }

      this.minecraft.gui.setScreen(this.minecraft.level == this.openedLevel ? this.parent : null);
   }

   public void removed() {
      if (this.editor != null) {
         this.finishEdit();
         this.editor.finishInteraction();
      }

      LarpLog.debug("HUD editor", "Released canvas input");
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (this.editor != null && this.minecraft.player != null && this.minecraft.level != null) {
         if (this.editor.endSession()) {
            this.resetEditorState();
            this.rebuild();
         }

         this.updateLayout();
         if (UiState.hudEditor.grid() && !this.liveContent) {
            HudEditor.isOverEditorUi(var1, this.width, this.height, this.height, 8, 5, 117440511, 318767103);
         }

         HudLayout.setHidden(var1, this.editor.getSelected(), this.previewHidden, this.liveContent);
         this.renderCanvasOverlays(var1, var2, var3);
         this.syncControls();
         ClientPreferences var5 = this.config().getPreferences();

         try (Theme.ThemeScope var6 = Theme.applyThemeWithLook(var5.clickGuiLight, var5.clickGuiOpacity, var5.clickGuiGradient, var5.clickGuiStyle())) {
            var1.nextStratum();
            if (this.layout.dock() != null) {
               finishEdit(var1, this.layout.dock());

               for (HudEditorScreen.ToolbarButton var8 : this.dockButtons) {
                  var8.render(var1, var2, var3);
               }

               if (this.retryButton.visible) {
                  this.retryButton.render(var1, var2, var3);
               }

               if (this.layout.panel() == null && !this.editor.commit().isDragging() && !this.retryButton.visible) {
                  String var16 = this.editor.getSelected() == null ? "HUD editor" : HudLayout.getDisplayName(this.editor.getSelected().id);
                  String var19 = this.editor.getSelected() == null ? "Drag to move  /  Right-click to edit" : "Right-click to edit  /  Arrows to nudge";
                  int var9 = FontRenderer.measureWidth(var16, FontRenderer.TextStyle.MODULE);
                  int var10 = FontRenderer.measureWidth(var19, FontRenderer.TextStyle.HELP);
                  int var11 = Math.min(this.width - 24, var9 + var10 + 28);
                  int var12 = (this.width - var11) / 2;
                  int var13 = this.layout.dock().top() - 28;
                  GlassPainter.fillRoundedRect(var1, var12, var13, var11, 21, 8, Theme.backgroundColor());
                  FontRenderer.drawText(var1, var16, var12 + 9, var13 + 6, Theme.textPrimary, FontRenderer.TextStyle.MODULE);
                  if (var9 + var10 + 28 <= var11) {
                     FontRenderer.drawText(var1, var19, var12 + var9 + 19, var13 + 7, Theme.textSecondary, FontRenderer.TextStyle.HELP);
                  }
               }
            }

            this.addSizeSlider(var1, var2, var3);

            for (Widget var20 : this.panelControls) {
               if (var20 instanceof SettingRowWidget var22 && var22.isPopupOpen() && var20.visible) {
                  var22.renderPopup(var1, var2, var3);
               }
            }

            if (this.widgets.stream().noneMatch(var0 -> var0 instanceof SettingRowWidget var1x && var1x.isPopupOpen())) {
               for (HudEditorScreen.ToolbarButton var21 : this.dockButtons) {
                  if (var21.visible && (var21.contains(var2, var3) || var21.focused && var21.focusVisible)) {
                     var21.renderTooltip(var1);
                  }
               }
            }
         }
      }
   }

   private static void finishEdit(GuiGraphicsExtractor var0, HudLayout2.Rect var1) {
      GlassPainter.drawShadowWithRadius(var0, var1.left(), var1.top(), var1.width(), var1.height(), 10);
      GlassPainter.drawPanel(var0, var1.left(), var1.top(), var1.width(), var1.height(), 10, Theme.backgroundColor(), 0.75);
   }

   private void addSizeSlider(GuiGraphicsExtractor var1, int var2, int var3) {
      HudLayout2.Rect var4 = this.layout.panel();
      HudLayout2.Rect var5 = this.layout.body();
      if (var4 != null) {
         if (!this.animationsEnabled) {
            this.panelAnimation.snapTo(1.0);
         }

         try (AlphaScope var6 = AlphaScope.push(this.panelAnimation.getValue())) {
            finishEdit(var1, var4);
            String var7 = this.panelMode == HudEditorSession.PanelMode.WIDGETS
               ? "Widgets"
               : (this.panelMode == HudEditorSession.PanelMode.OPTIONS ? "Editor options" : HudLayout.getDisplayName(this.editor.getSelected().id));
            if (this.panelMode == HudEditorSession.PanelMode.APPEARANCE || this.panelMode == HudEditorSession.PanelMode.POSITION) {
               Icons.texture(var1, Icons.get(Icons.hudIconName(this.editor.getSelected().id)), var4.left() + 13, var4.top() + 15, 13, Theme.textSecondary);
            }

            int var8 = var4.left()
               + (this.panelMode != HudEditorSession.PanelMode.APPEARANCE && this.panelMode != HudEditorSession.PanelMode.POSITION ? 14 : 34);
            FontRenderer.drawText(
               var1,
               FontRenderer.getTextWidth(var7, var4.left() + var4.width() - var8 - 42),
               var8,
               var4.top() + 16,
               Theme.textPrimary,
               FontRenderer.TextStyle.MODULE
            );

            for (HudEditorScreen.ToolbarButton var10 : this.panelButtons) {
               var10.render(var1, var2, var3);
            }

            if (this.searchField != null) {
               this.searchField.render(var1, var2, var3);
            }

            var1.enableScissor(var5.left(), var5.top(), var5.left() + var5.width(), var5.top() + var5.height());

            try {
               for (Widget var19 : this.panelControls) {
                  if (var19.visible) {
                     var19.render(var1, var5.contains(var2, var3, 0) ? var2 : -100, var3);
                  }
               }

               if (this.panelMode == HudEditorSession.PanelMode.WIDGETS && this.panelControls.stream().noneMatch(this::passesSearchFilter)) {
                  FontRenderer.drawText(var1, "No matching widgets", var5.left() + 6, var5.top() + 10, Theme.textSecondary, FontRenderer.TextStyle.BODY);
               }
            } finally {
               var1.disableScissor();
            }

            GlassPainter.drawScrollbar(var1, var4.left() + var4.width() - 5, var5.top(), var5.height(), this.contentHeight, this.scrollOffset);
         }
      }
   }

   private void renderCanvasOverlays(GuiGraphicsExtractor var1, int var2, int var3) {
      List var4 = HudLayout.registerOverlay();
      HudLayout.ElementBounds var5 = this.layout.canvasContains(var2, var3)
         ? HudLayout2.findTopmostAt(var4, var0 -> true, HudLayout.ElementBounds::bounds, var2, var3)
         : null;
      if (var5 != null && var5.element() != this.editor.getSelected()) {
         drawOutline(var1, var5.bounds(), -2137614688);
      }

      HudLayout.HudElement var6 = this.editor.getSelected();
      HudLayout.findElementBounds(var4, var6).ifPresent(var3x -> {
         drawOutline(var1, var3x, ColorUtil.withAlpha(Theme.accent, 0.85));
         if (!this.isLocked(var6) && !HudLayout.isAliasActive(var6)) {
            HudLayout2.Rect var4x = HudLayout2.resizeHandleBounds(var3x, this.width, this.height);
            GlassPainter.fillRoundedRect(var1, var4x.left() - 1, var4x.top() - 1, var4x.width() + 2, var4x.height() + 2, 3, -1072096999);
            GlassPainter.fillRoundedRect(var1, var4x.left() + 1, var4x.top() + 1, var4x.width() - 2, var4x.height() - 2, 2, Theme.accent);
         }

         if (this.editor.commit().isDragging()) {
            String var8 = var6.getX(this.width) + ", " + var6.getY(this.height);
            int var5x = FontRenderer.measureWidth(var8, FontRenderer.TextStyle.HELP) + 12;
            int var6x = HudLayout2.clampPosition(var3x.left(), var5x, this.width);
            int var7 = Math.max(2, var3x.top() - 20);
            GlassPainter.fillRoundedRect(var1, var6x, var7, var5x, 16, 5, -552003303);
            FontRenderer.drawText(var1, var8, var6x + 6, var7 + 4, -1381654, FontRenderer.TextStyle.HELP);
         }
      });
      if (this.editor.commit().isDragging()) {
         HudEditor.drawCenterGuides(var1, this.width, this.height, this.editor.commit().endGesture(), this.editor.commit().getGuideY());
      }
   }

   private static void drawOutline(GuiGraphicsExtractor var0, HudLayout2.Rect var1, int var2) {
      DrawUtil.drawOutline(var0, var1.left() - 1, var1.top() - 1, var1.width() + 2, var1.height() + 2, var2);
   }

   private final class ElementRow extends Widget {
      final HudLayout.HudElement element;
      private final Animation hoverAnimation = new Animation(0.0);
      private final Animation enabledAnimation = new Animation(0.0);

      ElementRow(HudLayout.HudElement var2) {
         this.element = var2;
         this.height = 36;
         this.enabledAnimation.snapTo(HudLayout.isEnabled(var2) ? 1.0 : 0.0);
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = HudLayout.isEnabled(this.element);
         int var5 = HudEditorScreen.this.editor.getSelected() == this.element ? 1 : 0;
         if (!HudEditorScreen.this.animationsEnabled) {
            this.hoverAnimation.snapTo(this.contains(var2, var3) ? 1.0 : 0.0);
            this.enabledAnimation.snapTo(var4 ? 1.0 : 0.0);
         }

         double var6 = this.hoverAnimation.animateTo(!this.contains(var2, var3) && !this.focused ? 0.0 : 1.0, 160);
         if (var5 != 0 || var6 > 0.001) {
            GlassPainter.fillRoundedRect(
               var1, this.x, this.y, this.width, this.height, 7, ColorUtil.lerpColor(Theme.background, Theme.borderColor, var5 != 0 ? 0.8 : var6 * 0.65)
            );
         }

         GlassPainter.fillRoundedRect(var1, this.x + 4, this.y + 6, 24, 24, 6, Theme.rowBackground);
         Icons.texture(var1, Icons.get(Icons.hudIconName(this.element.id)), this.x + 10, this.y + 12, 12, var4 ? Theme.textPrimary : Theme.textSecondary);
         FontRenderer.drawText(
            var1,
            FontRenderer.getTextWidth(HudLayout.getDisplayName(this.element.id), this.width - 74),
            this.x + 36,
            this.y + 7,
            Theme.textPrimary,
            FontRenderer.TextStyle.MODULE
         );
         FontRenderer.drawText(var1, var4 ? "Visible" : "Hidden", this.x + 36, this.y + 21, Theme.textSecondary, FontRenderer.TextStyle.HELP);
         GlassPainter.drawPulsingGlow(var1, this.x + this.width - 30, this.y + 12, this.enabledAnimation.animateTo(var4 ? 1.0 : 0.0, 180));
         if (this.focused && this.focusVisible) {
            GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, 7);
         }
      }

      private void toggleEnabled() {
         HudEditorScreen.this.editor.runEdit(() -> {
            HudLayout.setElementEnabled(this.element, !HudLayout.isEnabled(this.element));
            HudEditorScreen.this.editor.markDirty();
         });
         GuiSounds.play(HudLayout.isEnabled(this.element) ? GuiSounds.Sound.SWITCH_ON : GuiSounds.Sound.SWITCH_OFF);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 1) {
            HudEditorScreen.this.openEditPanel(this.element);
            return true;
         }

         if (var5 != 0) {
            return false;
         }

         if (var1 >= this.x + this.width - 38) {
            this.toggleEnabled();
         } else {
            HudEditorScreen.this.selectWidget(this.element);
         }

         return true;
      }

      @Override
      public boolean key(int var1) {
         if (var1 == 32) {
            this.toggleEnabled();
            return true;
         } else if (var1 == 257) {
            HudEditorScreen.this.selectWidget(this.element);
            return true;
         } else {
            return false;
         }
      }
   }

   private final class ToolbarButton extends Widget {
      final String label;
      final String iconName;
      final Runnable action;
      final BooleanSupplier isActive;
      final Animation hoverAnimation = new Animation(0.0);
      boolean iconOnly;
      boolean flipIcon;
      boolean primary;

      ToolbarButton(String var2, String var3, Runnable var4, BooleanSupplier var5) {
         this.label = var2;
         this.iconName = var3;
         this.action = var4;
         this.isActive = var5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.visible) {
            boolean var4 = this.isActive.getAsBoolean();
            int var5 = !this.contains(var2, var3) && (!this.focused || !this.focusVisible) ? 0 : 1;
            if (!HudEditorScreen.this.animationsEnabled) {
               this.hoverAnimation.snapTo(var5 != 0 ? 1.0 : 0.0);
            }

            double var6 = this.hoverAnimation.animateTo(var5 != 0 ? 1.0 : 0.0, 160);
            int var8 = this.primary ? Theme.background : (this.enabled ? Theme.textPrimary : Theme.textMuted);
            if (this.primary || var4 || var6 > 0.001) {
               GlassPainter.fillRoundedRect(
                  var1,
                  this.x,
                  this.y,
                  this.width,
                  this.height,
                  7,
                  this.primary
                     ? ColorUtil.lerpColor(Theme.textPrimary, Theme.textSecondary, var6 * 0.12)
                     : ColorUtil.lerpColor(Theme.background, Theme.borderColor, var4 ? 1.0 : var6)
               );
            }

            byte var9 = 13;
            int var10 = this.iconOnly ? 0 : FontRenderer.drawDefaultText(this.label);
            int var11 = var10 + (this.iconName.isEmpty() ? 0 : var9 + (var10 == 0 ? 0 : 6));
            int var12 = this.x + Math.max(5, (this.width - var11) / 2);
            if (!this.iconName.isEmpty()) {
               var1.pose().pushMatrix();

               try {
                  if (this.flipIcon) {
                     var1.pose().translate(var12 * 2 + var9, 0.0F);
                     var1.pose().scale(-1.0F, 1.0F);
                  }

                  Icons.texture(
                     var1, Icons.get(this.iconName), this.iconOnly ? this.x + (this.width - var9) / 2 : var12, this.y + (this.height - var9) / 2, var9, var8
                  );
               } finally {
                  var1.pose().popMatrix();
               }
            }

            if (!this.iconOnly) {
               FontRenderer.drawText(
                  var1,
                  FontRenderer.getTextWidth(this.label, this.width - (this.iconName.isEmpty() ? 12 : 27)),
                  var12 + (this.iconName.isEmpty() ? 0 : var9 + 6),
                  this.y + (this.height - 9) / 2,
                  var8
               );
            }

            if (this.focused && this.focusVisible) {
               GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, 7);
            }
         }
      }

      void renderTooltip(GuiGraphicsExtractor var1) {
         if (this.iconOnly) {
            int var2 = FontRenderer.measureWidth(this.label, FontRenderer.TextStyle.HELP) + 16;
            int var3 = HudLayout2.clampPosition(this.x + (this.width - var2) / 2, var2, HudEditorScreen.this.width);
            int var4 = Math.max(0, this.y - 27);
            GlassPainter.fillRoundedRect(var1, var3, var4, var2, 20, 6, Theme.headerBackground);
            FontRenderer.drawText(var1, this.label, var3 + 8, var4 + 6, Theme.textPrimary, FontRenderer.TextStyle.HELP);
         }
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            GuiSounds.play(GuiSounds.Sound.CLICK);
            this.action.run();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.enabled && (var1 == 257 || var1 == 32)) {
            GuiSounds.play(GuiSounds.Sound.CLICK);
            this.action.run();
            return true;
         } else {
            return false;
         }
      }
   }
}

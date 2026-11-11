package com.larpclient.gui.widget;

import com.google.gson.JsonObject;
import com.larpclient.LarpClient;
import com.larpclient.config.ConfigManager;
import com.larpclient.config.ProfileValidator;
import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.misc.FlowLayout;
import com.larpclient.gui.theme.Theme;
import com.larpclient.module.misc.Module;
import com.larpclient.network.ApiException;
import com.larpclient.network.ApiResponseValidator;
import com.larpclient.network.ServiceStatusClient;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.data.LatestFutureSlot;
import com.larpclient.util.misc.BugReportBuilder;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;

public final class ConfigsPanel extends TabPanel {
   final ServiceStatusClient cloudApi = ServiceStatusClient.iNSTANCE;
   final ConfigManager configManager = LarpClient.getInstance().getConfigManager();
   final ConfigsTab myConfigsView = new ConfigsTab(this);
   private final MarketplaceTab discoverView = new MarketplaceTab(this);
   String statusMessage = "";
   String shareCode = "";
   String searchQuery = "";
   boolean loading;
   boolean importing;
   private boolean showingSharedMenu;
   private boolean discoverTab;
   private String nameMode = "";
   private String nameInput = "";
   private Runnable confirmAction;
   private Runnable secondaryAction;
   private String confirmMessage = "";
   private String confirmLabel = "Confirm";
   private String secondaryLabel = "";
   private String cancelLabel = "Cancel";
   private boolean confirmDestructive;
   private final List<MenuButton> cards = new ArrayList<>();
   private final List<MenuButton> toolbarButtons = new ArrayList<>();
   private final Map<MenuButton, List<MenuButton>> cardActions = new LinkedHashMap<>();
   private TextField searchField;
   private TextField nameField;
   private TextField importField;
   private int cardScroll;
   private int cardsHeight;
   private int headerHeight = 62;
   private final Animation cardScrollAnimation = new Animation(0.0);
   private final LatestFutureSlot<JsonObject> requestRunner = new LatestFutureSlot<>(var1x -> this.getMinecraft().execute(var1x), this::isScreenActive);

   ConfigsPanel(ClickGuiScreen var1) {
      super("Configs");
      this.gui = var1;
   }

   private boolean isScreenActive() {
      return this.getMinecraft().gui.screen() == this.gui && this.gui.isPointInside(this);
   }

   List<Module> getModules() {
      return LarpClient.getInstance().getModuleManager().getModules();
   }

   Minecraft getMinecraft() {
      return this.gui.getMinecraft();
   }

   void addComponent(Widget var1) {
      this.widgets.add(var1);
   }

   @Override
   protected void text(String var1) {
      super.text(var1);
   }

   MenuButton addGuardedButton(String var1, Runnable var2) {
      return this.addButton(var1, () -> {
         if (!this.loading) {
            this.runAction(var2);
         }
      });
   }

   @Override
   protected MenuButton option(String var1, String var2, Runnable var3) {
      return super.option(var1, var2, () -> {
         if (!this.loading) {
            this.runAction(var3);
         }
      });
   }

   MenuButton addGuardedDialogButton(String var1, Runnable var2) {
      return this.addDialogButton(var1, () -> {
         if (!this.loading) {
            this.runAction(var2);
         }
      });
   }

   void setDialogText(String var1, String var2) {
      this.dialogTitle = var1;
      this.dialogBody = var2;
   }

   TextField addTextField(String var1, String var2, int var3, Consumer<String> var4) {
      TextField var5 = new TextField(var1, var4, var3);
      var5.text = var2;
      this.addWidget(var5);
      return var5;
   }

   void addConfigCard(String var1, String var2, boolean var3, Runnable var4) {
      MenuButton var5 = new MenuButton(var1, () -> {
         if (!this.loading) {
            this.shareCode = "";
            this.resetReveal();
            this.runAction(var4);
         }
      });
      var5.flat = (boolean)1;
      var5.serverCard = (boolean)1;
      var5.subtitle = var2;
      var5.selected = var2.equals("Active");
      this.cards.add(var5);
      this.widgets.add(var5);
   }

   void addCardAction(String var1, Runnable var2) {
      if (!this.cards.isEmpty()) {
         MenuButton var3 = this.cards.getLast();
         MenuButton var4 = new MenuButton(var1, () -> {
            if (!this.loading) {
               this.runAction(var2);
            }
         });
         var4.accented = (boolean)(!var1.equals("Load") && !var1.equals("Update") ? 0 : 1);
         var4.danger = var1.equals("Delete");
         var4.pill = (boolean)1;
         this.cardActions.computeIfAbsent(var3, var0 -> new ArrayList<>()).add(var4);
         this.widgets.add(var4);
      }
   }

   private void getModules(String var1, Runnable var2) {
      MenuButton var3 = new MenuButton(var1, () -> {
         if (!this.loading) {
            this.runAction(var2);
         }
      });
      var3.flat = (boolean)1;
      this.toolbarButtons.add(var3);
      this.widgets.add(var3);
   }

   private void runAction(Runnable var1) {
      try {
         var1.run();
      } catch (IllegalArgumentException var3) {
         this.statusMessage = var3.getMessage();
         LarpLog.warn("Config", "Config action rejected: {}", this.statusMessage);
         this.build();
      } catch (RuntimeException var4) {
         this.statusMessage = "The operation could not be completed. Try again.";
         LarpLog.error("Config", "Config action failed", var4);
         this.build();
      }
   }

   @Override
   protected void build() {
      this.clearWidgets();
      this.cards.clear();
      this.toolbarButtons.clear();
      this.cardActions.clear();
      this.nameField = this.importField = null;
      this.dialogOpen = (boolean)(!this.showingSharedMenu
            && !this.importing
            && this.myConfigsView.importPreview == null
            && this.confirmAction == null
            && this.nameMode.isEmpty()
            && !this.hasSelection()
         ? 0
         : 1);
      this.addWidget("My Configs", !this.discoverTab, () -> this.switchTab("My Configs"));
      this.addWidget("Discover", this.discoverTab, () -> this.switchTab("Discover"));
      this.searchField = new TextField(this.discoverTab ? "Search this page of configs..." : "Search configs...", var1x -> {
         if (this.discoverTab) {
            this.discoverView.query = var1x;
            this.discoverView.searchAtMillis = System.currentTimeMillis() + 180L;
         } else {
            this.searchQuery = var1x;
            this.getModules();
         }
      }, 80);
      this.searchField.text = this.discoverTab ? this.discoverView.query : this.searchQuery;
      this.widgets.add(this.searchField);
      if (!this.discoverTab) {
         this.getModules("Save as new config", () -> this.addConfigCard("New", ""));
         this.getModules("Import config", () -> {
            this.importing = (boolean)1;
            this.scrollOffset = 0;
            this.build();
            if (this.importField != null) {
               this.addLabeledValue(this.importField);
            }
         });
         if (this.frame != null && !this.frame.columns()) {
            this.getModules("More", () -> {
               this.showingSharedMenu = (boolean)1;
               this.scrollOffset = 0;
               this.build();
            });
         } else {
            this.getModules("Refresh shared", () -> this.myConfigsView.refreshCloudItems(false));
            if (this.cloudApi.isProfileReady()) {
               this.getModules(this.myConfigsView.experimental ? "Regular storage" : "Experimental storage", this::switchTab);
            }
         }

         if (!this.shareCode.isEmpty()) {
            this.getModules("Copy share code", () -> {
               this.getMinecraft().keyboardHandler.setClipboard(this.shareCode);
               this.statusMessage = "Share code copied to clipboard.";
            });
         }
      } else {
         this.getModules("Refresh", this.discoverView::loadPage);
         this.getModules("Previous", () -> {
            if (this.discoverView.offset > 0) {
               this.discoverView.offset = Math.max(0, this.discoverView.offset - 30);
               this.discoverView.loadPage();
            }
         });
         this.getModules("Next", () -> {
            if (this.discoverView.offset + 30 < this.discoverView.total) {
               this.discoverView.offset += 30;
               this.discoverView.loadPage();
            }
         });
         this.toolbarButtons.get(1).enabled = (boolean)(this.discoverView.offset > 0 ? 1 : 0);
         this.toolbarButtons.get(2).enabled = (boolean)(this.discoverView.offset + 30 < this.discoverView.total ? 1 : 0);
      }

      if (this.discoverTab) {
         this.discoverView.buildList();
      } else {
         this.myConfigsView.renderConfigList();
      }

      if (this.showingSharedMenu) {
         this.setDialogText("Shared configs", "");
         this.option("Refresh shared", "Add your uploaded configs to this list", () -> {
            this.showingSharedMenu = (boolean)0;
            this.myConfigsView.refreshCloudItems(false);
         });
         if (this.cloudApi.isProfileReady()) {
            this.option(
               this.myConfigsView.experimental ? "Regular storage" : "Experimental storage",
               this.myConfigsView.experimental ? "Show your regular shared configs" : "Show configs in experimental storage",
               () -> {
                  this.showingSharedMenu = (boolean)0;
                  this.switchTab();
               }
            );
         }
      } else if (this.confirmAction != null) {
         int var1 = this.confirmMessage.indexOf(10);
         this.setDialogText(var1 < 0 ? this.confirmMessage : this.confirmMessage.substring(0, var1), "");
         if (var1 >= 0) {
            this.text(this.confirmMessage.substring(var1 + 1));
         }

         this.addGuardedDialogButton(this.cancelLabel, () -> {
            this.confirmAction = this.secondaryAction = null;
            this.build();
         });
         if (this.secondaryAction != null) {
            this.addGuardedDialogButton(this.secondaryLabel, () -> this.addConfigCard(this.secondaryAction));
         }

         MenuButton var2 = this.addGuardedDialogButton(this.confirmLabel, () -> this.addConfigCard(this.confirmAction));
         var2.accented = (boolean)1;
         var2.danger = this.confirmDestructive;
      } else if (this.myConfigsView.importPreview != null) {
         this.myConfigsView.renderImportPreview();
      } else if (this.importing) {
         this.setDialogText("Import config", "Paste a share code. Nothing loads until you confirm.");
         this.importField = this.myConfigsView.shareCodeField();
         this.addGuardedDialogButton("Cancel", () -> {
            this.importing = (boolean)0;
            this.build();
         });
         this.addGuardedDialogButton(
               "Fetch preview", () -> this.myConfigsView.importByCode(ApiResponseValidator.normalizeShareCode(this.myConfigsView.shareCodeInput))
            )
            .accented = (boolean)1;
      } else if (!this.nameMode.isEmpty()) {
         switch (this.nameMode) {
            case "New":
               this.setDialogText("Save as new config", "Saves your current settings. Nothing is loaded.");
               break;
            case "Duplicate":
               this.setDialogText("Duplicate “" + this.myConfigsView.selectedLocal + "”", "Name the copy.");
               break;
            default:
               this.setDialogText(this.nameMode + " “" + this.myConfigsView.selectedLocal + "”", "");
         }

         this.nameField = this.addTextField("Config name", this.nameInput, 32, var1x -> this.nameInput = var1x);
         this.addGuardedDialogButton("Cancel", () -> {
            this.nameMode = "";
            this.build();
         });
         this.addGuardedDialogButton(this.nameMode.equals("Rename") ? "Rename" : "Save", this::submitName).accented = (boolean)1;
      } else if (this.discoverTab) {
         this.discoverView.buildDetail();
      } else {
         this.myConfigsView.renderDetails();
      }

      this.title = this.discoverTab ? "Discover" : "My Configs";

      for (Widget var6 : this.widgets) {
         if (!(var6 instanceof SectionLabel)) {
            var6.enabled = (boolean)(var6.enabled & (!this.loading ? 1 : 0));
         }
      }

      if (this.frame != null) {
         this.layout();
      }
   }

   private void switchTab() {
      this.myConfigsView.experimental = (boolean)(!this.myConfigsView.experimental ? 1 : 0);
      this.myConfigsView.clearImportSelection();
      this.myConfigsView.cloudItems = List.of();
      this.myConfigsView.refreshCloudItems(false);
   }

   void addCardAction() {
      this.getModules();
   }

   private void getModules() {
      int var1 = this.searchField != null && this.searchField.focused ? 1 : 0;
      this.cardScroll = 0;
      this.build();
      if (var1 != 0) {
         this.addLabeledValue(this.searchField);
      }
   }

   void addConfigCard(String var1, String var2) {
      this.nameMode = var1;
      this.nameInput = var2;
      this.scrollOffset = 0;
      this.build();
      if (this.nameField != null) {
         this.addLabeledValue(this.nameField);
      }
   }

   private void submitName() {
      this.gui.saveGuiState();
      String var1 = ProfileValidator.validateName(this.nameInput);

      try {
         if (this.nameMode.equals("Rename")) {
            this.configManager.renameProfile(this.myConfigsView.selectedLocal, var1);
         } else if (this.nameMode.equals("Duplicate")) {
            this.configManager.copyProfile(this.myConfigsView.selectedLocal, var1);
         } else {
            this.configManager.saveProfileAs(var1, false);
         }

         this.myConfigsView.selectedLocal = var1;
         this.nameMode = "";
         this.statusMessage = "Saved " + var1;
         this.build();
      } catch (IOException var3) {
         this.statusMessage = var3.getMessage();
         LarpLog.warn("Config", "Config {} failed: {}", this.nameMode, var3.toString());
         this.build();
      }
   }

   void switchTab(String var1) {
      if (!this.loading) {
         this.resetReveal();
         this.discoverTab = var1.equals("Discover");
         this.myConfigsView.importPreview = null;
         this.importing = (boolean)0;
         this.showingSharedMenu = (boolean)0;
         this.nameMode = "";
         this.confirmAction = null;
         this.discoverView.clearSelection();
         this.scrollOffset = this.cardScroll = 0;
         this.shareCode = "";
         this.statusMessage = "";
         LarpLog.info("GUI", "Configs switched to {}", this.discoverTab ? "Discover" : "My Configs");
         if (this.discoverTab && !this.discoverView.loaded) {
            this.discoverView.loadPage();
         } else {
            this.build();
         }
      }
   }

   boolean isBrowsingDiscover() {
      return this.discoverTab && !this.dialogOpen;
   }

   void showConfirm(String var1, String var2, Runnable var3) {
      this.showDialog(var1, var2, var3, "", null);
   }

   void addTextField(String var1, String var2, Runnable var3) {
      this.showDialog(var1, var2, var3, "", null);
      this.confirmDestructive = (boolean)1;
      this.build();
   }

   void showDialog(String var1, String var2, Runnable var3, String var4, Runnable var5) {
      this.confirmMessage = var1;
      this.confirmLabel = var2;
      this.confirmAction = var3;
      this.confirmDestructive = (boolean)0;
      this.cancelLabel = "Cancel";
      this.secondaryLabel = var4;
      this.secondaryAction = var5;
      this.scrollOffset = 0;
      this.build();
   }

   void showConfirmWithCancelLabel(String var1, String var2, Runnable var3, String var4) {
      this.showDialog(var1, var2, var3, "", null);
      this.cancelLabel = var4;
      this.build();
   }

   private void addConfigCard(Runnable var1) {
      this.confirmAction = this.secondaryAction = null;
      this.importing = (boolean)0;
      var1.run();
   }

   void runRequest(String var1, CompletableFuture<JsonObject> var2, Consumer<JsonObject> var3) {
      if (this.loading) {
         var2.cancel(true);
      } else {
         this.loading = (boolean)1;
         this.statusMessage = var1;
         this.build();
         BugReportBuilder.logEvent("API", BugReportBuilder.Event.REQUEST_STARTED);
         this.requestRunner.submit(var2, (var3x, var4) -> {
            this.loading = (boolean)0;
            if (var4 != null) {
               BugReportBuilder.logEvent("API", BugReportBuilder.Event.REQUEST_FAILED);
               this.statusMessage = this.cloudApi.describeError(var4);
               LarpLog.warn("API", "Config request failed: {}", this.statusMessage);
               ApiException var5 = ServiceStatusClient.unwrapError(var4);
               if (var5.getErrorCode().equals("REVISION_CONFLICT") && !var1.equals("Loading cloud items...")) {
                  this.myConfigsView.refreshCloudItems(true);
                  return;
               }

               if (var5.getStatusCode() == 403 && this.myConfigsView.experimental) {
                  this.myConfigsView.requestCloudAction();
               }

               if (var5.getStatusCode() == 401 || var5.getStatusCode() == 503 || var5.getErrorCode().equals("NETWORK")) {
                  this.cloudApi.fetchStatus();
               }
            } else {
               try {
                  var3.accept(var3x);
                  BugReportBuilder.logEvent("API", BugReportBuilder.Event.REQUEST_SUCCEEDED);
               } catch (RuntimeException var6) {
                  this.statusMessage = "Invalid server response. Nothing was loaded locally.";
                  LarpLog.warn("API", "Invalid config response: {}", var6.toString());
               }
            }

            this.build();
         });
      }
   }

   @Override
   protected boolean showTitle() {
      return false;
   }

   @Override
   protected boolean contentShown() {
      return this.dialogOpen;
   }

   @Override
   protected int bodyTop() {
      return this.frame.bodyTop() + this.headerHeight;
   }

   private int getRightEdge() {
      return this.frame.right();
   }

   private boolean hasSelection() {
      return this.discoverTab ? this.discoverView.selected != null : this.myConfigsView.selectedCloud != null || this.myConfigsView.selectedLocal != null;
   }

   @Override
   protected void layout() {
      int var1 = this.frame.contentX();
      int var2 = this.frame.right() - var1;
      int var3 = Math.min(68, Math.max(1, var2 - 60));
      int var4 = Math.min(180, Math.max(1, var2 - var3));
      this.searchField.bounds(var1 + var3, this.frame.bodyTop(), var4);
      this.searchField.height = 20;
      FlowLayout.Layout var5 = FlowLayout.layout(
         var2, this.toolbarButtons.stream().map(var0 -> FontRenderer.measureWidth(var0.label, FontRenderer.TextStyle.HELP) + 12).toList()
      );
      this.headerHeight = 27 + var5.height() + 8;

      for (int var6 = 0; var6 < this.toolbarButtons.size(); var6++) {
         MenuButton var7 = this.toolbarButtons.get(var6);
         FlowLayout.Cell var8 = var5.cells().get(var6);
         var7.pill = (boolean)1;
         var7.bounds(var1 + var8.x2(), this.frame.bodyTop() + 27 + var8.y2(), var8.width());
         var7.height = 24;
      }

      super.layout();
      CardGridLayout var12 = CardGridLayout.calculate(var2, this.cards.size());
      int var13 = Math.max(1, this.frame.bottom() - this.bodyTop());
      this.cardsHeight = var12.height();
      this.cardScroll = Math.clamp(this.cardScroll, 0, Math.max(0, this.cardsHeight - var13));
      int var14 = (int)Math.round(this.cardScrollAnimation.animateTo(this.cardScroll, 150));

      for (int var9 = 0; var9 < this.cards.size(); var9++) {
         MenuButton var10 = this.cards.get(var9);
         var10.bounds(var1 + var12.cardX(var9), this.bodyTop() + var12.cardY(var9) - var14, var12.cardWidth());
         var10.height = 48;
         List var11 = this.cardActions.getOrDefault(var10, List.of());
         layoutCardActions(var10, var11);
      }
   }

   static void layoutCardActions(MenuButton var0, List<MenuButton> var1) {
      int var2 = var1.stream().mapToInt(var0x -> var0x.danger ? 36 : 46).sum();
      int var3 = Math.max(0, var1.size() - 1) * 4;
      int var4 = var0.width - MenuButton.iconSize(var0.width) - 6 - var3;
      double var5 = Math.min(1.0, (double)Math.max(1, var4) / Math.max(1, var2));
      int var7 = var1.stream().mapToInt(var2x -> Math.max(1, (int)((var2x.danger ? 36 : 46) * var5))).sum() + var3;
      int var8 = var0.x + var0.width - 6 - var7;

      for (MenuButton var10 : var1) {
         int var11 = Math.max(1, (int)((var10.danger ? 36 : 46) * var5));
         var10.bounds(var8, var0.y + 31, var11);
         var10.height = 14;
         var10.visible = var0.visible;
         var8 += var11 + 4;
      }
   }

   @Override
   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
      FontRenderer.drawText(
         var1,
         FontRenderer.trimToWidth("Configs", Math.max(1, this.searchField.x - this.frame.contentX() - 6), FontRenderer.TextStyle.TITLE),
         this.frame.contentX(),
         this.frame.bodyTop() + 3,
         Theme.textPrimary,
         FontRenderer.TextStyle.TITLE
      );
      this.searchField.render(var1, var2, var3);

      for (MenuButton var5 : this.toolbarButtons) {
         var5.render(var1, var2, var3);
      }

      var1.enableScissor(this.frame.contentX(), this.bodyTop(), this.getRightEdge(), this.frame.bottom());

      try {
         int var12 = var3 >= this.bodyTop() && var3 < this.frame.bottom() ? var2 : -1;

         for (MenuButton var6 : this.cards) {
            if (var6.y + var6.height > this.bodyTop() && var6.y < this.frame.bottom()) {
               var6.render(var1, var12, var3);

               for (MenuButton var8 : this.cardActions.getOrDefault(var6, List.of())) {
                  var8.render(var1, var12, var3);
               }
            }
         }
      } finally {
         var1.disableScissor();
      }

      GlassPainter.drawScrollbar(
         var1,
         this.getRightEdge() - 3,
         this.bodyTop(),
         Math.max(1, this.frame.bottom() - this.bodyTop()),
         this.cardsHeight,
         (int)Math.round(this.cardScrollAnimation.getValue())
      );
      if (this.cards.isEmpty()) {
         FontRenderer.drawText(var1, this.loading ? "Loading..." : "No configs found", this.frame.contentX() + 8, this.bodyTop() + 8, Theme.textSecondary);
      }
   }

   @Override
   protected boolean accepts(Widget var1, double var2, double var4) {
      if (this.cards.contains(var1) && this.cardActions.getOrDefault(var1, List.of()).stream().anyMatch(var4x -> var4x.contains(var2, var4))) {
         return false;
      } else {
         return !this.cards.contains(var1) && !this.cardActions.values().stream().anyMatch(var1x -> var1x.contains(var1))
            ? super.accepts(var1, var2, var4)
            : !this.dialogOpen && var1.contains(var2, var4) && var4 >= this.bodyTop() && var4 < this.frame.bottom() && var2 < this.getRightEdge();
      }
   }

   @Override
   protected void focusChanged(Widget var1) {
      if (!this.cards.contains(var1) && !this.cardActions.values().stream().anyMatch(var1x -> var1x.contains(var1))) {
         super.focusChanged(var1);
      } else {
         if (var1.y < this.bodyTop()) {
            this.cardScroll = this.cardScroll - (this.bodyTop() - var1.y);
         } else if (var1.y + var1.height > this.frame.bottom()) {
            this.cardScroll = this.cardScroll + (var1.y + var1.height - this.frame.bottom());
         }

         this.cardScrollAnimation.snapTo(this.cardScroll);
         this.layout();
      }
   }

   @Override
   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!this.isModalOpen()
         && (this.frame.columns() || !this.hasSelection())
         && var1 >= this.frame.contentX()
         && var1 < this.getRightEdge()
         && var3 >= this.bodyTop()
         && var3 < this.frame.bottom()) {
         this.cardScroll -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return super.mouseScrolled(var1, var3, var5, var7);
      }
   }

   @Override
   protected void dismissDialog() {
      if (this.dialogOpen && !this.loading) {
         if (this.hasSelection()
            && !this.showingSharedMenu
            && this.confirmAction == null
            && this.myConfigsView.importPreview == null
            && !this.importing
            && this.nameMode.isEmpty()) {
            this.myConfigsView.selectedCloud = null;
            this.myConfigsView.selectedLocal = null;
            this.discoverView.clearSelection();
         } else if (this.showingSharedMenu) {
            this.showingSharedMenu = (boolean)0;
         } else if (this.confirmAction != null) {
            this.confirmAction = this.secondaryAction = null;
         } else if (this.myConfigsView.importPreview != null) {
            this.myConfigsView.importPreview = null;
         } else {
            this.importing = (boolean)0;
            this.nameMode = "";
         }

         this.build();
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (KeyCodes.keyOf(var1) == 256 && this.dialogOpen && !this.loading) {
         this.dismissDialog();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void tick() {
      this.discoverView.tickSearch();
   }

   @Override
   void suspend() {
      this.loading = (boolean)0;
      this.requestRunner.cancel();
      super.suspend();
   }
}

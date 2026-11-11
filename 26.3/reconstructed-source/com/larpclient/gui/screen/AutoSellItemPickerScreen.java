package com.larpclient.gui.screen;

import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.MenuButton;
import com.larpclient.gui.widget.TextField;
import com.larpclient.gui.widget.Widget;
import com.larpclient.module.misc.AutoSellModule;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.util.inventory.ItemSelection;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AutoSellItemPickerScreen extends WidgetScreen {
   private final Screen parent;
   private final AutoSellModule module;
   private final boolean excludeMode;
   private final LinkedHashSet<String> draft;
   private final List<AutoSellItemPickerScreen.ItemEntry> allItems = new ArrayList<>();
   private final List<Widget> rowWidgets = new ArrayList<>();
   private List<AutoSellItemPickerScreen.ItemEntry> filteredItems = List.of();
   private TextField searchField;
   private MenuButton inventoryButton;
   private MenuButton selectedButton;
   private MenuButton addHeldButton;
   private MenuButton selectResultsButton;
   private MenuButton clearButton;
   private MenuButton cancelButton;
   private MenuButton doneButton;
   private String query = "";
   private String statusMessage = "";
   private boolean inventoryOnly;
   private boolean selectedOnly;
   private int panelX;
   private int panelY;
   private int panelWidth;
   private int panelHeight;
   private int listTop;
   private int listBottom;
   private int columns;
   private int visibleRows;
   private int scrollRow;
   private Set<String> inventoryItemIds = Set.of();
   private Map<String, Integer> eligibleCounts = Map.of();
   private final Animation openAnimation = new Animation(0.0);

   public AutoSellItemPickerScreen(Screen var1, AutoSellModule var2, boolean var3) {
      super(var3 ? "Never sell" : "Choose sell items");
      this.parent = var1;
      this.module = var2;
      this.excludeMode = var3;
      this.draft = new LinkedHashSet<>(var2.getItemIds(var3));

      for (Item var5 : BuiltInRegistries.ITEM) {
         ItemStack var6 = new ItemStack(var5);
         if (!var6.isEmpty()) {
            String var7 = BuiltInRegistries.ITEM.getKey(var5).toString();
            String var8 = var6.getHoverName().getString();
            this.allItems.add(new AutoSellItemPickerScreen.ItemEntry(var7, var8, var6, (var8 + " " + var7).toLowerCase(Locale.ROOT).replace('_', ' ')));
         }
      }

      for (String var10 : this.draft) {
         if (this.allItems.stream().noneMatch(var1x -> var1x.id2.equals(var10))) {
            this.allItems.add(new AutoSellItemPickerScreen.ItemEntry(var10, var10 + " (unavailable)", ItemStack.EMPTY, var10.replace('_', ' ')));
         }
      }

      this.allItems
         .sort(
            Comparator.comparing(AutoSellItemPickerScreen.ItemEntry::name, String.CASE_INSENSITIVE_ORDER)
               .thenComparing(AutoSellItemPickerScreen.ItemEntry::id2)
         );
      this.refreshInventoryItems();
      this.openAnimation.animateTo(1.0, 200);
      LarpLog.debug("AutoSell", "Opened {} item picker; draft size={}", var3 ? "excluded" : "selected", this.draft.size());
   }

   private void refreshInventoryItems() {
      HashSet var1 = new HashSet();
      if (this.minecraft.player != null) {
         for (int var2 = 0; var2 < 36; var2++) {
            ItemStack var3 = this.minecraft.player.getInventory().getItem(var2);
            if (!var3.isEmpty()) {
               var1.add(BuiltInRegistries.ITEM.getKey(var3.getItem()).toString());
            }
         }
      }

      this.inventoryItemIds = Set.copyOf(var1);
   }

   protected void init() {
      this.clearWidgets();
      this.rowWidgets.clear();
      this.panelWidth = Math.max(1, Math.min(700, this.width - 24));
      this.panelHeight = Math.max(1, Math.min(500, this.height - 20));
      this.panelX = (this.width - this.panelWidth) / 2;
      this.panelY = (this.height - this.panelHeight) / 2;
      this.columns = this.panelWidth >= 440 ? 2 : 1;
      this.listTop = this.panelY + 111;
      this.listBottom = this.panelY + this.panelHeight - 66;
      this.visibleRows = Math.max(1, (this.listBottom - this.listTop) / 34);
      this.searchField = new TextField("Search items or namespace:item...", var1x -> {
         this.query = var1x;
         this.scrollRow = 0;
         this.refilter();
      }, 96);
      this.searchField.text = this.query;
      this.searchField.bounds(this.panelX + 12, this.panelY + 40, this.panelWidth - 24);
      this.searchField.height = 24;
      this.widgets.add(this.searchField);
      this.inventoryButton = this.addButton("In inventory", () -> {
         this.refreshInventoryItems();
         this.inventoryOnly = (boolean)(!this.inventoryOnly ? 1 : 0);
         this.scrollRow = 0;
         this.refilter();
      });
      this.selectedButton = this.addButton("Selected", () -> {
         this.selectedOnly = (boolean)(!this.selectedOnly ? 1 : 0);
         this.scrollRow = 0;
         this.refilter();
      });
      this.addHeldButton = this.addButton("Add held", () -> {
         if (this.minecraft.player != null && !this.minecraft.player.getMainHandItem().isEmpty()) {
            String var1x = BuiltInRegistries.ITEM.getKey(this.minecraft.player.getMainHandItem().getItem()).toString();
            this.applyEdit(() -> {
               LinkedHashSet var2x = new LinkedHashSet<>(this.draft);
               var2x.add(var1x);
               this.setDraft(var2x);
            });
         } else {
            this.statusMessage = "Hold an item first.";
         }
      });
      this.selectResultsButton = this.addButton("Select results", () -> this.applyEdit(() -> {
         LinkedHashSet var1x = new LinkedHashSet<>(this.draft);
         this.filteredItems.forEach(var1xx -> var1x.add(var1xx.id2));
         this.setDraft(var1x);
      }));
      this.clearButton = this.addButton("Clear", () -> this.applyEdit(this.draft::clear));
      this.cancelButton = this.addButton("Cancel", this::onClose);
      this.doneButton = this.addButton("Done", () -> {
         try {
            this.module.setItems(this.excludeMode, this.draft);
            this.module.confirmSale();
            LarpLog.debug("AutoSell", "Saved {} item selection; size={}", this.excludeMode ? "excluded" : "selected", this.draft.size());
            this.minecraft.gui.setScreen(this.parent);
         } catch (IllegalArgumentException var2x) {
            this.statusMessage = var2x.getMessage();
         }
      });
      this.doneButton.accented = (boolean)1;
      int var1 = (this.panelWidth - 32) / 3;
      this.inventoryButton.bounds(this.panelX + 12, this.panelY + 73, var1);
      this.selectedButton.bounds(this.panelX + 16 + var1, this.panelY + 73, var1);
      this.addHeldButton.bounds(this.panelX + 20 + var1 * 2, this.panelY + 73, var1);
      int var2 = this.panelY + this.panelHeight - 31;
      int var3 = (this.panelWidth - 36) / 4;
      this.selectResultsButton.bounds(this.panelX + 12, var2, var3);
      this.clearButton.bounds(this.panelX + 16 + var3, var2, var3);
      this.cancelButton.bounds(this.panelX + 20 + var3 * 2, var2, var3);
      this.doneButton.bounds(this.panelX + 24 + var3 * 3, var2, var3);
      this.refilter();
      this.focusedWidget = this.searchField;
      this.searchField.focused = (boolean)1;
   }

   private MenuButton addButton(String var1, Runnable var2) {
      MenuButton var3 = new MenuButton(var1, var2);
      this.widgets.add(var3);
      return var3;
   }

   private void setDraft(Collection<String> var1) {
      List var2 = ItemSelection.selectedKey(var1);
      this.draft.clear();
      this.draft.addAll(var2);
   }

   private void applyEdit(Runnable var1) {
      try {
         var1.run();
         this.statusMessage = "";
         this.refilter();
      } catch (IllegalArgumentException var3) {
         this.statusMessage = var3.getMessage();
      }
   }

   private void refilter() {
      String[] var1 = this.query.strip().toLowerCase(Locale.ROOT).replace('_', ' ').split("\\s+");
      this.filteredItems = this.allItems
         .stream()
         .filter(var1x -> !this.inventoryOnly || this.inventoryItemIds.contains(var1x.id2))
         .filter(var1x -> !this.selectedOnly || this.draft.contains(var1x.id2))
         .filter(var1x -> Arrays.stream(var1).allMatch(var1x.search::contains))
         .sorted(Comparator.comparing(var1x -> !this.draft.contains(var1x.id2)))
         .toList();
      this.scrollRow = Math.clamp(this.scrollRow, 0, Math.max(0, (this.filteredItems.size() + this.columns - 1) / this.columns - this.visibleRows));
      this.inventoryButton.selected = this.inventoryOnly;
      this.selectedButton.selected = this.selectedOnly;
      this.refreshEligibleCounts();
      this.rebuildRows();
   }

   private void refreshEligibleCounts() {
      ItemSelection.Selection var1 = this.module.getSelection();
      int var2 = !var1.error().isEmpty() ? 1 : 0;
      ItemSelection.Selection var3 = new ItemSelection.Selection(
         var2 != 0 ? ItemSelection.mODES.getFirst() : var1.mode(),
         this.excludeMode ? var1.selected() : this.draft,
         this.excludeMode ? this.draft : var1.excluded(),
         var2 != 0 || var1.hotbar(),
         var2 != 0 || var1.named(),
         var2 != 0 || var1.enchanted(),
         var2 != 0 || var1.filled(),
         ""
      );
      HashMap var4 = new HashMap();
      if (this.minecraft.player != null) {
         for (int var5 = 0; var5 < 36; var5++) {
            ItemSelection.InventoryItem var6 = AutoSellModule.toInventoryItem(this.minecraft.player.getInventory().getItem(var5), var5);
            if (var3.eligible(var6)) {
               var4.merge(var6.itemId(), 1, Integer::sum);
            }
         }
      }

      this.eligibleCounts = Map.copyOf(var4);
   }

   private void rebuildRows() {
      if (this.focusedWidget != null && this.rowWidgets.contains(this.focusedWidget)) {
         this.focusedWidget.blur();
         this.focusedWidget = null;
      }

      this.widgets.removeAll(this.rowWidgets);
      this.rowWidgets.clear();
      int var1 = (this.panelWidth - 28) / this.columns;

      for (int var2 = this.scrollRow * this.columns; var2 < Math.min(this.filteredItems.size(), (this.scrollRow + this.visibleRows) * this.columns); var2++) {
         AutoSellItemPickerScreen.ItemRow var3 = new AutoSellItemPickerScreen.ItemRow(this.filteredItems.get(var2));
         int var4 = var2 - this.scrollRow * this.columns;
         var3.bounds(this.panelX + 12 + var4 % this.columns * var1, this.listTop + var4 / this.columns * 34, var1 - 4);
         var3.height = 30;
         this.rowWidgets.add(var3);
         this.widgets.add(var3);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      double var5 = this.openAnimation.animateTo(1.0, 200);
      DrawUtil.fillRect(var1, 0, 0, this.width, this.height, -1609560044);

      try (AlphaScope var7 = AlphaScope.push(var5)) {
         GlassPainter.drawPanel(var1, this.panelX, this.panelY, this.panelWidth, this.panelHeight, Theme.radiusControl, Theme.background, 0.6);
         FontRenderer.drawText(
            var1, this.excludeMode ? "Never sell" : "Choose sell items", this.panelX + 12, this.panelY + 12, Theme.textPrimary, FontRenderer.TextStyle.TITLE
         );
         String var8 = this.draft.size() + " / 32";
         FontRenderer.drawText(var1, var8, this.panelX + this.panelWidth - 12 - FontRenderer.drawDefaultText(var8), this.panelY + 16, Theme.textSecondary);

         for (Widget var10 : this.widgets) {
            if (!this.rowWidgets.contains(var10)) {
               var10.render(var1, var2, var3);
            }
         }

         var1.enableScissor(this.panelX + 8, this.listTop, this.panelX + this.panelWidth - 8, this.listBottom);

         try {
            for (Widget var20 : this.rowWidgets) {
               var20.render(var1, var2, var3);
            }
         } finally {
            var1.disableScissor();
         }

         if (this.filteredItems.isEmpty()) {
            FontRenderer.drawText(var1, "No matching items", this.panelX + 16, this.listTop + 12, Theme.textSecondary);
         }

         int var19 = (this.filteredItems.size() + this.columns - 1) / this.columns * 34;
         GlassPainter.drawScrollbar(
            var1, this.panelX + this.panelWidth - 7, this.listTop, Math.max(1, this.listBottom - this.listTop), var19, this.scrollRow * 34
         );
         String var21 = !this.statusMessage.isEmpty()
            ? this.statusMessage
            : this.filteredItems.size()
               + " results, "
               + this.eligibleCounts.values().stream().mapToInt(Integer::intValue).sum()
               + " eligible inventory stacks. Escape cancels.";
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(var21, this.panelWidth - 24, FontRenderer.TextStyle.HELP),
            this.panelX + 12,
            this.panelY + this.panelHeight - 50,
            this.statusMessage.isEmpty() ? Theme.textSecondary : -24915,
            FontRenderer.TextStyle.HELP
         );
      }
   }

   @Override
   protected boolean accepts(Widget var1, double var2, double var4) {
      return super.accepts(var1, var2, var4) && (!this.rowWidgets.contains(var1) || var4 >= this.listTop && var4 < this.listBottom);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      return super.mouseClicked(var1, var2);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      if (!(var1 < this.panelX) && !(var1 >= this.panelX + this.panelWidth) && !(var3 < this.listTop) && !(var3 >= this.listBottom)) {
         this.scrollRow = this.scrollRow - (int)Math.signum(var7) * 2;
         this.refilter();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      if (KeyCodes.keyOf(var1) == 256) {
         this.onClose();
         return true;
      } else if (this.focusedWidget != this.searchField
         && (KeyCodes.keyOf(var1) == 266 || KeyCodes.keyOf(var1) == 267 || KeyCodes.keyOf(var1) == 264 || KeyCodes.keyOf(var1) == 265)) {
         this.scrollRow = this.scrollRow
            + (KeyCodes.keyOf(var1) == 266 ? -this.visibleRows : (KeyCodes.keyOf(var1) == 267 ? this.visibleRows : (KeyCodes.keyOf(var1) == 264 ? 1 : -1)));
         this.refilter();
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      LarpLog.debug("AutoSell", "Item picker draft discarded");
      this.minecraft.gui.setScreen(this.parent);
   }

   private record ItemEntry(String id, String name, ItemStack icon, String search) {
      private final String id2;

      private ItemEntry(String id, String name, ItemStack icon, String search) {
         this.id2 = id;
         this.name = name;
         this.icon = icon;
         this.search = search;
      }
   }

   private final class ItemRow extends Widget {
      private final AutoSellItemPickerScreen.ItemEntry entry;
      private final Animation hoverAnimation = new Animation(0.0);

      ItemRow(AutoSellItemPickerScreen.ItemEntry var2) {
         this.entry = var2;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         boolean var4 = AutoSellItemPickerScreen.this.draft.contains(this.entry.id2);
         double var5 = this.hoverAnimation.animateTo(!this.contains(var2, var3) && !this.focused ? 0.0 : 1.0, 160);
         GlassPainter.fillRoundedRect(
            var1,
            this.x,
            this.y,
            this.width,
            this.height,
            Theme.radiusMedium,
            ColorUtil.lerpColor(var4 ? Theme.borderColor : Theme.rowBackground, Theme.cardHoverBackground, var5 * 0.6)
         );
         if (!this.entry.icon.isEmpty()) {
            var1.item(this.entry.icon, this.x + 7, this.y + 7);
         }

         FontRenderer.drawText(var1, FontRenderer.getTextWidth(this.entry.name, this.width - 54), this.x + 29, this.y + 5, Theme.textPrimary);
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(this.entry.id2, this.width - 54, FontRenderer.TextStyle.HELP),
            this.x + 29,
            this.y + 18,
            Theme.textSecondary,
            FontRenderer.TextStyle.HELP
         );
         if (var4) {
            FontRenderer.drawText(var1, "+", this.x + this.width - 15, this.y + 10, Theme.accentAt(this.x));
         }

         if (this.focused && this.focusVisible) {
            GlassPainter.drawFocusRing(var1, this.x, this.y, this.width, this.height, Theme.radiusMedium);
         }

         if (this.contains(var2, var3)) {
            String var7 = this.entry.id2
               + (
                  AutoSellItemPickerScreen.this.inventoryItemIds.contains(this.entry.id2)
                     ? " | " + AutoSellItemPickerScreen.this.eligibleCounts.getOrDefault(this.entry.id2, 0) + " eligible inventory stacks"
                     : ""
               )
               + (
                  AutoSellItemPickerScreen.this.module.getItemIds(!AutoSellItemPickerScreen.this.excludeMode).contains(this.entry.id2)
                     ? (AutoSellItemPickerScreen.this.excludeMode ? " | Also selected for selling; exclusion wins" : " | Excluded from selling")
                     : ""
               );
            var1.setTooltipForNextFrame(Component.literal(var7), var2, var3);
         }
      }

      private void toggleSelection() {
         boolean var1 = this.focusVisible;
         AutoSellItemPickerScreen.this.applyEdit(() -> {
            LinkedHashSet var1x = new LinkedHashSet<>(AutoSellItemPickerScreen.this.draft);
            if (!var1x.remove(this.entry.id2)) {
               var1x.add(this.entry.id2);
            }

            AutoSellItemPickerScreen.this.setDraft(var1x);
         });

         for (Widget var3 : AutoSellItemPickerScreen.this.rowWidgets) {
            if (var3 instanceof AutoSellItemPickerScreen.ItemRow var4 && var4.entry.id2.equals(this.entry.id2)) {
               AutoSellItemPickerScreen.this.focusedWidget = var4;
               var4.focused = (boolean)1;
               var4.focusVisible = var1;
               break;
            }
         }

         GuiSounds.play(GuiSounds.Sound.CLICK);
      }

      @Override
      public boolean click(double var1, double var3, int var5) {
         if (var5 == 0 && this.contains(var1, var3)) {
            this.toggleSelection();
            return true;
         } else {
            return false;
         }
      }

      @Override
      public boolean key(int var1) {
         if (this.focused && (var1 == 257 || var1 == 32)) {
            this.toggleSelection();
            return true;
         } else {
            return false;
         }
      }
   }
}

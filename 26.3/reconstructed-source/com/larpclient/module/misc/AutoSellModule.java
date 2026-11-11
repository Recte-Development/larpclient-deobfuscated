package com.larpclient.module.misc;

import com.larpclient.LarpClient;
import com.larpclient.gui.screen.AutoSellItemPickerScreen;
import com.larpclient.module.player.SellMenuLogic;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.inventory.ItemSelection;
import com.larpclient.util.misc.ClickStepTimer;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class AutoSellModule extends Module {
   private static final int rESPONSE_TIMEOUT_TICKS = 100;
   private static AutoSellModule instance;
   private final TextSetting menuTitle = this.addSetting(new TextSetting("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final NumberSetting minCycleDelay = this.addDelaySetting("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final NumberSetting maxCycleDelay = this.addDelaySetting("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final NumberSetting minClickDelay = this.addDelaySetting(
      "Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1
   );
   private final NumberSetting maxClickDelay = this.addDelaySetting(
      "Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3
   );
   private final NumberSetting menuOpenWait = this.addDelaySetting("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final BooleanSetting randomizeDelays = this.canFitInMenu("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final BooleanSetting showMessages = this.canFitInMenu("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final BooleanSetting showStats = this.canFitInMenu("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final BooleanSetting backgroundMode = this.canFitInMenu("Background Mode", "Archived source preference", true);
   private final ModeSetting itemMode = this.addSetting(
      new ModeSetting(
         "Item Mode",
         "Choose selected items or explicitly allow all inventory items; exclusions always win",
         ItemSelection.mODES.getFirst(),
         ItemSelection.mODES.toArray(String[]::new)
      )
   );
   private final BooleanSetting protectHotbar = this.canFitInMenu("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final BooleanSetting protectNamedItems = this.canFitInMenu("Protect Named Items", "Keep items with a custom name", true);
   private final BooleanSetting protectEnchantedItems = this.canFitInMenu("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final BooleanSetting protectFilledContainers = this.canFitInMenu("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final TextSetting selectionVersion = this.addHiddenTextSetting("Selection Version", "1");
   private final TextSetting selectionError = this.addHiddenTextSetting("Selection Error", "");
   private final List<TextSetting> selectedItemSettings = new ArrayList<>();
   private final List<TextSetting> excludedItemSettings = new ArrayList<>();
   private final ClickStepTimer stepTimer = new ClickStepTimer();
   private Object player;
   private Object level;
   private Object connection;
   private ChestMenu sellMenu;
   private final Map<Integer, ItemStack> controlSlots = new LinkedHashMap<>();
   private Map<SellMenuLogic.ItemKey, Integer> expectedMenuItems = Map.of();
   private Map<SellMenuLogic.ItemKey, Integer> inventoryBeforeConfirm = Map.of();
   private SellMenuLogic.SellSlot pendingTransfer;
   private int pendingSlotIndex;
   private int confirmSlot = -1;
   private int confirmStateId;
   private int acknowledgedTransfers;
   private int confirmationAttempts;
   private int emptyMenuAcks;
   private int menuOpenAttempts;
   private int closeSettleTicks;
   private long startTimeNanos;
   private long selectionRevision;
   private long seenSelectionRevision;
   private boolean awaitingMenu;
   private boolean awaitingSaleResponse;
   private boolean closePacketReceived;
   private ItemSelection.Selection cachedSelection;

   public AutoSellModule() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      instance = this;
      this.minClickDelay.visibleWhen(() -> false);
      this.maxClickDelay.visibleWhen(() -> false);
      this.backgroundMode.visibleWhen(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.selectedItemSettings.add(this.addHiddenTextSetting(ItemSelection.selectedKey(var1), ""));
         this.excludedItemSettings.add(this.addHiddenTextSetting(ItemSelection.excludedKey(var1), ""));
      }

      for (Setting var2 : this.getSettings()) {
         if (ItemSelection.allKeys().contains(var2.getName())) {
            this.watchSetting(var2);
         }
      }
   }

   private void watchSetting(Setting var1) {
      var1.onChange(var1x -> {
         this.cachedSelection = null;
         this.selectionRevision++;
      });
   }

   private TextSetting addHiddenTextSetting(String var1, String var2) {
      TextSetting var3 = this.addSetting(new TextSetting(var1, "Auto Sell item selection data", var2));
      var3.visibleWhen(() -> false);
      return var3;
   }

   private NumberSetting addDelaySetting(String var1, String var2, int var3) {
      return this.addSetting(new NumberSetting(var1, var2, var3, 0.0, 20.0, 1.0));
   }

   private BooleanSetting canFitInMenu(String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting(var1, var2, var3));
   }

   public List<Setting<?>> getMenuSettings() {
      return List.of(this.menuTitle, this.menuOpenWait);
   }

   public List<Setting<?>> getDelaySettings() {
      return List.of(this.minCycleDelay, this.maxCycleDelay, this.minClickDelay, this.maxClickDelay, this.randomizeDelays);
   }

   public List<Setting<?>> getItemKey() {
      return List.of(this.showMessages, this.showStats, this.backgroundMode);
   }

   public List<Setting<?>> getSelectionSettings() {
      return List.of(this.itemMode, this.protectHotbar, this.protectNamedItems, this.protectEnchantedItems, this.protectFilledContainers);
   }

   public ItemSelection.Selection getSelection() {
      if (this.cachedSelection == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (Setting var3 : this.getSettings()) {
            if (ItemSelection.allKeys().contains(var3.getName())) {
               var1.put(var3.getName(), var3.getValue());
            }
         }

         this.cachedSelection = ItemSelection.parse(var1);
      }

      return this.cachedSelection;
   }

   public String getSelectionSummary() {
      ItemSelection.Selection var1 = this.getSelection();
      if (!var1.error().isEmpty()) {
         return var1.error();
      }

      long var2 = var1.selected().stream().filter(var0 -> !BuiltInRegistries.ITEM.containsKey(Identifier.parse(var0))).count();
      return var1.mode()
         + ": "
         + var1.selected().size()
         + " selected, "
         + var1.excluded().size()
         + " excluded"
         + (var2 == 0L ? "" : ", " + var2 + " unavailable")
         + (var1.needsSelection() ? ". Choose items to begin." : "");
   }

   public Set<String> getItemIds(boolean var1) {
      return var1 ? this.getSelection().excluded() : this.getSelection().selected();
   }

   public void setItems(boolean var1, Collection<String> var2) {
      List var3 = ItemSelection.selectedKey(var2);
      List var4 = var1 ? this.excludedItemSettings : this.selectedItemSettings;
      if (!this.getSelection().error().isEmpty()) {
         this.itemMode.setValue(ItemSelection.mODES.getFirst());

         for (TextSetting var6 : this.selectedItemSettings) {
            var6.setText("");
         }

         for (TextSetting var9 : this.excludedItemSettings) {
            var9.setText("");
         }

         this.protectHotbar.setValue(true);
         this.protectNamedItems.setValue(true);
         this.protectEnchantedItems.setValue(true);
         this.protectFilledContainers.setValue(true);
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((TextSetting)var4.get(var8)).setText(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.selectionVersion.setText("1");
      this.selectionError.setText("");
   }

   public void openItemPicker(Screen var1, boolean var2) {
      getMc().gui.setScreen(new AutoSellItemPickerScreen(var1, this, var2));
   }

   public void addHeldItem(boolean var1) {
      if (this.isInGame() && !minecraft.player.getMainHandItem().isEmpty()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.getItemIds(var1));
         var2.add(getLore(minecraft.player.getMainHandItem()));
         this.setItems(var1, var2);
         this.confirmSale();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void clearSelection(boolean var1) {
      this.setItems(var1, Set.of());
      this.confirmSale();
   }

   public void confirmSale() {
      LarpClient var1 = LarpClient.getInstance();
      if (var1 != null && var1.getConfigManager() != null) {
         var1.getConfigManager().saveActiveProfile();
      }
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      }

      ItemSelection.Selection var1 = this.getSelection();
      if (!var1.error().isEmpty()) {
         return "Repair item selection";
      }

      if (var1.needsSelection()) {
         return "Choose items to begin";
      }

      if (this.pendingTransfer != null) {
         return "Waiting for item transfer";
      }

      if (this.awaitingSaleResponse) {
         return "Waiting for sale response";
      }

      return switch (this.stepTimer.getPhase()) {
         case AWAIT_MENU -> "Waiting for Sell menu";
         case MOVE_ITEMS, SETTLE -> "Filling Sell menu";
         case CONFIRM, AWAIT_CLEAR -> "Confirming selected items";
         default -> "Waiting for eligible items";
      };
   }

   @Override
   public void onEnable() {
      this.resetState();
      if (this.isInGame() && minecraft.player.connection != null) {
         this.player = minecraft.player;
         this.level = minecraft.level;
         this.connection = minecraft.player.connection;
         this.startTimeNanos = System.nanoTime();
         this.seenSelectionRevision = this.selectionRevision;
         this.sendMessage(this.getSelection().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         LarpLog.debug(
            "AutoSell",
            "Activated with policy={}, selected={}, excluded={}",
            this.itemMode.getValue(),
            this.getItemIds(false).size(),
            this.getItemIds(true).size()
         );
      }
   }

   @Override
   public void onDisable() {
      if (this.startTimeNanos != 0L && this.showStats.getValue() && this.isInGame()) {
         this.sendChatMessage(
            "Auto Sell stopped: "
               + this.confirmationAttempts
               + " confirmation attempts, "
               + this.emptyMenuAcks
               + " empty-menu acknowledgments, "
               + this.acknowledgedTransfers
               + " acknowledged stack transfers."
         );
      }

      if (this.expectedMenuItems.isEmpty()
         && this.pendingTransfer == null
         && !this.awaitingSaleResponse
         && (this.sellMenu == null || this.getMenuContents(this.sellMenu).isEmpty())) {
         this.closeMenu();
      }

      this.resetState();
   }

   private void resetState() {
      this.stepTimer.reset();
      this.player = this.level = this.connection = null;
      this.sellMenu = null;
      this.controlSlots.clear();
      this.expectedMenuItems = this.inventoryBeforeConfirm = Map.of();
      this.pendingTransfer = null;
      this.confirmSlot = -1;
      this.confirmStateId = this.acknowledgedTransfers = this.confirmationAttempts = this.emptyMenuAcks = this.menuOpenAttempts = this.closeSettleTicks = 0;
      this.awaitingMenu = this.awaitingSaleResponse = this.closePacketReceived = (boolean)0;
      this.startTimeNanos = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && minecraft.player.connection != null) {
         if (minecraft.player != this.player || minecraft.level != this.level || minecraft.player.connection != this.connection) {
            this.resetState();
            this.onEnable();
         } else if (!minecraft.player.isAlive()) {
            this.stopWithWarning("Player is no longer alive; sale cancelled.");
         } else {
            ItemSelection.Selection var1 = this.getSelection();
            if (!var1.error().isEmpty()) {
               this.stopWithWarning(var1.error());
            } else {
               if (this.selectionRevision != this.seenSelectionRevision) {
                  if (this.sellMenu != null || this.awaitingMenu) {
                     this.stopWithWarning("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.seenSelectionRevision = this.selectionRevision;
               }

               ChestMenu var2 = this.getOpenSellMenu();
               if (this.sellMenu != null && var2 != this.sellMenu) {
                  if (this.awaitingSaleResponse && this.closePacketReceived && var2 == null && minecraft.player.containerMenu == minecraft.player.inventoryMenu
                     )
                   {
                     SellMenuLogic.TransferVerdict var12 = SellMenuLogic.classifyTransfer(
                        true,
                        ++this.closeSettleTicks,
                        minecraft.player.containerMenu.getCarried().isEmpty(),
                        this.inventoryBeforeConfirm,
                        this.getInventoryContents()
                     );
                     if (var12 != SellMenuLogic.TransferVerdict.ACCEPT) {
                        if (var12 == SellMenuLogic.TransferVerdict.ACCEPT_BULK) {
                           this.expectedMenuItems = Map.of();
                           this.awaitingSaleResponse = this.closePacketReceived = (boolean)0;
                           this.closeSettleTicks = 0;
                           this.sellMenu = null;
                           this.controlSlots.clear();
                           this.confirmSlot = -1;
                           this.sendMessage("Server closed the menu after confirmation. Checking remaining eligible items.");
                           LarpLog.debug("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.moveItems());
                        } else {
                           this.stopWithWarning("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.stopWithWarning(
                        this.awaitingSaleResponse
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!minecraft.player.containerMenu.getCarried().isEmpty()) {
                  if (this.pendingTransfer != null || this.awaitingSaleResponse) {
                     this.stepTimer.tick();
                     int var14 = this.stepTimer.getTicks();
                     byte var7 = 100;
                     int var6 = var14;
                     if (Integer.compare(var6, var7) >>> 31 == 0) {
                        this.stopWithWarning("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.sellMenu != null || this.awaitingMenu) {
                     this.stopWithWarning("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (minecraft.gui.screen() == null || var2 != null || this.awaitingMenu) {
                  this.stepTimer.tick();
                  if (this.sellMenu != null && !this.awaitingSaleResponse && !this.areControlsIntact(var2)) {
                     this.stopWithWarning("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.stepTimer.getPhase()) {
                        case AWAIT_MENU:
                           if (var2 != null) {
                              this.awaitingMenu = (boolean)0;
                              this.menuOpenAttempts = 0;
                              if (this.validateMenu(var2)) {
                                 this.addHiddenTextSetting(ClickStepTimer.Phase.MOVE_ITEMS, 1);
                              }
                           } else if (this.stepTimer.isTimedOut()) {
                              this.awaitingMenu = (boolean)0;
                              int var13 = ++this.menuOpenAttempts;
                              byte var5 = 3;
                              int var4 = var13;
                              if (Integer.compare(var4, var5) >>> 31 == 0) {
                                 this.stopWithWarning("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.getInventoryContents(3));
                           }
                           break;
                        case MOVE_ITEMS:
                           this.moveItems(var2);
                           break;
                        case SETTLE:
                           if (this.stepTimer.isDelayElapsed()) {
                              this.addHiddenTextSetting(ClickStepTimer.Phase.CONFIRM, 0);
                           }
                           break;
                        case CONFIRM:
                           this.confirmSale(var2);
                           break;
                        case AWAIT_CLEAR:
                           if (!this.stepTimer.isDelayElapsed()) {
                              return;
                           }

                           Map var3 = this.getMenuContents(var2);
                           if (SellMenuLogic.isTransferApplied(
                              this.confirmStateId, var2.getStateId(), var3, this.inventoryBeforeConfirm, this.getInventoryContents()
                           )) {
                              if (!this.areControlsIntact(var2)) {
                                 this.stopWithWarning("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.awaitingSaleResponse = (boolean)0;
                              byte var11 = 1;
                              int var10 = this.emptyMenuAcks;
                              this.emptyMenuAcks = var10 + var11;
                              this.expectedMenuItems = Map.of();
                              this.sendMessage("Server cleared the selected sell items.");
                              if (this.hasEligibleItems()) {
                                 this.addHiddenTextSetting(ClickStepTimer.Phase.MOVE_ITEMS, this.getInventoryContents(1));
                              } else {
                                 this.closeMenu();
                                 this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.moveItems());
                              }
                           } else {
                              int var10000 = this.stepTimer.getTicks();
                              byte var9 = 100;
                              int var8 = var10000;
                              if (Integer.compare(var8, var9) >>> 31 == 0) {
                                 this.stopWithWarning("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                              }
                           }
                           break;
                        case IDLE:
                           if (!this.stepTimer.isDelayElapsed() || var1.needsSelection()) {
                              return;
                           }

                           if (var2 != null && this.hasEligibleItems()) {
                              if (this.validateMenu(var2)) {
                                 this.addHiddenTextSetting(ClickStepTimer.Phase.MOVE_ITEMS, 1);
                              }
                           } else if (var2 == null && this.hasEligibleItems()) {
                              this.addHiddenTextSetting(ClickStepTimer.Phase.SEND_COMMAND, 0);
                           } else {
                              this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.moveItems());
                           }
                           break;
                        case SEND_COMMAND:
                           if (!this.hasEligibleItems()) {
                              this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.moveItems());
                              return;
                           }

                           if (minecraft.gui.screen() == null && minecraft.player.containerMenu == minecraft.player.inventoryMenu) {
                              this.awaitingMenu = (boolean)1;
                              minecraft.player.connection.sendCommand("sell");
                              this.sendMessage("Sent /sell command.");
                              this.addHiddenTextSetting(ClickStepTimer.Phase.AWAIT_MENU, this.getInventoryContents(this.menuOpenWait.getValue().intValue()));
                              break;
                           }

                           return;
                        default:
                           this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.moveItems());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean validateMenu(ChestMenu var1) {
      this.sellMenu = var1;
      this.controlSlots.clear();
      this.confirmSlot = -1;
      int var2 = var1.getRowCount() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         if (!var4.isEmpty() && confirmSale(var4) && var4.getCount() == 1) {
            if (isConfirmButton(var4, var3, var2)) {
               if (this.confirmSlot != -1) {
                  this.toInventoryItem(var1);
                  this.stopWithWarning("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.confirmSlot = var3;
               this.controlSlots.put(var3, var4.copy());
            } else if (SellMenuLogic.isCancelButton(getLore(var4), var4.getHoverName().getString(), var3, var2)) {
               this.controlSlots.put(var3, var4.copy());
            }
         }
      }

      if (this.confirmSlot < 0) {
         this.toInventoryItem(var1);
         this.stopWithWarning("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.getMenuContents(var1).isEmpty()) {
         this.stopWithWarning("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.expectedMenuItems = Map.of();
         this.seenSelectionRevision = this.selectionRevision;
         LarpLog.debug("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.getRowCount(), this.confirmSlot + 1, this.controlSlots.size());
         return true;
      }
   }

   private void toInventoryItem(ChestMenu var1) {
      int var2 = var1.getRowCount() * 9;
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < var2; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty()) {
            var3.append("\n  slot ")
               .append(var4 + 1)
               .append(var4 >= var2 - 9 ? " (bottom)" : "")
               .append(": ")
               .append(var5.getCount())
               .append("x ")
               .append(getLore(var5))
               .append(" '")
               .append(var5.getHoverName().getString())
               .append("' customName=")
               .append(var5.has(DataComponents.CUSTOM_NAME))
               .append(" itemName=")
               .append(var5.has(DataComponents.ITEM_NAME));

            for (String var7 : getLore(var5)) {
               var3.append("\n    | ").append(var7);
            }
         }
      }

      LarpLog.warn(
         "AutoSell", "Rejected {}-row menu titled '{}'; nonempty slots:{}", var1.getRowCount(), this.menuTitle.getValue(), var3.length() == 0 ? " none" : var3
      );
   }

   private void moveItems(ChestMenu var1) {
      if (var1 != null && minecraft.gameMode != null) {
         if (this.pendingTransfer != null) {
            ItemStack var5 = var1.getSlot(this.pendingSlotIndex).getItem();
            int var6 = var5.isEmpty() ? 0 : (getItemKey(var5).equals(this.pendingTransfer.key()) ? var5.getCount() : -1);
            int var4 = this.pendingTransfer.accepted(var1.getStateId(), var6, this.getMenuContents(var1), this.getInventoryContents());
            if (var4 > 0) {
               this.expectedMenuItems = SellMenuLogic.adjustCount(this.expectedMenuItems, this.pendingTransfer.key(), var4);
               this.acknowledgedTransfers++;
               this.pendingTransfer = null;
               if (!this.isMenuContentsValid(var1)) {
                  return;
               }

               this.addHiddenTextSetting(ClickStepTimer.Phase.MOVE_ITEMS, this.getInventoryContents(1));
            } else if (this.stepTimer.getTicks() >= 100) {
               this.stopWithWarning("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.stepTimer.isDelayElapsed() && this.isMenuContentsValid(var1)) {
            for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
               Slot var3 = var1.getSlot(var2);
               if (var3.container == minecraft.player.getInventory()
                  && this.isEligible(var3.getItem(), var3.getContainerSlot())
                  && this.canFitInMenu(var1, var3.getItem())) {
                  if (this.getOpenSellMenu() == var1
                     && minecraft.player.containerMenu == var1
                     && var1.getCarried().isEmpty()
                     && this.selectionRevision == this.seenSelectionRevision
                     && this.areControlsIntact(var1)) {
                     this.pendingSlotIndex = var2;
                     this.pendingTransfer = new SellMenuLogic.SellSlot(
                        var1.getStateId(), getItemKey(var3.getItem()), var3.getItem().getCount(), this.getMenuContents(var1), this.getInventoryContents()
                     );
                     minecraft.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, minecraft.player);
                     this.addHiddenTextSetting(ClickStepTimer.Phase.MOVE_ITEMS, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.expectedMenuItems.isEmpty()) {
               this.closeMenu();
               this.addHiddenTextSetting(ClickStepTimer.Phase.IDLE, this.moveItems());
            } else {
               this.addHiddenTextSetting(ClickStepTimer.Phase.SETTLE, this.getInventoryContents(1));
            }
         }
      }
   }

   private boolean isMenuContentsValid(ChestMenu var1) {
      if (!this.getMenuContents(var1).equals(this.expectedMenuItems)) {
         this.stopWithWarning("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      }

      for (int var2 = 0; var2 < var1.getRowCount() * 9; var2++) {
         if (!this.controlSlots.containsKey(var2)) {
            ItemStack var3 = var1.getSlot(var2).getItem();
            if (!var3.isEmpty() && !this.isEligible(var3, -1)) {
               this.stopWithWarning("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
               return false;
            }
         }
      }

      return true;
   }

   private boolean canFitInMenu(ChestMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.controlSlots.containsKey(var3)) {
            Slot var4 = var1.getSlot(var3);
            ItemStack var5 = var4.getItem();
            if (SellMenuLogic.classifyTransfer(
               var5.isEmpty(), getItemKey(var2).equals(getItemKey(var5)), var4.mayPlace(var2), var5.getCount(), var4.getMaxStackSize(var2)
            )) {
               return true;
            }
         }
      }

      return false;
   }

   private void confirmSale(ChestMenu var1) {
      if (var1 != null && minecraft.gameMode != null && !this.awaitingSaleResponse && !this.expectedMenuItems.isEmpty()) {
         if (this.isMenuContentsValid(var1)
            && this.areControlsIntact(var1)
            && this.getOpenSellMenu() == var1
            && var1.getCarried().isEmpty()
            && minecraft.player.containerMenu == var1
            && this.selectionRevision == this.seenSelectionRevision) {
            this.confirmStateId = var1.getStateId();
            this.inventoryBeforeConfirm = this.getInventoryContents();
            this.awaitingSaleResponse = (boolean)1;
            minecraft.gameMode.handleContainerInput(var1.containerId, this.confirmSlot, 0, ContainerInput.PICKUP, minecraft.player);
            this.confirmationAttempts++;
            this.sendMessage("Sent one confirmation for the selected items.");
            this.addHiddenTextSetting(ClickStepTimer.Phase.AWAIT_CLEAR, this.getInventoryContents(1));
         }
      }
   }

   private boolean areControlsIntact(ChestMenu var1) {
      if (var1 != null && var1 == this.sellMenu && this.confirmSlot >= 0) {
         int var2 = var1.getRowCount() * 9;

         for (Entry var4 : this.controlSlots.entrySet()) {
            ItemStack var5 = var1.getSlot((Integer)var4.getKey()).getItem();
            int var6 = var4.getKey() == this.confirmSlot
               ? (confirmSale(var5) && var5.getCount() == 1 && isConfirmButton(var5, this.confirmSlot, var2) ? 1 : 0)
               : ItemStack.matches((ItemStack)var4.getValue(), var5);
            if (var6 == 0) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean confirmSale(ItemStack var0) {
      return var0.has(DataComponents.CUSTOM_NAME) || var0.has(DataComponents.ITEM_NAME);
   }

   private static List<String> getLore(ItemStack var0) {
      ItemLore var1 = (ItemLore)var0.get(DataComponents.LORE);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (Component var4 : var1.lines()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static boolean isConfirmButton(ItemStack var0, int var1, int var2) {
      return SellMenuLogic.isSellConfirmButtonWithLore(getLore(var0), var0.getHoverName().getString(), getLore(var0), var1, var2);
   }

   private boolean hasEligibleItems() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.isEligible(minecraft.player.getInventory().getItem(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean isEligible(ItemStack var1, int var2) {
      return this.getSelection().eligible(toInventoryItem(var1, var2));
   }

   public static ItemSelection.InventoryItem toInventoryItem(ItemStack var0, int var1) {
      ItemContainerContents var2 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
      BundleContents var3 = (BundleContents)var0.get(DataComponents.BUNDLE_CONTENTS);
      ItemEnchantments var4 = (ItemEnchantments)var0.get(DataComponents.STORED_ENCHANTMENTS);
      int var5 = (var2 == null || !var2.nonEmptyItems().iterator().hasNext()) && (var3 == null || var3.isEmpty()) ? 0 : 1;
      return new ItemSelection.InventoryItem(
         getLore(var0), var0.getCount(), var1, var0.has(DataComponents.CUSTOM_NAME), var0.isEnchanted() || var4 != null && !var4.isEmpty(), (boolean)var5
      );
   }

   private Map<SellMenuLogic.ItemKey, Integer> getMenuContents(ChestMenu var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.controlSlots.containsKey(var3)) {
            addHiddenTextSetting(var2, var1.getSlot(var3).getItem());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<SellMenuLogic.ItemKey, Integer> getInventoryContents() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         addHiddenTextSetting(var1, minecraft.player.getInventory().getItem(var2));
      }

      return Map.copyOf(var1);
   }

   private static void addHiddenTextSetting(Map<SellMenuLogic.ItemKey, Integer> var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         var0.merge(getItemKey(var1), var1.getCount(), Integer::sum);
      }
   }

   public static SellMenuLogic.ItemKey getItemKey(ItemStack var0) {
      ItemStack var1 = var0.copy();
      var1.remove(DataComponents.LORE);
      return new SellMenuLogic.ItemKey(getLore(var1), var1.getComponents());
   }

   private static String getLore(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private ChestMenu getOpenSellMenu() {
      return minecraft.gui.screen() instanceof ContainerScreen var1
            && minecraft.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && ClickStepTimer.namesMatch(var1.getTitle().getString(), this.menuTitle.getValue())
         ? var2
         : null;
   }

   public static boolean isActiveSellMenu(ChestMenu var0) {
      return instance != null && instance.isEnabled()
         ? instance != null && instance.isRuntimeActive() && instance.isInGame() && instance.getOpenSellMenu() == var0
         : false;
   }

   public static void onContainerClosed(int var0) {
      AutoSellModule var1 = instance;
      if (var1 != null && var1.isRuntimeActive() && var1.sellMenu != null && var1.awaitingSaleResponse && var1.sellMenu.containerId == var0) {
         var1.closePacketReceived = (boolean)1;
         var1.closeSettleTicks = 0;
      }
   }

   private void addHiddenTextSetting(ClickStepTimer.Phase var1, int var2) {
      this.stepTimer.setPhase(var1, var2);
   }

   private int getInventoryContents(int var1) {
      return ClickStepTimer.jitterDelay(var1, this.randomizeDelays.getValue(), ThreadLocalRandom.current());
   }

   private int moveItems() {
      return ClickStepTimer.setPhase(
         this.minCycleDelay.getValue().intValue(), this.maxCycleDelay.getValue().intValue(), this.randomizeDelays.getValue(), ThreadLocalRandom.current()
      );
   }

   private void closeMenu() {
      if (this.sellMenu != null
         && this.isInGame()
         && minecraft.player == this.player
         && minecraft.level == this.level
         && minecraft.player.containerMenu == this.sellMenu) {
         minecraft.player.closeContainer();
      }

      this.sellMenu = null;
      this.controlSlots.clear();
      this.confirmSlot = -1;
      this.awaitingMenu = this.awaitingSaleResponse = (boolean)0;
   }

   private void stopWithWarning(String var1) {
      LarpLog.warn("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.sendChatMessage(var1);
      this.disable();
   }

   private void sendMessage(String var1) {
      if (this.showMessages.getValue()) {
         this.sendChatMessage(var1);
      }
   }

   private void sendChatMessage(String var1) {
      if (this.isInGame()) {
         minecraft.gui.chatListener().handleSystemMessage(Component.literal("[Auto Sell] " + var1), false);
      }
   }
}

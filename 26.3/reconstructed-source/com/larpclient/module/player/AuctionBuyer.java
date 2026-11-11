package com.larpclient.module.player;

import com.larpclient.mixin.DialogScreenMixin;
import com.larpclient.module.movement.RocketBooster;
import com.larpclient.render.world.WorldPathView;
import com.larpclient.util.misc.AuctionParser;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.ConfirmationDialog;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;

public final class AuctionBuyer {
   static final int tIMEOUT_TICKS = 100;
   static final int sTABLE_READS = 3;
   static final int cONFIRM_TIMEOUT_TICKS = 60;
   static final int pAGE_TIMEOUT_TICKS = 100;
   static final int mAX_SEARCHES = 12;
   static final int mAX_PAGE_ADVANCES = 5;
   static final int mAX_PURCHASES_PER_STOP = 192;
   static final int mAX_ITEM_PAGES = 16;
   static final int cLICK_COOLDOWN_TICKS = 6;
   static final int mAX_REPLANS = 3;
   private static final List<String> pendingMessages = new ArrayList<>();
   private final String logTag;
   private AuctionBuyer.State state = AuctionBuyer.State.IDLE;
   private AuctionBuyer.Phase phase = AuctionBuyer.Phase.SEARCH;
   private AuctionParser.AuctionItem kind;
   private int wantedCount;
   private int boughtCount;
   private int searchCount;
   private int pageAdvances;
   private int phaseTicks;
   private int stableTicks;
   private int menuHash;
   private int lastClickTick = -1000;
   private int tick;
   private int purchases;
   private long maxPricePer64;
   private long budget;
   private long spent;
   private int containerId = -1;
   private int page = -1;
   private int baselineCount;
   private AuctionBuyer.Listing chosenListing;
   private final Set<String> failedListings = new HashSet<>();
   private String message = "";
   private boolean outOfMoney;
   private boolean searchOpened;
   private Item item;
   private String itemName = "";
   private long maxPricePerListing = -1L;
   private boolean itemMode;
   private boolean scanning;
   private final List<AuctionBuyer.Listing> candidates = new ArrayList<>();
   private final List<AuctionBuyer.Listing> plan = new ArrayList<>();
   private int lastPlanPage;
   private int replanCount;
   private boolean rescanRequested;
   private long cheapestUnitPrice = -1L;

   AuctionBuyer(String var1) {
      this.logTag = var1;
   }

   public static void onChatMessage(Component var0) {
      if (var0 != null) {
         synchronized (pendingMessages) {
            if (pendingMessages.size() < 64) {
               pendingMessages.add(var0.getString());
            }
         }
      }
   }

   public void startKind(AuctionParser.AuctionItem var1, int var2, long var3, long var5) {
      this.item = null;
      this.itemName = "";
      this.maxPricePerListing = -1L;
      this.itemMode = (boolean)0;
      this.begin(var1, var2, var3, var5);
   }

   public void startItem(Item var1, int var2, long var3, long var5, long var7) {
      this.item = var1;
      this.itemName = new ItemStack(var1).getHoverName().getString();
      this.maxPricePerListing = var5;
      this.itemMode = (boolean)1;
      this.begin(null, var2, var3 * 64L, var7);
   }

   private void begin(AuctionParser.AuctionItem var1, int var2, long var3, long var5) {
      this.kind = var1;
      this.wantedCount = var2;
      this.maxPricePer64 = var3;
      this.budget = var5;
      this.boughtCount = this.searchCount = this.pageAdvances = this.purchases = 0;
      this.spent = 0L;
      this.failedListings.clear();
      this.chosenListing = null;
      this.message = "";
      this.containerId = this.page = -1;
      this.searchOpened = (boolean)0;
      this.outOfMoney = (boolean)0;
      this.scanning = (boolean)(this.item != null ? 1 : 0);
      this.rescanRequested = (boolean)0;
      this.candidates.clear();
      this.plan.clear();
      this.lastPlanPage = this.replanCount = 0;
      this.cheapestUnitPrice = -1L;
      synchronized (pendingMessages) {
         pendingMessages.clear();
      }

      this.state = AuctionBuyer.State.RUNNING;
      this.setPhase(AuctionBuyer.Phase.SEARCH);
      if (this.item != null) {
         LarpLog.info(
            this.logTag,
            "Buying {} {} from /ah: at most {} a block, {} a listing, budget {}",
            var2,
            this.describeTarget(),
            AuctionParser.formatPrice(var3 / 64L),
            this.maxPricePerListing < 0L ? "no cap" : AuctionParser.formatPrice(this.maxPricePerListing),
            AuctionParser.formatPrice(var5)
         );
      } else {
         LarpLog.info(
            this.logTag,
            "Buying {} {} from /ah: at most {} per 64, budget {}",
            var2,
            this.describeTarget(),
            AuctionParser.formatPrice(var3),
            AuctionParser.formatPrice(var5)
         );
      }
   }

   public AuctionBuyer.State getState() {
      return this.state;
   }

   public int getBoughtCount() {
      return this.boughtCount;
   }

   public long getSpent() {
      return this.spent;
   }

   public String onChatMessage() {
      return this.message;
   }

   public boolean isOutOfMoney() {
      return this.outOfMoney;
   }

   public boolean isRunning() {
      return this.state == AuctionBuyer.State.RUNNING;
   }

   public long openAuction() {
      return this.cheapestUnitPrice;
   }

   public void abort(String var1) {
      if (this.state == AuctionBuyer.State.RUNNING) {
         this.getSpent(AuctionBuyer.State.FAILED, var1);
      }
   }

   private void setPhase(AuctionBuyer.Phase var1) {
      this.phase = var1;
      this.phaseTicks = 0;
      this.stableTicks = 0;
      this.menuHash = 0;
   }

   private String describeTarget() {
      return this.item != null ? this.itemName : (this.kind == AuctionParser.AuctionItem.FIREWORK ? "Flight Duration 3 rockets" : "XP bottles");
   }

   private String searchCommand() {
      return this.item != null ? AuctionParser.auctionSearchCommand(this.itemName) : AuctionParser.auctionItemCommand(this.kind);
   }

   public AuctionBuyer.State tick() {
      if (this.state != AuctionBuyer.State.RUNNING) {
         return this.state;
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gameMode != null && var1.getConnection() != null) {
         this.tick++;
         this.phaseTicks++;
         if (!var1.player.containerMenu.getCarried().isEmpty()
            && this.phase != AuctionBuyer.Phase.AWAIT_CONFIRM
            && this.phase != AuctionBuyer.Phase.CONFIRM
            && this.tick - this.lastClickTick > 40) {
            return this.getSpent(AuctionBuyer.State.FAILED, "the cursor holds an item");
         }

         Screen var2 = WorldPathView.currentScreen();
         String var3 = var2 == null ? "" : var2.getTitle().getString();
         AbstractContainerMenu var4 = var1.player.containerMenu;
         int var5 = var4 instanceof ChestMenu var6 && var6.getRowCount() == 6 && AuctionParser.parsePageNumber(var3) > 0 ? 1 : 0;
         switch (this.phase) {
            case SEARCH:
               this.openAuction(var1, var2 != null, (boolean)var5);
               break;
            case SCAN:
            case NEXT_PAGE:
               if (var5 == 0) {
                  if (var2 != null && !this.searchOpened) {
                     return this.getSpent(AuctionBuyer.State.FAILED, "another menu opened: " + var3);
                  }

                  if (var2 == null && this.phaseTicks > 50) {
                     this.setPhase(AuctionBuyer.Phase.SEARCH);
                     return this.state;
                  }

                  if (var2 != null) {
                     return this.getSpent(AuctionBuyer.State.FAILED, "an unexpected menu opened: " + var3);
                  }

                  return this.state;
               }

               if (!this.isMenuStable(var4)) {
                  return this.state;
               }

               int var8 = AuctionParser.parsePageNumber(var3);
               if (this.phase == AuctionBuyer.Phase.NEXT_PAGE && var8 <= this.page) {
                  if (this.phaseTicks > 100) {
                     return this.getSpent(AuctionBuyer.State.FAILED, "the next page did not open");
                  }

                  return this.state;
               }

               this.containerId = var4.containerId;
               this.page = var8;
               if (this.phase == AuctionBuyer.Phase.NEXT_PAGE) {
                  this.setPhase(AuctionBuyer.Phase.SCAN);
               }

               this.abort(var1, (ChestMenu)var4, var8);
               break;
            case AWAIT_CONFIRM:
               if (var2 instanceof DialogScreen var7) {
                  this.handleDialog(var1, var7, var3);
               } else {
                  this.handleConfirmScreen(var1, var2 != null, (boolean)var5, var3, var4);
               }
               break;
            case CONFIRM:
               this.awaitDelivery(var1, (boolean)var5, var2 != null, var3);
         }

         return this.state;
      } else {
         return this.getSpent(AuctionBuyer.State.FAILED, "the world went away");
      }
   }

   private void openAuction(Minecraft var1, boolean var2, boolean var3) {
      if (var3 && this.searchOpened && !this.rescanRequested) {
         this.setPhase(AuctionBuyer.Phase.SCAN);
      } else if (this.phaseTicks == 1) {
         this.rescanRequested = (boolean)0;
         if (var2) {
            var1.player.closeContainer();
         }

         if (++this.searchCount > 12 + this.purchases) {
            this.getSpent(AuctionBuyer.State.FAILED, "searched /ah " + this.searchCount + " times without finishing");
         } else {
            this.pageAdvances = 0;
            var1.player.connection.sendCommand(this.searchCommand());
            this.searchOpened = (boolean)1;
            LarpLog.debug(this.logTag, "Sent /{}", this.searchCommand());
         }
      } else if (var3) {
         this.setPhase(AuctionBuyer.Phase.SCAN);
      } else {
         if (this.phaseTicks > 100) {
            this.getSpent(AuctionBuyer.State.FAILED, "/ah did not open");
         }
      }
   }

   private boolean isMenuStable(AbstractContainerMenu var1) {
      int var2 = var1.containerId * 31 + var1.getStateId();

      for (int var3 = 0; var3 < 54 && var3 < var1.slots.size(); var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         var2 = 31 * var2 + ItemStack.hashItemAndComponents(var4) * 7 + var4.getCount();
      }

      if (var2 != this.menuHash) {
         this.menuHash = var2;
         this.stableTicks = 0;
         return false;
      } else {
         return ++this.stableTicks >= 3;
      }
   }

   private void abort(Minecraft var1, ChestMenu var2, int var3) {
      if (this.boughtCount >= this.wantedCount) {
         this.getSpent(AuctionBuyer.State.FINISHED, "");
      } else if (this.purchases >= 192) {
         this.getSpent(AuctionBuyer.State.FINISHED, "at the purchase limit for one stop");
      } else if (this.tick - this.lastClickTick >= 6) {
         if (this.item != null) {
            this.planItemPurchase(var1, var2, var3);
         } else if (countEmptyHotbarSlots(var1) == 0) {
            this.getSpent(
               this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED, "no free hotbar slot for more " + this.describeTarget()
            );
         } else {
            AuctionBuyer.Listing var4 = null;
            long var5 = Long.MAX_VALUE;

            for (int var7 = 0; var7 < 45; var7++) {
               AuctionBuyer.Listing var8 = this.getState(var2, var7, var3);
               if (var8 != null
                  && !this.failedListings.contains(listingKey(var8))
                  && AuctionParser.isPriceWithinLimit(var8.price(), var8.count(), this.maxPricePer64)
                  && (this.budget < 0L || this.spent + var8.price() <= this.budget)
                  && var8.count() <= this.wantedCount - this.boughtCount + 63) {
                  long var9 = var8.price() * 64L / var8.count();
                  if (var9 < var5) {
                     var5 = var9;
                     var4 = var8;
                  }
               }
            }

            if (var4 != null) {
               this.buyListing(var1, var2, var4, var3);
            } else {
               int var11 = this.findButton(var2, AuctionParser.ArrowButton.NEXT);
               if (var11 >= 0 && ++this.pageAdvances <= 5) {
                  this.clickSlot(var1, var2, var11);
                  this.setPhase(AuctionBuyer.Phase.NEXT_PAGE);
               } else {
                  this.getSpent(
                     this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED,
                     this.budget >= 0L && this.spent > 0L
                        ? "no listing fits the price limit and the budget left"
                        : "no listing under the price limit (" + AuctionParser.formatPrice(this.maxPricePer64) + " per 64)"
                  );
               }
            }
         }
      }
   }

   private void planItemPurchase(Minecraft var1, ChestMenu var2, int var3) {
      int var4 = this.freeCapacity(var1);
      int var5 = Math.min(this.wantedCount - this.boughtCount, var4);
      if (var5 <= 0) {
         this.getSpent(
            this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED, "no free inventory room for more " + this.describeTarget()
         );
      } else {
         int var6 = this.findButton(var2, AuctionParser.ArrowButton.NEXT);
         if (this.scanning) {
            long var17 = 0L;

            for (int var18 = 0; var18 < 45; var18++) {
               ItemStack var10 = var2.getSlot(var18).getItem();
               if (!var10.isEmpty()) {
                  var17 = Math.max(var17, AuctionParser.parseSinglePrice(clickSlot(var10)));
               }

               AuctionBuyer.Listing var11 = this.getState(var2, var18, var3);
               if (var11 != null) {
                  long var12 = (var11.price() + var11.count() - 1L) / var11.count();
                  if (this.cheapestUnitPrice < 0L || var12 < this.cheapestUnitPrice) {
                     this.cheapestUnitPrice = var12;
                  }

                  if (AuctionParser.isPriceWithinLimit(var11.price(), var11.count(), this.maxPricePer64)
                     && (this.maxPricePerListing < 0L || var11.price() <= this.maxPricePerListing)
                     && !this.candidates.contains(var11)) {
                     this.candidates.add(var11);
                  }
               }
            }

            int var19 = new ItemStack(this.item).getMaxStackSize();
            long var20 = 0L;

            for (AuctionBuyer.Listing var13 : this.candidates) {
               if (var17 > 0L && var13.price() * var19 <= var17 * var13.count()) {
                  var20 += var13.count();
               }
            }

            if (var20 < var5 && var6 >= 0 && this.pageAdvances < 16) {
               this.pageAdvances++;
               this.clickSlot(var1, var2, var6);
               this.setPhase(AuctionBuyer.Phase.NEXT_PAGE);
            } else {
               this.scanning = (boolean)0;
               this.replanCount++;
               this.plan.clear();
               this.plan.addAll(planListings(this.candidates, var5, var4, this.budget < 0L ? -1L : this.budget - this.spent));
               if (this.plan.isEmpty()) {
                  this.getSpent(this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED, this.freeCapacity());
               } else {
                  this.lastPlanPage = 0;
                  long var22 = 0L;
                  int var14 = 0;

                  for (AuctionBuyer.Listing var16 : this.plan) {
                     this.lastPlanPage = Math.max(this.lastPlanPage, var16.page());
                     var22 += var16.price();
                     var14 += var16.count();
                  }

                  LarpLog.info(
                     this.logTag,
                     "Read {} pages of /ah for {}: {} listings under the caps, cheapest {} each; plan {} for {} in {} listings ({} each){}",
                     var3,
                     this.describeTarget(),
                     this.candidates.size(),
                     this.cheapestUnitPrice < 0L ? "none" : AuctionParser.formatPrice(this.cheapestUnitPrice),
                     var14,
                     AuctionParser.formatPrice(var22),
                     this.plan.size(),
                     AuctionParser.formatPrice((var22 + var14 - 1L) / var14),
                     var14 < var5 ? "; " + var5 + " were needed, the budget covers no more" : ""
                  );
                  this.rescanRequested = (boolean)1;
                  this.setPhase(AuctionBuyer.Phase.SEARCH);
               }
            }
         } else {
            for (int var7 = 0; var7 < 45; var7++) {
               AuctionBuyer.Listing var8 = this.getState(var2, var7, var3);
               if (var8 != null && !this.failedListings.contains(listingKey(var8))) {
                  int var9 = this.findButton(var8);
                  if (var9 >= 0 && (this.budget < 0L || this.spent + var8.price() <= this.budget)) {
                     this.plan.remove(var9);
                     this.buyListing(var1, var2, var8, var3);
                     return;
                  }
               }
            }

            if (!this.plan.isEmpty() && var6 >= 0 && var3 < this.lastPlanPage + 1 && this.pageAdvances < 16) {
               this.pageAdvances++;
               this.clickSlot(var1, var2, var6);
               this.setPhase(AuctionBuyer.Phase.NEXT_PAGE);
            } else if (this.replanCount < 3) {
               LarpLog.info(
                  this.logTag, "{} planned listings of {} were gone; reading /ah again for the {} still needed", this.plan.size(), this.describeTarget(), var5
               );
               this.scanning = (boolean)1;
               this.candidates.clear();
               this.plan.clear();
               this.rescanRequested = (boolean)1;
               this.setPhase(AuctionBuyer.Phase.SEARCH);
            } else {
               this.getSpent(
                  this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED,
                  "the listings kept changing; bought " + this.boughtCount + " of " + this.wantedCount
               );
            }
         }
      }
   }

   private String freeCapacity() {
      return this.budget >= 0L && this.spent > 0L
         ? "no listing fits the price limit and the budget left"
         : "no listing under "
            + AuctionParser.formatPrice(this.maxPricePer64 / 64L)
            + " per block"
            + (this.maxPricePerListing >= 0L ? " and " + AuctionParser.formatPrice(this.maxPricePerListing) + " per listing" : "")
            + (this.cheapestUnitPrice >= 0L ? " (the cheapest was " + AuctionParser.formatPrice(this.cheapestUnitPrice) + " each)" : "");
   }

   private int findButton(AuctionBuyer.Listing var1) {
      for (int var2 = 0; var2 < this.plan.size(); var2++) {
         if (this.plan.get(var2).count() == var1.count() && this.plan.get(var2).price() == var1.price()) {
            return var2;
         }
      }

      return -1;
   }

   private void buyListing(Minecraft var1, ChestMenu var2, AuctionBuyer.Listing var3, int var4) {
      this.chosenListing = var3;
      this.baselineCount = this.openAuction(var1);
      this.clickSlot(var1, var2, var3.slot());
      LarpLog.info(
         this.logTag, "Buying {} {} for {} (page {}, slot {})", var3.count(), this.describeTarget(), AuctionParser.formatPrice(var3.price()), var4, var3.slot()
      );
      this.setPhase(AuctionBuyer.Phase.AWAIT_CONFIRM);
   }

   static List<AuctionBuyer.Listing> planListings(List<AuctionBuyer.Listing> var0, int var1, int var2, long var3) {
      var1 = Math.min(var1, var2);
      if (var1 > 0 && !var0.isEmpty()) {
         long var5 = 2305843009213693951L;
         int var7 = var0.size();
         long[] var8 = new long[var2 + 1];
         Arrays.fill(var8, 2305843009213693951L);
         var8[0] = 0L;
         boolean[][] var9 = new boolean[var7][var2 + 1];

         for (int var10 = 0; var10 < var7; var10++) {
            int var11 = ((AuctionBuyer.Listing)var0.get(var10)).count();
            long var12 = ((AuctionBuyer.Listing)var0.get(var10)).price();
            if (var11 > 0 && var11 <= var2 && var12 > 0L) {
               for (int var14 = var2; var14 >= var11; var14--) {
                  if (var8[var14 - var11] != 2305843009213693951L) {
                     long var15 = var8[var14 - var11] + var12;
                     if (var15 < var8[var14]) {
                        var8[var14] = var15;
                        var9[var10][var14] = (boolean)1;
                     }
                  }
               }
            }
         }

         int var18 = -1;

         for (int var19 = var1; var19 <= var2; var19++) {
            if (var8[var19] < 2305843009213693951L && (var3 < 0L || var8[var19] <= var3) && (var18 < 0 || var8[var19] < var8[var18])) {
               var18 = var19;
            }
         }

         if (var18 < 0) {
            for (int var20 = var1 - 1; var20 > 0; var20--) {
               if (var8[var20] < 2305843009213693951L && (var3 < 0L || var8[var20] <= var3)) {
                  var18 = var20;
                  break;
               }
            }
         }

         if (var18 < 0) {
            return List.of();
         }

         ArrayList var21 = new ArrayList();
         int var22 = var7 - 1;

         for (int var13 = var18; var22 >= 0 && var13 > 0; var22--) {
            if (var9[var22][var13]) {
               var21.add((AuctionBuyer.Listing)var0.get(var22));
               var13 -= ((AuctionBuyer.Listing)var0.get(var22)).count();
            }
         }

         var21.sort(Comparator.comparingInt(AuctionBuyer.Listing::page).thenComparingInt(AuctionBuyer.Listing::slot));
         return var21;
      } else {
         return List.of();
      }
   }

   private int freeCapacity(Minecraft var1) {
      int var2 = new ItemStack(this.item).getMaxStackSize();
      int var3 = 0;
      Inventory var4 = var1.player.getInventory();

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var4.getItem(var5);
         if (var6.isEmpty()) {
            var3 += var2;
         } else if (this.planListings(var6) && var6.getComponentsPatch().isEmpty()) {
            var3 += Math.max(0, var2 - var6.getCount());
         }
      }

      return var3;
   }

   AuctionBuyer.Listing getState(ChestMenu var1, int var2, int var3) {
      ItemStack var4 = var1.getSlot(var2).getItem();
      if (!this.planListings(var4)) {
         return null;
      }

      long var5 = AuctionParser.parseSinglePrice(clickSlot(var4));
      return var5 <= 0L ? null : new AuctionBuyer.Listing(var2, var4.getCount(), var5, var3);
   }

   private boolean planListings(ItemStack var1) {
      if (var1.isEmpty()) {
         return false;
      } else if (this.item != null) {
         ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         return var1.is(this.item) && (var2 == null || var2.nonEmptyItemCopyStream().findAny().isEmpty());
      } else {
         return this.kind != AuctionParser.AuctionItem.FIREWORK
            ? var1.is(Items.EXPERIENCE_BOTTLE)
            : var1.is(Items.FIREWORK_ROCKET) && RocketBooster.isPlainRocket(var1) && RocketBooster.flightDuration(var1) == 3;
      }
   }

   private static List<String> clickSlot(ItemStack var0) {
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

   private static String listingKey(AuctionBuyer.Listing var0) {
      return var0.page() + ":" + var0.slot() + ":" + var0.count() + ":" + var0.price();
   }

   private int findButton(ChestMenu var1, AuctionParser.ArrowButton var2) {
      int var3 = -1;

      for (int var4 = 45; var4 < 54; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty()
            && AuctionParser.classifyArrowButton(BuiltInRegistries.ITEM.getKey(var5.getItem()).toString(), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -1;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void handleConfirmScreen(Minecraft var1, boolean var2, boolean var3, String var4, AbstractContainerMenu var5) {
      if (!this.checkChatReplies()) {
         if (this.openAuction(var1) > this.baselineCount) {
            this.setPhase(AuctionBuyer.Phase.CONFIRM);
         } else if (var3) {
            if (this.phaseTicks > 60) {
               this.failedListings.add(listingKey(this.chosenListing));
               LarpLog.info(this.logTag, "No confirm screen for that listing; trying another");
               this.setPhase(AuctionBuyer.Phase.SCAN);
            }
         } else if (!var2) {
            if (this.phaseTicks > 60) {
               this.getSpent(AuctionBuyer.State.FAILED, "the confirm screen never opened");
            }
         } else if (!this.isMenuStable(var5)) {
            if (this.phaseTicks > 60) {
               this.getSpent(AuctionBuyer.State.FAILED, "the confirm screen kept changing");
            }
         } else if (this.tick - this.lastClickTick >= 6) {
            int var6 = -1;
            int var7 = 0;
            byte var8 = 0;
            String var9 = null;

            for (int var10 = 0; var10 < var5.slots.size(); var10++) {
               Slot var11 = var5.getSlot(var10);
               if (var11.container != var1.player.getInventory()) {
                  ItemStack var12 = var11.getItem();
                  if (!var12.isEmpty()) {
                     String var13 = var12.getHoverName().getString();
                     String var14 = BuiltInRegistries.ITEM.getKey(var12.getItem()).toString();
                     if (AuctionParser.normalizeText(var13).contains("sell")) {
                        this.getSpent(AuctionBuyer.State.FAILED, "the confirm screen has a selling button: " + var4);
                        return;
                     }

                     if (this.planListings(var12)) {
                        long var15 = AuctionParser.parseSinglePrice(clickSlot(var12));
                        if (var12.getCount() != this.chosenListing.count() || var15 >= 0L && var15 != this.chosenListing.price()) {
                           this.getSpent(
                              AuctionBuyer.State.FAILED,
                              "the confirm screen shows " + var12.getCount() + " for " + AuctionParser.formatPrice(var15) + ", not the chosen listing"
                           );
                           return;
                        }

                        var8 = 1;
                     } else {
                        AuctionParser.ConfirmButton var17 = AuctionParser.classifyConfirmButton(var14, var13);
                        if (var17 == AuctionParser.ConfirmButton.CANCEL) {
                           var7++;
                        }

                        if (var17 == AuctionParser.ConfirmButton.CONFIRM) {
                           String var16 = var14 + "|" + AuctionParser.normalizeText(var13);
                           if (var9 != null && !var9.equals(var16)) {
                              this.getSpent(AuctionBuyer.State.FAILED, "the confirm screen has two different confirm buttons: " + var4);
                              return;
                           }

                           var9 = var16;
                           if (var6 < 0) {
                              var6 = var10;
                           }
                        }
                     }
                  }
               }
            }

            if (var8 != 0 && var6 >= 0 && var7 != 0) {
               this.clickSlot(var1, var5, var6);
               LarpLog.debug(this.logTag, "Confirmed on '{}' slot {}", var4, var6);
               this.setPhase(AuctionBuyer.Phase.CONFIRM);
            } else {
               LarpLog.info(
                  this.logTag,
                  "Unrecognized confirm screen '{}': listing shown {}, confirm slot {}, cancel buttons {}",
                  var4,
                  Boolean.valueOf((boolean)var8),
                  var6,
                  var7
               );
               this.getSpent(AuctionBuyer.State.FAILED, "the confirm screen was not recognized (" + var4 + ")");
            }
         }
      }
   }

   private void handleDialog(Minecraft var1, DialogScreen<?> var2, String var3) {
      if (!this.checkChatReplies()) {
         if (this.tick - this.lastClickTick >= 6) {
            Dialog var4 = ((DialogScreenMixin)var2).larp$dialog();
            ActionButton var5 = null;
            if (var4 instanceof ConfirmationDialog var6) {
               if (AuctionParser.isConfirmText(var6.yesButton().button().label().getString())) {
                  var5 = var6.yesButton();
               }
            } else if (var4 instanceof MultiActionDialog var7 && var7.actions().size() == 2) {
               String var8 = ((ActionButton)var7.actions().get(0)).button().label().getString();
               String var9 = ((ActionButton)var7.actions().get(1)).button().label().getString();
               if (AuctionParser.isConfirmText(var8) && AuctionParser.auctionSearchCommand(var9)) {
                  var5 = (ActionButton)var7.actions().get(0);
               } else if (AuctionParser.auctionSearchCommand(var8) && AuctionParser.isConfirmText(var9)) {
                  var5 = (ActionButton)var7.actions().get(1);
               }
            }

            if (var4 != null && var5 != null) {
               CommonDialogData var17 = var4.common();
               String var18 = AuctionParser.normalizeText(var17.title().getString());
               if (!AuctionParser.isBuyConfirmTitle(var18)) {
                  this.getSpent(AuctionBuyer.State.FAILED, "an unexpected dialog opened: " + var17.title().getString());
               } else if (!var17.inputs().isEmpty()) {
                  this.getSpent(AuctionBuyer.State.FAILED, "the purchase dialog asks for input");
               } else {
                  int var19 = 0;
                  long var20 = -1L;

                  for (DialogBody var12 : var17.body()) {
                     if (var12 instanceof ItemBody var13) {
                        ItemStack var21 = var13.item().create();
                        if (!this.planListings(var21) || var21.getCount() != this.chosenListing.count()) {
                           this.getSpent(
                              AuctionBuyer.State.FAILED,
                              "the purchase dialog shows " + var21.getCount() + " " + var21.getHoverName().getString() + ", not the chosen listing"
                           );
                           return;
                        }

                        var19++;
                        if (var13.description().isPresent()) {
                           var20 = Math.max(var20, AuctionParser.parseSinglePrice(List.of(((PlainMessage)var13.description().get()).contents().getString())));
                        }
                     } else if (var12 instanceof PlainMessage var14) {
                        long var15 = AuctionParser.parseSinglePrice(List.of(var14.contents().getString()));
                        if (var15 >= 0L) {
                           var20 = var15;
                        }
                     }
                  }

                  if (var19 == 1 && var20 == this.chosenListing.price()) {
                     var2.runAction(var5.action().flatMap(var0 -> var0.createAction(Map.of())));
                     this.lastClickTick = this.tick;
                     LarpLog.info(
                        this.logTag,
                        "Answered Yes to '{}' for {} {} at {}",
                        var17.title().getString(),
                        this.chosenListing.count(),
                        this.describeTarget(),
                        AuctionParser.formatPrice(this.chosenListing.price())
                     );
                     this.setPhase(AuctionBuyer.Phase.CONFIRM);
                  } else {
                     LarpLog.info(
                        this.logTag,
                        "Purchase dialog '{}' did not match: items {}, price {} for listing {}",
                        var17.title().getString(),
                        var19,
                        var20,
                        this.chosenListing
                     );
                     this.getSpent(AuctionBuyer.State.FAILED, "the purchase dialog did not show the chosen listing and its price");
                  }
               }
            } else {
               LarpLog.info(this.logTag, "Unrecognized dialog '{}': {}", var3, var4 == null ? "none" : var4.getClass().getSimpleName() + " " + var4);
               this.getSpent(AuctionBuyer.State.FAILED, "an unexpected dialog opened: " + var3);
            }
         }
      }
   }

   private void awaitDelivery(Minecraft var1, boolean var2, boolean var3, String var4) {
      if (!this.checkChatReplies()) {
         int var5 = this.openAuction(var1);
         if (var5 >= this.baselineCount + this.chosenListing.count() || var5 > this.baselineCount && this.phaseTicks > 20) {
            int var6 = var5 - this.baselineCount;
            this.boughtCount += var6;
            this.purchases++;
            this.spent = this.spent + this.chosenListing.price();
            this.failedListings.add(listingKey(this.chosenListing));
            LarpLog.info(
               this.logTag,
               "Bought {} {} for {}; {} of {} wanted, {} spent",
               var6,
               this.describeTarget(),
               AuctionParser.formatPrice(this.chosenListing.price()),
               this.boughtCount,
               this.wantedCount,
               AuctionParser.formatPrice(this.spent)
            );
            this.chosenListing = null;
            if (this.boughtCount >= this.wantedCount) {
               this.getSpent(AuctionBuyer.State.FINISHED, "");
            } else {
               this.setPhase(var2 ? AuctionBuyer.Phase.SCAN : AuctionBuyer.Phase.SEARCH);
            }
         } else if (var3 && !var2 && this.phaseTicks > 10 && !AuctionParser.normalizeText(var4).isEmpty()) {
            if (this.phaseTicks > 100) {
               this.getSpent(AuctionBuyer.State.FAILED, "the purchase was not confirmed (still on " + var4 + ")");
            }
         } else {
            if (this.phaseTicks > 100) {
               this.getSpent(AuctionBuyer.State.FAILED, "the items never arrived after confirming");
            }
         }
      }
   }

   private boolean checkChatReplies() {
      List var1;
      synchronized (pendingMessages) {
         var1 = List.copyOf(pendingMessages);
         pendingMessages.clear();
      }

      for (String var3 : var1) {
         AuctionParser.ChatResult var4 = AuctionParser.classifyChatMessage(var3);
         if (var4 != AuctionParser.ChatResult.IRRELEVANT) {
            LarpLog.info(this.logTag, "Auction reply ({}): {}", var4, var3);
            switch (var4) {
               case ALREADY_SOLD:
                  this.failedListings.add(listingKey(this.chosenListing));
                  this.chosenListing = null;
                  this.setPhase(AuctionBuyer.Phase.SEARCH);
                  return true;
               case INSUFFICIENT_FUNDS:
                  this.outOfMoney = (boolean)1;
                  this.getSpent(this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED, "not enough money");
                  return true;
               case INVENTORY_FULL:
                  this.getSpent(this.boughtCount > 0 ? AuctionBuyer.State.FINISHED : AuctionBuyer.State.FAILED, "the inventory is full");
                  return true;
               case PURCHASED:
                  long var5 = AuctionParser.parseFirstPrice(var3);
                  if (this.chosenListing != null && var5 > this.chosenListing.price()) {
                     this.getSpent(
                        AuctionBuyer.State.FAILED,
                        "the receipt says "
                           + AuctionParser.formatPrice(var5)
                           + ", more than the listing's "
                           + AuctionParser.formatPrice(this.chosenListing.price())
                     );
                     return true;
                  }
            }
         }
      }

      return false;
   }

   private void clickSlot(Minecraft var1, AbstractContainerMenu var2, int var3) {
      ItemStack var4 = var2.getSlot(var3).getItem();
      if (AuctionParser.normalizeText(var4.getHoverName().getString()).contains("sell")) {
         this.getSpent(AuctionBuyer.State.FAILED, "refused to click a selling button");
      } else {
         var1.gameMode.handleContainerInput(var2.containerId, var3, 0, ContainerInput.PICKUP, var1.player);
         this.lastClickTick = this.tick;
      }
   }

   private int openAuction(Minecraft var1) {
      int var2 = 0;
      Inventory var3 = var1.player.getInventory();

      for (int var4 = 0; var4 < 36; var4++) {
         if (this.planListings(var3.getItem(var4))) {
            var2 += var3.getItem(var4).getCount();
         }
      }

      if (this.planListings(var1.player.getOffhandItem())) {
         var2 += var1.player.getOffhandItem().getCount();
      }

      return var2;
   }

   private static int countEmptySlots(Minecraft var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (var0.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private static int countEmptyHotbarSlots(Minecraft var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private AuctionBuyer.State getSpent(AuctionBuyer.State var1, String var2) {
      this.state = var1;
      this.message = var2 == null ? "" : var2;
      Minecraft var3 = Minecraft.getInstance();
      if (this.searchOpened
         && var3.player != null
         && var3.player.containerMenu != var3.player.inventoryMenu
         && var3.player.containerMenu.getCarried().isEmpty()) {
         var3.player.closeContainer();
      }

      this.searchOpened = (boolean)0;
      LarpLog.info(
         this.logTag,
         "Buying {} ended: {}{}; bought {} for {}",
         this.describeTarget(),
         var1,
         this.message.isEmpty() ? "" : " (" + this.message + ")",
         this.boughtCount,
         AuctionParser.formatPrice(this.spent)
      );
      return this.state;
   }

   record Listing(int slot, int count, long price, int page) {
   }

   private enum Phase {
      SEARCH,
      SCAN,
      NEXT_PAGE,
      AWAIT_CONFIRM,
      CONFIRM;
   }

   public enum State {
      IDLE,
      RUNNING,
      FINISHED,
      FAILED;
   }
}

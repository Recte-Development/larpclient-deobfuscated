package com.larpclient.util.inventory;

import com.larpclient.util.data.ActionConfirmationTracker;
import com.larpclient.util.misc.RigLog;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;

final class ContainerSwapLedger {
   final AbstractContainerMenu menu;
   final List<ItemStack> serverSlots = new ArrayList<>();
   private final List<ItemStack> expectedSlots = new ArrayList<>();
   private final Set<Integer> touchedSlots = new HashSet<>();
   private final ActionConfirmationTracker ackTracker = new ActionConfirmationTracker();
   private ItemStack serverCursor = ItemStack.EMPTY;
   private ItemStack expectedCursor = ItemStack.EMPTY;
   private InventoryClickPlan.ClickBatch surplusBatch;
   private BiConsumer<Integer, Integer> clickSender;
   private boolean surplusReturn;
   private long revision;
   private int sourceSlot;
   private int destinationSlot;
   boolean pending;
   boolean clicking;
   boolean probing;
   boolean hasSnapshot;
   boolean issued;
   String abortReason;
   private final long createdNanos = System.nanoTime();
   private final ArrayDeque<String> events = new ArrayDeque<>();

   void logEvent(String var1) {
      if (this.events.size() == 48) {
         this.events.removeFirst();
      }

      this.events.addLast((System.nanoTime() - this.createdNanos) / 1000000L + "ms " + var1);
   }

   String describeState() {
      return "sync="
         + this.menu.containerId
         + " revision="
         + this.menu.getStateId()
         + " pending="
         + this.pending
         + " ack["
         + this.ackTracker.describe()
         + "] source{"
         + this.describeSlot(this.sourceSlot)
         + "} destination{"
         + this.describeSlot(this.destinationSlot)
         + "} "
         + this.describeSlot(-1)
         + " events="
         + this.events;
   }

   ContainerSwapLedger(AbstractContainerMenu var1) {
      this.menu = var1;
   }

   ContainerSwapLedger copy() {
      ContainerSwapLedger var1 = new ContainerSwapLedger(this.menu);
      if (this.hasSnapshot) {
         var1.setSnapshot(this.serverSlots, this.serverCursor);
      }

      return var1;
   }

   void setSnapshot(List<ItemStack> var1, ItemStack var2) {
      if (var1.size() == this.menu.slots.size()) {
         this.serverSlots.clear();

         for (ItemStack var4 : var1) {
            this.serverSlots.add(var4.copy());
         }

         this.serverCursor = var2.copy();
         this.hasSnapshot = (boolean)1;
         this.ackTracker.recordSync(this.menu, ++this.revision, true, -1, true);
      }
   }

   void updateSlot(int var1, ItemStack var2) {
      if (this.hasSnapshot) {
         if (var1 == -1) {
            this.serverCursor = var2.copy();
         } else {
            if (var1 < 0 || var1 >= this.serverSlots.size()) {
               return;
            }

            this.serverSlots.set(var1, var2.copy());
         }

         this.ackTracker.recordSync(this.menu, ++this.revision, false, var1, var1 == -1);
      }
   }

   boolean isLedgerConsistent() {
      if (this.hasSnapshot
         && ItemStackMatcher.matchesCount(this.serverCursor, this.expectedCursor)
         && ItemStackMatcher.matchesCount(this.menu.getCarried(), this.expectedCursor)) {
         for (int var1 = 0; var1 < this.serverSlots.size(); var1++) {
            if (!ItemStackMatcher.matchesCount(this.serverSlots.get(var1), this.menu.getSlot(var1).getItem())
               || this.touchedSlots.contains(var1) && !ItemStackMatcher.matchesCount(this.serverSlots.get(var1), this.expectedSlots.get(var1))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   ItemStack getExpectedStack(int var1) {
      return this.expectedSlots.get(var1);
   }

   boolean isTouched(int var1) {
      return this.touchedSlots.contains(var1);
   }

   boolean isIdle() {
      return this.hasSnapshot && !this.pending && this.abortReason == null && this.serverCursor.isEmpty() && this.menu.getCarried().isEmpty();
   }

   boolean isReady() {
      return this.pollReady(true);
   }

   boolean pollReady(boolean var1) {
      if (this.clicking) {
         return false;
      }

      if (this.abortReason != null) {
         return false;
      }

      if (!this.pending) {
         return true;
      }

      if (this.ackTracker
            .isConfirmedAndAll(
               ItemStackMatcher.matchesCount(this.serverSlots.get(this.sourceSlot), this.expectedSlots.get(this.sourceSlot)),
               ItemStackMatcher.matchesCount(this.serverSlots.get(this.destinationSlot), this.expectedSlots.get(this.destinationSlot)),
               ItemStackMatcher.matchesCount(this.serverCursor, this.expectedCursor)
            )
         && (!this.surplusReturn || this.ackTracker.isFullySynced())
         && this.isLedgerConsistent()) {
         if (this.surplusBatch == null) {
            this.pending = (boolean)0;
            return true;
         }

         ItemStack var2 = this.serverSlots.get(this.sourceSlot);
         if (ItemStack.matches(var2, this.menu.getSlot(this.sourceSlot).getItem())
            && ItemStack.matches(this.serverCursor, this.menu.getCarried())
            && (ItemStack.isSameItemSameComponents(var2, this.serverCursor) || ItemStackComparator.areMapsEquivalent(var2, this.serverCursor))
            && this.menu.getSlot(this.sourceSlot).mayPlace(this.serverCursor)
            && var2.getCount() + this.serverCursor.getCount()
               <= Math.min(this.menu.getSlot(this.sourceSlot).getMaxStackSize(this.serverCursor), this.serverCursor.getMaxStackSize())) {
            this.expectedSlots.set(this.sourceSlot, var2.copyWithCount(this.surplusBatch.returned().slotCount()));
            this.expectedCursor = ItemStack.EMPTY;
            this.surplusBatch = null;
            this.ackTracker.beginSwap(this.menu, this.sourceSlot, this.destinationSlot, this.revision);
            this.clicking = this.probing = (boolean)1;

            try {
               this.clickSender.accept(this.sourceSlot, 0);
            } catch (RuntimeException var7) {
               this.abortReason = "surplus click failed; no replay: " + var7.getClass().getSimpleName();
            } finally {
               this.clicking = this.probing = (boolean)0;
               this.clickSender = null;
            }

            return false;
         } else {
            this.abortReason = "unsafe authoritative surplus return; no replay";
            return false;
         }
      } else {
         if (var1 && this.ackTracker.isExpired()) {
            this.abortReason = "batch acknowledgement timeout; no replay " + this.ackTracker.describe();
         }

         return false;
      }
   }

   InventoryClickPlan.ClickBatch updateSlot(Minecraft var1, int var2, int var3, boolean var4) {
      return this.planSwap(var1, var2, var3, var4).batch();
   }

   InventoryClickPlan.ClickBatch planMergeBatch(Minecraft var1, int var2, int var3) {
      return this.isLedgerConsistent(var1, var2, var3, false, true).batch();
   }

   ContainerSwapLedger.Inner planSwap(Minecraft var1, int var2, int var3, boolean var4) {
      return this.isLedgerConsistent(var1, var2, var3, var4, false);
   }

   ContainerSwapLedger.Inner isLedgerConsistent(Minecraft var1, int var2, int var3, boolean var4, boolean var5) {
      if (!this.hasSnapshot) {
         return new ContainerSwapLedger.Inner(null, "authoritative inventory missing");
      }

      if (var2 >= 0 && var3 >= 0 && var2 < this.serverSlots.size() && var3 < this.serverSlots.size()) {
         Slot var6 = this.menu.getSlot(var2);
         Slot var7 = this.menu.getSlot(var3);
         ItemStack var8 = this.serverSlots.get(var2);
         ItemStack var9 = var4 ? ItemStack.EMPTY : this.serverSlots.get(var3);
         ItemStack var10 = var6.getItem();
         ItemStack var11 = var4 ? ItemStack.EMPTY : var7.getItem();
         int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9) && (!var5 || !ItemStackComparator.areMapsEquivalent(var8, var9))
            ? 0
            : 1;
         int var13 = !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11) && (!var5 || !ItemStackComparator.areMapsEquivalent(var10, var11))
            ? 0
            : 1;
         int var14 = var12 != 0 && var13 != 0 ? 1 : 0;
         InventoryClickPlan.ClickBatch var15 = InventoryClickPlan.batch(
            var2,
            var3,
            var8.getCount(),
            var9.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var8), var8.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var8), var8.getMaxStackSize())
         );
         InventoryClickPlan.ClickBatch var16 = InventoryClickPlan.batch(
            var2,
            var3,
            var10.getCount(),
            var11.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var10), var10.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
         );
         String var17 = null;
         if (var2 == var3) {
            var17 = "source equals destination";
         } else if (var14 == 0) {
            var17 = (var12 == 0 ? "server merge mismatch " + describeComponentDiff(var8, var9) : "")
               + (var12 == 0 && var13 == 0 ? "; " : "")
               + (var13 == 0 ? "local merge mismatch " + describeComponentDiff(var10, var11) : "");
         } else if (var15 == null) {
            var17 = "server count/capacity rejected";
         } else if (var16 == null) {
            var17 = "local count/capacity rejected";
         } else if (!var15.equals(var16)) {
            var17 = "local/server plan divergence server=" + var15 + " local=" + var16;
         } else if (!var6.mayPickup(var1.player)) {
            var17 = "source take permission denied";
         } else if (!var7.mayPlace(var8)) {
            var17 = "destination insert permission denied (server stack)";
         } else if (!var7.mayPlace(var10)) {
            var17 = "destination insert permission denied (local stack)";
         } else if (var15.clicks() == 3 && !var6.mayPlace(var8)) {
            var17 = "source remainder insert permission denied (server stack)";
         } else if (var15.clicks() == 3 && !var6.mayPlace(var10)) {
            var17 = "source remainder insert permission denied (local stack)";
         }

         return new ContainerSwapLedger.Inner(var17 == null ? var15 : null, var17 == null ? "ok" : var17);
      } else {
         return new ContainerSwapLedger.Inner(null, "source/destination slot unavailable");
      }
   }

   private static String describeComponentDiff(ItemStack var0, ItemStack var1) {
      HashSet var2 = new HashSet(var0.getComponents().keySet());
      var2.addAll(var1.getComponents().keySet());
      TreeSet var3 = new TreeSet();

      for (DataComponentType var5 : var2) {
         if (!Objects.equals(var0.getComponents().get(var5), var1.getComponents().get(var5))) {
            var3.add(String.valueOf(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(var5)));
         }
      }

      return "itemEqual="
         + var0.is(var1.getItem())
         + " componentKeys="
         + var3
         + (var3.contains("minecraft:lore") ? " loreA=" + ItemStackComparator.describeLore(var0) + " loreB=" + ItemStackComparator.describeLore(var1) : "");
   }

   String describeSlot(int var1) {
      if (var1 == -1) {
         return "cursor server{"
            + describeStack(this.serverCursor, null)
            + "} local{"
            + describeStack(this.menu.getCarried(), null)
            + "} server/local "
            + describeComponentDiff(this.serverCursor, this.menu.getCarried());
      } else if (this.hasSnapshot && var1 >= 0 && var1 < this.serverSlots.size()) {
         Slot var2 = this.menu.getSlot(var1);
         ItemStack var3 = this.serverSlots.get(var1);
         ItemStack var4 = var2.getItem();
         return "slot="
            + var1
            + " server{"
            + describeStack(var3, var2)
            + "} local{"
            + describeStack(var4, var2)
            + "} server/local "
            + describeComponentDiff(var3, var4);
      } else {
         return "slot=" + var1 + " unavailable";
      }
   }

   private static String describeStack(ItemStack var0, Slot var1) {
      MapId var2 = (MapId)var0.get(DataComponents.MAP_ID);
      return "map="
         + (var2 == null ? -1 : var2.id())
         + " count="
         + var0.getCount()
         + (var1 == null ? "" : " limit=" + Math.min(var1.getMaxStackSize(var0), var0.getMaxStackSize()));
   }

   String describeLimits(int var1, int var2) {
      Slot var3 = this.menu.getSlot(var1);
      Slot var4 = this.menu.getSlot(var2);
      ItemStack var5 = this.serverSlots.get(var1);
      ItemStack var6 = var3.getItem();
      return "effectiveLimits(server/local) source="
         + Math.min(var3.getMaxStackSize(var5), var5.getMaxStackSize())
         + "/"
         + Math.min(var3.getMaxStackSize(var6), var6.getMaxStackSize())
         + " destination="
         + Math.min(var4.getMaxStackSize(var5), var5.getMaxStackSize())
         + "/"
         + Math.min(var4.getMaxStackSize(var6), var6.getMaxStackSize());
   }

   boolean swap(Minecraft var1, int var2, int var3, String var4) {
      return this.describeStack(var1, var2, var3, var4, false);
   }

   boolean describeStack(Minecraft var1, int var2, int var3, String var4, boolean var5) {
      if (this.pending || this.abortReason != null) {
         return false;
      } else if (var1.gameMode == null) {
         this.abortReason = "interaction manager unavailable; no replay";
         return false;
      } else {
         ClientPacketListener var6 = var1.getConnection();
         PlayerInfo var7 = var6 == null ? null : var6.getPlayerInfo(var1.player.getUUID());
         this.ackTracker.setLatency(var7 == null ? 0 : var7.getLatency());
         InventoryClickPlan.ClickBatch var8 = this.isLedgerConsistent(var1, var2, var3, false, var5).batch();
         if (this.isLedgerConsistent() && var8 != null && var1.player.containerMenu == this.menu) {
            RigLog.logDebug("SWAP " + var4 + " batch clicks=" + var8.clicks() + " source=" + var2 + " destination=" + var3);
            return this.executeBatch(var2, var3, var8, var4, (var2x, var3x) -> {
               if (var1.player.containerMenu != this.menu) {
                  throw new IllegalStateException("handler lost");
               }

               var1.gameMode.handleContainerInput(this.menu.containerId, var2x, var3x, ContainerInput.PICKUP, var1.player);
            }, var5);
         } else {
            this.abortReason = "unsafe pre-batch ledger, permission, capacity or vanilla merge";
            return false;
         }
      }
   }

   boolean executeBatchStrict(int var1, int var2, InventoryClickPlan.ClickBatch var3, String var4, BiConsumer<Integer, Integer> var5) {
      return this.executeBatch(var1, var2, var3, var4, var5, false);
   }

   boolean executeBatch(int var1, int var2, InventoryClickPlan.ClickBatch var3, String var4, BiConsumer<Integer, Integer> var5, boolean var6) {
      if (!this.pending && this.abortReason == null && var3 != null && this.isLedgerConsistent()) {
         ItemStack var7 = this.serverSlots.get(var1);
         ItemStack var8 = this.serverSlots.get(var2);
         ItemStack var9 = this.menu.getSlot(var1).getItem().copy();
         ItemStack var10 = this.menu.getSlot(var2).getItem().copy();
         this.surplusReturn = (boolean)(!var6
               || var8.isEmpty()
               || ItemStack.isSameItemSameComponents(var7, var8) && ItemStack.isSameItemSameComponents(var9, var10)
            ? 0
            : 1);
         if (!this.surplusReturn
            || (ItemStack.isSameItemSameComponents(var7, var8) || ItemStackComparator.areMapsEquivalent(var7, var8))
               && (ItemStack.isSameItemSameComponents(var9, var10) || ItemStackComparator.areMapsEquivalent(var9, var10))) {
            if (this.expectedSlots.isEmpty()) {
               for (ItemStack var12 : this.serverSlots) {
                  this.expectedSlots.add(var12.copy());
               }
            }

            this.touchedSlots.add(var1);
            this.touchedSlots.add(var2);
            this.surplusBatch = this.surplusReturn && var3.clicks() == 3 ? var3 : null;
            this.clickSender = this.surplusBatch == null ? null : var5;
            this.expectedCursor = this.surplusBatch == null ? ItemStack.EMPTY : var7.copyWithCount(var3.placement().cursorCount());
            this.expectedSlots.set(var1, var7.copyWithCount(this.surplusBatch == null ? var3.returned().slotCount() : var3.pickup().slotCount()));
            this.expectedSlots.set(var2, (var8.isEmpty() ? var7 : var8).copyWithCount(var3.placement().slotCount()));
            this.sourceSlot = var1;
            this.destinationSlot = var2;
            this.logEvent("issue " + var4 + " source=" + var1 + " destination=" + var2 + " clicks=" + var3.clicks());
            this.pending = this.issued = (boolean)1;
            this.ackTracker.beginSwap(this.menu, var1, var2, this.revision);
            return var3.execute(
                  var7x -> {
                     this.clicking = (boolean)1;
                     this.probing = (boolean)(var7x == (this.surplusReturn ? 1 : var3.clicks() - 1) ? 1 : 0);

                     try {
                        this.logEvent("click call index=" + var7x + " probe=" + this.probing + " revision=" + this.menu.getStateId());
                        var5.accept(var7x == 1 ? var2 : var1, var7x == 0 ? var3.pickupButton() : (var7x == 1 ? 1 : 0));
                     } catch (RuntimeException var13) {
                        this.abortReason = "batch click failed; no replay: " + var13.getClass().getSimpleName();
                        return false;
                     } finally {
                        this.clicking = this.probing = (boolean)0;
                     }

                     if (this.surplusReturn && var7x == 1) {
                        return false;
                     }

                     this.logEvent("click returned index=" + var7x);
                     int var8x = var7x == 2 ? var3.returned().slotCount() : var3.pickup().slotCount();
                     int var9x = var7x == 0 ? var10.getCount() : var3.placement().slotCount();
                     int var10x = var7x == 0 ? var3.pickup().cursorCount() : (var7x == 1 ? var3.placement().cursorCount() : 0);
                     if (ItemStack.matches(this.menu.getSlot(var1).getItem(), var9.copyWithCount(var8x))
                        && ItemStack.matches(this.menu.getSlot(var2).getItem(), (var10.isEmpty() ? var9 : var10).copyWithCount(var9x))
                        && ItemStack.matches(this.menu.getCarried(), var9.copyWithCount(var10x))) {
                        return true;
                     }

                     this.abortReason = "batch prediction diverged after click=" + var7x + "; no replay";
                     return false;
                  }
               )
               || this.surplusReturn && this.abortReason == null;
         } else {
            this.abortReason = "worth return policy rejected before clicks; no replay";
            return false;
         }
      } else {
         return false;
      }
   }

   record Inner(InventoryClickPlan.ClickBatch batch, String reason) {
   }
}

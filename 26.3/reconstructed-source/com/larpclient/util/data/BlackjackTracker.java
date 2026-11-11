package com.larpclient.util.data;

import com.larpclient.module.exploit.HotbarSwapQueue2;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class BlackjackTracker {
   private static final BlackjackTracker iNSTANCE = new BlackjackTracker();
   private final List<Integer> playerCards = new ArrayList<>();
   private final List<Integer> dealerCards = new ArrayList<>();
   private final Deque<BlackjackTracker.Recipient> pendingDraws = new ArrayDeque<>();
   private final Map<Integer, Integer> mapCounts = new HashMap<>();
   private Map<Integer, Integer> lastMapCounts;
   private Map<String, Integer> lastSlotCounts = new HashMap<>();
   private BlackjackTracker.Turn turn = BlackjackTracker.Turn.PLAYER;
   private int plannedValue = -1;
   private boolean active;
   private boolean forceQueued;
   private int idleTicks;

   private BlackjackTracker() {
   }

   public static BlackjackTracker getInstance() {
      return iNSTANCE;
   }

   public void registerDraw(Minecraft var1, HotbarSwapQueue2 var2) {
      if (!this.active) {
         this.start(var1);
      }

      int var3 = this.pendingCardCount() + this.pendingDraws.size();
      BlackjackTracker.Recipient var4 = var3 == 0
         ? BlackjackTracker.Recipient.PLAYER
         : (
            var3 == 1
               ? BlackjackTracker.Recipient.DEALER
               : (this.turn == BlackjackTracker.Turn.DEALER ? BlackjackTracker.Recipient.DEALER : BlackjackTracker.Recipient.PLAYER)
         );
      this.pendingDraws.addLast(var4);
      if (!var2.isSwapBusy()) {
         int var5 = this.plannedValue;
         this.clearQueue(var2);
         if (var3 >= 2 && var4 == BlackjackTracker.Recipient.DEALER && this.turn == BlackjackTracker.Turn.DEALER && ForceQueue.getInstance().enabled) {
            this.plannedValue = var5 >= 1 ? var5 : this.pickPlannedValue(var2);
            if (this.plannedValue >= 1) {
               var2.queueForce(this.plannedValue);
               this.forceQueued = (boolean)1;
            }
         }
      }
   }

   public void tick(Minecraft var1, HotbarSwapQueue2 var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.stop(var2);
      } else if (!var2.isSwapBusy()) {
         Map var4 = this.countMaps(var1);
         Map var5 = this.countSlots(var1);
         if (this.lastMapCounts == null) {
            this.lastMapCounts = new HashMap<>(var4);
            this.lastSlotCounts = new HashMap<>(var5);
         } else {
            if (this.active && !this.pendingDraws.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.lastMapCounts.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.pendingDraws.isEmpty()) {
                     int var9 = this.findMapSlot(var1, var2, (Integer)var7.getKey(), var5);
                     if (var9 >= 1 && var9 <= 9) {
                        this.recordCard(this.pendingDraws.removeFirst(), var9, (Integer)var7.getKey());
                     }
                  }
               }
            }

            this.lastMapCounts = new HashMap<>(var4);
            this.lastSlotCounts = new HashMap<>(var5);
            this.tick(var2, var4);
            this.updatePlan(var2);
         }
      }
   }

   private void tick(HotbarSwapQueue2 var1, Map<Integer, Integer> var2) {
      if (this.pendingCardCount() <= 0) {
         this.idleTicks = 0;
      } else {
         int var3 = 0;

         for (Entry var5 : this.mapCounts.entrySet()) {
            int var6 = var2.getOrDefault(var5.getKey(), 0);
            var3 += Math.min((Integer)var5.getValue(), var6);
         }

         this.idleTicks = var3 <= 0 ? this.idleTicks + 1 : 0;
         if (this.idleTicks >= 6) {
            this.resetTracking(var1, var2);
         }
      }
   }

   public void toggleTurn(HotbarSwapQueue2 var1) {
      this.clearQueue(var1);

      this.turn = switch (this.turn) {
         case PLAYER -> BlackjackTracker.Turn.DEALER;
         case DEALER -> BlackjackTracker.Turn.PLAYER;
      };
      this.updatePlan(var1);
   }

   public void setTurn(String var1, HotbarSwapQueue2 var2) {
      if (var1 != null) {
         BlackjackTracker.Turn var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> BlackjackTracker.Turn.PLAYER;
            case "dealer" -> BlackjackTracker.Turn.DEALER;
            case "off" -> BlackjackTracker.Turn.PLAYER;
            default -> null;
         };
         if (var3 != null && var3 != this.turn) {
            this.clearQueue(var2);
            this.turn = var3;
            this.updatePlan(var2);
         }
      }
   }

   private void updatePlan(HotbarSwapQueue2 var1) {
      if (!var1.isSwapBusy() && this.pendingDraws.isEmpty()) {
         if (this.turn != BlackjackTracker.Turn.DEALER || this.pendingCardCount() < 2 || !ForceQueue.getInstance().enabled) {
            this.plannedValue = -1;
         } else if (this.plannedValue < 1) {
            this.plannedValue = this.pickPlannedValue(var1);
         }
      }
   }

   private void start(Minecraft var1) {
      HotbarSwapQueue2.getInstance().onMapCountRise();
      this.playerCards.clear();
      this.dealerCards.clear();
      this.mapCounts.clear();
      this.plannedValue = -1;
      this.pendingDraws.clear();
      this.idleTicks = 0;
      this.active = (boolean)1;
      this.forceQueued = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.lastMapCounts = this.countMaps(var1);
         this.lastSlotCounts = this.countSlots(var1);
      }
   }

   private void recordCard(BlackjackTracker.Recipient var1, int var2, int var3) {
      HotbarSwapQueue2.getInstance().continueFromPacket(var2, var3);
      if (var1 == BlackjackTracker.Recipient.PLAYER) {
         this.playerCards.add(var2);
      } else {
         this.dealerCards.add(var2);
      }

      this.mapCounts.merge(var3, 1, Integer::sum);
      this.forceQueued = (boolean)0;
      this.plannedValue = -1;
   }

   private int pickPlannedValue(HotbarSwapQueue2 var1) {
      int var2 = getInstance(this.playerCards);
      int var3 = getInstance(this.dealerCards);
      if (var2 <= 9 && var3 < 9) {
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         int var6 = -1;

         for (int var7 = 1; var7 <= 9; var7++) {
            if (var1.getTemplateForValue(var7) != null) {
               int var8 = var3 + var7;
               if (var8 <= 9) {
                  if (var2 >= 1 && var8 >= var2) {
                     var4.add(var7);
                  }

                  if (var8 > var6) {
                     var6 = var8;
                     var5.clear();
                     var5.add(var7);
                  } else if (var8 == var6) {
                     var5.add(var7);
                  }
               }
            }
         }

         if (!var4.isEmpty()) {
            return (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
         } else {
            return var5.isEmpty() ? -1 : (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
         }
      } else {
         return -1;
      }
   }

   private void clearQueue(HotbarSwapQueue2 var1) {
      if (var1 != null) {
         var1.clearQueue();
      }

      this.forceQueued = (boolean)0;
      this.plannedValue = -1;
   }

   private void stop(HotbarSwapQueue2 var1) {
      var1.onMapCountRise();
      this.clearQueue(var1);
      this.playerCards.clear();
      this.dealerCards.clear();
      this.mapCounts.clear();
      this.lastMapCounts = null;
      this.lastSlotCounts.clear();
      this.pendingDraws.clear();
      this.idleTicks = 0;
      this.active = (boolean)0;
   }

   private Map<Integer, Integer> countMaps(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         addMapStack(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         addMapStack(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void addMapStack(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> countSlots(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         ItemStack var4 = var1.player.getInventory().getItem(var3);
         if (var4 != null && !var4.isEmpty() && var4.is(Items.FILLED_MAP)) {
            MapId var5 = (MapId)var4.getComponents().get(DataComponents.MAP_ID);
            if (var5 != null) {
               var2.put("inventory[" + var3 + "]#" + var5.id(), var4.getCount());
            }
         }
      }

      return var2;
   }

   private int findMapSlot(Minecraft var1, HotbarSwapQueue2 var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.lastSlotCounts.getOrDefault(var9, 0)) {
               return var2.setPhase(var8, var6);
            }
         }
      }

      for (int var10 = 0; var10 < var1.player.getInventory().getContainerSize(); var10++) {
         ItemStack var11 = var1.player.getInventory().getItem(var10);
         if (var11 != null && !var11.isEmpty() && var11.is(Items.FILLED_MAP)) {
            MapId var12 = (MapId)var11.getComponents().get(DataComponents.MAP_ID);
            if (var12 != null && var12.id() == var3) {
               return var2.setPhase("inventory[" + var10 + "]", var11);
            }
         }
      }

      return -1;
   }

   private void resetTracking(HotbarSwapQueue2 var1, Map<Integer, Integer> var2) {
      this.clearQueue(var1);
      this.playerCards.clear();
      this.dealerCards.clear();
      this.mapCounts.clear();
      this.pendingDraws.clear();
      this.idleTicks = 0;
      this.active = (boolean)0;
      this.lastMapCounts = new HashMap<>(var2);
      this.lastSlotCounts = this.countSlots(Minecraft.getInstance());
   }

   private String formatHand(List<Integer> var1) {
      if (var1.isEmpty()) {
         return "-";
      }

      StringBuilder var2 = new StringBuilder();

      for (int var4 : var1) {
         if (!var2.isEmpty()) {
            var2.append(", ");
         }

         var2.append(var4);
      }

      var2.append(" = ").append(getInstance(var1));
      if (getInstance(var1) > 9) {
         var2.append(" BUST");
      }

      return var2.toString();
   }

   private int pendingCardCount() {
      return this.mapCounts.values().stream().mapToInt(Integer::intValue).sum();
   }

   private static int getInstance(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return var1;
   }

   public int updatePlan() {
      return getInstance(this.playerCards);
   }

   public int dealerTotal() {
      return getInstance(this.dealerCards);
   }

   public String playerHandText() {
      return this.formatHand(this.playerCards);
   }

   public String dealerHandText() {
      return this.formatHand(this.dealerCards);
   }

   public int toggleTurn() {
      return this.plannedValue;
   }

   public int dealerHandText() {
      int var1 = HotbarSwapQueue2.getInstance().getPendingValue();
      return var1 >= 1 ? var1 : this.plannedValue;
   }

   public String turnName() {
      return this.turn.name().toLowerCase(Locale.ROOT);
   }

   public enum Recipient {
      PLAYER,
      DEALER;
   }

   private enum Turn {
      PLAYER,
      DEALER;
   }
}

package com.larpclient.util.misc;

import com.larpclient.module.exploit.HotbarForceQueue;
import com.larpclient.util.data.BaccaratLog;
import com.larpclient.util.inventory.HotbarSwapQueue;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class BaccaratRig {
   private static final BaccaratRig instance = new BaccaratRig();
   private final List<Integer> playerCards = new ArrayList<>();
   private final List<Integer> bankerCards = new ArrayList<>();
   private final Deque<BaccaratRig.Side> pendingSides = new ArrayDeque<>();
   private final Map<Integer, Integer> dealtMapCounts = new HashMap<>();
   private final Map<Integer, Integer> returnedMapCounts = new HashMap<>();
   private Map<Integer, Integer> lastDispenserCounts = new HashMap<>();
   private int lastContainerId = -1;
   private Map<Integer, Integer> previousMapCounts;
   private Map<String, Integer> previousSlotCounts = new HashMap<>();
   private BaccaratRig.BetTarget target = BaccaratRig.BetTarget.OFF;
   private int dealtCards;
   private int plannedValue = -1;
   private boolean roundActive;
   private boolean roundComplete;
   private boolean forcePending;
   private boolean presetActive;
   private final ArrayDeque<Integer> presetQueue = new ArrayDeque<>();

   private BaccaratRig() {
   }

   public static BaccaratRig getInstance() {
      return instance;
   }

   public void onDispense(Minecraft var1, HotbarForceQueue var2, BaccaratRig.Side var3) {
      BaccaratLog.log(
         "DISP side="
            + var3
            + " roundActive="
            + this.roundActive
            + " roundComplete="
            + this.roundComplete
            + " dealtCards="
            + this.dealtCards
            + " pending="
            + this.pendingSides
            + " target="
            + this.target
            + " enabled="
            + HotbarSwapQueue.getInstance().queueEnabled
      );
      if (!this.roundActive || this.roundComplete) {
         this.resetRound(var1);
      }

      if (var2.isSwapBusy()) {
         this.pendingSides.addLast(var3);
      } else {
         if (this.presetActive) {
            if (!var2.hasQueuedForce() && !this.presetQueue.isEmpty()) {
               var2.queueForce(this.presetQueue.removeFirst());
            }

            if (var2.hasQueuedForce()) {
               BaccaratLog.log("PRESET draw side=" + var3 + " value=" + var2.getPendingValue() + " left=" + this.presetQueue);
               this.pendingSides.addLast(var3);
               return;
            }

            this.presetActive = (boolean)0;
            RigLog.logMessage("Baccarat", "Preset used up; the automatic plan resumes");
         }

         this.resetRound(var2);
         if (HotbarSwapQueue.getInstance().queueEnabled && this.target != BaccaratRig.BetTarget.OFF && this.dealtCards >= 3 && this.cardCount(var3) >= 1) {
            this.plannedValue = this.cycleTarget(var2, var3);
            if (this.plannedValue >= 1) {
               var2.queueForce(this.plannedValue);
               this.forcePending = (boolean)1;
            }
         }

         this.pendingSides.addLast(var3);
      }
   }

   public void tick(Minecraft var1, HotbarForceQueue var2, boolean var3) {
      if (var3 && var1 != null && var1.player != null) {
         Map var4 = this.countMaps(var1);
         Map var5 = this.bestFutureScore(var1);
         if (this.previousMapCounts == null) {
            this.previousMapCounts = new HashMap<>(var4);
            this.previousSlotCounts = new HashMap<>(var5);
         } else if (var2.isSwapBusy()) {
            BaccaratLog.log("HOLD swapBusy=" + var2.isSwapActive() + " defer=" + var2.findVisibleFace());
         } else {
            if (this.roundActive && !this.pendingSides.isEmpty()) {
               for (Entry var7 : var4.entrySet()) {
                  int var8 = Math.max(0, (Integer)var7.getValue() - this.previousMapCounts.getOrDefault(var7.getKey(), 0));

                  while (var8-- > 0 && !this.pendingSides.isEmpty()) {
                     int var9 = this.findPhysicalValue(var1, var2, (Integer)var7.getKey(), var5);
                     int var10 = this.forcePending && this.plannedValue >= 1 && var9 < 1 ? this.plannedValue : var9;
                     BaccaratLog.log(
                        "TICK mapId="
                           + var7.getKey()
                           + " physical="
                           + var9
                           + " chosen="
                           + var10
                           + " pending="
                           + this.pendingSides
                           + " prev="
                           + this.previousMapCounts.getOrDefault(var7.getKey(), 0)
                           + " cur="
                           + var7.getValue()
                     );
                     if (var10 >= 1 && var10 <= 9) {
                        this.recordCard(this.pendingSides.removeFirst(), var10, (Integer)var7.getKey());
                     } else {
                        BaccaratLog.log("DROPPED mapId=" + var7.getKey() + " value=" + var10 + " (consumed added but pending side not removed)");
                     }
                  }
               }
            }

            this.previousMapCounts = new HashMap<>(var4);
            this.previousSlotCounts = new HashMap<>(var5);
            if (this.dealtCards > 0 && this.findPhysicalValue(var1)) {
               this.resetAfterDispenser(var2, var4);
            }
         }
      } else {
         this.fullReset(var2);
      }
   }

   public void cycleTarget(HotbarForceQueue var1) {
      this.resetRound(var1);

      this.target = switch (this.target) {
         case OFF -> BaccaratRig.BetTarget.PLAYER;
         case PLAYER -> BaccaratRig.BetTarget.BANKER;
         case BANKER -> BaccaratRig.BetTarget.OFF;
      };
   }

   public void setTarget(String var1, HotbarForceQueue var2) {
      if (var1 != null) {
         BaccaratRig.BetTarget var3 = switch (var1.trim().toLowerCase(Locale.ROOT)) {
            case "player" -> BaccaratRig.BetTarget.PLAYER;
            case "banker" -> BaccaratRig.BetTarget.BANKER;
            case "off" -> BaccaratRig.BetTarget.OFF;
            default -> null;
         };
         if (var3 != null && var3 != this.target) {
            this.resetRound(var2);
            this.target = var3;
         }
      }
   }

   private void resetRound(Minecraft var1) {
      HotbarForceQueue.getInstance().onMapCountRise();
      this.playerCards.clear();
      this.bankerCards.clear();
      this.dealtMapCounts.clear();
      this.returnedMapCounts.clear();
      this.lastDispenserCounts.clear();
      this.lastContainerId = -1;
      this.dealtCards = 0;
      this.plannedValue = -1;
      this.pendingSides.clear();
      this.roundActive = (boolean)1;
      this.roundComplete = (boolean)0;
      this.forcePending = (boolean)0;
      if (var1 != null && var1.player != null) {
         this.previousMapCounts = this.countMaps(var1);
         this.previousSlotCounts = this.bestFutureScore(var1);
      }
   }

   private void recordCard(BaccaratRig.Side var1, int var2, int var3) {
      HotbarForceQueue.getInstance().continueFromPacket(var2, var3);
      BaccaratLog.log("RECORD side=" + var1 + " value=" + var2 + " mapId=" + var3 + " player=" + this.playerCards + " banker=" + this.bankerCards);
      if (var1 == BaccaratRig.Side.PLAYER) {
         this.playerCards.add(var2);
      } else {
         this.bankerCards.add(var2);
      }

      this.dealtMapCounts.merge(var3, 1, Integer::sum);
      this.dealtCards++;
      this.roundComplete = this.isRoundComplete();
      this.forcePending = (boolean)0;
      this.plannedValue = -1;
   }

   private boolean isRoundComplete() {
      if (this.playerCards.size() >= 2 && this.bankerCards.size() >= 2) {
         int var1 = handValue(this.playerCards);
         int var2 = handValue(this.bankerCards);
         if (this.playerCards.size() != 2 || this.bankerCards.size() != 2) {
            int var3 = this.playerCards.size() < 3 && var1 <= 5 ? 0 : 1;
            int var4 = this.bankerCards.size() < 3 && var2 <= 5 ? 0 : 1;
            return var3 != 0 && var4 != 0;
         } else {
            return var1 < 8 && var2 < 8 ? var1 > 5 && var2 > 5 : true;
         }
      } else {
         return false;
      }
   }

   private int cardCount(BaccaratRig.Side var1) {
      return var1 == BaccaratRig.Side.PLAYER ? this.playerCards.size() : this.bankerCards.size();
   }

   private int cycleTarget(HotbarForceQueue var1, BaccaratRig.Side var2) {
      int var3 = Integer.MIN_VALUE;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 9; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = this.scoreCandidate(var1, var2, var7);
            if (var8 >= 100) {
               var5.add(var7);
            } else if (var8 == 50) {
               var6.add(var7);
            }

            if (var8 > var3) {
               var3 = var8;
               var4.clear();
               var4.add(var7);
            } else if (var8 == var3) {
               var4.add(var7);
            }
         }
      }

      if (!var5.isEmpty()) {
         return (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
      } else if (!var6.isEmpty()) {
         return (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size()));
      } else {
         return var4.isEmpty() ? -1 : (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
      }
   }

   private int scoreCandidate(HotbarForceQueue var1, BaccaratRig.Side var2, int var3) {
      ArrayList var4 = new ArrayList<>(this.playerCards);
      ArrayList var5 = new ArrayList<>(this.bankerCards);
      if (var2 == BaccaratRig.Side.PLAYER) {
         var4.add(var3);
      } else {
         var5.add(var3);
      }

      int var6 = this.setTarget(var1, var4, var5);
      if (var4.size() == 2 && var5.size() == 2) {
         int var7 = this.target == BaccaratRig.BetTarget.PLAYER ? handValue(var4) - handValue(var5) : handValue(var5) - handValue(var4);
         if ((
               var2 == BaccaratRig.Side.PLAYER && this.target == BaccaratRig.BetTarget.PLAYER
                  || var2 == BaccaratRig.Side.BANKER && this.target == BaccaratRig.BetTarget.BANKER
            )
            && var7 > 0) {
            var6 += 1000 + var7;
         }
      }

      return var6;
   }

   private int setTarget(HotbarForceQueue var1, List<Integer> var2, List<Integer> var3) {
      if (var2.size() >= 2 && var3.size() >= 2) {
         int var4 = handValue(var2);
         int var5 = handValue(var3);
         if (var2.size() == 2 && var3.size() == 2) {
            if (var4 >= 8 || var5 >= 8) {
               return this.scoreDifference(var4, var5);
            } else if (var4 <= 5) {
               return this.bestFutureScore(var1, var2, var3, BaccaratRig.Side.PLAYER);
            } else {
               return var5 <= 5 ? this.bestFutureScore(var1, var2, var3, BaccaratRig.Side.BANKER) : this.scoreDifference(var4, var5);
            }
         } else {
            return var2.size() >= 3 && var3.size() == 2 && var5 <= 5
               ? this.bestFutureScore(var1, var2, var3, BaccaratRig.Side.BANKER)
               : this.scoreDifference(var4, var5);
         }
      } else {
         return this.scoreDifference(handValue(var2), handValue(var3));
      }
   }

   private int bestFutureScore(HotbarForceQueue var1, List<Integer> var2, List<Integer> var3, BaccaratRig.Side var4) {
      int var5 = Integer.MIN_VALUE;

      for (int var6 = 1; var6 <= 9; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            ArrayList var7 = new ArrayList(var2);
            ArrayList var8 = new ArrayList(var3);
            if (var4 == BaccaratRig.Side.PLAYER) {
               var7.add(var6);
            } else {
               var8.add(var6);
            }

            var5 = Math.max(var5, this.setTarget(var1, var7, var8));
         }
      }

      return var5 == Integer.MIN_VALUE ? this.scoreDifference(handValue(var2), handValue(var3)) : var5;
   }

   private int scoreDifference(int var1, int var2) {
      int var3 = this.target == BaccaratRig.BetTarget.PLAYER ? var1 - var2 : var2 - var1;
      if (var3 > 0) {
         return 100 + var3;
      } else {
         return var3 == 0 ? 50 : var3;
      }
   }

   public boolean shouldSwap(HotbarForceQueue var1, int var2, int var3) {
      if (this.target != BaccaratRig.BetTarget.OFF && var2 >= 1 && var3 >= 1) {
         BaccaratRig.Side var4 = this.pendingSides.peekFirst();
         if (var4 == null) {
            return true;
         }

         int var5 = this.scoreCandidate(var1, var4, var2);
         int var6 = this.scoreCandidate(var1, var4, var3);
         int var7 = var5 >= 100 || var6 < 100 && (var5 >= 50 || var6 != 50) ? 0 : 1;
         BaccaratLog.log(
            "SWAP_DECISION side="
               + var4
               + " target="
               + this.target
               + " real="
               + var2
               + " armed="
               + var3
               + " realScore="
               + var5
               + " armedScore="
               + var6
               + " swap="
               + var7
         );
         return (boolean)var7;
      } else {
         return true;
      }
   }

   public boolean startPreset(HotbarForceQueue var1, int[] var2) {
      if (var1 != null && var2 != null && var2.length != 0) {
         if (var1.isSwapBusy()) {
            return false;
         }

         this.resetRound(var1);

         for (int var6 : var2) {
            if (var1.getTemplateForValue(var6) != null) {
               this.presetQueue.addLast(var6);
            }
         }

         if (this.presetQueue.isEmpty()) {
            return false;
         }

         var1.queueForce(this.presetQueue.removeFirst());
         this.presetActive = var1.hasQueuedForce();
         if (!this.presetActive) {
            this.presetQueue.clear();
         }

         return this.presetActive;
      } else {
         return false;
      }
   }

   public String presetDisplay() {
      if (!this.presetActive) {
         return "";
      }

      StringBuilder var1 = new StringBuilder();
      int var2 = HotbarForceQueue.getInstance().getPendingValue();
      if (var2 >= 1) {
         var1.append(var2);
      }

      for (int var4 : this.presetQueue) {
         var1.append(var1.isEmpty() ? "" : " ").append(var4);
      }

      return var1.toString();
   }

   private void resetRound(HotbarForceQueue var1) {
      this.presetActive = (boolean)0;
      this.presetQueue.clear();
      if (var1 != null) {
         var1.clearQueue();
      }

      this.forcePending = (boolean)0;
      this.plannedValue = -1;
   }

   private void fullReset(HotbarForceQueue var1) {
      var1.onMapCountRise();
      this.resetRound(var1);
      this.playerCards.clear();
      this.bankerCards.clear();
      this.dealtMapCounts.clear();
      this.returnedMapCounts.clear();
      this.lastDispenserCounts.clear();
      this.lastContainerId = -1;
      this.previousMapCounts = null;
      this.previousSlotCounts.clear();
      this.pendingSides.clear();
      this.dealtCards = 0;
      this.roundActive = (boolean)0;
      this.roundComplete = (boolean)0;
   }

   private Map<Integer, Integer> countMaps(Minecraft var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.player.getInventory().getContainerSize(); var3++) {
         addMapCount(var2, var1.player.getInventory().getItem(var3));
      }

      if (var1.player.containerMenu != null) {
         addMapCount(var2, var1.player.containerMenu.getCarried());
      }

      return var2;
   }

   private static void addMapCount(Map<Integer, Integer> var0, ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
         if (var2 != null) {
            var0.merge(var2.id(), var1.getCount(), Integer::sum);
         }
      }
   }

   private Map<String, Integer> bestFutureScore(Minecraft var1) {
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

   private int findPhysicalValue(Minecraft var1, HotbarForceQueue var2, int var3, Map<String, Integer> var4) {
      for (int var5 = 0; var5 < var1.player.getInventory().getContainerSize(); var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
            MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
            String var8 = "inventory[" + var5 + "]";
            String var9 = var8 + "#" + var3;
            if (var7 != null && var7.id() == var3 && var4.getOrDefault(var9, 0) > this.previousSlotCounts.getOrDefault(var9, 0)) {
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

   private boolean findPhysicalValue(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2) {
         HashMap var7 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5 != null && !(var5.container instanceof Inventory)) {
               addMapCount(var7, var5.getItem());
            }
         }

         if (var2.containerId != this.lastContainerId) {
            this.lastContainerId = var2.containerId;
            this.lastDispenserCounts = var7;
            return false;
         }

         for (Entry var10 : var7.entrySet()) {
            if (this.dealtMapCounts.containsKey(var10.getKey())) {
               int var6 = Math.max(0, (Integer)var10.getValue() - this.lastDispenserCounts.getOrDefault(var10.getKey(), 0));
               if (var6 > 0) {
                  this.returnedMapCounts.merge((Integer)var10.getKey(), var6, Integer::sum);
               }
            }
         }

         this.lastDispenserCounts = var7;

         for (Entry var11 : this.dealtMapCounts.entrySet()) {
            if (this.returnedMapCounts.getOrDefault(var11.getKey(), 0) < (Integer)var11.getValue()) {
               return false;
            }
         }

         return true;
      } else {
         this.lastContainerId = -1;
         this.lastDispenserCounts.clear();
         return false;
      }
   }

   private void resetAfterDispenser(HotbarForceQueue var1, Map<Integer, Integer> var2) {
      this.resetRound(var1);
      this.playerCards.clear();
      this.bankerCards.clear();
      this.dealtMapCounts.clear();
      this.returnedMapCounts.clear();
      this.lastDispenserCounts.clear();
      this.lastContainerId = -1;
      this.pendingSides.clear();
      this.dealtCards = 0;
      this.roundActive = (boolean)0;
      this.roundComplete = (boolean)0;
      this.previousMapCounts = new HashMap<>(var2);
      this.previousSlotCounts = this.bestFutureScore(Minecraft.getInstance());
   }

   private static int handValue(List<Integer> var0) {
      int var1 = 0;

      for (int var3 : var0) {
         var1 += var3;
      }

      return Math.floorMod(var1, 10);
   }

   public int playerTotal() {
      return handValue(this.playerCards);
   }

   public int bankerTotal() {
      return handValue(this.bankerCards);
   }

   public int tick() {
      return this.plannedValue;
   }

   public int dealtCount() {
      return this.dealtCards;
   }

   public String targetName() {
      return this.target.name().toLowerCase(Locale.ROOT);
   }

   private enum BetTarget {
      OFF,
      PLAYER,
      BANKER;
   }

   public enum Side {
      PLAYER,
      BANKER;
   }
}

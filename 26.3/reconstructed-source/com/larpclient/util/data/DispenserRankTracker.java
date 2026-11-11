package com.larpclient.util.data;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.util.inventory.SlotCounts;
import com.larpclient.util.inventory.SlotSwapUtil;
import com.larpclient.util.misc.RankParser;
import com.larpclient.util.misc.RigLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

final class DispenserRankTracker {
   final BlockPos[] dispenserPositions = new BlockPos[2];
   private final ItemStack[][] rankStacks = new ItemStack[2][10];
   private final int[][] slotRanks = new int[2][9];
   private final int[] takenRanks = new int[]{0, 0};
   private final int[] spoofedRanks = new int[]{0, 0};
   private final boolean[] swapDetected = new boolean[]{false, false};
   private final boolean[] announced = new boolean[]{false, false};
   private final int[] swapSlotA = new int[]{-1, -1};
   private final int[] swapSlotB = new int[]{-1, -1};
   private final Set<UUID> seenItemEntities = new HashSet<>();
   private final Set<UUID> dispensedItemEntities = new HashSet<>();
   private final Set<Integer> dispensedKeys = new HashSet<>();
   private final HoldTimer takeDetector = new HoldTimer();
   private final List<Map<Integer, Integer>> menuCounts = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu openMenu;
   private int openMenuIndex = -1;
   private final Map<Integer, Integer> previousCounts = new HashMap<>();
   private final Map<Integer, Integer> baselineCounts = new HashMap<>();
   boolean ascending = (boolean)1;
   private boolean randomize = (boolean)1;
   private boolean initialized;
   private final Set<ItemStack> spoofedStacks = Collections.newSetFromMap(new WeakHashMap<>());
   private ClientLevel level;

   DispenserRankTracker() {
      for (ItemStack[] var4 : this.rankStacks) {
         Arrays.fill(var4, ItemStack.EMPTY);
      }
   }

   void scanDispensedItems(Minecraft var1, VisualWinnerConfig var2, BlockPos var3, BlockPos var4) {
      int var5 = this.level == var1.level
            && this.randomize == var2.enabled
            && Objects.equals(this.dispenserPositions[0], var3)
            && Objects.equals(this.dispenserPositions[1], var4)
         ? 0
         : 1;
      this.randomize = var2.enabled;
      this.dispenserPositions[0] = var3;
      this.dispenserPositions[1] = var4;
      this.level = var1.level;
      this.ascending = (boolean)(var2.getTarget() == VisualWinnerConfig.Target.HOST ? 1 : 0);

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 1; var7 <= 9; var7++) {
            this.rankStacks[var6][var7] = var2.getStack(var6 == 0, var7 - 1);
            if (this.slotRanks[var6][var7 - 1] == 0) {
               this.slotRanks[var6][var7 - 1] = var7;
            }
         }
      }

      if (var5 != 0 || !this.initialized) {
         this.reset(var1);
         this.initialized = (boolean)1;
      }
   }

   boolean hasTakenRank() {
      return this.takenRanks[0] > 0 || this.takenRanks[1] > 0;
   }

   void setSlotRank(int var1, int var2, int var3) {
      if (this.takenRanks[var1] == 0 && var2 >= 0 && var2 < 9 && var3 >= 1 && var3 <= 9) {
         this.slotRanks[var1][var2] = var3;
      }
   }

   boolean hasDispenser(int var1) {
      return this.dispenserPositions[var1] != null;
   }

   boolean reset() {
      return this.hasDispenser(0) && this.hasDispenser(1);
   }

   private int rankKeyOf(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      String var2 = var1.getHoverName().getString();
      int var3 = RankParser.parseRank(var2);
      if (var3 < 1) {
         int var8 = -1;

         for (int var9 = 0; var9 < 2; var9++) {
            for (int var6 = 1; var6 <= 9; var6++) {
               ItemStack var7 = this.rankStacks[var9][var6];
               if (this.hasDispenser(var9) && var7 != null && !var7.isEmpty() && var1.is(var7.getItem()) && var2.equals(var7.getHoverName().getString())) {
                  if (var8 >= 0) {
                     return -1;
                  }

                  var8 = var9 * 10 + var6;
               }
            }
         }

         return var8;
      } else {
         int var4 = RankParser.isViewerRank(var2) ? 0 : 1;
         ItemStack var5 = this.rankStacks[var4][var3];
         return this.hasDispenser(var4) && var5 != null && !var5.isEmpty() && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> countTrackedItems(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.openMenu) {
         this.snapshotMenu(var2, this.openMenuIndex);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.menuCounts) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.rankKeyOf(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu == null ? ItemStack.EMPTY : var1.player.containerMenu.getCarried();
      int var16 = this.rankKeyOf(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.rankKeyOf(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1 != null && var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.dispensedItemEntities.contains(var7.getUUID())) {
               int var10 = this.rankKeyOf(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void snapshotMenu(DispenserMenu var1, int var2) {
      Map var3 = this.menuCounts.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.rankKeyOf(var5);
         if (SlotSwapUtil.canSwapSlot(var2, var6 < 0 ? -1 : var6 / 10, this.isSwapDisplaced(var2, var4, var5))) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void countTrackedItems(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.openMenu == var1) {
         this.openMenu = var1;
         this.openMenuIndex = var2;
         this.snapshotMenu(var1, var2);
      }
   }

   private void scanDispensedItems(Minecraft var1) {
      if (var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.seenItemEntities.contains(var4.getUUID())) {
               int var5 = this.rankKeyOf(var4.getItem());
               if (var5 >= 0) {
                  this.seenItemEntities.add(var4.getUUID());
                  BlockPos var6 = this.dispenserPositions[var5 / 10];
                  if (var6 != null) {
                     BlockState var7 = var1.level.getBlockState(var6);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var6).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.dispensedItemEntities.add(var4.getUUID());
                           this.dispensedKeys.add(var5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack snapshotMenu(Minecraft var1, ItemEntity var2) {
      this.refresh(var1);
      return this.dispensedItemEntities.contains(var2.getUUID()) ? this.spoofStack(var2.getItem()) : var2.getItem();
   }

   public void reset(Minecraft var1) {
      Arrays.fill(this.takenRanks, 0);
      Arrays.fill(this.spoofedRanks, 0);
      Arrays.fill(this.announced, false);
      this.takeDetector.reset();

      for (Map var3 : this.menuCounts) {
         var3.clear();
      }

      this.openMenu = null;
      this.openMenuIndex = -1;
      Arrays.fill(this.swapDetected, false);
      Arrays.fill(this.swapSlotA, -1);
      Arrays.fill(this.swapSlotB, -1);
      this.dispensedItemEntities.clear();
      this.seenItemEntities.clear();
      this.dispensedKeys.clear();
      if (var1 != null && var1.level != null) {
         for (Entity var5 : var1.level.entitiesForRendering()) {
            this.seenItemEntities.add(var5.getUUID());
         }
      }

      this.previousCounts.clear();
      if (var1 != null && var1.player != null) {
         this.previousCounts.putAll(this.countTrackedItems(var1));
      }

      this.baselineCounts.clear();
      this.baselineCounts.putAll(this.previousCounts);
   }

   public void refresh(Minecraft var1) {
      if (this.reset() && var1 != null && var1.player != null && var1.level != null) {
         this.scanDispensedItems(var1);
         this.update(var1, this.countTrackedItems(var1), var1.level.getGameTime());
      }
   }

   void update(Minecraft var1, Map<Integer, Integer> var2, long var3) {
      if (this.takeDetector
         .isHeld(
            this.takenRanks[0] > 0 && this.takenRanks[1] > 0,
            var2.getOrDefault(this.takenRanks[0], 0) <= this.baselineCounts.getOrDefault(this.takenRanks[0], 0)
               && var2.getOrDefault(10 + this.takenRanks[1], 0) <= this.baselineCounts.getOrDefault(10 + this.takenRanks[1], 0)
               && (!this.swapDetected[0] || var2.getOrDefault(this.spoofedRanks[0], 0) <= this.baselineCounts.getOrDefault(this.spoofedRanks[0], 0))
               && (!this.swapDetected[1] || var2.getOrDefault(10 + this.spoofedRanks[1], 0) <= this.baselineCounts.getOrDefault(10 + this.spoofedRanks[1], 0)),
            var3
         )) {
         this.reset(var1);
      } else {
         for (Entry var6 : var2.entrySet()) {
            if (this.dispensedKeys.contains(var6.getKey()) && (Integer)var6.getValue() > this.previousCounts.getOrDefault(var6.getKey(), 0)) {
               int var7 = (Integer)var6.getKey() / 10;
               int var8 = (Integer)var6.getKey() % 10;
               if (this.takenRanks[var7] == 0) {
                  this.takenRanks[var7] = var8;
               }
            }
         }

         if (!this.randomize) {
            System.arraycopy(this.takenRanks, 0, this.spoofedRanks, 0, 2);
         }

         if (this.randomize && this.spoofedRanks[0] == 0 && (this.takenRanks[0] > 0 || this.takenRanks[1] > 0)) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();
            RankParser.RankPair var12 = RankParser.pickRankPair(
               this.takenRanks[0] > 0 ? this.takenRanks[0] : var10.nextInt(1, 10),
               this.takenRanks[1] > 0 ? this.takenRanks[1] : var10.nextInt(1, 10),
               this.ascending,
               var10.nextInt()
            );
            ArrayList var13 = new ArrayList();

            for (int var14 = 1; var14 <= 9; var14++) {
               for (int var9 = 1; var9 <= 9; var9++) {
                  if ((this.ascending ? var14 > var9 : var9 > var14) && !this.rankStacks[0][var14].isEmpty() && !this.rankStacks[1][var9].isEmpty()) {
                     var13.add(new RankParser.RankPair(var14, var9));
                  }
               }
            }

            if (this.rankStacks[0][var12.host()].isEmpty() || this.rankStacks[1][var12.viewer()].isEmpty()) {
               if (var13.isEmpty()) {
                  return;
               }

               List var15 = var13.stream().filter(var1x -> var1x.host() == this.takenRanks[0] || var1x.viewer() == this.takenRanks[1]).toList();
               List var16 = var15.isEmpty() ? var13 : var15;
               var12 = (RankParser.RankPair)var16.get(var10.nextInt(var16.size()));
            }

            this.spoofedRanks[0] = var12.host();
            this.spoofedRanks[1] = var12.viewer();
         }

         for (int var11 = 0; var11 < 2; var11++) {
            if (!this.announced[var11] && this.takenRanks[var11] > 0 && this.spoofedRanks[var11] > 0) {
               this.announced[var11] = (boolean)1;
               RigLog.logMessage(
                  "Paper",
                  (var11 == 0 ? "Host: " : "Viewer: ")
                     + RigLog.describeRig(Integer.toString(this.takenRanks[var11]), Integer.toString(this.spoofedRanks[var11]), this.randomize, true)
               );
            }
         }

         this.previousCounts.clear();
         this.previousCounts.putAll(var2);
      }
   }

   public ItemStack spoofStack(ItemStack var1) {
      if (this.spoofedStacks.contains(var1)) {
         return var1;
      } else {
         int var2 = this.rankKeyOf(var1);
         if (var2 < 0) {
            return var1;
         } else {
            int var3 = var2 / 10;
            int var4 = var2 % 10;
            if (this.swapDetected[var3] && var4 == this.spoofedRanks[var3]) {
               return this.copyTracked(this.rankStacks[var3][this.takenRanks[var3]], var1.getCount());
            } else {
               return this.takenRanks[var3] == var4 && this.spoofedRanks[var3] != 0 && this.spoofedRanks[var3] != var4
                  ? this.copyTracked(this.rankStacks[var3][this.spoofedRanks[var3]], var1.getCount())
                  : var1;
            }
         }
      }
   }

   private ItemStack copyTracked(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.spoofedStacks.add(var3);
      return var3;
   }

   public ItemStack spoofSlotStack(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.computeSlotStack(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.copyTracked(var5, var5.getCount());
   }

   private ItemStack computeSlotStack(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      ItemStack var6 = this.setSlotRank(var5, var3, false);
      int var7 = this.takenRanks[var3];
      int var8 = this.spoofedRanks[var3];
      if (var7 != 0 && var8 != 0 && var7 != var8) {
         this.detectSwap(var1, var2, var3);
         if (this.swapDetected[var3]) {
            int var18 = SlotSwapUtil.remapSlot(var4, this.swapSlotA[var3], this.swapSlotB[var3], true);
            ItemStack var19 = var2.getSlot(var18).getItem();
            return this.setSlotRank(var19, var3, var18 != var4 || this.isSwapDisplaced(var3, var18, var19));
         }

         int var9 = this.countTrackedItems(var1).getOrDefault(var3 * 10 + var7, 0) > this.baselineCounts.getOrDefault(var3 * 10 + var7, 0) ? 1 : 0;
         int var10 = -1;
         int var11 = -1;
         int var12 = -1;
         int var13 = 0;
         int[] var14 = new int[9];

         for (int var15 = 0; var15 < 9; var15++) {
            ItemStack var16 = var2.getSlot(var15).getItem();
            var14[var15] = var16.getCount();
            int var17 = this.rankKeyOf(var16);
            if (var17 == var3 * 10 + var7) {
               var10 = var15;
            }

            if (var17 == var3 * 10 + var8) {
               var11 = var15;
            }

            if (var16.isEmpty()) {
               var12 = var15;
               var13++;
            } else if (var17 / 10 == var3 && var17 > 0 && this.slotRanks[var3][var15] == 0) {
               this.slotRanks[var3][var15] = var17 % 10;
            }
         }

         if (var10 < 0) {
            for (int var20 = 0; var20 < 9; var20++) {
               if (this.slotRanks[var3][var20] == var7 && var2.getSlot(var20).getItem().isEmpty()) {
                  var10 = var20;
                  break;
               }
            }

            if (var10 < 0 && var13 == 1) {
               var10 = var12;
            }
         }

         if (var10 >= 0 && var14[var10] >= this.rankStacks[var3][var7].getMaxStackSize()) {
            return var6;
         } else {
            int[] var21 = SlotCounts.transferOne(var14, var10, var11, (boolean)var9);
            if (var21[var4] == var14[var4]) {
               return var6;
            } else {
               return var21[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.rankStacks[var3][var7] : var5).copyWithCount(var21[var4]);
            }
         }
      } else {
         return var6;
      }
   }

   private boolean isSwapDisplaced(int var1, int var2, ItemStack var3) {
      if (!this.swapDetected[var1]) {
         return false;
      }

      int var4 = this.rankKeyOf(var3);
      return var4 == var1 * 10 + this.takenRanks[var1] && var2 != this.swapSlotA[var1]
         || var4 == var1 * 10 + this.spoofedRanks[var1] && var2 != this.swapSlotB[var1];
   }

   private ItemStack setSlotRank(ItemStack var1, int var2, boolean var3) {
      int var4 = this.rankKeyOf(var1);
      if (var4 >= 0 && this.swapDetected[var4 / 10]) {
         return this.spoofStack(var1);
      } else {
         return SlotSwapUtil.canSwapSlot(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.spoofStack(var1) : var1;
      }
   }

   private void detectSwap(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.swapDetected[var3] && this.takenRanks[var3] != 0 && this.spoofedRanks[var3] != 0 && this.takenRanks[var3] != this.spoofedRanks[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.rankKeyOf(var2.getSlot(var8).getItem());
            if (this.slotRanks[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.slotRanks[var3][var8] = var9 % 10;
            }

            if (this.slotRanks[var3][var8] == this.takenRanks[var3] && var2.getSlot(var8).getItem().isEmpty()) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.spoofedRanks[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.slotRanks[var3][var4] = this.takenRanks[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.rankKeyOf(var2.getSlot(var4).getItem()) != var3 * 10 + this.takenRanks[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.countTrackedItems(var1).getOrDefault(var3 * 10 + this.takenRanks[var3], 0)
                  > this.baselineCounts.getOrDefault(var3 * 10 + this.takenRanks[var3], 0)) {
               this.swapSlotA[var3] = var4;
               this.swapSlotB[var3] = var5;
               this.swapDetected[var3] = (boolean)1;
            }
         }
      }
   }

   public int hasDispenser(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      this.detectSwap(var1, var2, var3);
      return SlotSwapUtil.remapSlot(var4, this.swapSlotA[var3], this.swapSlotB[var3], this.swapDetected[var3]);
   }
}

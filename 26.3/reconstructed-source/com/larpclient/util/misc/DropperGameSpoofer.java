package com.larpclient.util.misc;

import com.larpclient.module.exploit.HorseRacingPicker;
import com.larpclient.util.data.HoldTimer;
import com.larpclient.util.inventory.HotbarSlotMapper2;
import com.larpclient.util.inventory.SlotCounts;
import com.larpclient.util.inventory.SlotPatternUtil;
import com.larpclient.util.inventory.SlotSwapTracker;
import com.larpclient.util.inventory.SlotSwapUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

final class DropperGameSpoofer {
   private final Set<ItemStack> spoofedStacks = Collections.newSetFromMap(new WeakHashMap<>());
   private final HotbarSlotMapper2 displayAssigner = new HotbarSlotMapper2();
   private final Set<UUID> trackedWorldItems = new HashSet<>();
   private final Set<String> ignoredSlots = new HashSet<>();
   private final SlotSwapTracker swapTracker = new SlotSwapTracker();
   private final boolean soloMode;
   private final boolean russianRoulette;
   private boolean rouletteHit;
   private boolean horseArmorMode;
   private boolean armorGameMode;
   private final HorseRacingPicker horseArmorShuffle = new HorseRacingPicker();
   public int selectedRank = 1;
   private int seed;
   private final BlockPos[] dropperPositions = new BlockPos[2];
   private final ItemStack[][] setupStacks = new ItemStack[2][10];
   private final int[][] slotRanks = new int[2][9];
   private final int[] actualPicks = new int[]{0, 0};
   private final int[] displayedPicks = new int[]{0, 0};
   private final boolean[] swapActive = new boolean[]{false, false};
   private final int[] swapSlotFrom = new int[]{-1, -1};
   private final int[] swapSlotTo = new int[]{-1, -1};
   private final Map<UUID, Integer> entityDisplayRanks = new HashMap<>();
   private final Set<UUID> knownEntities = new HashSet<>();
   private final Set<UUID> dispensedEntities = new HashSet<>();
   private final HoldTimer resetDetector = new HoldTimer();
   private final List<Map<Integer, Integer>> menuItemCounts = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu lastMenu;
   private int lastMenuSide = -1;
   private final Map<Integer, Integer> previousCounts = new HashMap<>();
   private final Map<Integer, Integer> baselineCounts = new HashMap<>();
   public int setupSide;
   public boolean hostFirst = (boolean)1;

   private ItemStack copySpoofed(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.spoofedStacks.add(var3);
      return var3;
   }

   public boolean refreshDisplay() {
      boolean var1 = this.rouletteHit;
      this.rouletteHit = (boolean)0;
      return var1;
   }

   public static DropperGameSpoofer createArmorGame() {
      DropperGameSpoofer var0 = new DropperGameSpoofer(true);
      var0.armorGameMode = (boolean)1;
      return var0;
   }

   public static DropperGameSpoofer createHorseArmorGame() {
      DropperGameSpoofer var0 = new DropperGameSpoofer(true);
      var0.horseArmorMode = (boolean)1;
      return var0;
   }

   public DropperGameSpoofer() {
      this(false);
   }

   public DropperGameSpoofer(boolean var1) {
      this(var1, false);
   }

   public DropperGameSpoofer(boolean var1, boolean var2) {
      this.soloMode = var1;
      this.russianRoulette = var2;
   }

   public ItemStack getSetupStack(int var1) {
      return this.setupStacks[0][var1] == null ? ItemStack.EMPTY : this.setupStacks[0][var1].copy();
   }

   public int[] getSlotRanks() {
      return (int[])this.slotRanks[0].clone();
   }

   public void setupFromDropper(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      int var4 = this.isReady() && Objects.equals(this.dropperPositions[0], var2) ? 1 : 0;
      if (var4 != 0) {
         byte var5 = 1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var3.getSlot(var6).getItem().isEmpty()) {
               var5 = 0;
            }
         }

         if (var5 == 0 || this.hasDrawn(var1) || this.russianRoulette && this.displayAssigner.findEntry()) {
            this.trackMenu(var3, 0, true);
            return;
         }
      }

      try {
         this.captureSetup(var1, var2, var3);
      } catch (IllegalArgumentException var7) {
         if (var4 == 0) {
            throw var7;
         }
      }

      this.trackMenu(var3, 0, true);
   }

   public void setupWithItems(Minecraft var1, BlockPos var2, DispenserMenu var3, Item var4, Item var5, Item var6, int[] var7) {
      if (this.isReady() && Objects.equals(this.dropperPositions[0], var2) && this.hasDrawn(var1)) {
         this.trackMenu(var3, 0, true);
      } else {
         ItemStack[] var8 = new ItemStack[]{ItemStack.EMPTY, new ItemStack(var4), new ItemStack(var5), new ItemStack(var6)};

         for (int var9 = 0; var9 < 9; var9++) {
            ItemStack var10 = var3.getSlot(var9).getItem();
            int var11 = var7[var9];
            if (!var10.isEmpty() && var11 > 0 && var11 < 4) {
               var8[var11] = var10.copyWithCount(1);
            }
         }

         int var12 = Objects.equals(this.dropperPositions[0], var2) && Arrays.equals(this.slotRanks[0], var7) ? 0 : 1;

         for (int var13 = 1; var13 <= 3; var13++) {
            var12 |= this.setupStacks[0][var13] != null && ItemStack.isSameItemSameComponents(this.setupStacks[0][var13], var8[var13]) ? 0 : 1;
         }

         this.dropperPositions[0] = var2.immutable();
         this.slotRanks[0] = (int[])var7.clone();

         for (int var14 = 1; var14 <= 3; var14++) {
            this.setupStacks[0][var14] = var8[var14];
         }

         if (var12 != 0) {
            this.reset(var1);
         }

         this.trackMenu(var3, 0, true);
      }
   }

   public void createHorseArmorGame() {
      this.selectedRank = this.selectedRank % this.refreshDisplay() + 1;
   }

   public String getSetupItemName(int var1) {
      return this.setupStacks[0][var1] == null ? "" : this.setupStacks[0][var1].getHoverName().getString().replace('\n', ' ').replace('\r', ' ');
   }

   private int refreshDisplay() {
      return this.armorGameMode ? 4 : (this.russianRoulette ? 2 : (this.soloMode ? 3 : 9));
   }

   public boolean hasDrawn(Minecraft var1) {
      if (var1 == null || !this.isReady() || var1.player == null) {
         return false;
      }

      if (this.russianRoulette || this.horseArmorMode || this.armorGameMode) {
         this.refreshDisplay(var1);
         return this.displayAssigner.hasCustomRemap();
      }

      if (this.getArmorSlotIndex(var1)) {
         return true;
      }

      Map var2 = this.setupFromDropper(var1);

      for (int var3 = 0; var3 < 2; var3++) {
         if (this.actualPicks[var3] > 0 && this.displayedPicks[var3] > 0 && this.actualPicks[var3] != this.displayedPicks[var3]) {
            int var4 = var3 * 10 + this.actualPicks[var3];
            int var5 = var3 * 10 + this.displayedPicks[var3];
            if (var2.getOrDefault(var4, 0) > this.baselineCounts.getOrDefault(var4, 0)
               || this.swapActive[var3] && var2.getOrDefault(var5, 0) > this.baselineCounts.getOrDefault(var5, 0)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasDropper(int var1) {
      return this.dropperPositions[var1] != null;
   }

   public boolean isReady() {
      return this.hasDropper(0) && (this.soloMode || this.hasDropper(1));
   }

   public BlockPos hasDropper(int var1) {
      return this.dropperPositions[var1];
   }

   public String getDisplayLabel(int var1) {
      return this.actualPicks[var1] == 0 ? "" : Integer.toString(this.displayedPicks[var1]);
   }

   public void captureSetup(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      if (var2.equals(this.dropperPositions[1 - this.setupSide])) {
         throw new IllegalArgumentException("Host and Viewer need different droppers.");
      }

      ItemStack[] var4 = new ItemStack[10];
      int[] var5 = new int[9];
      if (this.armorGameMode) {
         this.setupSide = 0;
         int var6 = 0;

         for (int var7 = 0; var7 < 9; var7++) {
            ItemStack var8 = var3.getSlot(var7).getItem();
            if (!var8.isEmpty()) {
               int var9 = getArmorSlotIndex(var8);
               if (var9 < 1 || var8.getCount() != 1 || var4[var9] != null) {
                  throw new IllegalArgumentException("Use one helmet, chestplate, leggings and boots, with the other slots empty.");
               }

               var4[var9] = var8.copyWithCount(1);
               var5[var7] = var9;
               var6++;
            }
         }

         if (var6 != 4) {
            throw new IllegalArgumentException("Armor Game requires four armor pieces, one for each equipment slot.");
         }
      } else if (this.soloMode) {
         this.setupSide = 0;
         ItemStack[] var12 = new ItemStack[9];
         int[] var14 = new int[9];
         int var16 = 0;

         for (int var18 = 0; var18 < 9; var18++) {
            ItemStack var10 = var3.getSlot(var18).getItem();
            if (this.horseArmorMode && (var10.getCount() != 1 || !BuiltInRegistries.ITEM.getKey(var10.getItem()).getPath().endsWith("_horse_armor"))) {
               throw new IllegalArgumentException("Use one horse armor per slot, three of each type.");
            }

            if (var10.isEmpty()) {
               throw new IllegalArgumentException("This game requires nine occupied slots.");
            }

            int var11 = 0;

            while (var11 < var16 && !this.isSameSpoofItem(var12[var11], var10)) {
               var11++;
            }

            if (var11 == var16) {
               var12[var16++] = var10.copyWithCount(1);
            }

            var14[var18] = var11;
         }

         var5 = this.horseArmorMode
            ? HorseRacingPicker.toArmorSlots(var14)
            : (this.russianRoulette ? SlotPatternUtil.classifyRussianRoulette(var14) : SlotPatternUtil.classifySplit454510(var14));
         if (this.russianRoulette) {
            for (int var19 = 0; var19 < 9; var19++) {
               if (var5[var19] == 1 && var3.getSlot(var19).getItem().getMaxStackSize() != 1) {
                  throw new IllegalArgumentException("Russian Roulette safe items must be unstackable.");
               }
            }
         }

         for (int var20 = 0; var20 < 9; var20++) {
            var4[var5[var20]] = var3.getSlot(var20).getItem().copyWithCount(1);
         }
      } else {
         for (int var13 = 0; var13 < 9; var13++) {
            ItemStack var15 = var3.getSlot(var13).getItem();
            int var17 = RankParser.parseRank(var15.getHoverName().getString());
            String var21 = var15.getHoverName().getString();
            if (var15.isEmpty()) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + " is empty.");
            }

            if (var17 < 1) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": cannot read rank from [" + var21 + "].");
            }

            if (RankParser.isViewerRank(var21) != (this.setupSide == 0 ? 1 : 0)) {
               throw new IllegalArgumentException(
                  "Paper Game slot "
                     + (var13 + 1)
                     + ": ["
                     + var21
                     + "] belongs to the "
                     + (this.setupSide == 0 ? "Viewer" : "Host")
                     + " side; change the setup side."
               );
            }

            if (var4[var17] != null) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": duplicate rank " + var17 + " in [" + var21 + "].");
            }

            var4[var17] = var15.copyWithCount(1);
            var5[var13] = var17;
         }
      }

      this.setupStacks[this.setupSide] = var4;
      this.dropperPositions[this.setupSide] = var2.immutable();
      this.slotRanks[this.setupSide] = var5;
      this.reset(var1);
   }

   private boolean isSameSpoofItem(ItemStack var1, ItemStack var2) {
      return var1.is(var2.getItem())
         && (
            !this.russianRoulette && !this.horseArmorMode && !this.armorGameMode
               || Objects.equals(var1.get(DataComponents.CUSTOM_NAME), var2.get(DataComponents.CUSTOM_NAME))
         );
   }

   private int getItemId(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      if (this.soloMode) {
         for (int var6 = 1; var6 <= this.refreshDisplay(); var6++) {
            if (this.hasDropper(0) && this.setupStacks[0][var6] != null && this.isSameSpoofItem(var1, this.setupStacks[0][var6])) {
               return var6;
            }
         }

         return -1;
      } else {
         String var2 = var1.getHoverName().getString();
         int var3 = RankParser.parseRank(var2);
         if (var3 < 1) {
            return -1;
         }

         int var4 = RankParser.isViewerRank(var2) ? 0 : 1;
         ItemStack var5 = this.setupStacks[var4][var3];
         return this.hasDropper(var4) && var5 != null && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> setupFromDropper(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.lastMenu) {
         this.recordMenuCounts(var2, this.lastMenuSide);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.menuItemCounts) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.getItemId(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu.getCarried();
      int var16 = this.getItemId(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.getItemId(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.dispensedEntities.contains(var7.getUUID())) {
               int var10 = this.getItemId(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void recordMenuCounts(DispenserMenu var1, int var2) {
      Map var3 = this.menuItemCounts.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.getItemId(var5);
         if (SlotSwapUtil.canSwapSlot(var2, var6 < 0 ? -1 : var6 / 10, false)) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void trackMenu(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.lastMenu == var1) {
         this.lastMenu = var1;
         this.lastMenuSide = var2;
         this.recordMenuCounts(var1, var2);
         if (this.russianRoulette || this.horseArmorMode || this.armorGameMode) {
            this.refreshDisplay(Minecraft.getInstance());
         }
      }
   }

   private void scanItemEntities(Minecraft var1) {
      if (var1.level != null) {
         if (this.soloMode && this.swapTracker.hasSnapshot()) {
            this.syncSwapTracker(var1, this.getOpenSetupMenu(var1));
         }

         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.knownEntities.contains(var4.getUUID())) {
               int var5 = this.getItemId(var4.getItem());
               if (var5 >= 0) {
                  if (this.soloMode && this.displayedPicks[0] > 0 && var1.player != null && var4.distanceToSqr(var1.player) <= 9.0) {
                     int var6 = this.swapTracker.pollPending(var5, -1);
                     if (var6 > 0 || var5 == this.actualPicks[0]) {
                        this.entityDisplayRanks.put(var4.getUUID(), var6 > 0 ? var6 : this.displayedPicks[0]);
                        this.dispensedEntities.add(var4.getUUID());
                        this.knownEntities.add(var4.getUUID());
                        continue;
                     }
                  }

                  BlockPos var10 = this.dropperPositions[var5 / 10];
                  if (var10 != null) {
                     BlockState var7 = var1.level.getBlockState(var10);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var10).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.dispensedEntities.add(var4.getUUID());
                           this.knownEntities.add(var4.getUUID());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack getItemEntityDisplay(Minecraft var1, ItemEntity var2) {
      this.tick(var1);
      if (!this.russianRoulette && !this.horseArmorMode && !this.armorGameMode) {
         if (this.soloMode && this.entityDisplayRanks.containsKey(var2.getUUID())) {
            return this.applyDisplay(var2.getItem(), this.entityDisplayRanks.get(var2.getUUID()));
         } else {
            return this.dispensedEntities.contains(var2.getUUID()) ? this.getDisplayStack(var2.getItem()) : var2.getItem();
         }
      } else {
         return this.trackedWorldItems.contains(var2.getUUID())
            ? this.applyDisplay(var2.getItem(), this.displayAssigner.findEntry("world:" + var2.getUUID(), 1))
            : var2.getItem();
      }
   }

   public void reset(Minecraft var1) {
      this.horseArmorShuffle.reset();
      this.rouletteHit = (boolean)0;
      this.displayAssigner.reset(this.slotRanks[0]);
      this.trackedWorldItems.clear();
      this.ignoredSlots.clear();
      if ((this.russianRoulette || this.horseArmorMode || this.armorGameMode) && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            ItemStack var3 = var2 < 36
               ? var1.player.getInventory().getItem(var2)
               : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            if (this.getItemId(var3) > 0) {
               this.ignoredSlots.add("player:" + var2);
            }
         }
      }

      if (this.armorGameMode && var1.level != null) {
         for (Entity var4 : var1.level.entitiesForRendering()) {
            if (var4 instanceof ArmorStand var6) {
               for (int var5 = 1; var5 <= 4; var5++) {
                  if (!var6.getItemBySlot(toEquipmentSlot(var5)).isEmpty()) {
                     this.ignoredSlots.add("stand:" + var6.getUUID() + ":" + var5);
                  }
               }
            }
         }
      }

      this.swapTracker.reset();
      this.entityDisplayRanks.clear();
      this.seed = ThreadLocalRandom.current().nextInt(2520);
      Arrays.fill(this.actualPicks, 0);
      Arrays.fill(this.displayedPicks, 0);
      this.resetDetector.reset();

      for (Map var10 : this.menuItemCounts) {
         var10.clear();
      }

      this.lastMenu = null;
      this.lastMenuSide = -1;
      Arrays.fill(this.swapActive, false);
      Arrays.fill(this.swapSlotFrom, -1);
      Arrays.fill(this.swapSlotTo, -1);
      this.dispensedEntities.clear();
      this.knownEntities.clear();
      if (var1.level != null) {
         for (Entity var11 : var1.level.entitiesForRendering()) {
            this.knownEntities.add(var11.getUUID());
         }
      }

      this.previousCounts.clear();
      if (var1.player != null) {
         this.previousCounts.putAll(this.setupFromDropper(var1));
      }

      this.baselineCounts.clear();
      this.baselineCounts.putAll(this.previousCounts);
   }

   public void tick(Minecraft var1) {
      if (var1 != null && this.isReady() && var1.player != null && var1.level != null) {
         if (!this.russianRoulette && !this.horseArmorMode && !this.armorGameMode) {
            this.scanItemEntities(var1);
            int[] var2 = (int[])this.actualPicks.clone();
            Map var3 = this.setupFromDropper(var1);
            if (this.resetDetector
               .isHeld(
                  this.actualPicks[0] > 0 && (this.soloMode || this.actualPicks[1] > 0),
                  !this.getArmorSlotIndex(var1)
                     && var3.getOrDefault(this.actualPicks[0], 0) <= this.baselineCounts.getOrDefault(this.actualPicks[0], 0)
                     && var3.getOrDefault(10 + this.actualPicks[1], 0) <= this.baselineCounts.getOrDefault(10 + this.actualPicks[1], 0)
                     && (!this.swapActive[0] || var3.getOrDefault(this.displayedPicks[0], 0) <= this.baselineCounts.getOrDefault(this.displayedPicks[0], 0))
                     && (
                        !this.swapActive[1]
                           || var3.getOrDefault(10 + this.displayedPicks[1], 0) <= this.baselineCounts.getOrDefault(10 + this.displayedPicks[1], 0)
                     ),
                  var1.level.getGameTime()
               )) {
               this.reset(var1);
            } else {
               for (Entry var5 : var3.entrySet()) {
                  if ((Integer)var5.getValue() > this.previousCounts.getOrDefault(var5.getKey(), 0)) {
                     int var6 = (Integer)var5.getKey() / 10;
                     int var7 = (Integer)var5.getKey() % 10;
                     if (this.russianRoulette || this.horseArmorMode || this.armorGameMode) {
                        int var8 = SlotPatternUtil.mergeSlotValue(
                           this.actualPicks[var6], this.displayedPicks[var6] == 0 ? this.selectedRank : this.displayedPicks[var6], var7
                        );
                        if (var8 != this.actualPicks[var6]) {
                           this.swapTracker.reset();
                           this.actualPicks[var6] = var8;
                        }
                     } else if (this.actualPicks[var6] == 0) {
                        this.actualPicks[var6] = var7;
                     }
                  }
               }

               if (this.displayedPicks[0] == 0 && (this.actualPicks[0] > 0 || this.actualPicks[1] > 0)) {
                  if (this.soloMode) {
                     this.displayedPicks[0] = this.selectedRank;
                  } else {
                     ThreadLocalRandom var9 = ThreadLocalRandom.current();
                     RankParser.RankPair var11 = RankParser.pickRankPair(
                        this.actualPicks[0] > 0 ? this.actualPicks[0] : var9.nextInt(1, 10),
                        this.actualPicks[1] > 0 ? this.actualPicks[1] : var9.nextInt(1, 10),
                        this.hostFirst,
                        var9.nextInt()
                     );
                     this.displayedPicks[0] = var11.host();
                     this.displayedPicks[1] = var11.viewer();
                  }
               }

               for (int var10 = 0; var10 < 2; var10++) {
                  if (this.actualPicks[var10] > 0 && var2[var10] == 0) {
                     this.tick(var10, this.actualPicks[var10], this.displayedPicks[var10]);
                  }
               }

               this.previousCounts.clear();
               this.previousCounts.putAll(var3);
            }
         } else {
            this.refreshDisplay(var1);
         }
      }
   }

   private void tick(int var1, int var2, int var3) {
      if (var2 >= 1 && var3 >= 1) {
         String var4 = this.setupStacks[var1][var2].getHoverName().getString();
         String var5 = this.setupStacks[var1][var3].getHoverName().getString();
         RigLog.logMessage(
            this.russianRoulette ? "Russian Roulette" : "45/45/10",
            (this.soloMode ? "" : (var1 == 0 ? "Host: " : "Viewer: "))
               + "Drew "
               + var4
               + (var2 == var3 ? "; unchanged." : "; spoofed as " + var5 + " (visual only).")
         );
      }
   }

   private void refreshDisplay(Minecraft var1) {
      if (this.isReady() && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.getOpenSetupMenu(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.getItemId(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.getItemId(var6);
            if (var8 < 0) {
               this.ignoredSlots.remove(var7);
            }

            if (!this.ignoredSlots.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         BlockPos var15 = this.dropperPositions[0];
         BlockState var16 = var1.level.getBlockState(var15);
         Vec3 var17 = Vec3.atCenterOf(var15);
         if (var16.is(Blocks.DROPPER) || var16.is(Blocks.DISPENSER)) {
            Direction var18 = (Direction)var16.getValue(DispenserBlock.FACING);
            var17 = var17.add(var18.getStepX() * 0.7, var18.getStepY() * 0.7, var18.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.getItemId(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var17) <= 2.25 ? 1 : 0;
               if (!this.knownEntities.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.trackedWorldItems.add(var10.getUUID());
               }

               if (this.trackedWorldItems.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.getItemId(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (this.armorGameMode) {
            for (Entity var25 : var1.level.entitiesForRendering()) {
               if (var25 instanceof ArmorStand var20 && var20.distanceToSqr(var17) < 9.0) {
                  for (int var26 = 1; var26 <= 4; var26++) {
                     String var27 = "stand:" + var20.getUUID() + ":" + var26;
                     int var13 = this.getItemId(var20.getItemBySlot(toEquipmentSlot(var26)));
                     if (var13 < 1) {
                        this.ignoredSlots.remove(var27);
                     }

                     if (!this.ignoredSlots.contains(var27)) {
                        var2.put(var27, var13);
                        var3.add(var27);
                     }
                  }
               }
            }
         }

         if (this.russianRoulette && var4 != null) {
            this.displayAssigner.restoreStock(var2);
         }

         if (this.horseArmorMode) {
            this.displayAssigner.updateWithSupplier(var2, var3, () -> this.horseArmorShuffle.nextPick(this.selectedRank), this.seed, var4 != null);
         } else {
            this.displayAssigner.update(var2, var3, this.selectedRank, this.seed, var4 != null);
         }

         if (this.russianRoulette) {
            for (int var21 = 0; var21 < 9; var21++) {
               if (this.slotRanks[0][var21] == 2) {
                  this.displayAssigner.moveEntryToSlot(2, var21);
               }
            }
         }

         if (this.horseArmorMode && var4 != null && this.displayAssigner.isAllStock()) {
            this.horseArmorShuffle.reset();
         }

         for (HotbarSlotMapper2.PendingSwap var24 : this.displayAssigner.drainSwaps()) {
            if (this.russianRoulette && var24.display() == 2) {
               this.rouletteHit = (boolean)1;
               this.selectedRank = 1;
            }

            this.tick(0, var24.actual(), var24.display());
         }
      }
   }

   private static EquipmentSlot toEquipmentSlot(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int getArmorSlotIndex(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == toEquipmentSlot(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] applyDisplay(ArmorStand var1) {
      if (this.armorGameMode && this.isReady() && !(var1.distanceToSqr(Vec3.atCenterOf(this.dropperPositions[0])) > 16.0)) {
         this.refreshDisplay(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(toEquipmentSlot(var3));
            int var5 = this.displayAssigner.findEntry("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = this.getItemId(var4) > 0 ? this.applyDisplay(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   private ItemStack setupFromDropper(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.refreshDisplay(var2);
      if (var2.player == null) {
         return var1;
      }

      for (int var3 = 0; var3 < 38; var3++) {
         ItemStack var4 = var3 < 36
            ? var2.player.getInventory().getItem(var3)
            : (var3 == 36 ? var2.player.containerMenu.getCarried() : var2.player.getOffhandItem());
         if (var1 == var4) {
            return this.applyDisplay(
               var1,
               this.displayAssigner
                  .findEntry("player:" + var3, this.ignoredSlots.contains("player:" + var3) ? this.getItemId(var1) : (this.getItemId(var1) > 0 ? 1 : -1))
            );
         }
      }

      return var1;
   }

   private void syncSwapTracker(Minecraft var1, DispenserMenu var2) {
      if (this.soloMode && var1.player != null && this.displayedPicks[0] != 0) {
         int[] var3 = new int[47];
         int[] var4 = new int[47];
         Arrays.fill(var3, -2);
         Arrays.fill(var4, -1);

         for (int var5 = 0; var5 < 38; var5++) {
            ItemStack var6 = var5 < 36
               ? var1.player.getInventory().getItem(var5)
               : (var5 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            int var7 = this.getItemId(var6);
            var3[var5] = var7;
            var4[var5] = var7 == this.actualPicks[0] ? this.displayedPicks[0] : var7;
         }

         if (var2 != null) {
            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = this.getItemId(var2.getSlot(var8).getItem());
               var3[38 + var8] = var9;
               var4[38 + var8] = var9;
               if (this.swapActive[0] && (var8 == this.swapSlotFrom[0] || var8 == this.swapSlotTo[0])) {
                  var4[38 + var8] = var9 == this.actualPicks[0] ? this.displayedPicks[0] : (var9 == this.displayedPicks[0] ? this.actualPicks[0] : var9);
               }
            }
         }

         this.swapTracker.update(var3, var4);
      }
   }

   private boolean getArmorSlotIndex(Minecraft var1) {
      if (this.soloMode && !this.russianRoulette && !this.horseArmorMode && !this.armorGameMode && this.swapActive[0] && this.swapTracker.hasSnapshot()) {
         DispenserMenu var2 = this.getOpenSetupMenu(var1);
         if (var2 != null) {
            this.syncSwapTracker(var1, var2);
         }

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = SlotSwapUtil.remapSlot(var3, this.swapSlotFrom[0], this.swapSlotTo[0], true);
            if (this.swapTracker.getSlotValue(38 + var4) != this.slotRanks[0][var3]) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private DispenserMenu getOpenSetupMenu(Minecraft var1) {
      return var1.player != null && var1.player.containerMenu == this.lastMenu && this.lastMenuSide == 0 ? this.lastMenu : null;
   }

   private ItemStack applyDisplay(ItemStack var1, int var2) {
      return var2 > 0 && var2 <= this.refreshDisplay() && this.setupStacks[0][var2] != null
         ? this.copySpoofed(this.setupStacks[0][var2], var1.getCount())
         : var1;
   }

   public ItemStack getDisplayStack(ItemStack var1) {
      if (this.spoofedStacks.contains(var1)) {
         return var1;
      }

      if (!this.russianRoulette && !this.horseArmorMode && !this.armorGameMode) {
         if (this.soloMode && this.swapTracker.hasSnapshot()) {
            Minecraft var2 = Minecraft.getInstance();
            this.syncSwapTracker(var2, this.getOpenSetupMenu(var2));
            if (var2.player != null) {
               for (int var3 = 0; var3 < 36; var3++) {
                  if (var1 == var2.player.getInventory().getItem(var3)) {
                     return this.applyDisplay(var1, this.swapTracker.getSlotValue(var3));
                  }
               }

               if (var1 == var2.player.containerMenu.getCarried()) {
                  return this.applyDisplay(var1, this.swapTracker.getSlotValue(36));
               }

               if (var1 == var2.player.getOffhandItem()) {
                  return this.applyDisplay(var1, this.swapTracker.getSlotValue(37));
               }
            }
         }

         int var5 = this.getItemId(var1);
         if (var5 < 0) {
            return var1;
         } else {
            int var6 = var5 / 10;
            int var4 = var5 % 10;
            if (!this.soloMode && this.swapActive[var6] && var4 == this.displayedPicks[var6]) {
               return this.copySpoofed(this.setupStacks[var6][this.actualPicks[var6]], var1.getCount());
            } else {
               return this.actualPicks[var6] == var4 && this.displayedPicks[var6] != 0 && this.displayedPicks[var6] != var4
                  ? this.copySpoofed(this.setupStacks[var6][this.displayedPicks[var6]], var1.getCount())
                  : var1;
            }
         }
      } else {
         return this.setupFromDropper(var1);
      }
   }

   public ItemStack getMenuSlotDisplay(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.scanItemEntities(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.copySpoofed(var5, var5.getCount());
   }

   private ItemStack scanItemEntities(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      if (!this.russianRoulette && !this.horseArmorMode && !this.armorGameMode) {
         ItemStack var18 = this.spoofSideStack(var5, var3, false);
         int var19 = this.actualPicks[var3];
         int var8 = this.displayedPicks[var3];
         if (var19 != 0 && var8 != 0 && var19 != var8) {
            this.detectSwap(var1, var2, var3);
            if (this.soloMode && this.swapActive[var3]) {
               this.syncSwapTracker(var1, var2);
               int var21 = SlotSwapUtil.remapSlot(var4, this.swapSlotFrom[var3], this.swapSlotTo[var3], true);
               return this.applyDisplay(var2.getSlot(var21).getItem(), this.swapTracker.getSlotValue(38 + var21));
            }

            if (this.swapActive[var3]) {
               int var20 = SlotSwapUtil.remapSlot(var4, this.swapSlotFrom[var3], this.swapSlotTo[var3], true);
               ItemStack var22 = var2.getSlot(var20).getItem();
               return this.spoofSideStack(var22, var3, var20 != var4);
            }

            int var9 = this.setupFromDropper(var1).getOrDefault(var3 * 10 + var19, 0) > this.baselineCounts.getOrDefault(var3 * 10 + var19, 0) ? 1 : 0;
            int var10 = -1;
            int var11 = -1;
            int var12 = -1;
            int var13 = 0;
            int[] var14 = new int[9];

            for (int var15 = 0; var15 < 9; var15++) {
               ItemStack var16 = var2.getSlot(var15).getItem();
               var14[var15] = var16.getCount();
               int var17 = this.getItemId(var16);
               if (var17 == var3 * 10 + var19) {
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
               for (int var23 = 0; var23 < 9; var23++) {
                  if (this.slotRanks[var3][var23] == var19 && var2.getSlot(var23).getItem().isEmpty()) {
                     var10 = var23;
                     break;
                  }
               }

               if (var10 < 0 && var13 == 1) {
                  var10 = var12;
               }
            }

            if (this.soloMode) {
               var11 = this.findSwapTarget(var2, var8);
            }

            if (var10 >= 0 && var14[var10] >= this.setupStacks[var3][var19].getMaxStackSize()) {
               return var18;
            } else {
               int[] var24 = SlotCounts.transferOne(var14, var10, var11, (boolean)var9);
               if (var24[var4] == var14[var4]) {
                  return var18;
               } else {
                  return var24[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.setupStacks[var3][var19] : var5).copyWithCount(var24[var4]);
               }
            }
         } else {
            return var18;
         }
      } else {
         this.refreshDisplay(var1);
         int var6 = this.displayAssigner.mapSlot(var4);
         ItemStack var7 = var2.getSlot(var6).getItem();
         return this.applyDisplay(var7, this.displayAssigner.findEntry("stock:" + var6, this.getItemId(var7) > 0 ? 1 : -1));
      }
   }

   private ItemStack spoofSideStack(ItemStack var1, int var2, boolean var3) {
      int var4 = this.getItemId(var1);
      if (!this.soloMode && var4 >= 0 && this.swapActive[var4 / 10]) {
         return this.getDisplayStack(var1);
      } else {
         return SlotSwapUtil.canSwapSlot(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.getDisplayStack(var1) : var1;
      }
   }

   private int findSwapTarget(DispenserMenu var1, int var2) {
      int[] var3 = new int[9];

      for (int var4 = 0; var4 < 9; var4++) {
         var3[var4] = this.getItemId(var1.getSlot(var4).getItem());
      }

      return SlotPatternUtil.indexOfNthMatch(var3, var2, this.seed);
   }

   private void detectSwap(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.swapActive[var3] && this.actualPicks[var3] != 0 && this.displayedPicks[var3] != 0 && this.actualPicks[var3] != this.displayedPicks[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.getItemId(var2.getSlot(var8).getItem());
            if (this.slotRanks[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.slotRanks[var3][var8] = var9 % 10;
            }

            if (this.slotRanks[var3][var8] == this.actualPicks[var3] && (var4 < 0 || var2.getSlot(var8).getItem().isEmpty())) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.displayedPicks[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (this.soloMode) {
            var5 = this.findSwapTarget(var2, this.displayedPicks[var3]);
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.slotRanks[var3][var4] = this.actualPicks[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.getItemId(var2.getSlot(var4).getItem()) != var3 * 10 + this.actualPicks[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.setupFromDropper(var1).getOrDefault(var3 * 10 + this.actualPicks[var3], 0)
                  > this.baselineCounts.getOrDefault(var3 * 10 + this.actualPicks[var3], 0)) {
               this.swapSlotFrom[var3] = var4;
               this.swapSlotTo[var3] = var5;
               this.swapActive[var3] = (boolean)1;
            }
         }
      }
   }

   public int copySpoofed(Minecraft var1, int var2) {
      this.refreshDisplay(var1);
      return this.displayAssigner.stockSlotOf("player:" + var2);
   }

   public void setupWithItems(DispenserMenu var1, int var2) {
      int var3 = this.displayAssigner.findEntry("player:" + var2, -1);

      for (int var4 = 0; var4 < 9; var4++) {
         if ((!this.russianRoulette || (this.slotRanks[0][var4] == 2 ? 1 : 0) == (var3 == 2 ? 1 : 0))
            && var1.getSlot(this.displayAssigner.mapSlot(var4)).getItem().isEmpty()) {
            this.displayAssigner.swapKeyToSlot("player:" + var2, var4);
            return;
         }
      }
   }

   public boolean trySwapClick(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (this.soloMode && !this.russianRoulette && !this.horseArmorMode && !this.armorGameMode && this.swapActive[0]) {
         ItemStack var5 = var4 == 36 ? var2.getCarried() : var1.player.getInventory().getItem(var4);
         ItemStack var6 = var2.getSlot(var3).getItem();
         if (var6.getCount() == 1 && var6.getMaxStackSize() == 1 && ItemStack.matches(var6, var5)) {
            this.syncSwapTracker(var1, var2);
            return this.swapTracker.swapIfMatching(38 + var3, var4);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int trackMenu(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (!this.russianRoulette && !this.horseArmorMode && !this.armorGameMode) {
         this.detectSwap(var1, var2, var3);
         if (this.soloMode && this.swapActive[var3]) {
            this.syncSwapTracker(var1, var2);
         }

         return SlotSwapUtil.remapSlot(var4, this.swapSlotFrom[var3], this.swapSlotTo[var3], this.swapActive[var3]);
      } else {
         this.refreshDisplay(var1);
         return this.displayAssigner.mapSlot(var4);
      }
   }
}

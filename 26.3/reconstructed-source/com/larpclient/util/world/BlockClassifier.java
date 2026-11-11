package com.larpclient.util.world;

import com.larpclient.nq$k$SectionCounts;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class BlockClassifier {
   private static final Set<Block> rareBlocks = Set.of(
      Blocks.BEACON, Blocks.RESPAWN_ANCHOR, Blocks.ENCHANTING_TABLE, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.CONDUIT
   );
   private static final Set<Block> storageBlocks = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER);
   private static final Set<Block> utilityBlocks = Set.of(
      Blocks.CRAFTING_TABLE,
      Blocks.FURNACE,
      Blocks.BLAST_FURNACE,
      Blocks.SMOKER,
      Blocks.ANVIL,
      Blocks.CHIPPED_ANVIL,
      Blocks.DAMAGED_ANVIL,
      Blocks.BREWING_STAND,
      Blocks.SMITHING_TABLE,
      Blocks.LOOM,
      Blocks.STONECUTTER,
      Blocks.GRINDSTONE,
      Blocks.CARTOGRAPHY_TABLE,
      Blocks.FLETCHING_TABLE,
      Blocks.COMPOSTER
   );
   private static final Set<Block> buildingBlocks = Set.of(
      Blocks.GLASS,
      Blocks.GLASS_PANE,
      Blocks.TINTED_GLASS,
      Blocks.IRON_BLOCK,
      Blocks.GOLD_BLOCK,
      Blocks.DIAMOND_BLOCK,
      Blocks.EMERALD_BLOCK,
      Blocks.NETHERITE_BLOCK,
      Blocks.REDSTONE_BLOCK,
      Blocks.POLISHED_DEEPSLATE,
      Blocks.DEEPSLATE_BRICKS,
      Blocks.DEEPSLATE_TILES
   );

   public static boolean isFullHoneyHive(BlockState var0) {
      return (var0.is(Blocks.BEEHIVE) || var0.is(Blocks.BEE_NEST)) && (Integer)var0.getValue(BlockStateProperties.LEVEL_HONEY) == 5;
   }

   public static BlockClassifier.Category classify(BlockState var0, int var1, boolean var2) {
      Block var3 = var0.getBlock();
      if (var3 instanceof ShulkerBoxBlock || rareBlocks.contains(var3) || isFullHoneyHive(var0)) {
         return BlockClassifier.Category.DEDICATED;
      }

      if (var0.is(Blocks.ENDER_CHEST)) {
         return var2 ? BlockClassifier.Category.STORAGE : BlockClassifier.Category.DEDICATED;
      }

      if (storageBlocks.contains(var3)) {
         return BlockClassifier.Category.STORAGE;
      }

      if (utilityBlocks.contains(var3)) {
         return BlockClassifier.Category.UTILITY;
      }

      if (!buildingBlocks.contains(var3) && !(var3 instanceof StainedGlassBlock) && !(var3 instanceof StainedGlassPaneBlock)) {
         if (var0.is(Blocks.DEEPSLATE) && var0.getValue(BlockStateProperties.AXIS) != Axis.Y) {
            return BlockClassifier.Category.ALTERED;
         } else {
            return !var0.is(Blocks.COBBLED_DEEPSLATE) && (var1 < 16 || !var0.is(Blocks.DEEPSLATE))
               ? BlockClassifier.Category.NONE
               : BlockClassifier.Category.ALTERED;
         }
      } else {
         return BlockClassifier.Category.CRAFTED;
      }
   }

   public static final class BuildScanner {
      private final Map<Integer, nq$k$SectionCounts> sections = new HashMap<>();
      private final BlockClassifier.CategoryCache categoryCache = new BlockClassifier.CategoryCache();
      private nq$k$SectionCounts lastSection;
      private int lastSectionIndex;

      public void clear() {
         this.sections.clear();
         this.lastSection = null;
      }

      public int sectionCount() {
         return this.sections.size();
      }

      public BlockClassifier.Category classify(BlockState var1, int var2, boolean var3) {
         return this.categoryCache.classify(var1, var2, var3);
      }

      public void addBlock(BlockState var1, int var2, boolean var3, boolean var4) {
         this.addBlockAt(var1, 0, var2, 0, var3, var4);
      }

      public void addBlockAt(BlockState var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         this.addCategorized(this.categoryCache.classify(var1, var3, var5), var1, var2, var3, var4, var5, var6);
      }

      public void addCategorized(BlockClassifier.Category var1, BlockState var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (var1 != BlockClassifier.Category.NONE && (var1 != BlockClassifier.Category.ALTERED || var7)) {
            int var8 = var4 >> 4;
            nq$k$SectionCounts var9 = this.lastSection != null && this.lastSectionIndex == var8 ? this.lastSection : this.sections.get(var8);
            if (var9 == null) {
               this.sections.put(var8, var9 = new nq$k$SectionCounts(var4));
            }

            this.lastSection = var9;
            this.lastSectionIndex = var8;
            if (var2.is(Blocks.ENDER_CHEST) && !var6) {
               var9.enderChests++;
            }

            if (var1 != BlockClassifier.Category.ALTERED) {
               var9.expandBounds(var3, var4, var5);
            }

            switch (var1) {
               case STORAGE:
                  var9.storage++;
                  break;
               case UTILITY:
                  var9.utilities++;
                  break;
               case CRAFTED:
                  var9.crafted++;
                  break;
               case ALTERED:
                  var9.altered++;
                  break;
               case DEDICATED:
                  var9.dedicated++;
                  if (var9.clue.isEmpty()) {
                     var9.clue = var2.getBlock().getName().getString();
                     var9.sampleY = var4;
                  }
            }
         }
      }

      public BuildScanResult bestResult(String var1, int var2, int var3) {
         return this.bestResultWithEnderThreshold(var1, var2, var3, 1);
      }

      public BuildScanResult bestResultWithEnderThreshold(String var1, int var2, int var3, int var4) {
         BuildScanResult var5 = BuildScanResult.EMPTY;

         for (nq$k$SectionCounts var7 : this.sections.values()) {
            BuildScanResult var8 = var7.toResult();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }
   }

   public enum Category {
      NONE,
      STORAGE,
      UTILITY,
      CRAFTED,
      ALTERED,
      DEDICATED;
   }

   public static final class CategoryCache {
      private static final BlockClassifier.Category[] cATEGORIES = BlockClassifier.Category.values();
      private static final int sIZE = 256;
      private final BlockState[] states = new BlockState[256];
      private final byte[] shallowCategories = new byte[256];
      private final byte[] deepCategories = new byte[256];
      private boolean enderChestsCounted;

      public BlockClassifier.Category classify(BlockState var1, int var2, boolean var3) {
         if (var3 != this.enderChestsCounted) {
            this.clear();
            this.enderChestsCounted = var3;
         }

         int var4 = System.identityHashCode(var1) & 0xFF;
         if (this.states[var4] != var1) {
            this.shallowCategories[var4] = (byte)BlockClassifier.classify(var1, 15, var3).ordinal();
            this.deepCategories[var4] = (byte)BlockClassifier.classify(var1, 16, var3).ordinal();
            this.states[var4] = var1;
         }

         return cATEGORIES[var2 >= 16 ? this.deepCategories[var4] : this.shallowCategories[var4]];
      }

      public void clear() {
         Arrays.fill(this.states, null);
      }
   }
}

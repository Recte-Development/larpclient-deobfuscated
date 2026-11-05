package com.larpclient;

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
import net.minecraft.world.phys.AABB;

public final class j4 {
   private static final Set<Block> k = Set.of(Blocks.BEACON, Blocks.RESPAWN_ANCHOR, Blocks.ENCHANTING_TABLE, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.CONDUIT);
   private static final Set<Block> B = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER);
   private static final Set<Block> c = Set.of(
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
   private static final Set<Block> f = Set.of(
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

   public static boolean t(BlockState var0) {
      return (var0.is(Blocks.BEEHIVE) || var0.is(Blocks.BEE_NEST)) && (Integer)var0.getValue(BlockStateProperties.LEVEL_HONEY) == 5;
   }

   public static j4.t F(BlockState var0, int var1, boolean var2) {
      Block var3 = var0.getBlock();
      if (var3 instanceof ShulkerBoxBlock || k.contains(var3) || t(var0)) {
         return j4.t.i;
      }

      if (var0.is(Blocks.ENDER_CHEST)) {
         return var2 ? j4.t.A : j4.t.i;
      }

      if (B.contains(var3)) {
         return j4.t.A;
      }

      if (c.contains(var3)) {
         return j4.t.M;
      }

      if (!f.contains(var3) && !(var3 instanceof StainedGlassBlock) && !(var3 instanceof StainedGlassPaneBlock)) {
         if (var0.is(Blocks.DEEPSLATE) && var0.getValue(BlockStateProperties.AXIS) != Axis.Y) {
            return j4.t.c;
         } else {
            return !var0.is(Blocks.COBBLED_DEEPSLATE) && (var1 < 16 || !var0.is(Blocks.DEEPSLATE)) ? j4.t.p : j4.t.c;
         }
      } else {
         return j4.t.X;
      }
   }

   public enum t {
      p,
      A,
      M,
      X,
      c,
      i;
   }

   public static final class v {
      private final Map<Integer, j4.v.u> P = new HashMap<>();

      public void J() {
         this.P.clear();
      }

      public int r() {
         return this.P.size();
      }

      public void x(BlockState var1, int var2, boolean var3, boolean var4) {
         this.A(var1, 0, var2, 0, var3, var4);
      }

      public void A(BlockState var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         j4.t var7 = j4.F(var1, var3, var5);
         if (var7 != j4.t.p && (var7 != j4.t.c || var6)) {
            j4.v.u var8 = this.P.computeIfAbsent(var3 >> 4, var1x -> new j4.v.u(var3));
            if (var1.is(Blocks.ENDER_CHEST) && !var5) {
               var8.s++;
            }

            if (var7 != j4.t.c) {
               var8.w(var2, var3, var4);
            }

            switch (var7) {
               case A:
                  var8.Q++;
                  break;
               case M:
                  var8.U++;
                  break;
               case X:
                  var8.Z++;
                  break;
               case c:
                  var8.M++;
                  break;
               case i:
                  var8.T++;
                  if (var8.f.isEmpty()) {
                     var8.f = var1.getBlock().getName().getString();
                     var8.t = var3;
                  }
            }
         }
      }

      public l4 K(String var1, int var2, int var3) {
         return this.u(var1, var2, var3, 1);
      }

      public l4 u(String var1, int var2, int var3, int var4) {
         l4 var5 = l4.EMPTY;

         for (j4.v.u var7 : this.P.values()) {
            l4 var8 = var7.E();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }

      private static final class u {
         int Q;
         int U;
         int Z;
         int M;
         int T;
         int t;
         int s;
         String f = "";
         int C = 16;
         int e = Integer.MAX_VALUE;
         int J = 16;
         int L;
         int d = Integer.MIN_VALUE;
         int k;

         u(int var1) {
            this.t = var1;
         }

         void w(int var1, int var2, int var3) {
            this.C = Math.min(this.C, var1);
            this.e = Math.min(this.e, var2);
            this.J = Math.min(this.J, var3);
            this.L = Math.max(this.L, var1 + 1);
            this.d = Math.max(this.d, var2 + 1);
            this.k = Math.max(this.k, var3 + 1);
         }

         l4 E() {
            AABB var1 = this.e == Integer.MAX_VALUE ? null : new AABB(this.C, this.e, this.J, this.L, this.d, this.k);
            return new l4(this.Q, this.U, this.Z, this.M, this.T, this.t, this.f, var1, this.s);
         }
      }
   }
}

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

public final class xe {
   private static final Set<Block> D = Set.of(Blocks.BEACON, Blocks.RESPAWN_ANCHOR, Blocks.ENCHANTING_TABLE, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.CONDUIT);
   private static final Set<Block> V = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER);
   private static final Set<Block> g = Set.of(
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
   private static final Set<Block> m = Set.of(
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

   public static boolean y(BlockState var0) {
      return (var0.is(Blocks.BEEHIVE) || var0.is(Blocks.BEE_NEST)) && (Integer)var0.getValue(BlockStateProperties.LEVEL_HONEY) == 5;
   }

   public static xe.v j(BlockState var0, int var1, boolean var2) {
      Block var3 = var0.getBlock();
      if (var3 instanceof ShulkerBoxBlock || D.contains(var3) || y(var0)) {
         return xe.v.Z;
      }

      if (var0.is(Blocks.ENDER_CHEST)) {
         return var2 ? xe.v.t : xe.v.Z;
      }

      if (V.contains(var3)) {
         return xe.v.t;
      }

      if (g.contains(var3)) {
         return xe.v.p;
      }

      if (!m.contains(var3) && !(var3 instanceof StainedGlassBlock) && !(var3 instanceof StainedGlassPaneBlock)) {
         if (var0.is(Blocks.DEEPSLATE) && var0.getValue(BlockStateProperties.AXIS) != Axis.Y) {
            return xe.v.Y;
         } else {
            return !var0.is(Blocks.COBBLED_DEEPSLATE) && (var1 < 16 || !var0.is(Blocks.DEEPSLATE)) ? xe.v.f : xe.v.Y;
         }
      } else {
         return xe.v.D;
      }
   }

   public static final class l {
      private final Map<Integer, xe.l.s> H = new HashMap<>();

      public void O() {
         this.H.clear();
      }

      public int S() {
         return this.H.size();
      }

      public void G(BlockState var1, int var2, boolean var3, boolean var4) {
         this.j(var1, 0, var2, 0, var3, var4);
      }

      public void j(BlockState var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         xe.v var7 = xe.j(var1, var3, var5);
         if (var7 != xe.v.f && (var7 != xe.v.Y || var6)) {
            xe.l.s var8 = this.H.computeIfAbsent(var3 >> 4, var1x -> new xe.l.s(var3));
            if (var1.is(Blocks.ENDER_CHEST) && !var5) {
               var8.n++;
            }

            if (var7 != xe.v.Y) {
               var8.N(var2, var3, var4);
            }

            switch (var7) {
               case t:
                  var8.k++;
                  break;
               case p:
                  var8.G++;
                  break;
               case D:
                  var8.P++;
                  break;
               case Y:
                  var8.j++;
                  break;
               case Z:
                  var8.M++;
                  if (var8.N.isEmpty()) {
                     var8.N = var1.getBlock().getName().getString();
                     var8.B = var3;
                  }
            }
         }
      }

      public ce t(String var1, int var2, int var3) {
         return this.O(var1, var2, var3, 1);
      }

      public ce O(String var1, int var2, int var3, int var4) {
         ce var5 = ce.EMPTY;

         for (xe.l.s var7 : this.H.values()) {
            ce var8 = var7.S();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }

      private static final class s {
         int k;
         int G;
         int P;
         int j;
         int M;
         int B;
         int n;
         String N = "";
         int H = 16;
         int l = Integer.MAX_VALUE;
         int p = 16;
         int J;
         int z = Integer.MIN_VALUE;
         int V;

         s(int var1) {
            this.B = var1;
         }

         void N(int var1, int var2, int var3) {
            this.H = Math.min(this.H, var1);
            this.l = Math.min(this.l, var2);
            this.p = Math.min(this.p, var3);
            this.J = Math.max(this.J, var1 + 1);
            this.z = Math.max(this.z, var2 + 1);
            this.V = Math.max(this.V, var3 + 1);
         }

         ce S() {
            AABB var1 = this.l == Integer.MAX_VALUE ? null : new AABB(this.H, this.l, this.p, this.J, this.z, this.V);
            return new ce(this.k, this.G, this.P, this.j, this.M, this.B, this.N, var1, this.n);
         }
      }
   }

   public enum v {
      f,
      t,
      p,
      D,
      Y,
      Z;
   }
}

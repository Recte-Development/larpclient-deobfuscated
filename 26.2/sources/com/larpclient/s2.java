package com.larpclient;

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
import net.minecraft.world.phys.AABB;

public final class s2 {
   private static final Set<Block> w = Set.of(Blocks.BEACON, Blocks.RESPAWN_ANCHOR, Blocks.ENCHANTING_TABLE, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.CONDUIT);
   private static final Set<Block> n = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER);
   private static final Set<Block> l = Set.of(
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
   private static final Set<Block> y = Set.of(
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

   public static boolean k(BlockState var0) {
      return (var0.is(Blocks.BEEHIVE) || var0.is(Blocks.BEE_NEST)) && (Integer)var0.getValue(BlockStateProperties.LEVEL_HONEY) == 5;
   }

   public static s2.p Q(BlockState var0, int var1, boolean var2) {
      Block var3 = var0.getBlock();
      if (var3 instanceof ShulkerBoxBlock || w.contains(var3) || k(var0)) {
         return s2.p.R;
      }

      if (var0.is(Blocks.ENDER_CHEST)) {
         return var2 ? s2.p.r : s2.p.R;
      }

      if (n.contains(var3)) {
         return s2.p.r;
      }

      if (l.contains(var3)) {
         return s2.p.v;
      }

      if (!y.contains(var3) && !(var3 instanceof StainedGlassBlock) && !(var3 instanceof StainedGlassPaneBlock)) {
         if (var0.is(Blocks.DEEPSLATE) && var0.getValue(BlockStateProperties.AXIS) != Axis.Y) {
            return s2.p.Q;
         } else {
            return !var0.is(Blocks.COBBLED_DEEPSLATE) && (var1 < 16 || !var0.is(Blocks.DEEPSLATE)) ? s2.p.g : s2.p.Q;
         }
      } else {
         return s2.p.d;
      }
   }

   public static final class j {
      private static final s2.p[] I = s2.p.values();
      private static final int i = 256;
      private final BlockState[] Z = new BlockState[256];
      private final byte[] C = new byte[256];
      private final byte[] U = new byte[256];
      private boolean y;

      public s2.p T(BlockState var1, int var2, boolean var3) {
         if (var3 != this.y) {
            this.b();
            this.y = var3;
         }

         int var4 = System.identityHashCode(var1) & 0xFF;
         if (this.Z[var4] != var1) {
            this.C[var4] = (byte)s2.Q(var1, 15, var3).ordinal();
            this.U[var4] = (byte)s2.Q(var1, 16, var3).ordinal();
            this.Z[var4] = var1;
         }

         return I[var2 >= 16 ? this.U[var4] : this.C[var4]];
      }

      public void b() {
         Arrays.fill(this.Z, null);
      }
   }

   public enum p {
      g,
      r,
      v,
      d,
      Q,
      R;
   }

   public static final class q {
      private final Map<Integer, s2.q.z> l = new HashMap<>();
      private final s2.j L = new s2.j();
      private s2.q.z H;
      private int k;

      public void M() {
         this.l.clear();
         this.H = null;
      }

      public int W() {
         return this.l.size();
      }

      public s2.p B(BlockState var1, int var2, boolean var3) {
         return this.L.T(var1, var2, var3);
      }

      public void D(BlockState var1, int var2, boolean var3, boolean var4) {
         this.f(var1, 0, var2, 0, var3, var4);
      }

      public void f(BlockState var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         this.p(this.L.T(var1, var3, var5), var1, var2, var3, var4, var5, var6);
      }

      public void p(s2.p var1, BlockState var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (var1 != s2.p.g && (var1 != s2.p.Q || var7)) {
            int var8 = var4 >> 4;
            s2.q.z var9 = this.H != null && this.k == var8 ? this.H : this.l.get(var8);
            if (var9 == null) {
               this.l.put(var8, var9 = new s2.q.z(var4));
            }

            this.H = var9;
            this.k = var8;
            if (var2.is(Blocks.ENDER_CHEST) && !var6) {
               var9.d++;
            }

            if (var1 != s2.p.Q) {
               var9.e(var3, var4, var5);
            }

            switch (var1) {
               case r:
                  var9.L++;
                  break;
               case v:
                  var9.Y++;
                  break;
               case d:
                  var9.c++;
                  break;
               case Q:
                  var9.Q++;
                  break;
               case R:
                  var9.l++;
                  if (var9.U.isEmpty()) {
                     var9.U = var2.getBlock().getName().getString();
                     var9.z = var4;
                  }
            }
         }
      }

      public tm R(String var1, int var2, int var3) {
         return this.A(var1, var2, var3, 1);
      }

      public tm A(String var1, int var2, int var3, int var4) {
         tm var5 = tm.EMPTY;

         for (s2.q.z var7 : this.l.values()) {
            tm var8 = var7.Z();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }

      private static final class z {
         int L;
         int Y;
         int c;
         int Q;
         int l;
         int z;
         int d;
         String U = "";
         int I = 16;
         int B = Integer.MAX_VALUE;
         int m = 16;
         int t;
         int r = Integer.MIN_VALUE;
         int e;

         z(int var1) {
            this.z = var1;
         }

         void e(int var1, int var2, int var3) {
            this.I = Math.min(this.I, var1);
            this.B = Math.min(this.B, var2);
            this.m = Math.min(this.m, var3);
            this.t = Math.max(this.t, var1 + 1);
            this.r = Math.max(this.r, var2 + 1);
            this.e = Math.max(this.e, var3 + 1);
         }

         tm Z() {
            AABB var1 = this.B == Integer.MAX_VALUE ? null : new AABB(this.I, this.B, this.m, this.t, this.r, this.e);
            return new tm(this.L, this.Y, this.c, this.Q, this.l, this.z, this.U, var1, this.d);
         }
      }
   }
}

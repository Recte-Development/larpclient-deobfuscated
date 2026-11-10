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

public final class nq {
   private static final Set<Block> A = Set.of(Blocks.BEACON, Blocks.RESPAWN_ANCHOR, Blocks.ENCHANTING_TABLE, Blocks.CRAFTER, Blocks.OBSERVER, Blocks.CONDUIT);
   private static final Set<Block> e = Set.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL, Blocks.HOPPER, Blocks.DISPENSER, Blocks.DROPPER);
   private static final Set<Block> V = Set.of(
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
   private static final Set<Block> M = Set.of(
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

   public static boolean C(BlockState var0) {
      return (var0.is(Blocks.BEEHIVE) || var0.is(Blocks.BEE_NEST)) && (Integer)var0.getValue(BlockStateProperties.LEVEL_HONEY) == 5;
   }

   public static nq.m S(BlockState var0, int var1, boolean var2) {
      Block var3 = var0.getBlock();
      if (var3 instanceof ShulkerBoxBlock || A.contains(var3) || C(var0)) {
         return nq.m.S;
      }

      if (var0.is(Blocks.ENDER_CHEST)) {
         return var2 ? nq.m.e : nq.m.S;
      }

      if (e.contains(var3)) {
         return nq.m.e;
      }

      if (V.contains(var3)) {
         return nq.m.E;
      }

      if (!M.contains(var3) && !(var3 instanceof StainedGlassBlock) && !(var3 instanceof StainedGlassPaneBlock)) {
         if (var0.is(Blocks.DEEPSLATE) && var0.getValue(BlockStateProperties.AXIS) != Axis.Y) {
            return nq.m.Y;
         } else {
            return !var0.is(Blocks.COBBLED_DEEPSLATE) && (var1 < 16 || !var0.is(Blocks.DEEPSLATE)) ? nq.m.x : nq.m.Y;
         }
      } else {
         return nq.m.P;
      }
   }

   public static final class k {
      private final Map<Integer, nq.k.b> F = new HashMap<>();
      private final nq.r W = new nq.r();
      private nq.k.b m;
      private int a;

      public void d() {
         this.F.clear();
         this.m = null;
      }

      public int S() {
         return this.F.size();
      }

      public nq.m v(BlockState var1, int var2, boolean var3) {
         return this.W.F(var1, var2, var3);
      }

      public void U(BlockState var1, int var2, boolean var3, boolean var4) {
         this.o(var1, 0, var2, 0, var3, var4);
      }

      public void o(BlockState var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         this.X(this.W.F(var1, var3, var5), var1, var2, var3, var4, var5, var6);
      }

      public void X(nq.m var1, BlockState var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (var1 != nq.m.x && (var1 != nq.m.Y || var7)) {
            int var8 = var4 >> 4;
            nq.k.b var9 = this.m != null && this.a == var8 ? this.m : this.F.get(var8);
            if (var9 == null) {
               this.F.put(var8, var9 = new nq.k.b(var4));
            }

            this.m = var9;
            this.a = var8;
            if (var2.is(Blocks.ENDER_CHEST) && !var6) {
               var9.x++;
            }

            if (var1 != nq.m.Y) {
               var9.p(var3, var4, var5);
            }

            switch (var1) {
               case e:
                  var9.Q++;
                  break;
               case E:
                  var9.D++;
                  break;
               case P:
                  var9.s++;
                  break;
               case Y:
                  var9.Y++;
                  break;
               case S:
                  var9.F++;
                  if (var9.u.isEmpty()) {
                     var9.u = var2.getBlock().getName().getString();
                     var9.H = var4;
                  }
            }
         }
      }

      public qq T(String var1, int var2, int var3) {
         return this.Z(var1, var2, var3, 1);
      }

      public qq Z(String var1, int var2, int var3, int var4) {
         qq var5 = qq.EMPTY;

         for (nq.k.b var7 : this.F.values()) {
            qq var8 = var7.M();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }

      private static final class b {
         int Q;
         int D;
         int s;
         int Y;
         int F;
         int H;
         int x;
         String u = "";
         int h = 16;
         int q = Integer.MAX_VALUE;
         int J = 16;
         int r;
         int S = Integer.MIN_VALUE;
         int X;

         b(int var1) {
            this.H = var1;
         }

         void p(int var1, int var2, int var3) {
            this.h = Math.min(this.h, var1);
            this.q = Math.min(this.q, var2);
            this.J = Math.min(this.J, var3);
            this.r = Math.max(this.r, var1 + 1);
            this.S = Math.max(this.S, var2 + 1);
            this.X = Math.max(this.X, var3 + 1);
         }

         qq M() {
            AABB var1 = this.q == Integer.MAX_VALUE ? null : new AABB(this.h, this.q, this.J, this.r, this.S, this.X);
            return new qq(this.Q, this.D, this.s, this.Y, this.F, this.H, this.u, var1, this.x);
         }
      }
   }

   public enum m {
      x,
      e,
      E,
      P,
      Y,
      S;
   }

   public static final class r {
      private static final nq.m[] F = nq.m.values();
      private static final int r = 256;
      private final BlockState[] B = new BlockState[256];
      private final byte[] a = new byte[256];
      private final byte[] e = new byte[256];
      private boolean U;

      public nq.m F(BlockState var1, int var2, boolean var3) {
         if (var3 != this.U) {
            this.w();
            this.U = var3;
         }

         int var4 = System.identityHashCode(var1) & 0xFF;
         if (this.B[var4] != var1) {
            this.a[var4] = (byte)nq.S(var1, 15, var3).ordinal();
            this.e[var4] = (byte)nq.S(var1, 16, var3).ordinal();
            this.B[var4] = var1;
         }

         return F[var2 >= 16 ? this.e[var4] : this.a[var4]];
      }

      public void w() {
         Arrays.fill(this.B, null);
      }
   }
}

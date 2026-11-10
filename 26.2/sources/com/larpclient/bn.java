package com.larpclient;

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

final class bn {
   final BlockPos[] G = new BlockPos[2];
   private final ItemStack[][] k = new ItemStack[2][10];
   private final int[][] p = new int[2][9];
   private final int[] S = new int[]{0, 0};
   private final int[] Z = new int[]{0, 0};
   private final boolean[] F = new boolean[]{false, false};
   private final boolean[] c = new boolean[]{false, false};
   private final int[] z = new int[]{-1, -1};
   private final int[] v = new int[]{-1, -1};
   private final Set<UUID> l = new HashSet<>();
   private final Set<UUID> R = new HashSet<>();
   private final Set<Integer> r = new HashSet<>();
   private final uk h = new uk();
   private final List<Map<Integer, Integer>> U = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu D;
   private int s = -1;
   private final Map<Integer, Integer> q = new HashMap<>();
   private final Map<Integer, Integer> o = new HashMap<>();
   boolean E = (boolean)1;
   private boolean P = (boolean)1;
   private boolean I;
   private final Set<ItemStack> Y = Collections.newSetFromMap(new WeakHashMap<>());
   private ClientLevel K;

   bn() {
      for (ItemStack[] var4 : this.k) {
         Arrays.fill(var4, ItemStack.EMPTY);
      }
   }

   void k(Minecraft var1, g5 var2, BlockPos var3, BlockPos var4) {
      int var5 = this.K == var1.level && this.P == var2.Z && Objects.equals(this.G[0], var3) && Objects.equals(this.G[1], var4) ? 0 : 1;
      this.P = var2.Z;
      this.G[0] = var3;
      this.G[1] = var4;
      this.K = var1.level;
      this.E = (boolean)(var2.k() == g5.q.z ? 1 : 0);

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 1; var7 <= 9; var7++) {
            this.k[var6][var7] = var2.Y(var6 == 0, var7 - 1);
            if (this.p[var6][var7 - 1] == 0) {
               this.p[var6][var7 - 1] = var7;
            }
         }
      }

      if (var5 != 0 || !this.I) {
         this.h(var1);
         this.I = (boolean)1;
      }
   }

   boolean q() {
      return this.S[0] > 0 || this.S[1] > 0;
   }

   void y(int var1, int var2, int var3) {
      if (this.S[var1] == 0 && var2 >= 0 && var2 < 9 && var3 >= 1 && var3 <= 9) {
         this.p[var1][var2] = var3;
      }
   }

   boolean e(int var1) {
      return this.G[var1] != null;
   }

   boolean z() {
      return this.e(0) && this.e(1);
   }

   private int g(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      String var2 = var1.getHoverName().getString();
      int var3 = zv.K(var2);
      if (var3 < 1) {
         int var8 = -1;

         for (int var9 = 0; var9 < 2; var9++) {
            for (int var6 = 1; var6 <= 9; var6++) {
               ItemStack var7 = this.k[var9][var6];
               if (this.e(var9) && var7 != null && !var7.isEmpty() && var1.is(var7.getItem()) && var2.equals(var7.getHoverName().getString())) {
                  if (var8 >= 0) {
                     return -1;
                  }

                  var8 = var9 * 10 + var6;
               }
            }
         }

         return var8;
      } else {
         int var4 = zv.X(var2) ? 0 : 1;
         ItemStack var5 = this.k[var4][var3];
         return this.e(var4) && var5 != null && !var5.isEmpty() && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> u(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.D) {
         this.t(var2, this.s);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.U) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.g(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu == null ? ItemStack.EMPTY : var1.player.containerMenu.getCarried();
      int var16 = this.g(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.g(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1 != null && var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.R.contains(var7.getUUID())) {
               int var10 = this.g(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void t(DispenserMenu var1, int var2) {
      Map var3 = this.U.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.g(var5);
         if (k3.z(var2, var6 < 0 ? -1 : var6 / 10, this.K(var2, var4, var5))) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void M(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.D == var1) {
         this.D = var1;
         this.s = var2;
         this.t(var1, var2);
      }
   }

   private void Z(Minecraft var1) {
      if (var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.l.contains(var4.getUUID())) {
               int var5 = this.g(var4.getItem());
               if (var5 >= 0) {
                  this.l.add(var4.getUUID());
                  BlockPos var6 = this.G[var5 / 10];
                  if (var6 != null) {
                     BlockState var7 = var1.level.getBlockState(var6);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var6).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.R.add(var4.getUUID());
                           this.r.add(var5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack p(Minecraft var1, ItemEntity var2) {
      this.L(var1);
      return this.R.contains(var2.getUUID()) ? this.Y(var2.getItem()) : var2.getItem();
   }

   public void h(Minecraft var1) {
      Arrays.fill(this.S, 0);
      Arrays.fill(this.Z, 0);
      Arrays.fill(this.c, false);
      this.h.x();

      for (Map var3 : this.U) {
         var3.clear();
      }

      this.D = null;
      this.s = -1;
      Arrays.fill(this.F, false);
      Arrays.fill(this.z, -1);
      Arrays.fill(this.v, -1);
      this.R.clear();
      this.l.clear();
      this.r.clear();
      if (var1 != null && var1.level != null) {
         for (Entity var5 : var1.level.entitiesForRendering()) {
            this.l.add(var5.getUUID());
         }
      }

      this.q.clear();
      if (var1 != null && var1.player != null) {
         this.q.putAll(this.u(var1));
      }

      this.o.clear();
      this.o.putAll(this.q);
   }

   public void L(Minecraft var1) {
      if (this.z() && var1 != null && var1.player != null && var1.level != null) {
         this.Z(var1);
         this.C(var1, this.u(var1), var1.level.getGameTime());
      }
   }

   void C(Minecraft var1, Map<Integer, Integer> var2, long var3) {
      if (this.h
         .l(
            this.S[0] > 0 && this.S[1] > 0,
            var2.getOrDefault(this.S[0], 0) <= this.o.getOrDefault(this.S[0], 0)
               && var2.getOrDefault(10 + this.S[1], 0) <= this.o.getOrDefault(10 + this.S[1], 0)
               && (!this.F[0] || var2.getOrDefault(this.Z[0], 0) <= this.o.getOrDefault(this.Z[0], 0))
               && (!this.F[1] || var2.getOrDefault(10 + this.Z[1], 0) <= this.o.getOrDefault(10 + this.Z[1], 0)),
            var3
         )) {
         this.h(var1);
      } else {
         for (Entry var6 : var2.entrySet()) {
            if (this.r.contains(var6.getKey()) && (Integer)var6.getValue() > this.q.getOrDefault(var6.getKey(), 0)) {
               int var7 = (Integer)var6.getKey() / 10;
               int var8 = (Integer)var6.getKey() % 10;
               if (this.S[var7] == 0) {
                  this.S[var7] = var8;
               }
            }
         }

         if (!this.P) {
            System.arraycopy(this.S, 0, this.Z, 0, 2);
         }

         if (this.P && this.Z[0] == 0 && (this.S[0] > 0 || this.S[1] > 0)) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();
            zv.z var12 = zv.C(this.S[0] > 0 ? this.S[0] : var10.nextInt(1, 10), this.S[1] > 0 ? this.S[1] : var10.nextInt(1, 10), this.E, var10.nextInt());
            ArrayList var13 = new ArrayList();

            for (int var14 = 1; var14 <= 9; var14++) {
               for (int var9 = 1; var9 <= 9; var9++) {
                  if ((this.E ? var14 > var9 : var9 > var14) && !this.k[0][var14].isEmpty() && !this.k[1][var9].isEmpty()) {
                     var13.add(new zv.z(var14, var9));
                  }
               }
            }

            if (this.k[0][var12.host()].isEmpty() || this.k[1][var12.viewer()].isEmpty()) {
               if (var13.isEmpty()) {
                  return;
               }

               List var15 = var13.stream().filter(var1x -> var1x.host() == this.S[0] || var1x.viewer() == this.S[1]).toList();
               List var16 = var15.isEmpty() ? var13 : var15;
               var12 = (zv.z)var16.get(var10.nextInt(var16.size()));
            }

            this.Z[0] = var12.host();
            this.Z[1] = var12.viewer();
         }

         for (int var11 = 0; var11 < 2; var11++) {
            if (!this.c[var11] && this.S[var11] > 0 && this.Z[var11] > 0) {
               this.c[var11] = (boolean)1;
               te.Z("Paper", (var11 == 0 ? "Host: " : "Viewer: ") + te.I(Integer.toString(this.S[var11]), Integer.toString(this.Z[var11]), this.P, true));
            }
         }

         this.q.clear();
         this.q.putAll(var2);
      }
   }

   public ItemStack Y(ItemStack var1) {
      if (this.Y.contains(var1)) {
         return var1;
      } else {
         int var2 = this.g(var1);
         if (var2 < 0) {
            return var1;
         } else {
            int var3 = var2 / 10;
            int var4 = var2 % 10;
            if (this.F[var3] && var4 == this.Z[var3]) {
               return this.i(this.k[var3][this.S[var3]], var1.getCount());
            } else {
               return this.S[var3] == var4 && this.Z[var3] != 0 && this.Z[var3] != var4 ? this.i(this.k[var3][this.Z[var3]], var1.getCount()) : var1;
            }
         }
      }
   }

   private ItemStack i(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.Y.add(var3);
      return var3;
   }

   public ItemStack L(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.T(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.i(var5, var5.getCount());
   }

   private ItemStack T(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      ItemStack var6 = this.s(var5, var3, false);
      int var7 = this.S[var3];
      int var8 = this.Z[var3];
      if (var7 != 0 && var8 != 0 && var7 != var8) {
         this.D(var1, var2, var3);
         if (this.F[var3]) {
            int var18 = k3.f(var4, this.z[var3], this.v[var3], true);
            ItemStack var19 = var2.getSlot(var18).getItem();
            return this.s(var19, var3, var18 != var4 || this.K(var3, var18, var19));
         }

         int var9 = this.u(var1).getOrDefault(var3 * 10 + var7, 0) > this.o.getOrDefault(var3 * 10 + var7, 0) ? 1 : 0;
         int var10 = -1;
         int var11 = -1;
         int var12 = -1;
         int var13 = 0;
         int[] var14 = new int[9];

         for (int var15 = 0; var15 < 9; var15++) {
            ItemStack var16 = var2.getSlot(var15).getItem();
            var14[var15] = var16.getCount();
            int var17 = this.g(var16);
            if (var17 == var3 * 10 + var7) {
               var10 = var15;
            }

            if (var17 == var3 * 10 + var8) {
               var11 = var15;
            }

            if (var16.isEmpty()) {
               var12 = var15;
               var13++;
            } else if (var17 / 10 == var3 && var17 > 0 && this.p[var3][var15] == 0) {
               this.p[var3][var15] = var17 % 10;
            }
         }

         if (var10 < 0) {
            for (int var20 = 0; var20 < 9; var20++) {
               if (this.p[var3][var20] == var7 && var2.getSlot(var20).getItem().isEmpty()) {
                  var10 = var20;
                  break;
               }
            }

            if (var10 < 0 && var13 == 1) {
               var10 = var12;
            }
         }

         if (var10 >= 0 && var14[var10] >= this.k[var3][var7].getMaxStackSize()) {
            return var6;
         } else {
            int[] var21 = mi.N(var14, var10, var11, (boolean)var9);
            if (var21[var4] == var14[var4]) {
               return var6;
            } else {
               return var21[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.k[var3][var7] : var5).copyWithCount(var21[var4]);
            }
         }
      } else {
         return var6;
      }
   }

   private boolean K(int var1, int var2, ItemStack var3) {
      if (!this.F[var1]) {
         return false;
      }

      int var4 = this.g(var3);
      return var4 == var1 * 10 + this.S[var1] && var2 != this.z[var1] || var4 == var1 * 10 + this.Z[var1] && var2 != this.v[var1];
   }

   private ItemStack s(ItemStack var1, int var2, boolean var3) {
      int var4 = this.g(var1);
      if (var4 >= 0 && this.F[var4 / 10]) {
         return this.Y(var1);
      } else {
         return k3.z(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.Y(var1) : var1;
      }
   }

   private void D(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.F[var3] && this.S[var3] != 0 && this.Z[var3] != 0 && this.S[var3] != this.Z[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.g(var2.getSlot(var8).getItem());
            if (this.p[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.p[var3][var8] = var9 % 10;
            }

            if (this.p[var3][var8] == this.S[var3] && var2.getSlot(var8).getItem().isEmpty()) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.Z[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.p[var3][var4] = this.S[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.g(var2.getSlot(var4).getItem()) != var3 * 10 + this.S[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.u(var1).getOrDefault(var3 * 10 + this.S[var3], 0) > this.o.getOrDefault(var3 * 10 + this.S[var3], 0)) {
               this.z[var3] = var4;
               this.v[var3] = var5;
               this.F[var3] = (boolean)1;
            }
         }
      }
   }

   public int E(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      this.D(var1, var2, var3);
      return k3.f(var4, this.z[var3], this.v[var3], this.F[var3]);
   }
}

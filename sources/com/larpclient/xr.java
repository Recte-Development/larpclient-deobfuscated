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

final class xr {
   final BlockPos[] F = new BlockPos[2];
   private final ItemStack[][] Y = new ItemStack[2][10];
   private final int[][] b = new int[2][9];
   private final int[] v = new int[]{0, 0};
   private final int[] P = new int[]{0, 0};
   private final boolean[] y = new boolean[]{false, false};
   private final boolean[] s = new boolean[]{false, false};
   private final int[] q = new int[]{-1, -1};
   private final int[] w = new int[]{-1, -1};
   private final Set<UUID> I = new HashSet<>();
   private final Set<UUID> W = new HashSet<>();
   private final Set<Integer> Q = new HashSet<>();
   private final h6 Z = new h6();
   private final List<Map<Integer, Integer>> J = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu G;
   private int U = -1;
   private final Map<Integer, Integer> e = new HashMap<>();
   private final Map<Integer, Integer> g = new HashMap<>();
   boolean j = (boolean)1;
   private boolean h = (boolean)1;
   private boolean R;
   private final Set<ItemStack> t = Collections.newSetFromMap(new WeakHashMap<>());
   private ClientLevel V;

   xr() {
      for (ItemStack[] var4 : this.Y) {
         Arrays.fill(var4, ItemStack.EMPTY);
      }
   }

   void w(Minecraft var1, mi var2, BlockPos var3, BlockPos var4) {
      int var5 = this.V == var1.level && this.h == var2.p && Objects.equals(this.F[0], var3) && Objects.equals(this.F[1], var4) ? 0 : 1;
      this.h = var2.p;
      this.F[0] = var3;
      this.F[1] = var4;
      this.V = var1.level;
      this.j = (boolean)(var2.p() == mi.c.o ? 1 : 0);

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 1; var7 <= 9; var7++) {
            this.Y[var6][var7] = var2.m(var6 == 0, var7 - 1);
            if (this.b[var6][var7 - 1] == 0) {
               this.b[var6][var7 - 1] = var7;
            }
         }
      }

      if (var5 != 0 || !this.R) {
         this.n(var1);
         this.R = (boolean)1;
      }
   }

   boolean I() {
      return this.v[0] > 0 || this.v[1] > 0;
   }

   void o(int var1, int var2, int var3) {
      if (this.v[var1] == 0 && var2 >= 0 && var2 < 9 && var3 >= 1 && var3 <= 9) {
         this.b[var1][var2] = var3;
      }
   }

   boolean t(int var1) {
      return this.F[var1] != null;
   }

   boolean P() {
      return this.t(0) && this.t(1);
   }

   private int r(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      String var2 = var1.getHoverName().getString();
      int var3 = ol.N(var2);
      if (var3 < 1) {
         int var8 = -1;

         for (int var9 = 0; var9 < 2; var9++) {
            for (int var6 = 1; var6 <= 9; var6++) {
               ItemStack var7 = this.Y[var9][var6];
               if (this.t(var9) && var7 != null && !var7.isEmpty() && var1.is(var7.getItem()) && var2.equals(var7.getHoverName().getString())) {
                  if (var8 >= 0) {
                     return -1;
                  }

                  var8 = var9 * 10 + var6;
               }
            }
         }

         return var8;
      } else {
         int var4 = ol.U(var2) ? 0 : 1;
         ItemStack var5 = this.Y[var4][var3];
         return this.t(var4) && var5 != null && !var5.isEmpty() && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> T(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.G) {
         this.C(var2, this.U);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.J) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.r(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu == null ? ItemStack.EMPTY : var1.player.containerMenu.getCarried();
      int var16 = this.r(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.r(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1 != null && var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.W.contains(var7.getUUID())) {
               int var10 = this.r(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void C(DispenserMenu var1, int var2) {
      Map var3 = this.J.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.r(var5);
         if (tv.h(var2, var6 < 0 ? -1 : var6 / 10, this.j(var2, var4, var5))) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void u(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.G == var1) {
         this.G = var1;
         this.U = var2;
         this.C(var1, var2);
      }
   }

   private void S(Minecraft var1) {
      if (var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.I.contains(var4.getUUID())) {
               int var5 = this.r(var4.getItem());
               if (var5 >= 0) {
                  this.I.add(var4.getUUID());
                  BlockPos var6 = this.F[var5 / 10];
                  if (var6 != null) {
                     BlockState var7 = var1.level.getBlockState(var6);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var6).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.W.add(var4.getUUID());
                           this.Q.add(var5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack L(Minecraft var1, ItemEntity var2) {
      this.e(var1);
      return this.W.contains(var2.getUUID()) ? this.t(var2.getItem()) : var2.getItem();
   }

   public void n(Minecraft var1) {
      Arrays.fill(this.v, 0);
      Arrays.fill(this.P, 0);
      Arrays.fill(this.s, false);
      this.Z.Q();

      for (Map var3 : this.J) {
         var3.clear();
      }

      this.G = null;
      this.U = -1;
      Arrays.fill(this.y, false);
      Arrays.fill(this.q, -1);
      Arrays.fill(this.w, -1);
      this.W.clear();
      this.I.clear();
      this.Q.clear();
      if (var1 != null && var1.level != null) {
         for (Entity var5 : var1.level.entitiesForRendering()) {
            this.I.add(var5.getUUID());
         }
      }

      this.e.clear();
      if (var1 != null && var1.player != null) {
         this.e.putAll(this.T(var1));
      }

      this.g.clear();
      this.g.putAll(this.e);
   }

   public void e(Minecraft var1) {
      if (this.P() && var1 != null && var1.player != null && var1.level != null) {
         this.S(var1);
         this.a(var1, this.T(var1), var1.level.getGameTime());
      }
   }

   void a(Minecraft var1, Map<Integer, Integer> var2, long var3) {
      if (this.Z
         .x(
            this.v[0] > 0 && this.v[1] > 0,
            var2.getOrDefault(this.v[0], 0) <= this.g.getOrDefault(this.v[0], 0)
               && var2.getOrDefault(10 + this.v[1], 0) <= this.g.getOrDefault(10 + this.v[1], 0)
               && (!this.y[0] || var2.getOrDefault(this.P[0], 0) <= this.g.getOrDefault(this.P[0], 0))
               && (!this.y[1] || var2.getOrDefault(10 + this.P[1], 0) <= this.g.getOrDefault(10 + this.P[1], 0)),
            var3
         )) {
         this.n(var1);
      } else {
         for (Entry var6 : var2.entrySet()) {
            if (this.Q.contains(var6.getKey()) && (Integer)var6.getValue() > this.e.getOrDefault(var6.getKey(), 0)) {
               int var7 = (Integer)var6.getKey() / 10;
               int var8 = (Integer)var6.getKey() % 10;
               if (this.v[var7] == 0) {
                  this.v[var7] = var8;
               }
            }
         }

         if (!this.h) {
            System.arraycopy(this.v, 0, this.P, 0, 2);
         }

         if (this.h && this.P[0] == 0 && (this.v[0] > 0 || this.v[1] > 0)) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();
            ol.e var12 = ol.n(this.v[0] > 0 ? this.v[0] : var10.nextInt(1, 10), this.v[1] > 0 ? this.v[1] : var10.nextInt(1, 10), this.j, var10.nextInt());
            ArrayList var13 = new ArrayList();

            for (int var14 = 1; var14 <= 9; var14++) {
               for (int var9 = 1; var9 <= 9; var9++) {
                  if ((this.j ? var14 > var9 : var9 > var14) && !this.Y[0][var14].isEmpty() && !this.Y[1][var9].isEmpty()) {
                     var13.add(new ol.e(var14, var9));
                  }
               }
            }

            if (this.Y[0][var12.host()].isEmpty() || this.Y[1][var12.viewer()].isEmpty()) {
               if (var13.isEmpty()) {
                  return;
               }

               List var15 = var13.stream().filter(var1x -> var1x.host() == this.v[0] || var1x.viewer() == this.v[1]).toList();
               List var16 = var15.isEmpty() ? var13 : var15;
               var12 = (ol.e)var16.get(var10.nextInt(var16.size()));
            }

            this.P[0] = var12.host();
            this.P[1] = var12.viewer();
         }

         for (int var11 = 0; var11 < 2; var11++) {
            if (!this.s[var11] && this.v[var11] > 0 && this.P[var11] > 0) {
               this.s[var11] = (boolean)1;
               xa.q("Paper", (var11 == 0 ? "Host: " : "Viewer: ") + xa.U(Integer.toString(this.v[var11]), Integer.toString(this.P[var11]), this.h, true));
            }
         }

         this.e.clear();
         this.e.putAll(var2);
      }
   }

   public ItemStack t(ItemStack var1) {
      if (this.t.contains(var1)) {
         return var1;
      } else {
         int var2 = this.r(var1);
         if (var2 < 0) {
            return var1;
         } else {
            int var3 = var2 / 10;
            int var4 = var2 % 10;
            if (this.y[var3] && var4 == this.P[var3]) {
               return this.P(this.Y[var3][this.v[var3]], var1.getCount());
            } else {
               return this.v[var3] == var4 && this.P[var3] != 0 && this.P[var3] != var4 ? this.P(this.Y[var3][this.P[var3]], var1.getCount()) : var1;
            }
         }
      }
   }

   private ItemStack P(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.t.add(var3);
      return var3;
   }

   public ItemStack o(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.R(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.P(var5, var5.getCount());
   }

   private ItemStack R(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      ItemStack var6 = this.e(var5, var3, false);
      int var7 = this.v[var3];
      int var8 = this.P[var3];
      if (var7 != 0 && var8 != 0 && var7 != var8) {
         this.f(var1, var2, var3);
         if (this.y[var3]) {
            int var18 = tv.V(var4, this.q[var3], this.w[var3], true);
            ItemStack var19 = var2.getSlot(var18).getItem();
            return this.e(var19, var3, var18 != var4 || this.j(var3, var18, var19));
         }

         int var9 = this.T(var1).getOrDefault(var3 * 10 + var7, 0) > this.g.getOrDefault(var3 * 10 + var7, 0) ? 1 : 0;
         int var10 = -1;
         int var11 = -1;
         int var12 = -1;
         int var13 = 0;
         int[] var14 = new int[9];

         for (int var15 = 0; var15 < 9; var15++) {
            ItemStack var16 = var2.getSlot(var15).getItem();
            var14[var15] = var16.getCount();
            int var17 = this.r(var16);
            if (var17 == var3 * 10 + var7) {
               var10 = var15;
            }

            if (var17 == var3 * 10 + var8) {
               var11 = var15;
            }

            if (var16.isEmpty()) {
               var12 = var15;
               var13++;
            } else if (var17 / 10 == var3 && var17 > 0 && this.b[var3][var15] == 0) {
               this.b[var3][var15] = var17 % 10;
            }
         }

         if (var10 < 0) {
            for (int var20 = 0; var20 < 9; var20++) {
               if (this.b[var3][var20] == var7 && var2.getSlot(var20).getItem().isEmpty()) {
                  var10 = var20;
                  break;
               }
            }

            if (var10 < 0 && var13 == 1) {
               var10 = var12;
            }
         }

         if (var10 >= 0 && var14[var10] >= this.Y[var3][var7].getMaxStackSize()) {
            return var6;
         } else {
            int[] var21 = kn.q(var14, var10, var11, (boolean)var9);
            if (var21[var4] == var14[var4]) {
               return var6;
            } else {
               return var21[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.Y[var3][var7] : var5).copyWithCount(var21[var4]);
            }
         }
      } else {
         return var6;
      }
   }

   private boolean j(int var1, int var2, ItemStack var3) {
      if (!this.y[var1]) {
         return false;
      }

      int var4 = this.r(var3);
      return var4 == var1 * 10 + this.v[var1] && var2 != this.q[var1] || var4 == var1 * 10 + this.P[var1] && var2 != this.w[var1];
   }

   private ItemStack e(ItemStack var1, int var2, boolean var3) {
      int var4 = this.r(var1);
      if (var4 >= 0 && this.y[var4 / 10]) {
         return this.t(var1);
      } else {
         return tv.h(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.t(var1) : var1;
      }
   }

   private void f(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.y[var3] && this.v[var3] != 0 && this.P[var3] != 0 && this.v[var3] != this.P[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.r(var2.getSlot(var8).getItem());
            if (this.b[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.b[var3][var8] = var9 % 10;
            }

            if (this.b[var3][var8] == this.v[var3] && var2.getSlot(var8).getItem().isEmpty()) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.P[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.b[var3][var4] = this.v[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.r(var2.getSlot(var4).getItem()) != var3 * 10 + this.v[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.T(var1).getOrDefault(var3 * 10 + this.v[var3], 0) > this.g.getOrDefault(var3 * 10 + this.v[var3], 0)) {
               this.q[var3] = var4;
               this.w[var3] = var5;
               this.y[var3] = (boolean)1;
            }
         }
      }
   }

   public int P(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      this.f(var1, var2, var3);
      return tv.V(var4, this.q[var3], this.w[var3], this.y[var3]);
   }
}

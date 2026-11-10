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

final class j7 {
   final BlockPos[] w = new BlockPos[2];
   private final ItemStack[][] r = new ItemStack[2][10];
   private final int[][] q = new int[2][9];
   private final int[] W = new int[]{0, 0};
   private final int[] I = new int[]{0, 0};
   private final boolean[] s = new boolean[]{false, false};
   private final boolean[] p = new boolean[]{false, false};
   private final int[] H = new int[]{-1, -1};
   private final int[] l = new int[]{-1, -1};
   private final Set<UUID> O = new HashSet<>();
   private final Set<UUID> V = new HashSet<>();
   private final Set<Integer> G = new HashSet<>();
   private final pg J = new pg();
   private final List<Map<Integer, Integer>> B = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu v;
   private int P = -1;
   private final Map<Integer, Integer> C = new HashMap<>();
   private final Map<Integer, Integer> D = new HashMap<>();
   boolean b = (boolean)1;
   private boolean o = (boolean)1;
   private boolean k;
   private final Set<ItemStack> e = Collections.newSetFromMap(new WeakHashMap<>());
   private ClientLevel f;

   j7() {
      for (ItemStack[] var4 : this.r) {
         Arrays.fill(var4, ItemStack.EMPTY);
      }
   }

   void e(Minecraft var1, qo var2, BlockPos var3, BlockPos var4) {
      int var5 = this.f == var1.level && this.o == var2.u && Objects.equals(this.w[0], var3) && Objects.equals(this.w[1], var4) ? 0 : 1;
      this.o = var2.u;
      this.w[0] = var3;
      this.w[1] = var4;
      this.f = var1.level;
      this.b = (boolean)(var2.l() == qo.s.z ? 1 : 0);

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 1; var7 <= 9; var7++) {
            this.r[var6][var7] = var2.h(var6 == 0, var7 - 1);
            if (this.q[var6][var7 - 1] == 0) {
               this.q[var6][var7 - 1] = var7;
            }
         }
      }

      if (var5 != 0 || !this.k) {
         this.Z(var1);
         this.k = (boolean)1;
      }
   }

   boolean u() {
      return this.W[0] > 0 || this.W[1] > 0;
   }

   void C(int var1, int var2, int var3) {
      if (this.W[var1] == 0 && var2 >= 0 && var2 < 9 && var3 >= 1 && var3 <= 9) {
         this.q[var1][var2] = var3;
      }
   }

   boolean k(int var1) {
      return this.w[var1] != null;
   }

   boolean Z() {
      return this.k(0) && this.k(1);
   }

   private int F(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      String var2 = var1.getHoverName().getString();
      int var3 = j8.i(var2);
      if (var3 < 1) {
         int var8 = -1;

         for (int var9 = 0; var9 < 2; var9++) {
            for (int var6 = 1; var6 <= 9; var6++) {
               ItemStack var7 = this.r[var9][var6];
               if (this.k(var9) && var7 != null && !var7.isEmpty() && var1.is(var7.getItem()) && var2.equals(var7.getHoverName().getString())) {
                  if (var8 >= 0) {
                     return -1;
                  }

                  var8 = var9 * 10 + var6;
               }
            }
         }

         return var8;
      } else {
         int var4 = j8.P(var2) ? 0 : 1;
         ItemStack var5 = this.r[var4][var3];
         return this.k(var4) && var5 != null && !var5.isEmpty() && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> A(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.v) {
         this.I(var2, this.P);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.B) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.F(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu == null ? ItemStack.EMPTY : var1.player.containerMenu.getCarried();
      int var16 = this.F(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.F(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1 != null && var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.V.contains(var7.getUUID())) {
               int var10 = this.F(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void I(DispenserMenu var1, int var2) {
      Map var3 = this.B.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.F(var5);
         if (qz.K(var2, var6 < 0 ? -1 : var6 / 10, this.r(var2, var4, var5))) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void A(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.v == var1) {
         this.v = var1;
         this.P = var2;
         this.I(var1, var2);
      }
   }

   private void e(Minecraft var1) {
      if (var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.O.contains(var4.getUUID())) {
               int var5 = this.F(var4.getItem());
               if (var5 >= 0) {
                  this.O.add(var4.getUUID());
                  BlockPos var6 = this.w[var5 / 10];
                  if (var6 != null) {
                     BlockState var7 = var1.level.getBlockState(var6);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var6).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.V.add(var4.getUUID());
                           this.G.add(var5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack I(Minecraft var1, ItemEntity var2) {
      this.f(var1);
      return this.V.contains(var2.getUUID()) ? this.H(var2.getItem()) : var2.getItem();
   }

   public void Z(Minecraft var1) {
      Arrays.fill(this.W, 0);
      Arrays.fill(this.I, 0);
      Arrays.fill(this.p, false);
      this.J.s();

      for (Map var3 : this.B) {
         var3.clear();
      }

      this.v = null;
      this.P = -1;
      Arrays.fill(this.s, false);
      Arrays.fill(this.H, -1);
      Arrays.fill(this.l, -1);
      this.V.clear();
      this.O.clear();
      this.G.clear();
      if (var1 != null && var1.level != null) {
         for (Entity var5 : var1.level.entitiesForRendering()) {
            this.O.add(var5.getUUID());
         }
      }

      this.C.clear();
      if (var1 != null && var1.player != null) {
         this.C.putAll(this.A(var1));
      }

      this.D.clear();
      this.D.putAll(this.C);
   }

   public void f(Minecraft var1) {
      if (this.Z() && var1 != null && var1.player != null && var1.level != null) {
         this.e(var1);
         this.p(var1, this.A(var1), var1.level.getGameTime());
      }
   }

   void p(Minecraft var1, Map<Integer, Integer> var2, long var3) {
      if (this.J
         .z(
            this.W[0] > 0 && this.W[1] > 0,
            var2.getOrDefault(this.W[0], 0) <= this.D.getOrDefault(this.W[0], 0)
               && var2.getOrDefault(10 + this.W[1], 0) <= this.D.getOrDefault(10 + this.W[1], 0)
               && (!this.s[0] || var2.getOrDefault(this.I[0], 0) <= this.D.getOrDefault(this.I[0], 0))
               && (!this.s[1] || var2.getOrDefault(10 + this.I[1], 0) <= this.D.getOrDefault(10 + this.I[1], 0)),
            var3
         )) {
         this.Z(var1);
      } else {
         for (Entry var6 : var2.entrySet()) {
            if (this.G.contains(var6.getKey()) && (Integer)var6.getValue() > this.C.getOrDefault(var6.getKey(), 0)) {
               int var7 = (Integer)var6.getKey() / 10;
               int var8 = (Integer)var6.getKey() % 10;
               if (this.W[var7] == 0) {
                  this.W[var7] = var8;
               }
            }
         }

         if (!this.o) {
            System.arraycopy(this.W, 0, this.I, 0, 2);
         }

         if (this.o && this.I[0] == 0 && (this.W[0] > 0 || this.W[1] > 0)) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();
            j8.u var12 = j8.x(this.W[0] > 0 ? this.W[0] : var10.nextInt(1, 10), this.W[1] > 0 ? this.W[1] : var10.nextInt(1, 10), this.b, var10.nextInt());
            ArrayList var13 = new ArrayList();

            for (int var14 = 1; var14 <= 9; var14++) {
               for (int var9 = 1; var9 <= 9; var9++) {
                  if ((this.b ? var14 > var9 : var9 > var14) && !this.r[0][var14].isEmpty() && !this.r[1][var9].isEmpty()) {
                     var13.add(new j8.u(var14, var9));
                  }
               }
            }

            if (this.r[0][var12.host()].isEmpty() || this.r[1][var12.viewer()].isEmpty()) {
               if (var13.isEmpty()) {
                  return;
               }

               List var15 = var13.stream().filter(var1x -> var1x.host() == this.W[0] || var1x.viewer() == this.W[1]).toList();
               List var16 = var15.isEmpty() ? var13 : var15;
               var12 = (j8.u)var16.get(var10.nextInt(var16.size()));
            }

            this.I[0] = var12.host();
            this.I[1] = var12.viewer();
         }

         for (int var11 = 0; var11 < 2; var11++) {
            if (!this.p[var11] && this.W[var11] > 0 && this.I[var11] > 0) {
               this.p[var11] = (boolean)1;
               bn.j("Paper", (var11 == 0 ? "Host: " : "Viewer: ") + bn.W(Integer.toString(this.W[var11]), Integer.toString(this.I[var11]), this.o, true));
            }
         }

         this.C.clear();
         this.C.putAll(var2);
      }
   }

   public ItemStack H(ItemStack var1) {
      if (this.e.contains(var1)) {
         return var1;
      } else {
         int var2 = this.F(var1);
         if (var2 < 0) {
            return var1;
         } else {
            int var3 = var2 / 10;
            int var4 = var2 % 10;
            if (this.s[var3] && var4 == this.I[var3]) {
               return this.Y(this.r[var3][this.W[var3]], var1.getCount());
            } else {
               return this.W[var3] == var4 && this.I[var3] != 0 && this.I[var3] != var4 ? this.Y(this.r[var3][this.I[var3]], var1.getCount()) : var1;
            }
         }
      }
   }

   private ItemStack Y(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.e.add(var3);
      return var3;
   }

   public ItemStack Q(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.G(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.Y(var5, var5.getCount());
   }

   private ItemStack G(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      ItemStack var6 = this.C(var5, var3, false);
      int var7 = this.W[var3];
      int var8 = this.I[var3];
      if (var7 != 0 && var8 != 0 && var7 != var8) {
         this.V(var1, var2, var3);
         if (this.s[var3]) {
            int var18 = qz.z(var4, this.H[var3], this.l[var3], true);
            ItemStack var19 = var2.getSlot(var18).getItem();
            return this.C(var19, var3, var18 != var4 || this.r(var3, var18, var19));
         }

         int var9 = this.A(var1).getOrDefault(var3 * 10 + var7, 0) > this.D.getOrDefault(var3 * 10 + var7, 0) ? 1 : 0;
         int var10 = -1;
         int var11 = -1;
         int var12 = -1;
         int var13 = 0;
         int[] var14 = new int[9];

         for (int var15 = 0; var15 < 9; var15++) {
            ItemStack var16 = var2.getSlot(var15).getItem();
            var14[var15] = var16.getCount();
            int var17 = this.F(var16);
            if (var17 == var3 * 10 + var7) {
               var10 = var15;
            }

            if (var17 == var3 * 10 + var8) {
               var11 = var15;
            }

            if (var16.isEmpty()) {
               var12 = var15;
               var13++;
            } else if (var17 / 10 == var3 && var17 > 0 && this.q[var3][var15] == 0) {
               this.q[var3][var15] = var17 % 10;
            }
         }

         if (var10 < 0) {
            for (int var20 = 0; var20 < 9; var20++) {
               if (this.q[var3][var20] == var7 && var2.getSlot(var20).getItem().isEmpty()) {
                  var10 = var20;
                  break;
               }
            }

            if (var10 < 0 && var13 == 1) {
               var10 = var12;
            }
         }

         if (var10 >= 0 && var14[var10] >= this.r[var3][var7].getMaxStackSize()) {
            return var6;
         } else {
            int[] var21 = a3.F(var14, var10, var11, (boolean)var9);
            if (var21[var4] == var14[var4]) {
               return var6;
            } else {
               return var21[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.r[var3][var7] : var5).copyWithCount(var21[var4]);
            }
         }
      } else {
         return var6;
      }
   }

   private boolean r(int var1, int var2, ItemStack var3) {
      if (!this.s[var1]) {
         return false;
      }

      int var4 = this.F(var3);
      return var4 == var1 * 10 + this.W[var1] && var2 != this.H[var1] || var4 == var1 * 10 + this.I[var1] && var2 != this.l[var1];
   }

   private ItemStack C(ItemStack var1, int var2, boolean var3) {
      int var4 = this.F(var1);
      if (var4 >= 0 && this.s[var4 / 10]) {
         return this.H(var1);
      } else {
         return qz.K(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.H(var1) : var1;
      }
   }

   private void V(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.s[var3] && this.W[var3] != 0 && this.I[var3] != 0 && this.W[var3] != this.I[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.F(var2.getSlot(var8).getItem());
            if (this.q[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.q[var3][var8] = var9 % 10;
            }

            if (this.q[var3][var8] == this.W[var3] && var2.getSlot(var8).getItem().isEmpty()) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.I[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.q[var3][var4] = this.W[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.F(var2.getSlot(var4).getItem()) != var3 * 10 + this.W[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.A(var1).getOrDefault(var3 * 10 + this.W[var3], 0) > this.D.getOrDefault(var3 * 10 + this.W[var3], 0)) {
               this.H[var3] = var4;
               this.l[var3] = var5;
               this.s[var3] = (boolean)1;
            }
         }
      }
   }

   public int k(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      this.V(var1, var2, var3);
      return qz.z(var4, this.H[var3], this.l[var3], this.s[var3]);
   }
}

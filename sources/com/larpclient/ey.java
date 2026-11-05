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

final class ey {
   final BlockPos[] x = new BlockPos[2];
   private final ItemStack[][] i = new ItemStack[2][10];
   private final int[][] J = new int[2][9];
   private final int[] b = new int[]{0, 0};
   private final int[] l = new int[]{0, 0};
   private final boolean[] t = new boolean[]{false, false};
   private final boolean[] O = new boolean[]{false, false};
   private final int[] Z = new int[]{-1, -1};
   private final int[] I = new int[]{-1, -1};
   private final Set<UUID> Y = new HashSet<>();
   private final Set<UUID> A = new HashSet<>();
   private final Set<Integer> q = new HashSet<>();
   private final lp X = new lp();
   private final List<Map<Integer, Integer>> d = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu m;
   private int S = -1;
   private final Map<Integer, Integer> Q = new HashMap<>();
   private final Map<Integer, Integer> E = new HashMap<>();
   boolean C = (boolean)1;
   private boolean N = (boolean)1;
   private boolean M;
   private final Set<ItemStack> p = Collections.newSetFromMap(new WeakHashMap<>());
   private ClientLevel B;

   ey() {
      for (ItemStack[] var4 : this.i) {
         Arrays.fill(var4, ItemStack.EMPTY);
      }
   }

   void I(Minecraft var1, oi var2, BlockPos var3, BlockPos var4) {
      int var5 = this.B == var1.level && this.N == var2.D && Objects.equals(this.x[0], var3) && Objects.equals(this.x[1], var4) ? 0 : 1;
      this.N = var2.D;
      this.x[0] = var3;
      this.x[1] = var4;
      this.B = var1.level;
      this.C = (boolean)(var2.u() == oi.x.m ? 1 : 0);

      for (int var6 = 0; var6 < 2; var6++) {
         for (int var7 = 1; var7 <= 9; var7++) {
            this.i[var6][var7] = var2.y(var6 == 0, var7 - 1);
            if (this.J[var6][var7 - 1] == 0) {
               this.J[var6][var7 - 1] = var7;
            }
         }
      }

      if (var5 != 0 || !this.M) {
         this.r(var1);
         this.M = (boolean)1;
      }
   }

   boolean Q() {
      return this.b[0] > 0 || this.b[1] > 0;
   }

   void G(int var1, int var2, int var3) {
      if (this.b[var1] == 0 && var2 >= 0 && var2 < 9 && var3 >= 1 && var3 <= 9) {
         this.J[var1][var2] = var3;
      }
   }

   boolean H(int var1) {
      return this.x[var1] != null;
   }

   boolean C() {
      return this.H(0) && this.H(1);
   }

   private int B(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      String var2 = var1.getHoverName().getString();
      int var3 = _a.p(var2);
      if (var3 < 1) {
         int var8 = -1;

         for (int var9 = 0; var9 < 2; var9++) {
            for (int var6 = 1; var6 <= 9; var6++) {
               ItemStack var7 = this.i[var9][var6];
               if (this.H(var9) && var7 != null && !var7.isEmpty() && var1.is(var7.getItem()) && var2.equals(var7.getHoverName().getString())) {
                  if (var8 >= 0) {
                     return -1;
                  }

                  var8 = var9 * 10 + var6;
               }
            }
         }

         return var8;
      } else {
         int var4 = _a.R(var2) ? 0 : 1;
         ItemStack var5 = this.i[var4][var3];
         return this.H(var4) && var5 != null && !var5.isEmpty() && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> i(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.m) {
         this.X(var2, this.S);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.d) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.B(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu == null ? ItemStack.EMPTY : var1.player.containerMenu.getCarried();
      int var16 = this.B(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.B(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1 != null && var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.A.contains(var7.getUUID())) {
               int var10 = this.B(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void X(DispenserMenu var1, int var2) {
      Map var3 = this.d.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.B(var5);
         if (mr.A(var2, var6 < 0 ? -1 : var6 / 10, this.E(var2, var4, var5))) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void B(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.m == var1) {
         this.m = var1;
         this.S = var2;
         this.X(var1, var2);
      }
   }

   private void j(Minecraft var1) {
      if (var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.Y.contains(var4.getUUID())) {
               int var5 = this.B(var4.getItem());
               if (var5 >= 0) {
                  this.Y.add(var4.getUUID());
                  BlockPos var6 = this.x[var5 / 10];
                  if (var6 != null) {
                     BlockState var7 = var1.level.getBlockState(var6);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var6).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.A.add(var4.getUUID());
                           this.q.add(var5);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack y(Minecraft var1, ItemEntity var2) {
      this.z(var1);
      return this.A.contains(var2.getUUID()) ? this.Z(var2.getItem()) : var2.getItem();
   }

   public void r(Minecraft var1) {
      Arrays.fill(this.b, 0);
      Arrays.fill(this.l, 0);
      Arrays.fill(this.O, false);
      this.X.o();

      for (Map var3 : this.d) {
         var3.clear();
      }

      this.m = null;
      this.S = -1;
      Arrays.fill(this.t, false);
      Arrays.fill(this.Z, -1);
      Arrays.fill(this.I, -1);
      this.A.clear();
      this.Y.clear();
      this.q.clear();
      if (var1 != null && var1.level != null) {
         for (Entity var5 : var1.level.entitiesForRendering()) {
            this.Y.add(var5.getUUID());
         }
      }

      this.Q.clear();
      if (var1 != null && var1.player != null) {
         this.Q.putAll(this.i(var1));
      }

      this.E.clear();
      this.E.putAll(this.Q);
   }

   public void z(Minecraft var1) {
      if (this.C() && var1 != null && var1.player != null && var1.level != null) {
         this.j(var1);
         this.k(var1, this.i(var1), var1.level.getGameTime());
      }
   }

   void k(Minecraft var1, Map<Integer, Integer> var2, long var3) {
      if (this.X
         .e(
            this.b[0] > 0 && this.b[1] > 0,
            var2.getOrDefault(this.b[0], 0) <= this.E.getOrDefault(this.b[0], 0)
               && var2.getOrDefault(10 + this.b[1], 0) <= this.E.getOrDefault(10 + this.b[1], 0)
               && (!this.t[0] || var2.getOrDefault(this.l[0], 0) <= this.E.getOrDefault(this.l[0], 0))
               && (!this.t[1] || var2.getOrDefault(10 + this.l[1], 0) <= this.E.getOrDefault(10 + this.l[1], 0)),
            var3
         )) {
         this.r(var1);
      } else {
         for (Entry var6 : var2.entrySet()) {
            if (this.q.contains(var6.getKey()) && (Integer)var6.getValue() > this.Q.getOrDefault(var6.getKey(), 0)) {
               int var7 = (Integer)var6.getKey() / 10;
               int var8 = (Integer)var6.getKey() % 10;
               if (this.b[var7] == 0) {
                  this.b[var7] = var8;
               }
            }
         }

         if (!this.N) {
            System.arraycopy(this.b, 0, this.l, 0, 2);
         }

         if (this.N && this.l[0] == 0 && (this.b[0] > 0 || this.b[1] > 0)) {
            ThreadLocalRandom var10 = ThreadLocalRandom.current();
            _a.t var12 = _a.P(this.b[0] > 0 ? this.b[0] : var10.nextInt(1, 10), this.b[1] > 0 ? this.b[1] : var10.nextInt(1, 10), this.C, var10.nextInt());
            ArrayList var13 = new ArrayList();

            for (int var14 = 1; var14 <= 9; var14++) {
               for (int var9 = 1; var9 <= 9; var9++) {
                  if ((this.C ? var14 > var9 : var9 > var14) && !this.i[0][var14].isEmpty() && !this.i[1][var9].isEmpty()) {
                     var13.add(new _a.t(var14, var9));
                  }
               }
            }

            if (this.i[0][var12.host()].isEmpty() || this.i[1][var12.viewer()].isEmpty()) {
               if (var13.isEmpty()) {
                  return;
               }

               List var15 = var13.stream().filter(var1x -> var1x.host() == this.b[0] || var1x.viewer() == this.b[1]).toList();
               List var16 = var15.isEmpty() ? var13 : var15;
               var12 = (_a.t)var16.get(var10.nextInt(var16.size()));
            }

            this.l[0] = var12.host();
            this.l[1] = var12.viewer();
         }

         for (int var11 = 0; var11 < 2; var11++) {
            if (!this.O[var11] && this.b[var11] > 0 && this.l[var11] > 0) {
               this.O[var11] = (boolean)1;
               hh.K("Paper", (var11 == 0 ? "Host: " : "Viewer: ") + hh.e(Integer.toString(this.b[var11]), Integer.toString(this.l[var11]), this.N, true));
            }
         }

         this.Q.clear();
         this.Q.putAll(var2);
      }
   }

   public ItemStack Z(ItemStack var1) {
      if (this.p.contains(var1)) {
         return var1;
      } else {
         int var2 = this.B(var1);
         if (var2 < 0) {
            return var1;
         } else {
            int var3 = var2 / 10;
            int var4 = var2 % 10;
            if (this.t[var3] && var4 == this.l[var3]) {
               return this.u(this.i[var3][this.b[var3]], var1.getCount());
            } else {
               return this.b[var3] == var4 && this.l[var3] != 0 && this.l[var3] != var4 ? this.u(this.i[var3][this.l[var3]], var1.getCount()) : var1;
            }
         }
      }
   }

   private ItemStack u(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.p.add(var3);
      return var3;
   }

   public ItemStack w(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.Z(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.u(var5, var5.getCount());
   }

   private ItemStack Z(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      ItemStack var6 = this.V(var5, var3, false);
      int var7 = this.b[var3];
      int var8 = this.l[var3];
      if (var7 != 0 && var8 != 0 && var7 != var8) {
         this.K(var1, var2, var3);
         if (this.t[var3]) {
            int var18 = mr.l(var4, this.Z[var3], this.I[var3], true);
            ItemStack var19 = var2.getSlot(var18).getItem();
            return this.V(var19, var3, var18 != var4 || this.E(var3, var18, var19));
         }

         int var9 = this.i(var1).getOrDefault(var3 * 10 + var7, 0) > this.E.getOrDefault(var3 * 10 + var7, 0) ? 1 : 0;
         int var10 = -1;
         int var11 = -1;
         int var12 = -1;
         int var13 = 0;
         int[] var14 = new int[9];

         for (int var15 = 0; var15 < 9; var15++) {
            ItemStack var16 = var2.getSlot(var15).getItem();
            var14[var15] = var16.getCount();
            int var17 = this.B(var16);
            if (var17 == var3 * 10 + var7) {
               var10 = var15;
            }

            if (var17 == var3 * 10 + var8) {
               var11 = var15;
            }

            if (var16.isEmpty()) {
               var12 = var15;
               var13++;
            } else if (var17 / 10 == var3 && var17 > 0 && this.J[var3][var15] == 0) {
               this.J[var3][var15] = var17 % 10;
            }
         }

         if (var10 < 0) {
            for (int var20 = 0; var20 < 9; var20++) {
               if (this.J[var3][var20] == var7 && var2.getSlot(var20).getItem().isEmpty()) {
                  var10 = var20;
                  break;
               }
            }

            if (var10 < 0 && var13 == 1) {
               var10 = var12;
            }
         }

         if (var10 >= 0 && var14[var10] >= this.i[var3][var7].getMaxStackSize()) {
            return var6;
         } else {
            int[] var21 = xf.F(var14, var10, var11, (boolean)var9);
            if (var21[var4] == var14[var4]) {
               return var6;
            } else {
               return var21[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.i[var3][var7] : var5).copyWithCount(var21[var4]);
            }
         }
      } else {
         return var6;
      }
   }

   private boolean E(int var1, int var2, ItemStack var3) {
      if (!this.t[var1]) {
         return false;
      }

      int var4 = this.B(var3);
      return var4 == var1 * 10 + this.b[var1] && var2 != this.Z[var1] || var4 == var1 * 10 + this.l[var1] && var2 != this.I[var1];
   }

   private ItemStack V(ItemStack var1, int var2, boolean var3) {
      int var4 = this.B(var1);
      if (var4 >= 0 && this.t[var4 / 10]) {
         return this.Z(var1);
      } else {
         return mr.A(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.Z(var1) : var1;
      }
   }

   private void K(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.t[var3] && this.b[var3] != 0 && this.l[var3] != 0 && this.b[var3] != this.l[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.B(var2.getSlot(var8).getItem());
            if (this.J[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.J[var3][var8] = var9 % 10;
            }

            if (this.J[var3][var8] == this.b[var3] && var2.getSlot(var8).getItem().isEmpty()) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.l[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.J[var3][var4] = this.b[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.B(var2.getSlot(var4).getItem()) != var3 * 10 + this.b[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.i(var1).getOrDefault(var3 * 10 + this.b[var3], 0) > this.E.getOrDefault(var3 * 10 + this.b[var3], 0)) {
               this.Z[var3] = var4;
               this.I[var3] = var5;
               this.t[var3] = (boolean)1;
            }
         }
      }
   }

   public int k(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      this.K(var1, var2, var3);
      return mr.l(var4, this.Z[var3], this.I[var3], this.t[var3]);
   }
}

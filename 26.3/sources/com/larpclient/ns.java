package com.larpclient;

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

final class ns {
   private final Set<ItemStack> Y = Collections.newSetFromMap(new WeakHashMap<>());
   private final ab d = new ab();
   private final Set<UUID> j = new HashSet<>();
   private final Set<String> c = new HashSet<>();
   private final da G = new da();
   private final boolean f;
   private final boolean A;
   private boolean K;
   private boolean U;
   private boolean z;
   private final sa M = new sa();
   public int u = 1;
   private int q;
   private final BlockPos[] J = new BlockPos[2];
   private final ItemStack[][] S = new ItemStack[2][10];
   private final int[][] w = new int[2][9];
   private final int[] a = new int[]{0, 0};
   private final int[] e = new int[]{0, 0};
   private final boolean[] V = new boolean[]{false, false};
   private final int[] L = new int[]{-1, -1};
   private final int[] I = new int[]{-1, -1};
   private final Map<UUID, Integer> k = new HashMap<>();
   private final Set<UUID> X = new HashSet<>();
   private final Set<UUID> i = new HashSet<>();
   private final pg n = new pg();
   private final List<Map<Integer, Integer>> x = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu F;
   private int r = -1;
   private final Map<Integer, Integer> Q = new HashMap<>();
   private final Map<Integer, Integer> W = new HashMap<>();
   public int R;
   public boolean p = (boolean)1;

   private ItemStack m(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.Y.add(var3);
      return var3;
   }

   public boolean D() {
      boolean var1 = this.K;
      this.K = (boolean)0;
      return var1;
   }

   public static ns y() {
      ns var0 = new ns(true);
      var0.z = (boolean)1;
      return var0;
   }

   public static ns v() {
      ns var0 = new ns(true);
      var0.U = (boolean)1;
      return var0;
   }

   public ns() {
      this(false);
   }

   public ns(boolean var1) {
      this(var1, false);
   }

   public ns(boolean var1, boolean var2) {
      this.f = var1;
      this.A = var2;
   }

   public ItemStack U(int var1) {
      return this.S[0][var1] == null ? ItemStack.EMPTY : this.S[0][var1].copy();
   }

   public int[] r() {
      return (int[])this.w[0].clone();
   }

   public void A(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      int var4 = this.J() && Objects.equals(this.J[0], var2) ? 1 : 0;
      if (var4 != 0) {
         byte var5 = 1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var3.getSlot(var6).getItem().isEmpty()) {
               var5 = 0;
            }
         }

         if (var5 == 0 || this.I(var1) || this.A && this.d.Z()) {
            this.F(var3, 0, true);
            return;
         }
      }

      try {
         this.d(var1, var2, var3);
      } catch (IllegalArgumentException var7) {
         if (var4 == 0) {
            throw var7;
         }
      }

      this.F(var3, 0, true);
   }

   public void u(Minecraft var1, BlockPos var2, DispenserMenu var3, Item var4, Item var5, Item var6, int[] var7) {
      if (this.J() && Objects.equals(this.J[0], var2) && this.I(var1)) {
         this.F(var3, 0, true);
      } else {
         ItemStack[] var8 = new ItemStack[]{ItemStack.EMPTY, new ItemStack(var4), new ItemStack(var5), new ItemStack(var6)};

         for (int var9 = 0; var9 < 9; var9++) {
            ItemStack var10 = var3.getSlot(var9).getItem();
            int var11 = var7[var9];
            if (!var10.isEmpty() && var11 > 0 && var11 < 4) {
               var8[var11] = var10.copyWithCount(1);
            }
         }

         int var12 = Objects.equals(this.J[0], var2) && Arrays.equals(this.w[0], var7) ? 0 : 1;

         for (int var13 = 1; var13 <= 3; var13++) {
            var12 |= this.S[0][var13] != null && ItemStack.isSameItemSameComponents(this.S[0][var13], var8[var13]) ? 0 : 1;
         }

         this.J[0] = var2.immutable();
         this.w[0] = (int[])var7.clone();

         for (int var14 = 1; var14 <= 3; var14++) {
            this.S[0][var14] = var8[var14];
         }

         if (var12 != 0) {
            this.G(var1);
         }

         this.F(var3, 0, true);
      }
   }

   public void v() {
      this.u = this.u % this.D() + 1;
   }

   public String R(int var1) {
      return this.S[0][var1] == null ? "" : this.S[0][var1].getHoverName().getString().replace('\n', ' ').replace('\r', ' ');
   }

   private int D() {
      return this.z ? 4 : (this.A ? 2 : (this.f ? 3 : 9));
   }

   public boolean I(Minecraft var1) {
      if (var1 == null || !this.J() || var1.player == null) {
         return false;
      }

      if (this.A || this.U || this.z) {
         this.D(var1);
         return this.d.a();
      }

      if (this.l(var1)) {
         return true;
      }

      Map var2 = this.A(var1);

      for (int var3 = 0; var3 < 2; var3++) {
         if (this.a[var3] > 0 && this.e[var3] > 0 && this.a[var3] != this.e[var3]) {
            int var4 = var3 * 10 + this.a[var3];
            int var5 = var3 * 10 + this.e[var3];
            if (var2.getOrDefault(var4, 0) > this.W.getOrDefault(var4, 0) || this.V[var3] && var2.getOrDefault(var5, 0) > this.W.getOrDefault(var5, 0)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean h(int var1) {
      return this.J[var1] != null;
   }

   public boolean J() {
      return this.h(0) && (this.f || this.h(1));
   }

   public BlockPos h(int var1) {
      return this.J[var1];
   }

   public String c(int var1) {
      return this.a[var1] == 0 ? "" : Integer.toString(this.e[var1]);
   }

   public void d(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      if (var2.equals(this.J[1 - this.R])) {
         throw new IllegalArgumentException("Host and Viewer need different droppers.");
      }

      ItemStack[] var4 = new ItemStack[10];
      int[] var5 = new int[9];
      if (this.z) {
         this.R = 0;
         int var6 = 0;

         for (int var7 = 0; var7 < 9; var7++) {
            ItemStack var8 = var3.getSlot(var7).getItem();
            if (!var8.isEmpty()) {
               int var9 = l(var8);
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
      } else if (this.f) {
         this.R = 0;
         ItemStack[] var12 = new ItemStack[9];
         int[] var14 = new int[9];
         int var16 = 0;

         for (int var18 = 0; var18 < 9; var18++) {
            ItemStack var10 = var3.getSlot(var18).getItem();
            if (this.U && (var10.getCount() != 1 || !BuiltInRegistries.ITEM.getKey(var10.getItem()).getPath().endsWith("_horse_armor"))) {
               throw new IllegalArgumentException("Use one horse armor per slot, three of each type.");
            }

            if (var10.isEmpty()) {
               throw new IllegalArgumentException("This game requires nine occupied slots.");
            }

            int var11 = 0;

            while (var11 < var16 && !this.P(var12[var11], var10)) {
               var11++;
            }

            if (var11 == var16) {
               var12[var16++] = var10.copyWithCount(1);
            }

            var14[var18] = var11;
         }

         var5 = this.U ? sa.Y(var14) : (this.A ? h7.C(var14) : h7.X(var14));
         if (this.A) {
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
            int var17 = j8.i(var15.getHoverName().getString());
            String var21 = var15.getHoverName().getString();
            if (var15.isEmpty()) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + " is empty.");
            }

            if (var17 < 1) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": cannot read rank from [" + var21 + "].");
            }

            if (j8.P(var21) != (this.R == 0 ? 1 : 0)) {
               throw new IllegalArgumentException(
                  "Paper Game slot " + (var13 + 1) + ": [" + var21 + "] belongs to the " + (this.R == 0 ? "Viewer" : "Host") + " side; change the setup side."
               );
            }

            if (var4[var17] != null) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": duplicate rank " + var17 + " in [" + var21 + "].");
            }

            var4[var17] = var15.copyWithCount(1);
            var5[var13] = var17;
         }
      }

      this.S[this.R] = var4;
      this.J[this.R] = var2.immutable();
      this.w[this.R] = var5;
      this.G(var1);
   }

   private boolean P(ItemStack var1, ItemStack var2) {
      return var1.is(var2.getItem())
         && (!this.A && !this.U && !this.z || Objects.equals(var1.get(DataComponents.CUSTOM_NAME), var2.get(DataComponents.CUSTOM_NAME)));
   }

   private int O(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      if (this.f) {
         for (int var6 = 1; var6 <= this.D(); var6++) {
            if (this.h(0) && this.S[0][var6] != null && this.P(var1, this.S[0][var6])) {
               return var6;
            }
         }

         return -1;
      } else {
         String var2 = var1.getHoverName().getString();
         int var3 = j8.i(var2);
         if (var3 < 1) {
            return -1;
         }

         int var4 = j8.P(var2) ? 0 : 1;
         ItemStack var5 = this.S[var4][var3];
         return this.h(var4) && var5 != null && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> A(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.F) {
         this.S(var2, this.r);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.x) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.O(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu.getCarried();
      int var16 = this.O(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.O(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.i.contains(var7.getUUID())) {
               int var10 = this.O(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void S(DispenserMenu var1, int var2) {
      Map var3 = this.x.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.O(var5);
         if (qz.K(var2, var6 < 0 ? -1 : var6 / 10, false)) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void F(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.F == var1) {
         this.F = var1;
         this.r = var2;
         this.S(var1, var2);
         if (this.A || this.U || this.z) {
            this.D(Minecraft.getInstance());
         }
      }
   }

   private void j(Minecraft var1) {
      if (var1.level != null) {
         if (this.f && this.G.t()) {
            this.T(var1, this.a(var1));
         }

         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.X.contains(var4.getUUID())) {
               int var5 = this.O(var4.getItem());
               if (var5 >= 0) {
                  if (this.f && this.e[0] > 0 && var1.player != null && var4.distanceToSqr(var1.player) <= 9.0) {
                     int var6 = this.G.j(var5, -1);
                     if (var6 > 0 || var5 == this.a[0]) {
                        this.k.put(var4.getUUID(), var6 > 0 ? var6 : this.e[0]);
                        this.i.add(var4.getUUID());
                        this.X.add(var4.getUUID());
                        continue;
                     }
                  }

                  BlockPos var10 = this.J[var5 / 10];
                  if (var10 != null) {
                     BlockState var7 = var1.level.getBlockState(var10);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var10).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.i.add(var4.getUUID());
                           this.X.add(var4.getUUID());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack H(Minecraft var1, ItemEntity var2) {
      this.Z(var1);
      if (!this.A && !this.U && !this.z) {
         if (this.f && this.k.containsKey(var2.getUUID())) {
            return this.e(var2.getItem(), this.k.get(var2.getUUID()));
         } else {
            return this.i.contains(var2.getUUID()) ? this.q(var2.getItem()) : var2.getItem();
         }
      } else {
         return this.j.contains(var2.getUUID()) ? this.e(var2.getItem(), this.d.Z("world:" + var2.getUUID(), 1)) : var2.getItem();
      }
   }

   public void G(Minecraft var1) {
      this.M.U();
      this.K = (boolean)0;
      this.d.q(this.w[0]);
      this.j.clear();
      this.c.clear();
      if ((this.A || this.U || this.z) && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            ItemStack var3 = var2 < 36
               ? var1.player.getInventory().getItem(var2)
               : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            if (this.O(var3) > 0) {
               this.c.add("player:" + var2);
            }
         }
      }

      if (this.z && var1.level != null) {
         for (Entity var4 : var1.level.entitiesForRendering()) {
            if (var4 instanceof ArmorStand var6) {
               for (int var5 = 1; var5 <= 4; var5++) {
                  if (!var6.getItemBySlot(E(var5)).isEmpty()) {
                     this.c.add("stand:" + var6.getUUID() + ":" + var5);
                  }
               }
            }
         }
      }

      this.G.B();
      this.k.clear();
      this.q = ThreadLocalRandom.current().nextInt(2520);
      Arrays.fill(this.a, 0);
      Arrays.fill(this.e, 0);
      this.n.s();

      for (Map var10 : this.x) {
         var10.clear();
      }

      this.F = null;
      this.r = -1;
      Arrays.fill(this.V, false);
      Arrays.fill(this.L, -1);
      Arrays.fill(this.I, -1);
      this.i.clear();
      this.X.clear();
      if (var1.level != null) {
         for (Entity var11 : var1.level.entitiesForRendering()) {
            this.X.add(var11.getUUID());
         }
      }

      this.Q.clear();
      if (var1.player != null) {
         this.Q.putAll(this.A(var1));
      }

      this.W.clear();
      this.W.putAll(this.Q);
   }

   public void Z(Minecraft var1) {
      if (var1 != null && this.J() && var1.player != null && var1.level != null) {
         if (!this.A && !this.U && !this.z) {
            this.j(var1);
            int[] var2 = (int[])this.a.clone();
            Map var3 = this.A(var1);
            if (this.n
               .z(
                  this.a[0] > 0 && (this.f || this.a[1] > 0),
                  !this.l(var1)
                     && var3.getOrDefault(this.a[0], 0) <= this.W.getOrDefault(this.a[0], 0)
                     && var3.getOrDefault(10 + this.a[1], 0) <= this.W.getOrDefault(10 + this.a[1], 0)
                     && (!this.V[0] || var3.getOrDefault(this.e[0], 0) <= this.W.getOrDefault(this.e[0], 0))
                     && (!this.V[1] || var3.getOrDefault(10 + this.e[1], 0) <= this.W.getOrDefault(10 + this.e[1], 0)),
                  var1.level.getGameTime()
               )) {
               this.G(var1);
            } else {
               for (Entry var5 : var3.entrySet()) {
                  if ((Integer)var5.getValue() > this.Q.getOrDefault(var5.getKey(), 0)) {
                     int var6 = (Integer)var5.getKey() / 10;
                     int var7 = (Integer)var5.getKey() % 10;
                     if (this.A || this.U || this.z) {
                        int var8 = h7.g(this.a[var6], this.e[var6] == 0 ? this.u : this.e[var6], var7);
                        if (var8 != this.a[var6]) {
                           this.G.B();
                           this.a[var6] = var8;
                        }
                     } else if (this.a[var6] == 0) {
                        this.a[var6] = var7;
                     }
                  }
               }

               if (this.e[0] == 0 && (this.a[0] > 0 || this.a[1] > 0)) {
                  if (this.f) {
                     this.e[0] = this.u;
                  } else {
                     ThreadLocalRandom var9 = ThreadLocalRandom.current();
                     j8.u var11 = j8.x(this.a[0] > 0 ? this.a[0] : var9.nextInt(1, 10), this.a[1] > 0 ? this.a[1] : var9.nextInt(1, 10), this.p, var9.nextInt());
                     this.e[0] = var11.host();
                     this.e[1] = var11.viewer();
                  }
               }

               for (int var10 = 0; var10 < 2; var10++) {
                  if (this.a[var10] > 0 && var2[var10] == 0) {
                     this.Z(var10, this.a[var10], this.e[var10]);
                  }
               }

               this.Q.clear();
               this.Q.putAll(var3);
            }
         } else {
            this.D(var1);
         }
      }
   }

   private void Z(int var1, int var2, int var3) {
      if (var2 >= 1 && var3 >= 1) {
         String var4 = this.S[var1][var2].getHoverName().getString();
         String var5 = this.S[var1][var3].getHoverName().getString();
         bn.j(
            this.A ? "Russian Roulette" : "45/45/10",
            (this.f ? "" : (var1 == 0 ? "Host: " : "Viewer: ")) + "Drew " + var4 + (var2 == var3 ? "; unchanged." : "; spoofed as " + var5 + " (visual only).")
         );
      }
   }

   private void D(Minecraft var1) {
      if (this.J() && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.a(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.O(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.O(var6);
            if (var8 < 0) {
               this.c.remove(var7);
            }

            if (!this.c.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         BlockPos var15 = this.J[0];
         BlockState var16 = var1.level.getBlockState(var15);
         Vec3 var17 = Vec3.atCenterOf(var15);
         if (var16.is(Blocks.DROPPER) || var16.is(Blocks.DISPENSER)) {
            Direction var18 = (Direction)var16.getValue(DispenserBlock.FACING);
            var17 = var17.add(var18.getStepX() * 0.7, var18.getStepY() * 0.7, var18.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.O(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var17) <= 2.25 ? 1 : 0;
               if (!this.X.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.j.add(var10.getUUID());
               }

               if (this.j.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.O(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (this.z) {
            for (Entity var25 : var1.level.entitiesForRendering()) {
               if (var25 instanceof ArmorStand var20 && var20.distanceToSqr(var17) < 9.0) {
                  for (int var26 = 1; var26 <= 4; var26++) {
                     String var27 = "stand:" + var20.getUUID() + ":" + var26;
                     int var13 = this.O(var20.getItemBySlot(E(var26)));
                     if (var13 < 1) {
                        this.c.remove(var27);
                     }

                     if (!this.c.contains(var27)) {
                        var2.put(var27, var13);
                        var3.add(var27);
                     }
                  }
               }
            }
         }

         if (this.A && var4 != null) {
            this.d.d(var2);
         }

         if (this.U) {
            this.d.L(var2, var3, () -> this.M.B(this.u), this.q, var4 != null);
         } else {
            this.d.y(var2, var3, this.u, this.q, var4 != null);
         }

         if (this.A) {
            for (int var21 = 0; var21 < 9; var21++) {
               if (this.w[0][var21] == 2) {
                  this.d.D(2, var21);
               }
            }
         }

         if (this.U && var4 != null && this.d.s()) {
            this.M.U();
         }

         for (ab.x var24 : this.d.e()) {
            if (this.A && var24.display() == 2) {
               this.K = (boolean)1;
               this.u = 1;
            }

            this.Z(0, var24.actual(), var24.display());
         }
      }
   }

   private static EquipmentSlot E(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int l(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == E(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] e(ArmorStand var1) {
      if (this.z && this.J() && !(var1.distanceToSqr(Vec3.atCenterOf(this.J[0])) > 16.0)) {
         this.D(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(E(var3));
            int var5 = this.d.Z("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = this.O(var4) > 0 ? this.e(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   private ItemStack A(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.D(var2);
      if (var2.player == null) {
         return var1;
      }

      for (int var3 = 0; var3 < 38; var3++) {
         ItemStack var4 = var3 < 36
            ? var2.player.getInventory().getItem(var3)
            : (var3 == 36 ? var2.player.containerMenu.getCarried() : var2.player.getOffhandItem());
         if (var1 == var4) {
            return this.e(var1, this.d.Z("player:" + var3, this.c.contains("player:" + var3) ? this.O(var1) : (this.O(var1) > 0 ? 1 : -1)));
         }
      }

      return var1;
   }

   private void T(Minecraft var1, DispenserMenu var2) {
      if (this.f && var1.player != null && this.e[0] != 0) {
         int[] var3 = new int[47];
         int[] var4 = new int[47];
         Arrays.fill(var3, -2);
         Arrays.fill(var4, -1);

         for (int var5 = 0; var5 < 38; var5++) {
            ItemStack var6 = var5 < 36
               ? var1.player.getInventory().getItem(var5)
               : (var5 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            int var7 = this.O(var6);
            var3[var5] = var7;
            var4[var5] = var7 == this.a[0] ? this.e[0] : var7;
         }

         if (var2 != null) {
            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = this.O(var2.getSlot(var8).getItem());
               var3[38 + var8] = var9;
               var4[38 + var8] = var9;
               if (this.V[0] && (var8 == this.L[0] || var8 == this.I[0])) {
                  var4[38 + var8] = var9 == this.a[0] ? this.e[0] : (var9 == this.e[0] ? this.a[0] : var9);
               }
            }
         }

         this.G.i(var3, var4);
      }
   }

   private boolean l(Minecraft var1) {
      if (this.f && !this.A && !this.U && !this.z && this.V[0] && this.G.t()) {
         DispenserMenu var2 = this.a(var1);
         if (var2 != null) {
            this.T(var1, var2);
         }

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = qz.z(var3, this.L[0], this.I[0], true);
            if (this.G.T(38 + var4) != this.w[0][var3]) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private DispenserMenu a(Minecraft var1) {
      return var1.player != null && var1.player.containerMenu == this.F && this.r == 0 ? this.F : null;
   }

   private ItemStack e(ItemStack var1, int var2) {
      return var2 > 0 && var2 <= this.D() && this.S[0][var2] != null ? this.m(this.S[0][var2], var1.getCount()) : var1;
   }

   public ItemStack q(ItemStack var1) {
      if (this.Y.contains(var1)) {
         return var1;
      }

      if (!this.A && !this.U && !this.z) {
         if (this.f && this.G.t()) {
            Minecraft var2 = Minecraft.getInstance();
            this.T(var2, this.a(var2));
            if (var2.player != null) {
               for (int var3 = 0; var3 < 36; var3++) {
                  if (var1 == var2.player.getInventory().getItem(var3)) {
                     return this.e(var1, this.G.T(var3));
                  }
               }

               if (var1 == var2.player.containerMenu.getCarried()) {
                  return this.e(var1, this.G.T(36));
               }

               if (var1 == var2.player.getOffhandItem()) {
                  return this.e(var1, this.G.T(37));
               }
            }
         }

         int var5 = this.O(var1);
         if (var5 < 0) {
            return var1;
         } else {
            int var6 = var5 / 10;
            int var4 = var5 % 10;
            if (!this.f && this.V[var6] && var4 == this.e[var6]) {
               return this.m(this.S[var6][this.a[var6]], var1.getCount());
            } else {
               return this.a[var6] == var4 && this.e[var6] != 0 && this.e[var6] != var4 ? this.m(this.S[var6][this.e[var6]], var1.getCount()) : var1;
            }
         }
      } else {
         return this.A(var1);
      }
   }

   public ItemStack Q(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.j(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.m(var5, var5.getCount());
   }

   private ItemStack j(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      if (!this.A && !this.U && !this.z) {
         ItemStack var18 = this.W(var5, var3, false);
         int var19 = this.a[var3];
         int var8 = this.e[var3];
         if (var19 != 0 && var8 != 0 && var19 != var8) {
            this.f(var1, var2, var3);
            if (this.f && this.V[var3]) {
               this.T(var1, var2);
               int var21 = qz.z(var4, this.L[var3], this.I[var3], true);
               return this.e(var2.getSlot(var21).getItem(), this.G.T(38 + var21));
            }

            if (this.V[var3]) {
               int var20 = qz.z(var4, this.L[var3], this.I[var3], true);
               ItemStack var22 = var2.getSlot(var20).getItem();
               return this.W(var22, var3, var20 != var4);
            }

            int var9 = this.A(var1).getOrDefault(var3 * 10 + var19, 0) > this.W.getOrDefault(var3 * 10 + var19, 0) ? 1 : 0;
            int var10 = -1;
            int var11 = -1;
            int var12 = -1;
            int var13 = 0;
            int[] var14 = new int[9];

            for (int var15 = 0; var15 < 9; var15++) {
               ItemStack var16 = var2.getSlot(var15).getItem();
               var14[var15] = var16.getCount();
               int var17 = this.O(var16);
               if (var17 == var3 * 10 + var19) {
                  var10 = var15;
               }

               if (var17 == var3 * 10 + var8) {
                  var11 = var15;
               }

               if (var16.isEmpty()) {
                  var12 = var15;
                  var13++;
               } else if (var17 / 10 == var3 && var17 > 0 && this.w[var3][var15] == 0) {
                  this.w[var3][var15] = var17 % 10;
               }
            }

            if (var10 < 0) {
               for (int var23 = 0; var23 < 9; var23++) {
                  if (this.w[var3][var23] == var19 && var2.getSlot(var23).getItem().isEmpty()) {
                     var10 = var23;
                     break;
                  }
               }

               if (var10 < 0 && var13 == 1) {
                  var10 = var12;
               }
            }

            if (this.f) {
               var11 = this.K(var2, var8);
            }

            if (var10 >= 0 && var14[var10] >= this.S[var3][var19].getMaxStackSize()) {
               return var18;
            } else {
               int[] var24 = a3.F(var14, var10, var11, (boolean)var9);
               if (var24[var4] == var14[var4]) {
                  return var18;
               } else {
                  return var24[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.S[var3][var19] : var5).copyWithCount(var24[var4]);
               }
            }
         } else {
            return var18;
         }
      } else {
         this.D(var1);
         int var6 = this.d.T(var4);
         ItemStack var7 = var2.getSlot(var6).getItem();
         return this.e(var7, this.d.Z("stock:" + var6, this.O(var7) > 0 ? 1 : -1));
      }
   }

   private ItemStack W(ItemStack var1, int var2, boolean var3) {
      int var4 = this.O(var1);
      if (!this.f && var4 >= 0 && this.V[var4 / 10]) {
         return this.q(var1);
      } else {
         return qz.K(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.q(var1) : var1;
      }
   }

   private int K(DispenserMenu var1, int var2) {
      int[] var3 = new int[9];

      for (int var4 = 0; var4 < 9; var4++) {
         var3[var4] = this.O(var1.getSlot(var4).getItem());
      }

      return h7.b(var3, var2, this.q);
   }

   private void f(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.V[var3] && this.a[var3] != 0 && this.e[var3] != 0 && this.a[var3] != this.e[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.O(var2.getSlot(var8).getItem());
            if (this.w[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.w[var3][var8] = var9 % 10;
            }

            if (this.w[var3][var8] == this.a[var3] && (var4 < 0 || var2.getSlot(var8).getItem().isEmpty())) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.e[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (this.f) {
            var5 = this.K(var2, this.e[var3]);
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.w[var3][var4] = this.a[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.O(var2.getSlot(var4).getItem()) != var3 * 10 + this.a[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.A(var1).getOrDefault(var3 * 10 + this.a[var3], 0) > this.W.getOrDefault(var3 * 10 + this.a[var3], 0)) {
               this.L[var3] = var4;
               this.I[var3] = var5;
               this.V[var3] = (boolean)1;
            }
         }
      }
   }

   public int m(Minecraft var1, int var2) {
      this.D(var1);
      return this.d.p("player:" + var2);
   }

   public void u(DispenserMenu var1, int var2) {
      int var3 = this.d.Z("player:" + var2, -1);

      for (int var4 = 0; var4 < 9; var4++) {
         if ((!this.A || (this.w[0][var4] == 2 ? 1 : 0) == (var3 == 2 ? 1 : 0)) && var1.getSlot(this.d.T(var4)).getItem().isEmpty()) {
            this.d.k("player:" + var2, var4);
            return;
         }
      }
   }

   public boolean i(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (this.f && !this.A && !this.U && !this.z && this.V[0]) {
         ItemStack var5 = var4 == 36 ? var2.getCarried() : var1.player.getInventory().getItem(var4);
         ItemStack var6 = var2.getSlot(var3).getItem();
         if (var6.getCount() == 1 && var6.getMaxStackSize() == 1 && ItemStack.matches(var6, var5)) {
            this.T(var1, var2);
            return this.G.y(38 + var3, var4);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int F(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (!this.A && !this.U && !this.z) {
         this.f(var1, var2, var3);
         if (this.f && this.V[var3]) {
            this.T(var1, var2);
         }

         return qz.z(var4, this.L[var3], this.I[var3], this.V[var3]);
      } else {
         this.D(var1);
         return this.d.T(var4);
      }
   }
}

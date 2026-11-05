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

final class hf {
   private final Set<ItemStack> Q = Collections.newSetFromMap(new WeakHashMap<>());
   private final mp l = new mp();
   private final Set<UUID> P = new HashSet<>();
   private final Set<String> H = new HashSet<>();
   private final yr X = new yr();
   private final boolean r;
   private final boolean a;
   private boolean g;
   private boolean t;
   private boolean Y;
   private final h8 m = new h8();
   public int M = 1;
   private int I;
   private final BlockPos[] y = new BlockPos[2];
   private final ItemStack[][] p = new ItemStack[2][10];
   private final int[][] d = new int[2][9];
   private final int[] N = new int[]{0, 0};
   private final int[] S = new int[]{0, 0};
   private final boolean[] u = new boolean[]{false, false};
   private final int[] U = new int[]{-1, -1};
   private final int[] f = new int[]{-1, -1};
   private final Map<UUID, Integer> o = new HashMap<>();
   private final Set<UUID> J = new HashSet<>();
   private final Set<UUID> h = new HashSet<>();
   private final lp q = new lp();
   private final List<Map<Integer, Integer>> v = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu V;
   private int L = -1;
   private final Map<Integer, Integer> i = new HashMap<>();
   private final Map<Integer, Integer> c = new HashMap<>();
   public int Z;
   public boolean K = (boolean)1;

   private ItemStack s(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.Q.add(var3);
      return var3;
   }

   public boolean v() {
      boolean var1 = this.g;
      this.g = (boolean)0;
      return var1;
   }

   public static hf j() {
      hf var0 = new hf(true);
      var0.Y = (boolean)1;
      return var0;
   }

   public static hf P() {
      hf var0 = new hf(true);
      var0.t = (boolean)1;
      return var0;
   }

   public hf() {
      this(false);
   }

   public hf(boolean var1) {
      this(var1, false);
   }

   public hf(boolean var1, boolean var2) {
      this.r = var1;
      this.a = var2;
   }

   public ItemStack k(int var1) {
      return this.p[0][var1] == null ? ItemStack.EMPTY : this.p[0][var1].copy();
   }

   public int[] O() {
      return (int[])this.d[0].clone();
   }

   public void s(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      int var4 = this.E() && Objects.equals(this.y[0], var2) ? 1 : 0;
      if (var4 != 0) {
         byte var5 = 1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var3.getSlot(var6).getItem().isEmpty()) {
               var5 = 0;
            }
         }

         if (var5 == 0 || this.y(var1) || this.a && this.l.g()) {
            this.c(var3, 0, true);
            return;
         }
      }

      try {
         this.V(var1, var2, var3);
      } catch (IllegalArgumentException var7) {
         if (var4 == 0) {
            throw var7;
         }
      }

      this.c(var3, 0, true);
   }

   public void D(Minecraft var1, BlockPos var2, DispenserMenu var3, Item var4, Item var5, Item var6, int[] var7) {
      if (this.E() && Objects.equals(this.y[0], var2) && this.y(var1)) {
         this.c(var3, 0, true);
      } else {
         ItemStack[] var8 = new ItemStack[]{ItemStack.EMPTY, new ItemStack(var4), new ItemStack(var5), new ItemStack(var6)};

         for (int var9 = 0; var9 < 9; var9++) {
            ItemStack var10 = var3.getSlot(var9).getItem();
            int var11 = var7[var9];
            if (!var10.isEmpty() && var11 > 0 && var11 < 4) {
               var8[var11] = var10.copyWithCount(1);
            }
         }

         int var12 = Objects.equals(this.y[0], var2) && Arrays.equals(this.d[0], var7) ? 0 : 1;

         for (int var13 = 1; var13 <= 3; var13++) {
            var12 |= this.p[0][var13] != null && ItemStack.isSameItemSameComponents(this.p[0][var13], var8[var13]) ? 0 : 1;
         }

         this.y[0] = var2.immutable();
         this.d[0] = (int[])var7.clone();

         for (int var14 = 1; var14 <= 3; var14++) {
            this.p[0][var14] = var8[var14];
         }

         if (var12 != 0) {
            this.a(var1);
         }

         this.c(var3, 0, true);
      }
   }

   public void S() {
      this.M = this.M % this.N() + 1;
   }

   public String l(int var1) {
      return this.p[0][var1] == null ? "" : this.p[0][var1].getHoverName().getString().replace('\n', ' ').replace('\r', ' ');
   }

   private int N() {
      return this.Y ? 4 : (this.a ? 2 : (this.r ? 3 : 9));
   }

   public boolean y(Minecraft var1) {
      if (var1 == null || !this.E() || var1.player == null) {
         return false;
      }

      if (this.a || this.t || this.Y) {
         this.S(var1);
         return this.l.L();
      }

      if (this.C(var1)) {
         return true;
      }

      Map var2 = this.C(var1);

      for (int var3 = 0; var3 < 2; var3++) {
         if (this.N[var3] > 0 && this.S[var3] > 0 && this.N[var3] != this.S[var3]) {
            int var4 = var3 * 10 + this.N[var3];
            int var5 = var3 * 10 + this.S[var3];
            if (var2.getOrDefault(var4, 0) > this.c.getOrDefault(var4, 0) || this.u[var3] && var2.getOrDefault(var5, 0) > this.c.getOrDefault(var5, 0)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean k(int var1) {
      return this.y[var1] != null;
   }

   public boolean E() {
      return this.k(0) && (this.r || this.k(1));
   }

   public BlockPos t(int var1) {
      return this.y[var1];
   }

   public String n(int var1) {
      return this.N[var1] == 0 ? "" : Integer.toString(this.S[var1]);
   }

   public void V(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      if (var2.equals(this.y[1 - this.Z])) {
         throw new IllegalArgumentException("Host and Viewer need different droppers.");
      }

      ItemStack[] var4 = new ItemStack[10];
      int[] var5 = new int[9];
      if (this.Y) {
         this.Z = 0;
         int var6 = 0;

         for (int var7 = 0; var7 < 9; var7++) {
            ItemStack var8 = var3.getSlot(var7).getItem();
            if (!var8.isEmpty()) {
               int var9 = Q(var8);
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
      } else if (this.r) {
         this.Z = 0;
         ItemStack[] var12 = new ItemStack[9];
         int[] var14 = new int[9];
         int var16 = 0;

         for (int var18 = 0; var18 < 9; var18++) {
            ItemStack var10 = var3.getSlot(var18).getItem();
            if (this.t && (var10.getCount() != 1 || !BuiltInRegistries.ITEM.getKey(var10.getItem()).getPath().endsWith("_horse_armor"))) {
               throw new IllegalArgumentException("Use one horse armor per slot, three of each type.");
            }

            if (var10.isEmpty()) {
               throw new IllegalArgumentException("This game requires nine occupied slots.");
            }

            int var11 = 0;

            while (var11 < var16 && !this.O(var12[var11], var10)) {
               var11++;
            }

            if (var11 == var16) {
               var12[var16++] = var10.copyWithCount(1);
            }

            var14[var18] = var11;
         }

         var5 = this.t ? h8.R(var14) : (this.a ? fu.H(var14) : fu.k(var14));
         if (this.a) {
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
            int var17 = _a.p(var15.getHoverName().getString());
            String var21 = var15.getHoverName().getString();
            if (var15.isEmpty()) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + " is empty.");
            }

            if (var17 < 1) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": cannot read rank from [" + var21 + "].");
            }

            if (_a.R(var21) != (this.Z == 0 ? 1 : 0)) {
               throw new IllegalArgumentException(
                  "Paper Game slot " + (var13 + 1) + ": [" + var21 + "] belongs to the " + (this.Z == 0 ? "Viewer" : "Host") + " side; change the setup side."
               );
            }

            if (var4[var17] != null) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": duplicate rank " + var17 + " in [" + var21 + "].");
            }

            var4[var17] = var15.copyWithCount(1);
            var5[var13] = var17;
         }
      }

      this.p[this.Z] = var4;
      this.y[this.Z] = var2.immutable();
      this.d[this.Z] = var5;
      this.a(var1);
   }

   private boolean O(ItemStack var1, ItemStack var2) {
      return var1.is(var2.getItem())
         && (!this.a && !this.t && !this.Y || Objects.equals(var1.get(DataComponents.CUSTOM_NAME), var2.get(DataComponents.CUSTOM_NAME)));
   }

   private int q(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      if (this.r) {
         for (int var6 = 1; var6 <= this.N(); var6++) {
            if (this.k(0) && this.p[0][var6] != null && this.O(var1, this.p[0][var6])) {
               return var6;
            }
         }

         return -1;
      } else {
         String var2 = var1.getHoverName().getString();
         int var3 = _a.p(var2);
         if (var3 < 1) {
            return -1;
         }

         int var4 = _a.R(var2) ? 0 : 1;
         ItemStack var5 = this.p[var4][var3];
         return this.k(var4) && var5 != null && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> C(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.V) {
         this.v(var2, this.L);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.v) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.q(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu.getCarried();
      int var16 = this.q(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.q(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.h.contains(var7.getUUID())) {
               int var10 = this.q(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void v(DispenserMenu var1, int var2) {
      Map var3 = this.v.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.q(var5);
         if (mr.A(var2, var6 < 0 ? -1 : var6 / 10, false)) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void c(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.V == var1) {
         this.V = var1;
         this.L = var2;
         this.v(var1, var2);
         if (this.a || this.t || this.Y) {
            this.S(Minecraft.getInstance());
         }
      }
   }

   private void X(Minecraft var1) {
      if (var1.level != null) {
         if (this.r && this.X.d()) {
            this.n(var1, this.N(var1));
         }

         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.J.contains(var4.getUUID())) {
               int var5 = this.q(var4.getItem());
               if (var5 >= 0) {
                  if (this.r && this.S[0] > 0 && var1.player != null && var4.distanceToSqr(var1.player) <= 9.0) {
                     int var6 = this.X.I(var5, -1);
                     if (var6 > 0 || var5 == this.N[0]) {
                        this.o.put(var4.getUUID(), var6 > 0 ? var6 : this.S[0]);
                        this.h.add(var4.getUUID());
                        this.J.add(var4.getUUID());
                        continue;
                     }
                  }

                  BlockPos var10 = this.y[var5 / 10];
                  if (var10 != null) {
                     BlockState var7 = var1.level.getBlockState(var10);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var10).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.h.add(var4.getUUID());
                           this.J.add(var4.getUUID());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack Y(Minecraft var1, ItemEntity var2) {
      this.b(var1);
      if (!this.a && !this.t && !this.Y) {
         if (this.r && this.o.containsKey(var2.getUUID())) {
            return this.D(var2.getItem(), this.o.get(var2.getUUID()));
         } else {
            return this.h.contains(var2.getUUID()) ? this.j(var2.getItem()) : var2.getItem();
         }
      } else {
         return this.P.contains(var2.getUUID()) ? this.D(var2.getItem(), this.l.F("world:" + var2.getUUID(), 1)) : var2.getItem();
      }
   }

   public void a(Minecraft var1) {
      this.m.w();
      this.g = (boolean)0;
      this.l.I(this.d[0]);
      this.P.clear();
      this.H.clear();
      if ((this.a || this.t || this.Y) && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            ItemStack var3 = var2 < 36
               ? var1.player.getInventory().getItem(var2)
               : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            if (this.q(var3) > 0) {
               this.H.add("player:" + var2);
            }
         }
      }

      if (this.Y && var1.level != null) {
         for (Entity var4 : var1.level.entitiesForRendering()) {
            if (var4 instanceof ArmorStand var6) {
               for (int var5 = 1; var5 <= 4; var5++) {
                  if (!var6.getItemBySlot(w(var5)).isEmpty()) {
                     this.H.add("stand:" + var6.getUUID() + ":" + var5);
                  }
               }
            }
         }
      }

      this.X.A();
      this.o.clear();
      this.I = ThreadLocalRandom.current().nextInt(2520);
      Arrays.fill(this.N, 0);
      Arrays.fill(this.S, 0);
      this.q.o();

      for (Map var10 : this.v) {
         var10.clear();
      }

      this.V = null;
      this.L = -1;
      Arrays.fill(this.u, false);
      Arrays.fill(this.U, -1);
      Arrays.fill(this.f, -1);
      this.h.clear();
      this.J.clear();
      if (var1.level != null) {
         for (Entity var11 : var1.level.entitiesForRendering()) {
            this.J.add(var11.getUUID());
         }
      }

      this.i.clear();
      if (var1.player != null) {
         this.i.putAll(this.C(var1));
      }

      this.c.clear();
      this.c.putAll(this.i);
   }

   public void b(Minecraft var1) {
      if (var1 != null && this.E() && var1.player != null && var1.level != null) {
         if (!this.a && !this.t && !this.Y) {
            this.X(var1);
            int[] var2 = (int[])this.N.clone();
            Map var3 = this.C(var1);
            if (this.q
               .e(
                  this.N[0] > 0 && (this.r || this.N[1] > 0),
                  !this.C(var1)
                     && var3.getOrDefault(this.N[0], 0) <= this.c.getOrDefault(this.N[0], 0)
                     && var3.getOrDefault(10 + this.N[1], 0) <= this.c.getOrDefault(10 + this.N[1], 0)
                     && (!this.u[0] || var3.getOrDefault(this.S[0], 0) <= this.c.getOrDefault(this.S[0], 0))
                     && (!this.u[1] || var3.getOrDefault(10 + this.S[1], 0) <= this.c.getOrDefault(10 + this.S[1], 0)),
                  var1.level.getGameTime()
               )) {
               this.a(var1);
            } else {
               for (Entry var5 : var3.entrySet()) {
                  if ((Integer)var5.getValue() > this.i.getOrDefault(var5.getKey(), 0)) {
                     int var6 = (Integer)var5.getKey() / 10;
                     int var7 = (Integer)var5.getKey() % 10;
                     if (this.a || this.t || this.Y) {
                        int var8 = fu.p(this.N[var6], this.S[var6] == 0 ? this.M : this.S[var6], var7);
                        if (var8 != this.N[var6]) {
                           this.X.A();
                           this.N[var6] = var8;
                        }
                     } else if (this.N[var6] == 0) {
                        this.N[var6] = var7;
                     }
                  }
               }

               if (this.S[0] == 0 && (this.N[0] > 0 || this.N[1] > 0)) {
                  if (this.r) {
                     this.S[0] = this.M;
                  } else {
                     ThreadLocalRandom var9 = ThreadLocalRandom.current();
                     _a.t var11 = _a.P(this.N[0] > 0 ? this.N[0] : var9.nextInt(1, 10), this.N[1] > 0 ? this.N[1] : var9.nextInt(1, 10), this.K, var9.nextInt());
                     this.S[0] = var11.host();
                     this.S[1] = var11.viewer();
                  }
               }

               for (int var10 = 0; var10 < 2; var10++) {
                  if (this.N[var10] > 0 && var2[var10] == 0) {
                     this.j(var10, this.N[var10], this.S[var10]);
                  }
               }

               this.i.clear();
               this.i.putAll(var3);
            }
         } else {
            this.S(var1);
         }
      }
   }

   private void j(int var1, int var2, int var3) {
      if (var2 >= 1 && var3 >= 1) {
         String var4 = this.p[var1][var2].getHoverName().getString();
         String var5 = this.p[var1][var3].getHoverName().getString();
         hh.K(
            this.a ? "Russian Roulette" : "45/45/10",
            (this.r ? "" : (var1 == 0 ? "Host: " : "Viewer: ")) + "Drew " + var4 + (var2 == var3 ? "; unchanged." : "; spoofed as " + var5 + " (visual only).")
         );
      }
   }

   private void S(Minecraft var1) {
      if (this.E() && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.N(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.q(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.q(var6);
            if (var8 < 0) {
               this.H.remove(var7);
            }

            if (!this.H.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         BlockPos var15 = this.y[0];
         BlockState var16 = var1.level.getBlockState(var15);
         Vec3 var17 = Vec3.atCenterOf(var15);
         if (var16.is(Blocks.DROPPER) || var16.is(Blocks.DISPENSER)) {
            Direction var18 = (Direction)var16.getValue(DispenserBlock.FACING);
            var17 = var17.add(var18.getStepX() * 0.7, var18.getStepY() * 0.7, var18.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.q(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var17) <= 2.25 ? 1 : 0;
               if (!this.J.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.P.add(var10.getUUID());
               }

               if (this.P.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.q(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (this.Y) {
            for (Entity var25 : var1.level.entitiesForRendering()) {
               if (var25 instanceof ArmorStand var20 && var20.distanceToSqr(var17) < 9.0) {
                  for (int var26 = 1; var26 <= 4; var26++) {
                     String var27 = "stand:" + var20.getUUID() + ":" + var26;
                     int var13 = this.q(var20.getItemBySlot(w(var26)));
                     if (var13 < 1) {
                        this.H.remove(var27);
                     }

                     if (!this.H.contains(var27)) {
                        var2.put(var27, var13);
                        var3.add(var27);
                     }
                  }
               }
            }
         }

         if (this.a && var4 != null) {
            this.l.V(var2);
         }

         if (this.t) {
            this.l.c(var2, var3, () -> this.m.S(this.M), this.I, var4 != null);
         } else {
            this.l.v(var2, var3, this.M, this.I, var4 != null);
         }

         if (this.a) {
            for (int var21 = 0; var21 < 9; var21++) {
               if (this.d[0][var21] == 2) {
                  this.l.U(2, var21);
               }
            }
         }

         if (this.t && var4 != null && this.l.b()) {
            this.m.w();
         }

         for (mp.z var24 : this.l.J()) {
            if (this.a && var24.display() == 2) {
               this.g = (boolean)1;
               this.M = 1;
            }

            this.j(0, var24.actual(), var24.display());
         }
      }
   }

   private static EquipmentSlot w(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int Q(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == w(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] v(ArmorStand var1) {
      if (this.Y && this.E() && !(var1.distanceToSqr(Vec3.atCenterOf(this.y[0])) > 16.0)) {
         this.S(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(w(var3));
            int var5 = this.l.F("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = this.q(var4) > 0 ? this.D(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   private ItemStack C(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.S(var2);
      if (var2.player == null) {
         return var1;
      }

      for (int var3 = 0; var3 < 38; var3++) {
         ItemStack var4 = var3 < 36
            ? var2.player.getInventory().getItem(var3)
            : (var3 == 36 ? var2.player.containerMenu.getCarried() : var2.player.getOffhandItem());
         if (var1 == var4) {
            return this.D(var1, this.l.F("player:" + var3, this.H.contains("player:" + var3) ? this.q(var1) : (this.q(var1) > 0 ? 1 : -1)));
         }
      }

      return var1;
   }

   private void n(Minecraft var1, DispenserMenu var2) {
      if (this.r && var1.player != null && this.S[0] != 0) {
         int[] var3 = new int[47];
         int[] var4 = new int[47];
         Arrays.fill(var3, -2);
         Arrays.fill(var4, -1);

         for (int var5 = 0; var5 < 38; var5++) {
            ItemStack var6 = var5 < 36
               ? var1.player.getInventory().getItem(var5)
               : (var5 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            int var7 = this.q(var6);
            var3[var5] = var7;
            var4[var5] = var7 == this.N[0] ? this.S[0] : var7;
         }

         if (var2 != null) {
            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = this.q(var2.getSlot(var8).getItem());
               var3[38 + var8] = var9;
               var4[38 + var8] = var9;
               if (this.u[0] && (var8 == this.U[0] || var8 == this.f[0])) {
                  var4[38 + var8] = var9 == this.N[0] ? this.S[0] : (var9 == this.S[0] ? this.N[0] : var9);
               }
            }
         }

         this.X.n(var3, var4);
      }
   }

   private boolean C(Minecraft var1) {
      if (this.r && !this.a && !this.t && !this.Y && this.u[0] && this.X.d()) {
         DispenserMenu var2 = this.N(var1);
         if (var2 != null) {
            this.n(var1, var2);
         }

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = mr.l(var3, this.U[0], this.f[0], true);
            if (this.X.b(38 + var4) != this.d[0][var3]) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private DispenserMenu N(Minecraft var1) {
      return var1.player != null && var1.player.containerMenu == this.V && this.L == 0 ? this.V : null;
   }

   private ItemStack D(ItemStack var1, int var2) {
      return var2 > 0 && var2 <= this.N() && this.p[0][var2] != null ? this.s(this.p[0][var2], var1.getCount()) : var1;
   }

   public ItemStack j(ItemStack var1) {
      if (this.Q.contains(var1)) {
         return var1;
      }

      if (!this.a && !this.t && !this.Y) {
         if (this.r && this.X.d()) {
            Minecraft var2 = Minecraft.getInstance();
            this.n(var2, this.N(var2));
            if (var2.player != null) {
               for (int var3 = 0; var3 < 36; var3++) {
                  if (var1 == var2.player.getInventory().getItem(var3)) {
                     return this.D(var1, this.X.b(var3));
                  }
               }

               if (var1 == var2.player.containerMenu.getCarried()) {
                  return this.D(var1, this.X.b(36));
               }

               if (var1 == var2.player.getOffhandItem()) {
                  return this.D(var1, this.X.b(37));
               }
            }
         }

         int var5 = this.q(var1);
         if (var5 < 0) {
            return var1;
         } else {
            int var6 = var5 / 10;
            int var4 = var5 % 10;
            if (!this.r && this.u[var6] && var4 == this.S[var6]) {
               return this.s(this.p[var6][this.N[var6]], var1.getCount());
            } else {
               return this.N[var6] == var4 && this.S[var6] != 0 && this.S[var6] != var4 ? this.s(this.p[var6][this.S[var6]], var1.getCount()) : var1;
            }
         }
      } else {
         return this.C(var1);
      }
   }

   public ItemStack Q(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.P(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.s(var5, var5.getCount());
   }

   private ItemStack P(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      if (!this.a && !this.t && !this.Y) {
         ItemStack var18 = this.L(var5, var3, false);
         int var19 = this.N[var3];
         int var8 = this.S[var3];
         if (var19 != 0 && var8 != 0 && var19 != var8) {
            this.N(var1, var2, var3);
            if (this.r && this.u[var3]) {
               this.n(var1, var2);
               int var21 = mr.l(var4, this.U[var3], this.f[var3], true);
               return this.D(var2.getSlot(var21).getItem(), this.X.b(38 + var21));
            }

            if (this.u[var3]) {
               int var20 = mr.l(var4, this.U[var3], this.f[var3], true);
               ItemStack var22 = var2.getSlot(var20).getItem();
               return this.L(var22, var3, var20 != var4);
            }

            int var9 = this.C(var1).getOrDefault(var3 * 10 + var19, 0) > this.c.getOrDefault(var3 * 10 + var19, 0) ? 1 : 0;
            int var10 = -1;
            int var11 = -1;
            int var12 = -1;
            int var13 = 0;
            int[] var14 = new int[9];

            for (int var15 = 0; var15 < 9; var15++) {
               ItemStack var16 = var2.getSlot(var15).getItem();
               var14[var15] = var16.getCount();
               int var17 = this.q(var16);
               if (var17 == var3 * 10 + var19) {
                  var10 = var15;
               }

               if (var17 == var3 * 10 + var8) {
                  var11 = var15;
               }

               if (var16.isEmpty()) {
                  var12 = var15;
                  var13++;
               } else if (var17 / 10 == var3 && var17 > 0 && this.d[var3][var15] == 0) {
                  this.d[var3][var15] = var17 % 10;
               }
            }

            if (var10 < 0) {
               for (int var23 = 0; var23 < 9; var23++) {
                  if (this.d[var3][var23] == var19 && var2.getSlot(var23).getItem().isEmpty()) {
                     var10 = var23;
                     break;
                  }
               }

               if (var10 < 0 && var13 == 1) {
                  var10 = var12;
               }
            }

            if (this.r) {
               var11 = this.w(var2, var8);
            }

            if (var10 >= 0 && var14[var10] >= this.p[var3][var19].getMaxStackSize()) {
               return var18;
            } else {
               int[] var24 = xf.F(var14, var10, var11, (boolean)var9);
               if (var24[var4] == var14[var4]) {
                  return var18;
               } else {
                  return var24[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.p[var3][var19] : var5).copyWithCount(var24[var4]);
               }
            }
         } else {
            return var18;
         }
      } else {
         this.S(var1);
         int var6 = this.l.a(var4);
         ItemStack var7 = var2.getSlot(var6).getItem();
         return this.D(var7, this.l.F("stock:" + var6, this.q(var7) > 0 ? 1 : -1));
      }
   }

   private ItemStack L(ItemStack var1, int var2, boolean var3) {
      int var4 = this.q(var1);
      if (!this.r && var4 >= 0 && this.u[var4 / 10]) {
         return this.j(var1);
      } else {
         return mr.A(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.j(var1) : var1;
      }
   }

   private int w(DispenserMenu var1, int var2) {
      int[] var3 = new int[9];

      for (int var4 = 0; var4 < 9; var4++) {
         var3[var4] = this.q(var1.getSlot(var4).getItem());
      }

      return fu.g(var3, var2, this.I);
   }

   private void N(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.u[var3] && this.N[var3] != 0 && this.S[var3] != 0 && this.N[var3] != this.S[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.q(var2.getSlot(var8).getItem());
            if (this.d[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.d[var3][var8] = var9 % 10;
            }

            if (this.d[var3][var8] == this.N[var3] && (var4 < 0 || var2.getSlot(var8).getItem().isEmpty())) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.S[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (this.r) {
            var5 = this.w(var2, this.S[var3]);
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.d[var3][var4] = this.N[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.q(var2.getSlot(var4).getItem()) != var3 * 10 + this.N[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.C(var1).getOrDefault(var3 * 10 + this.N[var3], 0) > this.c.getOrDefault(var3 * 10 + this.N[var3], 0)) {
               this.U[var3] = var4;
               this.f[var3] = var5;
               this.u[var3] = (boolean)1;
            }
         }
      }
   }

   public int I(Minecraft var1, int var2) {
      this.S(var1);
      return this.l.J("player:" + var2);
   }

   public void E(DispenserMenu var1, int var2) {
      int var3 = this.l.F("player:" + var2, -1);

      for (int var4 = 0; var4 < 9; var4++) {
         if ((!this.a || (this.d[0][var4] == 2 ? 1 : 0) == (var3 == 2 ? 1 : 0)) && var1.getSlot(this.l.a(var4)).getItem().isEmpty()) {
            this.l.i("player:" + var2, var4);
            return;
         }
      }
   }

   public boolean L(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (this.r && !this.a && !this.t && !this.Y && this.u[0]) {
         ItemStack var5 = var4 == 36 ? var2.getCarried() : var1.player.getInventory().getItem(var4);
         ItemStack var6 = var2.getSlot(var3).getItem();
         if (var6.getCount() == 1 && var6.getMaxStackSize() == 1 && ItemStack.matches(var6, var5)) {
            this.n(var1, var2);
            return this.X.f(38 + var3, var4);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int L(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (!this.a && !this.t && !this.Y) {
         this.N(var1, var2, var3);
         if (this.r && this.u[var3]) {
            this.n(var1, var2);
         }

         return mr.l(var4, this.U[var3], this.f[var3], this.u[var3]);
      } else {
         this.S(var1);
         return this.l.a(var4);
      }
   }
}

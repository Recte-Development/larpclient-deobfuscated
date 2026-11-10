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

final class gs {
   private final Set<ItemStack> h = Collections.newSetFromMap(new WeakHashMap<>());
   private final p0 R = new p0();
   private final Set<UUID> G = new HashSet<>();
   private final Set<String> m = new HashSet<>();
   private final jr u = new jr();
   private final boolean U;
   private final boolean o;
   private boolean t;
   private boolean i;
   private boolean Y;
   private final u7 f = new u7();
   public int Q = 1;
   private int y;
   private final BlockPos[] n = new BlockPos[2];
   private final ItemStack[][] X = new ItemStack[2][10];
   private final int[][] O = new int[2][9];
   private final int[] g = new int[]{0, 0};
   private final int[] k = new int[]{0, 0};
   private final boolean[] V = new boolean[]{false, false};
   private final int[] b = new int[]{-1, -1};
   private final int[] A = new int[]{-1, -1};
   private final Map<UUID, Integer> T = new HashMap<>();
   private final Set<UUID> v = new HashSet<>();
   private final Set<UUID> C = new HashSet<>();
   private final uk j = new uk();
   private final List<Map<Integer, Integer>> I = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu a;
   private int B = -1;
   private final Map<Integer, Integer> s = new HashMap<>();
   private final Map<Integer, Integer> W = new HashMap<>();
   public int Z;
   public boolean z = (boolean)1;

   private ItemStack F(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.h.add(var3);
      return var3;
   }

   public boolean C() {
      boolean var1 = this.t;
      this.t = (boolean)0;
      return var1;
   }

   public static gs d() {
      gs var0 = new gs(true);
      var0.Y = (boolean)1;
      return var0;
   }

   public static gs p() {
      gs var0 = new gs(true);
      var0.i = (boolean)1;
      return var0;
   }

   public gs() {
      this(false);
   }

   public gs(boolean var1) {
      this(var1, false);
   }

   public gs(boolean var1, boolean var2) {
      this.U = var1;
      this.o = var2;
   }

   public ItemStack H(int var1) {
      return this.X[0][var1] == null ? ItemStack.EMPTY : this.X[0][var1].copy();
   }

   public int[] h() {
      return (int[])this.O[0].clone();
   }

   public void b(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      int var4 = this.O() && Objects.equals(this.n[0], var2) ? 1 : 0;
      if (var4 != 0) {
         byte var5 = 1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var3.getSlot(var6).getItem().isEmpty()) {
               var5 = 0;
            }
         }

         if (var5 == 0 || this.C(var1) || this.o && this.R.U()) {
            this.x(var3, 0, true);
            return;
         }
      }

      try {
         this.P(var1, var2, var3);
      } catch (IllegalArgumentException var7) {
         if (var4 == 0) {
            throw var7;
         }
      }

      this.x(var3, 0, true);
   }

   public void x(Minecraft var1, BlockPos var2, DispenserMenu var3, Item var4, Item var5, Item var6, int[] var7) {
      if (this.O() && Objects.equals(this.n[0], var2) && this.C(var1)) {
         this.x(var3, 0, true);
      } else {
         ItemStack[] var8 = new ItemStack[]{ItemStack.EMPTY, new ItemStack(var4), new ItemStack(var5), new ItemStack(var6)};

         for (int var9 = 0; var9 < 9; var9++) {
            ItemStack var10 = var3.getSlot(var9).getItem();
            int var11 = var7[var9];
            if (!var10.isEmpty() && var11 > 0 && var11 < 4) {
               var8[var11] = var10.copyWithCount(1);
            }
         }

         int var12 = Objects.equals(this.n[0], var2) && Arrays.equals(this.O[0], var7) ? 0 : 1;

         for (int var13 = 1; var13 <= 3; var13++) {
            var12 |= this.X[0][var13] != null && ItemStack.isSameItemSameComponents(this.X[0][var13], var8[var13]) ? 0 : 1;
         }

         this.n[0] = var2.immutable();
         this.O[0] = (int[])var7.clone();

         for (int var14 = 1; var14 <= 3; var14++) {
            this.X[0][var14] = var8[var14];
         }

         if (var12 != 0) {
            this.O(var1);
         }

         this.x(var3, 0, true);
      }
   }

   public void V() {
      this.Q = this.Q % this.t() + 1;
   }

   public String U(int var1) {
      return this.X[0][var1] == null ? "" : this.X[0][var1].getHoverName().getString().replace('\n', ' ').replace('\r', ' ');
   }

   private int t() {
      return this.Y ? 4 : (this.o ? 2 : (this.U ? 3 : 9));
   }

   public boolean C(Minecraft var1) {
      if (var1 == null || !this.O() || var1.player == null) {
         return false;
      }

      if (this.o || this.i || this.Y) {
         this.g(var1);
         return this.R.E();
      }

      if (this.X(var1)) {
         return true;
      }

      Map var2 = this.q(var1);

      for (int var3 = 0; var3 < 2; var3++) {
         if (this.g[var3] > 0 && this.k[var3] > 0 && this.g[var3] != this.k[var3]) {
            int var4 = var3 * 10 + this.g[var3];
            int var5 = var3 * 10 + this.k[var3];
            if (var2.getOrDefault(var4, 0) > this.W.getOrDefault(var4, 0) || this.V[var3] && var2.getOrDefault(var5, 0) > this.W.getOrDefault(var5, 0)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean U(int var1) {
      return this.n[var1] != null;
   }

   public boolean O() {
      return this.U(0) && (this.U || this.U(1));
   }

   public BlockPos Y(int var1) {
      return this.n[var1];
   }

   public String g(int var1) {
      return this.g[var1] == 0 ? "" : Integer.toString(this.k[var1]);
   }

   public void P(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      if (var2.equals(this.n[1 - this.Z])) {
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
               int var9 = I(var8);
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
      } else if (this.U) {
         this.Z = 0;
         ItemStack[] var12 = new ItemStack[9];
         int[] var14 = new int[9];
         int var16 = 0;

         for (int var18 = 0; var18 < 9; var18++) {
            ItemStack var10 = var3.getSlot(var18).getItem();
            if (this.i && (var10.getCount() != 1 || !BuiltInRegistries.ITEM.getKey(var10.getItem()).getPath().endsWith("_horse_armor"))) {
               throw new IllegalArgumentException("Use one horse armor per slot, three of each type.");
            }

            if (var10.isEmpty()) {
               throw new IllegalArgumentException("This game requires nine occupied slots.");
            }

            int var11 = 0;

            while (var11 < var16 && !this.c(var12[var11], var10)) {
               var11++;
            }

            if (var11 == var16) {
               var12[var16++] = var10.copyWithCount(1);
            }

            var14[var18] = var11;
         }

         var5 = this.i ? u7.S(var14) : (this.o ? un.j(var14) : un.w(var14));
         if (this.o) {
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
            int var17 = zv.K(var15.getHoverName().getString());
            String var21 = var15.getHoverName().getString();
            if (var15.isEmpty()) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + " is empty.");
            }

            if (var17 < 1) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": cannot read rank from [" + var21 + "].");
            }

            if (zv.X(var21) != (this.Z == 0 ? 1 : 0)) {
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

      this.X[this.Z] = var4;
      this.n[this.Z] = var2.immutable();
      this.O[this.Z] = var5;
      this.O(var1);
   }

   private boolean c(ItemStack var1, ItemStack var2) {
      return var1.is(var2.getItem())
         && (!this.o && !this.i && !this.Y || Objects.equals(var1.get(DataComponents.CUSTOM_NAME), var2.get(DataComponents.CUSTOM_NAME)));
   }

   private int R(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      if (this.U) {
         for (int var6 = 1; var6 <= this.t(); var6++) {
            if (this.U(0) && this.X[0][var6] != null && this.c(var1, this.X[0][var6])) {
               return var6;
            }
         }

         return -1;
      } else {
         String var2 = var1.getHoverName().getString();
         int var3 = zv.K(var2);
         if (var3 < 1) {
            return -1;
         }

         int var4 = zv.X(var2) ? 0 : 1;
         ItemStack var5 = this.X[var4][var3];
         return this.U(var4) && var5 != null && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> q(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.a) {
         this.P(var2, this.B);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.I) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.R(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu.getCarried();
      int var16 = this.R(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.R(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.C.contains(var7.getUUID())) {
               int var10 = this.R(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void P(DispenserMenu var1, int var2) {
      Map var3 = this.I.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.R(var5);
         if (k3.z(var2, var6 < 0 ? -1 : var6 / 10, false)) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void x(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.a == var1) {
         this.a = var1;
         this.B = var2;
         this.P(var1, var2);
         if (this.o || this.i || this.Y) {
            this.g(Minecraft.getInstance());
         }
      }
   }

   private void G(Minecraft var1) {
      if (var1.level != null) {
         if (this.U && this.u.p()) {
            this.y(var1, this.f(var1));
         }

         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.v.contains(var4.getUUID())) {
               int var5 = this.R(var4.getItem());
               if (var5 >= 0) {
                  if (this.U && this.k[0] > 0 && var1.player != null && var4.distanceToSqr(var1.player) <= 9.0) {
                     int var6 = this.u.f(var5, -1);
                     if (var6 > 0 || var5 == this.g[0]) {
                        this.T.put(var4.getUUID(), var6 > 0 ? var6 : this.k[0]);
                        this.C.add(var4.getUUID());
                        this.v.add(var4.getUUID());
                        continue;
                     }
                  }

                  BlockPos var10 = this.n[var5 / 10];
                  if (var10 != null) {
                     BlockState var7 = var1.level.getBlockState(var10);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var10).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.C.add(var4.getUUID());
                           this.v.add(var4.getUUID());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack V(Minecraft var1, ItemEntity var2) {
      this.z(var1);
      if (!this.o && !this.i && !this.Y) {
         if (this.U && this.T.containsKey(var2.getUUID())) {
            return this.h(var2.getItem(), this.T.get(var2.getUUID()));
         } else {
            return this.C.contains(var2.getUUID()) ? this.N(var2.getItem()) : var2.getItem();
         }
      } else {
         return this.G.contains(var2.getUUID()) ? this.h(var2.getItem(), this.R.e("world:" + var2.getUUID(), 1)) : var2.getItem();
      }
   }

   public void O(Minecraft var1) {
      this.f.g();
      this.t = (boolean)0;
      this.R.d(this.O[0]);
      this.G.clear();
      this.m.clear();
      if ((this.o || this.i || this.Y) && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            ItemStack var3 = var2 < 36
               ? var1.player.getInventory().getItem(var2)
               : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            if (this.R(var3) > 0) {
               this.m.add("player:" + var2);
            }
         }
      }

      if (this.Y && var1.level != null) {
         for (Entity var4 : var1.level.entitiesForRendering()) {
            if (var4 instanceof ArmorStand var6) {
               for (int var5 = 1; var5 <= 4; var5++) {
                  if (!var6.getItemBySlot(U(var5)).isEmpty()) {
                     this.m.add("stand:" + var6.getUUID() + ":" + var5);
                  }
               }
            }
         }
      }

      this.u.j();
      this.T.clear();
      this.y = ThreadLocalRandom.current().nextInt(2520);
      Arrays.fill(this.g, 0);
      Arrays.fill(this.k, 0);
      this.j.x();

      for (Map var10 : this.I) {
         var10.clear();
      }

      this.a = null;
      this.B = -1;
      Arrays.fill(this.V, false);
      Arrays.fill(this.b, -1);
      Arrays.fill(this.A, -1);
      this.C.clear();
      this.v.clear();
      if (var1.level != null) {
         for (Entity var11 : var1.level.entitiesForRendering()) {
            this.v.add(var11.getUUID());
         }
      }

      this.s.clear();
      if (var1.player != null) {
         this.s.putAll(this.q(var1));
      }

      this.W.clear();
      this.W.putAll(this.s);
   }

   public void z(Minecraft var1) {
      if (var1 != null && this.O() && var1.player != null && var1.level != null) {
         if (!this.o && !this.i && !this.Y) {
            this.G(var1);
            int[] var2 = (int[])this.g.clone();
            Map var3 = this.q(var1);
            if (this.j
               .l(
                  this.g[0] > 0 && (this.U || this.g[1] > 0),
                  !this.X(var1)
                     && var3.getOrDefault(this.g[0], 0) <= this.W.getOrDefault(this.g[0], 0)
                     && var3.getOrDefault(10 + this.g[1], 0) <= this.W.getOrDefault(10 + this.g[1], 0)
                     && (!this.V[0] || var3.getOrDefault(this.k[0], 0) <= this.W.getOrDefault(this.k[0], 0))
                     && (!this.V[1] || var3.getOrDefault(10 + this.k[1], 0) <= this.W.getOrDefault(10 + this.k[1], 0)),
                  var1.level.getGameTime()
               )) {
               this.O(var1);
            } else {
               for (Entry var5 : var3.entrySet()) {
                  if ((Integer)var5.getValue() > this.s.getOrDefault(var5.getKey(), 0)) {
                     int var6 = (Integer)var5.getKey() / 10;
                     int var7 = (Integer)var5.getKey() % 10;
                     if (this.o || this.i || this.Y) {
                        int var8 = un.j(this.g[var6], this.k[var6] == 0 ? this.Q : this.k[var6], var7);
                        if (var8 != this.g[var6]) {
                           this.u.j();
                           this.g[var6] = var8;
                        }
                     } else if (this.g[var6] == 0) {
                        this.g[var6] = var7;
                     }
                  }
               }

               if (this.k[0] == 0 && (this.g[0] > 0 || this.g[1] > 0)) {
                  if (this.U) {
                     this.k[0] = this.Q;
                  } else {
                     ThreadLocalRandom var9 = ThreadLocalRandom.current();
                     zv.z var11 = zv.C(this.g[0] > 0 ? this.g[0] : var9.nextInt(1, 10), this.g[1] > 0 ? this.g[1] : var9.nextInt(1, 10), this.z, var9.nextInt());
                     this.k[0] = var11.host();
                     this.k[1] = var11.viewer();
                  }
               }

               for (int var10 = 0; var10 < 2; var10++) {
                  if (this.g[var10] > 0 && var2[var10] == 0) {
                     this.E(var10, this.g[var10], this.k[var10]);
                  }
               }

               this.s.clear();
               this.s.putAll(var3);
            }
         } else {
            this.g(var1);
         }
      }
   }

   private void E(int var1, int var2, int var3) {
      if (var2 >= 1 && var3 >= 1) {
         String var4 = this.X[var1][var2].getHoverName().getString();
         String var5 = this.X[var1][var3].getHoverName().getString();
         te.Z(
            this.o ? "Russian Roulette" : "45/45/10",
            (this.U ? "" : (var1 == 0 ? "Host: " : "Viewer: ")) + "Drew " + var4 + (var2 == var3 ? "; unchanged." : "; spoofed as " + var5 + " (visual only).")
         );
      }
   }

   private void g(Minecraft var1) {
      if (this.O() && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.f(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.R(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.R(var6);
            if (var8 < 0) {
               this.m.remove(var7);
            }

            if (!this.m.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         BlockPos var15 = this.n[0];
         BlockState var16 = var1.level.getBlockState(var15);
         Vec3 var17 = Vec3.atCenterOf(var15);
         if (var16.is(Blocks.DROPPER) || var16.is(Blocks.DISPENSER)) {
            Direction var18 = (Direction)var16.getValue(DispenserBlock.FACING);
            var17 = var17.add(var18.getStepX() * 0.7, var18.getStepY() * 0.7, var18.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.R(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var17) <= 2.25 ? 1 : 0;
               if (!this.v.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.G.add(var10.getUUID());
               }

               if (this.G.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.R(var10.getItem()));
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
                     int var13 = this.R(var20.getItemBySlot(U(var26)));
                     if (var13 < 1) {
                        this.m.remove(var27);
                     }

                     if (!this.m.contains(var27)) {
                        var2.put(var27, var13);
                        var3.add(var27);
                     }
                  }
               }
            }
         }

         if (this.o && var4 != null) {
            this.R.K(var2);
         }

         if (this.i) {
            this.R.Y(var2, var3, () -> this.f.d(this.Q), this.y, var4 != null);
         } else {
            this.R.y(var2, var3, this.Q, this.y, var4 != null);
         }

         if (this.o) {
            for (int var21 = 0; var21 < 9; var21++) {
               if (this.O[0][var21] == 2) {
                  this.R.J(2, var21);
               }
            }
         }

         if (this.i && var4 != null && this.R.D()) {
            this.f.g();
         }

         for (p0.q var24 : this.R.T()) {
            if (this.o && var24.display() == 2) {
               this.t = (boolean)1;
               this.Q = 1;
            }

            this.E(0, var24.actual(), var24.display());
         }
      }
   }

   private static EquipmentSlot U(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int I(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == U(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] w(ArmorStand var1) {
      if (this.Y && this.O() && !(var1.distanceToSqr(Vec3.atCenterOf(this.n[0])) > 16.0)) {
         this.g(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(U(var3));
            int var5 = this.R.e("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = this.R(var4) > 0 ? this.h(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   private ItemStack g(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.g(var2);
      if (var2.player == null) {
         return var1;
      }

      for (int var3 = 0; var3 < 38; var3++) {
         ItemStack var4 = var3 < 36
            ? var2.player.getInventory().getItem(var3)
            : (var3 == 36 ? var2.player.containerMenu.getCarried() : var2.player.getOffhandItem());
         if (var1 == var4) {
            return this.h(var1, this.R.e("player:" + var3, this.m.contains("player:" + var3) ? this.R(var1) : (this.R(var1) > 0 ? 1 : -1)));
         }
      }

      return var1;
   }

   private void y(Minecraft var1, DispenserMenu var2) {
      if (this.U && var1.player != null && this.k[0] != 0) {
         int[] var3 = new int[47];
         int[] var4 = new int[47];
         Arrays.fill(var3, -2);
         Arrays.fill(var4, -1);

         for (int var5 = 0; var5 < 38; var5++) {
            ItemStack var6 = var5 < 36
               ? var1.player.getInventory().getItem(var5)
               : (var5 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            int var7 = this.R(var6);
            var3[var5] = var7;
            var4[var5] = var7 == this.g[0] ? this.k[0] : var7;
         }

         if (var2 != null) {
            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = this.R(var2.getSlot(var8).getItem());
               var3[38 + var8] = var9;
               var4[38 + var8] = var9;
               if (this.V[0] && (var8 == this.b[0] || var8 == this.A[0])) {
                  var4[38 + var8] = var9 == this.g[0] ? this.k[0] : (var9 == this.k[0] ? this.g[0] : var9);
               }
            }
         }

         this.u.P(var3, var4);
      }
   }

   private boolean X(Minecraft var1) {
      if (this.U && !this.o && !this.i && !this.Y && this.V[0] && this.u.p()) {
         DispenserMenu var2 = this.f(var1);
         if (var2 != null) {
            this.y(var1, var2);
         }

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = k3.f(var3, this.b[0], this.A[0], true);
            if (this.u.p(38 + var4) != this.O[0][var3]) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private DispenserMenu f(Minecraft var1) {
      return var1.player != null && var1.player.containerMenu == this.a && this.B == 0 ? this.a : null;
   }

   private ItemStack h(ItemStack var1, int var2) {
      return var2 > 0 && var2 <= this.t() && this.X[0][var2] != null ? this.F(this.X[0][var2], var1.getCount()) : var1;
   }

   public ItemStack N(ItemStack var1) {
      if (this.h.contains(var1)) {
         return var1;
      }

      if (!this.o && !this.i && !this.Y) {
         if (this.U && this.u.p()) {
            Minecraft var2 = Minecraft.getInstance();
            this.y(var2, this.f(var2));
            if (var2.player != null) {
               for (int var3 = 0; var3 < 36; var3++) {
                  if (var1 == var2.player.getInventory().getItem(var3)) {
                     return this.h(var1, this.u.p(var3));
                  }
               }

               if (var1 == var2.player.containerMenu.getCarried()) {
                  return this.h(var1, this.u.p(36));
               }

               if (var1 == var2.player.getOffhandItem()) {
                  return this.h(var1, this.u.p(37));
               }
            }
         }

         int var5 = this.R(var1);
         if (var5 < 0) {
            return var1;
         } else {
            int var6 = var5 / 10;
            int var4 = var5 % 10;
            if (!this.U && this.V[var6] && var4 == this.k[var6]) {
               return this.F(this.X[var6][this.g[var6]], var1.getCount());
            } else {
               return this.g[var6] == var4 && this.k[var6] != 0 && this.k[var6] != var4 ? this.F(this.X[var6][this.k[var6]], var1.getCount()) : var1;
            }
         }
      } else {
         return this.g(var1);
      }
   }

   public ItemStack U(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.G(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.F(var5, var5.getCount());
   }

   private ItemStack G(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      if (!this.o && !this.i && !this.Y) {
         ItemStack var18 = this.e(var5, var3, false);
         int var19 = this.g[var3];
         int var8 = this.k[var3];
         if (var19 != 0 && var8 != 0 && var19 != var8) {
            this.d(var1, var2, var3);
            if (this.U && this.V[var3]) {
               this.y(var1, var2);
               int var21 = k3.f(var4, this.b[var3], this.A[var3], true);
               return this.h(var2.getSlot(var21).getItem(), this.u.p(38 + var21));
            }

            if (this.V[var3]) {
               int var20 = k3.f(var4, this.b[var3], this.A[var3], true);
               ItemStack var22 = var2.getSlot(var20).getItem();
               return this.e(var22, var3, var20 != var4);
            }

            int var9 = this.q(var1).getOrDefault(var3 * 10 + var19, 0) > this.W.getOrDefault(var3 * 10 + var19, 0) ? 1 : 0;
            int var10 = -1;
            int var11 = -1;
            int var12 = -1;
            int var13 = 0;
            int[] var14 = new int[9];

            for (int var15 = 0; var15 < 9; var15++) {
               ItemStack var16 = var2.getSlot(var15).getItem();
               var14[var15] = var16.getCount();
               int var17 = this.R(var16);
               if (var17 == var3 * 10 + var19) {
                  var10 = var15;
               }

               if (var17 == var3 * 10 + var8) {
                  var11 = var15;
               }

               if (var16.isEmpty()) {
                  var12 = var15;
                  var13++;
               } else if (var17 / 10 == var3 && var17 > 0 && this.O[var3][var15] == 0) {
                  this.O[var3][var15] = var17 % 10;
               }
            }

            if (var10 < 0) {
               for (int var23 = 0; var23 < 9; var23++) {
                  if (this.O[var3][var23] == var19 && var2.getSlot(var23).getItem().isEmpty()) {
                     var10 = var23;
                     break;
                  }
               }

               if (var10 < 0 && var13 == 1) {
                  var10 = var12;
               }
            }

            if (this.U) {
               var11 = this.U(var2, var8);
            }

            if (var10 >= 0 && var14[var10] >= this.X[var3][var19].getMaxStackSize()) {
               return var18;
            } else {
               int[] var24 = mi.N(var14, var10, var11, (boolean)var9);
               if (var24[var4] == var14[var4]) {
                  return var18;
               } else {
                  return var24[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.X[var3][var19] : var5).copyWithCount(var24[var4]);
               }
            }
         } else {
            return var18;
         }
      } else {
         this.g(var1);
         int var6 = this.R.J(var4);
         ItemStack var7 = var2.getSlot(var6).getItem();
         return this.h(var7, this.R.e("stock:" + var6, this.R(var7) > 0 ? 1 : -1));
      }
   }

   private ItemStack e(ItemStack var1, int var2, boolean var3) {
      int var4 = this.R(var1);
      if (!this.U && var4 >= 0 && this.V[var4 / 10]) {
         return this.N(var1);
      } else {
         return k3.z(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.N(var1) : var1;
      }
   }

   private int U(DispenserMenu var1, int var2) {
      int[] var3 = new int[9];

      for (int var4 = 0; var4 < 9; var4++) {
         var3[var4] = this.R(var1.getSlot(var4).getItem());
      }

      return un.w(var3, var2, this.y);
   }

   private void d(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.V[var3] && this.g[var3] != 0 && this.k[var3] != 0 && this.g[var3] != this.k[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.R(var2.getSlot(var8).getItem());
            if (this.O[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.O[var3][var8] = var9 % 10;
            }

            if (this.O[var3][var8] == this.g[var3] && (var4 < 0 || var2.getSlot(var8).getItem().isEmpty())) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.k[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (this.U) {
            var5 = this.U(var2, this.k[var3]);
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.O[var3][var4] = this.g[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.R(var2.getSlot(var4).getItem()) != var3 * 10 + this.g[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.q(var1).getOrDefault(var3 * 10 + this.g[var3], 0) > this.W.getOrDefault(var3 * 10 + this.g[var3], 0)) {
               this.b[var3] = var4;
               this.A[var3] = var5;
               this.V[var3] = (boolean)1;
            }
         }
      }
   }

   public int l(Minecraft var1, int var2) {
      this.g(var1);
      return this.R.q("player:" + var2);
   }

   public void q(DispenserMenu var1, int var2) {
      int var3 = this.R.e("player:" + var2, -1);

      for (int var4 = 0; var4 < 9; var4++) {
         if ((!this.o || (this.O[0][var4] == 2 ? 1 : 0) == (var3 == 2 ? 1 : 0)) && var1.getSlot(this.R.J(var4)).getItem().isEmpty()) {
            this.R.V("player:" + var2, var4);
            return;
         }
      }
   }

   public boolean i(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (this.U && !this.o && !this.i && !this.Y && this.V[0]) {
         ItemStack var5 = var4 == 36 ? var2.getCarried() : var1.player.getInventory().getItem(var4);
         ItemStack var6 = var2.getSlot(var3).getItem();
         if (var6.getCount() == 1 && var6.getMaxStackSize() == 1 && ItemStack.matches(var6, var5)) {
            this.y(var1, var2);
            return this.u.c(38 + var3, var4);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int V(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (!this.o && !this.i && !this.Y) {
         this.d(var1, var2, var3);
         if (this.U && this.V[var3]) {
            this.y(var1, var2);
         }

         return k3.f(var4, this.b[var3], this.A[var3], this.V[var3]);
      } else {
         this.g(var1);
         return this.R.J(var4);
      }
   }
}

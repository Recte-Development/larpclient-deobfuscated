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

final class k2 {
   private final Set<ItemStack> Y = Collections.newSetFromMap(new WeakHashMap<>());
   private final dw l = new dw();
   private final Set<UUID> d = new HashSet<>();
   private final Set<String> n = new HashSet<>();
   private final pm u = new pm();
   private final boolean G;
   private final boolean m;
   private boolean P;
   private boolean W;
   private boolean f;
   private final tz B = new tz();
   public int b = 1;
   private int E;
   private final BlockPos[] N = new BlockPos[2];
   private final ItemStack[][] F = new ItemStack[2][10];
   private final int[][] h = new int[2][9];
   private final int[] e = new int[]{0, 0};
   private final int[] w = new int[]{0, 0};
   private final boolean[] y = new boolean[]{false, false};
   private final int[] i = new int[]{-1, -1};
   private final int[] A = new int[]{-1, -1};
   private final Map<UUID, Integer> Q = new HashMap<>();
   private final Set<UUID> v = new HashSet<>();
   private final Set<UUID> k = new HashSet<>();
   private final h6 U = new h6();
   private final List<Map<Integer, Integer>> C = List.of(new HashMap<>(), new HashMap<>());
   private DispenserMenu z;
   private int c = -1;
   private final Map<Integer, Integer> q = new HashMap<>();
   private final Map<Integer, Integer> I = new HashMap<>();
   public int H;
   public boolean K = (boolean)1;

   private ItemStack G(ItemStack var1, int var2) {
      ItemStack var3 = var1.copyWithCount(var2);
      this.Y.add(var3);
      return var3;
   }

   public boolean W() {
      boolean var1 = this.P;
      this.P = (boolean)0;
      return var1;
   }

   public static k2 C() {
      k2 var0 = new k2(true);
      var0.f = (boolean)1;
      return var0;
   }

   public static k2 B() {
      k2 var0 = new k2(true);
      var0.W = (boolean)1;
      return var0;
   }

   public k2() {
      this(false);
   }

   public k2(boolean var1) {
      this(var1, false);
   }

   public k2(boolean var1, boolean var2) {
      this.G = var1;
      this.m = var2;
   }

   public ItemStack Q(int var1) {
      return this.F[0][var1] == null ? ItemStack.EMPTY : this.F[0][var1].copy();
   }

   public int[] D() {
      return (int[])this.h[0].clone();
   }

   public void e(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      int var4 = this.X() && Objects.equals(this.N[0], var2) ? 1 : 0;
      if (var4 != 0) {
         byte var5 = 1;

         for (int var6 = 0; var6 < 9; var6++) {
            if (var3.getSlot(var6).getItem().isEmpty()) {
               var5 = 0;
            }
         }

         if (var5 == 0 || this.x(var1) || this.m && this.l.R()) {
            this.Y(var3, 0, true);
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

      this.Y(var3, 0, true);
   }

   public void f(Minecraft var1, BlockPos var2, DispenserMenu var3, Item var4, Item var5, Item var6, int[] var7) {
      if (this.X() && Objects.equals(this.N[0], var2) && this.x(var1)) {
         this.Y(var3, 0, true);
      } else {
         ItemStack[] var8 = new ItemStack[]{ItemStack.EMPTY, new ItemStack(var4), new ItemStack(var5), new ItemStack(var6)};

         for (int var9 = 0; var9 < 9; var9++) {
            ItemStack var10 = var3.getSlot(var9).getItem();
            int var11 = var7[var9];
            if (!var10.isEmpty() && var11 > 0 && var11 < 4) {
               var8[var11] = var10.copyWithCount(1);
            }
         }

         int var12 = Objects.equals(this.N[0], var2) && Arrays.equals(this.h[0], var7) ? 0 : 1;

         for (int var13 = 1; var13 <= 3; var13++) {
            var12 |= this.F[0][var13] != null && ItemStack.isSameItemSameComponents(this.F[0][var13], var8[var13]) ? 0 : 1;
         }

         this.N[0] = var2.immutable();
         this.h[0] = (int[])var7.clone();

         for (int var14 = 1; var14 <= 3; var14++) {
            this.F[0][var14] = var8[var14];
         }

         if (var12 != 0) {
            this.I(var1);
         }

         this.Y(var3, 0, true);
      }
   }

   public void d() {
      this.b = this.b % this.x() + 1;
   }

   public String L(int var1) {
      return this.F[0][var1] == null ? "" : this.F[0][var1].getHoverName().getString().replace('\n', ' ').replace('\r', ' ');
   }

   private int x() {
      return this.f ? 4 : (this.m ? 2 : (this.G ? 3 : 9));
   }

   public boolean x(Minecraft var1) {
      if (var1 == null || !this.X() || var1.player == null) {
         return false;
      }

      if (this.m || this.W || this.f) {
         this.j(var1);
         return this.l.h();
      }

      if (this.k(var1)) {
         return true;
      }

      Map var2 = this.a(var1);

      for (int var3 = 0; var3 < 2; var3++) {
         if (this.e[var3] > 0 && this.w[var3] > 0 && this.e[var3] != this.w[var3]) {
            int var4 = var3 * 10 + this.e[var3];
            int var5 = var3 * 10 + this.w[var3];
            if (var2.getOrDefault(var4, 0) > this.I.getOrDefault(var4, 0) || this.y[var3] && var2.getOrDefault(var5, 0) > this.I.getOrDefault(var5, 0)) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean h(int var1) {
      return this.N[var1] != null;
   }

   public boolean X() {
      return this.h(0) && (this.G || this.h(1));
   }

   public BlockPos X(int var1) {
      return this.N[var1];
   }

   public String W(int var1) {
      return this.e[var1] == 0 ? "" : Integer.toString(this.w[var1]);
   }

   public void d(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      if (var2.equals(this.N[1 - this.H])) {
         throw new IllegalArgumentException("Host and Viewer need different droppers.");
      }

      ItemStack[] var4 = new ItemStack[10];
      int[] var5 = new int[9];
      if (this.f) {
         this.H = 0;
         int var6 = 0;

         for (int var7 = 0; var7 < 9; var7++) {
            ItemStack var8 = var3.getSlot(var7).getItem();
            if (!var8.isEmpty()) {
               int var9 = g(var8);
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
      } else if (this.G) {
         this.H = 0;
         ItemStack[] var12 = new ItemStack[9];
         int[] var14 = new int[9];
         int var16 = 0;

         for (int var18 = 0; var18 < 9; var18++) {
            ItemStack var10 = var3.getSlot(var18).getItem();
            if (this.W && (var10.getCount() != 1 || !BuiltInRegistries.ITEM.getKey(var10.getItem()).getPath().endsWith("_horse_armor"))) {
               throw new IllegalArgumentException("Use one horse armor per slot, three of each type.");
            }

            if (var10.isEmpty()) {
               throw new IllegalArgumentException("This game requires nine occupied slots.");
            }

            int var11 = 0;

            while (var11 < var16 && !this.Z(var12[var11], var10)) {
               var11++;
            }

            if (var11 == var16) {
               var12[var16++] = var10.copyWithCount(1);
            }

            var14[var18] = var11;
         }

         var5 = this.W ? tz.l(var14) : (this.m ? ps.v(var14) : ps.Y(var14));
         if (this.m) {
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
            int var17 = ol.N(var15.getHoverName().getString());
            String var21 = var15.getHoverName().getString();
            if (var15.isEmpty()) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + " is empty.");
            }

            if (var17 < 1) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": cannot read rank from [" + var21 + "].");
            }

            if (ol.U(var21) != (this.H == 0 ? 1 : 0)) {
               throw new IllegalArgumentException(
                  "Paper Game slot " + (var13 + 1) + ": [" + var21 + "] belongs to the " + (this.H == 0 ? "Viewer" : "Host") + " side; change the setup side."
               );
            }

            if (var4[var17] != null) {
               throw new IllegalArgumentException("Paper Game slot " + (var13 + 1) + ": duplicate rank " + var17 + " in [" + var21 + "].");
            }

            var4[var17] = var15.copyWithCount(1);
            var5[var13] = var17;
         }
      }

      this.F[this.H] = var4;
      this.N[this.H] = var2.immutable();
      this.h[this.H] = var5;
      this.I(var1);
   }

   private boolean Z(ItemStack var1, ItemStack var2) {
      return var1.is(var2.getItem())
         && (!this.m && !this.W && !this.f || Objects.equals(var1.get(DataComponents.CUSTOM_NAME), var2.get(DataComponents.CUSTOM_NAME)));
   }

   private int a(ItemStack var1) {
      if (var1.isEmpty()) {
         return -1;
      }

      if (this.G) {
         for (int var6 = 1; var6 <= this.x(); var6++) {
            if (this.h(0) && this.F[0][var6] != null && this.Z(var1, this.F[0][var6])) {
               return var6;
            }
         }

         return -1;
      } else {
         String var2 = var1.getHoverName().getString();
         int var3 = ol.N(var2);
         if (var3 < 1) {
            return -1;
         }

         int var4 = ol.U(var2) ? 0 : 1;
         ItemStack var5 = this.F[var4][var3];
         return this.h(var4) && var5 != null && var1.is(var5.getItem()) ? var4 * 10 + var3 : -1;
      }
   }

   private Map<Integer, Integer> a(Minecraft var1) {
      if (var1.player.containerMenu instanceof DispenserMenu var2 && var2 == this.z) {
         this.U(var2, this.c);
      }

      HashMap var11 = new HashMap();

      for (Map var4 : this.C) {
         var4.forEach((var1x, var2x) -> var11.merge(var1x, var2x, Integer::sum));
      }

      for (int var13 = 0; var13 < 36; var13++) {
         ItemStack var15 = var1.player.getInventory().getItem(var13);
         int var5 = this.a(var15);
         if (var5 >= 0) {
            var11.merge(var5, var15.getCount(), Integer::sum);
         }
      }

      ItemStack var14 = var1.player.containerMenu.getCarried();
      int var16 = this.a(var14);
      if (var16 >= 0) {
         var11.merge(var16, var14.getCount(), Integer::sum);
      }

      ItemStack var17 = var1.player.getOffhandItem();
      int var6 = this.a(var17);
      if (var6 >= 0) {
         var11.merge(var6, var17.getCount(), Integer::sum);
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var7 && !var7.isRemoved() && this.k.contains(var7.getUUID())) {
               int var10 = this.a(var7.getItem());
               if (var10 >= 0) {
                  var11.merge(var10, var7.getItem().getCount(), Integer::sum);
               }
            }
         }
      }

      return var11;
   }

   private void U(DispenserMenu var1, int var2) {
      Map var3 = this.C.get(var2);
      var3.clear();

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         int var6 = this.a(var5);
         if (tv.h(var2, var6 < 0 ? -1 : var6 / 10, false)) {
            var3.merge(var6, var5.getCount(), Integer::sum);
         }
      }
   }

   public void Y(DispenserMenu var1, int var2, boolean var3) {
      if (var3 || this.z == var1) {
         this.z = var1;
         this.c = var2;
         this.U(var1, var2);
         if (this.m || this.W || this.f) {
            this.j(Minecraft.getInstance());
         }
      }
   }

   private void v(Minecraft var1) {
      if (var1.level != null) {
         if (this.G && this.u.f()) {
            this.q(var1, this.b(var1));
         }

         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4 && !var4.isRemoved() && !this.v.contains(var4.getUUID())) {
               int var5 = this.a(var4.getItem());
               if (var5 >= 0) {
                  if (this.G && this.w[0] > 0 && var1.player != null && var4.distanceToSqr(var1.player) <= 9.0) {
                     int var6 = this.u.v(var5, -1);
                     if (var6 > 0 || var5 == this.e[0]) {
                        this.Q.put(var4.getUUID(), var6 > 0 ? var6 : this.w[0]);
                        this.k.add(var4.getUUID());
                        this.v.add(var4.getUUID());
                        continue;
                     }
                  }

                  BlockPos var10 = this.N[var5 / 10];
                  if (var10 != null) {
                     BlockState var7 = var1.level.getBlockState(var10);
                     if (var7.is(Blocks.DROPPER) || var7.is(Blocks.DISPENSER)) {
                        Direction var8 = (Direction)var7.getValue(DispenserBlock.FACING);
                        Vec3 var9 = Vec3.atCenterOf(var10).add(var8.getStepX() * 0.7, var8.getStepY() * 0.7, var8.getStepZ() * 0.7);
                        if (var4.distanceToSqr(var9) <= 2.25) {
                           this.k.add(var4.getUUID());
                           this.v.add(var4.getUUID());
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public ItemStack A(Minecraft var1, ItemEntity var2) {
      this.C(var1);
      if (!this.m && !this.W && !this.f) {
         if (this.G && this.Q.containsKey(var2.getUUID())) {
            return this.R(var2.getItem(), this.Q.get(var2.getUUID()));
         } else {
            return this.k.contains(var2.getUUID()) ? this.u(var2.getItem()) : var2.getItem();
         }
      } else {
         return this.d.contains(var2.getUUID()) ? this.R(var2.getItem(), this.l.X("world:" + var2.getUUID(), 1)) : var2.getItem();
      }
   }

   public void I(Minecraft var1) {
      this.B.c();
      this.P = (boolean)0;
      this.l.h(this.h[0]);
      this.d.clear();
      this.n.clear();
      if ((this.m || this.W || this.f) && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            ItemStack var3 = var2 < 36
               ? var1.player.getInventory().getItem(var2)
               : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            if (this.a(var3) > 0) {
               this.n.add("player:" + var2);
            }
         }
      }

      if (this.f && var1.level != null) {
         for (Entity var4 : var1.level.entitiesForRendering()) {
            if (var4 instanceof ArmorStand var6) {
               for (int var5 = 1; var5 <= 4; var5++) {
                  if (!var6.getItemBySlot(g(var5)).isEmpty()) {
                     this.n.add("stand:" + var6.getUUID() + ":" + var5);
                  }
               }
            }
         }
      }

      this.u.c();
      this.Q.clear();
      this.E = ThreadLocalRandom.current().nextInt(2520);
      Arrays.fill(this.e, 0);
      Arrays.fill(this.w, 0);
      this.U.Q();

      for (Map var10 : this.C) {
         var10.clear();
      }

      this.z = null;
      this.c = -1;
      Arrays.fill(this.y, false);
      Arrays.fill(this.i, -1);
      Arrays.fill(this.A, -1);
      this.k.clear();
      this.v.clear();
      if (var1.level != null) {
         for (Entity var11 : var1.level.entitiesForRendering()) {
            this.v.add(var11.getUUID());
         }
      }

      this.q.clear();
      if (var1.player != null) {
         this.q.putAll(this.a(var1));
      }

      this.I.clear();
      this.I.putAll(this.q);
   }

   public void C(Minecraft var1) {
      if (var1 != null && this.X() && var1.player != null && var1.level != null) {
         if (!this.m && !this.W && !this.f) {
            this.v(var1);
            int[] var2 = (int[])this.e.clone();
            Map var3 = this.a(var1);
            if (this.U
               .x(
                  this.e[0] > 0 && (this.G || this.e[1] > 0),
                  !this.k(var1)
                     && var3.getOrDefault(this.e[0], 0) <= this.I.getOrDefault(this.e[0], 0)
                     && var3.getOrDefault(10 + this.e[1], 0) <= this.I.getOrDefault(10 + this.e[1], 0)
                     && (!this.y[0] || var3.getOrDefault(this.w[0], 0) <= this.I.getOrDefault(this.w[0], 0))
                     && (!this.y[1] || var3.getOrDefault(10 + this.w[1], 0) <= this.I.getOrDefault(10 + this.w[1], 0)),
                  var1.level.getGameTime()
               )) {
               this.I(var1);
            } else {
               for (Entry var5 : var3.entrySet()) {
                  if ((Integer)var5.getValue() > this.q.getOrDefault(var5.getKey(), 0)) {
                     int var6 = (Integer)var5.getKey() / 10;
                     int var7 = (Integer)var5.getKey() % 10;
                     if (this.m || this.W || this.f) {
                        int var8 = ps.K(this.e[var6], this.w[var6] == 0 ? this.b : this.w[var6], var7);
                        if (var8 != this.e[var6]) {
                           this.u.c();
                           this.e[var6] = var8;
                        }
                     } else if (this.e[var6] == 0) {
                        this.e[var6] = var7;
                     }
                  }
               }

               if (this.w[0] == 0 && (this.e[0] > 0 || this.e[1] > 0)) {
                  if (this.G) {
                     this.w[0] = this.b;
                  } else {
                     ThreadLocalRandom var9 = ThreadLocalRandom.current();
                     ol.e var11 = ol.n(this.e[0] > 0 ? this.e[0] : var9.nextInt(1, 10), this.e[1] > 0 ? this.e[1] : var9.nextInt(1, 10), this.K, var9.nextInt());
                     this.w[0] = var11.host();
                     this.w[1] = var11.viewer();
                  }
               }

               for (int var10 = 0; var10 < 2; var10++) {
                  if (this.e[var10] > 0 && var2[var10] == 0) {
                     this.a(var10, this.e[var10], this.w[var10]);
                  }
               }

               this.q.clear();
               this.q.putAll(var3);
            }
         } else {
            this.j(var1);
         }
      }
   }

   private void a(int var1, int var2, int var3) {
      if (var2 >= 1 && var3 >= 1) {
         String var4 = this.F[var1][var2].getHoverName().getString();
         String var5 = this.F[var1][var3].getHoverName().getString();
         xa.q(
            this.m ? "Russian Roulette" : "45/45/10",
            (this.G ? "" : (var1 == 0 ? "Host: " : "Viewer: ")) + "Drew " + var4 + (var2 == var3 ? "; unchanged." : "; spoofed as " + var5 + " (visual only).")
         );
      }
   }

   private void j(Minecraft var1) {
      if (this.X() && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.b(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.a(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.a(var6);
            if (var8 < 0) {
               this.n.remove(var7);
            }

            if (!this.n.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         BlockPos var15 = this.N[0];
         BlockState var16 = var1.level.getBlockState(var15);
         Vec3 var17 = Vec3.atCenterOf(var15);
         if (var16.is(Blocks.DROPPER) || var16.is(Blocks.DISPENSER)) {
            Direction var18 = (Direction)var16.getValue(DispenserBlock.FACING);
            var17 = var17.add(var18.getStepX() * 0.7, var18.getStepY() * 0.7, var18.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.a(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var17) <= 2.25 ? 1 : 0;
               if (!this.v.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.d.add(var10.getUUID());
               }

               if (this.d.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.a(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (this.f) {
            for (Entity var25 : var1.level.entitiesForRendering()) {
               if (var25 instanceof ArmorStand var20 && var20.distanceToSqr(var17) < 9.0) {
                  for (int var26 = 1; var26 <= 4; var26++) {
                     String var27 = "stand:" + var20.getUUID() + ":" + var26;
                     int var13 = this.a(var20.getItemBySlot(g(var26)));
                     if (var13 < 1) {
                        this.n.remove(var27);
                     }

                     if (!this.n.contains(var27)) {
                        var2.put(var27, var13);
                        var3.add(var27);
                     }
                  }
               }
            }
         }

         if (this.m && var4 != null) {
            this.l.D(var2);
         }

         if (this.W) {
            this.l.z(var2, var3, () -> this.B.V(this.b), this.E, var4 != null);
         } else {
            this.l.t(var2, var3, this.b, this.E, var4 != null);
         }

         if (this.m) {
            for (int var21 = 0; var21 < 9; var21++) {
               if (this.h[0][var21] == 2) {
                  this.l.u(2, var21);
               }
            }
         }

         if (this.W && var4 != null && this.l.S()) {
            this.B.c();
         }

         for (dw.t var24 : this.l.B()) {
            if (this.m && var24.display() == 2) {
               this.P = (boolean)1;
               this.b = 1;
            }

            this.a(0, var24.actual(), var24.display());
         }
      }
   }

   private static EquipmentSlot g(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int g(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == g(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] k(ArmorStand var1) {
      if (this.f && this.X() && !(var1.distanceToSqr(Vec3.atCenterOf(this.N[0])) > 16.0)) {
         this.j(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(g(var3));
            int var5 = this.l.X("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = this.a(var4) > 0 ? this.R(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   private ItemStack y(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.j(var2);
      if (var2.player == null) {
         return var1;
      }

      for (int var3 = 0; var3 < 38; var3++) {
         ItemStack var4 = var3 < 36
            ? var2.player.getInventory().getItem(var3)
            : (var3 == 36 ? var2.player.containerMenu.getCarried() : var2.player.getOffhandItem());
         if (var1 == var4) {
            return this.R(var1, this.l.X("player:" + var3, this.n.contains("player:" + var3) ? this.a(var1) : (this.a(var1) > 0 ? 1 : -1)));
         }
      }

      return var1;
   }

   private void q(Minecraft var1, DispenserMenu var2) {
      if (this.G && var1.player != null && this.w[0] != 0) {
         int[] var3 = new int[47];
         int[] var4 = new int[47];
         Arrays.fill(var3, -2);
         Arrays.fill(var4, -1);

         for (int var5 = 0; var5 < 38; var5++) {
            ItemStack var6 = var5 < 36
               ? var1.player.getInventory().getItem(var5)
               : (var5 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            int var7 = this.a(var6);
            var3[var5] = var7;
            var4[var5] = var7 == this.e[0] ? this.w[0] : var7;
         }

         if (var2 != null) {
            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = this.a(var2.getSlot(var8).getItem());
               var3[38 + var8] = var9;
               var4[38 + var8] = var9;
               if (this.y[0] && (var8 == this.i[0] || var8 == this.A[0])) {
                  var4[38 + var8] = var9 == this.e[0] ? this.w[0] : (var9 == this.w[0] ? this.e[0] : var9);
               }
            }
         }

         this.u.O(var3, var4);
      }
   }

   private boolean k(Minecraft var1) {
      if (this.G && !this.m && !this.W && !this.f && this.y[0] && this.u.f()) {
         DispenserMenu var2 = this.b(var1);
         if (var2 != null) {
            this.q(var1, var2);
         }

         for (int var3 = 0; var3 < 9; var3++) {
            int var4 = tv.V(var3, this.i[0], this.A[0], true);
            if (this.u.X(38 + var4) != this.h[0][var3]) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private DispenserMenu b(Minecraft var1) {
      return var1.player != null && var1.player.containerMenu == this.z && this.c == 0 ? this.z : null;
   }

   private ItemStack R(ItemStack var1, int var2) {
      return var2 > 0 && var2 <= this.x() && this.F[0][var2] != null ? this.G(this.F[0][var2], var1.getCount()) : var1;
   }

   public ItemStack u(ItemStack var1) {
      if (this.Y.contains(var1)) {
         return var1;
      }

      if (!this.m && !this.W && !this.f) {
         if (this.G && this.u.f()) {
            Minecraft var2 = Minecraft.getInstance();
            this.q(var2, this.b(var2));
            if (var2.player != null) {
               for (int var3 = 0; var3 < 36; var3++) {
                  if (var1 == var2.player.getInventory().getItem(var3)) {
                     return this.R(var1, this.u.X(var3));
                  }
               }

               if (var1 == var2.player.containerMenu.getCarried()) {
                  return this.R(var1, this.u.X(36));
               }

               if (var1 == var2.player.getOffhandItem()) {
                  return this.R(var1, this.u.X(37));
               }
            }
         }

         int var5 = this.a(var1);
         if (var5 < 0) {
            return var1;
         } else {
            int var6 = var5 / 10;
            int var4 = var5 % 10;
            if (!this.G && this.y[var6] && var4 == this.w[var6]) {
               return this.G(this.F[var6][this.e[var6]], var1.getCount());
            } else {
               return this.e[var6] == var4 && this.w[var6] != 0 && this.w[var6] != var4 ? this.G(this.F[var6][this.w[var6]], var1.getCount()) : var1;
            }
         }
      } else {
         return this.y(var1);
      }
   }

   public ItemStack W(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = this.y(var1, var2, var3, var4);
      return var5.isEmpty() ? var5 : this.G(var5, var5.getCount());
   }

   private ItemStack y(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      ItemStack var5 = var2.getSlot(var4).getItem();
      if (!this.m && !this.W && !this.f) {
         ItemStack var18 = this.J(var5, var3, false);
         int var19 = this.e[var3];
         int var8 = this.w[var3];
         if (var19 != 0 && var8 != 0 && var19 != var8) {
            this.x(var1, var2, var3);
            if (this.G && this.y[var3]) {
               this.q(var1, var2);
               int var21 = tv.V(var4, this.i[var3], this.A[var3], true);
               return this.R(var2.getSlot(var21).getItem(), this.u.X(38 + var21));
            }

            if (this.y[var3]) {
               int var20 = tv.V(var4, this.i[var3], this.A[var3], true);
               ItemStack var22 = var2.getSlot(var20).getItem();
               return this.J(var22, var3, var20 != var4);
            }

            int var9 = this.a(var1).getOrDefault(var3 * 10 + var19, 0) > this.I.getOrDefault(var3 * 10 + var19, 0) ? 1 : 0;
            int var10 = -1;
            int var11 = -1;
            int var12 = -1;
            int var13 = 0;
            int[] var14 = new int[9];

            for (int var15 = 0; var15 < 9; var15++) {
               ItemStack var16 = var2.getSlot(var15).getItem();
               var14[var15] = var16.getCount();
               int var17 = this.a(var16);
               if (var17 == var3 * 10 + var19) {
                  var10 = var15;
               }

               if (var17 == var3 * 10 + var8) {
                  var11 = var15;
               }

               if (var16.isEmpty()) {
                  var12 = var15;
                  var13++;
               } else if (var17 / 10 == var3 && var17 > 0 && this.h[var3][var15] == 0) {
                  this.h[var3][var15] = var17 % 10;
               }
            }

            if (var10 < 0) {
               for (int var23 = 0; var23 < 9; var23++) {
                  if (this.h[var3][var23] == var19 && var2.getSlot(var23).getItem().isEmpty()) {
                     var10 = var23;
                     break;
                  }
               }

               if (var10 < 0 && var13 == 1) {
                  var10 = var12;
               }
            }

            if (this.G) {
               var11 = this.L(var2, var8);
            }

            if (var10 >= 0 && var14[var10] >= this.F[var3][var19].getMaxStackSize()) {
               return var18;
            } else {
               int[] var24 = kn.q(var14, var10, var11, (boolean)var9);
               if (var24[var4] == var14[var4]) {
                  return var18;
               } else {
                  return var24[var4] == 0 ? ItemStack.EMPTY : (var5.isEmpty() ? this.F[var3][var19] : var5).copyWithCount(var24[var4]);
               }
            }
         } else {
            return var18;
         }
      } else {
         this.j(var1);
         int var6 = this.l.h(var4);
         ItemStack var7 = var2.getSlot(var6).getItem();
         return this.R(var7, this.l.X("stock:" + var6, this.a(var7) > 0 ? 1 : -1));
      }
   }

   private ItemStack J(ItemStack var1, int var2, boolean var3) {
      int var4 = this.a(var1);
      if (!this.G && var4 >= 0 && this.y[var4 / 10]) {
         return this.u(var1);
      } else {
         return tv.h(var2, var4 < 0 ? -1 : var4 / 10, var3) ? this.u(var1) : var1;
      }
   }

   private int L(DispenserMenu var1, int var2) {
      int[] var3 = new int[9];

      for (int var4 = 0; var4 < 9; var4++) {
         var3[var4] = this.a(var1.getSlot(var4).getItem());
      }

      return ps.e(var3, var2, this.E);
   }

   private void x(Minecraft var1, DispenserMenu var2, int var3) {
      if (!this.y[var3] && this.e[var3] != 0 && this.w[var3] != 0 && this.e[var3] != this.w[var3]) {
         int var4 = -1;
         int var5 = -1;
         int var6 = -1;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            int var9 = this.a(var2.getSlot(var8).getItem());
            if (this.h[var3][var8] == 0 && var9 > 0 && var9 / 10 == var3) {
               this.h[var3][var8] = var9 % 10;
            }

            if (this.h[var3][var8] == this.e[var3] && (var4 < 0 || var2.getSlot(var8).getItem().isEmpty())) {
               var4 = var8;
            }

            if (var9 == var3 * 10 + this.w[var3]) {
               var5 = var8;
            }

            if (var2.getSlot(var8).getItem().isEmpty()) {
               var6 = var8;
               var7++;
            }
         }

         if (this.G) {
            var5 = this.L(var2, this.w[var3]);
         }

         if (var4 < 0 && var7 == 1) {
            var4 = var6;
            this.h[var3][var4] = this.e[var3];
         }

         if (var4 >= 0 && var5 >= 0 && var4 != var5) {
            if (this.a(var2.getSlot(var4).getItem()) != var3 * 10 + this.e[var3]
               && var2.getSlot(var5).getItem().getCount() == 1
               && this.a(var1).getOrDefault(var3 * 10 + this.e[var3], 0) > this.I.getOrDefault(var3 * 10 + this.e[var3], 0)) {
               this.i[var3] = var4;
               this.A[var3] = var5;
               this.y[var3] = (boolean)1;
            }
         }
      }
   }

   public int Z(Minecraft var1, int var2) {
      this.j(var1);
      return this.l.c("player:" + var2);
   }

   public void P(DispenserMenu var1, int var2) {
      int var3 = this.l.X("player:" + var2, -1);

      for (int var4 = 0; var4 < 9; var4++) {
         if ((!this.m || (this.h[0][var4] == 2 ? 1 : 0) == (var3 == 2 ? 1 : 0)) && var1.getSlot(this.l.h(var4)).getItem().isEmpty()) {
            this.l.B("player:" + var2, var4);
            return;
         }
      }
   }

   public boolean n(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (this.G && !this.m && !this.W && !this.f && this.y[0]) {
         ItemStack var5 = var4 == 36 ? var2.getCarried() : var1.player.getInventory().getItem(var4);
         ItemStack var6 = var2.getSlot(var3).getItem();
         if (var6.getCount() == 1 && var6.getMaxStackSize() == 1 && ItemStack.matches(var6, var5)) {
            this.q(var1, var2);
            return this.u.m(38 + var3, var4);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public int P(Minecraft var1, DispenserMenu var2, int var3, int var4) {
      if (!this.m && !this.W && !this.f) {
         this.x(var1, var2, var3);
         if (this.G && this.y[var3]) {
            this.q(var1, var2);
         }

         return tv.V(var4, this.i[var3], this.A[var3], this.y[var3]);
      } else {
         this.j(var1);
         return this.l.h(var4);
      }
   }
}

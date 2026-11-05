package com.larpclient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public final class _g {
   public static final _g G = new _g(true);
   public static final _g Y = new _g(false);
   private final boolean U;
   private final dw Z = new dw();
   private final tz l = new tz();
   private final ItemStack[] s = new ItemStack[5];
   private int[] I = new int[9];
   private final Set<UUID> q = new HashSet<>();
   private final Set<UUID> t = new HashSet<>();
   private final Set<String> r = new HashSet<>();
   private BlockPos D;
   private ClientLevel a;
   private DispenserMenu L;
   private boolean A;
   private boolean P;
   private int k = 1;
   private int m;
   private String g = "Open the assigned dispenser to detect armor";
   private String w = "";

   private void f(DispenserMenu var1, String var2) {
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.Z.h(var4);
         var3.append(" v")
            .append(var4 + 1)
            .append("->p")
            .append(var5 + 1)
            .append(" real=")
            .append(this.C(var1.getSlot(var5).getItem()))
            .append(" shown=")
            .append(var1.getSlot(var5).getItem().isEmpty() ? -1 : this.Z.X("stock:" + var5, -1));
      }

      String var6 = var3.toString();
      if (!var2.equals("snapshot") || !var6.equals(this.w)) {
         pk.q(this.G() + " " + var2 + " sync=" + var1.containerId + var6);
      }

      this.w = var6;
   }

   _g(boolean var1) {
      this.U = var1;
      Arrays.fill(this.s, ItemStack.EMPTY);
   }

   public static _g E() {
      return G.A ? G : Y;
   }

   public static _g z(ep.o var0) {
      return var0 == ep.o.Q ? G : (var0 == ep.o.U ? Y : null);
   }

   private ep.o T() {
      return this.U ? ep.o.Q : ep.o.U;
   }

   public int r() {
      return this.U ? 3 : 4;
   }

   public boolean T() {
      return this.A;
   }

   public int i() {
      return this.k;
   }

   public String A() {
      return this.g;
   }

   public String G() {
      return this.U ? "Horse Racing" : "Armor Game";
   }

   public String D(int var1) {
      if (var1 >= 1 && var1 <= this.r()) {
         return this.s[var1].isEmpty()
            ? (this.U ? "Horse " + var1 : new String[]{"", "Helmet", "Chestplate", "Leggings", "Boots"}[var1])
            : this.s[var1].getHoverName().getString();
      } else {
         return "";
      }
   }

   public void o(int var1) {
      if (var1 >= 1 && var1 <= this.r()) {
         this.k = var1;
      }
   }

   public String R() {
      return !this.A ? "1" : (this.k == this.r() ? "off" : Integer.toString(this.k + 1));
   }

   public String X(int var1) {
      if (this.U) {
         return this.D(var1);
      }

      return switch (var1) {
         case 1 -> "Helmet";
         case 2 -> "Chestplate";
         case 3 -> "Leggings";
         case 4 -> "Boots";
         default -> "";
      };
   }

   public void m(boolean var1) {
      if (this.A != var1) {
         this.A = var1;
         this.e(Minecraft.getInstance());
      }
   }

   private boolean v() {
      return this.D != null && !this.s[1].isEmpty();
   }

   private static boolean I(ItemStack var0, ItemStack var1) {
      return !var0.isEmpty()
         && !var1.isEmpty()
         && var0.is(var1.getItem())
         && Objects.equals(var0.get(DataComponents.CUSTOM_NAME), var1.get(DataComponents.CUSTOM_NAME));
   }

   private int C(ItemStack var1) {
      for (int var2 = 1; var2 <= this.r(); var2++) {
         if (I(var1, this.s[var2])) {
            return var2;
         }
      }

      return -1;
   }

   boolean k(ItemStack[] var1) {
      ItemStack[] var2 = new ItemStack[5];
      Arrays.fill(var2, ItemStack.EMPTY);
      int[] var3 = new int[9];
      int var4 = 0;
      if (var1.length != 9) {
         throw new IllegalArgumentException("Expected nine dispenser slots");
      }

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = var1[var5];
         if (this.U || !var6.isEmpty()) {
            if (var6.getCount() != 1) {
               throw new IllegalArgumentException(
                  this.U ? "Use three horse armor types, three of each (one per slot)" : "Use one helmet, chestplate, leggings and boots"
               );
            }

            int var7;
            if (!this.U) {
               var7 = a(var6);
               if (var7 < 1 || !var2[var7].isEmpty()) {
                  throw new IllegalArgumentException("Use one piece for each armor slot; leave other slots empty");
               }
            } else {
               if (!BuiltInRegistries.ITEM.getKey(var6.getItem()).getPath().endsWith("_horse_armor")) {
                  throw new IllegalArgumentException("Horse Racing requires horse armor");
               }

               var7 = 1;

               while (var7 <= var4 && !I(var6, var2[var7])) {
                  var7++;
               }

               if (var7 > 3) {
                  throw new IllegalArgumentException("Use exactly three horse armor types");
               }

               var4 = Math.max(var4, var7);
            }

            var2[var7] = var6.copyWithCount(1);
            var3[var5] = var7;
         }
      }

      if (this.U) {
         int[] var10 = Arrays.stream(var3).map(var0 -> var0 - 1).toArray();
         tz.l(var10);
         int[] var13 = new int[4];
         byte var15 = 1;

         for (int var8 = 1; var8 <= 3; var8++) {
            var13[var8] = this.C(var2[var8]);
            if (var13[var8] < 1) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            ItemStack[] var16 = new ItemStack[5];
            Arrays.fill(var16, ItemStack.EMPTY);

            for (int var9 = 1; var9 <= 3; var9++) {
               var16[var13[var9]] = var2[var9];
            }

            for (int var17 = 0; var17 < 9; var17++) {
               var3[var17] = var13[var3[var17]];
            }

            var2 = var16;
         }
      } else {
         for (int var11 = 1; var11 <= 4; var11++) {
            if (var2[var11].isEmpty()) {
               throw new IllegalArgumentException("Armor Game needs all four armor pieces");
            }
         }
      }

      int var12 = !Arrays.equals(this.I, var3) ? 1 : 0;

      for (int var14 = 1; var14 <= this.r(); var14++) {
         var12 |= !ItemStack.isSameItemSameComponents(this.s[var14], var2[var14]) ? 1 : 0;
      }

      System.arraycopy(var2, 0, this.s, 0, 5);
      this.I = var3;
      return (boolean)var12;
   }

   public void C(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (var1 != null && var1.player != null && var3 != null) {
         int var4 = this.a == var1.level && Objects.equals(this.D, var3) ? 0 : 1;
         if (var4 != 0) {
            Arrays.fill(this.s, ItemStack.EMPTY);
            this.I = new int[9];
            this.D = var3;
            this.a = var1.level;
            this.e(var1);
         }

         this.L = var2;
         ItemStack[] var5 = new ItemStack[9];

         for (int var6 = 0; var6 < 9; var6++) {
            var5[var6] = var2.getSlot(var6).getItem();
         }

         int var15 = this.v() && this.Z.R() ? 1 : 0;

         for (ItemStack var10 : var5) {
            if (!var10.isEmpty() && (var10.getCount() != 1 || this.C(var10) < 1)) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            this.V(var1);

            for (ItemStack var24 : var5) {
               int var26 = this.C(var24);
               if (var26 > 0) {
                  this.s[var26] = var24.copyWithCount(1);
               }
            }

            this.g = "Refreshed armor; retained displayed slots";
         } else {
            try {
               this.k(var5);
               this.e(var1);
               this.L = var2;
               this.g = "Detected " + (this.U ? "9 horse armor slots" : "4 armor pieces and their slots");
               xa.q(this.G(), this.g);
            } catch (IllegalArgumentException var14) {
               byte var17 = this.v();

               for (ItemStack var12 : var5) {
                  if (!var12.isEmpty() && (var12.getCount() != 1 || this.C(var12) < 1)) {
                     var17 = 0;
                  }
               }

               if (var17 != 0) {
                  this.V(var1);

                  for (ItemStack var27 : var5) {
                     int var13 = this.C(var27);
                     if (var13 > 0) {
                        this.s[var13] = var27.copyWithCount(1);
                     }
                  }

                  this.g = "Tracking active round; detected occupied slots";
                  return;
               }

               this.g = var14.getMessage();
               Arrays.fill(this.s, ItemStack.EMPTY);
               this.I = new int[9];
               this.e(var1);
               xa.q(this.G(), "Not ready: " + this.g);
            }
         }
      }
   }

   public void D(Minecraft var1) {
      if (var1 != null && var1.level != null && this.a == var1.level) {
         ep var2 = ep.H();
         if (this.D != null && !Objects.equals(this.D, var2.n(this.T()))) {
            this.D = null;
            Arrays.fill(this.s, ItemStack.EMPTY);
            this.I = new int[9];
            this.e(var1);
         }

         this.V(var1);
      } else {
         this.D = null;
         this.a = var1 == null ? null : var1.level;
         Arrays.fill(this.s, ItemStack.EMPTY);
         this.I = new int[9];
         this.e(var1);
      }
   }

   private void e(Minecraft var1) {
      this.l.c();
      this.Z.h(this.I);
      this.q.clear();
      this.t.clear();
      this.r.clear();
      this.L = null;
      this.m = ThreadLocalRandom.current().nextInt(2520);
      if (var1 != null && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            if (this.C(this.X(var1, var2)) > 0) {
               this.r.add("player:" + var2);
            }
         }

         if (!this.U) {
            for (int var6 = 1; var6 <= 4; var6++) {
               if (!var1.player.getItemBySlot(L(var6)).isEmpty()) {
                  this.r.add("worn:" + var6);
               }
            }
         }

         if (var1.level != null) {
            for (Entity var3 : var1.level.entitiesForRendering()) {
               this.t.add(var3.getUUID());
               if (!this.U && var3 instanceof ArmorStand var4) {
                  for (int var5 = 1; var5 <= 4; var5++) {
                     if (!var4.getItemBySlot(L(var5)).isEmpty()) {
                        this.r.add("stand:" + var4.getUUID() + ":" + var5);
                     }
                  }
               }
            }
         }
      }
   }

   private ItemStack X(Minecraft var1, int var2) {
      return var2 < 36 ? var1.player.getInventory().getItem(var2) : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
   }

   private DispenserMenu s(Minecraft var1) {
      ep var2 = ep.H();
      return var1 != null
            && var1.player != null
            && this.L != null
            && var1.player.containerMenu == this.L
            && var2.V() == this.L.containerId
            && var2.U() == this.T()
            && Objects.equals(this.D, var2.x(var2.b()))
         ? this.L
         : null;
   }

   private ItemStack l(ItemStack var1, int var2) {
      return !var1.isEmpty() && var2 > 0 && var2 <= this.r() && !this.s[var2].isEmpty() ? this.s[var2].copyWithCount(var1.getCount()) : var1;
   }

   private void Q(int var1, int var2) {
      xa.q(this.G(), xa.U(this.D(var1), this.D(var2), true, true));
   }

   public ItemStack i(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.A && this.v() && var2 != null && var2.player != null && !var1.isEmpty()) {
         this.V(var2);

         for (int var3 = 0; var3 < 38; var3++) {
            if (var1 == this.X(var2, var3)) {
               return this.l(var1, this.Z.X("player:" + var3, this.C(var1)));
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack S(ItemEntity var1, ItemStack var2) {
      if (this.A && this.v()) {
         this.V(Minecraft.getInstance());
         return this.q.contains(var1.getUUID()) ? this.l(var2, this.Z.X("world:" + var1.getUUID(), this.C(var2))) : var2;
      } else {
         return var2;
      }
   }

   public ItemStack O(Minecraft var1, Slot var2) {
      if (this.A && this.v() && var2 != null) {
         if (!this.U && var2.container == var1.player.getInventory() && var2.getContainerSlot() >= 36 && var2.getContainerSlot() <= 39) {
            ItemStack[] var6 = this.O(var1);
            return var6 == null ? null : var6[40 - var2.getContainerSlot()];
         } else {
            DispenserMenu var3 = this.s(var1);
            if (var3 != null && var2.container != var1.player.getInventory() && var2.getContainerSlot() >= 0 && var2.getContainerSlot() <= 8) {
               this.V(var1);
               int var4 = this.Z.h(var2.getContainerSlot());
               ItemStack var5 = var3.getSlot(var4).getItem();
               return this.l(var5, this.Z.X("stock:" + var4, this.C(var5)));
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public boolean V(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.A && this.v() && !this.P && var3 >= 0 && var1 != null && var1.gameMode != null) {
         if (!this.U
            && var1.player != null
            && var1.player.containerMenu == var1.player.inventoryMenu
            && var2 == var1.player.inventoryMenu.containerId
            && var3 >= 5
            && var3 <= 8) {
            this.V(var1);
            int var6 = var3 - 4;
            int var7 = var6;
            ItemStack var8 = var1.player.containerMenu.getCarried();
            int var9 = a(var8);
            if (!var8.isEmpty() && var9 > 0 && var9 != var6 && this.Z.X("player:36", var9) == var6 && var1.player.getItemBySlot(L(var9)).isEmpty()) {
               this.P = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var9 + 4, var4, var5, var1.player);
               } finally {
                  this.P = (boolean)0;
               }

               return true;
            }

            for (int var10 = 1; var10 <= 4; var10++) {
               if (!var1.player.getItemBySlot(L(var10)).isEmpty() && this.Z.X("worn:" + var10, var10) == var6) {
                  var7 = var10;
                  break;
               }
            }

            if (var7 != var6) {
               this.P = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var7 + 4, var4, var5, var1.player);
               } finally {
                  this.P = (boolean)0;
               }

               return true;
            }

            if (!var1.player.getItemBySlot(L(var6)).isEmpty() && this.Z.X("worn:" + var6, var6) != var6) {
               return true;
            }
         }

         DispenserMenu var34 = this.s(var1);
         if (var34 == null || var34.containerId != var2) {
            return false;
         }

         if (var3 >= var34.slots.size()) {
            return false;
         }

         this.V(var1);
         if (var5 == ContainerInput.QUICK_MOVE && var34.getSlot(var3).container == var1.player.getInventory()) {
            int var35 = this.Z.c("player:" + var34.getSlot(var3).getContainerSlot());
            if (var35 >= 0 && var35 < 9) {
               if (var34.getCarried().isEmpty() && var34.getSlot(var35).getItem().isEmpty() && var34.getSlot(var3).getItem().getCount() == 1) {
                  if (this.U) {
                     for (int var37 = 0; var37 < 9; var37++) {
                        if (var34.getSlot(this.Z.h(var37)).getItem().isEmpty()) {
                           this.Z.B("player:" + var34.getSlot(var3).getContainerSlot(), var37);
                           break;
                        }
                     }
                  }

                  this.f(var34, "home-return source=" + var3 + " destination=" + var35);
                  this.P = (boolean)1;

                  try {
                     int var38 = var34.getSlot(var3).getContainerSlot();
                     if (var38 < 9) {
                        var1.gameMode.handleContainerInput(var2, var35, var38, ContainerInput.SWAP, var1.player);
                     } else {
                        var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                        var1.gameMode.handleContainerInput(var2, var35, 40, ContainerInput.SWAP, var1.player);
                        var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                     }
                  } finally {
                     this.P = (boolean)0;
                  }

                  this.V(var1);
                  return true;
               }

               return true;
            }
         }

         if (var3 > 8) {
            return false;
         }

         int var36 = this.Z.h(var3);
         if (var36 == var3) {
            return false;
         }

         this.P = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var36, var4, var5, var1.player);
         } finally {
            this.P = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public void V(Minecraft var1) {
      if (!this.P && this.A && this.v() && var1 != null && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.s(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.C(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.C(var6);
            if (var8 < 0) {
               this.r.remove(var7);
            }

            if (!this.r.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         if (!this.U) {
            for (int var15 = 1; var15 <= 4; var15++) {
               String var17 = "worn:" + var15;
               int var19 = this.C(var1.player.getItemBySlot(L(var15)));
               if (var19 < 1) {
                  this.r.remove(var17);
               }

               if (!this.r.contains(var17)) {
                  var2.put(var17, var19);
               }
            }
         }

         BlockPos var16 = this.D;
         BlockState var18 = var1.level.getBlockState(var16);
         Vec3 var20 = Vec3.atCenterOf(var16);
         if (var18.is(Blocks.DROPPER) || var18.is(Blocks.DISPENSER)) {
            Direction var21 = (Direction)var18.getValue(DispenserBlock.FACING);
            var20 = var20.add(var21.getStepX() * 0.7, var21.getStepY() * 0.7, var21.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.C(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var20) <= 2.25 ? 1 : 0;
               if (!this.t.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.q.add(var10.getUUID());
               }

               if (this.q.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.C(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (!this.U) {
            for (Entity var27 : var1.level.entitiesForRendering()) {
               if (var27 instanceof ArmorStand var23 && var23.distanceToSqr(var20) < 9.0) {
                  for (int var28 = 1; var28 <= 4; var28++) {
                     String var29 = "stand:" + var23.getUUID() + ":" + var28;
                     int var13 = this.C(var23.getItemBySlot(L(var28)));
                     if (var13 < 1) {
                        this.r.remove(var29);
                     }

                     if (!this.r.contains(var29)) {
                        var2.put(var29, var13);
                        var3.add(var29);
                     }
                  }
               }
            }
         }

         if (this.U) {
            this.Z.z(var2, var3, () -> this.l.V(this.k), this.m, var4 != null);
         } else {
            this.Z.t(var2, var3, this.k, this.m, var4 != null);
         }

         if (this.U && var4 != null && this.Z.S()) {
            this.l.c();
         }

         for (dw.t var26 : this.Z.B()) {
            this.Q(var26.actual(), var26.display());
         }

         if (var4 != null) {
            this.f(var4, "snapshot");
         }
      }
   }

   private static EquipmentSlot L(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int a(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == L(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] O(Minecraft var1) {
      if (!this.U && this.A && this.v() && var1 != null && var1.player != null) {
         this.V(var1);
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.player.getItemBySlot(L(var3));
            int var5 = this.Z.X("worn:" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = var5 == var3 ? var4 : this.l(var4, var5);
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public ItemStack[] v(ArmorStand var1) {
      if (this.A && !this.U && this.v() && !(var1.distanceToSqr(Vec3.atCenterOf(this.D)) > 16.0)) {
         this.V(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(L(var3));
            int var5 = this.Z.X("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[this.C(var4) > 0 ? var5 : var3] = this.C(var4) > 0 ? this.l(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public EntityHitResult d(ArmorStand var1, EntityHitResult var2) {
      if (this.v(var1) == null) {
         return var2;
      }

      double var3 = (var2.getLocation().y - var1.getY()) / (var1.isSmall() ? 0.5 : 1.0);
      int var5 = var3 >= 1.6 ? 1 : (var3 >= 0.9 ? 2 : (var3 >= 0.4 ? 3 : 4));

      for (int var6 = 1; var6 <= 4; var6++) {
         if (!var1.getItemBySlot(L(var6)).isEmpty() && this.Z.X("stand:" + var1.getUUID() + ":" + var6, var6) == var5) {
            double var7 = switch (var6) {
               case 1 -> 1.75;
               case 2 -> 1.2;
               case 3 -> 0.65;
               default -> 0.15;
            };
            return new EntityHitResult(var1, new Vec3(var2.getLocation().x, var1.getY() + var7 * (var1.isSmall() ? 0.5 : 1.0), var2.getLocation().z));
         }
      }

      return var2;
   }
}

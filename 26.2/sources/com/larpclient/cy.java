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

public final class cy {
   public static final cy Z = new cy(true);
   public static final cy d = new cy(false);
   private final boolean K;
   private final p0 C = new p0();
   private final u7 t = new u7();
   private final ItemStack[] O = new ItemStack[5];
   private int[] A = new int[9];
   private final Set<UUID> R = new HashSet<>();
   private final Set<UUID> Q = new HashSet<>();
   private final Set<String> q = new HashSet<>();
   private BlockPos c;
   private ClientLevel i;
   private DispenserMenu S;
   private boolean V;
   private boolean I;
   private int g = 1;
   private int r;
   private String M = "Open the assigned dispenser to detect armor";
   private String Y = "";

   private void H(DispenserMenu var1, String var2) {
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.C.J(var4);
         var3.append(" v")
            .append(var4 + 1)
            .append("->p")
            .append(var5 + 1)
            .append(" real=")
            .append(this.n(var1.getSlot(var5).getItem()))
            .append(" shown=")
            .append(var1.getSlot(var5).getItem().isEmpty() ? -1 : this.C.e("stock:" + var5, -1));
      }

      String var6 = var3.toString();
      if (!var2.equals("snapshot") || !var6.equals(this.Y)) {
         us.R(this.K() + " " + var2 + " sync=" + var1.containerId + var6);
      }

      this.Y = var6;
   }

   cy(boolean var1) {
      this.K = var1;
      Arrays.fill(this.O, ItemStack.EMPTY);
   }

   public static cy Q() {
      return Z.V ? Z : d;
   }

   public static cy V(gj.d var0) {
      return var0 == gj.d.T ? Z : (var0 == gj.d.u ? d : null);
   }

   private gj.d A() {
      return this.K ? gj.d.T : gj.d.u;
   }

   public int w() {
      return this.K ? 3 : 4;
   }

   public boolean E() {
      return this.V;
   }

   public int g() {
      return this.g;
   }

   public String L() {
      return this.M;
   }

   public String K() {
      return this.K ? "Horse Racing" : "Armor Game";
   }

   public String i(int var1) {
      if (var1 >= 1 && var1 <= this.w()) {
         return this.O[var1].isEmpty()
            ? (this.K ? "Horse " + var1 : new String[]{"", "Helmet", "Chestplate", "Leggings", "Boots"}[var1])
            : this.O[var1].getHoverName().getString();
      } else {
         return "";
      }
   }

   public void f(int var1) {
      if (var1 >= 1 && var1 <= this.w()) {
         this.g = var1;
      }
   }

   public String U() {
      return !this.V ? "1" : (this.g == this.w() ? "off" : Integer.toString(this.g + 1));
   }

   public String h(int var1) {
      if (this.K) {
         return this.i(var1);
      }

      return switch (var1) {
         case 1 -> "Helmet";
         case 2 -> "Chestplate";
         case 3 -> "Leggings";
         case 4 -> "Boots";
         default -> "";
      };
   }

   public void G(boolean var1) {
      if (this.V != var1) {
         this.V = var1;
         this.E(Minecraft.getInstance());
      }
   }

   private boolean R() {
      return this.c != null && !this.O[1].isEmpty();
   }

   private static boolean g(ItemStack var0, ItemStack var1) {
      return !var0.isEmpty()
         && !var1.isEmpty()
         && var0.is(var1.getItem())
         && Objects.equals(var0.get(DataComponents.CUSTOM_NAME), var1.get(DataComponents.CUSTOM_NAME));
   }

   private int n(ItemStack var1) {
      for (int var2 = 1; var2 <= this.w(); var2++) {
         if (g(var1, this.O[var2])) {
            return var2;
         }
      }

      return -1;
   }

   boolean s(ItemStack[] var1) {
      ItemStack[] var2 = new ItemStack[5];
      Arrays.fill(var2, ItemStack.EMPTY);
      int[] var3 = new int[9];
      int var4 = 0;
      if (var1.length != 9) {
         throw new IllegalArgumentException("Expected nine dispenser slots");
      }

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = var1[var5];
         if (this.K || !var6.isEmpty()) {
            if (var6.getCount() != 1) {
               throw new IllegalArgumentException(
                  this.K ? "Use three horse armor types, three of each (one per slot)" : "Use one helmet, chestplate, leggings and boots"
               );
            }

            int var7;
            if (!this.K) {
               var7 = A(var6);
               if (var7 < 1 || !var2[var7].isEmpty()) {
                  throw new IllegalArgumentException("Use one piece for each armor slot; leave other slots empty");
               }
            } else {
               if (!BuiltInRegistries.ITEM.getKey(var6.getItem()).getPath().endsWith("_horse_armor")) {
                  throw new IllegalArgumentException("Horse Racing requires horse armor");
               }

               var7 = 1;

               while (var7 <= var4 && !g(var6, var2[var7])) {
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

      if (this.K) {
         int[] var10 = Arrays.stream(var3).map(var0 -> var0 - 1).toArray();
         u7.S(var10);
         int[] var13 = new int[4];
         byte var15 = 1;

         for (int var8 = 1; var8 <= 3; var8++) {
            var13[var8] = this.n(var2[var8]);
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

      int var12 = !Arrays.equals(this.A, var3) ? 1 : 0;

      for (int var14 = 1; var14 <= this.w(); var14++) {
         var12 |= !ItemStack.isSameItemSameComponents(this.O[var14], var2[var14]) ? 1 : 0;
      }

      System.arraycopy(var2, 0, this.O, 0, 5);
      this.A = var3;
      return (boolean)var12;
   }

   public void W(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (var1 != null && var1.player != null && var3 != null) {
         int var4 = this.i == var1.level && Objects.equals(this.c, var3) ? 0 : 1;
         if (var4 != 0) {
            Arrays.fill(this.O, ItemStack.EMPTY);
            this.A = new int[9];
            this.c = var3;
            this.i = var1.level;
            this.E(var1);
         }

         this.S = var2;
         ItemStack[] var5 = new ItemStack[9];

         for (int var6 = 0; var6 < 9; var6++) {
            var5[var6] = var2.getSlot(var6).getItem();
         }

         int var15 = this.R() && this.C.U() ? 1 : 0;

         for (ItemStack var10 : var5) {
            if (!var10.isEmpty() && (var10.getCount() != 1 || this.n(var10) < 1)) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            this.z(var1);

            for (ItemStack var24 : var5) {
               int var26 = this.n(var24);
               if (var26 > 0) {
                  this.O[var26] = var24.copyWithCount(1);
               }
            }

            this.M = "Refreshed armor; retained displayed slots";
         } else {
            try {
               this.s(var5);
               this.E(var1);
               this.S = var2;
               this.M = "Detected " + (this.K ? "9 horse armor slots" : "4 armor pieces and their slots");
               te.Z(this.K(), this.M);
            } catch (IllegalArgumentException var14) {
               byte var17 = this.R();

               for (ItemStack var12 : var5) {
                  if (!var12.isEmpty() && (var12.getCount() != 1 || this.n(var12) < 1)) {
                     var17 = 0;
                  }
               }

               if (var17 != 0) {
                  this.z(var1);

                  for (ItemStack var27 : var5) {
                     int var13 = this.n(var27);
                     if (var13 > 0) {
                        this.O[var13] = var27.copyWithCount(1);
                     }
                  }

                  this.M = "Tracking active round; detected occupied slots";
                  return;
               }

               this.M = var14.getMessage();
               Arrays.fill(this.O, ItemStack.EMPTY);
               this.A = new int[9];
               this.E(var1);
               te.Z(this.K(), "Not ready: " + this.M);
            }
         }
      }
   }

   public void f(Minecraft var1) {
      if (var1 != null && var1.level != null && this.i == var1.level) {
         gj var2 = gj.c();
         if (this.c != null && !Objects.equals(this.c, var2.q(this.A()))) {
            this.c = null;
            Arrays.fill(this.O, ItemStack.EMPTY);
            this.A = new int[9];
            this.E(var1);
         }

         this.z(var1);
      } else {
         this.c = null;
         this.i = var1 == null ? null : var1.level;
         Arrays.fill(this.O, ItemStack.EMPTY);
         this.A = new int[9];
         this.E(var1);
      }
   }

   private void E(Minecraft var1) {
      this.t.g();
      this.C.d(this.A);
      this.R.clear();
      this.Q.clear();
      this.q.clear();
      this.S = null;
      this.r = ThreadLocalRandom.current().nextInt(2520);
      if (var1 != null && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            if (this.n(this.s(var1, var2)) > 0) {
               this.q.add("player:" + var2);
            }
         }

         if (!this.K) {
            for (int var6 = 1; var6 <= 4; var6++) {
               if (!var1.player.getItemBySlot(r(var6)).isEmpty()) {
                  this.q.add("worn:" + var6);
               }
            }
         }

         if (var1.level != null) {
            for (Entity var3 : var1.level.entitiesForRendering()) {
               this.Q.add(var3.getUUID());
               if (!this.K && var3 instanceof ArmorStand var4) {
                  for (int var5 = 1; var5 <= 4; var5++) {
                     if (!var4.getItemBySlot(r(var5)).isEmpty()) {
                        this.q.add("stand:" + var4.getUUID() + ":" + var5);
                     }
                  }
               }
            }
         }
      }
   }

   private ItemStack s(Minecraft var1, int var2) {
      return var2 < 36 ? var1.player.getInventory().getItem(var2) : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
   }

   private DispenserMenu Q(Minecraft var1) {
      gj var2 = gj.c();
      return var1 != null
            && var1.player != null
            && this.S != null
            && var1.player.containerMenu == this.S
            && var2.r() == this.S.containerId
            && var2.E() == this.A()
            && Objects.equals(this.c, var2.n(var2.C()))
         ? this.S
         : null;
   }

   private ItemStack I(ItemStack var1, int var2) {
      return !var1.isEmpty() && var2 > 0 && var2 <= this.w() && !this.O[var2].isEmpty() ? this.O[var2].copyWithCount(var1.getCount()) : var1;
   }

   private void G(int var1, int var2) {
      te.Z(this.K(), te.I(this.i(var1), this.i(var2), true, true));
   }

   public ItemStack D(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.V && this.R() && var2 != null && var2.player != null && !var1.isEmpty()) {
         this.z(var2);

         for (int var3 = 0; var3 < 38; var3++) {
            if (var1 == this.s(var2, var3)) {
               return this.I(var1, this.C.e("player:" + var3, this.n(var1)));
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack q(ItemEntity var1, ItemStack var2) {
      if (this.V && this.R()) {
         this.z(Minecraft.getInstance());
         return this.R.contains(var1.getUUID()) ? this.I(var2, this.C.e("world:" + var1.getUUID(), this.n(var2))) : var2;
      } else {
         return var2;
      }
   }

   public ItemStack E(Minecraft var1, Slot var2) {
      if (this.V && this.R() && var2 != null) {
         if (!this.K && var2.container == var1.player.getInventory() && var2.getContainerSlot() >= 36 && var2.getContainerSlot() <= 39) {
            ItemStack[] var6 = this.v(var1);
            return var6 == null ? null : var6[40 - var2.getContainerSlot()];
         } else {
            DispenserMenu var3 = this.Q(var1);
            if (var3 != null && var2.container != var1.player.getInventory() && var2.getContainerSlot() >= 0 && var2.getContainerSlot() <= 8) {
               this.z(var1);
               int var4 = this.C.J(var2.getContainerSlot());
               ItemStack var5 = var3.getSlot(var4).getItem();
               return this.I(var5, this.C.e("stock:" + var4, this.n(var5)));
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public boolean C(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.V && this.R() && !this.I && var3 >= 0 && var1 != null && var1.gameMode != null) {
         if (!this.K
            && var1.player != null
            && var1.player.containerMenu == var1.player.inventoryMenu
            && var2 == var1.player.inventoryMenu.containerId
            && var3 >= 5
            && var3 <= 8) {
            this.z(var1);
            int var6 = var3 - 4;
            int var7 = var6;
            ItemStack var8 = var1.player.containerMenu.getCarried();
            int var9 = A(var8);
            if (!var8.isEmpty() && var9 > 0 && var9 != var6 && this.C.e("player:36", var9) == var6 && var1.player.getItemBySlot(r(var9)).isEmpty()) {
               this.I = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var9 + 4, var4, var5, var1.player);
               } finally {
                  this.I = (boolean)0;
               }

               return true;
            }

            for (int var10 = 1; var10 <= 4; var10++) {
               if (!var1.player.getItemBySlot(r(var10)).isEmpty() && this.C.e("worn:" + var10, var10) == var6) {
                  var7 = var10;
                  break;
               }
            }

            if (var7 != var6) {
               this.I = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var7 + 4, var4, var5, var1.player);
               } finally {
                  this.I = (boolean)0;
               }

               return true;
            }

            if (!var1.player.getItemBySlot(r(var6)).isEmpty() && this.C.e("worn:" + var6, var6) != var6) {
               return true;
            }
         }

         DispenserMenu var34 = this.Q(var1);
         if (var34 == null || var34.containerId != var2) {
            return false;
         }

         if (var3 >= var34.slots.size()) {
            return false;
         }

         this.z(var1);
         if (var5 == ContainerInput.QUICK_MOVE && var34.getSlot(var3).container == var1.player.getInventory()) {
            int var35 = this.C.q("player:" + var34.getSlot(var3).getContainerSlot());
            if (var35 >= 0 && var35 < 9) {
               if (var34.getCarried().isEmpty() && var34.getSlot(var35).getItem().isEmpty() && var34.getSlot(var3).getItem().getCount() == 1) {
                  if (this.K) {
                     for (int var37 = 0; var37 < 9; var37++) {
                        if (var34.getSlot(this.C.J(var37)).getItem().isEmpty()) {
                           this.C.V("player:" + var34.getSlot(var3).getContainerSlot(), var37);
                           break;
                        }
                     }
                  }

                  this.H(var34, "home-return source=" + var3 + " destination=" + var35);
                  this.I = (boolean)1;

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
                     this.I = (boolean)0;
                  }

                  this.z(var1);
                  return true;
               }

               return true;
            }
         }

         if (var3 > 8) {
            return false;
         }

         int var36 = this.C.J(var3);
         if (var36 == var3) {
            return false;
         }

         this.I = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var36, var4, var5, var1.player);
         } finally {
            this.I = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public void z(Minecraft var1) {
      if (!this.I && this.V && this.R() && var1 != null && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.Q(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.n(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.n(var6);
            if (var8 < 0) {
               this.q.remove(var7);
            }

            if (!this.q.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         if (!this.K) {
            for (int var15 = 1; var15 <= 4; var15++) {
               String var17 = "worn:" + var15;
               int var19 = this.n(var1.player.getItemBySlot(r(var15)));
               if (var19 < 1) {
                  this.q.remove(var17);
               }

               if (!this.q.contains(var17)) {
                  var2.put(var17, var19);
               }
            }
         }

         BlockPos var16 = this.c;
         BlockState var18 = var1.level.getBlockState(var16);
         Vec3 var20 = Vec3.atCenterOf(var16);
         if (var18.is(Blocks.DROPPER) || var18.is(Blocks.DISPENSER)) {
            Direction var21 = (Direction)var18.getValue(DispenserBlock.FACING);
            var20 = var20.add(var21.getStepX() * 0.7, var21.getStepY() * 0.7, var21.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.n(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var20) <= 2.25 ? 1 : 0;
               if (!this.Q.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.R.add(var10.getUUID());
               }

               if (this.R.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.n(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (!this.K) {
            for (Entity var27 : var1.level.entitiesForRendering()) {
               if (var27 instanceof ArmorStand var23 && var23.distanceToSqr(var20) < 9.0) {
                  for (int var28 = 1; var28 <= 4; var28++) {
                     String var29 = "stand:" + var23.getUUID() + ":" + var28;
                     int var13 = this.n(var23.getItemBySlot(r(var28)));
                     if (var13 < 1) {
                        this.q.remove(var29);
                     }

                     if (!this.q.contains(var29)) {
                        var2.put(var29, var13);
                        var3.add(var29);
                     }
                  }
               }
            }
         }

         if (this.K) {
            this.C.Y(var2, var3, () -> this.t.d(this.g), this.r, var4 != null);
         } else {
            this.C.y(var2, var3, this.g, this.r, var4 != null);
         }

         if (this.K && var4 != null && this.C.D()) {
            this.t.g();
         }

         for (p0.q var26 : this.C.T()) {
            this.G(var26.actual(), var26.display());
         }

         if (var4 != null) {
            this.H(var4, "snapshot");
         }
      }
   }

   private static EquipmentSlot r(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int A(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == r(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] v(Minecraft var1) {
      if (!this.K && this.V && this.R() && var1 != null && var1.player != null) {
         this.z(var1);
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.player.getItemBySlot(r(var3));
            int var5 = this.C.e("worn:" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = var5 == var3 ? var4 : this.I(var4, var5);
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public ItemStack[] O(ArmorStand var1) {
      if (this.V && !this.K && this.R() && !(var1.distanceToSqr(Vec3.atCenterOf(this.c)) > 16.0)) {
         this.z(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(r(var3));
            int var5 = this.C.e("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[this.n(var4) > 0 ? var5 : var3] = this.n(var4) > 0 ? this.I(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public EntityHitResult d(ArmorStand var1, EntityHitResult var2) {
      if (this.O(var1) == null) {
         return var2;
      }

      double var3 = (var2.getLocation().y - var1.getY()) / (var1.isSmall() ? 0.5 : 1.0);
      int var5 = var3 >= 1.6 ? 1 : (var3 >= 0.9 ? 2 : (var3 >= 0.4 ? 3 : 4));

      for (int var6 = 1; var6 <= 4; var6++) {
         if (!var1.getItemBySlot(r(var6)).isEmpty() && this.C.e("stand:" + var1.getUUID() + ":" + var6, var6) == var5) {
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

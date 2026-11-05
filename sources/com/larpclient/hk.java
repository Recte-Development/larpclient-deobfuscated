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

public final class hk {
   public static final hk H = new hk(true);
   public static final hk T = new hk(false);
   private final boolean k;
   private final mp J = new mp();
   private final h8 G = new h8();
   private final ItemStack[] I = new ItemStack[5];
   private int[] K = new int[9];
   private final Set<UUID> W = new HashSet<>();
   private final Set<UUID> D = new HashSet<>();
   private final Set<String> g = new HashSet<>();
   private BlockPos L;
   private ClientLevel r;
   private DispenserMenu p;
   private boolean e;
   private boolean j;
   private int X = 1;
   private int C;
   private String Q = "Open the assigned dispenser to detect armor";
   private String d = "";

   private void w(DispenserMenu var1, String var2) {
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.J.a(var4);
         var3.append(" v")
            .append(var4 + 1)
            .append("->p")
            .append(var5 + 1)
            .append(" real=")
            .append(this.o(var1.getSlot(var5).getItem()))
            .append(" shown=")
            .append(var1.getSlot(var5).getItem().isEmpty() ? -1 : this.J.F("stock:" + var5, -1));
      }

      String var6 = var3.toString();
      if (!var2.equals("snapshot") || !var6.equals(this.d)) {
         o5.m(this.O() + " " + var2 + " sync=" + var1.containerId + var6);
      }

      this.d = var6;
   }

   hk(boolean var1) {
      this.k = var1;
      Arrays.fill(this.I, ItemStack.EMPTY);
   }

   public static hk G() {
      return H.e ? H : T;
   }

   public static hk r(f9.b var0) {
      return var0 == f9.b.g ? H : (var0 == f9.b.X ? T : null);
   }

   private f9.b B() {
      return this.k ? f9.b.g : f9.b.X;
   }

   public int m() {
      return this.k ? 3 : 4;
   }

   public boolean J() {
      return this.e;
   }

   public int A() {
      return this.X;
   }

   public String J() {
      return this.Q;
   }

   public String O() {
      return this.k ? "Horse Racing" : "Armor Game";
   }

   public String Z(int var1) {
      if (var1 >= 1 && var1 <= this.m()) {
         return this.I[var1].isEmpty()
            ? (this.k ? "Horse " + var1 : new String[]{"", "Helmet", "Chestplate", "Leggings", "Boots"}[var1])
            : this.I[var1].getHoverName().getString();
      } else {
         return "";
      }
   }

   public void X(int var1) {
      if (var1 >= 1 && var1 <= this.m()) {
         this.X = var1;
      }
   }

   public String T() {
      return !this.e ? "1" : (this.X == this.m() ? "off" : Integer.toString(this.X + 1));
   }

   public String X(int var1) {
      if (this.k) {
         return this.Z(var1);
      }

      return switch (var1) {
         case 1 -> "Helmet";
         case 2 -> "Chestplate";
         case 3 -> "Leggings";
         case 4 -> "Boots";
         default -> "";
      };
   }

   public void U(boolean var1) {
      if (this.e != var1) {
         this.e = var1;
         this.H(Minecraft.getInstance());
      }
   }

   private boolean a() {
      return this.L != null && !this.I[1].isEmpty();
   }

   private static boolean H(ItemStack var0, ItemStack var1) {
      return !var0.isEmpty()
         && !var1.isEmpty()
         && var0.is(var1.getItem())
         && Objects.equals(var0.get(DataComponents.CUSTOM_NAME), var1.get(DataComponents.CUSTOM_NAME));
   }

   private int o(ItemStack var1) {
      for (int var2 = 1; var2 <= this.m(); var2++) {
         if (H(var1, this.I[var2])) {
            return var2;
         }
      }

      return -1;
   }

   boolean g(ItemStack[] var1) {
      ItemStack[] var2 = new ItemStack[5];
      Arrays.fill(var2, ItemStack.EMPTY);
      int[] var3 = new int[9];
      int var4 = 0;
      if (var1.length != 9) {
         throw new IllegalArgumentException("Expected nine dispenser slots");
      }

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = var1[var5];
         if (this.k || !var6.isEmpty()) {
            if (var6.getCount() != 1) {
               throw new IllegalArgumentException(
                  this.k ? "Use three horse armor types, three of each (one per slot)" : "Use one helmet, chestplate, leggings and boots"
               );
            }

            int var7;
            if (!this.k) {
               var7 = A(var6);
               if (var7 < 1 || !var2[var7].isEmpty()) {
                  throw new IllegalArgumentException("Use one piece for each armor slot; leave other slots empty");
               }
            } else {
               if (!BuiltInRegistries.ITEM.getKey(var6.getItem()).getPath().endsWith("_horse_armor")) {
                  throw new IllegalArgumentException("Horse Racing requires horse armor");
               }

               var7 = 1;

               while (var7 <= var4 && !H(var6, var2[var7])) {
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

      if (this.k) {
         int[] var10 = Arrays.stream(var3).map(var0 -> var0 - 1).toArray();
         h8.R(var10);
         int[] var13 = new int[4];
         byte var15 = 1;

         for (int var8 = 1; var8 <= 3; var8++) {
            var13[var8] = this.o(var2[var8]);
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

      int var12 = !Arrays.equals(this.K, var3) ? 1 : 0;

      for (int var14 = 1; var14 <= this.m(); var14++) {
         var12 |= !ItemStack.isSameItemSameComponents(this.I[var14], var2[var14]) ? 1 : 0;
      }

      System.arraycopy(var2, 0, this.I, 0, 5);
      this.K = var3;
      return (boolean)var12;
   }

   public void I(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (var1 != null && var1.player != null && var3 != null) {
         int var4 = this.r == var1.level && Objects.equals(this.L, var3) ? 0 : 1;
         if (var4 != 0) {
            Arrays.fill(this.I, ItemStack.EMPTY);
            this.K = new int[9];
            this.L = var3;
            this.r = var1.level;
            this.H(var1);
         }

         this.p = var2;
         ItemStack[] var5 = new ItemStack[9];

         for (int var6 = 0; var6 < 9; var6++) {
            var5[var6] = var2.getSlot(var6).getItem();
         }

         int var15 = this.a() && this.J.g() ? 1 : 0;

         for (ItemStack var10 : var5) {
            if (!var10.isEmpty() && (var10.getCount() != 1 || this.o(var10) < 1)) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            this.s(var1);

            for (ItemStack var24 : var5) {
               int var26 = this.o(var24);
               if (var26 > 0) {
                  this.I[var26] = var24.copyWithCount(1);
               }
            }

            this.Q = "Refreshed armor; retained displayed slots";
         } else {
            try {
               this.g(var5);
               this.H(var1);
               this.p = var2;
               this.Q = "Detected " + (this.k ? "9 horse armor slots" : "4 armor pieces and their slots");
               hh.K(this.O(), this.Q);
            } catch (IllegalArgumentException var14) {
               byte var17 = this.a();

               for (ItemStack var12 : var5) {
                  if (!var12.isEmpty() && (var12.getCount() != 1 || this.o(var12) < 1)) {
                     var17 = 0;
                  }
               }

               if (var17 != 0) {
                  this.s(var1);

                  for (ItemStack var27 : var5) {
                     int var13 = this.o(var27);
                     if (var13 > 0) {
                        this.I[var13] = var27.copyWithCount(1);
                     }
                  }

                  this.Q = "Tracking active round; detected occupied slots";
                  return;
               }

               this.Q = var14.getMessage();
               Arrays.fill(this.I, ItemStack.EMPTY);
               this.K = new int[9];
               this.H(var1);
               hh.K(this.O(), "Not ready: " + this.Q);
            }
         }
      }
   }

   public void l(Minecraft var1) {
      if (var1 != null && var1.level != null && this.r == var1.level) {
         f9 var2 = f9.W();
         if (this.L != null && !Objects.equals(this.L, var2.R(this.B()))) {
            this.L = null;
            Arrays.fill(this.I, ItemStack.EMPTY);
            this.K = new int[9];
            this.H(var1);
         }

         this.s(var1);
      } else {
         this.L = null;
         this.r = var1 == null ? null : var1.level;
         Arrays.fill(this.I, ItemStack.EMPTY);
         this.K = new int[9];
         this.H(var1);
      }
   }

   private void H(Minecraft var1) {
      this.G.w();
      this.J.I(this.K);
      this.W.clear();
      this.D.clear();
      this.g.clear();
      this.p = null;
      this.C = ThreadLocalRandom.current().nextInt(2520);
      if (var1 != null && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            if (this.o(this.a(var1, var2)) > 0) {
               this.g.add("player:" + var2);
            }
         }

         if (!this.k) {
            for (int var6 = 1; var6 <= 4; var6++) {
               if (!var1.player.getItemBySlot(Y(var6)).isEmpty()) {
                  this.g.add("worn:" + var6);
               }
            }
         }

         if (var1.level != null) {
            for (Entity var3 : var1.level.entitiesForRendering()) {
               this.D.add(var3.getUUID());
               if (!this.k && var3 instanceof ArmorStand var4) {
                  for (int var5 = 1; var5 <= 4; var5++) {
                     if (!var4.getItemBySlot(Y(var5)).isEmpty()) {
                        this.g.add("stand:" + var4.getUUID() + ":" + var5);
                     }
                  }
               }
            }
         }
      }
   }

   private ItemStack a(Minecraft var1, int var2) {
      return var2 < 36 ? var1.player.getInventory().getItem(var2) : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
   }

   private DispenserMenu W(Minecraft var1) {
      f9 var2 = f9.W();
      return var1 != null
            && var1.player != null
            && this.p != null
            && var1.player.containerMenu == this.p
            && var2.i() == this.p.containerId
            && var2.o() == this.B()
            && Objects.equals(this.L, var2.j(var2.v()))
         ? this.p
         : null;
   }

   private ItemStack P(ItemStack var1, int var2) {
      return !var1.isEmpty() && var2 > 0 && var2 <= this.m() && !this.I[var2].isEmpty() ? this.I[var2].copyWithCount(var1.getCount()) : var1;
   }

   private void M(int var1, int var2) {
      hh.K(this.O(), hh.e(this.Z(var1), this.Z(var2), true, true));
   }

   public ItemStack B(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.e && this.a() && var2 != null && var2.player != null && !var1.isEmpty()) {
         this.s(var2);

         for (int var3 = 0; var3 < 38; var3++) {
            if (var1 == this.a(var2, var3)) {
               return this.P(var1, this.J.F("player:" + var3, this.o(var1)));
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack e(ItemEntity var1, ItemStack var2) {
      if (this.e && this.a()) {
         this.s(Minecraft.getInstance());
         return this.W.contains(var1.getUUID()) ? this.P(var2, this.J.F("world:" + var1.getUUID(), this.o(var2))) : var2;
      } else {
         return var2;
      }
   }

   public ItemStack n(Minecraft var1, Slot var2) {
      if (this.e && this.a() && var2 != null) {
         if (!this.k && var2.container == var1.player.getInventory() && var2.getContainerSlot() >= 36 && var2.getContainerSlot() <= 39) {
            ItemStack[] var6 = this.U(var1);
            return var6 == null ? null : var6[40 - var2.getContainerSlot()];
         } else {
            DispenserMenu var3 = this.W(var1);
            if (var3 != null && var2.container != var1.player.getInventory() && var2.getContainerSlot() >= 0 && var2.getContainerSlot() <= 8) {
               this.s(var1);
               int var4 = this.J.a(var2.getContainerSlot());
               ItemStack var5 = var3.getSlot(var4).getItem();
               return this.P(var5, this.J.F("stock:" + var4, this.o(var5)));
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public boolean a(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.e && this.a() && !this.j && var3 >= 0 && var1 != null && var1.gameMode != null) {
         if (!this.k
            && var1.player != null
            && var1.player.containerMenu == var1.player.inventoryMenu
            && var2 == var1.player.inventoryMenu.containerId
            && var3 >= 5
            && var3 <= 8) {
            this.s(var1);
            int var6 = var3 - 4;
            int var7 = var6;
            ItemStack var8 = var1.player.containerMenu.getCarried();
            int var9 = A(var8);
            if (!var8.isEmpty() && var9 > 0 && var9 != var6 && this.J.F("player:36", var9) == var6 && var1.player.getItemBySlot(Y(var9)).isEmpty()) {
               this.j = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var9 + 4, var4, var5, var1.player);
               } finally {
                  this.j = (boolean)0;
               }

               return true;
            }

            for (int var10 = 1; var10 <= 4; var10++) {
               if (!var1.player.getItemBySlot(Y(var10)).isEmpty() && this.J.F("worn:" + var10, var10) == var6) {
                  var7 = var10;
                  break;
               }
            }

            if (var7 != var6) {
               this.j = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var7 + 4, var4, var5, var1.player);
               } finally {
                  this.j = (boolean)0;
               }

               return true;
            }

            if (!var1.player.getItemBySlot(Y(var6)).isEmpty() && this.J.F("worn:" + var6, var6) != var6) {
               return true;
            }
         }

         DispenserMenu var34 = this.W(var1);
         if (var34 == null || var34.containerId != var2) {
            return false;
         }

         if (var3 >= var34.slots.size()) {
            return false;
         }

         this.s(var1);
         if (var5 == ContainerInput.QUICK_MOVE && var34.getSlot(var3).container == var1.player.getInventory()) {
            int var35 = this.J.J("player:" + var34.getSlot(var3).getContainerSlot());
            if (var35 >= 0 && var35 < 9) {
               if (var34.getCarried().isEmpty() && var34.getSlot(var35).getItem().isEmpty() && var34.getSlot(var3).getItem().getCount() == 1) {
                  if (this.k) {
                     for (int var37 = 0; var37 < 9; var37++) {
                        if (var34.getSlot(this.J.a(var37)).getItem().isEmpty()) {
                           this.J.i("player:" + var34.getSlot(var3).getContainerSlot(), var37);
                           break;
                        }
                     }
                  }

                  this.w(var34, "home-return source=" + var3 + " destination=" + var35);
                  this.j = (boolean)1;

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
                     this.j = (boolean)0;
                  }

                  this.s(var1);
                  return true;
               }

               return true;
            }
         }

         if (var3 > 8) {
            return false;
         }

         int var36 = this.J.a(var3);
         if (var36 == var3) {
            return false;
         }

         this.j = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var36, var4, var5, var1.player);
         } finally {
            this.j = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public void s(Minecraft var1) {
      if (!this.j && this.e && this.a() && var1 != null && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.W(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.o(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.o(var6);
            if (var8 < 0) {
               this.g.remove(var7);
            }

            if (!this.g.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         if (!this.k) {
            for (int var15 = 1; var15 <= 4; var15++) {
               String var17 = "worn:" + var15;
               int var19 = this.o(var1.player.getItemBySlot(Y(var15)));
               if (var19 < 1) {
                  this.g.remove(var17);
               }

               if (!this.g.contains(var17)) {
                  var2.put(var17, var19);
               }
            }
         }

         BlockPos var16 = this.L;
         BlockState var18 = var1.level.getBlockState(var16);
         Vec3 var20 = Vec3.atCenterOf(var16);
         if (var18.is(Blocks.DROPPER) || var18.is(Blocks.DISPENSER)) {
            Direction var21 = (Direction)var18.getValue(DispenserBlock.FACING);
            var20 = var20.add(var21.getStepX() * 0.7, var21.getStepY() * 0.7, var21.getStepZ() * 0.7);
         }

         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.o(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var20) <= 2.25 ? 1 : 0;
               if (!this.D.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.W.add(var10.getUUID());
               }

               if (this.W.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.o(var10.getItem()));
                  if (var11 != 0) {
                     var3.add(var12);
                  }
               }
            }
         }

         if (!this.k) {
            for (Entity var27 : var1.level.entitiesForRendering()) {
               if (var27 instanceof ArmorStand var23 && var23.distanceToSqr(var20) < 9.0) {
                  for (int var28 = 1; var28 <= 4; var28++) {
                     String var29 = "stand:" + var23.getUUID() + ":" + var28;
                     int var13 = this.o(var23.getItemBySlot(Y(var28)));
                     if (var13 < 1) {
                        this.g.remove(var29);
                     }

                     if (!this.g.contains(var29)) {
                        var2.put(var29, var13);
                        var3.add(var29);
                     }
                  }
               }
            }
         }

         if (this.k) {
            this.J.c(var2, var3, () -> this.G.S(this.X), this.C, var4 != null);
         } else {
            this.J.v(var2, var3, this.X, this.C, var4 != null);
         }

         if (this.k && var4 != null && this.J.b()) {
            this.G.w();
         }

         for (mp.z var26 : this.J.J()) {
            this.M(var26.actual(), var26.display());
         }

         if (var4 != null) {
            this.w(var4, "snapshot");
         }
      }
   }

   private static EquipmentSlot Y(int var0) {
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
         if (var1.slot() == Y(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] U(Minecraft var1) {
      if (!this.k && this.e && this.a() && var1 != null && var1.player != null) {
         this.s(var1);
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.player.getItemBySlot(Y(var3));
            int var5 = this.J.F("worn:" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = var5 == var3 ? var4 : this.P(var4, var5);
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public ItemStack[] z(ArmorStand var1) {
      if (this.e && !this.k && this.a() && !(var1.distanceToSqr(Vec3.atCenterOf(this.L)) > 16.0)) {
         this.s(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(Y(var3));
            int var5 = this.J.F("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[this.o(var4) > 0 ? var5 : var3] = this.o(var4) > 0 ? this.P(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public EntityHitResult X(ArmorStand var1, EntityHitResult var2) {
      if (this.z(var1) == null) {
         return var2;
      }

      double var3 = (var2.getLocation().y - var1.getY()) / (var1.isSmall() ? 0.5 : 1.0);
      int var5 = var3 >= 1.6 ? 1 : (var3 >= 0.9 ? 2 : (var3 >= 0.4 ? 3 : 4));

      for (int var6 = 1; var6 <= 4; var6++) {
         if (!var1.getItemBySlot(Y(var6)).isEmpty() && this.J.F("stand:" + var1.getUUID() + ":" + var6, var6) == var5) {
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

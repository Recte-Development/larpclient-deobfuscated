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

public final class sl {
   public static final sl I = new sl(true);
   public static final sl s = new sl(false);
   private final boolean K;
   private final ab O = new ab();
   private final sa a = new sa();
   private final ItemStack[] j = new ItemStack[5];
   private int[] M = new int[9];
   private final Set<UUID> F = new HashSet<>();
   private final Set<UUID> W = new HashSet<>();
   private final Set<String> Y = new HashSet<>();
   private BlockPos D;
   private ClientLevel Z;
   private DispenserMenu v;
   private boolean Q;
   private boolean q;
   private int n = 1;
   private int r;
   private String X = "Open the assigned dispenser to detect armor";
   private String b = "";

   private void Q(DispenserMenu var1, String var2) {
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < 9; var4++) {
         int var5 = this.O.T(var4);
         var3.append(" v")
            .append(var4 + 1)
            .append("->p")
            .append(var5 + 1)
            .append(" real=")
            .append(this.B(var1.getSlot(var5).getItem()))
            .append(" shown=")
            .append(var1.getSlot(var5).getItem().isEmpty() ? -1 : this.O.Z("stock:" + var5, -1));
      }

      String var6 = var3.toString();
      if (!var2.equals("snapshot") || !var6.equals(this.b)) {
         we.r(this.f() + " " + var2 + " sync=" + var1.containerId + var6);
      }

      this.b = var6;
   }

   sl(boolean var1) {
      this.K = var1;
      Arrays.fill(this.j, ItemStack.EMPTY);
   }

   public static sl X() {
      return I.Q ? I : s;
   }

   public static sl E(hk.k var0) {
      return var0 == hk.k.k ? I : (var0 == hk.k.V ? s : null);
   }

   private hk.k w() {
      return this.K ? hk.k.k : hk.k.V;
   }

   public int w() {
      return this.K ? 3 : 4;
   }

   public boolean I() {
      return this.Q;
   }

   public int m() {
      return this.n;
   }

   public String C() {
      return this.X;
   }

   public String f() {
      return this.K ? "Horse Racing" : "Armor Game";
   }

   public String z(int var1) {
      if (var1 >= 1 && var1 <= this.w()) {
         return this.j[var1].isEmpty()
            ? (this.K ? "Horse " + var1 : new String[]{"", "Helmet", "Chestplate", "Leggings", "Boots"}[var1])
            : this.j[var1].getHoverName().getString();
      } else {
         return "";
      }
   }

   public void S(int var1) {
      if (var1 >= 1 && var1 <= this.w()) {
         this.n = var1;
      }
   }

   public String U() {
      return !this.Q ? "1" : (this.n == this.w() ? "off" : Integer.toString(this.n + 1));
   }

   public String s(int var1) {
      if (this.K) {
         return this.z(var1);
      }

      return switch (var1) {
         case 1 -> "Helmet";
         case 2 -> "Chestplate";
         case 3 -> "Leggings";
         case 4 -> "Boots";
         default -> "";
      };
   }

   public void s(boolean var1) {
      if (this.Q != var1) {
         this.Q = var1;
         this.x(Minecraft.getInstance());
      }
   }

   private boolean R() {
      return this.D != null && !this.j[1].isEmpty();
   }

   private static boolean d(ItemStack var0, ItemStack var1) {
      return !var0.isEmpty()
         && !var1.isEmpty()
         && var0.is(var1.getItem())
         && Objects.equals(var0.get(DataComponents.CUSTOM_NAME), var1.get(DataComponents.CUSTOM_NAME));
   }

   private int B(ItemStack var1) {
      for (int var2 = 1; var2 <= this.w(); var2++) {
         if (d(var1, this.j[var2])) {
            return var2;
         }
      }

      return -1;
   }

   boolean I(ItemStack[] var1) {
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
               var7 = M(var6);
               if (var7 < 1 || !var2[var7].isEmpty()) {
                  throw new IllegalArgumentException("Use one piece for each armor slot; leave other slots empty");
               }
            } else {
               if (!BuiltInRegistries.ITEM.getKey(var6.getItem()).getPath().endsWith("_horse_armor")) {
                  throw new IllegalArgumentException("Horse Racing requires horse armor");
               }

               var7 = 1;

               while (var7 <= var4 && !d(var6, var2[var7])) {
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
         sa.Y(var10);
         int[] var13 = new int[4];
         byte var15 = 1;

         for (int var8 = 1; var8 <= 3; var8++) {
            var13[var8] = this.B(var2[var8]);
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

      int var12 = !Arrays.equals(this.M, var3) ? 1 : 0;

      for (int var14 = 1; var14 <= this.w(); var14++) {
         var12 |= !ItemStack.isSameItemSameComponents(this.j[var14], var2[var14]) ? 1 : 0;
      }

      System.arraycopy(var2, 0, this.j, 0, 5);
      this.M = var3;
      return (boolean)var12;
   }

   public void k(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (var1 != null && var1.player != null && var3 != null) {
         int var4 = this.Z == var1.level && Objects.equals(this.D, var3) ? 0 : 1;
         if (var4 != 0) {
            Arrays.fill(this.j, ItemStack.EMPTY);
            this.M = new int[9];
            this.D = var3;
            this.Z = var1.level;
            this.x(var1);
         }

         this.v = var2;
         ItemStack[] var5 = new ItemStack[9];

         for (int var6 = 0; var6 < 9; var6++) {
            var5[var6] = var2.getSlot(var6).getItem();
         }

         int var15 = this.R() && this.O.Z() ? 1 : 0;

         for (ItemStack var10 : var5) {
            if (!var10.isEmpty() && (var10.getCount() != 1 || this.B(var10) < 1)) {
               var15 = 0;
            }
         }

         if (var15 != 0) {
            this.F(var1);

            for (ItemStack var24 : var5) {
               int var26 = this.B(var24);
               if (var26 > 0) {
                  this.j[var26] = var24.copyWithCount(1);
               }
            }

            this.X = "Refreshed armor; retained displayed slots";
         } else {
            try {
               this.I(var5);
               this.x(var1);
               this.v = var2;
               this.X = "Detected " + (this.K ? "9 horse armor slots" : "4 armor pieces and their slots");
               bn.j(this.f(), this.X);
            } catch (IllegalArgumentException var14) {
               byte var17 = this.R();

               for (ItemStack var12 : var5) {
                  if (!var12.isEmpty() && (var12.getCount() != 1 || this.B(var12) < 1)) {
                     var17 = 0;
                  }
               }

               if (var17 != 0) {
                  this.F(var1);

                  for (ItemStack var27 : var5) {
                     int var13 = this.B(var27);
                     if (var13 > 0) {
                        this.j[var13] = var27.copyWithCount(1);
                     }
                  }

                  this.X = "Tracking active round; detected occupied slots";
                  return;
               }

               this.X = var14.getMessage();
               Arrays.fill(this.j, ItemStack.EMPTY);
               this.M = new int[9];
               this.x(var1);
               bn.j(this.f(), "Not ready: " + this.X);
            }
         }
      }
   }

   public void k(Minecraft var1) {
      if (var1 != null && var1.level != null && this.Z == var1.level) {
         hk var2 = hk.e();
         if (this.D != null && !Objects.equals(this.D, var2.f(this.w()))) {
            this.D = null;
            Arrays.fill(this.j, ItemStack.EMPTY);
            this.M = new int[9];
            this.x(var1);
         }

         this.F(var1);
      } else {
         this.D = null;
         this.Z = var1 == null ? null : var1.level;
         Arrays.fill(this.j, ItemStack.EMPTY);
         this.M = new int[9];
         this.x(var1);
      }
   }

   private void x(Minecraft var1) {
      this.a.U();
      this.O.q(this.M);
      this.F.clear();
      this.W.clear();
      this.Y.clear();
      this.v = null;
      this.r = ThreadLocalRandom.current().nextInt(2520);
      if (var1 != null && var1.player != null) {
         for (int var2 = 0; var2 < 38; var2++) {
            if (this.B(this.k(var1, var2)) > 0) {
               this.Y.add("player:" + var2);
            }
         }

         if (!this.K) {
            for (int var6 = 1; var6 <= 4; var6++) {
               if (!var1.player.getItemBySlot(Q(var6)).isEmpty()) {
                  this.Y.add("worn:" + var6);
               }
            }
         }

         if (var1.level != null) {
            for (Entity var3 : var1.level.entitiesForRendering()) {
               this.W.add(var3.getUUID());
               if (!this.K && var3 instanceof ArmorStand var4) {
                  for (int var5 = 1; var5 <= 4; var5++) {
                     if (!var4.getItemBySlot(Q(var5)).isEmpty()) {
                        this.Y.add("stand:" + var4.getUUID() + ":" + var5);
                     }
                  }
               }
            }
         }
      }
   }

   private ItemStack k(Minecraft var1, int var2) {
      return var2 < 36 ? var1.player.getInventory().getItem(var2) : (var2 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
   }

   private DispenserMenu R(Minecraft var1) {
      hk var2 = hk.e();
      return var1 != null
            && var1.player != null
            && this.v != null
            && var1.player.containerMenu == this.v
            && var2.v() == this.v.containerId
            && var2.l() == this.w()
            && Objects.equals(this.D, var2.Q(var2.v()))
         ? this.v
         : null;
   }

   private ItemStack s(ItemStack var1, int var2) {
      return !var1.isEmpty() && var2 > 0 && var2 <= this.w() && !this.j[var2].isEmpty() ? this.j[var2].copyWithCount(var1.getCount()) : var1;
   }

   private void l(int var1, int var2) {
      bn.j(this.f(), bn.W(this.z(var1), this.z(var2), true, true));
   }

   public ItemStack V(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.Q && this.R() && var2 != null && var2.player != null && !var1.isEmpty()) {
         this.F(var2);

         for (int var3 = 0; var3 < 38; var3++) {
            if (var1 == this.k(var2, var3)) {
               return this.s(var1, this.O.Z("player:" + var3, this.B(var1)));
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public ItemStack w(ItemEntity var1, ItemStack var2) {
      if (this.Q && this.R()) {
         this.F(Minecraft.getInstance());
         return this.F.contains(var1.getUUID()) ? this.s(var2, this.O.Z("world:" + var1.getUUID(), this.B(var2))) : var2;
      } else {
         return var2;
      }
   }

   public ItemStack o(Minecraft var1, Slot var2) {
      if (this.Q && this.R() && var2 != null) {
         if (!this.K && var2.container == var1.player.getInventory() && var2.getContainerSlot() >= 36 && var2.getContainerSlot() <= 39) {
            ItemStack[] var6 = this.S(var1);
            return var6 == null ? null : var6[40 - var2.getContainerSlot()];
         } else {
            DispenserMenu var3 = this.R(var1);
            if (var3 != null && var2.container != var1.player.getInventory() && var2.getContainerSlot() >= 0 && var2.getContainerSlot() <= 8) {
               this.F(var1);
               int var4 = this.O.T(var2.getContainerSlot());
               ItemStack var5 = var3.getSlot(var4).getItem();
               return this.s(var5, this.O.Z("stock:" + var4, this.B(var5)));
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public boolean N(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.Q && this.R() && !this.q && var3 >= 0 && var1 != null && var1.gameMode != null) {
         if (!this.K
            && var1.player != null
            && var1.player.containerMenu == var1.player.inventoryMenu
            && var2 == var1.player.inventoryMenu.containerId
            && var3 >= 5
            && var3 <= 8) {
            this.F(var1);
            int var6 = var3 - 4;
            int var7 = var6;
            ItemStack var8 = var1.player.containerMenu.getCarried();
            int var9 = M(var8);
            if (!var8.isEmpty() && var9 > 0 && var9 != var6 && this.O.Z("player:36", var9) == var6 && var1.player.getItemBySlot(Q(var9)).isEmpty()) {
               this.q = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var9 + 4, var4, var5, var1.player);
               } finally {
                  this.q = (boolean)0;
               }

               return true;
            }

            for (int var10 = 1; var10 <= 4; var10++) {
               if (!var1.player.getItemBySlot(Q(var10)).isEmpty() && this.O.Z("worn:" + var10, var10) == var6) {
                  var7 = var10;
                  break;
               }
            }

            if (var7 != var6) {
               this.q = (boolean)1;

               try {
                  var1.gameMode.handleContainerInput(var2, var7 + 4, var4, var5, var1.player);
               } finally {
                  this.q = (boolean)0;
               }

               return true;
            }

            if (!var1.player.getItemBySlot(Q(var6)).isEmpty() && this.O.Z("worn:" + var6, var6) != var6) {
               return true;
            }
         }

         DispenserMenu var34 = this.R(var1);
         if (var34 == null || var34.containerId != var2) {
            return false;
         }

         if (var3 >= var34.slots.size()) {
            return false;
         }

         this.F(var1);
         if (var5 == ContainerInput.QUICK_MOVE && var34.getSlot(var3).container == var1.player.getInventory()) {
            int var35 = this.O.p("player:" + var34.getSlot(var3).getContainerSlot());
            if (var35 >= 0 && var35 < 9) {
               if (var34.getCarried().isEmpty() && var34.getSlot(var35).getItem().isEmpty() && var34.getSlot(var3).getItem().getCount() == 1) {
                  if (this.K) {
                     for (int var37 = 0; var37 < 9; var37++) {
                        if (var34.getSlot(this.O.T(var37)).getItem().isEmpty()) {
                           this.O.k("player:" + var34.getSlot(var3).getContainerSlot(), var37);
                           break;
                        }
                     }
                  }

                  this.Q(var34, "home-return source=" + var3 + " destination=" + var35);
                  this.q = (boolean)1;

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
                     this.q = (boolean)0;
                  }

                  this.F(var1);
                  return true;
               }

               return true;
            }
         }

         if (var3 > 8) {
            return false;
         }

         int var36 = this.O.T(var3);
         if (var36 == var3) {
            return false;
         }

         this.q = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var36, var4, var5, var1.player);
         } finally {
            this.q = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public void F(Minecraft var1) {
      if (!this.q && this.Q && this.R() && var1 != null && var1.player != null && var1.level != null) {
         LinkedHashMap var2 = new LinkedHashMap();
         HashSet var3 = new HashSet();
         DispenserMenu var4 = this.R(var1);
         if (var4 != null) {
            for (int var5 = 0; var5 < 9; var5++) {
               var2.put("stock:" + var5, this.B(var4.getSlot(var5).getItem()));
            }
         }

         for (int var14 = 0; var14 < 38; var14++) {
            ItemStack var6 = var14 < 36
               ? var1.player.getInventory().getItem(var14)
               : (var14 == 36 ? var1.player.containerMenu.getCarried() : var1.player.getOffhandItem());
            String var7 = "player:" + var14;
            int var8 = this.B(var6);
            if (var8 < 0) {
               this.Y.remove(var7);
            }

            if (!this.Y.contains(var7)) {
               var2.put(var7, var8);
               if (var4 == null) {
                  var3.add(var7);
               }
            }
         }

         if (!this.K) {
            for (int var15 = 1; var15 <= 4; var15++) {
               String var17 = "worn:" + var15;
               int var19 = this.B(var1.player.getItemBySlot(Q(var15)));
               if (var19 < 1) {
                  this.Y.remove(var17);
               }

               if (!this.Y.contains(var17)) {
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
            if (var9 instanceof ItemEntity var10 && !var10.isRemoved() && this.B(var10.getItem()) > 0) {
               int var11 = var10.distanceToSqr(var20) <= 2.25 ? 1 : 0;
               if (!this.W.contains(var10.getUUID()) && (var11 != 0 || var10.distanceToSqr(var1.player) <= 9.0)) {
                  this.F.add(var10.getUUID());
               }

               if (this.F.contains(var10.getUUID())) {
                  String var12 = "world:" + var10.getUUID();
                  var2.put(var12, this.B(var10.getItem()));
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
                     int var13 = this.B(var23.getItemBySlot(Q(var28)));
                     if (var13 < 1) {
                        this.Y.remove(var29);
                     }

                     if (!this.Y.contains(var29)) {
                        var2.put(var29, var13);
                        var3.add(var29);
                     }
                  }
               }
            }
         }

         if (this.K) {
            this.O.L(var2, var3, () -> this.a.B(this.n), this.r, var4 != null);
         } else {
            this.O.y(var2, var3, this.n, this.r, var4 != null);
         }

         if (this.K && var4 != null && this.O.s()) {
            this.a.U();
         }

         for (ab.x var26 : this.O.e()) {
            this.l(var26.actual(), var26.display());
         }

         if (var4 != null) {
            this.Q(var4, "snapshot");
         }
      }
   }

   private static EquipmentSlot Q(int var0) {
      return switch (var0) {
         case 1 -> EquipmentSlot.HEAD;
         case 2 -> EquipmentSlot.CHEST;
         case 3 -> EquipmentSlot.LEGS;
         default -> EquipmentSlot.FEET;
      };
   }

   private static int M(ItemStack var0) {
      Equippable var1 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      if (var1 == null) {
         return -1;
      }

      for (int var2 = 1; var2 <= 4; var2++) {
         if (var1.slot() == Q(var2)) {
            return var2;
         }
      }

      return -1;
   }

   public ItemStack[] S(Minecraft var1) {
      if (!this.K && this.Q && this.R() && var1 != null && var1.player != null) {
         this.F(var1);
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.player.getItemBySlot(Q(var3));
            int var5 = this.O.Z("worn:" + var3, var3);
            if (!var4.isEmpty()) {
               var2[var5] = var5 == var3 ? var4 : this.s(var4, var5);
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public ItemStack[] d(ArmorStand var1) {
      if (this.Q && !this.K && this.R() && !(var1.distanceToSqr(Vec3.atCenterOf(this.D)) > 16.0)) {
         this.F(Minecraft.getInstance());
         ItemStack[] var2 = new ItemStack[5];
         Arrays.fill(var2, ItemStack.EMPTY);

         for (int var3 = 1; var3 <= 4; var3++) {
            ItemStack var4 = var1.getItemBySlot(Q(var3));
            int var5 = this.O.Z("stand:" + var1.getUUID() + ":" + var3, var3);
            if (!var4.isEmpty()) {
               var2[this.B(var4) > 0 ? var5 : var3] = this.B(var4) > 0 ? this.s(var4, var5) : var4;
            }
         }

         return var2;
      } else {
         return null;
      }
   }

   public EntityHitResult N(ArmorStand var1, EntityHitResult var2) {
      if (this.d(var1) == null) {
         return var2;
      }

      double var3 = (var2.getLocation().y - var1.getY()) / (var1.isSmall() ? 0.5 : 1.0);
      int var5 = var3 >= 1.6 ? 1 : (var3 >= 0.9 ? 2 : (var3 >= 0.4 ? 3 : 4));

      for (int var6 = 1; var6 <= 4; var6++) {
         if (!var1.getItemBySlot(Q(var6)).isEmpty() && this.O.Z("stand:" + var1.getUUID() + ":" + var6, var6) == var5) {
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

package com.larpclient;

import java.util.stream.IntStream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class zv {
   private static final zv z = new zv();
   public Item e = Items.STONE;
   public Item k = Items.COBBLESTONE;
   public Item n = Items.DIAMOND;
   public zv.u V = zv.u.D;
   public boolean Q;
   private final k2 N = new k2(true);
   private final zv.u[] r = new zv.u[9];
   private boolean v;
   private boolean g;

   public zv() {
      this.g();
   }

   public static zv x() {
      return z;
   }

   public void c() {
      this.V = this.V.P();
   }

   public Item n() {
      return switch (this.V) {
         case D -> this.e;
         case U -> this.k;
         case J -> this.n;
      };
   }

   public boolean Q(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.N.b = this.V.ordinal() + 1;
      if (var2 != null && var1 != null) {
         if (this.N.X() && var2.equals(this.N.X(0)) && this.N.x(var1)) {
            this.N.Y(var3, 0, true);
            return true;
         }

         Item var4 = null;
         Item var5 = null;
         Item var6 = null;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            ItemStack var9 = var3.getSlot(var8).getItem();
            if (!var9.isEmpty()) {
               var7++;
               if (var8 == 4) {
                  var6 = var9.getItem();
               } else if (var4 == null) {
                  var4 = var9.getItem();
               } else if (var9.getItem() != var4 && var5 == null) {
                  var5 = var9.getItem();
               }
            }
         }

         if (var7 == 0) {
            return false;
         }

         if (!this.N.X() || !var2.equals(this.N.X(0)) || var7 == 9) {
            if (var4 != null) {
               this.e = var4;
            }

            if (var5 != null) {
               this.k = var5;
            }

            if (var6 != null) {
               this.n = var6;
            }
         }

         for (int var10 = 0; var10 < 9; var10++) {
            ItemStack var11 = var3.getSlot(var10).getItem();
            if (!var11.isEmpty()) {
               this.J(var10, var10 == 4 ? zv.u.J : (var11.is(this.k) ? zv.u.U : zv.u.D));
            }
         }

         this.N.f(var1, var2, var3, this.e, this.k, this.n, this.W());
         return true;
      } else {
         return false;
      }
   }

   public void g() {
      this.j(new int[]{1, 1, 2, 1, 3, 2, 1, 2, 2});
   }

   public zv.u h(int var1) {
      return var1 >= 0 && var1 < 9 ? this.r[var1] : null;
   }

   public void J(int var1, zv.u var2) {
      if (var1 >= 0 && var1 < 9) {
         this.r[var1] = var2;
      }
   }

   public void R(int var1) {
      this.J(var1, null);
   }

   public int[] W() {
      int[] var1 = new int[9];

      for (int var2 = 0; var2 < 9; var2++) {
         var1[var2] = this.r[var2] == null ? 0 : this.r[var2].ordinal() + 1;
      }

      return var1;
   }

   public void j(int[] var1) {
      if (var1 != null && var1.length == 9) {
         for (int var2 = 0; var2 < 9; var2++) {
            this.r[var2] = var1[var2] >= 1 && var1[var2] <= 3 ? zv.u.values()[var1[var2] - 1] : null;
         }
      } else {
         this.g();
      }
   }

   public int[] w(zv.u var1) {
      return IntStream.range(0, 9).filter(var2 -> var1 != null && this.r[var2] == var1).toArray();
   }

   public void e() {
   }

   public ItemStack o(ItemStack var1, boolean var2) {
      return this.g(var1);
   }

   public String U(ItemStack var1, String var2) {
      return var2;
   }

   public static ItemStack s(Item var0, ItemStack var1, boolean var2) {
      ItemStack var3 = new ItemStack(var0, var1.getCount());

      for (int var4 = 1; var4 <= 3; var4++) {
         ItemStack var5 = z.N.Q(var4);
         if (var5.is(var0)) {
            var3 = var5.copyWithCount(var1.getCount());
            break;
         }
      }

      if (var2 && var1.has(DataComponents.CUSTOM_NAME)) {
         var3.set(DataComponents.CUSTOM_NAME, (Component)var1.get(DataComponents.CUSTOM_NAME));
      }

      return var3;
   }

   private boolean H(Minecraft var1, DispenserMenu var2) {
      ep var3 = ep.H();
      BlockPos var4 = this.N.X(0);
      return this.Q
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.V()
         && var3.m(var1, var4).equals(var3.b());
   }

   public void m(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (!this.Q) {
            if (this.g) {
               this.N.I(var1);
            }

            this.g = (boolean)0;
         } else {
            this.g = (boolean)1;
            this.N.b = this.V.ordinal() + 1;
            this.N.C(var1);
         }
      } else {
         if (this.g && var1 != null) {
            this.N.I(var1);
         }

         this.g = (boolean)0;
      }
   }

   public ItemStack g(ItemStack var1) {
      return this.Q && var1 != null && !var1.isEmpty() ? this.N.u(var1) : var1;
   }

   public ItemStack M(ItemStack var1) {
      return this.g(var1);
   }

   public boolean k(ItemStack var1) {
      return (var1.is(Items.WATER_BUCKET) || var1.is(Items.LAVA_BUCKET)) && this.g(var1).is(Items.TOTEM_OF_UNDYING);
   }

   public ItemStack H(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.g(var2) : var2;
   }

   public ItemStack x(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.Q && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.N.A(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack s(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.H(var1, var3)
         ? this.N.W(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean w(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.v
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9
         && this.H(var1, var6)) {
         int var12 = this.N.P(var1, var6, 0, var3);
         int var8 = var5 == ContainerInput.SWAP && var4 >= 0 && var4 < 9 ? var4 : (var5 == ContainerInput.PICKUP && var4 == 0 ? 36 : -1);
         if (var8 >= 0 && this.N.n(var1, var6, var12, var8)) {
            return true;
         }

         if (var12 == var3) {
            return false;
         }

         this.v = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var12, var4, var5, var1.player);
         } finally {
            this.v = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public enum u {
      D("1st"),
      U("2nd"),
      J("Middle");

      public final String t;

      u(String var3) {
         this.t = var3;
      }

      public zv.u P() {
         return switch (this) {
            case D -> J;
            case U -> D;
            case J -> U;
         };
      }
   }
}

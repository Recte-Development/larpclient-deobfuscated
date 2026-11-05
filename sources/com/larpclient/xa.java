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

public class xa {
   private static final xa v = new xa();
   public Item c = Items.STONE;
   public Item N = Items.COBBLESTONE;
   public Item b = Items.DIAMOND;
   public xa.i Y = xa.i.y;
   public boolean m;
   private final hf X = new hf(true);
   private final xa.i[] a = new xa.i[9];
   private boolean O;
   private boolean g;

   public xa() {
      this.J();
   }

   public static xa r() {
      return v;
   }

   public void Y() {
      this.Y = this.Y.d();
   }

   public Item o() {
      return switch (this.Y) {
         case y -> this.c;
         case Y -> this.N;
         case J -> this.b;
      };
   }

   public boolean U(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.X.M = this.Y.ordinal() + 1;
      if (var2 != null && var1 != null) {
         if (this.X.E() && var2.equals(this.X.t(0)) && this.X.y(var1)) {
            this.X.c(var3, 0, true);
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

         if (!this.X.E() || !var2.equals(this.X.t(0)) || var7 == 9) {
            if (var4 != null) {
               this.c = var4;
            }

            if (var5 != null) {
               this.N = var5;
            }

            if (var6 != null) {
               this.b = var6;
            }
         }

         for (int var10 = 0; var10 < 9; var10++) {
            ItemStack var11 = var3.getSlot(var10).getItem();
            if (!var11.isEmpty()) {
               this.z(var10, var10 == 4 ? xa.i.J : (var11.is(this.N) ? xa.i.Y : xa.i.y));
            }
         }

         this.X.D(var1, var2, var3, this.c, this.N, this.b, this.u());
         return true;
      } else {
         return false;
      }
   }

   public void J() {
      this.Z(new int[]{1, 1, 2, 1, 3, 2, 1, 2, 2});
   }

   public xa.i G(int var1) {
      return var1 >= 0 && var1 < 9 ? this.a[var1] : null;
   }

   public void z(int var1, xa.i var2) {
      if (var1 >= 0 && var1 < 9) {
         this.a[var1] = var2;
      }
   }

   public void l(int var1) {
      this.z(var1, null);
   }

   public int[] u() {
      int[] var1 = new int[9];

      for (int var2 = 0; var2 < 9; var2++) {
         var1[var2] = this.a[var2] == null ? 0 : this.a[var2].ordinal() + 1;
      }

      return var1;
   }

   public void Z(int[] var1) {
      if (var1 != null && var1.length == 9) {
         for (int var2 = 0; var2 < 9; var2++) {
            this.a[var2] = var1[var2] >= 1 && var1[var2] <= 3 ? xa.i.values()[var1[var2] - 1] : null;
         }
      } else {
         this.J();
      }
   }

   public int[] h(xa.i var1) {
      return IntStream.range(0, 9).filter(var2 -> var1 != null && this.a[var2] == var1).toArray();
   }

   public void j() {
   }

   public ItemStack d(ItemStack var1, boolean var2) {
      return this.I(var1);
   }

   public String B(ItemStack var1, String var2) {
      return var2;
   }

   public static ItemStack x(Item var0, ItemStack var1, boolean var2) {
      ItemStack var3 = new ItemStack(var0, var1.getCount());

      for (int var4 = 1; var4 <= 3; var4++) {
         ItemStack var5 = v.X.k(var4);
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

   private boolean B(Minecraft var1, DispenserMenu var2) {
      f9 var3 = f9.W();
      BlockPos var4 = this.X.t(0);
      return this.m
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.i()
         && var3.x(var1, var4).equals(var3.v());
   }

   public void Z(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (!this.m) {
            if (this.g) {
               this.X.a(var1);
            }

            this.g = (boolean)0;
         } else {
            this.g = (boolean)1;
            this.X.M = this.Y.ordinal() + 1;
            this.X.b(var1);
         }
      } else {
         if (this.g && var1 != null) {
            this.X.a(var1);
         }

         this.g = (boolean)0;
      }
   }

   public ItemStack I(ItemStack var1) {
      return this.m && var1 != null && !var1.isEmpty() ? this.X.j(var1) : var1;
   }

   public ItemStack f(ItemStack var1) {
      return this.I(var1);
   }

   public boolean v(ItemStack var1) {
      return (var1.is(Items.WATER_BUCKET) || var1.is(Items.LAVA_BUCKET)) && this.I(var1).is(Items.TOTEM_OF_UNDYING);
   }

   public ItemStack s(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.I(var2) : var2;
   }

   public ItemStack R(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.m && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.X.Y(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack a(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.B(var1, var3)
         ? this.X.Q(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean x(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.O
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9
         && this.B(var1, var6)) {
         int var12 = this.X.L(var1, var6, 0, var3);
         int var8 = var5 == ContainerInput.SWAP && var4 >= 0 && var4 < 9 ? var4 : (var5 == ContainerInput.PICKUP && var4 == 0 ? 36 : -1);
         if (var8 >= 0 && this.X.L(var1, var6, var12, var8)) {
            return true;
         }

         if (var12 == var3) {
            return false;
         }

         this.O = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var12, var4, var5, var1.player);
         } finally {
            this.O = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public enum i {
      y("1st"),
      Y("2nd"),
      J("Middle");

      public final String Q;

      i(String var3) {
         this.Q = var3;
      }

      public xa.i d() {
         return switch (this) {
            case y -> J;
            case Y -> y;
            case J -> Y;
         };
      }
   }
}

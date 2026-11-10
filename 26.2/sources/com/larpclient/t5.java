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

public class t5 {
   private static final t5 y = new t5();
   public Item F = Items.STONE;
   public Item m = Items.COBBLESTONE;
   public Item X = Items.DIAMOND;
   public t5.o b = t5.o.i;
   public boolean g;
   private final gs h = new gs(true);
   private final t5.o[] v = new t5.o[9];
   private boolean Y;
   private boolean z;

   public t5() {
      this.K();
   }

   public static t5 O() {
      return y;
   }

   public void h() {
      this.b = this.b.I();
   }

   public Item B() {
      return switch (this.b) {
         case i -> this.F;
         case E -> this.m;
         case B -> this.X;
      };
   }

   public boolean y(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.h.Q = this.b.ordinal() + 1;
      if (var2 != null && var1 != null) {
         if (this.h.O() && var2.equals(this.h.Y(0)) && this.h.C(var1)) {
            this.h.x(var3, 0, true);
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

         if (!this.h.O() || !var2.equals(this.h.Y(0)) || var7 == 9) {
            if (var4 != null) {
               this.F = var4;
            }

            if (var5 != null) {
               this.m = var5;
            }

            if (var6 != null) {
               this.X = var6;
            }
         }

         for (int var10 = 0; var10 < 9; var10++) {
            ItemStack var11 = var3.getSlot(var10).getItem();
            if (!var11.isEmpty()) {
               this.J(var10, var10 == 4 ? t5.o.B : (var11.is(this.m) ? t5.o.E : t5.o.i));
            }
         }

         this.h.x(var1, var2, var3, this.F, this.m, this.X, this.E());
         return true;
      } else {
         return false;
      }
   }

   public void K() {
      this.A(new int[]{1, 1, 2, 1, 3, 2, 1, 2, 2});
   }

   public t5.o t(int var1) {
      return var1 >= 0 && var1 < 9 ? this.v[var1] : null;
   }

   public void J(int var1, t5.o var2) {
      if (var1 >= 0 && var1 < 9) {
         this.v[var1] = var2;
      }
   }

   public void I(int var1) {
      this.J(var1, null);
   }

   public int[] E() {
      int[] var1 = new int[9];

      for (int var2 = 0; var2 < 9; var2++) {
         var1[var2] = this.v[var2] == null ? 0 : this.v[var2].ordinal() + 1;
      }

      return var1;
   }

   public void A(int[] var1) {
      if (var1 != null && var1.length == 9) {
         for (int var2 = 0; var2 < 9; var2++) {
            this.v[var2] = var1[var2] >= 1 && var1[var2] <= 3 ? t5.o.values()[var1[var2] - 1] : null;
         }
      } else {
         this.K();
      }
   }

   public int[] t(t5.o var1) {
      return IntStream.range(0, 9).filter(var2 -> var1 != null && this.v[var2] == var1).toArray();
   }

   public void r() {
   }

   public ItemStack X(ItemStack var1, boolean var2) {
      return this.U(var1);
   }

   public String v(ItemStack var1, String var2) {
      return var2;
   }

   public static ItemStack m(Item var0, ItemStack var1, boolean var2) {
      ItemStack var3 = new ItemStack(var0, var1.getCount());

      for (int var4 = 1; var4 <= 3; var4++) {
         ItemStack var5 = y.h.H(var4);
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

   private boolean p(Minecraft var1, DispenserMenu var2) {
      gj var3 = gj.c();
      BlockPos var4 = this.h.Y(0);
      return this.g
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.r()
         && var3.w(var1, var4).equals(var3.C());
   }

   public void O(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (!this.g) {
            if (this.z) {
               this.h.O(var1);
            }

            this.z = (boolean)0;
         } else {
            this.z = (boolean)1;
            this.h.Q = this.b.ordinal() + 1;
            this.h.z(var1);
         }
      } else {
         if (this.z && var1 != null) {
            this.h.O(var1);
         }

         this.z = (boolean)0;
      }
   }

   public ItemStack U(ItemStack var1) {
      return this.g && var1 != null && !var1.isEmpty() ? this.h.N(var1) : var1;
   }

   public ItemStack k(ItemStack var1) {
      return this.U(var1);
   }

   public boolean y(ItemStack var1) {
      return (var1.is(Items.WATER_BUCKET) || var1.is(Items.LAVA_BUCKET)) && this.U(var1).is(Items.TOTEM_OF_UNDYING);
   }

   public ItemStack O(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.U(var2) : var2;
   }

   public ItemStack z(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.g && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.h.V(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack m(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.p(var1, var3)
         ? this.h.U(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean J(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.Y
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9
         && this.p(var1, var6)) {
         int var12 = this.h.V(var1, var6, 0, var3);
         int var8 = var5 == ContainerInput.SWAP && var4 >= 0 && var4 < 9 ? var4 : (var5 == ContainerInput.PICKUP && var4 == 0 ? 36 : -1);
         if (var8 >= 0 && this.h.i(var1, var6, var12, var8)) {
            return true;
         }

         if (var12 == var3) {
            return false;
         }

         this.Y = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var12, var4, var5, var1.player);
         } finally {
            this.Y = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public enum o {
      i("1st"),
      E("2nd"),
      B("Middle");

      public final String G;

      o(String var3) {
         this.G = var3;
      }

      public t5.o I() {
         return switch (this) {
            case i -> B;
            case E -> i;
            case B -> E;
         };
      }
   }
}

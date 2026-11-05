package com.larpclient;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class js {
   private static final js p = new js();
   public Item E = Items.TNT;
   public Item Z = Items.PAPER;
   public boolean P;
   public boolean o;
   private final hf U = new hf(true, true);
   private boolean u;
   private boolean H;

   public static js A() {
      return p;
   }

   public void C() {
      this.o = (boolean)1;
      this.U.M = 2;
   }

   public boolean y(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.U.M = this.o ? 2 : 1;
      if (var2 != null && var1 != null) {
         try {
            this.U.s(var1, var2, var3);
         } catch (IllegalArgumentException var5) {
            return false;
         }

         this.Z = this.U.k(1).getItem();
         this.E = this.U.k(2).getItem();
         return true;
      } else {
         return false;
      }
   }

   private boolean Z(Minecraft var1, DispenserMenu var2) {
      f9 var3 = f9.W();
      BlockPos var4 = this.U.t(0);
      return this.P
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.i()
         && var3.x(var1, var4).equals(var3.v());
   }

   public void J(Minecraft var1) {
      if (!this.u) {
         if (var1 != null && var1.player != null && var1.level != null) {
            if (!this.P) {
               if (this.H) {
                  this.U.a(var1);
               }

               this.H = (boolean)0;
            } else {
               this.H = (boolean)1;
               if (this.U.v()) {
                  this.o = (boolean)0;
               }

               this.U.M = this.o ? 2 : 1;
               this.U.b(var1);
               if (this.U.v()) {
                  this.o = (boolean)0;
               }
            }
         } else {
            if (this.H && var1 != null) {
               this.U.a(var1);
            }

            this.H = (boolean)0;
         }
      }
   }

   public ItemStack T(ItemStack var1) {
      return this.P && var1 != null && !var1.isEmpty() ? this.U.j(var1) : var1;
   }

   public ItemStack B(ItemStack var1) {
      return this.T(var1);
   }

   public ItemStack w(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.T(var2) : var2;
   }

   public ItemStack Y(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.P && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.U.Y(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack u(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.Z(var1, var3)
         ? this.U.Q(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean X(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.u
         || var1 == null
         || var1.player == null
         || var1.gameMode == null
         || !(
            var1.player.containerMenu instanceof DispenserMenu var6 && var6.containerId == var2 && var3 >= 0 && var3 < var6.slots.size() && this.Z(var1, var6)
         )) {
         return false;
      }

      if (var5 == ContainerInput.QUICK_MOVE && var6.getSlot(var3).container == var1.player.getInventory()) {
         int var18 = var6.getSlot(var3).getContainerSlot();
         int var8 = this.U.I(var1, var18);
         if (var8 < 0 || var8 >= 9) {
            return false;
         }

         if (var6.getCarried().isEmpty() && var6.getSlot(var8).getItem().isEmpty() && var6.getSlot(var3).getItem().getCount() == 1) {
            this.U.E(var6, var18);
            this.u = (boolean)1;

            try {
               if (var18 < 9) {
                  var1.gameMode.handleContainerInput(var2, var8, var18, ContainerInput.SWAP, var1.player);
               } else {
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var8, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
               }
            } finally {
               this.u = (boolean)0;
            }

            this.U.b(var1);
            return true;
         } else {
            return true;
         }
      } else {
         if (var3 >= 9) {
            return false;
         }

         int var17 = this.U.L(var1, var6, 0, var3);
         if (var17 == var3) {
            return false;
         }

         this.u = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var17, var4, var5, var1.player);
         } finally {
            this.u = (boolean)0;
         }

         return true;
      }
   }
}

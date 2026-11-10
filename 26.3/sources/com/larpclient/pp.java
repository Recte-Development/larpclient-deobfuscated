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

public class pp {
   private static final pp b = new pp();
   public Item E = Items.TNT;
   public Item n = Items.PAPER;
   public boolean z;
   public boolean g;
   private final ns T = new ns(true, true);
   private boolean y;
   private boolean v;

   public static pp A() {
      return b;
   }

   public void z() {
      this.g = (boolean)1;
      this.T.u = 2;
   }

   public boolean H(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.T.u = this.g ? 2 : 1;
      if (var2 != null && var1 != null) {
         try {
            this.T.A(var1, var2, var3);
         } catch (IllegalArgumentException var5) {
            return false;
         }

         this.n = this.T.U(1).getItem();
         this.E = this.T.U(2).getItem();
         return true;
      } else {
         return false;
      }
   }

   private boolean U(Minecraft var1, DispenserMenu var2) {
      hk var3 = hk.e();
      BlockPos var4 = this.T.h(0);
      return this.z
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.v()
         && var3.v(var1, var4).equals(var3.v());
   }

   public void F(Minecraft var1) {
      if (!this.y) {
         if (var1 != null && var1.player != null && var1.level != null) {
            if (!this.z) {
               if (this.v) {
                  this.T.G(var1);
               }

               this.v = (boolean)0;
            } else {
               this.v = (boolean)1;
               if (this.T.D()) {
                  this.g = (boolean)0;
               }

               this.T.u = this.g ? 2 : 1;
               this.T.Z(var1);
               if (this.T.D()) {
                  this.g = (boolean)0;
               }
            }
         } else {
            if (this.v && var1 != null) {
               this.T.G(var1);
            }

            this.v = (boolean)0;
         }
      }
   }

   public ItemStack F(ItemStack var1) {
      return this.z && var1 != null && !var1.isEmpty() ? this.T.q(var1) : var1;
   }

   public ItemStack u(ItemStack var1) {
      return this.F(var1);
   }

   public ItemStack C(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.F(var2) : var2;
   }

   public ItemStack Y(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.z && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.T.H(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack P(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.U(var1, var3)
         ? this.T.Q(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean p(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.y
         || var1 == null
         || var1.player == null
         || var1.gameMode == null
         || !(
            var1.player.containerMenu instanceof DispenserMenu var6 && var6.containerId == var2 && var3 >= 0 && var3 < var6.slots.size() && this.U(var1, var6)
         )) {
         return false;
      }

      if (var5 == ContainerInput.QUICK_MOVE && var6.getSlot(var3).container == var1.player.getInventory()) {
         int var18 = var6.getSlot(var3).getContainerSlot();
         int var8 = this.T.m(var1, var18);
         if (var8 < 0 || var8 >= 9) {
            return false;
         }

         if (var6.getCarried().isEmpty() && var6.getSlot(var8).getItem().isEmpty() && var6.getSlot(var3).getItem().getCount() == 1) {
            this.T.u(var6, var18);
            this.y = (boolean)1;

            try {
               if (var18 < 9) {
                  var1.gameMode.handleContainerInput(var2, var8, var18, ContainerInput.SWAP, var1.player);
               } else {
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var8, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
               }
            } finally {
               this.y = (boolean)0;
            }

            this.T.Z(var1);
            return true;
         } else {
            return true;
         }
      } else {
         if (var3 >= 9) {
            return false;
         }

         int var17 = this.T.F(var1, var6, 0, var3);
         if (var17 == var3) {
            return false;
         }

         this.y = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var17, var4, var5, var1.player);
         } finally {
            this.y = (boolean)0;
         }

         return true;
      }
   }
}

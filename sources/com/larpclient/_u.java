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

public class _u {
   private static final _u j = new _u();
   public Item z = Items.TNT;
   public Item l = Items.PAPER;
   public boolean K;
   public boolean J;
   private final k2 Z = new k2(true, true);
   private boolean f;
   private boolean g;

   public static _u O() {
      return j;
   }

   public void e() {
      this.J = (boolean)1;
      this.Z.b = 2;
   }

   public boolean w(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.Z.b = this.J ? 2 : 1;
      if (var2 != null && var1 != null) {
         try {
            this.Z.e(var1, var2, var3);
         } catch (IllegalArgumentException var5) {
            return false;
         }

         this.l = this.Z.Q(1).getItem();
         this.z = this.Z.Q(2).getItem();
         return true;
      } else {
         return false;
      }
   }

   private boolean Q(Minecraft var1, DispenserMenu var2) {
      ep var3 = ep.H();
      BlockPos var4 = this.Z.X(0);
      return this.K
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.V()
         && var3.m(var1, var4).equals(var3.b());
   }

   public void U(Minecraft var1) {
      if (!this.f) {
         if (var1 != null && var1.player != null && var1.level != null) {
            if (!this.K) {
               if (this.g) {
                  this.Z.I(var1);
               }

               this.g = (boolean)0;
            } else {
               this.g = (boolean)1;
               if (this.Z.W()) {
                  this.J = (boolean)0;
               }

               this.Z.b = this.J ? 2 : 1;
               this.Z.C(var1);
               if (this.Z.W()) {
                  this.J = (boolean)0;
               }
            }
         } else {
            if (this.g && var1 != null) {
               this.Z.I(var1);
            }

            this.g = (boolean)0;
         }
      }
   }

   public ItemStack M(ItemStack var1) {
      return this.K && var1 != null && !var1.isEmpty() ? this.Z.u(var1) : var1;
   }

   public ItemStack u(ItemStack var1) {
      return this.M(var1);
   }

   public ItemStack P(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.M(var2) : var2;
   }

   public ItemStack v(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.K && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.Z.A(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack H(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.Q(var1, var3)
         ? this.Z.W(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean x(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.f
         || var1 == null
         || var1.player == null
         || var1.gameMode == null
         || !(
            var1.player.containerMenu instanceof DispenserMenu var6 && var6.containerId == var2 && var3 >= 0 && var3 < var6.slots.size() && this.Q(var1, var6)
         )) {
         return false;
      }

      if (var5 == ContainerInput.QUICK_MOVE && var6.getSlot(var3).container == var1.player.getInventory()) {
         int var18 = var6.getSlot(var3).getContainerSlot();
         int var8 = this.Z.Z(var1, var18);
         if (var8 < 0 || var8 >= 9) {
            return false;
         }

         if (var6.getCarried().isEmpty() && var6.getSlot(var8).getItem().isEmpty() && var6.getSlot(var3).getItem().getCount() == 1) {
            this.Z.P(var6, var18);
            this.f = (boolean)1;

            try {
               if (var18 < 9) {
                  var1.gameMode.handleContainerInput(var2, var8, var18, ContainerInput.SWAP, var1.player);
               } else {
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var8, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
               }
            } finally {
               this.f = (boolean)0;
            }

            this.Z.C(var1);
            return true;
         } else {
            return true;
         }
      } else {
         if (var3 >= 9) {
            return false;
         }

         int var17 = this.Z.P(var1, var6, 0, var3);
         if (var17 == var3) {
            return false;
         }

         this.f = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var17, var4, var5, var1.player);
         } finally {
            this.f = (boolean)0;
         }

         return true;
      }
   }
}

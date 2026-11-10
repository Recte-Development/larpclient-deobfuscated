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

public class pq {
   private static final pq h = new pq();
   public Item Y = Items.TNT;
   public Item c = Items.PAPER;
   public boolean s;
   public boolean a;
   private final gs Z = new gs(true, true);
   private boolean n;
   private boolean O;

   public static pq Q() {
      return h;
   }

   public void g() {
      this.a = (boolean)1;
      this.Z.Q = 2;
   }

   public boolean s(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.Z.Q = this.a ? 2 : 1;
      if (var2 != null && var1 != null) {
         try {
            this.Z.b(var1, var2, var3);
         } catch (IllegalArgumentException var5) {
            return false;
         }

         this.c = this.Z.H(1).getItem();
         this.Y = this.Z.H(2).getItem();
         return true;
      } else {
         return false;
      }
   }

   private boolean m(Minecraft var1, DispenserMenu var2) {
      gj var3 = gj.c();
      BlockPos var4 = this.Z.Y(0);
      return this.s
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.r()
         && var3.w(var1, var4).equals(var3.C());
   }

   public void Q(Minecraft var1) {
      if (!this.n) {
         if (var1 != null && var1.player != null && var1.level != null) {
            if (!this.s) {
               if (this.O) {
                  this.Z.O(var1);
               }

               this.O = (boolean)0;
            } else {
               this.O = (boolean)1;
               if (this.Z.C()) {
                  this.a = (boolean)0;
               }

               this.Z.Q = this.a ? 2 : 1;
               this.Z.z(var1);
               if (this.Z.C()) {
                  this.a = (boolean)0;
               }
            }
         } else {
            if (this.O && var1 != null) {
               this.Z.O(var1);
            }

            this.O = (boolean)0;
         }
      }
   }

   public ItemStack P(ItemStack var1) {
      return this.s && var1 != null && !var1.isEmpty() ? this.Z.N(var1) : var1;
   }

   public ItemStack G(ItemStack var1) {
      return this.P(var1);
   }

   public ItemStack I(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.P(var2) : var2;
   }

   public ItemStack f(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.s && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.Z.V(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack Y(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.m(var1, var3)
         ? this.Z.U(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean O(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.n
         || var1 == null
         || var1.player == null
         || var1.gameMode == null
         || !(
            var1.player.containerMenu instanceof DispenserMenu var6 && var6.containerId == var2 && var3 >= 0 && var3 < var6.slots.size() && this.m(var1, var6)
         )) {
         return false;
      }

      if (var5 == ContainerInput.QUICK_MOVE && var6.getSlot(var3).container == var1.player.getInventory()) {
         int var18 = var6.getSlot(var3).getContainerSlot();
         int var8 = this.Z.l(var1, var18);
         if (var8 < 0 || var8 >= 9) {
            return false;
         }

         if (var6.getCarried().isEmpty() && var6.getSlot(var8).getItem().isEmpty() && var6.getSlot(var3).getItem().getCount() == 1) {
            this.Z.q(var6, var18);
            this.n = (boolean)1;

            try {
               if (var18 < 9) {
                  var1.gameMode.handleContainerInput(var2, var8, var18, ContainerInput.SWAP, var1.player);
               } else {
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var8, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
               }
            } finally {
               this.n = (boolean)0;
            }

            this.Z.z(var1);
            return true;
         } else {
            return true;
         }
      } else {
         if (var3 >= 9) {
            return false;
         }

         int var17 = this.Z.V(var1, var6, 0, var3);
         if (var17 == var3) {
            return false;
         }

         this.n = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var17, var4, var5, var1.player);
         } finally {
            this.n = (boolean)0;
         }

         return true;
      }
   }
}

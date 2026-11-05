package com.larpclient.module;

import com.larpclient.r4;
import java.util.BitSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestStealerModule extends Module {
   private final lc n = this.addSetting(new lc("Delay", "Ticks between items", 2.0, 0.0, 10.0, 1.0));
   private int c;
   private ChestMenu u;
   private ItemStack[] S;
   private final BitSet d = new BitSet();
   private boolean j;

   public ChestStealerModule() {
      super("ChestStealer", "Automatically loot chests", Category.PLAYER, -1);
   }

   @Override
   public void onDisable() {
      this.R();
   }

   private void R() {
      this.c = 0;
      this.u = null;
      this.S = null;
      this.d.clear();
      this.j = (boolean)0;
   }

   private void z(ChestMenu var1) {
      if (var1 != this.u) {
         this.R();
         this.u = var1;
      }

      int var2 = this.S != null && this.S.length == var1.slots.size() ? 0 : 1;
      if (var2 == 0) {
         for (int var3 = 0; var3 < this.S.length; var3++) {
            if (!ItemStack.matches(this.S[var3], var1.getSlot(var3).getItem())) {
               var2 = 1;
               break;
            }
         }
      }

      if (var2 != 0) {
         this.S = new ItemStack[var1.slots.size()];

         for (int var4 = 0; var4 < this.S.length; var4++) {
            this.S[var4] = var1.getSlot(var4).getItem().copy();
         }

         this.d.clear();
         this.j = (boolean)0;
      }
   }

   private boolean e(ChestMenu var1, ItemStack var2, int var3) {
      for (int var4 = var3; var4 < var1.slots.size(); var4++) {
         Slot var5 = var1.getSlot(var4);
         ItemStack var6 = var5.getItem();
         if (var6.isEmpty()) {
            if (var5.mayPlace(var2) && var5.getMaxStackSize(var2) > 0) {
               return true;
            }
         } else if (var2.isStackable() && ItemStack.isSameItemSameComponents(var2, var6) && var6.getCount() < var5.getMaxStackSize(var6)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         AbstractContainerMenu var2 = mc.player.containerMenu;
         int var10000 = var2 instanceof ChestMenu;
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            ChestMenu var1 = (ChestMenu)var2;
            if (AutoSellModule.Q(var1)) {
               this.R();
               return;
            }

            this.z(var1);
            boolean var10 = false;
            int var9 = this.c;
            if ((-Integer.signum(var9) >>> 31 ^ 1) == 0) {
               byte var12 = 1;
               int var11 = this.c;
               this.c = var11 - var12;
               return;
            }

            if (mc.gameMode == null) {
               return;
            }

            int var15 = var1.getRowCount() * 9;
            byte var3 = 0;

            for (int var4 = 0; var4 < var15; var4++) {
               Slot var5 = var1.getSlot(var4);
               ItemStack var6 = var5.getItem();
               if (!var6.isEmpty()) {
                  var3 = 1;
                  if (!this.d.get(var4) && var5.mayPickup(mc.player) && this.e(var1, var6, var15)) {
                     this.d.set(var4);
                     mc.gameMode.handleContainerInput(var1.containerId, var4, 0, ContainerInput.QUICK_MOVE, mc.player);
                     this.c = this.n.getValue().intValue();
                     return;
                  }
               }
            }

            if (var3 == 0) {
               mc.player.closeContainer();
               this.R();
            } else {
               boolean var14 = false;
               byte var13 = this.j;
               var10000 = Integer.signum(var13);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  this.j = (boolean)1;
                  r4.g("ChestStealer", "Waiting for inventory capacity or container update");
               }
            }

            return;
         }
      }

      this.R();
   }
}

package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.qg;
import java.util.BitSet;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestStealerModule extends Module {
   private static final int z = 2;
   private static final int s = Integer.MAX_VALUE;
   private final b Q = this.addSetting(new b("Delay", "Ticks between items", 2.0, 0.0, 10.0, 1.0));
   private int y;
   private int q;
   private int J = Integer.MAX_VALUE;
   private int U;
   private int k;
   private ChestMenu P;
   private ItemStack[] S;
   private final BitSet M = new BitSet();
   private boolean O;
   private boolean V;

   public ChestStealerModule() {
      super("ChestStealer", "Automatically loot chests", Category.PLAYER, -1);
   }

   @Override
   public void onDisable() {
      this.S();
   }

   private void S() {
      if (this.U > 0) {
         _q.z("ChestStealer", "Chest finished: {} clicks, {} server corrections", this.U, this.k);
      }

      this.y = 0;
      this.q = 0;
      this.J = Integer.MAX_VALUE;
      this.U = 0;
      this.k = 0;
      this.P = null;
      this.S = null;
      this.M.clear();
      this.O = (boolean)0;
      this.V = (boolean)0;
   }

   private void N(ChestMenu var1, int var2) {
      if (var1 != this.P) {
         this.S();
         this.P = var1;
         this.y = 2;
      }

      if (this.w(var1) && this.J <= var2) {
         this.y = Math.max(this.y, var2 + 1);
         this.k++;
         _q.c("ChestStealer", "Server corrected the menu; pausing {} ticks", this.y);
      }
   }

   private boolean w(ChestMenu var1) {
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

         this.M.clear();
         this.O = (boolean)0;
      }

      return (boolean)var2;
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

   private boolean m() {
      Input var1 = mc.player.getLastSentInput();
      return var1.forward() || var1.backward() || var1.left() || var1.right() || var1.shift() || mc.player.isSprinting();
   }

   @Override
   public void onTick() {
      if (!(this.isInGame() && mc.player.containerMenu instanceof ChestMenu var1)) {
         this.S();
      } else if (AutoSellModule.O(var1)) {
         this.S();
      } else {
         int var16 = qg.C(mc.player);
         if (this.J != Integer.MAX_VALUE) {
            byte var9 = 1;
            int var8 = this.J;
            this.J = var8 + var9;
         }

         byte var11 = 1;
         int var10 = this.q;
         this.q = var10 + var11;
         this.N(var1, var16);
         if (this.y > 0) {
            byte var13 = 1;
            int var12 = this.y;
            this.y = var12 - var13;
         } else if (mc.gameMode != null) {
            if (this.m()) {
               if (!this.V) {
                  this.V = (boolean)1;
                  _q.c("ChestStealer", "Waiting for the player to stop moving");
               }
            } else {
               this.V = (boolean)0;
               int var3 = var1.getRowCount() * 9;
               byte var4 = 0;

               for (int var5 = 0; var5 < var3; var5++) {
                  Slot var6 = var1.getSlot(var5);
                  ItemStack var7 = var6.getItem();
                  if (!var7.isEmpty()) {
                     var4 = 1;
                     if (!this.M.get(var5) && var6.mayPickup(mc.player) && this.e(var1, var7, var3)) {
                        this.M.set(var5);
                        mc.gameMode.handleContainerInput(var1.containerId, var5, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.J = 0;
                        this.U++;
                        this.w(var1);
                        this.y = this.Q.getValue().intValue();
                        return;
                     }
                  }
               }

               if (var4 == 0) {
                  int var15 = var16;
                  int var14 = this.q;
                  if ((-Integer.compare(var14, var15) >>> 31 ^ 1) != 0 || this.J <= var16) {
                     return;
                  }

                  mc.player.closeContainer();
                  this.S();
               } else if (!this.O) {
                  this.O = (boolean)1;
                  _q.c("ChestStealer", "Waiting for inventory capacity or container update");
               }
            }
         }
      }
   }
}

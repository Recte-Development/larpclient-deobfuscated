package com.larpclient.module;

import com.larpclient.si;
import java.util.BitSet;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestStealerModule extends Module {
   private static final int k = 2;
   private static final int L = Integer.MAX_VALUE;
   private final e p = this.addSetting(new e("Delay", "Ticks between items", 2.0, 0.0, 10.0, 1.0));
   private int H;
   private int P;
   private int d = Integer.MAX_VALUE;
   private int T;
   private int z;
   private ChestMenu Z;
   private ItemStack[] m;
   private final BitSet w = new BitSet();
   private boolean F;
   private boolean K;

   public ChestStealerModule() {
      super("ChestStealer", "Automatically loot chests", Category.PLAYER, -1);
   }

   @Override
   public void onDisable() {
      this.F();
   }

   private void F() {
      if (this.T > 0) {
         com.larpclient.h.A("ChestStealer", "Chest finished: {} clicks, {} server corrections", this.T, this.z);
      }

      this.H = 0;
      this.P = 0;
      this.d = Integer.MAX_VALUE;
      this.T = 0;
      this.z = 0;
      this.Z = null;
      this.m = null;
      this.w.clear();
      this.F = (boolean)0;
      this.K = (boolean)0;
   }

   private void Z(ChestMenu var1, int var2) {
      if (var1 != this.Z) {
         this.F();
         this.Z = var1;
         this.H = 2;
      }

      if (this.a(var1) && this.d <= var2) {
         this.H = Math.max(this.H, var2 + 1);
         this.z++;
         com.larpclient.h.U("ChestStealer", "Server corrected the menu; pausing {} ticks", this.H);
      }
   }

   private boolean a(ChestMenu var1) {
      int var2 = this.m != null && this.m.length == var1.slots.size() ? 0 : 1;
      if (var2 == 0) {
         for (int var3 = 0; var3 < this.m.length; var3++) {
            if (!ItemStack.matches(this.m[var3], var1.getSlot(var3).getItem())) {
               var2 = 1;
               break;
            }
         }
      }

      if (var2 != 0) {
         this.m = new ItemStack[var1.slots.size()];

         for (int var4 = 0; var4 < this.m.length; var4++) {
            this.m[var4] = var1.getSlot(var4).getItem().copy();
         }

         this.w.clear();
         this.F = (boolean)0;
      }

      return (boolean)var2;
   }

   private boolean Q(ChestMenu var1, ItemStack var2, int var3) {
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

   private boolean t() {
      Input var1 = mc.player.getLastSentInput();
      return var1.forward() || var1.backward() || var1.left() || var1.right() || var1.shift() || mc.player.isSprinting();
   }

   @Override
   public void onTick() {
      if (!(this.isInGame() && mc.player.containerMenu instanceof ChestMenu var1)) {
         this.F();
      } else if (AutoSellModule.f(var1)) {
         this.F();
      } else {
         int var16 = si.Y(mc.player);
         if (this.d != Integer.MAX_VALUE) {
            byte var11 = 1;
            int var10 = this.d;
            this.d = var10 + var11;
         }

         byte var13 = 1;
         int var12 = this.P;
         this.P = var12 + var13;
         this.Z(var1, var16);
         if (this.H > 0) {
            byte var15 = 1;
            int var14 = this.H;
            this.H = var14 - var15;
         } else if (mc.gameMode != null) {
            if (this.t()) {
               if (!this.K) {
                  this.K = (boolean)1;
                  com.larpclient.h.U("ChestStealer", "Waiting for the player to stop moving");
               }
            } else {
               this.K = (boolean)0;
               int var3 = var1.getRowCount() * 9;
               byte var4 = 0;

               for (int var5 = 0; var5 < var3; var5++) {
                  Slot var6 = var1.getSlot(var5);
                  ItemStack var7 = var6.getItem();
                  if (!var7.isEmpty()) {
                     var4 = 1;
                     if (!this.w.get(var5) && var6.mayPickup(mc.player) && this.Q(var1, var7, var3)) {
                        this.w.set(var5);
                        mc.gameMode.handleContainerInput(var1.containerId, var5, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.d = 0;
                        this.T++;
                        this.a(var1);
                        this.H = this.p.getValue().intValue();
                        return;
                     }
                  }
               }

               if (var4 == 0) {
                  int var9 = var16;
                  int var8 = this.P;
                  if ((-Integer.compare(var8, var9) >>> 31 ^ 1) != 0 || this.d <= var16) {
                     return;
                  }

                  mc.player.closeContainer();
                  this.F();
               } else if (!this.F) {
                  this.F = (boolean)1;
                  com.larpclient.h.U("ChestStealer", "Waiting for inventory capacity or container update");
               }
            }
         }
      }
   }
}

package com.larpclient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

@Environment(EnvType.CLIENT)
public class w3 extends we {
   public static final int L8 = 0;
   public static final int LR = 1;
   private static final w3 L3 = new w3();
   private static final int[] Lx = new int[]{1, 2, 3, 4, 6, 7, 8, 9};
   private static final int[] Lp = new int[]{0, 1, 2, 3, 5, 6, 7, 8};
   private int L9 = 0;
   private int LU = -1;
   private int LX;

   private w3() {
   }

   public static w3 M() {
      return L3;
   }

   public void Q() {
      this.O(0);
   }

   public void u() {
      this.O(1);
   }

   public void j() {
      this.O(this.L9);
   }

   public void O(int var1) {
      int var2 = Y(var1);
      int var3 = this.V(var2);
      if (!b(var3)) {
         this.clearQueue();
      } else {
         this.LU = var2;
         super.queueForceSequence(var3);
      }
   }

   public int I() {
      return this.L9;
   }

   public void s(int var1) {
      this.L9 = Y(var1);
   }

   public void Y() {
      this.L9 = this.L9 == 1 ? 0 : 1;
   }

   public String c() {
      return this.L9 == 1 ? "Even" : "Odd";
   }

   @Override
   public void queueForce(int var1) {
      if (var1 == 1) {
         this.Q();
      } else if (var1 == 2) {
         this.u();
      }
   }

   @Override
   public void queueForceSequence(int... var1) {
      if (var1 != null && var1.length != 0) {
         for (int var5 : var1) {
            if (var5 == 1 || var5 == 2) {
               this.queueForce(var5);
               return;
            }
         }

         this.clearQueue();
      } else {
         this.clearQueue();
      }
   }

   @Override
   public void clearQueue() {
      this.LU = -1;
      this.LX = 0;
      super.clearQueue();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.LX > 0) {
         this.LX--;
      }

      if (this.getPendingValue() == -1) {
         int var2 = this.V(this.L9);
         if (b(var2)) {
            this.LU = this.L9;
            super.queueForceSequence(var2);
         }
      }

      super.tick(var1);
   }

   @Override
   public String pendingLabel() {
      return super.getPendingValue() != -1 && this.LU != -1 ? A(this.LU) : "none";
   }

   @Override
   public int getPendingValue() {
      return super.getPendingValue() != -1 && this.LU != -1 ? this.LU + 1 : -1;
   }

   @Override
   public void setTemplateForValue(int var1, MapId var2) {
      if (b(var1)) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public MapId getTemplateForValue(int var1) {
      return b(var1) ? super.getTemplateForValue(var1) : null;
   }

   @Override
   public int getTemplateIdForValue(int var1) {
      return b(var1) ? super.getTemplateIdForValue(var1) : -1;
   }

   @Override
   public void setTemplateIdForValue(int var1, int var2) {
      if (b(var1)) {
         super.setTemplateIdForValue(var1, var2);
      }
   }

   @Override
   public int countTemplates() {
      int var1 = 0;

      for (int var5 : Lx) {
         if (this.getTemplateForValue(var5) != null) {
            var1++;
         }
      }

      return var1;
   }

   public void q() {
      if (lj.M().j) {
         this.LX = 40;
      }
   }

   public boolean v() {
      return this.LX > 0 || this.isSwapBusy();
   }

   public boolean m(BlockPos var1) {
      return !this.v() || !Objects.equals(var1, this.getAssignedDispenserPosition(Minecraft.getInstance()));
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      super.onDealtCardSlotUpdate(var1, var2, var3);
      if (this.q(new MapId(var3)) >= 1) {
         this.LX = 0;
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return true;
   }

   @Override
   protected boolean swapWorthwhile(int var1, int var2) {
      return b(var1) && b(var2) && !h(var1, this.L9) && h(var2, this.L9);
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      return gj.c().q(gj.d.b);
   }

   @Override
   protected int maxQueuedForces() {
      return 1;
   }

   @Override
   public int containerIndexForValue(int var1) {
      return !b(var1) ? -1 : var1 - 1;
   }

   @Override
   protected boolean allowsReturnSlot(int var1, int var2) {
      return b(var2) && var1 == this.containerIndexForValue(var2);
   }

   private int V(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();

      for (int var8 : Lx) {
         if (h(var8, var1) && this.getTemplateForValue(var8) != null) {
            var3.add(var8);
         }
      }

      if (var2 != null && var2.player != null && var2.player.containerMenu instanceof DispenserMenu var13) {
         for (int var16 : var3) {
            MapId var17 = this.getTemplateForValue(var16);
            if (var17 != null) {
               for (Slot var10 : var13.slots) {
                  if (var10 != null && !(var10.container instanceof Inventory)) {
                     ItemStack var11 = var10.getItem();
                     if (var11 != null && !var11.isEmpty() && var11.is(Items.FILLED_MAP)) {
                        MapId var12 = (MapId)var11.getComponents().get(DataComponents.MAP_ID);
                        if (var12 != null && var12.id() == var17.id()) {
                           var4.add(var16);
                           break;
                        }
                     }
                  }
               }
            }
         }

         return !var4.isEmpty() ? (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size())) : -1;
      } else {
         return !var3.isEmpty() ? (Integer)var3.get(ThreadLocalRandom.current().nextInt(var3.size())) : -1;
      }
   }

   private static int Y(int var0) {
      return var0 == 1 ? 1 : 0;
   }

   public int[] F() {
      return (int[])Lp.clone();
   }

   public int[] q() {
      return (int[])Lx.clone();
   }

   private static boolean b(int var0) {
      return var0 >= 1 && var0 <= 9 && var0 != 5;
   }

   private static boolean h(int var0, int var1) {
      return ((var0 & 1) == 0 ? 1 : 0) == (Y(var1) == 1 ? 1 : 0);
   }

   private int I(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      for (int var5 : Lx) {
         MapId var6 = this.getTemplateForValue(var5);
         if (var6 != null && var6.id() == var1.id()) {
            return var5;
         }
      }

      return -1;
   }

   private static String A(int var0) {
      return Y(var0) == 1 ? "Even" : "Odd";
   }
}

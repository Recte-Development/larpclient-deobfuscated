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
public class ue extends ug {
   public static final int lQ = 0;
   public static final int lj = 1;
   private static final ue lJ = new ue();
   private static final int[] lP = new int[]{1, 2, 3, 4, 6, 7, 8, 9};
   private static final int[] lY = new int[]{0, 1, 2, 3, 5, 6, 7, 8};
   private int lR = 0;
   private int lB = -1;
   private int lC;

   private ue() {
   }

   public static ue V() {
      return lJ;
   }

   public void m() {
      this.e(0);
   }

   public void J() {
      this.e(1);
   }

   public void F() {
      this.e(this.lR);
   }

   public void e(int var1) {
      int var2 = J(var1);
      int var3 = this.e(var2);
      if (!e(var3)) {
         this.clearQueue();
      } else {
         this.lB = var2;
         super.queueForceSequence(var3);
      }
   }

   public int W() {
      return this.lR;
   }

   public void X(int var1) {
      this.lR = J(var1);
   }

   public void G() {
      this.lR = this.lR == 1 ? 0 : 1;
   }

   public String Q() {
      return this.lR == 1 ? "Even" : "Odd";
   }

   @Override
   public void queueForce(int var1) {
      if (var1 == 1) {
         this.m();
      } else if (var1 == 2) {
         this.J();
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
      this.lB = -1;
      this.lC = 0;
      super.clearQueue();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.lC > 0) {
         this.lC--;
      }

      if (this.getPendingValue() == -1) {
         int var2 = this.e(this.lR);
         if (e(var2)) {
            this.lB = this.lR;
            super.queueForceSequence(var2);
         }
      }

      super.tick(var1);
   }

   @Override
   public String pendingLabel() {
      return super.getPendingValue() != -1 && this.lB != -1 ? o(this.lB) : "none";
   }

   @Override
   public int getPendingValue() {
      return super.getPendingValue() != -1 && this.lB != -1 ? this.lB + 1 : -1;
   }

   @Override
   public void setTemplateForValue(int var1, MapId var2) {
      if (e(var1)) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public MapId getTemplateForValue(int var1) {
      return e(var1) ? super.getTemplateForValue(var1) : null;
   }

   @Override
   public int getTemplateIdForValue(int var1) {
      return e(var1) ? super.getTemplateIdForValue(var1) : -1;
   }

   @Override
   public void setTemplateIdForValue(int var1, int var2) {
      if (e(var1)) {
         super.setTemplateIdForValue(var1, var2);
      }
   }

   @Override
   public int countTemplates() {
      int var1 = 0;

      for (int var5 : lP) {
         if (this.getTemplateForValue(var5) != null) {
            var1++;
         }
      }

      return var1;
   }

   public void u() {
      if (_6.G().n) {
         this.lC = 40;
      }
   }

   public boolean D() {
      return this.lC > 0 || this.isSwapBusy();
   }

   public boolean W(BlockPos var1) {
      return !this.D() || !Objects.equals(var1, this.getAssignedDispenserPosition(Minecraft.getInstance()));
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      super.onDealtCardSlotUpdate(var1, var2, var3);
      if (this.u(new MapId(var3)) >= 1) {
         this.lC = 0;
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return true;
   }

   @Override
   protected boolean swapWorthwhile(int var1, int var2) {
      return e(var1) && e(var2) && !X(var1, this.lR) && X(var2, this.lR);
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      return f9.W().R(f9.b.T);
   }

   @Override
   protected int maxQueuedForces() {
      return 1;
   }

   @Override
   public int containerIndexForValue(int var1) {
      return !e(var1) ? -1 : var1 - 1;
   }

   @Override
   protected boolean allowsReturnSlot(int var1, int var2) {
      return e(var2) && var1 == this.containerIndexForValue(var2);
   }

   private int e(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();

      for (int var8 : lP) {
         if (X(var8, var1) && this.getTemplateForValue(var8) != null) {
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

   private static int J(int var0) {
      return var0 == 1 ? 1 : 0;
   }

   public int[] g() {
      return (int[])lY.clone();
   }

   public int[] R() {
      return (int[])lP.clone();
   }

   private static boolean e(int var0) {
      return var0 >= 1 && var0 <= 9 && var0 != 5;
   }

   private static boolean X(int var0, int var1) {
      return ((var0 & 1) == 0 ? 1 : 0) == (J(var1) == 1 ? 1 : 0);
   }

   private int m(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      for (int var5 : lP) {
         MapId var6 = this.getTemplateForValue(var5);
         if (var6 != null && var6.id() == var1.id()) {
            return var5;
         }
      }

      return -1;
   }

   private static String o(int var0) {
      return J(var0) == 1 ? "Even" : "Odd";
   }
}

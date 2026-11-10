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
public class vz extends vy {
   public static final int Gg = 0;
   public static final int G9 = 1;
   private static final vz GZ = new vz();
   private static final int[] GU = new int[]{1, 2, 3, 4, 6, 7, 8, 9};
   private static final int[] GD = new int[]{0, 1, 2, 3, 5, 6, 7, 8};
   private int GW = 0;
   private int Gs = -1;
   private int Gk;

   private vz() {
   }

   public static vz E() {
      return GZ;
   }

   public void C() {
      this.G(0);
   }

   public void Q() {
      this.G(1);
   }

   public void r() {
      this.G(this.GW);
   }

   public void G(int var1) {
      int var2 = K(var1);
      int var3 = this.j(var2);
      if (!U(var3)) {
         this.clearQueue();
      } else {
         this.Gs = var2;
         super.queueForceSequence(var3);
      }
   }

   public int F() {
      return this.GW;
   }

   public void d(int var1) {
      this.GW = K(var1);
   }

   public void d() {
      this.GW = this.GW == 1 ? 0 : 1;
   }

   public String y() {
      return this.GW == 1 ? "Even" : "Odd";
   }

   @Override
   public void queueForce(int var1) {
      if (var1 == 1) {
         this.C();
      } else if (var1 == 2) {
         this.Q();
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
      this.Gs = -1;
      this.Gk = 0;
      super.clearQueue();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.Gk > 0) {
         this.Gk--;
      }

      if (this.getPendingValue() == -1) {
         int var2 = this.j(this.GW);
         if (U(var2)) {
            this.Gs = this.GW;
            super.queueForceSequence(var2);
         }
      }

      super.tick(var1);
   }

   @Override
   public String pendingLabel() {
      return super.getPendingValue() != -1 && this.Gs != -1 ? E(this.Gs) : "none";
   }

   @Override
   public int getPendingValue() {
      return super.getPendingValue() != -1 && this.Gs != -1 ? this.Gs + 1 : -1;
   }

   @Override
   public void setTemplateForValue(int var1, MapId var2) {
      if (U(var1)) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public MapId getTemplateForValue(int var1) {
      return U(var1) ? super.getTemplateForValue(var1) : null;
   }

   @Override
   public int getTemplateIdForValue(int var1) {
      return U(var1) ? super.getTemplateIdForValue(var1) : -1;
   }

   @Override
   public void setTemplateIdForValue(int var1, int var2) {
      if (U(var1)) {
         super.setTemplateIdForValue(var1, var2);
      }
   }

   @Override
   public int countTemplates() {
      int var1 = 0;

      for (int var5 : GU) {
         if (this.getTemplateForValue(var5) != null) {
            var1++;
         }
      }

      return var1;
   }

   public void J() {
      if (ww.J().K) {
         this.Gk = 40;
      }
   }

   public boolean s() {
      return this.Gk > 0 || this.isSwapBusy();
   }

   public boolean V(BlockPos var1) {
      return !this.s() || !Objects.equals(var1, this.getAssignedDispenserPosition(Minecraft.getInstance()));
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      super.onDealtCardSlotUpdate(var1, var2, var3);
      if (this.V(new MapId(var3)) >= 1) {
         this.Gk = 0;
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return true;
   }

   @Override
   protected boolean swapWorthwhile(int var1, int var2) {
      return U(var1) && U(var2) && !A(var1, this.GW) && A(var2, this.GW);
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      return hk.e().f(hk.k.i);
   }

   @Override
   protected int maxQueuedForces() {
      return 1;
   }

   @Override
   public int containerIndexForValue(int var1) {
      return !U(var1) ? -1 : var1 - 1;
   }

   @Override
   protected boolean allowsReturnSlot(int var1, int var2) {
      return U(var2) && var1 == this.containerIndexForValue(var2);
   }

   private int j(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();

      for (int var8 : GU) {
         if (A(var8, var1) && this.getTemplateForValue(var8) != null) {
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

   private static int K(int var0) {
      return var0 == 1 ? 1 : 0;
   }

   public int[] y() {
      return (int[])GD.clone();
   }

   public int[] Q() {
      return (int[])GU.clone();
   }

   private static boolean U(int var0) {
      return var0 >= 1 && var0 <= 9 && var0 != 5;
   }

   private static boolean A(int var0, int var1) {
      return ((var0 & 1) == 0 ? 1 : 0) == (K(var1) == 1 ? 1 : 0);
   }

   private int e(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      for (int var5 : GU) {
         MapId var6 = this.getTemplateForValue(var5);
         if (var6 != null && var6.id() == var1.id()) {
            return var5;
         }
      }

      return -1;
   }

   private static String E(int var0) {
      return K(var0) == 1 ? "Even" : "Odd";
   }
}

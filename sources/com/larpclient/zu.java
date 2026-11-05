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
public class zu extends ze {
   public static final int Ll = 0;
   public static final int LG = 1;
   private static final zu La = new zu();
   private static final int[] LO = new int[]{1, 2, 3, 4, 6, 7, 8, 9};
   private static final int[] L9 = new int[]{0, 1, 2, 3, 5, 6, 7, 8};
   private int Lg = 0;
   private int L1 = -1;
   private int Lt;

   private zu() {
   }

   public static zu v() {
      return La;
   }

   public void t() {
      this.W(0);
   }

   public void l() {
      this.W(1);
   }

   public void U() {
      this.W(this.Lg);
   }

   public void W(int var1) {
      int var2 = B(var1);
      int var3 = this.I(var2);
      if (!x(var3)) {
         this.clearQueue();
      } else {
         this.L1 = var2;
         super.queueForceSequence(var3);
      }
   }

   public int n() {
      return this.Lg;
   }

   public void F(int var1) {
      this.Lg = B(var1);
   }

   public void A() {
      this.Lg = this.Lg == 1 ? 0 : 1;
   }

   public String k() {
      return this.Lg == 1 ? "Even" : "Odd";
   }

   @Override
   public void queueForce(int var1) {
      if (var1 == 1) {
         this.t();
      } else if (var1 == 2) {
         this.l();
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
      this.L1 = -1;
      this.Lt = 0;
      super.clearQueue();
   }

   @Override
   public void tick(Minecraft var1) {
      if (this.Lt > 0) {
         this.Lt--;
      }

      if (this.getPendingValue() == -1) {
         int var2 = this.I(this.Lg);
         if (x(var2)) {
            this.L1 = this.Lg;
            super.queueForceSequence(var2);
         }
      }

      super.tick(var1);
   }

   @Override
   public String pendingLabel() {
      return super.getPendingValue() != -1 && this.L1 != -1 ? D(this.L1) : "none";
   }

   @Override
   public int getPendingValue() {
      return super.getPendingValue() != -1 && this.L1 != -1 ? this.L1 + 1 : -1;
   }

   @Override
   public void setTemplateForValue(int var1, MapId var2) {
      if (x(var1)) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public MapId getTemplateForValue(int var1) {
      return x(var1) ? super.getTemplateForValue(var1) : null;
   }

   @Override
   public int getTemplateIdForValue(int var1) {
      return x(var1) ? super.getTemplateIdForValue(var1) : -1;
   }

   @Override
   public void setTemplateIdForValue(int var1, int var2) {
      if (x(var1)) {
         super.setTemplateIdForValue(var1, var2);
      }
   }

   @Override
   public int countTemplates() {
      int var1 = 0;

      for (int var5 : LO) {
         if (this.getTemplateForValue(var5) != null) {
            var1++;
         }
      }

      return var1;
   }

   public void R() {
      if (pg.G().G) {
         this.Lt = 40;
      }
   }

   public boolean F() {
      return this.Lt > 0 || this.isSwapBusy();
   }

   public boolean T(BlockPos var1) {
      return !this.F() || !Objects.equals(var1, this.getAssignedDispenserPosition(Minecraft.getInstance()));
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      super.onDealtCardSlotUpdate(var1, var2, var3);
      if (this.Y(new MapId(var3)) >= 1) {
         this.Lt = 0;
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return true;
   }

   @Override
   protected boolean swapWorthwhile(int var1, int var2) {
      return x(var1) && x(var2) && !E(var1, this.Lg) && E(var2, this.Lg);
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      return ep.H().n(ep.o.K);
   }

   @Override
   protected int maxQueuedForces() {
      return 1;
   }

   @Override
   public int containerIndexForValue(int var1) {
      return !x(var1) ? -1 : var1 - 1;
   }

   @Override
   protected boolean allowsReturnSlot(int var1, int var2) {
      return x(var2) && var1 == this.containerIndexForValue(var2);
   }

   private int I(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();

      for (int var8 : LO) {
         if (E(var8, var1) && this.getTemplateForValue(var8) != null) {
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

   private static int B(int var0) {
      return var0 == 1 ? 1 : 0;
   }

   public int[] x() {
      return (int[])L9.clone();
   }

   public int[] E() {
      return (int[])LO.clone();
   }

   private static boolean x(int var0) {
      return var0 >= 1 && var0 <= 9 && var0 != 5;
   }

   private static boolean E(int var0, int var1) {
      return ((var0 & 1) == 0 ? 1 : 0) == (B(var1) == 1 ? 1 : 0);
   }

   private int r(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      for (int var5 : LO) {
         MapId var6 = this.getTemplateForValue(var5);
         if (var6 != null && var6.id() == var1.id()) {
            return var5;
         }
      }

      return -1;
   }

   private static String D(int var0) {
      return B(var0) == 1 ? "Even" : "Odd";
   }
}

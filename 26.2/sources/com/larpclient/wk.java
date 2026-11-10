package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.saveddata.maps.MapId;

@Environment(EnvType.CLIENT)
public class wk extends we {
   private static final wk vZ = new wk();

   private wk() {
   }

   public static wk m() {
      return vZ;
   }

   @Override
   public void queueForce(int var1) {
      if (var1 >= 1 && var1 <= 9) {
         super.queueForce(var1);
      }
   }

   @Override
   public void queueForceSequence(int... var1) {
      if (var1 != null && var1.length != 0) {
         int[] var2 = new int[var1.length];
         int var3 = 0;

         for (int var7 : var1) {
            if (var7 >= 1 && var7 <= 9) {
               var2[var3++] = var7;
            }
         }

         if (var3 == 0) {
            this.clearQueue();
         } else if (var3 == var2.length) {
            super.queueForceSequence(var2);
         } else {
            int[] var8 = new int[var3];
            System.arraycopy(var2, 0, var8, 0, var3);
            super.queueForceSequence(var8);
         }
      } else {
         this.clearQueue();
      }
   }

   @Override
   public int getPendingValue() {
      int var1 = super.getPendingValue();
      return var1 >= 1 && var1 <= 9 ? var1 : -1;
   }

   @Override
   public String pendingLabel() {
      int var1 = this.getPendingValue();
      return var1 == -1 ? "none" : String.valueOf(var1);
   }

   @Override
   public void setTemplateForValue(int var1, MapId var2) {
      if (var1 >= 1 && var1 <= 9) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public MapId getTemplateForValue(int var1) {
      return var1 >= 1 && var1 <= 9 ? super.getTemplateForValue(var1) : null;
   }

   @Override
   public int getTemplateIdForValue(int var1) {
      return var1 >= 1 && var1 <= 9 ? super.getTemplateIdForValue(var1) : -1;
   }

   @Override
   public void setTemplateIdForValue(int var1, int var2) {
      if (var1 >= 1 && var1 <= 9) {
         super.setTemplateIdForValue(var1, var2);
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return true;
   }

   @Override
   protected boolean swapWorthwhile(int var1, int var2) {
      return m9.O().y(this, var1, var2);
   }

   @Override
   protected int maxQueuedForces() {
      return 1;
   }

   @Override
   public int containerIndexForValue(int var1) {
      return var1 >= 1 && var1 <= 9 ? var1 - 1 : -1;
   }
}

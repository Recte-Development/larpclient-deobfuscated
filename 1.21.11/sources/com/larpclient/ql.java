package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_9209;

@Environment(EnvType.CLIENT)
public class ql extends q7 {
   private static final ql KR = new ql();

   private ql() {
   }

   public static ql Y() {
      return KR;
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
   public void setTemplateForValue(int var1, class_9209 var2) {
      if (var1 >= 1 && var1 <= 9) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public class_9209 getTemplateForValue(int var1) {
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
      return true;
   }

   @Override
   protected class_2338 getAssignedDispenserPosition(class_310 var1) {
      return hu.f().F(hu.q.m);
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

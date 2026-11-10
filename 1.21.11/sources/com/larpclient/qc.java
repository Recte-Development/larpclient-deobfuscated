package com.larpclient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1661;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

@Environment(EnvType.CLIENT)
public class qc extends q7 {
   public static final int oS = 0;
   public static final int oq = 1;
   private static final qc oW = new qc();
   private static final int[] oX = new int[]{1, 2, 3, 4, 6, 7, 8, 9};
   private static final int[] oF = new int[]{0, 1, 2, 3, 5, 6, 7, 8};
   private int oT = 0;
   private int o9 = -1;
   private int oG;

   private qc() {
   }

   public static qc i() {
      return oW;
   }

   public void A() {
      this.R(0);
   }

   public void P() {
      this.R(1);
   }

   public void T() {
      this.R(this.oT);
   }

   public void R(int var1) {
      int var2 = a(var1);
      int var3 = this.L(var2);
      if (!c(var3)) {
         this.clearQueue();
      } else {
         this.o9 = var2;
         super.queueForceSequence(var3);
      }
   }

   public int a() {
      return this.oT;
   }

   public void p(int var1) {
      this.oT = a(var1);
   }

   public void m() {
      this.oT = this.oT == 1 ? 0 : 1;
   }

   public String h() {
      return this.oT == 1 ? "Even" : "Odd";
   }

   @Override
   public void queueForce(int var1) {
      if (var1 == 1) {
         this.A();
      } else if (var1 == 2) {
         this.P();
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
      this.o9 = -1;
      this.oG = 0;
      super.clearQueue();
   }

   @Override
   public void tick(class_310 var1) {
      if (this.oG > 0) {
         this.oG--;
      }

      if (this.getPendingValue() == -1) {
         int var2 = this.L(this.oT);
         if (c(var2)) {
            this.o9 = this.oT;
            super.queueForceSequence(var2);
         }
      }

      super.tick(var1);
   }

   @Override
   public String pendingLabel() {
      return super.getPendingValue() != -1 && this.o9 != -1 ? n(this.o9) : "none";
   }

   @Override
   public int getPendingValue() {
      return super.getPendingValue() != -1 && this.o9 != -1 ? this.o9 + 1 : -1;
   }

   @Override
   public void setTemplateForValue(int var1, class_9209 var2) {
      if (c(var1)) {
         super.setTemplateForValue(var1, var2);
      }
   }

   @Override
   public class_9209 getTemplateForValue(int var1) {
      return c(var1) ? super.getTemplateForValue(var1) : null;
   }

   @Override
   public int getTemplateIdForValue(int var1) {
      return c(var1) ? super.getTemplateIdForValue(var1) : -1;
   }

   @Override
   public void setTemplateIdForValue(int var1, int var2) {
      if (c(var1)) {
         super.setTemplateIdForValue(var1, var2);
      }
   }

   @Override
   public int countTemplates() {
      int var1 = 0;

      for (int var5 : oX) {
         if (this.getTemplateForValue(var5) != null) {
            var1++;
         }
      }

      return var1;
   }

   public void E() {
      if (hs.B().p) {
         this.oG = 40;
      }
   }

   public boolean o() {
      return this.oG > 0 || this.isSwapBusy();
   }

   public boolean Z(class_2338 var1) {
      return !this.o() || !Objects.equals(var1, this.getAssignedDispenserPosition(class_310.method_1551()));
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      super.onDealtCardSlotUpdate(var1, var2, var3);
      if (this.c(new class_9209(var3)) >= 1) {
         this.oG = 0;
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return true;
   }

   @Override
   protected boolean swapWorthwhile(int var1, int var2) {
      return c(var1) && c(var2) && !B(var1, this.oT) && B(var2, this.oT);
   }

   @Override
   protected class_2338 getAssignedDispenserPosition(class_310 var1) {
      return hu.f().F(hu.q.C);
   }

   @Override
   protected int maxQueuedForces() {
      return 1;
   }

   @Override
   public int containerIndexForValue(int var1) {
      return !c(var1) ? -1 : var1 - 1;
   }

   @Override
   protected boolean allowsReturnSlot(int var1, int var2) {
      return c(var2) && var1 == this.containerIndexForValue(var2);
   }

   private int L(int var1) {
      class_310 var2 = class_310.method_1551();
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();

      for (int var8 : oX) {
         if (B(var8, var1) && this.getTemplateForValue(var8) != null) {
            var3.add(var8);
         }
      }

      if (var2 != null && var2.field_1724 != null && var2.field_1724.field_7512 instanceof class_1716 var13) {
         for (int var16 : var3) {
            class_9209 var17 = this.getTemplateForValue(var16);
            if (var17 != null) {
               for (class_1735 var10 : var13.field_7761) {
                  if (var10 != null && !(var10.field_7871 instanceof class_1661)) {
                     class_1799 var11 = var10.method_7677();
                     if (var11 != null && !var11.method_7960() && var11.method_31574(class_1802.field_8204)) {
                        class_9209 var12 = (class_9209)var11.method_57353().method_58694(class_9334.field_49646);
                        if (var12 != null && var12.comp_2315() == var17.comp_2315()) {
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

   private static int a(int var0) {
      return var0 == 1 ? 1 : 0;
   }

   public int[] A() {
      return (int[])oF.clone();
   }

   public int[] S() {
      return (int[])oX.clone();
   }

   private static boolean c(int var0) {
      return var0 >= 1 && var0 <= 9 && var0 != 5;
   }

   private static boolean B(int var0, int var1) {
      return ((var0 & 1) == 0 ? 1 : 0) == (a(var1) == 1 ? 1 : 0);
   }

   private int A(class_9209 var1) {
      if (var1 == null) {
         return -1;
      }

      for (int var5 : oX) {
         class_9209 var6 = this.getTemplateForValue(var5);
         if (var6 != null && var6.comp_2315() == var1.comp_2315()) {
            return var5;
         }
      }

      return -1;
   }

   private static String n(int var0) {
      return a(var0) == 1 ? "Even" : "Odd";
   }
}

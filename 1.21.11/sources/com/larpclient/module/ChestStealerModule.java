package com.larpclient.module;

import com.larpclient.bn;
import com.larpclient.q8;
import java.util.BitSet;
import net.minecraft.class_10185;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;

public class ChestStealerModule extends Module {
   private static final int y = 2;
   private static final int O = Integer.MAX_VALUE;
   private final lo x = this.addSetting(new lo("Delay", "Ticks between items", 2.0, 0.0, 10.0, 1.0));
   private int z;
   private int b;
   private int I = Integer.MAX_VALUE;
   private int i;
   private int d;
   private class_1707 k;
   private class_1799[] Y;
   private final BitSet p = new BitSet();
   private boolean D;
   private boolean P;

   public ChestStealerModule() {
      super("ChestStealer", "Automatically loot chests", Category.PLAYER, -1);
   }

   @Override
   public void onDisable() {
      this.i();
   }

   private void i() {
      if (this.i > 0) {
         q8.G("ChestStealer", "Chest finished: {} clicks, {} server corrections", this.i, this.d);
      }

      this.z = 0;
      this.b = 0;
      this.I = Integer.MAX_VALUE;
      this.i = 0;
      this.d = 0;
      this.k = null;
      this.Y = null;
      this.p.clear();
      this.D = (boolean)0;
      this.P = (boolean)0;
   }

   private void R(class_1707 var1, int var2) {
      if (var1 != this.k) {
         this.i();
         this.k = var1;
         this.z = 2;
      }

      if (this.V(var1) && this.I <= var2) {
         this.z = Math.max(this.z, var2 + 1);
         this.d++;
         q8.v("ChestStealer", "Server corrected the menu; pausing {} ticks", this.z);
      }
   }

   private boolean V(class_1707 var1) {
      int var2 = this.Y != null && this.Y.length == var1.field_7761.size() ? 0 : 1;
      if (var2 == 0) {
         for (int var3 = 0; var3 < this.Y.length; var3++) {
            if (!class_1799.method_7973(this.Y[var3], var1.method_7611(var3).method_7677())) {
               var2 = 1;
               break;
            }
         }
      }

      if (var2 != 0) {
         this.Y = new class_1799[var1.field_7761.size()];

         for (int var4 = 0; var4 < this.Y.length; var4++) {
            this.Y[var4] = var1.method_7611(var4).method_7677().method_7972();
         }

         this.p.clear();
         this.D = (boolean)0;
      }

      return (boolean)var2;
   }

   private boolean I(class_1707 var1, class_1799 var2, int var3) {
      for (int var4 = var3; var4 < var1.field_7761.size(); var4++) {
         class_1735 var5 = var1.method_7611(var4);
         class_1799 var6 = var5.method_7677();
         if (var6.method_7960()) {
            if (var5.method_7680(var2) && var5.method_7676(var2) > 0) {
               return true;
            }
         } else if (var2.method_7946() && class_1799.method_31577(var2, var6) && var6.method_7947() < var5.method_7676(var6)) {
            return true;
         }
      }

      return false;
   }

   private boolean J() {
      class_10185 var1 = mc.field_1724.method_71091();
      return var1.comp_3159() || var1.comp_3160() || var1.comp_3161() || var1.comp_3162() || var1.comp_3164() || mc.field_1724.method_5624();
   }

   @Override
   public void onTick() {
      if (!(this.isInGame() && mc.field_1724.field_7512 instanceof class_1707 var1)) {
         this.i();
      } else if (AutoSellModule.c(var1)) {
         this.i();
      } else {
         int var16 = bn.Z(mc.field_1724);
         if (this.I != Integer.MAX_VALUE) {
            byte var11 = 1;
            int var10 = this.I;
            this.I = var10 + var11;
         }

         byte var13 = 1;
         int var12 = this.b;
         this.b = var12 + var13;
         this.R(var1, var16);
         if (this.z > 0) {
            byte var15 = 1;
            int var14 = this.z;
            this.z = var14 - var15;
         } else if (mc.field_1761 != null) {
            if (this.J()) {
               if (!this.P) {
                  this.P = (boolean)1;
                  q8.v("ChestStealer", "Waiting for the player to stop moving");
               }
            } else {
               this.P = (boolean)0;
               int var3 = var1.method_17388() * 9;
               byte var4 = 0;

               for (int var5 = 0; var5 < var3; var5++) {
                  class_1735 var6 = var1.method_7611(var5);
                  class_1799 var7 = var6.method_7677();
                  if (!var7.method_7960()) {
                     var4 = 1;
                     if (!this.p.get(var5) && var6.method_7674(mc.field_1724) && this.I(var1, var7, var3)) {
                        this.p.set(var5);
                        mc.field_1761.method_2906(var1.field_7763, var5, 0, class_1713.field_7794, mc.field_1724);
                        this.I = 0;
                        this.i++;
                        this.V(var1);
                        this.z = this.x.getValue().intValue();
                        return;
                     }
                  }
               }

               if (var4 == 0) {
                  int var9 = var16;
                  int var8 = this.b;
                  if ((-Integer.compare(var8, var9) >>> 31 ^ 1) != 0 || this.I <= var16) {
                     return;
                  }

                  mc.field_1724.method_7346();
                  this.i();
               } else if (!this.D) {
                  this.D = (boolean)1;
                  q8.v("ChestStealer", "Waiting for inventory capacity or container update");
               }
            }
         }
      }
   }
}

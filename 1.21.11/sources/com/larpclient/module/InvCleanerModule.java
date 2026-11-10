package com.larpclient.module;

import net.minecraft.class_1713;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;

public class InvCleanerModule extends Module {
   private final l3 a = this.addSetting(new l3("Dirt", "Drop dirt blocks", true));
   private final l3 U = this.addSetting(new l3("Cobble", "Drop cobblestone", true));
   private final l3 X = this.addSetting(new l3("Gravel", "Drop gravel", true));
   private final l3 D = this.addSetting(new l3("Seeds", "Drop seeds", true));
   private final l3 I = this.addSetting(new l3("Rotten Flesh", "Drop rotten flesh", true));
   private int V = 0;

   public InvCleanerModule() {
      super("InvCleaner", "Automatically drop junk items", Category.PLAYER, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.field_1755 == null) {
            boolean var5 = false;
            int var4 = this.V;
            if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
               byte var7 = 1;
               int var6 = this.V;
               this.V = var6 - var7;
            } else {
               for (int var1 = 0; var1 < 36; var1++) {
                  class_1799 var2 = mc.field_1724.method_31548().method_5438(var1);
                  if (!var2.method_7960() && this.D(var2)) {
                     int var3 = var1 < 9 ? var1 + 36 : var1;
                     mc.field_1761.method_2906(mc.field_1724.field_7512.field_7763, var3, 1, class_1713.field_7795, mc.field_1724);
                     this.V = 3;
                     return;
                  }
               }
            }
         }
      }
   }

   private boolean D(class_1799 var1) {
      class_1792 var2 = var1.method_7909();
      if (!this.a.getValue() || var2 != class_1802.field_8831 && var2 != class_1802.field_8460) {
         if (this.U.getValue() && var2 == class_1802.field_20412) {
            return true;
         } else if (!this.X.getValue() || var2 != class_1802.field_8110 && var2 != class_1802.field_8145) {
            return !this.D.getValue()
                  || var2 != class_1802.field_8317 && var2 != class_1802.field_8309 && var2 != class_1802.field_46250 && var2 != class_1802.field_46249
               ? this.I.getValue() && var2 == class_1802.field_8511
               : true;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}

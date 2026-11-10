package com.larpclient.module;

import com.larpclient.q8;
import net.minecraft.class_1297;
import net.minecraft.class_1511;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_310;
import net.minecraft.class_5134;
import net.minecraft.class_7924;
import net.minecraft.class_1297.class_5529;

public final class CrystalOptimizerModule extends Module {
   private static CrystalOptimizerModule A;
   private String m = "Ready";
   private boolean z;

   public CrystalOptimizerModule() {
      super("Crystal Optimizer", "Crystals you break disappear at once client-side for faster placement", Category.COMBAT, -1);
      A = this;
   }

   public static boolean b(double var0, int var2) {
      return var0 > 0.0 || var2 > 0;
   }

   public static boolean A(class_1657 var0) {
      class_1799 var1 = var0.method_59958();
      int var2 = var0.method_73183()
         .method_30349()
         .method_30530(class_7924.field_41265)
         .method_46746(class_1893.field_9118)
         .map(var1x -> class_1890.method_8225(var1x, var1))
         .orElse(0);
      return b(var0.method_45325(class_5134.field_23721), var2);
   }

   public static void K(class_1657 var0, class_1297 var1) {
      if (A != null && A.isEnabled()) {
         CrystalOptimizerModule var2 = A;
         if (var2 != null && var2.isRuntimeActive() && var1 instanceof class_1511 var3) {
            class_310 var4 = getMc();
            if (var0 == var4.field_1724
               && var4.field_1687 != null
               && var4.field_1761 != null
               && !var4.field_1761.method_2928()
               && var3.method_73183() == var4.field_1687
               && !var3.method_31481()
               && !var3.method_5655()) {
               if (!A(var0)) {
                  var2.m = "Your hit deals no damage";
               } else {
                  var4.field_1687.method_2945(var3.method_5628(), class_5529.field_26998);
                  var2.m = "Ready";
                  if (!var2.z) {
                     q8.v("CrystalOptimizer", "First crystal {} removed locally after the attack packet", var3.method_5628());
                     var2.z = (boolean)1;
                  }
               }
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.m : "";
   }

   @Override
   public void onEnable() {
      this.m = "Ready";
      this.z = (boolean)0;
      q8.G("CrystalOptimizer", "Enabled; crystals you break are removed client-side after the native attack");
   }
}

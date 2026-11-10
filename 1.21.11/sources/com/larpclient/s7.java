package com.larpclient;

import net.minecraft.class_2302;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2680;

public final class s7 extends s4 {
   private final com.larpclient.module.l3 i = this.G("Legit", "Look at reachable crops and harvest through the real crosshair", false);
   private final com.larpclient.module.lo d = this.Z("Range", "Harvest radius measured from your eyes", 4.0, 1.0, 6.0, 1.0);

   public s7() {
      super("Auto Farm", "Harvest mature crops around you while walking through a farm");
   }

   private boolean T(class_2338 var1) {
      if (!mc.field_1687.method_22340(var1)) {
         return false;
      }

      class_2680 var2 = mc.field_1687.method_8320(var1);
      return var2.method_26204() instanceof class_2302 var3 && var3.method_9825(var2);
   }

   @Override
   public void onTick() {
      int var10000 = this.r();
      boolean var13 = false;
      byte var12 = (byte)var10000;
      var10000 = Integer.signum(var12);
      if ((var10000 | -var10000) >>> 31 == 0) {
         bv.i(this);
      } else {
         double var1 = Math.min(this.d.getValue(), mc.field_1724.method_55754());
         int var3 = (int)Math.ceil(var1);
         class_2338 var4 = mc.field_1724.method_24515();
         class_2338 var5 = null;
         double var6 = var1 * var1;
         boolean var15 = false;
         int var14 = var3;
         int var10001 = -var14;
         boolean var17 = false;
         int var16 = var3;
         int var10002 = -var16;
         boolean var19 = false;
         int var18 = var3;

         for (class_2338 var9 : class_2338.method_10097(var4.method_10069(var10001, var10002, -var18), var4.method_10069(var3, var3, var3))) {
            if (this.T(var9)) {
               double var10 = mc.field_1724.method_33571().method_1025(class_243.method_24953(var9));
               if (!(var10 >= var6) && (!this.i.getValue() || bv.y(var9, var1) != null)) {
                  var5 = var9.method_10062();
                  var6 = var10;
               }
            }
         }

         if (var5 == null) {
            bv.i(this);
            this.m("No mature crops in reach");
         } else {
            class_2338 var20 = var5;
            bv.t(this, var20, var1, this.i.getValue(), 35.0F, false, () -> this.T(var20));
         }
      }
   }
}

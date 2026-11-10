package com.larpclient.module;

import com.larpclient.j;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_243;

public class EntitySpeedModule extends Module {
   private final lo c = this.addSetting(new lo("Speed", "Entity speed multiplier", 2.0, 1.0, 5.0, 0.1));

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public EntitySpeedModule() {
      super("EntitySpeed", "Ride entities faster", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (j.k()) {
            int var10000 = mc.field_1724.method_5765();
            boolean var25 = false;
            byte var24 = (byte)var10000;
            var10000 = Integer.signum(var24);
            if ((var10000 | -var10000) >>> 31 != 0) {
               class_1297 var1 = mc.field_1724.method_5854();
               if (var1 != null) {
                  float var2 = mc.field_1724.method_36454();
                  double var3 = Math.toRadians(var2);
                  double var5 = 0.0;
                  double var7 = 0.0;
                  if (mc.field_1690.field_1894.method_1434()) {
                     var5++;
                  }

                  if (mc.field_1690.field_1881.method_1434()) {
                     var5--;
                  }

                  int var27 = mc.field_1690.field_1913.method_1434();
                  boolean var19 = false;
                  byte var18 = (byte)var27;
                  var27 = Integer.signum(var18);
                  if (((var27 | -var27) >>> 31 ^ 1) == 0) {
                     var7++;
                  }

                  int var29 = mc.field_1690.field_1849.method_1434();
                  boolean var21 = false;
                  byte var20 = (byte)var29;
                  var29 = Integer.signum(var20);
                  if (((var29 | -var29) >>> 31 ^ 1) == 0) {
                     var7--;
                  }

                  if (var5 == 0.0) {
                     double var33;
                     var29 = (var33 = var7 - 0.0) == 0.0 ? 0 : (var33 < 0.0 ? -1 : 1);
                     boolean var23 = false;
                     int var22 = var29;
                     var29 = Integer.signum(var22);
                     if ((var29 | -var29) >>> 31 == 0) {
                        return;
                     }
                  }

                  double var9 = this.c.getValue() * 0.3;
                  double var11 = -Math.sin(var3) * var5 + Math.cos(var3) * var7;
                  double var13 = Math.cos(var3) * var5 + Math.sin(var3) * var7;
                  double var15 = Math.sqrt(var11 * var11 + var13 * var13);
                  if (var15 > 0.0) {
                     var11 = var11 / var15 * var9;
                     var13 = var13 / var15 * var9;
                  }

                  class_243 var17 = var1.method_18798();
                  var1.method_18800(var11, var17.field_1351, var13);
               }
            }
         }
      }
   }
}

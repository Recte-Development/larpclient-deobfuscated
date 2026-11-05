package com.larpclient.module;

import com.larpclient.yu;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntitySpeedModule extends Module {
   private final lc y = this.addSetting(new lc("Speed", "Entity speed multiplier", 2.0, 1.0, 5.0, 0.1));

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   public EntitySpeedModule() {
      super("EntitySpeed", "Ride entities faster", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = yu.r();
         boolean var19 = false;
         byte var18 = (byte)var10000;
         var10000 = Integer.signum(var18);
         if ((var10000 | -var10000) >>> 31 != 0) {
            if (mc.player.isPassenger()) {
               Entity var1 = mc.player.getVehicle();
               if (var1 != null) {
                  float var2 = mc.player.getYRot();
                  double var3 = Math.toRadians(var2);
                  double var5 = 0.0;
                  double var7 = 0.0;
                  int var27 = mc.options.keyUp.isDown();
                  boolean var21 = false;
                  byte var20 = (byte)var27;
                  var27 = Integer.signum(var20);
                  if (((var27 | -var27) >>> 31 ^ 1) == 0) {
                     var5++;
                  }

                  int var29 = mc.options.keyDown.isDown();
                  boolean var23 = false;
                  byte var22 = (byte)var29;
                  var29 = Integer.signum(var22);
                  if (((var29 | -var29) >>> 31 ^ 1) == 0) {
                     var5--;
                  }

                  int var31 = mc.options.keyLeft.isDown();
                  boolean var25 = false;
                  byte var24 = (byte)var31;
                  var31 = Integer.signum(var24);
                  if (((var31 | -var31) >>> 31 ^ 1) == 0) {
                     var7++;
                  }

                  if (mc.options.keyRight.isDown()) {
                     var7--;
                  }

                  if (var5 != 0.0 || var7 != 0.0) {
                     double var9 = this.y.getValue() * 0.3;
                     double var11 = -Math.sin(var3) * var5 + Math.cos(var3) * var7;
                     double var13 = Math.cos(var3) * var5 + Math.sin(var3) * var7;
                     double var15 = Math.sqrt(var11 * var11 + var13 * var13);
                     if (var15 > 0.0) {
                        var11 = var11 / var15 * var9;
                        var13 = var13 / var15 * var9;
                     }

                     Vec3 var17 = var1.getDeltaMovement();
                     var1.setDeltaMovement(var11, var17.y, var13);
                  }
               }
            }
         }
      }
   }
}

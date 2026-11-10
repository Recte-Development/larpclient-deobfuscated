package com.larpclient.module;

import com.larpclient.nz;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntitySpeedModule extends Module {
   private final b r = this.addSetting(new b("Speed", "Entity speed multiplier", 2.0, 1.0, 5.0, 0.1));

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public EntitySpeedModule() {
      super("EntitySpeed", "Ride entities faster", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (nz.v()) {
            int var10000 = mc.player.isPassenger();
            boolean var19 = false;
            byte var18 = (byte)var10000;
            var10000 = Integer.signum(var18);
            if ((var10000 | -var10000) >>> 31 != 0) {
               Entity var1 = mc.player.getVehicle();
               if (var1 != null) {
                  float var2 = mc.player.getYRot();
                  double var3 = Math.toRadians(var2);
                  double var5 = 0.0;
                  double var7 = 0.0;
                  if (mc.options.keyUp.isDown()) {
                     var5++;
                  }

                  if (mc.options.keyDown.isDown()) {
                     var5--;
                  }

                  int var27 = mc.options.keyLeft.isDown();
                  boolean var21 = false;
                  byte var20 = (byte)var27;
                  var27 = Integer.signum(var20);
                  if (((var27 | -var27) >>> 31 ^ 1) == 0) {
                     var7++;
                  }

                  int var29 = mc.options.keyRight.isDown();
                  boolean var23 = false;
                  byte var22 = (byte)var29;
                  var29 = Integer.signum(var22);
                  if (((var29 | -var29) >>> 31 ^ 1) == 0) {
                     var7--;
                  }

                  double var33;
                  var29 = (var33 = var5 - 0.0) == 0.0 ? 0 : (var33 < 0.0 ? -1 : 1);
                  boolean var25 = false;
                  int var24 = var29;
                  var29 = Integer.signum(var24);
                  if ((var29 | -var29) >>> 31 != 0 || var7 != 0.0) {
                     double var9 = this.r.getValue() * 0.3;
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

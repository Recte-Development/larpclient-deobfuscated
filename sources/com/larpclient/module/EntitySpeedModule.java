package com.larpclient.module;

import com.larpclient.te;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntitySpeedModule extends Module {
   private final d9 r = this.addSetting(new d9("Speed", "Entity speed multiplier", 2.0, 1.0, 5.0, 0.1));

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public EntitySpeedModule() {
      super("EntitySpeed", "Ride entities faster", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (te.i()) {
            if (mc.player.isPassenger()) {
               Entity var1 = mc.player.getVehicle();
               if (var1 != null) {
                  float var2 = mc.player.getYRot();
                  double var3 = Math.toRadians(var2);
                  double var5 = 0.0;
                  double var7 = 0.0;
                  int var10000 = mc.options.keyUp.isDown();
                  boolean var21 = false;
                  byte var20 = (byte)var10000;
                  var10000 = Integer.signum(var20);
                  if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                     var5++;
                  }

                  if (mc.options.keyDown.isDown()) {
                     var5--;
                  }

                  if (mc.options.keyLeft.isDown()) {
                     var7++;
                  }

                  if (mc.options.keyRight.isDown()) {
                     var7--;
                  }

                  double var32;
                  var10000 = (var32 = var5 - 0.0) == 0.0 ? 0 : (var32 < 0.0 ? -1 : 1);
                  boolean var23 = false;
                  int var22 = var10000;
                  var10000 = Integer.signum(var22);
                  if ((var10000 | -var10000) >>> 31 == 0) {
                     double var33;
                     var10000 = (var33 = var7 - 0.0) == 0.0 ? 0 : (var33 < 0.0 ? -1 : 1);
                     boolean var25 = false;
                     int var24 = var10000;
                     var10000 = Integer.signum(var24);
                     if ((var10000 | -var10000) >>> 31 == 0) {
                        return;
                     }
                  }

                  double var9 = this.r.P() * 0.3;
                  double var11 = -Math.sin(var3) * var5 + Math.cos(var3) * var7;
                  double var13 = Math.cos(var3) * var5 + Math.sin(var3) * var7;
                  double var15 = Math.sqrt(var11 * var11 + var13 * var13);
                  double var34;
                  var10000 = (var34 = var15 - 0.0) == 0.0 ? 0 : (var34 < 0.0 ? -1 : 1);
                  boolean var19 = false;
                  int var18 = var10000;
                  if ((-Integer.signum(var18) >>> 31 ^ 1) == 0) {
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

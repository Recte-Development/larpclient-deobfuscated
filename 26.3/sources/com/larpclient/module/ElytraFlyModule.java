package com.larpclient.module;

import com.larpclient.nz;
import com.larpclient.sy;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

public class ElytraFlyModule extends Module {
   private final b c = this.addSetting(new b("Speed", "Target flight speed", 1.5, 0.5, 5.0, 0.1));
   private final o i = this.addSetting(new o("Mode", "Bounded elytra assistance", "Boost", "Boost", "Glide", "Control"));

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public ElytraFlyModule() {
      super("ElytraFly", "Control elytra speed and flight", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         int var10000 = mc.player.isFallFlying();
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            int var13 = nz.v();
            boolean var7 = false;
            byte var6 = (byte)var13;
            var13 = Integer.signum(var6);
            if (((var13 | -var13) >>> 31 ^ 1) == 0) {
               int var15 = sy.L(this);
               boolean var9 = false;
               byte var8 = (byte)var15;
               var15 = Integer.signum(var8);
               if (((var15 | -var15) >>> 31 ^ 1) != 0) {
                  Vec3 var1 = mc.player.getDeltaMovement();
                  Vec3 var2 = mc.player.getLookAngle().scale(this.c.getValue());
                  int var17 = this.i.w("Glide");
                  boolean var11 = false;
                  byte var10 = (byte)var17;
                  var17 = Integer.signum(var10);
                  if (((var17 | -var17) >>> 31 ^ 1) == 0) {
                     Vec3 var3 = new Vec3(var1.x, 0.0, var1.z);
                     if (var3.length() > this.c.getValue()) {
                        var3 = var3.normalize().scale(this.c.getValue());
                     }

                     var2 = new Vec3(var3.x, Math.max(var1.y, -0.02), var3.z);
                  }

                  mc.player.setDeltaMovement(nz.g(var1, var2, this.i.w("Boost") ? 0.04 : 0.08));
                  return;
               }
            }
         }
      }
   }
}

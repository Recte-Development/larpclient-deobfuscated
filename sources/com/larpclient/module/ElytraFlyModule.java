package com.larpclient.module;

import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public class ElytraFlyModule extends Module {
   private final lc B = this.addSetting(new lc("Speed", "Target flight speed", 1.5, 0.5, 5.0, 0.1));
   private final lp y = this.addSetting(new lp("Mode", "Bounded elytra assistance", "Boost", "Boost", "Glide", "Control"));

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
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
            int var13 = yu.r();
            boolean var7 = false;
            byte var6 = (byte)var13;
            var13 = Integer.signum(var6);
            if ((var13 | -var13) >>> 31 != 0) {
               Vec3 var1 = mc.player.getDeltaMovement();
               Vec3 var2 = mc.player.getLookAngle().scale(this.B.getValue());
               if (this.y.i("Glide")) {
                  Vec3 var3 = new Vec3(var1.x, 0.0, var1.z);
                  double var18;
                  var13 = (var18 = var3.length() - this.B.getValue()) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
                  boolean var9 = false;
                  int var8 = var13;
                  if ((-Integer.signum(var8) >>> 31 ^ 1) == 0) {
                     var3 = var3.normalize().scale(this.B.getValue());
                  }

                  var2 = new Vec3(var3.x, Math.max(var1.y, -0.02), var3.z);
               }

               LocalPlayer var16 = mc.player;
               int var10003 = this.y.i("Boost");
               boolean var11 = false;
               byte var10 = (byte)var10003;
               var10003 = Integer.signum(var10);
               var16.setDeltaMovement(yu.y(var1, var2, ((var10003 | -var10003) >>> 31 ^ 1) == 0 ? 0.04 : 0.08));
               return;
            }
         }
      }
   }
}

package com.larpclient.module;

import com.larpclient.c5;
import com.larpclient.cu;
import java.util.Set;
import net.minecraft.world.phys.Vec3;

public class ElytraFlyModule extends Module {
   private final e F = this.addSetting(new e("Speed", "Target flight speed", 1.5, 0.5, 5.0, 0.1));
   private final l H = this.addSetting(new l("Mode", "Bounded elytra assistance", "Boost", "Boost", "Glide", "Control"));

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   public ElytraFlyModule() {
      super("ElytraFly", "Control elytra speed and flight", Category.MOVEMENT, -1);
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.isFallFlying()) {
         int var10000 = c5.J();
         boolean var5 = false;
         byte var4 = (byte)var10000;
         var10000 = Integer.signum(var4);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            int var13 = cu.n(this);
            boolean var7 = false;
            byte var6 = (byte)var13;
            var13 = Integer.signum(var6);
            if (((var13 | -var13) >>> 31 ^ 1) != 0) {
               Vec3 var1 = mc.player.getDeltaMovement();
               Vec3 var2 = mc.player.getLookAngle().scale(this.F.getValue());
               int var15 = this.H.O("Glide");
               boolean var9 = false;
               byte var8 = (byte)var15;
               var15 = Integer.signum(var8);
               if (((var15 | -var15) >>> 31 ^ 1) == 0) {
                  Vec3 var3 = new Vec3(var1.x, 0.0, var1.z);
                  double var18;
                  var15 = (var18 = var3.length() - this.F.getValue()) == 0.0 ? 0 : (var18 < 0.0 ? -1 : 1);
                  boolean var11 = false;
                  int var10 = var15;
                  if ((-Integer.signum(var10) >>> 31 ^ 1) == 0) {
                     var3 = var3.normalize().scale(this.F.getValue());
                  }

                  var2 = new Vec3(var3.x, Math.max(var1.y, -0.02), var3.z);
               }

               mc.player.setDeltaMovement(c5.S(var1, var2, this.H.O("Boost") ? 0.04 : 0.08));
               return;
            }
         }
      }
   }
}

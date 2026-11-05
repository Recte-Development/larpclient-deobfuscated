package com.larpclient.module;

import java.util.Set;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.level.ServerPlayer;

public class NoFallModule extends Module {
   private static NoFallModule d;
   private volatile int J;

   public NoFallModule() {
      super("NoFall", "Try to reduce fall damage", Category.MOVEMENT, -1);
      d = this;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public static boolean M(boolean var0) {
      if (d != null && d.isEnabled()) {
         LocalPlayer var1 = getMc().player;
         return var0
            || d != null
               && d.isRuntimeActive()
               && var1 != null
               && var1.isAlive()
               && var1.fallDistance > 2.0
               && var1.getDeltaMovement().y < 0.0
               && !var1.isFallFlying()
               && !var1.getAbilities().flying
               && !var1.isPassenger()
               && !var1.isInWater()
               && !var1.isInLava();
      } else {
         return var0;
      }
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? "Fall protection active" : "Inactive";
   }

   @Override
   public void onEnable() {
      this.J++;
   }

   @Override
   public void onDisable() {
      this.J++;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         IntegratedServer var1 = mc.getSingleplayerServer();
         if (var1 != null) {
            UUID var2 = mc.player.getUUID();
            int var3 = this.J;
            var1.execute(() -> {
               ServerPlayer var4 = var1.getPlayerList().getPlayer(var2);
               if (this.J == var3 && var4 != null && var4.isAlive()) {
                  var4.fallDistance = 0.0;
               }
            });
         }
      }
   }
}

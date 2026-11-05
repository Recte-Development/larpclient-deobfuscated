package com.larpclient.module;

import com.larpclient.r4;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class AutoRespawnModule extends Module {
   private LocalPlayer m;
   private ClientLevel W;

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawn automatically", Category.PLAYER, -1);
   }

   @Override
   protected boolean canActivateWhileDead() {
      return true;
   }

   @Override
   public void onEnable() {
      this.i();
      r4.V("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.i();
   }

   @Override
   public void suspendForDisconnect() {
      this.i();
      super.suspendForDisconnect();
   }

   private void i() {
      if (!this.isInGame() || mc.player != this.m || mc.level != this.W || mc.player.isAlive()) {
         this.m = null;
         this.W = null;
      }
   }

   @Override
   public void onTick() {
      this.i();
      if (this.isInGame()) {
         int var10000 = mc.player.isDeadOrDying();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.m == null) {
            this.m = mc.player;
            this.W = mc.level;
            mc.player.respawn();
            mc.gui.setScreen(null);
            r4.V("AutoRespawn", "Requested respawn; waiting for server confirmation");
            return;
         }
      }
   }
}

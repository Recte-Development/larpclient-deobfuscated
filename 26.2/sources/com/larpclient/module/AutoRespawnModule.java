package com.larpclient.module;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class AutoRespawnModule extends Module {
   private LocalPlayer w;
   private ClientLevel U;

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawn automatically", Category.PLAYER, -1);
   }

   @Override
   protected boolean canActivateWhileDead() {
      return true;
   }

   @Override
   public void onEnable() {
      this.p();
      com.larpclient.h.A("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.p();
   }

   @Override
   public void suspendForDisconnect() {
      this.p();
      super.suspendForDisconnect();
   }

   private void p() {
      if (!this.isInGame() || mc.player != this.w || mc.level != this.U || mc.player.isAlive()) {
         this.w = null;
         this.U = null;
      }
   }

   @Override
   public void onTick() {
      this.p();
      if (this.isInGame()) {
         int var10000 = mc.player.isDeadOrDying();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.w == null) {
            this.w = mc.player;
            this.U = mc.level;
            mc.player.respawn();
            mc.gui.setScreen(null);
            com.larpclient.h.A("AutoRespawn", "Requested respawn; waiting for server confirmation");
            return;
         }
      }
   }
}

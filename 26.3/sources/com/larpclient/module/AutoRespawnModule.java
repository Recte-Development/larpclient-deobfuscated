package com.larpclient.module;

import com.larpclient._q;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class AutoRespawnModule extends Module {
   private LocalPlayer B;
   private ClientLevel q;

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawn automatically", Category.PLAYER, -1);
   }

   @Override
   protected boolean canActivateWhileDead() {
      return true;
   }

   @Override
   public void onEnable() {
      this.U();
      _q.z("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.U();
   }

   @Override
   public void suspendForDisconnect() {
      this.U();
      super.suspendForDisconnect();
   }

   private void U() {
      if (!this.isInGame() || mc.player != this.B || mc.level != this.q || mc.player.isAlive()) {
         this.B = null;
         this.q = null;
      }
   }

   @Override
   public void onTick() {
      this.U();
      if (this.isInGame()) {
         int var10000 = mc.player.isDeadOrDying();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.B == null) {
            this.B = mc.player;
            this.q = mc.level;
            mc.player.respawn();
            mc.gui.setScreen(null);
            _q.z("AutoRespawn", "Requested respawn; waiting for server confirmation");
            return;
         }
      }
   }
}

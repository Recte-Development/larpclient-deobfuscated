package com.larpclient.module.player;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class AutoRespawnModule extends Module {
   private LocalPlayer respawnPlayer;
   private ClientLevel respawnLevel;

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawn automatically", Category.PLAYER, -1);
   }

   @Override
   protected boolean canActivateWhileDead() {
      return true;
   }

   @Override
   public void onEnable() {
      this.clearIfStale();
      LarpLog.info("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.clearIfStale();
   }

   @Override
   public void suspendForDisconnect() {
      this.clearIfStale();
      super.suspendForDisconnect();
   }

   private void clearIfStale() {
      if (!this.isInGame() || minecraft.player != this.respawnPlayer || minecraft.level != this.respawnLevel || minecraft.player.isAlive()) {
         this.respawnPlayer = null;
         this.respawnLevel = null;
      }
   }

   @Override
   public void onTick() {
      this.clearIfStale();
      if (this.isInGame()) {
         int var10000 = minecraft.player.isDeadOrDying();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.respawnPlayer == null) {
            this.respawnPlayer = minecraft.player;
            this.respawnLevel = minecraft.level;
            minecraft.player.respawn();
            minecraft.gui.setScreen(null);
            LarpLog.info("AutoRespawn", "Requested respawn; waiting for server confirmation");
            return;
         }
      }
   }
}

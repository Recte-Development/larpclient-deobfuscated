package com.larpclient.module;

import com.larpclient.mt;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class AutoRespawnModule extends Module {
   private LocalPlayer y;
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
      this.e();
      mt.K("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.e();
   }

   @Override
   public void suspendForDisconnect() {
      this.e();
      super.suspendForDisconnect();
   }

   private void e() {
      if (!this.isInGame() || mc.player != this.y || mc.level != this.q || mc.player.isAlive()) {
         this.y = null;
         this.q = null;
      }
   }

   @Override
   public void onTick() {
      this.e();
      if (this.isInGame()) {
         int var10000 = mc.player.isDeadOrDying();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.y == null) {
            this.y = mc.player;
            this.q = mc.level;
            mc.player.respawn();
            mc.gui.setScreen(null);
            mt.K("AutoRespawn", "Requested respawn; waiting for server confirmation");
            return;
         }
      }
   }
}

package com.larpclient.module;

import com.larpclient.q8;
import net.minecraft.class_638;
import net.minecraft.class_746;

public class AutoRespawnModule extends Module {
   private class_746 X;
   private class_638 m;

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawn automatically", Category.PLAYER, -1);
   }

   @Override
   protected boolean canActivateWhileDead() {
      return true;
   }

   @Override
   public void onEnable() {
      this.f();
      q8.G("AutoRespawn", "Active; waiting for death or respawn confirmation");
   }

   @Override
   public void onDisable() {
      this.f();
   }

   @Override
   public void suspendForDisconnect() {
      this.f();
      super.suspendForDisconnect();
   }

   private void f() {
      if (!this.isInGame() || mc.field_1724 != this.X || mc.field_1687 != this.m || mc.field_1724.method_5805()) {
         this.X = null;
         this.m = null;
      }
   }

   @Override
   public void onTick() {
      this.f();
      if (this.isInGame()) {
         int var10000 = mc.field_1724.method_29504();
         boolean var2 = false;
         byte var1 = (byte)var10000;
         var10000 = Integer.signum(var1);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.X == null) {
            this.X = mc.field_1724;
            this.m = mc.field_1687;
            mc.field_1724.method_7331();
            mc.method_1507(null);
            q8.G("AutoRespawn", "Requested respawn; waiting for server confirmation");
            return;
         }
      }
   }
}

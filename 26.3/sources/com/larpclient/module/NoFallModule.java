package com.larpclient.module;

import com.larpclient._q;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class NoFallModule extends Module {
   private static final String N = "No Fall";
   private static final double o = 0.5;
   private static NoFallModule Z;
   private volatile int r;
   private LocalPlayer A;
   private double T;
   private boolean X;
   private int y;
   private double M;
   private boolean k;
   private Packet<?> x;

   public NoFallModule() {
      super("NoFall", "Try to reduce fall damage", Category.MOVEMENT, -1);
      Z = this;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public static boolean O(boolean var0) {
      if (Z != null && Z.isEnabled()) {
         NoFallModule var1 = Z;
         LocalPlayer var2 = getMc().player;
         return var1 != null && var2 != null ? var1.J(var2, var0) : var0;
      } else {
         return var0;
      }
   }

   private boolean J(LocalPlayer var1, boolean var2) {
      if (var1 != this.A) {
         this.c(var1);
      }

      double var3 = var1.fallDistance;
      this.M = Math.max(this.M, var3);
      this.X = (boolean)0;
      int var5 = !var2 && !var1.isInWater() && !var1.isInLava() && !var1.onClimbable() && !var1.isPassenger() && !var1.getAbilities().flying && var1.isAlive()
         ? 0
         : 1;
      if (var5 != 0) {
         this.O(var2 ? "landed" : "ended");
         this.T = 0.0;
         return var2;
      }

      if (var3 < this.T) {
         this.T = 0.0;
      }

      if (this.isRuntimeActive() && !this.k && !var1.isFallFlying()) {
         double var6 = var1.getAttributeValue(Attributes.SAFE_FALL_DISTANCE) - 0.5;
         if (var3 - this.T <= var6) {
            return false;
         }

         this.T = var3;
         this.X = (boolean)1;
         this.y++;
         if (this.y == 1) {
            _q.c("No Fall", "Ground report sent after {} blocks of fall", d(var3));
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean z(Packet<?> var0) {
      if (Z != null && Z.isEnabled()) {
         NoFallModule var1 = Z;
         if (var1 != null
            && var1.X
            && var1.x == null
            && var0 instanceof ServerboundPlayerCommandPacket var2
            && var2.getAction() == Action.START_FALL_FLYING
            && getMc().player == var1.A) {
            var1.x = var0;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void B(LocalPlayer var0) {
      NoFallModule var1 = Z;
      if (var1 != null && var1.x != null) {
         Packet var2 = var1.x;
         var1.x = null;
         if (var0 == var1.A && var0.connection != null) {
            var0.connection.send(var2);
            _q.c("No Fall", "Glide start sent after the airborne report, so the server accepts it");
         }
      }
   }

   public static void C(LocalPlayer var0) {
      NoFallModule var1 = Z;
      if (var1 != null && var0 != null && var0 == var1.A && var1.y != 0 && !var1.k) {
         var1.k = (boolean)1;
         var1.X = (boolean)0;
         _q.z(
            "No Fall",
            "Server corrected your position after {} ground report(s) this fall, likely an anticheat (Grim flags airborne ground reports and keeps the damage); real reports until you land",
            var1.y
         );
      }
   }

   private void O(String var1) {
      if (this.y > 0 || this.k) {
         _q.z("No Fall", "Fall {} after {} blocks: {} ground report(s){}", var1, d(this.M), this.y, this.k ? ", stopped by a server correction" : "");
      }

      this.y = 0;
      this.M = 0.0;
      this.k = (boolean)0;
   }

   private void c(LocalPlayer var1) {
      this.A = var1;
      this.T = 0.0;
      this.X = (boolean)0;
      this.y = 0;
      this.M = 0.0;
      this.k = (boolean)0;
      this.x = null;
   }

   private static double d(double var0) {
      return Math.round(var0 * 10.0) / 10.0;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return this.k ? "Paused until landing (server corrected position)" : "Fall protection active";
      }
   }

   @Override
   public void onEnable() {
      this.r++;
   }

   @Override
   public void onDisable() {
      this.r++;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         IntegratedServer var1 = mc.getSingleplayerServer();
         if (var1 != null) {
            UUID var2 = mc.player.getUUID();
            int var3 = this.r;
            var1.execute(() -> {
               ServerPlayer var4 = var1.getPlayerList().getPlayer(var2);
               if (this.r == var3 && var4 != null && var4.isAlive()) {
                  var4.fallDistance = 0.0;
               }
            });
         }
      }
   }
}

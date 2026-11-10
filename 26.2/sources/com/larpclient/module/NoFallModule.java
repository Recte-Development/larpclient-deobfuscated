package com.larpclient.module;

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
   private static final String Q = "No Fall";
   private static final double m = 0.5;
   private static NoFallModule r;
   private volatile int l;
   private LocalPlayer V;
   private double K;
   private boolean W;
   private int M;
   private double g;
   private boolean J;
   private Packet<?> y;

   public NoFallModule() {
      super("NoFall", "Try to reduce fall damage", Category.MOVEMENT, -1);
      r = this;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   public static boolean f(boolean var0) {
      if (r != null && r.isEnabled()) {
         NoFallModule var1 = r;
         LocalPlayer var2 = getMc().player;
         return var1 != null && var2 != null ? var1.d(var2, var0) : var0;
      } else {
         return var0;
      }
   }

   private boolean d(LocalPlayer var1, boolean var2) {
      if (var1 != this.V) {
         this.b(var1);
      }

      double var3 = var1.fallDistance;
      this.g = Math.max(this.g, var3);
      this.W = (boolean)0;
      int var5 = !var2 && !var1.isInWater() && !var1.isInLava() && !var1.onClimbable() && !var1.isPassenger() && !var1.getAbilities().flying && var1.isAlive()
         ? 0
         : 1;
      if (var5 != 0) {
         this.K(var2 ? "landed" : "ended");
         this.K = 0.0;
         return var2;
      }

      if (var3 < this.K) {
         this.K = 0.0;
      }

      if (this.isRuntimeActive() && !this.J && !var1.isFallFlying()) {
         double var6 = var1.getAttributeValue(Attributes.SAFE_FALL_DISTANCE) - 0.5;
         if (var3 - this.K <= var6) {
            return false;
         }

         this.K = var3;
         this.W = (boolean)1;
         this.M++;
         if (this.M == 1) {
            com.larpclient.h.U("No Fall", "Ground report sent after {} blocks of fall", Z(var3));
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean F(Packet<?> var0) {
      if (r != null && r.isEnabled()) {
         NoFallModule var1 = r;
         if (var1 != null
            && var1.W
            && var1.y == null
            && var0 instanceof ServerboundPlayerCommandPacket var2
            && var2.getAction() == Action.START_FALL_FLYING
            && getMc().player == var1.V) {
            var1.y = var0;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void R(LocalPlayer var0) {
      NoFallModule var1 = r;
      if (var1 != null && var1.y != null) {
         Packet var2 = var1.y;
         var1.y = null;
         if (var0 == var1.V && var0.connection != null) {
            var0.connection.send(var2);
            com.larpclient.h.U("No Fall", "Glide start sent after the airborne report, so the server accepts it");
         }
      }
   }

   public static void M(LocalPlayer var0) {
      NoFallModule var1 = r;
      if (var1 != null && var0 != null && var0 == var1.V && var1.M != 0 && !var1.J) {
         var1.J = (boolean)1;
         var1.W = (boolean)0;
         com.larpclient.h.A(
            "No Fall",
            "Server corrected your position after {} ground report(s) this fall, likely an anticheat (Grim flags airborne ground reports and keeps the damage); real reports until you land",
            var1.M
         );
      }
   }

   private void K(String var1) {
      if (this.M > 0 || this.J) {
         com.larpclient.h.A(
            "No Fall", "Fall {} after {} blocks: {} ground report(s){}", var1, Z(this.g), this.M, this.J ? ", stopped by a server correction" : ""
         );
      }

      this.M = 0;
      this.g = 0.0;
      this.J = (boolean)0;
   }

   private void b(LocalPlayer var1) {
      this.V = var1;
      this.K = 0.0;
      this.W = (boolean)0;
      this.M = 0;
      this.g = 0.0;
      this.J = (boolean)0;
      this.y = null;
   }

   private static double Z(double var0) {
      return Math.round(var0 * 10.0) / 10.0;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return this.J ? "Paused until landing (server corrected position)" : "Fall protection active";
      }
   }

   @Override
   public void onEnable() {
      this.l++;
   }

   @Override
   public void onDisable() {
      this.l++;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         IntegratedServer var1 = mc.getSingleplayerServer();
         if (var1 != null) {
            UUID var2 = mc.player.getUUID();
            int var3 = this.l;
            var1.execute(() -> {
               ServerPlayer var4 = var1.getPlayerList().getPlayer(var2);
               if (this.l == var3 && var4 != null && var4.isAlive()) {
                  var4.fallDistance = 0.0;
               }
            });
         }
      }
   }
}

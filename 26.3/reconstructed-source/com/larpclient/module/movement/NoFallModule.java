package com.larpclient.module.movement;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.LarpLog;
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
   private static final String lOG_TAG = "No Fall";
   private static final double sAFE_FALL_MARGIN = 0.5;
   private static NoFallModule instance;
   private volatile int generation;
   private LocalPlayer trackedPlayer;
   private double lastReportedFall;
   private boolean reportedThisTick;
   private int groundReportCount;
   private double maxFallDistance;
   private boolean serverCorrected;
   private Packet<?> heldGlidePacket;

   public NoFallModule() {
      super("NoFall", "Try to reduce fall damage", Category.MOVEMENT, -1);
      instance = this;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   public static boolean modifyOnGround(boolean var0) {
      if (instance != null && instance.isEnabled()) {
         NoFallModule var1 = instance;
         LocalPlayer var2 = getMc().player;
         return var1 != null && var2 != null ? var1.evaluateGroundReport(var2, var0) : var0;
      } else {
         return var0;
      }
   }

   private boolean evaluateGroundReport(LocalPlayer var1, boolean var2) {
      if (var1 != this.trackedPlayer) {
         this.resetFor(var1);
      }

      double var3 = var1.fallDistance;
      this.maxFallDistance = Math.max(this.maxFallDistance, var3);
      this.reportedThisTick = (boolean)0;
      int var5 = !var2 && !var1.isInWater() && !var1.isInLava() && !var1.onClimbable() && !var1.isPassenger() && !var1.getAbilities().flying && var1.isAlive()
         ? 0
         : 1;
      if (var5 != 0) {
         this.modifyOnGround(var2 ? "landed" : "ended");
         this.lastReportedFall = 0.0;
         return var2;
      }

      if (var3 < this.lastReportedFall) {
         this.lastReportedFall = 0.0;
      }

      if (this.isRuntimeActive() && !this.serverCorrected && !var1.isFallFlying()) {
         double var6 = var1.getAttributeValue(Attributes.SAFE_FALL_DISTANCE) - 0.5;
         if (var3 - this.lastReportedFall <= var6) {
            return false;
         }

         this.lastReportedFall = var3;
         this.reportedThisTick = (boolean)1;
         this.groundReportCount++;
         if (this.groundReportCount == 1) {
            LarpLog.debug("No Fall", "Ground report sent after {} blocks of fall", roundToTenth(var3));
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean holdGlideStart(Packet<?> var0) {
      if (instance != null && instance.isEnabled()) {
         NoFallModule var1 = instance;
         if (var1 != null
            && var1.reportedThisTick
            && var1.heldGlidePacket == null
            && var0 instanceof ServerboundPlayerCommandPacket var2
            && var2.getAction() == Action.START_FALL_FLYING
            && getMc().player == var1.trackedPlayer) {
            var1.heldGlidePacket = var0;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void flushHeldPacket(LocalPlayer var0) {
      NoFallModule var1 = instance;
      if (var1 != null && var1.heldGlidePacket != null) {
         Packet var2 = var1.heldGlidePacket;
         var1.heldGlidePacket = null;
         if (var0 == var1.trackedPlayer && var0.connection != null) {
            var0.connection.send(var2);
            LarpLog.debug("No Fall", "Glide start sent after the airborne report, so the server accepts it");
         }
      }
   }

   public static void onPositionCorrection(LocalPlayer var0) {
      NoFallModule var1 = instance;
      if (var1 != null && var0 != null && var0 == var1.trackedPlayer && var1.groundReportCount != 0 && !var1.serverCorrected) {
         var1.serverCorrected = (boolean)1;
         var1.reportedThisTick = (boolean)0;
         LarpLog.info(
            "No Fall",
            "Server corrected your position after {} ground report(s) this fall, likely an anticheat (Grim flags airborne ground reports and keeps the damage); real reports until you land",
            var1.groundReportCount
         );
      }
   }

   private void modifyOnGround(String var1) {
      if (this.groundReportCount > 0 || this.serverCorrected) {
         LarpLog.info(
            "No Fall",
            "Fall {} after {} blocks: {} ground report(s){}",
            var1,
            roundToTenth(this.maxFallDistance),
            this.groundReportCount,
            this.serverCorrected ? ", stopped by a server correction" : ""
         );
      }

      this.groundReportCount = 0;
      this.maxFallDistance = 0.0;
      this.serverCorrected = (boolean)0;
   }

   private void resetFor(LocalPlayer var1) {
      this.trackedPlayer = var1;
      this.lastReportedFall = 0.0;
      this.reportedThisTick = (boolean)0;
      this.groundReportCount = 0;
      this.maxFallDistance = 0.0;
      this.serverCorrected = (boolean)0;
      this.heldGlidePacket = null;
   }

   private static double roundToTenth(double var0) {
      return Math.round(var0 * 10.0) / 10.0;
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      } else {
         return this.serverCorrected ? "Paused until landing (server corrected position)" : "Fall protection active";
      }
   }

   @Override
   public void onEnable() {
      this.generation++;
   }

   @Override
   public void onDisable() {
      this.generation++;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         IntegratedServer var1 = minecraft.getSingleplayerServer();
         if (var1 != null) {
            UUID var2 = minecraft.player.getUUID();
            int var3 = this.generation;
            var1.execute(() -> {
               ServerPlayer var4 = var1.getPlayerList().getPlayer(var2);
               if (this.generation == var3 && var4 != null && var4.isAlive()) {
                  var4.fallDistance = 0.0;
               }
            });
         }
      }
   }
}

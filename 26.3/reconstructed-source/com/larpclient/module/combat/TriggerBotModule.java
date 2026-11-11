package com.larpclient.module.combat;

import com.larpclient.hud.KeystrokesHud;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.data.DwellTimer;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TriggerBotModule extends Module {
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final BooleanSetting playersOnly = this.addSetting(new BooleanSetting("Players Only", "Only attack players", false));
   private final BooleanSetting smartDelay = this.addSetting(new BooleanSetting("Smart Delay", "Wait for attack cooldown", true));
   private final DwellTimer dwellTimer = new DwellTimer();
   private Entity target;
   private String status = "Waiting for a target";
   private long lastLogMillis;

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   @Override
   public void onDisable() {
      this.reset("Waiting for a target");
      LarpLog.debug("TriggerBot", "Released target and queued attack");
   }

   private void reset(String var1) {
      this.target = null;
      this.dwellTimer.reset();
      InteractionArbiter.findAttackBlocker(this);
      this.setStatus(var1);
   }

   public TriggerBotModule() {
      super("TriggerBot", "Attack the entity you aim at", Category.COMBAT, -1);
   }

   @Override
   public void onTick() {
      if (!this.canAct()) {
         this.reset("Paused: input unavailable");
      } else {
         Entity var1 = this.findTarget();
         int var10000 = this.isValidTarget(var1);
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.reset("Waiting for a target");
         } else {
            String var2 = InteractionArbiter.findAttackBlocker(var1, minecraft.player.entityInteractionRange(), minecraft.player.getLookAngle());
            if (var2 != null) {
               this.reset("Waiting: " + var2);
            } else {
               this.target = var1;
               int var12 = this.dwellTimer.isReady(var1, minecraft.level.getGameTime(), this.delay.getValue().intValue());
               boolean var4 = false;
               byte var3 = (byte)var12;
               var12 = Integer.signum(var3);
               if ((var12 | -var12) >>> 31 == 0) {
                  this.setStatus("Waiting for delay");
               } else {
                  int var14 = this.smartDelay.getValue();
                  boolean var8 = false;
                  byte var7 = (byte)var14;
                  var14 = Integer.signum(var7);
                  if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                     int var16 = InteractionArbiter.processQueue();
                     boolean var10 = false;
                     byte var9 = (byte)var16;
                     var16 = Integer.signum(var9);
                     if ((var16 | -var16) >>> 31 == 0) {
                        this.setStatus("Waiting for attack cooldown");
                        return;
                     }
                  }

                  this.setStatus("Ready");
                  InteractionArbiter.queueAttackWithCallbacks(
                     this,
                     var1,
                     minecraft.player.entityInteractionRange(),
                     this.smartDelay.getValue(),
                     () -> this.canAct() && this.target == var1 && this.isValidTarget(var1),
                     1,
                     () -> {
                        this.dwellTimer.markFired(var1, minecraft.level.getGameTime());
                        KeystrokesHud.recordAttack();
                        this.setStatus("Attacked " + var1.getName().getString());
                     },
                     var1x -> this.setStatus("Waiting: " + var1x)
                  );
               }
            }
         }
      }
   }

   private boolean canAct() {
      return this.isInGame()
         && InteractionArbiter.canInteract()
         && minecraft.isWindowActive()
         && minecraft.gui.overlay() == null
         && !minecraft.player.isUsingItem()
         && !minecraft.options.keyUse.isDown();
   }

   private boolean isValidTarget(Entity var1) {
      return var1 instanceof LivingEntity var2
         && var2.isAlive()
         && !var1.isRemoved()
         && var1.level() == minecraft.level
         && var1 != minecraft.player
         && (!this.playersOnly.getValue() || var1 instanceof Player);
   }

   private Entity findTarget() {
      Vec3 var1 = minecraft.player.getEyePosition();
      Vec3 var2 = minecraft.player.getLookAngle();
      double var3 = minecraft.player.entityInteractionRange();
      Vec3 var5 = var1.add(var2.scale(var3));
      return raycastEntity(minecraft.player, var1, var2, var3, minecraft.level.getEntities(minecraft.player, new AABB(var1, var5).inflate(1.0)));
   }

   private static Entity raycastEntity(Entity var0, Vec3 var1, Vec3 var2, double var3, Iterable<Entity> var5) {
      Entity var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (Entity var10 : var5) {
         if (var10 != var0 && var10.isPickable() && !var10.isSpectator() && !var10.isRemoved() && var10.getRootVehicle() != var0.getRootVehicle()) {
            AABB var11 = var10.getBoundingBox().inflate(var10.getPickRadius());
            if (!var11.contains(var1) || var10.canBePickedFromInside()) {
               Optional var12 = BoxRaycastUtil.raycastBox(var1, var2, var11, var3);
               if (!var12.isEmpty()) {
                  double var13 = var1.distanceToSqr((Vec3)var12.get());
                  if (var13 < var7) {
                     var6 = var10;
                     var7 = var13;
                  }
               }
            }
         }
      }

      return var6;
   }

   private void setStatus(String var1) {
      if (!this.status.equals(var1)) {
         this.status = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.lastLogMillis >= 1000L) {
            this.lastLogMillis = var2;
            LarpLog.debug("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.reset("Waiting for a target");
      LarpLog.debug(
         "TriggerBot",
         "Fresh body-ray trigger active; delay={}, cooldown={}, players={}",
         this.delay.getValue(),
         this.smartDelay.getValue(),
         this.playersOnly.getValue()
      );
   }

   @Override
   public String getStatus() {
      return this.status;
   }
}

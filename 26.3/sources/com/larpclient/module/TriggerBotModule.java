package com.larpclient.module;

import com.larpclient._k;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.di;
import com.larpclient.jz;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TriggerBotModule extends Module {
   private final b M = this.addSetting(new b("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final m m = this.addSetting(new m("Players Only", "Only attack players", false));
   private final m Z = this.addSetting(new m("Smart Delay", "Wait for attack cooldown", true));
   private final di O = new di();
   private Entity K;
   private String j = "Waiting for a target";
   private long a;

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }

   @Override
   public void onDisable() {
      this.K("Waiting for a target");
      _q.c("TriggerBot", "Released target and queued attack");
   }

   private void K(String var1) {
      this.K = null;
      this.O.z();
      _p.e(this);
      this.H(var1);
   }

   public TriggerBotModule() {
      super("TriggerBot", "Attack the entity you aim at", Category.COMBAT, -1);
   }

   @Override
   public void onTick() {
      if (!this.W()) {
         this.K("Paused: input unavailable");
      } else {
         Entity var1 = this.C();
         int var10000 = this.n(var1);
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.K("Waiting for a target");
         } else {
            String var2 = _p.e(var1, mc.player.entityInteractionRange(), mc.player.getLookAngle());
            if (var2 != null) {
               this.K("Waiting: " + var2);
            } else {
               this.K = var1;
               int var12 = this.O.m(var1, mc.level.getGameTime(), this.M.getValue().intValue());
               boolean var4 = false;
               byte var3 = (byte)var12;
               var12 = Integer.signum(var3);
               if ((var12 | -var12) >>> 31 == 0) {
                  this.H("Waiting for delay");
               } else {
                  int var14 = this.Z.getValue();
                  boolean var8 = false;
                  byte var7 = (byte)var14;
                  var14 = Integer.signum(var7);
                  if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                     int var16 = _p.P();
                     boolean var10 = false;
                     byte var9 = (byte)var16;
                     var16 = Integer.signum(var9);
                     if ((var16 | -var16) >>> 31 == 0) {
                        this.H("Waiting for attack cooldown");
                        return;
                     }
                  }

                  this.H("Ready");
                  _p.U(this, var1, mc.player.entityInteractionRange(), this.Z.getValue(), () -> this.W() && this.K == var1 && this.n(var1), 1, () -> {
                     this.O.S(var1, mc.level.getGameTime());
                     _k.y();
                     this.H("Attacked " + var1.getName().getString());
                  }, var1x -> this.H("Waiting: " + var1x));
               }
            }
         }
      }
   }

   private boolean W() {
      return this.isInGame() && _p.g() && mc.isWindowActive() && mc.gui.overlay() == null && !mc.player.isUsingItem() && !mc.options.keyUse.isDown();
   }

   private boolean n(Entity var1) {
      return var1 instanceof LivingEntity var2
         && var2.isAlive()
         && !var1.isRemoved()
         && var1.level() == mc.level
         && var1 != mc.player
         && (!this.m.getValue() || var1 instanceof Player);
   }

   private Entity C() {
      Vec3 var1 = mc.player.getEyePosition();
      Vec3 var2 = mc.player.getLookAngle();
      double var3 = mc.player.entityInteractionRange();
      Vec3 var5 = var1.add(var2.scale(var3));
      return S(mc.player, var1, var2, var3, mc.level.getEntities(mc.player, new AABB(var1, var5).inflate(1.0)));
   }

   private static Entity S(Entity var0, Vec3 var1, Vec3 var2, double var3, Iterable<Entity> var5) {
      Entity var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (Entity var10 : var5) {
         if (var10 != var0 && var10.isPickable() && !var10.isSpectator() && !var10.isRemoved() && var10.getRootVehicle() != var0.getRootVehicle()) {
            AABB var11 = var10.getBoundingBox().inflate(var10.getPickRadius());
            if (!var11.contains(var1) || var10.canBePickedFromInside()) {
               Optional var12 = jz.D(var1, var2, var11, var3);
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

   private void H(String var1) {
      if (!this.j.equals(var1)) {
         this.j = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.a >= 1000L) {
            this.a = var2;
            _q.c("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.K("Waiting for a target");
      _q.c("TriggerBot", "Fresh body-ray trigger active; delay={}, cooldown={}, players={}", this.M.getValue(), this.Z.getValue(), this.m.getValue());
   }

   @Override
   public String getStatus() {
      return this.j;
   }
}

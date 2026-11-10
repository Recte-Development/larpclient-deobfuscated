package com.larpclient.module;

import com.larpclient.jt;
import com.larpclient.ta;
import com.larpclient.wi;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TriggerBotModule extends Module {
   private final e j = this.addSetting(new e("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final s p = this.addSetting(new s("Players Only", "Only attack players", false));
   private final s g = this.addSetting(new s("Smart Delay", "Wait for attack cooldown", true));
   private final jt A = new jt();
   private Entity E;
   private String O = "Waiting for a target";
   private long o;

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K);
   }

   @Override
   public void onDisable() {
      this.u("Waiting for a target");
      com.larpclient.h.U("TriggerBot", "Released target and queued attack");
   }

   private void u(String var1) {
      this.E = null;
      this.A.U();
      ta.s(this);
      this.d(var1);
   }

   public TriggerBotModule() {
      super("TriggerBot", "Attack the entity you aim at", Category.COMBAT, -1);
   }

   @Override
   public void onTick() {
      int var10000 = this.L();
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.u("Paused: input unavailable");
      } else {
         Entity var1 = this.j();
         if (!this.P(var1)) {
            this.u("Waiting for a target");
         } else {
            String var2 = ta.I(var1, mc.player.entityInteractionRange(), mc.player.getLookAngle());
            if (var2 != null) {
               this.u("Waiting: " + var2);
            } else {
               this.E = var1;
               int var12 = this.A.x(var1, mc.level.getGameTime(), this.j.getValue().intValue());
               boolean var10 = false;
               byte var9 = (byte)var12;
               var12 = Integer.signum(var9);
               if ((var12 | -var12) >>> 31 == 0) {
                  this.d("Waiting for delay");
               } else {
                  int var14 = this.g.getValue();
                  boolean var6 = false;
                  byte var5 = (byte)var14;
                  var14 = Integer.signum(var5);
                  if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                     int var16 = ta.K();
                     boolean var8 = false;
                     byte var7 = (byte)var16;
                     var16 = Integer.signum(var7);
                     if ((var16 | -var16) >>> 31 == 0) {
                        this.d("Waiting for attack cooldown");
                        return;
                     }
                  }

                  this.d("Ready");
                  ta.M(this, var1, mc.player.entityInteractionRange(), this.g.getValue(), () -> this.L() && this.E == var1 && this.P(var1), 1, () -> {
                     this.A.b(var1, mc.level.getGameTime());
                     com.larpclient.to.B();
                     this.d("Attacked " + var1.getName().getString());
                  }, var1x -> this.d("Waiting: " + var1x));
               }
            }
         }
      }
   }

   private boolean L() {
      return this.isInGame() && ta.Y() && mc.isWindowActive() && mc.gui.overlay() == null && !mc.player.isUsingItem() && !mc.options.keyUse.isDown();
   }

   private boolean P(Entity var1) {
      return var1 instanceof LivingEntity var2
         && var2.isAlive()
         && !var1.isRemoved()
         && var1.level() == mc.level
         && var1 != mc.player
         && (!this.p.getValue() || var1 instanceof Player);
   }

   private Entity j() {
      Vec3 var1 = mc.player.getEyePosition();
      Vec3 var2 = mc.player.getLookAngle();
      double var3 = mc.player.entityInteractionRange();
      Vec3 var5 = var1.add(var2.scale(var3));
      return f(mc.player, var1, var2, var3, mc.level.getEntities(mc.player, new AABB(var1, var5).inflate(1.0)));
   }

   private static Entity f(Entity var0, Vec3 var1, Vec3 var2, double var3, Iterable<Entity> var5) {
      Entity var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (Entity var10 : var5) {
         if (var10 != var0 && var10.isPickable() && !var10.isSpectator() && !var10.isRemoved() && var10.getRootVehicle() != var0.getRootVehicle()) {
            AABB var11 = var10.getBoundingBox().inflate(var10.getPickRadius());
            if (!var11.contains(var1) || var10.canBePickedFromInside()) {
               Optional var12 = wi.x(var1, var2, var11, var3);
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

   private void d(String var1) {
      if (!this.O.equals(var1)) {
         this.O = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.o >= 1000L) {
            this.o = var2;
            com.larpclient.h.U("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.u("Waiting for a target");
      com.larpclient.h.U(
         "TriggerBot", "Fresh body-ray trigger active; delay={}, cooldown={}, players={}", this.j.getValue(), this.g.getValue(), this.p.getValue()
      );
   }

   @Override
   public String getStatus() {
      return this.O;
   }
}

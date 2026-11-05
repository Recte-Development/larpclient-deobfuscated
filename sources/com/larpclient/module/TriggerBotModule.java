package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.d_;
import com.larpclient.r4;
import com.larpclient.yz;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TriggerBotModule extends Module {
   private final lc I = this.addSetting(new lc("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final ln J = this.addSetting(new ln("Players Only", "Only attack players", false));
   private final ln S = this.addSetting(new ln("Smart Delay", "Wait for attack cooldown", true));
   private final yz C = new yz();
   private Entity W;
   private String N = "Waiting for a target";
   private long p;

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   @Override
   public void onDisable() {
      this.l("Waiting for a target");
      r4.g("TriggerBot", "Released target and queued attack");
   }

   private void l(String var1) {
      this.W = null;
      this.C.K();
      d8.K(this);
      this.f(var1);
   }

   public TriggerBotModule() {
      super("TriggerBot", "Attack the entity you aim at", Category.COMBAT, -1);
   }

   @Override
   public void onTick() {
      int var10000 = this.I();
      boolean var4 = false;
      byte var3 = (byte)var10000;
      var10000 = Integer.signum(var3);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.l("Paused: input unavailable");
      } else {
         Entity var1 = this.i();
         int var12 = this.U(var1);
         boolean var8 = false;
         byte var7 = (byte)var12;
         var12 = Integer.signum(var7);
         if ((var12 | -var12) >>> 31 == 0) {
            this.l("Waiting for a target");
         } else {
            String var2 = d8.R(var1, mc.player.entityInteractionRange(), mc.player.getLookAngle());
            if (var2 != null) {
               this.l("Waiting: " + var2);
            } else {
               this.W = var1;
               int var14 = this.C.e(var1, mc.level.getGameTime(), this.I.getValue().intValue());
               boolean var6 = false;
               byte var5 = (byte)var14;
               var14 = Integer.signum(var5);
               if ((var14 | -var14) >>> 31 == 0) {
                  this.f("Waiting for delay");
               } else {
                  if (this.S.getValue()) {
                     int var16 = d8.R();
                     boolean var10 = false;
                     byte var9 = (byte)var16;
                     var16 = Integer.signum(var9);
                     if ((var16 | -var16) >>> 31 == 0) {
                        this.f("Waiting for attack cooldown");
                        return;
                     }
                  }

                  this.f("Ready");
                  d8.n(this, var1, mc.player.entityInteractionRange(), this.S.getValue(), () -> this.I() && this.W == var1 && this.U(var1), 1, () -> {
                     this.C.P(var1, mc.level.getGameTime());
                     this.f("Attacked " + var1.getName().getString());
                  }, var1x -> this.f("Waiting: " + var1x));
               }
            }
         }
      }
   }

   private boolean I() {
      return this.isInGame() && d8.a() && mc.isWindowActive() && mc.gui.overlay() == null && !mc.player.isUsingItem() && !mc.options.keyUse.isDown();
   }

   private boolean U(Entity var1) {
      return var1 instanceof LivingEntity var2
         && var2.isAlive()
         && !var1.isRemoved()
         && var1.level() == mc.level
         && var1 != mc.player
         && (!this.J.getValue() || var1 instanceof Player);
   }

   private Entity i() {
      Vec3 var1 = mc.player.getEyePosition();
      Vec3 var2 = mc.player.getLookAngle();
      double var3 = mc.player.entityInteractionRange();
      Vec3 var5 = var1.add(var2.scale(var3));
      return K(mc.player, var1, var2, var3, mc.level.getEntities(mc.player, new AABB(var1, var5).inflate(1.0)));
   }

   private static Entity K(Entity var0, Vec3 var1, Vec3 var2, double var3, Iterable<Entity> var5) {
      Entity var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (Entity var10 : var5) {
         if (var10 != var0
            && var10.isPickable()
            && !var10.isSpectator()
            && !var10.isRemoved()
            && var10.getRootVehicle() != var0.getRootVehicle()
            && (!var10.getBoundingBox().contains(var1) || var10.canBePickedFromInside())) {
            Optional var11 = d_.b(var1, var2, var10.getBoundingBox(), var3);
            if (!var11.isEmpty()) {
               double var12 = var1.distanceToSqr((Vec3)var11.get());
               if (var12 < var7) {
                  var6 = var10;
                  var7 = var12;
               }
            }
         }
      }

      return var6;
   }

   private void f(String var1) {
      if (!this.N.equals(var1)) {
         this.N = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.p >= 1000L) {
            this.p = var2;
            r4.g("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.l("Waiting for a target");
      r4.g("TriggerBot", "Fresh body-ray trigger active; delay={}, cooldown={}, players={}", this.I.getValue(), this.S.getValue(), this.J.getValue());
   }

   @Override
   public String getStatus() {
      return this.N;
   }
}

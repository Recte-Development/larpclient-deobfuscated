package com.larpclient.module;

import com.larpclient._l;
import com.larpclient._m;
import com.larpclient.mt;
import com.larpclient.p1;
import java.util.Optional;
import java.util.Set;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TriggerBotModule extends Module {
   private final d9 z = this.addSetting(new d9("Delay", "Attack delay (ticks)", 0.0, 0.0, 10.0, 1.0));
   private final dh X = this.addSetting(new dh("Players Only", "Only attack players", false));
   private final dh C = this.addSetting(new dh("Smart Delay", "Wait for attack cooldown", true));
   private final _l b = new _l();
   private Entity Z;
   private String q = "Waiting for a target";
   private long E;

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   @Override
   public void onDisable() {
      this.d("Waiting for a target");
      mt.U("TriggerBot", "Released target and queued attack");
   }

   private void d(String var1) {
      this.Z = null;
      this.b.m();
      _m.w(this);
      this.m(var1);
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
         this.d("Paused: input unavailable");
      } else {
         Entity var1 = this.q();
         int var12 = this.Y(var1);
         boolean var6 = false;
         byte var5 = (byte)var12;
         var12 = Integer.signum(var5);
         if ((var12 | -var12) >>> 31 == 0) {
            this.d("Waiting for a target");
         } else {
            String var2 = _m.V(var1, mc.player.entityInteractionRange(), mc.player.getLookAngle());
            if (var2 != null) {
               this.d("Waiting: " + var2);
            } else {
               this.Z = var1;
               if (!this.b.L(var1, mc.level.getGameTime(), this.z.P().intValue())) {
                  this.m("Waiting for delay");
               } else {
                  int var14 = this.C.P();
                  boolean var8 = false;
                  byte var7 = (byte)var14;
                  var14 = Integer.signum(var7);
                  if (((var14 | -var14) >>> 31 ^ 1) == 0) {
                     int var16 = _m.d();
                     boolean var10 = false;
                     byte var9 = (byte)var16;
                     var16 = Integer.signum(var9);
                     if ((var16 | -var16) >>> 31 == 0) {
                        this.m("Waiting for attack cooldown");
                        return;
                     }
                  }

                  this.m("Ready");
                  _m.f(this, var1, mc.player.entityInteractionRange(), this.C.P(), () -> this.I() && this.Z == var1 && this.Y(var1), 1, () -> {
                     this.b.X(var1, mc.level.getGameTime());
                     this.m("Attacked " + var1.getName().getString());
                  }, var1x -> this.m("Waiting: " + var1x));
               }
            }
         }
      }
   }

   private boolean I() {
      return this.isInGame() && _m.z() && mc.isWindowActive() && mc.gui.overlay() == null && !mc.player.isUsingItem() && !mc.options.keyUse.isDown();
   }

   private boolean Y(Entity var1) {
      return var1 instanceof LivingEntity var2
         && var2.isAlive()
         && !var1.isRemoved()
         && var1.level() == mc.level
         && var1 != mc.player
         && (!this.X.P() || var1 instanceof Player);
   }

   private Entity q() {
      Vec3 var1 = mc.player.getEyePosition();
      Vec3 var2 = mc.player.getLookAngle();
      double var3 = mc.player.entityInteractionRange();
      Vec3 var5 = var1.add(var2.scale(var3));
      return P(mc.player, var1, var2, var3, mc.level.getEntities(mc.player, new AABB(var1, var5).inflate(1.0)));
   }

   private static Entity P(Entity var0, Vec3 var1, Vec3 var2, double var3, Iterable<Entity> var5) {
      Entity var6 = null;
      double var7 = var3 * var3 + 1.0E-7;

      for (Entity var10 : var5) {
         if (var10 != var0
            && var10.isPickable()
            && !var10.isSpectator()
            && !var10.isRemoved()
            && var10.getRootVehicle() != var0.getRootVehicle()
            && (!var10.getBoundingBox().contains(var1) || var10.canBePickedFromInside())) {
            Optional var11 = p1.S(var1, var2, var10.getBoundingBox(), var3);
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

   private void m(String var1) {
      if (!this.q.equals(var1)) {
         this.q = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.E >= 1000L) {
            this.E = var2;
            mt.U("TriggerBot", "{}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.d("Waiting for a target");
      mt.U("TriggerBot", "Fresh body-ray trigger active; delay={}, cooldown={}, players={}", this.z.P(), this.C.P(), this.X.P());
   }

   @Override
   public String getStatus() {
      return this.q;
   }
}

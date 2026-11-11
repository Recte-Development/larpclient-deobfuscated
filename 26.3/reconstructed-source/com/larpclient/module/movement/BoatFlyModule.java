package com.larpclient.module.movement;

import com.larpclient.module.exploit.AntiKickMotion;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import net.minecraft.world.phys.Vec3;

public final class BoatFlyModule extends Module {
   private static BoatFlyModule instance;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Horizontal flight speed", 20.0, 2.0, 80.0, 1.0)).setSuffix(" b/s");
   private final NumberSetting verticalSpeed = this.addSetting(
         new NumberSetting("Vertical Speed", "Climb speed while holding Jump and descent speed while holding Sprint", 10.0, 1.0, 40.0, 1.0)
      )
      .setSuffix(" b/s");
   private final NumberSetting fallSpeed = this.addSetting(
         new NumberSetting("Fall Speed", "Slow descent with no vertical input; 0 hovers in place", 0.0, 0.0, 10.0, 0.1)
      )
      .setSuffix(" b/s");
   private final NumberSetting smoothing = this.addSetting(
      new NumberSetting("Smoothing", "Ticks to reach full speed or come to a stop; 0 is instant", 3.0, 0.0, 20.0, 1.0)
   );
   private final BooleanSetting antiKick = this.addSetting(
      new BooleanSetting("Anti Kick", "Dip slightly every 2 seconds in open air so a vanilla server does not kick you for floating", true)
   );
   private AbstractBoat boat;
   private AntiKickMotion motion;
   private double lastY;
   private long flightTicks;
   private int serverCorrections;
   private int correctionCooldown;
   private String status = "Ride a boat";

   public BoatFlyModule() {
      super("Boat Fly", "Fly the boat you steer where you look; Jump climbs, Sprint descends", Category.MOVEMENT, -1);
      instance = this;
   }

   public static BoatFlyModule getInstance() {
      return instance;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.status : "";
   }

   @Override
   public void onEnable() {
      this.reset(null);
      LarpLog.debug(
         "BoatFly",
         "Enabled: speed {} b/s, vertical {} b/s, fall {} b/s, smoothing {}, anti kick {}",
         this.speed.getValue(),
         this.verticalSpeed.getValue(),
         this.fallSpeed.getValue(),
         this.smoothing.getValue(),
         this.antiKick.getValue()
      );
   }

   @Override
   public void onTick() {
      if (this.boat != null) {
         if (!this.isInGame() || minecraft.player.getVehicle() != this.boat || this.boat.getControllingPassenger() != minecraft.player) {
            this.reset("Left the boat");
         }
      }
   }

   @Override
   public void onDisable() {
      this.reset("Disabled");
   }

   public static boolean controlBoat(AbstractBoat var0) {
      if (instance != null && instance.isEnabled()) {
         BoatFlyModule var1 = instance;
         if (var1 != null && var1.isRuntimeActive() && var0 != null) {
            try {
               Minecraft var2 = getMc();
               if (var2.player != null && var2.player.isAlive() && var0.level() == var2.level && var0.getControllingPassenger() == var2.player) {
                  var1.flyBoat(var0);
                  return true;
               } else {
                  return false;
               }
            } catch (RuntimeException var3) {
               LarpLog.error("BoatFly", "Boat control failed; disabling and returning native boat steering", var3);
               var1.disable();
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static void onServerCorrection() {
      BoatFlyModule var0 = instance;
      if (var0 != null && var0.boat != null && var0.motion != null) {
         var0.motion.reset();
         var0.serverCorrections++;
         var0.correctionCooldown = 40;
         var0.status = "Server corrected the boat; try a lower speed";
         if (var0.serverCorrections == 1 || var0.serverCorrections % 20 == 0) {
            LarpLog.warn("BoatFly", "Server corrected the boat position ({} so far, speed {} b/s)", var0.serverCorrections, var0.speed.getValue());
         }
      }
   }

   private void flyBoat(AbstractBoat var1) {
      if (var1 != this.boat) {
         this.reset(this.boat == null ? null : "Changed boats");
         this.boat = var1;
         this.motion = new AntiKickMotion();
         this.lastY = var1.getY();
         LarpLog.debug("BoatFly", "Flying {} at {}", var1.getName().getString(), var1.blockPosition().toShortString());
      }

      this.flightTicks++;
      this.motion.updateAirTicks(var1.getY() - this.lastY, var1.verticalCollisionBelow || !this.getInstance(var1));
      this.lastY = var1.getY();
      Options var2 = minecraft.options;
      int var3 = minecraft.gui.screen() == null && PlayerInputUtil.isInWorld() ? 1 : 0;
      Vec3 var4 = var3 != 0 ? PlayerInputUtil.getInputDirection() : Vec3.ZERO;
      int var5 = var3 == 0 ? 0 : (var2.keyJump.isDown() ? 1 : 0) - (PlayerInputUtil.isKeyDown(var2.keySprint) ? 1 : 0);
      int var6 = this.motion.getDipCount();
      Vec3 var7 = this.motion
         .computeMotion(
            var4,
            this.speed.getValue() / 20.0,
            this.smoothing.getValue().intValue(),
            var5,
            this.verticalSpeed.getValue() / 20.0,
            this.fallSpeed.getValue() / 20.0,
            this.antiKick.getValue()
         );
      if (this.motion.getDipCount() == 1 && var6 == 0) {
         LarpLog.debug("BoatFly", "Anti Kick dip after {} floating ticks; later dips are not logged", 40);
      }

      var1.setYRot(minecraft.player.getYRot());
      var1.setDeltaMovement(var7);
      int var8 = this.motion.getHorizontalVelocity().lengthSqr() > 1.0E-4 ? 1 : 0;
      var1.setPaddleState((boolean)var8, (boolean)var8);
      if (this.correctionCooldown > 0) {
         this.correctionCooldown--;
      } else {
         if (this.motion.isDipping()) {
            this.status = "Anti Kick dip";
         } else if (var8 == 0 && var5 == 0) {
            this.status = this.fallSpeed.getValue() > 0.0 ? "Gliding down" : "Hovering";
         } else {
            this.status = "Flying";
         }
      }
   }

   private boolean getInstance(AbstractBoat var1) {
      return var1.level().getBlockStates(var1.getBoundingBox().inflate(0.0625).expandTowards(0.0, -0.55, 0.0)).allMatch(BlockStateBase::isAir);
   }

   private void reset(String var1) {
      if (this.boat != null && var1 != null) {
         LarpLog.debug(
            "BoatFly",
            "{} after {} ticks: {} anti kick dips, {} server corrections",
            var1,
            this.flightTicks,
            this.motion == null ? 0 : this.motion.getDipCount(),
            this.serverCorrections
         );
      }

      this.boat = null;
      this.motion = null;
      this.flightTicks = 0L;
      this.serverCorrections = 0;
      this.correctionCooldown = 0;
      this.status = "Ride a boat";
   }
}

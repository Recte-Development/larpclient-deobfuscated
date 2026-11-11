package com.larpclient.mixin;

import com.larpclient.module.combat.AimAssistModule;
import com.larpclient.module.combat.AutoCartModule;
import com.larpclient.module.combat.AutoCrystalModule;
import com.larpclient.module.combat.ReachModule;
import com.larpclient.module.combat.WTapModule;
import com.larpclient.module.exploit.FakeLagModule;
import com.larpclient.module.misc.AutoGGModule;
import com.larpclient.module.misc.AutoSellModule;
import com.larpclient.module.misc.TpsTracker;
import com.larpclient.module.movement.BoatFlyModule;
import com.larpclient.module.movement.FlyModule;
import com.larpclient.module.movement.GrimSpeedModule;
import com.larpclient.module.movement.NoFallModule;
import com.larpclient.module.movement.SpeedModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.player.OrderDropperModule;
import com.larpclient.module.render.DamageMarkersModule;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.module.render.KillFXModule;
import com.larpclient.module.world.AutoAreaMiner;
import com.larpclient.render.particle.CometTrailRenderer;
import com.larpclient.util.data.ActivityTracker2;
import com.larpclient.util.entity.CombatState;
import com.larpclient.util.math.KnockbackScaler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Optional;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin_2 {
   @Inject(method = "handleContainerContent(Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)V", at = @At("TAIL"))
   private void Y(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      OrderDropperModule.sendChatMessage(var1.containerId());
   }

   @Inject(method = "handleContainerClose(Lnet/minecraft/network/protocol/game/ClientboundContainerClosePacket;)V", at = @At("RETURN"))
   private void r(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      AutoSellModule.onContainerClosed(var1.getContainerId());
   }

   @Inject(method = "handleSetTime", at = @At("TAIL"))
   private void u(ClientboundSetTimePacket var1, CallbackInfo var2) {
      TpsTracker.onTimeUpdate();
   }

   @Inject(method = "handleDamageEvent", at = @At("TAIL"))
   private void z(ClientboundDamageEventPacket var1, CallbackInfo var2) {
      ActivityTracker2.onDamage(var1);
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 != null && var1.entityId() == var3.getId()) {
         KnockbackScaler.markHit();
      }

      DamageMarkersModule var4 = DamageMarkersModule.getInstance();
      if (var4 != null) {
         var4.onDamageEvent(var1);
      }

      KillFXModule var5 = KillFXModule.getInstance();
      if (var5 != null) {
         var5.onDamageEvent(var1);
      }

      AutoGGModule var6 = AutoGGModule.getInstance();
      if (var6 != null && var6.isEnabled()) {
         var6.onDamageEvent(var1);
      }
   }

   @Inject(method = "handleEntityEvent", at = @At("TAIL"))
   private void D(ClientboundEntityEventPacket var1, CallbackInfo var2) {
      ActivityTracker2.onEntityEvent(var1);
      KillFXModule var3 = KillFXModule.getInstance();
      if (var3 != null) {
         var3.onEntityEvent(var1);
      }

      AutoGGModule var4 = AutoGGModule.getInstance();
      if (var4 != null && var4.isEnabled()) {
         var4.onEntityEvent(var1);
      }

      AutoCrystalModule var5 = AutoCrystalModule.placeCrystal();
      if (var5 != null) {
         var5.onEntityEvent(var1);
      }
   }

   @Inject(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;absSnapTo(DDDFF)V", shift = Shift.AFTER))
   private void R(ClientboundMoveVehiclePacket var1, CallbackInfo var2) {
      BoatFlyModule.onServerCorrection();
   }

   @Inject(method = "handleAnimate", at = @At("TAIL"))
   private void s(ClientboundAnimatePacket var1, CallbackInfo var2) {
      DamageMarkersModule var3 = DamageMarkersModule.getInstance();
      if (var3 != null) {
         var3.onAnimatePacket(var1);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("HEAD"))
   private static void q(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         AutoAreaMiner.getReach(var5);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("RETURN"))
   private static void f(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         CombatState.reset();
         FakeLagModule.onServerCorrection(var5);
         SpeedModule.onPlayerTeleport(var5);
         TargetStrafeModule.onPositionCorrection(var5);
         GrimSpeedModule.onPositionCorrection(var5);
         NoFallModule.onPositionCorrection(var5);
         AimAssistModule.resetFor(var5);
         WTapModule.onPositionCorrection(var5);
         AutoAreaMiner.getCurrentLayer(var5);
         CometTrailRenderer.reset();
         JumpFXModule var6 = JumpFXModule.getInstance();
         if (var6 != null) {
            var6.reset();
         }
      }
   }

   @WrapOperation(
      method = "handleSetEntityMotion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(Lnet/minecraft/world/phys/Vec3;)V")
   )
   private void X(Entity var1, Vec3 var2, Operation<Void> var3) {
      if (var1 == Minecraft.getInstance().player) {
         var2 = KnockbackScaler.applyHitKnockback(var1.getDeltaMovement(), var2);
      }

      var3.call(new Object[]{var1, var2});
   }

   @Redirect(
      method = "handleExplosion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;playerKnockback()Ljava/util/Optional;")
   )
   private Optional<Vec3> o(ClientboundExplodePacket var1) {
      return var1.playerKnockback().map(var0 -> KnockbackScaler.scaleVelocity(var0, true));
   }

   @Inject(method = "handleExplosion", at = @At("RETURN"))
   private void D(ClientboundExplodePacket var1, CallbackInfo var2) {
      AutoCartModule.fail(var1.center(), var1.radius());
   }

   @Redirect(
      method = "handleUpdateAttributes",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V")
   )
   private void Q(AttributeInstance var1, double var2) {
      ReachModule var4 = ReachModule.getInstance();
      if (var4 != null) {
         var4.detachAttribute(var1);
      }

      var1.setBaseValue(var2);
   }

   @Inject(
      method = "handlePlayerAbilities",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", ordinal = 0)
   )
   private void m(ClientboundPlayerAbilitiesPacket var1, CallbackInfo var2) {
      FlyModule var3 = FlyModule.getInstance();
      if (var3 != null) {
         var3.forgetPlayer();
      }

      FreecamModule.setGuidedSpeed();
   }
}

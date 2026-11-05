package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._0;
import com.larpclient.f6;
import com.larpclient.h;
import com.larpclient.xq;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AutoCartModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoSellModule;
import com.larpclient.module.BoatFlyModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.FakeLagModule;
import com.larpclient.module.FlyModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.HalloweenModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.KillFXModule;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.ReachModule;
import com.larpclient.module.SpeedModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import com.larpclient.module.lm;
import java.util.Optional;
import java.util.Set;
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
public class qc387cd65601f713d {
   @Inject(method = "handleContainerContent(Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)V", at = @At("TAIL"))
   private void k(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      OrderDropperModule.x(var1.containerId());
   }

   @Inject(method = "handleContainerClose(Lnet/minecraft/network/protocol/game/ClientboundContainerClosePacket;)V", at = @At("RETURN"))
   private void o(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      AutoSellModule.O(var1.getContainerId());
   }

   @Inject(method = "handleSetTime", at = @At("TAIL"))
   private void Q(ClientboundSetTimePacket var1, CallbackInfo var2) {
      lm.M();
   }

   @Inject(method = "handleDamageEvent", at = @At("TAIL"))
   private void x(ClientboundDamageEventPacket var1, CallbackInfo var2) {
      h.k(var1);
      DamageMarkersModule var3 = DamageMarkersModule.c();
      if (var3 != null) {
         var3.c(var1);
      }

      KillFXModule var4 = KillFXModule.X();
      if (var4 != null) {
         var4.n(var1);
      }

      HalloweenModule var5 = HalloweenModule.U();
      if (var5 != null) {
         var5.C(var1);
      }
   }

   @Inject(method = "handleEntityEvent", at = @At("TAIL"))
   private void J(ClientboundEntityEventPacket var1, CallbackInfo var2) {
      h.u(var1);
      KillFXModule var3 = KillFXModule.X();
      if (var3 != null) {
         var3.e(var1);
      }

      AutoCrystalModule var4 = AutoCrystalModule.T();
      if (var4 != null) {
         var4.w(var1);
      }
   }

   @Inject(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;absSnapTo(DDDFF)V", shift = Shift.AFTER))
   private void x(ClientboundMoveVehiclePacket var1, CallbackInfo var2) {
      BoatFlyModule.E();
   }

   @Inject(method = "handleAnimate", at = @At("TAIL"))
   private void m(ClientboundAnimatePacket var1, CallbackInfo var2) {
      DamageMarkersModule var3 = DamageMarkersModule.c();
      if (var3 != null) {
         var3.c(var1);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("RETURN"))
   private static void e(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         xq.y();
         FakeLagModule.z(var5);
         SpeedModule.P(var5);
         TargetStrafeModule.H(var5);
         GrimSpeedModule.u(var5);
         AimAssistModule.w(var5);
         WTapModule.N(var5);
         _0.q();
         JumpFXModule var6 = JumpFXModule.m();
         if (var6 != null) {
            var6.p();
         }
      }
   }

   @Redirect(
      method = "handleExplosion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;playerKnockback()Ljava/util/Optional;")
   )
   private Optional<Vec3> K(ClientboundExplodePacket var1) {
      return var1.playerKnockback().map(var0 -> f6.z(var0, true));
   }

   @Inject(method = "handleExplosion", at = @At("RETURN"))
   private void D(ClientboundExplodePacket var1, CallbackInfo var2) {
      AutoCartModule.E(var1.center(), var1.radius());
   }

   @Redirect(
      method = "handleUpdateAttributes",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V")
   )
   private void D(AttributeInstance var1, double var2) {
      ReachModule var4 = ReachModule.I();
      if (var4 != null) {
         var4.V(var1);
      }

      var1.setBaseValue(var2);
   }

   @Inject(
      method = "handlePlayerAbilities",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", ordinal = 0)
   )
   private void G(ClientboundPlayerAbilitiesPacket var1, CallbackInfo var2) {
      FlyModule var3 = FlyModule.s();
      if (var3 != null) {
         var3.k();
      }

      FreecamModule.I();
   }
}

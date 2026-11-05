package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.mu;
import com.larpclient.pl;
import com.larpclient.x;
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
import com.larpclient.module.e;
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
public class q570f6c6426db355f {
   @Inject(method = "handleContainerContent(Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)V", at = @At("TAIL"))
   private void q(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      OrderDropperModule.A(var1.containerId());
   }

   @Inject(method = "handleContainerClose(Lnet/minecraft/network/protocol/game/ClientboundContainerClosePacket;)V", at = @At("RETURN"))
   private void L(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      AutoSellModule.T(var1.getContainerId());
   }

   @Inject(method = "handleSetTime", at = @At("TAIL"))
   private void w(ClientboundSetTimePacket var1, CallbackInfo var2) {
      e.w();
   }

   @Inject(method = "handleDamageEvent", at = @At("TAIL"))
   private void u(ClientboundDamageEventPacket var1, CallbackInfo var2) {
      com.larpclient.e.k(var1);
      DamageMarkersModule var3 = DamageMarkersModule.U();
      if (var3 != null) {
         var3.v(var1);
      }

      KillFXModule var4 = KillFXModule.l();
      if (var4 != null) {
         var4.h(var1);
      }

      HalloweenModule var5 = HalloweenModule.X();
      if (var5 != null) {
         var5.S(var1);
      }
   }

   @Inject(method = "handleEntityEvent", at = @At("TAIL"))
   private void E(ClientboundEntityEventPacket var1, CallbackInfo var2) {
      com.larpclient.e.X(var1);
      KillFXModule var3 = KillFXModule.l();
      if (var3 != null) {
         var3.e(var1);
      }

      AutoCrystalModule var4 = AutoCrystalModule.S();
      if (var4 != null) {
         var4.C(var1);
      }
   }

   @Inject(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;absSnapTo(DDDFF)V", shift = Shift.AFTER))
   private void C(ClientboundMoveVehiclePacket var1, CallbackInfo var2) {
      BoatFlyModule.y();
   }

   @Inject(method = "handleAnimate", at = @At("TAIL"))
   private void z(ClientboundAnimatePacket var1, CallbackInfo var2) {
      DamageMarkersModule var3 = DamageMarkersModule.U();
      if (var3 != null) {
         var3.x(var1);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("RETURN"))
   private static void m(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         pl.l();
         FakeLagModule.q(var5);
         SpeedModule.w(var5);
         TargetStrafeModule.W(var5);
         GrimSpeedModule.K(var5);
         AimAssistModule.o(var5);
         WTapModule.F(var5);
         mu.s();
         JumpFXModule var6 = JumpFXModule.m();
         if (var6 != null) {
            var6.o();
         }
      }
   }

   @Redirect(
      method = "handleExplosion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;playerKnockback()Ljava/util/Optional;")
   )
   private Optional<Vec3> u(ClientboundExplodePacket var1) {
      return var1.playerKnockback().map(var0 -> x.l(var0, true));
   }

   @Inject(method = "handleExplosion", at = @At("RETURN"))
   private void y(ClientboundExplodePacket var1, CallbackInfo var2) {
      AutoCartModule.E(var1.center(), var1.radius());
   }

   @Redirect(
      method = "handleUpdateAttributes",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V")
   )
   private void K(AttributeInstance var1, double var2) {
      ReachModule var4 = ReachModule.w();
      if (var4 != null) {
         var4.W(var1);
      }

      var1.setBaseValue(var2);
   }

   @Inject(
      method = "handlePlayerAbilities",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", ordinal = 0)
   )
   private void Z(ClientboundPlayerAbilitiesPacket var1, CallbackInfo var2) {
      FlyModule var3 = FlyModule.d();
      if (var3 != null) {
         var3.i();
      }

      FreecamModule.N();
   }
}

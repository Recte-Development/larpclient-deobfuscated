package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ae;
import com.larpclient.hz;
import com.larpclient.kq;
import com.larpclient.l_;
import com.larpclient.lq;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AutoCartModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoGGModule;
import com.larpclient.module.AutoSellModule;
import com.larpclient.module.BoatFlyModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.FakeLagModule;
import com.larpclient.module.FlyModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.KillFXModule;
import com.larpclient.module.NoFallModule;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.ReachModule;
import com.larpclient.module.SpeedModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import com.larpclient.module.t3;
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
public class q75cecf6eabb0b509 {
   @Inject(method = "handleContainerContent(Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)V", at = @At("TAIL"))
   private void u(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      OrderDropperModule.Q(var1.containerId());
   }

   @Inject(method = "handleContainerClose(Lnet/minecraft/network/protocol/game/ClientboundContainerClosePacket;)V", at = @At("RETURN"))
   private void f(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      AutoSellModule.L(var1.getContainerId());
   }

   @Inject(method = "handleSetTime", at = @At("TAIL"))
   private void y(ClientboundSetTimePacket var1, CallbackInfo var2) {
      t3.S();
   }

   @Inject(method = "handleDamageEvent", at = @At("TAIL"))
   private void C(ClientboundDamageEventPacket var1, CallbackInfo var2) {
      lq.m(var1);
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 != null && var1.entityId() == var3.getId()) {
         hz.a();
      }

      DamageMarkersModule var4 = DamageMarkersModule.t();
      if (var4 != null) {
         var4.d(var1);
      }

      KillFXModule var5 = KillFXModule.t();
      if (var5 != null) {
         var5.K(var1);
      }

      AutoGGModule var6 = AutoGGModule.i();
      if (var6 != null && var6.isEnabled()) {
         var6.k(var1);
      }
   }

   @Inject(method = "handleEntityEvent", at = @At("TAIL"))
   private void V(ClientboundEntityEventPacket var1, CallbackInfo var2) {
      lq.B(var1);
      KillFXModule var3 = KillFXModule.t();
      if (var3 != null) {
         var3.H(var1);
      }

      AutoGGModule var4 = AutoGGModule.i();
      if (var4 != null && var4.isEnabled()) {
         var4.P(var1);
      }

      AutoCrystalModule var5 = AutoCrystalModule.S();
      if (var5 != null) {
         var5.p(var1);
      }
   }

   @Inject(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;absSnapTo(DDDFF)V", shift = Shift.AFTER))
   private void c(ClientboundMoveVehiclePacket var1, CallbackInfo var2) {
      BoatFlyModule.q();
   }

   @Inject(method = "handleAnimate", at = @At("TAIL"))
   private void n(ClientboundAnimatePacket var1, CallbackInfo var2) {
      DamageMarkersModule var3 = DamageMarkersModule.t();
      if (var3 != null) {
         var3.W(var1);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("HEAD"))
   private static void p(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         ae.h(var5);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("RETURN"))
   private static void s(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         kq.Q();
         FakeLagModule.r(var5);
         SpeedModule.C(var5);
         TargetStrafeModule.X(var5);
         GrimSpeedModule.a(var5);
         NoFallModule.M(var5);
         AimAssistModule.k(var5);
         WTapModule.W(var5);
         ae.s(var5);
         l_.f();
         JumpFXModule var6 = JumpFXModule.f();
         if (var6 != null) {
            var6.q();
         }
      }
   }

   @WrapOperation(
      method = "handleSetEntityMotion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(Lnet/minecraft/world/phys/Vec3;)V")
   )
   private void v(Entity var1, Vec3 var2, Operation<Void> var3) {
      if (var1 == Minecraft.getInstance().player) {
         var2 = hz.V(var1.getDeltaMovement(), var2);
      }

      var3.call(new Object[]{var1, var2});
   }

   @Redirect(
      method = "handleExplosion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;playerKnockback()Ljava/util/Optional;")
   )
   private Optional<Vec3> l(ClientboundExplodePacket var1) {
      return var1.playerKnockback().map(var0 -> hz.i(var0, true));
   }

   @Inject(method = "handleExplosion", at = @At("RETURN"))
   private void r(ClientboundExplodePacket var1, CallbackInfo var2) {
      AutoCartModule.c(var1.center(), var1.radius());
   }

   @Redirect(
      method = "handleUpdateAttributes",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V")
   )
   private void F(AttributeInstance var1, double var2) {
      ReachModule var4 = ReachModule.i();
      if (var4 != null) {
         var4.D(var1);
      }

      var1.setBaseValue(var2);
   }

   @Inject(
      method = "handlePlayerAbilities",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", ordinal = 0)
   )
   private void D(ClientboundPlayerAbilitiesPacket var1, CallbackInfo var2) {
      FlyModule var3 = FlyModule.S();
      if (var3 != null) {
         var3.x();
      }

      FreecamModule.f();
   }
}

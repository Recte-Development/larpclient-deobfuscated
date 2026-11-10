package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._6;
import com.larpclient.d6;
import com.larpclient.j_;
import com.larpclient.q1;
import com.larpclient.vc;
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
import com.larpclient.module.y;
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
public class q2f57d747cbabc9ba {
   @Inject(method = "handleContainerContent(Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;)V", at = @At("TAIL"))
   private void Y(ClientboundContainerSetContentPacket var1, CallbackInfo var2) {
      OrderDropperModule.M(var1.containerId());
   }

   @Inject(method = "handleContainerClose(Lnet/minecraft/network/protocol/game/ClientboundContainerClosePacket;)V", at = @At("RETURN"))
   private void r(ClientboundContainerClosePacket var1, CallbackInfo var2) {
      AutoSellModule.F(var1.getContainerId());
   }

   @Inject(method = "handleSetTime", at = @At("TAIL"))
   private void u(ClientboundSetTimePacket var1, CallbackInfo var2) {
      y.z();
   }

   @Inject(method = "handleDamageEvent", at = @At("TAIL"))
   private void z(ClientboundDamageEventPacket var1, CallbackInfo var2) {
      vc.K(var1);
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 != null && var1.entityId() == var3.getId()) {
         j_.K();
      }

      DamageMarkersModule var4 = DamageMarkersModule.D();
      if (var4 != null) {
         var4.Z(var1);
      }

      KillFXModule var5 = KillFXModule.J();
      if (var5 != null) {
         var5.M(var1);
      }

      AutoGGModule var6 = AutoGGModule.J();
      if (var6 != null && var6.isEnabled()) {
         var6.T(var1);
      }
   }

   @Inject(method = "handleEntityEvent", at = @At("TAIL"))
   private void D(ClientboundEntityEventPacket var1, CallbackInfo var2) {
      vc.l(var1);
      KillFXModule var3 = KillFXModule.J();
      if (var3 != null) {
         var3.i(var1);
      }

      AutoGGModule var4 = AutoGGModule.J();
      if (var4 != null && var4.isEnabled()) {
         var4.e(var1);
      }

      AutoCrystalModule var5 = AutoCrystalModule.d();
      if (var5 != null) {
         var5.M(var1);
      }
   }

   @Inject(method = "handleMoveVehicle", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;absSnapTo(DDDFF)V", shift = Shift.AFTER))
   private void R(ClientboundMoveVehiclePacket var1, CallbackInfo var2) {
      BoatFlyModule.y();
   }

   @Inject(method = "handleAnimate", at = @At("TAIL"))
   private void s(ClientboundAnimatePacket var1, CallbackInfo var2) {
      DamageMarkersModule var3 = DamageMarkersModule.D();
      if (var3 != null) {
         var3.Q(var1);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("HEAD"))
   private static void q(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         _6.n(var5);
      }
   }

   @Inject(method = "setValuesFromPositionPacket", at = @At("RETURN"))
   private static void f(PositionMoveRotation var0, Set<Relative> var1, Entity var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof LocalPlayer var5) {
         q1.r();
         FakeLagModule.p(var5);
         SpeedModule.K(var5);
         TargetStrafeModule.m(var5);
         GrimSpeedModule.U(var5);
         NoFallModule.C(var5);
         AimAssistModule.H(var5);
         WTapModule.M(var5);
         _6.g(var5);
         d6.N();
         JumpFXModule var6 = JumpFXModule.f();
         if (var6 != null) {
            var6.x();
         }
      }
   }

   @WrapOperation(
      method = "handleSetEntityMotion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;lerpMotion(Lnet/minecraft/world/phys/Vec3;)V")
   )
   private void X(Entity var1, Vec3 var2, Operation<Void> var3) {
      if (var1 == Minecraft.getInstance().player) {
         var2 = j_.o(var1.getDeltaMovement(), var2);
      }

      var3.call(new Object[]{var1, var2});
   }

   @Redirect(
      method = "handleExplosion",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;playerKnockback()Ljava/util/Optional;")
   )
   private Optional<Vec3> o(ClientboundExplodePacket var1) {
      return var1.playerKnockback().map(var0 -> j_.a(var0, true));
   }

   @Inject(method = "handleExplosion", at = @At("RETURN"))
   private void D(ClientboundExplodePacket var1, CallbackInfo var2) {
      AutoCartModule.h(var1.center(), var1.radius());
   }

   @Redirect(
      method = "handleUpdateAttributes",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;setBaseValue(D)V")
   )
   private void Q(AttributeInstance var1, double var2) {
      ReachModule var4 = ReachModule.N();
      if (var4 != null) {
         var4.z(var1);
      }

      var1.setBaseValue(var2);
   }

   @Inject(
      method = "handlePlayerAbilities",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAbilities()Lnet/minecraft/world/entity/player/Abilities;", ordinal = 0)
   )
   private void m(ClientboundPlayerAbilitiesPacket var1, CallbackInfo var2) {
      FlyModule var3 = FlyModule.Q();
      if (var3 != null) {
         var3.q();
      }

      FreecamModule.D();
   }
}

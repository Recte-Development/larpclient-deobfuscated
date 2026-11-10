package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient.dt;
import com.larpclient.ij;
import com.larpclient.qg;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoSprintModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class q9dd235bdbec8805c {
   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = Shift.AFTER))
   private void S(CallbackInfo var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      var2.input.keyPresses = AutoSprintModule.l(var2, var2.input.keyPresses);
      var2.input.keyPresses = AuraModule.T(var2, var2.input.keyPresses);
      var2.input.keyPresses = TargetStrafeModule.f(var2, var2.input.keyPresses);
      var2.input.keyPresses = GrimSpeedModule.n(var2, var2.input.keyPresses);
      var2.input.keyPresses = WTapModule.W(var2, var2.input.keyPresses);
      var2.input.keyPresses = MaceModule.B(var2, var2.input.keyPresses);
      var2.input.keyPresses = dt.u(var2, var2.input.keyPresses);
      var2.input.keyPresses = ScaffoldModule.z(var2, var2.input.keyPresses);
      if (MaceModule.Z() || AuraModule.M()) {
         var2.input.keyPresses = qg.Y(var2.input.keyPresses);
      }

      AuraModule.H(var2, "input-final");
   }

   @WrapOperation(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"), require = 1, allow = 1)
   private float U(LocalPlayer var1, Operation<Float> var2) {
      float var3 = ij.G(var1.yBob);
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @WrapOperation(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"), require = 1, allow = 1)
   private float C(LocalPlayer var1, Operation<Float> var2) {
      float var3 = ij.M();
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfoReturnable<Boolean> var1) {
      if (WTapModule.h() || MaceModule.Z() || AuraModule.M()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void t(CallbackInfo var1) {
      if (LarpClient.N() != null && LarpClient.N().j() != null) {
         LarpClient.N().j().H();
      }
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void F(CallbackInfo var1) {
      AuraModule.H((LocalPlayer)this, "tick-end");
   }

   @Inject(method = "modifyInput", at = @At("RETURN"), cancellable = true)
   private void x(Vec2 var1, CallbackInfoReturnable<Vec2> var2) {
      var2.setReturnValue(dt.Z((Vec2)var2.getReturnValue()));
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"))
   private void s(CallbackInfo var1) {
      _p.h();
      _p.P();
   }
}

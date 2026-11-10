package com.larp.obf.qa300483f54d804a3;

import com.larpclient.LarpClient;
import com.larpclient.cj;
import com.larpclient.p_;
import com.larpclient.si;
import com.larpclient.ta;
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
public class qbfbb4578bad95c07 {
   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = Shift.AFTER))
   private void o(CallbackInfo var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      var2.input.keyPresses = AutoSprintModule.j(var2, var2.input.keyPresses);
      var2.input.keyPresses = AuraModule.O(var2, var2.input.keyPresses);
      var2.input.keyPresses = TargetStrafeModule.v(var2, var2.input.keyPresses);
      var2.input.keyPresses = GrimSpeedModule.M(var2, var2.input.keyPresses);
      var2.input.keyPresses = WTapModule.X(var2, var2.input.keyPresses);
      var2.input.keyPresses = MaceModule.e(var2, var2.input.keyPresses);
      var2.input.keyPresses = cj.w(var2, var2.input.keyPresses);
      var2.input.keyPresses = ScaffoldModule.x(var2, var2.input.keyPresses);
      if (MaceModule.d() || AuraModule.G()) {
         var2.input.keyPresses = si.X(var2.input.keyPresses);
      }

      AuraModule.M(var2, "input-final");
   }

   @WrapOperation(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"), require = 1, allow = 1)
   private float W(LocalPlayer var1, Operation<Float> var2) {
      float var3 = p_.M(var1.yBob);
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @WrapOperation(method = "applyInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"), require = 1, allow = 1)
   private float r(LocalPlayer var1, Operation<Float> var2) {
      float var3 = p_.f();
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
   private void S(CallbackInfoReturnable<Boolean> var1) {
      if (WTapModule.a() || MaceModule.d() || AuraModule.G()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void Q(CallbackInfo var1) {
      if (LarpClient.R() != null && LarpClient.R().z() != null) {
         LarpClient.R().z().r();
      }
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void E(CallbackInfo var1) {
      AuraModule.M((LocalPlayer)this, "tick-end");
   }

   @Inject(method = "modifyInput", at = @At("RETURN"), cancellable = true)
   private void Q(Vec2 var1, CallbackInfoReturnable<Vec2> var2) {
      var2.setReturnValue(cj.a((Vec2)var2.getReturnValue()));
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"))
   private void Y(CallbackInfo var1) {
      ta.j();
      ta.K();
   }
}

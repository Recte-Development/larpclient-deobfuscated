package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient._m;
import com.larpclient.ks;
import com.larpclient.w7;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoSprintModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class q9d2445b8d12f71a7 {
   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = Shift.AFTER))
   private void k(CallbackInfo var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      var2.input.keyPresses = AutoSprintModule.H(var2, var2.input.keyPresses);
      var2.input.keyPresses = AuraModule.r(var2, var2.input.keyPresses);
      var2.input.keyPresses = TargetStrafeModule.U(var2, var2.input.keyPresses);
      var2.input.keyPresses = GrimSpeedModule.p(var2, var2.input.keyPresses);
      var2.input.keyPresses = WTapModule.g(var2, var2.input.keyPresses);
      var2.input.keyPresses = MaceModule.N(var2, var2.input.keyPresses);
      var2.input.keyPresses = ks.p(var2, var2.input.keyPresses);
      var2.input.keyPresses = ScaffoldModule.J(var2, var2.input.keyPresses);
      if (MaceModule.f() || AuraModule.z()) {
         var2.input.keyPresses = w7.E(var2.input.keyPresses);
      }

      AuraModule.k(var2, "input-final");
   }

   @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
   private void j(CallbackInfoReturnable<Boolean> var1) {
      if (WTapModule.u() || MaceModule.f() || AuraModule.z()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void s(CallbackInfo var1) {
      if (LarpClient.F() != null && LarpClient.F().x() != null) {
         LarpClient.F().x().L();
      }
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void G(CallbackInfo var1) {
      AuraModule.k((LocalPlayer)this, "tick-end");
   }

   @Inject(method = "modifyInput", at = @At("RETURN"), cancellable = true)
   private void o(Vec2 var1, CallbackInfoReturnable<Vec2> var2) {
      var2.setReturnValue(ks.V((Vec2)var2.getReturnValue()));
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"))
   private void W(CallbackInfo var1) {
      _m.j();
      _m.w();
   }
}

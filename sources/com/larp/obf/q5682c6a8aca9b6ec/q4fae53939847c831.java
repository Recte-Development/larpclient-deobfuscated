package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.j5;
import com.larpclient.oo;
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
public class q4fae53939847c831 {
   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V", shift = Shift.AFTER))
   private void H(CallbackInfo var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      var2.input.keyPresses = AutoSprintModule.M(var2, var2.input.keyPresses);
      var2.input.keyPresses = AuraModule.B(var2, var2.input.keyPresses);
      var2.input.keyPresses = TargetStrafeModule.f(var2, var2.input.keyPresses);
      var2.input.keyPresses = GrimSpeedModule.f(var2, var2.input.keyPresses);
      var2.input.keyPresses = WTapModule.A(var2, var2.input.keyPresses);
      var2.input.keyPresses = MaceModule.r(var2, var2.input.keyPresses);
      var2.input.keyPresses = oo.i(var2, var2.input.keyPresses);
      var2.input.keyPresses = ScaffoldModule.D(var2, var2.input.keyPresses);
      if (MaceModule.c() || AuraModule.w()) {
         var2.input.keyPresses = j5.g(var2.input.keyPresses);
      }

      AuraModule.J(var2, "input-final");
   }

   @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
   private void J(CallbackInfoReturnable<Boolean> var1) {
      if (WTapModule.I() || MaceModule.c() || AuraModule.w()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "tick", at = @At("HEAD"))
   private void a(CallbackInfo var1) {
      if (LarpClient.y() != null && LarpClient.y().e() != null) {
         LarpClient.y().e().I();
      }
   }

   @Inject(method = "tick", at = @At("RETURN"))
   private void h(CallbackInfo var1) {
      AuraModule.J((LocalPlayer)this, "tick-end");
   }

   @Inject(method = "modifyInput", at = @At("RETURN"), cancellable = true)
   private void P(Vec2 var1, CallbackInfoReturnable<Vec2> var2) {
      var2.setReturnValue(oo.L((Vec2)var2.getReturnValue()));
   }

   @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V"))
   private void e(CallbackInfo var1) {
      d8.I();
      d8.d();
   }
}

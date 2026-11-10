package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.ff;
import com.larpclient.wp;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoSprintModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_241;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_746.class)
public class qe25b83c5dc495f91 {
   @Inject(method = "method_6007()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_744;method_3129()V", shift = Shift.AFTER))
   private void C(CallbackInfo var1) {
      class_746 var2 = (class_746)this;
      var2.field_3913.field_54155 = AutoSprintModule.f(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = AuraModule.j(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = TargetStrafeModule.P(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = GrimSpeedModule.t(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = WTapModule.B(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = MaceModule.p(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = ff.j(var2, var2.field_3913.field_54155);
      var2.field_3913.field_54155 = ScaffoldModule.F(var2, var2.field_3913.field_54155);
      if (MaceModule.O() || AuraModule.N()) {
         var2.field_3913.field_54155 = bn.I(var2.field_3913.field_54155);
      }

      AuraModule.e(var2, "input-final");
   }

   @WrapOperation(method = "method_66282", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_36454()F"), require = 1, allow = 1)
   private float p(class_746 var1, Operation<Float> var2) {
      float var3 = wp.R(var1.field_3932);
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @WrapOperation(method = "method_66282", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_36455()F"), require = 1, allow = 1)
   private float I(class_746 var1, Operation<Float> var2) {
      float var3 = wp.n();
      return Float.isNaN(var3) ? (Float)var2.call(new Object[]{var1}) : var3;
   }

   @Inject(method = "method_48300", at = @At("HEAD"), cancellable = true)
   private void X(CallbackInfoReturnable<Boolean> var1) {
      if (WTapModule.C() || MaceModule.O() || AuraModule.N()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "method_5773", at = @At("HEAD"))
   private void z(CallbackInfo var1) {
      if (LarpClient.b() != null && LarpClient.b().G() != null) {
         LarpClient.b().G().j();
      }
   }

   @Inject(method = "method_5773", at = @At("RETURN"))
   private void e(CallbackInfo var1) {
      AuraModule.e((class_746)this, "tick-end");
   }

   @Inject(method = "method_67270", at = @At("RETURN"), cancellable = true)
   private void P(class_241 var1, CallbackInfoReturnable<class_241> var2) {
      var2.setReturnValue(ff.B((class_241)var2.getReturnValue()));
   }

   @Inject(method = "method_5773", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_742;method_5773()V"))
   private void E(CallbackInfo var1) {
      cr.Q();
      cr.p();
   }
}

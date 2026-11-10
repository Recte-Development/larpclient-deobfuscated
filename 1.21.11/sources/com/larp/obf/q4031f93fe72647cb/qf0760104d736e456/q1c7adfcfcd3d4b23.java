package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.b1;
import com.larpclient.cz;
import com.larpclient.iy;
import com.larpclient.t0;
import com.larpclient.yp;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Set;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_465.class)
public abstract class q1c7adfcfcd3d4b23 {
   @Shadow
   protected class_1703 field_2797;
   @Shadow
   protected class_1735 field_2787;
   @Shadow
   @Final
   protected Set<class_1735> field_2793;

   @ModifyVariable(method = "method_2385", at = @At("HEAD"), argsOnly = true)
   private class_1735 u(class_1735 var1) {
      return iy.x(this.field_2797, var1);
   }

   @Inject(method = "method_64240", at = @At("RETURN"), cancellable = true)
   private void R(double var1, double var3, CallbackInfoReturnable<class_1735> var5) {
      if (var5.getReturnValue() != null) {
         var5.setReturnValue(iy.i(this.field_2797, (class_1735)var5.getReturnValue(), this.field_2793));
      }
   }

   @WrapOperation(
      method = {"method_2385", "method_71083"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1799;method_46651(I)Lnet/minecraft/class_1799;")
   )
   private class_1799 a(class_1799 var1, int var2, Operation<class_1799> var3) {
      return cz.T().K(var1, (class_1799)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "method_64508", at = @At("TAIL"))
   private void M(class_332 var1, int var2, int var3, CallbackInfo var4) {
      if (this.field_2787 != null) {
         this.field_2787 = iy.x(this.field_2797, this.field_2787);
      }
   }

   @Inject(method = "method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V", at = @At("HEAD"), cancellable = true)
   private void O(class_1735 var1, int var2, int var3, class_1713 var4, CallbackInfo var5) {
      if (iy.m()) {
         var5.cancel();
      } else if (var4 == class_1713.field_7790 && cz.T().h(this.field_2797, var1, var3)) {
         var5.cancel();
      } else if (iy.l(var1)) {
         b1.q(class_2561.method_43470("This slot is reserved or awaiting real items. Free a physical slot or finish the operation first."), true);
         var5.cancel();
      } else if (var4 == class_1713.field_7793 && cz.T().H(this.field_2797.method_34255())) {
         b1.q(class_2561.method_43470("These backing stacks are reserved by placed spawners. Move visible stacks individually."), true);
         var5.cancel();
      } else if (var4 == class_1713.field_7791 && var3 >= 0 && var3 < 9 && iy.n()) {
         iy.i(this.field_2797, var1, var3);
         var5.cancel();
      } else {
         if (var4 == class_1713.field_7794) {
            cz.T().C(var1);
         }

         if (var4 == class_1713.field_7791 && var1 != null && var1.method_7677().method_31574(class_1802.field_8606) && var3 >= 0 && var3 < 9) {
            cz.T().e(iy.E(var3));
         }

         cz.T().q(this.field_2797, var1);
         iy.U(var1);
      }
   }

   @Inject(method = "method_25432", at = @At("HEAD"))
   private void f(CallbackInfo var1) {
      cz.T().W(((class_437)this).method_25440(), this.field_2797);
   }

   @Inject(method = "method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V", at = @At("HEAD"))
   private void D(class_1735 var1, int var2, int var3, class_1713 var4, CallbackInfo var5) {
      if (var1 != null && var4 == class_1713.field_7790 && t0.u(var1.method_7677())) {
         long var6 = t0.k();
         if (var6 > 0L) {
            yp.K(var6);
         }
      }
   }
}

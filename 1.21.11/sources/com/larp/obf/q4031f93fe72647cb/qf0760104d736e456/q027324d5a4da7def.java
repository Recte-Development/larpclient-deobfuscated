package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import com.larpclient.fn;
import com.larpclient.h_;
import com.larpclient.jn;
import com.larpclient.wu;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1657;
import net.minecraft.class_1713;
import net.minecraft.class_1750;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_636.class)
public abstract class q027324d5a4da7def {
   @Unique
   private int b;

   @Inject(method = "method_2909", at = @At("HEAD"), cancellable = true)
   private void F(class_1799 var1, int var2, CallbackInfo var3) {
      if (jn.Z(var1)) {
         var3.cancel();
      }
   }

   @Inject(method = "method_2915", at = @At("HEAD"), cancellable = true)
   private void S(class_1799 var1, CallbackInfo var2) {
      if (jn.Z(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "method_2919", at = @At("HEAD"), cancellable = true)
   private void t(class_1657 var1, class_1268 var2, CallbackInfoReturnable<class_1269> var3) {
      if (var1 == class_310.method_1551().field_1724) {
         cz var4 = cz.T();
         if (var4.W(var2 == class_1268.field_5808 ? var1.method_31548().method_67532() : 40) || wu.e()) {
            var3.setReturnValue(class_1269.field_5811);
         }
      }
   }

   @WrapMethod(method = "method_2906")
   private void s(int var1, int var2, int var3, class_1713 var4, class_1657 var5, Operation<Void> var6) {
      if (var1 >= 0 && var5 == class_310.method_1551().field_1724) {
         if (cz.T().J(var1, var2, var3, var4)) {
            cz.T().u(() -> var6.call(new Object[]{var1, var2, var3, var4, var5}));
         }
      } else {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      }
   }

   @Inject(method = "method_2896", at = @At("HEAD"))
   private void x(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      this.b = var1.method_5998(var2).method_7947();
   }

   @Inject(method = "method_2910", at = @At("HEAD"), cancellable = true)
   private void a(class_2338 var1, class_2350 var2, CallbackInfoReturnable<Boolean> var3) {
      cz var4 = cz.T();
      if (!var4.Y(var1) && (!var4.z(var1) || !var4.C(var1))) {
         if (var4.J() && var4.e(var1)) {
            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "method_2902", at = @At("HEAD"), cancellable = true)
   private void q(class_2338 var1, class_2350 var2, CallbackInfoReturnable<Boolean> var3) {
      cz var4 = cz.T();
      boolean var5 = var4.z(var1);
      if (!var4.Y(var1) && (!var5 || !var4.C(var1))) {
         if (var4.J() && var4.j(var1, var2)) {
            if (var5 && !var4.z(var1)) {
               class_310.method_1551().field_1769.method_18145(var1.method_10263() >> 4, var1.method_10264() >> 4, var1.method_10260() >> 4);
            }

            var3.setReturnValue(true);
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "method_2896", at = @At("HEAD"), cancellable = true)
   private void z(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      cz var5 = cz.T();
      if (var5.J() && var1 != null && var3 != null) {
         if (wu.e() || var5.W(var2 == class_1268.field_5808 ? var1.method_31548().method_67532() : 40)) {
            var4.setReturnValue(class_1269.field_5811);
            return;
         }

         class_2338 var6 = var3.method_17777().method_10062();
         int var7 = var2 == class_1268.field_5808 ? var1.method_31548().method_67532() : 40;
         if (var5.z(var6)) {
            if (var5.C(var6)) {
               var4.setReturnValue(class_1269.field_5812);
               return;
            }

            if (!var5.T(var6, var1.method_5998(var2), var7, var1.method_5715() || class_310.method_1551().field_1690.field_1832.method_1434())) {
               class_310.method_1551().execute(() -> var5.S(var6));
            } else if (var1.method_5715() || class_310.method_1551().field_1690.field_1832.method_1434()) {
               var5.l(var6.method_10093(var3.method_17780()));
            }

            var4.setReturnValue(class_1269.field_5812);
            return;
         }

         if (!var5.X(var1.method_5998(var2)) && var5.V(var1.method_5998(var2).method_7909()) != null) {
            class_1750 var9 = new class_1750(var1, var2, var1.method_5998(var2), var3);
            if (var9.method_7716()) {
               var5.E(var9.method_8037().method_10062());
            }

            return;
         }

         if (var5.X(var1.method_5998(var2))) {
            class_1750 var8 = new class_1750(var1, var2, var1.method_5998(var2), var3);
            if (var8.method_7716()) {
               var5.Q(
                  var8.method_8037().method_10062(),
                  var1.method_5998(var2),
                  var7,
                  var1.method_5715() || class_310.method_1551().field_1690.field_1832.method_1434()
               );
            }

            return;
         }
      }
   }

   @Inject(method = "method_2896", at = @At("RETURN"))
   private void M(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      cz var5 = cz.T();
      if (((class_1269)var4.getReturnValue()).method_23665()) {
         var5.Q();
      }

      boolean var6 = var5.X(var1.method_5998(var2));
      if (var6
         && ((class_1269)var4.getReturnValue()).method_23665()
         && this.b > 1
         && var1.method_5998(var2).method_7947() == this.b - 1
         && var5.X(var1.method_5998(var2))) {
         var5.R(var2);
      }
   }

   @Inject(method = "method_2899", at = @At("RETURN"))
   private void L(class_2338 var1, CallbackInfoReturnable<Boolean> var2) {
      cz var3 = cz.T();
      if (var3.J() && (Boolean)var2.getReturnValue()) {
         var3.m(var1);
      }
   }

   @WrapOperation(
      method = {"method_2910", "method_2902"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_638;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;")
   )
   private class_2680 v(class_638 var1, class_2338 var2, Operation<class_2680> var3) {
      return cz.T().Z(var1, var2, (class_2680)var3.call(new Object[]{var1, var2}));
   }

   @WrapOperation(
      method = "method_2899",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1937;method_8320(Lnet/minecraft/class_2338;)Lnet/minecraft/class_2680;")
   )
   private class_2680 D(class_1937 var1, class_2338 var2, Operation<class_2680> var3) {
      return cz.T().Z(var1, var2, (class_2680)var3.call(new Object[]{var1, var2}));
   }

   @Inject(method = "method_2906", at = @At("HEAD"), cancellable = true)
   private void s(int var1, int var2, int var3, class_1713 var4, class_1657 var5, CallbackInfo var6) {
      if (class_310.method_1551().field_1755 instanceof h_ || class_310.method_1551().field_1755 instanceof fn) {
         var6.cancel();
      } else if (var1 < 0 && var5 != null) {
         var5.field_7512.method_7593(var2, var3, var4, var5);
         var6.cancel();
      }
   }
}

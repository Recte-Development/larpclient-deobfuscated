package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.f4;
import com.larpclient.i;
import com.larpclient.il;
import com.larpclient.ju;
import com.larpclient.q7;
import com.larpclient.qc;
import com.larpclient.qr;
import com.larpclient.t3;
import com.larpclient.tw;
import com.larpclient.ux;
import com.larpclient.yb;
import com.larpclient.yu;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1531;
import net.minecraft.class_1533;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_2246;
import net.minecraft.class_2275;
import net.minecraft.class_2868;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_636.class)
public class qd2cf7094dc850e59 {
   @WrapOperation(method = "method_2906", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1703;method_37421()I"))
   private int w(class_1703 var1, Operation<Integer> var2) {
      return tw.s().w(var1, ju.b().q(var1, q7.B().clickRevision(var1)));
   }

   @Inject(method = "method_2906", at = @At("HEAD"), cancellable = true)
   private void u(int var1, int var2, int var3, class_1713 var4, class_1657 var5, CallbackInfo var6) {
      if (!tw.s().g()) {
         if (il.U(var1, var2, var3, var4)) {
            var6.cancel();
         } else {
            il.s();
            if (i.J(class_310.method_1551(), var2, var3, var4)) {
               var6.cancel();
            } else if (ju.b().S()) {
               var6.cancel();
            } else if (tw.s().x()) {
               var6.cancel();
            } else if (!(q7.L() instanceof qr) && q7.L().blocksUserInteraction()) {
               var6.cancel();
            } else if (!q7.L().isSwapActive()) {
               class_310 var7 = class_310.method_1551();
               if (var7 != null && var7.field_1724 != null && var7.field_1724.field_7512 != null) {
                  if (var7.field_1724.field_7512.field_7763 == var1) {
                     if (f4.L().g(var7, var1, var2, var3, var4)
                        || t3.c().T(var7, var1, var2, var3, var4)
                        || ux.o().L(var7, var1, var2, var3, var4)
                        || yb.b().e(var7, var1, var2, var3, var4)) {
                        var6.cancel();
                     }
                  }
               }
            }
         }
      }
   }

   @Inject(method = "method_2906", at = @At("TAIL"))
   private void d(int var1, int var2, int var3, class_1713 var4, class_1657 var5, CallbackInfo var6) {
      il.s();
   }

   @ModifyVariable(method = "method_2917", at = @At("HEAD"), argsOnly = true)
   private class_3966 d(class_3966 var1) {
      return var1.method_17782() instanceof class_1531 var2 ? f4.Y.F(var2, var1) : var1;
   }

   @Inject(method = "method_2917", at = @At("HEAD"), cancellable = true)
   private void Y(class_1657 var1, class_1297 var2, class_3966 var3, class_1268 var4, CallbackInfoReturnable<class_1269> var5) {
      il.w();
      if (var4 == class_1268.field_5808 && !il.p()) {
         var5.setReturnValue(class_1269.field_5814);
      }
   }

   @Inject(method = "method_2905", at = @At("HEAD"), cancellable = true)
   private void v(class_1657 var1, class_1297 var2, class_1268 var3, CallbackInfoReturnable<class_1269> var4) {
      il.w();
      if (var3 == class_1268.field_5808 && !il.p()) {
         var4.setReturnValue(class_1269.field_5814);
      } else if (var2 instanceof class_1533 var5
         && var5.method_6940().method_7960()
         && var3 == class_1268.field_5808
         && !i.R(class_310.method_1551(), var3)
         && i.R(class_310.method_1551(), class_1268.field_5810)) {
         var4.setReturnValue(class_1269.field_5811);
      } else if (var2 instanceof class_1533 var6 && i.r(class_310.method_1551(), var6, var3)) {
         var4.setReturnValue(class_1269.field_5812);
      } else if (var2 instanceof class_1533 && q7.L().s(var1.method_6047())) {
         var4.setReturnValue(class_1269.field_5814);
      }
   }

   @Inject(method = "method_2905", at = @At("RETURN"))
   private void k(class_1657 var1, class_1297 var2, class_1268 var3, CallbackInfoReturnable<class_1269> var4) {
      if (var2 instanceof class_1533 var5 && var3 == class_1268.field_5808 && ((class_1269)var4.getReturnValue()).method_23665()) {
         q7.L().signalFramePlacement(var5);
      }
   }

   @Inject(method = "method_2918", at = @At("HEAD"), cancellable = true)
   private void L(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      if (var2 instanceof class_1533 var4 && i.B(class_310.method_1551(), var4)) {
         var3.cancel();
      } else {
         if (var2 instanceof class_1533 && q7.L().s(var1.method_6047())) {
            var3.cancel();
         }
      }
   }

   @Inject(method = "method_2896", at = @At("HEAD"), cancellable = true)
   private void Q(class_746 var1, class_1268 var2, class_3965 var3, CallbackInfoReturnable<class_1269> var4) {
      if (i.R(class_310.method_1551(), var2)) {
         var4.setReturnValue(class_1269.field_5814);
      } else if (!q7.L().r()) {
         tw.s().D(var3.method_17777());
         if (var1.method_73183().method_8320(var3.method_17777()).method_27852(class_2246.field_10363)) {
            tw.s().l(var3.method_17777());
         }

         if (ux.o().c(var1.method_5998(var2)) && var1.method_73183().method_8320(var3.method_17777()).method_26204() instanceof class_2275) {
            var4.setReturnValue(class_1269.field_5814);
         } else if (ju.b().X(class_310.method_1551(), var2, var3)) {
            var4.setReturnValue(class_1269.field_5812);
         } else if ((!q7.L().blocksUserInteraction() || q7.L().allowsRecoveryOpen(var3.method_17777()))
            && qr.h().g(var3.method_17777())
            && qc.i().Z(var3.method_17777())) {
            if (var2 == class_1268.field_5808) {
               class_310 var5 = class_310.method_1551();
               yu var6 = yu.o();
               if (var6.o(var5, var3.method_17777()) || var6.K(var5, var3.method_17777())) {
                  var4.setReturnValue(class_1269.field_5812);
               }
            }
         } else {
            var4.setReturnValue(class_1269.field_5814);
         }
      }
   }

   @Inject(method = "method_2919", at = @At("HEAD"), cancellable = true)
   private void K(class_1657 var1, class_1268 var2, CallbackInfoReturnable<class_1269> var3) {
      if (i.R(class_310.method_1551(), var2)) {
         var3.setReturnValue(class_1269.field_5814);
      } else if (var2 == class_1268.field_5808 && !il.p()) {
         var3.setReturnValue(class_1269.field_5814);
      } else if (q7.L().blocksUserInteraction() || ux.o().c(var1.method_5998(var2))) {
         var3.setReturnValue(class_1269.field_5814);
      } else if (var2 == class_1268.field_5808) {
         bk var4 = bk.W();
         if (!var4.l() && var4.g()) {
            class_310 var5 = class_310.method_1551();
            int var6 = var4.n();
            if (var6 >= 0 && var6 <= 8) {
               int var7 = var1.method_31548().method_67532();
               if (var7 != var6) {
                  var4.i();

                  try {
                     var5.method_1562().method_52787(new class_2868(var6));
                     class_1269 var8 = var5.field_1761.method_2919(var1, var2);
                     var5.method_1562().method_52787(new class_2868(var7));
                     var3.setReturnValue(var8);
                  } finally {
                     var4.R();
                  }
               }
            }
         }
      }
   }
}

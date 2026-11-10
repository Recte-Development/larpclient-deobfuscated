package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.hu;
import com.larpclient.il;
import com.larpclient.ju;
import com.larpclient.q7;
import com.larpclient.qr;
import com.larpclient.t3;
import com.larpclient.tw;
import com.larpclient.yu;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2626;
import net.minecraft.class_2637;
import net.minecraft.class_2645;
import net.minecraft.class_2649;
import net.minecraft.class_2653;
import net.minecraft.class_2673;
import net.minecraft.class_2767;
import net.minecraft.class_2775;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_634;
import net.minecraft.class_6880;
import net.minecraft.class_9209;
import net.minecraft.class_9334;
import net.minecraft.class_9834;
import net.minecraft.class_9835;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_634.class)
public abstract class qd83e4308433096ef {
   private static final double X = 256.0;

   @Inject(method = "method_11098", at = @At("TAIL"))
   private void O(class_2673 var1, CallbackInfo var2) {
      if (var1.method_11532() == 1000) {
         tw.s().x(var1.method_11531());
      }
   }

   @Inject(method = "method_11136", at = @At("TAIL"))
   private void e(class_2626 var1, CallbackInfo var2) {
      ju.b().H(var1.method_11309(), var1.method_11308());
      tw.s().d(var1.method_11309(), var1.method_11308());
      hu.f().E(class_310.method_1551(), var1.method_11309(), var1.method_11308());
   }

   @Inject(method = "method_11100", at = @At("TAIL"))
   private void q(class_2637 var1, CallbackInfo var2) {
      class_310 var3 = class_310.method_1551();
      hu var4 = hu.f();
      var1.method_30621((var2x, var3x) -> {
         ju.b().H(var2x, var3x);
         tw.s().d(var2x, var3x);
         var4.E(var3, var2x, var3x);
      });
   }

   @Inject(
      method = "method_11102",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_2600;method_11074(Lnet/minecraft/class_2596;Lnet/minecraft/class_2547;Lnet/minecraft/class_11980;)V",
         shift = Shift.AFTER
      )
   )
   private void h(class_2645 var1, CallbackInfo var2) {
      q7.B().L(var1.method_36148());
   }

   @Inject(method = "method_11153", at = @At("TAIL"))
   private void E(class_2649 var1, CallbackInfo var2) {
      if (var1 != null) {
         q7.B().onInventorySynchronized(var1.comp_3837(), var1.comp_3839(), var1.comp_3840());
         hu.f().S(var1.comp_3837());
         il.z(var1.comp_3837());
         ju.b().k(var1.comp_3837(), var1.comp_3839(), var1.comp_3840());
         tw.s().s(var1.comp_3837(), var1.comp_3839(), var1.comp_3840());
      }
   }

   @Inject(method = "method_61187", at = @At("TAIL"))
   private void q(class_9834 var1, CallbackInfo var2) {
      q7.B().onServerSlot(-1, -1, var1.comp_2890());
   }

   @Inject(method = "method_61188", at = @At("TAIL"))
   private void X(class_9835 var1, CallbackInfo var2) {
      ju.b().b(var1.comp_2891(), var1.comp_2892());
      tw.s().t(var1.comp_2891(), var1.comp_2892());
      boolean var3 = !(q7.B() instanceof qr) && q7.B().isSwapJuggling();
      q7.B().onPlayerInventorySlot(var1.comp_2891(), var1.comp_2892());
      q7 var4 = q7.L();
      class_310 var5 = class_310.method_1551();
      if (!var3 && !(var4 instanceof qr) && !var4.isSwapActive() && var5.field_1724 != null) {
         class_9209 var6 = (class_9209)var1.comp_2892().method_58694(class_9334.field_49646);
         if (var6 != null && var1.comp_2892().method_31574(class_1802.field_8204)) {
            for (class_1735 var8 : var5.field_1724.field_7498.field_7761) {
               if (var8.field_7871 == var5.field_1724.method_31548() && var8.method_34266() == var1.comp_2891()) {
                  var4.onDealtCardSlotUpdate(0, var8.field_7874, var6.comp_2315());
                  break;
               }
            }
         }
      }
   }

   @Inject(method = "method_11109", at = @At("TAIL"))
   private void i(class_2653 var1, CallbackInfo var2) {
      ju.b().d(var1.method_11452(), var1.method_11450(), var1.method_11449());
      tw.s().K(var1.method_11452(), var1.method_11450(), var1.method_11449());
      if (var1 != null) {
         boolean var3 = !(q7.B() instanceof qr) && q7.B().isSwapJuggling();
         q7.B().onServerSlot(var1.method_11452(), var1.method_11450(), var1.method_11449());
         il.g(var1.method_11452(), var1.method_11450());
         hu var4 = hu.f();
         var4.V(var1.method_11452(), var1.method_11450());
         yu var5 = yu.o();
         if (var4.f() == hu.q.Y && var4.z() == var1.method_11452()) {
            int var6 = var1.method_11450();
            if (var6 >= 0 && var6 <= 44) {
               var5.r(var4.W(), var6, var1.method_11449());
            }
         } else if (var1.method_11452() == 0) {
            var5.G(var1.method_11450(), var1.method_11449());
         }

         int var12 = var1.method_11452();
         int var7 = var1.method_11450();
         q7 var8 = q7.L();
         if (!var3 && (var8 instanceof qr || !var8.isSwapJuggling())) {
            boolean var9 = var12 == 0 || var12 == var8.getSwapSyncId() && var7 >= (var8 instanceof qr ? 36 : 9);
            if (var9) {
               class_1799 var10 = var1.method_11449();
               if (var10 != null && !var10.method_7960() && var10.method_31574(class_1802.field_8204)) {
                  class_9209 var11 = (class_9209)var10.method_57353().method_58694(class_9334.field_49646);
                  if (var11 != null) {
                     var8.onDealtCardSlotUpdate(var12, var7, var11.comp_2315());
                  }
               }
            }
         }
      }
   }

   @WrapOperation(method = "method_11109", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1799;method_7912(I)V"), require = 0)
   private void Z(class_1799 var1, int var2, Operation<Void> var3) {
      if (q7.L().isDealBobAllowed(var1)) {
         var3.call(new Object[]{var1, var2});
      }
   }

   @Inject(method = "method_11150", at = @At("TAIL"))
   private void p(class_2775 var1, CallbackInfo var2) {
      class_310 var3 = class_310.method_1551();
      if (var1 != null && var3 != null && var3.field_1724 != null) {
         t3.c().n(var3, var1.method_11915(), var1.method_11912(), var1.method_11913());
      }
   }

   @Inject(method = "method_11146", at = @At("HEAD"), cancellable = true)
   private void N(class_2767 var1, CallbackInfo var2) {
      if (var1 != null) {
         class_6880 var3 = var1.method_11894();
         if (var3 != null) {
            class_3414 var4 = (class_3414)var3.comp_349();
            if (var4 == class_3417.field_14585 || var4 == class_3417.field_14770) {
               class_310 var5 = class_310.method_1551();
               if (var5 != null && var5.field_1687 != null) {
                  yu var6 = yu.o();
                  if (var6.C && !"off".equals(var6.B())) {
                     class_2338 var7 = hu.f().F(hu.q.Y);
                     if (var7 != null) {
                        double var8 = var1.method_11890() - (var7.method_10263() + 0.5);
                        double var10 = var1.method_11889() - (var7.method_10264() + 0.5);
                        double var12 = var1.method_11893() - (var7.method_10260() + 0.5);
                        if (var8 * var8 + var10 * var10 + var12 * var12 <= 256.0) {
                           var2.cancel();
                        }
                     }
                  }
               }
            }
         }
      }
   }
}

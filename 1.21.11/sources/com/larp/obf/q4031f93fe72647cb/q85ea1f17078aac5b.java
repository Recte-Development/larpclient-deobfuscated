package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.i7;
import com.larpclient.wp;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.b;
import com.larpclient.module.g;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(class_1309.class)
public class q85ea1f17078aac5b {
   @ModifyArg(
      method = "method_26318(Lnet/minecraft/class_243;F)Lnet/minecraft/class_243;",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1309;method_5784(Lnet/minecraft/class_1313;Lnet/minecraft/class_243;)V"),
      index = 1
   )
   private class_243 A(class_243 var1) {
      return this instanceof class_746 var2 ? TargetStrafeModule.N(var2, var1) : var1;
   }

   @Inject(method = "method_6007()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1309;method_6070()V"))
   private void w(CallbackInfo var1) {
      if (this instanceof class_746 var2) {
         GrimSpeedModule.c(var2);
      }
   }

   @ModifyVariable(method = "method_6031(F)V", at = @At("HEAD"), argsOnly = true)
   private float p(float var1) {
      if (this != class_310.method_1551().field_1724) {
         return var1;
      }

      ScaffoldModule var2 = ScaffoldModule.y();
      if (var2 != null) {
         var1 = var2.V(var1);
      }

      g var3 = g.K();
      if (var3 != null) {
         var1 = var3.B(var1);
      }

      b var4 = b.e();
      if (var4 != null) {
         var1 = var4.S(var1);
      }

      AutoAnchorModule var5 = AutoAnchorModule.T();
      if (var5 != null) {
         var1 = var5.a(var1);
      }

      AutoCrystalModule var6 = AutoCrystalModule.l();
      return var6 == null ? var1 : var6.R(var1);
   }

   @Inject(method = "method_6043()V", at = @At("HEAD"))
   private void d(CallbackInfo var1) {
      if (this == class_310.method_1551().field_1724) {
         AuraModule.e((class_746)this, "jump-before");
         JumpFXModule var2 = JumpFXModule.p();
         if (var2 != null) {
            var2.i();
         }
      }
   }

   @Inject(method = "method_6043()V", at = @At("RETURN"))
   private void S(CallbackInfo var1) {
      if (this == class_310.method_1551().field_1724) {
         AuraModule.e((class_746)this, "jump-after");
      }
   }

   @Redirect(method = "method_6043()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1309;method_36454()F"))
   private float H(class_1309 var1) {
      if (var1 != class_310.method_1551().field_1724) {
         return var1.method_36454();
      } else {
         float var2 = ScaffoldModule.f();
         if (!Float.isNaN(var2)) {
            return var2;
         } else {
            return wp.o() == wp.z.e ? wp.o() : var1.method_36454();
         }
      }
   }

   @ModifyArgs(method = "method_6005(DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1309;method_18800(DDD)V"))
   private void J(Args var1, double var2, double var4, double var6) {
      if (this == class_310.method_1551().field_1724) {
         class_243 var8 = ((class_1309)this).method_18798();
         class_243 var9 = new class_243((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
         class_243 var10 = var8.method_1019(i7.D(var9.method_1020(var8), false));
         var1.set(0, var10.field_1352);
         var1.set(1, var10.field_1351);
         var1.set(2, var10.field_1350);
      }
   }
}

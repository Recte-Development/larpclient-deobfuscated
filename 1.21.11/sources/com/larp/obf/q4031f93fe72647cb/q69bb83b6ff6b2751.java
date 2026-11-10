package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.f8;
import com.larpclient.wi;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.SkyShaderModule;
import net.minecraft.class_12076;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_638;
import net.minecraft.class_9975;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_9975.class)
public class q69bb83b6ff6b2751 {
   @Unique
   private final f8 B = new f8();

   @Inject(method = "method_62302", at = @At("HEAD"), cancellable = true)
   private void D(CallbackInfo var1) {
      if (this.k()) {
         var1.cancel();
      }
   }

   @Inject(method = "method_62312", at = @At("HEAD"), cancellable = true)
   private void v(CallbackInfo var1) {
      if (this.k()) {
         var1.cancel();
      }
   }

   @Unique
   private boolean k() {
      class_276 var1 = class_310.method_1551().method_1522();
      class_276 var2 = wi.A(var1);
      boolean var3 = var2 != var1;
      if (CustomSkyModule.h()) {
         return this.B.J(var2, CustomSkyModule.M()) && !var3;
      }

      SkyShaderModule var4 = SkyShaderModule.y();
      return var4 != null && var4.g() && var4.g(var2) && !var3;
   }

   @Unique
   private static boolean x() {
      SkyShaderModule var0 = SkyShaderModule.y();
      return var0 != null && var0.O();
   }

   @Inject(method = "method_62307", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfo var1) {
      if (!wi.s()) {
         if (x()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "method_74926", at = @At("RETURN"))
   private void K(class_638 var1, float var2, class_4184 var3, class_12076 var4, CallbackInfo var5) {
      if (!wi.s()) {
         if (CustomSkyModule.h()) {
            var4.field_63094 = CustomSkyModule.M().H() ? 0.0F : CustomSkyModule.M().M() * var4.field_63093;
         }
      }
   }

   @Inject(method = "method_62303", at = @At("HEAD"), cancellable = true)
   private void R(CallbackInfo var1) {
      if (!wi.s()) {
         if (CustomSkyModule.h() && !CustomSkyModule.M().o()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "method_62304", at = @At("HEAD"), cancellable = true)
   private void j(CallbackInfo var1) {
      if (!wi.s()) {
         if (CustomSkyModule.h() && !CustomSkyModule.M().T()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "method_62305", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfo var1) {
      if (!wi.s()) {
         if (CustomSkyModule.h() || x()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "method_62306", at = @At("HEAD"), cancellable = true)
   private void p(CallbackInfo var1) {
      if (!wi.s()) {
         if (CustomSkyModule.h() || x()) {
            var1.cancel();
         }
      }
   }

   @Inject(method = "close", at = @At("HEAD"))
   private void d(CallbackInfo var1) {
      this.B.close();
   }
}

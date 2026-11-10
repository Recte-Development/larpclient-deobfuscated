package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.jr;
import com.larpclient.qx;
import com.larpclient.tk;
import com.larpclient.wi;
import com.larpclient.module.WorldMenuModule;
import net.minecraft.class_11246;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_310.class)
public abstract class q1c22855490f8905a {
   @Inject(method = "method_1507", at = @At("HEAD"))
   private void p(class_437 var1, CallbackInfo var2) {
      class_437 var3 = ((class_310)this).field_1755;
      if (var3 != var1) {
         jr var4 = qx.P(var3);
         if (var4 != null) {
            var4.w();
         }
      }
   }

   @Inject(method = "method_1507", at = @At("HEAD"), cancellable = true)
   private void G(class_437 var1, CallbackInfo var2) {
      if (var1 instanceof tk var3 && WorldMenuModule.a(var3)) {
         var2.cancel();
         class_310 var4 = (class_310)this;
         if (var4.field_1755 != null) {
            var4.method_1507(null);
         }
      }
   }

   @Mixin(class_757.class)
   public abstract static class qacc5a8f3d913d476 {
      @Shadow
      @Final
      private class_310 field_4015;
      @Shadow
      @Final
      private class_11246 field_59966;

      @Inject(method = "method_3192", at = @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/class_11246;method_70926()V"))
      private void B(class_9779 var1, boolean var2, CallbackInfo var3) {
         WorldMenuModule.a(new class_332(this.field_4015, wi.p(this.field_59966), -10000, -10000), var1.method_60637(false));
      }

      @Inject(
         method = "method_3192",
         at = @At(value = "INVOKE", target = "Lnet/minecraft/class_11228;method_70890(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V")
      )
      private void f(class_9779 var1, boolean var2, CallbackInfo var3) {
         if (tk.n()) {
            tk.h(new class_332(this.field_4015, wi.p(this.field_59966), -10000, -10000), var1.method_60637(false));
         }
      }
   }
}

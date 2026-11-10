package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.la;
import com.larpclient.module.MotionScannerModule;
import com.larpclient.module.PrimeChunkFinderModule;
import net.minecraft.class_2672;
import net.minecraft.class_2676;
import net.minecraft.class_634;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_634.class)
public abstract class q44748d1eeb0a6f69 {
   @Shadow
   private class_638 field_3699;

   @Inject(method = "method_11128", at = @At("TAIL"))
   private void K(class_2672 var1, CallbackInfo var2) {
      PrimeChunkFinderModule.k(this.field_3699, var1.method_11523(), var1.method_11524(), var1.method_38599(), true);
      la.m(this.field_3699, var1.method_11523(), var1.method_11524(), var1.method_38599());
      MotionScannerModule.H(this.field_3699, var1.method_11523(), var1.method_11524(), var1.method_38599());
   }

   @Inject(method = "method_11143", at = @At("TAIL"))
   private void v(class_2676 var1, CallbackInfo var2) {
      PrimeChunkFinderModule.k(this.field_3699, var1.method_11558(), var1.method_11554(), var1.method_38600(), false);
      la.m(this.field_3699, var1.method_11558(), var1.method_11554(), var1.method_38600());
      MotionScannerModule.H(this.field_3699, var1.method_11558(), var1.method_11554(), var1.method_38600());
   }
}

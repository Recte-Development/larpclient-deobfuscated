package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.cp;
import com.larpclient.k;
import com.larpclient.q8;
import com.larpclient.s2;
import com.larpclient.tr;
import java.util.List;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_408;
import net.minecraft.class_437;
import net.minecraft.class_4717;
import net.minecraft.class_12225.class_12226;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_408.class)
public abstract class qafb60861b04db25c extends class_437 {
   @Shadow
   protected class_342 field_2382;
   @Shadow
   private class_4717 field_21616;
   @Unique
   private final cp F = new cp();
   @Unique
   private final tr r = new tr();
   @Unique
   private Object m;

   protected qafb60861b04db25c(class_2561 var1) {
      super(var1);
   }

   @Unique
   private boolean T() {
      return this.getClass() == class_408.class && this.field_22787.field_1724 != null && this.field_22787.field_1687 != null;
   }

   @Unique
   private boolean w(double var1, double var3) {
      if (!(var3 >= this.field_22790 - 14) && !this.field_2382.method_25405(var1, var3)) {
         if (this.field_21616.field_21612 != null && this.field_21616.field_21612.field_2771.method_3318((int)var1, (int)var3)) {
            return true;
         }

         class_12226 var5 = new class_12226(this.method_64506(), (int)var1, (int)var3).method_76756(true);
         this.field_22787.field_1705.method_1743().method_75803(var5, this.field_22790, this.field_22787.field_1705.method_1738(), true);
         return var5.method_75777() != null;
      } else {
         return true;
      }
   }

   @Inject(method = "method_25426", at = @At("HEAD"))
   private void Y(CallbackInfo var1) {
      this.F.P(this.field_22787.method_1569(), this.m == null || this.m == this.field_22787.field_1687);
      this.F.u();
      this.r.q();
   }

   @Inject(method = "method_25426", at = @At("TAIL"))
   private void H(CallbackInfo var1) {
      this.m = this.field_22787.field_1687;
      if (this.T()) {
         q8.v("HUD editor", "Chat mouse editing ready");
      }
   }

   @Inject(method = "method_25402", at = @At("HEAD"), cancellable = true)
   private void v(class_11909 var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      this.F.P(this.field_22787.method_1569(), this.m == this.field_22787.field_1687);
      if (!this.T() || !this.F.O() && !this.r.y()) {
         if (this.T() && this.w(var1.comp_4798(), var1.comp_4799())) {
            this.F.o();
            this.r.K();
         }
      } else {
         var3.setReturnValue(true);
      }
   }

   @Inject(method = "method_25402", at = @At("RETURN"), cancellable = true)
   private void F(class_11909 var1, boolean var2, CallbackInfoReturnable<Boolean> var3) {
      if (!(Boolean)var3.getReturnValue() && this.T() && this.field_22787.method_1569()) {
         List var4 = k.y();
         boolean var5 = this.w(var1.comp_4798(), var1.comp_4799());
         if (!var5
            && !this.F.B(var1.comp_4798(), var1.comp_4799(), this.field_22789, this.field_22790, var4)
            && this.r.l(var1.comp_4798(), var1.comp_4799(), s2.m(var1))) {
            this.F.H(null);
            var3.setReturnValue(true);
         } else {
            this.r.K();
            if (this.F.F(var1.comp_4798(), var1.comp_4799(), s2.m(var1), this.field_22789, this.field_22790, var4, var5)) {
               var3.setReturnValue(true);
            }
         }
      }
   }

   public boolean method_25403(class_11909 var1, double var2, double var4) {
      this.F.P(this.field_22787.method_1569(), this.m == this.field_22787.field_1687);
      if (this.T() && this.r.d(var1.comp_4798(), var1.comp_4799(), s2.m(var1))) {
         return true;
      } else {
         return this.T() && this.F.f(var1.comp_4798(), var1.comp_4799(), s2.m(var1), this.field_22789, this.field_22790, this.field_22787.method_74189())
            ? true
            : super.method_25403(var1, var2, var4);
      }
   }

   public boolean method_25406(class_11909 var1) {
      this.F.P(this.field_22787.method_1569(), this.m == this.field_22787.field_1687);
      if (this.T() && this.r.j(s2.m(var1))) {
         return true;
      } else {
         return this.T() && this.F.u(s2.m(var1)) ? true : super.method_25406(var1);
      }
   }

   @Inject(method = "method_25401", at = @At("HEAD"), cancellable = true)
   private void o(double var1, double var3, double var5, double var7, CallbackInfoReturnable<Boolean> var9) {
      if (this.T() && this.field_22787.method_1569()) {
         boolean var10 = this.w(var1, var3);
         if (this.F.y(var1, var3, var7, var10) || !var10 && !this.F.B(var1, var3, this.field_22789, this.field_22790, k.y()) && this.r.k(var1, var3, var7)) {
            var9.setReturnValue(true);
         }
      }
   }

   @Inject(method = "method_25404", at = @At("HEAD"), cancellable = true)
   private void w(class_11908 var1, CallbackInfoReturnable<Boolean> var2) {
      if (this.T() && this.field_22787.method_1569() && this.F.D(s2.i(var1))) {
         var2.setReturnValue(true);
      }
   }

   public boolean method_25400(class_11905 var1) {
      return this.T() && this.field_22787.method_1569() && this.F.f(var1.comp_4793()) ? true : super.method_25400(var1);
   }

   public void method_25393() {
      super.method_25393();
      this.F.P(this.field_22787.method_1569(), this.m == this.field_22787.field_1687);
      this.r.x(this.field_22787.method_1569());
   }

   @Inject(method = "method_25394", at = @At("HEAD"))
   private void p(class_332 var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.F.P(this.field_22787.method_1569(), this.m == this.field_22787.field_1687);
      if (this.T() && this.field_22787.method_1569()) {
         var1.method_71048();
         boolean var6 = this.w(var2, var3);
         this.r.k(var1, var2, var3, var6 || this.F.B(var2, var3, this.field_22789, this.field_22790, k.y()));
         this.F.e(var1, var2, var3, this.field_22789, this.field_22790, var6);
         var1.method_71048();
      }
   }

   @Inject(method = "method_25432", at = @At("HEAD"))
   private void d(CallbackInfo var1) {
      this.F.P(true, true);
      this.F.u();
      this.r.q();
   }
}

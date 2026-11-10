package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bw;
import com.larpclient.gv;
import com.larpclient.ik;
import com.larpclient.w6;
import com.larpclient.we;
import com.larpclient.wp;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.ViewModelModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_1007;
import net.minecraft.class_10444;
import net.minecraft.class_11659;
import net.minecraft.class_12122;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_591;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_759;
import net.minecraft.class_811;
import net.minecraft.class_9334;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_759.class)
public class q827838f2b23fcd34 {
   @Unique
   private float O;
   @Unique
   private float w;
   @Unique
   private class_1268 z = class_1268.field_5808;

   @Inject(method = "method_3233", at = @At("HEAD"))
   private void x(class_1309 var1, class_1799 var2, class_811 var3, class_4587 var4, class_11659 var5, int var6, CallbackInfo var7) {
      var4.method_22903();
      if (var3.method_29998() && var1 == class_310.method_1551().field_1724) {
         ViewModelModule var8 = ViewModelModule.B();
         if (var8 != null && var8.isRuntimeActive()) {
            var8.R(var4, this.z, var3 == class_811.field_4321);
         }

         AnimationsModule var9 = AnimationsModule.W();
         if (var9 != null && !var2.method_7960() && !var2.method_57826(class_9334.field_49646)) {
            var9.g(var4);
         }
      }
   }

   @Inject(method = "method_3233", at = @At("RETURN"))
   private void T(class_1309 var1, class_1799 var2, class_811 var3, class_4587 var4, class_11659 var5, int var6, CallbackInfo var7) {
      var4.method_22909();
   }

   @Redirect(
      method = "method_3233",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_10444;method_65604(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;III)V")
   )
   private void p(
      class_10444 var1,
      class_4587 var2,
      class_11659 var3,
      int var4,
      int var5,
      int var6,
      class_1309 var7,
      class_1799 var8,
      class_811 var9,
      class_4587 var10,
      class_11659 var11,
      int var12
   ) {
      class_11659 var13 = var9.method_29998() && var7 == class_310.method_1551().field_1724 ? gv.m(var3) : var3;
      if (var9.method_29998() && var7 == class_310.method_1551().field_1724) {
         bw.o(this.z, var9 == class_811.field_4321, var2, var1);
      }

      if (var9.method_29998() && var7 == class_310.method_1551().field_1724) {
         we.J(var1, var2, var13, var3, var8, var9, this.z, this.O, this.w, var4, var5, var6);
      } else {
         var1.method_65604(var2, var13, var4, var5, var6);
      }
   }

   @Shadow
   private void method_65816(float var1, class_4587 var2, int var3, class_1306 var4) {
      throw new AssertionError("Mixin shadow");
   }

   @Shadow
   private void method_3224(class_4587 var1, class_1306 var2, float var3) {
      throw new AssertionError("Mixin shadow");
   }

   @Redirect(
      method = "method_3228",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_759;method_65816(FLnet/minecraft/class_4587;ILnet/minecraft/class_1306;)V", ordinal = 2),
      require = 1,
      allow = 1
   )
   private void A(class_759 var1, float var2, class_4587 var3, int var4, class_1306 var5) {
      AnimationsModule var6 = AnimationsModule.W();
      if (var6 != null && var6.a(var5)) {
         var6.b(var3, var5, var2);
      } else {
         this.method_65816(var2, var3, var4, var5);
      }
   }

   @Redirect(
      method = "method_3228",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_759;method_3224(Lnet/minecraft/class_4587;Lnet/minecraft/class_1306;F)V", ordinal = 4),
      require = 1,
      allow = 1
   )
   private void r(
      class_759 var1,
      class_4587 var2,
      class_1306 var3,
      float var4,
      class_742 var5,
      float var6,
      float var7,
      class_1268 var8,
      float var9,
      class_1799 var10,
      float var11,
      class_4587 var12,
      class_11659 var13,
      int var14
   ) {
      AnimationsModule var15 = AnimationsModule.W();
      boolean var16 = var15 != null && var15.a(var3) && w6.t(var10).comp_4975() == class_12122.field_63399;
      if (!var16) {
         this.method_3224(var2, var3, var4);
      }
   }

   @Inject(method = "method_3228", at = @At("HEAD"), cancellable = true)
   private void g(
      class_742 var1,
      float var2,
      float var3,
      class_1268 var4,
      float var5,
      class_1799 var6,
      float var7,
      class_4587 var8,
      class_11659 var9,
      int var10,
      CallbackInfo var11
   ) {
      this.z = var4;
      this.O = var5;
      this.w = var7;
      if (ik.C(var1, var4, var6, var5, var7, var2, var8, var9, var10)) {
         var11.cancel();
      } else {
         var8.method_22903();
         ViewModelModule var12 = ViewModelModule.B();
         if (var12 != null && var12.isRuntimeActive()) {
            boolean var13 = var4 == class_1268.field_5808 ? var1.method_6068() == class_1306.field_6182 : var1.method_6068() != class_1306.field_6182;
            var12.K(var8, var4, var13);
         }
      }
   }

   @Inject(method = "method_22976", at = @At("HEAD"))
   private void W(float var1, class_4587 var2, class_11659 var3, class_746 var4, int var5, CallbackInfo var6) {
      bw.y();
   }

   @WrapOperation(method = "method_22976", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_5705(F)F"), require = 1, allow = 1)
   private float w(class_746 var1, float var2, Operation<Float> var3) {
      float var4 = wp.R(class_3532.method_16439(var2, var1.field_3931, var1.field_3932));
      return Float.isNaN(var4) ? (Float)var3.call(new Object[]{var1, var2}) : var4;
   }

   @WrapOperation(
      method = "method_22976",
      at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_5695(F)F"),
            @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_61414(F)F")
      },
      require = 2,
      allow = 2
   )
   private float D(class_746 var1, float var2, Operation<Float> var3) {
      float var4 = wp.n();
      return Float.isNaN(var4) ? (Float)var3.call(new Object[]{var1, var2}) : var4;
   }

   @WrapOperation(
      method = "method_3219",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1007;method_4220(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Z)V"
      ),
      require = 1,
      allow = 1
   )
   private void W(class_1007 var1, class_4587 var2, class_11659 var3, int var4, class_2960 var5, boolean var6, Operation<Void> var7) {
      t(var2, false);
      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      bw.f(class_1268.field_5808, false, var2, ((class_591)var1.method_4038()).field_3401);
   }

   @WrapOperation(
      method = "method_3219",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_1007;method_4221(Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;ILnet/minecraft/class_2960;Z)V"
      ),
      require = 1,
      allow = 1
   )
   private void F(class_1007 var1, class_4587 var2, class_11659 var3, int var4, class_2960 var5, boolean var6, Operation<Void> var7) {
      t(var2, true);
      var7.call(new Object[]{var1, var2, var3, var4, var5, var6});
      bw.f(class_1268.field_5808, true, var2, ((class_591)var1.method_4038()).field_27433);
   }

   @Unique
   private static void t(class_4587 var0, boolean var1) {
      ViewModelModule var2 = ViewModelModule.B();
      if (var2 != null && var2.isRuntimeActive()) {
         var2.n(var0, class_1268.field_5808, var1);
      }
   }

   @Inject(method = "method_3228", at = @At("RETURN"))
   private void i(
      class_742 var1,
      float var2,
      float var3,
      class_1268 var4,
      float var5,
      class_1799 var6,
      float var7,
      class_4587 var8,
      class_11659 var9,
      int var10,
      CallbackInfo var11
   ) {
      var8.method_22909();
   }
}

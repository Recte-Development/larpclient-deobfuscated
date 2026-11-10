package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.ix;
import com.larpclient.lb;
import com.larpclient.q3;
import com.larpclient.qx;
import com.larpclient.module.GUIAnimationModule;
import net.minecraft.class_11909;
import net.minecraft.class_310;
import net.minecraft.class_312;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(class_312.class)
public abstract class qc9511e836f99c012 {
   @Unique
   private static float v() {
      class_437 var0 = class_310.method_1551().field_1755;
      return var0 instanceof class_465 && var0 instanceof q3 var1 ? var1.larpContainerScale() : 1.0F;
   }

   @Unique
   private static double[] K(double var0, double var2) {
      class_437 var4 = class_310.method_1551().field_1755;
      if (var4 == null) {
         return new double[]{var0, var2};
      }

      lb.m var5 = lb.X(var4);
      if (var5 != null) {
         double[] var6 = var5.inverse(var0, var2);
         var0 = var6[0];
         var2 = var6[1];
      }

      ix.g var10 = qx.A(var4);
      var0 = var10.localX(var0, var4.field_22789 / 2.0);
      var2 = var10.localY(var2, var4.field_22790 / 2.0);
      GUIAnimationModule var7 = GUIAnimationModule.Z();
      return var7 == null ? new double[]{var0, var2} : new double[]{var7.Q(var0, var4.field_22789 / 2.0, v()), var7.Q(var2, var4.field_22790 / 2.0, v())};
   }

   @Unique
   private static class_11909 N(class_11909 var0) {
      double[] var1 = K(var0.comp_4798(), var0.comp_4799());
      return new class_11909(var1[0], var1[1], var0.comp_4800());
   }

   @ModifyArgs(method = "method_1601", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25402(Lnet/minecraft/class_11909;Z)Z"))
   private void X(Args var1) {
      var1.set(0, N((class_11909)var1.get(0)));
   }

   @ModifyArgs(method = "method_1601", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25406(Lnet/minecraft/class_11909;)Z"))
   private void A(Args var1) {
      var1.set(0, N((class_11909)var1.get(0)));
   }

   @ModifyArgs(method = "method_55793", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25403(Lnet/minecraft/class_11909;DD)Z"))
   private void M(Args var1) {
      class_11909 var2 = (class_11909)var1.get(0);
      double var3 = (Double)var1.get(1);
      double var5 = (Double)var1.get(2);
      double[] var7 = K(var2.comp_4798() - var3, var2.comp_4799() - var5);
      double[] var8 = K(var2.comp_4798(), var2.comp_4799());
      var1.set(0, N(var2));
      var1.set(1, var8[0] - var7[0]);
      var1.set(2, var8[1] - var7[1]);
   }

   @ModifyArgs(method = "method_55793", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_16014(DD)V"))
   private void T(Args var1) {
      double[] var2 = K((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }

   @ModifyArgs(method = "method_1598", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_437;method_25401(DDDD)Z"))
   private void Q(Args var1) {
      double[] var2 = K((Double)var1.get(0), (Double)var1.get(1));
      var1.set(0, var2[0]);
      var1.set(1, var2[1]);
   }
}

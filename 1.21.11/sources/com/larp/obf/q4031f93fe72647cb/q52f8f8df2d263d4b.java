package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import com.larpclient.module.Feature3DUIModule;
import com.larpclient.module.FreelookModule;
import com.larpclient.module.GUIAnimationModule;
import com.larpclient.module.RemovalsModule;
import com.larpclient.module.SmoothCameraModule;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_5134;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_4184.class)
public abstract class q52f8f8df2d263d4b {
   @Unique
   private float u = 4.0F;
   @Shadow
   private float field_18721;
   @Shadow
   private float field_18722;

   @Shadow
   protected abstract void method_19325(float var1, float var2);

   @Shadow
   protected abstract void method_19324(float var1, float var2, float var3);

   @Invoker("method_19318")
   protected abstract float invokeGetMaxZoom(float var1);

   @Shadow
   protected abstract void method_19327(double var1, double var3, double var5);

   @Inject(
      method = "method_19321",
      at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/class_4184;method_19327(DDD)V", shift = Shift.AFTER),
            @At(value = "INVOKE", target = "Lnet/minecraft/class_4184;method_19322(Lnet/minecraft/class_243;)V", shift = Shift.AFTER)
      }
   )
   private void f(CallbackInfo var1) {
      if (!wp.Y()) {
         SmoothCameraModule var2 = SmoothCameraModule.L();
         if (var2 != null) {
            class_4184 var3 = (class_4184)this;
            class_243 var4 = var2.h(var3, var3.method_71156());
            this.method_19327(var4.field_1352, var4.field_1351, var4.field_1350);
         }
      }
   }

   @ModifyArg(method = "method_19321", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4184;method_19318(F)F"), index = 0)
   private float s(float var1) {
      this.u = var1;
      GUIAnimationModule var2 = GUIAnimationModule.Z();
      return var2 != null && var2.N() ? var1 * var2.T(true) : var1;
   }

   @Inject(method = "method_19321", at = @At("TAIL"))
   private void Y(CallbackInfo var1) {
      class_310 var2 = class_310.method_1551();
      GUIAnimationModule var3 = GUIAnimationModule.Z();
      if (var3 != null && var3.N() && var2.field_1690.method_31044().method_31034() && var2.field_1724 != null && !var2.field_1724.method_6113()) {
         float var4 = var3.T(false);
         if (var4 > 0.001F) {
            this.method_19324(-this.invokeGetMaxZoom(this.u * var4), 0.0F, 0.0F);
         }
      }
   }

   @Inject(method = "method_19321", at = @At("TAIL"))
   private void f(class_1937 var1, class_1297 var2, boolean var3, boolean var4, float var5, CallbackInfo var6) {
      class_1297 var7 = ((class_4184)this).method_19331();
      if (wp.d()) {
         class_243 var17 = wp.W(((class_4184)this).method_55437());
         this.method_19325(wp.g(), wp.J());
         this.method_19327(var17.field_1352, var17.field_1351, var17.field_1350);
      } else if (wp.Y() && var7 != null) {
         class_310 var16 = class_310.method_1551();
         float var18 = wp.g();
         float var19 = wp.J();
         this.method_19325(var18, var19);
         class_243 var20 = var7.method_30950(var5).method_1031(0.0, var7.method_5751(), 0.0);
         SmoothCameraModule var21 = SmoothCameraModule.L();
         if (var21 != null) {
            var20 = var21.h((class_4184)this, var20);
         }

         this.method_19327(var20.field_1352, var20.field_1351, var20.field_1350);
         if (!var16.field_1690.method_31044().method_31034()) {
            float var22 = 4.0F;
            float var14 = 1.0F;
            if (var7 instanceof class_1309 var15) {
               var22 = (float)var15.method_45325(class_5134.field_59674);
               var14 = var15.method_55693();
            }

            this.method_19324(-this.invokeGetMaxZoom(var22 * var14), 0.0F, 0.0F);
         }
      } else if (FreelookModule.l() && var7 != null) {
         float var8 = FreelookModule.O();
         float var9 = FreelookModule.G();
         float var10 = FreelookModule.f();
         float var11 = ((class_4184)this).method_55437();
         class_243 var12 = var7.method_30950(var11);
         double var13 = var12.field_1351 + var7.method_5751();
         this.method_19325(var8, var9);
         this.method_19327(var12.field_1352, var13, var12.field_1350);
         this.method_19324(-var10, 0.0F, 0.0F);
      }
   }

   @Inject(method = "method_19321", at = @At("HEAD"))
   private void y(CallbackInfo var1) {
      Feature3DUIModule.h();
   }

   @Inject(method = "method_19321", at = @At("TAIL"))
   private void q(CallbackInfo var1) {
      Feature3DUIModule var2 = Feature3DUIModule.f();
      if (var2 != null) {
         class_4184 var3 = (class_4184)this;
         float var4 = var3.method_55437();
         Feature3DUIModule.k var5 = var2.T(
            var3.method_71156(),
            var3.method_19330(),
            var3.method_19329(),
            var3.method_19331(),
            var4,
            class_3532.method_16439(var4, this.field_18722, this.field_18721)
         );
         if (var5 != null) {
            this.method_19325(var5.yaw(), var5.pitch());
            this.method_19327(var5.x(), var5.y(), var5.z());
         }
      }
   }

   @Inject(method = "method_19318", at = @At("HEAD"), cancellable = true)
   private void P(float var1, CallbackInfoReturnable<Float> var2) {
      if (RemovalsModule.o("clip")) {
         var2.setReturnValue(var1);
      }
   }
}

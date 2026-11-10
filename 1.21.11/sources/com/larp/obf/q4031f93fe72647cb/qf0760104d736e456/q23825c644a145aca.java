package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import com.larpclient.iv;
import com.larpclient.jn;
import com.larpclient.s3;
import com.larpclient.ts;
import com.larpclient.u2;
import com.larpclient.wa;
import com.larpclient.wu;
import com.larpclient.yp;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_2765;
import net.minecraft.class_2767;
import net.minecraft.class_2775;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_3944;
import net.minecraft.class_5894;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_7439;
import net.minecraft.class_746;
import net.minecraft.class_7827;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_634.class)
public abstract class q23825c644a145aca {
   @Unique
   private final Map<Integer, class_1799> m = new HashMap<>();

   @WrapOperation(
      method = "method_11150",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_638;method_8486(DDDLnet/minecraft/class_3414;Lnet/minecraft/class_3419;FFZ)V")
   )
   private void Z(
      class_638 var1,
      double var2,
      double var4,
      double var6,
      class_3414 var8,
      class_3419 var9,
      float var10,
      float var11,
      boolean var12,
      Operation<Void> var13,
      class_2775 var14
   ) {
      class_746 var15 = class_310.method_1551().field_1724;
      boolean var16 = var15 != null
         && var14.method_11912() == var15.method_5628()
         && var1.method_8469(var14.method_11915()) instanceof class_1542 var17
         && cz.T().Q(var17);
      if (!var16) {
         var13.call(new Object[]{var1, var2, var4, var6, var8, var9, var10, var11, var12});
      }
   }

   @Inject(method = "method_11150", at = @At("HEAD"))
   private void Y(class_2775 var1, CallbackInfo var2) {
      class_310 var3 = class_310.method_1551();
      if (var3.method_18854() && var3.field_1724 != null && var3.field_1687 != null && var1.method_11912() == var3.field_1724.method_5628()) {
         if (var3.field_1687.method_8469(var1.method_11915()) instanceof class_1542 var4) {
            u2.X(var4);
            if (jn.N(var4.method_6983())) {
               ts.j(var4.method_6983(), var1.method_11913());
            }
         }
      }
   }

   @Inject(method = {"method_11153", "method_11109", "method_61188", "method_61187"}, at = @At("HEAD"))
   private void z(CallbackInfo var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.method_18854()) {
         this.m.clear();
         if (var2.field_1724 != null) {
            for (int var3 = 0; var3 < var2.field_1724.method_31548().method_5439(); var3++) {
               class_1799 var4 = var2.field_1724.method_31548().method_5438(var3);
               if (wa.x(var4)) {
                  this.m.put(var3, var4.method_7972());
               }
            }

            class_1799 var5 = var2.field_1724.field_7512.method_34255();
            if (wa.x(var5)) {
               this.m.put(-1, var5.method_7972());
            }
         }
      }
   }

   @Unique
   private void A() {
      class_310 var1 = class_310.method_1551();
      if (var1.method_18854() && var1.field_1724 != null) {
         for (Entry var3 : this.m.entrySet()) {
            class_1799 var4 = var3.getKey() == -1
               ? var1.field_1724.field_7512.method_34255()
               : var1.field_1724.method_31548().method_5438((Integer)var3.getKey());
            if (class_1799.method_31577(var4, (class_1799)var3.getValue()) && var4.method_7947() <= ((class_1799)var3.getValue()).method_7947()) {
               wa.C(var4, true);
            }
         }

         this.m.clear();
      }
   }

   @Inject(method = {"method_11153", "method_11109", "method_61188", "method_61187"}, at = @At("RETURN"))
   private void a(CallbackInfo var1) {
      this.A();
      cz.T().D();
      wu.n();
      cz.T().P();
      iv.c();
      ts.v();
   }

   @Inject(method = "method_43596", at = @At("HEAD"), cancellable = true)
   private void E(class_7439 var1, CallbackInfo var2) {
      if (cz.T().J()) {
         s3.T().W(var1.comp_763());
      }

      if (iv.c(var1.comp_763(), var1.comp_906()) || yp.p(var1.comp_763(), var1.comp_906())) {
         var2.cancel();
      }
   }

   @Inject(method = "method_45724", at = @At("HEAD"), cancellable = true)
   private void U(class_7827 var1, CallbackInfo var2) {
      if (cz.T().J()) {
         s3.T().W(var1.comp_1097());
      }

      if (iv.c(var1.comp_1097(), false) || yp.p(var1.comp_1097(), false)) {
         var2.cancel();
      }
   }

   @Inject(method = "method_34076", at = @At("HEAD"), cancellable = true)
   private void G(class_5894 var1, CallbackInfo var2) {
      if (iv.c(var1.comp_2279(), true) || yp.p(var1.comp_2279(), true)) {
         var2.cancel();
      }
   }

   @Inject(method = "method_17587", at = @At("HEAD"), cancellable = true)
   private void E(class_3944 var1, CallbackInfo var2) {
      if (iv.z(var1)) {
         var2.cancel();
      }
   }

   @Inject(method = "method_11146", at = @At("HEAD"), cancellable = true)
   private void E(class_2767 var1, CallbackInfo var2) {
      class_746 var3 = class_310.method_1551().field_1724;
      if (iv.G() && var3 != null && var3.method_5649(var1.method_11890(), var1.method_11889(), var1.method_11893()) <= 16.0) {
         var2.cancel();
      }
   }

   @Inject(method = "method_11125", at = @At("HEAD"), cancellable = true)
   private void K(class_2765 var1, CallbackInfo var2) {
      class_746 var3 = class_310.method_1551().field_1724;
      if (iv.G() && var3 != null && var1.method_11883() == var3.method_5628()) {
         var2.cancel();
      }
   }
}

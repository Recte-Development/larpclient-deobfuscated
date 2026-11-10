package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.f4;
import com.larpclient.il;
import com.larpclient.ju;
import com.larpclient.q;
import com.larpclient.q7;
import com.larpclient.t3;
import com.larpclient.tw;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.lang.reflect.Method;
import net.minecraft.class_1304;
import net.minecraft.class_1324;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_5134;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_329.class)
public class qd55912c0338dc118 {
   @Shadow
   private class_1799 field_2031;
   private class_1799 f;
   private static final Method z = n();

   @WrapOperation(method = {"method_1736", "method_1759"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_7261(F)F"))
   private float k(class_746 var1, float var2, Operation<Float> var3) {
      return q7.L().isSwapFreezing() ? 1.0F : (Float)var3.call(new Object[]{var1, var2});
   }

   @WrapOperation(method = "method_58478", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_6096()I"))
   private static int K(class_1657 var0, Operation<Integer> var1) {
      class_310 var2 = class_310.method_1551();
      if (var0 != var2.field_1724) {
         return (Integer)var1.call(new Object[]{var0});
      }

      class_1799[] var3 = f4.Y.p(var2);
      if (var3 == null) {
         return (Integer)var1.call(new Object[]{var0});
      }

      class_6880 var4 = class_5134.field_23724;
      class_1324 var5 = var0.method_5996(var4);
      if (var5 == null) {
         return (Integer)var1.call(new Object[]{var0});
      }

      class_1324 var6 = new class_1324(var4, var0x -> {});
      var6.method_26831(var5);
      class_1304[] var7 = new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166};

      for (class_1304 var11 : var7) {
         var0.method_6118(var11).method_57354(var11, (var2x, var3x) -> {
            if (var2x.equals(var4)) {
               var6.method_6200(var3x.comp_2447());
            }
         });
      }

      for (int var12 = 0; var12 < var7.length; var12++) {
         var3[var12 + 1].method_57354(var7[var12], (var2x, var3x) -> {
            if (var2x.equals(var4)) {
               var6.method_55696(var3x);
            }
         });
      }

      return class_3532.method_15357(var6.method_6194());
   }

   @WrapOperation(method = "method_1748()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_7391()Lnet/minecraft/class_1799;"))
   private class_1799 a(class_1661 var1, Operation<class_1799> var2) {
      return q7.L().projectSlot(il.I(), (class_1799)var2.call(new Object[]{var1}));
   }

   @ModifyVariable(method = "method_1762", at = @At("HEAD"), argsOnly = true)
   private class_1799 Q(class_1799 var1, class_332 var2, int var3, int var4, class_9779 var5, class_1657 var6, class_1799 var7, int var8) {
      if (!var1.method_7960() && ju.b().q(var1).method_7960()) {
         return class_1799.field_8037;
      }

      if (!var1.method_7960() && tw.s().k(var1).method_7960()) {
         return class_1799.field_8037;
      }

      q var9 = q.U();
      if (var9 == null) {
         return var1;
      }

      int var10 = var8 >= 1 && var8 <= 9 ? var8 - 1 : -1;
      if (var6 == class_310.method_1551().field_1724 && var10 >= 0) {
         var10 = K(var10);
      }

      if (var6 == class_310.method_1551().field_1724) {
         class_1799 var11 = q7.L().getInventorySlotProjection(var10);
         if (var11 != null) {
            return var11;
         }
      }

      class_1799 var12 = var9.g(var1);
      var12 = t3.c().Z(var10, var12);
      return bk.W().c(var10, var12);
   }

   private static Method n() {
      try {
         return Class.forName("com.verz.fakeelytra.client.VisualInventory", false, class_310.class.getClassLoader()).getMethod("actual", int.class);
      } catch (ReflectiveOperationException | LinkageError var1) {
         return null;
      }
   }

   private static int K(int var0) {
      if (z != null) {
         try {
            return (Integer)z.invoke(null, var0);
         } catch (ReflectiveOperationException | LinkageError var2) {
         }
      }

      return var0;
   }

   @WrapOperation(method = "method_1762", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1799;method_7965()I"), require = 1)
   private int f(class_1799 var1, Operation<Integer> var2) {
      return q7.L().isHotbarBobAllowed(var1) ? (Integer)var2.call(new Object[]{var1}) : 0;
   }

   @WrapOperation(method = "method_1759", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1661;method_67532()I"))
   private int k(class_1661 var1, Operation<Integer> var2) {
      return bk.W().E() ? bk.W().i() : il.I();
   }

   @Inject(method = "method_1749", at = @At("HEAD"))
   private void W(class_332 var1, CallbackInfo var2) {
      q var3 = q.U();
      if (var3 != null && this.field_2031 != null) {
         this.f = this.field_2031;
         this.field_2031 = var3.g(this.field_2031);
         class_310 var4 = class_310.method_1551();
         if (var4.field_1724 != null) {
            int var5 = il.I();
            class_1799 var6 = q7.L().getInventorySlotProjection(var5);
            if (var6 != null) {
               this.field_2031 = var6;
               return;
            }

            this.field_2031 = f4.L().U(this.field_2031);
            this.field_2031 = t3.c().Z(var5, this.field_2031);
            this.field_2031 = bk.W().c(var5, this.field_2031);
         }
      }
   }

   @Inject(method = "method_1749", at = @At("RETURN"))
   private void F(class_332 var1, CallbackInfo var2) {
      if (this.f != null) {
         this.field_2031 = this.f;
         this.f = null;
      }
   }
}

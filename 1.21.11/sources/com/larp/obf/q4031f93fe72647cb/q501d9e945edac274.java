package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.ci;
import com.larpclient.hi;
import com.larpclient.jf;
import com.larpclient.wi;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_757;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_757.class)
public abstract class q501d9e945edac274 {
   @WrapOperation(method = "method_3188", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_757;method_3172(FZLorg/joml/Matrix4f;)V"))
   private void v(class_757 var1, float var2, boolean var3, Matrix4f var4, Operation<Void> var5) {
      if (var1.method_35765()) {
         jf.o();
         hi.c();
         ci.H();
      }

      boolean var6 = !var1.method_35765() && wi.p();
      if (var6) {
         wi.i();

         try {
            var5.call(new Object[]{var1, var2, var3, var4});
         } finally {
            wi.X();
         }
      }

      boolean var7 = !var1.method_35765() && hi.Y();
      boolean var8 = !var1.method_35765() && jf.H();
      boolean var9 = !var1.method_35765() && ci.V();
      boolean var10 = false;

      try {
         if (var6) {
            wi.k(() -> var5.call(new Object[]{var1, var2, var3, var4}));
         } else {
            var5.call(new Object[]{var1, var2, var3, var4});
         }

         var10 = true;
      } finally {
         if (var7) {
            if (var10) {
               hi.J();
            } else {
               hi.c();
            }
         }

         if (var8) {
            if (var10) {
               jf.O();
            } else {
               jf.x();
            }
         }

         if (var9) {
            if (var10) {
               ci.q();
            } else {
               ci.H();
            }
         }

         if (var6) {
            wi.V();
         }
      }
   }
}

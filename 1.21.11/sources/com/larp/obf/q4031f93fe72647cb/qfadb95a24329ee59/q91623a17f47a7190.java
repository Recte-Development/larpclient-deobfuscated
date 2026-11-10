package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.f4;
import com.larpclient.hu;
import com.larpclient.il;
import com.larpclient.ju;
import com.larpclient.q7;
import com.larpclient.t3;
import com.larpclient.tw;
import com.larpclient.ux;
import com.larpclient.yb;
import com.larpclient.yu;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import java.util.Set;
import net.minecraft.class_11909;
import net.minecraft.class_1661;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_465;
import net.minecraft.class_9334;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_465.class)
public class q91623a17f47a7190 {
   @Shadow
   protected int field_2776;
   @Shadow
   protected int field_2800;
   @Shadow
   protected class_1735 field_2787;
   @Shadow
   protected Set<class_1735> field_2793;
   @Shadow
   protected boolean field_2794;
   @Shadow
   private int field_2790;
   private boolean w;
   private boolean l;

   @Shadow
   protected List<class_2561> method_51454(class_1799 var1) {
      throw new AssertionError();
   }

   @Inject(method = "method_25419", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfo var1) {
      ju var2 = ju.b();
      class_310 var3 = class_310.method_1551();
      class_465 var4 = (class_465)this;
      if (tw.s().E(var4.method_17577())) {
         var3.method_1507(null);
         var1.cancel();
      } else if (var2.U() && var2.a() && var4.method_17577() == var3.field_1724.field_7512 && var4.method_17577().method_34255().method_7960()) {
         var3.method_1507(null);
         var1.cancel();
      } else {
         q7 var5 = q7.L();
         if (var5.Y(((class_465)this).method_17577())) {
            class_310.method_1551().method_1507(null);
            var1.cancel();
         } else if (var5.blocksClose()) {
            var1.cancel();
         }
      }
   }

   private boolean I(class_1735 var1) {
      if (var1 == null || var1.method_34266() < 0 || var1.method_34266() > 8) {
         return false;
      }

      if (var1.field_7871 instanceof class_1661) {
         return false;
      }

      class_1703 var2 = ((class_465)this).method_17577();
      if (!(var2 instanceof class_1716)) {
         return false;
      }

      hu var3 = hu.f();
      return var3.f() == hu.q.Y && var3.z() == var2.field_7763;
   }

   @ModifyVariable(method = "method_2385", at = @At("STORE"), ordinal = 0)
   private class_1799 j(class_1799 var1, class_332 var2, class_1735 var3, int var4, int var5) {
      class_310 var6 = class_310.method_1551();
      if (var6.field_1724 != null && var3.field_7871 == var6.field_1724.method_31548()) {
         class_1799 var7 = q7.L().getInventorySlotProjection(var3.method_34266());
         if (var7 != null) {
            return var7;
         }

         class_1799 var8 = tw.s().k(var1);
         var8 = ju.b().q(var8);
         var8 = f4.L().U(var8);
         var8 = t3.c().Z(var3.method_34266(), var8);
         var8 = ux.o().X(var8);
         var8 = yb.b().U(var8);
         class_1799 var9 = yu.o().H(var3.method_34266(), var8);
         if (var9 != null) {
            var8 = var9;
         }

         return bk.W().c(var3.method_34266(), var8);
      } else {
         return var1;
      }
   }

   @Inject(method = "method_2385", at = @At("HEAD"), cancellable = true)
   private void O(class_332 var1, class_1735 var2, int var3, int var4, CallbackInfo var5) {
      if (var2 != null) {
         class_1799 var6 = var2.field_7871 instanceof class_1661 ? null : il.K(var2);
         if (var6 != null) {
            if (!var6.method_7960()) {
               var1.method_51427(var6, var2.field_7873, var2.field_7872);
               var1.method_51431(class_310.method_1551().field_1772, var6, var2.field_7873, var2.field_7872);
            }

            var5.cancel();
            return;
         }

         bk var7 = bk.W();
         boolean var8 = var2.field_7871 instanceof class_1661 && var7.E() && var2.method_34266() == var7.n();
         if (var8) {
            boolean var9 = var3 - this.field_2776 >= var2.field_7873
               && var3 - this.field_2776 < var2.field_7873 + 16
               && var4 - this.field_2800 >= var2.field_7872
               && var4 - this.field_2800 < var2.field_7872 + 16;
            if (this.field_2794) {
               if (var9) {
                  this.l = true;
               }
            } else {
               this.l = false;
            }

            if (var9 || this.l) {
               class_1799 var10 = ((class_465)this).method_17577().method_34255();
               if (!var10.method_7960()) {
                  class_1799 var11 = this.T(var10);
                  var1.method_51427(var11, var2.field_7873, var2.field_7872);
                  var1.method_51431(class_310.method_1551().field_1772, var11, var2.field_7873, var2.field_7872);
                  var5.cancel();
                  return;
               }
            }
         }

         class_1799 var12 = tw.s().I(class_310.method_1551(), ((class_465)this).method_17577(), var2);
         if (var12 == null) {
            var12 = ju.b().Y(class_310.method_1551(), ((class_465)this).method_17577(), var2);
         }

         if (var12 == null) {
            var12 = f4.L().y(class_310.method_1551(), var2);
         }

         if (var12 == null) {
            var12 = t3.c().X(class_310.method_1551(), var2);
         }

         if (var12 == null) {
            var12 = ux.o().g(class_310.method_1551(), var2);
         }

         if (var12 == null) {
            var12 = yb.b().r(class_310.method_1551(), var2);
         }

         if (var12 == null && !(var2.field_7871 instanceof class_1661) && this.I(var2)) {
            class_1799 var13 = yu.o().t(class_310.method_1551(), var2.method_34266(), var2.method_7677());
            if (var13 != null) {
               var12 = var13;
            }
         }

         if (var12 != null) {
            if (var12.method_7960()) {
               var5.cancel();
               return;
            }

            var1.method_51427(var12, var2.field_7873, var2.field_7872);
            var1.method_51431(class_310.method_1551().field_1772, var12, var2.field_7873, var2.field_7872);
            var5.cancel();
         }
      }
   }

   @Inject(method = "method_25403", at = @At("TAIL"))
   private void Z(class_11909 var1, double var2, double var4, CallbackInfoReturnable<Boolean> var6) {
      bk var7 = bk.W();
      if (var7.E() && !this.field_2793.isEmpty()) {
         int var8 = var7.n();
         this.field_2793.removeIf(var1x -> var1x.field_7871 instanceof class_1661 && var1x.method_34266() == var8);
      }
   }

   @Inject(method = "method_25406", at = @At("HEAD"))
   private void m(class_11909 var1, CallbackInfoReturnable<Boolean> var2) {
      bk var3 = bk.W();
      if (var3.E()) {
         int var4 = var3.n();
         class_1735 var5 = null;

         for (class_1735 var7 : ((class_465)this).method_17577().field_7761) {
            if (var7.field_7871 instanceof class_1661 && var7.method_34266() == var4) {
               var5 = var7;
               break;
            }
         }

         if (var5 != null) {
            if (var1.comp_4798() - this.field_2776 >= var5.field_7873
               && var1.comp_4798() - this.field_2776 < var5.field_7873 + 16
               && var1.comp_4799() - this.field_2800 >= var5.field_7872
               && var1.comp_4799() - this.field_2800 < var5.field_7872 + 16) {
               class_1799 var8 = ((class_465)this).method_17577().method_34255();
               if (!var8.method_7960()) {
                  var3.u(this.T(var8));
               }
            }
         }
      }
   }

   @Inject(method = "method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V", at = @At("HEAD"), cancellable = true)
   private void W(class_1735 var1, int var2, int var3, class_1713 var4, CallbackInfo var5) {
      q7.L().t(((class_465)this).method_17577());
      if (q7.L().blocksUserInteraction() || q7.L().Y(((class_465)this).method_17577())) {
         var5.cancel();
      } else if (var1 != null && var1.field_7871 instanceof class_1661) {
         bk var6 = bk.W();
         if (var6.E() && var1.method_34266() == var6.n()) {
            if (var4 == class_1713.field_7790 && !var6.i().method_7960()) {
               var6.h();
            }

            var5.cancel();
         }
      }
   }

   @Inject(method = "method_2383(Lnet/minecraft/class_1735;IILnet/minecraft/class_1713;)V", at = @At("HEAD"))
   private void R(class_1735 var1, int var2, int var3, class_1713 var4, CallbackInfo var5) {
      if (var4 == class_1713.field_7796 && var1 != null) {
         yu var6 = yu.o();
         class_310 var7 = class_310.method_1551();
         if (var1.field_7871 instanceof class_1661) {
            var6.c(var1.method_7677(), var6.H(var1.method_34266(), var1.method_7677()));
         } else if (this.I(var1)) {
            var6.c(var1.method_7677(), var6.t(var7, var1.method_34266(), var1.method_7677()));
         }
      }
   }

   private class_1799 T(class_1799 var1) {
      int var2;
      if (this.field_2790 == 1) {
         var2 = 1;
      } else if (this.field_2790 == 0) {
         int var3 = this.field_2793.size();
         var2 = var3 <= 0 ? var1.method_7947() : Math.max(1, var1.method_7947() / var3);
      } else {
         var2 = var1.method_7947();
      }

      return var1.method_46651(Math.max(1, Math.min(var2, var1.method_7914())));
   }

   @WrapOperation(method = "method_71083", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1703;method_34255()Lnet/minecraft/class_1799;"))
   private class_1799 b(class_1703 var1, Operation<class_1799> var2) {
      if (q7.L().hidesCursor(var1)) {
         return class_1799.field_8037;
      }

      class_1799 var3 = f4.L().U(il.Y((class_1799)var2.call(new Object[]{var1})));
      var3 = t3.c().o(var3);
      var3 = yu.o().z(var3);
      return yb.b().U(ux.o().V(var3));
   }

   @WrapOperation(method = "method_2380", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1703;method_34255()Lnet/minecraft/class_1799;"))
   private class_1799 d(class_1703 var1, Operation<class_1799> var2) {
      if (q7.L().hidesCursor(var1)) {
         return class_1799.field_8037;
      }

      class_1799 var3 = f4.L().U(il.Y((class_1799)var2.call(new Object[]{var1})));
      var3 = t3.c().o(var3);
      var3 = yu.o().z(var3);
      return yb.b().U(ux.o().V(var3));
   }

   @Inject(method = "method_2380", at = @At("HEAD"), cancellable = true)
   private void R(class_332 var1, int var2, int var3, CallbackInfo var4) {
      class_310 var5 = class_310.method_1551();
      if (var5.field_1724 != null && this.field_2787 != null) {
         class_1799 var6 = tw.s().I(var5, ((class_465)this).method_17577(), this.field_2787);
         if (var6 == null) {
            var6 = ju.b().Y(var5, ((class_465)this).method_17577(), this.field_2787);
         }

         if (var6 == null) {
            var6 = il.K(this.field_2787);
         }

         if (var6 == null) {
            var6 = f4.L().y(var5, this.field_2787);
         }

         if (var6 == null) {
            var6 = t3.c().X(var5, this.field_2787);
         }

         if (var6 == null) {
            var6 = ux.o().g(var5, this.field_2787);
         }

         if (var6 == null) {
            var6 = yb.b().r(var5, this.field_2787);
         }

         if (var6 == null && this.field_2787.field_7871 == var5.field_1724.method_31548()) {
            var6 = q7.L().getInventorySlotProjection(this.field_2787.method_34266());
         }

         if (var6 != null) {
            class_1703 var7 = ((class_465)this).method_17577();
            if (!var6.method_7960() && (var7.method_34255().method_7960() || q7.L().hidesCursor(var7))) {
               this.w = true;

               try {
                  var1.method_51437(
                     var5.field_1772, this.method_51454(var6), var6.method_32347(), var2, var3, (class_2960)var6.method_58694(class_9334.field_54198)
                  );
               } finally {
                  this.w = false;
               }
            }

            var4.cancel();
         }
      }
   }

   @Inject(method = "method_51454", at = @At("HEAD"), cancellable = true)
   private void W(class_1799 var1, CallbackInfoReturnable<List<class_2561>> var2) {
      if (!this.w && var1 != null && !var1.method_7960()) {
         class_1799 var3 = null;
         if (this.field_2787 != null) {
            var3 = f4.L().y(class_310.method_1551(), this.field_2787);
            if (var3 == null) {
               var3 = t3.c().X(class_310.method_1551(), this.field_2787);
            }

            if (var3 == null) {
               var3 = ux.o().g(class_310.method_1551(), this.field_2787);
            }

            if (var3 == null) {
               var3 = yb.b().r(class_310.method_1551(), this.field_2787);
            }

            if (var3 == null && this.field_2787.field_7871 instanceof class_1661) {
               class_1799 var4 = t3.c().Z(this.field_2787.method_34266(), var1);
               var4 = f4.L().U(var4);
               class_1799 var5 = q7.L().getVisualOverride(var4);
               if (var5 != null) {
                  var4 = var5;
               }

               var4 = ux.o().X(var4);
               var4 = yb.b().U(var4);
               var4 = bk.W().c(this.field_2787.method_34266(), var4);
               if (var4 != var1) {
                  var3 = var4;
               }

               class_1799 var6 = yu.o().H(this.field_2787.method_34266(), var1);
               if (var3 == null && var6 != null) {
                  var3 = var6;
               }
            }

            if (var3 == null && this.I(this.field_2787)) {
               var3 = yu.o().t(class_310.method_1551(), this.field_2787.method_34266(), var1);
            }
         }

         if (var3 != null) {
            if (var3.method_7960()) {
               var2.setReturnValue(List.of());
            } else {
               this.w = true;

               try {
                  var2.setReturnValue(this.method_51454(var3));
               } finally {
                  this.w = false;
               }
            }
         }
      }
   }
}

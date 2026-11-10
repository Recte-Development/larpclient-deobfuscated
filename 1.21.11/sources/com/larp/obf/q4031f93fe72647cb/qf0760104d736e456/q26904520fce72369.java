package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import com.larpclient.gb;
import com.larpclient.jn;
import com.larpclient.qh;
import com.larpclient.t0;
import com.larpclient.wa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1836;
import net.minecraft.class_2561;
import net.minecraft.class_9334;
import net.minecraft.class_1792.class_9635;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1799.class)
public abstract class q26904520fce72369 implements jn.e, wa.k {
   @Unique
   private boolean b;
   @Unique
   private boolean I;
   @Unique
   private boolean t;

   @Override
   public boolean fakeSpawner$isPreview() {
      return this.t;
   }

   @Override
   public void fakeSpawner$setPreview(boolean var1) {
      this.t = var1;
   }

   @Override
   public boolean fakeElytra$isRealPurchase() {
      return this.b;
   }

   @Override
   public void fakeElytra$setRealPurchase(boolean var1) {
      this.b = var1;
   }

   @Override
   public boolean fakeSpawner$isWithdrawnBone() {
      return this.I;
   }

   @Override
   public void fakeSpawner$setWithdrawnBone(boolean var1) {
      this.I = var1;
   }

   @Inject(method = {"method_7972", "method_46651", "method_7971"}, at = @At("RETURN"))
   private void m(CallbackInfoReturnable<class_1799> var1) {
      jn.Y((class_1799)var1.getReturnValue(), this.b);
      wa.C((class_1799)var1.getReturnValue(), this.I);
      if (this.t) {
         jn.Q((class_1799)var1.getReturnValue());
      }
   }

   @Inject(method = "method_7964", at = @At("HEAD"), cancellable = true)
   private void L(CallbackInfoReturnable<class_2561> var1) {
      cz var2 = cz.T();
      class_1799 var3 = (class_1799)this;
      if (var2.X(var3)) {
         var1.setReturnValue(var2.Z(var3));
      }
   }

   @Inject(method = "method_7958", at = @At("HEAD"), cancellable = true)
   private void v(CallbackInfoReturnable<Boolean> var1) {
      cz var2 = cz.T();
      if (var2.X((class_1799)this)) {
         var1.setReturnValue(var2.p());
      }
   }

   @Inject(method = "method_7950", at = @At("HEAD"), cancellable = true)
   private void k(class_9635 var1, class_1657 var2, class_1836 var3, CallbackInfoReturnable<List<class_2561>> var4) {
      class_1799 var5 = (class_1799)this;
      cz var6 = cz.T();
      if (var6.X(var5)) {
         ArrayList var7 = new ArrayList();
         var7.add(var6.Z(var5));
         var7.addAll(var6.v(var5, !gb.j(), var3 != null && var3.method_8035()));
         var4.setReturnValue(var7);
      } else if (var6.G(var5)) {
         var4.setReturnValue(var6.Y(var5, false));
      }
   }

   @Inject(method = "method_7950", at = @At("RETURN"), cancellable = true)
   private void n(class_9635 var1, class_1657 var2, class_1836 var3, CallbackInfoReturnable<List<class_2561>> var4) {
      class_1799 var5 = (class_1799)this;
      cz var6 = cz.T();
      if (var6.J() && !t0.V(var5) && !gb.j()) {
         long var7 = t0.u(var5) ? t0.k() : (!qh.I(var5.method_7909()) && !var5.method_57826(class_9334.field_49622) ? 0L : var6.L(var5));
         if (var7 > 0L) {
            ArrayList var9 = new ArrayList((Collection)var4.getReturnValue());
            var9.removeIf(var0 -> {
               String var1x = var0.getString().trim().toLowerCase(Locale.ROOT);
               return var1x.startsWith("worth:") || var1x.startsWith("~$") || var1x.contains("$");
            });
            var9.add(Math.min(1, var9.size()), var6.r(var7));
            var4.setReturnValue(var9);
         }
      }
   }
}

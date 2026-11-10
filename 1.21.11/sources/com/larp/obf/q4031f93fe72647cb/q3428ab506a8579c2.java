package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bd;
import com.larpclient.e_;
import com.larpclient.i7;
import com.larpclient.sh;
import com.larpclient.uf;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.AutoCartModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoGGModule;
import com.larpclient.module.AutoSellModule;
import com.larpclient.module.BoatFlyModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.FakeLagModule;
import com.larpclient.module.FlyModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.KillFXModule;
import com.larpclient.module.NoFallModule;
import com.larpclient.module.OrderDropperModule;
import com.larpclient.module.ReachModule;
import com.larpclient.module.SpeedModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import com.larpclient.module.a1;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_10182;
import net.minecraft.class_1297;
import net.minecraft.class_1324;
import net.minecraft.class_243;
import net.minecraft.class_2616;
import net.minecraft.class_2645;
import net.minecraft.class_2649;
import net.minecraft.class_2663;
import net.minecraft.class_2664;
import net.minecraft.class_2692;
import net.minecraft.class_2696;
import net.minecraft.class_2709;
import net.minecraft.class_2761;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;
import net.minecraft.class_8143;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_634.class)
public class q3428ab506a8579c2 {
   @Inject(method = "method_11153(Lnet/minecraft/class_2649;)V", at = @At("TAIL"))
   private void O(class_2649 var1, CallbackInfo var2) {
      OrderDropperModule.F(var1.comp_3837());
   }

   @Inject(method = "method_11102(Lnet/minecraft/class_2645;)V", at = @At("RETURN"))
   private void m(class_2645 var1, CallbackInfo var2) {
      AutoSellModule.K(var1.method_36148());
   }

   @Inject(method = "method_11079", at = @At("TAIL"))
   private void G(class_2761 var1, CallbackInfo var2) {
      a1.x();
   }

   @Inject(method = "method_49034", at = @At("TAIL"))
   private void n(class_8143 var1, CallbackInfo var2) {
      uf.h(var1);
      class_746 var3 = class_310.method_1551().field_1724;
      if (var3 != null && var1.comp_1267() == var3.method_5628()) {
         i7.K();
      }

      DamageMarkersModule var4 = DamageMarkersModule.g();
      if (var4 != null) {
         var4.b(var1);
      }

      KillFXModule var5 = KillFXModule.y();
      if (var5 != null) {
         var5.a(var1);
      }

      AutoGGModule var6 = AutoGGModule.A();
      if (var6 != null && var6.isEnabled()) {
         var6.w(var1);
      }
   }

   @Inject(method = "method_11148", at = @At("TAIL"))
   private void f(class_2663 var1, CallbackInfo var2) {
      uf.J(var1);
      KillFXModule var3 = KillFXModule.y();
      if (var3 != null) {
         var3.t(var1);
      }

      AutoGGModule var4 = AutoGGModule.A();
      if (var4 != null && var4.isEnabled()) {
         var4.c(var1);
      }

      AutoCrystalModule var5 = AutoCrystalModule.l();
      if (var5 != null) {
         var5.L(var1);
      }
   }

   @Inject(method = "method_11134", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_5641(DDDFF)V", shift = Shift.AFTER))
   private void A(class_2692 var1, CallbackInfo var2) {
      BoatFlyModule.D();
   }

   @Inject(method = "method_11160", at = @At("TAIL"))
   private void w(class_2616 var1, CallbackInfo var2) {
      DamageMarkersModule var3 = DamageMarkersModule.g();
      if (var3 != null) {
         var3.e(var1);
      }
   }

   @Inject(method = "method_64897", at = @At("HEAD"))
   private static void B(class_10182 var0, Set<class_2709> var1, class_1297 var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof class_746 var5) {
         sh.f(var5);
      }
   }

   @Inject(method = "method_64897", at = @At("RETURN"))
   private static void Q(class_10182 var0, Set<class_2709> var1, class_1297 var2, boolean var3, CallbackInfoReturnable<Boolean> var4) {
      if (var2 instanceof class_746 var5) {
         bd.l();
         FakeLagModule.e(var5);
         SpeedModule.c(var5);
         TargetStrafeModule.r(var5);
         GrimSpeedModule.u(var5);
         NoFallModule.M(var5);
         AimAssistModule.n(var5);
         WTapModule.i(var5);
         sh.I(var5);
         e_.Y();
         JumpFXModule var6 = JumpFXModule.p();
         if (var6 != null) {
            var6.w();
         }
      }
   }

   @WrapOperation(method = "method_11132", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1297;method_5750(Lnet/minecraft/class_243;)V"))
   private void F(class_1297 var1, class_243 var2, Operation<Void> var3) {
      if (var1 == class_310.method_1551().field_1724) {
         var2 = i7.O(var1.method_18798(), var2);
      }

      var3.call(new Object[]{var1, var2});
   }

   @Redirect(method = "method_11124", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_2664;comp_2884()Ljava/util/Optional;"))
   private Optional<class_243> a(class_2664 var1) {
      return var1.comp_2884().map(var0 -> i7.D(var0, true));
   }

   @Inject(method = "method_11124", at = @At("RETURN"))
   private void J(class_2664 var1, CallbackInfo var2) {
      AutoCartModule.w(var1.comp_2883(), var1.comp_4594());
   }

   @Redirect(method = "method_11149", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1324;method_6192(D)V"))
   private void W(class_1324 var1, double var2) {
      ReachModule var4 = ReachModule.b();
      if (var4 != null) {
         var4.w(var1);
      }

      var1.method_6192(var2);
   }

   @Inject(method = "method_11154", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_1657;method_31549()Lnet/minecraft/class_1656;", ordinal = 0))
   private void k(class_2696 var1, CallbackInfo var2) {
      FlyModule var3 = FlyModule.W();
      if (var3 != null) {
         var3.q();
      }

      FreecamModule.b();
   }
}

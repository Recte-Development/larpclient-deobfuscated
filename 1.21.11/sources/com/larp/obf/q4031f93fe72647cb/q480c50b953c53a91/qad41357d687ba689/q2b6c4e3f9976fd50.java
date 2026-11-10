package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689;

import com.larpclient.CrystalChamsModule;
import com.larpclient.bb;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_10014;
import net.minecraft.class_11659;
import net.minecraft.class_12249;
import net.minecraft.class_1511;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_3879;
import net.minecraft.class_4587;
import net.minecraft.class_892;
import net.minecraft.class_11683.class_11792;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_892.class)
public class q2b6c4e3f9976fd50 {
   @WrapOperation(
      method = "method_3908(Lnet/minecraft/class_10014;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4587;method_22905(FFF)V")
   )
   private void y(class_4587 var1, float var2, float var3, float var4, Operation<Void> var5) {
      CrystalChamsModule var6 = CrystalChamsModule.X();
      float var7 = var6 == null ? 1.0F : var6.s();
      var5.call(new Object[]{var1, var2 * var7, var3 * var7, var4 * var7});
   }

   @WrapOperation(
      method = "method_3908(Lnet/minecraft/class_10014;Lnet/minecraft/class_4587;Lnet/minecraft/class_11659;Lnet/minecraft/class_12075;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_11659;method_73489(Lnet/minecraft/class_3879;Ljava/lang/Object;Lnet/minecraft/class_4587;Lnet/minecraft/class_1921;IIILnet/minecraft/class_11683$class_11792;)V"
      )
   )
   private <S> void o(
      class_11659 var1, class_3879<S> var2, S var3, class_4587 var4, class_1921 var5, int var6, int var7, int var8, class_11792 var9, Operation<Void> var10
   ) {
      CrystalChamsModule var11 = CrystalChamsModule.X();
      if (var11 == null || !var11.b(var3)) {
         if (var11 != null && var11.p()) {
            class_2960 var12 = class_2960.method_60656("textures/entity/end_crystal/end_crystal.png");
            class_1921 var13 = var11.P() ? class_12249.method_76002(var12) : (var11.K() ? class_12249.method_76000(var12) : var2.method_23500(var12));
            int var14 = var3 instanceof class_10014 var15 ? (int)(var15.field_53325 * 7.0 + var15.field_53327 * 13.0) : 0;
            var1.method_73490(var2, var3, var4, var13, var11.P() ? 15728880 : var6, var7, var11.o(var14), null, var8, var9);
         } else {
            var10.call(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8, var9});
         }
      }
   }

   @Inject(method = "method_23155", at = @At("RETURN"), cancellable = true)
   private static void h(float var0, CallbackInfoReturnable<Float> var1) {
      CrystalChamsModule var2 = CrystalChamsModule.X();
      if (var2 != null) {
         var1.setReturnValue(var1.getReturnValueF() * var2.d());
      }
   }

   @Inject(method = "method_62417(Lnet/minecraft/class_1511;Lnet/minecraft/class_10014;F)V", at = @At("TAIL"))
   private void e(class_1511 var1, class_10014 var2, float var3, CallbackInfo var4) {
      ((bb)var2).larpCrystalId(var1.method_5628());
      CrystalChamsModule var5 = CrystalChamsModule.X();
      Boolean var6 = var5 == null ? null : var5.a();
      if (var6 != null) {
         var2.field_53311 = var6;
      }
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.IgnoreEntityModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.function.Predicate;
import net.minecraft.class_1297;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(class_746.class)
public class q797d5dd1633a5d90 {
   @ModifyExpressionValue(method = "method_76763", at = @At(value = "FIELD", target = "Lnet/minecraft/class_1301;field_52443:Ljava/util/function/Predicate;"))
   private static Predicate<class_1297> T(Predicate<class_1297> var0) {
      return !class_310.method_1551().field_1690.field_1904.method_1434() ? var0 : var0.and(var0x -> !AntiTrapModule.C(var0x) && !IgnoreEntityModule.U(var0x));
   }

   @ModifyExpressionValue(method = "method_76762", at = @At(value = "FIELD", target = "Lnet/minecraft/class_1301;field_52443:Ljava/util/function/Predicate;"))
   private Predicate<class_1297> f(Predicate<class_1297> var1) {
      return T(var1);
   }
}

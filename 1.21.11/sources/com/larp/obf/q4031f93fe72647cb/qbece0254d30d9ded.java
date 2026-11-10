package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.sg;
import net.minecraft.class_11659;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_759;
import net.minecraft.class_811;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_759.class)
public final class qbece0254d30d9ded {
   @Inject(method = "method_3233", at = @At("HEAD"))
   private void u(class_1309 var1, class_1799 var2, class_811 var3, class_4587 var4, class_11659 var5, int var6, CallbackInfo var7) {
      if (var1 == class_310.method_1551().field_1724 && var3.method_29998() && !var2.method_7960()) {
         sg.D(var3 == class_811.field_4321 ? class_1306.field_6182 : class_1306.field_6183, var4, var5, true);
      }
   }

   @Inject(method = "method_3219", at = @At("RETURN"))
   private void I(class_4587 var1, class_11659 var2, int var3, float var4, float var5, class_1306 var6, CallbackInfo var7) {
      class_310 var8 = class_310.method_1551();
      if (var8.field_1724 != null) {
         class_1799 var9 = var6 == var8.field_1724.method_6068() ? var8.field_1724.method_6047() : var8.field_1724.method_6079();
         sg.D(var6, var1, var2, !var9.method_7960());
      }
   }

   @Inject(
      method = "method_3216",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_4587;method_22909()V", shift = Shift.BEFORE),
      require = 1,
      allow = 1
   )
   private void m(class_4587 var1, class_11659 var2, int var3, class_1306 var4, CallbackInfo var5) {
      sg.D(var4, var1, var2, true);
   }
}

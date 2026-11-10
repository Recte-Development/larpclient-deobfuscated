package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wc;
import com.larpclient.module.ClickPulseModule;
import com.larpclient.module.DamageMarkersModule;
import com.larpclient.module.HitSoundsModule;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_636.class)
public class qeaad9396e0d449bf {
   @Inject(method = "method_2918", at = @At("HEAD"))
   private void f(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      DamageMarkersModule var4 = DamageMarkersModule.g();
      if (var4 != null && var1 == class_310.method_1551().field_1724) {
         var4.U(var2);
      }

      HitSoundsModule var5 = HitSoundsModule.B();
      if (var5 != null && var1 == class_310.method_1551().field_1724) {
         var5.J(var2);
      }

      ClickPulseModule var6 = ClickPulseModule.T();
      if (var6 != null && var1 == class_310.method_1551().field_1724) {
         var6.y(var2);
      }
   }

   @Inject(method = "method_2918", at = @At("TAIL"))
   private void P(class_1657 var1, class_1297 var2, CallbackInfo var3) {
      if (var2 instanceof class_1309 var4) {
         wc.P(var4);
      }
   }
}

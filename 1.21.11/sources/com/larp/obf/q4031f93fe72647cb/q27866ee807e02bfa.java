package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.q8;
import com.larpclient.module.AuraModule;
import com.larpclient.module.ChamsModule;
import com.larpclient.module.ESPModule;
import com.larpclient.module.Feature3DUIModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.SpinBotModule;
import net.minecraft.class_10042;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_583;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_922.class)
public class q27866ee807e02bfa<T extends class_1309, S extends class_10042, M extends class_583<? super S>> {
   @Inject(method = "method_62355(Lnet/minecraft/class_1309;Lnet/minecraft/class_10042;F)V", at = @At("RETURN"))
   private void R(T var1, S var2, float var3, CallbackInfo var4) {
      try {
         ChamsModule var5 = ChamsModule.K();
         int var6 = var5 != null && var5.isEnabled() ? var5.B(var1) : 0;
         if (var6 == 0 && ESPModule.s()) {
            var6 = ESPModule.j(var1);
         }

         if (var6 != 0) {
            var2.field_61821 = var6;
         }

         AuraModule var7 = AuraModule.R();
         MaceModule var8 = MaceModule.c();
         if ((var7 != null || var8 != null) && var1 == class_310.method_1551().field_1724) {
            float var9 = var2.field_53446 + var2.field_53447;
            if (var8 != null && var8.l()) {
               var2.field_53446 = var8.I(var2.field_53446, var9);
            } else if (var7 != null) {
               var2.field_53446 = var7.F(var2.field_53446, var9);
            }

            var2.field_53447 = class_3532.method_15393(var9 - var2.field_53446);
         }

         SpinBotModule var11 = SpinBotModule.A();
         if (var11 != null && var1 == class_310.method_1551().field_1724) {
            var2.field_53446 = var11.u(var2.field_53446, var3);
            var2.field_53447 = var11.o(var2.field_53447);
         }

         if (var1 == class_310.method_1551().field_1724) {
            Feature3DUIModule.G(var1, var2, var3);
         }
      } catch (Exception var10) {
         q8.O("Render", "LivingEntityRendererMixin callback failed", var10);
      }
   }
}

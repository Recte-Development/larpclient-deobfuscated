package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.w6;
import com.larpclient.module.AnimationsModule;
import com.larpclient.module.SwordModelsModule;
import com.larpclient.module.ViewModelModule;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_310;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1309.class)
public class q5f276f3dfca5ecea {
   @Inject(method = "method_6028", at = @At("RETURN"), cancellable = true)
   private void N(CallbackInfoReturnable<Integer> var1) {
      if (this instanceof class_746 var2) {
         if (this == class_310.method_1551().field_1724) {
            SwordModelsModule var9 = SwordModelsModule.X();
            class_1268 var4 = w6.z(var2);
            class_1268 var5 = var4 == null ? class_1268.field_5808 : var4;
            if (var9 == null || !var9.b(var2.method_5998(var5))) {
               int var6 = (Integer)var1.getReturnValue();
               AnimationsModule var7 = AnimationsModule.W();
               if (var7 != null && var7.isRuntimeActive() && var7.F()) {
                  var6 = var7.h(var6);
               }

               ViewModelModule var8 = ViewModelModule.B();
               if (var8 != null && var8.isRuntimeActive()) {
                  var6 = var8.D(var6);
               }

               if (var6 != (Integer)var1.getReturnValue()) {
                  var1.setReturnValue(var6);
               }
            }
         }
      }
   }
}

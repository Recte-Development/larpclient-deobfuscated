package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.LarpClient;
import com.larpclient.hh;
import com.larpclient.tm;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_442.class)
public abstract class q877ac70aaf08ece3 extends class_437 {
   protected q877ac70aaf08ece3(class_2561 var1) {
      super(var1);
   }

   @Inject(method = "method_25426", at = @At("TAIL"))
   private void Y(CallbackInfo var1) {
      class_310 var2 = class_310.method_1551();
      LarpClient.q = true;
      if (!hh.D()) {
         class_437 var3 = this;
         this.method_37063(
            class_4185.method_46430(class_2561.method_43470("Larp client"), var2x -> var2.method_1507(tm.y(var3))).method_46434(4, 4, 90, 20).method_46431()
         );
      } else {
         var2.execute(() -> {
            if (var2.field_1755 instanceof class_442) {
               if (hh.D()) {
                  var2.method_1507(new hh());
               }
            }
         });
      }
   }
}

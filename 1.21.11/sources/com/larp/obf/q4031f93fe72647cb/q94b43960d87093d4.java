package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.PlayerModelModule;
import net.minecraft.class_10017;
import net.minecraft.class_1306;
import net.minecraft.class_3881;
import net.minecraft.class_4587;
import net.minecraft.class_989;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_989.class)
public class q94b43960d87093d4 {
   @Redirect(
      method = "method_4192",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_3881;method_2803(Lnet/minecraft/class_10017;Lnet/minecraft/class_1306;Lnet/minecraft/class_4587;)V"
      )
   )
   private void Z(class_3881<class_10017> var1, class_10017 var2, class_1306 var3, class_4587 var4) {
      PlayerModelModule var5 = PlayerModelModule.h();
      if (var5 == null || !var5.o(var2, var3, var4)) {
         var1.method_2803(var2, var3, var4);
      }
   }
}

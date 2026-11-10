package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_2818;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_6603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_634.class)
public abstract class qa8f5013f725a68fe {
   @Shadow
   private class_638 field_3699;

   @Inject(method = "method_38539", at = @At("TAIL"))
   private void U(int var1, int var2, class_6603 var3, CallbackInfo var4) {
      cz var5 = cz.T();
      if (this.field_3699 != null && var5.J()) {
         class_2818 var6 = this.field_3699.method_8497(var1, var2);
         var5.a(var6, var6.method_12214().keySet());
      }
   }
}

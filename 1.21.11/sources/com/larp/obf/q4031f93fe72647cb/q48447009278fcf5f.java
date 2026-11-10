package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.AutoTotemModule;
import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_746.class)
public class q48447009278fcf5f {
   @Inject(method = "method_6007()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_310;method_1577()Lnet/minecraft/class_1156;"))
   private void H(CallbackInfo var1) {
      class_746 var2 = (class_746)this;
      if (AutoTotemModule.s(var2)) {
         var2.field_3913.field_54155 = class_10185.field_54098;
         var2.field_3913.field_55868 = class_241.field_1340;
      }
   }
}

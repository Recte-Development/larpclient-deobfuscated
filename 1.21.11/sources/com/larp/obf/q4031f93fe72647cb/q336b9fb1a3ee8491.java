package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.u5;
import com.larpclient.module.OrderDropperModule;
import net.minecraft.class_10185;
import net.minecraft.class_743;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_743.class)
public abstract class q336b9fb1a3ee8491 extends class_744 {
   @Inject(method = "method_3129", at = @At("TAIL"))
   private void v(CallbackInfo var1) {
      class_10185 var2 = OrderDropperModule.d();
      if (var2 != null) {
         this.field_54155 = var2;
         this.field_55868 = u5.R(var2);
      }
   }
}

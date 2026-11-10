package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.hq;
import com.larpclient.wp;
import com.larpclient.module.FreecamModule;
import net.minecraft.class_743;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_743.class)
public abstract class qe2ae824d924e4139 extends class_744 {
   @Inject(method = "method_3129", at = @At("TAIL"))
   private void a(CallbackInfo var1) {
      if (wp.d()) {
         this.field_54155 = FreecamModule.i(this.field_54155);
         this.field_55868 = hq.T(this.field_54155);
      }
   }
}

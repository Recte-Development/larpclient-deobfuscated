package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q71d799de1d67a89b;

import com.larpclient.sq;
import com.larpclient.u5;
import net.minecraft.class_10185;
import net.minecraft.class_743;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_743.class)
public abstract class q70c9b307f1c8a08c extends class_744 {
   @Inject(method = "method_3129", at = @At("TAIL"))
   private void M(CallbackInfo var1) {
      class_10185 var2 = sq.B();
      if (var2 != null) {
         this.field_54155 = var2;
         this.field_55868 = u5.R(var2);
      }
   }
}

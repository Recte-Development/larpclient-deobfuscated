package com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494;

import com.larpclient.s0;
import com.larpclient.sb;
import com.larpclient.sh;
import com.larpclient.sz;
import com.larpclient.u5;
import net.minecraft.class_10185;
import net.minecraft.class_743;
import net.minecraft.class_744;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_743.class)
public abstract class qaf91d44ef1d271e7 extends class_744 {
   @Inject(method = "method_3129", at = @At("TAIL"))
   private void v(CallbackInfo var1) {
      class_10185 var2 = sb.O();
      if (var2 == null) {
         var2 = sz.e();
      }

      if (var2 == null) {
         var2 = sh.k();
      }

      if (var2 == null) {
         var2 = s0.e();
      }

      if (var2 != null) {
         this.field_54155 = var2;
         this.field_55868 = u5.R(var2);
      }
   }
}

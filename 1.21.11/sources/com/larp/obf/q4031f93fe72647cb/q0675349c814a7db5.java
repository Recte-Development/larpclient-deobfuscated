package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.ix;
import com.larpclient.qx;
import net.minecraft.class_310;
import net.minecraft.class_339;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(class_339.class)
public abstract class q0675349c814a7db5 {
   @ModifyArgs(method = "method_25394", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_332;method_58135(II)Z"))
   private void o(Args var1) {
      class_437 var2 = class_310.method_1551().field_1755;
      if (var2 instanceof qx var3 && var3.larpDrawingMenuPopup()) {
         ix.g var4 = qx.A(var2);
         var1.set(0, (int)Math.round(var4.screenX(((Integer)var1.get(0)).intValue(), var2.field_22789 / 2.0)));
         var1.set(1, (int)Math.round(var4.screenY(((Integer)var1.get(1)).intValue(), var2.field_22790 / 2.0)));
      }
   }
}

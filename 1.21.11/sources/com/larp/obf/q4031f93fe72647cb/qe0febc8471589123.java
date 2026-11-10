package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.iw;
import com.larpclient.module.AimAssistModule;
import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(class_312.class)
public abstract class qe0febc8471589123 {
   @ModifyArgs(
      method = "method_1606(D)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_746;method_5872(DD)V"),
      order = 1100,
      require = 1,
      allow = 1
   )
   private void s(Args var1, double var2) {
      iw.u var4 = AimAssistModule.Q(class_310.method_1551().field_1724, (Double)var1.get(0), (Double)var1.get(1), var2);
      var1.set(0, (Double)var1.get(0) + var4.yaw());
      var1.set(1, (Double)var1.get(1) + var4.pitch());
   }
}

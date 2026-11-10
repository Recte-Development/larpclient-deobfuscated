package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.bn;
import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1657.class)
public abstract class qf1adca6e407c639f {
   @Inject(method = "method_75124", at = @At("RETURN"))
   private void e(CallbackInfo var1) {
      bn.b((class_1657)this);
   }
}

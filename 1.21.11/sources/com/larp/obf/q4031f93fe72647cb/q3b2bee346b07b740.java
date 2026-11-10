package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.NoJumpDelayModule;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1309.class)
public class q3b2bee346b07b740 {
   @Shadow
   private int field_6228;

   @Inject(method = "method_6007()V", at = @At("HEAD"))
   private void p(CallbackInfo var1) {
      this.field_6228 = Math.min(this.field_6228, NoJumpDelayModule.o((class_1309)this, this.field_6228));
   }

   @ModifyConstant(method = "method_6007()V", constant = @Constant(intValue = 10), require = 1, allow = 1)
   private int w(int var1) {
      return NoJumpDelayModule.o((class_1309)this, var1);
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.FastUseModule;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(class_310.class)
public class qa252b8f26f7fd9e5 {
   @ModifyConstant(method = "method_1583()V", constant = @Constant(intValue = 4), require = 1, allow = 1)
   private int N(int var1) {
      return FastUseModule.q(var1);
   }
}

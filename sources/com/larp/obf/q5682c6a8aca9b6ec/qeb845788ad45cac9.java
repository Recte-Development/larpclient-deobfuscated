package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.FastUseModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class qeb845788ad45cac9 {
   @ModifyConstant(method = "startUseItem()V", constant = @Constant(intValue = 4), require = 1, allow = 1)
   private int t(int var1) {
      return FastUseModule.I(var1);
   }
}

package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.FastUseModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class q7946f9af1eb6ec69 {
   @ModifyConstant(method = "startUseItem()V", constant = @Constant(intValue = 4), require = 1, allow = 1)
   private int y(int var1) {
      return FastUseModule.F(var1);
   }
}

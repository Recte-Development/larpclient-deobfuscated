package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.FastUseModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class qe638a5fb718c63bf {
   @ModifyConstant(method = "startUseItem()V", constant = @Constant(intValue = 4), require = 1, allow = 1)
   private int D(int var1) {
      return FastUseModule.w(var1);
   }
}

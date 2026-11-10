package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.FastUseModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class qcc026974ca1bae4b {
   @ModifyConstant(method = "startUseItem()V", constant = @Constant(intValue = 4), require = 1, allow = 1)
   private int z(int var1) {
      return FastUseModule.X(var1);
   }
}

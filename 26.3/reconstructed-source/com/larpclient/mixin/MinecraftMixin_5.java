package com.larpclient.mixin;

import com.larpclient.module.player.FastUseModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class MinecraftMixin_5 {
   @ModifyConstant(method = "startUseItem()V", constant = @Constant(intValue = 4), require = 1, allow = 1)
   private int z(int var1) {
      return FastUseModule.getUseDelay(var1);
   }
}

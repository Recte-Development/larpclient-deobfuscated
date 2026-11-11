package com.larpclient.mixin;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface KeyMappingMixin {
   @Accessor("clickCount")
   int larpClickCount();

   @Accessor("clickCount")
   void larpSetClickCount(int var1);
}

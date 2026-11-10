package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface qa7574265f54f83cc {
   @Accessor("clickCount")
   int larpClickCount();

   @Accessor("clickCount")
   void larpSetClickCount(int var1);
}

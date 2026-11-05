package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface q19f6d37a64cd71d5 {
   @Accessor("clickCount")
   int larpClickCount();
}

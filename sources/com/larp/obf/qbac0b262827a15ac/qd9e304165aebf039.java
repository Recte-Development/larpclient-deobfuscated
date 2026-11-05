package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface qd9e304165aebf039 {
   @Accessor("clickCount")
   int larpClickCount();
}

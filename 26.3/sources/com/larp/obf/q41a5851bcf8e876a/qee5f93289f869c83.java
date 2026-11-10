package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyMapping.class)
public interface qee5f93289f869c83 {
   @Accessor("clickCount")
   int larpClickCount();

   @Accessor("clickCount")
   void larpSetClickCount(int var1);
}

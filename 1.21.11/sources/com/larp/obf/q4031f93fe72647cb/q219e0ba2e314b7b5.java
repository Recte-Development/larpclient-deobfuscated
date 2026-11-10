package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_304;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_304.class)
public interface q219e0ba2e314b7b5 {
   @Accessor("field_1661")
   int larpClickCount();

   @Accessor("field_1661")
   void larpSetClickCount(int var1);
}

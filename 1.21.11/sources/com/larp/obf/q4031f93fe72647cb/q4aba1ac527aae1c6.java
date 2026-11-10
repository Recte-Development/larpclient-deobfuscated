package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_1309.class)
public interface q4aba1ac527aae1c6 {
   @Accessor("field_6273")
   int larpAttackStrengthTicker();

   @Accessor("field_6273")
   void larpAttackStrengthTicker(int var1);

   @Accessor("field_6228")
   int larpNoJumpDelay();
}

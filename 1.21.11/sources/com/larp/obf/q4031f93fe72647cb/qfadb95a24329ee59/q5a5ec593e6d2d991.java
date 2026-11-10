package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import net.minecraft.class_2561;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_437.class)
public interface q5a5ec593e6d2d991 {
   @Accessor("field_22785")
   class_2561 LarpRig$getTitle();

   @Mutable
   @Accessor("field_22785")
   void LarpRig$setTitle(class_2561 var1);
}

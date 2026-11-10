package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_465.class)
public interface q58db36eeaf095e51 {
   @Accessor("field_2776")
   int larpLeftPos();

   @Accessor("field_2800")
   int larpTopPos();

   @Accessor("field_2792")
   int larpImageWidth();

   @Accessor("field_2779")
   int larpImageHeight();
}

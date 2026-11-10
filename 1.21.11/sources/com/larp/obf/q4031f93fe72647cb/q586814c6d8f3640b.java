package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_10185;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_746.class)
public interface q586814c6d8f3640b {
   @Accessor("field_54156")
   class_10185 larpLastSentInput();

   @Accessor("field_3919")
   boolean larpWasSprinting();

   @Accessor("field_3941")
   float larpYRotLast();

   @Accessor("field_3925")
   float larpXRotLast();

   @Accessor("field_3926")
   double larpXLast();

   @Accessor("field_3940")
   double larpYLast();

   @Accessor("field_3924")
   double larpZLast();
}

package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_2338;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_636.class)
public interface q226fb0b431f37244 {
   @Invoker("method_2911")
   void larpSyncCarriedItem();

   @Accessor("field_3716")
   int larpDestroyDelay();

   @Accessor("field_3716")
   void larpDestroyDelay(int var1);

   @Accessor("field_3715")
   float larpDestroyProgress();

   @Accessor("field_3717")
   boolean larpIsDestroying();

   @Accessor("field_3714")
   class_2338 larpDestroyBlockPos();

   @Accessor("field_3714")
   void larpDestroyBlockPos(class_2338 var1);

   @Accessor("field_3721")
   int larpCarriedIndex();
}

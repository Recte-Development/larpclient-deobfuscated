package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_310.class)
public interface qf65e76ba6afee893 {
   @Accessor("field_1771")
   int larpAttackDelay();

   @Accessor("field_1771")
   void larpAttackDelay(int var1);

   @Accessor("field_1752")
   int larpUseDelay();

   @Accessor("field_1752")
   void larpUseDelay(int var1);
}

package com.larp.obf.qa300483f54d804a3;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface q56b215c3695b1780 {
   @Accessor("attackStrengthTicker")
   int larpAttackStrengthTicker();

   @Accessor("attackStrengthTicker")
   void larpAttackStrengthTicker(int var1);

   @Accessor("noJumpDelay")
   int larpNoJumpDelay();
}

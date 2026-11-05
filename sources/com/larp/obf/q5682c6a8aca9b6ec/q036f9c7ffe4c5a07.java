package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface q036f9c7ffe4c5a07 {
   @Accessor("attackStrengthTicker")
   int larpAttackStrengthTicker();

   @Accessor("attackStrengthTicker")
   void larpAttackStrengthTicker(int var1);
}

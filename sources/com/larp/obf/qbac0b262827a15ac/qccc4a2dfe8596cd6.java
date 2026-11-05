package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface qccc4a2dfe8596cd6 {
   @Accessor("attackStrengthTicker")
   int larpAttackStrengthTicker();

   @Accessor("attackStrengthTicker")
   void larpAttackStrengthTicker(int var1);
}

package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface q0c224d8eb12ccb1e {
   @Accessor("attackStrengthTicker")
   int larpAttackStrengthTicker();

   @Accessor("attackStrengthTicker")
   void larpAttackStrengthTicker(int var1);

   @Accessor("noJumpDelay")
   int larpNoJumpDelay();
}

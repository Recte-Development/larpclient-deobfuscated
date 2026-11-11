package com.larpclient.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface LivingEntityMixin {
   @Accessor("attackStrengthTicker")
   int larpAttackStrengthTicker();

   @Accessor("attackStrengthTicker")
   void larpAttackStrengthTicker(int var1);

   @Accessor("noJumpDelay")
   int larpNoJumpDelay();
}

package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.NoSlowdownModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class q4c5b63af49896ca0 {
   @Inject(method = "makeStuckInBlock", at = @At("HEAD"), cancellable = true)
   private void E(BlockState var1, Vec3 var2, CallbackInfo var3) {
      NoSlowdownModule var4 = NoSlowdownModule.Q();
      if (this == Minecraft.getInstance().player && var4 != null && var4.Y() && var1.is(Blocks.COBWEB)) {
         ((Entity)this).resetFallDistance();
         var3.cancel();
      }
   }

   @Inject(method = "getBlockSpeedFactor", at = @At("RETURN"), cancellable = true)
   private void F(CallbackInfoReturnable<Float> var1) {
      NoSlowdownModule var2 = NoSlowdownModule.Q();
      Entity var3 = (Entity)this;
      if (var3 == Minecraft.getInstance().player && var2 != null && var2.H()) {
         BlockState var4 = var3.level().getBlockState(var3.blockPosition());
         BlockState var5 = var3.level().getBlockState(var3.getBlockPosBelowThatAffectsMyMovement());
         if (var4.is(Blocks.SOUL_SAND) || var4.getBlock().getSpeedFactor() == 1.0F && var5.is(Blocks.SOUL_SAND)) {
            var1.setReturnValue(1.0F);
         }
      }
   }
}

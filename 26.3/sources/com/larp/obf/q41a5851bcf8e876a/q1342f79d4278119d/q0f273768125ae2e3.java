package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.jn;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class q0f273768125ae2e3 {
   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void l(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      if (this instanceof ClientLevel var3) {
         SpawnerBlockEntity var5 = jn.g().r(var3, var1);
         if (var5 != null) {
            var2.setReturnValue(var5);
         }
      }
   }
}

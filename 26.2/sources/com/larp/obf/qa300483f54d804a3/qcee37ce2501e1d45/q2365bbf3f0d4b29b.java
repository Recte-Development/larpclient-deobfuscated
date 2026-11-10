package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
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
public abstract class q2365bbf3f0d4b29b {
   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void a(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      if (this instanceof ClientLevel var3) {
         SpawnerBlockEntity var5 = b8.k().n(var3, var1);
         if (var5 != null) {
            var2.setReturnValue(var5);
         }
      }
   }
}

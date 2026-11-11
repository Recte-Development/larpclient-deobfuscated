package com.larpclient.mixin;

import com.larpclient.util.world.FakeSpawnerController;
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
public abstract class LevelMixin2 {
   @Inject(method = "getBlockEntity", at = @At("HEAD"), cancellable = true)
   private void l(BlockPos var1, CallbackInfoReturnable<BlockEntity> var2) {
      if (this instanceof ClientLevel var3) {
         SpawnerBlockEntity var5 = FakeSpawnerController.getInstance().savePiles(var3, var1);
         if (var5 != null) {
            var2.setReturnValue(var5);
         }
      }
   }
}

package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q6b10a8f7e862a6d5;

import com.larpclient.hr;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class qf36c549ebdaf1aaf {
   @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
   private void a(BlockPos var1, BlockState var2, BlockState var3, int var4, CallbackInfo var5) {
      if (var2 != var3) {
         hr.E(var1, var2, var3);
      }
   }
}

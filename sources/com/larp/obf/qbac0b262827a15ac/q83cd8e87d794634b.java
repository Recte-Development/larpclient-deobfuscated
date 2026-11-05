package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.HolesESPModule;
import com.larpclient.module.a0;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class q83cd8e87d794634b {
   @Inject(method = "setServerVerifiedBlockState", at = @At("HEAD"))
   private void b(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      a0.J((ClientLevel)this, var1, var2);
   }

   @Inject(method = "setServerVerifiedBlockState", at = @At("TAIL"))
   private void H(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      HolesESPModule.Q((ClientLevel)this, var1);
   }
}

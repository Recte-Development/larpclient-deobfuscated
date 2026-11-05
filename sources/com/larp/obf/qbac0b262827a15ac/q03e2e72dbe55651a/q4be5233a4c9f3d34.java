package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.class)
public interface q4be5233a4c9f3d34 {
   @Accessor("blockStatePredictionHandler")
   BlockStatePredictionHandler LarpRig$getPendingUpdateManager();
}

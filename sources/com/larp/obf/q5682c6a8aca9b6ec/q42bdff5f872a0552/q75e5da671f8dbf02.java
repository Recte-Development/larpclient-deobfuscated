package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.class)
public interface q75e5da671f8dbf02 {
   @Accessor("blockStatePredictionHandler")
   BlockStatePredictionHandler LarpRig$getPendingUpdateManager();
}

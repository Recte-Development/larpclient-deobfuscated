package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.class)
public interface qaee82196bfd62460 {
   @Accessor("blockStatePredictionHandler")
   BlockStatePredictionHandler LarpRig$getPendingUpdateManager();
}

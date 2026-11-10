package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientLevel.class)
public interface qa6b3f1c381a58196 {
   @Accessor("blockStatePredictionHandler")
   BlockStatePredictionHandler LarpRig$getPendingUpdateManager();
}

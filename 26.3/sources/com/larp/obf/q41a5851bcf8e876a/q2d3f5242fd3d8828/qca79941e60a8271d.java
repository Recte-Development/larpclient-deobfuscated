package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface qca79941e60a8271d {
   @Invoker("ensureHasSentCarriedItem")
   void larpRig$syncSelectedSlot();

   @Invoker("startPrediction")
   void larpRig$sendSequencedPacket(ClientLevel var1, PredictiveAction var2);
}

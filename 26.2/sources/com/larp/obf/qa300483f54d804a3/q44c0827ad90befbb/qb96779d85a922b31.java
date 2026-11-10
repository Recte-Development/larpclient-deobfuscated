package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface qb96779d85a922b31 {
   @Invoker("ensureHasSentCarriedItem")
   void larpRig$syncSelectedSlot();

   @Invoker("startPrediction")
   void larpRig$sendSequencedPacket(ClientLevel var1, PredictiveAction var2);
}

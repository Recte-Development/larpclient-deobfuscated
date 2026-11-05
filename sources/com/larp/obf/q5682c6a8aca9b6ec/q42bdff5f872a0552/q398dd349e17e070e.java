package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface q398dd349e17e070e {
   @Invoker("ensureHasSentCarriedItem")
   void larpRig$syncSelectedSlot();

   @Invoker("startPrediction")
   void larpRig$sendSequencedPacket(ClientLevel var1, PredictiveAction var2);
}

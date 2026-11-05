package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface qf9e8c8a41361a516 {
   @Invoker("ensureHasSentCarriedItem")
   void larpRig$syncSelectedSlot();

   @Invoker("startPrediction")
   void larpRig$sendSequencedPacket(ClientLevel var1, PredictiveAction var2);
}

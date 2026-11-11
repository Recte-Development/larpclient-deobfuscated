package com.larpclient.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.multiplayer.prediction.PredictiveAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface MultiPlayerGameModeMixin_42 {
   @Invoker("ensureHasSentCarriedItem")
   void larpRig$syncSelectedSlot();

   @Invoker("startPrediction")
   void larpRig$sendSequencedPacket(ClientLevel var1, PredictiveAction var2);
}

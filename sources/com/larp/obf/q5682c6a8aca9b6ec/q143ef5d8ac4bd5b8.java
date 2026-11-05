package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface q143ef5d8ac4bd5b8 {
   @Invoker("ensureHasSentCarriedItem")
   void larpSyncCarriedItem();

   @Accessor("destroyDelay")
   int larpDestroyDelay();

   @Accessor("destroyDelay")
   void larpDestroyDelay(int var1);
}

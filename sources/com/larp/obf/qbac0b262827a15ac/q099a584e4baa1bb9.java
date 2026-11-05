package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface q099a584e4baa1bb9 {
   @Invoker("ensureHasSentCarriedItem")
   void larpSyncCarriedItem();

   @Accessor("destroyDelay")
   int larpDestroyDelay();

   @Accessor("destroyDelay")
   void larpDestroyDelay(int var1);
}

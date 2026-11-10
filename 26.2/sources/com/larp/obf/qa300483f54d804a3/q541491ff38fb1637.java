package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiPlayerGameMode.class)
public interface q541491ff38fb1637 {
   @Invoker("ensureHasSentCarriedItem")
   void larpSyncCarriedItem();

   @Accessor("destroyDelay")
   int larpDestroyDelay();

   @Accessor("destroyDelay")
   void larpDestroyDelay(int var1);

   @Accessor("destroyProgress")
   float larpDestroyProgress();

   @Accessor("isDestroying")
   boolean larpIsDestroying();

   @Accessor("destroyBlockPos")
   BlockPos larpDestroyBlockPos();

   @Accessor("destroyBlockPos")
   void larpDestroyBlockPos(BlockPos var1);

   @Accessor("carriedIndex")
   int larpCarriedIndex();
}

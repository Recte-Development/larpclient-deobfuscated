package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStatePredictionHandler.class)
public interface q3ddfad491c6be35c {
   @Accessor("serverVerifiedStates")
   Long2ObjectOpenHashMap<?> larp$unconfirmed();
}

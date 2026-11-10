package com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockStatePredictionHandler.class)
public interface q0f2922fe81ac8eae {
   @Accessor("serverVerifiedStates")
   Long2ObjectOpenHashMap<?> larp$unconfirmed();
}

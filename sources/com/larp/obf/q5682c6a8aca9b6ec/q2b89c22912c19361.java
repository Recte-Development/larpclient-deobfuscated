package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface q2b89c22912c19361 {
   @Accessor("serverChunkRadius")
   int larpServerChunkRadius();
}

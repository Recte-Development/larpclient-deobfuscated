package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface q11756370fb0d6f1d {
   @Accessor("serverChunkRadius")
   int larpServerChunkRadius();
}

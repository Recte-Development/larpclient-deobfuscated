package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface q90878c7ca95059f9 {
   @Accessor("serverChunkRadius")
   int larpServerChunkRadius();
}

package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface q1ff3fe7b68a0197e {
   @Accessor("serverChunkRadius")
   int larpServerChunkRadius();
}

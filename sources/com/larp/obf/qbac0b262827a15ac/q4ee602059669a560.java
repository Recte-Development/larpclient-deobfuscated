package com.larp.obf.qbac0b262827a15ac;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public interface q4ee602059669a560 {
   @Accessor("channel")
   Channel larpProxyChannel();
}

package com.larp.obf.q41a5851bcf8e876a;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public interface q116cf2707f75cf3c {
   @Accessor("channel")
   Channel larpProxyChannel();
}

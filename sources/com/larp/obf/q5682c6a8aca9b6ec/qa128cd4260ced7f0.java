package com.larp.obf.q5682c6a8aca9b6ec;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public interface qa128cd4260ced7f0 {
   @Accessor("channel")
   Channel larpProxyChannel();
}

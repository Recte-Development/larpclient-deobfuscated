package com.larp.obf.qa300483f54d804a3;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Connection.class)
public interface q95783080261f9ad6 {
   @Accessor("channel")
   Channel larpProxyChannel();
}

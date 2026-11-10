package com.larp.obf.q4031f93fe72647cb;

import io.netty.channel.Channel;
import net.minecraft.class_2535;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_2535.class)
public interface q035afe6f3da5ac7a {
   @Accessor("field_11651")
   Channel larpProxyChannel();
}

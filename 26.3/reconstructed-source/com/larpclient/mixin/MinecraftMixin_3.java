package com.larpclient.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface MinecraftMixin_3 {
   @Invoker("pick")
   void larpPick(float var1);

   @Accessor("missTime")
   int larpAttackDelay();

   @Accessor("missTime")
   void larpAttackDelay(int var1);

   @Accessor("rightClickDelay")
   int larpUseDelay();

   @Accessor("rightClickDelay")
   void larpUseDelay(int var1);
}

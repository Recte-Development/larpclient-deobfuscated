package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface qb131e08b200c8a57 {
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

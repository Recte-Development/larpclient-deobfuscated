package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface q7ae1dbb86ec789d8 {
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

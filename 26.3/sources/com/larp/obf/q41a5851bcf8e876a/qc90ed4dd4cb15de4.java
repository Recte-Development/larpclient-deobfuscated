package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Minecraft.class)
public interface qc90ed4dd4cb15de4 {
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

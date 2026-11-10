package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface q1881b7c262e73011 {
   @Accessor("lastSentInput")
   Input larpLastSentInput();

   @Accessor("wasSprinting")
   boolean larpWasSprinting();

   @Accessor("yRotLast")
   float larpYRotLast();

   @Accessor("xRotLast")
   float larpXRotLast();

   @Accessor("xLast")
   double larpXLast();

   @Accessor("yLast")
   double larpYLast();

   @Accessor("zLast")
   double larpZLast();
}

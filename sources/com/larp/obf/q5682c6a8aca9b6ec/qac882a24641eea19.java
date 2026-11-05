package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LocalPlayer.class)
public interface qac882a24641eea19 {
   @Accessor("lastSentInput")
   Input larpLastSentInput();

   @Accessor("wasSprinting")
   boolean larpWasSprinting();

   @Accessor("yRotLast")
   float larpYRotLast();

   @Accessor("xRotLast")
   float larpXRotLast();
}

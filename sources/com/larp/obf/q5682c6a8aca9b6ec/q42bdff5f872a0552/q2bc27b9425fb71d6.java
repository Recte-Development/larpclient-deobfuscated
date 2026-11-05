package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface q2bc27b9425fb71d6 {
   @Accessor("title")
   Component LarpRig$getTitle();

   @Mutable
   @Accessor("title")
   void LarpRig$setTitle(Component var1);
}

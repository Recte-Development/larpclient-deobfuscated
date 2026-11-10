package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface q5550a417b3ecd119 {
   @Accessor("title")
   Component LarpRig$getTitle();

   @Mutable
   @Accessor("title")
   void LarpRig$setTitle(Component var1);
}

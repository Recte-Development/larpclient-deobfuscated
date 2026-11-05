package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface qf7f90f72b0d68155 {
   @Accessor("title")
   Component LarpRig$getTitle();

   @Mutable
   @Accessor("title")
   void LarpRig$setTitle(Component var1);
}

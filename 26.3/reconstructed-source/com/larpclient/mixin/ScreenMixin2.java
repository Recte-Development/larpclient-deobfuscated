package com.larpclient.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface ScreenMixin2 {
   @Accessor("title")
   Component LarpRig$getTitle();

   @Mutable
   @Accessor("title")
   void LarpRig$setTitle(Component var1);
}

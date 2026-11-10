package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Screen.class)
public interface qbc9c177a3c361622 {
   @Accessor("title")
   Component LarpRig$getTitle();

   @Mutable
   @Accessor("title")
   void LarpRig$setTitle(Component var1);
}

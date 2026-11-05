package com.larp.obf.q5682c6a8aca9b6ec;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface q6be60df302bad6e2 {
   @Accessor("hoveredSlot")
   Slot larpHoveredSlot();
}

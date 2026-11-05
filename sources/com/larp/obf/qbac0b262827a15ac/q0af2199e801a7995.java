package com.larp.obf.qbac0b262827a15ac;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface q0af2199e801a7995 {
   @Accessor("hoveredSlot")
   Slot larpHoveredSlot();
}

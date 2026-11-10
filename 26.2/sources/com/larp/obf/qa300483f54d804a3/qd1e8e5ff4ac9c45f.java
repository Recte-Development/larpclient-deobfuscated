package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface qd1e8e5ff4ac9c45f {
   @Accessor("hoveredSlot")
   Slot larpHoveredSlot();
}

package com.larp.obf.q41a5851bcf8e876a;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface q2013b2ade2f9112e {
   @Accessor("hoveredSlot")
   Slot larpHoveredSlot();
}

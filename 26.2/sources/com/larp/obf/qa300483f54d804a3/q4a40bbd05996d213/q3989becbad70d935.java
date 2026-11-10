package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlockItem.class)
public interface q3989becbad70d935 {
   @Invoker("getPlacementState")
   BlockState larp$placementState(BlockPlaceContext var1);
}

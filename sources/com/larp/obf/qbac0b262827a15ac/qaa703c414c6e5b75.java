package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.XRayModule;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.model.AbstractBlockRenderContext", remap = false)
public abstract class qaa703c414c6e5b75 {
   @WrapOperation(
      method = "shouldDrawSide",
      require = 0,
      remap = false,
      at = @At(
         value = "INVOKE",
         ordinal = 0,
         target = "Lnet/minecraft/world/level/block/state/BlockState;getFaceOcclusionShape(Lnet/minecraft/core/Direction;)Lnet/minecraft/world/phys/shapes/VoxelShape;"
      )
   )
   private VoxelShape t(BlockState var1, Direction var2, Operation<VoxelShape> var3) {
      return XRayModule.m() && !XRayModule.z(var1.getBlock()) ? Shapes.empty() : (VoxelShape)var3.call(new Object[]{var1, var2});
   }
}

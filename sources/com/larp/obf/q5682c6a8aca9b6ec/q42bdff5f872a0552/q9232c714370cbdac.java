package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface q9232c714370cbdac {
   @Accessor("position")
   Vec3 LarpRig$getPos();
}

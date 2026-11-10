package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface qa01e4d50212b31f4 {
   @Accessor("position")
   Vec3 LarpRig$getPos();
}

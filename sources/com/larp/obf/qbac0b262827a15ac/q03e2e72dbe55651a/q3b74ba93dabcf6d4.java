package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface q3b74ba93dabcf6d4 {
   @Accessor("position")
   Vec3 LarpRig$getPos();
}

package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Camera.class)
public interface q4cbf606370a4de13 {
   @Accessor("position")
   Vec3 LarpRig$getPos();
}

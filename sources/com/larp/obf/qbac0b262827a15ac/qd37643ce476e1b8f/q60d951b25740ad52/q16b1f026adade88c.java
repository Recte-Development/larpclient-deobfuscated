package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q60d951b25740ad52;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelPart.class)
public interface q16b1f026adade88c {
   @Accessor("children")
   Map<String, ModelPart> larpWalkChildren();

   @Accessor("cubes")
   List<Cube> larpWalkCubes();
}

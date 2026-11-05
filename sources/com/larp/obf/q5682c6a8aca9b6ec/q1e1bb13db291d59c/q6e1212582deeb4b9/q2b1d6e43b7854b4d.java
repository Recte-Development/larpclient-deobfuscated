package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelPart.class)
public interface q2b1d6e43b7854b4d {
   @Accessor("children")
   Map<String, ModelPart> larpWalkChildren();

   @Accessor("cubes")
   List<Cube> larpWalkCubes();
}

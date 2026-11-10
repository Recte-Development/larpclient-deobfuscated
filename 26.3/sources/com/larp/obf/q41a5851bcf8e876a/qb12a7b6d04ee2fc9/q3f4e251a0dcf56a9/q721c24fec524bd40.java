package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q3f4e251a0dcf56a9;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelPart.class)
public interface q721c24fec524bd40 {
   @Accessor("children")
   Map<String, ModelPart> larpWalkChildren();

   @Accessor("cubes")
   List<Cube> larpWalkCubes();
}

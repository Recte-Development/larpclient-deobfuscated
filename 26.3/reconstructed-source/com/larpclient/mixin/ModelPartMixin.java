package com.larpclient.mixin;

import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.ModelPart.Cube;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelPart.class)
public interface ModelPartMixin {
   @Accessor("children")
   Map<String, ModelPart> larpWalkChildren();

   @Accessor("cubes")
   List<Cube> larpWalkCubes();
}

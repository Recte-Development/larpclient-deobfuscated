package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.qad41357d687ba689;

import java.util.List;
import java.util.Map;
import net.minecraft.class_630;
import net.minecraft.class_630.class_628;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(class_630.class)
public interface q2ee3efb11fe52f83 {
   @Accessor("field_3661")
   Map<String, class_630> larpWalkChildren();

   @Accessor("field_3663")
   List<class_628> larpWalkCubes();
}

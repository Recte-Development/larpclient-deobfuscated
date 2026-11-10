package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_3887;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_922.class)
public interface q03091a017f3d0273 {
   @Invoker("method_4046")
   boolean larp$addLayer(class_3887 var1);
}

package com.larp.obf.q4031f93fe72647cb;

import net.minecraft.class_2350;
import net.minecraft.class_846.class_851;
import net.minecraft.class_8679.class_762;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_762.class)
public interface q50c98a508c767a5d {
   @Invoker("<init>")
   static class_762 larpCreateNode(class_851 var0, class_2350 var1, int var2) {
      throw new AssertionError("Freecam graph constructor invoker was not applied");
   }
}

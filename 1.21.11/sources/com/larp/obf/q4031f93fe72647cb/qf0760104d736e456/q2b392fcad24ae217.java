package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.cz;
import net.minecraft.class_10428;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_10428.class)
public abstract class q2b392fcad24ae217 {
   @ModifyVariable(method = "method_65581", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private class_1799 I(class_1799 var1) {
      return cz.T().i(var1);
   }
}

package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.er;
import net.minecraft.class_327;
import net.minecraft.class_5348;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(class_327.class)
public class q20b3f4823a52d356 {
   @ModifyVariable(
      method = {"method_27525(Lnet/minecraft/class_5348;)I", "method_1728(Lnet/minecraft/class_5348;I)Ljava/util/List;"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private class_5348 V(class_5348 var1) {
      return er.W(var1);
   }
}

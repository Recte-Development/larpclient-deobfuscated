package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.er;
import net.minecraft.class_303;
import net.minecraft.class_5348;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(class_303.class)
public class qcbcd54f5a2b686f4 {
   @ModifyArg(
      method = "method_75757",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/class_341;method_1850(Lnet/minecraft/class_5348;ILnet/minecraft/class_327;)Ljava/util/List;"),
      index = 0
   )
   private class_5348 E(class_5348 var1) {
      return er.W(var1);
   }
}

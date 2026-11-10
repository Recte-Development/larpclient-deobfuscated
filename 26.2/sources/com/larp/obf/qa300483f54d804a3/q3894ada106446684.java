package com.larp.obf.qa300483f54d804a3;

import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Options.class)
public abstract class q3894ada106446684 {
   @ModifyArg(
      method = "<init>",
      slice = @Slice(
         from = @At(value = "CONSTANT", args = "stringValue=options.guiScale"),
         to = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;guiScale:Lnet/minecraft/client/OptionInstance;")
      ),
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/OptionInstance;<init>(Ljava/lang/String;Lnet/minecraft/client/OptionInstance$TooltipSupplier;Lnet/minecraft/client/OptionInstance$CaptionBasedToString;Lnet/minecraft/client/OptionInstance$ValueSet;Ljava/lang/Object;Lnet/minecraft/client/OptionInstance$ValueUpdateListener;)V"
      ),
      index = 4,
      require = 1,
      allow = 1
   )
   private Object o(Object var1) {
      return 2;
   }
}

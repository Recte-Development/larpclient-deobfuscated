package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.ih;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net/caffeinemc/mods/sodium/client/gl/shader/ShaderLoader", remap = false)
public abstract class q0a50eaab0fa90006 {
   @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true, require = 0, remap = false)
   private static void l(class_2960 var0, CallbackInfoReturnable<String> var1) {
      String var2 = (String)var1.getReturnValue();
      String var3 = ih.K(var0, var2);
      if (var3 != var2) {
         var1.setReturnValue(var3);
      }
   }
}

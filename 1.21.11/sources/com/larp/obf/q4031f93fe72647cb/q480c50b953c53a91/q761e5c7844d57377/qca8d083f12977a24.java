package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.ih;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net/caffeinemc/mods/sodium/client/render/chunk/ShaderChunkRenderer", remap = false)
public abstract class qca8d083f12977a24 {
   @Inject(method = "compileProgram", at = @At("HEAD"), cancellable = true, require = 0, remap = false)
   private void A(@Coerce Object var1, CallbackInfoReturnable<Object> var2) {
      Object var3 = ih.z(this, var1);
      if (var3 != null) {
         var2.setReturnValue(var3);
      }
   }

   @Inject(method = "delete", at = @At("TAIL"), require = 0, remap = false)
   private void P(CallbackInfo var1) {
      ih.d(this);
   }
}

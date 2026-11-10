package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.XRayModule;
import net.minecraft.class_1087;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer", remap = false)
public abstract class qc00af1d83a849574 {
   @Unique
   private boolean k;

   @Inject(method = "renderModel", at = @At("HEAD"), require = 0, remap = false)
   private void q(class_1087 var1, class_2680 var2, class_2338 var3, class_2338 var4, CallbackInfo var5) {
      this.k = XRayModule.m() && XRayModule.r(var2.method_26204());
   }

   @ModifyArg(
      method = "renderModel",
      require = 0,
      remap = false,
      at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;prepareAoInfo(Z)V")
   )
   private boolean i(boolean var1) {
      return var1 && !this.k;
   }

   @ModifyArg(
      method = "processQuad",
      index = 2,
      require = 0,
      remap = false,
      at = @At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;shadeQuad(Lnet/caffeinemc/mods/sodium/client/render/model/MutableQuadViewImpl;Lnet/caffeinemc/mods/sodium/client/model/light/LightMode;ZLnet/caffeinemc/mods/sodium/client/render/model/SodiumShadeMode;)V"
      )
   )
   private boolean W(boolean var1) {
      return var1 || this.k;
   }
}

package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.XRayModule;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer", remap = false)
public abstract class qcfe230fbb4a7968e {
   @Unique
   private boolean l;

   @Inject(method = "renderModel", at = @At("HEAD"), require = 0, remap = false)
   private void F(BlockStateModel var1, BlockState var2, BlockPos var3, BlockPos var4, CallbackInfo var5) {
      this.l = XRayModule.q() && XRayModule.c(var2.getBlock());
   }

   @ModifyArg(
      method = "renderModel",
      require = 0,
      remap = false,
      at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer;prepareAoInfo(Z)V")
   )
   private boolean k(boolean var1) {
      return var1 && !this.l;
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
   private boolean H(boolean var1) {
      return var1 || this.l;
   }
}

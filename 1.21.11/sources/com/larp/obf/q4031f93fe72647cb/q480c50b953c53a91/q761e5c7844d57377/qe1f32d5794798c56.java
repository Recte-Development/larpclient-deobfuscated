package com.larp.obf.q4031f93fe72647cb.q480c50b953c53a91.q761e5c7844d57377;

import com.larpclient.ih;
import com.larpclient.j5;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "net/caffeinemc/mods/sodium/client/render/chunk/compile/pipeline/BlockRenderer", remap = false)
public abstract class qe1f32d5794798c56 {
   @Unique
   private class_1922 b;

   @Inject(method = "prepare", at = @At("HEAD"), require = 0, remap = false)
   private void a(@Coerce Object var1, @Coerce Object var2, @Coerce Object var3, CallbackInfo var4) {
      this.b = var2 instanceof class_1922 var5 ? var5 : null;
   }

   @Inject(method = "release", at = @At("HEAD"), require = 0, remap = false)
   private void J(CallbackInfo var1) {
      this.b = null;
      j5.n();
   }

   @Inject(method = "renderModel", at = @At("HEAD"), require = 0, remap = false)
   private void D(@Coerce Object var1, class_2680 var2, class_2338 var3, class_2338 var4, CallbackInfo var5) {
      j5.b(var2, var4, this.b, var3);
   }

   @Inject(method = "renderModel", at = @At("RETURN"), require = 0, remap = false)
   private void j(@Coerce Object var1, class_2680 var2, class_2338 var3, class_2338 var4, CallbackInfo var5) {
      j5.n();
   }

   @Inject(
      method = "bufferQuad",
      at = @At(
         value = "INVOKE",
         target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/builder/ChunkMeshBufferBuilder;push([Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/ChunkVertexEncoder$Vertex;I)V"
      ),
      require = 0,
      remap = false
   )
   private void q(@Coerce Object var1, float[] var2, @Coerce Object var3, CallbackInfo var4) {
      ih.k(this);
   }
}
